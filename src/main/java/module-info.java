module gui.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens gui.project2 to javafx.fxml;
    exports gui.project2;
}