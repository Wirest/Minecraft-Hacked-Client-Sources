package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;

public class CommandPublishLocalServer extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "publish";
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.publish.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        String s = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);

        if (s != null)
        {
            notifyOperators(sender, this, "commands.publish.started", s);
        }
        else
        {
            notifyOperators(sender, this, "commands.publish.failed");
        }
    }
}
