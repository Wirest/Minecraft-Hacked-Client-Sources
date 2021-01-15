/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityItem extends Entity
/*     */ {
/*  22 */   private static final org.apache.logging.log4j.Logger logger = ;
/*     */   
/*     */ 
/*     */   private int age;
/*     */   
/*     */   private int delayBeforeCanPickup;
/*     */   
/*     */   private int health;
/*     */   
/*     */   private String thrower;
/*     */   
/*     */   private String owner;
/*     */   
/*     */   public float hoverStart;
/*     */   
/*     */ 
/*     */   public EntityItem(World worldIn, double x, double y, double z)
/*     */   {
/*  40 */     super(worldIn);
/*  41 */     this.health = 5;
/*  42 */     this.hoverStart = ((float)(Math.random() * 3.141592653589793D * 2.0D));
/*  43 */     setSize(0.25F, 0.25F);
/*  44 */     setPosition(x, y, z);
/*  45 */     this.rotationYaw = ((float)(Math.random() * 360.0D));
/*  46 */     this.motionX = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
/*  47 */     this.motionY = 0.20000000298023224D;
/*  48 */     this.motionZ = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
/*     */   }
/*     */   
/*     */   public EntityItem(World worldIn, double x, double y, double z, ItemStack stack)
/*     */   {
/*  53 */     this(worldIn, x, y, z);
/*  54 */     setEntityItemStack(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  63 */     return false;
/*     */   }
/*     */   
/*     */   public EntityItem(World worldIn)
/*     */   {
/*  68 */     super(worldIn);
/*  69 */     this.health = 5;
/*  70 */     this.hoverStart = ((float)(Math.random() * 3.141592653589793D * 2.0D));
/*  71 */     setSize(0.25F, 0.25F);
/*  72 */     setEntityItemStack(new ItemStack(Blocks.air, 0));
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  77 */     getDataWatcher().addObjectByDataType(10, 5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  85 */     if (getEntityItem() == null)
/*     */     {
/*  87 */       setDead();
/*     */     }
/*     */     else
/*     */     {
/*  91 */       super.onUpdate();
/*     */       
/*  93 */       if ((this.delayBeforeCanPickup > 0) && (this.delayBeforeCanPickup != 32767))
/*     */       {
/*  95 */         this.delayBeforeCanPickup -= 1;
/*     */       }
/*     */       
/*  98 */       this.prevPosX = this.posX;
/*  99 */       this.prevPosY = this.posY;
/* 100 */       this.prevPosZ = this.posZ;
/* 101 */       this.motionY -= 0.03999999910593033D;
/* 102 */       this.noClip = pushOutOfBlocks(this.posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, this.posZ);
/* 103 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/* 104 */       boolean flag = ((int)this.prevPosX != (int)this.posX) || ((int)this.prevPosY != (int)this.posY) || ((int)this.prevPosZ != (int)this.posZ);
/*     */       
/* 106 */       if ((flag) || (this.ticksExisted % 25 == 0))
/*     */       {
/* 108 */         if (this.worldObj.getBlockState(new net.minecraft.util.BlockPos(this)).getBlock().getMaterial() == net.minecraft.block.material.Material.lava)
/*     */         {
/* 110 */           this.motionY = 0.20000000298023224D;
/* 111 */           this.motionX = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/* 112 */           this.motionZ = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/* 113 */           playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
/*     */         }
/*     */         
/* 116 */         if (!this.worldObj.isRemote)
/*     */         {
/* 118 */           searchForOtherItemsNearby();
/*     */         }
/*     */       }
/*     */       
/* 122 */       float f = 0.98F;
/*     */       
/* 124 */       if (this.onGround)
/*     */       {
/* 126 */         f = this.worldObj.getBlockState(new net.minecraft.util.BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
/*     */       }
/*     */       
/* 129 */       this.motionX *= f;
/* 130 */       this.motionY *= 0.9800000190734863D;
/* 131 */       this.motionZ *= f;
/*     */       
/* 133 */       if (this.onGround)
/*     */       {
/* 135 */         this.motionY *= -0.5D;
/*     */       }
/*     */       
/* 138 */       if (this.age != 32768)
/*     */       {
/* 140 */         this.age += 1;
/*     */       }
/*     */       
/* 143 */       handleWaterMovement();
/*     */       
/* 145 */       if ((!this.worldObj.isRemote) && (this.age >= 6000))
/*     */       {
/* 147 */         setDead();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void searchForOtherItemsNearby()
/*     */   {
/* 157 */     for (EntityItem entityitem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(0.5D, 0.0D, 0.5D)))
/*     */     {
/* 159 */       combineItems(entityitem);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean combineItems(EntityItem other)
/*     */   {
/* 169 */     if (other == this)
/*     */     {
/* 171 */       return false;
/*     */     }
/* 173 */     if ((other.isEntityAlive()) && (isEntityAlive()))
/*     */     {
/* 175 */       ItemStack itemstack = getEntityItem();
/* 176 */       ItemStack itemstack1 = other.getEntityItem();
/*     */       
/* 178 */       if ((this.delayBeforeCanPickup != 32767) && (other.delayBeforeCanPickup != 32767))
/*     */       {
/* 180 */         if ((this.age != 32768) && (other.age != 32768))
/*     */         {
/* 182 */           if (itemstack1.getItem() != itemstack.getItem())
/*     */           {
/* 184 */             return false;
/*     */           }
/* 186 */           if ((itemstack1.hasTagCompound() ^ itemstack.hasTagCompound()))
/*     */           {
/* 188 */             return false;
/*     */           }
/* 190 */           if ((itemstack1.hasTagCompound()) && (!itemstack1.getTagCompound().equals(itemstack.getTagCompound())))
/*     */           {
/* 192 */             return false;
/*     */           }
/* 194 */           if (itemstack1.getItem() == null)
/*     */           {
/* 196 */             return false;
/*     */           }
/* 198 */           if ((itemstack1.getItem().getHasSubtypes()) && (itemstack1.getMetadata() != itemstack.getMetadata()))
/*     */           {
/* 200 */             return false;
/*     */           }
/* 202 */           if (itemstack1.stackSize < itemstack.stackSize)
/*     */           {
/* 204 */             return other.combineItems(this);
/*     */           }
/* 206 */           if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize())
/*     */           {
/* 208 */             return false;
/*     */           }
/*     */           
/*     */ 
/* 212 */           itemstack1.stackSize += itemstack.stackSize;
/* 213 */           other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
/* 214 */           other.age = Math.min(other.age, this.age);
/* 215 */           other.setEntityItemStack(itemstack1);
/* 216 */           setDead();
/* 217 */           return true;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 222 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 227 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 232 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAgeToCreativeDespawnTime()
/*     */   {
/* 242 */     this.age = 4800;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean handleWaterMovement()
/*     */   {
/* 250 */     if (this.worldObj.handleMaterialAcceleration(getEntityBoundingBox(), net.minecraft.block.material.Material.water, this))
/*     */     {
/* 252 */       if ((!this.inWater) && (!this.firstUpdate))
/*     */       {
/* 254 */         resetHeight();
/*     */       }
/*     */       
/* 257 */       this.inWater = true;
/*     */     }
/*     */     else
/*     */     {
/* 261 */       this.inWater = false;
/*     */     }
/*     */     
/* 264 */     return this.inWater;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dealFireDamage(int amount)
/*     */   {
/* 273 */     attackEntityFrom(DamageSource.inFire, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 281 */     if (isEntityInvulnerable(source))
/*     */     {
/* 283 */       return false;
/*     */     }
/* 285 */     if ((getEntityItem() != null) && (getEntityItem().getItem() == Items.nether_star) && (source.isExplosion()))
/*     */     {
/* 287 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 291 */     setBeenAttacked();
/* 292 */     this.health = ((int)(this.health - amount));
/*     */     
/* 294 */     if (this.health <= 0)
/*     */     {
/* 296 */       setDead();
/*     */     }
/*     */     
/* 299 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 308 */     tagCompound.setShort("Health", (short)(byte)this.health);
/* 309 */     tagCompound.setShort("Age", (short)this.age);
/* 310 */     tagCompound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
/*     */     
/* 312 */     if (getThrower() != null)
/*     */     {
/* 314 */       tagCompound.setString("Thrower", this.thrower);
/*     */     }
/*     */     
/* 317 */     if (getOwner() != null)
/*     */     {
/* 319 */       tagCompound.setString("Owner", this.owner);
/*     */     }
/*     */     
/* 322 */     if (getEntityItem() != null)
/*     */     {
/* 324 */       tagCompound.setTag("Item", getEntityItem().writeToNBT(new NBTTagCompound()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 333 */     this.health = (tagCompund.getShort("Health") & 0xFF);
/* 334 */     this.age = tagCompund.getShort("Age");
/*     */     
/* 336 */     if (tagCompund.hasKey("PickupDelay"))
/*     */     {
/* 338 */       this.delayBeforeCanPickup = tagCompund.getShort("PickupDelay");
/*     */     }
/*     */     
/* 341 */     if (tagCompund.hasKey("Owner"))
/*     */     {
/* 343 */       this.owner = tagCompund.getString("Owner");
/*     */     }
/*     */     
/* 346 */     if (tagCompund.hasKey("Thrower"))
/*     */     {
/* 348 */       this.thrower = tagCompund.getString("Thrower");
/*     */     }
/*     */     
/* 351 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Item");
/* 352 */     setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound));
/*     */     
/* 354 */     if (getEntityItem() == null)
/*     */     {
/* 356 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn)
/*     */   {
/* 365 */     if (!this.worldObj.isRemote)
/*     */     {
/* 367 */       ItemStack itemstack = getEntityItem();
/* 368 */       int i = itemstack.stackSize;
/*     */       
/* 370 */       if ((this.delayBeforeCanPickup == 0) && ((this.owner == null) || (6000 - this.age <= 200) || (this.owner.equals(entityIn.getName()))) && (entityIn.inventory.addItemStackToInventory(itemstack)))
/*     */       {
/* 372 */         if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log))
/*     */         {
/* 374 */           entityIn.triggerAchievement(AchievementList.mineWood);
/*     */         }
/*     */         
/* 377 */         if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log2))
/*     */         {
/* 379 */           entityIn.triggerAchievement(AchievementList.mineWood);
/*     */         }
/*     */         
/* 382 */         if (itemstack.getItem() == Items.leather)
/*     */         {
/* 384 */           entityIn.triggerAchievement(AchievementList.killCow);
/*     */         }
/*     */         
/* 387 */         if (itemstack.getItem() == Items.diamond)
/*     */         {
/* 389 */           entityIn.triggerAchievement(AchievementList.diamonds);
/*     */         }
/*     */         
/* 392 */         if (itemstack.getItem() == Items.blaze_rod)
/*     */         {
/* 394 */           entityIn.triggerAchievement(AchievementList.blazeRod);
/*     */         }
/*     */         
/* 397 */         if ((itemstack.getItem() == Items.diamond) && (getThrower() != null))
/*     */         {
/* 399 */           EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(getThrower());
/*     */           
/* 401 */           if ((entityplayer != null) && (entityplayer != entityIn))
/*     */           {
/* 403 */             entityplayer.triggerAchievement(AchievementList.diamondsToYou);
/*     */           }
/*     */         }
/*     */         
/* 407 */         if (!isSilent())
/*     */         {
/* 409 */           this.worldObj.playSoundAtEntity(entityIn, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*     */         }
/*     */         
/* 412 */         entityIn.onItemPickup(this, i);
/*     */         
/* 414 */         if (itemstack.stackSize <= 0)
/*     */         {
/* 416 */           setDead();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 427 */     return hasCustomName() ? getCustomNameTag() : net.minecraft.util.StatCollector.translateToLocal("item." + getEntityItem().getUnlocalizedName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canAttackWithItem()
/*     */   {
/* 435 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void travelToDimension(int dimensionId)
/*     */   {
/* 443 */     super.travelToDimension(dimensionId);
/*     */     
/* 445 */     if (!this.worldObj.isRemote)
/*     */     {
/* 447 */       searchForOtherItemsNearby();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getEntityItem()
/*     */   {
/* 457 */     ItemStack itemstack = getDataWatcher().getWatchableObjectItemStack(10);
/*     */     
/* 459 */     if (itemstack == null)
/*     */     {
/* 461 */       if (this.worldObj != null)
/*     */       {
/* 463 */         logger.error("Item entity " + getEntityId() + " has no item?!");
/*     */       }
/*     */       
/* 466 */       return new ItemStack(Blocks.stone);
/*     */     }
/*     */     
/*     */ 
/* 470 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEntityItemStack(ItemStack stack)
/*     */   {
/* 479 */     getDataWatcher().updateObject(10, stack);
/* 480 */     getDataWatcher().setObjectWatched(10);
/*     */   }
/*     */   
/*     */   public String getOwner()
/*     */   {
/* 485 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(String owner)
/*     */   {
/* 490 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public String getThrower()
/*     */   {
/* 495 */     return this.thrower;
/*     */   }
/*     */   
/*     */   public void setThrower(String thrower)
/*     */   {
/* 500 */     this.thrower = thrower;
/*     */   }
/*     */   
/*     */   public int getAge()
/*     */   {
/* 505 */     return this.age;
/*     */   }
/*     */   
/*     */   public void setDefaultPickupDelay()
/*     */   {
/* 510 */     this.delayBeforeCanPickup = 10;
/*     */   }
/*     */   
/*     */   public void setNoPickupDelay()
/*     */   {
/* 515 */     this.delayBeforeCanPickup = 0;
/*     */   }
/*     */   
/*     */   public void setInfinitePickupDelay()
/*     */   {
/* 520 */     this.delayBeforeCanPickup = 32767;
/*     */   }
/*     */   
/*     */   public void setPickupDelay(int ticks)
/*     */   {
/* 525 */     this.delayBeforeCanPickup = ticks;
/*     */   }
/*     */   
/*     */   public boolean cannotPickup()
/*     */   {
/* 530 */     return this.delayBeforeCanPickup > 0;
/*     */   }
/*     */   
/*     */   public void setNoDespawn()
/*     */   {
/* 535 */     this.age = 59536;
/*     */   }
/*     */   
/*     */   public void func_174870_v()
/*     */   {
/* 540 */     setInfinitePickupDelay();
/* 541 */     this.age = 5999;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */