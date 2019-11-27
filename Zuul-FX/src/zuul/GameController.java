package zuul;

import java.util.ArrayList;
import java.util.List;

import command.commandView.LookOutput;
//import command.game.eventOutput.SingleOrMultiOutput;
import npc.AllNPCDataController;
import npc.NPCController;

/**
 * Main controller and router for all logic in the game
 * @author Steve
 *
 */
public class GameController {
	private static boolean singlePlayer = true;
	private static AllRoomDataController roomData;
	private static AllNPCDataController npcData;
	private static NPCController npcController;
	private static Player currentPlayer;
	private static ArrayList<Player> playerArr;
	private static String startLocation;

	/**
	 * @return the currently occupied room.
	 */
	public static Room getCurrentRoom() {
		return roomData.getCurrentRoom();
	}

	/**
	 * Sets the starting location of the game.
	 * @param location the name of the location.
	 */
	public static void setStartLocation(String location) {
		startLocation = location;
	}

	/**
	 * @return the starting location of the game.
	 */
	public static String getStartLocation() {
		return startLocation;
	}

	/**
	 * Changes the room the current player is in.
	 * @param roomName the name of the room.
	 */
	public static void setNewCurrentRoom(String roomName) {
		roomData.setNewCurrentRoom(roomName);
	}

	/**
	 * @return the controller responsible for the NPCs.
	 */
	public static NPCController getNPCContoller() {
		return npcController;
	}

	/**
	 * @return the currently active player.
	 */
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @param player the next player to play.
	 */
	public static void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}

	/**
	 * Sets whether the game is single player.
	 * 
	 * @param b true/false
	 */
	public static void setSingleplayer(boolean b) {
		singlePlayer = b;
	}

	/**
	 * @return true (single-player)/false (multi-player).
	 */
	public static boolean getSinglePlayer() {
		return singlePlayer;
	}

	public static void initPlayerArr(ArrayList<Player> playerArr) {
		if (GameController.playerArr == null) {
			GameController.playerArr = playerArr;
		} else {
			System.err.println("playerArr has already been set");
		}
	}

	public static ArrayList<Player> getPlayerArr() {
		if (playerArr == null) {
			System.err.println("playerArr has not been initalised yet.");
			return null;
		}
		return playerArr;
	}

	/**
	 * Initialises the game as single or multi-player.
	 */
//	private static void setGameType() {
//		SingleOrMultiOutput setSingleplayer = new SingleOrMultiOutput();
//		setSingleplayer.init(new String[] { startLocation });
//		if (singlePlayer) {
//			currentPlayer = new Player(startLocation);
//		}
//	}

	/**
	 * Passes in the gametype and the room list to be converted into
	 * roomDat
	 * @param rooms Either an ObservableList or a List
	 * @param game Either GameType.DEFAULT or CUSTOM
	 */
	public static void initRooms(List<?> rooms, GameType game) {
		roomData = new AllRoomDataController(rooms, game);
	}

	public static void start() {
		npcData = new AllNPCDataController();
		npcController = new NPCController();
		currentPlayer = new Player(startLocation);
		LookOutput look = new LookOutput();
////		setGameType();
		npcController.init(npcData);
		roomData.setNewCurrentRoom(getCurrentPlayer().getLocation());
		look.init(new String[] {});
	}

	public static AllRoomDataController getAllRoomDataController() {
		return roomData;
	}

}