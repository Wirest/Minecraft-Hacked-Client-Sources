/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityGuardian;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class MobAppearance extends EntityFX
/*    */ {
/*    */   private EntityLivingBase entity;
/*    */   
/*    */   protected MobAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
/*    */   {
/* 20 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 21 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);
/* 22 */     this.motionX = (this.motionY = this.motionZ = 0.0D);
/* 23 */     this.particleGravity = 0.0F;
/* 24 */     this.particleMaxAge = 30;
/*    */   }
/*    */   
/*    */   public int getFXLayer()
/*    */   {
/* 29 */     return 3;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 37 */     super.onUpdate();
/*    */     
/* 39 */     if (this.entity == null)
/*    */     {
/* 41 */       EntityGuardian entityguardian = new EntityGuardian(this.worldObj);
/* 42 */       entityguardian.setElder();
/* 43 */       this.entity = entityguardian;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 52 */     if (this.entity != null)
/*    */     {
/* 54 */       RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
/* 55 */       rendermanager.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
/* 56 */       float f = 0.42553192F;
/* 57 */       float f1 = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 58 */       GlStateManager.depthMask(true);
/* 59 */       GlStateManager.enableBlend();
/* 60 */       GlStateManager.enableDepth();
/* 61 */       GlStateManager.blendFunc(770, 771);
/* 62 */       float f2 = 240.0F;
/* 63 */       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f2);
/* 64 */       GlStateManager.pushMatrix();
/* 65 */       float f3 = 0.05F + 0.5F * MathHelper.sin(f1 * 3.1415927F);
/* 66 */       GlStateManager.color(1.0F, 1.0F, 1.0F, f3);
/* 67 */       GlStateManager.translate(0.0F, 1.8F, 0.0F);
/* 68 */       GlStateManager.rotate(180.0F - entityIn.rotationYaw, 0.0F, 1.0F, 0.0F);
/* 69 */       GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 1.0F, 0.0F, 0.0F);
/* 70 */       GlStateManager.translate(0.0F, -0.4F, -1.5F);
/* 71 */       GlStateManager.scale(f, f, f);
/* 72 */       this.entity.rotationYaw = (this.entity.prevRotationYaw = 0.0F);
/* 73 */       this.entity.rotationYawHead = (this.entity.prevRotationYawHead = 0.0F);
/* 74 */       rendermanager.renderEntityWithPosYaw(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
/* 75 */       GlStateManager.popMatrix();
/* 76 */       GlStateManager.enableDepth();
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 84 */       return new MobAppearance(worldIn, xCoordIn, yCoordIn, zCoordIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\MobAppearance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */