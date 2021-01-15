/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemFishFood.FishType;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.WeightedRandomFishable;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class EntityFishHook extends Entity
/*     */ {
/*  34 */   private static final List<WeightedRandomFishable> JUNK = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeDamage()), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10) });
/*  35 */   private static final List<WeightedRandomFishable> TREASURE = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), new WeightedRandomFishable(new ItemStack(Items.bow), 1).setMaxDamagePercent(0.25F).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1).setMaxDamagePercent(0.25F).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.book), 1).setEnchantable() });
/*  36 */   private static final List<WeightedRandomFishable> FISH = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 13) });
/*     */   private int xTile;
/*     */   private int yTile;
/*     */   private int zTile;
/*     */   private Block inTile;
/*     */   private boolean inGround;
/*     */   public int shake;
/*     */   public EntityPlayer angler;
/*     */   private int ticksInGround;
/*     */   private int ticksInAir;
/*     */   private int ticksCatchable;
/*     */   private int ticksCaughtDelay;
/*     */   private int ticksCatchableDelay;
/*     */   private float fishApproachAngle;
/*     */   public Entity caughtEntity;
/*     */   private int fishPosRotationIncrements;
/*     */   private double fishX;
/*     */   private double fishY;
/*     */   private double fishZ;
/*     */   private double fishYaw;
/*     */   private double fishPitch;
/*     */   private double clientMotionX;
/*     */   private double clientMotionY;
/*     */   private double clientMotionZ;
/*     */   
/*     */   public static List<WeightedRandomFishable> func_174855_j()
/*     */   {
/*  63 */     return FISH;
/*     */   }
/*     */   
/*     */   public EntityFishHook(World worldIn)
/*     */   {
/*  68 */     super(worldIn);
/*  69 */     this.xTile = -1;
/*  70 */     this.yTile = -1;
/*  71 */     this.zTile = -1;
/*  72 */     setSize(0.25F, 0.25F);
/*  73 */     this.ignoreFrustumCheck = true;
/*     */   }
/*     */   
/*     */   public EntityFishHook(World worldIn, double x, double y, double z, EntityPlayer anglerIn)
/*     */   {
/*  78 */     this(worldIn);
/*  79 */     setPosition(x, y, z);
/*  80 */     this.ignoreFrustumCheck = true;
/*  81 */     this.angler = anglerIn;
/*  82 */     anglerIn.fishEntity = this;
/*     */   }
/*     */   
/*     */   public EntityFishHook(World worldIn, EntityPlayer fishingPlayer)
/*     */   {
/*  87 */     super(worldIn);
/*  88 */     this.xTile = -1;
/*  89 */     this.yTile = -1;
/*  90 */     this.zTile = -1;
/*  91 */     this.ignoreFrustumCheck = true;
/*  92 */     this.angler = fishingPlayer;
/*  93 */     this.angler.fishEntity = this;
/*  94 */     setSize(0.25F, 0.25F);
/*  95 */     setLocationAndAngles(fishingPlayer.posX, fishingPlayer.posY + fishingPlayer.getEyeHeight(), fishingPlayer.posZ, fishingPlayer.rotationYaw, fishingPlayer.rotationPitch);
/*  96 */     this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
/*  97 */     this.posY -= 0.10000000149011612D;
/*  98 */     this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
/*  99 */     setPosition(this.posX, this.posY, this.posZ);
/* 100 */     float f = 0.4F;
/* 101 */     this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/* 102 */     this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/* 103 */     this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f);
/* 104 */     handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
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
/* 117 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/* 119 */     if (Double.isNaN(d0))
/*     */     {
/* 121 */       d0 = 4.0D;
/*     */     }
/*     */     
/* 124 */     d0 *= 64.0D;
/* 125 */     return distance < d0 * d0;
/*     */   }
/*     */   
/*     */   public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_)
/*     */   {
/* 130 */     float f = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
/* 131 */     p_146035_1_ /= f;
/* 132 */     p_146035_3_ /= f;
/* 133 */     p_146035_5_ /= f;
/* 134 */     p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
/* 135 */     p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
/* 136 */     p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
/* 137 */     p_146035_1_ *= p_146035_7_;
/* 138 */     p_146035_3_ *= p_146035_7_;
/* 139 */     p_146035_5_ *= p_146035_7_;
/* 140 */     this.motionX = p_146035_1_;
/* 141 */     this.motionY = p_146035_3_;
/* 142 */     this.motionZ = p_146035_5_;
/* 143 */     float f1 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
/* 144 */     this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(p_146035_1_, p_146035_5_) * 180.0D / 3.141592653589793D));
/* 145 */     this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(p_146035_3_, f1) * 180.0D / 3.141592653589793D));
/* 146 */     this.ticksInGround = 0;
/*     */   }
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
/*     */   {
/* 151 */     this.fishX = x;
/* 152 */     this.fishY = y;
/* 153 */     this.fishZ = z;
/* 154 */     this.fishYaw = yaw;
/* 155 */     this.fishPitch = pitch;
/* 156 */     this.fishPosRotationIncrements = posRotationIncrements;
/* 157 */     this.motionX = this.clientMotionX;
/* 158 */     this.motionY = this.clientMotionY;
/* 159 */     this.motionZ = this.clientMotionZ;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVelocity(double x, double y, double z)
/*     */   {
/* 167 */     this.clientMotionX = (this.motionX = x);
/* 168 */     this.clientMotionY = (this.motionY = y);
/* 169 */     this.clientMotionZ = (this.motionZ = z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 177 */     super.onUpdate();
/*     */     
/* 179 */     if (this.fishPosRotationIncrements > 0)
/*     */     {
/* 181 */       double d7 = this.posX + (this.fishX - this.posX) / this.fishPosRotationIncrements;
/* 182 */       double d8 = this.posY + (this.fishY - this.posY) / this.fishPosRotationIncrements;
/* 183 */       double d9 = this.posZ + (this.fishZ - this.posZ) / this.fishPosRotationIncrements;
/* 184 */       double d1 = MathHelper.wrapAngleTo180_double(this.fishYaw - this.rotationYaw);
/* 185 */       this.rotationYaw = ((float)(this.rotationYaw + d1 / this.fishPosRotationIncrements));
/* 186 */       this.rotationPitch = ((float)(this.rotationPitch + (this.fishPitch - this.rotationPitch) / this.fishPosRotationIncrements));
/* 187 */       this.fishPosRotationIncrements -= 1;
/* 188 */       setPosition(d7, d8, d9);
/* 189 */       setRotation(this.rotationYaw, this.rotationPitch);
/*     */     }
/*     */     else
/*     */     {
/* 193 */       if (!this.worldObj.isRemote)
/*     */       {
/* 195 */         ItemStack itemstack = this.angler.getCurrentEquippedItem();
/*     */         
/* 197 */         if ((this.angler.isDead) || (!this.angler.isEntityAlive()) || (itemstack == null) || (itemstack.getItem() != Items.fishing_rod) || (getDistanceSqToEntity(this.angler) > 1024.0D))
/*     */         {
/* 199 */           setDead();
/* 200 */           this.angler.fishEntity = null;
/* 201 */           return;
/*     */         }
/*     */         
/* 204 */         if (this.caughtEntity != null)
/*     */         {
/* 206 */           if (!this.caughtEntity.isDead)
/*     */           {
/* 208 */             this.posX = this.caughtEntity.posX;
/* 209 */             double d17 = this.caughtEntity.height;
/* 210 */             this.posY = (this.caughtEntity.getEntityBoundingBox().minY + d17 * 0.8D);
/* 211 */             this.posZ = this.caughtEntity.posZ;
/* 212 */             return;
/*     */           }
/*     */           
/* 215 */           this.caughtEntity = null;
/*     */         }
/*     */       }
/*     */       
/* 219 */       if (this.shake > 0)
/*     */       {
/* 221 */         this.shake -= 1;
/*     */       }
/*     */       
/* 224 */       if (this.inGround)
/*     */       {
/* 226 */         if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
/*     */         {
/* 228 */           this.ticksInGround += 1;
/*     */           
/* 230 */           if (this.ticksInGround == 1200)
/*     */           {
/* 232 */             setDead();
/*     */           }
/*     */           
/* 235 */           return;
/*     */         }
/*     */         
/* 238 */         this.inGround = false;
/* 239 */         this.motionX *= this.rand.nextFloat() * 0.2F;
/* 240 */         this.motionY *= this.rand.nextFloat() * 0.2F;
/* 241 */         this.motionZ *= this.rand.nextFloat() * 0.2F;
/* 242 */         this.ticksInGround = 0;
/* 243 */         this.ticksInAir = 0;
/*     */       }
/*     */       else
/*     */       {
/* 247 */         this.ticksInAir += 1;
/*     */       }
/*     */       
/* 250 */       Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 251 */       Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 252 */       MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3);
/* 253 */       vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 254 */       vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */       
/* 256 */       if (movingobjectposition != null)
/*     */       {
/* 258 */         vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */       }
/*     */       
/* 261 */       Entity entity = null;
/* 262 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 263 */       double d0 = 0.0D;
/*     */       
/* 265 */       for (int i = 0; i < list.size(); i++)
/*     */       {
/* 267 */         Entity entity1 = (Entity)list.get(i);
/*     */         
/* 269 */         if ((entity1.canBeCollidedWith()) && ((entity1 != this.angler) || (this.ticksInAir >= 5)))
/*     */         {
/* 271 */           float f = 0.3F;
/* 272 */           AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
/* 273 */           MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec31, vec3);
/*     */           
/* 275 */           if (movingobjectposition1 != null)
/*     */           {
/* 277 */             double d2 = vec31.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 279 */             if ((d2 < d0) || (d0 == 0.0D))
/*     */             {
/* 281 */               entity = entity1;
/* 282 */               d0 = d2;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 288 */       if (entity != null)
/*     */       {
/* 290 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */       
/* 293 */       if (movingobjectposition != null)
/*     */       {
/* 295 */         if (movingobjectposition.entityHit != null)
/*     */         {
/* 297 */           if (movingobjectposition.entityHit.attackEntityFrom(net.minecraft.util.DamageSource.causeThrownDamage(this, this.angler), 0.0F))
/*     */           {
/* 299 */             this.caughtEntity = movingobjectposition.entityHit;
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 304 */           this.inGround = true;
/*     */         }
/*     */       }
/*     */       
/* 308 */       if (!this.inGround)
/*     */       {
/* 310 */         moveEntity(this.motionX, this.motionY, this.motionZ);
/* 311 */         float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 312 */         this.rotationYaw = ((float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
/*     */         
/* 314 */         for (this.rotationPitch = ((float)(MathHelper.func_181159_b(this.motionY, f5) * 180.0D / 3.141592653589793D)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 319 */         while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */         {
/* 321 */           this.prevRotationPitch += 360.0F;
/*     */         }
/*     */         
/* 324 */         while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */         {
/* 326 */           this.prevRotationYaw -= 360.0F;
/*     */         }
/*     */         
/* 329 */         while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */         {
/* 331 */           this.prevRotationYaw += 360.0F;
/*     */         }
/*     */         
/* 334 */         this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
/* 335 */         this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
/* 336 */         float f6 = 0.92F;
/*     */         
/* 338 */         if ((this.onGround) || (this.isCollidedHorizontally))
/*     */         {
/* 340 */           f6 = 0.5F;
/*     */         }
/*     */         
/* 343 */         int j = 5;
/* 344 */         double d10 = 0.0D;
/*     */         
/* 346 */         for (int k = 0; k < j; k++)
/*     */         {
/* 348 */           AxisAlignedBB axisalignedbb1 = getEntityBoundingBox();
/* 349 */           double d3 = axisalignedbb1.maxY - axisalignedbb1.minY;
/* 350 */           double d4 = axisalignedbb1.minY + d3 * k / j;
/* 351 */           double d5 = axisalignedbb1.minY + d3 * (k + 1) / j;
/* 352 */           AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb1.minX, d4, axisalignedbb1.minZ, axisalignedbb1.maxX, d5, axisalignedbb1.maxZ);
/*     */           
/* 354 */           if (this.worldObj.isAABBInMaterial(axisalignedbb2, net.minecraft.block.material.Material.water))
/*     */           {
/* 356 */             d10 += 1.0D / j;
/*     */           }
/*     */         }
/*     */         
/* 360 */         if ((!this.worldObj.isRemote) && (d10 > 0.0D))
/*     */         {
/* 362 */           WorldServer worldserver = (WorldServer)this.worldObj;
/* 363 */           int l = 1;
/* 364 */           BlockPos blockpos = new BlockPos(this).up();
/*     */           
/* 366 */           if ((this.rand.nextFloat() < 0.25F) && (this.worldObj.canLightningStrike(blockpos)))
/*     */           {
/* 368 */             l = 2;
/*     */           }
/*     */           
/* 371 */           if ((this.rand.nextFloat() < 0.5F) && (!this.worldObj.canSeeSky(blockpos)))
/*     */           {
/* 373 */             l--;
/*     */           }
/*     */           
/* 376 */           if (this.ticksCatchable > 0)
/*     */           {
/* 378 */             this.ticksCatchable -= 1;
/*     */             
/* 380 */             if (this.ticksCatchable <= 0)
/*     */             {
/* 382 */               this.ticksCaughtDelay = 0;
/* 383 */               this.ticksCatchableDelay = 0;
/*     */             }
/*     */           }
/* 386 */           else if (this.ticksCatchableDelay > 0)
/*     */           {
/* 388 */             this.ticksCatchableDelay -= l;
/*     */             
/* 390 */             if (this.ticksCatchableDelay <= 0)
/*     */             {
/* 392 */               this.motionY -= 0.20000000298023224D;
/* 393 */               playSound("random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/* 394 */               float f8 = MathHelper.floor_double(getEntityBoundingBox().minY);
/* 395 */               worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, f8 + 1.0F, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0D, this.width, 0.20000000298023224D, new int[0]);
/* 396 */               worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, f8 + 1.0F, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0D, this.width, 0.20000000298023224D, new int[0]);
/* 397 */               this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
/*     */             }
/*     */             else
/*     */             {
/* 401 */               this.fishApproachAngle = ((float)(this.fishApproachAngle + this.rand.nextGaussian() * 4.0D));
/* 402 */               float f7 = this.fishApproachAngle * 0.017453292F;
/* 403 */               float f10 = MathHelper.sin(f7);
/* 404 */               float f11 = MathHelper.cos(f7);
/* 405 */               double d13 = this.posX + f10 * this.ticksCatchableDelay * 0.1F;
/* 406 */               double d15 = MathHelper.floor_double(getEntityBoundingBox().minY) + 1.0F;
/* 407 */               double d16 = this.posZ + f11 * this.ticksCatchableDelay * 0.1F;
/* 408 */               Block block1 = worldserver.getBlockState(new BlockPos((int)d13, (int)d15 - 1, (int)d16)).getBlock();
/*     */               
/* 410 */               if ((block1 == Blocks.water) || (block1 == Blocks.flowing_water))
/*     */               {
/* 412 */                 if (this.rand.nextFloat() < 0.15F)
/*     */                 {
/* 414 */                   worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d13, d15 - 0.10000000149011612D, d16, 1, f10, 0.1D, f11, 0.0D, new int[0]);
/*     */                 }
/*     */                 
/* 417 */                 float f3 = f10 * 0.04F;
/* 418 */                 float f4 = f11 * 0.04F;
/* 419 */                 worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, f4, 0.01D, -f3, 1.0D, new int[0]);
/* 420 */                 worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, -f4, 0.01D, f3, 1.0D, new int[0]);
/*     */               }
/*     */             }
/*     */           }
/* 424 */           else if (this.ticksCaughtDelay > 0)
/*     */           {
/* 426 */             this.ticksCaughtDelay -= l;
/* 427 */             float f1 = 0.15F;
/*     */             
/* 429 */             if (this.ticksCaughtDelay < 20)
/*     */             {
/* 431 */               f1 = (float)(f1 + (20 - this.ticksCaughtDelay) * 0.05D);
/*     */             }
/* 433 */             else if (this.ticksCaughtDelay < 40)
/*     */             {
/* 435 */               f1 = (float)(f1 + (40 - this.ticksCaughtDelay) * 0.02D);
/*     */             }
/* 437 */             else if (this.ticksCaughtDelay < 60)
/*     */             {
/* 439 */               f1 = (float)(f1 + (60 - this.ticksCaughtDelay) * 0.01D);
/*     */             }
/*     */             
/* 442 */             if (this.rand.nextFloat() < f1)
/*     */             {
/* 444 */               float f9 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
/* 445 */               float f2 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
/* 446 */               double d12 = this.posX + MathHelper.sin(f9) * f2 * 0.1F;
/* 447 */               double d14 = MathHelper.floor_double(getEntityBoundingBox().minY) + 1.0F;
/* 448 */               double d6 = this.posZ + MathHelper.cos(f9) * f2 * 0.1F;
/* 449 */               Block block = worldserver.getBlockState(new BlockPos((int)d12, (int)d14 - 1, (int)d6)).getBlock();
/*     */               
/* 451 */               if ((block == Blocks.water) || (block == Blocks.flowing_water))
/*     */               {
/* 453 */                 worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d12, d14, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
/*     */               }
/*     */             }
/*     */             
/* 457 */             if (this.ticksCaughtDelay <= 0)
/*     */             {
/* 459 */               this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
/* 460 */               this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 465 */             this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
/* 466 */             this.ticksCaughtDelay -= EnchantmentHelper.getLureModifier(this.angler) * 20 * 5;
/*     */           }
/*     */           
/* 469 */           if (this.ticksCatchable > 0)
/*     */           {
/* 471 */             this.motionY -= this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat() * 0.2D;
/*     */           }
/*     */         }
/*     */         
/* 475 */         double d11 = d10 * 2.0D - 1.0D;
/* 476 */         this.motionY += 0.03999999910593033D * d11;
/*     */         
/* 478 */         if (d10 > 0.0D)
/*     */         {
/* 480 */           f6 = (float)(f6 * 0.9D);
/* 481 */           this.motionY *= 0.8D;
/*     */         }
/*     */         
/* 484 */         this.motionX *= f6;
/* 485 */         this.motionY *= f6;
/* 486 */         this.motionZ *= f6;
/* 487 */         setPosition(this.posX, this.posY, this.posZ);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 497 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 498 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 499 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 500 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 501 */     tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
/* 502 */     tagCompound.setByte("shake", (byte)this.shake);
/* 503 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 511 */     this.xTile = tagCompund.getShort("xTile");
/* 512 */     this.yTile = tagCompund.getShort("yTile");
/* 513 */     this.zTile = tagCompund.getShort("zTile");
/*     */     
/* 515 */     if (tagCompund.hasKey("inTile", 8))
/*     */     {
/* 517 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else
/*     */     {
/* 521 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     }
/*     */     
/* 524 */     this.shake = (tagCompund.getByte("shake") & 0xFF);
/* 525 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/*     */   }
/*     */   
/*     */   public int handleHookRetraction()
/*     */   {
/* 530 */     if (this.worldObj.isRemote)
/*     */     {
/* 532 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 536 */     int i = 0;
/*     */     
/* 538 */     if (this.caughtEntity != null)
/*     */     {
/* 540 */       double d0 = this.angler.posX - this.posX;
/* 541 */       double d2 = this.angler.posY - this.posY;
/* 542 */       double d4 = this.angler.posZ - this.posZ;
/* 543 */       double d6 = MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d4 * d4);
/* 544 */       double d8 = 0.1D;
/* 545 */       this.caughtEntity.motionX += d0 * d8;
/* 546 */       this.caughtEntity.motionY += d2 * d8 + MathHelper.sqrt_double(d6) * 0.08D;
/* 547 */       this.caughtEntity.motionZ += d4 * d8;
/* 548 */       i = 3;
/*     */     }
/* 550 */     else if (this.ticksCatchable > 0)
/*     */     {
/* 552 */       EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, getFishingResult());
/* 553 */       double d1 = this.angler.posX - this.posX;
/* 554 */       double d3 = this.angler.posY - this.posY;
/* 555 */       double d5 = this.angler.posZ - this.posZ;
/* 556 */       double d7 = MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
/* 557 */       double d9 = 0.1D;
/* 558 */       entityitem.motionX = (d1 * d9);
/* 559 */       entityitem.motionY = (d3 * d9 + MathHelper.sqrt_double(d7) * 0.08D);
/* 560 */       entityitem.motionZ = (d5 * d9);
/* 561 */       this.worldObj.spawnEntityInWorld(entityitem);
/* 562 */       this.angler.worldObj.spawnEntityInWorld(new net.minecraft.entity.item.EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
/* 563 */       i = 1;
/*     */     }
/*     */     
/* 566 */     if (this.inGround)
/*     */     {
/* 568 */       i = 2;
/*     */     }
/*     */     
/* 571 */     setDead();
/* 572 */     this.angler.fishEntity = null;
/* 573 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   private ItemStack getFishingResult()
/*     */   {
/* 579 */     float f = this.worldObj.rand.nextFloat();
/* 580 */     int i = EnchantmentHelper.getLuckOfSeaModifier(this.angler);
/* 581 */     int j = EnchantmentHelper.getLureModifier(this.angler);
/* 582 */     float f1 = 0.1F - i * 0.025F - j * 0.01F;
/* 583 */     float f2 = 0.05F + i * 0.01F - j * 0.01F;
/* 584 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 585 */     f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
/*     */     
/* 587 */     if (f < f1)
/*     */     {
/* 589 */       this.angler.triggerAchievement(StatList.junkFishedStat);
/* 590 */       return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, JUNK)).getItemStack(this.rand);
/*     */     }
/*     */     
/*     */ 
/* 594 */     f -= f1;
/*     */     
/* 596 */     if (f < f2)
/*     */     {
/* 598 */       this.angler.triggerAchievement(StatList.treasureFishedStat);
/* 599 */       return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, TREASURE)).getItemStack(this.rand);
/*     */     }
/*     */     
/*     */ 
/* 603 */     float f3 = f - f2;
/* 604 */     this.angler.triggerAchievement(StatList.fishCaughtStat);
/* 605 */     return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, FISH)).getItemStack(this.rand);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDead()
/*     */   {
/* 615 */     super.setDead();
/*     */     
/* 617 */     if (this.angler != null)
/*     */     {
/* 619 */       this.angler.fishEntity = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntityFishHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */