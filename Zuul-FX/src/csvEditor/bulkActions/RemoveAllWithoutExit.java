package csvEditor.bulkActions;

import java.util.ArrayList;
import java.util.List;

import csvEditor.BulkAction;
import csvEditor.CSVEditorCell;
import csvEditor.EditCSVController;
import csvLoader.headers.HeaderEnum;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

/**
 * Removes all rooms without exits from the CSV data. This does not write directly to
 * the CSV file itself rather the matrix representation of the CSV file that was
 * loaded earlier.
 * 
 * @author Steve
 *
 */
public class RemoveAllWithoutExit implements BulkAction {
	private MenuItem menuItem;

	@Override
	public MenuItem getMenuItem() {
		return menuItem;
	}

	private void execute() {
		ObservableList<ObservableList<CSVEditorCell>> rooms = EditCSVController.getRooms();
		int amountRemoved = rooms.size();
		// Get's list of rooms without exits
		List<ObservableList<CSVEditorCell>> toRemove = new ArrayList<ObservableList<CSVEditorCell>>();
		for (ObservableList<CSVEditorCell> r : rooms) {
			if(r.stream()
			.filter(e -> e.getHeader().getEnum().equals(HeaderEnum.DIRECTION))
			.map(e-> e.getProperty().getValue())
			.allMatch(e-> e.equals("null"))) {
				toRemove.add(r);
			}
		}
		rooms.removeAll(toRemove);
		
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(amountRemoved - rooms.size() + " room(s) removed.");
		a.show();
	}
	
	public RemoveAllWithoutExit() {
		menuItem = new MenuItem("Remove All Rooms w/o Exit");
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				execute();
			}
			
		});
	}

}
