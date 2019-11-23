package csvEditor.bulkActions;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddItemToAllRooms {
	private MenuItem menuItem;

	public MenuItem getMenuItem() {
		return menuItem;
	}

	private void execute() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addItemDialog.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent, 300, 200);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public AddItemToAllRooms() {
		menuItem = new MenuItem("Add Item To All Rooms");
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				execute();
			}
			
		});
	}

}
