package splash.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import splash.Splash;
import splash.api.command.Command;
import splash.api.friend.Friend;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice
 * Created: 19:15, 23-Jun-20
 * Project: utility mod
 */
public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend");
    }

    @Override
    public String usage() {
        return "friend add <name>, remove <name>";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
    if(commandArguments.length != 2) {
        printUsage();
    }

        if(commandArguments.length == 2) {
            if(commandArguments[0].equalsIgnoreCase("add")) {
                for(Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                    if(entity instanceof EntityPlayer) {
                        String name = entity.getName();

                        if(name.equalsIgnoreCase(commandArguments[1])) {
                            ClientLogger.printToMinecraft("Added friend " + commandArguments[1]);
                            Splash.getInstance().getFriendManager().addContent(new Friend(name));
                        }
                    }
                }
            } else if(commandArguments[0].equalsIgnoreCase("remove")) {
                if(Splash.getInstance().getFriendManager().getContents().contains(Splash.getInstance().getFriendManager().getFriendByName(commandArguments[1]))) {
                    ClientLogger.printToMinecraft("Removed friend " + commandArguments[1]);
                    Splash.getInstance().getFriendManager().getContents().remove(Splash.getInstance().getFriendManager().getFriendByName(commandArguments[1]));
                } else {
                    ClientLogger.printToMinecraft("That player is not your friend.");
                }
            }
        }
    }
}
