/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.Timer;
/*    */ import rip.jutting.polaris.event.EventTarget;
/*    */ import rip.jutting.polaris.event.events.Event3D;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class Tracers extends Module
/*    */ {
/*    */   private float oldBrightness;
/*    */   
/*    */   public Tracers()
/*    */   {
/* 20 */     super("Tracers", 0, Category.RENDER);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender(Event3D event) {
/* 25 */     Iterator var2 = mc.theWorld.loadedEntityList.iterator();
/*    */     
/* 27 */     while (var2.hasNext()) {
/* 28 */       Object theObject = var2.next();
/* 29 */       if ((theObject instanceof EntityLivingBase)) {
/* 30 */         EntityLivingBase entity = (EntityLivingBase)theObject;
/* 31 */         if ((entity instanceof EntityPlayer)) {
/* 32 */           Minecraft var10000 = mc;
/* 33 */           Minecraft var10001 = mc;
/* 34 */           if (entity != mc.thePlayer) {
/* 35 */             player(entity);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void player(EntityLivingBase entity) {
/* 43 */     float red = 1.0F;
/* 44 */     float green = 1.0F;
/* 45 */     float blue = 1.0F;
/* 46 */     double var10001 = entity.posX - entity.lastTickPosX;
/* 47 */     Timer var10002 = mc.timer;
/* 48 */     double var10000 = entity.lastTickPosX + var10001 * mc.timer.renderPartialTicks;
/* 49 */     mc.getRenderManager();
/* 50 */     double xPos = var10000 - RenderManager.renderPosX;
/* 51 */     var10001 = entity.posY - entity.lastTickPosY;
/* 52 */     var10002 = mc.timer;
/* 53 */     var10000 = entity.lastTickPosY + var10001 * mc.timer.renderPartialTicks;
/* 54 */     mc.getRenderManager();
/* 55 */     double yPos = var10000 - RenderManager.renderPosY;
/* 56 */     var10001 = entity.posZ - entity.lastTickPosZ;
/* 57 */     var10002 = mc.timer;
/* 58 */     var10000 = entity.lastTickPosZ + var10001 * mc.timer.renderPartialTicks;
/* 59 */     mc.getRenderManager();
/* 60 */     double zPos = var10000 - RenderManager.renderPosZ;
/* 61 */     render(red, green, blue, xPos, yPos, zPos);
/*    */   }
/*    */   
/*    */   public void render(float red, float green, float blue, double x, double y, double z) {
/* 65 */     rip.jutting.polaris.utils.RenderUtils.R3DUtils.drawTracerLine1(x, y, z, 255.0F, 100.0F, 255.0F, 12.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Tracers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */