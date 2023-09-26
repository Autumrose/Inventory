package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Autumrose Stubbs
 */
public class Product {
    // Initialize all product variables
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    
    // Constructor, assign and initialize all variables
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }
    
    /**
     * 
     * Setter methods to assign any Product variables
     * @param id, name, price, stock, min, max
     * 
     */
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    public void setMin(int min){
        this.min = min;
    }
    public void setMax(int max){
        this.max = max;
    }
    
    /**
     * Getter methods to return any Product variables 
     * @return 
     */
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public double getPrice(){
        return this.price;
    }
    public int getStock(){
        return this.stock;
    }
    public int getMin(){
        return this.min;
    }
    public int getMax(){
        return this.max;
    }
    
    /**
     * Add an input part to the Observable list associated parts for this specific product
     * @param part 
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    
    /**
     * Delete the input part from the associated parts list for the specific product 
     * @param selectedAssociatedPart
     * @return 
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        return associatedParts.remove(selectedAssociatedPart);
    }
    
    /**
     * Return all associated parts for the given product 
     * @return 
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}

