/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ 
/*     */ public class CommandTime
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  15 */     return "time";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  23 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  31 */     return "commands.time.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  39 */     if (args.length > 1)
/*     */     {
/*  41 */       if (args[0].equals("set"))
/*     */       {
/*     */         int l;
/*     */         int l;
/*  45 */         if (args[1].equals("day"))
/*     */         {
/*  47 */           l = 1000;
/*     */         } else { int l;
/*  49 */           if (args[1].equals("night"))
/*     */           {
/*  51 */             l = 13000;
/*     */           }
/*     */           else
/*     */           {
/*  55 */             l = parseInt(args[1], 0);
/*     */           }
/*     */         }
/*  58 */         setTime(sender, l);
/*  59 */         notifyOperators(sender, this, "commands.time.set", new Object[] { Integer.valueOf(l) });
/*  60 */         return;
/*     */       }
/*     */       
/*  63 */       if (args[0].equals("add"))
/*     */       {
/*  65 */         int k = parseInt(args[1], 0);
/*  66 */         addTime(sender, k);
/*  67 */         notifyOperators(sender, this, "commands.time.added", new Object[] { Integer.valueOf(k) });
/*  68 */         return;
/*     */       }
/*     */       
/*  71 */       if (args[0].equals("query"))
/*     */       {
/*  73 */         if (args[1].equals("daytime"))
/*     */         {
/*  75 */           int j = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
/*  76 */           sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
/*  77 */           notifyOperators(sender, this, "commands.time.query", new Object[] { Integer.valueOf(j) });
/*  78 */           return;
/*     */         }
/*     */         
/*  81 */         if (args[1].equals("gametime"))
/*     */         {
/*  83 */           int i = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
/*  84 */           sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
/*  85 */           notifyOperators(sender, this, "commands.time.query", new Object[] { Integer.valueOf(i) });
/*  86 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  91 */     throw new WrongUsageException("commands.time.usage", new Object[0]);
/*     */   }
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/*  96 */     return (args.length == 2) && (args[0].equals("query")) ? getListOfStringsMatchingLastWord(args, new String[] { "daytime", "gametime" }) : (args.length == 2) && (args[0].equals("set")) ? getListOfStringsMatchingLastWord(args, new String[] { "day", "night" }) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "set", "add", "query" }) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setTime(ICommandSender p_71552_1_, int p_71552_2_)
/*     */   {
/* 104 */     for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++)
/*     */     {
/* 106 */       MinecraftServer.getServer().worldServers[i].setWorldTime(p_71552_2_);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addTime(ICommandSender p_71553_1_, int p_71553_2_)
/*     */   {
/* 115 */     for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++)
/*     */     {
/* 117 */       WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
/* 118 */       worldserver.setWorldTime(worldserver.getWorldTime() + p_71553_2_);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */