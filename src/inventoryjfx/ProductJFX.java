package inventoryjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Product view
 * @author Autumrose Stubbs
 */
public class ProductJFX extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
        @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/ProductJFX.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    
}
