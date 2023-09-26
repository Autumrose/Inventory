package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Data class to store all parts and products as well as any updates. 
 * FUTURE ENHANCEMENT: A future enhancement that would be to actually store this information either locally or on a server and access 
 * its location every time so the data isn't lost when the user exits out of the application. 
 * 
 * @author Autumrose Stubbs
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Methods to add a part to the global list. 
     * @param newPart 
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    } 
    /**
     * Methods to add a product to the global list. 
     * @param newProduct 
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    /**
     * Method to look up one specific part based on the Id and return the part. 
     * @param partId
     * @return the matching part
     */
    public static Part lookupPart(int partId){
        for (Part p : allParts){
            if (p.getId() == partId){
                return p;
            }
        }
        return null;
    }
    /**
     * Method to look up one specific product based on the Id and return the product. 
     * @param productId
     * @return product with input id
     */
    public static Product lookupProduct(int productId){
      for (Product p : allProducts){
            if (p.getId() == productId){
                return p;
            }
       }  
      return null;
    }
    /**
     * Method to lookup a part based on the name or a partial name and return all matching parts. 
     * @param partName
     * @return observable list of matching parts
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        for (Part p : allParts){
            // Check if the current name contains the input name as it doesn't have to be exact
            if (p.getName().toLowerCase().contains(partName.toLowerCase())){
                matchingParts.add(p);
            }
        }
        return matchingParts;

    }
    /**
     * Method to lookup a product based on the name or a partial name and return all matching products. 
     * @param productName
     * @return 
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        for (Product p : allProducts){
            // Check if the current name contains the input name as it doesn't have to be exact
            if (p.getName().toLowerCase().contains(productName.toLowerCase())){
                matchingProducts.add(p);
            }
       }
        return matchingProducts;
    }
    
    /** 
     * 
     * Use the Observable List given functions to set or remove an item for parts or products
     * 
     */
    /**
     * Update a part in the global list using the input index and part. 
     * @param index
     * @param selectedPart 
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }
    /**
     * Update a product in the global list using the input index and product. 
     * @param index
     * @param newProduct 
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }
    /**
     * Delete the given part. 
     * @param selectedPart
     * @return true if the item was deleted successfully, false if it didn't exist
     */
    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }
    
    /**
     * Delete the given product. 
     * @param selectedProduct
     * @return true if the item was deleted successfully, false if it didn't exist
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }
    
    /**
     * Getter methods to return the list of all parts.  
     * @return observable list
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Getter methods to return the list of all products.  
     * @return observable list
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    
}
