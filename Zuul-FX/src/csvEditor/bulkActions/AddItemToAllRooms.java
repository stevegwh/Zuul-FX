package csvEditor.bulkActions;

import java.io.IOException;

import csvEditor.BulkAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Adds an item to every row of the CSV data. This does not write directly to
 * the CSV file itself rather the matrix representation of the CSV file that was
 * loaded earlier.
 * 
 * @author Steve
 *
 */
public class AddItemToAllRooms implements BulkAction {
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
