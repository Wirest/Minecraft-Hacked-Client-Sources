/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ import net.minecraft.server.management.UserListBans;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandPardonPlayer
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 19 */     return "pardon";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 27 */     return 3;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 35 */     return "commands.unban.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canCommandSenderUseCommand(ICommandSender sender)
/*    */   {
/* 43 */     return (MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer()) && (super.canCommandSenderUseCommand(sender));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 51 */     if ((args.length == 1) && (args[0].length() > 0))
/*    */     {
/* 53 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 54 */       GameProfile gameprofile = minecraftserver.getConfigurationManager().getBannedPlayers().isUsernameBanned(args[0]);
/*    */       
/* 56 */       if (gameprofile == null)
/*    */       {
/* 58 */         throw new CommandException("commands.unban.failed", new Object[] { args[0] });
/*    */       }
/*    */       
/*    */ 
/* 62 */       minecraftserver.getConfigurationManager().getBannedPlayers().removeEntry(gameprofile);
/* 63 */       notifyOperators(sender, this, "commands.unban.success", new Object[] { args[0] });
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/* 68 */       throw new WrongUsageException("commands.unban.usage", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 74 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandPardonPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */