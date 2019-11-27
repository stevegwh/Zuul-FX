package csvEditor;

import javafx.fxml.FXML;
import csvLoader.headers.HeaderEnum;
import csvLoader.headers.ItemNameHeader;
import csvLoader.headers.ItemWeightHeader;
import javafx.collections.ObservableList;
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
		ItemNameHeader itemNameHeader = new ItemNameHeader();
		ItemWeightHeader itemWeightHeader = new ItemWeightHeader();
		// Use the CSV editor's tooltip generator to validate the input the user has
		// entered. validateFieldText returns null if no error found.
		boolean validWeightValue = itemWeightHeader.validateFieldText(itemWeight.getText()) == null;
		boolean validNameValue = itemNameHeader.validateFieldText(itemName.getText()) == null;

		if (itemName.getText() != null && itemWeight.getText() != null && validNameValue && validWeightValue) {

			ObservableList<ObservableList<CSVEditorCell>> rooms = EditCSVController.getRooms();
			for (ObservableList<CSVEditorCell> room : rooms) {
				// Continue if room doesn't have exits.
				if (room.stream().filter(e -> e.header.getEnum().equals(HeaderEnum.DIRECTION))
						.allMatch(e -> e.getProperty().getValue().equals("null"))) {
					continue;
				}
				// Adds item to all room with exits.
				room.get(room.size() - 2).getProperty().setValue(itemName.getText());
				room.get(room.size() - 1).getProperty().setValue(itemWeight.getText());
			}
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setContentText("Added item to all rooms with exits.");
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
