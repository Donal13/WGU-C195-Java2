package c195.DAO;

import c195.Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class serves as a data access object (DAO) for managing country information in the database.
 * It provides static methods to perform operations related to countries.
 */
public abstract class CountriesDao {

    public static ObservableList<Countries> allCountries() throws SQLException {
        String sql = "SELECT * FROM countries";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        ObservableList<Countries> countryIdAndName = FXCollections.observableArrayList();

        while (result.next()) {
            int countryId = result.getInt("Country_ID");
            String countryName = result.getString("Country");
            countryIdAndName.add(new Countries(countryId, countryName));
        }
        return countryIdAndName;
    }

    /**
     * Retrieves all countries from the 'countries' table in the database and populates them into an observable list.
     * @return An ObservableList of Countries objects, each representing a country with its ID and name.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<String> countries() throws SQLException {
        List<String> countriesList = new ArrayList<String>();
        String sql = "SELECT * FROM countries";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            countriesList.add(result.getString("Country"));
        }
        return countriesList;
    }

    /**
     * Retrieves a list of all country names from the countries table in the database.
     * @return A List of strings, each representing a country name.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static String countriesName(int countryId) throws SQLException {
        String sql = "SELECT Country FROM countries WHERE Country_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, countryId);
        ResultSet result = statement.executeQuery();

        String countryName = null;
        while (result.next()) {
            countryName = result.getString("Country");
        }
        return countryName;
    }

    /**
     * Retrieves the ID of a country based on its name from the countries table in the database.
     * @param countryName The name of the country to be retrieved.
     * @return An integer representing countryID, or 0 if the country name does not exist in the database.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int countriesId(String countryName) throws SQLException {
        String sql = "SELECT Country_ID FROM countries WHERE Country = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, countryName);
        ResultSet result = statement.executeQuery();

        int countryId = 0;
        while (result.next()) {
            countryId = result.getInt("Country_ID");
        }
        return countryId;
    }

}
