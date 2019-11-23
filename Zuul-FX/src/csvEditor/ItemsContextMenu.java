package csvEditor;

import javafx.scene.control.ContextMenu;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import zuul.CommandHandler;

public class ItemsContextMenu {

	// Code adapted from:
	// https://stackoverflow.com/questions/28264907/javafx-listview-contextmenu
	public Callback<ListView<String>, ListCell<String>> getContextMenu(CommandHandler commandHandler) {
		return (lv) -> {
			ListCell<String> cell = new ListCell<>();
			ContextMenu contextMenu = new ContextMenu();
			MenuItem editItem = new MenuItem();
			editItem.textProperty().bind(Bindings.format("Take \"%s\"", cell.itemProperty()));
			editItem.setOnAction(event -> {
				String toTake = cell.getItem();
				commandHandler.handleCommand(new String[] { "Take", toTake });
			});
			contextMenu.getItems().addAll(editItem);

			cell.textProperty().bind(cell.itemProperty());

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(contextMenu);
				}
			});
			return cell;
		};
	}
}
