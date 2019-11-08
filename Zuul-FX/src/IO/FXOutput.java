package IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
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

	ListProperty<String> itemsListProperty = new SimpleListProperty<>();
	ListProperty<String> inventoryListProperty = new SimpleListProperty<>();
	ListProperty<String> actorListProperty = new SimpleListProperty<>();

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
	 * TODO: Assign these directly as ObservableList and add eventlisteners to them.
	 */
	public void updateView() {
		itemsListProperty.set(FXCollections.observableArrayList(GameController.getCurrentRoom().getTakeableItems()
				.stream().map(e -> e.getName()).collect(Collectors.toList())));
		itemsInRoom.itemsProperty().bind(itemsListProperty);
		inventoryListProperty.set(FXCollections.observableArrayList(GameController.getCurrentPlayer().getInvModel()
				.getInventory().stream().map(e -> e.getName()).collect(Collectors.toList())));
		inventory.itemsProperty().bind(inventoryListProperty);
		actorListProperty.set(FXCollections.observableArrayList(GameController.getCurrentRoom().getActorsInRoom()
				.stream().map(e -> e.getName()).collect(Collectors.toList())));
		actorsInRoom.itemsProperty().bind(actorListProperty);
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
