/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityFX
/*     */   extends Entity
/*     */ {
/*     */   protected int particleTextureIndexX;
/*     */   protected int particleTextureIndexY;
/*     */   protected float particleTextureJitterX;
/*     */   protected float particleTextureJitterY;
/*     */   protected int particleAge;
/*     */   protected int particleMaxAge;
/*     */   protected float particleScale;
/*     */   protected float particleGravity;
/*     */   protected float particleRed;
/*     */   protected float particleGreen;
/*     */   protected float particleBlue;
/*     */   protected float particleAlpha;
/*     */   protected TextureAtlasSprite particleIcon;
/*     */   public static double interpPosX;
/*     */   public static double interpPosY;
/*     */   public static double interpPosZ;
/*     */   
/*     */   protected EntityFX(World worldIn, double posXIn, double posYIn, double posZIn)
/*     */   {
/*  46 */     super(worldIn);
/*  47 */     this.particleAlpha = 1.0F;
/*  48 */     setSize(0.2F, 0.2F);
/*  49 */     setPosition(posXIn, posYIn, posZIn);
/*  50 */     this.lastTickPosX = (this.prevPosX = posXIn);
/*  51 */     this.lastTickPosY = (this.prevPosY = posYIn);
/*  52 */     this.lastTickPosZ = (this.prevPosZ = posZIn);
/*  53 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);
/*  54 */     this.particleTextureJitterX = (this.rand.nextFloat() * 3.0F);
/*  55 */     this.particleTextureJitterY = (this.rand.nextFloat() * 3.0F);
/*  56 */     this.particleScale = ((this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F);
/*  57 */     this.particleMaxAge = ((int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F)));
/*  58 */     this.particleAge = 0;
/*     */   }
/*     */   
/*     */   public EntityFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*     */   {
/*  63 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn);
/*  64 */     this.motionX = (xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D);
/*  65 */     this.motionY = (ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D);
/*  66 */     this.motionZ = (zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D);
/*  67 */     float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
/*  68 */     float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
/*  69 */     this.motionX = (this.motionX / f1 * f * 0.4000000059604645D);
/*  70 */     this.motionY = (this.motionY / f1 * f * 0.4000000059604645D + 0.10000000149011612D);
/*  71 */     this.motionZ = (this.motionZ / f1 * f * 0.4000000059604645D);
/*     */   }
/*     */   
/*     */   public EntityFX multiplyVelocity(float multiplier)
/*     */   {
/*  76 */     this.motionX *= multiplier;
/*  77 */     this.motionY = ((this.motionY - 0.10000000149011612D) * multiplier + 0.10000000149011612D);
/*  78 */     this.motionZ *= multiplier;
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public EntityFX multipleParticleScaleBy(float p_70541_1_)
/*     */   {
/*  84 */     setSize(0.2F * p_70541_1_, 0.2F * p_70541_1_);
/*  85 */     this.particleScale *= p_70541_1_;
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn)
/*     */   {
/*  91 */     this.particleRed = particleRedIn;
/*  92 */     this.particleGreen = particleGreenIn;
/*  93 */     this.particleBlue = particleBlueIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlphaF(float alpha)
/*     */   {
/* 101 */     if ((this.particleAlpha == 1.0F) && (alpha < 1.0F))
/*     */     {
/* 103 */       Minecraft.getMinecraft().effectRenderer.moveToAlphaLayer(this);
/*     */     }
/* 105 */     else if ((this.particleAlpha < 1.0F) && (alpha == 1.0F))
/*     */     {
/* 107 */       Minecraft.getMinecraft().effectRenderer.moveToNoAlphaLayer(this);
/*     */     }
/*     */     
/* 110 */     this.particleAlpha = alpha;
/*     */   }
/*     */   
/*     */   public float getRedColorF()
/*     */   {
/* 115 */     return this.particleRed;
/*     */   }
/*     */   
/*     */   public float getGreenColorF()
/*     */   {
/* 120 */     return this.particleGreen;
/*     */   }
/*     */   
/*     */   public float getBlueColorF()
/*     */   {
/* 125 */     return this.particleBlue;
/*     */   }
/*     */   
/*     */   public float getAlpha()
/*     */   {
/* 130 */     return this.particleAlpha;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/* 139 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void entityInit() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 151 */     this.prevPosX = this.posX;
/* 152 */     this.prevPosY = this.posY;
/* 153 */     this.prevPosZ = this.posZ;
/*     */     
/* 155 */     if (this.particleAge++ >= this.particleMaxAge)
/*     */     {
/* 157 */       setDead();
/*     */     }
/*     */     
/* 160 */     this.motionY -= 0.04D * this.particleGravity;
/* 161 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 162 */     this.motionX *= 0.9800000190734863D;
/* 163 */     this.motionY *= 0.9800000190734863D;
/* 164 */     this.motionZ *= 0.9800000190734863D;
/*     */     
/* 166 */     if (this.onGround)
/*     */     {
/* 168 */       this.motionX *= 0.699999988079071D;
/* 169 */       this.motionZ *= 0.699999988079071D;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*     */   {
/* 178 */     float f = this.particleTextureIndexX / 16.0F;
/* 179 */     float f1 = f + 0.0624375F;
/* 180 */     float f2 = this.particleTextureIndexY / 16.0F;
/* 181 */     float f3 = f2 + 0.0624375F;
/* 182 */     float f4 = 0.1F * this.particleScale;
/*     */     
/* 184 */     if (this.particleIcon != null)
/*     */     {
/* 186 */       f = this.particleIcon.getMinU();
/* 187 */       f1 = this.particleIcon.getMaxU();
/* 188 */       f2 = this.particleIcon.getMinV();
/* 189 */       f3 = this.particleIcon.getMaxV();
/*     */     }
/*     */     
/* 192 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 193 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 194 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 195 */     int i = getBrightnessForRender(partialTicks);
/* 196 */     int j = i >> 16 & 0xFFFF;
/* 197 */     int k = i & 0xFFFF;
/* 198 */     worldRendererIn.pos(f5 - p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 - p_180434_6_ * f4 - p_180434_8_ * f4).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/* 199 */     worldRendererIn.pos(f5 - p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 - p_180434_6_ * f4 + p_180434_8_ * f4).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/* 200 */     worldRendererIn.pos(f5 + p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 + p_180434_6_ * f4 + p_180434_8_ * f4).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/* 201 */     worldRendererIn.pos(f5 + p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 + p_180434_6_ * f4 - p_180434_8_ * f4).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/*     */   }
/*     */   
/*     */   public int getFXLayer()
/*     */   {
/* 206 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParticleIcon(TextureAtlasSprite icon)
/*     */   {
/* 228 */     int i = getFXLayer();
/*     */     
/* 230 */     if (i == 1)
/*     */     {
/* 232 */       this.particleIcon = icon;
/*     */     }
/*     */     else
/*     */     {
/* 236 */       throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParticleTextureIndex(int particleTextureIndex)
/*     */   {
/* 245 */     if (getFXLayer() != 0)
/*     */     {
/* 247 */       throw new RuntimeException("Invalid call to Particle.setMiscTex");
/*     */     }
/*     */     
/*     */ 
/* 251 */     this.particleTextureIndexX = (particleTextureIndex % 16);
/* 252 */     this.particleTextureIndexY = (particleTextureIndex / 16);
/*     */   }
/*     */   
/*     */ 
/*     */   public void nextTextureIndexX()
/*     */   {
/* 258 */     this.particleTextureIndexX += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canAttackWithItem()
/*     */   {
/* 266 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 271 */     return getClass().getSimpleName() + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */