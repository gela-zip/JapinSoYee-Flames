package flamesgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlamesGame extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root, 800, 600);
        
        stage.setTitle("FLAMES Game");
        stage.setScene(scene);
        stage.setResizable(false); // Prevents window resizing from adding black bars
        stage.sizeToScene(); // Clamps window bounds to exact 800x600 resolution
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}