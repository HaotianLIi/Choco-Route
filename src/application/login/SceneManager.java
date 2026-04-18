package application.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxml) {
        try {
        	System.out.println("Loading: " + fxml);
            System.out.println("URL: " + SceneManager.class.getResource(fxml)); 
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxml));
            primaryStage.setScene(new Scene(root,1200,900));
            primaryStage.centerOnScreen();
        } catch (Exception e) {
        	System.out.println("Failed to load: " + fxml);
            e.printStackTrace();
        }
    }
}
