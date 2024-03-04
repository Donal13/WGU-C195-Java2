package c195.Controllers;

import c195.DAO.AppointmentsDao;
import c195.DAO.ContactsDao;
import c195.DAO.CustomersDao;
import c195.DAO.UsersDao;
import c195.Helper.DateTimeFormat;
import c195.Helper.GenericInterface;
import c195.Model.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for adding new appointments.
 * This class handles user inputs from the UI for creating a new appointment.
 * It provides functionality to save the new appointment to the database or cancel the operation.
 */
public class AddAppointmentController implements Initializable {

    public TextField apptIdTxt;
    public TextField titleTxt;
    public TextField descriptionTxt;
    public TextField locationTxt;
    public ComboBox<Contact> contNameCombo;
    public TextField typeTxt;
    public DatePicker startDatePicker;
    public ChoiceBox<LocalTime> startTimeChoice;
    public DatePicker endDatePicker;
    public ChoiceBox<LocalTime> endTimeChoice;
    public ComboBox<Integer> custIdCombo;
    public ComboBox<Integer> userIdCombo;
    public Button addApptSaveBtn;
    public Button addApptCancelBtn;

    /**
     * Sets the default values for date pickers and time choice boxes, populates the user and customer combo boxes, and configures the contactName combo box.
     *<p><b>
     * The lambda expressions used here are used to define how the items of the ListView in the ComboBox are displayed.</b>
     * I used anonymous classes to override the necessary methods, providing custom cell output.
     * This approach was chosen for conciseness and encapsulation, given that the customization is specific to this ComboBox and does not need to be reused elsewhere.
     * </p>
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            startDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(LocalDate.now());

            LocalTime nowTruncated = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
            startTimeChoice.setValue(nowTruncated);
            endTimeChoice.setValue(nowTruncated);

            LocalTime easternStartHr = DateTimeFormat.easternTime().toLocalTime();
            LocalTime easternEndHr = DateTimeFormat.easternTime().toLocalTime().plusHours(14);

            while(easternStartHr.isBefore(easternEndHr.plusSeconds(1))) {
                startTimeChoice.getItems().add(easternStartHr);
                endTimeChoice.getItems().add(easternStartHr);
                easternStartHr = easternStartHr.plusMinutes(15);
            }

            userIdCombo.getItems().addAll(UsersDao.userId());
            custIdCombo.getItems().addAll(CustomersDao.custId());

            for (Contact contact : ContactsDao.contName()) {
                contNameCombo.getItems().add(contact);
            }
            // Set up the cellFactory for the contact name combo box using a lambda expression
            contNameCombo.setCellFactory(lv -> new ListCell<Contact>() {
                @Override
                protected void updateItem(Contact item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : item.getName());
                }
            });
            contNameCombo.setConverter(new StringConverter<Contact>() {
                @Override
                public String toString(Contact contact) {
                    return contact == null ? "" : contact.getName();
                }
                @Override
                public Contact fromString(String string) {
                    return contNameCombo.getItems().stream().filter(item ->
                            item.getName().equals(string)).findFirst().orElse(null);
                }
            });
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Validates the user input and saves the new appointment to the database if validation passes.
     * Displays error messages for any validation failures.
     * <p><b>
     * The lambda expression is used here to streamline the display of warning messages for various validation failures.
     * </b></p>
     * @param event The action event triggered by clicking the 'Save' button.
     * @throws SQLException If there is a database access error.
     * @throws IOException If there is an I/O error.
     */
    public void OnAddApptSaveBtn(ActionEvent event) {
        try {
            LocalDateTime beginDateTime = LocalDateTime.of(startDatePicker.getValue(), startTimeChoice.getValue());
            LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTimeChoice.getValue());
            // Lambda expression to display a warning alert with a custom message
            GenericInterface errorMessage = (message) -> new Alert(Alert.AlertType.WARNING, message).showAndWait();

            if (startTimeChoice.getSelectionModel().isEmpty()){
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
            } else if (contNameCombo.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select a Contact ID.");
            } else if (custIdCombo.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select a Customer ID.");
            } else if (endDateTime.isBefore(beginDateTime)) {
                errorMessage.performAction("Start date and time must occur before end date and time.");
            } else if (DateTimeFormat.checkApptOverlap(beginDateTime, endDateTime, custIdCombo.getValue(), -1)) {
                errorMessage.performAction("Overlapping appointment times detected.");
            } else {
                String appId = null;
                String title = titleTxt.getText();
                String description = descriptionTxt.getText();
                String location = locationTxt.getText();
                String type = typeTxt.getText();
                int custId = custIdCombo.getValue();
                int userId = userIdCombo.getValue();

                Contact selectedContact = contNameCombo.getSelectionModel().getSelectedItem();
                int contId = selectedContact.getId();

                AppointmentsDao.AddAppointment(title, description, location, type, beginDateTime, endDateTime, custId, userId, contId);

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointment-view.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Prompts the user to confirm cancellation. If confirmed, discards any input and returns to the appointment view without saving the new appointment.
     * @param event The action event triggered by clicking the 'Cancel' button.
     * @throws IOException If there is an I/O error during scene transition.
     */
    public void OnAddApptCancelBtn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm cancellation. \nAppointment will not be saved to the database.");
        Optional<ButtonType> saving = alert.showAndWait();
        if (saving.isPresent() && saving.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointment-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

}
