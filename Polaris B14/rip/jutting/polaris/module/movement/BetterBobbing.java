/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class BetterBobbing extends Module
/*    */ {
/*    */   public BetterBobbing()
/*    */   {
/* 12 */     super("BetterBobbing", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 17 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("Bobbing", this, 1.0D, 0.1D, 5.0D, false));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 22 */     setDisplayName("CoolBobbing");
/* 23 */     if ((mc.thePlayer.isMoving()) && 
/* 24 */       (mc.thePlayer.onGround)) {
/* 25 */       mc.thePlayer.cameraYaw = (0.090909086F * (float)Polaris.instance.settingsManager.getSettingByName("Bobbing").getValDouble());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onDisable()
/*    */   {
/* 32 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\BetterBobbing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */