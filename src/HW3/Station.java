package HW3;

import java.util.ArrayList;

/**
 * Class representing a taxi station in the taxi management system
 */
public class Station {
    private String stationName;
    private ArrayList<Taxi> taxis;
    
    /**
     * Constructor for Station
     * @param stationName Name of the station
     * @param taxis List of taxis at this station
     */
    public Station(String stationName, ArrayList<Taxi> taxis) {
        this.stationName = stationName;
        this.taxis = taxis != null ? new ArrayList<>(taxis) : new ArrayList<>();
    }
    
    /**
     * Get the station name
     * @return station name
     */
    public String getStationName() {
        return stationName;
    }
    
    /**
     * Set the station name
     * @param stationName new station name
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    
    /**
     * Get all taxis at this station
     * @return list of taxis
     */
    public ArrayList<Taxi> getTaxis() {
        return taxis;
    }
    
    /**
     * Set the taxis list
     * @param taxis new taxis list
     */
    public void setTaxis(ArrayList<Taxi> taxis) {
        this.taxis = taxis != null ? new ArrayList<>(taxis) : new ArrayList<>();
    }
    
    /**
     * Add a taxi to this station
     * @param taxi taxi to add
     * @return true if added successfully, false if already exists
     */
    public boolean addTaxi(Taxi taxi) {
        if (taxi != null && !taxis.contains(taxi)) {
            return taxis.add(taxi);
        }
        return false;
    }
    
    /**
     * Remove a taxi from this station
     * @param taxi taxi to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeTaxi(Taxi taxi) {
        return taxis.remove(taxi);
    }
    
    /**
     * Get all available taxis at this station
     * @return list of available taxis
     */
    public ArrayList<Taxi> getAvailableTaxis() {
        ArrayList<Taxi> availableTaxis = new ArrayList<>();
        for (Taxi taxi : taxis) {
            if (taxi.isAvailable()) {
                availableTaxis.add(taxi);
            }
        }
        return availableTaxis;
    }
    
    /**
     * Get the number of taxis at this station
     * @return number of taxis
     */
    public int getNumberOfTaxis() {
        return taxis.size();
    }
    
    /**
     * Get the number of available taxis at this station
     * @return number of available taxis
     */
    public int getNumberOfAvailableTaxis() {
        return getAvailableTaxis().size();
    }
    
    /**
     * Check if a specific taxi is at this station
     * @param taxiCode taxi code to check
     * @return true if taxi is at this station, false otherwise
     */
    public boolean hasTaxi(String taxiCode) {
        for (Taxi taxi : taxis) {
            if (taxi.getTaxiCode().equals(taxiCode)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Find a taxi by code at this station
     * @param taxiCode taxi code to find
     * @return taxi if found, null otherwise
     */
    public Taxi findTaxi(String taxiCode) {
        for (Taxi taxi : taxis) {
            if (taxi.getTaxiCode().equals(taxiCode)) {
                return taxi;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("Station: %s - Total Taxis: %d, Available: %d", 
                           stationName, getNumberOfTaxis(), getNumberOfAvailableTaxis());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Station station = (Station) obj;
        return stationName != null ? stationName.equals(station.stationName) : station.stationName == null;
    }
    
    @Override
    public int hashCode() {
        return stationName != null ? stationName.hashCode() : 0;
    }
}