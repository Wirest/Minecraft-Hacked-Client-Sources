package net.minecraft.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandTrigger extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "trigger";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
	public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    @Override
	public String getCommandUsage(ICommandSender sender)
    {
        return "commands.trigger.usage";
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
        if (args.length < 3)
        {
            throw new WrongUsageException("commands.trigger.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3;

            if (sender instanceof EntityPlayerMP)
            {
                var3 = (EntityPlayerMP)sender;
            }
            else
            {
                Entity var4 = sender.getCommandSenderEntity();

                if (!(var4 instanceof EntityPlayerMP))
                {
                    throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
                }

                var3 = (EntityPlayerMP)var4;
            }

            Scoreboard var8 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
            ScoreObjective var5 = var8.getObjective(args[0]);

            if (var5 != null && var5.getCriteria() == IScoreObjectiveCriteria.TRIGGER)
            {
                int var6 = parseInt(args[2]);

                if (!var8.entityHasObjective(var3.getCommandSenderName(), var5))
                {
                    throw new CommandException("commands.trigger.invalidObjective", new Object[] {args[0]});
                }
                else
                {
                    Score var7 = var8.getValueFromObjective(var3.getCommandSenderName(), var5);

                    if (var7.isLocked())
                    {
                        throw new CommandException("commands.trigger.disabled", new Object[] {args[0]});
                    }
                    else
                    {
                        if ("set".equals(args[1]))
                        {
                            var7.setScorePoints(var6);
                        }
                        else
                        {
                            if (!"add".equals(args[1]))
                            {
                                throw new CommandException("commands.trigger.invalidMode", new Object[] {args[1]});
                            }

                            var7.increseScore(var6);
                        }

                        var7.setLocked(true);

                        if (var3.theItemInWorldManager.isCreative())
                        {
                            notifyOperators(sender, this, "commands.trigger.success", new Object[] {args[0], args[1], args[2]});
                        }
                    }
                }
            }
            else
            {
                throw new CommandException("commands.trigger.invalidObjective", new Object[] {args[0]});
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
        if (args.length == 1)
        {
            Scoreboard var4 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
            ArrayList var5 = Lists.newArrayList();
            Iterator var6 = var4.getScoreObjectives().iterator();

            while (var6.hasNext())
            {
                ScoreObjective var7 = (ScoreObjective)var6.next();

                if (var7.getCriteria() == IScoreObjectiveCriteria.TRIGGER)
                {
                    var5.add(var7.getName());
                }
            }

            return getListOfStringsMatchingLastWord(args, (String[])var5.toArray(new String[var5.size()]));
        }
        else
        {
            return args.length == 2 ? getListOfStringsMatchingLastWord(args, new String[] {"add", "set"}): null;
        }
    }
}
