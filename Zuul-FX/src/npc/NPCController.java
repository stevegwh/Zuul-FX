package npc;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import IO.IOHandler;
import javafx.application.Platform;


public class NPCController {
	private AllNPCDataController npcData;
	private List<String> actors;
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			Platform.runLater(() -> updateActors());
		}
	};

	public void updateActors() {
		actors.forEach(e -> npcData.getNPC(e).update());
	}
	
	public void init(AllNPCDataController npcData) {
		this.npcData = npcData;
		actors = npcData.getAllNPCS();
		timer.schedule(task, 100, 5000);
	}
}
