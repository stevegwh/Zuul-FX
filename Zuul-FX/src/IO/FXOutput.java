package IO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import command.game.eventOutput.GameStartOutput;
import csvLoader.CSVEditor;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.DropContextMenu;
import view.ItemsContextMenu;
import zuul.GameController;
import zuul.GameType;
import zuul.CommandHandler;

public class FXOutput {
	private String csvPath;
	private final int SCENE_WIDTH = 900;
	private final int SCENE_HEIGHT = 600;
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
	private MenuItem menuItemStartCustomGame;

	private ListProperty<String> actorListProperty = new SimpleListProperty<>();

	public String getCSVPath() {
		return csvPath;
	}

	@FXML
	public void quitProgram() {
		Platform.exit();
		System.exit(0);
	}

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
	 * Binds the data of the game data's array and the view's ListView
	 */
	public void updateView() {
		itemsInRoom.itemsProperty().bind(GameController.getCurrentRoom().getItemListProperty());
		inventory.itemsProperty()
				.bind(GameController.getCurrentPlayer().getInvModel().getInventoryListProperty());
	}

	public void updateActors() {
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
			dropClicked = false;
		}
	}

	public void itemsClicked() {
		if (takeClicked) {
			String toTake = (String) itemsInRoom.getSelectionModel().getSelectedItem();
			commandHandler.handleCommand(new String[] { "Take", toTake });
			takeClicked = false;
		}
	}

	public void takeClicked() {
		takeClicked = true;
	}

	@FXML
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
		Platform.runLater(() -> updateActors());
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
		csvPath = file.getPath();
		// TODO: Would be good if initRooms returned a boolean so we can confirm if it
		// loaded correctly or not
//		GameController.initRooms(path);
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText("File loaded successfully.");
		a.showAndWait();
		initEditCSVView();
	}

	public void startDefaultGame() {
		// TODO: Find out directory of this
//		String path = "C:\\Users\\Steve\\git\\Zuul-FX\\Zuul-FX\\src\\csvLoader\\defaultRoomData.csv";
		String path = "/home/forest/git/Zuul-FX/Zuul-FX/src/csvLoader/defaultRoomData.csv";
		CSVEditor csvEditor = new CSVEditor(path);
		List<List<String>> roomData = csvEditor.getRoomData();
		GameController.initRooms(roomData, GameType.DEFAULT);
		startGame();
	}

	public void println(String ele) {
		System.out.println(ele);
		String newLine = System.getProperty("line.separator");
		Platform.runLater(() -> gameText.appendText(ele));
		Platform.runLater(() -> gameText.appendText(newLine));

	}

	public void printf(String ele) {
		System.out.println(ele);
		Platform.runLater(() -> gameText.appendText(ele));

	}

	public void printCharDialog(String ele) {
		System.out.println(ele);
		Platform.runLater(() -> gameText.appendText(ele));

	}

	public void printError(String error) {
		Alert a = new Alert(AlertType.ERROR);
		a.setContentText(error);
		a.show();
	}

	public void setStage(Stage primaryStage) {
		stage = primaryStage;
	}

	public void initEditCSVView() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editCSVView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent, SCENE_WIDTH, SCENE_HEIGHT);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();
			menuItemStartCustomGame.setDisable(false);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setContextMenus() {
		DropContextMenu dropContextMenu = new DropContextMenu();
		ItemsContextMenu itemsContextMenu = new ItemsContextMenu();
		inventory.setContextMenu(dropContextMenu.getContextMenu());
		itemsInRoom.setContextMenu(itemsContextMenu.getContextMenu());
	}

	FXOutput() {
		commandHandler = new CommandHandler();
		Platform.runLater(() -> setContextMenus());
	}

	public void onLoad() {
		GameStartOutput welcome = new GameStartOutput();
		welcome.init(new String[] {});
	}

}
