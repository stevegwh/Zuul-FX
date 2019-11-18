package csvLoader.headers;

public abstract class Header {
	private final String name;

	public String getName() {
		return name;
	}

	public abstract String validateFieldText(String textFieldValue);

	public Header(String name) {
		this.name = name;
	}
}
