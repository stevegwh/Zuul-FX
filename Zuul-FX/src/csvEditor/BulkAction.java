package csvEditor;

import javafx.scene.control.MenuItem;

/**
 * Generic interface for 'bulk actions' of the CSVEditor. For example, 'add item
 * to all rooms', 'remove all without exit' etc.
 * 
 * @author Steve
 *
 */
public interface BulkAction {

	public MenuItem getMenuItem();

}
