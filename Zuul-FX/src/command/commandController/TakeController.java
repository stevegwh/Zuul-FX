package command.commandController;

import command.CommandController;
import zuul.GameController;
import zuul.TakeableItem;

/**
 * Takes the specified object if the item does not make the current player
 * exceed their weight limit.
 * 
 * @author Steve
 *
 */
public class TakeController extends CommandController {
	protected String toTake;
	protected TakeableItem item;
	private int COMMAND_LENGTH = 2;
	int weight;

	@Override
	protected String validateUserInput(String[] inputArray) {
		if (inputArray.length > COMMAND_LENGTH) {
			return "Please type 'take' followed by the item.";
		}
		if (inputArray.length == 1) {
			return "Take what?";
		}
		toTake = inputArray[1];
		item = GameController.getCurrentRoom().ifItemExistsReturnIt(toTake);
		if (item == null) {
			return toTake + " not in room";
		}
		weight = item.getWeight();
		if (GameController.getCurrentPlayer().getInvModel().overWeightLimit(weight)) {
			return "Sorry, this item is too heavy for you to carry. Try dropping something first";
		}
		return null;
	}

	@Override
	public boolean execute(String[] inputArray) {
		GameController.getCurrentPlayer().getInvModel().addItem(item);
		GameController.getCurrentPlayer().getInvModel().setWeight(weight);
		GameController.getCurrentRoom().removeTakeableItem(item);
		return true;

	}

}
