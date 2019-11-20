package command.commandView;

import command.ICommandOutput;
import command.commandController.TakeController;
import view.IOHandler;

public class TakeOutput extends TakeController implements ICommandOutput {
	public void init(String[] inputArray) {
		String error = super.validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if (super.execute(inputArray)) {
			IOHandler.output.println("You picked up " + toTake);
		}
	}
}
