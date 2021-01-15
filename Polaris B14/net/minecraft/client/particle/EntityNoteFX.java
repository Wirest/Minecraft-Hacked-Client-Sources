/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityNoteFX extends EntityFX
/*    */ {
/*    */   float noteParticleScale;
/*    */   
/*    */   protected EntityNoteFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46353_8_, double p_i46353_10_, double p_i46353_12_)
/*    */   {
/* 14 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46353_8_, p_i46353_10_, p_i46353_12_, 2.0F);
/*    */   }
/*    */   
/*    */   protected EntityNoteFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1217_8_, double p_i1217_10_, double p_i1217_12_, float p_i1217_14_)
/*    */   {
/* 19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 20 */     this.motionX *= 0.009999999776482582D;
/* 21 */     this.motionY *= 0.009999999776482582D;
/* 22 */     this.motionZ *= 0.009999999776482582D;
/* 23 */     this.motionY += 0.2D;
/* 24 */     this.particleRed = (MathHelper.sin(((float)p_i1217_8_ + 0.0F) * 3.1415927F * 2.0F) * 0.65F + 0.35F);
/* 25 */     this.particleGreen = (MathHelper.sin(((float)p_i1217_8_ + 0.33333334F) * 3.1415927F * 2.0F) * 0.65F + 0.35F);
/* 26 */     this.particleBlue = (MathHelper.sin(((float)p_i1217_8_ + 0.6666667F) * 3.1415927F * 2.0F) * 0.65F + 0.35F);
/* 27 */     this.particleScale *= 0.75F;
/* 28 */     this.particleScale *= p_i1217_14_;
/* 29 */     this.noteParticleScale = this.particleScale;
/* 30 */     this.particleMaxAge = 6;
/* 31 */     this.noClip = false;
/* 32 */     setParticleTextureIndex(64);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 40 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/* 41 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 42 */     this.particleScale = (this.noteParticleScale * f);
/* 43 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 51 */     this.prevPosX = this.posX;
/* 52 */     this.prevPosY = this.posY;
/* 53 */     this.prevPosZ = this.posZ;
/*    */     
/* 55 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 57 */       setDead();
/*    */     }
/*    */     
/* 60 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*    */     
/* 62 */     if (this.posY == this.prevPosY)
/*    */     {
/* 64 */       this.motionX *= 1.1D;
/* 65 */       this.motionZ *= 1.1D;
/*    */     }
/*    */     
/* 68 */     this.motionX *= 0.6600000262260437D;
/* 69 */     this.motionY *= 0.6600000262260437D;
/* 70 */     this.motionZ *= 0.6600000262260437D;
/*    */     
/* 72 */     if (this.onGround)
/*    */     {
/* 74 */       this.motionX *= 0.699999988079071D;
/* 75 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 83 */       return new EntityNoteFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityNoteFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */