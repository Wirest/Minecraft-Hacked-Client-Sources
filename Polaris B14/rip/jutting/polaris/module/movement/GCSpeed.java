/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class GCSpeed extends Module
/*    */ {
/*    */   public GCSpeed()
/*    */   {
/* 14 */     super("GCSpeed", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 19 */     Polaris.instance.settingsManager.rSetting(new Setting("Ground Speed", this, 1.0D, 0.0D, 5.0D, false));
/* 20 */     Polaris.instance.settingsManager.rSetting(new Setting("Air Speed", this, 1.0D, 0.0D, 5.0D, false));
/* 21 */     Polaris.instance.settingsManager.rSetting(new Setting("Motion Y", this, 0.4D, 0.0D, 2.0D, false));
/* 22 */     Polaris.instance.settingsManager.rSetting(new Setting("-Motion Y", this, 0.4D, 0.0D, 2.0D, false));
/* 23 */     Polaris.instance.settingsManager.rSetting(new Setting("Timer", this, 1.0D, 0.1D, 10.0D, false));
/* 24 */     Polaris.instance.settingsManager.rSetting(new Setting("YPort", this, false));
/* 25 */     Polaris.instance.settingsManager.rSetting(new Setting("Air Speeed", this, false));
/* 26 */     Polaris.instance.settingsManager.rSetting(new Setting("Strafe", this, true));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/* 31 */     if (mc.thePlayer.isMoving())
/*    */     {
/* 33 */       mc.timer.timerSpeed = ((float)Polaris.instance.settingsManager.getSettingByName("Timer").getValDouble());
/*    */       
/*    */ 
/* 36 */       if (mc.thePlayer.onGround) {
/* 37 */         mc.thePlayer.setSpeed(Polaris.instance.settingsManager.getSettingByName("Ground Speed").getValDouble());
/* 38 */         mc.thePlayer.motionY = Polaris.instance.settingsManager.getSettingByName("Motion Y").getValDouble();
/* 39 */         mc.timer.timerSpeed = ((float)Polaris.instance.settingsManager.getSettingByName("Timer").getValDouble());
/*    */ 
/*    */       }
/* 42 */       else if (mc.thePlayer.isAirBorne) {
/* 43 */         if (Polaris.instance.settingsManager.getSettingByName("Air Speeed").getValBoolean()) {
/* 44 */           mc.thePlayer.setSpeed(Polaris.instance.settingsManager.getSettingByName("Air Speed").getValDouble());
/*    */         }
/*    */         
/* 47 */         if (Polaris.instance.settingsManager.getSettingByName("Strafe").getValBoolean()) {
/* 48 */           mc.thePlayer.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
/*    */         }
/*    */         
/* 51 */         if (Polaris.instance.settingsManager.getSettingByName("YPort").getValBoolean()) {
/* 52 */           mc.thePlayer.motionY = (-Polaris.instance.settingsManager.getSettingByName("-Motion Y").getValDouble());
/*    */         }
/*    */       }
/*    */       else {
/* 56 */         mc.thePlayer.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
/* 57 */         if (Polaris.instance.settingsManager.getSettingByName("YPort").getValBoolean()) {
/* 58 */           mc.thePlayer.motionY = (-Polaris.instance.settingsManager.getSettingByName("-Motion Y").getValDouble());
/*    */         }
/*    */       }
/*    */     }
/*    */     else {
/* 63 */       mc.thePlayer.motionX = 0.0D;
/* 64 */       mc.thePlayer.motionZ = 0.0D;
/*    */     }
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 70 */     mc.timer.timerSpeed = 1.0F;
/* 71 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\GCSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */