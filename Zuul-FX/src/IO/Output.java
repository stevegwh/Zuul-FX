package IO;

public interface Output {

	/**
	 * Prints specified string with \n character.
	 * 
	 * @param ele The string you want to print.
	 */
	public void println(String ele);

	/**
	 * Prints specified string without \n character.
	 * 
	 * @param ele The string you want to print.
	 */
	public void printf(String ele);

	/**
	 * Prints specified string formatted for character speech.
	 * 
	 * @param ele The string you want to print.
	 */
	public void printCharDialog(String ele);

	/**
	 * Prints specified string formatted for displaying errors.
	 * 
	 * @param ele The string you want to print.
	 */
	public void printError(String error);
}
