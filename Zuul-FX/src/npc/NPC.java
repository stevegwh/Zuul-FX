package npc;

import java.util.ArrayList;
import java.util.Random;

import IO.IOHandler;
import command.game.eventOutput.AnnounceEntranceOutput;
import zuul.GameController;
import zuul.Room;

public class NPC {
	private String name;
	private String currentLocation = "pub";
	private String validItem;
	private ArrayList<String> dialogOptions;
	private ArrayList<String> dialogResponses;

	private String getUserDialogChoice() {
//		String[] inputArray = IOHandler.input.getUserInput();
//		return inputArray[0];
		return "";
	}

	public boolean onGive(String takeableItem) {
		return true;
	}

	public void update() {
		if (!currentLocation.equals(GameController.getCurrentPlayer().getLocation())) {
			move();
		}
	}

	public void printDialog() {
		for (int i = 0, len = dialogOptions.size(); i < len; i++) {
			String option = dialogOptions.get(i);
			IOHandler.output.printCharDialog(Integer.toString(i + 1) + ": " + option);
		}
	}

	public void onTalk() {
		printDialog();
		String userChoice = getUserDialogChoice();
		if (userChoice.length() > 1 || userChoice.matches("d")) {
			IOHandler.output.printError("Invalid Command");
			return;
		}
		int idx = Integer.parseInt(userChoice) - 1;
		IOHandler.output.printCharDialog((dialogResponses.get(idx)));
	}

	public String getRandomRoom() {
		Room currentRoom = GameController.getAllRoomDataController().getRoom(currentLocation);
		ArrayList<String> exits = currentRoom.getAllDirections();
		Random generator = new Random();
		int randomIndex = generator.nextInt(exits.size());
		String direction = exits.get(randomIndex);
		return currentRoom.getExit(direction);
	}

	/**
	 * Updates the actorsInRoom field of the destination room and the room specified
	 * in the NPC's currentLocation field.
	 * 
	 */
	public void move() {
		String destinationRoomName = getRandomRoom();
		GameController.getAllRoomDataController().getRoom(destinationRoomName).addActor(this);
		GameController.getAllRoomDataController().getRoom(currentLocation).removeActor(this);
		currentLocation = destinationRoomName;
		if (destinationRoomName.equals(GameController.getCurrentPlayer().getLocation())) {
			AnnounceEntranceOutput announcement = new AnnounceEntranceOutput();
			announcement.init(new String[] { name });
		}
	}

	/**
	 * validItem is the name TakeableItem that this NPC accepts. For example, the
	 * NPC 'John' could accept the TakeableItem 'Gum' Can return null if the NPC
	 * doesn't accept any TakeableItems. Returns the name only, not the object
	 * itself.
	 * 
	 * @return the name of the TakeableItem that this NPC accepts.
	 */
	public String getValidItem() {
		return validItem;
	}

	public void setValidItem(String validItem) {
		this.validItem = validItem;
	}

	/**
	 * @return The name of this NPC.
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The current location of this NPC.
	 */
	public String getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * @param newLocation The new location for the NPC.
	 */
	public void setCurrentLocation(String newLocation) {
		currentLocation = newLocation;
	}

	public void setDialog(ArrayList<String> dialogOptions, ArrayList<String> dialogResponses) {
		this.dialogOptions = dialogOptions;
		this.dialogResponses = dialogResponses;
	}

	public NPC() {
	}

}
