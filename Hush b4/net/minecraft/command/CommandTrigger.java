// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandTrigger extends CommandBase
{
    @Override
    public String getCommandName() {
        return "trigger";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.trigger.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 3) {
            throw new WrongUsageException("commands.trigger.usage", new Object[0]);
        }
        EntityPlayerMP entityplayermp;
        if (sender instanceof EntityPlayerMP) {
            entityplayermp = (EntityPlayerMP)sender;
        }
        else {
            final Entity entity = sender.getCommandSenderEntity();
            if (!(entity instanceof EntityPlayerMP)) {
                throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
            }
            entityplayermp = (EntityPlayerMP)entity;
        }
        final Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
        final ScoreObjective scoreobjective = scoreboard.getObjective(args[0]);
        if (scoreobjective == null || scoreobjective.getCriteria() != IScoreObjectiveCriteria.TRIGGER) {
            throw new CommandException("commands.trigger.invalidObjective", new Object[] { args[0] });
        }
        final int i = CommandBase.parseInt(args[2]);
        if (!scoreboard.entityHasObjective(entityplayermp.getName(), scoreobjective)) {
            throw new CommandException("commands.trigger.invalidObjective", new Object[] { args[0] });
        }
        final Score score = scoreboard.getValueFromObjective(entityplayermp.getName(), scoreobjective);
        if (score.isLocked()) {
            throw new CommandException("commands.trigger.disabled", new Object[] { args[0] });
        }
        if ("set".equals(args[1])) {
            score.setScorePoints(i);
        }
        else {
            if (!"add".equals(args[1])) {
                throw new CommandException("commands.trigger.invalidMode", new Object[] { args[1] });
            }
            score.increseScore(i);
        }
        score.setLocked(true);
        if (entityplayermp.theItemInWorldManager.isCreative()) {
            CommandBase.notifyOperators(sender, this, "commands.trigger.success", args[0], args[1], args[2]);
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            final Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
            final List<String> list = (List<String>)Lists.newArrayList();
            for (final ScoreObjective scoreobjective : scoreboard.getScoreObjectives()) {
                if (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER) {
                    list.add(scoreobjective.getName());
                }
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, (String[])list.toArray(new String[list.size()]));
        }
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, "add", "set") : null;
    }
}
