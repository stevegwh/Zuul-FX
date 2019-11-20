package command.commandView;

import command.ICommandOutput;
import command.commandController.LookController;
import view.IOHandler;

/**
 * Called at the beginning of every game loop by GameController.
 * Outputs contents of the room and the room description.
 * 
 * @author Steve
 *
 */
public class LookOutput extends LookController implements ICommandOutput {

	public void init(String[] inputArray) {
		if (super.execute(inputArray)) {
			IOHandler.output.println(description);
			IOHandler.output.println(" ");
		}
	}
}
