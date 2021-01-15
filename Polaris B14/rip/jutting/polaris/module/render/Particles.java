/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class Particles extends Module
/*    */ {
/*    */   public Particles()
/*    */   {
/* 17 */     super("Particles", 0, rip.jutting.polaris.module.Category.RENDER);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 22 */     ArrayList<String> options = new ArrayList();
/* 23 */     options.add("Cloud");
/* 24 */     options.add("Flame");
/* 25 */     options.add("Smoke");
/* 26 */     options.add("Hearts");
/* 27 */     options.add("Redstone");
/* 28 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("Particle Modes", this, "Hearts", options));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 33 */     String mode = Polaris.instance.settingsManager.getSettingByName("Particle Modes").getValString();
/* 34 */     if (mode.equalsIgnoreCase("Hearts")) {
/* 35 */       mc.theWorld.spawnParticle(EnumParticleTypes.HEART, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*    */     }
/* 37 */     if (mode.equalsIgnoreCase("Cloud")) {
/* 38 */       mc.theWorld.spawnParticle(EnumParticleTypes.CLOUD, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*    */     }
/* 40 */     if (mode.equalsIgnoreCase("Redstone")) {
/* 41 */       mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.1D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 42 */       mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 43 */       mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.3D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 44 */       mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.4D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 45 */       mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.5D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*    */     }
/* 47 */     if (mode.equalsIgnoreCase("Flame")) {
/* 48 */       mc.theWorld.spawnParticle(EnumParticleTypes.FLAME, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*    */     }
/* 50 */     if (mode.equalsIgnoreCase("Smoke")) {
/* 51 */       mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_LARGE, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Particles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */