package zuul;

import java.util.ArrayList;
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
		ArrayList<String> toRemove = new ArrayList<>();
		for (String roomName : rooms.keySet()) {
			Room room = rooms.get(roomName);
			if (room.getExits().size() == 0) {
				toRemove.add(roomName);
			}
		}
		for (String roomName : toRemove) {
			int amountRemoved = rooms.size();
			rooms.remove(roomName);
			amountRemoved -= rooms.size();
			return amountRemoved;
		}
		return 0;
	}

	public void removeAllWithoutItems() {
		ArrayList<String> toRemove = new ArrayList<>();
		for (String roomName : rooms.keySet()) {
			Room room = rooms.get(roomName);
			if (room.getTakeableItems().size() == 0) {
				toRemove.add(roomName);
			}
		}

		for (String roomName : toRemove) {
			rooms.remove(roomName);
		}

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
		for (String roomName : rooms.keySet()) {
			Room room = rooms.get(roomName);
			room.addTakeableItem(item);
		}
	}

	public AllRoomDataController(String path) {
		CSVParser csvParser = new CSVParser();

		rooms = csvParser.loadCSV(mapToItem, path).stream()
				.collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));
	}

}
