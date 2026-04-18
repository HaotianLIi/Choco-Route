package application.customer;

import application.UIHelper;
import application.db.CartDAO;
import application.db.ProductDAO;
import application.login.SceneManager;
import application.login.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomerDashboardController {

    @FXML private Label lblWelcome;
    @FXML private Label lblMessage;
    @FXML private TableView<String[]> tblProducts;
    @FXML private TableColumn<String[], String> colId;
    @FXML private TableColumn<String[], String> colName;
    @FXML private TableColumn<String[], String> colDescription;
    @FXML private TableColumn<String[], String> colPrice;
    @FXML private TableColumn<String[], String> colStock;
    @FXML private Spinner<Integer> spnQuantity;

    @FXML
    public void initialize() {
        // Product row layout: [0]=id, [1]=name, [2]=description, [3]=price, [4]=stock
        UIHelper.bindColumn(colId, 0);
        UIHelper.bindColumn(colName, 1);
        UIHelper.bindColumn(colDescription, 2);
        UIHelper.bindColumn(colPrice, 3, "$");
        UIHelper.bindColumn(colStock, 4);

        spnQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        tblProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        UserSession session = UserSession.getInstance();
        lblWelcome.setText("Welcome, " + (session != null ? session.getUsername() : "Guest") + "!");
        loadProducts();
    }

    private void loadProducts() {
        UIHelper.loadTable(tblProducts, ProductDAO.getAllProducts());
    }

    @FXML
    public void addToCart() {
        String[] selected = tblProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIHelper.showMessage(lblMessage, "Select a product first.", false);
            return;
        }

        int productId = Integer.parseInt(selected[0]);
        int quantity  = spnQuantity.getValue();
        int userId    = UserSession.getInstance().getUserId();

        // Query live stock at click time — the table value may be stale
        int liveStock = ProductDAO.getLiveStock(productId);

        if (liveStock <= 0 || quantity > liveStock) {
            UIHelper.showMessage(lblMessage,
                "The item is currently out of stock, please check at another time.", false);
            return;
        }

        if (CartDAO.itemExists(userId, productId)) {
            // Match by productId — not by name
            int cartId     = CartDAO.getCartIdByProduct(userId, productId);
            int currentQty = CartDAO.getCartQuantity(cartId);
            int newQty     = currentQty + quantity;

            if (newQty > liveStock) {
                UIHelper.showMessage(lblMessage,
                    "The item is currently out of stock, please check at another time.", false);
                return;
            }

            CartDAO.updateCartItem(cartId, newQty);
            UIHelper.showMessage(lblMessage, "Updated " + selected[1] + " in cart!", true);
        } else {
            CartDAO.insertCartItem(userId, productId, quantity);
            UIHelper.showMessage(lblMessage, selected[1] + " added to cart!", true);
        }
    }

    @FXML
    public void goToCart() {
        SceneManager.switchScene("/application/customer/CustomerCart.fxml");
    }

    @FXML
    public void goToOrders() {
        SceneManager.switchScene("/application/customer/CustomerOrders.fxml");
    }

    @FXML
    public void logout() {
        UserSession.clear();
        SceneManager.switchScene("/application/login/LoginView.fxml");
    }
}