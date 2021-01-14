package info.sigmaclient.management.waypoints;

import info.sigmaclient.util.FileUtils;
import net.minecraft.util.Vec3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Arithmo on 5/8/2017 at 2:47 PM.
 */
public class WaypointManager {

    private static WaypointManager waypointManager;

    private List<Waypoint> waypoints = new CopyOnWriteArrayList<>();

    public WaypointManager() {
        waypointManager = this;
        loadWaypoints();
    }

    private final File WAYPOINT_DIR = FileUtils.getConfigFile("Waypoints");

    public void loadWaypoints() {
        try {
            List<String> fileContent = FileUtils.read(WAYPOINT_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                String waypointName = split[0];
                // name x y z color string
                assert split.length == 6;
                createWaypoint(waypointName, new Vec3(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])), Integer.parseInt(split[4]), split[5]);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed loading waypoints! Please check that waypoints.txt is in the valid format!");
            e.printStackTrace();
        }
    }

    public void saveWaypoints() {
        List<String> fileContent = new ArrayList<>();
        for (Waypoint waypoint : getWaypoints()) {
            String waypointName = waypoint.getName();
            String x = String.valueOf(waypoint.getVec3().xCoord);
            String y = String.valueOf(waypoint.getVec3().yCoord);
            String z = String.valueOf(waypoint.getVec3().zCoord);
            fileContent.add(String.format("%s:%s:%s:%s:%s:%s", waypointName, x, y, z, waypoint.getColor(), waypoint.getAddress()));
        }
        FileUtils.write(WAYPOINT_DIR, fileContent, true);
    }

    public void deleteWaypoint(Waypoint waypoint) {
        waypoints.remove(waypoint);
    }

    public void createWaypoint(String name, Vec3 vec3, int color, String address) {
        waypoints.add(new Waypoint(name, vec3, color, address));
        saveWaypoints();
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public static WaypointManager getManager() {
        return waypointManager;
    }

    public boolean containsName(String name) {
        for (Waypoint waypoint : getWaypoints()) {
            if (waypoint.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
