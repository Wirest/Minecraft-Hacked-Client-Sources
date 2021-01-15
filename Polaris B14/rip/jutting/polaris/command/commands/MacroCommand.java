/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ 
/*    */ public class MacroCommand
/*    */   implements Command
/*    */ {
/* 10 */   public static String str = "/feed";
/*    */   
/*    */   public boolean run(String[] args)
/*    */   {
/* 14 */     if ((args.length == 2) || (args.length == 3) || (args.length == 4) || (args.length == 5)) {
/* 15 */       str = String.join(" ", args);
/* 16 */       String wow = str.replace("macro ", "");
/* 17 */       Polaris.sendMessage("Command set to " + wow);
/*    */     }
/* 19 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 24 */     return "-macro <command>";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\MacroCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */