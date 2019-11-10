package zuul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import csvLoader.CSVParser;

public class AllRoomDataController {
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

	public int removeAllWithoutExit() {
		List<String> toRemove = rooms.keySet().stream().filter(e -> rooms.get(e).getExits().size() == 0)
				.collect(Collectors.toList());
		int amountRemoved = rooms.size();
		toRemove.forEach(e -> rooms.remove(e));
		return amountRemoved - rooms.size();
	}

	public void removeAllWithoutItems() {
		// Get list of room names to remove
		List<String> toRemove = rooms.keySet().stream().filter(e -> rooms.get(e).getTakeableItems().size() == 0)
				.collect(Collectors.toList());

		// Remove the rooms without items from the main room map
		toRemove.forEach(e -> rooms.remove(e));

		// Remove all references to the removed rooms
		for (String roomName : rooms.keySet()) {
			Room room = rooms.get(roomName);
			HashMap<String, String> exits = room.getExits();
			for (String toRemoveName : toRemove) {
				// Get entries with values that reference any removed room
				Set<String> tmp = exits.entrySet().stream().filter(e -> !e.getValue().equals(toRemoveName))
						.map(Map.Entry::getKey).collect(Collectors.toSet());
				// Get keys of the values that reference the removed rooms
				List<String> keysToRemove = exits.keySet().stream().filter(k -> !tmp.contains(k))
						.collect(Collectors.toList());
				keysToRemove.forEach(e -> exits.remove(e));
				// Overwrite the exits HashMap with the new one
				room.setExits(exits);
			}
		}
	}

	public void addItemToAllRooms(TakeableItem item) {
		rooms.keySet().stream().forEach(e -> rooms.get(e).addTakeableItem(item));
	}

	public AllRoomDataController(String path) {
		CSVParser csvParser = new CSVParser();

		rooms = csvParser.loadCSV(mapToItem, path).stream()
				.collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));
	}

}
