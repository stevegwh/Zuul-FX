package csvEditor;

import javafx.fxml.FXML;
import csvLoader.CSVEditorCell;
import java.util.List;

import csvLoader.CSVEditorLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.IOHandler;
import zuul.GameController;
import zuul.GameType;
import csvEditor.bulkActions.*;

public class EditCSVController {
	private CSVEditorLoader csvEditor;
	private CSVGridFactory csvGrid;
	private static ObservableList<ObservableList<CSVEditorCell>> rooms = FXCollections.observableArrayList();

	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu bulkActionMenu;
	@FXML
	private MenuItem undoMenuItem;
	@FXML
	private VBox csvDataWrapper;
	@FXML
	private ScrollPane csvContainer;

	public static ObservableList<ObservableList<CSVEditorCell>> getRooms() {
		return rooms;
	}

	@FXML
	public void submitCSV(ActionEvent event) {
		// TODO: If there are errors do not allow to finish.
		// TODO: Disable loadCSV after this.
		GameController.initRooms(rooms, GameType.CUSTOM);
		Stage stage = (Stage) ((Node) menuBar).getScene().getWindow();
		stage.close();
	}

	@FXML
	public void saveCSV() {
		// TODO: To be implemented
	}

	private void buildObservableList(List<List<String>> roomData) {
		for (List<String> room : roomData) {
			ObservableList<CSVEditorCell> row = FXCollections.observableArrayList();

			// Instantiates CSVCells with String and an index.
			// The index is used to decide what csv header the String represents (Name,
			// Description etc).
			int i = 0;
			for (String e : room) {
				row.add(new CSVEditorCell(e, i));
				i++;
			}

			// Add an extra two blank cells at the end.
			row.add(new CSVEditorCell("", i));
			row.add(new CSVEditorCell("", i + 1));
			rooms.add(row);
		}
		for (ObservableList<CSVEditorCell> room : rooms) {
			room.addListener((ListChangeListener<CSVEditorCell>) c -> {
				System.out.println("Row changed to : " + room);
				csvGrid.drawGrid();
			});
		}
		rooms.addListener((ListChangeListener<ObservableList<CSVEditorCell>>) c -> {
			System.out.println("Row changed to : " + rooms);
			csvGrid.drawGrid();
		});
	}
	
	private void loadBulkActions() {
		BulkActionInstantiator bulkActionInstantiator = new BulkActionInstantiator();
		bulkActionInstantiator.populateMenu(bulkActionMenu);
	}

	public EditCSVController() {
		csvEditor = new CSVEditorLoader(IOHandler.output.getCSVPath());
		List<List<String>> roomData = csvEditor.getRoomData();
		buildObservableList(roomData);
		GridPane csvGridPane = new GridPane();
		csvGrid = new CSVGridFactory(csvGridPane);
		Platform.runLater(() -> csvContainer.setContent(csvGridPane));
		Platform.runLater(() -> csvGrid.drawGrid());
		Platform.runLater(() -> loadBulkActions());
	}

}
