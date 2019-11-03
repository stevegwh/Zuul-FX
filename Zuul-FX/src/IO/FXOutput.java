package IO;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import zuul.GameController;
import zuul.CommandHandler;

public class FXOutput implements Output {
	private CommandHandler commandHandler;
	private boolean takeClicked = false;
	private boolean dropClicked = true;
	private long invLastEdited = 0;
	private HashMap<String, Long> lastEdited = new HashMap<>();

	@FXML
	private TextArea gameText;
	@FXML
	private ListView<String> inventory;
	@FXML
	private Button buttonGoWest, buttonGoEast, buttonGoSouth, buttonGoNorth;

	@FXML

	private void setDirectionButtons() {
		ArrayList<String> exits = GameController.getRoomModel()
				.getAllDirections(GameController.getCurrentPlayer().getLocation());
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
		if (lastEdited.get("inventory") < GameController.getCurrentPlayer().getInvModel().getInventory().getLastEdit()) {
			ObservableList<String> arr = FXCollections.observableArrayList();
			GameController.getCurrentPlayer().getInvModel().getInventory().forEach(e -> arr.add(e.getName()));
			inventory.getItems().removeAll();
			inventory.setItems(arr);
			invLastEdited = GameController.getCurrentPlayer().getInvModel().getInventory().getLastEdit();
		}
	}

	public void removeInvItem(String toRemove) {
		inventory.getItems().remove(toRemove);
	}

	public void addInvItem(String toAdd) {
		inventory.getItems().add(toAdd);
	}

	public void startClicked() {
		init();
	}

	public void lookClicked() {
		commandHandler.handleCommand(new String[] { "Look" });
	}

	public void inventoryClicked() {
		if (dropClicked) {
			String t1 = (String) inventory.getSelectionModel().getSelectedItem();
			commandHandler.handleCommand(new String[] { "Drop", t1 });
			updateView();
			dropClicked = false;
		}
	}

	public void takeClicked() {
		String toTake = "sword";
		commandHandler.handleCommand(new String[] { "Take", toTake });
		updateView();
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
	}
	
	public void init() {
		GameController.start();
		setDirectionButtons();
		gameText.setDisable(true);
		gameText.setStyle("-fx-opacity: 1;");
		lastEdited.put("inventory", (long) 0);
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
		gameText.setText(error);

	}

	FXOutput() {
		commandHandler = new CommandHandler();
	}

}
