/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIFollowOwner;
/*     */ import net.minecraft.entity.ai.EntityAILeapAtTarget;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIOcelotSit;
/*     */ import net.minecraft.entity.ai.EntityAISit;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityOcelot extends EntityTameable
/*     */ {
/*     */   private EntityAIAvoidEntity<EntityPlayer> avoidEntity;
/*     */   private EntityAITempt aiTempt;
/*     */   
/*     */   public EntityOcelot(World worldIn)
/*     */   {
/*  45 */     super(worldIn);
/*  46 */     setSize(0.6F, 0.7F);
/*  47 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  48 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  49 */     this.tasks.addTask(2, this.aiSit);
/*  50 */     this.tasks.addTask(3, this.aiTempt = new EntityAITempt(this, 0.6D, Items.fish, true));
/*  51 */     this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
/*  52 */     this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8D));
/*  53 */     this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
/*  54 */     this.tasks.addTask(8, new net.minecraft.entity.ai.EntityAIOcelotAttack(this));
/*  55 */     this.tasks.addTask(9, new EntityAIMate(this, 0.8D));
/*  56 */     this.tasks.addTask(10, new EntityAIWander(this, 0.8D));
/*  57 */     this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
/*  58 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAITargetNonTamed(this, EntityChicken.class, false, null));
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  63 */     super.entityInit();
/*  64 */     this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public void updateAITasks()
/*     */   {
/*  69 */     if (getMoveHelper().isUpdating())
/*     */     {
/*  71 */       double d0 = getMoveHelper().getSpeed();
/*     */       
/*  73 */       if (d0 == 0.6D)
/*     */       {
/*  75 */         setSneaking(true);
/*  76 */         setSprinting(false);
/*     */       }
/*  78 */       else if (d0 == 1.33D)
/*     */       {
/*  80 */         setSneaking(false);
/*  81 */         setSprinting(true);
/*     */       }
/*     */       else
/*     */       {
/*  85 */         setSneaking(false);
/*  86 */         setSprinting(false);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  91 */       setSneaking(false);
/*  92 */       setSprinting(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDespawn()
/*     */   {
/* 101 */     return (!isTamed()) && (this.ticksExisted > 2400);
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/* 106 */     super.applyEntityAttributes();
/* 107 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/* 108 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 120 */     super.writeEntityToNBT(tagCompound);
/* 121 */     tagCompound.setInteger("CatType", getTameSkin());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 129 */     super.readEntityFromNBT(tagCompund);
/* 130 */     setTameSkin(tagCompund.getInteger("CatType"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 138 */     return isTamed() ? "mob.cat.meow" : this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : isInLove() ? "mob.cat.purr" : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 146 */     return "mob.cat.hitt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 154 */     return "mob.cat.hitt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundVolume()
/*     */   {
/* 162 */     return 0.4F;
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 167 */     return Items.leather;
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 172 */     return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 180 */     if (isEntityInvulnerable(source))
/*     */     {
/* 182 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 186 */     this.aiSit.setSitting(false);
/* 187 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interact(EntityPlayer player)
/*     */   {
/* 203 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 205 */     if (isTamed())
/*     */     {
/* 207 */       if ((isOwner(player)) && (!this.worldObj.isRemote) && (!isBreedingItem(itemstack)))
/*     */       {
/* 209 */         this.aiSit.setSitting(!isSitting());
/*     */       }
/*     */     }
/* 212 */     else if ((this.aiTempt.isRunning()) && (itemstack != null) && (itemstack.getItem() == Items.fish) && (player.getDistanceSqToEntity(this) < 9.0D))
/*     */     {
/* 214 */       if (!player.capabilities.isCreativeMode)
/*     */       {
/* 216 */         itemstack.stackSize -= 1;
/*     */       }
/*     */       
/* 219 */       if (itemstack.stackSize <= 0)
/*     */       {
/* 221 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*     */       }
/*     */       
/* 224 */       if (!this.worldObj.isRemote)
/*     */       {
/* 226 */         if (this.rand.nextInt(3) == 0)
/*     */         {
/* 228 */           setTamed(true);
/* 229 */           setTameSkin(1 + this.worldObj.rand.nextInt(3));
/* 230 */           setOwnerId(player.getUniqueID().toString());
/* 231 */           playTameEffect(true);
/* 232 */           this.aiSit.setSitting(true);
/* 233 */           this.worldObj.setEntityState(this, (byte)7);
/*     */         }
/*     */         else
/*     */         {
/* 237 */           playTameEffect(false);
/* 238 */           this.worldObj.setEntityState(this, (byte)6);
/*     */         }
/*     */       }
/*     */       
/* 242 */       return true;
/*     */     }
/*     */     
/* 245 */     return super.interact(player);
/*     */   }
/*     */   
/*     */   public EntityOcelot createChild(EntityAgeable ageable)
/*     */   {
/* 250 */     EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
/*     */     
/* 252 */     if (isTamed())
/*     */     {
/* 254 */       entityocelot.setOwnerId(getOwnerId());
/* 255 */       entityocelot.setTamed(true);
/* 256 */       entityocelot.setTameSkin(getTameSkin());
/*     */     }
/*     */     
/* 259 */     return entityocelot;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBreedingItem(ItemStack stack)
/*     */   {
/* 268 */     return (stack != null) && (stack.getItem() == Items.fish);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canMateWith(EntityAnimal otherAnimal)
/*     */   {
/* 276 */     if (otherAnimal == this)
/*     */     {
/* 278 */       return false;
/*     */     }
/* 280 */     if (!isTamed())
/*     */     {
/* 282 */       return false;
/*     */     }
/* 284 */     if (!(otherAnimal instanceof EntityOcelot))
/*     */     {
/* 286 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 290 */     EntityOcelot entityocelot = (EntityOcelot)otherAnimal;
/* 291 */     return entityocelot.isTamed();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getTameSkin()
/*     */   {
/* 297 */     return this.dataWatcher.getWatchableObjectByte(18);
/*     */   }
/*     */   
/*     */   public void setTameSkin(int skinId)
/*     */   {
/* 302 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)skinId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 310 */     return this.worldObj.rand.nextInt(3) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNotColliding()
/*     */   {
/* 318 */     if ((this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), this)) && (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) && (!this.worldObj.isAnyLiquid(getEntityBoundingBox())))
/*     */     {
/* 320 */       BlockPos blockpos = new BlockPos(this.posX, getEntityBoundingBox().minY, this.posZ);
/*     */       
/* 322 */       if (blockpos.getY() < this.worldObj.func_181545_F())
/*     */       {
/* 324 */         return false;
/*     */       }
/*     */       
/* 327 */       Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
/*     */       
/* 329 */       if ((block == net.minecraft.init.Blocks.grass) || (block.getMaterial() == Material.leaves))
/*     */       {
/* 331 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 335 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 343 */     return isTamed() ? net.minecraft.util.StatCollector.translateToLocal("entity.Cat.name") : hasCustomName() ? getCustomNameTag() : super.getName();
/*     */   }
/*     */   
/*     */   public void setTamed(boolean tamed)
/*     */   {
/* 348 */     super.setTamed(tamed);
/*     */   }
/*     */   
/*     */   protected void setupTamedAI()
/*     */   {
/* 353 */     if (this.avoidEntity == null)
/*     */     {
/* 355 */       this.avoidEntity = new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D);
/*     */     }
/*     */     
/* 358 */     this.tasks.removeTask(this.avoidEntity);
/*     */     
/* 360 */     if (!isTamed())
/*     */     {
/* 362 */       this.tasks.addTask(4, this.avoidEntity);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 372 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*     */     
/* 374 */     if (this.worldObj.rand.nextInt(7) == 0)
/*     */     {
/* 376 */       for (int i = 0; i < 2; i++)
/*     */       {
/* 378 */         EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
/* 379 */         entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 380 */         entityocelot.setGrowingAge(41536);
/* 381 */         this.worldObj.spawnEntityInWorld(entityocelot);
/*     */       }
/*     */     }
/*     */     
/* 385 */     return livingdata;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityOcelot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */