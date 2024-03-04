package c195.DAO;

import c195.Model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a data access object (DAO) for managing contact information in a database.
 * It provides static methods to perform operations related to contact entities.
 */
public abstract class ContactsDao {

    /**
     * Retrieves a list of all contact IDs from the contacts table in the database.
     * Queries the database for all rows in the contacts table and extracts the Contact_ID from each row, adding it to a list of integers which is then returned.
     * @return A List of integers where each integer represents a unique contact ID from the database.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Integer> contId() throws SQLException {
        List<Integer> contactsList = new ArrayList<Integer>();
        String sql = "SELECT * FROM contacts";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            contactsList.add(result.getInt("Contact_ID"));
        }
        return contactsList;
    }

    public static List<Contact> contName() throws SQLException {
        List<Contact> contactsList = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int id = result.getInt("Contact_ID");
            String name = result.getString("Contact_Name");
            // Create a new Contact object and add it to the list
            Contact contact = new Contact(id, name);
            contactsList.add(contact);
        }
        return contactsList;
    }
}
