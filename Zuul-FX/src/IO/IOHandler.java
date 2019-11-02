package IO;

/**
 * Class that routes I/O in the game. Defaults to 'Console' mode
 * (System.out/in). Assigns the input/output variables to the specified
 * implementation of Input/Output found in this package. I/O route is specified
 * by using the '--view' flag while executing the game. E.g. --view console or
 * --view gui.
 * 
 * @author Steve
 *
 */
public class IOHandler {
	public static Output output = new FXOutput();
	public static Input input = new ConsoleInput();
}
