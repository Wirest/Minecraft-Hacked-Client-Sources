package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "setidletimeout";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.setidletimeout.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length != 1)
        {
            throw new WrongUsageException("commands.setidletimeout.usage");
        }
        else
        {
            int i = parseInt(args[0], 0);
            MinecraftServer.getServer().setPlayerIdleTimeout(i);
            notifyOperators(sender, this, "commands.setidletimeout.success", i);
        }
    }
}
