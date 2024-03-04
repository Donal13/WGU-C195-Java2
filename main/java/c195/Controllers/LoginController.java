package c195.Controllers;

import c195.DAO.AppointmentsDao;
import c195.DAO.UsersDao;
import c195.Helper.DateTimeFormat;
import c195.Model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static java.lang.String.*;

/**
 * Controller for the login view.
 * Handles user authentication, internationalization (French), and navigation to the appointment view upon successful login.
 */
public class LoginController implements Initializable {
    @FXML
    public TextField UserNameTxt;
    @FXML
    public TextField PasswordTxt;
    @FXML
    public Label UserNameLbl;
    @FXML
    public Label PasswordLbl;
    @FXML
    public Label LoginPleaseLbl;
    @FXML
    public Label LoginTitleLbl;
    @FXML
    public Button LoginSubmitBtn;
    @FXML
    public Button LoginCancelBtn;
    @FXML
    public Label LocationLbl;
    @FXML
    public TextField LocationTxt;
    @FXML
    public String AlertTitle;
    @FXML
    public String AlertMessage;

    /**
     * Handles the login submission.
     * Validates user credentials and navigates to the appointment view if successful.
     * Logs login attempts and displays appropriate alerts for login failures or pending appointments.
     * @param actionEvent The event triggered by clicking the login submit button.
     * @throws Exception If there is an error during login validation.
     */
    @FXML
    public void OnLoginSubmitBtn(ActionEvent actionEvent) throws Exception {
        try {
            DateTimeFormat.localTime();

            String userNameLog = valueOf(UserNameTxt.getText()); // Assuming 'userName' is your TextField for username
            String passwordLog = valueOf(PasswordTxt.getText()); // Assuming 'password' is your TextField for password

            Users tryLogin = UsersDao.Validation(userNameLog, passwordLog);

            if(tryLogin == null){
                loginTracking("Login Failed");

                if (Locale.getDefault().getLanguage().equals("fr")) {
                    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.getDefault());

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(rb.getString("warning"));
                    alert.setContentText(rb.getString("warningText"));
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("Invalid Username and / or Password");
                    alert.showAndWait();
                }
            } else {
                loginTracking("Login Successful");

                if (Locale.getDefault().getLanguage().equals("fr")) {
                    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.getDefault());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(rb.getString("PendingAppointment"));
                    alert.setContentText(AppointmentsDao.appointmentAlert());
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pending Appointment");
                    alert.setContentText(AppointmentsDao.appointmentAlert());
                    alert.showAndWait();
                }

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointment-view.fxml")));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException | SQLException e) {
            System.out.println("Error.");
        }
    }

    /**
     * Logs login attempts to a file with a timestamp, username, and the result of the login attempt.
     * @param loginLog The string to log, indicating the outcome of the login attempt.
     * @throws Exception If there is an error writing to the log file.
     */
    private void loginTracking(String loginLog) throws Exception {
        PrintWriter print = new PrintWriter(new FileOutputStream(new File("login_activity.txt"), true));
        print.append("Date and Time of access: ").append(DateTimeFormat.localTime()).append(" - User Name: ").append(UserNameTxt.getText()).append(" - ").append(loginLog).append("\n");
        print.close();
    }

    /**
     * Sets the text elements of the view based on the system's default locale for internationalization.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.getDefault());
        LocationTxt.setText(valueOf(ZoneId.systemDefault()));

        if(Locale.getDefault().getLanguage().equals("fr")) {
            LoginTitleLbl.setText(rb.getString("titleLbl"));
            LoginPleaseLbl.setText(rb.getString("pleaseLbl"));
            UserNameLbl.setText(rb.getString("usernameLbl"));
            PasswordLbl.setText(rb.getString("passwordLbl"));
            LocationLbl.setText(rb.getString("locationLbl"));

            UserNameTxt.setPromptText(rb.getString("usernameTxt"));
            PasswordTxt.setPromptText(rb.getString("passwordTxt"));

            LoginSubmitBtn.setText(rb.getString("submitBtn"));
            LoginCancelBtn.setText(rb.getString("cancelBtn"));

        }
    }

    /**
     * Handles the action of the cancel button.
     * Closes the current stage, effectively exiting the login view.
     * @param event The event triggered by clicking the cancel button.
     */
    @FXML
    public void OnLoginCancelBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}