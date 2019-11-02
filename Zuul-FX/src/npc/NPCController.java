package npc;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class NPCController {
	private HashMap<String, NPC> actors = new HashMap<>();
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			updateActors();
		}
	};

	public void updateActors() {
		actors.keySet().forEach(a -> actors.get(a).update());
	}

	public NPC getActor(String actorName) {
		return actors.get(actorName);
	}
	
	public void init() {
		NPCFactory npcFactory = new NPCFactory();
		actors = npcFactory.getNPCCollection();
		timer.schedule(task, 100, 5000);
	}
}
