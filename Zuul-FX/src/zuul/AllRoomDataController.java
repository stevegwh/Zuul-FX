package zuul;

import java.util.HashMap;

import csvLoader.Parser;

public class AllRoomDataController {
	private HashMap<String, Room> rooms;
	private Room currentRoom;
	
	
	public void setNewRoom(String name) {
		currentRoom = rooms.get(name);
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public Room getRoom(String name) {
		return rooms.get(name);
	}
	
	public AllRoomDataController() {
		Parser csvParser = new Parser("/roomData.csv");
		rooms = csvParser.loadCSV();
	}
}
