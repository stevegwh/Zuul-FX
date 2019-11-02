package zuul;

public class TakeableItem {
	private String name;
	private int weight;
	boolean perishable = false; // Decides if object should be deleted after use

	public TakeableItem(String name, int weight) {
		this.name = name;
		this.weight = weight;
	}

	public TakeableItem(String name, int weight, boolean perishable) {
		this.name = name;
		this.weight = weight;
		this.perishable = perishable;
	}

	public int getWeight() {
		return weight;
	}

	public String getName() {
		return name;
	}

	public boolean isPerishable() {
		return perishable;
	}
}
