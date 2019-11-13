package view;

import javafx.fxml.FXML;

import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddItemController {

	@FXML
	private TextField itemName, itemWeight;

	/*
	 * Adds specified item to each room in the rooms list.
	 */
	@FXML
	public void addItem(ActionEvent event) {

		List<String> toAdd = Arrays.asList(itemName.getText(), itemWeight.getText());
		if (itemName.getText() != null && itemWeight.getText() != null && itemWeight.getText().matches("\\d+")
				&& itemName.getText().matches("\\w+")) {
			EditCSVController.getRooms().forEach(e -> e.addAll(toAdd));
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setContentText("Added item to all rooms.");
			a.show();
			closeStage(event);
		} else {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Item not valid");
			a.show();
		}
	}

	@FXML
	public void closeWindow(ActionEvent event) {
		closeStage(event);
	}

	private void closeStage(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

}
