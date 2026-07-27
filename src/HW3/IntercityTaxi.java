package HW3;

import java.util.ArrayList;

/**
 * Class representing an intercity taxi in the taxi management system
 * Intercity taxis serve multiple cities and have time limitations
 */
public class IntercityTaxi extends Taxi {
    private ArrayList<String> cities;
    private double extraPrice;
    private int maxHours;
    
    /**
     * Constructor for IntercityTaxi
     * @param taxiCode Unique taxi code
     * @param available Availability status
     * @param minPrice Minimum price for the taxi service
     * @param cities List of cities served
     * @param extraPrice Additional price for intercity service
     * @param maxHours Maximum hours for the service
     */
    public IntercityTaxi(String taxiCode, boolean available, double minPrice, 
                        ArrayList<String> cities, double extraPrice, int maxHours) {
        super(taxiCode, available, minPrice);
        this.cities = cities != null ? new ArrayList<>(cities) : new ArrayList<>();
        this.extraPrice = extraPrice;
        this.maxHours = maxHours;
    }
    
    /**
     * Get the list of cities served
     * @return list of cities
     */
    public ArrayList<String> getCities() {
        return cities;
    }
    
    /**
     * Set the list of cities served
     * @param cities new list of cities
     */
    public void setCities(ArrayList<String> cities) {
        this.cities = cities != null ? new ArrayList<>(cities) : new ArrayList<>();
    }
    
    /**
     * Add a city to the service area
     * @param city city to add
     * @return true if added successfully, false if already exists
     */
    public boolean addCity(String city) {
        if (city != null && !cities.contains(city)) {
            return cities.add(city);
        }
        return false;
    }
    
    /**
     * Remove a city from the service area
     * @param city city to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeCity(String city) {
        return cities.remove(city);
    }
    
    /**
     * Check if a city is served
     * @param city city to check
     * @return true if served, false otherwise
     */
    public boolean servesCity(String city) {
        return cities.contains(city);
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
     * Get the maximum hours
     * @return maximum hours
     */
    public int getMaxHours() {
        return maxHours;
    }
    
    /**
     * Set the maximum hours
     * @param maxHours new maximum hours
     */
    public void setMaxHours(int maxHours) {
        this.maxHours = maxHours;
    }
    
    /**
     * Calculate the total price for the intercity service
     * @return total price (minimum price + extra price)
     */
    @Override
    public double calculatePrice() {
        return minPrice + extraPrice;
    }
    
    /**
     * Get the number of cities served
     * @return number of cities
     */
    public int getNumberOfCities() {
        return cities.size();
    }
    
    @Override
    public String toString() {
        return String.format("Intercity Taxi - Code: %s, Available: %s, Min Price: %.2f, " +
                           "Cities: %s, Extra Price: %.2f, Max Hours: %d, Total: %.2f", 
                           taxiCode, available ? "Yes" : "No", minPrice, 
                           cities.toString(), extraPrice, maxHours, calculatePrice());
    }
}