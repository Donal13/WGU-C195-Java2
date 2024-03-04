package c195.Controllers;

import c195.DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class that launches the JavaFX application.
 * This class extends Application and sets up the primary stage by loading the first FXML view.
 */
public class Main extends Application {

    /**
     * This method initializes the first FXML view, sets the application title, and displays the primary scene.
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Appointment Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main entry point for a JavaFX application.
     * This method opens a connection to the database, launches the JavaFX application, and closes the database connection when the application is closed.
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {

        JDBC.openConnection();

        launch();

        JDBC.closeConnection();

    }
}