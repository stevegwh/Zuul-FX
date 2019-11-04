package command.commandController;

import command.CommandController;
import zuul.GameController;
import zuul.TakeableItem;
import zuulutils.EditLogArrayList;

/**
 * Prints the active player's inventory.
 * 
 * @author Steve
 *
 */
public class InventoryController extends CommandController {
	protected EditLogArrayList<TakeableItem> inventory = new EditLogArrayList<>();
	private int COMMAND_LENGTH = 1;

	protected String validateUserInput(String[] inputArray) {
		if (inputArray.length > COMMAND_LENGTH) {
			return "To see your inventory please just type 'inventory'";
		}
		inventory = GameController.getCurrentPlayer().getInvModel().getInventory();
		if (inventory.size() == 0) {
			return "You do not currently have anything in your inventory";
		}
		return null;
	}

	@Override
	protected boolean execute(String[] inputArray) {
		return false;
	}
}
