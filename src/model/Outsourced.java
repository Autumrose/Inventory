package model;

/**
 * Extend the Part class to inherit all of its variables and methods
 * Additionally contains the variable company name and its getters and setters
 * @author Autumrose Stubbs
 */
public class Outsourced extends Part{
    String companyName;
    /**
     * Constructor, calls super for Part and includes company name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /** 
     * Returns the value of companyName
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }
    
    /**
     * Sets the value of company name
     * @param companyName 
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
}
