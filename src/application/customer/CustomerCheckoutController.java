package application.customer;

import application.UIHelper;
import application.db.CartDAO;
import application.db.OrderDAO;
import application.db.ProductDAO;
import application.db.ShippingDAO;
import application.login.SceneManager;
import application.login.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CustomerCheckoutController {

    @FXML private TableView<String[]> tblSummary;
    @FXML private TableColumn<String[], String> colProduct;
    @FXML private TableColumn<String[], String> colPrice;
    @FXML private TableColumn<String[], String> colQuantity;
    @FXML private TableColumn<String[], String> colSubtotal;
    @FXML private ComboBox<String> cmbCountry;
    @FXML private Label lblItemsTotal;
    @FXML private Label lblShipping;
    @FXML private Label lblGrandTotal;
    @FXML private Label lblMessage;

    private List<String[]> cartItems;
    private double itemsTotal = 0;
    private double shippingFee = 0;

    @FXML
    public void initialize() {
        // Cart row layout: [0]=cartId, [1]=productId, [2]=productName, [3]=price, [4]=quantity
        UIHelper.bindColumn(colProduct, 2);
        UIHelper.bindColumn(colPrice, 3, "$");
        UIHelper.bindColumn(colQuantity, 4);

        colSubtotal.setCellValueFactory(data -> {
            double price = Double.parseDouble(data.getValue()[3]);
            int qty = Integer.parseInt(data.getValue()[4]);
            return new SimpleStringProperty(String.format("$%.2f", price * qty));
        });

        tblSummary.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        int userId = UserSession.getInstance().getUserId();
        cartItems = CartDAO.getAllCartItem(userId);
        UIHelper.loadTable(tblSummary, cartItems);

        for (String[] row : cartItems) {
            itemsTotal += Double.parseDouble(row[3]) * Integer.parseInt(row[4]);
        }
        lblItemsTotal.setText(String.format("Items Total: $%.2f", itemsTotal));

        cmbCountry.setItems(FXCollections.observableArrayList(ShippingDAO.getAllCountries()));
    }

    @FXML
    public void updateShipping() {
        String country = cmbCountry.getValue();
        if (country == null) return;

        shippingFee = ShippingDAO.getShippingFee(country);
        if (shippingFee < 0) {
            UIHelper.showMessage(lblMessage, "Shipping not available for " + country, false);
            shippingFee = 0;
        }

        lblShipping.setText(String.format("Shipping: $%.2f", shippingFee));
        lblGrandTotal.setText(String.format("Grand Total: $%.2f", itemsTotal + shippingFee));
    }

    @FXML
    public void confirmOrder() {
        String country = cmbCountry.getValue();
        if (country == null || country.isEmpty()) {
            UIHelper.showMessage(lblMessage, "Please select a shipping country.", false);
            return;
        }
        if (cartItems.isEmpty()) {
            UIHelper.showMessage(lblMessage, "Your cart is empty!", false);
            return;
        }

        int userId = UserSession.getInstance().getUserId();
        int orderId = OrderDAO.createOrder(userId, country, shippingFee, itemsTotal + shippingFee);
        if (orderId < 0) {
            UIHelper.showMessage(lblMessage, "Failed to create order. Please try again.", false);
            return;
        }

        // Cart row layout: [0]=cartId, [1]=productId, [2]=productName, [3]=price, [4]=quantity
        for (String[] item : cartItems) {
            int productId = Integer.parseInt(item[1]);
            double price  = Double.parseDouble(item[3]);
            int qty       = Integer.parseInt(item[4]);

            OrderDAO.addOrderItem(orderId, productId, qty, price);

            // Atomic stock guard — if another user bought the last unit simultaneously, this fails
            if (!ProductDAO.reduceStock(productId, qty)) {
                OrderDAO.deleteOrder(orderId); // rollback
                UIHelper.showMessage(lblMessage,
                    "Sorry, \"" + item[2] + "\" is out of stock. Your order was not placed.", false);
                return;
            }
        }

        CartDAO.removeEntireCart(userId);
        SceneManager.switchScene("/application/customer/CustomerOrders.fxml");
    }

    @FXML
    public void goToCart() {
        SceneManager.switchScene("/application/customer/CustomerCart.fxml");
    }

    @FXML
    public void logout() {
        UserSession.clear();
        SceneManager.switchScene("/application/login/LoginView.fxml");
    }
}