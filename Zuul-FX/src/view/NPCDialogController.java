package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import npc.NPC;

/**
 * Controller for the 'dialog view' when the player speaks to an NPC.
 * 
 * @author Steve
 *
 */
public class NPCDialogController {

	@FXML
	private VBox npcDialogWrapper, npcPortraitWrapper;

	public NPCDialogController() {
		Platform.runLater(() -> onLoad());
	}

	@FXML
	public List<Button> setDialogOptions(ArrayList<String> options) {
		return options.stream().map(Button::new).collect(Collectors.toList());
	}

	private void onClick(ActionEvent event) {
		ArrayList<String> responses = DialogView.getCurrentNPC().getDialogResponses();
		// Instantiate a button to get the ID of the option the user has clicked.
		Button btn = (Button) event.getSource();
		String id = btn.getId();
		// Pass in the id in order to get the appropriate response.
		Button response = new Button(responses.get(Integer.parseInt(id.split("_")[1])));
		// Clear the view minus the NPC portrait.
		npcDialogWrapper.getChildren().removeAll(npcDialogWrapper.getChildren());
		// Generate the appropriate buttons to display the response to the user and a
		// close option.
		Button close = new Button("Close Window");
		response.setDisable(true);
		response.setStyle("-fx-opacity: 1");
		close.getStyleClass().add("response-button");
		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				closeStage(event);
			}
		});
		npcDialogWrapper.getChildren().add(response);
		npcDialogWrapper.getChildren().add(close);
	}

	/**
	 * Prepares the dialog window for the user
	 */
	private void onLoad() {
		NPC npc = DialogView.getCurrentNPC();
		List<Button> dialogOptions = setDialogOptions(npc.getDialogOptions());
		int i = 0;
		//Prepare the option buttons
		for (Button option : dialogOptions) {
			option.setMaxWidth(Double.MAX_VALUE);
			option.setPrefHeight(40);
			option.setId("option_" + i);
			option.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					onClick(event);
				}
			});
			i++;
		}
		// Get the character portrait image
		FileInputStream inputstream = null;
		try {
			inputstream = new FileInputStream(npc.getImagePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image image = new Image(inputstream);

		npcDialogWrapper.getChildren().addAll(dialogOptions);
		ImageView imageView = new ImageView(image);
		npcPortraitWrapper.getChildren().add(imageView);

	}

	@FXML
	public void closeWindow(ActionEvent event) {
		closeStage(event);
	}

	private void closeStage(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

}
