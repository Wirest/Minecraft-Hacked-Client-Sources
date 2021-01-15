/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityCrit2FX extends EntityFX
/*    */ {
/*    */   float field_174839_a;
/*    */   
/*    */   protected EntityCrit2FX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46284_8_, double p_i46284_10_, double p_i46284_12_)
/*    */   {
/* 14 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46284_8_, p_i46284_10_, p_i46284_12_, 1.0F);
/*    */   }
/*    */   
/*    */   protected EntityCrit2FX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46285_8_, double p_i46285_10_, double p_i46285_12_, float p_i46285_14_)
/*    */   {
/* 19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 20 */     this.motionX *= 0.10000000149011612D;
/* 21 */     this.motionY *= 0.10000000149011612D;
/* 22 */     this.motionZ *= 0.10000000149011612D;
/* 23 */     this.motionX += p_i46285_8_ * 0.4D;
/* 24 */     this.motionY += p_i46285_10_ * 0.4D;
/* 25 */     this.motionZ += p_i46285_12_ * 0.4D;
/* 26 */     this.particleRed = (this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D + 0.6000000238418579D));
/* 27 */     this.particleScale *= 0.75F;
/* 28 */     this.particleScale *= p_i46285_14_;
/* 29 */     this.field_174839_a = this.particleScale;
/* 30 */     this.particleMaxAge = ((int)(6.0D / (Math.random() * 0.8D + 0.6D)));
/* 31 */     this.particleMaxAge = ((int)(this.particleMaxAge * p_i46285_14_));
/* 32 */     this.noClip = false;
/* 33 */     setParticleTextureIndex(65);
/* 34 */     onUpdate();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 42 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/* 43 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 44 */     this.particleScale = (this.field_174839_a * f);
/* 45 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 53 */     this.prevPosX = this.posX;
/* 54 */     this.prevPosY = this.posY;
/* 55 */     this.prevPosZ = this.posZ;
/*    */     
/* 57 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 59 */       setDead();
/*    */     }
/*    */     
/* 62 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 63 */     this.particleGreen = ((float)(this.particleGreen * 0.96D));
/* 64 */     this.particleBlue = ((float)(this.particleBlue * 0.9D));
/* 65 */     this.motionX *= 0.699999988079071D;
/* 66 */     this.motionY *= 0.699999988079071D;
/* 67 */     this.motionZ *= 0.699999988079071D;
/* 68 */     this.motionY -= 0.019999999552965164D;
/*    */     
/* 70 */     if (this.onGround)
/*    */     {
/* 72 */       this.motionX *= 0.699999988079071D;
/* 73 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 81 */       return new EntityCrit2FX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class MagicFactory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 89 */       EntityFX entityfx = new EntityCrit2FX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 90 */       entityfx.setRBGColorF(entityfx.getRedColorF() * 0.3F, entityfx.getGreenColorF() * 0.8F, entityfx.getBlueColorF());
/* 91 */       entityfx.nextTextureIndexX();
/* 92 */       return entityfx;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityCrit2FX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */