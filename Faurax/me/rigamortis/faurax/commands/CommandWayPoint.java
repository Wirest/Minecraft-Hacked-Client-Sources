package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.rigamortis.faurax.module.modules.render.waypoint.*;
import me.cupboard.command.argument.*;

public class CommandWayPoint extends Command
{
    public CommandWayPoint() {
        super("waypoint", new String[] { "wp" });
    }
    
    @Argument(handles = { "add", "a" })
    protected String addWaypoint(final String name, final int x, final int y, final int z) {
        WayPointManager.addWayPoint(name, x, y, z);
        return "Added Waypoint " + name + " at " + x + ", " + y + ", " + z;
    }
    
    @Argument(handles = { "rem", "r" })
    protected String remWaypoint(final String name) {
        try {
            WayPointManager.removeWayPoint(name);
        }
        catch (Exception ex) {}
        return "Removed waypoint " + name;
    }
}
