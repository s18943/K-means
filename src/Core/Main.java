package Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gui/scene.fxml"));
        Scene scene = new Scene(root, 720, 536);
        String css = this.getClass().getResource("../gui/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setTitle("k-NN");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
