/*     */ package net.minecraft.command;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.play.server.S19PacketEntityStatus;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.GameRules.ValueType;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class CommandGameRule extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  18 */     return "gamerule";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  26 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  34 */     return "commands.gamerule.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  42 */     GameRules gamerules = getGameRules();
/*  43 */     String s = args.length > 0 ? args[0] : "";
/*  44 */     String s1 = args.length > 1 ? buildString(args, 1) : "";
/*     */     
/*  46 */     switch (args.length)
/*     */     {
/*     */     case 0: 
/*  49 */       sender.addChatMessage(new ChatComponentText(joinNiceString(gamerules.getRules())));
/*  50 */       break;
/*     */     
/*     */     case 1: 
/*  53 */       if (!gamerules.hasRule(s))
/*     */       {
/*  55 */         throw new CommandException("commands.gamerule.norule", new Object[] { s });
/*     */       }
/*     */       
/*  58 */       String s2 = gamerules.getString(s);
/*  59 */       sender.addChatMessage(new ChatComponentText(s).appendText(" = ").appendText(s2));
/*  60 */       sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
/*  61 */       break;
/*     */     
/*     */     default: 
/*  64 */       if ((gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE)) && (!"true".equals(s1)) && (!"false".equals(s1)))
/*     */       {
/*  66 */         throw new CommandException("commands.generic.boolean.invalid", new Object[] { s1 });
/*     */       }
/*     */       
/*  69 */       gamerules.setOrCreateGameRule(s, s1);
/*  70 */       func_175773_a(gamerules, s);
/*  71 */       notifyOperators(sender, this, "commands.gamerule.success", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void func_175773_a(GameRules p_175773_0_, String p_175773_1_)
/*     */   {
/*  77 */     if ("reducedDebugInfo".equals(p_175773_1_))
/*     */     {
/*  79 */       byte b0 = (byte)(p_175773_0_.getBoolean(p_175773_1_) ? 22 : 23);
/*     */       
/*  81 */       for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().func_181057_v())
/*     */       {
/*  83 */         entityplayermp.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(entityplayermp, b0));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/*  90 */     if (args.length == 1)
/*     */     {
/*  92 */       return getListOfStringsMatchingLastWord(args, getGameRules().getRules());
/*     */     }
/*     */     
/*     */ 
/*  96 */     if (args.length == 2)
/*     */     {
/*  98 */       GameRules gamerules = getGameRules();
/*     */       
/* 100 */       if (gamerules.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE))
/*     */       {
/* 102 */         return getListOfStringsMatchingLastWord(args, new String[] { "true", "false" });
/*     */       }
/*     */     }
/*     */     
/* 106 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private GameRules getGameRules()
/*     */   {
/* 115 */     return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandGameRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */