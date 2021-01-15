/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Color
/*    */   extends Module
/*    */ {
/*    */   public Color()
/*    */   {
/* 18 */     super("Color", 0, Category.RENDER);
/*    */   }
/*    */   
/*    */   public void setup() {
/* 22 */     Polaris.instance.settingsManager.rSetting(new Setting("Red", this, 174.0D, 0.0D, 255.0D, true));
/* 23 */     Polaris.instance.settingsManager.rSetting(new Setting("Green", this, 91.0D, 0.0D, 255.0D, true));
/* 24 */     Polaris.instance.settingsManager.rSetting(new Setting("Blue", this, 219.0D, 0.0D, 255.0D, true));
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 29 */     super.onEnable();
/* 30 */     toggle();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Color.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */