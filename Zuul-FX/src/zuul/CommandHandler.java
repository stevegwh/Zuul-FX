package zuul;

import java.util.Arrays;
import java.util.HashMap;

import command.*;

/**
 * Responsible for passing the command forward to the CommandInstatiator and
 * executing it. If the class has already been instantiated then it is stored in
 * the commands HashMap for later use.
 * 
 * @author Steve
 *
 */
public class CommandHandler {

	HashMap<String, ICommandOutput> commands = new HashMap<>();

	/**
	 * Takes the first element of the inputArray and attempts to instantiate it.
	 * 
	 * @param inputArray the input produced by the user
	 */
	public void handleCommand(String[] inputArray) {
		inputArray = Arrays.stream(inputArray).map(e -> e.toLowerCase()).toArray(String[]::new);
		String commandName = inputArray[0];
		if (!commands.containsKey(commandName)) {
			CommandInstantiator instantiator = new CommandInstantiator();
			Object command = instantiator.createInstance(commandName);
			if (command != null) {
				commands.put(commandName, (ICommandOutput) command);
				((ICommandOutput) command).init(inputArray);
			}
		} else {
			commands.get(commandName).init(inputArray);
		}
	}
}
