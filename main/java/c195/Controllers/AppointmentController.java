package c195.Controllers;

import c195.DAO.AppointmentsDao;
import c195.Helper.GenericInterface;
import c195.Model.Appointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for managing appointments in a table view.
 * This class is responsible for initializing the table with data, handles adding, modifying, or deleting appointments, and filtering them based on different time criteria.
 */
public class AppointmentController implements Initializable {

    public TableView<Appointments> appointmentTable;
    public TableColumn<Appointments, Integer> apptIdColumn;
    public TableColumn<Appointments, String> titleColumn;
    public TableColumn<Appointments, String> descriptionColumn;
    public TableColumn<Appointments, String> locationColumn;
    public TableColumn<Appointments, String> typeColumn;
    public TableColumn<Appointments, Timestamp> startsAtColumn;
    public TableColumn<Appointments, Timestamp> endsAtColumn;
    public TableColumn<Appointments, Integer> custIdColumn;
    public TableColumn<Appointments, Integer> userIdColumn;
    public TableColumn<Appointments, Integer> contactIdColumn;
    public ToggleGroup apptTimeToggle;
    public RadioButton allTimeRadio;
    public RadioButton thisMonthRadio;
    public RadioButton thisWeekRadio;
    public Button addApptBtn;
    public Button modApptBtn;
    public Button delApptBtn;
    public Button customersBtn;
    public Button reportsBtn;
    public Button exitBtn;

    /**
     * This method initializes the table columns and loads the appointments into the table view.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointmentTable.setItems(AppointmentsDao.selectAllAppointments());

            apptIdColumn.setCellValueFactory(new PropertyValueFactory<>("appId"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startsAtColumn.setCellValueFactory(new PropertyValueFactory<>("begin"));
            endsAtColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            custIdColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contId"));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Loads the 'Add Appointment' view.
     * @param event The event that occurred.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public void OnAddApptBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addAppointment-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the 'Modify Appointment' view and passes the selected appointment data.
     * @param event The event that occurred.
     */
    public void OnModApptBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("modAppointment-view.fxml"));
            loader.load();

            ModAppointmentController modify = loader.getController();
            modify.ApptData(appointmentTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (NullPointerException | IOException e){
            System.out.println("Modify Error");
        }
    }

    /**
     * Confirms the deletion with the user and deletes the selected appointment if confirmed.
     * @param event The event that occurred.
     * @throws SQLException If there is a database access error or the delete operation fails.
     */
    public void OnDelApptBtn(ActionEvent event) throws SQLException {
        try {
            int custId = appointmentTable.getSelectionModel().getSelectedItem().getCustId();
            int apptId = appointmentTable.getSelectionModel().getSelectedItem().getAppId();
            String apptType = appointmentTable.getSelectionModel().getSelectedItem().getType();

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            confirmDialog.setHeaderText(null);
            confirmDialog.setTitle("Confirm Deletion");

            Optional<ButtonType> result = confirmDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                AppointmentsDao.DeleteAppointment(apptId);
                appointmentTable.setItems(AppointmentsDao.selectAllAppointments());

                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Appointment ID " + apptId + " of type " + apptType + " deleted successfully.");
                infoAlert.setHeaderText(null);
                infoAlert.showAndWait();
            } else {

            }

        } catch (NullPointerException e) {
            System.out.println("Deletion Error: " + e.getMessage());
        }
    }

    /**
     * Handles the event when the 'All Time' radio button is selected.
     * Loads all appointments into the table view.
     * @param event The event that occurred.
     */
    public void OnAllTimeRadio(ActionEvent event) {
        try {
            appointmentTable.setItems(AppointmentsDao.selectAllAppointments());

            apptIdColumn.setCellValueFactory(new PropertyValueFactory<>("appId"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startsAtColumn.setCellValueFactory(new PropertyValueFactory<>("begin"));
            endsAtColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            custIdColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contId"));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Handles the event when the 'This Month' radio button is selected.
     * Filters and loads appointments that are within the current month.
     * @param event The event that occurred.
     */
    public void OnThisMonthRadio(ActionEvent event) {
        try {
            appointmentTable.setItems(AppointmentsDao.selectAppsByMonth());

            apptIdColumn.setCellValueFactory(new PropertyValueFactory<>("appId"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startsAtColumn.setCellValueFactory(new PropertyValueFactory<>("begin"));
            endsAtColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            custIdColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contId"));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Handles the event when the 'This Week' radio button is selected.
     * Filters and loads appointments that are within the current week.
     * @param event The event that occurred.
     */
    public void OnThisWeekRadio(ActionEvent event) {
        try {
            appointmentTable.setItems(AppointmentsDao.selectAppsByWeek());

            apptIdColumn.setCellValueFactory(new PropertyValueFactory<>("appId"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startsAtColumn.setCellValueFactory(new PropertyValueFactory<>("begin"));
            endsAtColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            custIdColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contId"));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Navigates the user to the customers view.
     * @param event The event triggered by clicking the Customers button.
     * @throws IOException If an I/O error occurs during view loading.
     */
    public void OnCustomersBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates the user to the reports view.
     * @param event The event triggered by clicking the Reports button.
     * @throws IOException If an I/O error occurs during view loading.
     */
    public void OnReportsBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("report-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the current stage, effectively exiting the Appointments view.
     * @param event The event triggered by clicking the Exit button.
     */
    @FXML
    public void OnExitBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
