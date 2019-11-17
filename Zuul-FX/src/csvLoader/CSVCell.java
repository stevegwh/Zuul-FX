package csvLoader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CSVCell {
	private StringProperty prop = new SimpleStringProperty();
	private String val;

	public StringProperty getProperty() {
		return prop;
	}

	public String getValue() {
		return val;
	}

	public CSVCell(String value) {
		getProperty().set(value);
		val = getProperty().get();
	}
}
