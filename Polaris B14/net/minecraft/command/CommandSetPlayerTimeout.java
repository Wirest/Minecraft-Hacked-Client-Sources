/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandSetPlayerTimeout
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 12 */     return "setidletimeout";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 20 */     return 3;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 28 */     return "commands.setidletimeout.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 36 */     if (args.length != 1)
/*    */     {
/* 38 */       throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 42 */     int i = parseInt(args[0], 0);
/* 43 */     MinecraftServer.getServer().setPlayerIdleTimeout(i);
/* 44 */     notifyOperators(sender, this, "commands.setidletimeout.success", new Object[] { Integer.valueOf(i) });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandSetPlayerTimeout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */