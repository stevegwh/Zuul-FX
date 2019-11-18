package csvLoader.headers;

import java.util.List;

public class DirectionHeader extends Header {
	private static final List<Integer> INDEX_RANGE = List.of(2, 3, 4, 5);

	public DirectionHeader() {
		super("DIRECTION");
	}

	@Override
	public String validateFieldText(String textFieldValue) {
		if (!textFieldValue.matches("\\w+")) {
			return "Field must be one word in length with no spaces.";
		}
		return null;
	}

	public static boolean matchesIndexCondition(int csvIndex) {
		return INDEX_RANGE.contains(csvIndex);
	}
}
