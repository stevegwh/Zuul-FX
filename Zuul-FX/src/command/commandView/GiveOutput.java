package command.commandView;

import command.ICommandOutput;
import command.commandController.GiveController;
import view.IOHandler;

public class GiveOutput extends GiveController implements ICommandOutput {

	public void init(String[] inputArray) {
		String error = super.validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if (super.execute(inputArray)) {
			return;
		} else {
			IOHandler.output.printError(npc.getName() + " didn't seem to want the " + itemName);
		}
	}
}
