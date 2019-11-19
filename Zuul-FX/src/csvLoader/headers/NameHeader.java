package csvLoader.headers;

public class NameHeader extends Header {
	private static final int INDEX = 0;
	public NameHeader() {
		super(HeaderEnum.NAME);
	}

	@Override
	public String validateFieldText(String textFieldValue) {
		if (!textFieldValue.matches("\\w+")) {
			return "Field must be one word in length with no spaces.";
		}
		return null;
	}

	public static boolean matchesIndexCondition(int csvIndex) {
		return csvIndex == INDEX;
	}
}
