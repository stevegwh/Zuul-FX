package csvLoader;

import csvLoader.headers.Header;
import csvLoader.headers.HeaderEnum;
import csvLoader.headers.HeaderFactory;
import csvLoader.headers.ItemNameHeader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tooltip;

// TODO: Figure out a way to have this hold the TextView so that you can add the tooltips etc directly
public class CSVEditorCell {
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
			setTooltipText(header.getEnum().name());
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

	public boolean hasError() {
		return header.validateFieldText(prop.getValue()) != null;
	}

	public CSVEditorCell(String value, int idx) {
		HeaderFactory headerFactory = new HeaderFactory(idx);
		header = headerFactory.getHeader();
		if (header.getEnum().equals(HeaderEnum.ITEMNAME)) {
			((ItemNameHeader) header).setItemPair(idx);
		}
//		System.out.println("Given header of: " + header.getEnum());
		getProperty().set(value);
		tooltip = new Tooltip();
	}


}
