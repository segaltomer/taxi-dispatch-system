package HW3;

import java.util.ArrayList;

/**
 * Class representing a manager in the taxi management system
 */
public class Manager extends Person {
    protected ArrayList<Taxi> taxis;
    
    /**
     * Constructor for Manager
     * @param id Manager's ID
     * @param firstName Manager's first name
     * @param lastName Manager's last name
     * @param phone Manager's phone number
     * @param address Manager's address
     */
    public Manager(String id, String firstName, String lastName, String phone, String address) {
        super(id, firstName, lastName, phone, address);
        this.taxis = new ArrayList<>();
    }
    
    /**
     * Add a taxi to this manager's responsibility
     * @param taxi The taxi to add
     * @return true if added successfully, false otherwise
     */
    public boolean addTaxi(Taxi taxi) {
        if (taxi != null && !taxis.contains(taxi)) {
            return taxis.add(taxi);
        }
        return false;
    }
    
    /**
     * Remove a taxi from this manager's responsibility
     * @param taxi The taxi to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeTaxi(Taxi taxi) {
        return taxis.remove(taxi);
    }
    
    /**
     * Get all taxis managed by this manager
     * @return ArrayList of taxis
     */
    public ArrayList<Taxi> getTaxis() {
        return taxis;
    }
    
    /**
     * Set the taxis list (mainly for initialization)
     * @param taxis The new taxis list
     */
    public void setTaxis(ArrayList<Taxi> taxis) {
        this.taxis = taxis != null ? taxis : new ArrayList<>();
    }
    
    /**
     * Get the number of taxis managed by this manager
     * @return Number of taxis
     */
    public int getNumberOfTaxis() {
        return taxis.size();
    }
    
    /**
     * Check if this manager is responsible for a specific taxi
     * @param taxiCode The taxi code to check
     * @return true if responsible, false otherwise
     */
    public boolean isResponsibleFor(String taxiCode) {
        for (Taxi taxi : taxis) {
            if (taxi.getTaxiCode().equals(taxiCode)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("Manager - %s, Manages %d taxis", 
                           super.toString(), taxis.size());
    }
}