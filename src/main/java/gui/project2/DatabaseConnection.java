/*
    Name: Joshua Samontanez
    Course: CNT 4714 Summer 2022
    Assignment title: Project 2 – A Two-tier Client-Server Application
    Date: June 26, 2022
    Class: C001
*/

package gui.project2;

import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class DatabaseConnection {
    // Method that will try to connect to a server, returns the data type "Connection"
    public Connection getConnection(String inputUser, String inputPass, String fileProperty) throws IOException {
        DatabaseConnection connect = new DatabaseConnection();
        Utility utils = new Utility();
        Connection databaseLink = null;

        String[] accountInfo = utils.readProperties(fileProperty);
        String databaseName = accountInfo[2];
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        // Validate the input username and password using the properties file
        boolean connected = connect.validateLogin(inputUser, inputPass, accountInfo);

        // If the connected is valid
        if (connected) {
            // Try to connect to the database using the username, password and url provided by the user
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                databaseLink = DriverManager.getConnection(url, inputUser, inputPass);
            } catch (Exception e) {
                // Catch all the exceptions
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

    // Using the root account, connect to the operations log to log the number of queries and updates
    public Connection connectOperationsLog(String user, String pass, String database) {
        Connection databaseLink = null;
        String url = "jdbc:mysql://localhost:3306/" + database;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }

    // Validate the username and password
    public boolean validateLogin(String inputUser, String inputPass, String[] accountInfo) {
        String user = accountInfo[0];
        String pass = accountInfo[1];

        return Objects.equals(inputUser, user) && Objects.equals(inputPass, pass);
    }
}
