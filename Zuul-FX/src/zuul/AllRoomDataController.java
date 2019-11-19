package zuul;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import csvLoader.CSVCell;
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
	 * Same process as parsing through the CSV file and converting to a List of
	 * Lists, this function takes a List of Lists and forms Room objects from them.
	 */
	private Function<ObservableList<CSVCell>, Room> mapToItem = (line) -> {
		Room room = new Room();
		
		line.removeIf(e-> e.getProperty().getValue().isEmpty());

		String name = line.stream().filter(e->e.getHeader().getName().equals(HeaderEnum.NAME)).map(e-> e.getProperty().getValue()).findFirst().orElse("null");
		room.setName(name);

		String description = line.stream().filter(e->e.getHeader().getName().equals(HeaderEnum.DESCRIPTION)).map(e-> e.getProperty().getValue()).findFirst().orElse("null");
		room.setDescription(description);

		Map<String, String> exits = line.stream()
				.filter(e-> e.getHeader().getName().equals(HeaderEnum.DIRECTION))
				.filter(e-> !e.getProperty().getValue().equals("null"))
				.collect(Collectors.toMap(e-> ((DirectionHeader) e.getHeader()).getDirection(), e-> e.getProperty().getValue()));
		exits.entrySet().forEach(e-> System.out.println("K: " + e.getKey() + " V: " + e.getValue()));

		room.setExits(exits);

		List<CSVCell> items = line.stream().filter(e -> e.getHeader().getName().equals(HeaderEnum.ITEMNAME))
				.collect(Collectors.toList());
		items.forEach(e -> room.addTakeableItem(new TakeableItem(e.getProperty().getValue(),
				Integer.parseInt(line.get(((ItemNameHeader) e.getHeader()).getItemPair()).getProperty().getValue()))));
		
		
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
