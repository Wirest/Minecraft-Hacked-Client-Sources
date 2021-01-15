/*      */ package net.minecraft.entity.item;
/*      */ 
/*      */ import com.google.common.collect.Maps;
/*      */ import java.util.Map;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockRailBase;
/*      */ import net.minecraft.block.BlockRailBase.EnumRailDirection;
/*      */ import net.minecraft.block.BlockRailPowered;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.entity.DataWatcher;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.ai.EntityMinecartMobSpawner;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.WorldServer;
/*      */ 
/*      */ public abstract class EntityMinecart extends Entity implements net.minecraft.world.IWorldNameable
/*      */ {
/*      */   private boolean isInReverse;
/*      */   private String entityName;
/*   39 */   private static final int[][][] matrix = { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1 }, { 1 } }, { { -1, -1 }, { 1 } }, { { -1 }, { 1, -1 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1 } }, { { 0, 0, 1 }, { -1 } }, { { 0, 0, -1 }, { -1 } }, { { 0, 0, -1 }, { 1 } } };
/*      */   
/*      */   private int turnProgress;
/*      */   
/*      */   private double minecartX;
/*      */   private double minecartY;
/*      */   private double minecartZ;
/*      */   private double minecartYaw;
/*      */   private double minecartPitch;
/*      */   private double velocityX;
/*      */   private double velocityY;
/*      */   private double velocityZ;
/*      */   
/*      */   public EntityMinecart(World worldIn)
/*      */   {
/*   54 */     super(worldIn);
/*   55 */     this.preventEntitySpawning = true;
/*   56 */     setSize(0.98F, 0.7F);
/*      */   }
/*      */   
/*      */   public static EntityMinecart func_180458_a(World worldIn, double p_180458_1_, double p_180458_3_, double p_180458_5_, EnumMinecartType p_180458_7_)
/*      */   {
/*   61 */     switch (p_180458_7_)
/*      */     {
/*      */     case COMMAND_BLOCK: 
/*   64 */       return new EntityMinecartChest(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
/*      */     
/*      */     case FURNACE: 
/*   67 */       return new EntityMinecartFurnace(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
/*      */     
/*      */     case HOPPER: 
/*   70 */       return new EntityMinecartTNT(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
/*      */     
/*      */     case RIDEABLE: 
/*   73 */       return new EntityMinecartMobSpawner(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
/*      */     
/*      */     case SPAWNER: 
/*   76 */       return new EntityMinecartHopper(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
/*      */     
/*      */     case TNT: 
/*   79 */       return new net.minecraft.entity.EntityMinecartCommandBlock(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
/*      */     }
/*      */     
/*   82 */     return new EntityMinecartEmpty(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean canTriggerWalking()
/*      */   {
/*   92 */     return false;
/*      */   }
/*      */   
/*      */   protected void entityInit()
/*      */   {
/*   97 */     this.dataWatcher.addObject(17, new Integer(0));
/*   98 */     this.dataWatcher.addObject(18, new Integer(1));
/*   99 */     this.dataWatcher.addObject(19, new Float(0.0F));
/*  100 */     this.dataWatcher.addObject(20, new Integer(0));
/*  101 */     this.dataWatcher.addObject(21, new Integer(6));
/*  102 */     this.dataWatcher.addObject(22, Byte.valueOf((byte)0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AxisAlignedBB getCollisionBox(Entity entityIn)
/*      */   {
/*  111 */     return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public AxisAlignedBB getCollisionBoundingBox()
/*      */   {
/*  119 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBePushed()
/*      */   {
/*  127 */     return true;
/*      */   }
/*      */   
/*      */   public EntityMinecart(World worldIn, double x, double y, double z)
/*      */   {
/*  132 */     this(worldIn);
/*  133 */     setPosition(x, y, z);
/*  134 */     this.motionX = 0.0D;
/*  135 */     this.motionY = 0.0D;
/*  136 */     this.motionZ = 0.0D;
/*  137 */     this.prevPosX = x;
/*  138 */     this.prevPosY = y;
/*  139 */     this.prevPosZ = z;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getMountedYOffset()
/*      */   {
/*  147 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean attackEntityFrom(DamageSource source, float amount)
/*      */   {
/*  155 */     if ((!this.worldObj.isRemote) && (!this.isDead))
/*      */     {
/*  157 */       if (isEntityInvulnerable(source))
/*      */       {
/*  159 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  163 */       setRollingDirection(-getRollingDirection());
/*  164 */       setRollingAmplitude(10);
/*  165 */       setBeenAttacked();
/*  166 */       setDamage(getDamage() + amount * 10.0F);
/*  167 */       boolean flag = ((source.getEntity() instanceof EntityPlayer)) && (((EntityPlayer)source.getEntity()).capabilities.isCreativeMode);
/*      */       
/*  169 */       if ((flag) || (getDamage() > 40.0F))
/*      */       {
/*  171 */         if (this.riddenByEntity != null)
/*      */         {
/*  173 */           this.riddenByEntity.mountEntity(null);
/*      */         }
/*      */         
/*  176 */         if ((flag) && (!hasCustomName()))
/*      */         {
/*  178 */           setDead();
/*      */         }
/*      */         else
/*      */         {
/*  182 */           killMinecart(source);
/*      */         }
/*      */       }
/*      */       
/*  186 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  191 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public void killMinecart(DamageSource p_94095_1_)
/*      */   {
/*  197 */     setDead();
/*      */     
/*  199 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*      */     {
/*  201 */       ItemStack itemstack = new ItemStack(net.minecraft.init.Items.minecart, 1);
/*      */       
/*  203 */       if (this.entityName != null)
/*      */       {
/*  205 */         itemstack.setStackDisplayName(this.entityName);
/*      */       }
/*      */       
/*  208 */       entityDropItem(itemstack, 0.0F);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void performHurtAnimation()
/*      */   {
/*  217 */     setRollingDirection(-getRollingDirection());
/*  218 */     setRollingAmplitude(10);
/*  219 */     setDamage(getDamage() + getDamage() * 10.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBeCollidedWith()
/*      */   {
/*  227 */     return !this.isDead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDead()
/*      */   {
/*  235 */     super.setDead();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate()
/*      */   {
/*  243 */     if (getRollingAmplitude() > 0)
/*      */     {
/*  245 */       setRollingAmplitude(getRollingAmplitude() - 1);
/*      */     }
/*      */     
/*  248 */     if (getDamage() > 0.0F)
/*      */     {
/*  250 */       setDamage(getDamage() - 1.0F);
/*      */     }
/*      */     
/*  253 */     if (this.posY < -64.0D)
/*      */     {
/*  255 */       kill();
/*      */     }
/*      */     
/*  258 */     if ((!this.worldObj.isRemote) && ((this.worldObj instanceof WorldServer)))
/*      */     {
/*  260 */       this.worldObj.theProfiler.startSection("portal");
/*  261 */       MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
/*  262 */       int i = getMaxInPortalTime();
/*      */       
/*  264 */       if (this.inPortal)
/*      */       {
/*  266 */         if (minecraftserver.getAllowNether())
/*      */         {
/*  268 */           if ((this.ridingEntity == null) && (this.portalCounter++ >= i))
/*      */           {
/*  270 */             this.portalCounter = i;
/*  271 */             this.timeUntilPortal = getPortalCooldown();
/*      */             int j;
/*      */             int j;
/*  274 */             if (this.worldObj.provider.getDimensionId() == -1)
/*      */             {
/*  276 */               j = 0;
/*      */             }
/*      */             else
/*      */             {
/*  280 */               j = -1;
/*      */             }
/*      */             
/*  283 */             travelToDimension(j);
/*      */           }
/*      */           
/*  286 */           this.inPortal = false;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  291 */         if (this.portalCounter > 0)
/*      */         {
/*  293 */           this.portalCounter -= 4;
/*      */         }
/*      */         
/*  296 */         if (this.portalCounter < 0)
/*      */         {
/*  298 */           this.portalCounter = 0;
/*      */         }
/*      */       }
/*      */       
/*  302 */       if (this.timeUntilPortal > 0)
/*      */       {
/*  304 */         this.timeUntilPortal -= 1;
/*      */       }
/*      */       
/*  307 */       this.worldObj.theProfiler.endSection();
/*      */     }
/*      */     
/*  310 */     if (this.worldObj.isRemote)
/*      */     {
/*  312 */       if (this.turnProgress > 0)
/*      */       {
/*  314 */         double d4 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
/*  315 */         double d5 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
/*  316 */         double d6 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
/*  317 */         double d1 = MathHelper.wrapAngleTo180_double(this.minecartYaw - this.rotationYaw);
/*  318 */         this.rotationYaw = ((float)(this.rotationYaw + d1 / this.turnProgress));
/*  319 */         this.rotationPitch = ((float)(this.rotationPitch + (this.minecartPitch - this.rotationPitch) / this.turnProgress));
/*  320 */         this.turnProgress -= 1;
/*  321 */         setPosition(d4, d5, d6);
/*  322 */         setRotation(this.rotationYaw, this.rotationPitch);
/*      */       }
/*      */       else
/*      */       {
/*  326 */         setPosition(this.posX, this.posY, this.posZ);
/*  327 */         setRotation(this.rotationYaw, this.rotationPitch);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  332 */       this.prevPosX = this.posX;
/*  333 */       this.prevPosY = this.posY;
/*  334 */       this.prevPosZ = this.posZ;
/*  335 */       this.motionY -= 0.03999999910593033D;
/*  336 */       int k = MathHelper.floor_double(this.posX);
/*  337 */       int l = MathHelper.floor_double(this.posY);
/*  338 */       int i1 = MathHelper.floor_double(this.posZ);
/*      */       
/*  340 */       if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(k, l - 1, i1)))
/*      */       {
/*  342 */         l--;
/*      */       }
/*      */       
/*  345 */       BlockPos blockpos = new BlockPos(k, l, i1);
/*  346 */       IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/*      */       
/*  348 */       if (BlockRailBase.isRailBlock(iblockstate))
/*      */       {
/*  350 */         func_180460_a(blockpos, iblockstate);
/*      */         
/*  352 */         if (iblockstate.getBlock() == Blocks.activator_rail)
/*      */         {
/*  354 */           onActivatorRailPass(k, l, i1, ((Boolean)iblockstate.getValue(BlockRailPowered.POWERED)).booleanValue());
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  359 */         moveDerailedMinecart();
/*      */       }
/*      */       
/*  362 */       doBlockCollisions();
/*  363 */       this.rotationPitch = 0.0F;
/*  364 */       double d0 = this.prevPosX - this.posX;
/*  365 */       double d2 = this.prevPosZ - this.posZ;
/*      */       
/*  367 */       if (d0 * d0 + d2 * d2 > 0.001D)
/*      */       {
/*  369 */         this.rotationYaw = ((float)(MathHelper.func_181159_b(d2, d0) * 180.0D / 3.141592653589793D));
/*      */         
/*  371 */         if (this.isInReverse)
/*      */         {
/*  373 */           this.rotationYaw += 180.0F;
/*      */         }
/*      */       }
/*      */       
/*  377 */       double d3 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);
/*      */       
/*  379 */       if ((d3 < -170.0D) || (d3 >= 170.0D))
/*      */       {
/*  381 */         this.rotationYaw += 180.0F;
/*  382 */         this.isInReverse = (!this.isInReverse);
/*      */       }
/*      */       
/*  385 */       setRotation(this.rotationYaw, this.rotationPitch);
/*      */       
/*  387 */       for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D)))
/*      */       {
/*  389 */         if ((entity != this.riddenByEntity) && (entity.canBePushed()) && ((entity instanceof EntityMinecart)))
/*      */         {
/*  391 */           entity.applyEntityCollision(this);
/*      */         }
/*      */       }
/*      */       
/*  395 */       if ((this.riddenByEntity != null) && (this.riddenByEntity.isDead))
/*      */       {
/*  397 */         if (this.riddenByEntity.ridingEntity == this)
/*      */         {
/*  399 */           this.riddenByEntity.ridingEntity = null;
/*      */         }
/*      */         
/*  402 */         this.riddenByEntity = null;
/*      */       }
/*      */       
/*  405 */       handleWaterMovement();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected double getMaximumSpeed()
/*      */   {
/*  414 */     return 0.4D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void moveDerailedMinecart()
/*      */   {
/*  429 */     double d0 = getMaximumSpeed();
/*  430 */     this.motionX = MathHelper.clamp_double(this.motionX, -d0, d0);
/*  431 */     this.motionZ = MathHelper.clamp_double(this.motionZ, -d0, d0);
/*      */     
/*  433 */     if (this.onGround)
/*      */     {
/*  435 */       this.motionX *= 0.5D;
/*  436 */       this.motionY *= 0.5D;
/*  437 */       this.motionZ *= 0.5D;
/*      */     }
/*      */     
/*  440 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*      */     
/*  442 */     if (!this.onGround)
/*      */     {
/*  444 */       this.motionX *= 0.949999988079071D;
/*  445 */       this.motionY *= 0.949999988079071D;
/*  446 */       this.motionZ *= 0.949999988079071D;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_)
/*      */   {
/*  453 */     this.fallDistance = 0.0F;
/*  454 */     Vec3 vec3 = func_70489_a(this.posX, this.posY, this.posZ);
/*  455 */     this.posY = p_180460_1_.getY();
/*  456 */     boolean flag = false;
/*  457 */     boolean flag1 = false;
/*  458 */     BlockRailBase blockrailbase = (BlockRailBase)p_180460_2_.getBlock();
/*      */     
/*  460 */     if (blockrailbase == Blocks.golden_rail)
/*      */     {
/*  462 */       flag = ((Boolean)p_180460_2_.getValue(BlockRailPowered.POWERED)).booleanValue();
/*  463 */       flag1 = !flag;
/*      */     }
/*      */     
/*  466 */     double d0 = 0.0078125D;
/*  467 */     BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)p_180460_2_.getValue(blockrailbase.getShapeProperty());
/*      */     
/*  469 */     switch (blockrailbase$enumraildirection)
/*      */     {
/*      */     case ASCENDING_SOUTH: 
/*  472 */       this.motionX -= 0.0078125D;
/*  473 */       this.posY += 1.0D;
/*  474 */       break;
/*      */     
/*      */     case ASCENDING_WEST: 
/*  477 */       this.motionX += 0.0078125D;
/*  478 */       this.posY += 1.0D;
/*  479 */       break;
/*      */     
/*      */     case EAST_WEST: 
/*  482 */       this.motionZ += 0.0078125D;
/*  483 */       this.posY += 1.0D;
/*  484 */       break;
/*      */     
/*      */     case NORTH_EAST: 
/*  487 */       this.motionZ -= 0.0078125D;
/*  488 */       this.posY += 1.0D;
/*      */     }
/*      */     
/*  491 */     int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
/*  492 */     double d1 = aint[1][0] - aint[0][0];
/*  493 */     double d2 = aint[1][2] - aint[0][2];
/*  494 */     double d3 = Math.sqrt(d1 * d1 + d2 * d2);
/*  495 */     double d4 = this.motionX * d1 + this.motionZ * d2;
/*      */     
/*  497 */     if (d4 < 0.0D)
/*      */     {
/*  499 */       d1 = -d1;
/*  500 */       d2 = -d2;
/*      */     }
/*      */     
/*  503 */     double d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */     
/*  505 */     if (d5 > 2.0D)
/*      */     {
/*  507 */       d5 = 2.0D;
/*      */     }
/*      */     
/*  510 */     this.motionX = (d5 * d1 / d3);
/*  511 */     this.motionZ = (d5 * d2 / d3);
/*      */     
/*  513 */     if ((this.riddenByEntity instanceof EntityLivingBase))
/*      */     {
/*  515 */       double d6 = ((EntityLivingBase)this.riddenByEntity).moveForward;
/*      */       
/*  517 */       if (d6 > 0.0D)
/*      */       {
/*  519 */         double d7 = -Math.sin(this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F);
/*  520 */         double d8 = Math.cos(this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F);
/*  521 */         double d9 = this.motionX * this.motionX + this.motionZ * this.motionZ;
/*      */         
/*  523 */         if (d9 < 0.01D)
/*      */         {
/*  525 */           this.motionX += d7 * 0.1D;
/*  526 */           this.motionZ += d8 * 0.1D;
/*  527 */           flag1 = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  532 */     if (flag1)
/*      */     {
/*  534 */       double d17 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */       
/*  536 */       if (d17 < 0.03D)
/*      */       {
/*  538 */         this.motionX *= 0.0D;
/*  539 */         this.motionY *= 0.0D;
/*  540 */         this.motionZ *= 0.0D;
/*      */       }
/*      */       else
/*      */       {
/*  544 */         this.motionX *= 0.5D;
/*  545 */         this.motionY *= 0.0D;
/*  546 */         this.motionZ *= 0.5D;
/*      */       }
/*      */     }
/*      */     
/*  550 */     double d18 = 0.0D;
/*  551 */     double d19 = p_180460_1_.getX() + 0.5D + aint[0][0] * 0.5D;
/*  552 */     double d20 = p_180460_1_.getZ() + 0.5D + aint[0][2] * 0.5D;
/*  553 */     double d21 = p_180460_1_.getX() + 0.5D + aint[1][0] * 0.5D;
/*  554 */     double d10 = p_180460_1_.getZ() + 0.5D + aint[1][2] * 0.5D;
/*  555 */     d1 = d21 - d19;
/*  556 */     d2 = d10 - d20;
/*      */     
/*  558 */     if (d1 == 0.0D)
/*      */     {
/*  560 */       this.posX = (p_180460_1_.getX() + 0.5D);
/*  561 */       d18 = this.posZ - p_180460_1_.getZ();
/*      */     }
/*  563 */     else if (d2 == 0.0D)
/*      */     {
/*  565 */       this.posZ = (p_180460_1_.getZ() + 0.5D);
/*  566 */       d18 = this.posX - p_180460_1_.getX();
/*      */     }
/*      */     else
/*      */     {
/*  570 */       double d11 = this.posX - d19;
/*  571 */       double d12 = this.posZ - d20;
/*  572 */       d18 = (d11 * d1 + d12 * d2) * 2.0D;
/*      */     }
/*      */     
/*  575 */     this.posX = (d19 + d1 * d18);
/*  576 */     this.posZ = (d20 + d2 * d18);
/*  577 */     setPosition(this.posX, this.posY, this.posZ);
/*  578 */     double d22 = this.motionX;
/*  579 */     double d23 = this.motionZ;
/*      */     
/*  581 */     if (this.riddenByEntity != null)
/*      */     {
/*  583 */       d22 *= 0.75D;
/*  584 */       d23 *= 0.75D;
/*      */     }
/*      */     
/*  587 */     double d13 = getMaximumSpeed();
/*  588 */     d22 = MathHelper.clamp_double(d22, -d13, d13);
/*  589 */     d23 = MathHelper.clamp_double(d23, -d13, d13);
/*  590 */     moveEntity(d22, 0.0D, d23);
/*      */     
/*  592 */     if ((aint[0][1] != 0) && (MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[0][0]) && (MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[0][2]))
/*      */     {
/*  594 */       setPosition(this.posX, this.posY + aint[0][1], this.posZ);
/*      */     }
/*  596 */     else if ((aint[1][1] != 0) && (MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[1][0]) && (MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[1][2]))
/*      */     {
/*  598 */       setPosition(this.posX, this.posY + aint[1][1], this.posZ);
/*      */     }
/*      */     
/*  601 */     applyDrag();
/*  602 */     Vec3 vec31 = func_70489_a(this.posX, this.posY, this.posZ);
/*      */     
/*  604 */     if ((vec31 != null) && (vec3 != null))
/*      */     {
/*  606 */       double d14 = (vec3.yCoord - vec31.yCoord) * 0.05D;
/*  607 */       d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */       
/*  609 */       if (d5 > 0.0D)
/*      */       {
/*  611 */         this.motionX = (this.motionX / d5 * (d5 + d14));
/*  612 */         this.motionZ = (this.motionZ / d5 * (d5 + d14));
/*      */       }
/*      */       
/*  615 */       setPosition(this.posX, vec31.yCoord, this.posZ);
/*      */     }
/*      */     
/*  618 */     int j = MathHelper.floor_double(this.posX);
/*  619 */     int i = MathHelper.floor_double(this.posZ);
/*      */     
/*  621 */     if ((j != p_180460_1_.getX()) || (i != p_180460_1_.getZ()))
/*      */     {
/*  623 */       d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*  624 */       this.motionX = (d5 * (j - p_180460_1_.getX()));
/*  625 */       this.motionZ = (d5 * (i - p_180460_1_.getZ()));
/*      */     }
/*      */     
/*  628 */     if (flag)
/*      */     {
/*  630 */       double d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*      */       
/*  632 */       if (d15 > 0.01D)
/*      */       {
/*  634 */         double d16 = 0.06D;
/*  635 */         this.motionX += this.motionX / d15 * d16;
/*  636 */         this.motionZ += this.motionZ / d15 * d16;
/*      */       }
/*  638 */       else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
/*      */       {
/*  640 */         if (this.worldObj.getBlockState(p_180460_1_.west()).getBlock().isNormalCube())
/*      */         {
/*  642 */           this.motionX = 0.02D;
/*      */         }
/*  644 */         else if (this.worldObj.getBlockState(p_180460_1_.east()).getBlock().isNormalCube())
/*      */         {
/*  646 */           this.motionX = -0.02D;
/*      */         }
/*      */       }
/*  649 */       else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
/*      */       {
/*  651 */         if (this.worldObj.getBlockState(p_180460_1_.north()).getBlock().isNormalCube())
/*      */         {
/*  653 */           this.motionZ = 0.02D;
/*      */         }
/*  655 */         else if (this.worldObj.getBlockState(p_180460_1_.south()).getBlock().isNormalCube())
/*      */         {
/*  657 */           this.motionZ = -0.02D;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void applyDrag()
/*      */   {
/*  665 */     if (this.riddenByEntity != null)
/*      */     {
/*  667 */       this.motionX *= 0.996999979019165D;
/*  668 */       this.motionY *= 0.0D;
/*  669 */       this.motionZ *= 0.996999979019165D;
/*      */     }
/*      */     else
/*      */     {
/*  673 */       this.motionX *= 0.9599999785423279D;
/*  674 */       this.motionY *= 0.0D;
/*  675 */       this.motionZ *= 0.9599999785423279D;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPosition(double x, double y, double z)
/*      */   {
/*  684 */     this.posX = x;
/*  685 */     this.posY = y;
/*  686 */     this.posZ = z;
/*  687 */     float f = this.width / 2.0F;
/*  688 */     float f1 = this.height;
/*  689 */     setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f1, z + f));
/*      */   }
/*      */   
/*      */   public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_)
/*      */   {
/*  694 */     int i = MathHelper.floor_double(p_70495_1_);
/*  695 */     int j = MathHelper.floor_double(p_70495_3_);
/*  696 */     int k = MathHelper.floor_double(p_70495_5_);
/*      */     
/*  698 */     if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k)))
/*      */     {
/*  700 */       j--;
/*      */     }
/*      */     
/*  703 */     IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
/*      */     
/*  705 */     if (BlockRailBase.isRailBlock(iblockstate))
/*      */     {
/*  707 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
/*  708 */       p_70495_3_ = j;
/*      */       
/*  710 */       if (blockrailbase$enumraildirection.isAscending())
/*      */       {
/*  712 */         p_70495_3_ = j + 1;
/*      */       }
/*      */       
/*  715 */       int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
/*  716 */       double d0 = aint[1][0] - aint[0][0];
/*  717 */       double d1 = aint[1][2] - aint[0][2];
/*  718 */       double d2 = Math.sqrt(d0 * d0 + d1 * d1);
/*  719 */       d0 /= d2;
/*  720 */       d1 /= d2;
/*  721 */       p_70495_1_ += d0 * p_70495_7_;
/*  722 */       p_70495_5_ += d1 * p_70495_7_;
/*      */       
/*  724 */       if ((aint[0][1] != 0) && (MathHelper.floor_double(p_70495_1_) - i == aint[0][0]) && (MathHelper.floor_double(p_70495_5_) - k == aint[0][2]))
/*      */       {
/*  726 */         p_70495_3_ += aint[0][1];
/*      */       }
/*  728 */       else if ((aint[1][1] != 0) && (MathHelper.floor_double(p_70495_1_) - i == aint[1][0]) && (MathHelper.floor_double(p_70495_5_) - k == aint[1][2]))
/*      */       {
/*  730 */         p_70495_3_ += aint[1][1];
/*      */       }
/*      */       
/*  733 */       return func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
/*      */     }
/*      */     
/*      */ 
/*  737 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_)
/*      */   {
/*  743 */     int i = MathHelper.floor_double(p_70489_1_);
/*  744 */     int j = MathHelper.floor_double(p_70489_3_);
/*  745 */     int k = MathHelper.floor_double(p_70489_5_);
/*      */     
/*  747 */     if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k)))
/*      */     {
/*  749 */       j--;
/*      */     }
/*      */     
/*  752 */     IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
/*      */     
/*  754 */     if (BlockRailBase.isRailBlock(iblockstate))
/*      */     {
/*  756 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
/*  757 */       int[][] aint = matrix[blockrailbase$enumraildirection.getMetadata()];
/*  758 */       double d0 = 0.0D;
/*  759 */       double d1 = i + 0.5D + aint[0][0] * 0.5D;
/*  760 */       double d2 = j + 0.0625D + aint[0][1] * 0.5D;
/*  761 */       double d3 = k + 0.5D + aint[0][2] * 0.5D;
/*  762 */       double d4 = i + 0.5D + aint[1][0] * 0.5D;
/*  763 */       double d5 = j + 0.0625D + aint[1][1] * 0.5D;
/*  764 */       double d6 = k + 0.5D + aint[1][2] * 0.5D;
/*  765 */       double d7 = d4 - d1;
/*  766 */       double d8 = (d5 - d2) * 2.0D;
/*  767 */       double d9 = d6 - d3;
/*      */       
/*  769 */       if (d7 == 0.0D)
/*      */       {
/*  771 */         p_70489_1_ = i + 0.5D;
/*  772 */         d0 = p_70489_5_ - k;
/*      */       }
/*  774 */       else if (d9 == 0.0D)
/*      */       {
/*  776 */         p_70489_5_ = k + 0.5D;
/*  777 */         d0 = p_70489_1_ - i;
/*      */       }
/*      */       else
/*      */       {
/*  781 */         double d10 = p_70489_1_ - d1;
/*  782 */         double d11 = p_70489_5_ - d3;
/*  783 */         d0 = (d10 * d7 + d11 * d9) * 2.0D;
/*      */       }
/*      */       
/*  786 */       p_70489_1_ = d1 + d7 * d0;
/*  787 */       p_70489_3_ = d2 + d8 * d0;
/*  788 */       p_70489_5_ = d3 + d9 * d0;
/*      */       
/*  790 */       if (d8 < 0.0D)
/*      */       {
/*  792 */         p_70489_3_ += 1.0D;
/*      */       }
/*      */       
/*  795 */       if (d8 > 0.0D)
/*      */       {
/*  797 */         p_70489_3_ += 0.5D;
/*      */       }
/*      */       
/*  800 */       return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
/*      */     }
/*      */     
/*      */ 
/*  804 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*      */   {
/*  813 */     if (tagCompund.getBoolean("CustomDisplayTile"))
/*      */     {
/*  815 */       int i = tagCompund.getInteger("DisplayData");
/*      */       
/*  817 */       if (tagCompund.hasKey("DisplayTile", 8))
/*      */       {
/*  819 */         Block block = Block.getBlockFromName(tagCompund.getString("DisplayTile"));
/*      */         
/*  821 */         if (block == null)
/*      */         {
/*  823 */           func_174899_a(Blocks.air.getDefaultState());
/*      */         }
/*      */         else
/*      */         {
/*  827 */           func_174899_a(block.getStateFromMeta(i));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  832 */         Block block1 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));
/*      */         
/*  834 */         if (block1 == null)
/*      */         {
/*  836 */           func_174899_a(Blocks.air.getDefaultState());
/*      */         }
/*      */         else
/*      */         {
/*  840 */           func_174899_a(block1.getStateFromMeta(i));
/*      */         }
/*      */       }
/*      */       
/*  844 */       setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
/*      */     }
/*      */     
/*  847 */     if ((tagCompund.hasKey("CustomName", 8)) && (tagCompund.getString("CustomName").length() > 0))
/*      */     {
/*  849 */       this.entityName = tagCompund.getString("CustomName");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*      */   {
/*  858 */     if (hasDisplayTile())
/*      */     {
/*  860 */       tagCompound.setBoolean("CustomDisplayTile", true);
/*  861 */       IBlockState iblockstate = getDisplayTile();
/*  862 */       ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(iblockstate.getBlock());
/*  863 */       tagCompound.setString("DisplayTile", resourcelocation == null ? "" : resourcelocation.toString());
/*  864 */       tagCompound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
/*  865 */       tagCompound.setInteger("DisplayOffset", getDisplayTileOffset());
/*      */     }
/*      */     
/*  868 */     if ((this.entityName != null) && (this.entityName.length() > 0))
/*      */     {
/*  870 */       tagCompound.setString("CustomName", this.entityName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void applyEntityCollision(Entity entityIn)
/*      */   {
/*  879 */     if (!this.worldObj.isRemote)
/*      */     {
/*  881 */       if ((!entityIn.noClip) && (!this.noClip))
/*      */       {
/*  883 */         if (entityIn != this.riddenByEntity)
/*      */         {
/*  885 */           if (((entityIn instanceof EntityLivingBase)) && (!(entityIn instanceof EntityPlayer)) && (!(entityIn instanceof net.minecraft.entity.monster.EntityIronGolem)) && (getMinecartType() == EnumMinecartType.RIDEABLE) && (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D) && (this.riddenByEntity == null) && (entityIn.ridingEntity == null))
/*      */           {
/*  887 */             entityIn.mountEntity(this);
/*      */           }
/*      */           
/*  890 */           double d0 = entityIn.posX - this.posX;
/*  891 */           double d1 = entityIn.posZ - this.posZ;
/*  892 */           double d2 = d0 * d0 + d1 * d1;
/*      */           
/*  894 */           if (d2 >= 9.999999747378752E-5D)
/*      */           {
/*  896 */             d2 = MathHelper.sqrt_double(d2);
/*  897 */             d0 /= d2;
/*  898 */             d1 /= d2;
/*  899 */             double d3 = 1.0D / d2;
/*      */             
/*  901 */             if (d3 > 1.0D)
/*      */             {
/*  903 */               d3 = 1.0D;
/*      */             }
/*      */             
/*  906 */             d0 *= d3;
/*  907 */             d1 *= d3;
/*  908 */             d0 *= 0.10000000149011612D;
/*  909 */             d1 *= 0.10000000149011612D;
/*  910 */             d0 *= (1.0F - this.entityCollisionReduction);
/*  911 */             d1 *= (1.0F - this.entityCollisionReduction);
/*  912 */             d0 *= 0.5D;
/*  913 */             d1 *= 0.5D;
/*      */             
/*  915 */             if ((entityIn instanceof EntityMinecart))
/*      */             {
/*  917 */               double d4 = entityIn.posX - this.posX;
/*  918 */               double d5 = entityIn.posZ - this.posZ;
/*  919 */               Vec3 vec3 = new Vec3(d4, 0.0D, d5).normalize();
/*  920 */               Vec3 vec31 = new Vec3(MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F), 0.0D, MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F)).normalize();
/*  921 */               double d6 = Math.abs(vec3.dotProduct(vec31));
/*      */               
/*  923 */               if (d6 < 0.800000011920929D)
/*      */               {
/*  925 */                 return;
/*      */               }
/*      */               
/*  928 */               double d7 = entityIn.motionX + this.motionX;
/*  929 */               double d8 = entityIn.motionZ + this.motionZ;
/*      */               
/*  931 */               if ((((EntityMinecart)entityIn).getMinecartType() == EnumMinecartType.FURNACE) && (getMinecartType() != EnumMinecartType.FURNACE))
/*      */               {
/*  933 */                 this.motionX *= 0.20000000298023224D;
/*  934 */                 this.motionZ *= 0.20000000298023224D;
/*  935 */                 addVelocity(entityIn.motionX - d0, 0.0D, entityIn.motionZ - d1);
/*  936 */                 entityIn.motionX *= 0.949999988079071D;
/*  937 */                 entityIn.motionZ *= 0.949999988079071D;
/*      */               }
/*  939 */               else if ((((EntityMinecart)entityIn).getMinecartType() != EnumMinecartType.FURNACE) && (getMinecartType() == EnumMinecartType.FURNACE))
/*      */               {
/*  941 */                 entityIn.motionX *= 0.20000000298023224D;
/*  942 */                 entityIn.motionZ *= 0.20000000298023224D;
/*  943 */                 entityIn.addVelocity(this.motionX + d0, 0.0D, this.motionZ + d1);
/*  944 */                 this.motionX *= 0.949999988079071D;
/*  945 */                 this.motionZ *= 0.949999988079071D;
/*      */               }
/*      */               else
/*      */               {
/*  949 */                 d7 /= 2.0D;
/*  950 */                 d8 /= 2.0D;
/*  951 */                 this.motionX *= 0.20000000298023224D;
/*  952 */                 this.motionZ *= 0.20000000298023224D;
/*  953 */                 addVelocity(d7 - d0, 0.0D, d8 - d1);
/*  954 */                 entityIn.motionX *= 0.20000000298023224D;
/*  955 */                 entityIn.motionZ *= 0.20000000298023224D;
/*  956 */                 entityIn.addVelocity(d7 + d0, 0.0D, d8 + d1);
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  961 */               addVelocity(-d0, 0.0D, -d1);
/*  962 */               entityIn.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
/*      */   {
/*  972 */     this.minecartX = x;
/*  973 */     this.minecartY = y;
/*  974 */     this.minecartZ = z;
/*  975 */     this.minecartYaw = yaw;
/*  976 */     this.minecartPitch = pitch;
/*  977 */     this.turnProgress = (posRotationIncrements + 2);
/*  978 */     this.motionX = this.velocityX;
/*  979 */     this.motionY = this.velocityY;
/*  980 */     this.motionZ = this.velocityZ;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setVelocity(double x, double y, double z)
/*      */   {
/*  988 */     this.velocityX = (this.motionX = x);
/*  989 */     this.velocityY = (this.motionY = y);
/*  990 */     this.velocityZ = (this.motionZ = z);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDamage(float p_70492_1_)
/*      */   {
/*  999 */     this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getDamage()
/*      */   {
/* 1008 */     return this.dataWatcher.getWatchableObjectFloat(19);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRollingAmplitude(int p_70497_1_)
/*      */   {
/* 1016 */     this.dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRollingAmplitude()
/*      */   {
/* 1024 */     return this.dataWatcher.getWatchableObjectInt(17);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRollingDirection(int p_70494_1_)
/*      */   {
/* 1032 */     this.dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRollingDirection()
/*      */   {
/* 1040 */     return this.dataWatcher.getWatchableObjectInt(18);
/*      */   }
/*      */   
/*      */   public abstract EnumMinecartType getMinecartType();
/*      */   
/*      */   public IBlockState getDisplayTile()
/*      */   {
/* 1047 */     return !hasDisplayTile() ? getDefaultDisplayTile() : Block.getStateById(getDataWatcher().getWatchableObjectInt(20));
/*      */   }
/*      */   
/*      */   public IBlockState getDefaultDisplayTile()
/*      */   {
/* 1052 */     return Blocks.air.getDefaultState();
/*      */   }
/*      */   
/*      */   public int getDisplayTileOffset()
/*      */   {
/* 1057 */     return !hasDisplayTile() ? getDefaultDisplayTileOffset() : getDataWatcher().getWatchableObjectInt(21);
/*      */   }
/*      */   
/*      */   public int getDefaultDisplayTileOffset()
/*      */   {
/* 1062 */     return 6;
/*      */   }
/*      */   
/*      */   public void func_174899_a(IBlockState p_174899_1_)
/*      */   {
/* 1067 */     getDataWatcher().updateObject(20, Integer.valueOf(Block.getStateId(p_174899_1_)));
/* 1068 */     setHasDisplayTile(true);
/*      */   }
/*      */   
/*      */   public void setDisplayTileOffset(int p_94086_1_)
/*      */   {
/* 1073 */     getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
/* 1074 */     setHasDisplayTile(true);
/*      */   }
/*      */   
/*      */   public boolean hasDisplayTile()
/*      */   {
/* 1079 */     return getDataWatcher().getWatchableObjectByte(22) == 1;
/*      */   }
/*      */   
/*      */   public void setHasDisplayTile(boolean p_94096_1_)
/*      */   {
/* 1084 */     getDataWatcher().updateObject(22, Byte.valueOf((byte)(p_94096_1_ ? 1 : 0)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCustomNameTag(String name)
/*      */   {
/* 1092 */     this.entityName = name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/* 1100 */     return this.entityName != null ? this.entityName : super.getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasCustomName()
/*      */   {
/* 1108 */     return this.entityName != null;
/*      */   }
/*      */   
/*      */   public String getCustomNameTag()
/*      */   {
/* 1113 */     return this.entityName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public net.minecraft.util.IChatComponent getDisplayName()
/*      */   {
/* 1121 */     if (hasCustomName())
/*      */     {
/* 1123 */       ChatComponentText chatcomponenttext = new ChatComponentText(this.entityName);
/* 1124 */       chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 1125 */       chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
/* 1126 */       return chatcomponenttext;
/*      */     }
/*      */     
/*      */ 
/* 1130 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(getName(), new Object[0]);
/* 1131 */     chatcomponenttranslation.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 1132 */     chatcomponenttranslation.getChatStyle().setInsertion(getUniqueID().toString());
/* 1133 */     return chatcomponenttranslation;
/*      */   }
/*      */   
/*      */ 
/*      */   public static enum EnumMinecartType
/*      */   {
/* 1139 */     RIDEABLE(0, "MinecartRideable"), 
/* 1140 */     CHEST(1, "MinecartChest"), 
/* 1141 */     FURNACE(2, "MinecartFurnace"), 
/* 1142 */     TNT(3, "MinecartTNT"), 
/* 1143 */     SPAWNER(4, "MinecartSpawner"), 
/* 1144 */     HOPPER(5, "MinecartHopper"), 
/* 1145 */     COMMAND_BLOCK(6, "MinecartCommandBlock");
/*      */     
/*      */     private static final Map<Integer, EnumMinecartType> ID_LOOKUP;
/*      */     private final int networkID;
/*      */     private final String name;
/*      */     
/*      */     private EnumMinecartType(int networkID, String name)
/*      */     {
/* 1153 */       this.networkID = networkID;
/* 1154 */       this.name = name;
/*      */     }
/*      */     
/*      */     public int getNetworkID()
/*      */     {
/* 1159 */       return this.networkID;
/*      */     }
/*      */     
/*      */     public String getName()
/*      */     {
/* 1164 */       return this.name;
/*      */     }
/*      */     
/*      */     public static EnumMinecartType byNetworkID(int id)
/*      */     {
/* 1169 */       EnumMinecartType entityminecart$enumminecarttype = (EnumMinecartType)ID_LOOKUP.get(Integer.valueOf(id));
/* 1170 */       return entityminecart$enumminecarttype == null ? RIDEABLE : entityminecart$enumminecarttype;
/*      */     }
/*      */     
/*      */     static
/*      */     {
/* 1147 */       ID_LOOKUP = Maps.newHashMap();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       EnumMinecartType[] arrayOfEnumMinecartType;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1174 */       int j = (arrayOfEnumMinecartType = values()).length; for (int i = 0; i < j; i++) { EnumMinecartType entityminecart$enumminecarttype = arrayOfEnumMinecartType[i];
/*      */         
/* 1176 */         ID_LOOKUP.put(Integer.valueOf(entityminecart$enumminecarttype.getNetworkID()), entityminecart$enumminecarttype);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */