package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import model.TrackIds;

/**
 * 
 * FXML Product Controller class.
 * Contains all event handlers for the addition and modification for the Products view 
 * RUNTIME ERROR: my logical error was in my handler methods onActionAdd and onActionRemoveAssocPart, my table was allowing me to add/remove items even when there were no items in the table
 * I was throwing a try catch similar to what I did on my home page and it wasn't catching the NullPointerException like I thought it would. It might have been happening because I check if the list 
 * contains the item first before adding it to the list of associated parts. Instead I went ahead and just checked using an if statement if the part object was null, if so then I showed the alert
 * otherwise I let the function work as it should. 
 * @author Autumrose Stubbs
 * 
 */
public class ProductJFXController implements Initializable {
    // Declare all fields for the view, text fields, buttons, and labels. 
    Stage stage;
    Parent scene;
    private ObservableList<Part> tempAssocParts;

    @FXML
    private TableColumn<Part, Integer> allPartID;

    @FXML
    private TableColumn<Part, Integer> allPartInv;

    @FXML
    private TableColumn<Part, String> allPartName;

    @FXML
    private TableColumn<Part, Double> allPartPrice;
    
    @FXML
    private TableView<Part> allPartTable;

    @FXML
    private TableColumn<Part, Integer> assocPartID;

    @FXML
    private TableColumn<Part, Integer> assocPartInv;

    @FXML
    private TableColumn<Part, String> assocPartName;

    @FXML
    private TableColumn<Part, Double> assocPartPrice;
        
    @FXML
    private TableView<Part> assocPartTable;

    @FXML
    private TextField productID;

    @FXML
    private TextField productInv;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    @FXML
    private TextField productName;

    @FXML
    private TextField productPrice;
    
    @FXML
    private TextField searchPart;
    
    @FXML
    private Label productTitle;

    /**
     * Event handler method for the search part text field. 
     * If input is a number search for an Id that matches
     * Else search for product names that are a partial or full match to the input string
     * 
     * @param event when the search field has information input and the enter key is selected
     */
    @FXML
    void onActionSearchPart(ActionEvent event) {
        // Create list to add matching result(s)
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        String input = searchPart.getText();
        String type = "";
        // If length is put back at 0 load all results
        if (input.length() <= 0){
            filteredParts = Inventory.getAllParts();
        }
        // Try to parse int, if successful use the method in Inventory class to lookup an ID
        try{
            int id = Integer.parseInt(input);
            Part part = Inventory.lookupPart(id);
            if (part != null){
                filteredParts.add(part);
            }
            type = "id";
        // If unsuccessful use the Inventory class to look up a partial/full name to find a part
        }catch(Exception e){
            filteredParts = Inventory.lookupPart(input);
            type = "letters";
        }
        if (filteredParts.isEmpty()){
            showErrorAlert("No results found", "No results were found with the entered " + type + " please change your search criteria and try again");
            searchPart.setText("");
            filteredParts = Inventory.getAllParts();
        }else{   
            // Set the parts table view with the filtered list
            allPartTable.setItems(filteredParts);
            allPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
            allPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            allPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
            allPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }
    
    /**
     * Event handler method for the add button. 
     * Add the selected part to the associated parts list 
     * @param event when the add button is clicked 
     */
    @FXML
    void onActionAdd(ActionEvent event) {
            Part part = allPartTable.getSelectionModel().getSelectedItem();
            if ( part!= null){
                if(!tempAssocParts.contains(part)){
                    tempAssocParts.add(part);
                }
            }else{
                showErrorAlert("Please select a row", "Please select an item from the table, if no items available please return to home to add additional parts");
            }
    }
    
    /** 
     * Even handler method for the cancel button. 
     * When the cancel button is clicked, don't save any data
     * Change the view to show the home page once again
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showConfirmationAlert("Confirm Cancellation?", "Are you sure you would like to return to the home page? Data you filled out in this form will be lost.");
        if (result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();   
            scene = FXMLLoader.load(getClass().getResource("/view/InventoryMainFormJFX.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /**
     *
     * Remove the selected part from the temp associated parts list. 
     * Product's associated parts list will only be updated on a save
     * 
     * @param event 
     */
    @FXML
    void onActionRemoveAssocPart(ActionEvent event) {
        Part part = assocPartTable.getSelectionModel().getSelectedItem();
        if(part != null){
            Optional<ButtonType> result = showConfirmationAlert("Confirm Deletion?", "Are you sure you would like to remove the selected item? The Part will no longer be associated with this product.");
            if (result.isPresent() && result.get() == ButtonType.OK){
                tempAssocParts.remove(part);
            }
        }else{
            showErrorAlert("Please select a row", "Please select an item from the table, if no items available you can add more items to this product from the table above");
        }
    }
    
    /**
     * Event handler for the save button, either save successfully or receive an error. 
     *   Save information to a new product object
     *   Update an existing product object with the new information 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try{
            int stock = Integer.parseInt(productInv.getText());
            int max = Integer.parseInt(productMax.getText());
            int min = Integer.parseInt(productMin.getText());
            String name = productName.getText();
            double price = Double.parseDouble(productPrice.getText());
            // Validating no empty strings and the min, max, and inv are within allowed range
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
            Product product;
            // Check if we are adding or modifying a part
            if (productTitle.getText().equals("Add Product")){
                // Generate next id number
                int id = TrackIds.getNextProductId();            
                product = new Product(id, name, price, stock, min, max);

                // Add new part to Inventory all part list with assigned variables
                Inventory.addProduct(product);

            }else{
                // Find part id from the field 
                int id = Integer.parseInt(productID.getText());
                int index = 0;
                // Find part's index to update 
                for (Product p : Inventory.getAllProducts()){
                    if (p.getId() == id){
                        break;
                    }
                    index++;
                }
                product = new Product(id, name, price, stock, min, max);

                // Call the Inventory field for updating part 
                Inventory.updateProduct(index, product);
            }
            for (Part p : tempAssocParts){
                product.addAssociatedPart(p);
            }
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();   
            scene = FXMLLoader.load(getClass().getResource("/view/InventoryMainFormJFX.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }catch(NumberFormatException e){
            showErrorAlert("Invalid entry", "Please enter a valid input for all fields of this product\t" + e.getMessage());
        }
    }
    
    /**
     * Method for Inventory main controller to communicate with our Product controller for a modification. 
     * Set all values passed in for part into the text fields 
     * @param product passes in the current product being modified in order to fill out all the product fields to edit 
     */
    public void sendProduct(Product product){
        // First set the title as modify instead of add
        productTitle.setText("Modify Product");
        // Set all our text boxes to the values passed in
        productID.setText(String.valueOf(product.getId()));
        productInv.setText(String.valueOf(product.getStock()));
        productMax.setText(String.valueOf(product.getMax()));
        productMin.setText(String.valueOf(product.getMin()));
        productName.setText(product.getName());
        productPrice.setText(String.valueOf(product.getPrice()));
        
        // Add all associated parts to the temp list to assign to the table 
        for (Part p : product.getAllAssociatedParts()){
            tempAssocParts.add(p);
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
        tempAssocParts = FXCollections.observableArrayList();

        // Initialize the table with all parts to the all parts list
        allPartTable.setItems(Inventory.getAllParts());
        allPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        
        // Initialize the table with associated parts to the local temp parts list
        assocPartTable.setItems(tempAssocParts);
        assocPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    
    
    
}
