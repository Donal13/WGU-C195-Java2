package c195.Model;

/**
 * Represents a user with a unique ID and username.
 * This class offers two constructors to create user instances either by user ID or by username.
 */
public class Users {
    private int userId;
    private String userName;

    /**
     * Constructs a Users instance with the specified user ID.
     * @param userId The unique ID for the user.
     */
    public Users(int userId){
        this.userId = userId;
    }

    /**
     * Constructs a Users instance with the specified username.
     * @param userName The username of the user.
     */
    public Users(String userName){
        this.userName = userName;
    }
}
