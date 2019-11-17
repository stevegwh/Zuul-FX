package csvLoader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CSVCell {
	HeaderType header;
	private StringProperty prop = new SimpleStringProperty();

	public enum HeaderType {
		NAME, DESCRIPTION, DIRECTION, ITEMNAME, ITEMWEIGHT;
	}

	public boolean checkValidity() {
		String regex;
		switch (header) {
		case NAME:
		case DIRECTION:
		case ITEMNAME:
			regex = "\\w+";
			break;
		case DESCRIPTION:
			regex = "[a-zA-Z0-9]+";
			break;
		case ITEMWEIGHT:
			regex = "[0-9]+";
			break;
		default:
			regex = null;
			break;
		}

		return prop.getValue().matches(regex);
	}

	private HeaderType setHeader(int idx) {

		if (idx == 0) {
			return HeaderType.NAME;
		} else if (idx == 1) {
			return HeaderType.DESCRIPTION;
		} else if (idx == 2) {
			return HeaderType.ITEMNAME;
		} else if (idx >= 3 && idx <= 6) {
			return HeaderType.DIRECTION;
		} else if (idx > 6) {
			if (idx % 2 == 0) {
				return HeaderType.ITEMNAME;
			} else {
				return HeaderType.ITEMWEIGHT;
			}
		}

		return null;
	}

	public HeaderType getHeader() {
		return header;
	}

	public StringProperty getProperty() {
		return prop;
	}

	public CSVCell(String value, int idx) {
		getProperty().set(value);
		this.header = setHeader(idx);
	}
}
