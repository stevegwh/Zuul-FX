package csvLoader;

import csvLoader.headers.Header;
import csvLoader.headers.HeaderFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tooltip;

// TODO: Figure out a way to have this hold the TextView so that you can add the tooltips etc directly
public class CSVCell {
	Header header;
	private Tooltip tooltip;
	private String style = "";
	private StringProperty prop = new SimpleStringProperty();

	public void checkValidity() {
		String tooltip = header.validateFieldText(prop.getValue());
		if (tooltip != null) {
			style = "-fx-background-color: orange;";
			setTooltipText(tooltip);
		} else {
			style = "";
			setTooltipText(header.getName());
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

	public Header getHeader() {
		return header;
	}

	public StringProperty getProperty() {
		return prop;
	}

	public CSVCell(String value, int idx) {
		HeaderFactory headerFactory = new HeaderFactory(idx);
		header = headerFactory.getHeader();
		// TODO: Use enum for this.
		if (header.getName().equals("ITEMWEIGHT")) {
			
		}
		System.out.println("Given header of: " + header.getName());
		getProperty().set(value);
		tooltip = new Tooltip();
	}

}
