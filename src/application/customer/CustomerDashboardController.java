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
        // Bind columns: SELECT * FROM products -> [0]=id, [1]=name, [2]=description, [3]=price, [4]=stock
        UIHelper.bindColumn(colId, 0);
        UIHelper.bindColumn(colName, 1);
        UIHelper.bindColumn(colDescription, 2);
        UIHelper.bindColumn(colPrice, 3, "$");
        UIHelper.bindColumn(colStock, 4);

        spnQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        tblProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        UserSession session = UserSession.getInstance();
        String name = (session != null) ? session.getUsername() : "Guest";
        lblWelcome.setText("Welcome, " + name + "!");

        loadProducts();
    }

    private void loadProducts() {
        UIHelper.loadTable(tblProducts, ProductDAO.getAllProducts());
    }

//    @FXML
//    public void addToCart() {
//        String[] selected = tblProducts.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            UIHelper.showMessage(lblMessage, "Select a product first.", false);
//            return;
//        }
//
//        int productId = Integer.parseInt(selected[0]);
//        int stock = Integer.parseInt(selected[4]);
//        int quantity = spnQuantity.getValue();
//        int userId = UserSession.getInstance().getUserId();
//
//        if (quantity > stock) {
//            UIHelper.showMessage(lblMessage, "Not enough stock! Only " + stock + " available.", false);
//            return;
//        }
//
//        // If item already in cart, update quantity; otherwise insert new
//        if (CartDAO.itemExists(userId, productId)) {
//            for (String[] cartRow : CartDAO.getAllCartItem(userId)) {
//                // cartRow: [0]=cartId, [1]=productName, [2]=price, [3]=quantity
//                if (cartRow[1].equals(selected[1])) {
//                    int currentQty = Integer.parseInt(cartRow[3]);
//                    CartDAO.updateCartItem(Integer.parseInt(cartRow[0]), currentQty + quantity);
//                    break;
//                }
//            }
//            UIHelper.showMessage(lblMessage, "Updated " + selected[1] + " in cart!", true);
//        } else {
//            CartDAO.insertCartItem(userId, productId, quantity);
//            UIHelper.showMessage(lblMessage, selected[1] + " added to cart!", true);
//        }
//    }
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
 
        // Query live stock from DB — never trust the cached table value,
        // another customer may have bought the item since this screen loaded
        int liveStock = ProductDAO.getLiveStock(productId);
 
        if (liveStock <= 0 || quantity > liveStock) {
            UIHelper.showMessage(lblMessage,
                "The item is currently out of stock, please check at another time.", false);
            return;
        }
 
        if (CartDAO.itemExists(userId, productId)) {
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
        SceneManager.switchScene("/application/login/LoginView.fxml");
    }
}
