package command.game.eventOutput;

import IO.IOHandler;
import command.ICommandOutput;
import command.game.eventController.SingleOrMultiController;

/**
 * Asks the user to specify single or multi-player.
 * 
 * @author Steve
 *
 */
public class SingleOrMultiOutput extends SingleOrMultiController implements ICommandOutput {
	@Override
	public void init(String[] inputArray) {
		IOHandler.output.println("Please choose an option.");
		IOHandler.output.println("1. Singleplayer");
		IOHandler.output.println("2. Multiplayer");
		String[] choiceArr = IOHandler.input.getUserInput();
		String error = validateUserInput(choiceArr);
		if (error != null) {
			IOHandler.output.println(error);
			init(inputArray);
		} else {
			execute(inputArray);
		}
	}

}
