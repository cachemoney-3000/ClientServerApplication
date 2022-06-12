package gui.project2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection(String user, String pass, String fileProperty) throws IOException {
        String databaseName = "project2";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        boolean connection = false;
        DatabaseConnection dc = new DatabaseConnection();

        if (Objects.equals(fileProperty, "root.properties")) {
            connection = dc.connectToRoot(user, pass);
        }

        else if (Objects.equals(fileProperty, "client.properties")) {
            connection = dc.connectToClient(user, pass);
        }

        if (connection) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                databaseLink = DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account not found!");
            alert.setContentText("Invalid username or password");

            alert.showAndWait();
        }

        return databaseLink;
    }

    public boolean connectToRoot(String user, String pass) {
        return Objects.equals(user, "root") && Objects.equals(pass, "Owaako29!");
    }

    public boolean connectToClient(String user, String pass) {
        return Objects.equals(user, "client") && Objects.equals(pass, "client");
    }
}
