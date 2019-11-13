package zuul;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import csvLoader.CSVParser;

/**
 * Responsible for managing all the Room objects in the game.
 * 
 * @author Steve
 *
 */
public class AllRoomDataController {
	/**
	 * Function that get's passed into the CSVLoader to instantiate all Room
	 * objects. This defines what we want to happen to every line in the CSV file in
	 * this context.
	 */
	private Function<Object, Object> mapToItem = (line) -> {
		String[] p = ((String) line).split(", ");
		Room room = new Room();
		room.setName(p[0]);
		room.setDescription(p[1]);
		HashMap<String, String> exits = new HashMap<>();
		if (!p[2].equals("null")) {
			exits.put("north", p[2]);
		}
		if (!p[3].equals("null")) {
			exits.put("east", p[3]);
		}
		if (!p[4].equals("null")) {
			exits.put("south", p[4]);
		}
		if (!p[5].equals("null")) {
			exits.put("west", p[5]);
		}
		room.setExits(exits);
		// TODO: Return error if there is no weight
		for (int i = 6; i < p.length; i += 2) {
			TakeableItem takeableItem = new TakeableItem(p[i], Integer.parseInt(p[i + 1]));
			room.addTakeableItem(takeableItem);
		}
		return room;
	};

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

	public AllRoomDataController(String path) {
		CSVParser csvParser = new CSVParser();

		rooms = csvParser.loadCSV(mapToItem, path).stream()
				.collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));
	}

}
