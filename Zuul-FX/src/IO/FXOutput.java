package IO;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import zuul.GameController;
import zuul.CommandHandler;

public class FXOutput implements Output {
	private CommandHandler commandHandler;

	@FXML
	private TextArea gameText;
	@FXML
	private ListView<String> inventory;
	@FXML
	private Button buttonGoWest;
	@FXML
	private Button buttonGoEast;
	@FXML
	private Button buttonGoSouth;
	@FXML
	private Button buttonGoNorth;

	private void setDirectionButtons() {
		ArrayList<String> exits = GameController.getRoomModel().getAllDirections(GameController.getCurrentPlayer().getLocation());
		Button[] btnList = {buttonGoWest, buttonGoEast, buttonGoNorth, buttonGoSouth};
		for (Button btn : btnList) {
			btn.setDisable(true);
			for (String direction : exits) {
				String btnDirection = btn.getId();
				if(btnDirection.equals(direction.toLowerCase())) {
					btn.setDisable(false);
				}
			}
		}
	}
	public void startClicked() {
		GameController.start();
		setDirectionButtons();
		gameText.setDisable(true);
		gameText.setStyle("-fx-opacity: 1;");
	}
	
	public void lookClicked() {
		commandHandler.handleCommand(new String[] {"Look"});
	}
	
	public void goClicked(MouseEvent event) {
		Button tmp = (Button) event.getSource();
		String direction = tmp.getId();
		gameText.setText("");
		Platform.runLater(() -> commandHandler.handleCommand(new String[] {"Go", direction}));
		Platform.runLater(() -> setDirectionButtons());
	}

	@Override
	public void println(String ele) {
		System.out.println(ele);
		String newLine = System.getProperty("line.separator");			
		Platform.runLater(() -> gameText.appendText(ele));
		Platform.runLater(() -> gameText.appendText(newLine));

	}

	@Override
	public void printf(String ele) {
		System.out.println(ele);
		Platform.runLater(() -> gameText.appendText(ele));

	}

	@Override
	public void printCharDialog(String ele) {
		System.out.println(ele);
		Platform.runLater(() -> gameText.appendText(ele));

	}

	@Override
	public void printError(String error) {
		gameText.setText(error);

	}
	
	FXOutput() {
		commandHandler = new CommandHandler();
	}
	

}
