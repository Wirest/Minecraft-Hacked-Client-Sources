package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandXP extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "xp";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.xp.usage");
        }
        else
        {
            String s = args[0];
            boolean flag = s.endsWith("l") || s.endsWith("L");

            if (flag && s.length() > 1)
            {
                s = s.substring(0, s.length() - 1);
            }

            int i = parseInt(s);
            boolean flag1 = i < 0;

            if (flag1)
            {
                i *= -1;
            }

            EntityPlayer entityplayer = args.length > 1 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);

            if (flag)
            {
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);

                if (flag1)
                {
                    entityplayer.addExperienceLevel(-i);
                    notifyOperators(sender, this, "commands.xp.success.negative.levels", i, entityplayer.getCommandSenderName());
                }
                else
                {
                    entityplayer.addExperienceLevel(i);
                    notifyOperators(sender, this, "commands.xp.success.levels", i, entityplayer.getCommandSenderName());
                }
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);

                if (flag1)
                {
                    throw new CommandException("commands.xp.failure.widthdrawXp");
                }

                entityplayer.addExperience(i);
                notifyOperators(sender, this, "commands.xp.success", i, entityplayer.getCommandSenderName());
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
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
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}
