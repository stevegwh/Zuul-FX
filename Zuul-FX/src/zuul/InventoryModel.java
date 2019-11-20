package zuul;

//import java.time.Instant;
import java.util.ArrayList;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryModel {
	private int totalWeight;
	private final int WEIGHT_LIMIT = 10;
	private ArrayList<TakeableItem> inventory = new ArrayList<>();
	private ObservableList<String> inventoryNames = FXCollections.observableList(new ArrayList<String>());
	private ListProperty<String> inventoryListProperty = new SimpleListProperty<>(inventoryNames);

	public int getWeight() {
		return totalWeight;
	}

	public void setWeight(int weight) {
		totalWeight += weight;
	}

	public ArrayList<TakeableItem> getInventory() {
		// TODO: unsafe. Can be edited when you don't want it.
		return inventory;
	}

	public void addItem(TakeableItem item) {
		inventory.add(item);
		inventoryListProperty.add(item.getName());
	}

	public void removeItem(TakeableItem item) {
		inventory.remove(item);
		inventoryListProperty.remove(item.getName());
	}
	
	public ListProperty<String> getInventoryListProperty() {
		return inventoryListProperty;
	}

	public TakeableItem getItem(String itemName) {
		return inventory.stream().filter(o -> (o).getName().equals(itemName)).findFirst().orElse(null);
	}

	public boolean checkIfExists(String itemToCheck) {
		if (inventory.size() == 0) {
			return false;
		}
		TakeableItem item = inventory.stream().filter(o -> (o).getName().equals(itemToCheck)).findFirst().orElse(null);
		return item != null;
	}

	public boolean overWeightLimit(int weight) {
		return totalWeight + weight > WEIGHT_LIMIT;
	}

	public InventoryModel() {
		setWeight(0);
	}

}
