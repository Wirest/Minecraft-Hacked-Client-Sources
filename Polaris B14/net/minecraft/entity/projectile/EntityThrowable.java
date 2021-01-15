/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public abstract class EntityThrowable extends Entity implements IProjectile
/*     */ {
/*  24 */   private int xTile = -1;
/*  25 */   private int yTile = -1;
/*  26 */   private int zTile = -1;
/*     */   
/*     */   private Block inTile;
/*     */   
/*     */   protected boolean inGround;
/*     */   public int throwableShake;
/*     */   private EntityLivingBase thrower;
/*     */   private String throwerName;
/*     */   private int ticksInGround;
/*     */   private int ticksInAir;
/*     */   
/*     */   public EntityThrowable(World worldIn)
/*     */   {
/*  39 */     super(worldIn);
/*  40 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void entityInit() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInRangeToRenderDist(double distance)
/*     */   {
/*  53 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/*  55 */     if (Double.isNaN(d0))
/*     */     {
/*  57 */       d0 = 4.0D;
/*     */     }
/*     */     
/*  60 */     d0 *= 64.0D;
/*  61 */     return distance < d0 * d0;
/*     */   }
/*     */   
/*     */   public EntityThrowable(World worldIn, EntityLivingBase throwerIn)
/*     */   {
/*  66 */     super(worldIn);
/*  67 */     this.thrower = throwerIn;
/*  68 */     setSize(0.25F, 0.25F);
/*  69 */     setLocationAndAngles(throwerIn.posX, throwerIn.posY + throwerIn.getEyeHeight(), throwerIn.posZ, throwerIn.rotationYaw, throwerIn.rotationPitch);
/*  70 */     this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
/*  71 */     this.posY -= 0.10000000149011612D;
/*  72 */     this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
/*  73 */     setPosition(this.posX, this.posY, this.posZ);
/*  74 */     float f = 0.4F;
/*  75 */     this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/*  76 */     this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/*  77 */     this.motionY = (-MathHelper.sin((this.rotationPitch + getInaccuracy()) / 180.0F * 3.1415927F) * f);
/*  78 */     setThrowableHeading(this.motionX, this.motionY, this.motionZ, getVelocity(), 1.0F);
/*     */   }
/*     */   
/*     */   public EntityThrowable(World worldIn, double x, double y, double z)
/*     */   {
/*  83 */     super(worldIn);
/*  84 */     this.ticksInGround = 0;
/*  85 */     setSize(0.25F, 0.25F);
/*  86 */     setPosition(x, y, z);
/*     */   }
/*     */   
/*     */   protected float getVelocity()
/*     */   {
/*  91 */     return 1.5F;
/*     */   }
/*     */   
/*     */   protected float getInaccuracy()
/*     */   {
/*  96 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
/*     */   {
/* 104 */     float f = MathHelper.sqrt_double(x * x + y * y + z * z);
/* 105 */     x /= f;
/* 106 */     y /= f;
/* 107 */     z /= f;
/* 108 */     x += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
/* 109 */     y += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
/* 110 */     z += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
/* 111 */     x *= velocity;
/* 112 */     y *= velocity;
/* 113 */     z *= velocity;
/* 114 */     this.motionX = x;
/* 115 */     this.motionY = y;
/* 116 */     this.motionZ = z;
/* 117 */     float f1 = MathHelper.sqrt_double(x * x + z * z);
/* 118 */     this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / 3.141592653589793D));
/* 119 */     this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(y, f1) * 180.0D / 3.141592653589793D));
/* 120 */     this.ticksInGround = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVelocity(double x, double y, double z)
/*     */   {
/* 128 */     this.motionX = x;
/* 129 */     this.motionY = y;
/* 130 */     this.motionZ = z;
/*     */     
/* 132 */     if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
/*     */     {
/* 134 */       float f = MathHelper.sqrt_double(x * x + z * z);
/* 135 */       this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / 3.141592653589793D));
/* 136 */       this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(y, f) * 180.0D / 3.141592653589793D));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 145 */     this.lastTickPosX = this.posX;
/* 146 */     this.lastTickPosY = this.posY;
/* 147 */     this.lastTickPosZ = this.posZ;
/* 148 */     super.onUpdate();
/*     */     
/* 150 */     if (this.throwableShake > 0)
/*     */     {
/* 152 */       this.throwableShake -= 1;
/*     */     }
/*     */     
/* 155 */     if (this.inGround)
/*     */     {
/* 157 */       if (this.worldObj.getBlockState(new net.minecraft.util.BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
/*     */       {
/* 159 */         this.ticksInGround += 1;
/*     */         
/* 161 */         if (this.ticksInGround == 1200)
/*     */         {
/* 163 */           setDead();
/*     */         }
/*     */         
/* 166 */         return;
/*     */       }
/*     */       
/* 169 */       this.inGround = false;
/* 170 */       this.motionX *= this.rand.nextFloat() * 0.2F;
/* 171 */       this.motionY *= this.rand.nextFloat() * 0.2F;
/* 172 */       this.motionZ *= this.rand.nextFloat() * 0.2F;
/* 173 */       this.ticksInGround = 0;
/* 174 */       this.ticksInAir = 0;
/*     */     }
/*     */     else
/*     */     {
/* 178 */       this.ticksInAir += 1;
/*     */     }
/*     */     
/* 181 */     Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 182 */     Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 183 */     MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
/* 184 */     vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 185 */     vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */     
/* 187 */     if (movingobjectposition != null)
/*     */     {
/* 189 */       vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */     }
/*     */     
/* 192 */     if (!this.worldObj.isRemote)
/*     */     {
/* 194 */       Entity entity = null;
/* 195 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 196 */       double d0 = 0.0D;
/* 197 */       EntityLivingBase entitylivingbase = getThrower();
/*     */       
/* 199 */       for (int j = 0; j < list.size(); j++)
/*     */       {
/* 201 */         Entity entity1 = (Entity)list.get(j);
/*     */         
/* 203 */         if ((entity1.canBeCollidedWith()) && ((entity1 != entitylivingbase) || (this.ticksInAir >= 5)))
/*     */         {
/* 205 */           float f = 0.3F;
/* 206 */           AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
/* 207 */           MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
/*     */           
/* 209 */           if (movingobjectposition1 != null)
/*     */           {
/* 211 */             double d1 = vec3.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 213 */             if ((d1 < d0) || (d0 == 0.0D))
/*     */             {
/* 215 */               entity = entity1;
/* 216 */               d0 = d1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 222 */       if (entity != null)
/*     */       {
/* 224 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */     }
/*     */     
/* 228 */     if (movingobjectposition != null)
/*     */     {
/* 230 */       if ((movingobjectposition.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK) && (this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == net.minecraft.init.Blocks.portal))
/*     */       {
/* 232 */         func_181015_d(movingobjectposition.getBlockPos());
/*     */       }
/*     */       else
/*     */       {
/* 236 */         onImpact(movingobjectposition);
/*     */       }
/*     */     }
/*     */     
/* 240 */     this.posX += this.motionX;
/* 241 */     this.posY += this.motionY;
/* 242 */     this.posZ += this.motionZ;
/* 243 */     float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 244 */     this.rotationYaw = ((float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
/*     */     
/* 246 */     for (this.rotationPitch = ((float)(MathHelper.func_181159_b(this.motionY, f1) * 180.0D / 3.141592653589793D)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 251 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */     {
/* 253 */       this.prevRotationPitch += 360.0F;
/*     */     }
/*     */     
/* 256 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */     {
/* 258 */       this.prevRotationYaw -= 360.0F;
/*     */     }
/*     */     
/* 261 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */     {
/* 263 */       this.prevRotationYaw += 360.0F;
/*     */     }
/*     */     
/* 266 */     this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
/* 267 */     this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
/* 268 */     float f2 = 0.99F;
/* 269 */     float f3 = getGravityVelocity();
/*     */     
/* 271 */     if (isInWater())
/*     */     {
/* 273 */       for (int i = 0; i < 4; i++)
/*     */       {
/* 275 */         float f4 = 0.25F;
/* 276 */         this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */       }
/*     */       
/* 279 */       f2 = 0.8F;
/*     */     }
/*     */     
/* 282 */     this.motionX *= f2;
/* 283 */     this.motionY *= f2;
/* 284 */     this.motionZ *= f2;
/* 285 */     this.motionY -= f3;
/* 286 */     setPosition(this.posX, this.posY, this.posZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getGravityVelocity()
/*     */   {
/* 294 */     return 0.03F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void onImpact(MovingObjectPosition paramMovingObjectPosition);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 307 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 308 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 309 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 310 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 311 */     tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
/* 312 */     tagCompound.setByte("shake", (byte)this.throwableShake);
/* 313 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/*     */     
/* 315 */     if (((this.throwerName == null) || (this.throwerName.length() == 0)) && ((this.thrower instanceof EntityPlayer)))
/*     */     {
/* 317 */       this.throwerName = this.thrower.getName();
/*     */     }
/*     */     
/* 320 */     tagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 328 */     this.xTile = tagCompund.getShort("xTile");
/* 329 */     this.yTile = tagCompund.getShort("yTile");
/* 330 */     this.zTile = tagCompund.getShort("zTile");
/*     */     
/* 332 */     if (tagCompund.hasKey("inTile", 8))
/*     */     {
/* 334 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else
/*     */     {
/* 338 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     }
/*     */     
/* 341 */     this.throwableShake = (tagCompund.getByte("shake") & 0xFF);
/* 342 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/* 343 */     this.thrower = null;
/* 344 */     this.throwerName = tagCompund.getString("ownerName");
/*     */     
/* 346 */     if ((this.throwerName != null) && (this.throwerName.length() == 0))
/*     */     {
/* 348 */       this.throwerName = null;
/*     */     }
/*     */     
/* 351 */     this.thrower = getThrower();
/*     */   }
/*     */   
/*     */   public EntityLivingBase getThrower()
/*     */   {
/* 356 */     if ((this.thrower == null) && (this.throwerName != null) && (this.throwerName.length() > 0))
/*     */     {
/* 358 */       this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
/*     */       
/* 360 */       if ((this.thrower == null) && ((this.worldObj instanceof WorldServer)))
/*     */       {
/*     */         try
/*     */         {
/* 364 */           Entity entity = ((WorldServer)this.worldObj).getEntityFromUuid(java.util.UUID.fromString(this.throwerName));
/*     */           
/* 366 */           if ((entity instanceof EntityLivingBase))
/*     */           {
/* 368 */             this.thrower = ((EntityLivingBase)entity);
/*     */           }
/*     */         }
/*     */         catch (Throwable var2)
/*     */         {
/* 373 */           this.thrower = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 378 */     return this.thrower;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntityThrowable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */