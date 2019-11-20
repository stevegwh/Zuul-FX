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
	private ObservableList<NPC> actorsInRoom = FXCollections.observableList(new ArrayList<NPC>());
	private List<TakeableItem> takeableItems = new ArrayList<TakeableItem>();
	private Map<String, String> exits;
	// String representation of 'takeableItems'
	private ObservableList<String> itemNames = FXCollections.observableList(new ArrayList<String>());
	// Necessary to bind data with view
	private ListProperty<String> itemsListProperty = new SimpleListProperty<>(itemNames);

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TakeableItem> getTakeableItems() {
		return takeableItems;
	}

	public void setTakeableItems(ArrayList<TakeableItem> takeableItems) {
		this.takeableItems = FXCollections.observableList(takeableItems);
	}

	public ObservableList<NPC> getActorsInRoom() {
		return actorsInRoom;
	}

	public void setActorsInRoom(ArrayList<NPC> actorsInRoom) {
		this.actorsInRoom = FXCollections.observableList(actorsInRoom);
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

	public void addActor(NPC npc) {
		actorsInRoom.add(npc);
	}

	public void removeActor(NPC npc) {
		actorsInRoom.remove(npc);
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
