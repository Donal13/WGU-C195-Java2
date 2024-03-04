package c195.DAO;

import c195.Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class serves as a data access object (DAO) for managing customer information in the database.
 * It provides static methods for CRUD (creating, reading, updating, and deleting) customer records, as well as retrieving lists of customers.
 */
public abstract class CustomersDao {

    private static ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();

    /**
     * Retrieves all customer records from the customers table in the database and populates them into an observable list.
     * Each record is encapsulated as a Customers object.
     * @return An ObservableList of Customers objects, each representing a customer record.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static ObservableList<Customers> allCustomers() throws SQLException {
        customersObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int custId = result.getInt("Customer_ID");
            String custName = result.getString("Customer_Name");
            String address = result.getString("Address");
            String zipCode = result.getString("Postal_Code");
            String phoneNum = result.getString("Phone");
            int divId = result.getInt("Division_ID");

            customersObservableList.add(new Customers(custId, custName, address, zipCode, phoneNum, divId));
        }
        return customersObservableList;
    }

    /**
     * Retrieves a list of all customer IDs from the customers table in the database.
     * @return A List of integers, each representing a unique customer ID.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<Integer> custId() throws SQLException {
        List<Integer> custIdList = new ArrayList<Integer>();
        String sql = "SELECT * FROM customers";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            custIdList.add(result.getInt("Customer_ID"));
        }
        return custIdList;
    }

    /**
     * Inserts a new customer record into the customers table in the database.
     * @param custId The ID of the new customer.
     * @param custName The name of the new customer.
     * @param address The address of the new customer.
     * @param zipCode The postal code of the new customer.
     * @param phoneNum The phone number of the new customer.
     * @param divId The division ID of the new customer.
     * @return The number of rows effected by the insert operation, typically 1 if a new record is successfully added.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int AddCustomer(String custId, String custName, String address, String zipCode, String phoneNum, int divId) throws SQLException {
        String sql = "INSERT INTO client_schedule.customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?,?,?,?,?,?)";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, custId);
        statement.setString(2, custName);
        statement.setString(3, address);
        statement.setString(4, zipCode);
        statement.setString(5, phoneNum);
        statement.setInt(6, divId);
        int effectedRows = statement.executeUpdate();
        return effectedRows;
    }

    /**
     * Updates an existing customer record in the customers table in the database based on the provided customer ID.
     * @param custId The ID of the customer to update.
     * @param custName The customer name to update.
     * @param address The address to update.
     * @param zipCode The postal code to update.
     * @param phoneNum The phone number to update.
     * @param divId The division ID to update..
     * @return The number of rows effected by the update operation, typically 1 if the record is successfully updated.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int ModifyCustomer(int custId, String custName, String address, String zipCode, String phoneNum, int divId) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, custName);
        statement.setString(2, address);
        statement.setString(3, zipCode);
        statement.setString(4, phoneNum);
        statement.setInt(5, divId);
        statement.setInt(6, custId);
        int effectedRows = statement.executeUpdate();
        return effectedRows;
    }

    /**
     * Deletes a customer record from the customers table in the database based on the provided customer ID.
     * @param custId The ID of the customer to delete.
     * @return The number of rows effected by the delete operation, typically 1 if the record is successfully deleted.
     */
    public static int DeleteCustomer(int custId) {
        try {
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, custId);
            int effectedRows = statement.executeUpdate();
            System.out.println(effectedRows);
            return effectedRows;
        } catch (SQLException e) {
            System.out.println("SQL Error");
            return 0;
        }
    }

}
