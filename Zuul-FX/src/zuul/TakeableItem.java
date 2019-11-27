package zuul;

/**
 * Class to represent any item the player can take/drop/give in the game.
 * @author Steve
 *
 */
public class TakeableItem {
	private String name;
	private int weight;

	public TakeableItem(String name, int weight) {
		this.name = name;
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public String getName() {
		return name;
	}
}
