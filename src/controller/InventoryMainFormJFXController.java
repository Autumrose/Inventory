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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * 
 * FXML Inventory Main Menu Controller class. 
 * Contains all event handlers for the addition and modification of the main inventory page
 * Contains a part and product grid with information as well as the option to add, modify, or delete
 * 
 * @author Autumrose Stubbs
 */
public class InventoryMainFormJFXController implements Initializable {
    // Declare all fields for the view, text fields, and labels. 
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField searchPart;

    @FXML
    private TextField searchProduct;
    
     @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, Integer> productInvCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;
    
    @FXML
    private TableView<Product> productsTableView;
    
    /**
     * Event handler for the part search field. 
     * If input is a number search for an Id that matches
     * Else search for part names that are a partial or full match to the input string
     * 
     * @param event 
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
            partsTableView.setItems(filteredParts);
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }
    
    /**
     * Event handler for the product search field. 
     * If input is a number search for an Id that matches
     * Else search for product names that are a partial or full match to the input string
     * 
     * @param event 
     */
    @FXML
    void onActionSearchProduct(ActionEvent event) {
        // Create list to add matching result(s)
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        String input = searchProduct.getText();
        String type = "";
        
        // If length is put back at 0 load all results
        if (input.length() <= 0){
            filteredProducts = Inventory.getAllProducts();
        }
        // Try to parse int, if successful use the method in Inventory class to lookup an ID
        try{
            int id = Integer.parseInt(input);
            Product product = Inventory.lookupProduct(id);
            if (product != null){
                filteredProducts.add(product);
                type = "id";
            }
        // If unsuccessful use the Inventory class to look up a partial/full name to find a part
        }catch(Exception e){
            filteredProducts = Inventory.lookupProduct(input);
            type = "letters";
        }
        if (filteredProducts.isEmpty()){
            showErrorAlert("No results found", "No results were found with the entered " + type + " please change your search criteria and try again");
            searchPart.setText("");
            filteredProducts = Inventory.getAllProducts();
        }else{
            // Set the parts table view with the filtered list
            productsTableView.setItems(filteredProducts);
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }
    
    /** 
     * 
     * Event for the add part button. 
     * Launch the In House Part form for the user to fill out the required fields and save
     * In House is the default, once open user can switch to Outsourced if desired
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();   
        scene = FXMLLoader.load(getClass().getResource("/view/InHousePartJFX.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** 
     * 
     * Launch the Add Product form for the user to fill out the required fields and save
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();   
        scene = FXMLLoader.load(getClass().getResource("/view/ProductJFX.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     *
     * Event handler for the delete part button. 
     * Call the Inventory method deletePart() to remove the Part object from the master list
     * 
     * @param event 
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {
        Optional<ButtonType> result = showConfirmationAlert("Confirm Deletion?", "Are you sure you would like to delete the selected item? The Part's data will be lost.");
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if (part == null){
            showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
        }
        if (result.isPresent() && result.get() == ButtonType.OK){
            Inventory.deletePart(part);
        }
    }
    
    /**
     *
     * Event handler for the delete product button. 
     * Call the Inventory method deleteProduct() to remove the Product object from the master list
     * 
     * @param event 
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        if (product == null){
            showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
            return;
        }
        if(!product.getAllAssociatedParts().isEmpty()){
            showErrorAlert("Invalid product selection", "This product still has parts associated with it, please remove the associated parts before deleting the product.");
            return;
        }
        Optional<ButtonType> result = showConfirmationAlert("Confirm Deletion?", "Are you sure you would like to delete the selected item? The Product's data will be lost.");
        if (result.isPresent() && result.get() == ButtonType.OK){
            Inventory.deleteProduct(product);
        }
    }
    
    /**
     * Event handler for the exit button. Close the window
     * Close the window
     * @param event 
     */
    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);
    }
    
    /** 
     * 
     * Event handler for the modify part button. 
     * Launch correct Part form based on the selected row for the user to edit any of the required fields and save
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {
        try{
            // Assign the selected row to a part variable and initialize an FXML loader
            Part part = partsTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();

            // Check if the part object is In House or Outsourced to decide where to send the user
            if (part instanceof InHouse){
                // Load the user into In House
                loader.setLocation(getClass().getResource("/view/InHousePartJFX.fxml"));  
                loader.load();
                InHousePartJFXController partController = loader.getController();
                // Use the method sendPart to send the selected row's information so it's already prefilled for the user 
                partController.sendPart(part);
            }else{
                // Load the user into Outsourced
                loader.setLocation(getClass().getResource("/view/OutsourcedPartJFX.fxml")); 
                loader.load();
                OutsourcedPartJFXController partController = loader.getController();
                // Use the method sendPart to send the selected row's information so it's already prefilled for the user 
                partController.sendPart(part);
            }
            // Load and show the stage and scene
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }catch(NullPointerException e){
            showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
        }
    }

    /** 
     * 
     * Event handler for the modify product button. 
     * Launch the Product form based on the selected row for the user to edit any of the required fields and save
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {
        try{
            // Create an FXML loader of the Product view class
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ProductJFX.fxml"));
            loader.load();
            // Create an instance of the product controller to pass information 
            ProductJFXController prodController = loader.getController();
            // Use the sendProduct method to send the selected row's information to preload into the form
            prodController.sendProduct(productsTableView.getSelectionModel().getSelectedItem());

            // Load and show the stage/scene
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }catch(NullPointerException e){
            showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
        }
    }
    
    /**
     * Method to show an error alert pop up
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
     * Method to show a confirmation alert 
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
        // Initialize the part table with any exisitng parts
        partsTableView.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Initialize the product table with any exisitng products
        productsTableView.setItems(Inventory.getAllProducts());
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
    }    
    
}
