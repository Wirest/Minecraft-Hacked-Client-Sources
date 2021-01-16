package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandReload extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "reload";
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
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.reload.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0)
        {
            throw new WrongUsageException("commands.reload.usage", new Object[0]);
        }
        else
        {
            server.func_193031_aM();
            notifyCommandListener(sender, this, "commands.reload.success", new Object[0]);
        }
    }
}
