package command.commandView;

import command.ICommandOutput;
import command.commandController.TalkController;
import view.IOHandler;

public class TalkOutput extends TalkController implements ICommandOutput {
	public void init(String[] inputArray) {
		String error = validateUserInput(inputArray);
		if (error != null) {
			IOHandler.output.printError(error);
			return;
		}
		if (super.execute(inputArray)) {
//			actor.onTalk();
		}
	}
}
