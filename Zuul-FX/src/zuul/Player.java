package zuul;

public class Player {
	private InventoryModel inventory = new InventoryModel();
	private String currentLocation;
	private int maxTurns = 2;
	private int turnCount = maxTurns;

	public void decTurnCount() {
		turnCount--;
	}

	public int getTurnCount() {
		return turnCount;
	}

	public void resetTurnCount() {
		turnCount = maxTurns;
	}

	public int getMaxCount() {
		return maxTurns;
	}

	public InventoryModel getInvModel() {
		return inventory;
	}

	public void setLocation(String location) {
		currentLocation = location;
	}

	public String getLocation() {
		return currentLocation;
	}

	public Player(String startingLocation) {
		currentLocation = startingLocation;
	}

}
