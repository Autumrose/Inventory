package inventoryjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the In House Part view
 * @author Autumrose Stubbs
 */
public class InHousePartJFX extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop(){
        
    }
    
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/InHousePartJFX.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    
}
