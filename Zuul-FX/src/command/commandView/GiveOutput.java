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
			IOHandler.output.println("You gave the " + itemName + " to " + npc.getName());
			return;
		} else {
			IOHandler.output.println(npc.getName() + " didn't seem to want the " + itemName);
		}
	}
}
