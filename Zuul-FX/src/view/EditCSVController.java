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
import javafx.beans.InvalidationListener;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zuul.GameController;

public class EditCSVController {
	private final int MAX_UNDO_SIZE = 10;
	private CSVEditor csvEditor;
	private static ObservableList<ObservableList<CSVCell>> rooms = FXCollections.observableArrayList();
	private List<ObservableList<ObservableList<CSVCell>>> undoArr = new ArrayList<>();

	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem undoMenuItem;
	@FXML
	private VBox csvDataWrapper;
	@FXML
	private GridPane csvGridPane;

	private int lastFocusedRow;
	private int lastFocusedCol;

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

	private void updateView() {
		Platform.runLater(() -> drawGrid());
		Platform.runLater(() -> undoMenuItem.setDisable(undoArr.size() == 0));
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
		System.out.println(names.size());

		rooms.removeAll(toRemove);

		// Changes any reference to the room name in other rooms to 'null'
		for (CSVCell name : names) {
			for (ObservableList<CSVCell> room : rooms) {
				// TODO: Range is hard coded
				for (int i = 2; i <= 5; i++) {
					if (room.get(i).getProperty().getValue().equals(name.getProperty().getValue())) {
						System.out.println("THIS NEVER HAPPENS");
						room.set(i, new CSVCell("null"));
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

	private int findLongestArr(ObservableList<ObservableList<CSVCell>> rooms2) {
		int longest = 0;
		for (ObservableList<CSVCell> arr : rooms2) {
			if (arr.size() > longest)
				longest = arr.size();
		}
		return longest;
	}

	// Reference:
	// https://www.programcreek.com/java-api-examples/?class=javafx.scene.layout.GridPane&method=getChildren
	public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();
		for (Node node : childrens) {
			if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
				result = node;
				break;
			}
		}
		return result;
	}

	@FXML
	private void drawGrid() {
		csvGridPane.getChildren().clear();
		int ROW_LENGTH = rooms.size();
		// To have an even grid we find the longest row in the CSV file to use as our
		// boundary.
		int COL_LENGTH = findLongestArr(rooms);

		for (int row = 0; row < ROW_LENGTH; row++) {
			for (int col = 0; col < COL_LENGTH; col++) {
				TextField cell = new TextField();
				if (col < rooms.get(row).size()) {
					CSVCell element = rooms.get(row).get(col);
					int colIdx = col, rowIdx = row;
					cell.setText(element.getProperty().getValue());
					cell.textProperty().bindBidirectional(element.getProperty());
					cell.textProperty().addListener((InvalidationListener) c -> {
						// If both the last cell and the second to last (i.e. the item name + the item
						// weight) are not empty then add two extra spaces at the end of the cell row
						// for the user.
						lastFocusedRow = rowIdx;
						lastFocusedCol = colIdx;
						if ((colIdx == rooms.get(rowIdx).size() - 1
								&& !rooms.get(rowIdx).get(colIdx - 1).getProperty().getValue().isEmpty())
								|| (colIdx == rooms.get(rowIdx).size() - 2)
										&& !rooms.get(rowIdx).get(colIdx + 1).getProperty().getValue().isEmpty()) {
							rooms.get(rowIdx).add(new CSVCell(""));
							rooms.get(rowIdx).add(new CSVCell(""));
						}
					});
				} else if (col > rooms.get(row).size() - 1) {
					// From this point on in the row 'dummy' TextFields are instantiated and set to
					// disabled.
					// They are not bound to an underlying array of CSVCells like the previous
					// TextFields.
					cell.setDisable(true);
				}

				csvGridPane.add(cell, col, row);
			}
		}
		// Ensures the grid is focusing the user's last row.
		// TODO: Ensure the textfield that was focused exists still. At the moment it throws an error if you are selecting a field that get's deleted.
		TextField toFocus = (TextField) getNodeByRowColumnIndex(lastFocusedRow, lastFocusedCol, csvGridPane);
		toFocus.requestFocus();
		Platform.runLater(() -> toFocus.positionCaret(toFocus.textProperty().getValue().length() + 1));
	}

	// TODO: Could make this a class
	private void buildObservableList(List<List<String>> roomData) {
		for (List<String> room : roomData) {
			ObservableList<CSVCell> row = FXCollections.observableArrayList();
			room.forEach(e -> row.add(new CSVCell(e)));
			// Add an extra two blank cells at the end.
			row.add(new CSVCell(""));
			row.add(new CSVCell(""));
			rooms.add(row);
		}
		for (ObservableList<CSVCell> room : rooms) {
			room.addListener((ListChangeListener<CSVCell>) c -> {
				// respond to list changes
				System.out.println("Row changed to : " + room);
				drawGrid();
			});
		}
		rooms.addListener((ListChangeListener<ObservableList<CSVCell>>) c -> {
			// respond to list changes
			System.out.println("Row changed to : " + rooms);
			drawGrid();
		});
	}

	public EditCSVController() {
		csvEditor = new CSVEditor(IOHandler.output.getCSVPath());
		List<List<String>> roomData = csvEditor.getRoomData();
		buildObservableList(roomData);
		Platform.runLater(() -> drawGrid());
	}

}
