package gui.project2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ExecuteCommand {
    // Pops up an alert if the SQL syntax is wrong or the "client" tried to edit the database
    public void showAlert(SQLException e, boolean exception, String firstWord) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Syntax not allowed!");

        if (!exception)
            alert.setContentText(firstWord.toUpperCase() + " command not allowed to user \"client@localhost\"");

        else
            alert.setContentText(e.toString());

        alert.showAndWait();
    }

    // Gather all the data stored on the database based on the query result and return it as observable list
    public ObservableList<ObservableList> showQuery(ResultSet output, ResultSetMetaData meta, TableView table) throws SQLException {
        // For every query, clear the table
        table.getColumns().clear();

        // Get how many columns are in the table
        int columnsNumber = meta.getColumnCount();

        // SHOWING THE HEADER FOR EACH COLUMN INTO THE TABLE
        for (int i = 0; i < columnsNumber; i++) {
            final int finalIdx = i;
            // Get the header name for each column
            String colName = meta.getColumnName(i + 1).toUpperCase().replace("_", " ");

            // Insert the header name and show it on the table view
            TableColumn col = new TableColumn(colName);
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>)
                    param -> new SimpleStringProperty(param.getValue().get(finalIdx).toString()));
            table.getColumns().addAll(col);

            // Set the size for each column
            col.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
            col.setResizable(true);
        }

        // SHOWING ALL THE ITEMS INTO THE TABLE
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        // Loop through all the items in the database
        while (output.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            // Loop through each column
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                // Add the items for each row into the observable list
                row.add(output.getString(i));
            }
            data.add(row);
        }

        // Returns all the items from the query result
        return data;
    }
}
