package command.commandController;

import command.CommandController;
import npc.NPC;
import zuul.GameController;
import zuul.TakeableItem;
import zuulutils.ZuulTools;

/**
 * Gives the specified NPC object the specified item. Removes item from active
 * player's inventory.
 * 
 * @author Steve
 *
 */
public class GiveController extends CommandController {
	protected String itemName;
	protected String actorName;
	protected TakeableItem takeableItem;
	protected NPC npc;
	private int COMMAND_LENGTH = 3;

	@Override
	public String validateUserInput(String[] inputArray) {
		if (inputArray.length > COMMAND_LENGTH) {
			return "Invalid CommandController";
		}
		if (inputArray.length == 1) {
			return "Give to who?";
		}
		String actorName = inputArray[1];
		actorName = ZuulTools.capitalize(actorName);
		NPC npc = GameController.getNPCContoller().getActor(actorName);
		if (npc == null) {
			return actorName + " is not in the room";
		} else {
			this.npc = npc;
		}
		if (!npc.getCurrentLocation().equals(GameController.getCurrentPlayer().getLocation())) {
			return actorName + " is not in the room";
		}
		if (inputArray.length <= 2) {
			return "Give what?";
		}
		itemName = inputArray[2];
		takeableItem = GameController.getCurrentPlayer().getInvModel().getItem(itemName);
		boolean itemInInv = GameController.getCurrentPlayer().getInvModel().checkIfExists(itemName);
		if (!itemInInv) {
			return "You don't have the " + itemName;
		}

		return null;
	}

	@Override
	public boolean execute(String[] inputArray) {
		if (!npc.onGive(takeableItem.getName())) {
			return false;
		} else {
			if (takeableItem.isPerishable()) {
				GameController.getCurrentPlayer().getInvModel().setWeight(takeableItem.getWeight());
				GameController.getCurrentPlayer().getInvModel().removeItem(takeableItem);
			}
			return true;
		}
	}

}
