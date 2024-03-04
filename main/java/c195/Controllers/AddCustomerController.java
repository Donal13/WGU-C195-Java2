package c195.Controllers;

import c195.DAO.CountriesDao;
import c195.DAO.CustomersDao;
import c195.DAO.FirstLevelDivisionsDao;
import c195.Helper.GenericInterface;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Handles user inputs from the UI for creating a new customer.
 * It provides functionality to save the new customer to the database or cancel the operation.
 */
public class AddCustomerController implements Initializable {
    public TextField custIdTxt;
    public TextField custFirstNameTxt;
    public TextField custLastNameTxt;
    public TextField addressTxt;
    public TextField postCodeTxt;
    public TextField phoneNumberTxt;
    public ComboBox countryCombo;
    public ComboBox divisionCombo;
    public Button addCustomerCancelBtn;
    public Button addCustomerSaveBtn;

    /**
     * Sets up the country combo box with available countries.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryCombo.getItems().addAll(CountriesDao.countries());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Handles the selection event on the country combo box.
     * It clears and populates the division combo box based on the selected country.
     * @param event The action event triggered by selecting an item in the country combo box.
     * @throws SQLException If there is a database access error while retrieving divisions based on the selected country.
     */
    public void OnCountryCombo(ActionEvent event) throws SQLException {
        divisionCombo.getItems().clear();
        countryCombo.getSelectionModel().getSelectedItem();
        String countryName = String.valueOf(countryCombo.getSelectionModel().getSelectedItem());
        int countryId = CountriesDao.countriesId(countryName);
        divisionCombo.getItems().addAll(FirstLevelDivisionsDao.firstLvlDivisions(countryId));
    }

    /**
     * Validates the user input and saves the new customer to the database if validation passes.
     * Displays error messages for any validation failures.
     * <p><b>
     * The lambda expression is used here to streamline the display of warning messages for various validation failures.
     * </b></p>
     * @param event The action event triggered by clicking the 'Save' button.
     */
    public void OnAddCustomerSaveBtn(ActionEvent event) {
        try {
            // Lambda expression to display a warning alert with a custom message
            GenericInterface errorMessage = (message) -> new Alert(Alert.AlertType.WARNING, message).showAndWait();

            if (custFirstNameTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid first name.");
            } else if (custLastNameTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid last name.");
            } else if (addressTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid address.");
            } else if (postCodeTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter a valid postal code.");
            } else if (phoneNumberTxt.getText().isBlank()) {
                errorMessage.performAction("Please enter the customer's phone number.");
            } else if (countryCombo.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select the customer's country.");
            } else if (divisionCombo.getSelectionModel().isEmpty()) {
                errorMessage.performAction("Please select the customer's state or province.");
            } else {
                String custId = null;
                String custName = custFirstNameTxt.getText() + " " + custLastNameTxt.getText();
                String address = addressTxt.getText();
                String postCode = postCodeTxt.getText();
                String phoneNum = phoneNumberTxt.getText();
                int divId = FirstLevelDivisionsDao.firstDivisionId(String.valueOf(divisionCombo.getValue()));

                CustomersDao.AddCustomer(null, custName, address, postCode, phoneNum, divId);

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException | IOException exception) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Prompts the user to confirm cancellation.
     * If confirmed, discards any input and returns to the main customer view without saving the new customer.
     * @param event The action event triggered.
     * @throws IOException If there is an I/O error.
     */
    public void OnAddCustomerCancelBtn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "New customer will not be saved to the database.");
        Optional<ButtonType> canceling = alert.showAndWait();
        if (canceling.isPresent() && canceling.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

}
