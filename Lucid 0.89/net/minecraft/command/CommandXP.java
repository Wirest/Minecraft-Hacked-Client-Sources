package net.minecraft.command;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandXP extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "xp";
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
        return "commands.xp.usage";
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
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
        else
        {
            String var3 = args[0];
            boolean var4 = var3.endsWith("l") || var3.endsWith("L");

            if (var4 && var3.length() > 1)
            {
                var3 = var3.substring(0, var3.length() - 1);
            }

            int var5 = parseInt(var3);
            boolean var6 = var5 < 0;

            if (var6)
            {
                var5 *= -1;
            }

            EntityPlayerMP var7 = args.length > 1 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);

            if (var4)
            {
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, var7.experienceLevel);

                if (var6)
                {
                    var7.addExperienceLevel(-var5);
                    notifyOperators(sender, this, "commands.xp.success.negative.levels", new Object[] {Integer.valueOf(var5), var7.getCommandSenderName()});
                }
                else
                {
                    var7.addExperienceLevel(var5);
                    notifyOperators(sender, this, "commands.xp.success.levels", new Object[] {Integer.valueOf(var5), var7.getCommandSenderName()});
                }
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, var7.experienceTotal);

                if (var6)
                {
                    throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
                }

                var7.addExperience(var5);
                notifyOperators(sender, this, "commands.xp.success", new Object[] {Integer.valueOf(var5), var7.getCommandSenderName()});
            }
        }
    }

    /**
     * Return a list of options when the user types TAB
     *  
     * @param sender The {@link ICommandSender sender} who pressed TAB
     * @param args The arguments that were present when TAB was pressed
     * @param pos The block that the player is targeting, <b>May be {@code null}</b>
     */
    @Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 2 ? getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : null;
    }

    protected String[] getAllUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
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
        return index == 1;
    }
}
