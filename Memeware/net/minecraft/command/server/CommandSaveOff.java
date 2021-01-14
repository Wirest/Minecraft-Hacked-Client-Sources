package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOff extends CommandBase {
    private static final String __OBFID = "CL_00000847";

    public String getCommandName() {
        return "save-off";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.save-off.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        MinecraftServer var3 = MinecraftServer.getServer();
        boolean var4 = false;

        for (int var5 = 0; var5 < var3.worldServers.length; ++var5) {
            if (var3.worldServers[var5] != null) {
                WorldServer var6 = var3.worldServers[var5];

                if (!var6.disableLevelSaving) {
                    var6.disableLevelSaving = true;
                    var4 = true;
                }
            }
        }

        if (var4) {
            notifyOperators(sender, this, "commands.save.disabled", new Object[0]);
        } else {
            throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
        }
    }
}
