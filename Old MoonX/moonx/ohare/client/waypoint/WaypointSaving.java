package moonx.ohare.client.waypoint;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import moonx.ohare.client.Moonx;

public class WaypointSaving {
	private static File waypointFile;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	WaypointSaving(File dir) {
		waypointFile = new File(dir + File.separator + "waypoints.json");

	}

	public void setup() {
		try {
			if (!waypointFile.exists()) {
				waypointFile.createNewFile();
				saveFile();
				return;
			}
			loadFile();
		} catch (IOException exception) {
		}
	}


	public void loadFile() {
		try (FileReader inFile = new FileReader(waypointFile)) {
			Moonx.INSTANCE.getWaypointManager().setWaypoints((GSON.fromJson(inFile, new com.google.common.reflect.TypeToken<ArrayList<Waypoint>>() {
			}.getType())));
			if (Moonx.INSTANCE.getWaypointManager().getWaypoints() == null)
				Moonx.INSTANCE.getWaypointManager().setWaypoints(new ArrayList<>());
		} catch (Exception e) {
		}
	}

	public void saveFile() {
		if (waypointFile.exists()) {
			try (PrintWriter writer = new PrintWriter(waypointFile)) {
				writer.print(GSON.toJson(Moonx.INSTANCE.getWaypointManager().getWaypoints()));
			} catch (Exception e) {
			}
		}
	}
}
