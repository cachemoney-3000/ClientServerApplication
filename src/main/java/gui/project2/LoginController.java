/*
    Name: Joshua Samontanez
    Course: CNT 4714 Summer 2022
    Assignment title: Project 2 – A Two-tier Client-Server Application
    Date: June 26, 2022
    Class: C001
*/

package gui.project2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ComboBox<String> fileSelection;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField userTextField;

    private static Connection connectDB;
    private static Connection operationsDB;

    ObservableList<String> list = FXCollections.observableArrayList("root.properties", "client.properties");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileSelection.setItems(list);
    }

    @FXML
    void connectButton(ActionEvent event) throws IOException {
        // Get the current stage
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();

        // Get the username, password, and file property from the user
        String user = userTextField.getText();
        String pass = passwordTextField.getText();
        String fileProperty = fileSelection.getValue();

        // If any fields were left blank by the user, show an error
        if (Objects.equals(user, "") || Objects.equals(pass, "") || fileProperty == null)
            warningHandler(user, pass, fileProperty);

        else {
            // Connect to the database
            DatabaseConnection connect = new DatabaseConnection();
            Utility utils = new Utility();
            // The getConnection method will check if the username and password were valid
            connectDB = connect.getConnection(user, pass, fileProperty);

            // If the user was able to connect to the database
            if(connectDB != null) {
                try {
                    // Connect to the operations log database using the root account
                    String[] accountInfo = utils.readProperties("operations.properties");
                    String actualUser = accountInfo[0];
                    String actualPass = accountInfo[1];
                    String database = accountInfo[2];
                    operationsDB = connect.connectOperationsLog(actualUser, actualPass, database);

                    // Get the database ready
                    Statement statement = connectDB.createStatement();

                    // Then change the scene from login page to the main page
                    changeScene(currentStage, statement, fileProperty);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void changeScene(Stage currentStage, Statement statement, String fileProperty) {
        try {
            // Hide the current stage
            currentStage.hide();

            // Get the next scene ready
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
            Parent root = fxmlLoader.load();

            MainController mc = fxmlLoader.getController();

            String showText = "Connected to jdbc:mysql://localhost:3306/project2";
            mc.setText(showText);
            mc.setStatement(statement, fileProperty, operationsDB);

            // Show the main landing page
            Stage newStage = new Stage();
            newStage.setTitle("SQL Client-Server Application");
            newStage.setResizable(false);
            newStage.setScene(new Scene(root));
            newStage.show();

        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void logout() throws SQLException {
        // Close the database
        connectDB.close();
        operationsDB.close();
    }

    // Show a warning based on what text fields is empty
    private void warningHandler(String user, String pass, String property) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Account not found!");

        if (Objects.equals(user, ""))
            alert.setContentText("Username field is empty.");

        else if (Objects.equals(pass, ""))
            alert.setContentText("Password field is empty");

        else if (property == null)
            alert.setContentText("Please select a property file");

        alert.showAndWait();
    }
}