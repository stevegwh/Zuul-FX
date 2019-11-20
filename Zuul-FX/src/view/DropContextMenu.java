package view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class DropContextMenu {
	
	public ContextMenu getContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Choice 1");
        MenuItem menuItem2 = new MenuItem("Choice 2");
        MenuItem menuItem3 = new MenuItem("Choice 3");

        menuItem3.setOnAction((event) -> {
            System.out.println("Choice 3 clicked!");
        });

        contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3);
        return contextMenu;
	}
	
	public DropContextMenu() {
		
	}

}
