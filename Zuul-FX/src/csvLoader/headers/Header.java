package csvLoader.headers;

public abstract class Header {

	private final String name;
	private final int[] indexRange;

	public String getName() {
		return name;
	}

	public abstract String validateFieldText(String textFieldValue);

	public int[] getIndexRange() {
		return indexRange;
	}

	public Header(String name, int[] indexRange) {
		this.name = name;
		this.indexRange = indexRange;
	}
}
