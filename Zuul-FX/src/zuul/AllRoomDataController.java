package zuul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import csvEditor.CSVEditorCell;
import javafx.collections.ObservableList;
import csvLoader.headers.HeaderEnum;
import csvLoader.headers.ItemNameHeader;
import csvLoader.headers.DirectionHeader;

/**
 * Responsible for managing all the Room objects in the game.
 * 
 * @author Steve
 *
 */
public class AllRoomDataController {
	/**
	 * Same process as parsing through the CSV file for the CSV editor by converting
	 * to a List of Lists, this function takes a List of CSVEditorCells and forms
	 * Room objects from them.
	 */
	private Function<ObservableList<CSVEditorCell>, Room> customMap = (line) -> {
		Room room = new Room();

		line.removeIf(e -> e.getProperty().getValue().isEmpty());

		String name = line.stream().filter(e -> e.getHeader().getEnum().equals(HeaderEnum.NAME))
				.map(csvCell -> csvCell.getProperty().getValue()).findFirst().orElse(null);
		if (name.equals(null)) {
			System.err.println("Unable to map the name from the following row: " + line);
		}
		room.setName(name);
		if (GameController.getStartLocation() == null) {
			GameController.setStartLocation(name);
		}

		String description = line.stream().filter(e -> e.getHeader().getEnum().equals(HeaderEnum.DESCRIPTION))
				.map(csvCell -> csvCell.getProperty().getValue()).findFirst().orElse(null);
		if (description.equals(null)) {
			System.err.println("Unable to map the description from the following row: " + line);
		}
		room.setDescription(description);

		Map<String, String> exits = line.stream().filter(e -> e.getHeader().getEnum().equals(HeaderEnum.DIRECTION))
				.filter(e -> !e.getProperty().getValue().equals("null")).collect(Collectors
						.toMap(e -> ((DirectionHeader) e.getHeader()).getDirection(), e -> e.getProperty().getValue()));
//		exits.entrySet().forEach(e -> System.out.println("K: " + e.getKey() + " V: " + e.getValue()));

		room.setExits(exits);

		List<CSVEditorCell> items = line.stream().filter(e -> e.getHeader().getEnum().equals(HeaderEnum.ITEMNAME))
				.collect(Collectors.toList());
		// Get's the index of its item weight pair.
		items.forEach(e -> room.addTakeableItem(new TakeableItem(e.getProperty().getValue(),
				Integer.parseInt(line.get(((ItemNameHeader) e.getHeader()).getItemPair()).getProperty().getValue()))));

		return room;

	};

	/**
	 * Forms rooms from the default CSV file. As the data has not been converted to
	 * CSVEditorCells we just pass in the direct hard-coded index values of the CSV.
	 */
	private Function<List<String>, Room> defaultMap = (line) -> {
		Room room = new Room();
		room.setName(line.get(0));
		if (GameController.getStartLocation() == null) {
			GameController.setStartLocation(line.get(0));
		}
		room.setDescription(line.get(1));
		Map<String, String> exits = new HashMap<>();
		if (!line.get(2).equals("null")) {
			exits.put("north", line.get(2));
		}
		if (!line.get(3).equals("null")) {
			exits.put("east", line.get(3));
		}
		if (!line.get(4).equals("null")) {
			exits.put("south", line.get(4));
		}
		if (!line.get(5).equals("null")) {
			exits.put("west", line.get(5));
		}
		room.setExits(exits);
		for (int i = 6; i < line.size(); i += 2) {
			if (!line.get(i).isEmpty()) {
				TakeableItem takeableItem = new TakeableItem(line.get(i), Integer.parseInt(line.get(i + 1)));
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

	public Map<String, Room> getAllRooms() {
		return rooms;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public Room getRoom(String name) {
		return rooms.get(name);
	}

	@SuppressWarnings("unchecked")
	public AllRoomDataController(List<?> csvData, GameType game) {
		if (game.equals(GameType.DEFAULT)) {
			rooms = ((List<List<String>>) csvData).stream().map(defaultMap)
					.collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));
		} else if (game.equals(GameType.CUSTOM)) {
			rooms = ((List<ObservableList<CSVEditorCell>>) csvData).stream().map(customMap)
					.collect(Collectors.toMap(e -> ((Room) e).getName(), e -> (Room) e));
		}

	}

}
