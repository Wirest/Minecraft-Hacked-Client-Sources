package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "time";
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
        return "commands.time.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 1)
        {
            if (args[0].equals("set"))
            {
                int l;

                if (args[1].equals("day"))
                {
                    l = 1000;
                }
                else if (args[1].equals("night"))
                {
                    l = 13000;
                }
                else
                {
                    l = parseInt(args[1], 0);
                }

                this.setTime(sender, l);
                notifyOperators(sender, this, "commands.time.set", l);
                return;
            }

            if (args[0].equals("add"))
            {
                int k = parseInt(args[1], 0);
                this.addTime(sender, k);
                notifyOperators(sender, this, "commands.time.added", k);
                return;
            }

            if (args[0].equals("query"))
            {
                if (args[1].equals("daytime"))
                {
                    int j = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
                    notifyOperators(sender, this, "commands.time.query", j);
                    return;
                }

                if (args[1].equals("gametime"))
                {
                    int i = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
                    notifyOperators(sender, this, "commands.time.query", i);
                    return;
                }
            }
        }

        throw new WrongUsageException("commands.time.usage");
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "set", "add", "query"): (args.length == 2 && args[0].equals("set") ? getListOfStringsMatchingLastWord(args, "day", "night"): (args.length == 2 && args[0].equals("query") ? getListOfStringsMatchingLastWord(args, "daytime", "gametime"): null));
    }

    /**
     * Set the time in the server object.
     */
    protected void setTime(ICommandSender p_71552_1_, int p_71552_2_)
    {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i)
        {
            MinecraftServer.getServer().worldServers[i].setWorldTime(p_71552_2_);
        }
    }

    /**
     * Adds (or removes) time in the server object.
     */
    protected void addTime(ICommandSender p_71553_1_, int p_71553_2_)
    {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i)
        {
            WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
            worldserver.setWorldTime(worldserver.getWorldTime() + (long)p_71553_2_);
        }
    }
}
