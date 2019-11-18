package zuul;

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
	/**
	 * Same process as parsing through the CSV file and converting to a List of
	 * Lists, this function takes a List of Lists and forms Room objects from them.
	 */
	private Function<ObservableList<CSVCell>, Room> mapToItem = (line) -> {
		// Change ObservableList<CSVCell> into List<String> to make it easier to parse
		List<String> p = line.stream().map(e -> e.getProperty().getValue()).collect(Collectors.toList());
		Map<String, String> headers = new HashMap<String, String>();
		for (CSVCell ele : line) {
			headers.put(ele.getHeader().getName(), ele.getProperty().getValue());
		}
		

		Room room = new Room();
		room.setName(headers.get("NAME"));
		room.setDescription(headers.get("DESCRIPTION"));
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
		// TODO: Implement this after you fix the bug where they are set to 2x ITEMNAME 2x ITEMWEIGHT
		Map<String, String> items = headers.entrySet().stream().filter(e-> "ITEMNAME".equals(e.getKey()) || "ITEMWEIGHT".equals(e.getKey())).collect(Collectors.toMap(e->e.getKey(), e-> e.getValue()));
		items.keySet().forEach(e->System.out.println(e));
		for (int i = 6; i < p.size(); i += 2) {
			// TODO: Check if the former CSVCell held a value or not before converting to an item.
			// Might not be necessary now after the CSV editor.
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
		rooms = csvData.stream().map(mapToItem).collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));

	}

}
