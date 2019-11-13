package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import IO.IOHandler;
import csvLoader.CSVEditor;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditCSVController {
	private CSVEditor csvEditor;
	private static List<List<String>> rooms;
	private List<List<List<String>>>changesArray;
	ListProperty<String> csvDataProperty = new SimpleListProperty<>();

	@FXML
	private TextArea csvText; 

	
	public static List<List<String>> getRooms() {
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
			updateView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateView() {
		rooms.forEach(e -> e.forEach(f -> appendText(f)));
		
	}

    private void closeStage(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Adds text to the main TextArea of the 'Edit CSV' scene.
     * @param ele the element to add.
     */
	@FXML
    private void appendText(String ele) {
		String newLine = System.getProperty("line.separator");
		Platform.runLater(() -> csvText.appendText(ele));
		Platform.runLater(() -> csvText.appendText(newLine));
    }
    
    // TODO: Print the contents of the CSV file to the TextArea
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
