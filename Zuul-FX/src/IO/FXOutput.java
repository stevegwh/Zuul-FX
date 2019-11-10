package IO;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import command.game.eventOutput.GameStartOutput;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import zuul.GameController;
import zuul.TakeableItem;
import zuul.CommandHandler;

public class FXOutput implements Output {
	private String path = "";
	private Stage stage;
	private CommandHandler commandHandler;
	private boolean takeClicked = false;
	private boolean dropClicked = false;

	@FXML
	private TextArea gameText;
	@FXML
	private ListView<String> inventory, itemsInRoom, actorsInRoom;
	@FXML
	private Button buttonGoWest, buttonGoEast, buttonGoSouth, buttonGoNorth, buttonLook, buttonTake, buttonDrop,
			buttonGive;
	@FXML
	private MenuItem menuItemRemoveItems, menuItemRemoveExits, menuItemAddItems, menuItemStartCustomGame;

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

	private void setEditMenuDisable(boolean toggle) {
		MenuItem[] menuList = { menuItemAddItems, menuItemRemoveExits, menuItemRemoveItems };
		for (MenuItem item : menuList) {
			if (toggle) {
				item.setDisable(true);
			} else {
				item.setDisable(false);
			}
		}
	}

	private void enableAllButtons() {
		Button[] buttons = { buttonTake, buttonLook, buttonDrop, buttonGive };
		for (Button btn : buttons) {
			btn.setDisable(false);
		}
	}

	public void startGame() {
		GameController.start();
		setDirectionButtons();
		enableAllButtons();
		setEditMenuDisable(true);
		updateView();
	}

	public void openFileChooser() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("CSV", "*.csv", "*.csv");
		fileChooser.setTitle("Select file...");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			openFile(file);
		}
	}

	private void openFile(File file) {
		path = file.getPath();
		// TODO: Would be good if initRooms returned a boolean so we can confirm if it
		// loaded correctly or not
		GameController.initRooms(path);
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText("File loaded successfully.");
		a.show();
		setEditMenuDisable(false);
		menuItemStartCustomGame.setDisable(false);
	}

	public void startDefaultGame() {
		GameController.initRooms("");
		startGame();
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

	public void setStage(Stage primaryStage) {
		stage = primaryStage;
	}

	public void removeAllWithoutExit() {
		int amountRemoved = GameController.getAllRoomDataController().removeAllWithoutExit();
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved + " room(s) removed.");
		a.show();
	}

	// TODO: Need to remove exit's reference from all other rooms
	public void removeAllWithoutItems() {
		GameController.getAllRoomDataController().removeAllWithoutItems();
	}

	// TODO: Dialog box where user can specify a name and weight, then pass it to
	// this
	public void addItemToAllRooms() {
		TakeableItem item = new TakeableItem("sword", 5);
		GameController.getAllRoomDataController().addItemToAllRooms(item);
	}

	FXOutput() {
		commandHandler = new CommandHandler();
	}

	public void onLoad() {
		GameStartOutput welcome = new GameStartOutput();
		welcome.init(new String[] {});
	}

}
