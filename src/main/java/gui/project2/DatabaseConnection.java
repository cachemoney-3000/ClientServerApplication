/*
    Name: Joshua Samontanez
    Course: CNT 4714 Summer 2022
    Assignment title: Project 2 – A Two-tier Client-Server Application
    Date: June 26, 2022
    Class: C001
*/

package gui.project2;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class DatabaseConnection {
    // Method that will try to connect to a server, returns the data type "Connection"
    public Connection getConnection(String inputUser, String inputPass, String fileProperty) {
        DatabaseConnection connect = new DatabaseConnection();
        Utility utils = new Utility();
        Connection databaseLink = null;

        String[] properties = utils.readProperties(fileProperty);
        String url = properties[2];
        String driver = properties[3];

        // Validate the input username and password using the properties file
        boolean valid = connect.validateLogin(inputUser, inputPass, properties);

        // If the username and password are valid
        if (valid) {
            // Try to connect to the database using the username, password and url provided by the user
            try {
                Class.forName(driver);
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
    public Connection connectOperationsLog(String[] properties) {
        Connection databaseLink = null;
        String user = properties[0];
        String pass = properties[1];
        String url = properties[2];
        String driver = properties[3];

        try {
            Class.forName(driver);
            databaseLink = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }

    // Validate the username and password
    public boolean validateLogin(String inputUser, String inputPass, String[] properties) {
        String user = properties[0];
        String pass = properties[1];

        return Objects.equals(inputUser, user) && Objects.equals(inputPass, pass);
    }
}
