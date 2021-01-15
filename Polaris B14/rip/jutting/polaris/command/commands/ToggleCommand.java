/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.module.ModuleManager;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class ToggleCommand
/*    */   implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 14 */     if (args.length == 2)
/*    */     {
/* 16 */       Module module = Polaris.instance.moduleManager.getModuleByName(args[1]);
/*    */       
/* 18 */       if (module == null) {
/* 19 */         Polaris.sendMessage("The module with the name " + args[1] + " does not exist.");
/* 20 */         return true;
/*    */       }
/*    */       
/* 23 */       module.toggle();
/* 24 */       ModuleManager.save();
/* 25 */       SettingsManager.save();
/*    */       
/* 27 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 31 */     return false;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 36 */     return "-toggle [module]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\ToggleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */