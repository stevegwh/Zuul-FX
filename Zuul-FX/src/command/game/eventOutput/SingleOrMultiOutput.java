package command.game.eventOutput;

import java.util.Optional;

import command.ICommandOutput;
import command.game.eventController.SingleOrMultiController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Asks the user to specify single or multi-player.
 * 
 * @author Steve
 *
 */
public class SingleOrMultiOutput extends SingleOrMultiController implements ICommandOutput {
	@Override
	public void init(String[] inputArray) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText("Single or Multiplayer game?");
		Optional <ButtonType> action = alert.showAndWait();
		if (action.get() == ButtonType.OK) {
			execute(new String[] {"1"});
		} else {
			execute(new String[] {"2"});
		}
	}

}
