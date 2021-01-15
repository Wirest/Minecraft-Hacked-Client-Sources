/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityPortalFX extends EntityFX
/*     */ {
/*     */   private float portalParticleScale;
/*     */   private double portalPosX;
/*     */   private double portalPosY;
/*     */   private double portalPosZ;
/*     */   
/*     */   protected EntityPortalFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*     */   {
/*  16 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*  17 */     this.motionX = xSpeedIn;
/*  18 */     this.motionY = ySpeedIn;
/*  19 */     this.motionZ = zSpeedIn;
/*  20 */     this.portalPosX = (this.posX = xCoordIn);
/*  21 */     this.portalPosY = (this.posY = yCoordIn);
/*  22 */     this.portalPosZ = (this.posZ = zCoordIn);
/*  23 */     float f = this.rand.nextFloat() * 0.6F + 0.4F;
/*  24 */     this.portalParticleScale = (this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F);
/*  25 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F * f);
/*  26 */     this.particleGreen *= 0.3F;
/*  27 */     this.particleRed *= 0.9F;
/*  28 */     this.particleMaxAge = ((int)(Math.random() * 10.0D) + 40);
/*  29 */     this.noClip = true;
/*  30 */     setParticleTextureIndex((int)(Math.random() * 8.0D));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderParticle(WorldRenderer worldRendererIn, net.minecraft.entity.Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*     */   {
/*  38 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/*  39 */     f = 1.0F - f;
/*  40 */     f *= f;
/*  41 */     f = 1.0F - f;
/*  42 */     this.particleScale = (this.portalParticleScale * f);
/*  43 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/*  48 */     int i = super.getBrightnessForRender(partialTicks);
/*  49 */     float f = this.particleAge / this.particleMaxAge;
/*  50 */     f *= f;
/*  51 */     f *= f;
/*  52 */     int j = i & 0xFF;
/*  53 */     int k = i >> 16 & 0xFF;
/*  54 */     k += (int)(f * 15.0F * 16.0F);
/*     */     
/*  56 */     if (k > 240)
/*     */     {
/*  58 */       k = 240;
/*     */     }
/*     */     
/*  61 */     return j | k << 16;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBrightness(float partialTicks)
/*     */   {
/*  69 */     float f = super.getBrightness(partialTicks);
/*  70 */     float f1 = this.particleAge / this.particleMaxAge;
/*  71 */     f1 = f1 * f1 * f1 * f1;
/*  72 */     return f * (1.0F - f1) + f1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  80 */     this.prevPosX = this.posX;
/*  81 */     this.prevPosY = this.posY;
/*  82 */     this.prevPosZ = this.posZ;
/*  83 */     float f = this.particleAge / this.particleMaxAge;
/*  84 */     f = -f + f * f * 2.0F;
/*  85 */     f = 1.0F - f;
/*  86 */     this.posX = (this.portalPosX + this.motionX * f);
/*  87 */     this.posY = (this.portalPosY + this.motionY * f + (1.0F - f));
/*  88 */     this.posZ = (this.portalPosZ + this.motionZ * f);
/*     */     
/*  90 */     if (this.particleAge++ >= this.particleMaxAge)
/*     */     {
/*  92 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Factory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 100 */       return new EntityPortalFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityPortalFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */