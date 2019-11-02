package command;

/**
 * Abstract base class for all Commands in the game.
 * 
 * @author Steve
 *
 */
public abstract class CommandController {
	/**
	 * Method to check all necessary components of the sentence are in place. Used
	 * to safeguard against game crashes due to badly formed sentences. Please note:
	 * the validity of the command name itself has been already checked in the
	 * CommandInstantiator class.
	 * 
	 * @param inputArray An array containing the sentence entered by user.
	 * @return An error message or null.
	 */
	abstract protected String validateUserInput(String[] inputArray);

	/**
	 * Executes the command's functionality.
	 * 
	 * @param inputArray An array containing the sentence entered by user.
	 * @return true if the command was successful
	 */
	abstract protected boolean execute(String[] inputArray);
}