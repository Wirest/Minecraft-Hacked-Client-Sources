/*    */ package rip.jutting.polaris.module.combat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*    */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventSendPacket;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class Criticals extends Module
/*    */ {
/*    */   public Criticals()
/*    */   {
/* 19 */     super("Criticals", 0, rip.jutting.polaris.module.Category.COMBAT);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 24 */     ArrayList<String> options = new ArrayList();
/* 25 */     options.add("Packet");
/* 26 */     options.add("MiniJump");
/* 27 */     options.add("Hypixel");
/* 28 */     Polaris.instance.settingsManager.rSetting(new Setting("Criticals Mode", this, "Packet", options));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/* 33 */     String mode = Polaris.instance.settingsManager.getSettingByName("Criticals Mode").getValString();
/* 34 */     if (mode.equalsIgnoreCase("Packet")) {
/* 35 */       setDisplayName("Criticals §7- Packet");
/*    */     }
/* 37 */     if (mode.equalsIgnoreCase("MiniJump")) {
/* 38 */       setDisplayName("Criticals §7- MiniJump");
/*    */     }
/* 40 */     if (mode.equalsIgnoreCase("Hypixel")) {
/* 41 */       setDisplayName("Criticals §7- Hypixel");
/*    */     }
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 47 */     String mode = Polaris.instance.settingsManager.getSettingByName("Criticals Mode").getValString();
/* 48 */     if ((event.getPacket() instanceof C02PacketUseEntity)) {
/* 49 */       if (mode.equalsIgnoreCase("MiniJump")) {
/* 50 */         if (mc.thePlayer.onGround) {
/* 51 */           mc.thePlayer.motionY = 0.1200000119209288D;
/*    */         } else {
/* 53 */           mc.thePlayer.motionY = -0.5D;
/*    */         }
/*    */       }
/* 56 */       C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
/* 57 */       if ((packet.getAction() == net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK) && 
/* 58 */         (mc.thePlayer.onGround)) {
/* 59 */         if (mode.equalsIgnoreCase("Packet")) {
/* 60 */           System.out.println("xd");
/* 61 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 62 */             mc.thePlayer.posX, mc.thePlayer.posY + 0.1625D, mc.thePlayer.posZ, false));
/* 63 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 64 */             mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
/* 65 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 66 */             mc.thePlayer.posX, mc.thePlayer.posY + 4.0E-6D, mc.thePlayer.posZ, false));
/* 67 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 68 */             mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
/* 69 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 70 */             mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-6D, mc.thePlayer.posZ, false));
/* 71 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 72 */             mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
/* 73 */           mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer());
/*    */         }
/* 75 */         if (mode.equalsIgnoreCase("Hypixel")) {
/* 76 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 77 */             mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
/* 78 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 79 */             mc.thePlayer.posX, mc.thePlayer.posY + rip.jutting.polaris.utils.AutoUtils.setRandom(0.027D, 0.033D), 
/* 80 */             mc.thePlayer.posZ, false));
/* 81 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 82 */             mc.thePlayer.posX, mc.thePlayer.posY + rip.jutting.polaris.utils.AutoUtils.setRandom(0.045000000000000005D, 0.055D), 
/* 83 */             mc.thePlayer.posZ, false));
/* 84 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 85 */             mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean canCrit()
/*    */   {
/* 93 */     return (!rip.jutting.polaris.utils.PlayerUtils.isInLiquid()) && (mc.thePlayer.onGround);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\Criticals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */