package inventoryjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Main Menu view.
 * JavaDoc folder is located in the project folder inventory\JavaDocs. Index.html is the starting menu
 * @author Autumrose Stubbs
 */
public class InventoryMainFormJFX extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/InventoryMainFormJFX.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    
}
