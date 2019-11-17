package view;

import java.util.List;

import csvLoader.CSVCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ObservableListFactory {

	private List<List<String>> roomData;
	private CSVGrid csvGrid;

	public ObservableList<ObservableList<CSVCell>> start() {
		ObservableList<ObservableList<CSVCell>> rooms = FXCollections.observableArrayList();
		for (List<String> room : roomData) {
			ObservableList<CSVCell> row = FXCollections.observableArrayList();
			int i = 0;
			for (String e : room) {
				row.add(new CSVCell(e, i));
				i++;
			}
			// Add an extra two blank cells at the end.
			row.add(new CSVCell("", i));
			row.add(new CSVCell("", i + 1));
			rooms.add(row);
		}
		for (ObservableList<CSVCell> room : rooms) {
			room.addListener((ListChangeListener<CSVCell>) c -> {
				System.out.println("Row changed to : " + room);
				csvGrid.drawGrid();
			});
		}
		rooms.addListener((ListChangeListener<ObservableList<CSVCell>>) c -> {
			System.out.println("Row changed to : " + rooms);
			csvGrid.drawGrid();
//			undoMenuItem.setDisable(undoArr.size() == 0);
		});
		return rooms;
	}

	public ObservableListFactory(List<List<String>> roomData, CSVGrid csvGrid) {
		this.roomData = roomData;
		this.csvGrid = csvGrid;
	}
}
