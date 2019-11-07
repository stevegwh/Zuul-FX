package csvLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


import zuul.Room;
import zuul.TakeableItem;

public class Parser {
	private String csvFile;

	private Function<String, Room> mapToItem = (line) -> {
		String[] p = line.split(", ");
		Room room = new Room();

		room.setName(p[0]);
		room.setDescription(p[1]);
		HashMap<String, String> exits = new HashMap<>();
		if (!p[2].equals("null")) {
			exits.put("north", p[2]);
		}
		if (!p[3].equals("null")) {
			exits.put("east", p[3]);
		}
		if (!p[4].equals("null")) {
			exits.put("south", p[4]);
		}
		if (!p[5].equals("null")) {
			exits.put("west", p[5]);
		}
		room.setExits(exits);
		// TODO: Return error if there is no weight
		for (int i = 6; i < p.length; i += 2) {
			TakeableItem takeableItem = new TakeableItem(p[i], Integer.parseInt(p[i + 1]));
			room.addTakeableItem(takeableItem);
		}
		return room;

	};

	public Map<String, Room> loadCSV() {
		// TODO: Hard coded file path
		File inputF = new File("/home/forest/git/Zuul-FX/Zuul-FX/src/csvLoader/roomData.csv");

		InputStream inputFS = null;
		try {
			inputFS = new FileInputStream(inputF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

		// skip the header of the csv

		Map<String, Room> records = br.lines().map(mapToItem).collect(Collectors.toMap(e->e.getName(), e-> e));

		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return records;
	}

	public Parser(String path) {
		this.csvFile = path;
	}

}
