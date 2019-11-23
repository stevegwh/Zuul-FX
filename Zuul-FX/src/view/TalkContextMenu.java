package view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import zuul.GameController;

public class TalkContextMenu {

	// Code adapted from:
	// https://stackoverflow.com/questions/28264907/javafx-listview-contextmenu
	public Callback<ListView<String>, ListCell<String>> getContextMenu() {
		return (lv) -> {
			ListCell<String> cell = new ListCell<>();
			ContextMenu contextMenu = new ContextMenu();
			MenuItem editItem = new MenuItem();
			editItem.textProperty().bind(Bindings.format("Talk to \"%s\"", cell.itemProperty()));
			editItem.setOnAction(event -> {
				String toTalk = cell.getItem();
				GameController.getNPCContoller().getActor(toTalk).onTalk();;
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
