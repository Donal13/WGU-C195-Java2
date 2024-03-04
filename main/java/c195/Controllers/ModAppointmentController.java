package c195.Controllers;

import c195.DAO.AppointmentsDao;
import c195.DAO.ContactsDao;
import c195.DAO.CustomersDao;
import c195.DAO.UsersDao;
import c195.Helper.DateTimeFormat;
import c195.Helper.GenericInterface;
import c195.Model.Appointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class allows for editing appointment details such as title, description, location, type, start and end times, and IDs for customer, user, and contact.
 * It handles loading existing appointment data into the form, validating user inputs, and saving the updated appointment details to the database.
 */
public class ModAppointmentController implements Initializable {

    public TextField apptIdTxt;
    public TextField titleTxt;
    public TextField descriptionTxt;
    public TextField locationTxt;
    public ComboBox<Integer> contIdCombo;
    public TextField typeTxt;
    public DatePicker startDatePicker;
    public ChoiceBox<LocalTime> startTimeChoice;
    public DatePicker endDatePicker;
    public ChoiceBox<LocalTime> endTimeChoice;
    public ComboBox<Integer> custIdCombo;
    public ComboBox<Integer> userIdCombo;
    private Appointments appointmentSelected = null;

    /**
     * Sets up the time choice boxes with appropriate times and populates the user, customer, and contact ID combo boxes.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            LocalTime easternStartHr = DateTimeFormat.easternTime().toLocalTime();
            LocalTime easternEndHr = DateTimeFormat.easternTime().toLocalTime().plusHours(14);

            while(easternStartHr.isBefore(easternEndHr.plusSeconds(1))) {
                startTimeChoice.getItems().add(easternStartHr);
                endTimeChoice.getItems().add(easternStartHr);
                easternStartHr = easternStartHr.plusMinutes(15);
            }
            userIdCombo.getItems().addAll(UsersDao.userId());
            custIdCombo.getItems().addAll(CustomersDao.custId());
            contIdCombo.getItems().addAll(ContactsDao.contId());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Loads the selected appointment's data into the UI components for editing.
     * @param appointments The appointment data to load into the form for editing.
     */
    public void ApptData(Appointments appointments) {
        appointmentSelected = appointments;

        apptIdTxt.setText(String.valueOf(appointmentSelected.getAppId()));
        titleTxt.setText(String.valueOf(appointmentSelected.getTitle()));
        descriptionTxt.setText(String.valueOf(appointmentSelected.getDescription()));
        locationTxt.setText(String.valueOf(appointmentSelected.getLocation()));
        contIdCombo.setValue(appointmentSelected.getContId());
        typeTxt.setText(String.valueOf(appointmentSelected.getType()));
        startDatePicker.setValue(appointmentSelected.getBegin().toLocalDate());
        startTimeChoice.setValue(appointmentSelected.getBegin().toLocalTime());
        endDatePicker.setValue(appointmentSelected.getEnd().toLocalDate());
        endTimeChoice.setValue(appointmentSelected.getEnd().toLocalTime());
        custIdCombo.setValue(appointmentSelected.getCustId());
        userIdCombo.setValue(appointmentSelected.getUserId());
    }

    /**
     * Validates the user input and updates the appointment in the database if validation passes.
     * Displays error messages for any validation failures.
     * <p><b>
     * The lambda expression is used here to streamline the display of warning messages for various validation failures.
     * </b></p>
     * @param event The action event triggered by clicking the 'Save' button.
     */
    public void OnModApptSaveBtn(ActionEvent event) {
        try {
            LocalDateTime beginDateTime = LocalDateTime.of(startDatePicker.getValue(), startTimeChoice.getValue());
            LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTimeChoice.getValue());
            // Lambda expression to display a warning alert with a custom message
            GenericInterface errorMessage = (message) -> new Alert(Alert.AlertType.WARNING, message).showAndWait();

            if (startTimeChoice.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select a valid Start Time.");
            } else if (endTimeChoice.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select a valid End Time.");
            } else if (startDatePicker.getValue() == null) {
                errorMessage.performAction("Please select a valid Start Date.");
            } else if (endDatePicker.getValue() == null) {
                errorMessage.performAction("Please select a valid End Date.");
            } else if (titleTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid Title.");
            } else if (descriptionTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid Description.");
            } else if (locationTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid Location.");
            } else if (typeTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid Type.");
            } else if (userIdCombo.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select a User ID.");
            } else if (contIdCombo.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select a Contact ID.");
            } else if (custIdCombo.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select a Customer ID.");
            } else if (endDateTime.isBefore(beginDateTime)) {
                errorMessage.performAction("Start date must occur before end date.");
            } else if (DateTimeFormat.checkApptOverlap(beginDateTime, endDateTime, custIdCombo.getValue(), Integer.parseInt(apptIdTxt.getText()))) {
                errorMessage.performAction("Overlapping appointment times detected.");
            } else {
                int appId = appointmentSelected.getAppId();
                String title = titleTxt.getText();
                String description = descriptionTxt.getText();
                String location = locationTxt.getText();
                String type = typeTxt.getText();
                int custId = custIdCombo.getValue();
                int userId = userIdCombo.getValue();
                int contId = contIdCombo.getValue();

                AppointmentsDao.ModifyAppointment(appId, title, description, location, type, beginDateTime, endDateTime, custId, userId, contId);

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointment-view.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to confirm cancellation.
     * If confirmed, discards any changes and returns to the main appointment view without saving the updates.
     * @param event The action event triggered by clicking the 'Cancel' button.
     * @throws IOException If there is an I/O error during scene transition.
     */
    public void OnModApptCancelBtn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm cancellation. \nChanges will not be saved to the database.");
        Optional<ButtonType> canceling = alert.showAndWait();
        if (canceling.isPresent() && canceling.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointment-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }


}

