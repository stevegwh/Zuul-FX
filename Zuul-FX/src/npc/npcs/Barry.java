package npc.npcs;

import IO.IOHandler;
import npc.NPC;
import zuul.GameController;
import zuul.TakeableItem;

public class Barry extends NPC {
	public Barry() {
		super(new Object() {
		}.getClass().getEnclosingClass().getSimpleName());
	}

	@Override
	public boolean onGive(String toCompare) {
		String validItem = super.getValidItem();
		if (toCompare.contentEquals(validItem)) {
			IOHandler.output.printCharDialog("Oh, thank you for the " + validItem);
			TakeableItem item = GameController.getCurrentPlayer().getInvModel().getItem(toCompare);
			GameController.getCurrentPlayer().getInvModel().removeItem(item);
			return true;
		}
		return false;
	}
}
