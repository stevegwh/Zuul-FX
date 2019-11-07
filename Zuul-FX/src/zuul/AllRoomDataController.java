package zuul;

import java.util.Map;

import csvLoader.Parser;

public class AllRoomDataController {
	private Map<String, Room> rooms;
	private Room currentRoom;
	
	
	public void setNewCurrentRoom(String name) {
		currentRoom = rooms.get(name);
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public Room getRoom(String name) {
		return rooms.get(name);
	}
	
	public AllRoomDataController() {
		Parser csvParser = new Parser("roomData.csv");
		rooms = csvParser.loadCSV();
	}
}
