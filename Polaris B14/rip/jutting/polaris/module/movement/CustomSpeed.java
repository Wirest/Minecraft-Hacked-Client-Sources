/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.util.MovementInput;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class CustomSpeed extends Module
/*    */ {
/*    */   public CustomSpeed()
/*    */   {
/* 15 */     super("CustomSpeed", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 20 */     Polaris.instance.settingsManager.rSetting(new Setting("Motion Y", this, 0.4D, 0.0D, 2.0D, false));
/* 21 */     Polaris.instance.settingsManager.rSetting(new Setting("Down Multiplier", this, 1.0D, 0.0D, 3.0D, false));
/* 22 */     Polaris.instance.settingsManager.rSetting(new Setting("Air Speed", this, 0.4D, 0.0D, 2.0D, false));
/* 23 */     Polaris.instance.settingsManager.rSetting(new Setting("Friction", this, 0.4D, 0.0D, 3.0D, false));
/* 24 */     Polaris.instance.settingsManager.rSetting(new Setting("Air Friction", this, 0.1D, 0.1D, 3.0D, false));
/* 25 */     Polaris.instance.settingsManager.rSetting(new Setting("Ground Speed", this, 0.1D, 0.1D, 3.0D, false));
/* 26 */     Polaris.instance.settingsManager.rSetting(new Setting("Timer Speed", this, 0.1D, 0.1D, 3.0D, false));
/* 27 */     Polaris.instance.settingsManager.rSetting(new Setting("Strafe", this, true));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/* 32 */     float tspeed = Float.parseFloat(String.valueOf(Polaris.instance.settingsManager.getSettingByName("Tim3r Sp33d").getValDouble()));
/* 33 */     mc.timer.timerSpeed = tspeed;
/*    */     
/* 35 */     Minecraft.getMinecraft();
/* 36 */     if (mc.thePlayer.isMoving()) {
/* 37 */       if (Minecraft.getMinecraft().thePlayer.onGround) {
/* 38 */         mc.thePlayer.setSpeed(Polaris.instance.settingsManager.getSettingByName("Ground Sp33d").getValDouble());
/*    */         
/* 40 */         Minecraft.getMinecraft();
/* 41 */         MovementInput var3 = Minecraft.getMinecraft().thePlayer.movementInput;
/* 42 */         if (MovementInput.moveForward == 0.0D) {
/* 43 */           Minecraft.getMinecraft();
/* 44 */           var3 = Minecraft.getMinecraft().thePlayer.movementInput;
/* 45 */           if (MovementInput.moveStrafe == 0.0D) {}
/*    */ 
/*    */         }
/*    */         else
/*    */         {
/* 50 */           Minecraft.getMinecraft().thePlayer.motionY = Polaris.instance.settingsManager.getSettingByName("M0tion Y").getValDouble();
/*    */         }
/*    */       }
/* 53 */       else if (mc.thePlayer.isAirBorne) {
/* 54 */         mc.thePlayer.setSpeed(Polaris.instance.settingsManager.getSettingByName("Air Sp33d").getValDouble());
/*    */       }
/*    */     }
/*    */     
/* 58 */     if (Polaris.instance.settingsManager.getSettingByName("Str4fe").getValBoolean()) {
/* 59 */       mc.thePlayer.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
/*    */     }
/*    */     
/* 62 */     Minecraft.getMinecraft();
/* 63 */     if (Minecraft.getMinecraft().thePlayer.motionY <= 0.0D) {
/* 64 */       Minecraft.getMinecraft().thePlayer.motionY *= Polaris.instance.settingsManager.getSettingByName("Down Multiplier").getValDouble();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onDisable()
/*    */   {
/* 71 */     mc.timer.timerSpeed = 1.0F;
/* 72 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\CustomSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */