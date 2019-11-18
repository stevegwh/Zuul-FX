package csvLoader.headers;

public class ItemNameHeader extends Header {
	public ItemNameHeader() {
		super("ITEMNAME");
	}

	@Override
	public String validateFieldText(String textFieldValue) {
		if (!textFieldValue.matches("\\w+")) {
			return "Field must be one word in length with no spaces.";
		}
		return null;
	}

	public static boolean matchesIndexCondition(int csvIndex) {
		return csvIndex >= 6 && csvIndex % 2 == 0;
	}
}
