module c195.c195project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens c195.DAO to javafx.base;
    opens c195.Model to javafx.base;
    opens c195.Helper to javafx.base;
    opens c195.Controllers to javafx.fxml;
    exports c195.Controllers;
}