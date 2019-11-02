package command.commandController;

import command.CommandController;
import command.game.eventOutput.NewTurnOutput;
import zuul.GameController;

/**
 * Updates CurrentRoomJSON in RoomModel to the next room. If game is multiplayer
 * then the active player has their turn count reduced. If their turn count
 * reaches zero then the active player is changed to the next on the list.
 * 
 * @author Steve
 *
 */
public class GoController extends CommandController {
	protected String direction;
	protected String nextRoom;
	protected int turnsLeft;
	private int COMMAND_LENGTH = 2;

	private enum Directions {
		NORTH, SOUTH, EAST, WEST
	}

	@Override
	public String validateUserInput(String[] inputArray) {
		if (inputArray.length > COMMAND_LENGTH) {
			return "Invalid Command";
		}
		direction = inputArray[1].toLowerCase();
		if (!isValidDirection(direction.toUpperCase())) {
			return "Invalid Direction";
		}
		nextRoom = GameController.getRoomModel().getExit(direction, GameController.getCurrentPlayer().getLocation());
		if (nextRoom == null) {
			return "You can't go that way.";
		}
		return null;
	}

	private boolean isValidDirection(String userInput) {
		for (Directions c : Directions.values()) {
			if (c.name().equals(userInput)) {
				return true;
			}
		}
		return false;
	}

	public boolean execute(String[] inputArray) {
		GameController.getRoomModel().setNewRoom(nextRoom);
		GameController.getCurrentPlayer().setLocation(nextRoom);
		if (!GameController.getSinglePlayer()) {
			if (GameController.getCurrentPlayer().getTurnCount() > 0) {
				GameController.getCurrentPlayer().decTurnCount();
				turnsLeft = GameController.getCurrentPlayer().getTurnCount();
				return true;
			} else {
				NewTurnOutput newTurn = new NewTurnOutput();
				newTurn.init(new String[] {});
				return false;
			}
		}
		return true;
	}
}