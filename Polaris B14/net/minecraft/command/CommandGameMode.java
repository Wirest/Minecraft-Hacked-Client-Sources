/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.GameRules;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.WorldSettings;
/*    */ import net.minecraft.world.WorldSettings.GameType;
/*    */ 
/*    */ public class CommandGameMode extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 18 */     return "gamemode";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 26 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 34 */     return "commands.gamemode.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 42 */     if (args.length <= 0)
/*    */     {
/* 44 */       throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 48 */     WorldSettings.GameType worldsettings$gametype = getGameModeFromCommand(sender, args[0]);
/* 49 */     EntityPlayer entityplayer = args.length >= 2 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
/* 50 */     entityplayer.setGameType(worldsettings$gametype);
/* 51 */     entityplayer.fallDistance = 0.0F;
/*    */     
/* 53 */     if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback"))
/*    */     {
/* 55 */       entityplayer.addChatMessage(new ChatComponentTranslation("gameMode.changed", new Object[0]));
/*    */     }
/*    */     
/* 58 */     IChatComponent ichatcomponent = new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]);
/*    */     
/* 60 */     if (entityplayer != sender)
/*    */     {
/* 62 */       notifyOperators(sender, this, 1, "commands.gamemode.success.other", new Object[] { entityplayer.getName(), ichatcomponent });
/*    */     }
/*    */     else
/*    */     {
/* 66 */       notifyOperators(sender, this, 1, "commands.gamemode.success.self", new Object[] { ichatcomponent });
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected WorldSettings.GameType getGameModeFromCommand(ICommandSender p_71539_1_, String p_71539_2_)
/*    */     throws CommandException, NumberInvalidException
/*    */   {
/* 76 */     return (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName())) && (!p_71539_2_.equalsIgnoreCase("s")) ? WorldSettings.GameType.CREATIVE : (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName())) && (!p_71539_2_.equalsIgnoreCase("c")) ? WorldSettings.GameType.ADVENTURE : (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName())) && (!p_71539_2_.equalsIgnoreCase("a")) ? WorldSettings.GameType.SPECTATOR : (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName())) && (!p_71539_2_.equalsIgnoreCase("sp")) ? WorldSettings.getGameTypeById(parseInt(p_71539_2_, 0, WorldSettings.GameType.values().length - 2)) : WorldSettings.GameType.SURVIVAL;
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 81 */     return args.length == 2 ? getListOfStringsMatchingLastWord(args, getListOfPlayerUsernames()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "survival", "creative", "adventure", "spectator" }) : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String[] getListOfPlayerUsernames()
/*    */   {
/* 89 */     return MinecraftServer.getServer().getAllUsernames();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUsernameIndex(String[] args, int index)
/*    */   {
/* 97 */     return index == 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandGameMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */