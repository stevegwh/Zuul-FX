package csvLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads the CSV into Lists for the CSVEditor to use.
 * @author Steve
 *
 */
public class CSVEditorLoader {
	/**
	 * Recipe to create the rows from the CSV file.
	 */
	private Function<Object, Object> getRow = (line) -> {
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
	public CSVEditorLoader(String path) {
		CSVParser csvParser = new CSVParser();

		rooms = csvParser.loadCSV(getRow, path).stream().map(e -> (List<String>) e).collect(Collectors.toList());

	}

}
