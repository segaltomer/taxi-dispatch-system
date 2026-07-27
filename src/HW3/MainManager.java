package HW3;

/**
 * Class representing a main manager in the taxi management system
 * Main managers have additional privileges and login credentials
 */
public class MainManager extends Manager {
    private String userName;
    private String password;
    
    /**
     * Constructor for MainManager
     * @param id Manager's ID
     * @param firstName Manager's first name
     * @param lastName Manager's last name
     * @param phone Manager's phone number
     * @param address Manager's address
     * @param userName Login username
     * @param password Login password
     */
    public MainManager(String id, String firstName, String lastName, String phone, 
                      String address, String userName, String password) {
        super(id, firstName, lastName, phone, address);
        this.userName = userName;
        this.password = password;
    }
    
    /**
     * Get the username
     * @return username
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Set the username
     * @param userName new username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Get the password
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Set the password
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Verify login credentials
     * @param userName provided username
     * @param password provided password
     * @return true if credentials match, false otherwise
     */
    public boolean verifyCredentials(String userName, String password) {
        return this.userName.equals(userName) && this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return String.format("Main Manager - %s, Username: %s, Manages %d taxis", 
                           super.toString().replace("Manager - ", ""), 
                           userName, getTaxis().size());
    }
}