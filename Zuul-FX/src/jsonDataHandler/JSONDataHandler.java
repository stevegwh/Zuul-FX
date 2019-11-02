package jsonDataHandler;

import com.github.cliftonlabs.json_simple.JsonObject;

// Important class that holds JSON jsonData
public class JSONDataHandler {
	private JsonObject jsonData;
	private Parser parser;

	public JsonObject getAllData() {
		return jsonData;
	}

	public String getJsonString() {
		return parser.serializeData(jsonData);
	}

	/**
	 * Queries the JsonObject for a certain field.
	 * 
	 * @param key The key of the JsonObject you wish to return
	 * @return
	 */
	public JsonObject getField(String key) {
		JsonObject fieldData = (JsonObject) jsonData.get(key);
		return fieldData;
	}

	/**
	 * Constructor for the JSONDataHandler. Requires you specify a path for the JSON
	 * file.
	 * 
	 * @param path The path of the JSON file in the project.
	 */
	public JSONDataHandler(String path) {
		parser = new Parser();
		jsonData = (JsonObject) parser.generateData(path);
	}
}