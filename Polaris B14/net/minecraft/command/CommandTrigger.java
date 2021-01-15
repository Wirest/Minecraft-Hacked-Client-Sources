/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ItemInWorldManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class CommandTrigger
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  21 */     return "trigger";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  29 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  37 */     return "commands.trigger.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  45 */     if (args.length < 3)
/*     */     {
/*  47 */       throw new WrongUsageException("commands.trigger.usage", new Object[0]);
/*     */     }
/*     */     
/*     */     EntityPlayerMP entityplayermp;
/*     */     
/*     */     EntityPlayerMP entityplayermp;
/*  53 */     if ((sender instanceof EntityPlayerMP))
/*     */     {
/*  55 */       entityplayermp = (EntityPlayerMP)sender;
/*     */     }
/*     */     else
/*     */     {
/*  59 */       Entity entity = sender.getCommandSenderEntity();
/*     */       
/*  61 */       if (!(entity instanceof EntityPlayerMP))
/*     */       {
/*  63 */         throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
/*     */       }
/*     */       
/*  66 */       entityplayermp = (EntityPlayerMP)entity;
/*     */     }
/*     */     
/*  69 */     Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/*  70 */     ScoreObjective scoreobjective = scoreboard.getObjective(args[0]);
/*     */     
/*  72 */     if ((scoreobjective != null) && (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER))
/*     */     {
/*  74 */       int i = parseInt(args[2]);
/*     */       
/*  76 */       if (!scoreboard.entityHasObjective(entityplayermp.getName(), scoreobjective))
/*     */       {
/*  78 */         throw new CommandException("commands.trigger.invalidObjective", new Object[] { args[0] });
/*     */       }
/*     */       
/*     */ 
/*  82 */       Score score = scoreboard.getValueFromObjective(entityplayermp.getName(), scoreobjective);
/*     */       
/*  84 */       if (score.isLocked())
/*     */       {
/*  86 */         throw new CommandException("commands.trigger.disabled", new Object[] { args[0] });
/*     */       }
/*     */       
/*     */ 
/*  90 */       if ("set".equals(args[1]))
/*     */       {
/*  92 */         score.setScorePoints(i);
/*     */       }
/*     */       else
/*     */       {
/*  96 */         if (!"add".equals(args[1]))
/*     */         {
/*  98 */           throw new CommandException("commands.trigger.invalidMode", new Object[] { args[1] });
/*     */         }
/*     */         
/* 101 */         score.increseScore(i);
/*     */       }
/*     */       
/* 104 */       score.setLocked(true);
/*     */       
/* 106 */       if (entityplayermp.theItemInWorldManager.isCreative())
/*     */       {
/* 108 */         notifyOperators(sender, this, "commands.trigger.success", new Object[] { args[0], args[1], args[2] });
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 115 */       throw new CommandException("commands.trigger.invalidObjective", new Object[] { args[0] });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 122 */     if (args.length == 1)
/*     */     {
/* 124 */       Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/* 125 */       List<String> list = Lists.newArrayList();
/*     */       
/* 127 */       for (ScoreObjective scoreobjective : scoreboard.getScoreObjectives())
/*     */       {
/* 129 */         if (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER)
/*     */         {
/* 131 */           list.add(scoreobjective.getName());
/*     */         }
/*     */       }
/*     */       
/* 135 */       return getListOfStringsMatchingLastWord(args, (String[])list.toArray(new String[list.size()]));
/*     */     }
/*     */     
/*     */ 
/* 139 */     return args.length == 2 ? getListOfStringsMatchingLastWord(args, new String[] { "add", "set" }) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */