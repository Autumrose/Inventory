package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.TrackIds;

/**
 * FXML In House Part Controller class. 
 * Contains all event handlers for the addition and modification of the In House specific parts
 *
 * @author autum
 */
public class InHousePartJFXController implements Initializable {
    // Declare all fields for the view, text fields, and labels. 
    Stage stage;
    Parent scene;
    
    
    @FXML
    private TextField partCost;

    @FXML
    private TextField partID;

    @FXML
    private TextField partInv;

    @FXML
    private TextField partMachineID;

    @FXML
    private TextField partMax;

    @FXML
    private TextField partMin;

    @FXML
    private TextField partName;
    
    @FXML
    private Label partTitle;
    
    
    /** 
     * Event handler method for the cancel button. 
     * When the cancel button is clicked, don't save any data
     * Change the view to show the home page once again
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionCancelPart(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showConfirmationAlert("Confirm Cancellation?", "Are you sure you would like to return to the home page? Data you filled out in this form will be lost.");
        if (result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();   
            scene = FXMLLoader.load(getClass().getResource("/view/InventoryMainFormJFX.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /**
     * Event handler method for the outsourced radio button. 
     * If the outsourced radio button is selected, switch the view to the Outsourced Part view
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionOutsourced(ActionEvent event) throws IOException {
        if (partTitle.getText().equals("Modify Part")){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/OutsourcedPartJFX.fxml"));  
            loader.load();
            OutsourcedPartJFXController partController = loader.getController();
            // Send a new Outsourced part with current fields. Set company name to an empty string to signal that it should be left blank in OutsourcedController
            partController.sendPart(new Outsourced(Integer.parseInt(partID.getText()), partName.getText(), 
                    Double.parseDouble(partCost.getText()), Integer.parseInt(partInv.getText()),
                    Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), ""));
            stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }else{
            stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();   
            scene = FXMLLoader.load(getClass().getResource("/view/OutsourcedPartJFX.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /**
     * Event handler for the save button. 
     *   Save information to a new part object or
     *   Update an existing part object with the new information 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        try{
            // Assign variables with the information currently in each text field
            double price = Double.parseDouble(partCost.getText());
            int stock = Integer.parseInt(partInv.getText());
            int machineId = Integer.parseInt(partMachineID.getText());
            int max = Integer.parseInt(partMax.getText());
            int min = Integer.parseInt(partMin.getText());
            String name = partName.getText();
            
            if (name.length() <= 0){
                showErrorAlert("Invalid Name", "The \"Name\" field can not be blank!");
                return;
            }
            if (min > max){
                showErrorAlert("Invalid range", "The minimum value can not be greater than the maximum value");
                return;
            }
            if (stock > max || stock < min){
               showErrorAlert("Invalid range", "The inv value must be between the min and the max");
                return;
            }
            // Check if we are adding or modifying a part
            if (partTitle.getText().equals("Add Part")){
                // Generate next id number
                int id = TrackIds.getNextPartId();            
                // Add new part to Inventory all part list with assigned variables
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
            }else{
                // Find part id from the field 
                int id = Integer.parseInt(partID.getText());
                int i= 0;
                // Find part's index to update 
                for (Part p : Inventory.getAllParts()){
                    if (p.getId() == id){
                        break;
                    }
                    i++;
                }
                // Call the Inventory field for updating part 
                Inventory.updatePart(i, new InHouse(id, name, price, stock, min, max, machineId));
            }

            // Set stage back to the home page
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();   
            scene = FXMLLoader.load(getClass().getResource("/view/InventoryMainFormJFX.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }catch(NumberFormatException e){
            showErrorAlert("Invalid entry", "Please enter a valid input for all fields of this part\t" + e.getMessage());
        }
    }
    
    
    /**
     * Method for Inventory main controller to communicate with our in house controller for a modification. 
     * Set all values passed in for part into the text fields 
     * @param part 
     */
    public void sendPart(Part part){
        // Update the title to state modify instead of add
        partTitle.setText("Modify Part");
        // Assign the remainder of the text fields to the part object's values
        partID.setText(String.valueOf(part.getId()));
        partInv.setText(String.valueOf(part.getStock()));
        partMax.setText(String.valueOf(part.getMax()));
        partMin.setText(String.valueOf(part.getMin()));
        partName.setText(part.getName());
        partCost.setText(String.valueOf(part.getPrice()));
        if (((InHouse)part).getMachineId() != -1){
            partMachineID.setText(String.valueOf(((InHouse)part).getMachineId()));
        }

    }
    
    /**
     * Method to show an error alert pop up. 
     * 
     * @param title the title of the alert pop up
     * @param message the full message to include in the alert
     */
    public void showErrorAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Method to show a confirmation alert. 
     * 
     * @param title the title of the alert pop up
     * @param message the full message to include in the alert
     * @return the button the user selected 
     */
    public Optional<ButtonType> showConfirmationAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
