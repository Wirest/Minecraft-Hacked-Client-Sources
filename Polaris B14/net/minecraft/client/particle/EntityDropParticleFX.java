/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class EntityDropParticleFX
/*     */   extends EntityFX
/*     */ {
/*     */   private Material materialType;
/*     */   private int bobTimer;
/*     */   
/*     */   protected EntityDropParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, Material p_i1203_8_)
/*     */   {
/*  21 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/*  22 */     this.motionX = (this.motionY = this.motionZ = 0.0D);
/*     */     
/*  24 */     if (p_i1203_8_ == Material.water)
/*     */     {
/*  26 */       this.particleRed = 0.0F;
/*  27 */       this.particleGreen = 0.0F;
/*  28 */       this.particleBlue = 1.0F;
/*     */     }
/*     */     else
/*     */     {
/*  32 */       this.particleRed = 1.0F;
/*  33 */       this.particleGreen = 0.0F;
/*  34 */       this.particleBlue = 0.0F;
/*     */     }
/*     */     
/*  37 */     setParticleTextureIndex(113);
/*  38 */     setSize(0.01F, 0.01F);
/*  39 */     this.particleGravity = 0.06F;
/*  40 */     this.materialType = p_i1203_8_;
/*  41 */     this.bobTimer = 40;
/*  42 */     this.particleMaxAge = ((int)(64.0D / (Math.random() * 0.8D + 0.2D)));
/*  43 */     this.motionX = (this.motionY = this.motionZ = 0.0D);
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/*  48 */     return this.materialType == Material.water ? super.getBrightnessForRender(partialTicks) : 257;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBrightness(float partialTicks)
/*     */   {
/*  56 */     return this.materialType == Material.water ? super.getBrightness(partialTicks) : 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  64 */     this.prevPosX = this.posX;
/*  65 */     this.prevPosY = this.posY;
/*  66 */     this.prevPosZ = this.posZ;
/*     */     
/*  68 */     if (this.materialType == Material.water)
/*     */     {
/*  70 */       this.particleRed = 0.2F;
/*  71 */       this.particleGreen = 0.3F;
/*  72 */       this.particleBlue = 1.0F;
/*     */     }
/*     */     else
/*     */     {
/*  76 */       this.particleRed = 1.0F;
/*  77 */       this.particleGreen = (16.0F / (40 - this.bobTimer + 16));
/*  78 */       this.particleBlue = (4.0F / (40 - this.bobTimer + 8));
/*     */     }
/*     */     
/*  81 */     this.motionY -= this.particleGravity;
/*     */     
/*  83 */     if (this.bobTimer-- > 0)
/*     */     {
/*  85 */       this.motionX *= 0.02D;
/*  86 */       this.motionY *= 0.02D;
/*  87 */       this.motionZ *= 0.02D;
/*  88 */       setParticleTextureIndex(113);
/*     */     }
/*     */     else
/*     */     {
/*  92 */       setParticleTextureIndex(112);
/*     */     }
/*     */     
/*  95 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*  96 */     this.motionX *= 0.9800000190734863D;
/*  97 */     this.motionY *= 0.9800000190734863D;
/*  98 */     this.motionZ *= 0.9800000190734863D;
/*     */     
/* 100 */     if (this.particleMaxAge-- <= 0)
/*     */     {
/* 102 */       setDead();
/*     */     }
/*     */     
/* 105 */     if (this.onGround)
/*     */     {
/* 107 */       if (this.materialType == Material.water)
/*     */       {
/* 109 */         setDead();
/* 110 */         this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */       else
/*     */       {
/* 114 */         setParticleTextureIndex(114);
/*     */       }
/*     */       
/* 117 */       this.motionX *= 0.699999988079071D;
/* 118 */       this.motionZ *= 0.699999988079071D;
/*     */     }
/*     */     
/* 121 */     BlockPos blockpos = new BlockPos(this);
/* 122 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 123 */     Material material = iblockstate.getBlock().getMaterial();
/*     */     
/* 125 */     if ((material.isLiquid()) || (material.isSolid()))
/*     */     {
/* 127 */       double d0 = 0.0D;
/*     */       
/* 129 */       if ((iblockstate.getBlock() instanceof BlockLiquid))
/*     */       {
/* 131 */         d0 = BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue());
/*     */       }
/*     */       
/* 134 */       double d1 = MathHelper.floor_double(this.posY) + 1 - d0;
/*     */       
/* 136 */       if (this.posY < d1)
/*     */       {
/* 138 */         setDead();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LavaFactory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 147 */       return new EntityDropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.lava);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaterFactory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 155 */       return new EntityDropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.water);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityDropParticleFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */