package csvEditor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import npc.NPC;

public class DialogView {
	static NPC currentNPC;

	public static NPC getCurrentNPC() {
		return currentNPC;
	}

	public void startDialog() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/npcDialog.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent, 500, 300);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public DialogView(NPC npc) {
		currentNPC = npc;
	}
}
