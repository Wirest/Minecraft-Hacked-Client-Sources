/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.newconfig.GuiAltManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Config
/*    */   extends Module
/*    */ {
/*    */   public Config()
/*    */   {
/* 17 */     super("Config", 0, Category.OTHER);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 22 */     super.onEnable();
/* 23 */     mc.displayGuiScreen(new GuiAltManager());
/* 24 */     toggle();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */