package npc;

import java.util.ArrayList;
import java.util.Random;

import command.game.eventOutput.AnnounceEntranceOutput;
import view.IOHandler;
import zuul.GameController;
import zuul.Room;

public class NPC {
	private String name;
	private String currentLocation = "pub";
	private String validItem;
	private String imagePath;
	private ArrayList<String> dialogOptions;
	private ArrayList<String> dialogResponses;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String ipath) {
		imagePath = ipath;
		
	}

	public boolean onGive(String takeableItem) {
		if (takeableItem.equals(validItem)) {
			IOHandler.output.println(name + " says: Oh, thank you for the " + takeableItem);
			GameController.getCurrentPlayer().getInvModel().removeItem(takeableItem);
			return true;
		} else {
			return false;
		}
	}

	public void update() {
		if (!currentLocation.equals(GameController.getCurrentPlayer().getLocation())) {
			move();
		}
	}

	public ArrayList<String> getDialogOptions() {
		return dialogOptions;
	}

	public ArrayList<String> getDialogResponses() {
		return dialogResponses;
	}

	public void onTalk() {
		IOHandler.output.initDialogView(this);
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
