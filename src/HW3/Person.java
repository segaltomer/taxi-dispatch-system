package HW3;

/**
 * Abstract base class representing a person in the taxi management system
 */
public abstract class Person {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected String address;
    
    /**
     * Constructor for Person
     * @param id The person's ID
     * @param firstName The person's first name
     * @param lastName The person's last name
     * @param phone The person's phone number
     * @param address The person's address
     */
    public Person(String id, String firstName, String lastName, String phone, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s %s, Phone: %s, Address: %s", 
                           id, firstName, lastName, phone, address);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id != null ? id.equals(person.id) : person.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}