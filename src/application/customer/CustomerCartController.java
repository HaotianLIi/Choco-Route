package application.customer;

import application.UIHelper;
import application.db.CartDAO;
import application.login.SceneManager;
import application.login.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CustomerCartController {

    @FXML private TableView<String[]> tblCart;
    @FXML private TableColumn<String[], String> colCartId;
    @FXML private TableColumn<String[], String> colProduct;
    @FXML private TableColumn<String[], String> colPrice;
    @FXML private TableColumn<String[], String> colQuantity;
    @FXML private TableColumn<String[], String> colSubtotal;
    @FXML private Spinner<Integer> spnQuantity;
    @FXML private Label lblTotal;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        // Cart query returns: [0]=cartId, [1]=productName, [2]=price, [3]=quantity
        UIHelper.bindColumn(colCartId, 0);
        UIHelper.bindColumn(colProduct, 1);
        UIHelper.bindColumn(colPrice, 2, "$");
        UIHelper.bindColumn(colQuantity, 3);

        // Subtotal = price * quantity
        colSubtotal.setCellValueFactory(data -> {
            double price = Double.parseDouble(data.getValue()[2]);
            int qty = Integer.parseInt(data.getValue()[3]);
            return new SimpleStringProperty(String.format("$%.2f", price * qty));
        });

        spnQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        tblCart.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadCart();
    }

    private void loadCart() {
        int userId = UserSession.getInstance().getUserId();
        List<String[]> items = CartDAO.getAllCartItem(userId);
        UIHelper.loadTable(tblCart, items);
        updateTotal(items);
    }

    private void updateTotal(List<String[]> items) {
        double total = 0;
        for (String[] row : items) {
            total += Double.parseDouble(row[2]) * Integer.parseInt(row[3]);
        }
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    @FXML
    public void updateQuantity() {
        String[] selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIHelper.showMessage(lblMessage, "Select an item first.", false);
            return;
        }

        int cartId = Integer.parseInt(selected[0]);
        int newQty = spnQuantity.getValue();

        if (CartDAO.updateCartItem(cartId, newQty)) {
            UIHelper.showMessage(lblMessage, "Quantity updated.", true);
            loadCart();
        } else {
            UIHelper.showMessage(lblMessage, "Failed to update quantity.", false);
        }
    }

    @FXML
    public void removeItem() {
        String[] selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIHelper.showMessage(lblMessage, "Select an item first.", false);
            return;
        }

        if (CartDAO.removeCartItem(Integer.parseInt(selected[0]))) {
            UIHelper.showMessage(lblMessage, selected[1] + " removed from cart.", true);
            loadCart();
        } else {
            UIHelper.showMessage(lblMessage, "Failed to remove item.", false);
        }
    }

    @FXML
    public void proceedToCheckout() {
        int userId = UserSession.getInstance().getUserId();
        if (CartDAO.getAllCartItem(userId).isEmpty()) {
            UIHelper.showMessage(lblMessage, "Your cart is empty!", false);
            return;
        }
        SceneManager.switchScene("/application/customer/CustomerCheckout.fxml");
    }

    @FXML
    public void goToDashboard() {
        SceneManager.switchScene("/application/customer/CustomerDashboard.fxml");
    }

    @FXML
    public void logout() {
        SceneManager.switchScene("/application/login/LoginView.fxml");
    }
}
