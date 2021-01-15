/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBreakDoor;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.ai.attributes.RangedAttribute;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityZombie extends EntityMob
/*     */ {
/*  51 */   protected static final IAttribute reinforcementChance = new RangedAttribute(null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D).setDescription("Spawn Reinforcements Chance");
/*  52 */   private static final UUID babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
/*  53 */   private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost", 0.5D, 1);
/*  54 */   private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
/*     */   
/*     */ 
/*     */   private int conversionTime;
/*     */   
/*     */ 
/*  60 */   private boolean isBreakDoorsTaskSet = false;
/*     */   
/*     */ 
/*  63 */   private float zombieWidth = -1.0F;
/*     */   
/*     */   private float zombieHeight;
/*     */   
/*     */ 
/*     */   public EntityZombie(World worldIn)
/*     */   {
/*  70 */     super(worldIn);
/*  71 */     ((PathNavigateGround)getNavigator()).setBreakDoors(true);
/*  72 */     this.tasks.addTask(0, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  73 */     this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
/*  74 */     this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
/*  75 */     this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
/*  76 */     this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  77 */     this.tasks.addTask(8, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  78 */     applyEntityAI();
/*  79 */     setSize(0.6F, 1.95F);
/*     */   }
/*     */   
/*     */   protected void applyEntityAI()
/*     */   {
/*  84 */     this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
/*  85 */     this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0D, true));
/*  86 */     this.tasks.addTask(6, new net.minecraft.entity.ai.EntityAIMoveThroughVillage(this, 1.0D, false));
/*  87 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
/*  88 */     this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*  89 */     this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
/*  90 */     this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  95 */     super.applyEntityAttributes();
/*  96 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
/*  97 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*  98 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
/*  99 */     getAttributeMap().registerAttribute(reinforcementChance).setBaseValue(this.rand.nextDouble() * 0.10000000149011612D);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 104 */     super.entityInit();
/* 105 */     getDataWatcher().addObject(12, Byte.valueOf((byte)0));
/* 106 */     getDataWatcher().addObject(13, Byte.valueOf((byte)0));
/* 107 */     getDataWatcher().addObject(14, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTotalArmorValue()
/*     */   {
/* 115 */     int i = super.getTotalArmorValue() + 2;
/*     */     
/* 117 */     if (i > 20)
/*     */     {
/* 119 */       i = 20;
/*     */     }
/*     */     
/* 122 */     return i;
/*     */   }
/*     */   
/*     */   public boolean isBreakDoorsTaskSet()
/*     */   {
/* 127 */     return this.isBreakDoorsTaskSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBreakDoorsAItask(boolean par1)
/*     */   {
/* 135 */     if (this.isBreakDoorsTaskSet != par1)
/*     */     {
/* 137 */       this.isBreakDoorsTaskSet = par1;
/*     */       
/* 139 */       if (par1)
/*     */       {
/* 141 */         this.tasks.addTask(1, this.breakDoor);
/*     */       }
/*     */       else
/*     */       {
/* 145 */         this.tasks.removeTask(this.breakDoor);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isChild()
/*     */   {
/* 155 */     return getDataWatcher().getWatchableObjectByte(12) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getExperiencePoints(EntityPlayer player)
/*     */   {
/* 163 */     if (isChild())
/*     */     {
/* 165 */       this.experienceValue = ((int)(this.experienceValue * 2.5F));
/*     */     }
/*     */     
/* 168 */     return super.getExperiencePoints(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChild(boolean childZombie)
/*     */   {
/* 176 */     getDataWatcher().updateObject(12, Byte.valueOf((byte)(childZombie ? 1 : 0)));
/*     */     
/* 178 */     if ((this.worldObj != null) && (!this.worldObj.isRemote))
/*     */     {
/* 180 */       IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 181 */       iattributeinstance.removeModifier(babySpeedBoostModifier);
/*     */       
/* 183 */       if (childZombie)
/*     */       {
/* 185 */         iattributeinstance.applyModifier(babySpeedBoostModifier);
/*     */       }
/*     */     }
/*     */     
/* 189 */     setChildSize(childZombie);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVillager()
/*     */   {
/* 197 */     return getDataWatcher().getWatchableObjectByte(13) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVillager(boolean villager)
/*     */   {
/* 205 */     getDataWatcher().updateObject(13, Byte.valueOf((byte)(villager ? 1 : 0)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 214 */     if ((this.worldObj.isDaytime()) && (!this.worldObj.isRemote) && (!isChild()))
/*     */     {
/* 216 */       float f = getBrightness(1.0F);
/* 217 */       BlockPos blockpos = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
/*     */       
/* 219 */       if ((f > 0.5F) && (this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) && (this.worldObj.canSeeSky(blockpos)))
/*     */       {
/* 221 */         boolean flag = true;
/* 222 */         ItemStack itemstack = getEquipmentInSlot(4);
/*     */         
/* 224 */         if (itemstack != null)
/*     */         {
/* 226 */           if (itemstack.isItemStackDamageable())
/*     */           {
/* 228 */             itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
/*     */             
/* 230 */             if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
/*     */             {
/* 232 */               renderBrokenItemStack(itemstack);
/* 233 */               setCurrentItemOrArmor(4, null);
/*     */             }
/*     */           }
/*     */           
/* 237 */           flag = false;
/*     */         }
/*     */         
/* 240 */         if (flag)
/*     */         {
/* 242 */           setFire(8);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 247 */     if ((isRiding()) && (getAttackTarget() != null) && ((this.ridingEntity instanceof EntityChicken)))
/*     */     {
/* 249 */       ((EntityLiving)this.ridingEntity).getNavigator().setPath(getNavigator().getPath(), 1.5D);
/*     */     }
/*     */     
/* 252 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 260 */     if (super.attackEntityFrom(source, amount))
/*     */     {
/* 262 */       EntityLivingBase entitylivingbase = getAttackTarget();
/*     */       
/* 264 */       if ((entitylivingbase == null) && ((source.getEntity() instanceof EntityLivingBase)))
/*     */       {
/* 266 */         entitylivingbase = (EntityLivingBase)source.getEntity();
/*     */       }
/*     */       
/* 269 */       if ((entitylivingbase != null) && (this.worldObj.getDifficulty() == EnumDifficulty.HARD) && (this.rand.nextFloat() < getEntityAttribute(reinforcementChance).getAttributeValue()))
/*     */       {
/* 271 */         int i = MathHelper.floor_double(this.posX);
/* 272 */         int j = MathHelper.floor_double(this.posY);
/* 273 */         int k = MathHelper.floor_double(this.posZ);
/* 274 */         EntityZombie entityzombie = new EntityZombie(this.worldObj);
/*     */         
/* 276 */         for (int l = 0; l < 50; l++)
/*     */         {
/* 278 */           int i1 = i + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
/* 279 */           int j1 = j + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
/* 280 */           int k1 = k + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
/*     */           
/* 282 */           if ((World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(i1, j1 - 1, k1))) && (this.worldObj.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10))
/*     */           {
/* 284 */             entityzombie.setPosition(i1, j1, k1);
/*     */             
/* 286 */             if ((!this.worldObj.isAnyPlayerWithinRangeAt(i1, j1, k1, 7.0D)) && (this.worldObj.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie)) && (this.worldObj.getCollidingBoundingBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty()) && (!this.worldObj.isAnyLiquid(entityzombie.getEntityBoundingBox())))
/*     */             {
/* 288 */               this.worldObj.spawnEntityInWorld(entityzombie);
/* 289 */               entityzombie.setAttackTarget(entitylivingbase);
/* 290 */               entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityzombie)), null);
/* 291 */               getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
/* 292 */               entityzombie.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
/* 293 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 299 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 303 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 312 */     if ((!this.worldObj.isRemote) && (isConverting()))
/*     */     {
/* 314 */       int i = getConversionTimeBoost();
/* 315 */       this.conversionTime -= i;
/*     */       
/* 317 */       if (this.conversionTime <= 0)
/*     */       {
/* 319 */         convertToVillager();
/*     */       }
/*     */     }
/*     */     
/* 323 */     super.onUpdate();
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 328 */     boolean flag = super.attackEntityAsMob(entityIn);
/*     */     
/* 330 */     if (flag)
/*     */     {
/* 332 */       int i = this.worldObj.getDifficulty().getDifficultyId();
/*     */       
/* 334 */       if ((getHeldItem() == null) && (isBurning()) && (this.rand.nextFloat() < i * 0.3F))
/*     */       {
/* 336 */         entityIn.setFire(2 * i);
/*     */       }
/*     */     }
/*     */     
/* 340 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 348 */     return "mob.zombie.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 356 */     return "mob.zombie.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 364 */     return "mob.zombie.death";
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 369 */     playSound("mob.zombie.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 374 */     return Items.rotten_flesh;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumCreatureAttribute getCreatureAttribute()
/*     */   {
/* 382 */     return EnumCreatureAttribute.UNDEAD;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addRandomDrop()
/*     */   {
/* 390 */     switch (this.rand.nextInt(3))
/*     */     {
/*     */     case 0: 
/* 393 */       dropItem(Items.iron_ingot, 1);
/* 394 */       break;
/*     */     
/*     */     case 1: 
/* 397 */       dropItem(Items.carrot, 1);
/* 398 */       break;
/*     */     
/*     */     case 2: 
/* 401 */       dropItem(Items.potato, 1);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
/*     */   {
/* 410 */     super.setEquipmentBasedOnDifficulty(difficulty);
/*     */     
/* 412 */     if (this.rand.nextFloat() < (this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F))
/*     */     {
/* 414 */       int i = this.rand.nextInt(3);
/*     */       
/* 416 */       if (i == 0)
/*     */       {
/* 418 */         setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
/*     */       }
/*     */       else
/*     */       {
/* 422 */         setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 432 */     super.writeEntityToNBT(tagCompound);
/*     */     
/* 434 */     if (isChild())
/*     */     {
/* 436 */       tagCompound.setBoolean("IsBaby", true);
/*     */     }
/*     */     
/* 439 */     if (isVillager())
/*     */     {
/* 441 */       tagCompound.setBoolean("IsVillager", true);
/*     */     }
/*     */     
/* 444 */     tagCompound.setInteger("ConversionTime", isConverting() ? this.conversionTime : -1);
/* 445 */     tagCompound.setBoolean("CanBreakDoors", isBreakDoorsTaskSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 453 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 455 */     if (tagCompund.getBoolean("IsBaby"))
/*     */     {
/* 457 */       setChild(true);
/*     */     }
/*     */     
/* 460 */     if (tagCompund.getBoolean("IsVillager"))
/*     */     {
/* 462 */       setVillager(true);
/*     */     }
/*     */     
/* 465 */     if ((tagCompund.hasKey("ConversionTime", 99)) && (tagCompund.getInteger("ConversionTime") > -1))
/*     */     {
/* 467 */       startConversion(tagCompund.getInteger("ConversionTime"));
/*     */     }
/*     */     
/* 470 */     setBreakDoorsAItask(tagCompund.getBoolean("CanBreakDoors"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onKillEntity(EntityLivingBase entityLivingIn)
/*     */   {
/* 478 */     super.onKillEntity(entityLivingIn);
/*     */     
/* 480 */     if (((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) || (this.worldObj.getDifficulty() == EnumDifficulty.HARD)) && ((entityLivingIn instanceof EntityVillager)))
/*     */     {
/* 482 */       if ((this.worldObj.getDifficulty() != EnumDifficulty.HARD) && (this.rand.nextBoolean()))
/*     */       {
/* 484 */         return;
/*     */       }
/*     */       
/* 487 */       EntityLiving entityliving = (EntityLiving)entityLivingIn;
/* 488 */       EntityZombie entityzombie = new EntityZombie(this.worldObj);
/* 489 */       entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
/* 490 */       this.worldObj.removeEntity(entityLivingIn);
/* 491 */       entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityzombie)), null);
/* 492 */       entityzombie.setVillager(true);
/*     */       
/* 494 */       if (entityLivingIn.isChild())
/*     */       {
/* 496 */         entityzombie.setChild(true);
/*     */       }
/*     */       
/* 499 */       entityzombie.setNoAI(entityliving.isAIDisabled());
/*     */       
/* 501 */       if (entityliving.hasCustomName())
/*     */       {
/* 503 */         entityzombie.setCustomNameTag(entityliving.getCustomNameTag());
/* 504 */         entityzombie.setAlwaysRenderNameTag(entityliving.getAlwaysRenderNameTag());
/*     */       }
/*     */       
/* 507 */       this.worldObj.spawnEntityInWorld(entityzombie);
/* 508 */       this.worldObj.playAuxSFXAtEntity(null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 514 */     float f = 1.74F;
/*     */     
/* 516 */     if (isChild())
/*     */     {
/* 518 */       f = (float)(f - 0.81D);
/*     */     }
/*     */     
/* 521 */     return f;
/*     */   }
/*     */   
/*     */   protected boolean func_175448_a(ItemStack stack)
/*     */   {
/* 526 */     return (stack.getItem() == Items.egg) && (isChild()) && (isRiding()) ? false : super.func_175448_a(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 535 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 536 */     float f = difficulty.getClampedAdditionalDifficulty();
/* 537 */     setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);
/*     */     
/* 539 */     if (livingdata == null)
/*     */     {
/* 541 */       livingdata = new GroupData(this.worldObj.rand.nextFloat() < 0.05F, this.worldObj.rand.nextFloat() < 0.05F, null);
/*     */     }
/*     */     
/* 544 */     if ((livingdata instanceof GroupData))
/*     */     {
/* 546 */       GroupData entityzombie$groupdata = (GroupData)livingdata;
/*     */       
/* 548 */       if (entityzombie$groupdata.isVillager)
/*     */       {
/* 550 */         setVillager(true);
/*     */       }
/*     */       
/* 553 */       if (entityzombie$groupdata.isChild)
/*     */       {
/* 555 */         setChild(true);
/*     */         
/* 557 */         if (this.worldObj.rand.nextFloat() < 0.05D)
/*     */         {
/* 559 */           List<EntityChicken> list = this.worldObj.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), net.minecraft.util.EntitySelectors.IS_STANDALONE);
/*     */           
/* 561 */           if (!list.isEmpty())
/*     */           {
/* 563 */             EntityChicken entitychicken = (EntityChicken)list.get(0);
/* 564 */             entitychicken.setChickenJockey(true);
/* 565 */             mountEntity(entitychicken);
/*     */           }
/*     */         }
/* 568 */         else if (this.worldObj.rand.nextFloat() < 0.05D)
/*     */         {
/* 570 */           EntityChicken entitychicken1 = new EntityChicken(this.worldObj);
/* 571 */           entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 572 */           entitychicken1.onInitialSpawn(difficulty, null);
/* 573 */           entitychicken1.setChickenJockey(true);
/* 574 */           this.worldObj.spawnEntityInWorld(entitychicken1);
/* 575 */           mountEntity(entitychicken1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 580 */     setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1F);
/* 581 */     setEquipmentBasedOnDifficulty(difficulty);
/* 582 */     setEnchantmentBasedOnDifficulty(difficulty);
/*     */     
/* 584 */     if (getEquipmentInSlot(4) == null)
/*     */     {
/* 586 */       Calendar calendar = this.worldObj.getCurrentDate();
/*     */       
/* 588 */       if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31) && (this.rand.nextFloat() < 0.25F))
/*     */       {
/* 590 */         setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
/* 591 */         this.equipmentDropChances[4] = 0.0F;
/*     */       }
/*     */     }
/*     */     
/* 595 */     getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
/* 596 */     double d0 = this.rand.nextDouble() * 1.5D * f;
/*     */     
/* 598 */     if (d0 > 1.0D)
/*     */     {
/* 600 */       getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
/*     */     }
/*     */     
/* 603 */     if (this.rand.nextFloat() < f * 0.05F)
/*     */     {
/* 605 */       getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
/* 606 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
/* 607 */       setBreakDoorsAItask(true);
/*     */     }
/*     */     
/* 610 */     return livingdata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interact(EntityPlayer player)
/*     */   {
/* 618 */     ItemStack itemstack = player.getCurrentEquippedItem();
/*     */     
/* 620 */     if ((itemstack != null) && (itemstack.getItem() == Items.golden_apple) && (itemstack.getMetadata() == 0) && (isVillager()) && (isPotionActive(Potion.weakness)))
/*     */     {
/* 622 */       if (!player.capabilities.isCreativeMode)
/*     */       {
/* 624 */         itemstack.stackSize -= 1;
/*     */       }
/*     */       
/* 627 */       if (itemstack.stackSize <= 0)
/*     */       {
/* 629 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*     */       }
/*     */       
/* 632 */       if (!this.worldObj.isRemote)
/*     */       {
/* 634 */         startConversion(this.rand.nextInt(2401) + 3600);
/*     */       }
/*     */       
/* 637 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 641 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void startConversion(int ticks)
/*     */   {
/* 651 */     this.conversionTime = ticks;
/* 652 */     getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
/* 653 */     removePotionEffect(Potion.weakness.id);
/* 654 */     addPotionEffect(new PotionEffect(Potion.damageBoost.id, ticks, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
/* 655 */     this.worldObj.setEntityState(this, (byte)16);
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 660 */     if (id == 16)
/*     */     {
/* 662 */       if (!isSilent())
/*     */       {
/* 664 */         this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 669 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDespawn()
/*     */   {
/* 678 */     return !isConverting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConverting()
/*     */   {
/* 686 */     return getDataWatcher().getWatchableObjectByte(14) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void convertToVillager()
/*     */   {
/* 694 */     EntityVillager entityvillager = new EntityVillager(this.worldObj);
/* 695 */     entityvillager.copyLocationAndAnglesFrom(this);
/* 696 */     entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityvillager)), null);
/* 697 */     entityvillager.setLookingForHome();
/*     */     
/* 699 */     if (isChild())
/*     */     {
/* 701 */       entityvillager.setGrowingAge(41536);
/*     */     }
/*     */     
/* 704 */     this.worldObj.removeEntity(this);
/* 705 */     entityvillager.setNoAI(isAIDisabled());
/*     */     
/* 707 */     if (hasCustomName())
/*     */     {
/* 709 */       entityvillager.setCustomNameTag(getCustomNameTag());
/* 710 */       entityvillager.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
/*     */     }
/*     */     
/* 713 */     this.worldObj.spawnEntityInWorld(entityvillager);
/* 714 */     entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
/* 715 */     this.worldObj.playAuxSFXAtEntity(null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getConversionTimeBoost()
/*     */   {
/* 723 */     int i = 1;
/*     */     
/* 725 */     if (this.rand.nextFloat() < 0.01F)
/*     */     {
/* 727 */       int j = 0;
/* 728 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */       
/* 730 */       for (int k = (int)this.posX - 4; (k < (int)this.posX + 4) && (j < 14); k++)
/*     */       {
/* 732 */         for (int l = (int)this.posY - 4; (l < (int)this.posY + 4) && (j < 14); l++)
/*     */         {
/* 734 */           for (int i1 = (int)this.posZ - 4; (i1 < (int)this.posZ + 4) && (j < 14); i1++)
/*     */           {
/* 736 */             Block block = this.worldObj.getBlockState(blockpos$mutableblockpos.func_181079_c(k, l, i1)).getBlock();
/*     */             
/* 738 */             if ((block == Blocks.iron_bars) || (block == Blocks.bed))
/*     */             {
/* 740 */               if (this.rand.nextFloat() < 0.3F)
/*     */               {
/* 742 */                 i++;
/*     */               }
/*     */               
/* 745 */               j++;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 752 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChildSize(boolean isChild)
/*     */   {
/* 760 */     multiplySize(isChild ? 0.5F : 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void setSize(float width, float height)
/*     */   {
/* 768 */     boolean flag = (this.zombieWidth > 0.0F) && (this.zombieHeight > 0.0F);
/* 769 */     this.zombieWidth = width;
/* 770 */     this.zombieHeight = height;
/*     */     
/* 772 */     if (!flag)
/*     */     {
/* 774 */       multiplySize(1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void multiplySize(float size)
/*     */   {
/* 783 */     super.setSize(this.zombieWidth * size, this.zombieHeight * size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getYOffset()
/*     */   {
/* 791 */     return isChild() ? 0.0D : -0.35D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDeath(DamageSource cause)
/*     */   {
/* 799 */     super.onDeath(cause);
/*     */     
/* 801 */     if (((cause.getEntity() instanceof EntityCreeper)) && (!(this instanceof EntityPigZombie)) && (((EntityCreeper)cause.getEntity()).getPowered()) && (((EntityCreeper)cause.getEntity()).isAIEnabled()))
/*     */     {
/* 803 */       ((EntityCreeper)cause.getEntity()).func_175493_co();
/* 804 */       entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   class GroupData implements IEntityLivingData
/*     */   {
/*     */     public boolean isChild;
/*     */     public boolean isVillager;
/*     */     
/*     */     private GroupData(boolean isBaby, boolean isVillagerZombie)
/*     */     {
/* 815 */       this.isChild = false;
/* 816 */       this.isVillager = false;
/* 817 */       this.isChild = isBaby;
/* 818 */       this.isVillager = isVillagerZombie;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */