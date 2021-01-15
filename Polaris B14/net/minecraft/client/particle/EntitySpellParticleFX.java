/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySpellParticleFX extends EntityFX
/*     */ {
/*  11 */   private static final Random RANDOM = new Random();
/*     */   
/*     */ 
/*  14 */   private int baseSpellTextureIndex = 128;
/*     */   
/*     */   protected EntitySpellParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1229_8_, double p_i1229_10_, double p_i1229_12_)
/*     */   {
/*  18 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5D - RANDOM.nextDouble(), p_i1229_10_, 0.5D - RANDOM.nextDouble());
/*  19 */     this.motionY *= 0.20000000298023224D;
/*     */     
/*  21 */     if ((p_i1229_8_ == 0.0D) && (p_i1229_12_ == 0.0D))
/*     */     {
/*  23 */       this.motionX *= 0.10000000149011612D;
/*  24 */       this.motionZ *= 0.10000000149011612D;
/*     */     }
/*     */     
/*  27 */     this.particleScale *= 0.75F;
/*  28 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
/*  29 */     this.noClip = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*     */   {
/*  37 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/*  38 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/*  39 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  47 */     this.prevPosX = this.posX;
/*  48 */     this.prevPosY = this.posY;
/*  49 */     this.prevPosZ = this.posZ;
/*     */     
/*  51 */     if (this.particleAge++ >= this.particleMaxAge)
/*     */     {
/*  53 */       setDead();
/*     */     }
/*     */     
/*  56 */     setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
/*  57 */     this.motionY += 0.004D;
/*  58 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */     
/*  60 */     if (this.posY == this.prevPosY)
/*     */     {
/*  62 */       this.motionX *= 1.1D;
/*  63 */       this.motionZ *= 1.1D;
/*     */     }
/*     */     
/*  66 */     this.motionX *= 0.9599999785423279D;
/*  67 */     this.motionY *= 0.9599999785423279D;
/*  68 */     this.motionZ *= 0.9599999785423279D;
/*     */     
/*  70 */     if (this.onGround)
/*     */     {
/*  72 */       this.motionX *= 0.699999988079071D;
/*  73 */       this.motionZ *= 0.699999988079071D;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBaseSpellTextureIndex(int baseSpellTextureIndexIn)
/*     */   {
/*  82 */     this.baseSpellTextureIndex = baseSpellTextureIndexIn;
/*     */   }
/*     */   
/*     */   public static class AmbientMobFactory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/*  89 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*  90 */       entityfx.setAlphaF(0.15F);
/*  91 */       entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
/*  92 */       return entityfx;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Factory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 100 */       return new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class InstantFactory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 108 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 109 */       ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
/* 110 */       return entityfx;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MobFactory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 118 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 119 */       entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
/* 120 */       return entityfx;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WitchFactory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 128 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 129 */       ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
/* 130 */       float f = worldIn.rand.nextFloat() * 0.5F + 0.35F;
/* 131 */       entityfx.setRBGColorF(1.0F * f, 0.0F * f, 1.0F * f);
/* 132 */       return entityfx;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntitySpellParticleFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */