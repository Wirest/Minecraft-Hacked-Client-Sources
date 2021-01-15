/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import org.lwjgl.opengl.Display;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NameCommand
/*    */   implements Command
/*    */ {
/* 13 */   public static String name = "Polaris";
/*    */   
/*    */   public boolean run(String[] args)
/*    */   {
/* 17 */     if (args.length < 1) {
/* 18 */       return true;
/*    */     }
/* 20 */     if (args.length == 2) {
/* 21 */       name = args[1];
/* 22 */       Polaris.sendMessage("Name set to " + name);
/* 23 */       Display.setTitle(name + " " + Polaris.instance.version);
/*    */     }
/*    */     
/* 26 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 31 */     return "-name [name]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\NameCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */