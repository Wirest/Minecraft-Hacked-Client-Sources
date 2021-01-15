/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class Scoreboard extends rip.jutting.polaris.module.Module
/*    */ {
/*    */   public Scoreboard()
/*    */   {
/* 11 */     super("Scoreboard", 0, rip.jutting.polaris.module.Category.RENDER);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 16 */     Polaris.instance.settingsManager.rSetting(new Setting("Disable", this, false));
/* 17 */     Polaris.instance.settingsManager.rSetting(new Setting("Numbers", this, false));
/* 18 */     Polaris.instance.settingsManager.rSetting(new Setting("SB Font", this, true));
/* 19 */     Polaris.instance.settingsManager.rSetting(new Setting("Height", this, 80.0D, 0.0D, 150.0D, true));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Scoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */