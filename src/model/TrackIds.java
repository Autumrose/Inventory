package model;

/**
 * Static class to track part and product IDs throughout the controllers
 * @author Autumrose Stubbs
 */
public class TrackIds {
    // Static variables to track the part and product id numbers while the window is open
    private static int currPartId = 0;
    private static int currProductId = 0;
    
    /**
     * Return the part id incremented by 1 each time
     * @return part id after it's incremented
     */
    public static int getNextPartId(){
        currPartId++;
        return currPartId;
    }
    
    /**
     * Return the product id incremented by 1 each time
     * @return product id after being incremented
     */
    public static int getNextProductId(){
        currProductId++;
        return currProductId;
    }
}
