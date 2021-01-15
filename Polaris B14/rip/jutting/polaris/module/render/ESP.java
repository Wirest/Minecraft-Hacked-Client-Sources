/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.EventTarget;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ import rip.jutting.polaris.utils.RenderUtils;
/*    */ 
/*    */ public class ESP extends Module
/*    */ {
/*    */   public ESP()
/*    */   {
/* 21 */     super("ESP", 0, rip.jutting.polaris.module.Category.RENDER);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 26 */     ArrayList<String> options = new ArrayList();
/* 27 */     options.add("Outline");
/* 28 */     options.add("Square");
/* 29 */     Polaris.instance.settingsManager.rSetting(new Setting("ESP Mode", this, "Outline", options));
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 34 */     String mode = Polaris.instance.settingsManager.getSettingByName("ESP Mode").getValString();
/* 35 */     if (mode.equalsIgnoreCase("Outline")) {
/* 36 */       setDisplayName("ESP §7- Outline");
/*    */     }
/* 38 */     if (mode.equalsIgnoreCase("Square")) {
/* 39 */       setDisplayName("ESP §7- Square");
/*    */     }
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender(rip.jutting.polaris.event.events.Event3D event) {
/* 45 */     String mode = Polaris.instance.settingsManager.getSettingByName("ESP Mode").getValString();
/* 46 */     if (mode.equalsIgnoreCase("Square")) {
/* 47 */       for (Object entity : mc.theWorld.loadedEntityList) {
/* 48 */         if ((entity instanceof EntityPlayer)) {
/* 49 */           EntityLivingBase p = (EntityPlayer)entity;
/* 50 */           if ((p.isEntityAlive()) && (p != mc.thePlayer) && (RenderUtils.isInFrustumView(p))) {
/* 51 */             double x = RenderUtils.interpolate(p.posX, p.lastTickPosX);
/* 52 */             double y = RenderUtils.interpolate(p.posY, p.lastTickPosY);
/* 53 */             double z = RenderUtils.interpolate(p.posZ, p.lastTickPosZ);
/* 54 */             int color = p.hurtTime >= 1 ? new Color(255, 20, 20).getRGB() : 
/* 55 */               new Color(
/* 56 */               (int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 57 */               (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 58 */               (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 59 */               .getRGB();
/* 60 */             rip.jutting.polaris.utils.RenderUtils.R2DUtils.draw2DCorner(p, p.posX - RenderManager.renderPosX, 
/* 61 */               p.posY - RenderManager.renderPosY, p.posZ - RenderManager.renderPosZ, color);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\ESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */