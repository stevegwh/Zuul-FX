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

public class NPCDialogController {

	@FXML
	private VBox npcDialogWrapper, npcPortraitWrapper;

	public NPCDialogController() {
		Platform.runLater(() -> onLoad());
	}

	public List<Button> setDialogOptions(ArrayList<String> options) {
		return options.stream().map(e -> new Button(e)).collect(Collectors.toList());
	}

	// TODO: Refactor and change the dialog response to not be a button.
	// TODO: Make sure the dialog window can't be resized.
	private void onClick(ActionEvent event) {
		ArrayList<String> responses = DialogView.getCurrentNPC().getDialogResponses();
		Button btn = (Button) event.getSource();
		String id = btn.getId();
		Button response = new Button(responses.get(Integer.parseInt(id.split("_")[1])));
		npcDialogWrapper.getChildren().removeAll(npcDialogWrapper.getChildren());
		Button close = new Button("Close Window");
		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				closeStage(event);
			}
		});
		npcDialogWrapper.getChildren().add(response);
		npcDialogWrapper.getChildren().add(close);
	}

	private void onLoad() {
		NPC npc = DialogView.getCurrentNPC();
		List<Button> dialogOptions = setDialogOptions(npc.getDialogOptions());
		int i = 0;
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
