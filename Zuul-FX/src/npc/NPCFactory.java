package npc;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class NPCFactory {
	private HashMap<String, NPC> npcs = new HashMap<String, NPC>();

	/**
	 * Attempts to create an instance of the class found in the NPC package.
	 * 
	 * @param className The current class' file name.
	 * @return An NPC object or null.
	 */
	private NPC instantiate(String className) {
		Object npc = null;
		try {
			npc = Class.forName(NPCFactory.class.getPackageName() + ".npcs." + className).getConstructor()
					.newInstance();
			return (NPC) npc;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | java.lang.ClassNotFoundException
				| java.lang.ClassCastException e) {
			System.err.println("Cannot instantiate " + className + " as NPC class.");
			System.err.println("Please check " + className + " for errors and that it implements the NPC interface");
			System.err.println("Please ensure that the class name matches the name of the NPC.");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Scans the npc package for all npc files and stores them in the npcs HashMap
	 */
	private void populateNPCArr() {
		File file = new File("src/" + NPCFactory.class.getPackageName() + "/npcs/");
		String[] list = file.list();
		for (String item : list) {
			String[] tmp = item.split(".java");
			item = tmp[0];
			NPC npc = instantiate(item);
			npcs.put(npc.getName(), npc);
		}
	}

	public HashMap<String, NPC> getNPCCollection() {
		return npcs;
	}

	public NPCFactory() {
		populateNPCArr();
	}
}
