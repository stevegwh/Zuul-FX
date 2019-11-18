package view;

import csvLoader.CSVCell;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CSVGridFactory {
	ObservableList<ObservableList<CSVCell>> rooms = EditCSVController.getRooms();
	private GridPane csvGridPane;

	private int lastFocusedRow;
	private int lastFocusedCol;

	/**
	 * Finds the longest row of the grid. Required to make each row of equal length.
	 * 
	 * @param rooms2 ObservableList of all rows.
	 * @return the longest row of the grid.
	 */
	private int findLongestArr(ObservableList<ObservableList<CSVCell>> rooms2) {
		int longest = 0;
		for (ObservableList<CSVCell> arr : rooms2) {
			if (arr.size() > longest)
				longest = arr.size();
		}
		return longest;
	}

	// Reference:
	// https://www.programcreek.com/java-api-examples/?class=javafx.scene.layout.GridPane&method=getChildren
	@SuppressWarnings("static-access")
	public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
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
	
	
	public void showTooltip(TextField textField, String tooltipText)
		{
			Stage owner = (Stage) textField.getScene().getWindow();
		    Point2D p = textField.localToScene(0.0, -20.0);

		    final Tooltip customTooltip = new Tooltip();
		    customTooltip.setText(tooltipText);

		    textField.setTooltip(customTooltip);
		    customTooltip.setAutoHide(true);

		    customTooltip.show(owner, p.getX()
		        + textField.getScene().getX() + textField.getScene().getWindow().getX(), p.getY()
		        + textField.getScene().getY() + textField.getScene().getWindow().getY());

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
				TextField cell = new TextField();
				if (col < rooms.get(row).size()) {

					// 'final' elements for the InvalidationListener to use.
					CSVCell element = rooms.get(row).get(col);
					int colIdx = col, rowIdx = row;

					// Binding of the underlying ObservableList to the TextField's data
					cell.setText(element.getProperty().getValue());
					cell.textProperty().bindBidirectional(element.getProperty());

					cell.textProperty().addListener((InvalidationListener) c -> {
						// If both the last cell and the second to last (i.e. the item name + the item
						// weight) are not empty then add two extra spaces at the end of the cell row
						// for the user.
						lastFocusedRow = rowIdx;
						lastFocusedCol = colIdx;
						if ((colIdx == rooms.get(rowIdx).size() - 1
								&& !rooms.get(rowIdx).get(colIdx - 1).getProperty().getValue().isEmpty())
								|| (colIdx == rooms.get(rowIdx).size() - 2)
										&& !rooms.get(rowIdx).get(colIdx + 1).getProperty().getValue().isEmpty()) {
							// TODO: Currently this resets all error coloring and tooltips.
							// Probably can be solved by adding the tooltip to the CSVCell itself.
							rooms.get(rowIdx).add(new CSVCell("", colIdx));
							rooms.get(rowIdx).add(new CSVCell("", colIdx));
						}
						Platform.runLater(()->cellErrorCheck(rowIdx, colIdx, cell));

					});
				} else if (col > rooms.get(row).size() - 1) {
					// From this point on in the row 'dummy' TextFields are instantiated and set to
					// disabled.
					cell.setDisable(true);
				}

				csvGridPane.add(cell, col, row);
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
	
	
	// TODO: Find a sensible way of handling error checking and getting the correct tooltip
	private void cellErrorCheck(int rowIdx, int colIdx, TextField cell) {
		if (!rooms.get(rowIdx).get(colIdx).checkValidity()) {
			final String cssDefault = "-fx-background-color: orange;";
			cell.setStyle(cssDefault);
			Tooltip tooltip = new Tooltip("Error Message");
			cell.setTooltip(tooltip);
//			showTooltip(cell, "Error Message");
		} else if (rooms.get(rowIdx).get(colIdx).checkValidity()) {
			cell.setStyle("");
			cell.setTooltip(null);
		}
		
	}
	
	public CSVGridFactory(GridPane csvGridPane) {
		this.csvGridPane = csvGridPane;
	}

}