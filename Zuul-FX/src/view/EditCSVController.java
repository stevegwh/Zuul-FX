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
import javafx.beans.value.ChangeListener;
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
//	private static List<List<String>> rooms;
	private static List<ObservableList<CSVCell>> rooms = new ArrayList<ObservableList<CSVCell>>();
	private List<List<ObservableList<CSVCell>>> undoArr = new ArrayList<>();

//	@FXML
//	private TextArea csvText;
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

	public static List<ObservableList<CSVCell>> getRooms() {
		return rooms;
	}

	/**
	 * Shows the 'add item to all rooms' popup dialog window.
	 */
	public void addItemToAllRooms() {
//		for (List<CSVCell> room : rooms) { 
//			for (CSVCell cell : room) {
//				System.out.println(cell.getProperty().getValue());
//			}
//		}
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addItemDialog.fxml"));
		try {
			addUndoItem(rooms);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent, 300, 200);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();
			updateView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateView() {
		Platform.runLater(() -> initGrid());
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
		// Get's list of rooms with exits
		List<ObservableList<CSVCell>> toRemove = rooms.stream()
				.filter(e -> !(e.get(2).getProperty().getValue().equals("null")
						&& e.get(3).getProperty().getValue().equals("null")
						&& e.get(4).getProperty().getValue().equals("null")
						&& e.get(5).getProperty().getValue().equals("null")))
				.collect(Collectors.toList());
		rooms = toRemove;
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed.");
		a.show();
		updateView();
	}

	// TODO: Broken. References do not get updated to 'null' now.
	// Also, you can no longer check for size of the array as an indicator that
	// there are no items as you are adding null CSVCels to the table for the user
	// to input values in.
	public void removeAllWithoutItem() {
		// Stores the room array before modification
		int amountRemoved = rooms.size();
		// Saves previous state of room for undo
		addUndoItem(rooms);

		// Finds all rooms that do not have any items (<= length 6)
		List<ObservableList<CSVCell>> toRemove = rooms.stream().filter(e -> e.size() <= 6).collect(Collectors.toList());
		// Stores the names of these rooms
		List<CSVCell> names = toRemove.stream().map(e -> e.get(0)).collect(Collectors.toList());
		System.out.println(names.size());

		// TODO: Instead of removing you could set all to null?
		rooms.removeAll(toRemove);

		// Changes any reference to the room name in other rooms to 'null'
		for (CSVCell name : names) {
			for (ObservableList<CSVCell> room : rooms) {
				int idx = room.indexOf(name);
				System.out.println(idx);
				if (idx >= 0) {
					System.out.println("THIS NEVER HAPPENS");
					room.set(idx, new CSVCell("null"));
				}
			}
		}

		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed.");
		a.show();
		updateView();
	}

	@FXML
	public void undoAction() {
		rooms = undoArr.get(undoArr.size() - 1);
		undoArr.remove(undoArr.size() - 1);
		updateView();
	}

	/**
	 * Adds item to the undo history array.
	 * 
	 * @param rooms2
	 */
	private void addUndoItem(List<ObservableList<CSVCell>> rooms2) {
		// TODO: Make this redo/undo by +/- an index to the current operation
		if (undoArr.size() > MAX_UNDO_SIZE) {
			undoArr.remove(0);
		}
		undoArr.add(rooms2);
	}

	private int findLongestArr(List<ObservableList<CSVCell>> rooms2) {
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
	private void initGrid() {
		// TODO: Check if the user is focusing a TextField here.
		csvGridPane.getChildren().clear();
		int ROW_LENGTH = rooms.size();
		int COL_LENGTH = findLongestArr(rooms);

		for (int row = 0; row < ROW_LENGTH; row++) {
			for (int col = 0; col < COL_LENGTH; col++) {
				TextField cell = new TextField();
				if (col < rooms.get(row).size()) {
					CSVCell element = rooms.get(row).get(col);
					int colIdx = col;
					int rowIdx = row;
					cell.setText(element.getProperty().getValue());
					cell.textProperty().bindBidirectional(element.getProperty());
					cell.textProperty().addListener((InvalidationListener) c -> {
						System.out.println("Cell changed");
//						element.getProperty().set("Hello");
						// TODO: Simplify
//						if (colIdx == rooms.get(rowIdx).size() - 2) {
//							System.out.println("We are editing the second from last item in the row");
//							if (!rooms.get(rowIdx).get(colIdx + 1).getProperty().getValue().isEmpty()) {
//								lastFocusedRow = rowIdx;
//								lastFocusedCol = colIdx;
//								rooms.get(rowIdx).add(new CSVCell(""));
//								rooms.get(rowIdx).add(new CSVCell(""));
//								System.out.println("Both name and weight are populated");
//							}
//						}
//						if (colIdx == rooms.get(rowIdx).size() - 1) {
//							System.out.println("We are editing the final item in the row");
//							if (!rooms.get(rowIdx).get(colIdx - 1).getProperty().getValue().isEmpty()) {
//								System.out.println("Both name and weight are populated");
//								lastFocusedRow = rowIdx;
//								lastFocusedCol = colIdx;
//								rooms.get(rowIdx).add(new CSVCell(""));
//								rooms.get(rowIdx).add(new CSVCell(""));
//							}
//						}
						if ((colIdx == rooms.get(rowIdx).size() - 1
								&& !rooms.get(rowIdx).get(colIdx - 1).getProperty().getValue().isEmpty())
								|| (colIdx == rooms.get(rowIdx).size() - 2)
										&& !rooms.get(rowIdx).get(colIdx + 1).getProperty().getValue().isEmpty()) {
							lastFocusedRow = rowIdx;
							lastFocusedCol = colIdx;
							rooms.get(rowIdx).add(new CSVCell(""));
							rooms.get(rowIdx).add(new CSVCell(""));
						}
					});
				} else if (col > rooms.get(row).size() - 1) {
					// From this point on in the row 'dummy' TextFields are instantiated and set to
					// disabled.
					// They are not bound to the underlying array of CSVCells unlike the two blank
					// cells added previously.
					cell.setDisable(true);
				}

				csvGridPane.add(cell, col, row);
			}
		}
		// TODO: Refocus previous TextField here if it exists.
		System.out.println(lastFocusedRow + " " + lastFocusedCol);
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
				initGrid();
			});
		}
	}

	public EditCSVController() {
		csvEditor = new CSVEditor(IOHandler.output.getCSVPath());
		List<List<String>> roomData = csvEditor.getRoomData();
		buildObservableList(roomData);
		updateView();
	}

}
