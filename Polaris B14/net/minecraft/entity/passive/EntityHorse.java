/*      */ package net.minecraft.entity.passive;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.Block.SoundType;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.entity.DataWatcher;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityAgeable;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.IEntityLivingData;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.EntityAIMate;
/*      */ import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
/*      */ import net.minecraft.entity.ai.EntityAITasks;
/*      */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*      */ import net.minecraft.entity.ai.attributes.IAttribute;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.ai.attributes.RangedAttribute;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.AnimalChest;
/*      */ import net.minecraft.inventory.InventoryBasic;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.pathfinding.PathNavigateGround;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.StatCollector;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public class EntityHorse extends EntityAnimal implements net.minecraft.inventory.IInvBasic
/*      */ {
/*   45 */   private static final Predicate<Entity> horseBreedingSelector = new Predicate()
/*      */   {
/*      */     public boolean apply(Entity p_apply_1_)
/*      */     {
/*   49 */       return ((p_apply_1_ instanceof EntityHorse)) && (((EntityHorse)p_apply_1_).isBreeding());
/*      */     }
/*      */   };
/*   52 */   private static final IAttribute horseJumpStrength = new RangedAttribute(null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D).setDescription("Jump Strength").setShouldWatch(true);
/*   53 */   private static final String[] horseArmorTextures = { 0, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png" };
/*   54 */   private static final String[] HORSE_ARMOR_TEXTURES_ABBR = { "", "meo", "goo", "dio" };
/*   55 */   private static final int[] armorValues = { 0, 5, 7, 11 };
/*   56 */   private static final String[] horseTextures = { "textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png" };
/*   57 */   private static final String[] HORSE_TEXTURES_ABBR = { "hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb" };
/*   58 */   private static final String[] horseMarkingTextures = { 0, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png" };
/*   59 */   private static final String[] HORSE_MARKING_TEXTURES_ABBR = { "", "wo_", "wmo", "wdo", "bdo" };
/*      */   
/*      */   private int eatingHaystackCounter;
/*      */   
/*      */   private int openMouthCounter;
/*      */   
/*      */   private int jumpRearingCounter;
/*      */   
/*      */   public int field_110278_bp;
/*      */   
/*      */   public int field_110279_bq;
/*      */   
/*      */   protected boolean horseJumping;
/*      */   private AnimalChest horseChest;
/*      */   private boolean hasReproduced;
/*      */   protected int temper;
/*      */   protected float jumpPower;
/*      */   private boolean field_110294_bI;
/*      */   private float headLean;
/*      */   private float prevHeadLean;
/*      */   private float rearingAmount;
/*      */   private float prevRearingAmount;
/*      */   private float mouthOpenness;
/*      */   private float prevMouthOpenness;
/*      */   private int gallopTime;
/*      */   private String texturePrefix;
/*   85 */   private String[] horseTexturesArray = new String[3];
/*   86 */   private boolean field_175508_bO = false;
/*      */   
/*      */   public EntityHorse(World worldIn)
/*      */   {
/*   90 */     super(worldIn);
/*   91 */     setSize(1.4F, 1.6F);
/*   92 */     this.isImmuneToFire = false;
/*   93 */     setChested(false);
/*   94 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*   95 */     this.tasks.addTask(0, new net.minecraft.entity.ai.EntityAISwimming(this));
/*   96 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIPanic(this, 1.2D));
/*   97 */     this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
/*   98 */     this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
/*   99 */     this.tasks.addTask(4, new net.minecraft.entity.ai.EntityAIFollowParent(this, 1.0D));
/*  100 */     this.tasks.addTask(6, new net.minecraft.entity.ai.EntityAIWander(this, 0.7D));
/*  101 */     this.tasks.addTask(7, new net.minecraft.entity.ai.EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
/*  102 */     this.tasks.addTask(8, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  103 */     initHorseChest();
/*      */   }
/*      */   
/*      */   protected void entityInit()
/*      */   {
/*  108 */     super.entityInit();
/*  109 */     this.dataWatcher.addObject(16, Integer.valueOf(0));
/*  110 */     this.dataWatcher.addObject(19, Byte.valueOf((byte)0));
/*  111 */     this.dataWatcher.addObject(20, Integer.valueOf(0));
/*  112 */     this.dataWatcher.addObject(21, String.valueOf(""));
/*  113 */     this.dataWatcher.addObject(22, Integer.valueOf(0));
/*      */   }
/*      */   
/*      */   public void setHorseType(int type)
/*      */   {
/*  118 */     this.dataWatcher.updateObject(19, Byte.valueOf((byte)type));
/*  119 */     resetTexturePrefix();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getHorseType()
/*      */   {
/*  127 */     return this.dataWatcher.getWatchableObjectByte(19);
/*      */   }
/*      */   
/*      */   public void setHorseVariant(int variant)
/*      */   {
/*  132 */     this.dataWatcher.updateObject(20, Integer.valueOf(variant));
/*  133 */     resetTexturePrefix();
/*      */   }
/*      */   
/*      */   public int getHorseVariant()
/*      */   {
/*  138 */     return this.dataWatcher.getWatchableObjectInt(20);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  146 */     if (hasCustomName())
/*      */     {
/*  148 */       return getCustomNameTag();
/*      */     }
/*      */     
/*      */ 
/*  152 */     int i = getHorseType();
/*      */     
/*  154 */     switch (i)
/*      */     {
/*      */     case 0: 
/*      */     default: 
/*  158 */       return StatCollector.translateToLocal("entity.horse.name");
/*      */     
/*      */     case 1: 
/*  161 */       return StatCollector.translateToLocal("entity.donkey.name");
/*      */     
/*      */     case 2: 
/*  164 */       return StatCollector.translateToLocal("entity.mule.name");
/*      */     
/*      */     case 3: 
/*  167 */       return StatCollector.translateToLocal("entity.zombiehorse.name");
/*      */     }
/*      */     
/*  170 */     return StatCollector.translateToLocal("entity.skeletonhorse.name");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean getHorseWatchableBoolean(int p_110233_1_)
/*      */   {
/*  177 */     return (this.dataWatcher.getWatchableObjectInt(16) & p_110233_1_) != 0;
/*      */   }
/*      */   
/*      */   private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_)
/*      */   {
/*  182 */     int i = this.dataWatcher.getWatchableObjectInt(16);
/*      */     
/*  184 */     if (p_110208_2_)
/*      */     {
/*  186 */       this.dataWatcher.updateObject(16, Integer.valueOf(i | p_110208_1_));
/*      */     }
/*      */     else
/*      */     {
/*  190 */       this.dataWatcher.updateObject(16, Integer.valueOf(i & (p_110208_1_ ^ 0xFFFFFFFF)));
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isAdultHorse()
/*      */   {
/*  196 */     return !isChild();
/*      */   }
/*      */   
/*      */   public boolean isTame()
/*      */   {
/*  201 */     return getHorseWatchableBoolean(2);
/*      */   }
/*      */   
/*      */   public boolean func_110253_bW()
/*      */   {
/*  206 */     return isAdultHorse();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getOwnerId()
/*      */   {
/*  214 */     return this.dataWatcher.getWatchableObjectString(21);
/*      */   }
/*      */   
/*      */   public void setOwnerId(String id)
/*      */   {
/*  219 */     this.dataWatcher.updateObject(21, id);
/*      */   }
/*      */   
/*      */   public float getHorseSize()
/*      */   {
/*  224 */     return 0.5F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScaleForAge(boolean p_98054_1_)
/*      */   {
/*  232 */     if (p_98054_1_)
/*      */     {
/*  234 */       setScale(getHorseSize());
/*      */     }
/*      */     else
/*      */     {
/*  238 */       setScale(1.0F);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isHorseJumping()
/*      */   {
/*  244 */     return this.horseJumping;
/*      */   }
/*      */   
/*      */   public void setHorseTamed(boolean tamed)
/*      */   {
/*  249 */     setHorseWatchableBoolean(2, tamed);
/*      */   }
/*      */   
/*      */   public void setHorseJumping(boolean jumping)
/*      */   {
/*  254 */     this.horseJumping = jumping;
/*      */   }
/*      */   
/*      */   public boolean allowLeashing()
/*      */   {
/*  259 */     return (!isUndead()) && (super.allowLeashing());
/*      */   }
/*      */   
/*      */   protected void func_142017_o(float p_142017_1_)
/*      */   {
/*  264 */     if ((p_142017_1_ > 6.0F) && (isEatingHaystack()))
/*      */     {
/*  266 */       setEatingHaystack(false);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isChested()
/*      */   {
/*  272 */     return getHorseWatchableBoolean(8);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getHorseArmorIndexSynced()
/*      */   {
/*  280 */     return this.dataWatcher.getWatchableObjectInt(22);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getHorseArmorIndex(ItemStack itemStackIn)
/*      */   {
/*  288 */     if (itemStackIn == null)
/*      */     {
/*  290 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  294 */     Item item = itemStackIn.getItem();
/*  295 */     return item == Items.diamond_horse_armor ? 3 : item == Items.golden_horse_armor ? 2 : item == Items.iron_horse_armor ? 1 : 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isEatingHaystack()
/*      */   {
/*  301 */     return getHorseWatchableBoolean(32);
/*      */   }
/*      */   
/*      */   public boolean isRearing()
/*      */   {
/*  306 */     return getHorseWatchableBoolean(64);
/*      */   }
/*      */   
/*      */   public boolean isBreeding()
/*      */   {
/*  311 */     return getHorseWatchableBoolean(16);
/*      */   }
/*      */   
/*      */   public boolean getHasReproduced()
/*      */   {
/*  316 */     return this.hasReproduced;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHorseArmorStack(ItemStack itemStackIn)
/*      */   {
/*  324 */     this.dataWatcher.updateObject(22, Integer.valueOf(getHorseArmorIndex(itemStackIn)));
/*  325 */     resetTexturePrefix();
/*      */   }
/*      */   
/*      */   public void setBreeding(boolean breeding)
/*      */   {
/*  330 */     setHorseWatchableBoolean(16, breeding);
/*      */   }
/*      */   
/*      */   public void setChested(boolean chested)
/*      */   {
/*  335 */     setHorseWatchableBoolean(8, chested);
/*      */   }
/*      */   
/*      */   public void setHasReproduced(boolean hasReproducedIn)
/*      */   {
/*  340 */     this.hasReproduced = hasReproducedIn;
/*      */   }
/*      */   
/*      */   public void setHorseSaddled(boolean saddled)
/*      */   {
/*  345 */     setHorseWatchableBoolean(4, saddled);
/*      */   }
/*      */   
/*      */   public int getTemper()
/*      */   {
/*  350 */     return this.temper;
/*      */   }
/*      */   
/*      */   public void setTemper(int temperIn)
/*      */   {
/*  355 */     this.temper = temperIn;
/*      */   }
/*      */   
/*      */   public int increaseTemper(int p_110198_1_)
/*      */   {
/*  360 */     int i = MathHelper.clamp_int(getTemper() + p_110198_1_, 0, getMaxTemper());
/*  361 */     setTemper(i);
/*  362 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean attackEntityFrom(DamageSource source, float amount)
/*      */   {
/*  370 */     Entity entity = source.getEntity();
/*  371 */     return (this.riddenByEntity != null) && (this.riddenByEntity.equals(entity)) ? false : super.attackEntityFrom(source, amount);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTotalArmorValue()
/*      */   {
/*  379 */     return armorValues[getHorseArmorIndexSynced()];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBePushed()
/*      */   {
/*  387 */     return this.riddenByEntity == null;
/*      */   }
/*      */   
/*      */   public boolean prepareChunkForSpawn()
/*      */   {
/*  392 */     int i = MathHelper.floor_double(this.posX);
/*  393 */     int j = MathHelper.floor_double(this.posZ);
/*  394 */     this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, j));
/*  395 */     return true;
/*      */   }
/*      */   
/*      */   public void dropChests()
/*      */   {
/*  400 */     if ((!this.worldObj.isRemote) && (isChested()))
/*      */     {
/*  402 */       dropItem(Item.getItemFromBlock(Blocks.chest), 1);
/*  403 */       setChested(false);
/*      */     }
/*      */   }
/*      */   
/*      */   private void func_110266_cB()
/*      */   {
/*  409 */     openHorseMouth();
/*      */     
/*  411 */     if (!isSilent())
/*      */     {
/*  413 */       this.worldObj.playSoundAtEntity(this, "eating", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/*      */     }
/*      */   }
/*      */   
/*      */   public void fall(float distance, float damageMultiplier)
/*      */   {
/*  419 */     if (distance > 1.0F)
/*      */     {
/*  421 */       playSound("mob.horse.land", 0.4F, 1.0F);
/*      */     }
/*      */     
/*  424 */     int i = MathHelper.ceiling_float_int((distance * 0.5F - 3.0F) * damageMultiplier);
/*      */     
/*  426 */     if (i > 0)
/*      */     {
/*  428 */       attackEntityFrom(DamageSource.fall, i);
/*      */       
/*  430 */       if (this.riddenByEntity != null)
/*      */       {
/*  432 */         this.riddenByEntity.attackEntityFrom(DamageSource.fall, i);
/*      */       }
/*      */       
/*  435 */       Block block = this.worldObj.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - this.prevRotationYaw, this.posZ)).getBlock();
/*      */       
/*  437 */       if ((block.getMaterial() != Material.air) && (!isSilent()))
/*      */       {
/*  439 */         Block.SoundType block$soundtype = block.stepSound;
/*  440 */         this.worldObj.playSoundAtEntity(this, block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.5F, block$soundtype.getFrequency() * 0.75F);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getChestSize()
/*      */   {
/*  450 */     int i = getHorseType();
/*  451 */     return (!isChested()) || ((i != 1) && (i != 2)) ? 2 : 17;
/*      */   }
/*      */   
/*      */   private void initHorseChest()
/*      */   {
/*  456 */     AnimalChest animalchest = this.horseChest;
/*  457 */     this.horseChest = new AnimalChest("HorseChest", getChestSize());
/*  458 */     this.horseChest.setCustomName(getName());
/*      */     
/*  460 */     if (animalchest != null)
/*      */     {
/*  462 */       animalchest.func_110132_b(this);
/*  463 */       int i = Math.min(animalchest.getSizeInventory(), this.horseChest.getSizeInventory());
/*      */       
/*  465 */       for (int j = 0; j < i; j++)
/*      */       {
/*  467 */         ItemStack itemstack = animalchest.getStackInSlot(j);
/*      */         
/*  469 */         if (itemstack != null)
/*      */         {
/*  471 */           this.horseChest.setInventorySlotContents(j, itemstack.copy());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  476 */     this.horseChest.func_110134_a(this);
/*  477 */     updateHorseSlots();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateHorseSlots()
/*      */   {
/*  485 */     if (!this.worldObj.isRemote)
/*      */     {
/*  487 */       setHorseSaddled(this.horseChest.getStackInSlot(0) != null);
/*      */       
/*  489 */       if (canWearArmor())
/*      */       {
/*  491 */         setHorseArmorStack(this.horseChest.getStackInSlot(1));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onInventoryChanged(InventoryBasic p_76316_1_)
/*      */   {
/*  501 */     int i = getHorseArmorIndexSynced();
/*  502 */     boolean flag = isHorseSaddled();
/*  503 */     updateHorseSlots();
/*      */     
/*  505 */     if (this.ticksExisted > 20)
/*      */     {
/*  507 */       if ((i == 0) && (i != getHorseArmorIndexSynced()))
/*      */       {
/*  509 */         playSound("mob.horse.armor", 0.5F, 1.0F);
/*      */       }
/*  511 */       else if (i != getHorseArmorIndexSynced())
/*      */       {
/*  513 */         playSound("mob.horse.armor", 0.5F, 1.0F);
/*      */       }
/*      */       
/*  516 */       if ((!flag) && (isHorseSaddled()))
/*      */       {
/*  518 */         playSound("mob.horse.leather", 0.5F, 1.0F);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCanSpawnHere()
/*      */   {
/*  528 */     prepareChunkForSpawn();
/*  529 */     return super.getCanSpawnHere();
/*      */   }
/*      */   
/*      */   protected EntityHorse getClosestHorse(Entity entityIn, double distance)
/*      */   {
/*  534 */     double d0 = Double.MAX_VALUE;
/*  535 */     Entity entity = null;
/*      */     
/*  537 */     for (Entity entity1 : this.worldObj.getEntitiesInAABBexcluding(entityIn, entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), horseBreedingSelector))
/*      */     {
/*  539 */       double d1 = entity1.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);
/*      */       
/*  541 */       if (d1 < d0)
/*      */       {
/*  543 */         entity = entity1;
/*  544 */         d0 = d1;
/*      */       }
/*      */     }
/*      */     
/*  548 */     return (EntityHorse)entity;
/*      */   }
/*      */   
/*      */   public double getHorseJumpStrength()
/*      */   {
/*  553 */     return getEntityAttribute(horseJumpStrength).getAttributeValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDeathSound()
/*      */   {
/*  561 */     openHorseMouth();
/*  562 */     int i = getHorseType();
/*  563 */     return (i != 1) && (i != 2) ? "mob.horse.death" : i == 4 ? "mob.horse.skeleton.death" : i == 3 ? "mob.horse.zombie.death" : "mob.horse.donkey.death";
/*      */   }
/*      */   
/*      */   protected Item getDropItem()
/*      */   {
/*  568 */     boolean flag = this.rand.nextInt(4) == 0;
/*  569 */     int i = getHorseType();
/*  570 */     return i == 3 ? Items.rotten_flesh : flag ? null : i == 4 ? Items.bone : Items.leather;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getHurtSound()
/*      */   {
/*  578 */     openHorseMouth();
/*      */     
/*  580 */     if (this.rand.nextInt(3) == 0)
/*      */     {
/*  582 */       makeHorseRear();
/*      */     }
/*      */     
/*  585 */     int i = getHorseType();
/*  586 */     return (i != 1) && (i != 2) ? "mob.horse.hit" : i == 4 ? "mob.horse.skeleton.hit" : i == 3 ? "mob.horse.zombie.hit" : "mob.horse.donkey.hit";
/*      */   }
/*      */   
/*      */   public boolean isHorseSaddled()
/*      */   {
/*  591 */     return getHorseWatchableBoolean(4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getLivingSound()
/*      */   {
/*  599 */     openHorseMouth();
/*      */     
/*  601 */     if ((this.rand.nextInt(10) == 0) && (!isMovementBlocked()))
/*      */     {
/*  603 */       makeHorseRear();
/*      */     }
/*      */     
/*  606 */     int i = getHorseType();
/*  607 */     return (i != 1) && (i != 2) ? "mob.horse.idle" : i == 4 ? "mob.horse.skeleton.idle" : i == 3 ? "mob.horse.zombie.idle" : "mob.horse.donkey.idle";
/*      */   }
/*      */   
/*      */   protected String getAngrySoundName()
/*      */   {
/*  612 */     openHorseMouth();
/*  613 */     makeHorseRear();
/*  614 */     int i = getHorseType();
/*  615 */     return (i != 3) && (i != 4) ? "mob.horse.donkey.angry" : (i != 1) && (i != 2) ? "mob.horse.angry" : null;
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, Block blockIn)
/*      */   {
/*  620 */     Block.SoundType block$soundtype = blockIn.stepSound;
/*      */     
/*  622 */     if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer)
/*      */     {
/*  624 */       block$soundtype = Blocks.snow_layer.stepSound;
/*      */     }
/*      */     
/*  627 */     if (!blockIn.getMaterial().isLiquid())
/*      */     {
/*  629 */       int i = getHorseType();
/*      */       
/*  631 */       if ((this.riddenByEntity != null) && (i != 1) && (i != 2))
/*      */       {
/*  633 */         this.gallopTime += 1;
/*      */         
/*  635 */         if ((this.gallopTime > 5) && (this.gallopTime % 3 == 0))
/*      */         {
/*  637 */           playSound("mob.horse.gallop", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */           
/*  639 */           if ((i == 0) && (this.rand.nextInt(10) == 0))
/*      */           {
/*  641 */             playSound("mob.horse.breathe", block$soundtype.getVolume() * 0.6F, block$soundtype.getFrequency());
/*      */           }
/*      */         }
/*  644 */         else if (this.gallopTime <= 5)
/*      */         {
/*  646 */           playSound("mob.horse.wood", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */         }
/*      */       }
/*  649 */       else if (block$soundtype == Block.soundTypeWood)
/*      */       {
/*  651 */         playSound("mob.horse.wood", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */       }
/*      */       else
/*      */       {
/*  655 */         playSound("mob.horse.soft", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void applyEntityAttributes()
/*      */   {
/*  662 */     super.applyEntityAttributes();
/*  663 */     getAttributeMap().registerAttribute(horseJumpStrength);
/*  664 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0D);
/*  665 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22499999403953552D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxSpawnedInChunk()
/*      */   {
/*  673 */     return 6;
/*      */   }
/*      */   
/*      */   public int getMaxTemper()
/*      */   {
/*  678 */     return 100;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float getSoundVolume()
/*      */   {
/*  686 */     return 0.8F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTalkInterval()
/*      */   {
/*  694 */     return 400;
/*      */   }
/*      */   
/*      */   public boolean func_110239_cn()
/*      */   {
/*  699 */     return (getHorseType() == 0) || (getHorseArmorIndexSynced() > 0);
/*      */   }
/*      */   
/*      */   private void resetTexturePrefix()
/*      */   {
/*  704 */     this.texturePrefix = null;
/*      */   }
/*      */   
/*      */   public boolean func_175507_cI()
/*      */   {
/*  709 */     return this.field_175508_bO;
/*      */   }
/*      */   
/*      */   private void setHorseTexturePaths()
/*      */   {
/*  714 */     this.texturePrefix = "horse/";
/*  715 */     this.horseTexturesArray[0] = null;
/*  716 */     this.horseTexturesArray[1] = null;
/*  717 */     this.horseTexturesArray[2] = null;
/*  718 */     int i = getHorseType();
/*  719 */     int j = getHorseVariant();
/*      */     
/*  721 */     if (i == 0)
/*      */     {
/*  723 */       int k = j & 0xFF;
/*  724 */       int l = (j & 0xFF00) >> 8;
/*      */       
/*  726 */       if (k >= horseTextures.length)
/*      */       {
/*  728 */         this.field_175508_bO = false;
/*  729 */         return;
/*      */       }
/*      */       
/*  732 */       this.horseTexturesArray[0] = horseTextures[k];
/*  733 */       this.texturePrefix += HORSE_TEXTURES_ABBR[k];
/*      */       
/*  735 */       if (l >= horseMarkingTextures.length)
/*      */       {
/*  737 */         this.field_175508_bO = false;
/*  738 */         return;
/*      */       }
/*      */       
/*  741 */       this.horseTexturesArray[1] = horseMarkingTextures[l];
/*  742 */       this.texturePrefix += HORSE_MARKING_TEXTURES_ABBR[l];
/*      */     }
/*      */     else
/*      */     {
/*  746 */       this.horseTexturesArray[0] = "";
/*  747 */       this.texturePrefix = (this.texturePrefix + "_" + i + "_");
/*      */     }
/*      */     
/*  750 */     int i1 = getHorseArmorIndexSynced();
/*      */     
/*  752 */     if (i1 >= horseArmorTextures.length)
/*      */     {
/*  754 */       this.field_175508_bO = false;
/*      */     }
/*      */     else
/*      */     {
/*  758 */       this.horseTexturesArray[2] = horseArmorTextures[i1];
/*  759 */       this.texturePrefix += HORSE_ARMOR_TEXTURES_ABBR[i1];
/*  760 */       this.field_175508_bO = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public String getHorseTexture()
/*      */   {
/*  766 */     if (this.texturePrefix == null)
/*      */     {
/*  768 */       setHorseTexturePaths();
/*      */     }
/*      */     
/*  771 */     return this.texturePrefix;
/*      */   }
/*      */   
/*      */   public String[] getVariantTexturePaths()
/*      */   {
/*  776 */     if (this.texturePrefix == null)
/*      */     {
/*  778 */       setHorseTexturePaths();
/*      */     }
/*      */     
/*  781 */     return this.horseTexturesArray;
/*      */   }
/*      */   
/*      */   public void openGUI(EntityPlayer playerEntity)
/*      */   {
/*  786 */     if ((!this.worldObj.isRemote) && ((this.riddenByEntity == null) || (this.riddenByEntity == playerEntity)) && (isTame()))
/*      */     {
/*  788 */       this.horseChest.setCustomName(getName());
/*  789 */       playerEntity.displayGUIHorse(this, this.horseChest);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean interact(EntityPlayer player)
/*      */   {
/*  798 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*      */     
/*  800 */     if ((itemstack != null) && (itemstack.getItem() == Items.spawn_egg))
/*      */     {
/*  802 */       return super.interact(player);
/*      */     }
/*  804 */     if ((!isTame()) && (isUndead()))
/*      */     {
/*  806 */       return false;
/*      */     }
/*  808 */     if ((isTame()) && (isAdultHorse()) && (player.isSneaking()))
/*      */     {
/*  810 */       openGUI(player);
/*  811 */       return true;
/*      */     }
/*  813 */     if ((func_110253_bW()) && (this.riddenByEntity != null))
/*      */     {
/*  815 */       return super.interact(player);
/*      */     }
/*      */     
/*      */ 
/*  819 */     if (itemstack != null)
/*      */     {
/*  821 */       boolean flag = false;
/*      */       
/*  823 */       if (canWearArmor())
/*      */       {
/*  825 */         int i = -1;
/*      */         
/*  827 */         if (itemstack.getItem() == Items.iron_horse_armor)
/*      */         {
/*  829 */           i = 1;
/*      */         }
/*  831 */         else if (itemstack.getItem() == Items.golden_horse_armor)
/*      */         {
/*  833 */           i = 2;
/*      */         }
/*  835 */         else if (itemstack.getItem() == Items.diamond_horse_armor)
/*      */         {
/*  837 */           i = 3;
/*      */         }
/*      */         
/*  840 */         if (i >= 0)
/*      */         {
/*  842 */           if (!isTame())
/*      */           {
/*  844 */             makeHorseRearWithSound();
/*  845 */             return true;
/*      */           }
/*      */           
/*  848 */           openGUI(player);
/*  849 */           return true;
/*      */         }
/*      */       }
/*      */       
/*  853 */       if ((!flag) && (!isUndead()))
/*      */       {
/*  855 */         float f = 0.0F;
/*  856 */         int j = 0;
/*  857 */         int k = 0;
/*      */         
/*  859 */         if (itemstack.getItem() == Items.wheat)
/*      */         {
/*  861 */           f = 2.0F;
/*  862 */           j = 20;
/*  863 */           k = 3;
/*      */         }
/*  865 */         else if (itemstack.getItem() == Items.sugar)
/*      */         {
/*  867 */           f = 1.0F;
/*  868 */           j = 30;
/*  869 */           k = 3;
/*      */         }
/*  871 */         else if (Block.getBlockFromItem(itemstack.getItem()) == Blocks.hay_block)
/*      */         {
/*  873 */           f = 20.0F;
/*  874 */           j = 180;
/*      */         }
/*  876 */         else if (itemstack.getItem() == Items.apple)
/*      */         {
/*  878 */           f = 3.0F;
/*  879 */           j = 60;
/*  880 */           k = 3;
/*      */         }
/*  882 */         else if (itemstack.getItem() == Items.golden_carrot)
/*      */         {
/*  884 */           f = 4.0F;
/*  885 */           j = 60;
/*  886 */           k = 5;
/*      */           
/*  888 */           if ((isTame()) && (getGrowingAge() == 0))
/*      */           {
/*  890 */             flag = true;
/*  891 */             setInLove(player);
/*      */           }
/*      */         }
/*  894 */         else if (itemstack.getItem() == Items.golden_apple)
/*      */         {
/*  896 */           f = 10.0F;
/*  897 */           j = 240;
/*  898 */           k = 10;
/*      */           
/*  900 */           if ((isTame()) && (getGrowingAge() == 0))
/*      */           {
/*  902 */             flag = true;
/*  903 */             setInLove(player);
/*      */           }
/*      */         }
/*      */         
/*  907 */         if ((getHealth() < getMaxHealth()) && (f > 0.0F))
/*      */         {
/*  909 */           heal(f);
/*  910 */           flag = true;
/*      */         }
/*      */         
/*  913 */         if ((!isAdultHorse()) && (j > 0))
/*      */         {
/*  915 */           addGrowth(j);
/*  916 */           flag = true;
/*      */         }
/*      */         
/*  919 */         if ((k > 0) && ((flag) || (!isTame())) && (k < getMaxTemper()))
/*      */         {
/*  921 */           flag = true;
/*  922 */           increaseTemper(k);
/*      */         }
/*      */         
/*  925 */         if (flag)
/*      */         {
/*  927 */           func_110266_cB();
/*      */         }
/*      */       }
/*      */       
/*  931 */       if ((!isTame()) && (!flag))
/*      */       {
/*  933 */         if ((itemstack != null) && (itemstack.interactWithEntity(player, this)))
/*      */         {
/*  935 */           return true;
/*      */         }
/*      */         
/*  938 */         makeHorseRearWithSound();
/*  939 */         return true;
/*      */       }
/*      */       
/*  942 */       if ((!flag) && (canCarryChest()) && (!isChested()) && (itemstack.getItem() == Item.getItemFromBlock(Blocks.chest)))
/*      */       {
/*  944 */         setChested(true);
/*  945 */         playSound("mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*  946 */         flag = true;
/*  947 */         initHorseChest();
/*      */       }
/*      */       
/*  950 */       if ((!flag) && (func_110253_bW()) && (!isHorseSaddled()) && (itemstack.getItem() == Items.saddle))
/*      */       {
/*  952 */         openGUI(player);
/*  953 */         return true;
/*      */       }
/*      */       
/*  956 */       if (flag)
/*      */       {
/*  958 */         if (!player.capabilities.isCreativeMode) { if (--itemstack.stackSize == 0)
/*      */           {
/*  960 */             player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*      */           }
/*      */         }
/*  963 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  967 */     if ((func_110253_bW()) && (this.riddenByEntity == null))
/*      */     {
/*  969 */       if ((itemstack != null) && (itemstack.interactWithEntity(player, this)))
/*      */       {
/*  971 */         return true;
/*      */       }
/*      */       
/*      */ 
/*  975 */       mountTo(player);
/*  976 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  981 */     return super.interact(player);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void mountTo(EntityPlayer player)
/*      */   {
/*  988 */     player.rotationYaw = this.rotationYaw;
/*  989 */     player.rotationPitch = this.rotationPitch;
/*  990 */     setEatingHaystack(false);
/*  991 */     setRearing(false);
/*      */     
/*  993 */     if (!this.worldObj.isRemote)
/*      */     {
/*  995 */       player.mountEntity(this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canWearArmor()
/*      */   {
/* 1004 */     return getHorseType() == 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canCarryChest()
/*      */   {
/* 1012 */     int i = getHorseType();
/* 1013 */     return (i == 2) || (i == 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isMovementBlocked()
/*      */   {
/* 1021 */     return (this.riddenByEntity != null) && (isHorseSaddled());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUndead()
/*      */   {
/* 1029 */     int i = getHorseType();
/* 1030 */     return (i == 3) || (i == 4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSterile()
/*      */   {
/* 1038 */     return (isUndead()) || (getHorseType() == 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBreedingItem(ItemStack stack)
/*      */   {
/* 1047 */     return false;
/*      */   }
/*      */   
/*      */   private void func_110210_cH()
/*      */   {
/* 1052 */     this.field_110278_bp = 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDeath(DamageSource cause)
/*      */   {
/* 1060 */     super.onDeath(cause);
/*      */     
/* 1062 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1064 */       dropChestItems();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onLivingUpdate()
/*      */   {
/* 1074 */     if (this.rand.nextInt(200) == 0)
/*      */     {
/* 1076 */       func_110210_cH();
/*      */     }
/*      */     
/* 1079 */     super.onLivingUpdate();
/*      */     
/* 1081 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1083 */       if ((this.rand.nextInt(900) == 0) && (this.deathTime == 0))
/*      */       {
/* 1085 */         heal(1.0F);
/*      */       }
/*      */       
/* 1088 */       if ((!isEatingHaystack()) && (this.riddenByEntity == null) && (this.rand.nextInt(300) == 0) && (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ))).getBlock() == Blocks.grass))
/*      */       {
/* 1090 */         setEatingHaystack(true);
/*      */       }
/*      */       
/* 1093 */       if ((isEatingHaystack()) && (++this.eatingHaystackCounter > 50))
/*      */       {
/* 1095 */         this.eatingHaystackCounter = 0;
/* 1096 */         setEatingHaystack(false);
/*      */       }
/*      */       
/* 1099 */       if ((isBreeding()) && (!isAdultHorse()) && (!isEatingHaystack()))
/*      */       {
/* 1101 */         EntityHorse entityhorse = getClosestHorse(this, 16.0D);
/*      */         
/* 1103 */         if ((entityhorse != null) && (getDistanceSqToEntity(entityhorse) > 4.0D))
/*      */         {
/* 1105 */           this.navigator.getPathToEntityLiving(entityhorse);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate()
/*      */   {
/* 1116 */     super.onUpdate();
/*      */     
/* 1118 */     if ((this.worldObj.isRemote) && (this.dataWatcher.hasObjectChanged()))
/*      */     {
/* 1120 */       this.dataWatcher.func_111144_e();
/* 1121 */       resetTexturePrefix();
/*      */     }
/*      */     
/* 1124 */     if ((this.openMouthCounter > 0) && (++this.openMouthCounter > 30))
/*      */     {
/* 1126 */       this.openMouthCounter = 0;
/* 1127 */       setHorseWatchableBoolean(128, false);
/*      */     }
/*      */     
/* 1130 */     if ((!this.worldObj.isRemote) && (this.jumpRearingCounter > 0) && (++this.jumpRearingCounter > 20))
/*      */     {
/* 1132 */       this.jumpRearingCounter = 0;
/* 1133 */       setRearing(false);
/*      */     }
/*      */     
/* 1136 */     if ((this.field_110278_bp > 0) && (++this.field_110278_bp > 8))
/*      */     {
/* 1138 */       this.field_110278_bp = 0;
/*      */     }
/*      */     
/* 1141 */     if (this.field_110279_bq > 0)
/*      */     {
/* 1143 */       this.field_110279_bq += 1;
/*      */       
/* 1145 */       if (this.field_110279_bq > 300)
/*      */       {
/* 1147 */         this.field_110279_bq = 0;
/*      */       }
/*      */     }
/*      */     
/* 1151 */     this.prevHeadLean = this.headLean;
/*      */     
/* 1153 */     if (isEatingHaystack())
/*      */     {
/* 1155 */       this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;
/*      */       
/* 1157 */       if (this.headLean > 1.0F)
/*      */       {
/* 1159 */         this.headLean = 1.0F;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1164 */       this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;
/*      */       
/* 1166 */       if (this.headLean < 0.0F)
/*      */       {
/* 1168 */         this.headLean = 0.0F;
/*      */       }
/*      */     }
/*      */     
/* 1172 */     this.prevRearingAmount = this.rearingAmount;
/*      */     
/* 1174 */     if (isRearing())
/*      */     {
/* 1176 */       this.prevHeadLean = (this.headLean = 0.0F);
/* 1177 */       this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;
/*      */       
/* 1179 */       if (this.rearingAmount > 1.0F)
/*      */       {
/* 1181 */         this.rearingAmount = 1.0F;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1186 */       this.field_110294_bI = false;
/* 1187 */       this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;
/*      */       
/* 1189 */       if (this.rearingAmount < 0.0F)
/*      */       {
/* 1191 */         this.rearingAmount = 0.0F;
/*      */       }
/*      */     }
/*      */     
/* 1195 */     this.prevMouthOpenness = this.mouthOpenness;
/*      */     
/* 1197 */     if (getHorseWatchableBoolean(128))
/*      */     {
/* 1199 */       this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;
/*      */       
/* 1201 */       if (this.mouthOpenness > 1.0F)
/*      */       {
/* 1203 */         this.mouthOpenness = 1.0F;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1208 */       this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;
/*      */       
/* 1210 */       if (this.mouthOpenness < 0.0F)
/*      */       {
/* 1212 */         this.mouthOpenness = 0.0F;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void openHorseMouth()
/*      */   {
/* 1219 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1221 */       this.openMouthCounter = 1;
/* 1222 */       setHorseWatchableBoolean(128, true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean canMate()
/*      */   {
/* 1231 */     return (this.riddenByEntity == null) && (this.ridingEntity == null) && (isTame()) && (isAdultHorse()) && (!isSterile()) && (getHealth() >= getMaxHealth()) && (isInLove());
/*      */   }
/*      */   
/*      */   public void setEating(boolean eating)
/*      */   {
/* 1236 */     setHorseWatchableBoolean(32, eating);
/*      */   }
/*      */   
/*      */   public void setEatingHaystack(boolean p_110227_1_)
/*      */   {
/* 1241 */     setEating(p_110227_1_);
/*      */   }
/*      */   
/*      */   public void setRearing(boolean rearing)
/*      */   {
/* 1246 */     if (rearing)
/*      */     {
/* 1248 */       setEatingHaystack(false);
/*      */     }
/*      */     
/* 1251 */     setHorseWatchableBoolean(64, rearing);
/*      */   }
/*      */   
/*      */   private void makeHorseRear()
/*      */   {
/* 1256 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1258 */       this.jumpRearingCounter = 1;
/* 1259 */       setRearing(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public void makeHorseRearWithSound()
/*      */   {
/* 1265 */     makeHorseRear();
/* 1266 */     String s = getAngrySoundName();
/*      */     
/* 1268 */     if (s != null)
/*      */     {
/* 1270 */       playSound(s, getSoundVolume(), getSoundPitch());
/*      */     }
/*      */   }
/*      */   
/*      */   public void dropChestItems()
/*      */   {
/* 1276 */     dropItemsInChest(this, this.horseChest);
/* 1277 */     dropChests();
/*      */   }
/*      */   
/*      */   private void dropItemsInChest(Entity entityIn, AnimalChest animalChestIn)
/*      */   {
/* 1282 */     if ((animalChestIn != null) && (!this.worldObj.isRemote))
/*      */     {
/* 1284 */       for (int i = 0; i < animalChestIn.getSizeInventory(); i++)
/*      */       {
/* 1286 */         ItemStack itemstack = animalChestIn.getStackInSlot(i);
/*      */         
/* 1288 */         if (itemstack != null)
/*      */         {
/* 1290 */           entityDropItem(itemstack, 0.0F);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean setTamedBy(EntityPlayer player)
/*      */   {
/* 1298 */     setOwnerId(player.getUniqueID().toString());
/* 1299 */     setHorseTamed(true);
/* 1300 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void moveEntityWithHeading(float strafe, float forward)
/*      */   {
/* 1308 */     if ((this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityLivingBase)) && (isHorseSaddled()))
/*      */     {
/* 1310 */       this.prevRotationYaw = (this.rotationYaw = this.riddenByEntity.rotationYaw);
/* 1311 */       this.rotationPitch = (this.riddenByEntity.rotationPitch * 0.5F);
/* 1312 */       setRotation(this.rotationYaw, this.rotationPitch);
/* 1313 */       this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
/* 1314 */       strafe = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5F;
/* 1315 */       forward = ((EntityLivingBase)this.riddenByEntity).moveForward;
/*      */       
/* 1317 */       if (forward <= 0.0F)
/*      */       {
/* 1319 */         forward *= 0.25F;
/* 1320 */         this.gallopTime = 0;
/*      */       }
/*      */       
/* 1323 */       if ((this.onGround) && (this.jumpPower == 0.0F) && (isRearing()) && (!this.field_110294_bI))
/*      */       {
/* 1325 */         strafe = 0.0F;
/* 1326 */         forward = 0.0F;
/*      */       }
/*      */       
/* 1329 */       if ((this.jumpPower > 0.0F) && (!isHorseJumping()) && (this.onGround))
/*      */       {
/* 1331 */         this.motionY = (getHorseJumpStrength() * this.jumpPower);
/*      */         
/* 1333 */         if (isPotionActive(Potion.jump))
/*      */         {
/* 1335 */           this.motionY += (getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
/*      */         }
/*      */         
/* 1338 */         setHorseJumping(true);
/* 1339 */         this.isAirBorne = true;
/*      */         
/* 1341 */         if (forward > 0.0F)
/*      */         {
/* 1343 */           float f = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
/* 1344 */           float f1 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
/* 1345 */           this.motionX += -0.4F * f * this.jumpPower;
/* 1346 */           this.motionZ += 0.4F * f1 * this.jumpPower;
/* 1347 */           playSound("mob.horse.jump", 0.4F, 1.0F);
/*      */         }
/*      */         
/* 1350 */         this.jumpPower = 0.0F;
/*      */       }
/*      */       
/* 1353 */       this.stepHeight = 1.0F;
/* 1354 */       this.jumpMovementFactor = (getAIMoveSpeed() * 0.1F);
/*      */       
/* 1356 */       if (!this.worldObj.isRemote)
/*      */       {
/* 1358 */         setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
/* 1359 */         super.moveEntityWithHeading(strafe, forward);
/*      */       }
/*      */       
/* 1362 */       if (this.onGround)
/*      */       {
/* 1364 */         this.jumpPower = 0.0F;
/* 1365 */         setHorseJumping(false);
/*      */       }
/*      */       
/* 1368 */       this.prevLimbSwingAmount = this.limbSwingAmount;
/* 1369 */       double d1 = this.posX - this.prevPosX;
/* 1370 */       double d0 = this.posZ - this.prevPosZ;
/* 1371 */       float f2 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;
/*      */       
/* 1373 */       if (f2 > 1.0F)
/*      */       {
/* 1375 */         f2 = 1.0F;
/*      */       }
/*      */       
/* 1378 */       this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
/* 1379 */       this.limbSwing += this.limbSwingAmount;
/*      */     }
/*      */     else
/*      */     {
/* 1383 */       this.stepHeight = 0.5F;
/* 1384 */       this.jumpMovementFactor = 0.02F;
/* 1385 */       super.moveEntityWithHeading(strafe, forward);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*      */   {
/* 1394 */     super.writeEntityToNBT(tagCompound);
/* 1395 */     tagCompound.setBoolean("EatingHaystack", isEatingHaystack());
/* 1396 */     tagCompound.setBoolean("ChestedHorse", isChested());
/* 1397 */     tagCompound.setBoolean("HasReproduced", getHasReproduced());
/* 1398 */     tagCompound.setBoolean("Bred", isBreeding());
/* 1399 */     tagCompound.setInteger("Type", getHorseType());
/* 1400 */     tagCompound.setInteger("Variant", getHorseVariant());
/* 1401 */     tagCompound.setInteger("Temper", getTemper());
/* 1402 */     tagCompound.setBoolean("Tame", isTame());
/* 1403 */     tagCompound.setString("OwnerUUID", getOwnerId());
/*      */     
/* 1405 */     if (isChested())
/*      */     {
/* 1407 */       NBTTagList nbttaglist = new NBTTagList();
/*      */       
/* 1409 */       for (int i = 2; i < this.horseChest.getSizeInventory(); i++)
/*      */       {
/* 1411 */         ItemStack itemstack = this.horseChest.getStackInSlot(i);
/*      */         
/* 1413 */         if (itemstack != null)
/*      */         {
/* 1415 */           NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 1416 */           nbttagcompound.setByte("Slot", (byte)i);
/* 1417 */           itemstack.writeToNBT(nbttagcompound);
/* 1418 */           nbttaglist.appendTag(nbttagcompound);
/*      */         }
/*      */       }
/*      */       
/* 1422 */       tagCompound.setTag("Items", nbttaglist);
/*      */     }
/*      */     
/* 1425 */     if (this.horseChest.getStackInSlot(1) != null)
/*      */     {
/* 1427 */       tagCompound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
/*      */     }
/*      */     
/* 1430 */     if (this.horseChest.getStackInSlot(0) != null)
/*      */     {
/* 1432 */       tagCompound.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*      */   {
/* 1441 */     super.readEntityFromNBT(tagCompund);
/* 1442 */     setEatingHaystack(tagCompund.getBoolean("EatingHaystack"));
/* 1443 */     setBreeding(tagCompund.getBoolean("Bred"));
/* 1444 */     setChested(tagCompund.getBoolean("ChestedHorse"));
/* 1445 */     setHasReproduced(tagCompund.getBoolean("HasReproduced"));
/* 1446 */     setHorseType(tagCompund.getInteger("Type"));
/* 1447 */     setHorseVariant(tagCompund.getInteger("Variant"));
/* 1448 */     setTemper(tagCompund.getInteger("Temper"));
/* 1449 */     setHorseTamed(tagCompund.getBoolean("Tame"));
/* 1450 */     String s = "";
/*      */     
/* 1452 */     if (tagCompund.hasKey("OwnerUUID", 8))
/*      */     {
/* 1454 */       s = tagCompund.getString("OwnerUUID");
/*      */     }
/*      */     else
/*      */     {
/* 1458 */       String s1 = tagCompund.getString("Owner");
/* 1459 */       s = net.minecraft.server.management.PreYggdrasilConverter.getStringUUIDFromName(s1);
/*      */     }
/*      */     
/* 1462 */     if (s.length() > 0)
/*      */     {
/* 1464 */       setOwnerId(s);
/*      */     }
/*      */     
/* 1467 */     IAttributeInstance iattributeinstance = getAttributeMap().getAttributeInstanceByName("Speed");
/*      */     
/* 1469 */     if (iattributeinstance != null)
/*      */     {
/* 1471 */       getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(iattributeinstance.getBaseValue() * 0.25D);
/*      */     }
/*      */     
/* 1474 */     if (isChested())
/*      */     {
/* 1476 */       NBTTagList nbttaglist = tagCompund.getTagList("Items", 10);
/* 1477 */       initHorseChest();
/*      */       
/* 1479 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*      */       {
/* 1481 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 1482 */         int j = nbttagcompound.getByte("Slot") & 0xFF;
/*      */         
/* 1484 */         if ((j >= 2) && (j < this.horseChest.getSizeInventory()))
/*      */         {
/* 1486 */           this.horseChest.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1491 */     if (tagCompund.hasKey("ArmorItem", 10))
/*      */     {
/* 1493 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("ArmorItem"));
/*      */       
/* 1495 */       if ((itemstack != null) && (isArmorItem(itemstack.getItem())))
/*      */       {
/* 1497 */         this.horseChest.setInventorySlotContents(1, itemstack);
/*      */       }
/*      */     }
/*      */     
/* 1501 */     if (tagCompund.hasKey("SaddleItem", 10))
/*      */     {
/* 1503 */       ItemStack itemstack1 = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("SaddleItem"));
/*      */       
/* 1505 */       if ((itemstack1 != null) && (itemstack1.getItem() == Items.saddle))
/*      */       {
/* 1507 */         this.horseChest.setInventorySlotContents(0, itemstack1);
/*      */       }
/*      */     }
/* 1510 */     else if (tagCompund.getBoolean("Saddle"))
/*      */     {
/* 1512 */       this.horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
/*      */     }
/*      */     
/* 1515 */     updateHorseSlots();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canMateWith(EntityAnimal otherAnimal)
/*      */   {
/* 1523 */     if (otherAnimal == this)
/*      */     {
/* 1525 */       return false;
/*      */     }
/* 1527 */     if (otherAnimal.getClass() != getClass())
/*      */     {
/* 1529 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1533 */     EntityHorse entityhorse = (EntityHorse)otherAnimal;
/*      */     
/* 1535 */     if ((canMate()) && (entityhorse.canMate()))
/*      */     {
/* 1537 */       int i = getHorseType();
/* 1538 */       int j = entityhorse.getHorseType();
/* 1539 */       return (i == j) || ((i == 0) && (j == 1)) || ((i == 1) && (j == 0));
/*      */     }
/*      */     
/*      */ 
/* 1543 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public EntityAgeable createChild(EntityAgeable ageable)
/*      */   {
/* 1550 */     EntityHorse entityhorse = (EntityHorse)ageable;
/* 1551 */     EntityHorse entityhorse1 = new EntityHorse(this.worldObj);
/* 1552 */     int i = getHorseType();
/* 1553 */     int j = entityhorse.getHorseType();
/* 1554 */     int k = 0;
/*      */     
/* 1556 */     if (i == j)
/*      */     {
/* 1558 */       k = i;
/*      */     }
/* 1560 */     else if (((i == 0) && (j == 1)) || ((i == 1) && (j == 0)))
/*      */     {
/* 1562 */       k = 2;
/*      */     }
/*      */     
/* 1565 */     if (k == 0)
/*      */     {
/* 1567 */       int i1 = this.rand.nextInt(9);
/*      */       int l;
/*      */       int l;
/* 1570 */       if (i1 < 4)
/*      */       {
/* 1572 */         l = getHorseVariant() & 0xFF;
/*      */       } else { int l;
/* 1574 */         if (i1 < 8)
/*      */         {
/* 1576 */           l = entityhorse.getHorseVariant() & 0xFF;
/*      */         }
/*      */         else
/*      */         {
/* 1580 */           l = this.rand.nextInt(7);
/*      */         }
/*      */       }
/* 1583 */       int j1 = this.rand.nextInt(5);
/*      */       
/* 1585 */       if (j1 < 2)
/*      */       {
/* 1587 */         l |= getHorseVariant() & 0xFF00;
/*      */       }
/* 1589 */       else if (j1 < 4)
/*      */       {
/* 1591 */         l |= entityhorse.getHorseVariant() & 0xFF00;
/*      */       }
/*      */       else
/*      */       {
/* 1595 */         l |= this.rand.nextInt(5) << 8 & 0xFF00;
/*      */       }
/*      */       
/* 1598 */       entityhorse1.setHorseVariant(l);
/*      */     }
/*      */     
/* 1601 */     entityhorse1.setHorseType(k);
/* 1602 */     double d1 = getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + getModifiedMaxHealth();
/* 1603 */     entityhorse1.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(d1 / 3.0D);
/* 1604 */     double d2 = getEntityAttribute(horseJumpStrength).getBaseValue() + ageable.getEntityAttribute(horseJumpStrength).getBaseValue() + getModifiedJumpStrength();
/* 1605 */     entityhorse1.getEntityAttribute(horseJumpStrength).setBaseValue(d2 / 3.0D);
/* 1606 */     double d0 = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + getModifiedMovementSpeed();
/* 1607 */     entityhorse1.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(d0 / 3.0D);
/* 1608 */     return entityhorse1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*      */   {
/* 1617 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 1618 */     int i = 0;
/* 1619 */     int j = 0;
/*      */     
/* 1621 */     if ((livingdata instanceof GroupData))
/*      */     {
/* 1623 */       i = ((GroupData)livingdata).horseType;
/* 1624 */       j = ((GroupData)livingdata).horseVariant & 0xFF | this.rand.nextInt(5) << 8;
/*      */     }
/*      */     else
/*      */     {
/* 1628 */       if (this.rand.nextInt(10) == 0)
/*      */       {
/* 1630 */         i = 1;
/*      */       }
/*      */       else
/*      */       {
/* 1634 */         int k = this.rand.nextInt(7);
/* 1635 */         int l = this.rand.nextInt(5);
/* 1636 */         i = 0;
/* 1637 */         j = k | l << 8;
/*      */       }
/*      */       
/* 1640 */       livingdata = new GroupData(i, j);
/*      */     }
/*      */     
/* 1643 */     setHorseType(i);
/* 1644 */     setHorseVariant(j);
/*      */     
/* 1646 */     if (this.rand.nextInt(5) == 0)
/*      */     {
/* 1648 */       setGrowingAge(41536);
/*      */     }
/*      */     
/* 1651 */     if ((i != 4) && (i != 3))
/*      */     {
/* 1653 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getModifiedMaxHealth());
/*      */       
/* 1655 */       if (i == 0)
/*      */       {
/* 1657 */         getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(getModifiedMovementSpeed());
/*      */       }
/*      */       else
/*      */       {
/* 1661 */         getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.17499999701976776D);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1666 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
/* 1667 */       getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
/*      */     }
/*      */     
/* 1670 */     if ((i != 2) && (i != 1))
/*      */     {
/* 1672 */       getEntityAttribute(horseJumpStrength).setBaseValue(getModifiedJumpStrength());
/*      */     }
/*      */     else
/*      */     {
/* 1676 */       getEntityAttribute(horseJumpStrength).setBaseValue(0.5D);
/*      */     }
/*      */     
/* 1679 */     setHealth(getMaxHealth());
/* 1680 */     return livingdata;
/*      */   }
/*      */   
/*      */   public float getGrassEatingAmount(float p_110258_1_)
/*      */   {
/* 1685 */     return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
/*      */   }
/*      */   
/*      */   public float getRearingAmount(float p_110223_1_)
/*      */   {
/* 1690 */     return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
/*      */   }
/*      */   
/*      */   public float getMouthOpennessAngle(float p_110201_1_)
/*      */   {
/* 1695 */     return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
/*      */   }
/*      */   
/*      */   public void setJumpPower(int jumpPowerIn)
/*      */   {
/* 1700 */     if (isHorseSaddled())
/*      */     {
/* 1702 */       if (jumpPowerIn < 0)
/*      */       {
/* 1704 */         jumpPowerIn = 0;
/*      */       }
/*      */       else
/*      */       {
/* 1708 */         this.field_110294_bI = true;
/* 1709 */         makeHorseRear();
/*      */       }
/*      */       
/* 1712 */       if (jumpPowerIn >= 90)
/*      */       {
/* 1714 */         this.jumpPower = 1.0F;
/*      */       }
/*      */       else
/*      */       {
/* 1718 */         this.jumpPower = (0.4F + 0.4F * jumpPowerIn / 90.0F);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void spawnHorseParticles(boolean p_110216_1_)
/*      */   {
/* 1728 */     EnumParticleTypes enumparticletypes = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;
/*      */     
/* 1730 */     for (int i = 0; i < 7; i++)
/*      */     {
/* 1732 */       double d0 = this.rand.nextGaussian() * 0.02D;
/* 1733 */       double d1 = this.rand.nextGaussian() * 0.02D;
/* 1734 */       double d2 = this.rand.nextGaussian() * 0.02D;
/* 1735 */       this.worldObj.spawnParticle(enumparticletypes, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleStatusUpdate(byte id)
/*      */   {
/* 1741 */     if (id == 7)
/*      */     {
/* 1743 */       spawnHorseParticles(true);
/*      */     }
/* 1745 */     else if (id == 6)
/*      */     {
/* 1747 */       spawnHorseParticles(false);
/*      */     }
/*      */     else
/*      */     {
/* 1751 */       super.handleStatusUpdate(id);
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateRiderPosition()
/*      */   {
/* 1757 */     super.updateRiderPosition();
/*      */     
/* 1759 */     if (this.prevRearingAmount > 0.0F)
/*      */     {
/* 1761 */       float f = MathHelper.sin(this.renderYawOffset * 3.1415927F / 180.0F);
/* 1762 */       float f1 = MathHelper.cos(this.renderYawOffset * 3.1415927F / 180.0F);
/* 1763 */       float f2 = 0.7F * this.prevRearingAmount;
/* 1764 */       float f3 = 0.15F * this.prevRearingAmount;
/* 1765 */       this.riddenByEntity.setPosition(this.posX + f2 * f, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset() + f3, this.posZ - f2 * f1);
/*      */       
/* 1767 */       if ((this.riddenByEntity instanceof EntityLivingBase))
/*      */       {
/* 1769 */         ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private float getModifiedMaxHealth()
/*      */   {
/* 1779 */     return 15.0F + this.rand.nextInt(8) + this.rand.nextInt(9);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private double getModifiedJumpStrength()
/*      */   {
/* 1787 */     return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private double getModifiedMovementSpeed()
/*      */   {
/* 1795 */     return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D) * 0.25D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isArmorItem(Item p_146085_0_)
/*      */   {
/* 1803 */     return (p_146085_0_ == Items.iron_horse_armor) || (p_146085_0_ == Items.golden_horse_armor) || (p_146085_0_ == Items.diamond_horse_armor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOnLadder()
/*      */   {
/* 1811 */     return false;
/*      */   }
/*      */   
/*      */   public float getEyeHeight()
/*      */   {
/* 1816 */     return this.height;
/*      */   }
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
/*      */   {
/* 1821 */     if ((inventorySlot == 499) && (canCarryChest()))
/*      */     {
/* 1823 */       if ((itemStackIn == null) && (isChested()))
/*      */       {
/* 1825 */         setChested(false);
/* 1826 */         initHorseChest();
/* 1827 */         return true;
/*      */       }
/*      */       
/* 1830 */       if ((itemStackIn != null) && (itemStackIn.getItem() == Item.getItemFromBlock(Blocks.chest)) && (!isChested()))
/*      */       {
/* 1832 */         setChested(true);
/* 1833 */         initHorseChest();
/* 1834 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 1838 */     int i = inventorySlot - 400;
/*      */     
/* 1840 */     if ((i >= 0) && (i < 2) && (i < this.horseChest.getSizeInventory()))
/*      */     {
/* 1842 */       if ((i == 0) && (itemStackIn != null) && (itemStackIn.getItem() != Items.saddle))
/*      */       {
/* 1844 */         return false;
/*      */       }
/* 1846 */       if ((i != 1) || (((itemStackIn == null) || (isArmorItem(itemStackIn.getItem()))) && (canWearArmor())))
/*      */       {
/* 1848 */         this.horseChest.setInventorySlotContents(i, itemStackIn);
/* 1849 */         updateHorseSlots();
/* 1850 */         return true;
/*      */       }
/*      */       
/*      */ 
/* 1854 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1859 */     int j = inventorySlot - 500 + 2;
/*      */     
/* 1861 */     if ((j >= 2) && (j < this.horseChest.getSizeInventory()))
/*      */     {
/* 1863 */       this.horseChest.setInventorySlotContents(j, itemStackIn);
/* 1864 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1868 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static class GroupData
/*      */     implements IEntityLivingData
/*      */   {
/*      */     public int horseType;
/*      */     public int horseVariant;
/*      */     
/*      */     public GroupData(int type, int variant)
/*      */     {
/* 1880 */       this.horseType = type;
/* 1881 */       this.horseVariant = variant;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */