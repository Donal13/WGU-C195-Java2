package c195.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class acts as a data access object (DAO) for managing first-level division data within the database.
 * It provides static methods for querying division names, country IDs associated with divisions, and division names or IDs based on various criteria.
 */
public abstract class FirstLevelDivisionsDao {

    /**
     * Retrieves a list of division names associated with a specific country ID from the first_level_divisions table in the database.
     * @param contId The country ID used to filter division names.
     * @return A List of division names within the specified country.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<String> firstLvlDivisions(int contId) throws SQLException {
        List<String> divNames = new ArrayList<String>();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, contId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            divNames.add(result.getString("Division"));
        }
        return divNames;
    }

    /**
     * Retrieves the country ID associated with a specific division ID from the first_level_divisions table.
     * @param divId The division ID for the country ID to be retrieved.
     * @return An integer representing the country ID associated with the specified division.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int countryIdByDivision(int divId) throws SQLException {
        String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, divId);
        ResultSet result = statement.executeQuery();

        int country = 0;
        while (result.next()) {
            country = result.getInt("Country_ID");
        }
        return country;
    }

    /**
     * Retrieves the name of a division based on its division ID from the first_level_divisions table.
     * @param divId The division ID of the division to be retrieved.
     * @return The name of the division, or null if no division is associated with the ID.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static String firstDivisionName(int divId) throws SQLException {
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, divId);
        ResultSet result = statement.executeQuery();

        String division = null;
        while (result.next()) {
            division = result.getString("Division");
        }
        return division;
    }

    /**
     * Retrieves the division ID associated with a specific division name from the first_level_divisions table.
     * @param divName The name of the division to be retrieved.
     * @return The division ID associated with the specified division name.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int firstDivisionId(String divName) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, divName);
        ResultSet result = statement.executeQuery();

        int divId = 0;
        while (result.next()) {
            divId = result.getInt("Division_ID");
        }
        return divId;
    }

}
