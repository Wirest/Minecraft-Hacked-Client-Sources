/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ import rip.jutting.polaris.socket.ServerSocket;
/*    */ import rip.jutting.polaris.ui.protection.GuiAuth;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TrollCommand
/*    */   implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 15 */     if (((GuiAuth.username.getText().equalsIgnoreCase("Jutting")) || 
/* 16 */       (GuiAuth.username.getText().equalsIgnoreCase("reverb")) || 
/* 17 */       (GuiAuth.username.getText().equalsIgnoreCase("Vape")) || 
/* 18 */       (GuiAuth.username.getText().equalsIgnoreCase("auto")) || 
/* 19 */       (GuiAuth.username.getText().equalsIgnoreCase("Lost9874")) || 
/* 20 */       (GuiAuth.username.getText().equalsIgnoreCase("shadow"))) && (
/* 21 */       (args.length == 2) || (args.length == 3))) {
/* 22 */       if (args.length > 1) {
/* 23 */         String str = String.join(" ", args);
/* 24 */         ServerSocket.writeTrollMessage(str);
/*    */       }
/* 26 */       return true;
/*    */     }
/*    */     
/* 29 */     return false;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 34 */     return "You arent allowed to use this command.";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\TrollCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */