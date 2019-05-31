package ndsf.srun;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Interface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("srun.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("srun.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Srun");
        primaryStage.setScene(new Scene(root, 335, 600));

        // TODO load settings

        Controller controller = loader.getController();
        controller.init();

        primaryStage.show();
    }
}