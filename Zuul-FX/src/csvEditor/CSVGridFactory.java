package csvEditor;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Responsible for creating the grid for the 'Excel' style CSV editor. The grid
 * is a two-dimensional array of CSVEditorCells that are represented as a
 * GridPane of TextView objects.
 * 
 * @author Steve
 *
 */
public class CSVGridFactory {
	ObservableList<ObservableList<CSVEditorCell>> rooms = EditCSVController.getRooms();
	private GridPane csvGridPane;

	private int lastFocusedRow;
	private int lastFocusedCol;

	/**
	 * Finds the longest row of the grid. Required to make each row of equal length.
	 * 
	 * @param rooms2 ObservableList of all rows.
	 * @return the longest row of the grid.
	 */
	private int findLongestArr(ObservableList<ObservableList<CSVEditorCell>> rooms2) {
		int longest = 0;
		for (ObservableList<CSVEditorCell> arr : rooms2) {
			if (arr.size() > longest)
				longest = arr.size();
		}
		return longest;
	}

	/**
	 * Finds the node in the gridpane with the corresponding row/column index.
	 * 
	 * @param row
	 * @param column
	 * @param gridPane
	 * @return The requested node
	 */
	@SuppressWarnings("static-access")
	public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
		// Reference:
		// https://www.programcreek.com/java-api-examples/?class=javafx.scene.layout.GridPane&method=getChildren
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();
		for (Node node : childrens) {
			if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
				result = node;
				break;
			}
		}
		return result;
	}

	/**
	 * Checks the validity of the text content of the cell every time the content is
	 * changed. If there is an error then the tooltip and style of the cell will
	 * change to reflect that. Also decides when it is necessary to add two extra
	 * cells at the end of the row for the user.
	 * 
	 * @param row
	 * @param col
	 * @param element
	 * @param csvTextField
	 * @return
	 */
	private InvalidationListener getTextFieldListener(int row, int col, CSVEditorCell element, TextField csvTextField) {
		return new InvalidationListener() {
			@Override
			public void invalidated(Observable c) {
				// If both the last cell and the second to last (i.e. the item name + the item
				// weight) are not empty then add two extra spaces at the end of the cell row
				// for the user.

				// Editing last cell
				if ((col == rooms.get(row).size() - 1
						&& !rooms.get(row).get(col - 1).getProperty().getValue().isEmpty())) {
					lastFocusedRow = row;
					lastFocusedCol = col;
					rooms.get(row).add(new CSVEditorCell("", col - 1));
					rooms.get(row).add(new CSVEditorCell("", col));

				}
				// Editing second from last cell
				if ((col == rooms.get(row).size() - 2)
						&& !rooms.get(row).get(col + 1).getProperty().getValue().isEmpty()) {
					lastFocusedRow = row;
					lastFocusedCol = col;
					rooms.get(row).add(new CSVEditorCell("", col));
					rooms.get(row).add(new CSVEditorCell("", col + 1));
				}

				// Error checks user input letter by letter to see if it is correct formed.
				Platform.runLater(() -> element.checkValidity());
				Platform.runLater(() -> csvTextField.setTooltip(element.getTooltip()));
				Platform.runLater(() -> csvTextField.setStyle(element.getStyle()));
			}
		};
	}

	/**
	 * Represents the data from the CSV file in a grid pattern. Each 'row' and
	 * 'cell' of the grid is bound to the 'rooms' ObservableList. All rows are of
	 * equal length. The length is the size of the current ObservableList plus two
	 * extra fields for the user to enter a new item plus its weight. If there are
	 * rows that are shorter than others then this space is accounted for by adding
	 * extra TextField objects that are set to disabled. These 'dummy' TextFields
	 * are not bound to any data structure.
	 */
	@FXML
	public void drawGrid() {
		csvGridPane.getChildren().clear();
		int ROW_LENGTH = rooms.size();
		// To have an even grid we find the longest row in the CSV file to use as our
		// boundary.
		int COL_LENGTH = findLongestArr(rooms);

		for (int row = 0; row < ROW_LENGTH; row++) {
			for (int col = 0; col < COL_LENGTH; col++) {
				TextField csvTextField = new TextField();
				if (col < rooms.get(row).size()) {
					CSVEditorCell currentCSVCell = rooms.get(row).get(col);
					// Binding of the underlying ObservableList to the TextField's data
					csvTextField.setText(currentCSVCell.getProperty().getValue());
					csvTextField.textProperty().bindBidirectional(currentCSVCell.getProperty());
					// Checks if the current content of the CSVCell is valid or not.
					currentCSVCell.checkValidity();
					csvTextField.setTooltip(currentCSVCell.getTooltip());
					csvTextField.setStyle(currentCSVCell.getStyle());
					csvTextField.textProperty()
							.addListener(getTextFieldListener(row, col, currentCSVCell, csvTextField));
				} else if (col > rooms.get(row).size() - 1) {
					// From this point on in the row 'dummy' TextFields are instantiated and set to
					// disabled.
					csvTextField.setDisable(true);
				}

				csvGridPane.add(csvTextField, col, row);
			}
		}
		// Ensures the grid is focusing the user's last row.
		TextField toFocus = (TextField) getNodeByRowColumnIndex(lastFocusedRow, lastFocusedCol, csvGridPane);
		// Check if focused cell still exists.
		if (toFocus != null) {
			toFocus.requestFocus();
			Platform.runLater(() -> toFocus.positionCaret(toFocus.textProperty().getValue().length() + 1));
		}
	}

	public CSVGridFactory(GridPane csvGridPane) {
		this.csvGridPane = csvGridPane;
	}

}
