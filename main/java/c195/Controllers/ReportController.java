package c195.Controllers;

import c195.DAO.AppointmentsDao;
import c195.DAO.ContactsDao;
import c195.DAO.CountriesDao;
import c195.DAO.FirstLevelDivisionsDao;
import c195.Model.Appointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Controller class for generating reports.
 * This class handles different report generation.
 * It also allows for navigation back to the appointments and customers views.
 */
public class ReportController implements Initializable {
    public TableView<Appointments> scheduleTableview;
    public TableColumn<Appointments, Integer> apptIdScheduleColumn;
    public TableColumn<Appointments, String> titleScheduleColumn;
    public TableColumn<Appointments, String> typeScheduleColumn;
    public TableColumn<Appointments, String> descriptionScheduleColumn;
    public TableColumn<Appointments, String> startScheduleColumn;
    public TableColumn<Appointments, String> endScheduleColumn;
    public TableColumn<Appointments, Integer> custIdScheduleColumn;
    public ListView<String> divisionList;
    public TextArea trackerTxtArea;
    public ComboBox<Integer> contactCombo;
    public ComboBox<String> countryCombo;
    public ComboBox<String> monthCombo;
    public ComboBox<String> typeCombo;
    public Button reportsApptsBtn;
    public Button reportsCustomersBtn;
    public Button reportsExitBtn;
    public Label totalLbl;
    private final String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    /**
     * Populates the text area with login activity, fills combo boxes with data from the database, and initializes the month combo box with month names.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Scanner scan = new Scanner(new File("login_activity.txt"));
            while (scan.hasNext()) {
                if (scan.hasNextLine()) {
                    trackerTxtArea.appendText(scan.nextLine() + "\n");
                } else {
                    trackerTxtArea.appendText(scan.next() + "");
                }
            }
            countryCombo.getItems().addAll(CountriesDao.countries());
            contactCombo.getItems().addAll(ContactsDao.contId());
            monthCombo.getItems().addAll(months);
        } catch (SQLException | FileNotFoundException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Handles selection changes in the contact combo box.
     * Populates the schedule table view with appointments corresponding to the selected contact.
     * @param event The event triggered by selecting an item in the contact combo box.
     */
    public void OnContactCombo(ActionEvent event) {
        try {
            scheduleTableview.setItems(AppointmentsDao.selectAppsByCont(contactCombo.getValue()));

            apptIdScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("appId"));
            titleScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            typeScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            startScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("begin"));
            endScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            custIdScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Handles selection changes in the country combo box.
     * Populates the division list with first-level divisions corresponding to the selected country.
     * @param event The event triggered by selecting an item in the country combo box.
     * @throws SQLException If a database access error occurs.
     */
    public void OnCountryCombo(ActionEvent event) throws SQLException {
        divisionList.getItems().clear();
        int countryId = CountriesDao.countriesId(countryCombo.getSelectionModel().getSelectedItem());
        divisionList.getItems().addAll(FirstLevelDivisionsDao.firstLvlDivisions(countryId));
    }

    /**
     * Handles selection changes in the month combo box.
     * Clears and populates the type combo box with appointment types for the selected month.
     * @param event The event triggered by selecting an item in the month combo box.
     * @throws SQLException If a database access error occurs.
     */
    public void OnMonthCombo(ActionEvent event) throws SQLException {
        typeCombo.getItems().clear();
        monthCombo.getSelectionModel().getSelectedItem();
        String month = monthCombo.getSelectionModel().getSelectedItem();
        typeCombo.getItems().addAll(AppointmentsDao.type(month));
    }

    /**
     * Handles selection changes in the type combo box.
     * Updates the total label with the count of appointments matching the selected month and type.
     * @param event The event triggered by selecting an item in the type combo box.
     * @throws SQLException If a database access error occurs.
     */
    public void OnTypeCombo(ActionEvent event) throws SQLException {
        typeCombo.getSelectionModel().getSelectedItem();
        String month = monthCombo.getSelectionModel().getSelectedItem();
        String type = typeCombo.getSelectionModel().getSelectedItem();
        totalLbl.setText(String.valueOf(AppointmentsDao.appsByMonthType(month, type)));
    }

    /**
     * Navigates the user to the appointments view.
     * @param event The event triggered by clicking the Appointments button.
     * @throws IOException If an I/O error occurs during view loading.
     */
    public void OnReportsApptsBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointment-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates the user to the customers view.
     * @param event The event triggered by clicking the Customers button.
     * @throws IOException If an I/O error occurs during view loading.
     */
    public void OnReportsCustomersBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the current stage, effectively exiting the reports view.
     * @param event The event triggered by clicking the Reports Exit button.
     */
    public void OnReportsExitBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
