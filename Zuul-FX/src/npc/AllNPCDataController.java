package npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import csvLoader.CSVParser;

public class AllNPCDataController {
	private Map<String, NPC> npcs;
	private Function<Object, Object> mapToItem = (line) -> {
		String[] p = ((String) line).split(", ");
		NPC npc = new NPC();

		npc.setName(p[0]);
		npc.setValidItem(p[1]);
		ArrayList<String> dialogOptions = new ArrayList<>();
		ArrayList<String> dialogResponses = new ArrayList<>();
		// TODO: Return error if there's no response
		for (int i = 2; i < p.length; i += 2) {
			dialogOptions.add(p[i]);
			dialogResponses.add(p[i + 1]);
		}
		npc.setDialog(dialogOptions, dialogResponses);
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
