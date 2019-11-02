package command.commandView;

import com.github.cliftonlabs.json_simple.JsonObject;

import IO.IOHandler;
import command.ICommandOutput;
import command.commandController.LookController;
import zuulutils.ZuulTools;

/**
 * Called at the beginning of every game loop by GameController.
 * Outputs contents of the room and the room description.
 * 
 * @author Steve
 *
 */
public class LookOutput extends LookController implements ICommandOutput {

	public void init(String[] inputArray) {
		if (super.execute(inputArray)) {
			IOHandler.output.println("---------");
			IOHandler.output.println(description);
			IOHandler.output.printf("People in room: ");
			actors.forEach((e) -> IOHandler.output.printf((String) e + ", "));
			IOHandler.output.println(" ");
			if (items != null) {
				IOHandler.output.printf("Items in room: ");
				items.forEach(e -> IOHandler.output.printf(ZuulTools.capitalize(((JsonObject) e).get("name").toString())
						+ "(" + ((JsonObject) e).get("weight").toString() + "), "));
			} else {
				IOHandler.output.printf("No items in room");
			}
			IOHandler.output.println(" ");
			IOHandler.output.printf("Exits: ");
			exits.forEach((k) -> IOHandler.output.printf(ZuulTools.capitalize((String) k) + ", "));
			IOHandler.output.println(" ");
			IOHandler.output.println("---------");
		}
	}
}
