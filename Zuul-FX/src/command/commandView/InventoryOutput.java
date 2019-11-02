package command.commandView;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.InventoryController;
import zuulutils.ZuulTools;

public class InventoryOutput extends InventoryController implements ICommandOutput {

	public void init(String[] inputArray) {
		String error = super.validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		IOHandler.output.println("You are currently carrying: ");
		inventory.forEach(s -> IOHandler.output.println(ZuulTools.capitalize(s.getName()) + "(" + s.getWeight() + ")"));

	}
}
