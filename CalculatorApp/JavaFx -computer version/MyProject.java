import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyProject extends Application {

	public void start(Stage stage) throws Exception {
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("MyProject.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("The Calculator");
		stage.setScene(scene);
		stage.setWidth(330);
		stage.setHeight(440);
		stage.show();

		stage.setMinWidth(300);
		stage.setMinHeight(300);
	}

	public static void main(String[] args) {
		launch(args);
		System.out.println();
	}
}
