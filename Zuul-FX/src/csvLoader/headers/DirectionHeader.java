package csvLoader.headers;

import java.util.List;
import java.util.stream.Collectors;

import csvLoader.CSVCell;
import javafx.collections.ObservableList;
import view.EditCSVController;

public class DirectionHeader extends Header {
	private static final List<Integer> INDEX_RANGE = List.of(2, 3, 4, 5);
	private final String direction;

	public DirectionHeader(int idx) {
		super(HeaderEnum.DIRECTION);
		switch(idx) {
		case 1:
			direction = "NORTH";
			break;
		case 2:
			direction = "SOUTH";
			break;
		case 3:
			direction = "EAST";
			break;
		case 4:
			direction = "WEST";
			break;
		default:
			direction = "NULL";
		}
	}

	@Override
	public String validateFieldText(String textFieldValue) {
		ObservableList<ObservableList<CSVCell>> rooms = EditCSVController.getRooms();
		List<String> roomNames = rooms.stream().map(e -> e.get(0).getProperty().getValue())
				.collect(Collectors.toList());

		if (!textFieldValue.matches("\\w+")) {
			return "Field must be one word in length with no spaces.";
		}

		if (!roomNames.contains(textFieldValue) && !textFieldValue.equals("null")) {
			return "Not a valid direction";
		}

		return null;
	}

	public static boolean matchesIndexCondition(int csvIndex) {
		return INDEX_RANGE.contains(csvIndex);
	}

	public String getDirection() {
		return direction;
	}
}
