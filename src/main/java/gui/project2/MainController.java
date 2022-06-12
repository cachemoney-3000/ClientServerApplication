package gui.project2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Statement statement;


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
    private TableView<?> table;

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

        try {
            ResultSet output = statement.executeQuery(connectQuery);
            
            while (output.next()) {
                System.out.println(output.getString("bikename"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void setStatement (Statement statement) {
        this.statement = statement;
    }
}
