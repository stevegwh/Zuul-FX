package command.commandController;

import java.util.ArrayList;

import com.github.cliftonlabs.json_simple.JsonArray;

import command.CommandController;
import zuul.GameController;

/**
 * Prints the description, actors, items and exits of the current room. Called
 * by command.game.eventOutput.LookOutput every time a new room is entered.
 * 
 * @author Steve
 *
 */
public class LookController extends CommandController {
	protected String description;
	protected JsonArray actors;
	protected JsonArray items;
	protected ArrayList<String> exits;
	private int COMMAND_LENGTH = 1;

	@Override
	public String validateUserInput(String[] inputArray) {
		if (inputArray.length > COMMAND_LENGTH) {
			return "To look please just type 'look'";
		}
		if (inputArray.length > 1) {
			return "Look what?";
		}
		return null;
	}

	public boolean execute(String[] inputArray) {
		description = GameController.getRoomModel().getDescription();
		actors = GameController.getRoomModel().getActorsInRoom(GameController.getCurrentPlayer().getLocation());
		items = GameController.getRoomModel().getTakeableItems();
		exits = GameController.getRoomModel().getAllDirections(GameController.getCurrentPlayer().getLocation());
		return true;
	}

}
