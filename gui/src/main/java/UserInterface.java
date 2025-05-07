import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserInterface extends Application {

    @Override
    public void init() throws Exception {
        //System.out.println("Before");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/usage_window.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Übersicht Energieverteilung");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        //System.out.println("After");
    }
}