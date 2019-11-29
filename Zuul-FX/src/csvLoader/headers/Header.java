package csvLoader.headers;

/**
 * Parent class for all 'headers' in the CSV file. All subclasses should also
 * have a static "matchesIndexCondition" method which validates what index in
 * the list a certain header should appear in. For example, name could expect to
 * be at index 0, directions from 2-5 etc.
 * 
 * @author Steve
 *
 */
public abstract class Header {
	private final HeaderEnum name;

	public HeaderEnum getEnum() {
		return name;
	}

	/**
	 * Validates the text the user inputs in the CSV editor.
	 * @param textFieldValue the text the user is currently entering.
	 * @return null if no error, error tooltip if so.
	 */
	public abstract String validateFieldText(String textFieldValue);

	public Header(HeaderEnum name) {
		this.name = name;
	}

}
