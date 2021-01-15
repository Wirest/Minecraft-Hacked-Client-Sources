/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.stats.Achievement;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.stats.StatisticsFile;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ 
/*     */ public class CommandAchievement
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  26 */     return "achievement";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  34 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  42 */     return "commands.achievement.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  50 */     if (args.length < 2)
/*     */     {
/*  52 */       throw new WrongUsageException("commands.achievement.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  56 */     final StatBase statbase = StatList.getOneShotStat(args[1]);
/*     */     
/*  58 */     if ((statbase == null) && (!args[1].equals("*")))
/*     */     {
/*  60 */       throw new CommandException("commands.achievement.unknownAchievement", new Object[] { args[1] });
/*     */     }
/*     */     
/*     */ 
/*  64 */     final EntityPlayerMP entityplayermp = args.length >= 3 ? getPlayer(sender, args[2]) : getCommandSenderAsPlayer(sender);
/*  65 */     boolean flag = args[0].equalsIgnoreCase("give");
/*  66 */     boolean flag1 = args[0].equalsIgnoreCase("take");
/*     */     
/*  68 */     if ((flag) || (flag1))
/*     */     {
/*  70 */       if (statbase == null)
/*     */       {
/*  72 */         if (flag)
/*     */         {
/*  74 */           for (Achievement achievement4 : AchievementList.achievementList)
/*     */           {
/*  76 */             entityplayermp.triggerAchievement(achievement4);
/*     */           }
/*     */           
/*  79 */           notifyOperators(sender, this, "commands.achievement.give.success.all", new Object[] { entityplayermp.getName() });
/*     */         }
/*  81 */         else if (flag1)
/*     */         {
/*  83 */           for (Achievement achievement5 : Lists.reverse(AchievementList.achievementList))
/*     */           {
/*  85 */             entityplayermp.func_175145_a(achievement5);
/*     */           }
/*     */           
/*  88 */           notifyOperators(sender, this, "commands.achievement.take.success.all", new Object[] { entityplayermp.getName() });
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  93 */         if ((statbase instanceof Achievement))
/*     */         {
/*  95 */           Achievement achievement = (Achievement)statbase;
/*     */           
/*  97 */           if (flag)
/*     */           {
/*  99 */             if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement))
/*     */             {
/* 101 */               throw new CommandException("commands.achievement.alreadyHave", new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 106 */             for (Object list = Lists.newArrayList(); (achievement.parentAchievement != null) && (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement.parentAchievement)); achievement = achievement.parentAchievement)
/*     */             {
/* 108 */               ((List)list).add(achievement.parentAchievement);
/*     */             }
/*     */             
/* 111 */             for (Achievement achievement1 : Lists.reverse((List)list))
/*     */             {
/* 113 */               entityplayermp.triggerAchievement(achievement1);
/*     */             }
/*     */           }
/* 116 */           else if (flag1)
/*     */           {
/* 118 */             if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement))
/*     */             {
/* 120 */               throw new CommandException("commands.achievement.dontHave", new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
/*     */             }
/*     */             
/* 123 */             Object list1 = Lists.newArrayList(Iterators.filter(AchievementList.achievementList.iterator(), new Predicate()
/*     */             {
/*     */               public boolean apply(Achievement p_apply_1_)
/*     */               {
/* 127 */                 return (entityplayermp.getStatFile().hasAchievementUnlocked(p_apply_1_)) && (p_apply_1_ != statbase);
/*     */               }
/* 129 */             }));
/* 130 */             List<Achievement> list2 = Lists.newArrayList((Iterable)list1);
/*     */             
/* 132 */             for (Achievement achievement2 : (List)list1)
/*     */             {
/* 134 */               Achievement achievement3 = achievement2;
/*     */               
/*     */ 
/* 137 */               for (boolean flag2 = false; achievement3 != null; achievement3 = achievement3.parentAchievement)
/*     */               {
/* 139 */                 if (achievement3 == statbase)
/*     */                 {
/* 141 */                   flag2 = true;
/*     */                 }
/*     */               }
/*     */               
/* 145 */               if (!flag2)
/*     */               {
/* 147 */                 for (achievement3 = achievement2; achievement3 != null; achievement3 = achievement3.parentAchievement)
/*     */                 {
/* 149 */                   list2.remove(achievement2);
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 154 */             for (Achievement achievement6 : list2)
/*     */             {
/* 156 */               entityplayermp.func_175145_a(achievement6);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 161 */         if (flag)
/*     */         {
/* 163 */           entityplayermp.triggerAchievement(statbase);
/* 164 */           notifyOperators(sender, this, "commands.achievement.give.success.one", new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
/*     */         }
/* 166 */         else if (flag1)
/*     */         {
/* 168 */           entityplayermp.func_175145_a(statbase);
/* 169 */           notifyOperators(sender, this, "commands.achievement.take.success.one", new Object[] { statbase.func_150955_j(), entityplayermp.getName() });
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 179 */     if (args.length == 1)
/*     */     {
/* 181 */       return getListOfStringsMatchingLastWord(args, new String[] { "give", "take" });
/*     */     }
/* 183 */     if (args.length != 2)
/*     */     {
/* 185 */       return args.length == 3 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*     */     }
/*     */     
/*     */ 
/* 189 */     List<String> list = Lists.newArrayList();
/*     */     
/* 191 */     for (StatBase statbase : StatList.allStats)
/*     */     {
/* 193 */       list.add(statbase.statId);
/*     */     }
/*     */     
/* 196 */     return getListOfStringsMatchingLastWord(args, list);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 205 */     return index == 2;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandAchievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */