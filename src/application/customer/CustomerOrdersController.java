package application.customer;

import application.UIHelper;
import application.db.OrderDAO;
import application.login.SceneManager;
import application.login.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomerOrdersController {

    @FXML private TableView<String[]> tblOrders;
    @FXML private TableColumn<String[], String> colOrderId;
    @FXML private TableColumn<String[], String> colCountry;
    @FXML private TableColumn<String[], String> colShipping;
    @FXML private TableColumn<String[], String> colTotal;
    @FXML private TableColumn<String[], String> colStatus;
    @FXML private TableColumn<String[], String> colDate;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        // OrderDAO.getOrderByUser returns: [0]=id, [1]=country, [2]=shippingFee, [3]=totalPrice, [4]=status, [5]=orderDate
        UIHelper.bindColumn(colOrderId, 0);
        UIHelper.bindColumn(colCountry, 1);
        UIHelper.bindColumn(colShipping, 2, "$");
        UIHelper.bindColumn(colTotal, 3, "$");
        UIHelper.bindColumn(colStatus, 4);
        UIHelper.bindColumn(colDate, 5);

        tblOrders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadOrders();
    }

    private void loadOrders() {
        int userId = UserSession.getInstance().getUserId();
        UIHelper.loadTable(tblOrders, OrderDAO.getOrderByUser(userId));
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
