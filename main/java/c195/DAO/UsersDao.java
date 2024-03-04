package c195.DAO;

import c195.Model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class serves as a data access object (DAO) for managing user data in a database.
 * It provides static methods for user authentication and retrieving user IDs.
 */
public abstract class UsersDao {

    /**
     * Validates a user's login credentials against the users table in the database.
     * Messages indicating the success or failure of the login attempt are printed to the console.
     * @param userName The username provided for login.
     * @param password The password provided for login.
     * @return A Users object representing the authenticated user if login is successful.
     */
    public static Users Validation(String userName, String password){
        try{
            String sql = "SELECT User_Name, Password FROM client_schedule.users WHERE User_Name = ? AND Password = ?";
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                Users current = new Users(result.getString("User_Name"));
                System.out.println("Successful Login!");
                return current;
            } else{
                System.out.println("Failed Login!");
                return null;
            }
        } catch (SQLException throwable){
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a list of all user IDs from the users table in the database.
     * @return A List unique user ID from the database.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Integer> userId() throws SQLException {
        List<Integer> listOfUserIds = new ArrayList<Integer>();
        String sql = "SELECT * FROM users";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            listOfUserIds.add(result.getInt("User_ID"));
        }
        return listOfUserIds;
    }

}