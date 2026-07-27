package HW3;

/**
 * Class representing a subscriber in the taxi management system
 */
public class Subscription extends Person {
    private String subCode;
    
    /**
     * Constructor for Subscription
     * @param subCode Subscriber's unique code
     * @param firstName Subscriber's first name
     * @param lastName Subscriber's last name
     * @param phone Subscriber's phone number
     * @param address Subscriber's address
     */
    public Subscription(String subCode, String firstName, String lastName, String phone, String address) {
        super(subCode, firstName, lastName, phone, address);
        this.subCode = subCode;
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
        this.id = subCode; // Keep id synchronized with subCode
    }
    
    @Override
    public String toString() {
        return String.format("Subscriber - Code: %s, Name: %s %s, Phone: %s, Address: %s", 
                           subCode, firstName, lastName, phone, address);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subscription that = (Subscription) obj;
        return subCode != null ? subCode.equals(that.subCode) : that.subCode == null;
    }
    
    @Override
    public int hashCode() {
        return subCode != null ? subCode.hashCode() : 0;
    }
}