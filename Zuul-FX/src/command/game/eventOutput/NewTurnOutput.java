package command.game.eventOutput;

import command.ICommandOutput;
import command.game.eventController.NewTurnController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NewTurnOutput extends NewTurnController implements ICommandOutput {

	@Override
	public void init(String[] inputArray) {
		super.execute(inputArray);
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText("Player " + (idx + 1) + "'s turn.");
		a.showAndWait();
//		IOHandler.output.println("Player " + (idx + 1) + "'s turn");
		System.out.println("Player " + (idx + 1) + "'s turn");
	}

}
