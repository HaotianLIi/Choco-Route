package application;

import application.db.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		DatabaseInitializer.initialize();
		// TODO Auto-generated method stub
		try {
			application.login.SceneManager.setStage(primaryStage);
			
			Parent root = FXMLLoader.load(getClass().getResource("/application/login/LoginView.fxml"));
			Scene scene = new Scene(root, 1000, 800);
			primaryStage.setTitle("ChocoRoute - Chocolate E-Commerce");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
    public static void main(String[] args) {
        launch(args);
    }

}
