package zuul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import npc.NPC;

public class Room {
	private String name;
	private String description;
	private List<NPC> actorsInRoom = new ArrayList<NPC>();
	private List<TakeableItem> takeableItems = new ArrayList<TakeableItem>();
	private Map<String, String> exits;
	// String representation of 'takeableItems'
	private ObservableList<String> itemNames = FXCollections.observableList(new ArrayList<String>());
	private ObservableList<String> actorNames = FXCollections.observableList(new ArrayList<String>());
	// Necessary to bind data with view
	private ListProperty<String> itemsListProperty = new SimpleListProperty<>(itemNames);
	private ListProperty<String> actorsListProperty = new SimpleListProperty<>(actorNames);

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TakeableItem> getTakeableItems() {
		return takeableItems;
	}

	public List<NPC> getActorsInRoom() {
		return actorsInRoom;
	}

	public Map<String, String> getExits() {
		return exits;
	}

	public void setExits(Map<String, String> exits) {
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
		itemNames.add(item.getName());
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
		itemNames.remove(item.getName());
	}

	public ListProperty<String> getItemListProperty() {
		return itemsListProperty;
	}

	public ListProperty<String> getActorListProperty() {
		return actorsListProperty;
	}

	public void addActor(NPC npc) {
		actorsInRoom.add(npc);
		actorNames.add(npc.getName());
	}

	public void removeActor(NPC npc) {
		actorsInRoom.remove(npc);
		actorNames.remove(npc.getName());
	}

	public boolean hasActor(String toTalk) {
		for (NPC actor : actorsInRoom) {
			if (actor.getName().equals(toTalk)) {
				return true;
			}
		}
		return false;
	}

	public Room() {
	}

}
