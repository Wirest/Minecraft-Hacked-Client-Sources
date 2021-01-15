/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBeg;
/*     */ import net.minecraft.entity.ai.EntityAIFollowOwner;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILeapAtTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
/*     */ import net.minecraft.entity.ai.EntityAISit;
/*     */ import net.minecraft.entity.ai.EntityAITargetNonTamed;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.monster.EntityCreeper;
/*     */ import net.minecraft.entity.monster.EntityGhast;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWolf extends EntityTameable
/*     */ {
/*     */   private float headRotationCourse;
/*     */   private float headRotationCourseOld;
/*     */   private boolean isWet;
/*     */   private boolean isShaking;
/*     */   private float timeWolfIsShaking;
/*     */   private float prevTimeWolfIsShaking;
/*     */   
/*     */   public EntityWolf(World worldIn)
/*     */   {
/*  61 */     super(worldIn);
/*  62 */     setSize(0.6F, 0.8F);
/*  63 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  64 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  65 */     this.tasks.addTask(2, this.aiSit);
/*  66 */     this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
/*  67 */     this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
/*  68 */     this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
/*  69 */     this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
/*  70 */     this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
/*  71 */     this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
/*  72 */     this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  73 */     this.tasks.addTask(9, new EntityAILookIdle(this));
/*  74 */     this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
/*  75 */     this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
/*  76 */     this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
/*  77 */     this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate()
/*     */     {
/*     */       public boolean apply(Entity p_apply_1_)
/*     */       {
/*  81 */         return ((p_apply_1_ instanceof EntitySheep)) || ((p_apply_1_ instanceof EntityRabbit));
/*     */       }
/*  83 */     }));
/*  84 */     this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, net.minecraft.entity.monster.EntitySkeleton.class, false));
/*  85 */     setTamed(false);
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  90 */     super.applyEntityAttributes();
/*  91 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */     
/*  93 */     if (isTamed())
/*     */     {
/*  95 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
/*     */     }
/*     */     else
/*     */     {
/*  99 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*     */     }
/*     */     
/* 102 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
/* 103 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
/*     */   {
/* 111 */     super.setAttackTarget(entitylivingbaseIn);
/*     */     
/* 113 */     if (entitylivingbaseIn == null)
/*     */     {
/* 115 */       setAngry(false);
/*     */     }
/* 117 */     else if (!isTamed())
/*     */     {
/* 119 */       setAngry(true);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/* 125 */     this.dataWatcher.updateObject(18, Float.valueOf(getHealth()));
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 130 */     super.entityInit();
/* 131 */     this.dataWatcher.addObject(18, new Float(getHealth()));
/* 132 */     this.dataWatcher.addObject(19, new Byte((byte)0));
/* 133 */     this.dataWatcher.addObject(20, new Byte((byte)EnumDyeColor.RED.getMetadata()));
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 138 */     playSound("mob.wolf.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 146 */     super.writeEntityToNBT(tagCompound);
/* 147 */     tagCompound.setBoolean("Angry", isAngry());
/* 148 */     tagCompound.setByte("CollarColor", (byte)getCollarColor().getDyeDamage());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 156 */     super.readEntityFromNBT(tagCompund);
/* 157 */     setAngry(tagCompund.getBoolean("Angry"));
/*     */     
/* 159 */     if (tagCompund.hasKey("CollarColor", 99))
/*     */     {
/* 161 */       setCollarColor(EnumDyeColor.byDyeDamage(tagCompund.getByte("CollarColor")));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 170 */     return this.rand.nextInt(3) == 0 ? "mob.wolf.panting" : (isTamed()) && (this.dataWatcher.getWatchableObjectFloat(18) < 10.0F) ? "mob.wolf.whine" : isAngry() ? "mob.wolf.growl" : "mob.wolf.bark";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 178 */     return "mob.wolf.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 186 */     return "mob.wolf.death";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundVolume()
/*     */   {
/* 194 */     return 0.4F;
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 199 */     return Item.getItemById(-1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 208 */     super.onLivingUpdate();
/*     */     
/* 210 */     if ((!this.worldObj.isRemote) && (this.isWet) && (!this.isShaking) && (!hasPath()) && (this.onGround))
/*     */     {
/* 212 */       this.isShaking = true;
/* 213 */       this.timeWolfIsShaking = 0.0F;
/* 214 */       this.prevTimeWolfIsShaking = 0.0F;
/* 215 */       this.worldObj.setEntityState(this, (byte)8);
/*     */     }
/*     */     
/* 218 */     if ((!this.worldObj.isRemote) && (getAttackTarget() == null) && (isAngry()))
/*     */     {
/* 220 */       setAngry(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 229 */     super.onUpdate();
/* 230 */     this.headRotationCourseOld = this.headRotationCourse;
/*     */     
/* 232 */     if (isBegging())
/*     */     {
/* 234 */       this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
/*     */     }
/*     */     else
/*     */     {
/* 238 */       this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
/*     */     }
/*     */     
/* 241 */     if (isWet())
/*     */     {
/* 243 */       this.isWet = true;
/* 244 */       this.isShaking = false;
/* 245 */       this.timeWolfIsShaking = 0.0F;
/* 246 */       this.prevTimeWolfIsShaking = 0.0F;
/*     */     }
/* 248 */     else if (((this.isWet) || (this.isShaking)) && (this.isShaking))
/*     */     {
/* 250 */       if (this.timeWolfIsShaking == 0.0F)
/*     */       {
/* 252 */         playSound("mob.wolf.shake", getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*     */       }
/*     */       
/* 255 */       this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
/* 256 */       this.timeWolfIsShaking += 0.05F;
/*     */       
/* 258 */       if (this.prevTimeWolfIsShaking >= 2.0F)
/*     */       {
/* 260 */         this.isWet = false;
/* 261 */         this.isShaking = false;
/* 262 */         this.prevTimeWolfIsShaking = 0.0F;
/* 263 */         this.timeWolfIsShaking = 0.0F;
/*     */       }
/*     */       
/* 266 */       if (this.timeWolfIsShaking > 0.4F)
/*     */       {
/* 268 */         float f = (float)getEntityBoundingBox().minY;
/* 269 */         int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);
/*     */         
/* 271 */         for (int j = 0; j < i; j++)
/*     */         {
/* 273 */           float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
/* 274 */           float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
/* 275 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f1, f + 0.8F, this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWolfWet()
/*     */   {
/* 286 */     return this.isWet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getShadingWhileWet(float p_70915_1_)
/*     */   {
/* 294 */     return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
/*     */   }
/*     */   
/*     */   public float getShakeAngle(float p_70923_1_, float p_70923_2_)
/*     */   {
/* 299 */     float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;
/*     */     
/* 301 */     if (f < 0.0F)
/*     */     {
/* 303 */       f = 0.0F;
/*     */     }
/* 305 */     else if (f > 1.0F)
/*     */     {
/* 307 */       f = 1.0F;
/*     */     }
/*     */     
/* 310 */     return MathHelper.sin(f * 3.1415927F) * MathHelper.sin(f * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
/*     */   }
/*     */   
/*     */   public float getInterestedAngle(float p_70917_1_)
/*     */   {
/* 315 */     return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * 3.1415927F;
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 320 */     return this.height * 0.8F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getVerticalFaceSpeed()
/*     */   {
/* 329 */     return isSitting() ? 20 : super.getVerticalFaceSpeed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 337 */     if (isEntityInvulnerable(source))
/*     */     {
/* 339 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 343 */     Entity entity = source.getEntity();
/* 344 */     this.aiSit.setSitting(false);
/*     */     
/* 346 */     if ((entity != null) && (!(entity instanceof EntityPlayer)) && (!(entity instanceof EntityArrow)))
/*     */     {
/* 348 */       amount = (amount + 1.0F) / 2.0F;
/*     */     }
/*     */     
/* 351 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 357 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (int)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
/*     */     
/* 359 */     if (flag)
/*     */     {
/* 361 */       applyEnchantments(this, entityIn);
/*     */     }
/*     */     
/* 364 */     return flag;
/*     */   }
/*     */   
/*     */   public void setTamed(boolean tamed)
/*     */   {
/* 369 */     super.setTamed(tamed);
/*     */     
/* 371 */     if (tamed)
/*     */     {
/* 373 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
/*     */     }
/*     */     else
/*     */     {
/* 377 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*     */     }
/*     */     
/* 380 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interact(EntityPlayer player)
/*     */   {
/* 388 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 390 */     if (isTamed())
/*     */     {
/* 392 */       if (itemstack != null)
/*     */       {
/* 394 */         if ((itemstack.getItem() instanceof ItemFood))
/*     */         {
/* 396 */           ItemFood itemfood = (ItemFood)itemstack.getItem();
/*     */           
/* 398 */           if ((itemfood.isWolfsFavoriteMeat()) && (this.dataWatcher.getWatchableObjectFloat(18) < 20.0F))
/*     */           {
/* 400 */             if (!player.capabilities.isCreativeMode)
/*     */             {
/* 402 */               itemstack.stackSize -= 1;
/*     */             }
/*     */             
/* 405 */             heal(itemfood.getHealAmount(itemstack));
/*     */             
/* 407 */             if (itemstack.stackSize <= 0)
/*     */             {
/* 409 */               player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*     */             }
/*     */             
/* 412 */             return true;
/*     */           }
/*     */         }
/* 415 */         else if (itemstack.getItem() == Items.dye)
/*     */         {
/* 417 */           EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
/*     */           
/* 419 */           if (enumdyecolor != getCollarColor())
/*     */           {
/* 421 */             setCollarColor(enumdyecolor);
/*     */             
/* 423 */             if (!player.capabilities.isCreativeMode) { if (--itemstack.stackSize <= 0)
/*     */               {
/* 425 */                 player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*     */               }
/*     */             }
/* 428 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 433 */       if ((isOwner(player)) && (!this.worldObj.isRemote) && (!isBreedingItem(itemstack)))
/*     */       {
/* 435 */         this.aiSit.setSitting(!isSitting());
/* 436 */         this.isJumping = false;
/* 437 */         this.navigator.clearPathEntity();
/* 438 */         setAttackTarget(null);
/*     */       }
/*     */     }
/* 441 */     else if ((itemstack != null) && (itemstack.getItem() == Items.bone) && (!isAngry()))
/*     */     {
/* 443 */       if (!player.capabilities.isCreativeMode)
/*     */       {
/* 445 */         itemstack.stackSize -= 1;
/*     */       }
/*     */       
/* 448 */       if (itemstack.stackSize <= 0)
/*     */       {
/* 450 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*     */       }
/*     */       
/* 453 */       if (!this.worldObj.isRemote)
/*     */       {
/* 455 */         if (this.rand.nextInt(3) == 0)
/*     */         {
/* 457 */           setTamed(true);
/* 458 */           this.navigator.clearPathEntity();
/* 459 */           setAttackTarget(null);
/* 460 */           this.aiSit.setSitting(true);
/* 461 */           setHealth(20.0F);
/* 462 */           setOwnerId(player.getUniqueID().toString());
/* 463 */           playTameEffect(true);
/* 464 */           this.worldObj.setEntityState(this, (byte)7);
/*     */         }
/*     */         else
/*     */         {
/* 468 */           playTameEffect(false);
/* 469 */           this.worldObj.setEntityState(this, (byte)6);
/*     */         }
/*     */       }
/*     */       
/* 473 */       return true;
/*     */     }
/*     */     
/* 476 */     return super.interact(player);
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 481 */     if (id == 8)
/*     */     {
/* 483 */       this.isShaking = true;
/* 484 */       this.timeWolfIsShaking = 0.0F;
/* 485 */       this.prevTimeWolfIsShaking = 0.0F;
/*     */     }
/*     */     else
/*     */     {
/* 489 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getTailRotation()
/*     */   {
/* 495 */     return isTamed() ? (0.55F - (20.0F - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02F) * 3.1415927F : isAngry() ? 1.5393804F : 0.62831855F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBreedingItem(ItemStack stack)
/*     */   {
/* 504 */     return !(stack.getItem() instanceof ItemFood) ? false : stack == null ? false : ((ItemFood)stack.getItem()).isWolfsFavoriteMeat();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxSpawnedInChunk()
/*     */   {
/* 512 */     return 8;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAngry()
/*     */   {
/* 520 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x2) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAngry(boolean angry)
/*     */   {
/* 528 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 530 */     if (angry)
/*     */     {
/* 532 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x2)));
/*     */     }
/*     */     else
/*     */     {
/* 536 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFD)));
/*     */     }
/*     */   }
/*     */   
/*     */   public EnumDyeColor getCollarColor()
/*     */   {
/* 542 */     return EnumDyeColor.byDyeDamage(this.dataWatcher.getWatchableObjectByte(20) & 0xF);
/*     */   }
/*     */   
/*     */   public void setCollarColor(EnumDyeColor collarcolor)
/*     */   {
/* 547 */     this.dataWatcher.updateObject(20, Byte.valueOf((byte)(collarcolor.getDyeDamage() & 0xF)));
/*     */   }
/*     */   
/*     */   public EntityWolf createChild(EntityAgeable ageable)
/*     */   {
/* 552 */     EntityWolf entitywolf = new EntityWolf(this.worldObj);
/* 553 */     String s = getOwnerId();
/*     */     
/* 555 */     if ((s != null) && (s.trim().length() > 0))
/*     */     {
/* 557 */       entitywolf.setOwnerId(s);
/* 558 */       entitywolf.setTamed(true);
/*     */     }
/*     */     
/* 561 */     return entitywolf;
/*     */   }
/*     */   
/*     */   public void setBegging(boolean beg)
/*     */   {
/* 566 */     if (beg)
/*     */     {
/* 568 */       this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
/*     */     }
/*     */     else
/*     */     {
/* 572 */       this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canMateWith(EntityAnimal otherAnimal)
/*     */   {
/* 581 */     if (otherAnimal == this)
/*     */     {
/* 583 */       return false;
/*     */     }
/* 585 */     if (!isTamed())
/*     */     {
/* 587 */       return false;
/*     */     }
/* 589 */     if (!(otherAnimal instanceof EntityWolf))
/*     */     {
/* 591 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 595 */     EntityWolf entitywolf = (EntityWolf)otherAnimal;
/* 596 */     return entitywolf.isTamed();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isBegging()
/*     */   {
/* 602 */     return this.dataWatcher.getWatchableObjectByte(19) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDespawn()
/*     */   {
/* 610 */     return (!isTamed()) && (this.ticksExisted > 2400);
/*     */   }
/*     */   
/*     */   public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_)
/*     */   {
/* 615 */     if ((!(p_142018_1_ instanceof EntityCreeper)) && (!(p_142018_1_ instanceof EntityGhast)))
/*     */     {
/* 617 */       if ((p_142018_1_ instanceof EntityWolf))
/*     */       {
/* 619 */         EntityWolf entitywolf = (EntityWolf)p_142018_1_;
/*     */         
/* 621 */         if ((entitywolf.isTamed()) && (entitywolf.getOwner() == p_142018_2_))
/*     */         {
/* 623 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 627 */       return (!(p_142018_1_ instanceof EntityPlayer)) || (!(p_142018_2_ instanceof EntityPlayer)) || (((EntityPlayer)p_142018_2_).canAttackPlayer((EntityPlayer)p_142018_1_));
/*     */     }
/*     */     
/*     */ 
/* 631 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean allowLeashing()
/*     */   {
/* 637 */     return (!isAngry()) && (super.allowLeashing());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */