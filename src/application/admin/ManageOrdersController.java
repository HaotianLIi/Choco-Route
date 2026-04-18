package application.admin;

import application.UIHelper;
import application.login.SceneManager;
import application.db.OrderDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManageOrdersController {
    @FXML private TableView<String[]> tblOrders;
    @FXML private TableColumn<String[], String> colOrderId;
    @FXML private TableColumn<String[], String> colUsername;
    @FXML private TableColumn<String[], String> colCountry;
    @FXML private TableColumn<String[], String> colShipping;
    @FXML private TableColumn<String[], String> colTotal;
    @FXML private TableColumn<String[], String> colStatus;
    @FXML private TableColumn<String[], String> colDate;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
    	tblOrders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Bind columns: matches getAllOrderByAdmin() query
        // [0]=id, [1]=username, [2]=country, [3]=shippingFee, [4]=totalPrice, [5]=status, [6]=orderDate
        UIHelper.bindColumn(colOrderId, 0);
        UIHelper.bindColumn(colUsername, 1);
        UIHelper.bindColumn(colCountry, 2);
        UIHelper.bindColumn(colShipping, 3, "$");
        UIHelper.bindColumn(colTotal, 4, "$");
        UIHelper.bindColumn(colStatus, 5);
        UIHelper.bindColumn(colDate, 6);

        cmbStatus.getItems().addAll("Pending", "Processing", "Shipped", "Delivered", "Cancelled");
        cmbStatus.setValue("Processing");

        loadOrders();
    }

    private void loadOrders() {
        UIHelper.loadTable(tblOrders, OrderDAO.getAllOrderByAdmin());
    }

    @FXML
    public void updateStatus(ActionEvent event) {
        String[] selected = tblOrders.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIHelper.showMessage(lblMessage, "Select an order first.", false);
            return;
        }

        int orderId = Integer.parseInt(selected[0]);
        String newStatus = cmbStatus.getValue();

        if (OrderDAO.updateOrderStatusByAdmin(orderId, newStatus)) {
            UIHelper.showMessage(lblMessage, "Order #" + orderId + " updated to " + newStatus, true);
            loadOrders();
        } else {
            UIHelper.showMessage(lblMessage, "Failed to update order.", false);
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
