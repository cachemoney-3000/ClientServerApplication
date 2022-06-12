package gui.project2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ComboBox<String> fileSelection;

    @FXML
    private PasswordField password_textfield;

    @FXML
    private Button signin_button;

    @FXML
    private TextField user_textfield;

    ObservableList<String> list = FXCollections.observableArrayList("root.properties", "client.properties");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileSelection.setItems(list);
    }

    @FXML
    void connectButton(ActionEvent event) throws IOException, SQLException {
        String user = user_textfield.getText();
        String pass = password_textfield.getText();
        String fileProperty = fileSelection.getValue();

        if (Objects.equals(user, "") || Objects.equals(pass, "") || fileProperty == null)
            warningHandler(user, pass, fileProperty);

        else {
            DatabaseConnection connect = new DatabaseConnection();
            Connection connectDB = connect.getConnection(user, pass, fileProperty);

            if(connectDB != null) {
                /*
                Application app = new Application();
                app.changeScene("main-view.fxml");
                 */

                //String connectQuery = "select * from bikes";

                try {
                    Statement statement = connectDB.createStatement();
                    //ResultSet output = statement.executeQuery(connectQuery);

                    try {
                        Application app = new Application();
                        app.changeScene();

                        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
                        Parent root = fxmlLoader.load();

                        MainController mc = fxmlLoader.getController();

                        String showText = "Connected to jdbc:mysql://localhost:3306/project2";
                        mc.setText(showText);
                        mc.setStatement(statement);


                        Stage stage = new Stage();
                        stage.setResizable(false);
                        stage.setScene(new Scene(root));
                        stage.show();

                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*
                    while (output.next()) {
                        System.out.println(output.getString("bikename"));
                    }

                     */
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

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


    @FXML
    void getPassword(ActionEvent event) {

    }

    @FXML
    void getUsername(ActionEvent event) {

    }

    @FXML
    void selectProperties(ActionEvent event) {

    }
}