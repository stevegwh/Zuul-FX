package csvLoader.headers;

public class DescriptionHeader extends Header {
	private static int INDEX = 1;

	public DescriptionHeader() {
		super(HeaderEnum.DESCRIPTION);
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
