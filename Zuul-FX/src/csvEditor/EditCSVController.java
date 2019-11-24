package csvEditor;

import javafx.fxml.FXML;
import csvLoader.CSVEditorCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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

	private boolean checkForErrors() {
		boolean errorFound = false;
		for (ObservableList<CSVEditorCell> r : rooms) {
			// Get the last two elements first and check if they're both null
			// Due to the CSV Editor adding extra cells when it detects both fields are
			// filled it is impossible for the final two cells to contain data without
			// it being an error.
			if (!r.get(r.size() - 1).getProperty().getValue().isEmpty()
					|| !r.get(r.size() - 2).getProperty().getValue().isEmpty()
					|| !r.get(r.size() - 1).getProperty().getValue().isBlank()
					|| !r.get(r.size() - 2).getProperty().getValue().isBlank()) {
				errorFound = true;
				break;
			}
			if (r.stream().filter(e -> r.indexOf(e) < r.size() - 2).anyMatch(e -> e.hasError())) {
				errorFound = true;
				break;
			}
		}
		if (errorFound) {
			IOHandler.output.printError("Please correct mistakes before finishing.");
		}
		return errorFound;
	}

	@FXML
	public void submitCSV(ActionEvent event) {
		if (!checkForErrors()) {
			GameController.initRooms(rooms, GameType.CUSTOM);
			Stage stage = (Stage) ((Node) menuBar).getScene().getWindow();
			stage.close();
		}
	}

	@FXML
	public void saveCSV() {
		if (checkForErrors()) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("CSV", "*.csv", "*.csv");
		fileChooser.setTitle("Select destination...");
		fileChooser.getExtensionFilters().add(filter);
		Stage stage = (Stage) csvContainer.getScene().getWindow();
		File file = fileChooser.showSaveDialog(stage);
		if (file != null) {
			writeFile(file);
		}
	}

	private String prepareCSVSave() {
		String toReturn = "";
		// Remove the additional empty cells and convert CSVEditorCell to string.
		List<List<String>> arr = rooms.stream()
				.map(r -> r.stream().filter(e -> !e.getProperty().getValue().isEmpty())
						.map(e -> e.getProperty().getValue()).collect(Collectors.toList()))
				.collect(Collectors.toList());
		for (List<String> r : arr) {
			int i = 0;
			for (String cell : r) {
				toReturn += cell + (i == r.size() - 1 ? "\n" : ", ");
				i++;
			}
		}
		return toReturn;
	}

	// Based on:
	// https://www.mkyong.com/java/how-to-write-to-file-in-java-fileoutputstream-example/
	private void writeFile(File file) {
		FileOutputStream fop = null;
		String content = prepareCSVSave();

		try {
			fop = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}

			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
