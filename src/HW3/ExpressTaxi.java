package HW3;

/**
 * Class representing an express taxi in the taxi management system
 * Express taxis have additional features and pricing
 */
public class ExpressTaxi extends Taxi {
    private boolean cityTaxi;
    private double extraPrice;
    
    /**
     * Constructor for ExpressTaxi
     * @param taxiCode Unique taxi code
     * @param available Availability status
     * @param minPrice Minimum price for the taxi service
     * @param cityTaxi Whether this is a city taxi
     * @param extraPrice Additional price for express service
     */
    public ExpressTaxi(String taxiCode, boolean available, double minPrice, 
                      boolean cityTaxi, double extraPrice) {
        super(taxiCode, available, minPrice);
        this.cityTaxi = cityTaxi;
        this.extraPrice = extraPrice;
    }
    
    /**
     * Check if this is a city taxi
     * @return true if city taxi, false otherwise
     */
    public boolean isCityTaxi() {
        return cityTaxi;
    }
    
    /**
     * Set city taxi status
     * @param cityTaxi new city taxi status
     */
    public void setCityTaxi(boolean cityTaxi) {
        this.cityTaxi = cityTaxi;
    }
    
    /**
     * Get the extra price
     * @return extra price
     */
    public double getExtraPrice() {
        return extraPrice;
    }
    
    /**
     * Set the extra price
     * @param extraPrice new extra price
     */
    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }
    
    /**
     * Calculate the total price for the express service
     * @return total price (minimum price + extra price)
     */
    @Override
    public double calculatePrice() {
        return minPrice + extraPrice;
    }
    
    @Override
    public String toString() {
        return String.format("Express Taxi - Code: %s, Available: %s, Min Price: %.2f, " +
                           "City Taxi: %s, Extra Price: %.2f, Total: %.2f", 
                           taxiCode, available ? "Yes" : "No", minPrice, 
                           cityTaxi ? "Yes" : "No", extraPrice, calculatePrice());
    }
}