package npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import csvLoader.CSVParser;
import zuul.GameController;
import zuul.Room;

public class AllNPCDataController {
	private Map<String, NPC> npcs;
	/**
	 * Loads in the NPC from the CSV file
	 */
	private Function<Object, Object> mapToItem = (line) -> {
		String[] p = ((String) line).split(", ");
		NPC npc = new NPC();

		npc.setName(p[0]);
		npc.setValidItem(p[1]);
		npc.setImagePath(p[2]);
		ArrayList<String> dialogOptions = new ArrayList<>();
		ArrayList<String> dialogResponses = new ArrayList<>();
		for (int i = 3; i < p.length; i += 2) {
			dialogOptions.add(p[i]);
			dialogResponses.add(p[i + 1]);
		}
		npc.setDialog(dialogOptions, dialogResponses);
		// Get's a location from the available rooms
		Map<String, Room> rooms = GameController.getAllRoomDataController().getAllRooms();
		List<String> list = new ArrayList<String>(rooms.keySet());
		npc.setCurrentLocation(list.get(0));
		return npc;
	};

	public NPC getNPC(String name) {
		return npcs.get(name);
	}

	/**
	 * @return a list of all NPC's names
	 */
	public List<String> getAllNPCS() {
		return npcs.keySet().stream().collect(Collectors.toList());
	}

	public AllNPCDataController() {
		CSVParser csvParser = new CSVParser();
		String path = "src/npc/npcData.csv";
		npcs = csvParser.loadCSV(mapToItem, path).stream()
				.collect(Collectors.toMap(e -> ((NPC) e).getName(), e -> (NPC) e));
	}

}
