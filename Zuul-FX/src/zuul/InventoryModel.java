package zuul;

//import java.time.Instant;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryModel {
//	private long lastEdited;
	private int totalWeight;
	private final int WEIGHT_LIMIT = 10;
//	private ArrayList<TakeableItem> inventory = new ArrayList<TakeableItem>();
	private ArrayList<TakeableItem> inventory = new ArrayList<>();

	public int getWeight() {
		return totalWeight;
	}

	public void setWeight(int weight) {
		totalWeight += weight;
	}
	
//	public long getLastEdited() {
//		return lastEdited;
//	}
//	
//	public void setLastEdited() {
//		lastEdited = Instant.now().getEpochSecond();
//	}

	public ArrayList<TakeableItem> getInventory() {
		// TODO: unsafe. Can be edited when you don't want it.
		return inventory;
	}
	
	/**
	 * @return a list of all the names of the items in the inventory.
	 */
	public ObservableList<String> getInventoryStrings() {
		ObservableList<String> observableInvList = FXCollections.observableArrayList();
		inventory.forEach(e -> observableInvList.add(e.getName()));
		return observableInvList;
	}

	public void addItem(TakeableItem item) {
		inventory.add(item);
	}

	public void removeItem(TakeableItem item) {
		inventory.remove(item);
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
