/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.world.WorldServer;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandToggleDownfall
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 13 */     return "toggledownfall";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 21 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 29 */     return "commands.downfall.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 37 */     toggleDownfall();
/* 38 */     notifyOperators(sender, this, "commands.downfall.success", new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void toggleDownfall()
/*    */   {
/* 46 */     WorldInfo worldinfo = net.minecraft.server.MinecraftServer.getServer().worldServers[0].getWorldInfo();
/* 47 */     worldinfo.setRaining(!worldinfo.isRaining());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandToggleDownfall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */