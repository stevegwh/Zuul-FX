package csvEditor;

import javafx.fxml.FXML;
import csvLoader.CSVEditorCell;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import csvLoader.CSVEditorLoader;
import csvLoader.headers.HeaderEnum;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.IOHandler;
import zuul.GameController;
import zuul.GameType;

public class EditCSVController {
	private CSVEditorLoader csvEditor;
	private CSVGridFactory csvGrid;
	private static ObservableList<ObservableList<CSVEditorCell>> rooms = FXCollections.observableArrayList();

	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem undoMenuItem;
	@FXML
	private VBox csvDataWrapper;
	@FXML
	private ScrollPane csvContainer;

	public static ObservableList<ObservableList<CSVEditorCell>> getRooms() {
		return rooms;
	}

	/**
	 * Shows the 'add item to all rooms' popup dialog window.
	 */
	public void addItemToAllRooms() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addItemDialog.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent, 300, 200);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public void removeAllWithoutExit() {
		int amountRemoved = rooms.size();
		// Get's list of rooms without exits
		List<ObservableList<CSVEditorCell>> toRemove = new ArrayList<ObservableList<CSVEditorCell>>();
		for (ObservableList<CSVEditorCell> r : rooms) {
			if(r.stream()
			.filter(e -> e.getHeader().getEnum().equals(HeaderEnum.DIRECTION))
			.map(e-> e.getProperty().getValue())
			.allMatch(e-> e.equals("null"))) {
				toRemove.add(r);
			}
		}
		rooms.removeAll(toRemove);
		
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed.");
		a.show();
	}

	public void removeAllWithoutItem() {
		// Stores the room array before modification
		int amountRemoved = rooms.size();

		// Get's list of rooms without exits
		List<ObservableList<CSVEditorCell>> toRemove = rooms.stream().filter(r->
			r.stream()
			.filter(e -> !e.getProperty().getValue().isEmpty())
			.filter(e -> e.getHeader().getEnum().equals(HeaderEnum.ITEMNAME) || e.getHeader().getEnum().equals(HeaderEnum.ITEMWEIGHT))
			.collect(Collectors.toList())
			.size() == 0
		).collect(Collectors.toCollection(FXCollections::observableArrayList));

		// Get's all names of rooms staged for removal
		List<String> names = toRemove.stream().map(
				e -> e.stream()
				.filter(f-> f.getHeader().getEnum().equals(HeaderEnum.NAME)).findFirst().orElse(null).getProperty().getValue()
				).collect(Collectors.toList());
		// Deference all instances of the rooms staged to be removed.
		// Does this before removing everything to avoid 'index out of bounds' exceptions.
		rooms.forEach(r ->
				r.stream()
				.filter(e-> e.getHeader().getEnum().equals(HeaderEnum.DIRECTION))
				.filter(e-> names.contains(e.getProperty().getValue()))
				.forEach(e-> e.getProperty().setValue("null"))
		);
		rooms.removeAll(toRemove);

		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(
				amountRemoved - rooms.size() + " room(s) removed. All references to removed rooms are now 'null'.");
		a.show();
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

	public EditCSVController() {
		csvEditor = new CSVEditorLoader(IOHandler.output.getCSVPath());
		List<List<String>> roomData = csvEditor.getRoomData();
		buildObservableList(roomData);
		GridPane csvGridPane = new GridPane();
		csvGrid = new CSVGridFactory(csvGridPane);
		Platform.runLater(() -> csvContainer.setContent(csvGridPane));
		Platform.runLater(() -> csvGrid.drawGrid());
	}

}
