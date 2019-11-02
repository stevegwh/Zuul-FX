package command.commandController;

import command.CommandController;

/**
 * Ends the game.
 * 
 * @author Steve
 *
 */
public class QuitController extends CommandController {
	@Override
	protected String validateUserInput(String[] inputArray) {
		if (inputArray.length > 1) {
			return "Quit what?";
		}
		return null;
	}

	@Override
	public boolean execute(String[] inputArray) {
		return true;
	}

}
