/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIArrowAttack;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIFleeSun;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProviderHell;
/*     */ 
/*     */ public class EntitySkeleton extends EntityMob implements IRangedAttackMob
/*     */ {
/*  44 */   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
/*  45 */   private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
/*     */   
/*     */   public EntitySkeleton(World worldIn)
/*     */   {
/*  49 */     super(worldIn);
/*  50 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  51 */     this.tasks.addTask(2, new net.minecraft.entity.ai.EntityAIRestrictSun(this));
/*  52 */     this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
/*  53 */     this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
/*  54 */     this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
/*  55 */     this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  56 */     this.tasks.addTask(6, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  57 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, false, new Class[0]));
/*  58 */     this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*  59 */     this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
/*     */     
/*  61 */     if ((worldIn != null) && (!worldIn.isRemote))
/*     */     {
/*  63 */       setCombatTask();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  69 */     super.applyEntityAttributes();
/*  70 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  75 */     super.entityInit();
/*  76 */     this.dataWatcher.addObject(13, new Byte((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  84 */     return "mob.skeleton.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  92 */     return "mob.skeleton.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 100 */     return "mob.skeleton.death";
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 105 */     playSound("mob.skeleton.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 110 */     if (super.attackEntityAsMob(entityIn))
/*     */     {
/* 112 */       if ((getSkeletonType() == 1) && ((entityIn instanceof EntityLivingBase)))
/*     */       {
/* 114 */         ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
/*     */       }
/*     */       
/* 117 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 121 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumCreatureAttribute getCreatureAttribute()
/*     */   {
/* 130 */     return EnumCreatureAttribute.UNDEAD;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 139 */     if ((this.worldObj.isDaytime()) && (!this.worldObj.isRemote))
/*     */     {
/* 141 */       float f = getBrightness(1.0F);
/* 142 */       BlockPos blockpos = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
/*     */       
/* 144 */       if ((f > 0.5F) && (this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) && (this.worldObj.canSeeSky(blockpos)))
/*     */       {
/* 146 */         boolean flag = true;
/* 147 */         ItemStack itemstack = getEquipmentInSlot(4);
/*     */         
/* 149 */         if (itemstack != null)
/*     */         {
/* 151 */           if (itemstack.isItemStackDamageable())
/*     */           {
/* 153 */             itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
/*     */             
/* 155 */             if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
/*     */             {
/* 157 */               renderBrokenItemStack(itemstack);
/* 158 */               setCurrentItemOrArmor(4, null);
/*     */             }
/*     */           }
/*     */           
/* 162 */           flag = false;
/*     */         }
/*     */         
/* 165 */         if (flag)
/*     */         {
/* 167 */           setFire(8);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 172 */     if ((this.worldObj.isRemote) && (getSkeletonType() == 1))
/*     */     {
/* 174 */       setSize(0.72F, 2.535F);
/*     */     }
/*     */     
/* 177 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateRidden()
/*     */   {
/* 185 */     super.updateRidden();
/*     */     
/* 187 */     if ((this.ridingEntity instanceof EntityCreature))
/*     */     {
/* 189 */       EntityCreature entitycreature = (EntityCreature)this.ridingEntity;
/* 190 */       this.renderYawOffset = entitycreature.renderYawOffset;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDeath(DamageSource cause)
/*     */   {
/* 199 */     super.onDeath(cause);
/*     */     
/* 201 */     if (((cause.getSourceOfDamage() instanceof EntityArrow)) && ((cause.getEntity() instanceof EntityPlayer)))
/*     */     {
/* 203 */       EntityPlayer entityplayer = (EntityPlayer)cause.getEntity();
/* 204 */       double d0 = entityplayer.posX - this.posX;
/* 205 */       double d1 = entityplayer.posZ - this.posZ;
/*     */       
/* 207 */       if (d0 * d0 + d1 * d1 >= 2500.0D)
/*     */       {
/* 209 */         entityplayer.triggerAchievement(net.minecraft.stats.AchievementList.snipeSkeleton);
/*     */       }
/*     */     }
/* 212 */     else if (((cause.getEntity() instanceof EntityCreeper)) && (((EntityCreeper)cause.getEntity()).getPowered()) && (((EntityCreeper)cause.getEntity()).isAIEnabled()))
/*     */     {
/* 214 */       ((EntityCreeper)cause.getEntity()).func_175493_co();
/* 215 */       entityDropItem(new ItemStack(Items.skull, 1, getSkeletonType() == 1 ? 1 : 0), 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 221 */     return Items.arrow;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 229 */     if (getSkeletonType() == 1)
/*     */     {
/* 231 */       int i = this.rand.nextInt(3 + p_70628_2_) - 1;
/*     */       
/* 233 */       for (int j = 0; j < i; j++)
/*     */       {
/* 235 */         dropItem(Items.coal, 1);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 240 */       int k = this.rand.nextInt(3 + p_70628_2_);
/*     */       
/* 242 */       for (int i1 = 0; i1 < k; i1++)
/*     */       {
/* 244 */         dropItem(Items.arrow, 1);
/*     */       }
/*     */     }
/*     */     
/* 248 */     int l = this.rand.nextInt(3 + p_70628_2_);
/*     */     
/* 250 */     for (int j1 = 0; j1 < l; j1++)
/*     */     {
/* 252 */       dropItem(Items.bone, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addRandomDrop()
/*     */   {
/* 261 */     if (getSkeletonType() == 1)
/*     */     {
/* 263 */       entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
/*     */   {
/* 272 */     super.setEquipmentBasedOnDifficulty(difficulty);
/* 273 */     setCurrentItemOrArmor(0, new ItemStack(Items.bow));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 282 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*     */     
/* 284 */     if (((this.worldObj.provider instanceof WorldProviderHell)) && (getRNG().nextInt(5) > 0))
/*     */     {
/* 286 */       this.tasks.addTask(4, this.aiAttackOnCollide);
/* 287 */       setSkeletonType(1);
/* 288 */       setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
/* 289 */       getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
/*     */     }
/*     */     else
/*     */     {
/* 293 */       this.tasks.addTask(4, this.aiArrowAttack);
/* 294 */       setEquipmentBasedOnDifficulty(difficulty);
/* 295 */       setEnchantmentBasedOnDifficulty(difficulty);
/*     */     }
/*     */     
/* 298 */     setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());
/*     */     
/* 300 */     if (getEquipmentInSlot(4) == null)
/*     */     {
/* 302 */       Calendar calendar = this.worldObj.getCurrentDate();
/*     */       
/* 304 */       if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31) && (this.rand.nextFloat() < 0.25F))
/*     */       {
/* 306 */         setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
/* 307 */         this.equipmentDropChances[4] = 0.0F;
/*     */       }
/*     */     }
/*     */     
/* 311 */     return livingdata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCombatTask()
/*     */   {
/* 319 */     this.tasks.removeTask(this.aiAttackOnCollide);
/* 320 */     this.tasks.removeTask(this.aiArrowAttack);
/* 321 */     ItemStack itemstack = getHeldItem();
/*     */     
/* 323 */     if ((itemstack != null) && (itemstack.getItem() == Items.bow))
/*     */     {
/* 325 */       this.tasks.addTask(4, this.aiArrowAttack);
/*     */     }
/*     */     else
/*     */     {
/* 329 */       this.tasks.addTask(4, this.aiAttackOnCollide);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
/*     */   {
/* 338 */     EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, 14 - this.worldObj.getDifficulty().getDifficultyId() * 4);
/* 339 */     int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, getHeldItem());
/* 340 */     int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, getHeldItem());
/* 341 */     entityarrow.setDamage(p_82196_2_ * 2.0F + this.rand.nextGaussian() * 0.25D + this.worldObj.getDifficulty().getDifficultyId() * 0.11F);
/*     */     
/* 343 */     if (i > 0)
/*     */     {
/* 345 */       entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
/*     */     }
/*     */     
/* 348 */     if (j > 0)
/*     */     {
/* 350 */       entityarrow.setKnockbackStrength(j);
/*     */     }
/*     */     
/* 353 */     if ((EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, getHeldItem()) > 0) || (getSkeletonType() == 1))
/*     */     {
/* 355 */       entityarrow.setFire(100);
/*     */     }
/*     */     
/* 358 */     playSound("random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
/* 359 */     this.worldObj.spawnEntityInWorld(entityarrow);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSkeletonType()
/*     */   {
/* 367 */     return this.dataWatcher.getWatchableObjectByte(13);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSkeletonType(int p_82201_1_)
/*     */   {
/* 375 */     this.dataWatcher.updateObject(13, Byte.valueOf((byte)p_82201_1_));
/* 376 */     this.isImmuneToFire = (p_82201_1_ == 1);
/*     */     
/* 378 */     if (p_82201_1_ == 1)
/*     */     {
/* 380 */       setSize(0.72F, 2.535F);
/*     */     }
/*     */     else
/*     */     {
/* 384 */       setSize(0.6F, 1.95F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 393 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 395 */     if (tagCompund.hasKey("SkeletonType", 99))
/*     */     {
/* 397 */       int i = tagCompund.getByte("SkeletonType");
/* 398 */       setSkeletonType(i);
/*     */     }
/*     */     
/* 401 */     setCombatTask();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 409 */     super.writeEntityToNBT(tagCompound);
/* 410 */     tagCompound.setByte("SkeletonType", (byte)getSkeletonType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
/*     */   {
/* 418 */     super.setCurrentItemOrArmor(slotIn, stack);
/*     */     
/* 420 */     if ((!this.worldObj.isRemote) && (slotIn == 0))
/*     */     {
/* 422 */       setCombatTask();
/*     */     }
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 428 */     return getSkeletonType() == 1 ? super.getEyeHeight() : 1.74F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getYOffset()
/*     */   {
/* 436 */     return isChild() ? 0.0D : -0.35D;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntitySkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */