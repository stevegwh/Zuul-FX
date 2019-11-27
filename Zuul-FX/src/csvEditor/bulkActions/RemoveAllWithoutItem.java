package csvEditor.bulkActions;

import java.util.List;
import java.util.stream.Collectors;

import csvEditor.BulkAction;
import csvEditor.CSVEditorCell;
import csvEditor.EditCSVController;
import csvLoader.headers.HeaderEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

/**
 * Removes all rooms without an item from the CSV data. This does not write directly to
 * the CSV file itself rather the matrix representation of the CSV file that was
 * loaded earlier. This will also remove all references to the room as it has now been removed.
 * 
 * @author Steve
 *
 */
public class RemoveAllWithoutItem implements BulkAction {
	private MenuItem menuItem;

	@Override
	public MenuItem getMenuItem() {
		return menuItem;
	}

	private void execute() {
		ObservableList<ObservableList<CSVEditorCell>> rooms = EditCSVController.getRooms();
		// Stores the room array before modification
		int amountRemoved = rooms.size();

		// Get's list of rooms without exits
		List<ObservableList<CSVEditorCell>> toRemove = rooms.stream().filter(r->
			r.stream()
			.filter(e -> !e.getProperty().getValue().isEmpty())
			.filter(e -> e.getHeader().getEnum().equals(HeaderEnum.ITEMNAME) || e.getHeader().getEnum().equals(HeaderEnum.ITEMWEIGHT))
			.collect(Collectors.toList())
			.size() == 0
		).collect(Collectors.toCollection(FXCollections::observableArrayList));

		// Get's all names of rooms staged for removal
		List<String> names = toRemove.stream().map(
				e -> e.stream()
				.filter(f-> f.getHeader().getEnum().equals(HeaderEnum.NAME)).findFirst().orElse(null).getProperty().getValue()
				).collect(Collectors.toList());
		// Deference all instances of the rooms staged to be removed.
		// Does this before removing everything to avoid 'index out of bounds' exceptions.
		rooms.forEach(r ->
				r.stream()
				.filter(e-> e.getHeader().getEnum().equals(HeaderEnum.DIRECTION))
				.filter(e-> names.contains(e.getProperty().getValue()))
				.forEach(e-> e.getProperty().setValue("null"))
		);
		rooms.removeAll(toRemove);

		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText(
				amountRemoved - rooms.size() + " room(s) removed. All references to removed rooms are now 'null'.");
		a.show();
	}
	
	public RemoveAllWithoutItem() {
		menuItem = new MenuItem("Remove All Rooms w/o Item");
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				execute();
			}
			
		});
	}

}
