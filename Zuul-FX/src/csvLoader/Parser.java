package csvLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;

import zuul.Room;

public class Parser {
	private String csvFile;

	public HashMap<String, Room> loadCSV() {
		List<List<String>> records = new ArrayList<List<String>>();
		try (CSVReader csvReader = new CSVReader(new FileReader("roomData.csv"));) {
		    String[] values = null;
		    while ((values = csvReader.readNext()) != null) {
		        records.add(Arrays.asList(values));
		    }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (CsvValidationException e) {
			e.printStackTrace();
		}

		HashMap<String, Room> rooms = new HashMap<>();
		for (List<String> list : records) {
			Room room = new Room(list);
			System.out.println(room.getName());
			rooms.put(room.getName(), room);
		}
		return rooms;
	}
	
	
	public Parser(String path) {
		this.csvFile = path;
	}

}
