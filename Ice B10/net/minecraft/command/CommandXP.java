package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandXP extends CommandBase
{
    private static final String __OBFID = "CL_00000398";

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

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.xp.usage";
    }

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
                sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var7.experienceLevel);

                if (var6)
                {
                    var7.addExperienceLevel(-var5);
                    notifyOperators(sender, this, "commands.xp.success.negative.levels", new Object[] {Integer.valueOf(var5), var7.getName()});
                }
                else
                {
                    var7.addExperienceLevel(var5);
                    notifyOperators(sender, this, "commands.xp.success.levels", new Object[] {Integer.valueOf(var5), var7.getName()});
                }
            }
            else
            {
                sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var7.experienceTotal);

                if (var6)
                {
                    throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
                }

                var7.addExperience(var5);
                notifyOperators(sender, this, "commands.xp.success", new Object[] {Integer.valueOf(var5), var7.getName()});
            }
        }
    }

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
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}
