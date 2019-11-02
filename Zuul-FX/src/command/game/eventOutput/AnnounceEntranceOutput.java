package command.game.eventOutput;

import IO.IOHandler;
import command.ICommandOutput;

/**
 * Used to output to the user that another player or NPC has entered the room.
 * 
 * @author Steve
 *
 */
public class AnnounceEntranceOutput implements ICommandOutput {

	@Override
	public void init(String[] inputArray) {
		String name = inputArray[0];
		IOHandler.output.println(name + " enters the room.");
		IOHandler.output.printf(">> ");
	}

}
