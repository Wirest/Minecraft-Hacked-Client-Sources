package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandKill extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "kill";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
	public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    @Override
	public String getCommandUsage(ICommandSender sender)
    {
        return "commands.kill.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    @Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 0)
        {
            EntityPlayerMP var4 = getCommandSenderAsPlayer(sender);
            var4.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {var4.getDisplayName()});
        }
        else
        {
            Entity var3 = func_175768_b(sender, args[0]);
            var3.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {var3.getDisplayName()});
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *  
     * @param args The arguments that were given
     * @param index The argument index that we are checking
     */
    @Override
	public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
