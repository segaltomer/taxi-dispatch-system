package HW3;

/**
 * Class representing a regular taxi in the taxi management system
 */
public class Taxi {
    protected String taxiCode;
    protected boolean available;
    protected double minPrice;
    
    /**
     * Constructor for Taxi
     * @param taxiCode Unique taxi code
     * @param available Availability status
     * @param minPrice Minimum price for the taxi service
     */
    public Taxi(String taxiCode, boolean available, double minPrice) {
        this.taxiCode = taxiCode;
        this.available = available;
        this.minPrice = minPrice;
    }
    
    /**
     * Get the taxi code
     * @return taxi code
     */
    public String getTaxiCode() {
        return taxiCode;
    }
    
    /**
     * Set the taxi code
     * @param taxiCode new taxi code
     */
    public void setTaxiCode(String taxiCode) {
        this.taxiCode = taxiCode;
    }
    
    /**
     * Check if taxi is available
     * @return true if available, false otherwise
     */
    public boolean isAvailable() {
        return available;
    }
    
    /**
     * Set taxi availability
     * @param available new availability status
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    /**
     * Get the minimum price
     * @return minimum price
     */
    public double getMinPrice() {
        return minPrice;
    }
    
    /**
     * Set the minimum price
     * @param minPrice new minimum price
     */
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }
    
    /**
     * Calculate the total price for the service
     * For regular taxi, this is just the minimum price
     * @return total price
     */
    public double calculatePrice() {
        return minPrice;
    }
    
    @Override
    public String toString() {
        return String.format("Regular Taxi - Code: %s, Available: %s, Min Price: %.2f", 
                           taxiCode, available ? "Yes" : "No", minPrice);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Taxi taxi = (Taxi) obj;
        return taxiCode != null ? taxiCode.equals(taxi.taxiCode) : taxi.taxiCode == null;
    }
    
    @Override
    public int hashCode() {
        return taxiCode != null ? taxiCode.hashCode() : 0;
    }
}