package view;

import javafx.fxml.FXML;
import zuul.GameController;
import zuul.TakeableItem;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddItemController {
	@FXML
	public void addItem(ActionEvent event) {
		TakeableItem item = new TakeableItem("sword", 5);
		GameController.getAllRoomDataController().addItemToAllRooms(item);
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
