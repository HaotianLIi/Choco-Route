package application.admin;

import application.login.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminDashboardController {
    @FXML
    private Label lblWelcome;

    @FXML
    public void initialize() {
        // This runs when the page loads
        lblWelcome.setText("Welcome, Admin!");
    }

    @FXML
    public void logout() {
    	SceneManager.switchScene("/application/login/LoginView.fxml");
    }

    @FXML
    public void goToProducts() {
        SceneManager.switchScene("/application/admin/ManageProducts.fxml");
    }

    @FXML
    public void goToOrders() {
        SceneManager.switchScene("/application/admin/ManageOrders.fxml");
    }
}