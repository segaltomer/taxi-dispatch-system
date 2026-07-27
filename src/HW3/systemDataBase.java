package HW3;

import java.util.*;

/**
 * Main database class for the taxi management system
 * Contains all collections and management methods
 */
public class systemDataBase {
    private ArrayList<Manager> managers;
    private ArrayList<Subscription> subscriptions;
    private ArrayList<Taxi> taxis;
    private ArrayList<Station> stations;
    private ArrayList<Order> orders;
    
    // Additional collections for efficient data retrieval
    private HashMap<String, ArrayList<Order>> ordersPerSub;
    private HashMap<String, ArrayList<Taxi>> taxisPerSub;
    
    /**
     * Constructor - initializes all collections
     */
    public systemDataBase() {
        this.managers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.taxis = new ArrayList<>();
        this.stations = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.ordersPerSub = new HashMap<>();
        this.taxisPerSub = new HashMap<>();
    }
    
    // Manager operations
    
    /**
     * Add a manager to the system
     * @param manager manager to add
     * @return true if added successfully, false if already exists
     */
    public boolean addManager(Manager manager) {
        if (manager == null) return false;
        
        // Check if manager with same ID already exists
        for (Manager m : managers) {
            if (m.getId().equals(manager.getId())) {
                return false;
            }
        }
        
        return managers.add(manager);
    }
    
    /**
     * Remove a manager from the system
     * @param manager manager to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeManager(Manager manager) {
        return manager != null && managers.remove(manager);
    }
    
    /**
     * Find a manager by ID
     * @param managerId manager ID to search for
     * @return manager if found, null otherwise
     */
    public Manager findManager(String managerId) {
        for (Manager manager : managers) {
            if (manager.getId().equals(managerId)) {
                return manager;
            }
        }
        return null;
    }
    
    /**
     * Get all managers
     * @return list of all managers
     */
    public ArrayList<Manager> getManagers() {
        return managers;
    }
    
    // Subscription operations
    
    /**
     * Add a subscription to the system
     * @param subscription subscription to add
     * @return true if added successfully, false if already exists
     */
    public boolean addSubscription(Subscription subscription) {
        if (subscription == null) return false;
        
        // Check if subscription with same code already exists
        for (Subscription s : subscriptions) {
            if (s.getSubCode().equals(subscription.getSubCode())) {
                return false;
            }
        }
        
        subscriptions.add(subscription);
        // Initialize collections for this subscriber
        ordersPerSub.put(subscription.getSubCode(), new ArrayList<>());
        taxisPerSub.put(subscription.getSubCode(), new ArrayList<>());
        return true;
    }
    
    /**
     * Remove a subscription from the system
     * @param subscription subscription to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeSubscription(Subscription subscription) {
        if (subscription == null) return false;
        
        boolean removed = subscriptions.remove(subscription);
        if (removed) {
            // Clean up related collections
            ordersPerSub.remove(subscription.getSubCode());
            taxisPerSub.remove(subscription.getSubCode());
        }
        return removed;
    }
    
    /**
     * Find a subscription by code
     * @param subCode subscription code to search for
     * @return subscription if found, null otherwise
     */
    public Subscription findSubscription(String subCode) {
        for (Subscription subscription : subscriptions) {
            if (subscription.getSubCode().equals(subCode)) {
                return subscription;
            }
        }
        return null;
    }
    
    /**
     * Get all subscriptions
     * @return list of all subscriptions
     */
    public ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }
    
    // Taxi operations
    
    /**
     * Add a taxi to the system
     * @param taxi taxi to add
     * @return true if added successfully, false if already exists
     */
    public boolean addTaxi(Taxi taxi) {
        if (taxi == null) return false;
        
        // Check if taxi with same code already exists
        for (Taxi t : taxis) {
            if (t.getTaxiCode().equals(taxi.getTaxiCode())) {
                return false;
            }
        }
        
        return taxis.add(taxi);
    }
    
    /**
     * Remove a taxi from the system
     * @param taxi taxi to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeTaxi(Taxi taxi) {
        if (taxi == null) return false;
        
        boolean removed = taxis.remove(taxi);
        if (removed) {
            // Remove from all managers
            for (Manager manager : managers) {
                manager.removeTaxi(taxi);
            }
            
            // Remove from all stations
            for (Station station : stations) {
                station.removeTaxi(taxi);
            }
            
            // Remove from subscriber taxi collections
            for (ArrayList<Taxi> taxiList : taxisPerSub.values()) {
                taxiList.remove(taxi);
            }
        }
        return removed;
    }
    
    /**
     * Find a taxi by code
     * @param taxiCode taxi code to search for
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
    
    /**
     * Get all taxis
     * @return list of all taxis
     */
    public ArrayList<Taxi> getTaxis() {
        return taxis;
    }
    
    /**
     * Get all available taxis
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
    
    // Station operations
    
    /**
     * Add a station to the system
     * @param station station to add
     * @return true if added successfully, false if already exists
     */
    public boolean addStation(Station station) {
        if (station == null) return false;
        
        // Check if station with same name already exists
        for (Station s : stations) {
            if (s.getStationName().equals(station.getStationName())) {
                return false;
            }
        }
        
        return stations.add(station);
    }
    
    /**
     * Remove a station from the system
     * @param station station to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeStation(Station station) {
        return station != null && stations.remove(station);
    }
    
    /**
     * Find a station by name
     * @param stationName station name to search for
     * @return station if found, null otherwise
     */
    public Station findStation(String stationName) {
        for (Station station : stations) {
            if (station.getStationName().equals(stationName)) {
                return station;
            }
        }
        return null;
    }
    
    /**
     * Get all stations
     * @return list of all stations
     */
    public ArrayList<Station> getStations() {
        return stations;
    }
    
    /**
     * Get free taxis from a specific station
     * @param station the station to check
     * @return list of free taxis at the station
     */
    public ArrayList<Taxi> getFreeTaxis(Station station) {
        if (station == null) return new ArrayList<>();
        return station.getAvailableTaxis();
    }
    
    // Order operations
    
    /**
     * Add an order to the system
     * @param order order to add
     * @return true if added successfully, false if already exists
     */
    public boolean addOrder(Order order) {
        if (order == null) return false;
        
        // Check if order with same number already exists
        for (Order o : orders) {
            if (o.getOrderNum().equals(order.getOrderNum())) {
                return false;
            }
        }
        
        orders.add(order);
        
        // Add to subscriber's orders
        String subCode = order.getSubCode();
        if (!ordersPerSub.containsKey(subCode)) {
            ordersPerSub.put(subCode, new ArrayList<>());
        }
        ordersPerSub.get(subCode).add(order);
        
        // Add taxi to subscriber's taxi list
        if (!taxisPerSub.containsKey(subCode)) {
            taxisPerSub.put(subCode, new ArrayList<>());
        }
        if (!taxisPerSub.get(subCode).contains(order.getTaxi())) {
            taxisPerSub.get(subCode).add(order.getTaxi());
        }
        
        return true;
    }
    
    /**
     * Remove an order from the system
     * @param order order to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeOrder(Order order) {
        if (order == null) return false;
        
        boolean removed = orders.remove(order);
        if (removed) {
            // Remove from subscriber's orders
            ArrayList<Order> subOrders = ordersPerSub.get(order.getSubCode());
            if (subOrders != null) {
                subOrders.remove(order);
            }
        }
        return removed;
    }
    
    /**
     * Find an order by number
     * @param orderNum order number to search for
     * @return order if found, null otherwise
     */
    public Order findOrder(String orderNum) {
        for (Order order : orders) {
            if (order.getOrderNum().equals(orderNum)) {
                return order;
            }
        }
        return null;
    }
    
    /**
     * Get all orders
     * @return list of all orders
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }
    
    /**
     * Get orders per subscriber
     * @return map of subscriber codes to their orders
     */
    public HashMap<String, ArrayList<Order>> getOrdersPerSub() {
        return ordersPerSub;
    }
    
    /**
     * Get taxis per subscriber
     * @return map of subscriber codes to their taxis
     */
    public HashMap<String, ArrayList<Taxi>> getTaxisPerSub() {
        return taxisPerSub;
    }
    
    /**
     * Get express taxis for a specific subscriber
     * @param subscription the subscriber
     * @return list of express taxis used by the subscriber
     */
    public ArrayList<ExpressTaxi> getExpressTaxis(Subscription subscription) {
        ArrayList<ExpressTaxi> expressTaxis = new ArrayList<>();
        if (subscription == null) return expressTaxis;
        
        ArrayList<Taxi> subTaxis = taxisPerSub.get(subscription.getSubCode());
        if (subTaxis != null) {
            for (Taxi taxi : subTaxis) {
                if (taxi instanceof ExpressTaxi) {
                    expressTaxis.add((ExpressTaxi) taxi);
                }
            }
        }
        return expressTaxis;
    }
    
    /**
     * Get orders by manager ID
     * @param managerId manager ID to search for
     * @return list of orders created by the manager
     */
    public ArrayList<Order> getOrdersByManager(String managerId) {
        ArrayList<Order> managerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getManagerId().equals(managerId)) {
                managerOrders.add(order);
            }
        }
        return managerOrders;
    }
    
    /**
     * Get statistics about the system
     * @return formatted string with system statistics
     */
    public String getSystemStatistics() {
        return String.format("System Statistics:\n" +
                           "- Total Managers: %d\n" +
                           "- Total Subscribers: %d\n" +
                           "- Total Taxis: %d (Available: %d)\n" +
                           "- Total Stations: %d\n" +
                           "- Total Orders: %d",
                           managers.size(),
                           subscriptions.size(),
                           taxis.size(),
                           getAvailableTaxis().size(),
                           stations.size(),
                           orders.size());
    }
}