package csvLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVEditor {
	// TODO: More than likely can be simplified
	private Function<Object, Object> mapToItem = (line) -> {
		String[] p = ((String) line).split(", ");
		List<String> list = new ArrayList<String>();
		for (String i : p) {
			list.add(i);
		}

		return list;
	};
	private List<List<String>> rooms;

	public List<List<String>> getRoomData() {
		return rooms;
	}

	@SuppressWarnings("unchecked")
	public CSVEditor(String path) {
		CSVParser csvParser = new CSVParser();

		rooms = csvParser.loadCSV(mapToItem, path).stream().map(e -> (List<String>) e).collect(Collectors.toList());

	}

}
