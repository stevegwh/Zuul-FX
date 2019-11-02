package zuul;

import java.util.ArrayList;

public class InventoryModel {
	private int totalWeight;
	private final int WEIGHT_LIMIT = 10;
	private ArrayList<TakeableItem> inventory = new ArrayList<TakeableItem>();

	public int getWeight() {
		return totalWeight;
	}

	public void setWeight(int weight) {
		totalWeight += weight;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TakeableItem> getInventory() {
		return (ArrayList<TakeableItem>) inventory.clone();
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
