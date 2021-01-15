/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.module.ModuleManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BindCommand
/*    */   implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 16 */     if (args.length == 3)
/*    */     {
/* 18 */       Module m = Polaris.instance.moduleManager.getModuleByName(args[1]);
/*    */       
/* 20 */       if (m == null) {
/* 21 */         return false;
/*    */       }
/* 23 */       m.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
/* 24 */       Polaris.sendMessage(m.getName() + " has been bound to " + args[2] + ".");
/* 25 */       ModuleManager.save();
/* 26 */       return true;
/*    */     }
/* 28 */     return false;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 33 */     return "-bind [module] [key]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\BindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */