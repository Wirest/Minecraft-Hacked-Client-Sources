/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityArrow extends Entity implements net.minecraft.entity.IProjectile
/*     */ {
/*  30 */   private int xTile = -1;
/*  31 */   private int yTile = -1;
/*  32 */   private int zTile = -1;
/*     */   
/*     */   private Block inTile;
/*     */   
/*     */   private int inData;
/*     */   
/*     */   private boolean inGround;
/*     */   
/*     */   public int canBePickedUp;
/*     */   
/*     */   public int arrowShake;
/*     */   
/*     */   public Entity shootingEntity;
/*     */   private int ticksInGround;
/*     */   private int ticksInAir;
/*  47 */   private double damage = 2.0D;
/*     */   
/*     */   private int knockbackStrength;
/*     */   
/*     */ 
/*     */   public EntityArrow(World worldIn)
/*     */   {
/*  54 */     super(worldIn);
/*  55 */     this.renderDistanceWeight = 10.0D;
/*  56 */     setSize(0.5F, 0.5F);
/*     */   }
/*     */   
/*     */   public EntityArrow(World worldIn, double x, double y, double z)
/*     */   {
/*  61 */     super(worldIn);
/*  62 */     this.renderDistanceWeight = 10.0D;
/*  63 */     setSize(0.5F, 0.5F);
/*  64 */     setPosition(x, y, z);
/*     */   }
/*     */   
/*     */   public EntityArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_)
/*     */   {
/*  69 */     super(worldIn);
/*  70 */     this.renderDistanceWeight = 10.0D;
/*  71 */     this.shootingEntity = shooter;
/*     */     
/*  73 */     if ((shooter instanceof EntityPlayer))
/*     */     {
/*  75 */       this.canBePickedUp = 1;
/*     */     }
/*     */     
/*  78 */     this.posY = (shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D);
/*  79 */     double d0 = p_i1755_3_.posX - shooter.posX;
/*  80 */     double d1 = p_i1755_3_.getEntityBoundingBox().minY + p_i1755_3_.height / 3.0F - this.posY;
/*  81 */     double d2 = p_i1755_3_.posZ - shooter.posZ;
/*  82 */     double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
/*     */     
/*  84 */     if (d3 >= 1.0E-7D)
/*     */     {
/*  86 */       float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
/*  87 */       float f1 = (float)-(MathHelper.func_181159_b(d1, d3) * 180.0D / 3.141592653589793D);
/*  88 */       double d4 = d0 / d3;
/*  89 */       double d5 = d2 / d3;
/*  90 */       setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f, f1);
/*  91 */       float f2 = (float)(d3 * 0.20000000298023224D);
/*  92 */       setThrowableHeading(d0, d1 + f2, d2, p_i1755_4_, p_i1755_5_);
/*     */     }
/*     */   }
/*     */   
/*     */   public EntityArrow(World worldIn, EntityLivingBase shooter, float velocity)
/*     */   {
/*  98 */     super(worldIn);
/*  99 */     this.renderDistanceWeight = 10.0D;
/* 100 */     this.shootingEntity = shooter;
/*     */     
/* 102 */     if ((shooter instanceof EntityPlayer))
/*     */     {
/* 104 */       this.canBePickedUp = 1;
/*     */     }
/*     */     
/* 107 */     setSize(0.5F, 0.5F);
/* 108 */     setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
/* 109 */     this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
/* 110 */     this.posY -= 0.10000000149011612D;
/* 111 */     this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
/* 112 */     setPosition(this.posX, this.posY, this.posZ);
/* 113 */     this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
/* 114 */     this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
/* 115 */     this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F));
/* 116 */     setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 121 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
/*     */   {
/* 129 */     float f = MathHelper.sqrt_double(x * x + y * y + z * z);
/* 130 */     x /= f;
/* 131 */     y /= f;
/* 132 */     z /= f;
/* 133 */     x += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
/* 134 */     y += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
/* 135 */     z += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
/* 136 */     x *= velocity;
/* 137 */     y *= velocity;
/* 138 */     z *= velocity;
/* 139 */     this.motionX = x;
/* 140 */     this.motionY = y;
/* 141 */     this.motionZ = z;
/* 142 */     float f1 = MathHelper.sqrt_double(x * x + z * z);
/* 143 */     this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / 3.141592653589793D));
/* 144 */     this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(y, f1) * 180.0D / 3.141592653589793D));
/* 145 */     this.ticksInGround = 0;
/*     */   }
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
/*     */   {
/* 150 */     setPosition(x, y, z);
/* 151 */     setRotation(yaw, pitch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVelocity(double x, double y, double z)
/*     */   {
/* 159 */     this.motionX = x;
/* 160 */     this.motionY = y;
/* 161 */     this.motionZ = z;
/*     */     
/* 163 */     if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
/*     */     {
/* 165 */       float f = MathHelper.sqrt_double(x * x + z * z);
/* 166 */       this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / 3.141592653589793D));
/* 167 */       this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(y, f) * 180.0D / 3.141592653589793D));
/* 168 */       this.prevRotationPitch = this.rotationPitch;
/* 169 */       this.prevRotationYaw = this.rotationYaw;
/* 170 */       setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/* 171 */       this.ticksInGround = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 180 */     super.onUpdate();
/*     */     
/* 182 */     if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
/*     */     {
/* 184 */       float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 185 */       this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
/* 186 */       this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f) * 180.0D / 3.141592653589793D));
/*     */     }
/*     */     
/* 189 */     BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
/* 190 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 191 */     Block block = iblockstate.getBlock();
/*     */     
/* 193 */     if (block.getMaterial() != Material.air)
/*     */     {
/* 195 */       block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
/* 196 */       AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);
/*     */       
/* 198 */       if ((axisalignedbb != null) && (axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ))))
/*     */       {
/* 200 */         this.inGround = true;
/*     */       }
/*     */     }
/*     */     
/* 204 */     if (this.arrowShake > 0)
/*     */     {
/* 206 */       this.arrowShake -= 1;
/*     */     }
/*     */     
/* 209 */     if (this.inGround)
/*     */     {
/* 211 */       int j = block.getMetaFromState(iblockstate);
/*     */       
/* 213 */       if ((block == this.inTile) && (j == this.inData))
/*     */       {
/* 215 */         this.ticksInGround += 1;
/*     */         
/* 217 */         if (this.ticksInGround >= 1200)
/*     */         {
/* 219 */           setDead();
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 224 */         this.inGround = false;
/* 225 */         this.motionX *= this.rand.nextFloat() * 0.2F;
/* 226 */         this.motionY *= this.rand.nextFloat() * 0.2F;
/* 227 */         this.motionZ *= this.rand.nextFloat() * 0.2F;
/* 228 */         this.ticksInGround = 0;
/* 229 */         this.ticksInAir = 0;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 234 */       this.ticksInAir += 1;
/* 235 */       Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 236 */       Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 237 */       MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
/* 238 */       vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 239 */       vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */       
/* 241 */       if (movingobjectposition != null)
/*     */       {
/* 243 */         vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */       }
/*     */       
/* 246 */       Entity entity = null;
/* 247 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 248 */       double d0 = 0.0D;
/*     */       
/* 250 */       for (int i = 0; i < list.size(); i++)
/*     */       {
/* 252 */         Entity entity1 = (Entity)list.get(i);
/*     */         
/* 254 */         if ((entity1.canBeCollidedWith()) && ((entity1 != this.shootingEntity) || (this.ticksInAir >= 5)))
/*     */         {
/* 256 */           float f1 = 0.3F;
/* 257 */           AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand(f1, f1, f1);
/* 258 */           MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
/*     */           
/* 260 */           if (movingobjectposition1 != null)
/*     */           {
/* 262 */             double d1 = vec31.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 264 */             if ((d1 < d0) || (d0 == 0.0D))
/*     */             {
/* 266 */               entity = entity1;
/* 267 */               d0 = d1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 273 */       if (entity != null)
/*     */       {
/* 275 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */       
/* 278 */       if ((movingobjectposition != null) && (movingobjectposition.entityHit != null) && ((movingobjectposition.entityHit instanceof EntityPlayer)))
/*     */       {
/* 280 */         EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;
/*     */         
/* 282 */         if ((entityplayer.capabilities.disableDamage) || (((this.shootingEntity instanceof EntityPlayer)) && (!((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))))
/*     */         {
/* 284 */           movingobjectposition = null;
/*     */         }
/*     */       }
/*     */       
/* 288 */       if (movingobjectposition != null)
/*     */       {
/* 290 */         if (movingobjectposition.entityHit != null)
/*     */         {
/* 292 */           float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
/* 293 */           int l = MathHelper.ceiling_double_int(f2 * this.damage);
/*     */           
/* 295 */           if (getIsCritical())
/*     */           {
/* 297 */             l += this.rand.nextInt(l / 2 + 2);
/*     */           }
/*     */           
/*     */           DamageSource damagesource;
/*     */           DamageSource damagesource;
/* 302 */           if (this.shootingEntity == null)
/*     */           {
/* 304 */             damagesource = DamageSource.causeArrowDamage(this, this);
/*     */           }
/*     */           else
/*     */           {
/* 308 */             damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
/*     */           }
/*     */           
/* 311 */           if ((isBurning()) && (!(movingobjectposition.entityHit instanceof EntityEnderman)))
/*     */           {
/* 313 */             movingobjectposition.entityHit.setFire(5);
/*     */           }
/*     */           
/* 316 */           if (movingobjectposition.entityHit.attackEntityFrom(damagesource, l))
/*     */           {
/* 318 */             if ((movingobjectposition.entityHit instanceof EntityLivingBase))
/*     */             {
/* 320 */               EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;
/*     */               
/* 322 */               if (!this.worldObj.isRemote)
/*     */               {
/* 324 */                 entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
/*     */               }
/*     */               
/* 327 */               if (this.knockbackStrength > 0)
/*     */               {
/* 329 */                 float f7 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */                 
/* 331 */                 if (f7 > 0.0F)
/*     */                 {
/* 333 */                   movingobjectposition.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f7, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f7);
/*     */                 }
/*     */               }
/*     */               
/* 337 */               if ((this.shootingEntity instanceof EntityLivingBase))
/*     */               {
/* 339 */                 EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
/* 340 */                 EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
/*     */               }
/*     */               
/* 343 */               if ((this.shootingEntity != null) && (movingobjectposition.entityHit != this.shootingEntity) && ((movingobjectposition.entityHit instanceof EntityPlayer)) && ((this.shootingEntity instanceof EntityPlayerMP)))
/*     */               {
/* 345 */                 ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S2BPacketChangeGameState(6, 0.0F));
/*     */               }
/*     */             }
/*     */             
/* 349 */             playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
/*     */             
/* 351 */             if (!(movingobjectposition.entityHit instanceof EntityEnderman))
/*     */             {
/* 353 */               setDead();
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 358 */             this.motionX *= -0.10000000149011612D;
/* 359 */             this.motionY *= -0.10000000149011612D;
/* 360 */             this.motionZ *= -0.10000000149011612D;
/* 361 */             this.rotationYaw += 180.0F;
/* 362 */             this.prevRotationYaw += 180.0F;
/* 363 */             this.ticksInAir = 0;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 368 */           BlockPos blockpos1 = movingobjectposition.getBlockPos();
/* 369 */           this.xTile = blockpos1.getX();
/* 370 */           this.yTile = blockpos1.getY();
/* 371 */           this.zTile = blockpos1.getZ();
/* 372 */           IBlockState iblockstate1 = this.worldObj.getBlockState(blockpos1);
/* 373 */           this.inTile = iblockstate1.getBlock();
/* 374 */           this.inData = this.inTile.getMetaFromState(iblockstate1);
/* 375 */           this.motionX = ((float)(movingobjectposition.hitVec.xCoord - this.posX));
/* 376 */           this.motionY = ((float)(movingobjectposition.hitVec.yCoord - this.posY));
/* 377 */           this.motionZ = ((float)(movingobjectposition.hitVec.zCoord - this.posZ));
/* 378 */           float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
/* 379 */           this.posX -= this.motionX / f5 * 0.05000000074505806D;
/* 380 */           this.posY -= this.motionY / f5 * 0.05000000074505806D;
/* 381 */           this.posZ -= this.motionZ / f5 * 0.05000000074505806D;
/* 382 */           playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
/* 383 */           this.inGround = true;
/* 384 */           this.arrowShake = 7;
/* 385 */           setIsCritical(false);
/*     */           
/* 387 */           if (this.inTile.getMaterial() != Material.air)
/*     */           {
/* 389 */             this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate1, this);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 394 */       if (getIsCritical())
/*     */       {
/* 396 */         for (int k = 0; k < 4; k++)
/*     */         {
/* 398 */           this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * k / 4.0D, this.posY + this.motionY * k / 4.0D, this.posZ + this.motionZ * k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
/*     */         }
/*     */       }
/*     */       
/* 402 */       this.posX += this.motionX;
/* 403 */       this.posY += this.motionY;
/* 404 */       this.posZ += this.motionZ;
/* 405 */       float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 406 */       this.rotationYaw = ((float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
/*     */       
/* 408 */       for (this.rotationPitch = ((float)(MathHelper.func_181159_b(this.motionY, f3) * 180.0D / 3.141592653589793D)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 413 */       while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */       {
/* 415 */         this.prevRotationPitch += 360.0F;
/*     */       }
/*     */       
/* 418 */       while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */       {
/* 420 */         this.prevRotationYaw -= 360.0F;
/*     */       }
/*     */       
/* 423 */       while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */       {
/* 425 */         this.prevRotationYaw += 360.0F;
/*     */       }
/*     */       
/* 428 */       this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
/* 429 */       this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
/* 430 */       float f4 = 0.99F;
/* 431 */       float f6 = 0.05F;
/*     */       
/* 433 */       if (isInWater())
/*     */       {
/* 435 */         for (int i1 = 0; i1 < 4; i1++)
/*     */         {
/* 437 */           float f8 = 0.25F;
/* 438 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f8, this.posY - this.motionY * f8, this.posZ - this.motionZ * f8, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         }
/*     */         
/* 441 */         f4 = 0.6F;
/*     */       }
/*     */       
/* 444 */       if (isWet())
/*     */       {
/* 446 */         extinguish();
/*     */       }
/*     */       
/* 449 */       this.motionX *= f4;
/* 450 */       this.motionY *= f4;
/* 451 */       this.motionZ *= f4;
/* 452 */       this.motionY -= f6;
/* 453 */       setPosition(this.posX, this.posY, this.posZ);
/* 454 */       doBlockCollisions();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 463 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 464 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 465 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 466 */     tagCompound.setShort("life", (short)this.ticksInGround);
/* 467 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 468 */     tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
/* 469 */     tagCompound.setByte("inData", (byte)this.inData);
/* 470 */     tagCompound.setByte("shake", (byte)this.arrowShake);
/* 471 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/* 472 */     tagCompound.setByte("pickup", (byte)this.canBePickedUp);
/* 473 */     tagCompound.setDouble("damage", this.damage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 481 */     this.xTile = tagCompund.getShort("xTile");
/* 482 */     this.yTile = tagCompund.getShort("yTile");
/* 483 */     this.zTile = tagCompund.getShort("zTile");
/* 484 */     this.ticksInGround = tagCompund.getShort("life");
/*     */     
/* 486 */     if (tagCompund.hasKey("inTile", 8))
/*     */     {
/* 488 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else
/*     */     {
/* 492 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     }
/*     */     
/* 495 */     this.inData = (tagCompund.getByte("inData") & 0xFF);
/* 496 */     this.arrowShake = (tagCompund.getByte("shake") & 0xFF);
/* 497 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/*     */     
/* 499 */     if (tagCompund.hasKey("damage", 99))
/*     */     {
/* 501 */       this.damage = tagCompund.getDouble("damage");
/*     */     }
/*     */     
/* 504 */     if (tagCompund.hasKey("pickup", 99))
/*     */     {
/* 506 */       this.canBePickedUp = tagCompund.getByte("pickup");
/*     */     }
/* 508 */     else if (tagCompund.hasKey("player", 99))
/*     */     {
/* 510 */       this.canBePickedUp = (tagCompund.getBoolean("player") ? 1 : 0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn)
/*     */   {
/* 519 */     if ((!this.worldObj.isRemote) && (this.inGround) && (this.arrowShake <= 0))
/*     */     {
/* 521 */       boolean flag = (this.canBePickedUp == 1) || ((this.canBePickedUp == 2) && (entityIn.capabilities.isCreativeMode));
/*     */       
/* 523 */       if ((this.canBePickedUp == 1) && (!entityIn.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Items.arrow, 1))))
/*     */       {
/* 525 */         flag = false;
/*     */       }
/*     */       
/* 528 */       if (flag)
/*     */       {
/* 530 */         playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/* 531 */         entityIn.onItemPickup(this, 1);
/* 532 */         setDead();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/* 543 */     return false;
/*     */   }
/*     */   
/*     */   public void setDamage(double damageIn)
/*     */   {
/* 548 */     this.damage = damageIn;
/*     */   }
/*     */   
/*     */   public double getDamage()
/*     */   {
/* 553 */     return this.damage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKnockbackStrength(int knockbackStrengthIn)
/*     */   {
/* 561 */     this.knockbackStrength = knockbackStrengthIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canAttackWithItem()
/*     */   {
/* 569 */     return false;
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 574 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIsCritical(boolean critical)
/*     */   {
/* 582 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 584 */     if (critical)
/*     */     {
/* 586 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else
/*     */     {
/* 590 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getIsCritical()
/*     */   {
/* 599 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/* 600 */     return (b0 & 0x1) != 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntityArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */