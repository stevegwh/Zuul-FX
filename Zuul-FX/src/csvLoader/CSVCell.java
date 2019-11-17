package csvLoader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CSVCell {
	private StringProperty prop = new SimpleStringProperty();

	public StringProperty getProperty() {
		return prop;
	}

	public CSVCell(String value) {
		getProperty().set(value);
	}
}
