package zuul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import csvLoader.CSVCell;
import javafx.collections.ObservableList;

/**
 * Responsible for managing all the Room objects in the game.
 * 
 * @author Steve
 *
 */
public class AllRoomDataController {
	private Function<ObservableList<CSVCell>, Room> mapToItem = (line) -> {
		// Change ObservableList<CSVCell> into List<String> to make it easier to parse
		List<String> p = new ArrayList<String>();
		line.forEach(e->p.add(e.getProperty().getValue()));

		Room room = new Room();
		room.setName(p.get(0));
		room.setDescription(p.get(1));
		HashMap<String, String> exits = new HashMap<>();
		if (!p.get(2).equals("null")) {
			exits.put("north", p.get(2));
		}
		if (!p.get(3).equals("null")) {
			exits.put("east", p.get(3));
		}
		if (!p.get(4).equals("null")) {
			exits.put("south", p.get(4));
		}
		if (!p.get(5).equals("null")) {
			exits.put("west", p.get(5));
		}
		room.setExits(exits);
		// TODO: Return error if there is no weight
		for (int i = 6; i < p.size(); i += 2) {
			// Check if the former CSVCell held a value or not before converting to an item.
			if (!p.get(i).isEmpty()) {
				TakeableItem takeableItem = new TakeableItem(p.get(i), Integer.parseInt(p.get(i + 1)));
				room.addTakeableItem(takeableItem);
			}
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

	public AllRoomDataController(List<ObservableList<CSVCell>> csvData) {
//		CSVParser csvParser = new CSVParser();
		
		rooms = csvData.stream().map(mapToItem)
				.collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));

		// TODO: This needs to load a List<List<String>> (Or ObservableList), not take the raw CSV file.
		// This means even the default game should first build a List<List<String>> (Or we can hard code it)
		// before it gets processed into rooms.
//		rooms = csvParser.loadCSV(mapToItem, path).stream()
//				.collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));
	}

}
