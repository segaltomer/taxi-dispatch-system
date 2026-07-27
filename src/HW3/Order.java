package HW3;

/**
 * Class representing an order in the taxi management system
 */
public class Order {
    private String orderNum;
    private String managerId;
    private int day;
    private int month;
    private int hour;
    private String subCode;
    private Taxi taxi;
    private double orderPrice;
    
    /**
     * Constructor for Order
     * @param orderNum Unique order number
     * @param managerId ID of the manager who created the order
     * @param day Day of the order
     * @param month Month of the order
     * @param hour Hour of the order
     * @param subCode Subscriber code who placed the order
     * @param taxi Taxi assigned to the order
     * @param orderPrice Price of the order
     */
    public Order(String orderNum, String managerId, int day, int month, int hour, 
                String subCode, Taxi taxi, double orderPrice) {
        this.orderNum = orderNum;
        this.managerId = managerId;
        this.day = day;
        this.month = month;
        this.hour = hour;
        this.subCode = subCode;
        this.taxi = taxi;
        this.orderPrice = orderPrice;
    }
    
    /**
     * Get the order number
     * @return order number
     */
    public String getOrderNum() {
        return orderNum;
    }
    
    /**
     * Set the order number
     * @param orderNum new order number
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
    
    /**
     * Get the manager ID
     * @return manager ID
     */
    public String getManagerId() {
        return managerId;
    }
    
    /**
     * Set the manager ID
     * @param managerId new manager ID
     */
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    /**
     * Get the day
     * @return day
     */
    public int getDay() {
        return day;
    }
    
    /**
     * Set the day
     * @param day new day
     */
    public void setDay(int day) {
        this.day = day;
    }
    
    /**
     * Get the month
     * @return month
     */
    public int getMonth() {
        return month;
    }
    
    /**
     * Set the month
     * @param month new month
     */
    public void setMonth(int month) {
        this.month = month;
    }
    
    /**
     * Get the hour
     * @return hour
     */
    public int getHour() {
        return hour;
    }
    
    /**
     * Set the hour
     * @param hour new hour
     */
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    /**
     * Get the subscriber code
     * @return subscriber code
     */
    public String getSubCode() {
        return subCode;
    }
    
    /**
     * Set the subscriber code
     * @param subCode new subscriber code
     */
    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }
    
    /**
     * Get the assigned taxi
     * @return taxi
     */
    public Taxi getTaxi() {
        return taxi;
    }
    
    /**
     * Set the assigned taxi
     * @param taxi new taxi
     */
    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }
    
    /**
     * Get the order price
     * @return order price
     */
    public double getOrderPrice() {
        return orderPrice;
    }
    
    /**
     * Set the order price
     * @param orderPrice new order price
     */
    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }
    
    /**
     * Get formatted date string
     * @return formatted date as "day/month"
     */
    public String getFormattedDate() {
        return String.format("%02d/%02d", day, month);
    }
    
    /**
     * Get formatted time string
     * @return formatted time as "hour:00"
     */
    public String getFormattedTime() {
        return String.format("%02d:00", hour);
    }
    
    @Override
    public String toString() {
        return String.format("Order #%s - Date: %s, Time: %s, Subscriber: %s, " +
                           "Taxi: %s (%s), Manager: %s, Price: %.2f", 
                           orderNum, getFormattedDate(), getFormattedTime(), 
                           subCode, taxi.getTaxiCode(), taxi.getClass().getSimpleName(), 
                           managerId, orderPrice);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderNum != null ? orderNum.equals(order.orderNum) : order.orderNum == null;
    }
    
    @Override
    public int hashCode() {
        return orderNum != null ? orderNum.hashCode() : 0;
    }
}