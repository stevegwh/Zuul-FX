package command.commandView;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.TakeController;
import zuul.GameController;

public class TakeOutput extends TakeController implements ICommandOutput {
	public void init(String[] inputArray) {
		String error = super.validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if (super.execute(inputArray)) {
			IOHandler.output.println("You picked up " + toTake);
//			IOHandler.output.addInvItem(toTake);
//			GameController.getCurrentPlayer().getInvModel().setLastEdited();
		}
	}
}
