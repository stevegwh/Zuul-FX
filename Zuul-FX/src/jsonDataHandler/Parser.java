package jsonDataHandler;

import java.io.FileReader;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

/**
 * Responsible for deserializing a JSON file and returning it.
 * 
 * @author Steve
 *
 */
public final class Parser {
	private Object obj = new Object();

	/**
	 * Deserializes the JSON file
	 * 
	 * @param file Path of the JSON file
	 * @return The deserialized JSON object
	 */
	public Object generateData(String file) {
		try {
			obj = Jsoner.deserialize(new FileReader(file));
			return (JsonObject) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	public String serializeData(JsonObject json) {
		return Jsoner.serialize(json);
	}
}
