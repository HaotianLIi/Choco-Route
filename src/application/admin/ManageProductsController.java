package application.admin;

import application.UIHelper;
import application.login.SceneManager;
import application.db.ProductDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManageProductsController {
    @FXML private TableView<String[]> tblProducts;
    @FXML private TableColumn<String[], String> colId;
    @FXML private TableColumn<String[], String> colName;
    @FXML private TableColumn<String[], String> colDescription;
    @FXML private TableColumn<String[], String> colPrice;
    @FXML private TableColumn<String[], String> colStock;
    @FXML private TextField txtName;
    @FXML private TextField txtDesc;
    @FXML private TextField txtPrice;
    @FXML private TextField txtStock;
    @FXML private Spinner<Integer> spnAddStock;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
    	tblProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Bind table columns: index matches SELECT * FROM products
        // [0]=id, [1]=name, [2]=description, [3]=price, [4]=stock
        UIHelper.bindColumn(colId, 0);
        UIHelper.bindColumn(colName, 1);
        UIHelper.bindColumn(colDescription, 2);
        UIHelper.bindColumn(colPrice, 3, "$");
        UIHelper.bindColumn(colStock, 4);

        spnAddStock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
        loadProducts();
    }

    private void loadProducts() {
        UIHelper.loadTable(tblProducts, ProductDAO.getAllProducts());
    }

    @FXML
    public void addProduct(ActionEvent event) {
        String name = txtName.getText().trim();
        String desc = txtDesc.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String stockStr = txtStock.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            UIHelper.showMessage(lblMessage, "Name, Price, and Stock are required.", false);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            if (ProductDAO.createProduct(name, desc, price, stock)) {
                UIHelper.showMessage(lblMessage, "Product '" + name + "' added!", true);
                txtName.clear(); txtDesc.clear(); txtPrice.clear(); txtStock.clear();
                loadProducts();
            } else {
                UIHelper.showMessage(lblMessage, "Failed to add product.", false);
            }
        } catch (NumberFormatException e) {
            UIHelper.showMessage(lblMessage, "Price and Stock must be numbers.", false);
        }
    }

    @FXML
    public void updateStock(ActionEvent event) {
        String[] selected = tblProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIHelper.showMessage(lblMessage, "Select a product first.", false);
            return;
        }

        int productId = Integer.parseInt(selected[0]);
        int addQty = spnAddStock.getValue();

        if (ProductDAO.updateProduct(productId, selected[1], selected[2],
                Double.parseDouble(selected[3]), Integer.parseInt(selected[4]) + addQty)) {
            UIHelper.showMessage(lblMessage, "Added " + addQty + " units to " + selected[1], true);
            loadProducts();
        } else {
            UIHelper.showMessage(lblMessage, "Failed to update stock.", false);
        }
    }

    @FXML
    public void deleteProduct(ActionEvent event) {
        String[] selected = tblProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIHelper.showMessage(lblMessage, "Select a product first.", false);
            return;
        }

        if (ProductDAO.deleteProduct(Integer.parseInt(selected[0]))) {
            UIHelper.showMessage(lblMessage, selected[1] + " deleted.", true);
            loadProducts();
        } else {
            UIHelper.showMessage(lblMessage, "Failed to delete product.", false);
        }
    }

    @FXML
    public void goToDashboard(ActionEvent event) {
        SceneManager.switchScene("/application/admin/AdminDashboard.fxml");
    }

    @FXML
    public void logout() {
        SceneManager.switchScene("/application/login/LoginView.fxml");
    }
}
