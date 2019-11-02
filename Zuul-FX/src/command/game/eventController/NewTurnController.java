package command.game.eventController;

import java.util.ArrayList;

import command.CommandController;
import zuul.GameController;
import zuul.Player;

/**
 * Sets the currentPlayer variable to the next Player object in the playerArr
 * array.
 */
public class NewTurnController extends CommandController {
	protected int idx;

	@Override
	protected String validateUserInput(String[] inputArray) {
		return null;
	}

	@Override
	protected boolean execute(String[] inputArray) {
		GameController.getCurrentPlayer().resetTurnCount();
		ArrayList<Player> playerArr = GameController.getPlayerArr();
		idx = playerArr.indexOf(GameController.getCurrentPlayer());
		idx += 1;
		if (idx >= playerArr.size()) {
			idx = 0;
		}
		Player player = playerArr.get(idx);
		GameController.setCurrentPlayer(player);
		return false;
	}

}
