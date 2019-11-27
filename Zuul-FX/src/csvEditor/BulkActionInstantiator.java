package csvEditor;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Menu;

/**
 * Scans the 'bulkActions' directory and attempts to instantiate all via
 * reflection and store them in the 'bulkActions' ArrayList.
 * 
 * @author Steve
 *
 */
public class BulkActionInstantiator {
	List<String> bulkActions = new ArrayList<>();

	public void populateMenu(Menu bulkActionMenuBar) {

		for (String bulkActionName : bulkActions) {
			BulkAction bAction;
			try {
				bAction = (BulkAction) Class.forName("csvEditor.bulkActions." + bulkActionName).getConstructor()
						.newInstance();
				bulkActionMenuBar.getItems().add(bAction.getMenuItem());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				System.err.println("Cannot instantiate " + bulkActionName + " as a bulk action.");
				System.err.println(
						"Please check " + bulkActionName + " for errors and that it implements the correct interface");
				e.printStackTrace();
				return;
			}
		}

	}

	/**
	 * Scans the bulkActions directory and stores all file names in the bulkActions
	 * ArrayList
	 */
	private void populateArr() {
		File file = new File("src/csvEditor/bulkActions/");
		String[] list = file.list();
		for (String item : list) {
			String[] tmp = item.split(".java");
			item = tmp[0];
			bulkActions.add(item);
		}
	}

	public BulkActionInstantiator() {
		populateArr();
	}

}
