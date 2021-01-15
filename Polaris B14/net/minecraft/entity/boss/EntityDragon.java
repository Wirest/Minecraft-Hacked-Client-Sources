/*     */ package net.minecraft.entity.boss;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockTorch;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityMultiPart;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.monster.IMob;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntityDamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityDragon
/*     */   extends EntityLiving implements IBossDisplayData, IEntityMultiPart, IMob
/*     */ {
/*     */   public double targetX;
/*     */   public double targetY;
/*     */   public double targetZ;
/*  39 */   public double[][] ringBuffer = new double[64][3];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public int ringBufferIndex = -1;
/*     */   
/*     */ 
/*     */   public EntityDragonPart[] dragonPartArray;
/*     */   
/*     */ 
/*     */   public EntityDragonPart dragonPartHead;
/*     */   
/*     */ 
/*     */   public EntityDragonPart dragonPartBody;
/*     */   
/*     */ 
/*     */   public EntityDragonPart dragonPartTail1;
/*     */   
/*     */   public EntityDragonPart dragonPartTail2;
/*     */   
/*     */   public EntityDragonPart dragonPartTail3;
/*     */   
/*     */   public EntityDragonPart dragonPartWing1;
/*     */   
/*     */   public EntityDragonPart dragonPartWing2;
/*     */   
/*     */   public float prevAnimTime;
/*     */   
/*     */   public float animTime;
/*     */   
/*     */   public boolean forceNewTarget;
/*     */   
/*     */   public boolean slowed;
/*     */   
/*     */   private Entity target;
/*     */   
/*     */   public int deathTicks;
/*     */   
/*     */   public EntityEnderCrystal healingEnderCrystal;
/*     */   
/*     */ 
/*     */   public EntityDragon(World worldIn)
/*     */   {
/*  83 */     super(worldIn);
/*  84 */     this.dragonPartArray = new EntityDragonPart[] { this.dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F) };
/*  85 */     setHealth(getMaxHealth());
/*  86 */     setSize(16.0F, 8.0F);
/*  87 */     this.noClip = true;
/*  88 */     this.isImmuneToFire = true;
/*  89 */     this.targetY = 100.0D;
/*  90 */     this.ignoreFrustumCheck = true;
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  95 */     super.applyEntityAttributes();
/*  96 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 101 */     super.entityInit();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
/*     */   {
/* 110 */     if (getHealth() <= 0.0F)
/*     */     {
/* 112 */       p_70974_2_ = 0.0F;
/*     */     }
/*     */     
/* 115 */     p_70974_2_ = 1.0F - p_70974_2_;
/* 116 */     int i = this.ringBufferIndex - p_70974_1_ * 1 & 0x3F;
/* 117 */     int j = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 0x3F;
/* 118 */     double[] adouble = new double[3];
/* 119 */     double d0 = this.ringBuffer[i][0];
/* 120 */     double d1 = MathHelper.wrapAngleTo180_double(this.ringBuffer[j][0] - d0);
/* 121 */     adouble[0] = (d0 + d1 * p_70974_2_);
/* 122 */     d0 = this.ringBuffer[i][1];
/* 123 */     d1 = this.ringBuffer[j][1] - d0;
/* 124 */     adouble[1] = (d0 + d1 * p_70974_2_);
/* 125 */     adouble[2] = (this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * p_70974_2_);
/* 126 */     return adouble;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 135 */     if (this.worldObj.isRemote)
/*     */     {
/* 137 */       float f = MathHelper.cos(this.animTime * 3.1415927F * 2.0F);
/* 138 */       float f1 = MathHelper.cos(this.prevAnimTime * 3.1415927F * 2.0F);
/*     */       
/* 140 */       if ((f1 <= -0.3F) && (f >= -0.3F) && (!isSilent()))
/*     */       {
/* 142 */         this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
/*     */       }
/*     */     }
/*     */     
/* 146 */     this.prevAnimTime = this.animTime;
/*     */     
/* 148 */     if (getHealth() <= 0.0F)
/*     */     {
/* 150 */       float f11 = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 151 */       float f13 = (this.rand.nextFloat() - 0.5F) * 4.0F;
/* 152 */       float f14 = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 153 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f11, this.posY + 2.0D + f13, this.posZ + f14, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     else
/*     */     {
/* 157 */       updateDragonEnderCrystal();
/* 158 */       float f10 = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
/* 159 */       f10 *= (float)Math.pow(2.0D, this.motionY);
/*     */       
/* 161 */       if (this.slowed)
/*     */       {
/* 163 */         this.animTime += f10 * 0.5F;
/*     */       }
/*     */       else
/*     */       {
/* 167 */         this.animTime += f10;
/*     */       }
/*     */       
/* 170 */       this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
/*     */       
/* 172 */       if (isAIDisabled())
/*     */       {
/* 174 */         this.animTime = 0.5F;
/*     */       }
/*     */       else
/*     */       {
/* 178 */         if (this.ringBufferIndex < 0)
/*     */         {
/* 180 */           for (int i = 0; i < this.ringBuffer.length; i++)
/*     */           {
/* 182 */             this.ringBuffer[i][0] = this.rotationYaw;
/* 183 */             this.ringBuffer[i][1] = this.posY;
/*     */           }
/*     */         }
/*     */         
/* 187 */         if (++this.ringBufferIndex == this.ringBuffer.length)
/*     */         {
/* 189 */           this.ringBufferIndex = 0;
/*     */         }
/*     */         
/* 192 */         this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
/* 193 */         this.ringBuffer[this.ringBufferIndex][1] = this.posY;
/*     */         
/* 195 */         if (this.worldObj.isRemote)
/*     */         {
/* 197 */           if (this.newPosRotationIncrements > 0)
/*     */           {
/* 199 */             double d10 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
/* 200 */             double d0 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
/* 201 */             double d1 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
/* 202 */             double d2 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
/* 203 */             this.rotationYaw = ((float)(this.rotationYaw + d2 / this.newPosRotationIncrements));
/* 204 */             this.rotationPitch = ((float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements));
/* 205 */             this.newPosRotationIncrements -= 1;
/* 206 */             setPosition(d10, d0, d1);
/* 207 */             setRotation(this.rotationYaw, this.rotationPitch);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 212 */           double d11 = this.targetX - this.posX;
/* 213 */           double d12 = this.targetY - this.posY;
/* 214 */           double d13 = this.targetZ - this.posZ;
/* 215 */           double d14 = d11 * d11 + d12 * d12 + d13 * d13;
/*     */           
/* 217 */           if (this.target != null)
/*     */           {
/* 219 */             this.targetX = this.target.posX;
/* 220 */             this.targetZ = this.target.posZ;
/* 221 */             double d3 = this.targetX - this.posX;
/* 222 */             double d5 = this.targetZ - this.posZ;
/* 223 */             double d7 = Math.sqrt(d3 * d3 + d5 * d5);
/* 224 */             double d8 = 0.4000000059604645D + d7 / 80.0D - 1.0D;
/*     */             
/* 226 */             if (d8 > 10.0D)
/*     */             {
/* 228 */               d8 = 10.0D;
/*     */             }
/*     */             
/* 231 */             this.targetY = (this.target.getEntityBoundingBox().minY + d8);
/*     */           }
/*     */           else
/*     */           {
/* 235 */             this.targetX += this.rand.nextGaussian() * 2.0D;
/* 236 */             this.targetZ += this.rand.nextGaussian() * 2.0D;
/*     */           }
/*     */           
/* 239 */           if ((this.forceNewTarget) || (d14 < 100.0D) || (d14 > 22500.0D) || (this.isCollidedHorizontally) || (this.isCollidedVertically))
/*     */           {
/* 241 */             setNewTarget();
/*     */           }
/*     */           
/* 244 */           d12 /= MathHelper.sqrt_double(d11 * d11 + d13 * d13);
/* 245 */           float f17 = 0.6F;
/* 246 */           d12 = MathHelper.clamp_double(d12, -f17, f17);
/* 247 */           this.motionY += d12 * 0.10000000149011612D;
/* 248 */           this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
/* 249 */           double d4 = 180.0D - MathHelper.func_181159_b(d11, d13) * 180.0D / 3.141592653589793D;
/* 250 */           double d6 = MathHelper.wrapAngleTo180_double(d4 - this.rotationYaw);
/*     */           
/* 252 */           if (d6 > 50.0D)
/*     */           {
/* 254 */             d6 = 50.0D;
/*     */           }
/*     */           
/* 257 */           if (d6 < -50.0D)
/*     */           {
/* 259 */             d6 = -50.0D;
/*     */           }
/*     */           
/* 262 */           Vec3 vec3 = new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
/* 263 */           double d15 = -MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
/* 264 */           Vec3 vec31 = new Vec3(MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F), this.motionY, d15).normalize();
/* 265 */           float f5 = ((float)vec31.dotProduct(vec3) + 0.5F) / 1.5F;
/*     */           
/* 267 */           if (f5 < 0.0F)
/*     */           {
/* 269 */             f5 = 0.0F;
/*     */           }
/*     */           
/* 272 */           this.randomYawVelocity *= 0.8F;
/* 273 */           float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
/* 274 */           double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;
/*     */           
/* 276 */           if (d9 > 40.0D)
/*     */           {
/* 278 */             d9 = 40.0D;
/*     */           }
/*     */           
/* 281 */           this.randomYawVelocity = ((float)(this.randomYawVelocity + d6 * (0.699999988079071D / d9 / f6)));
/* 282 */           this.rotationYaw += this.randomYawVelocity * 0.1F;
/* 283 */           float f7 = (float)(2.0D / (d9 + 1.0D));
/* 284 */           float f8 = 0.06F;
/* 285 */           moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));
/*     */           
/* 287 */           if (this.slowed)
/*     */           {
/* 289 */             moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
/*     */           }
/*     */           else
/*     */           {
/* 293 */             moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */           }
/*     */           
/* 296 */           Vec3 vec32 = new Vec3(this.motionX, this.motionY, this.motionZ).normalize();
/* 297 */           float f9 = ((float)vec32.dotProduct(vec31) + 1.0F) / 2.0F;
/* 298 */           f9 = 0.8F + 0.15F * f9;
/* 299 */           this.motionX *= f9;
/* 300 */           this.motionZ *= f9;
/* 301 */           this.motionY *= 0.9100000262260437D;
/*     */         }
/*     */         
/* 304 */         this.renderYawOffset = this.rotationYaw;
/* 305 */         this.dragonPartHead.width = (this.dragonPartHead.height = 3.0F);
/* 306 */         this.dragonPartTail1.width = (this.dragonPartTail1.height = 2.0F);
/* 307 */         this.dragonPartTail2.width = (this.dragonPartTail2.height = 2.0F);
/* 308 */         this.dragonPartTail3.width = (this.dragonPartTail3.height = 2.0F);
/* 309 */         this.dragonPartBody.height = 3.0F;
/* 310 */         this.dragonPartBody.width = 5.0F;
/* 311 */         this.dragonPartWing1.height = 2.0F;
/* 312 */         this.dragonPartWing1.width = 4.0F;
/* 313 */         this.dragonPartWing2.height = 3.0F;
/* 314 */         this.dragonPartWing2.width = 4.0F;
/* 315 */         float f12 = (float)(getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
/* 316 */         float f2 = MathHelper.cos(f12);
/* 317 */         float f15 = -MathHelper.sin(f12);
/* 318 */         float f3 = this.rotationYaw * 3.1415927F / 180.0F;
/* 319 */         float f16 = MathHelper.sin(f3);
/* 320 */         float f4 = MathHelper.cos(f3);
/* 321 */         this.dragonPartBody.onUpdate();
/* 322 */         this.dragonPartBody.setLocationAndAngles(this.posX + f16 * 0.5F, this.posY, this.posZ - f4 * 0.5F, 0.0F, 0.0F);
/* 323 */         this.dragonPartWing1.onUpdate();
/* 324 */         this.dragonPartWing1.setLocationAndAngles(this.posX + f4 * 4.5F, this.posY + 2.0D, this.posZ + f16 * 4.5F, 0.0F, 0.0F);
/* 325 */         this.dragonPartWing2.onUpdate();
/* 326 */         this.dragonPartWing2.setLocationAndAngles(this.posX - f4 * 4.5F, this.posY + 2.0D, this.posZ - f16 * 4.5F, 0.0F, 0.0F);
/*     */         
/* 328 */         if ((!this.worldObj.isRemote) && (this.hurtTime == 0))
/*     */         {
/* 330 */           collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
/* 331 */           collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
/* 332 */           attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D)));
/*     */         }
/*     */         
/* 335 */         double[] adouble1 = getMovementOffsets(5, 1.0F);
/* 336 */         double[] adouble = getMovementOffsets(0, 1.0F);
/* 337 */         float f18 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F - this.randomYawVelocity * 0.01F);
/* 338 */         float f19 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F - this.randomYawVelocity * 0.01F);
/* 339 */         this.dragonPartHead.onUpdate();
/* 340 */         this.dragonPartHead.setLocationAndAngles(this.posX + f18 * 5.5F * f2, this.posY + (adouble[1] - adouble1[1]) * 1.0D + f15 * 5.5F, this.posZ - f19 * 5.5F * f2, 0.0F, 0.0F);
/*     */         
/* 342 */         for (int j = 0; j < 3; j++)
/*     */         {
/* 344 */           EntityDragonPart entitydragonpart = null;
/*     */           
/* 346 */           if (j == 0)
/*     */           {
/* 348 */             entitydragonpart = this.dragonPartTail1;
/*     */           }
/*     */           
/* 351 */           if (j == 1)
/*     */           {
/* 353 */             entitydragonpart = this.dragonPartTail2;
/*     */           }
/*     */           
/* 356 */           if (j == 2)
/*     */           {
/* 358 */             entitydragonpart = this.dragonPartTail3;
/*     */           }
/*     */           
/* 361 */           double[] adouble2 = getMovementOffsets(12 + j * 2, 1.0F);
/* 362 */           float f20 = this.rotationYaw * 3.1415927F / 180.0F + simplifyAngle(adouble2[0] - adouble1[0]) * 3.1415927F / 180.0F * 1.0F;
/* 363 */           float f21 = MathHelper.sin(f20);
/* 364 */           float f22 = MathHelper.cos(f20);
/* 365 */           float f23 = 1.5F;
/* 366 */           float f24 = (j + 1) * 2.0F;
/* 367 */           entitydragonpart.onUpdate();
/* 368 */           entitydragonpart.setLocationAndAngles(this.posX - (f16 * f23 + f21 * f24) * f2, this.posY + (adouble2[1] - adouble1[1]) * 1.0D - (f24 + f23) * f15 + 1.5D, this.posZ + (f4 * f23 + f22 * f24) * f2, 0.0F, 0.0F);
/*     */         }
/*     */         
/* 371 */         if (!this.worldObj.isRemote)
/*     */         {
/* 373 */           this.slowed = (destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateDragonEnderCrystal()
/*     */   {
/* 384 */     if (this.healingEnderCrystal != null)
/*     */     {
/* 386 */       if (this.healingEnderCrystal.isDead)
/*     */       {
/* 388 */         if (!this.worldObj.isRemote)
/*     */         {
/* 390 */           attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource(null), 10.0F);
/*     */         }
/*     */         
/* 393 */         this.healingEnderCrystal = null;
/*     */       }
/* 395 */       else if ((this.ticksExisted % 10 == 0) && (getHealth() < getMaxHealth()))
/*     */       {
/* 397 */         setHealth(getHealth() + 1.0F);
/*     */       }
/*     */     }
/*     */     
/* 401 */     if (this.rand.nextInt(10) == 0)
/*     */     {
/* 403 */       float f = 32.0F;
/* 404 */       List<EntityEnderCrystal> list = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, getEntityBoundingBox().expand(f, f, f));
/* 405 */       EntityEnderCrystal entityendercrystal = null;
/* 406 */       double d0 = Double.MAX_VALUE;
/*     */       
/* 408 */       for (EntityEnderCrystal entityendercrystal1 : list)
/*     */       {
/* 410 */         double d1 = entityendercrystal1.getDistanceSqToEntity(this);
/*     */         
/* 412 */         if (d1 < d0)
/*     */         {
/* 414 */           d0 = d1;
/* 415 */           entityendercrystal = entityendercrystal1;
/*     */         }
/*     */       }
/*     */       
/* 419 */       this.healingEnderCrystal = entityendercrystal;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void collideWithEntities(List<Entity> p_70970_1_)
/*     */   {
/* 428 */     double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
/* 429 */     double d1 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;
/*     */     
/* 431 */     for (Entity entity : p_70970_1_)
/*     */     {
/* 433 */       if ((entity instanceof EntityLivingBase))
/*     */       {
/* 435 */         double d2 = entity.posX - d0;
/* 436 */         double d3 = entity.posZ - d1;
/* 437 */         double d4 = d2 * d2 + d3 * d3;
/* 438 */         entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void attackEntitiesInList(List<Entity> p_70971_1_)
/*     */   {
/* 448 */     for (int i = 0; i < p_70971_1_.size(); i++)
/*     */     {
/* 450 */       Entity entity = (Entity)p_70971_1_.get(i);
/*     */       
/* 452 */       if ((entity instanceof EntityLivingBase))
/*     */       {
/* 454 */         entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
/* 455 */         applyEnchantments(this, entity);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setNewTarget()
/*     */   {
/* 465 */     this.forceNewTarget = false;
/* 466 */     List<EntityPlayer> list = Lists.newArrayList(this.worldObj.playerEntities);
/* 467 */     Iterator<EntityPlayer> iterator = list.iterator();
/*     */     
/* 469 */     while (iterator.hasNext())
/*     */     {
/* 471 */       if (((EntityPlayer)iterator.next()).isSpectator())
/*     */       {
/* 473 */         iterator.remove();
/*     */       }
/*     */     }
/*     */     
/* 477 */     if ((this.rand.nextInt(2) == 0) && (!list.isEmpty()))
/*     */     {
/* 479 */       this.target = ((Entity)list.get(this.rand.nextInt(list.size())));
/*     */     }
/*     */     else
/*     */     {
/*     */       boolean flag;
/*     */       do {
/* 485 */         this.targetX = 0.0D;
/* 486 */         this.targetY = (70.0F + this.rand.nextFloat() * 50.0F);
/* 487 */         this.targetZ = 0.0D;
/* 488 */         this.targetX += this.rand.nextFloat() * 120.0F - 60.0F;
/* 489 */         this.targetZ += this.rand.nextFloat() * 120.0F - 60.0F;
/* 490 */         double d0 = this.posX - this.targetX;
/* 491 */         double d1 = this.posY - this.targetY;
/* 492 */         double d2 = this.posZ - this.targetZ;
/* 493 */         flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
/*     */       }
/* 495 */       while (!flag);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 501 */       this.target = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private float simplifyAngle(double p_70973_1_)
/*     */   {
/* 510 */     return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
/*     */   {
/* 518 */     int i = MathHelper.floor_double(p_70972_1_.minX);
/* 519 */     int j = MathHelper.floor_double(p_70972_1_.minY);
/* 520 */     int k = MathHelper.floor_double(p_70972_1_.minZ);
/* 521 */     int l = MathHelper.floor_double(p_70972_1_.maxX);
/* 522 */     int i1 = MathHelper.floor_double(p_70972_1_.maxY);
/* 523 */     int j1 = MathHelper.floor_double(p_70972_1_.maxZ);
/* 524 */     boolean flag = false;
/* 525 */     boolean flag1 = false;
/*     */     
/* 527 */     for (int k1 = i; k1 <= l; k1++)
/*     */     {
/* 529 */       for (int l1 = j; l1 <= i1; l1++)
/*     */       {
/* 531 */         for (int i2 = k; i2 <= j1; i2++)
/*     */         {
/* 533 */           BlockPos blockpos = new BlockPos(k1, l1, i2);
/* 534 */           Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */           
/* 536 */           if (block.getMaterial() != Material.air)
/*     */           {
/* 538 */             if ((block != Blocks.barrier) && (block != Blocks.obsidian) && (block != Blocks.end_stone) && (block != Blocks.bedrock) && (block != Blocks.command_block) && (this.worldObj.getGameRules().getBoolean("mobGriefing")))
/*     */             {
/* 540 */               flag1 = (this.worldObj.setBlockToAir(blockpos)) || (flag1);
/*     */             }
/*     */             else
/*     */             {
/* 544 */               flag = true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 551 */     if (flag1)
/*     */     {
/* 553 */       double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * this.rand.nextFloat();
/* 554 */       double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * this.rand.nextFloat();
/* 555 */       double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * this.rand.nextFloat();
/* 556 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     
/* 559 */     return flag;
/*     */   }
/*     */   
/*     */   public boolean attackEntityFromPart(EntityDragonPart dragonPart, DamageSource source, float p_70965_3_)
/*     */   {
/* 564 */     if (dragonPart != this.dragonPartHead)
/*     */     {
/* 566 */       p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
/*     */     }
/*     */     
/* 569 */     float f = this.rotationYaw * 3.1415927F / 180.0F;
/* 570 */     float f1 = MathHelper.sin(f);
/* 571 */     float f2 = MathHelper.cos(f);
/* 572 */     this.targetX = (this.posX + f1 * 5.0F + (this.rand.nextFloat() - 0.5F) * 2.0F);
/* 573 */     this.targetY = (this.posY + this.rand.nextFloat() * 3.0F + 1.0D);
/* 574 */     this.targetZ = (this.posZ - f2 * 5.0F + (this.rand.nextFloat() - 0.5F) * 2.0F);
/* 575 */     this.target = null;
/*     */     
/* 577 */     if (((source.getEntity() instanceof EntityPlayer)) || (source.isExplosion()))
/*     */     {
/* 579 */       attackDragonFrom(source, p_70965_3_);
/*     */     }
/*     */     
/* 582 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 590 */     if (((source instanceof EntityDamageSource)) && (((EntityDamageSource)source).getIsThornsDamage()))
/*     */     {
/* 592 */       attackDragonFrom(source, amount);
/*     */     }
/*     */     
/* 595 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean attackDragonFrom(DamageSource source, float amount)
/*     */   {
/* 603 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onKillCommand()
/*     */   {
/* 611 */     setDead();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onDeathUpdate()
/*     */   {
/* 619 */     this.deathTicks += 1;
/*     */     
/* 621 */     if ((this.deathTicks >= 180) && (this.deathTicks <= 200))
/*     */     {
/* 623 */       float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 624 */       float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
/* 625 */       float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 626 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     
/* 629 */     boolean flag = this.worldObj.getGameRules().getBoolean("doMobLoot");
/*     */     
/* 631 */     if (!this.worldObj.isRemote)
/*     */     {
/* 633 */       if ((this.deathTicks > 150) && (this.deathTicks % 5 == 0) && (flag))
/*     */       {
/* 635 */         int i = 1000;
/*     */         
/* 637 */         while (i > 0)
/*     */         {
/* 639 */           int k = EntityXPOrb.getXPSplit(i);
/* 640 */           i -= k;
/* 641 */           this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, k));
/*     */         }
/*     */       }
/*     */       
/* 645 */       if (this.deathTicks == 1)
/*     */       {
/* 647 */         this.worldObj.playBroadcastSound(1018, new BlockPos(this), 0);
/*     */       }
/*     */     }
/*     */     
/* 651 */     moveEntity(0.0D, 0.10000000149011612D, 0.0D);
/* 652 */     this.renderYawOffset = (this.rotationYaw += 20.0F);
/*     */     
/* 654 */     if ((this.deathTicks == 200) && (!this.worldObj.isRemote))
/*     */     {
/* 656 */       if (flag)
/*     */       {
/* 658 */         int j = 2000;
/*     */         
/* 660 */         while (j > 0)
/*     */         {
/* 662 */           int l = EntityXPOrb.getXPSplit(j);
/* 663 */           j -= l;
/* 664 */           this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, l));
/*     */         }
/*     */       }
/*     */       
/* 668 */       generatePortal(new BlockPos(this.posX, 64.0D, this.posZ));
/* 669 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void generatePortal(BlockPos pos)
/*     */   {
/* 678 */     int i = 4;
/* 679 */     double d0 = 12.25D;
/* 680 */     double d1 = 6.25D;
/*     */     
/* 682 */     for (int j = -1; j <= 32; j++)
/*     */     {
/* 684 */       for (int k = -4; k <= 4; k++)
/*     */       {
/* 686 */         for (int l = -4; l <= 4; l++)
/*     */         {
/* 688 */           double d2 = k * k + l * l;
/*     */           
/* 690 */           if (d2 <= 12.25D)
/*     */           {
/* 692 */             BlockPos blockpos = pos.add(k, j, l);
/*     */             
/* 694 */             if (j < 0)
/*     */             {
/* 696 */               if (d2 <= 6.25D)
/*     */               {
/* 698 */                 this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
/*     */               }
/*     */             }
/* 701 */             else if (j > 0)
/*     */             {
/* 703 */               this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState());
/*     */             }
/* 705 */             else if (d2 > 6.25D)
/*     */             {
/* 707 */               this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
/*     */             }
/*     */             else
/*     */             {
/* 711 */               this.worldObj.setBlockState(blockpos, Blocks.end_portal.getDefaultState());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 718 */     this.worldObj.setBlockState(pos, Blocks.bedrock.getDefaultState());
/* 719 */     this.worldObj.setBlockState(pos.up(), Blocks.bedrock.getDefaultState());
/* 720 */     BlockPos blockpos1 = pos.up(2);
/* 721 */     this.worldObj.setBlockState(blockpos1, Blocks.bedrock.getDefaultState());
/* 722 */     this.worldObj.setBlockState(blockpos1.west(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST));
/* 723 */     this.worldObj.setBlockState(blockpos1.east(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST));
/* 724 */     this.worldObj.setBlockState(blockpos1.north(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
/* 725 */     this.worldObj.setBlockState(blockpos1.south(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
/* 726 */     this.worldObj.setBlockState(pos.up(3), Blocks.bedrock.getDefaultState());
/* 727 */     this.worldObj.setBlockState(pos.up(4), Blocks.dragon_egg.getDefaultState());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void despawnEntity() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Entity[] getParts()
/*     */   {
/* 742 */     return this.dragonPartArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/* 750 */     return false;
/*     */   }
/*     */   
/*     */   public World getWorld()
/*     */   {
/* 755 */     return this.worldObj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 763 */     return "mob.enderdragon.growl";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 771 */     return "mob.enderdragon.hit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundVolume()
/*     */   {
/* 779 */     return 5.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\boss\EntityDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */