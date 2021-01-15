/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ public class SpammerCommand
/*    */   implements Command
/*    */ {
/*  9 */   public static String msg = "Polaris = Best client in da game!";
/*    */   
/*    */   public boolean run(String[] args)
/*    */   {
/* 13 */     if (args.length > 1) {
/* 14 */       String str = String.join(" ", args);
/* 15 */       String xd = str.replace("spam", "");
/* 16 */       msg = xd;
/* 17 */       Polaris.sendMessage("Spam message set to " + msg);
/* 18 */       return true;
/*    */     }
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 25 */     return "-spam <message>";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\SpammerCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */