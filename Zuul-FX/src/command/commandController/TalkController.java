package command.commandController;

import zuul.GameController;
import zuulutils.ZuulTools;
import command.CommandController;
import npc.NPC;

/**
 * Attempts to call the specified NPC's onTalk() if present.
 * 
 * @author Steve
 *
 */
public class TalkController extends CommandController {
	protected int COMMAND_LENGTH = 2;
	protected NPC actor;
	protected String toTalk;

	@Override
	protected String validateUserInput(String[] inputArray) {
		if (inputArray.length > COMMAND_LENGTH) {
			return "To talk to someone please type 'talk' followed by a name";

		}
		if (inputArray.length < 2) {
			return "Talk to who?";
		}
		toTalk = inputArray[1]; // talk[0] to[1] Barry[2]
		toTalk = ZuulTools.capitalize(toTalk);
		if (!GameController.getRoomModel().hasActor(toTalk)) {
			return toTalk + " not in room.";
		}
		return null;
	}

	@Override
	public boolean execute(String[] args) {
		actor = GameController.getNPCContoller().getActor(toTalk);
		return true;
	}

}
