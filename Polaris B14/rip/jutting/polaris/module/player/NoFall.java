/*    */ package rip.jutting.polaris.module.player;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class NoFall extends Module
/*    */ {
/*    */   public NoFall()
/*    */   {
/* 15 */     super("NoFall", 0, rip.jutting.polaris.module.Category.PLAYER);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 20 */     ArrayList<String> options = new ArrayList();
/* 21 */     options.add("Packet");
/* 22 */     options.add("Ground");
/* 23 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("NoFall Mode", this, "Packet", options));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 28 */     String mode = Polaris.instance.settingsManager.getSettingByName("NoFall Mode").getValString();
/* 29 */     if (mode.equalsIgnoreCase("Packet")) {
/* 30 */       setDisplayName("NoFall §7- Packet");
/* 31 */       if (mc.thePlayer.fallDistance > 3.0F) {
/* 32 */         mc.thePlayer.sendQueue.addToSendAuraQueue(new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, 
/* 33 */           mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
/* 34 */         mc.thePlayer.fallDistance = 0.0F;
/*    */       }
/*    */     }
/* 37 */     if (mode.equalsIgnoreCase("Ground")) {
/* 38 */       setDisplayName("NoFall §7- Ground");
/* 39 */       if (mc.thePlayer.fallDistance > 3.0F) {
/* 40 */         mc.thePlayer.sendQueue.addToSendAuraQueue(new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, 
/* 41 */           mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
/* 42 */         event.setGround(true);
/* 43 */         mc.thePlayer.fallDistance = 0.0F;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\player\NoFall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */