package view;

import IO.IOHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("zuul.fxml"));
		loader.setController(IOHandler.output);
		VBox vbox = loader.<VBox>load();

		Scene scene = new Scene(vbox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
