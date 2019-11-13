package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import IO.IOHandler;
import csvLoader.CSVEditor;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditCSVController {
	private final int MAX_UNDO_SIZE = 10;
	private CSVEditor csvEditor;
	private static List<List<String>> rooms;
	private List<List<List<String>>> undoArr = new ArrayList<>();
	ListProperty<String> csvDataProperty = new SimpleListProperty<>();

	@FXML
	private TextArea csvText;
	@FXML
	private MenuItem undoMenuItem;

	public static List<List<String>> getRooms() {
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
			updateView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateView() {
		Platform.runLater(() -> csvText.setText(" "));
		rooms.forEach(e -> e.forEach(f -> appendText(f)));
		Platform.runLater(() -> undoMenuItem.setDisable(undoArr.size() == 0));
	}

	private void closeStage(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	public void removeAllWithoutExit() {
		int amountRemoved = rooms.size();
		addUndoItem(rooms);
		List<List<String>> toRemove = rooms.stream().filter(e -> !(e.get(2).equals("null") && e.get(3).equals("null")
				&& e.get(4).equals("null") && e.get(5).equals("null"))).collect(Collectors.toList());
		rooms = toRemove;
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed.");
		a.show();
		updateView();
	}

	public void removeAllWithoutItem() {
		// Stores the room array before modification
		int amountRemoved = rooms.size();
		// Saves previous state of room for undo
		addUndoItem(rooms);

		// Finds all rooms that do not have any items (<= length 6)
		List<List<String>> toRemove = rooms.stream().filter(e -> e.size() <= 6).collect(Collectors.toList());
		// Stores the names of these rooms
		List<String> names = toRemove.stream().map(e -> e.get(0)).collect(Collectors.toList());

		rooms.removeAll(toRemove);
		
		// Changes any reference to the room name in other rooms to 'null'
		for (String name : names) {
			for (List<String> room : rooms) {
				int idx = room.indexOf(name);
				if (idx >= 0) {
					room.set(idx, "null");
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
		undoArr.remove(undoArr.size() -1);
		updateView();
	}
	
	/**
	 * Adds item to the undo history array.
	 * @param room
	 */
	private void addUndoItem(List<List<String>> room) {
		// TODO: Make this redo/undo by +/- an index to the current operation
		if (undoArr.size() > MAX_UNDO_SIZE) {
			undoArr.remove(0);
		}
		undoArr.add(room);
	}
	
	
	@FXML
	public void commitChanges() {
		
	}

	/**
	 * Adds text to the main TextArea of the 'Edit CSV' scene.
	 * 
	 * @param ele the element to add.
	 */
	@FXML
	private void appendText(String ele) {
		String newLine = System.getProperty("line.separator");
		Platform.runLater(() -> csvText.appendText(ele));
		Platform.runLater(() -> csvText.appendText(newLine));
	}

	@FXML
	private void displayCSV() {
		rooms = csvEditor.getRoomData();
		updateView();
	}

	public EditCSVController() {
		csvEditor = new CSVEditor(IOHandler.output.getCSVPath());
		displayCSV();
	}

}
