import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CannonGame extends Application {

    @Override
    public void start(Stage stage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("CannonGame.fxml"));

	Scene scene = new Scene(root);
	stage.setScene(scene);
	stage.setTitle("Cannon Game");
	stage.show();
    }

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	launch(args);
    }

}
