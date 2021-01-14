package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.waypoints.WaypointManager;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.util.Vec3;

/**
 * Created by Arithmo on 5/15/2017 at 1:25 AM.
 */
public class Waypoint extends Command {

    public Waypoint(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("del")) {
                if (args.length == 2) {
                    for (info.sigmaclient.management.waypoints.Waypoint waypoint : WaypointManager.getManager().getWaypoints()) {
                        if (waypoint.getName().equalsIgnoreCase(args[1])) {
                            WaypointManager.getManager().deleteWaypoint(waypoint);
                            ChatUtil.printChat(chatPrefix + "\2477Waypoint \247c" + args[1] + "\2477 has been removed.");
                            return;
                        }
                    }
                    ChatUtil.printChat(chatPrefix + "\2477No Waypoint under the name \247c" + args[1] + "\2477 was found.");
                    return;
                }
                printUsage();
                return;
            } else if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("add")) {
                //.wp add (arg 1) name (arg 2)
                if (args.length == 2) {
                    if (!WaypointManager.getManager().containsName(args[1])) {
                        int color = Colors.getColor((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()));
                        WaypointManager.getManager().createWaypoint(args[1], new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ), color, mc.getCurrentServerData().serverIP);
                        ChatUtil.printChat(chatPrefix + "\2477Waypoint \247c" + args[1] + "\2477 has been successfully created.");
                        return;
                    } else {
                        ChatUtil.printChat(chatPrefix + "\2477Waypoint \247c" + args[1] + "\2477 already exists.");
                        printUsage();
                        return;
                    }
                } else if (args.length == 5) {
                    if (!WaypointManager.getManager().containsName(args[1])) {
                        int color = Colors.getColor((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()));
                        WaypointManager.getManager().createWaypoint(args[1], new Vec3(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])), color, mc.getCurrentServerData().serverIP);
                        ChatUtil.printChat(chatPrefix + "\2477Waypoint \247c" + args[1] + " \2477has been successfully created.");
                        return;
                    } else {
                        ChatUtil.printChat(chatPrefix + "\2477Waypoint \247c" + args[1] + " \2477already exists.");
                        printUsage();
                        return;
                    }
                } else {
                    printUsage();
                    return;
                }
            }
        } else {
            printUsage();
            return;
        }
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public String getUsage() {
        return "add/del <name> or add <name> <x> <y> <z>";
    }

}
