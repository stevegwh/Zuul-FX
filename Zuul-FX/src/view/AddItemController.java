package view;

import javafx.fxml.FXML;

import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddItemController {

	/*
	 * Adds specified item to each room in the rooms list.
	 */
	@FXML
	public void addItem(ActionEvent event) {
		List<String> toAdd = Arrays.asList( "sword", "5");
		EditCSVController.getRooms().forEach(e->e.addAll(toAdd));
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText("Added item to all rooms.");
		a.show();
		closeStage(event);
	}
    private void closeStage(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
