package command.commandController;



import java.util.ArrayList;

import command.CommandController;
import npc.NPC;
import zuul.GameController;
import zuul.TakeableItem;

/**
 * Prints the description, actors, items and exits of the current room. Called
 * by command.game.eventOutput.LookOutput every time a new room is entered.
 * 
 * @author Steve
 *
 */
public class LookController extends CommandController {
	protected String description;
	protected ArrayList<NPC> actors;
	protected ArrayList<TakeableItem> items;
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
		description = GameController.getCurrentRoom().getDescription();
		actors = GameController.getCurrentRoom().getActorsInRoom();
		return true;
	}

}
