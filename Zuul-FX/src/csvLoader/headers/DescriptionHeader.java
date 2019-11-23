package csvLoader.headers;

public class DescriptionHeader extends Header {
	private static int INDEX = 1;

	public DescriptionHeader() {
		super(HeaderEnum.DESCRIPTION);
	}

	@Override
	public String validateFieldText(String textFieldValue) {
		if (textFieldValue.isEmpty()) {
			return "Field must not be blank.";
		}
		return null;
	}

	public static boolean matchesIndexCondition(int csvIndex) {
		return csvIndex == INDEX;
	}
}
