/*
    Name: Joshua Samontanez
    Course: CNT 4714 Summer 2022
    Assignment title: Project 2 – A Two-tier Client-Server Application
    Date: June 26, 2022
    Class: C001
*/

package gui.project2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextArea commandArea;

    @FXML
    private Button clearResults;

    @FXML
    private Button clearButton;

    @FXML
    private HBox glow;

    @FXML
    private Text command;

    @FXML
    private Text showConnection;

    @FXML
    private TableView table;

    private Statement statement;

    private String propertyType;

    private Connection operationsDB;

    private Statement secondStatement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void clearCommands(ActionEvent event) {
        // Clears the command text area
        if (event.getSource().equals(clearButton))
            commandArea.clear();
    }

    @FXML
    void clearResults(ActionEvent event) {
        if (event.getSource().equals(clearResults)){
            // Clear all the items on the table
            table.getColumns().clear();
            table.getItems().clear();
            // Update the command indicator
            command.setText("Results cleared");
            glow.setStyle("-fx-background-color: #4b5864; -fx-background-radius: 10");
        }

    }

    @FXML
    void executeCommands(ActionEvent event) {
        // Get the query command from the textfield
        String connectQuery = commandArea.getText();

        // Get the first word in the string
        String[] arr = connectQuery.split(" ", 2);
        String firstWord = arr[0].toLowerCase();

        ExecuteCommand execute = new ExecuteCommand();

        try {
            // Client server can only do SELECT operations
            if (Objects.equals(propertyType, "client.properties"))
                clientServer(firstWord, connectQuery);

            // Root server can do SELECT and UPDATE operations
            else if (Objects.equals(propertyType, "root.properties"))
                rootServer(firstWord, connectQuery);




        } catch (SQLException e) {
            // Show the error command and update the command indicator
            command.setText("Command was unsuccessful");
            glow.setStyle("-fx-background-color:  #644b4c; -fx-background-radius: 10");
            execute.showAlert(e, true, "");
        }
    }

    @FXML
    void logout(ActionEvent event) throws SQLException {
        // Get the current stage
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        try {
            // Hide the current stage
            stage.hide();

            // Get the login scene ready
            FXMLLoader fxmlLoader = new FXMLLoader(MainLauncher.class.getResource("gui-view.fxml"));
            Parent root = fxmlLoader.load();
            LoginController lc = fxmlLoader.getController();

            // Close the database connection
            lc.logout();

            // Go back to the login page
            Stage newStage = new Stage();
            newStage.setTitle("SQL Client-Server Application");
            newStage.setResizable(false);
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void clientServer(String firstWord, String connectQuery) throws SQLException {
        ObservableList<ObservableList> data;
        ExecuteCommand execute = new ExecuteCommand();

        // Check if the first word of the query is a "select" operation
        if (Objects.equals(firstWord, "select")) {
            // Execute the query
            ResultSet output = statement.executeQuery(connectQuery);
            ResultSetMetaData metaData = output.getMetaData();
            data = execute.showQuery(output, metaData, table);

            // Show it to the table
            table.setItems(data);

            // Update the command indicator
            command.setText("Command was executed successfully");
            glow.setStyle("-fx-background-color:  #4c644b; -fx-background-radius: 10");

            // Update the logs
            updateLogs(false);
        }

        else {
            // Update the command indicator
            command.setText("Command not allowed");
            glow.setStyle("-fx-background-color:  #644b4c; -fx-background-radius: 10");
            execute.showAlert(null, false, firstWord);
        }
    }

    // If changeType = false, update the num_queries. If changeType = true, update the num_updates
    private void updateLogs(boolean changeType) throws SQLException {
        String commandQuery = "update operationscount set num_queries = num_queries + 1;";
        String commandUpdate = "update operationscount set num_updates = num_updates + 1;";

        if (changeType) {
            secondStatement.executeUpdate(commandUpdate);
        }
        else {
            secondStatement.executeUpdate(commandQuery);
        }
    }

    private void rootServer(String firstWord, String connectQuery) throws SQLException {
        ExecuteCommand execute = new ExecuteCommand();
        ObservableList<ObservableList> data;

        // Check if the first word of the query is a "select" operation
        if ("select".equals(firstWord)) {
            // Execute the query
            ResultSet output = statement.executeQuery(connectQuery);
            ResultSetMetaData metaData = output.getMetaData();
            data = execute.showQuery(output, metaData, table);

            // Show the query result to the table
            table.setItems(data);

            // Update the command indicator
            command.setText("Command was executed successfully");
            glow.setStyle("-fx-background-color:  #4c644b; -fx-background-radius: 10");

            // Update the logs
            updateLogs(false);
        }

        else {
            // Execute the update query
            int count = statement.executeUpdate(connectQuery);
            // Update the command indicator
            command.setText("Command executed successfully. There are " + count + " record(s) updated");
            glow.setStyle("-fx-background-color:  #4c644b; -fx-background-radius: 10");

            // Update the logs
            updateLogs(true);
        }
    }

    public void setText(String text) {
        showConnection.setText(text);
    }

    public void setStatement (Statement statement, String propertyType, Connection operationsDB) throws SQLException {
        this.statement = statement;
        this.propertyType = propertyType;
        this.operationsDB = operationsDB;

        if (this.operationsDB != null) {
            this.secondStatement = MainController.this.operationsDB.createStatement();
        }

    }
}
