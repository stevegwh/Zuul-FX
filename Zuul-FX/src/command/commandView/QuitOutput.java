package command.commandView;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.QuitController;
import zuul.GameController;

public class QuitOutput extends QuitController implements ICommandOutput {
	public void init(String[] inputArray) {
		String error = super.validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if (super.execute(inputArray)) {
			IOHandler.output.println("Thank you for playing. Good bye");
			GameController.quit();
		}
	}

}
