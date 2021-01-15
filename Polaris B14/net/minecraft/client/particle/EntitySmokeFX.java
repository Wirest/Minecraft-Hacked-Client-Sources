/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntitySmokeFX extends EntityFX
/*    */ {
/*    */   float smokeParticleScale;
/*    */   
/*    */   private EntitySmokeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46347_8_, double p_i46347_10_, double p_i46347_12_)
/*    */   {
/* 14 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46347_8_, p_i46347_10_, p_i46347_12_, 1.0F);
/*    */   }
/*    */   
/*    */   protected EntitySmokeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46348_8_, double p_i46348_10_, double p_i46348_12_, float p_i46348_14_)
/*    */   {
/* 19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 20 */     this.motionX *= 0.10000000149011612D;
/* 21 */     this.motionY *= 0.10000000149011612D;
/* 22 */     this.motionZ *= 0.10000000149011612D;
/* 23 */     this.motionX += p_i46348_8_;
/* 24 */     this.motionY += p_i46348_10_;
/* 25 */     this.motionZ += p_i46348_12_;
/* 26 */     this.particleRed = (this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D));
/* 27 */     this.particleScale *= 0.75F;
/* 28 */     this.particleScale *= p_i46348_14_;
/* 29 */     this.smokeParticleScale = this.particleScale;
/* 30 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
/* 31 */     this.particleMaxAge = ((int)(this.particleMaxAge * p_i46348_14_));
/* 32 */     this.noClip = false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 40 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/* 41 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 42 */     this.particleScale = (this.smokeParticleScale * f);
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
/* 60 */     setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
/* 61 */     this.motionY += 0.004D;
/* 62 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*    */     
/* 64 */     if (this.posY == this.prevPosY)
/*    */     {
/* 66 */       this.motionX *= 1.1D;
/* 67 */       this.motionZ *= 1.1D;
/*    */     }
/*    */     
/* 70 */     this.motionX *= 0.9599999785423279D;
/* 71 */     this.motionY *= 0.9599999785423279D;
/* 72 */     this.motionZ *= 0.9599999785423279D;
/*    */     
/* 74 */     if (this.onGround)
/*    */     {
/* 76 */       this.motionX *= 0.699999988079071D;
/* 77 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 85 */       return new EntitySmokeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, null);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntitySmokeFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */