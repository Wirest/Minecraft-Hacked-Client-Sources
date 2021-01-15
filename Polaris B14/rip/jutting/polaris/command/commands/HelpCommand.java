/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ public class HelpCommand implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 11 */     Polaris.sendMessage("Here are the list of commands:");
/* 12 */     for (Command c : Polaris.instance.cmdManager.getCommands().values()) {
/* 13 */       Polaris.sendMessage(c.usage());
/*    */     }
/* 15 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 20 */     return "-help";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\HelpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */