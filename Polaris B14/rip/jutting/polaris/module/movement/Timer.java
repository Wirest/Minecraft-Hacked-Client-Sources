/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class Timer extends Module
/*    */ {
/*    */   public Timer()
/*    */   {
/* 14 */     super("Timer", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 19 */     Polaris.instance.settingsManager.rSetting(new Setting("Timer Speed", this, 1.0D, 0.1D, 10.0D, false));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 24 */     mc.timer.timerSpeed = ((float)Polaris.instance.settingsManager.getSettingByName("Timer Speed").getValDouble());
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 29 */     mc.timer.timerSpeed = 1.0F;
/* 30 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */