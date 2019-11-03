package command.commandView;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.DropController;
import zuul.GameController;

public class DropOutput extends DropController implements ICommandOutput {
	public void init(String[] inputArray) {
		String error = validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if (execute(inputArray)) {
			IOHandler.output.println("You dropped " + toDrop);
			IOHandler.output.removeInvItem(toDrop);
//			GameController.getCurrentPlayer().getInvModel().setLastEdited();
		}
	}

}
