package command.commandView;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.LookController;
import zuul.GameController;

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
			IOHandler.output.printf("People in room: ");
			actors.forEach(e -> IOHandler.output.printf(e.getName() + ", "));
			IOHandler.output.println(" ");
			if (items != null) {
				IOHandler.output.printf("Items in room: ");
				items.forEach(e-> IOHandler.output.println(e.getName()));
			} else {
				IOHandler.output.printf("No items in room");
			}
			GameController.getCurrentRoom().getAllDirections().forEach(e-> IOHandler.output.println(e));
			IOHandler.output.println(" ");
		}
	}
}
