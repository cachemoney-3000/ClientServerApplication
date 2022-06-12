package gui.project2;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public static Statement statement;
    private static String propertyType;


    @FXML
    private TextArea SQLCommands;

    @FXML
    private Button clearResults_button;

    @FXML
    private Button clear_button;

    @FXML
    private Button execute_button;

    @FXML
    private Button logout_button;

    @FXML
    private Text showConnection;

    @FXML
    private TableView<ObservableList<String>> table;

    @FXML
    void clearCommands(ActionEvent event) {
        SQLCommands.clear();
    }

    @FXML
    void clearResults(ActionEvent event) {

    }

    @FXML
    void executeCommands(ActionEvent event) {
        String connectQuery = SQLCommands.getText();

        // Get the first word in the string
        String[] arr = connectQuery.split(" ", 2);
        String firstWord = arr[0].toLowerCase();

        ResultSet output;
        ResultSetMetaData rsmd;


        try {
            // Client server can only do SELECT operations
            if (Objects.equals(propertyType, "client.properties")){
                if (Objects.equals(firstWord, "select")) {
                    output = statement.executeQuery(connectQuery);

                    rsmd = output.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    table.getColumns().clear();


                    for (int i = 1; i <= columnsNumber; i++) {

                        final int finalIdx = i;
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                                rsmd.getColumnName(i).toUpperCase());

                        column.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
                        column.setResizable(false);

                        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
                        table.getColumns().add(column);
                    }



                    while (output.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) System.out.print(",  ");
                            String columnValue = output.getString(i);
                            System.out.print(columnValue + " " + rsmd.getColumnName(i));
                        }
                        System.out.println("");
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Command not allowed!");

                    alert.setContentText(firstWord.toUpperCase() + " command not allowed to user \"client@localhost\"");
                    alert.showAndWait();
                }

            }

            // Root server can do SELECT and UPDATE operations
            else if (Objects.equals(propertyType, "root.properties")) {
                // SHOW QUERY
                if (firstWord == "select") {
                    output = statement.executeQuery(connectQuery);

                    while (output.next()) {
                        System.out.println(output.getString("bikename"));
                    }
                }

                // UPDATE TABLE
                else {
                    int count = statement.executeUpdate(connectQuery);
                    System.out.println(count + " records updated");
                }
            }


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Syntax not allowed!");

            alert.setContentText(e.toString());
            alert.showAndWait();
            //e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setText(String text) {
        showConnection.setText(text);
    }

    public void setStatement (Statement statement, String propertyType) {
        this.statement = statement;
        this.propertyType = propertyType;
    }
}
