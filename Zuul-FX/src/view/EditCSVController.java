package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import zuul.GameController;
import zuul.TakeableItem;

import java.io.IOException;
import java.util.List;

import IO.IOHandler;
import csvLoader.CSVEditor;
import javafx.application.Platform;
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

	@FXML
	private TextArea csvText; 

	@FXML
	public void addItem(ActionEvent event) {
		TakeableItem item = new TakeableItem("sword", 5);
		GameController.getAllRoomDataController().addItemToAllRooms(item);
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText("Added item to all rooms.");
		a.show();
		closeStage(event);
	}

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

    private void closeStage(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
	@FXML
    private void appendText(String ele) {
		String newLine = System.getProperty("line.separator");
		Platform.runLater(() -> csvText.appendText(ele));
		Platform.runLater(() -> csvText.appendText(newLine));
    }
    
    // TODO: Print the contents of the CSV file to the TextArea
	@FXML
    private void displayCSV() {
    	List<List<String>> rooms = csvEditor.getRoomData();
		rooms.forEach(e -> e.forEach(f -> appendText(f)));
    }
    
    public EditCSVController() {
		csvEditor = new CSVEditor(IOHandler.output.getCSVPath());
		displayCSV();
    }

}
