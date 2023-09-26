package model;

/**
 * Extend the Part class to inherit all of its variables and methods
 * Additionally contains the variable machine id and its getters and setters
 * @author Autumrose Stubbs
 */
public class InHouse extends Part{
    private int machineId;
    /**
     * Constructor, calls super for Part and includes machine id
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    
    /** 
     * Returns the value of machineId
     * @return the machine id
     */
    public int getMachineId() {
        return machineId;
    }
    
    /**
     * Sets the value of machineId
     * @param machineId 
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
}
