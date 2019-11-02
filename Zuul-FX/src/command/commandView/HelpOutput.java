package command.commandView;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.HelpController;

public class HelpOutput extends HelpController implements ICommandOutput {
	public void init(String[] inputArray) {
		String error = super.validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if (super.execute(inputArray)) {
			IOHandler.output.println("You are lost. You are alone. You wander around at the university");
			IOHandler.output.println(" ");
			IOHandler.output.println("Your command words are: ");
			String end = commands.get(commands.size() - 1);
			commands.remove(end);
			commands.forEach(e -> IOHandler.output.printf(e + ", "));
			IOHandler.output.printf(end + ".");
			IOHandler.output.println(" ");
		}
	}
}
