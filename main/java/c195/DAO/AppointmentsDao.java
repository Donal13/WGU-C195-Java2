package c195.DAO;

import c195.Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * This abstract class serves as a data access object (DAO) for managing appointments in a database.
 * It provides various static methods to perform CRUD (Create, Read, Update, Delete) operations and queries related to appointments.
 */
public abstract class AppointmentsDao {

    /**
     * Checks for appointments starting within the next 15 minutes and generates an alert message.
     * The message is localized based on the default locale settings, either English or French.
     * @return A message indicating the upcoming appointment details or a message stating no upcoming appointments.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static String appointmentAlert() throws SQLException {

        String sql = "SELECT * FROM appointments WHERE TIMESTAMP(Start) BETWEEN ? AND ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setObject(1, Instant.now());
        statement.setObject(2, Instant.now().plus(15, ChronoUnit.MINUTES));
        ResultSet set = statement.executeQuery();
        if (set.next()) {
            int appointmentId = set.getInt("Appointment_ID");
            String start = set.getString("Start");
            if (Locale.getDefault().getLanguage().equals("fr")) {
                ResourceBundle bundle = ResourceBundle.getBundle("Lang", Locale.getDefault());
                String result = String.format((bundle.getString("yesAppointment")) + ":\n" + (bundle.getString("appointmentID")) + ": " + appointmentId + "\n" + (bundle.getString("date&time")) + ": " + start);
                return result;
            } else {
                String result = String.format("There is an appointment starting soon:\nAppointment ID: " + appointmentId + "\nDate & Time: " + start);
                return result;
            }
        } else {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                ResourceBundle bundle = ResourceBundle.getBundle("Lang", Locale.getDefault());
                String result = String.format(bundle.getString("noAppointment"));
                return result;
            } else {
                String result = String.format("There are no pending appointments");
                return result;
            }
        }
    }

    private static ObservableList<Appointments> appointmentObservableList = FXCollections.observableArrayList();

    /**
     * Retrieves all appointments from the database and populates them into an observable list.
     * @return An ObservableList containing all appointments.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static ObservableList<Appointments> selectAllAppointments() throws SQLException {
        appointmentObservableList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }
        return appointmentObservableList;
    }

    /**
     * Retrieves all appointments for a specific customer from the database and populates them into an observable list.
     * @param custId The customer ID to filter appointments by.
     * @return An ObservableList containing appointments for the specified customer.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static ObservableList<Appointments> selectAppsByCust(int custId) throws SQLException {
        appointmentObservableList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, custId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int userId = result.getInt("User_ID");
            int contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }
        return appointmentObservableList;
    }

    /**
     * Retrieves all appointments associated with a specific contact from the database and populates them into an observable list.
     * @param contId The contact ID to filter appointments by.
     * @return An ObservableList containing appointments associated with the specified contact.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static ObservableList<Appointments> selectAppsByCont(int contId) throws SQLException {
        appointmentObservableList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setInt(1, contId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }
        return appointmentObservableList;
    }

    /**
     * Retrieves all appointments for the current month from the database and populates them into an observable list.
     * @return An ObservableList containing appointments for the current month.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static ObservableList<Appointments> selectAppsByMonth() throws SQLException {
        appointmentObservableList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(NOW())";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while(result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }

        return appointmentObservableList;
    }

    /**
     * Retrieves all appointments for the current week from the database and populates them into an observable list.
     * @return An ObservableList containing appointments for the current week.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static ObservableList<Appointments> selectAppsByWeek() throws SQLException {
        appointmentObservableList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE WEEK(Start) = WEEK(NOW())";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        while(result.next()) {
            int appId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDateTime begin = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contId = result.getInt("Contact_ID");

            appointmentObservableList.add(new Appointments(appId, title, description, location, type, begin, end, custId, userId, contId));
        }

        return appointmentObservableList;
    }

    /**
     * Adds a new appointment to the database.
     * @param title The title of the appointment.
     * @param description The description of the appointment.
     * @param location The location of the appointment.
     * @param type The type of the appointment.
     * @param begin The start time and date of the appointment.
     * @param end The end time and date of the appointment.
     * @param custId The customer ID associated with the appointment.
     * @param userId The user ID who created the appointment.
     * @param contName The contact Name associated with the appointment.
     * @return The number of rows effected by the insert operation.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int AddAppointment(String title, String description, String location, String type, LocalDateTime begin, LocalDateTime end, int custId, int userId, int contName) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(null,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, description);
        statement.setString(3, location);
        statement.setString(4, type);
        statement.setTimestamp(5, Timestamp.valueOf(begin));
        statement.setTimestamp(6, Timestamp.valueOf(end));
        statement.setInt(7, custId);
        statement.setInt(8, userId);
        statement.setInt(9, contName);
        int effectedRows = statement.executeUpdate();
        return effectedRows;
    }

    /**
     * Modifies an existing appointment in the database.
     * @param appId The ID of the appointment to modify.
     * @param title The new title of the appointment.
     * @param description The new description of the appointment.
     * @param location The new location of the appointment.
     * @param type The new type of the appointment.
     * @param begin The new start time and date of the appointment.
     * @param end The new end time and date of the appointment.
     * @param custId The customer ID associated with the appointment.
     * @param userId The user ID who created the appointment.
     * @param contId The contact ID associated with the appointment.
     * @return The number of rows effected by the update operation.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int ModifyAppointment(int appId, String title, String description, String location, String type, LocalDateTime begin, LocalDateTime end, int custId, int userId, int contId) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, description);
        statement.setString(3, location);
        statement.setString(4, type);
        statement.setTimestamp(5, Timestamp.valueOf(begin));
        statement.setTimestamp(6, Timestamp.valueOf(end));
        statement.setInt(7, custId);
        statement.setInt(8, userId);
        statement.setInt(9, contId);
        statement.setInt(10, appId);
        int effectedRows = statement.executeUpdate();
        return effectedRows;
    }

    /**
     * Deletes an appointment from the database based on the appointment ID.
     * @param appId The ID of the appointment to delete.
     * @return The number of rows effected by the delete operation, or 0 if an error occurs.
     */
    public static int DeleteAppointment(int appId) {
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, appId);
            int effectedRows = statement.executeUpdate();
            return effectedRows;
        } catch (SQLException e) {
            System.out.println("SQL Error");
            return 0;
        }
    }

    /**
     * Retrieves a distinct list of appointment types for a given month.
     * @param month The month for which to retrieve appointment types.
     * @return A List containing distinct appointment types for the specified month.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static List<String> type(String month) throws SQLException {
        List<String> types = new ArrayList<String>();
        String sql = "SELECT Type FROM appointments WHERE MONTHNAME(Start) = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, month);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            types.add(result.getString("Type"));
        }

        Set<String> strings = new HashSet<>(types);
        types.clear();
        types.addAll(strings);
        return types;
    }

    /**
     * Counts the number of appointments for a given month and type.
     * @param month The month to filter appointments by.
     * @param type The type of appointments to count.
     * @return The total number of appointments matching the specified month and type.
     * @throws SQLException If there is an issue executing the SQL query.
     */
    public static int appsByMonthType(String month, String type) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE MONTHNAME(Start) = ? AND Type = ?";
        PreparedStatement statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, month);
        statement.setString(2, type);
        ResultSet result = statement.executeQuery();

        int total = 0;
        while(result.next()) {
            total = total + 1;
        }
        return total;
    }

}