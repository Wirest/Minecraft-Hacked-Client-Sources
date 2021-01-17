package me.rigamortis.faurax.module.modules.render.waypoint;

import javax.vecmath.*;
import java.util.*;

public class WayPointManager
{
    public static ArrayList<WayPoint> points;
    
    static {
        WayPointManager.points = new ArrayList<WayPoint>();
    }
    
    public static void addWayPoint(final String name, final int x, final int y, final int z) {
        WayPointManager.points.add(new WayPoint(new Vector3f((float)x, (float)y, (float)z), name));
    }
    
    public static void removeWayPoint(final String name) {
        for (final WayPoint wp : WayPointManager.points) {
            if (wp.name.equalsIgnoreCase(name) && WayPointManager.points.contains(wp)) {
                WayPointManager.points.remove(wp);
            }
        }
    }
}
