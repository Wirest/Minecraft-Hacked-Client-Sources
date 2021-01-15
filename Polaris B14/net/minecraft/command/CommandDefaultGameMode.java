/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.world.WorldSettings.GameType;
/*    */ 
/*    */ 
/*    */ public class CommandDefaultGameMode
/*    */   extends CommandGameMode
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 15 */     return "defaultgamemode";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 23 */     return "commands.defaultgamemode.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 31 */     if (args.length <= 0)
/*    */     {
/* 33 */       throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 37 */     WorldSettings.GameType worldsettings$gametype = getGameModeFromCommand(sender, args[0]);
/* 38 */     setGameType(worldsettings$gametype);
/* 39 */     notifyOperators(sender, this, "commands.defaultgamemode.success", new Object[] { new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]) });
/*    */   }
/*    */   
/*    */ 
/*    */   protected void setGameType(WorldSettings.GameType p_71541_1_)
/*    */   {
/* 45 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 46 */     minecraftserver.setGameType(p_71541_1_);
/*    */     
/* 48 */     if (minecraftserver.getForceGamemode())
/*    */     {
/* 50 */       for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().func_181057_v())
/*    */       {
/* 52 */         entityplayermp.setGameType(p_71541_1_);
/* 53 */         entityplayermp.fallDistance = 0.0F;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandDefaultGameMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */