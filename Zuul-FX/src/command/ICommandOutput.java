package command;

/**
 * Interface for handling the output to the user when a command succeeds or not.
 * Could output text, play an animation, play a sound etc. The class must have
 * the suffix "Output", e.g. LookOutput. Commands available to user should be
 * placed in commandController/commandOutput. Commands (events) reserved for the game to
 * run directly should be placed in
 * game.eventController/game.eventOutput.
 * 
 * @author Steve
 *
 */
public interface ICommandOutput {
	/**
	 * Generally should call the super's validateUserInput() method and after
	 * checking no error has been returned should call the super's execute() method.
	 * After this the desired result can be output to the user.
	 * 
	 * @param inputArray
	 */
	public void init(String[] inputArray);
}
