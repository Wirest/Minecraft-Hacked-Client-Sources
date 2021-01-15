/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityFireball extends Entity
/*     */ {
/*  21 */   private int xTile = -1;
/*  22 */   private int yTile = -1;
/*  23 */   private int zTile = -1;
/*     */   private Block inTile;
/*     */   private boolean inGround;
/*     */   public EntityLivingBase shootingEntity;
/*     */   private int ticksAlive;
/*     */   private int ticksInAir;
/*     */   public double accelerationX;
/*     */   public double accelerationY;
/*     */   public double accelerationZ;
/*     */   
/*     */   public EntityFireball(World worldIn)
/*     */   {
/*  35 */     super(worldIn);
/*  36 */     setSize(1.0F, 1.0F);
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
/*  49 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/*  51 */     if (Double.isNaN(d0))
/*     */     {
/*  53 */       d0 = 4.0D;
/*     */     }
/*     */     
/*  56 */     d0 *= 64.0D;
/*  57 */     return distance < d0 * d0;
/*     */   }
/*     */   
/*     */   public EntityFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
/*     */   {
/*  62 */     super(worldIn);
/*  63 */     setSize(1.0F, 1.0F);
/*  64 */     setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
/*  65 */     setPosition(x, y, z);
/*  66 */     double d0 = MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
/*  67 */     this.accelerationX = (accelX / d0 * 0.1D);
/*  68 */     this.accelerationY = (accelY / d0 * 0.1D);
/*  69 */     this.accelerationZ = (accelZ / d0 * 0.1D);
/*     */   }
/*     */   
/*     */   public EntityFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
/*     */   {
/*  74 */     super(worldIn);
/*  75 */     this.shootingEntity = shooter;
/*  76 */     setSize(1.0F, 1.0F);
/*  77 */     setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
/*  78 */     setPosition(this.posX, this.posY, this.posZ);
/*  79 */     this.motionX = (this.motionY = this.motionZ = 0.0D);
/*  80 */     accelX += this.rand.nextGaussian() * 0.4D;
/*  81 */     accelY += this.rand.nextGaussian() * 0.4D;
/*  82 */     accelZ += this.rand.nextGaussian() * 0.4D;
/*  83 */     double d0 = MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
/*  84 */     this.accelerationX = (accelX / d0 * 0.1D);
/*  85 */     this.accelerationY = (accelY / d0 * 0.1D);
/*  86 */     this.accelerationZ = (accelZ / d0 * 0.1D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  94 */     if ((this.worldObj.isRemote) || (((this.shootingEntity == null) || (!this.shootingEntity.isDead)) && (this.worldObj.isBlockLoaded(new net.minecraft.util.BlockPos(this)))))
/*     */     {
/*  96 */       super.onUpdate();
/*  97 */       setFire(1);
/*     */       
/*  99 */       if (this.inGround)
/*     */       {
/* 101 */         if (this.worldObj.getBlockState(new net.minecraft.util.BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
/*     */         {
/* 103 */           this.ticksAlive += 1;
/*     */           
/* 105 */           if (this.ticksAlive == 600)
/*     */           {
/* 107 */             setDead();
/*     */           }
/*     */           
/* 110 */           return;
/*     */         }
/*     */         
/* 113 */         this.inGround = false;
/* 114 */         this.motionX *= this.rand.nextFloat() * 0.2F;
/* 115 */         this.motionY *= this.rand.nextFloat() * 0.2F;
/* 116 */         this.motionZ *= this.rand.nextFloat() * 0.2F;
/* 117 */         this.ticksAlive = 0;
/* 118 */         this.ticksInAir = 0;
/*     */       }
/*     */       else
/*     */       {
/* 122 */         this.ticksInAir += 1;
/*     */       }
/*     */       
/* 125 */       Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 126 */       Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 127 */       MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
/* 128 */       vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 129 */       vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */       
/* 131 */       if (movingobjectposition != null)
/*     */       {
/* 133 */         vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */       }
/*     */       
/* 136 */       Entity entity = null;
/* 137 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 138 */       double d0 = 0.0D;
/*     */       
/* 140 */       for (int i = 0; i < list.size(); i++)
/*     */       {
/* 142 */         Entity entity1 = (Entity)list.get(i);
/*     */         
/* 144 */         if ((entity1.canBeCollidedWith()) && ((!entity1.isEntityEqual(this.shootingEntity)) || (this.ticksInAir >= 25)))
/*     */         {
/* 146 */           float f = 0.3F;
/* 147 */           AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
/* 148 */           MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
/*     */           
/* 150 */           if (movingobjectposition1 != null)
/*     */           {
/* 152 */             double d1 = vec3.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 154 */             if ((d1 < d0) || (d0 == 0.0D))
/*     */             {
/* 156 */               entity = entity1;
/* 157 */               d0 = d1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 163 */       if (entity != null)
/*     */       {
/* 165 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */       
/* 168 */       if (movingobjectposition != null)
/*     */       {
/* 170 */         onImpact(movingobjectposition);
/*     */       }
/*     */       
/* 173 */       this.posX += this.motionX;
/* 174 */       this.posY += this.motionY;
/* 175 */       this.posZ += this.motionZ;
/* 176 */       float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 177 */       this.rotationYaw = ((float)(MathHelper.func_181159_b(this.motionZ, this.motionX) * 180.0D / 3.141592653589793D) + 90.0F);
/*     */       
/* 179 */       for (this.rotationPitch = ((float)(MathHelper.func_181159_b(f1, this.motionY) * 180.0D / 3.141592653589793D) - 90.0F); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 184 */       while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */       {
/* 186 */         this.prevRotationPitch += 360.0F;
/*     */       }
/*     */       
/* 189 */       while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */       {
/* 191 */         this.prevRotationYaw -= 360.0F;
/*     */       }
/*     */       
/* 194 */       while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */       {
/* 196 */         this.prevRotationYaw += 360.0F;
/*     */       }
/*     */       
/* 199 */       this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
/* 200 */       this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
/* 201 */       float f2 = getMotionFactor();
/*     */       
/* 203 */       if (isInWater())
/*     */       {
/* 205 */         for (int j = 0; j < 4; j++)
/*     */         {
/* 207 */           float f3 = 0.25F;
/* 208 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         }
/*     */         
/* 211 */         f2 = 0.8F;
/*     */       }
/*     */       
/* 214 */       this.motionX += this.accelerationX;
/* 215 */       this.motionY += this.accelerationY;
/* 216 */       this.motionZ += this.accelerationZ;
/* 217 */       this.motionX *= f2;
/* 218 */       this.motionY *= f2;
/* 219 */       this.motionZ *= f2;
/* 220 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 221 */       setPosition(this.posX, this.posY, this.posZ);
/*     */     }
/*     */     else
/*     */     {
/* 225 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getMotionFactor()
/*     */   {
/* 234 */     return 0.95F;
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
/* 247 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 248 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 249 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 250 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 251 */     tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
/* 252 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/* 253 */     tagCompound.setTag("direction", newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 261 */     this.xTile = tagCompund.getShort("xTile");
/* 262 */     this.yTile = tagCompund.getShort("yTile");
/* 263 */     this.zTile = tagCompund.getShort("zTile");
/*     */     
/* 265 */     if (tagCompund.hasKey("inTile", 8))
/*     */     {
/* 267 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else
/*     */     {
/* 271 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     }
/*     */     
/* 274 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/*     */     
/* 276 */     if (tagCompund.hasKey("direction", 9))
/*     */     {
/* 278 */       NBTTagList nbttaglist = tagCompund.getTagList("direction", 6);
/* 279 */       this.motionX = nbttaglist.getDoubleAt(0);
/* 280 */       this.motionY = nbttaglist.getDoubleAt(1);
/* 281 */       this.motionZ = nbttaglist.getDoubleAt(2);
/*     */     }
/*     */     else
/*     */     {
/* 285 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/* 294 */     return true;
/*     */   }
/*     */   
/*     */   public float getCollisionBorderSize()
/*     */   {
/* 299 */     return 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 307 */     if (isEntityInvulnerable(source))
/*     */     {
/* 309 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 313 */     setBeenAttacked();
/*     */     
/* 315 */     if (source.getEntity() != null)
/*     */     {
/* 317 */       Vec3 vec3 = source.getEntity().getLookVec();
/*     */       
/* 319 */       if (vec3 != null)
/*     */       {
/* 321 */         this.motionX = vec3.xCoord;
/* 322 */         this.motionY = vec3.yCoord;
/* 323 */         this.motionZ = vec3.zCoord;
/* 324 */         this.accelerationX = (this.motionX * 0.1D);
/* 325 */         this.accelerationY = (this.motionY * 0.1D);
/* 326 */         this.accelerationZ = (this.motionZ * 0.1D);
/*     */       }
/*     */       
/* 329 */       if ((source.getEntity() instanceof EntityLivingBase))
/*     */       {
/* 331 */         this.shootingEntity = ((EntityLivingBase)source.getEntity());
/*     */       }
/*     */       
/* 334 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 338 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBrightness(float partialTicks)
/*     */   {
/* 348 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/* 353 */     return 15728880;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntityFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */