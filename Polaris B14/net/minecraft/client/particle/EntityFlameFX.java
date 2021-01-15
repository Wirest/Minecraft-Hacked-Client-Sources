/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityFlameFX extends EntityFX
/*    */ {
/*    */   private float flameScale;
/*    */   
/*    */   protected EntityFlameFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*    */   {
/* 15 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 16 */     this.motionX = (this.motionX * 0.009999999776482582D + xSpeedIn);
/* 17 */     this.motionY = (this.motionY * 0.009999999776482582D + ySpeedIn);
/* 18 */     this.motionZ = (this.motionZ * 0.009999999776482582D + zSpeedIn);
/* 19 */     this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
/* 20 */     this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
/* 21 */     this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
/* 22 */     this.flameScale = this.particleScale;
/* 23 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);
/* 24 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4);
/* 25 */     this.noClip = true;
/* 26 */     setParticleTextureIndex(48);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 34 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 35 */     this.particleScale = (this.flameScale * (1.0F - f * f * 0.5F));
/* 36 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*    */   }
/*    */   
/*    */   public int getBrightnessForRender(float partialTicks)
/*    */   {
/* 41 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 42 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 43 */     int i = super.getBrightnessForRender(partialTicks);
/* 44 */     int j = i & 0xFF;
/* 45 */     int k = i >> 16 & 0xFF;
/* 46 */     j += (int)(f * 15.0F * 16.0F);
/*    */     
/* 48 */     if (j > 240)
/*    */     {
/* 50 */       j = 240;
/*    */     }
/*    */     
/* 53 */     return j | k << 16;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public float getBrightness(float partialTicks)
/*    */   {
/* 61 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 62 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 63 */     float f1 = super.getBrightness(partialTicks);
/* 64 */     return f1 * f + (1.0F - f);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 72 */     this.prevPosX = this.posX;
/* 73 */     this.prevPosY = this.posY;
/* 74 */     this.prevPosZ = this.posZ;
/*    */     
/* 76 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 78 */       setDead();
/*    */     }
/*    */     
/* 81 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 82 */     this.motionX *= 0.9599999785423279D;
/* 83 */     this.motionY *= 0.9599999785423279D;
/* 84 */     this.motionZ *= 0.9599999785423279D;
/*    */     
/* 86 */     if (this.onGround)
/*    */     {
/* 88 */       this.motionX *= 0.699999988079071D;
/* 89 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 97 */       return new EntityFlameFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityFlameFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */