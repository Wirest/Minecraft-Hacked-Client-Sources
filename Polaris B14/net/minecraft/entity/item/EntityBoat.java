/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityBoat extends Entity
/*     */ {
/*     */   private boolean isBoatEmpty;
/*     */   private double speedMultiplier;
/*     */   private int boatPosRotationIncrements;
/*     */   private double boatX;
/*     */   private double boatY;
/*     */   private double boatZ;
/*     */   private double boatYaw;
/*     */   private double boatPitch;
/*     */   private double velocityX;
/*     */   private double velocityY;
/*     */   private double velocityZ;
/*     */   
/*     */   public EntityBoat(World worldIn)
/*     */   {
/*  38 */     super(worldIn);
/*  39 */     this.isBoatEmpty = true;
/*  40 */     this.speedMultiplier = 0.07D;
/*  41 */     this.preventEntitySpawning = true;
/*  42 */     setSize(1.5F, 0.6F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  56 */     this.dataWatcher.addObject(17, new Integer(0));
/*  57 */     this.dataWatcher.addObject(18, new Integer(1));
/*  58 */     this.dataWatcher.addObject(19, new Float(0.0F));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AxisAlignedBB getCollisionBox(Entity entityIn)
/*     */   {
/*  67 */     return entityIn.getEntityBoundingBox();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AxisAlignedBB getCollisionBoundingBox()
/*     */   {
/*  75 */     return getEntityBoundingBox();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBePushed()
/*     */   {
/*  83 */     return true;
/*     */   }
/*     */   
/*     */   public EntityBoat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_)
/*     */   {
/*  88 */     this(worldIn);
/*  89 */     setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
/*  90 */     this.motionX = 0.0D;
/*  91 */     this.motionY = 0.0D;
/*  92 */     this.motionZ = 0.0D;
/*  93 */     this.prevPosX = p_i1705_2_;
/*  94 */     this.prevPosY = p_i1705_4_;
/*  95 */     this.prevPosZ = p_i1705_6_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getMountedYOffset()
/*     */   {
/* 103 */     return -0.3D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 111 */     if (isEntityInvulnerable(source))
/*     */     {
/* 113 */       return false;
/*     */     }
/* 115 */     if ((!this.worldObj.isRemote) && (!this.isDead))
/*     */     {
/* 117 */       if ((this.riddenByEntity != null) && (this.riddenByEntity == source.getEntity()) && ((source instanceof net.minecraft.util.EntityDamageSourceIndirect)))
/*     */       {
/* 119 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 123 */       setForwardDirection(-getForwardDirection());
/* 124 */       setTimeSinceHit(10);
/* 125 */       setDamageTaken(getDamageTaken() + amount * 10.0F);
/* 126 */       setBeenAttacked();
/* 127 */       boolean flag = ((source.getEntity() instanceof EntityPlayer)) && (((EntityPlayer)source.getEntity()).capabilities.isCreativeMode);
/*     */       
/* 129 */       if ((flag) || (getDamageTaken() > 40.0F))
/*     */       {
/* 131 */         if (this.riddenByEntity != null)
/*     */         {
/* 133 */           this.riddenByEntity.mountEntity(this);
/*     */         }
/*     */         
/* 136 */         if ((!flag) && (this.worldObj.getGameRules().getBoolean("doEntityDrops")))
/*     */         {
/* 138 */           dropItemWithOffset(Items.boat, 1, 0.0F);
/*     */         }
/*     */         
/* 141 */         setDead();
/*     */       }
/*     */       
/* 144 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 149 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void performHurtAnimation()
/*     */   {
/* 158 */     setForwardDirection(-getForwardDirection());
/* 159 */     setTimeSinceHit(10);
/* 160 */     setDamageTaken(getDamageTaken() * 11.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/* 168 */     return !this.isDead;
/*     */   }
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
/*     */   {
/* 173 */     if ((p_180426_10_) && (this.riddenByEntity != null))
/*     */     {
/* 175 */       this.prevPosX = (this.posX = x);
/* 176 */       this.prevPosY = (this.posY = y);
/* 177 */       this.prevPosZ = (this.posZ = z);
/* 178 */       this.rotationYaw = yaw;
/* 179 */       this.rotationPitch = pitch;
/* 180 */       this.boatPosRotationIncrements = 0;
/* 181 */       setPosition(x, y, z);
/* 182 */       this.motionX = (this.velocityX = 0.0D);
/* 183 */       this.motionY = (this.velocityY = 0.0D);
/* 184 */       this.motionZ = (this.velocityZ = 0.0D);
/*     */     }
/*     */     else
/*     */     {
/* 188 */       if (this.isBoatEmpty)
/*     */       {
/* 190 */         this.boatPosRotationIncrements = (posRotationIncrements + 5);
/*     */       }
/*     */       else
/*     */       {
/* 194 */         double d0 = x - this.posX;
/* 195 */         double d1 = y - this.posY;
/* 196 */         double d2 = z - this.posZ;
/* 197 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*     */         
/* 199 */         if (d3 <= 1.0D)
/*     */         {
/* 201 */           return;
/*     */         }
/*     */         
/* 204 */         this.boatPosRotationIncrements = 3;
/*     */       }
/*     */       
/* 207 */       this.boatX = x;
/* 208 */       this.boatY = y;
/* 209 */       this.boatZ = z;
/* 210 */       this.boatYaw = yaw;
/* 211 */       this.boatPitch = pitch;
/* 212 */       this.motionX = this.velocityX;
/* 213 */       this.motionY = this.velocityY;
/* 214 */       this.motionZ = this.velocityZ;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVelocity(double x, double y, double z)
/*     */   {
/* 223 */     this.velocityX = (this.motionX = x);
/* 224 */     this.velocityY = (this.motionY = y);
/* 225 */     this.velocityZ = (this.motionZ = z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 233 */     super.onUpdate();
/*     */     
/* 235 */     if (getTimeSinceHit() > 0)
/*     */     {
/* 237 */       setTimeSinceHit(getTimeSinceHit() - 1);
/*     */     }
/*     */     
/* 240 */     if (getDamageTaken() > 0.0F)
/*     */     {
/* 242 */       setDamageTaken(getDamageTaken() - 1.0F);
/*     */     }
/*     */     
/* 245 */     this.prevPosX = this.posX;
/* 246 */     this.prevPosY = this.posY;
/* 247 */     this.prevPosZ = this.posZ;
/* 248 */     int i = 5;
/* 249 */     double d0 = 0.0D;
/*     */     
/* 251 */     for (int j = 0; j < i; j++)
/*     */     {
/* 253 */       double d1 = getEntityBoundingBox().minY + (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (j + 0) / i - 0.125D;
/* 254 */       double d3 = getEntityBoundingBox().minY + (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (j + 1) / i - 0.125D;
/* 255 */       AxisAlignedBB axisalignedbb = new AxisAlignedBB(getEntityBoundingBox().minX, d1, getEntityBoundingBox().minZ, getEntityBoundingBox().maxX, d3, getEntityBoundingBox().maxZ);
/*     */       
/* 257 */       if (this.worldObj.isAABBInMaterial(axisalignedbb, net.minecraft.block.material.Material.water))
/*     */       {
/* 259 */         d0 += 1.0D / i;
/*     */       }
/*     */     }
/*     */     
/* 263 */     double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */     
/* 265 */     if (d9 > 0.2975D)
/*     */     {
/* 267 */       double d2 = Math.cos(this.rotationYaw * 3.141592653589793D / 180.0D);
/* 268 */       double d4 = Math.sin(this.rotationYaw * 3.141592653589793D / 180.0D);
/*     */       
/* 270 */       for (int k = 0; k < 1.0D + d9 * 60.0D; k++)
/*     */       {
/* 272 */         double d5 = this.rand.nextFloat() * 2.0F - 1.0F;
/* 273 */         double d6 = (this.rand.nextInt(2) * 2 - 1) * 0.7D;
/*     */         
/* 275 */         if (this.rand.nextBoolean())
/*     */         {
/* 277 */           double d7 = this.posX - d2 * d5 * 0.8D + d4 * d6;
/* 278 */           double d8 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
/* 279 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.posY - 0.125D, d8, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         }
/*     */         else
/*     */         {
/* 283 */           double d24 = this.posX + d2 + d4 * d5 * 0.7D;
/* 284 */           double d25 = this.posZ + d4 - d2 * d5 * 0.7D;
/* 285 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d24, this.posY - 0.125D, d25, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 290 */     if ((this.worldObj.isRemote) && (this.isBoatEmpty))
/*     */     {
/* 292 */       if (this.boatPosRotationIncrements > 0)
/*     */       {
/* 294 */         double d12 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
/* 295 */         double d16 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
/* 296 */         double d19 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
/* 297 */         double d22 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
/* 298 */         this.rotationYaw = ((float)(this.rotationYaw + d22 / this.boatPosRotationIncrements));
/* 299 */         this.rotationPitch = ((float)(this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements));
/* 300 */         this.boatPosRotationIncrements -= 1;
/* 301 */         setPosition(d12, d16, d19);
/* 302 */         setRotation(this.rotationYaw, this.rotationPitch);
/*     */       }
/*     */       else
/*     */       {
/* 306 */         double d13 = this.posX + this.motionX;
/* 307 */         double d17 = this.posY + this.motionY;
/* 308 */         double d20 = this.posZ + this.motionZ;
/* 309 */         setPosition(d13, d17, d20);
/*     */         
/* 311 */         if (this.onGround)
/*     */         {
/* 313 */           this.motionX *= 0.5D;
/* 314 */           this.motionY *= 0.5D;
/* 315 */           this.motionZ *= 0.5D;
/*     */         }
/*     */         
/* 318 */         this.motionX *= 0.9900000095367432D;
/* 319 */         this.motionY *= 0.949999988079071D;
/* 320 */         this.motionZ *= 0.9900000095367432D;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 325 */       if (d0 < 1.0D)
/*     */       {
/* 327 */         double d10 = d0 * 2.0D - 1.0D;
/* 328 */         this.motionY += 0.03999999910593033D * d10;
/*     */       }
/*     */       else
/*     */       {
/* 332 */         if (this.motionY < 0.0D)
/*     */         {
/* 334 */           this.motionY /= 2.0D;
/*     */         }
/*     */         
/* 337 */         this.motionY += 0.007000000216066837D;
/*     */       }
/*     */       
/* 340 */       if ((this.riddenByEntity instanceof EntityLivingBase))
/*     */       {
/* 342 */         EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
/* 343 */         float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
/* 344 */         this.motionX += -Math.sin(f * 3.1415927F / 180.0F) * this.speedMultiplier * entitylivingbase.moveForward * 0.05000000074505806D;
/* 345 */         this.motionZ += Math.cos(f * 3.1415927F / 180.0F) * this.speedMultiplier * entitylivingbase.moveForward * 0.05000000074505806D;
/*     */       }
/*     */       
/* 348 */       double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */       
/* 350 */       if (d11 > 0.35D)
/*     */       {
/* 352 */         double d14 = 0.35D / d11;
/* 353 */         this.motionX *= d14;
/* 354 */         this.motionZ *= d14;
/* 355 */         d11 = 0.35D;
/*     */       }
/*     */       
/* 358 */       if ((d11 > d9) && (this.speedMultiplier < 0.35D))
/*     */       {
/* 360 */         this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
/*     */         
/* 362 */         if (this.speedMultiplier > 0.35D)
/*     */         {
/* 364 */           this.speedMultiplier = 0.35D;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 369 */         this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
/*     */         
/* 371 */         if (this.speedMultiplier < 0.07D)
/*     */         {
/* 373 */           this.speedMultiplier = 0.07D;
/*     */         }
/*     */       }
/*     */       
/* 377 */       for (int i1 = 0; i1 < 4; i1++)
/*     */       {
/* 379 */         int l1 = MathHelper.floor_double(this.posX + (i1 % 2 - 0.5D) * 0.8D);
/* 380 */         int i2 = MathHelper.floor_double(this.posZ + (i1 / 2 - 0.5D) * 0.8D);
/*     */         
/* 382 */         for (int j2 = 0; j2 < 2; j2++)
/*     */         {
/* 384 */           int l = MathHelper.floor_double(this.posY) + j2;
/* 385 */           BlockPos blockpos = new BlockPos(l1, l, i2);
/* 386 */           Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */           
/* 388 */           if (block == Blocks.snow_layer)
/*     */           {
/* 390 */             this.worldObj.setBlockToAir(blockpos);
/* 391 */             this.isCollidedHorizontally = false;
/*     */           }
/* 393 */           else if (block == Blocks.waterlily)
/*     */           {
/* 395 */             this.worldObj.destroyBlock(blockpos, true);
/* 396 */             this.isCollidedHorizontally = false;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 401 */       if (this.onGround)
/*     */       {
/* 403 */         this.motionX *= 0.5D;
/* 404 */         this.motionY *= 0.5D;
/* 405 */         this.motionZ *= 0.5D;
/*     */       }
/*     */       
/* 408 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */       
/* 410 */       if ((this.isCollidedHorizontally) && (d9 > 0.2975D))
/*     */       {
/* 412 */         if ((!this.worldObj.isRemote) && (!this.isDead))
/*     */         {
/* 414 */           setDead();
/*     */           
/* 416 */           if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */           {
/* 418 */             for (int j1 = 0; j1 < 3; j1++)
/*     */             {
/* 420 */               dropItemWithOffset(net.minecraft.item.Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
/*     */             }
/*     */             
/* 423 */             for (int k1 = 0; k1 < 2; k1++)
/*     */             {
/* 425 */               dropItemWithOffset(Items.stick, 1, 0.0F);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 432 */         this.motionX *= 0.9900000095367432D;
/* 433 */         this.motionY *= 0.949999988079071D;
/* 434 */         this.motionZ *= 0.9900000095367432D;
/*     */       }
/*     */       
/* 437 */       this.rotationPitch = 0.0F;
/* 438 */       double d15 = this.rotationYaw;
/* 439 */       double d18 = this.prevPosX - this.posX;
/* 440 */       double d21 = this.prevPosZ - this.posZ;
/*     */       
/* 442 */       if (d18 * d18 + d21 * d21 > 0.001D)
/*     */       {
/* 444 */         d15 = (float)(MathHelper.func_181159_b(d21, d18) * 180.0D / 3.141592653589793D);
/*     */       }
/*     */       
/* 447 */       double d23 = MathHelper.wrapAngleTo180_double(d15 - this.rotationYaw);
/*     */       
/* 449 */       if (d23 > 20.0D)
/*     */       {
/* 451 */         d23 = 20.0D;
/*     */       }
/*     */       
/* 454 */       if (d23 < -20.0D)
/*     */       {
/* 456 */         d23 = -20.0D;
/*     */       }
/*     */       
/* 459 */       this.rotationYaw = ((float)(this.rotationYaw + d23));
/* 460 */       setRotation(this.rotationYaw, this.rotationPitch);
/*     */       
/* 462 */       if (!this.worldObj.isRemote)
/*     */       {
/* 464 */         List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
/*     */         
/* 466 */         if ((list != null) && (!list.isEmpty()))
/*     */         {
/* 468 */           for (int k2 = 0; k2 < list.size(); k2++)
/*     */           {
/* 470 */             Entity entity = (Entity)list.get(k2);
/*     */             
/* 472 */             if ((entity != this.riddenByEntity) && (entity.canBePushed()) && ((entity instanceof EntityBoat)))
/*     */             {
/* 474 */               entity.applyEntityCollision(this);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 479 */         if ((this.riddenByEntity != null) && (this.riddenByEntity.isDead))
/*     */         {
/* 481 */           this.riddenByEntity = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateRiderPosition()
/*     */   {
/* 489 */     if (this.riddenByEntity != null)
/*     */     {
/* 491 */       double d0 = Math.cos(this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
/* 492 */       double d1 = Math.sin(this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
/* 493 */       this.riddenByEntity.setPosition(this.posX + d0, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interactFirst(EntityPlayer playerIn)
/*     */   {
/* 516 */     if ((this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityPlayer)) && (this.riddenByEntity != playerIn))
/*     */     {
/* 518 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 522 */     if (!this.worldObj.isRemote)
/*     */     {
/* 524 */       playerIn.mountEntity(this);
/*     */     }
/*     */     
/* 527 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos)
/*     */   {
/* 533 */     if (onGroundIn)
/*     */     {
/* 535 */       if (this.fallDistance > 3.0F)
/*     */       {
/* 537 */         fall(this.fallDistance, 1.0F);
/*     */         
/* 539 */         if ((!this.worldObj.isRemote) && (!this.isDead))
/*     */         {
/* 541 */           setDead();
/*     */           
/* 543 */           if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */           {
/* 545 */             for (int i = 0; i < 3; i++)
/*     */             {
/* 547 */               dropItemWithOffset(net.minecraft.item.Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
/*     */             }
/*     */             
/* 550 */             for (int j = 0; j < 2; j++)
/*     */             {
/* 552 */               dropItemWithOffset(Items.stick, 1, 0.0F);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 557 */         this.fallDistance = 0.0F;
/*     */       }
/*     */     }
/* 560 */     else if ((this.worldObj.getBlockState(new BlockPos(this).down()).getBlock().getMaterial() != net.minecraft.block.material.Material.water) && (y < 0.0D))
/*     */     {
/* 562 */       this.fallDistance = ((float)(this.fallDistance - y));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDamageTaken(float p_70266_1_)
/*     */   {
/* 571 */     this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getDamageTaken()
/*     */   {
/* 579 */     return this.dataWatcher.getWatchableObjectFloat(19);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeSinceHit(int p_70265_1_)
/*     */   {
/* 587 */     this.dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTimeSinceHit()
/*     */   {
/* 595 */     return this.dataWatcher.getWatchableObjectInt(17);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setForwardDirection(int p_70269_1_)
/*     */   {
/* 603 */     this.dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getForwardDirection()
/*     */   {
/* 611 */     return this.dataWatcher.getWatchableObjectInt(18);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIsBoatEmpty(boolean p_70270_1_)
/*     */   {
/* 619 */     this.isBoatEmpty = p_70270_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */