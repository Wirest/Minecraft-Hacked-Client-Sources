/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityReddustFX extends EntityFX
/*    */ {
/*    */   float reddustParticleScale;
/*    */   
/*    */   protected EntityReddustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_)
/*    */   {
/* 14 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1.0F, p_i46349_8_, p_i46349_9_, p_i46349_10_);
/*    */   }
/*    */   
/*    */   protected EntityReddustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46350_8_, float p_i46350_9_, float p_i46350_10_, float p_i46350_11_)
/*    */   {
/* 19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 20 */     this.motionX *= 0.10000000149011612D;
/* 21 */     this.motionY *= 0.10000000149011612D;
/* 22 */     this.motionZ *= 0.10000000149011612D;
/*    */     
/* 24 */     if (p_i46350_9_ == 0.0F)
/*    */     {
/* 26 */       p_i46350_9_ = 1.0F;
/*    */     }
/*    */     
/* 29 */     float f = (float)Math.random() * 0.4F + 0.6F;
/* 30 */     this.particleRed = (((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_9_ * f);
/* 31 */     this.particleGreen = (((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_10_ * f);
/* 32 */     this.particleBlue = (((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_11_ * f);
/* 33 */     this.particleScale *= 0.75F;
/* 34 */     this.particleScale *= p_i46350_8_;
/* 35 */     this.reddustParticleScale = this.particleScale;
/* 36 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
/* 37 */     this.particleMaxAge = ((int)(this.particleMaxAge * p_i46350_8_));
/* 38 */     this.noClip = false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 46 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/* 47 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 48 */     this.particleScale = (this.reddustParticleScale * f);
/* 49 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 57 */     this.prevPosX = this.posX;
/* 58 */     this.prevPosY = this.posY;
/* 59 */     this.prevPosZ = this.posZ;
/*    */     
/* 61 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 63 */       setDead();
/*    */     }
/*    */     
/* 66 */     setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
/* 67 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*    */     
/* 69 */     if (this.posY == this.prevPosY)
/*    */     {
/* 71 */       this.motionX *= 1.1D;
/* 72 */       this.motionZ *= 1.1D;
/*    */     }
/*    */     
/* 75 */     this.motionX *= 0.9599999785423279D;
/* 76 */     this.motionY *= 0.9599999785423279D;
/* 77 */     this.motionZ *= 0.9599999785423279D;
/*    */     
/* 79 */     if (this.onGround)
/*    */     {
/* 81 */       this.motionX *= 0.699999988079071D;
/* 82 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 90 */       return new EntityReddustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, (float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityReddustFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */