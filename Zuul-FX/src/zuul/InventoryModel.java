package zuul;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryModel {
	private int totalWeight;
	private StringProperty totalWeightProp = new SimpleStringProperty("Total Weight: 0");
	private final int WEIGHT_LIMIT = 10;
	private ArrayList<TakeableItem> inventory = new ArrayList<>();
	private ObservableList<String> inventoryNames = FXCollections.observableList(new ArrayList<String>());

	public int getTotalWeight() {
		return totalWeight;
	}

	public void setWeight(int weight) {
		totalWeight += weight;
		totalWeightProp.setValue("Total Weight: " + Integer.toString(totalWeight));
	}

	public ArrayList<TakeableItem> getInventory() {
		return inventory;
	}

	public void addItem(TakeableItem item) {
		inventory.add(item);
		inventoryNames.add(item.getName());
	}

	public void removeItem(TakeableItem item) {
		inventory.remove(item);
		inventoryNames.remove(item.getName());
	}
	
	public ObservableList<String> getInventoryNames() {
		return inventoryNames;
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

	public StringProperty getTotalWeightProp() {
		return totalWeightProp;
	}

	public boolean overWeightLimit(int weight) {
		return totalWeight + weight > WEIGHT_LIMIT;
	}

	public InventoryModel() {
		setWeight(0);
	}

	public void removeItem(String item) {
		TakeableItem toRemove = inventory.stream().filter(e-> e.getName().equals(item)).findFirst().orElse(null);
		removeItem(toRemove);
		
	}


}
