package view;

import javafx.fxml.FXML;
import csvLoader.CSVCell;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import IO.IOHandler;
import csvLoader.CSVEditor;
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
import zuul.GameController;

public class EditCSVController {
	private final int MAX_UNDO_SIZE = 10;
	private CSVEditor csvEditor;
	private CSVGrid csvGrid;
	private static ObservableList<ObservableList<CSVCell>> rooms = FXCollections.observableArrayList();
	private List<ObservableList<ObservableList<CSVCell>>> undoArr = new ArrayList<>();

	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem undoMenuItem;
	@FXML
	private VBox csvDataWrapper;
	@FXML
	private ScrollPane csvContainer;

	public static ObservableList<ObservableList<CSVCell>> getRooms() {
		return rooms;
	}

	/**
	 * Shows the 'add item to all rooms' popup dialog window.
	 */
	public void addItemToAllRooms() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addItemDialog.fxml"));
		try {
			addUndoItem(rooms);
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
		GameController.initRooms(rooms);
		Stage stage = (Stage) ((Node) menuBar).getScene().getWindow();
		stage.close();
	}

	@FXML
	public void saveCSV() {
		// TODO: To be implemented
	}

	public void removeAllWithoutExit() {
		int amountRemoved = rooms.size();
		addUndoItem(rooms);
		// Get's list of rooms without exits
		List<ObservableList<CSVCell>> toRemove = rooms.stream()
				.filter(e -> (e.get(2).getProperty().getValue().equals("null")
						&& e.get(3).getProperty().getValue().equals("null")
						&& e.get(4).getProperty().getValue().equals("null")
						&& e.get(5).getProperty().getValue().equals("null")))
				.collect(Collectors.toList());
		rooms.removeAll(toRemove);
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed.");
		a.show();
	}

	// TODO: Tidy
	public void removeAllWithoutItem() {
		// Stores the room array before modification
		int amountRemoved = rooms.size();
		// Saves previous state of room for undo
		addUndoItem(rooms);

		// Finds all rooms that do not have any items (<= length 6)
		List<ObservableList<CSVCell>> toRemove = rooms.stream().filter(e -> e.size() <= 8).collect(Collectors.toList());
		// Stores the names of these rooms
		List<CSVCell> names = toRemove.stream().map(e -> e.get(0)).collect(Collectors.toList());

		rooms.removeAll(toRemove);

		// Changes any reference to the room name in other rooms to 'null'
		for (CSVCell name : names) {
			for (ObservableList<CSVCell> room : rooms) {
				// TODO: Range is hard coded
				for (int i = 2; i <= 5; i++) {
					if (room.get(i).getProperty().getValue().equals(name.getProperty().getValue())) {
						room.set(i, new CSVCell("null", i));
					}

				}
			}
		}

		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed.");
		a.show();
	}

	@FXML
	public void undoAction() {
		rooms = undoArr.get(undoArr.size() - 1);
		undoArr.remove(undoArr.size() - 1);
	}

	/**
	 * Adds item to the undo history array.
	 * 
	 * @param rooms2
	 */
	private void addUndoItem(ObservableList<ObservableList<CSVCell>> rooms2) {
		// TODO: Make this redo/undo by +/- an index to the current operation
		if (undoArr.size() > MAX_UNDO_SIZE) {
			undoArr.remove(0);
		}
		undoArr.add(rooms2);
	}

	// TODO: Could make this a class
	private void buildObservableList(List<List<String>> roomData) {
		for (List<String> room : roomData) {
			ObservableList<CSVCell> row = FXCollections.observableArrayList();
			int i = 0;
			for (String e : room) {
				row.add(new CSVCell(e, i));
				i++;
			}
			// Add an extra two blank cells at the end.
			row.add(new CSVCell("", i));
			row.add(new CSVCell("", i + 1));
			rooms.add(row);
		}
		for (ObservableList<CSVCell> room : rooms) {
			room.addListener((ListChangeListener<CSVCell>) c -> {
				System.out.println("Row changed to : " + room);
				csvGrid.drawGrid();
			});
		}
		rooms.addListener((ListChangeListener<ObservableList<CSVCell>>) c -> {
			System.out.println("Row changed to : " + rooms);
			csvGrid.drawGrid();
			undoMenuItem.setDisable(undoArr.size() == 0);
		});
	}

	public EditCSVController() {
		csvEditor = new CSVEditor(IOHandler.output.getCSVPath());
		List<List<String>> roomData = csvEditor.getRoomData();
		buildObservableList(roomData);
		GridPane csvGridPane = new GridPane();
		csvGrid = new CSVGrid(csvGridPane);
		Platform.runLater(() -> csvContainer.setContent(csvGridPane));
		Platform.runLater(() -> csvGrid.drawGrid());
	}

}
