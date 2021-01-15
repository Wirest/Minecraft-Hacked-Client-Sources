/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.WorldServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandSaveOn
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 16 */     return "save-on";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 24 */     return "commands.save-on.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 32 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 33 */     boolean flag = false;
/*    */     
/* 35 */     for (int i = 0; i < minecraftserver.worldServers.length; i++)
/*    */     {
/* 37 */       if (minecraftserver.worldServers[i] != null)
/*    */       {
/* 39 */         WorldServer worldserver = minecraftserver.worldServers[i];
/*    */         
/* 41 */         if (worldserver.disableLevelSaving)
/*    */         {
/* 43 */           worldserver.disableLevelSaving = false;
/* 44 */           flag = true;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 49 */     if (flag)
/*    */     {
/* 51 */       notifyOperators(sender, this, "commands.save.enabled", new Object[0]);
/*    */     }
/*    */     else
/*    */     {
/* 55 */       throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandSaveOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */