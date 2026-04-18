package application.login;

import application.db.UserDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private Button handleLogin;
	@FXML private Label lblError;
	
	public void handleLogin(ActionEvent event) {
	    String username = txtUsername.getText().trim();
	    String password = txtPassword.getText().trim();
	    String role = UserDAO.authenticate(username, password);

	    if (role != null) {
	    	int userId = UserDAO.getUserId(username);
	    	UserSession.setInstance(userId, username, role);
	    	if(role.equals("admin")) {
	    		SceneManager.switchScene("/application/admin/AdminDashboard.fxml");
	    	} else {
	    		SceneManager.switchScene("/application/customer/CustomerDashboard.fxml");
	    	}
	    } else {
	    	lblError.setText("Invalid credentials");
	    }
	}
}
