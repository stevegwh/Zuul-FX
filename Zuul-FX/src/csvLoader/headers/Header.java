package csvLoader.headers;

public abstract class Header {
	private final HeaderEnum name;

	public HeaderEnum getEnum() {
		return name;
	}

	public abstract String validateFieldText(String textFieldValue);

	public Header(HeaderEnum name) {
		this.name = name;
	}
}
