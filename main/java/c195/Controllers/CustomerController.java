package c195.Controllers;

import c195.DAO.AppointmentsDao;
import c195.DAO.CustomersDao;
import c195.Helper.GenericInterface;
import c195.Model.Appointments;
import c195.Model.Customers;
import javafx.event.ActionEvent;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Handles displaying a list of customers in a table view, and provides functionalities to add, modify, and delete customers.
 * Additionally, it supports navigation to appointments and reports related to the customers.
 */
public class CustomerController implements Initializable {
    public TableView<Customers> customerTable;
    public TableColumn<Customers, Integer> custIdColumn;
    public TableColumn<Customers, String> custNameColumn;
    public TableColumn<Customers, String> addressColumn;
    public TableColumn<Customers, Integer> postCodeColumn;
    public TableColumn<Customers, Integer> phoneNumberColumn;
    public TableColumn<Customers, Integer> divIdColumn;
    public Button addCustBtn;
    public Button modCustBtn;
    public Button delCustBtn;
    public Button appointmentsBtn;
    public Button reportsBtn;
    public Button exitBtn;

    /**
     * This method sets up the table columns and loads the customers into the table view.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customerTable.setItems(CustomersDao.allCustomers());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("custName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        divIdColumn.setCellValueFactory(new PropertyValueFactory<>("divId"));
    }

    /**
     * Navigates the user to the addCustomer view.
     * @param event The action event triggered by clicking the Add Customer button.
     * @throws IOException If the FXML file for the Add Customer view cannot be loaded.
     */
    public void OnAddCustBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addCustomer-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates the user to the Modify Customer view with a customer's data loaded for editing.
     * @param event The action event triggered by clicking the Modify Customer button.
     */
    public void OnModCustBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("modCustomer-view.fxml"));
            loader.load();

            ModCustomerController mod = loader.getController();
            mod.custFormInfo(customerTable.getSelectionModel().getSelectedItem());

            Parent scene = loader.getRoot();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException | IOException | SQLException ignored){
            System.out.println("Error.");
        }
    }

    /**
     * Confirms the deletion with the user and deletes the selected customer and any associated appointments.
     * @param event The action event triggered by clicking the Delete Customer button.
     */
    public void OnDelCustBtn(ActionEvent event) {
        try {
            Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a customer to delete.");
                alert.showAndWait();
                return;
            }

            int custId = selectedCustomer.getCustId();

            List<Appointments> customerAppointments = AppointmentsDao.selectAppsByCust(custId);
            if (!customerAppointments.isEmpty()) {
                Alert appointmentAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "This customer has " + customerAppointments.size() +
                                " appointments. All customer's appointments must be deleted before deleting the customer. Proceed?");
                appointmentAlert.setHeaderText("Delete All Appointments?");
                appointmentAlert.setTitle("Confirm Appointment Deletion");

                Optional<ButtonType> appointmentResult = appointmentAlert.showAndWait();
                if (appointmentResult.isPresent() && appointmentResult.get() == ButtonType.OK) {
                    for (Appointments appointment : customerAppointments) {
                        AppointmentsDao.DeleteAppointment(appointment.getAppId());
                    }
                } else {
                    return;
                }
            }

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
            confirmDialog.setHeaderText(null);
            confirmDialog.setTitle("Confirm Customer Deletion");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int effectedRows = CustomersDao.DeleteCustomer(custId);
                customerTable.setItems(CustomersDao.allCustomers());
                if (effectedRows > 0) {
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Customer and all associated appointments deleted successfully.");
                    infoAlert.setHeaderText(null);
                    infoAlert.showAndWait();
                } else {
                    System.out.println("No customer was deleted.");
                }
            }
        } catch (NullPointerException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates the user to the appointments view.
     * @param event The event triggered by clicking the Appointments button.
     * @throws IOException If an I/O error occurs during view loading.
     */
    public void OnAppointmentsBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("appointment-view.fxml")));
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
     * Closes the current stage, effectively exiting the program.
     * @param event The event triggered by clicking the Exit button.
     */
    public void OnExitBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
