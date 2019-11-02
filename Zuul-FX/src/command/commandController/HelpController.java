package command.commandController;

import java.util.ArrayList;
import java.util.List;

import command.CommandController;
import command.CommandInstantiator;

/**
 * Prints all valid commands in the game.
 * 
 * @author Steve
 *
 */
public class HelpController extends CommandController {
	private int COMMAND_LENGTH = 1;
	protected List<String> commands = new ArrayList<>();

	@Override
	public boolean execute(String[] inputArray) {
		CommandInstantiator instantiator = new CommandInstantiator();
		commands = instantiator.getCommands();
		return true;
	}

	@Override
	protected String validateUserInput(String[] inputArray) {
		if (inputArray.length > COMMAND_LENGTH) {
			return "To get help please just type 'help'";
		}
		return null;
	}
}
