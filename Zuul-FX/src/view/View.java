package view;

import javafx.application.Application;
import javafx.application.Platform;
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
		IOHandler.output.setStage(primaryStage);
		VBox vbox = loader.<VBox>load();

		Scene scene = new Scene(vbox);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e->{
			Platform.exit();
			System.exit(0);
		});
		primaryStage.show();
		IOHandler.output.onLoad();
	}

}
