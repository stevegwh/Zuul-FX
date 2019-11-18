package csvLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Parses through the provided CSV file and maps each object to the function
 * passed in.
 * 
 * @author Steve
 *
 */
public class CSVParser {
	public List<Object> loadCSV(Function<Object, Object> mapToItem, String path) {
		File inputF = new File(path);

		InputStream inputFS = null;
		try {
			inputFS = new FileInputStream(inputF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

		List<Object> records = br.lines().map(mapToItem).collect(Collectors.toList());

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

	public CSVParser() {
	}

}
