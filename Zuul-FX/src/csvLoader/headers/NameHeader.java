package csvLoader.headers;

public class NameHeader extends Header {
	public NameHeader() {
		super("NAME", new int[] { 1, 1 });
	}

	@Override
	public String validateFieldText(String textFieldValue) {
		if (!textFieldValue.matches("\\w+")) {
			return "Field must be one word in length with no spaces.";
		}
		return null;
	}
}
