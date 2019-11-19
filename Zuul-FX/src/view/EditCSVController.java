package view;

import javafx.fxml.FXML;
import csvLoader.CSVCell;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
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
	private CSVEditor csvEditor;
	private CSVGridFactory csvGrid;
	private static ObservableList<ObservableList<CSVCell>> rooms = FXCollections.observableArrayList();

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

	// TODO: Should also dereference rooms that point to it?
	public void removeAllWithoutExit() {
		int amountRemoved = rooms.size();
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

	public void removeAllWithoutItem() {
		// Stores the room array before modification
		int amountRemoved = rooms.size();
		// Saves previous state of room for undo

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
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed. All references to removed rooms are now 'null'.");
		a.show();
	}

	private void buildObservableList(List<List<String>> roomData) {
		for (List<String> room : roomData) {
			ObservableList<CSVCell> row = FXCollections.observableArrayList();

			// Instantiates CSVCells with String and an index.
			// The index is used to decide what csv header the String represents (Name, Description etc).
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
		});
	}

	public EditCSVController() {
		csvEditor = new CSVEditor(IOHandler.output.getCSVPath());
		List<List<String>> roomData = csvEditor.getRoomData();
		buildObservableList(roomData);
		GridPane csvGridPane = new GridPane();
		csvGrid = new CSVGridFactory(csvGridPane);
		Platform.runLater(() -> csvContainer.setContent(csvGridPane));
		Platform.runLater(() -> csvGrid.drawGrid());
	}

}
