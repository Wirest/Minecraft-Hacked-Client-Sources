/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityFirework
/*     */ {
/*     */   public static class Factory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/*  19 */       EntityFirework.SparkFX entityfirework$sparkfx = new EntityFirework.SparkFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Minecraft.getMinecraft().effectRenderer);
/*  20 */       entityfirework$sparkfx.setAlphaF(0.99F);
/*  21 */       return entityfirework$sparkfx;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OverlayFX extends EntityFX
/*     */   {
/*     */     protected OverlayFX(World p_i46466_1_, double p_i46466_2_, double p_i46466_4_, double p_i46466_6_)
/*     */     {
/*  29 */       super(p_i46466_2_, p_i46466_4_, p_i46466_6_);
/*  30 */       this.particleMaxAge = 4;
/*     */     }
/*     */     
/*     */     public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*     */     {
/*  35 */       float f = 0.25F;
/*  36 */       float f1 = 0.5F;
/*  37 */       float f2 = 0.125F;
/*  38 */       float f3 = 0.375F;
/*  39 */       float f4 = 7.1F * MathHelper.sin((this.particleAge + partialTicks - 1.0F) * 0.25F * 3.1415927F);
/*  40 */       this.particleAlpha = (0.6F - (this.particleAge + partialTicks - 1.0F) * 0.25F * 0.5F);
/*  41 */       float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/*  42 */       float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/*  43 */       float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/*  44 */       int i = getBrightnessForRender(partialTicks);
/*  45 */       int j = i >> 16 & 0xFFFF;
/*  46 */       int k = i & 0xFFFF;
/*  47 */       worldRendererIn.pos(f5 - p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 - p_180434_6_ * f4 - p_180434_8_ * f4).tex(0.5D, 0.375D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/*  48 */       worldRendererIn.pos(f5 - p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 - p_180434_6_ * f4 + p_180434_8_ * f4).tex(0.5D, 0.125D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/*  49 */       worldRendererIn.pos(f5 + p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 + p_180434_6_ * f4 + p_180434_8_ * f4).tex(0.25D, 0.125D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/*  50 */       worldRendererIn.pos(f5 + p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 + p_180434_6_ * f4 - p_180434_8_ * f4).tex(0.25D, 0.375D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SparkFX extends EntityFX
/*     */   {
/*  56 */     private int baseTextureIndex = 160;
/*     */     private boolean trail;
/*     */     private boolean twinkle;
/*     */     private final EffectRenderer field_92047_az;
/*     */     private float fadeColourRed;
/*     */     private float fadeColourGreen;
/*     */     private float fadeColourBlue;
/*     */     private boolean hasFadeColour;
/*     */     
/*     */     public SparkFX(World p_i46465_1_, double p_i46465_2_, double p_i46465_4_, double p_i46465_6_, double p_i46465_8_, double p_i46465_10_, double p_i46465_12_, EffectRenderer p_i46465_14_)
/*     */     {
/*  67 */       super(p_i46465_2_, p_i46465_4_, p_i46465_6_);
/*  68 */       this.motionX = p_i46465_8_;
/*  69 */       this.motionY = p_i46465_10_;
/*  70 */       this.motionZ = p_i46465_12_;
/*  71 */       this.field_92047_az = p_i46465_14_;
/*  72 */       this.particleScale *= 0.75F;
/*  73 */       this.particleMaxAge = (48 + this.rand.nextInt(12));
/*  74 */       this.noClip = false;
/*     */     }
/*     */     
/*     */     public void setTrail(boolean trailIn)
/*     */     {
/*  79 */       this.trail = trailIn;
/*     */     }
/*     */     
/*     */     public void setTwinkle(boolean twinkleIn)
/*     */     {
/*  84 */       this.twinkle = twinkleIn;
/*     */     }
/*     */     
/*     */     public void setColour(int colour)
/*     */     {
/*  89 */       float f = ((colour & 0xFF0000) >> 16) / 255.0F;
/*  90 */       float f1 = ((colour & 0xFF00) >> 8) / 255.0F;
/*  91 */       float f2 = ((colour & 0xFF) >> 0) / 255.0F;
/*  92 */       float f3 = 1.0F;
/*  93 */       setRBGColorF(f * f3, f1 * f3, f2 * f3);
/*     */     }
/*     */     
/*     */     public void setFadeColour(int faceColour)
/*     */     {
/*  98 */       this.fadeColourRed = (((faceColour & 0xFF0000) >> 16) / 255.0F);
/*  99 */       this.fadeColourGreen = (((faceColour & 0xFF00) >> 8) / 255.0F);
/* 100 */       this.fadeColourBlue = (((faceColour & 0xFF) >> 0) / 255.0F);
/* 101 */       this.hasFadeColour = true;
/*     */     }
/*     */     
/*     */     public AxisAlignedBB getCollisionBoundingBox()
/*     */     {
/* 106 */       return null;
/*     */     }
/*     */     
/*     */     public boolean canBePushed()
/*     */     {
/* 111 */       return false;
/*     */     }
/*     */     
/*     */     public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*     */     {
/* 116 */       if ((!this.twinkle) || (this.particleAge < this.particleMaxAge / 3) || ((this.particleAge + this.particleMaxAge) / 3 % 2 == 0))
/*     */       {
/* 118 */         super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*     */       }
/*     */     }
/*     */     
/*     */     public void onUpdate()
/*     */     {
/* 124 */       this.prevPosX = this.posX;
/* 125 */       this.prevPosY = this.posY;
/* 126 */       this.prevPosZ = this.posZ;
/*     */       
/* 128 */       if (this.particleAge++ >= this.particleMaxAge)
/*     */       {
/* 130 */         setDead();
/*     */       }
/*     */       
/* 133 */       if (this.particleAge > this.particleMaxAge / 2)
/*     */       {
/* 135 */         setAlphaF(1.0F - (this.particleAge - this.particleMaxAge / 2) / this.particleMaxAge);
/*     */         
/* 137 */         if (this.hasFadeColour)
/*     */         {
/* 139 */           this.particleRed += (this.fadeColourRed - this.particleRed) * 0.2F;
/* 140 */           this.particleGreen += (this.fadeColourGreen - this.particleGreen) * 0.2F;
/* 141 */           this.particleBlue += (this.fadeColourBlue - this.particleBlue) * 0.2F;
/*     */         }
/*     */       }
/*     */       
/* 145 */       setParticleTextureIndex(this.baseTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
/* 146 */       this.motionY -= 0.004D;
/* 147 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/* 148 */       this.motionX *= 0.9100000262260437D;
/* 149 */       this.motionY *= 0.9100000262260437D;
/* 150 */       this.motionZ *= 0.9100000262260437D;
/*     */       
/* 152 */       if (this.onGround)
/*     */       {
/* 154 */         this.motionX *= 0.699999988079071D;
/* 155 */         this.motionZ *= 0.699999988079071D;
/*     */       }
/*     */       
/* 158 */       if ((this.trail) && (this.particleAge < this.particleMaxAge / 2) && ((this.particleAge + this.particleMaxAge) % 2 == 0))
/*     */       {
/* 160 */         SparkFX entityfirework$sparkfx = new SparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, this.field_92047_az);
/* 161 */         entityfirework$sparkfx.setAlphaF(0.99F);
/* 162 */         entityfirework$sparkfx.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
/* 163 */         entityfirework$sparkfx.particleAge = (entityfirework$sparkfx.particleMaxAge / 2);
/*     */         
/* 165 */         if (this.hasFadeColour)
/*     */         {
/* 167 */           entityfirework$sparkfx.hasFadeColour = true;
/* 168 */           entityfirework$sparkfx.fadeColourRed = this.fadeColourRed;
/* 169 */           entityfirework$sparkfx.fadeColourGreen = this.fadeColourGreen;
/* 170 */           entityfirework$sparkfx.fadeColourBlue = this.fadeColourBlue;
/*     */         }
/*     */         
/* 173 */         entityfirework$sparkfx.twinkle = this.twinkle;
/* 174 */         this.field_92047_az.addEffect(entityfirework$sparkfx);
/*     */       }
/*     */     }
/*     */     
/*     */     public int getBrightnessForRender(float partialTicks)
/*     */     {
/* 180 */       return 15728880;
/*     */     }
/*     */     
/*     */     public float getBrightness(float partialTicks)
/*     */     {
/* 185 */       return 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class StarterFX extends EntityFX
/*     */   {
/*     */     private int fireworkAge;
/*     */     private final EffectRenderer theEffectRenderer;
/*     */     private NBTTagList fireworkExplosions;
/*     */     boolean twinkle;
/*     */     
/*     */     public StarterFX(World p_i46464_1_, double p_i46464_2_, double p_i46464_4_, double p_i46464_6_, double p_i46464_8_, double p_i46464_10_, double p_i46464_12_, EffectRenderer p_i46464_14_, NBTTagCompound p_i46464_15_)
/*     */     {
/* 198 */       super(p_i46464_2_, p_i46464_4_, p_i46464_6_, 0.0D, 0.0D, 0.0D);
/* 199 */       this.motionX = p_i46464_8_;
/* 200 */       this.motionY = p_i46464_10_;
/* 201 */       this.motionZ = p_i46464_12_;
/* 202 */       this.theEffectRenderer = p_i46464_14_;
/* 203 */       this.particleMaxAge = 8;
/*     */       
/* 205 */       if (p_i46464_15_ != null)
/*     */       {
/* 207 */         this.fireworkExplosions = p_i46464_15_.getTagList("Explosions", 10);
/*     */         
/* 209 */         if (this.fireworkExplosions.tagCount() == 0)
/*     */         {
/* 211 */           this.fireworkExplosions = null;
/*     */         }
/*     */         else
/*     */         {
/* 215 */           this.particleMaxAge = (this.fireworkExplosions.tagCount() * 2 - 1);
/*     */           
/* 217 */           for (int i = 0; i < this.fireworkExplosions.tagCount(); i++)
/*     */           {
/* 219 */             NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);
/*     */             
/* 221 */             if (nbttagcompound.getBoolean("Flicker"))
/*     */             {
/* 223 */               this.twinkle = true;
/* 224 */               this.particleMaxAge += 15;
/* 225 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {}
/*     */     
/*     */ 
/*     */     public void onUpdate()
/*     */     {
/* 238 */       if ((this.fireworkAge == 0) && (this.fireworkExplosions != null))
/*     */       {
/* 240 */         boolean flag = func_92037_i();
/* 241 */         boolean flag1 = false;
/*     */         
/* 243 */         if (this.fireworkExplosions.tagCount() >= 3)
/*     */         {
/* 245 */           flag1 = true;
/*     */         }
/*     */         else
/*     */         {
/* 249 */           for (int i = 0; i < this.fireworkExplosions.tagCount(); i++)
/*     */           {
/* 251 */             NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);
/*     */             
/* 253 */             if (nbttagcompound.getByte("Type") == 1)
/*     */             {
/* 255 */               flag1 = true;
/* 256 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 261 */         String s1 = "fireworks." + (flag1 ? "largeBlast" : "blast") + (flag ? "_far" : "");
/* 262 */         this.worldObj.playSound(this.posX, this.posY, this.posZ, s1, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);
/*     */       }
/*     */       
/* 265 */       if ((this.fireworkAge % 2 == 0) && (this.fireworkExplosions != null) && (this.fireworkAge / 2 < this.fireworkExplosions.tagCount()))
/*     */       {
/* 267 */         int k = this.fireworkAge / 2;
/* 268 */         NBTTagCompound nbttagcompound1 = this.fireworkExplosions.getCompoundTagAt(k);
/* 269 */         int l = nbttagcompound1.getByte("Type");
/* 270 */         boolean flag4 = nbttagcompound1.getBoolean("Trail");
/* 271 */         boolean flag2 = nbttagcompound1.getBoolean("Flicker");
/* 272 */         int[] aint = nbttagcompound1.getIntArray("Colors");
/* 273 */         int[] aint1 = nbttagcompound1.getIntArray("FadeColors");
/*     */         
/* 275 */         if (aint.length == 0)
/*     */         {
/* 277 */           aint = new int[] { net.minecraft.item.ItemDye.dyeColors[0] };
/*     */         }
/*     */         
/* 280 */         if (l == 1)
/*     */         {
/* 282 */           createBall(0.5D, 4, aint, aint1, flag4, flag2);
/*     */         }
/* 284 */         else if (l == 2)
/*     */         {
/* 286 */           createShaped(0.5D, new double[][] { { 0.0D, 1.0D }, { 0.3455D, 0.309D }, { 0.9511D, 0.309D }, { 0.3795918367346939D, -0.12653061224489795D }, { 0.6122448979591837D, -0.8040816326530612D }, { 0.0D, -0.35918367346938773D } }, aint, aint1, flag4, flag2, false);
/*     */         }
/* 288 */         else if (l == 3)
/*     */         {
/* 290 */           createShaped(0.5D, new double[][] { { 0.0D, 0.2D }, { 0.2D, 0.2D }, { 0.2D, 0.6D }, { 0.6D, 0.6D }, { 0.6D, 0.2D }, { 0.2D, 0.2D }, { 0.2D, 0.0D }, { 0.4D, 0.0D }, { 0.4D, -0.6D }, { 0.2D, -0.6D }, { 0.2D, -0.4D }, { 0.0D, -0.4D } }, aint, aint1, flag4, flag2, true);
/*     */         }
/* 292 */         else if (l == 4)
/*     */         {
/* 294 */           createBurst(aint, aint1, flag4, flag2);
/*     */         }
/*     */         else
/*     */         {
/* 298 */           createBall(0.25D, 2, aint, aint1, flag4, flag2);
/*     */         }
/*     */         
/* 301 */         int j = aint[0];
/* 302 */         float f = ((j & 0xFF0000) >> 16) / 255.0F;
/* 303 */         float f1 = ((j & 0xFF00) >> 8) / 255.0F;
/* 304 */         float f2 = ((j & 0xFF) >> 0) / 255.0F;
/* 305 */         EntityFirework.OverlayFX entityfirework$overlayfx = new EntityFirework.OverlayFX(this.worldObj, this.posX, this.posY, this.posZ);
/* 306 */         entityfirework$overlayfx.setRBGColorF(f, f1, f2);
/* 307 */         this.theEffectRenderer.addEffect(entityfirework$overlayfx);
/*     */       }
/*     */       
/* 310 */       this.fireworkAge += 1;
/*     */       
/* 312 */       if (this.fireworkAge > this.particleMaxAge)
/*     */       {
/* 314 */         if (this.twinkle)
/*     */         {
/* 316 */           boolean flag3 = func_92037_i();
/* 317 */           String s = "fireworks." + (flag3 ? "twinkle_far" : "twinkle");
/* 318 */           this.worldObj.playSound(this.posX, this.posY, this.posZ, s, 20.0F, 0.9F + this.rand.nextFloat() * 0.15F, true);
/*     */         }
/*     */         
/* 321 */         setDead();
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean func_92037_i()
/*     */     {
/* 327 */       Minecraft minecraft = Minecraft.getMinecraft();
/* 328 */       return (minecraft == null) || (minecraft.getRenderViewEntity() == null) || (minecraft.getRenderViewEntity().getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0D);
/*     */     }
/*     */     
/*     */     private void createParticle(double p_92034_1_, double p_92034_3_, double p_92034_5_, double p_92034_7_, double p_92034_9_, double p_92034_11_, int[] p_92034_13_, int[] p_92034_14_, boolean p_92034_15_, boolean p_92034_16_)
/*     */     {
/* 333 */       EntityFirework.SparkFX entityfirework$sparkfx = new EntityFirework.SparkFX(this.worldObj, p_92034_1_, p_92034_3_, p_92034_5_, p_92034_7_, p_92034_9_, p_92034_11_, this.theEffectRenderer);
/* 334 */       entityfirework$sparkfx.setAlphaF(0.99F);
/* 335 */       entityfirework$sparkfx.setTrail(p_92034_15_);
/* 336 */       entityfirework$sparkfx.setTwinkle(p_92034_16_);
/* 337 */       int i = this.rand.nextInt(p_92034_13_.length);
/* 338 */       entityfirework$sparkfx.setColour(p_92034_13_[i]);
/*     */       
/* 340 */       if ((p_92034_14_ != null) && (p_92034_14_.length > 0))
/*     */       {
/* 342 */         entityfirework$sparkfx.setFadeColour(p_92034_14_[this.rand.nextInt(p_92034_14_.length)]);
/*     */       }
/*     */       
/* 345 */       this.theEffectRenderer.addEffect(entityfirework$sparkfx);
/*     */     }
/*     */     
/*     */     private void createBall(double speed, int size, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn)
/*     */     {
/* 350 */       double d0 = this.posX;
/* 351 */       double d1 = this.posY;
/* 352 */       double d2 = this.posZ;
/*     */       
/* 354 */       for (int i = -size; i <= size; i++)
/*     */       {
/* 356 */         for (int j = -size; j <= size; j++)
/*     */         {
/* 358 */           for (int k = -size; k <= size; k++)
/*     */           {
/* 360 */             double d3 = j + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
/* 361 */             double d4 = i + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
/* 362 */             double d5 = k + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
/* 363 */             double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5) / speed + this.rand.nextGaussian() * 0.05D;
/* 364 */             createParticle(d0, d1, d2, d3 / d6, d4 / d6, d5 / d6, colours, fadeColours, trail, twinkleIn);
/*     */             
/* 366 */             if ((i != -size) && (i != size) && (j != -size) && (j != size))
/*     */             {
/* 368 */               k += size * 2 - 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void createShaped(double speed, double[][] shape, int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn, boolean p_92038_8_)
/*     */     {
/* 377 */       double d0 = shape[0][0];
/* 378 */       double d1 = shape[0][1];
/* 379 */       createParticle(this.posX, this.posY, this.posZ, d0 * speed, d1 * speed, 0.0D, colours, fadeColours, trail, twinkleIn);
/* 380 */       float f = this.rand.nextFloat() * 3.1415927F;
/* 381 */       double d2 = p_92038_8_ ? 0.034D : 0.34D;
/*     */       
/* 383 */       for (int i = 0; i < 3; i++)
/*     */       {
/* 385 */         double d3 = f + i * 3.1415927F * d2;
/* 386 */         double d4 = d0;
/* 387 */         double d5 = d1;
/*     */         
/* 389 */         for (int j = 1; j < shape.length; j++)
/*     */         {
/* 391 */           double d6 = shape[j][0];
/* 392 */           double d7 = shape[j][1];
/*     */           
/* 394 */           for (double d8 = 0.25D; d8 <= 1.0D; d8 += 0.25D)
/*     */           {
/* 396 */             double d9 = (d4 + (d6 - d4) * d8) * speed;
/* 397 */             double d10 = (d5 + (d7 - d5) * d8) * speed;
/* 398 */             double d11 = d9 * Math.sin(d3);
/* 399 */             d9 *= Math.cos(d3);
/*     */             
/* 401 */             for (double d12 = -1.0D; d12 <= 1.0D; d12 += 2.0D)
/*     */             {
/* 403 */               createParticle(this.posX, this.posY, this.posZ, d9 * d12, d10, d11 * d12, colours, fadeColours, trail, twinkleIn);
/*     */             }
/*     */           }
/*     */           
/* 407 */           d4 = d6;
/* 408 */           d5 = d7;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void createBurst(int[] colours, int[] fadeColours, boolean trail, boolean twinkleIn)
/*     */     {
/* 415 */       double d0 = this.rand.nextGaussian() * 0.05D;
/* 416 */       double d1 = this.rand.nextGaussian() * 0.05D;
/*     */       
/* 418 */       for (int i = 0; i < 70; i++)
/*     */       {
/* 420 */         double d2 = this.motionX * 0.5D + this.rand.nextGaussian() * 0.15D + d0;
/* 421 */         double d3 = this.motionZ * 0.5D + this.rand.nextGaussian() * 0.15D + d1;
/* 422 */         double d4 = this.motionY * 0.5D + this.rand.nextDouble() * 0.5D;
/* 423 */         createParticle(this.posX, this.posY, this.posZ, d2, d4, d3, colours, fadeColours, trail, twinkleIn);
/*     */       }
/*     */     }
/*     */     
/*     */     public int getFXLayer()
/*     */     {
/* 429 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityFirework.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */