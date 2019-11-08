package zuul;

import java.util.ArrayList;

import command.commandView.LookOutput;
import command.game.eventOutput.GameStartOutput;
import command.game.eventOutput.SingleOrMultiOutput;
import npc.AllNPCDataController;
import npc.NPCController;

public class GameController {
	private static boolean singlePlayer = true;
	private static AllRoomDataController roomData;
	private static AllNPCDataController npcData;
	private static NPCController npcController;
	private static Player currentPlayer;
	private static ArrayList<Player> playerArr;
	private final static String START_LOCATION = "entrance";

	public static Room getCurrentRoom() {
		return roomData.getCurrentRoom();
	}
	
	public static void setNewCurrentRoom(String roomName) {
		roomData.setNewCurrentRoom(roomName);
	}

//	public static NPCController getNPCContoller() {
//		return npcController;
//	}

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
	 * @param b
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
		if(playerArr == null) {
			System.err.println("playerArr has not been initalised yet.");
			return null;
		}
		return playerArr;
	}

	/**
	 * Initialises the game as single or multi-player.
	 */
	private static void setGameType() {
		SingleOrMultiOutput setSingleplayer = new SingleOrMultiOutput();
		setSingleplayer.init(new String[] { START_LOCATION });
		if (singlePlayer) {
			currentPlayer = new Player(START_LOCATION);
		}
	}

	public static void start() {
		roomData = new AllRoomDataController();
		npcData = new AllNPCDataController();
		npcController = new NPCController();
		currentPlayer = new Player("entrance");
		GameStartOutput welcome = new GameStartOutput();
		LookOutput look = new LookOutput();
////		setGameType();
		npcController.init(npcData);
		welcome.init(new String[] {});
		roomData.setNewCurrentRoom(getCurrentPlayer().getLocation());
		look.init(new String[] {});
	}

	public static AllRoomDataController getAllRoomDataController() {
		return roomData;
	}

}