package csvLoader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tooltip;

public class CSVCell {
	HeaderType header;
	private Tooltip tooltip;
	private String style = "";
	private StringProperty prop = new SimpleStringProperty();

	public enum HeaderType {
		NAME, DESCRIPTION, DIRECTION, ITEMNAME, ITEMWEIGHT;
	}

	public void checkValidity() {
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

		if (!prop.getValue().matches(regex)) {
			style = "-fx-background-color: orange;";
			setTooltipText("Error Message");
		} else {
			style = "";
			setTooltipText(header.name());
		}
	}

	public Tooltip getTooltip() {
		return tooltip;
	}

	public String getStyle() {
		return style;
	}

	public void setTooltipText(String text) {
		tooltip.setText(text);
	}

	private void setHeader(int idx) {
		if (idx == 0) {
			header = HeaderType.NAME;
		} else if (idx == 1) {
			header = HeaderType.DESCRIPTION;
		} else if (idx == 2) {
			header = HeaderType.ITEMNAME;
		} else if (idx >= 3 && idx <= 6) {
			header = HeaderType.DIRECTION;
		} else if (idx > 6) {
			if (idx % 2 == 0) {
				header = HeaderType.ITEMNAME;
			} else {
				header = HeaderType.ITEMWEIGHT;
			}
		}
	}

	public HeaderType getHeader() {
		return header;
	}

	public StringProperty getProperty() {
		return prop;
	}

	public CSVCell(String value, int idx) {
		getProperty().set(value);
		setHeader(idx);
		tooltip = new Tooltip();
	}

}
