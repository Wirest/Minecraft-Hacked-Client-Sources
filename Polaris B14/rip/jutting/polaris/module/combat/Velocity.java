/*     */ package rip.jutting.polaris.module.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*     */ import net.minecraft.network.play.server.S27PacketExplosion;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.EventReceivePacket;
/*     */ import rip.jutting.polaris.event.events.EventSendPacket;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ 
/*     */ public class Velocity extends Module
/*     */ {
/*     */   public double percent;
/*     */   
/*     */   public Velocity()
/*     */   {
/*  23 */     super("Velocity", 0, rip.jutting.polaris.module.Category.COMBAT);
/*  24 */     this.percent = 0.0D;
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  29 */     ArrayList<String> options = new ArrayList();
/*  30 */     options.add("Packet");
/*  31 */     options.add("Cancel");
/*  32 */     options.add("Old");
/*  33 */     Polaris.instance.settingsManager.rSetting(new Setting("Velocity Mode", this, "Cancel", options));
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/*  38 */     String mode = Polaris.instance.settingsManager.getSettingByName("Velocity Mode").getValString();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPacketReceive(EventSendPacket event) {
/*  43 */     String mode = Polaris.instance.settingsManager.getSettingByName("Velocity Mode").getValString();
/*  44 */     if ((event.getPacket() instanceof S12PacketEntityVelocity)) {
/*  45 */       S12PacketEntityVelocity s27PacketExplosion = (S12PacketEntityVelocity)event.getPacket();
/*  46 */       if ((mode.equalsIgnoreCase("Cancel")) && (mc.theWorld.getEntityByID(s27PacketExplosion.getEntityID()) == mc.thePlayer)) {
/*  47 */         event.setCancelled(true);
/*     */       }
/*  49 */       if (((event.getPacket() instanceof S12PacketEntityVelocity)) || ((event.getPacket() instanceof S27PacketExplosion))) {
/*  50 */         event.setCancelled(true);
/*     */       }
/*  52 */       if ((mode.equalsIgnoreCase("Packet")) && (mc.theWorld.getEntityByID(s27PacketExplosion.getEntityID()) == mc.thePlayer)) {
/*  53 */         event.setCancelled(true);
/*     */       }
/*  55 */     } else if ((event.getPacket() instanceof S27PacketExplosion)) {
/*     */       S27PacketExplosion s27PacketExplosion1;
/*  57 */       S27PacketExplosion packet2 = s27PacketExplosion1 = (S27PacketExplosion)event.getPacket();
/*  58 */       if (mode.equalsIgnoreCase("Packet")) {
/*  59 */         S27PacketExplosion tmp164_163 = s27PacketExplosion1;tmp164_163.field_149152_f = ((float)(tmp164_163.field_149152_f * 0.0D)); S27PacketExplosion 
/*  60 */           tmp177_175 = packet2;tmp177_175.field_149153_g = ((float)(tmp177_175.field_149153_g * 0.0D)); S27PacketExplosion 
/*  61 */           tmp190_188 = packet2;tmp190_188.field_149159_h = ((float)(tmp190_188.field_149159_h * 0.0D));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPacketReceive(EventReceivePacket event) {
/*  68 */     String mode = Polaris.instance.settingsManager.getSettingByName("Velocity Mode").getValString();
/*  69 */     if ((event.getPacket() instanceof S12PacketEntityVelocity)) {
/*  70 */       S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
/*  71 */       if (mc.theWorld.getEntityByID(packet.getEntityID()) == mc.thePlayer) {
/*  72 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*  75 */     else if ((event.getPacket() instanceof S27PacketExplosion)) {
/*     */       S27PacketExplosion s27PacketExplosion;
/*  77 */       S27PacketExplosion packet2 = s27PacketExplosion = (S27PacketExplosion)event.getPacket();
/*  78 */       s27PacketExplosion.field_149152_f *= 0.0F;
/*  79 */       S27PacketExplosion s27PacketExplosion2 = packet2;
/*  80 */       s27PacketExplosion2.field_149153_g *= 0.0F;
/*  81 */       S27PacketExplosion s27PacketExplosion3 = packet2;
/*  82 */       s27PacketExplosion3.field_149159_h *= 0.0F;
/*     */     }
/*  84 */     if ((event.getPacket() instanceof S12PacketEntityVelocity)) {
/*  85 */       S12PacketEntityVelocity s27PacketExplosion = (S12PacketEntityVelocity)event.getPacket();
/*  86 */       if ((mode.equalsIgnoreCase("Cancel")) && (mc.theWorld.getEntityByID(s27PacketExplosion.getEntityID()) == mc.thePlayer)) {
/*  87 */         event.setCancelled(true);
/*     */       }
/*  89 */       if (((event.getPacket() instanceof S12PacketEntityVelocity)) || ((event.getPacket() instanceof S27PacketExplosion))) {
/*  90 */         event.setCancelled(true);
/*     */       }
/*  92 */       if ((mode.equalsIgnoreCase("Packet")) && (mc.theWorld.getEntityByID(s27PacketExplosion.getEntityID()) == mc.thePlayer)) {
/*  93 */         event.setCancelled(true);
/*     */       }
/*  95 */     } else if ((event.getPacket() instanceof S27PacketExplosion)) {
/*     */       S27PacketExplosion s27PacketExplosion1;
/*  97 */       S27PacketExplosion packet2 = s27PacketExplosion1 = (S27PacketExplosion)event.getPacket();
/*  98 */       if (mode.equalsIgnoreCase("Packet")) {
/*  99 */         S27PacketExplosion tmp273_272 = s27PacketExplosion1;tmp273_272.field_149152_f = ((float)(tmp273_272.field_149152_f * 0.0D)); S27PacketExplosion 
/* 100 */           tmp286_284 = packet2;tmp286_284.field_149153_g = ((float)(tmp286_284.field_149153_g * 0.0D)); S27PacketExplosion 
/* 101 */           tmp299_297 = packet2;tmp299_297.field_149159_h = ((float)(tmp299_297.field_149159_h * 0.0D));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onMotion(rip.jutting.polaris.event.events.EventPreMotionUpdate event) {
/* 108 */     String mode = Polaris.instance.settingsManager.getSettingByName("Velocity Mode").getValString();
/* 109 */     if ((mode.equalsIgnoreCase("Old")) && (
/* 110 */       (mc.thePlayer.hurtTime == 1) || (mc.thePlayer.hurtTime == 2) || (mc.thePlayer.hurtTime == 3) || (mc.thePlayer.hurtTime == 4) || (mc.thePlayer.hurtTime == 5) || (mc.thePlayer.hurtTime == 6) || (mc.thePlayer.hurtTime == 7) || (mc.thePlayer.hurtTime == 8))) {
/* 111 */       mc.thePlayer.motionX = 0.05D;
/* 112 */       mc.thePlayer.motionY = 0.05D;
/* 113 */       mc.thePlayer.motionZ = 0.05D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\Velocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */