package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        String css = this.getClass().getResource("style.css").toExternalForm();
        primaryStage.setTitle("McQueen Tabular Method");
        primaryStage.setScene(new Scene(root, 653, 400));
        root.getStylesheets().add(css);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
