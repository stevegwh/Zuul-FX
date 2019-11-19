package csvLoader.headers;

public class ItemWeightHeader extends Header {
	public ItemWeightHeader() {
		super(HeaderEnum.ITEMWEIGHT);
	}

	@Override
	public String validateFieldText(String textFieldValue) {
		if (!textFieldValue.matches("\\d+")) {
			return "Field must be a number with no spaces.";
		}
		return null;
	}

	public static boolean matchesIndexCondition(int csvIndex) {
		return csvIndex > 6 && csvIndex % 2 != 0;
	}
}
