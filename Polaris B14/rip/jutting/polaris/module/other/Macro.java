/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.command.commands.MacroCommand;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class Macro extends Module
/*    */ {
/*    */   public Macro()
/*    */   {
/* 12 */     super("Macro", 0, Category.OTHER);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 17 */     super.onEnable();
/* 18 */     mc.thePlayer.sendChatMessage(MacroCommand.str);
/* 19 */     toggle();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\Macro.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */