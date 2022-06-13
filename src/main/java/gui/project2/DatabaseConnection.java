/*
    Name: Joshua Samontanez
    Course: CNT 4714 Summer 2022
    Assignment title: Project 2 – A Two-tier Client-Server Application
    Date: June 26, 2022
    Class: C001
*/

package gui.project2;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.Objects;

public class DatabaseConnection {
    public Connection databaseLink;

    // Method that will try to connect to a server, returns the data type "Connection"
    public Connection getConnection(String user, String pass, String fileProperty) {
        String databaseName = "project2";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        boolean connection = false;
        DatabaseConnection dc = new DatabaseConnection();

        // If the user choose to connect to the root server, validate the user and password
        if (Objects.equals(fileProperty, "root.properties"))
            connection = dc.connectToRoot(user, pass);

        // If the user choose to connect to the client server, validate the user and password
        else if (Objects.equals(fileProperty, "client.properties"))
            connection = dc.connectToClient(user, pass);

        // If the connection is valid
        if (connection) {
            // Try to connect to the database using the username, password and url provided by the user
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                databaseLink = DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If the username or password is incorrect, show an error
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account not found!");
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }

        return databaseLink;
    }

    public Connection connectOperationsLog(String user, String pass) {
        String databaseOperationsLog = "operationslog";
        String url = "jdbc:mysql://localhost:3306/" + databaseOperationsLog;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }

    // Validate the username and password for root server
    public boolean connectToRoot(String user, String pass) {
        return Objects.equals(user, "root") && Objects.equals(pass, "Owaako29!");
    }

    // Validate the username and password for client server
    public boolean connectToClient(String user, String pass) {
        return Objects.equals(user, "client") && Objects.equals(pass, "client");
    }
}
