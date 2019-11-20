package command.game.eventOutput;

import command.ICommandOutput;
import command.game.eventController.InitPlayersController;
import view.IOHandler;

public class InitPlayersOutput extends InitPlayersController implements ICommandOutput {

	@Override
	public void init(String[] inputArray) {
		super.execute(inputArray);
		IOHandler.output.println("Player " + (idx + 1) + "'s turn");

	}

}
