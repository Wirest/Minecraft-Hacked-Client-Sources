/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntitySnowShovelFX extends EntityFX
/*    */ {
/*    */   float snowDigParticleScale;
/*    */   
/*    */   protected EntitySnowShovelFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*    */   {
/* 14 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 1.0F);
/*    */   }
/*    */   
/*    */   protected EntitySnowShovelFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float p_i1228_14_)
/*    */   {
/* 19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 20 */     this.motionX *= 0.10000000149011612D;
/* 21 */     this.motionY *= 0.10000000149011612D;
/* 22 */     this.motionZ *= 0.10000000149011612D;
/* 23 */     this.motionX += xSpeedIn;
/* 24 */     this.motionY += ySpeedIn;
/* 25 */     this.motionZ += zSpeedIn;
/* 26 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * 0.30000001192092896D));
/* 27 */     this.particleScale *= 0.75F;
/* 28 */     this.particleScale *= p_i1228_14_;
/* 29 */     this.snowDigParticleScale = this.particleScale;
/* 30 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
/* 31 */     this.particleMaxAge = ((int)(this.particleMaxAge * p_i1228_14_));
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
/* 42 */     this.particleScale = (this.snowDigParticleScale * f);
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
/* 61 */     this.motionY -= 0.03D;
/* 62 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 63 */     this.motionX *= 0.9900000095367432D;
/* 64 */     this.motionY *= 0.9900000095367432D;
/* 65 */     this.motionZ *= 0.9900000095367432D;
/*    */     
/* 67 */     if (this.onGround)
/*    */     {
/* 69 */       this.motionX *= 0.699999988079071D;
/* 70 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 78 */       return new EntitySnowShovelFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntitySnowShovelFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */