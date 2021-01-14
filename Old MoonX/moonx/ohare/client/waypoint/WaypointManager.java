package moonx.ohare.client.waypoint;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WaypointManager {
	private ArrayList<Waypoint> waypoints = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private File waypointsFile;

	public WaypointManager(File dir) {
        waypointsFile = new File(dir + File.separator + "waypoints.json");
        load();
    }

    public void save() {
        if (waypointsFile == null) return;

        try {

            // Creates the file if it doesn't exist already.
            if (!waypointsFile.exists()) waypointsFile.createNewFile();

            PrintWriter printWriter = new PrintWriter(waypointsFile);

            printWriter.write(gson.toJson(toJson()));
            printWriter.close();
        } catch (IOException ignored) {
        }
    }

    public void load() {
        if (!waypointsFile.exists()) {
            save();

            return;
        }

        try {
            JsonObject json = new JsonParser().parse(new FileReader(waypointsFile)).getAsJsonObject();

            fromJson(json);
        } catch (IOException ignored) {
        }
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        getWaypoints().forEach(waypoint -> jsonArray.add(waypoint.toJson()));

        jsonObject.add("waypoints", jsonArray);

        return jsonObject;
    }

    public void fromJson(JsonObject json) {
        JsonArray jsonArray = json.get("waypoints").getAsJsonArray();

        jsonArray.forEach(jsonElement -> {

            JsonObject jsonObject = (JsonObject) jsonElement;

            Waypoint waypoint = new Waypoint();

            waypoint.fromJson(jsonObject);

            getWaypoints().add(waypoint);
        });
    }

	private Waypoint getWaypoint(String label, String server) {
		for (Waypoint waypoint : waypoints) {
			if (waypoint.getLabel().equalsIgnoreCase(label) && waypoint.getServer().equals(server)) {
				return waypoint;
			}
		}
		return null;
	}

	void setWaypoints(ArrayList<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}


	public ArrayList<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void add(String label, double x, double y, double z, String server, int dimension) {
		waypoints.add(new Waypoint(label, x, y, z, server, dimension));
	}


	public void remove(String label, String server) {
		Waypoint f = getWaypoint(label, server);
		if (f != null) {
			waypoints.remove(f);
		}
	}

	public boolean isWaypoint(String ign, String server) {
		return getWaypoint(ign, server) != null;
	}
}
