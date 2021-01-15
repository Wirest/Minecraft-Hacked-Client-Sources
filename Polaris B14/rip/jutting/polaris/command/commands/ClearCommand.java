/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ import rip.jutting.polaris.module.other.Server;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClearCommand
/*    */   implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 17 */     if (args.length < 1) {
/* 18 */       return true;
/*    */     }
/* 20 */     if (args.length == 1) {
/* 21 */       Server.clear();
/* 22 */       Polaris.sendMessage("Cleared all IRC messages.");
/*    */     }
/*    */     
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 30 */     return "-clear";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\ClearCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */