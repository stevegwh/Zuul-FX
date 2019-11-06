package IO;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import zuul.GameController;
import zuul.CommandHandler;

public class FXOutput implements Output {
	private CommandHandler commandHandler;
	private boolean takeClicked = false;
	private boolean dropClicked = false;
	private HashMap<String, Long> lastEdited = new HashMap<>();

	@FXML
	private TextArea gameText;
	@FXML
	private ListView<String> inventory, itemsInRoom, actorsInRoom;
	@FXML
	private Button buttonGoWest, buttonGoEast, buttonGoSouth, buttonGoNorth;

	@FXML

	private void setDirectionButtons() {
		ArrayList<String> exits = GameController.getCurrentRoom().getAllDirections();
		Button[] btnList = { buttonGoWest, buttonGoEast, buttonGoNorth, buttonGoSouth };
		for (Button btn : btnList) {
			btn.setDisable(true);
			for (String direction : exits) {
				String btnDirection = btn.getId();
				if (btnDirection.equals(direction.toLowerCase())) {
					btn.setDisable(false);
				}
			}
		}
	}

	/**
	 * Updates the view if things have changed
	 */
	public void updateView() {
		ObservableList<String> arr = FXCollections.observableArrayList();
		GameController.getCurrentPlayer().getInvModel().getInventory().forEach(e -> arr.add(e.getName()));
		inventory.getItems().removeAll();
		inventory.setItems(arr);
		ObservableList<String> arr1 = FXCollections.observableArrayList();
		GameController.getCurrentRoom().getTakeableItems().forEach(e -> arr1.add(e.getName()));
		itemsInRoom.getItems().removeAll();
		itemsInRoom.setItems(arr1);
	}

	public void startClicked() {
		init();
	}

	public void lookClicked() {
		commandHandler.handleCommand(new String[] { "Look" });
	}

	public void inventoryClicked() {
		if (dropClicked) {
			String toDrop = (String) inventory.getSelectionModel().getSelectedItem();
			commandHandler.handleCommand(new String[] { "Drop", toDrop });
			updateView();
			dropClicked = false;
		}
	}

	public void itemsClicked() {
		if (takeClicked) {
			String toTake = (String) itemsInRoom.getSelectionModel().getSelectedItem();
			commandHandler.handleCommand(new String[] { "Take", toTake });
			updateView();
			takeClicked = false;
		}
	}

	public void takeClicked() {
		takeClicked = true;
	}

	public void dropClicked() {
		dropClicked = true;
	}

	public void goClicked(MouseEvent event) {
		Button tmp = (Button) event.getSource();
		String direction = tmp.getId();
		gameText.setText("");
		Platform.runLater(() -> commandHandler.handleCommand(new String[] { "Go", direction }));
		Platform.runLater(() -> setDirectionButtons());
		Platform.runLater(() -> updateView());
	}

	public void init() {
		GameController.start();
		setDirectionButtons();
		gameText.setDisable(true);
		gameText.setStyle("-fx-opacity: 1;");
		lastEdited.put("inventory", (long) 0);
		lastEdited.put("itemsInRoom", (long) 0);
		lastEdited.put("actorsInRoom", (long) 0);
		updateView();
	}

	@Override
	public void println(String ele) {
		System.out.println(ele);
		String newLine = System.getProperty("line.separator");
		Platform.runLater(() -> gameText.appendText(ele));
		Platform.runLater(() -> gameText.appendText(newLine));

	}

	@Override
	public void printf(String ele) {
		System.out.println(ele);
		Platform.runLater(() -> gameText.appendText(ele));

	}

	@Override
	public void printCharDialog(String ele) {
		System.out.println(ele);
		Platform.runLater(() -> gameText.appendText(ele));

	}

	@Override
	public void printError(String error) {
		Alert a = new Alert(AlertType.ERROR);
		a.setContentText(error);
		a.show();

	}

	FXOutput() {
		commandHandler = new CommandHandler();
	}

}
