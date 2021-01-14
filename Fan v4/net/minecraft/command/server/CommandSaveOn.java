package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOn extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "save-on";
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.save-on.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        boolean flag = false;

        for (int i = 0; i < minecraftserver.worldServers.length; ++i)
        {
            if (minecraftserver.worldServers[i] != null)
            {
                WorldServer worldserver = minecraftserver.worldServers[i];

                if (worldserver.disableLevelSaving)
                {
                    worldserver.disableLevelSaving = false;
                    flag = true;
                }
            }
        }

        if (flag)
        {
            notifyOperators(sender, this, "commands.save.enabled");
        }
        else
        {
            throw new CommandException("commands.save-on.alreadyOn");
        }
    }
}
