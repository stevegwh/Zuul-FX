package zuul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import npc.NPC;

public class Room {
	private String name;
	private String description;
	private ArrayList<TakeableItem> takeableItems = new ArrayList<>();
	private ArrayList<NPC> actorsInRoom = new ArrayList<>();
	private HashMap<String, String> exits = new HashMap<>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<TakeableItem> getTakeableItems() {
		return takeableItems;
	}

	public void setTakeableItems(ArrayList<TakeableItem> takeableItems) {
		this.takeableItems = takeableItems;
	}

	public ArrayList<NPC> getActorsInRoom() {
		return actorsInRoom;
	}

	public void setActorsInRoom(ArrayList<NPC> actorsInRoom) {
		this.actorsInRoom = actorsInRoom;
	}

	public HashMap<String, String> getExits() {
		return exits;
	}

	public void setExits(HashMap<String, String> exits) {
		this.exits = exits;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTakeableItem(TakeableItem item) {
		takeableItems.add(item);
	}

	public String getExit(String direction) {
		return exits.get(direction);
	}

	public TakeableItem ifItemExistsReturnIt(String toTake) {
		for (TakeableItem item : takeableItems) {
			if (item.getName().equals(toTake)) {
				return item;
			}
		}
		return null;
	}

	public ArrayList<String> getAllDirections() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(exits.keySet());
		return list;
	}

	public void removeTakeableItem(TakeableItem item) {
		takeableItems.remove(item);
	}

	public boolean hasActor(String toTalk) {
		for (NPC actor : actorsInRoom) {
			if (actor.getName().equals(toTalk)) {
				return true;
			}
		}
		return false;
	}

	public Room(List<String> data) {
		data = data.stream().map(e -> e.trim()).collect(Collectors.toList());
		name = data.get(0);
		description = data.get(1);
		if (!data.get(2).equals("null")) {
			exits.put("north", data.get(2));
		}
		if (!data.get(3).equals("null")) {
			exits.put("east", data.get(3));
		}
		if (!data.get(4).equals("null")) {
			exits.put("south", data.get(4));
		}
		if (!data.get(5).equals("null")) {
			exits.put("west", data.get(5));
		}
//		// TODO: Return error if there is no weight
		for (int i = 6; i < data.size(); i += 2) {
			TakeableItem item = new TakeableItem(data.get(i), Integer.parseInt(data.get(i + 1)));
			takeableItems.add(item);
		}

	}

}
