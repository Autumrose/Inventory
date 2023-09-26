package inventoryjfx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Outsourced Part view
 * @author Autumrose Stubbs
 */
public class OutsourcedPartJFX extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/OutsourcedPartJFX.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    
}
