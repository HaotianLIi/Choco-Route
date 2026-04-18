package application.customer;

import application.UIHelper;
import application.db.*;
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
        // Cart query returns: [0]=cartId, [1]=productName, [2]=price, [3]=quantity
        UIHelper.bindColumn(colProduct, 1);
        UIHelper.bindColumn(colPrice, 2, "$");
        UIHelper.bindColumn(colQuantity, 3);

        colSubtotal.setCellValueFactory(data -> {
            double price = Double.parseDouble(data.getValue()[2]);
            int qty = Integer.parseInt(data.getValue()[3]);
            return new SimpleStringProperty(String.format("$%.2f", price * qty));
        });

        tblSummary.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load cart items
        int userId = UserSession.getInstance().getUserId();
        cartItems = CartDAO.getAllCartItem(userId);
        UIHelper.loadTable(tblSummary, cartItems);

        // Calculate items total
        for (String[] row : cartItems) {
            itemsTotal += Double.parseDouble(row[2]) * Integer.parseInt(row[3]);
        }
        lblItemsTotal.setText(String.format("Items Total: $%.2f", itemsTotal));

        // Load countries
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
        double grandTotal = itemsTotal + shippingFee;

        // 1. Create the order
        int orderId = OrderDAO.createOrder(userId, country, shippingFee, grandTotal);
        if (orderId < 0) {
            UIHelper.showMessage(lblMessage, "Failed to create order.", false);
            return;
        }

        // 2. Add each cart item as an order item and reduce stock
        for (String[] item : cartItems) {
            int productId = Integer.parseInt(item[0]); // cartId, but we need productId
            // Note: cartDAO returns cartId, not productId — we need to get productId differently
            // Actually cart query: c.id, p.name, p.price, c.quantity
            // We need productId for orderItems. Let's use a workaround:
            double price = Double.parseDouble(item[2]);
            int qty = Integer.parseInt(item[3]);
            OrderDAO.addOrderItem(orderId, getProductIdFromCart(Integer.parseInt(item[0])), qty, price);
        }

        // 3. Clear the cart
        CartDAO.removeEntireCart(userId);

        // 4. Navigate to orders
        SceneManager.switchScene("/application/customer/CustomerOrders.fxml");
    }

    private int getProductIdFromCart(int cartId) {
        // Query the cart table to get productId by cartId
        List<String[]> result = DatabaseHelper.query(
            "SELECT productId FROM cart WHERE id = ?", cartId);
        return result.isEmpty() ? -1 : Integer.parseInt(result.get(0)[0]);
    }

    @FXML
    public void goToCart() {
        SceneManager.switchScene("/application/customer/CustomerCart.fxml");
    }

    @FXML
    public void logout() {
        SceneManager.switchScene("/application/login/LoginView.fxml");
    }
}
