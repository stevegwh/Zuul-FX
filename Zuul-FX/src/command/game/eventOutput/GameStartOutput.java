package command.game.eventOutput;

import IO.IOHandler;
import command.ICommandOutput;

/**
 * Used to output to the user the beginning of the game.
 * 
 * @author Steve
 *
 */
public class GameStartOutput implements ICommandOutput {

	@Override
	public void init(String[] inputArray) {
		IOHandler.output.println("");
		IOHandler.output.println("Welcome to the World of Zuul!");
		IOHandler.output.println("World of Zuul is a new, incredibly boring adventure game.");
		IOHandler.output.println("Type 'help' if you need help.");
		IOHandler.output.println("");
	}

}
