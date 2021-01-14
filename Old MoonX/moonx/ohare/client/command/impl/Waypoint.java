package moonx.ohare.client.command.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.utils.Printer;
import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.client.Minecraft;

public class Waypoint extends Command {

    public Waypoint() {
        super("Waypoint", new String[]{"waypoint", "wp", "way", "points"});
    }

    @Override
    public void onRun(String[] args) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            Printer.print("Waypoints cannot be used in singleplayer!");
            return;
        }
        if (args.length > 1) {
            switch (args[1]) {
                case "help":
                    Printer.print("[Add] [Name] [X] [Y] [Z] / [Del] [Name]");
                    break;
                case "reload":
                    Moonx.INSTANCE.getWaypointManager().getWaypoints().clear();
                    Moonx.INSTANCE.getWaypointManager().load();
                    Printer.print("Waypoints reloaded!");
                    break;
                case "clear":
                case "reset":
                    if (Moonx.INSTANCE.getWaypointManager().getWaypoints().isEmpty()) {
                        Printer.print("Waypoints list is empty!");
                    } else {
                        Moonx.INSTANCE.getWaypointManager().getWaypoints().clear();
                        Printer.print("Cleared waypoints list!");
                        Moonx.INSTANCE.getWaypointManager().save();
                    }
                    break;
                case "list":
                    if (Moonx.INSTANCE.getWaypointManager().getWaypoints().isEmpty()) {
                        Printer.print("Waypoints list is empty!");
                    } else {
                        Printer.print("Here is a list of all your waypoints:");
                        Moonx.INSTANCE.getWaypointManager().getWaypoints().forEach(waypoint -> {
                            if (Minecraft.getMinecraft().getCurrentServerData().serverIP.equals(waypoint.getServer()) && Minecraft.getMinecraft().thePlayer.dimension == waypoint.getDimension()) {
                                Printer.print(waypoint.getLabel() + " (" + waypoint.getX() + ", " + waypoint.getY() + ", " + waypoint.getZ() + ", " + waypoint.getServer() + ")");
                            }
                        });
                    }
                    break;
                case "add":
                    if (args.length == 6) {
                        if ((!NumberUtils.isNumber(args[2]) && NumberUtils.isNumber(args[3]) && NumberUtils.isNumber(args[4]) && NumberUtils.isNumber(args[5]))) {
                            if (!Moonx.INSTANCE.getWaypointManager().isWaypoint(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                                Printer.print("Waypoint " + args[2] + " (" + args[3] + ", " + args[4] + ", " + args[5] + ") has been added.");
                                Moonx.INSTANCE.getWaypointManager().add(args[2], Double.valueOf(args[3]), Double.valueOf(args[4]), Double.valueOf(args[5]), Minecraft.getMinecraft().getCurrentServerData().serverIP, Minecraft.getMinecraft().thePlayer.dimension);
                                Moonx.INSTANCE.getWaypointManager().save();
                            } else {
                                Printer.print(args[2] + " is already a waypoint!");
                            }
                        }
                    } else if (args.length == 3) {
                        if (!Moonx.INSTANCE.getWaypointManager().isWaypoint(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                            Printer.print("Waypoint " + args[2] + " (" + (int) Minecraft.getMinecraft().thePlayer.posX + ", " + (int) Minecraft.getMinecraft().thePlayer.posY + ", " + (int) Minecraft.getMinecraft().thePlayer.posZ + ") has been added.");
                            Moonx.INSTANCE.getWaypointManager().add(args[2], (int) Minecraft.getMinecraft().thePlayer.posX, (int) Minecraft.getMinecraft().thePlayer.posY, (int) Minecraft.getMinecraft().thePlayer.posZ, Minecraft.getMinecraft().getCurrentServerData().serverIP, Minecraft.getMinecraft().thePlayer.dimension);
                            Moonx.INSTANCE.getWaypointManager().save();
                        } else {
                            Printer.print(args[2] + " is already a waypoint!");
                        }
                    } else {
                        Printer.print("Either one of the needed variables is null or is a string/number!");
                    }
                    break;
                case "del":
                case "remove":
                    if (Moonx.INSTANCE.getWaypointManager().isWaypoint(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                        Printer.print(args[2] + " has been removed from your waypoints!");
                        Moonx.INSTANCE.getWaypointManager().remove(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP);
                        Moonx.INSTANCE.getWaypointManager().save();
                    } else {
                        Printer.print(args[2] + " is not a waypoint!");
                    }
                    break;
            }
        }
    }
}
