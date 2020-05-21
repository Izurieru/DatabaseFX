package databasefx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();
            
            Scene s = new Scene(root);
            
            primaryStage.setScene(s);
            primaryStage.setTitle("DatabaseFX");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
}
