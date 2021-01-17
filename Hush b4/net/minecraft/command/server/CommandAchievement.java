// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import java.util.List;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommand;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.command.CommandException;
import net.minecraft.stats.StatList;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandAchievement extends CommandBase
{
    @Override
    public String getCommandName() {
        return "achievement";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.achievement.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.achievement.usage", new Object[0]);
        }
        final StatBase statbase = StatList.getOneShotStat(args[1]);
        if (statbase == null && !args[1].equals("*")) {
            throw new CommandException("commands.achievement.unknownAchievement", new Object[] { args[1] });
        }
        final EntityPlayerMP entityplayermp = (args.length >= 3) ? CommandBase.getPlayer(sender, args[2]) : CommandBase.getCommandSenderAsPlayer(sender);
        final boolean flag = args[0].equalsIgnoreCase("give");
        final boolean flag2 = args[0].equalsIgnoreCase("take");
        if (flag || flag2) {
            if (statbase == null) {
                if (flag) {
                    for (final Achievement achievement4 : AchievementList.achievementList) {
                        entityplayermp.triggerAchievement(achievement4);
                    }
                    CommandBase.notifyOperators(sender, this, "commands.achievement.give.success.all", entityplayermp.getName());
                }
                else if (flag2) {
                    for (final Achievement achievement5 : Lists.reverse(AchievementList.achievementList)) {
                        entityplayermp.func_175145_a(achievement5);
                    }
                    CommandBase.notifyOperators(sender, this, "commands.achievement.take.success.all", entityplayermp.getName());
                }
            }
            else {
                if (statbase instanceof Achievement) {
                    Achievement achievement6 = (Achievement)statbase;
                    if (flag) {
                        if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement6)) {
                            throw new CommandException("commands.achievement.alreadyHave", new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
                        }
                        final List<Achievement> list = (List<Achievement>)Lists.newArrayList();
                        while (achievement6.parentAchievement != null && !entityplayermp.getStatFile().hasAchievementUnlocked(achievement6.parentAchievement)) {
                            list.add(achievement6.parentAchievement);
                            achievement6 = achievement6.parentAchievement;
                        }
                        for (final Achievement achievement7 : Lists.reverse(list)) {
                            entityplayermp.triggerAchievement(achievement7);
                        }
                    }
                    else if (flag2) {
                        if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement6)) {
                            throw new CommandException("commands.achievement.dontHave", new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
                        }
                        final List<Achievement> list2 = (List<Achievement>)Lists.newArrayList((Iterator<?>)Iterators.filter(AchievementList.achievementList.iterator(), new Predicate<Achievement>() {
                            @Override
                            public boolean apply(final Achievement p_apply_1_) {
                                return entityplayermp.getStatFile().hasAchievementUnlocked(p_apply_1_) && p_apply_1_ != statbase;
                            }
                        }));
                        final List<Achievement> list3 = (List<Achievement>)Lists.newArrayList((Iterable<?>)list2);
                        for (Achievement achievement9 : list2) {
                            final Achievement achievement8 = achievement9;
                            boolean flag3 = false;
                            while (achievement9 != null) {
                                if (achievement9 == statbase) {
                                    flag3 = true;
                                }
                                achievement9 = achievement9.parentAchievement;
                            }
                            if (!flag3) {
                                for (achievement9 = achievement8; achievement9 != null; achievement9 = achievement9.parentAchievement) {
                                    list3.remove(achievement8);
                                }
                            }
                        }
                        for (final Achievement achievement10 : list3) {
                            entityplayermp.func_175145_a(achievement10);
                        }
                    }
                }
                if (flag) {
                    entityplayermp.triggerAchievement(statbase);
                    CommandBase.notifyOperators(sender, this, "commands.achievement.give.success.one", entityplayermp.getName(), statbase.func_150955_j());
                }
                else if (flag2) {
                    entityplayermp.func_175145_a(statbase);
                    CommandBase.notifyOperators(sender, this, "commands.achievement.take.success.one", statbase.func_150955_j(), entityplayermp.getName());
                }
            }
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "give", "take");
        }
        if (args.length != 2) {
            return (args.length == 3) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
        }
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final StatBase statbase : StatList.allStats) {
            list.add(statbase.statId);
        }
        return CommandBase.getListOfStringsMatchingLastWord(args, list);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 2;
    }
}
