package view.ContextMenus;

import java.util.List;
import java.util.stream.Collectors;

import command.commandView.GiveOutput;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import zuul.GameController;

public class ActorContextMenu {
	ListProperty<String> items = new SimpleListProperty<>();

	// Code adapted from:
	// https://stackoverflow.com/questions/28264907/javafx-listview-contextmenu
	public Callback<ListView<String>, ListCell<String>> getContextMenu() {
		return (lv) -> {
			ListCell<String> cell = new ListCell<>();
			ContextMenu mainContextMenu = new ContextMenu();

			MenuItem talkItem = new MenuItem();
			talkItem.textProperty().bind(Bindings.format("Talk to \"%s\"", cell.itemProperty()));
			talkItem.setOnAction(event -> {
				String toTalk = cell.getItem();
				GameController.getNPCContoller().getActor(toTalk).onTalk();;
			});

			EventHandler<ActionEvent> itemAction = new EventHandler<ActionEvent>() { 
				public void handle(ActionEvent e) {
					String item = ((MenuItem) e.getSource()).getText();
					GiveOutput giveOutput = new GiveOutput();
					giveOutput.init(new String[] {"Give", cell.getText(), item});

//					GameController.getNPCContoller().getActor(cell.getText()).onGive(item);;
				}
			};

			Menu giveMenu = new Menu("Give..");
			// TODO: Set action event for when they click the item
			EventHandler<WindowEvent> event = new EventHandler<WindowEvent>() { 
				public void handle(WindowEvent e) { 
					List<MenuItem> inventory = GameController.getCurrentPlayer().getInvModel().getInventoryListProperty().stream().map(f-> new MenuItem(f)).collect(Collectors.toList());
					inventory.forEach(f-> f.setOnAction(itemAction));
					giveMenu.getItems().removeAll(giveMenu.getItems());
					giveMenu.getItems().addAll(inventory);
				} 
			}; 

			mainContextMenu.getItems().addAll(talkItem, giveMenu);
			mainContextMenu.setOnShown(event);

			cell.textProperty().bind(cell.itemProperty());

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(mainContextMenu);
				}
			});
			return cell;
		};
	}

}
