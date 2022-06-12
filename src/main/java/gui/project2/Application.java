package gui.project2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 484, 545);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /*
    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        
        stage.getScene().setRoot(pane);
    }
     */

    public void changeScene() {
        stage.hide();
    }

    public static void main(String[] args) {
        launch();
    }
}