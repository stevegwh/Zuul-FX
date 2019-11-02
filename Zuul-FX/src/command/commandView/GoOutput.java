package command.commandView;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.GoController;
import zuul.GameController;

public class GoOutput extends GoController implements ICommandOutput {
	public void init(String[] inputArray) {
		String error = super.validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if(super.execute(inputArray)) {
			if(!GameController.getSinglePlayer()) {
				IOHandler.output.println(turnsLeft + " turns left.");
			}
		}
		LookOutput look = new LookOutput();
		look.init(inputArray);;
	}
}
