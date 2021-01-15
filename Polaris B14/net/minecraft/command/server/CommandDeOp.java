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
/*    */ import net.minecraft.server.management.UserListOps;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandDeOp
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 19 */     return "deop";
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
/* 35 */     return "commands.deop.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 43 */     if ((args.length == 1) && (args[0].length() > 0))
/*    */     {
/* 45 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 46 */       GameProfile gameprofile = minecraftserver.getConfigurationManager().getOppedPlayers().getGameProfileFromName(args[0]);
/*    */       
/* 48 */       if (gameprofile == null)
/*    */       {
/* 50 */         throw new CommandException("commands.deop.failed", new Object[] { args[0] });
/*    */       }
/*    */       
/*    */ 
/* 54 */       minecraftserver.getConfigurationManager().removeOp(gameprofile);
/* 55 */       notifyOperators(sender, this, "commands.deop.success", new Object[] { args[0] });
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/* 60 */       throw new WrongUsageException("commands.deop.usage", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 66 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandDeOp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */