/*    */ package rip.jutting.polaris.module.combat;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.util.FoodStats;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class Regen extends Module
/*    */ {
/*    */   public Regen()
/*    */   {
/* 17 */     super("Regen", 0, rip.jutting.polaris.module.Category.COMBAT);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 22 */     Polaris.instance.settingsManager.rSetting(new Setting("Guardian", this, false));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 27 */     if (Polaris.instance.settingsManager.getSettingByName("Guardian").getValBoolean()) {
/* 28 */       setDisplayName("Regen §7- Velt");
/*    */     } else {
/* 30 */       setDisplayName("Regen §7- Vanilla");
/*    */     }
/* 32 */     if ((mc.thePlayer.onGround) && (mc.thePlayer.getHealth() < 16.0D) && 
/* 33 */       (mc.thePlayer.getFoodStats().getFoodLevel() > 17) && (mc.thePlayer.isCollidedVertically)) {
/* 34 */       for (int i = 0; i < 60; i++) {
/* 35 */         mc.thePlayer.sendQueue.addToSendQueue(
/* 36 */           new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, 
/* 37 */           mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
/*    */       }
/* 39 */       if ((Polaris.instance.settingsManager.getSettingByName("Guardian").getValBoolean()) && 
/* 40 */         (mc.thePlayer.ticksExisted % 3 == 0)) {
/* 41 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/* 42 */           mc.thePlayer.posY - 2.0D, mc.thePlayer.posZ, false));
/*    */       }
/*    */       
/* 45 */       mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(
/* 46 */         net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\Regen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */