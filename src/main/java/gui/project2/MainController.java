package gui.project2;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public static Statement statement;
    private static String propertyType;
    private ObservableList<ObservableList> data;


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
    private TableView table;

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

                    // PRINT HEADER FOR THE TABLE
                    rsmd = output.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    // CLEAR ITEMS IN THE TABLE ** might move to another spot
                    table.getColumns().clear();
                    for (int i = 0; i < columnsNumber; i++) {
                        final int finalIdx = i;

                        TableColumn col = new TableColumn(rsmd.getColumnName(i + 1).toUpperCase());
                        col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(finalIdx).toString()));

                        col.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
                        col.setResizable(false);

                        table.getColumns().addAll(col);
                    }


                    data = FXCollections.observableArrayList();
                    while (output.next()) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            row.add(output.getString(i));
                        }
                        System.out.println("Row [1] added " + row);
                        data.add(row);
                    }
                    table.setItems(data);
                    /*
                    // PRINT THE ROWS TO THE TABLE
                    while (output.next()) {
                        ObservableList<String> row = FXCollections.observableArrayList();

                        for (int i = 1; i <= columnsNumber; i++) {
                            String columnValue = output.getString(i);
                            row.add(columnValue);
                        }
                        System.out.println("Row [1] added " + row);
                        data.add(row);
                    }
                    table.setItems(data);

                     */
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
