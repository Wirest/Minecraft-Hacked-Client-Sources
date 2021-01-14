package net.minecraft.command.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;

public class CommandAchievement extends CommandBase {
   public String getCommandName() {
      return "achievement";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.achievement.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 2) {
         throw new WrongUsageException("commands.achievement.usage", new Object[0]);
      } else {
         final StatBase statbase = StatList.getOneShotStat(args[1]);
         if (statbase == null && !args[1].equals("*")) {
            throw new CommandException("commands.achievement.unknownAchievement", new Object[]{args[1]});
         } else {
            final EntityPlayerMP entityplayermp = args.length >= 3 ? getPlayer(sender, args[2]) : getCommandSenderAsPlayer(sender);
            boolean flag = args[0].equalsIgnoreCase("give");
            boolean flag1 = args[0].equalsIgnoreCase("take");
            if (flag || flag1) {
               if (statbase == null) {
                  Iterator var7;
                  Achievement achievement5;
                  if (flag) {
                     var7 = AchievementList.achievementList.iterator();

                     while(var7.hasNext()) {
                        achievement5 = (Achievement)var7.next();
                        entityplayermp.triggerAchievement(achievement5);
                     }

                     notifyOperators(sender, this, "commands.achievement.give.success.all", new Object[]{entityplayermp.getName()});
                  } else if (flag1) {
                     var7 = Lists.reverse(AchievementList.achievementList).iterator();

                     while(var7.hasNext()) {
                        achievement5 = (Achievement)var7.next();
                        entityplayermp.func_175145_a(achievement5);
                     }

                     notifyOperators(sender, this, "commands.achievement.take.success.all", new Object[]{entityplayermp.getName()});
                  }
               } else {
                  if (statbase instanceof Achievement) {
                     Achievement achievement = (Achievement)statbase;
                     ArrayList list1;
                     if (flag) {
                        if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
                           throw new CommandException("commands.achievement.alreadyHave", new Object[]{entityplayermp.getName(), statbase.func_150955_j()});
                        }

                        for(list1 = Lists.newArrayList(); achievement.parentAchievement != null && !entityplayermp.getStatFile().hasAchievementUnlocked(achievement.parentAchievement); achievement = achievement.parentAchievement) {
                           list1.add(achievement.parentAchievement);
                        }

                        Iterator var16 = Lists.reverse(list1).iterator();

                        while(var16.hasNext()) {
                           Achievement achievement1 = (Achievement)var16.next();
                           entityplayermp.triggerAchievement(achievement1);
                        }
                     } else if (flag1) {
                        if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
                           throw new CommandException("commands.achievement.dontHave", new Object[]{entityplayermp.getName(), statbase.func_150955_j()});
                        }

                        list1 = Lists.newArrayList(Iterators.filter(AchievementList.achievementList.iterator(), new Predicate() {
                           public boolean apply(Achievement p_apply_1_) {
                              return entityplayermp.getStatFile().hasAchievementUnlocked(p_apply_1_) && p_apply_1_ != statbase;
                           }
                        }));
                        List list2 = Lists.newArrayList(list1);
                        Iterator var10 = list1.iterator();

                        label111:
                        while(true) {
                           Achievement achievement2;
                           Achievement achievement3;
                           boolean flag2;
                           do {
                              if (!var10.hasNext()) {
                                 var10 = list2.iterator();

                                 while(true) {
                                    if (!var10.hasNext()) {
                                       break label111;
                                    }

                                    achievement2 = (Achievement)var10.next();
                                    entityplayermp.func_175145_a(achievement2);
                                 }
                              }

                              achievement2 = (Achievement)var10.next();
                              achievement3 = achievement2;

                              for(flag2 = false; achievement3 != null; achievement3 = achievement3.parentAchievement) {
                                 if (achievement3 == statbase) {
                                    flag2 = true;
                                 }
                              }
                           } while(flag2);

                           for(achievement3 = achievement2; achievement3 != null; achievement3 = achievement3.parentAchievement) {
                              list2.remove(achievement2);
                           }
                        }
                     }
                  }

                  if (flag) {
                     entityplayermp.triggerAchievement(statbase);
                     notifyOperators(sender, this, "commands.achievement.give.success.one", new Object[]{entityplayermp.getName(), statbase.func_150955_j()});
                  } else if (flag1) {
                     entityplayermp.func_175145_a(statbase);
                     notifyOperators(sender, this, "commands.achievement.take.success.one", new Object[]{statbase.func_150955_j(), entityplayermp.getName()});
                  }
               }
            }

         }
      }
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length == 1) {
         return getListOfStringsMatchingLastWord(args, new String[]{"give", "take"});
      } else if (args.length != 2) {
         return args.length == 3 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
      } else {
         List list = Lists.newArrayList();
         Iterator var5 = StatList.allStats.iterator();

         while(var5.hasNext()) {
            StatBase statbase = (StatBase)var5.next();
            list.add(statbase.statId);
         }

         return getListOfStringsMatchingLastWord(args, list);
      }
   }

   public boolean isUsernameIndex(String[] args, int index) {
      return index == 2;
   }
}
