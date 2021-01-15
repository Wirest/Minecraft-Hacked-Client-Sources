/*      */ package net.minecraft.entity.player;
/*      */ 
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockBed;
/*      */ import net.minecraft.block.BlockDirectional;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.command.server.CommandBlockLogic;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.DataWatcher;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.EntityList.EntityEggInfo;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.IEntityMultiPart;
/*      */ import net.minecraft.entity.IMerchant;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.boss.EntityDragonPart;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityMinecart;
/*      */ import net.minecraft.entity.monster.EntityMob;
/*      */ import net.minecraft.entity.passive.EntityHorse;
/*      */ import net.minecraft.entity.passive.EntityPig;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.entity.projectile.EntityFishHook;
/*      */ import net.minecraft.event.ClickEvent;
/*      */ import net.minecraft.event.ClickEvent.Action;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.InventoryEnderChest;
/*      */ import net.minecraft.item.EnumAction;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.stats.StatList;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.CombatTracker;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.FoodStats;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.GameRules;
/*      */ import net.minecraft.world.IInteractionObject;
/*      */ import net.minecraft.world.LockCode;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldSettings.GameType;
/*      */ 
/*      */ public abstract class EntityPlayer extends EntityLivingBase
/*      */ {
/*   82 */   public InventoryPlayer inventory = new InventoryPlayer(this);
/*   83 */   private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
/*      */   
/*      */ 
/*      */ 
/*      */   public Container inventoryContainer;
/*      */   
/*      */ 
/*      */ 
/*      */   public Container openContainer;
/*      */   
/*      */ 
/*   94 */   protected FoodStats foodStats = new FoodStats();
/*      */   
/*      */ 
/*      */   protected int flyToggleTimer;
/*      */   
/*      */ 
/*      */   public float prevCameraYaw;
/*      */   
/*      */ 
/*      */   public float cameraYaw;
/*      */   
/*      */   public int xpCooldown;
/*      */   
/*      */   public double prevChasingPosX;
/*      */   
/*      */   public double prevChasingPosY;
/*      */   
/*      */   public double prevChasingPosZ;
/*      */   
/*      */   public double chasingPosX;
/*      */   
/*      */   public double chasingPosY;
/*      */   
/*      */   public double chasingPosZ;
/*      */   
/*      */   protected boolean sleeping;
/*      */   
/*      */   public BlockPos playerLocation;
/*      */   
/*      */   private int sleepTimer;
/*      */   
/*      */   public float renderOffsetX;
/*      */   
/*      */   public float renderOffsetY;
/*      */   
/*      */   public float renderOffsetZ;
/*      */   
/*      */   private BlockPos spawnChunk;
/*      */   
/*      */   private boolean spawnForced;
/*      */   
/*      */   private BlockPos startMinecartRidingCoordinate;
/*      */   
/*  137 */   public PlayerCapabilities capabilities = new PlayerCapabilities();
/*      */   
/*      */ 
/*      */ 
/*      */   public int experienceLevel;
/*      */   
/*      */ 
/*      */ 
/*      */   public int experienceTotal;
/*      */   
/*      */ 
/*      */ 
/*      */   public float experience;
/*      */   
/*      */ 
/*      */ 
/*      */   private int xpSeed;
/*      */   
/*      */ 
/*      */ 
/*      */   private ItemStack itemInUse;
/*      */   
/*      */ 
/*      */   private int itemInUseCount;
/*      */   
/*      */ 
/*  163 */   protected float speedOnGround = 0.1F;
/*  164 */   protected float speedInAir = 0.02F;
/*      */   
/*      */   private int lastXPSound;
/*      */   
/*      */   private final GameProfile gameProfile;
/*  169 */   private boolean hasReducedDebug = false;
/*      */   
/*      */ 
/*      */   public EntityFishHook fishEntity;
/*      */   
/*      */ 
/*      */ 
/*      */   public EntityPlayer(World worldIn, GameProfile gameProfileIn)
/*      */   {
/*  178 */     super(worldIn);
/*  179 */     this.entityUniqueID = getUUID(gameProfileIn);
/*  180 */     this.gameProfile = gameProfileIn;
/*  181 */     this.inventoryContainer = new net.minecraft.inventory.ContainerPlayer(this.inventory, !worldIn.isRemote, this);
/*  182 */     this.openContainer = this.inventoryContainer;
/*  183 */     BlockPos blockpos = worldIn.getSpawnPoint();
/*  184 */     setLocationAndAngles(blockpos.getX() + 0.5D, blockpos.getY() + 1, blockpos.getZ() + 0.5D, 0.0F, 0.0F);
/*  185 */     this.field_70741_aB = 180.0F;
/*  186 */     this.fireResistance = 20;
/*      */   }
/*      */   
/*      */   protected void applyEntityAttributes()
/*      */   {
/*  191 */     super.applyEntityAttributes();
/*  192 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
/*  193 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
/*      */   }
/*      */   
/*      */   protected void entityInit()
/*      */   {
/*  198 */     super.entityInit();
/*  199 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*  200 */     this.dataWatcher.addObject(17, Float.valueOf(0.0F));
/*  201 */     this.dataWatcher.addObject(18, Integer.valueOf(0));
/*  202 */     this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack getItemInUse()
/*      */   {
/*  210 */     return this.itemInUse;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getItemInUseCount()
/*      */   {
/*  218 */     return this.itemInUseCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUsingItem()
/*      */   {
/*  226 */     return this.itemInUse != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getItemInUseDuration()
/*      */   {
/*  234 */     return isUsingItem() ? this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount : 0;
/*      */   }
/*      */   
/*      */   public void stopUsingItem()
/*      */   {
/*  239 */     if (this.itemInUse != null)
/*      */     {
/*  241 */       this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
/*      */     }
/*      */     
/*  244 */     clearItemInUse();
/*      */   }
/*      */   
/*      */   public void clearItemInUse()
/*      */   {
/*  249 */     this.itemInUse = null;
/*  250 */     this.itemInUseCount = 0;
/*      */     
/*  252 */     if (!this.worldObj.isRemote)
/*      */     {
/*  254 */       setEating(false);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isBlocking()
/*      */   {
/*  260 */     return (isUsingItem()) && (this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.BLOCK);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate()
/*      */   {
/*  268 */     this.noClip = isSpectator();
/*      */     
/*  270 */     if (isSpectator())
/*      */     {
/*  272 */       this.onGround = false;
/*      */     }
/*      */     
/*  275 */     if (this.itemInUse != null)
/*      */     {
/*  277 */       ItemStack itemstack = this.inventory.getCurrentItem();
/*      */       
/*  279 */       if (itemstack == this.itemInUse)
/*      */       {
/*  281 */         if ((this.itemInUseCount <= 25) && (this.itemInUseCount % 4 == 0))
/*      */         {
/*  283 */           updateItemUse(itemstack, 5);
/*      */         }
/*      */         
/*  286 */         if ((--this.itemInUseCount == 0) && (!this.worldObj.isRemote))
/*      */         {
/*  288 */           onItemUseFinish();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  293 */         clearItemInUse();
/*      */       }
/*      */     }
/*      */     
/*  297 */     if (this.xpCooldown > 0)
/*      */     {
/*  299 */       this.xpCooldown -= 1;
/*      */     }
/*      */     
/*  302 */     if (isPlayerSleeping())
/*      */     {
/*  304 */       this.sleepTimer += 1;
/*      */       
/*  306 */       if (this.sleepTimer > 100)
/*      */       {
/*  308 */         this.sleepTimer = 100;
/*      */       }
/*      */       
/*  311 */       if (!this.worldObj.isRemote)
/*      */       {
/*  313 */         if (!isInBed())
/*      */         {
/*  315 */           wakeUpPlayer(true, true, false);
/*      */         }
/*  317 */         else if (this.worldObj.isDaytime())
/*      */         {
/*  319 */           wakeUpPlayer(false, true, true);
/*      */         }
/*      */       }
/*      */     }
/*  323 */     else if (this.sleepTimer > 0)
/*      */     {
/*  325 */       this.sleepTimer += 1;
/*      */       
/*  327 */       if (this.sleepTimer >= 110)
/*      */       {
/*  329 */         this.sleepTimer = 0;
/*      */       }
/*      */     }
/*      */     
/*  333 */     super.onUpdate();
/*      */     
/*  335 */     if ((!this.worldObj.isRemote) && (this.openContainer != null) && (!this.openContainer.canInteractWith(this)))
/*      */     {
/*  337 */       closeScreen();
/*  338 */       this.openContainer = this.inventoryContainer;
/*      */     }
/*      */     
/*  341 */     if ((isBurning()) && (this.capabilities.disableDamage))
/*      */     {
/*  343 */       extinguish();
/*      */     }
/*      */     
/*  346 */     this.prevChasingPosX = this.chasingPosX;
/*  347 */     this.prevChasingPosY = this.chasingPosY;
/*  348 */     this.prevChasingPosZ = this.chasingPosZ;
/*  349 */     double d5 = this.posX - this.chasingPosX;
/*  350 */     double d0 = this.posY - this.chasingPosY;
/*  351 */     double d1 = this.posZ - this.chasingPosZ;
/*  352 */     double d2 = 10.0D;
/*      */     
/*  354 */     if (d5 > d2)
/*      */     {
/*  356 */       this.prevChasingPosX = (this.chasingPosX = this.posX);
/*      */     }
/*      */     
/*  359 */     if (d1 > d2)
/*      */     {
/*  361 */       this.prevChasingPosZ = (this.chasingPosZ = this.posZ);
/*      */     }
/*      */     
/*  364 */     if (d0 > d2)
/*      */     {
/*  366 */       this.prevChasingPosY = (this.chasingPosY = this.posY);
/*      */     }
/*      */     
/*  369 */     if (d5 < -d2)
/*      */     {
/*  371 */       this.prevChasingPosX = (this.chasingPosX = this.posX);
/*      */     }
/*      */     
/*  374 */     if (d1 < -d2)
/*      */     {
/*  376 */       this.prevChasingPosZ = (this.chasingPosZ = this.posZ);
/*      */     }
/*      */     
/*  379 */     if (d0 < -d2)
/*      */     {
/*  381 */       this.prevChasingPosY = (this.chasingPosY = this.posY);
/*      */     }
/*      */     
/*  384 */     this.chasingPosX += d5 * 0.25D;
/*  385 */     this.chasingPosZ += d1 * 0.25D;
/*  386 */     this.chasingPosY += d0 * 0.25D;
/*      */     
/*  388 */     if (this.ridingEntity == null)
/*      */     {
/*  390 */       this.startMinecartRidingCoordinate = null;
/*      */     }
/*      */     
/*  393 */     if (!this.worldObj.isRemote)
/*      */     {
/*  395 */       this.foodStats.onUpdate(this);
/*  396 */       triggerAchievement(StatList.minutesPlayedStat);
/*      */       
/*  398 */       if (isEntityAlive())
/*      */       {
/*  400 */         triggerAchievement(StatList.timeSinceDeathStat);
/*      */       }
/*      */     }
/*      */     
/*  404 */     int i = 29999999;
/*  405 */     double d3 = MathHelper.clamp_double(this.posX, -2.9999999E7D, 2.9999999E7D);
/*  406 */     double d4 = MathHelper.clamp_double(this.posZ, -2.9999999E7D, 2.9999999E7D);
/*      */     
/*  408 */     if ((d3 != this.posX) || (d4 != this.posZ))
/*      */     {
/*  410 */       setPosition(d3, this.posY, d4);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxInPortalTime()
/*      */   {
/*  419 */     return this.capabilities.disableDamage ? 0 : 80;
/*      */   }
/*      */   
/*      */   protected String getSwimSound()
/*      */   {
/*  424 */     return "game.player.swim";
/*      */   }
/*      */   
/*      */   protected String getSplashSound()
/*      */   {
/*  429 */     return "game.player.swim.splash";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPortalCooldown()
/*      */   {
/*  437 */     return 10;
/*      */   }
/*      */   
/*      */   public void playSound(String name, float volume, float pitch)
/*      */   {
/*  442 */     this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateItemUse(ItemStack itemStackIn, int p_71010_2_)
/*      */   {
/*  450 */     if (itemStackIn.getItemUseAction() == EnumAction.DRINK)
/*      */     {
/*  452 */       playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*      */     }
/*      */     
/*  455 */     if (itemStackIn.getItemUseAction() == EnumAction.EAT)
/*      */     {
/*  457 */       for (int i = 0; i < p_71010_2_; i++)
/*      */       {
/*  459 */         Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
/*  460 */         vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/*  461 */         vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/*  462 */         double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
/*  463 */         Vec3 vec31 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
/*  464 */         vec31 = vec31.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/*  465 */         vec31 = vec31.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/*  466 */         vec31 = vec31.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
/*      */         
/*  468 */         if (itemStackIn.getHasSubtypes())
/*      */         {
/*  470 */           this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()), itemStackIn.getMetadata() });
/*      */         }
/*      */         else
/*      */         {
/*  474 */           this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()) });
/*      */         }
/*      */       }
/*      */       
/*  478 */       playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void onItemUseFinish()
/*      */   {
/*  487 */     if (this.itemInUse != null)
/*      */     {
/*  489 */       updateItemUse(this.itemInUse, 16);
/*  490 */       int i = this.itemInUse.stackSize;
/*  491 */       ItemStack itemstack = this.itemInUse.onItemUseFinish(this.worldObj, this);
/*      */       
/*  493 */       if ((itemstack != this.itemInUse) || ((itemstack != null) && (itemstack.stackSize != i)))
/*      */       {
/*  495 */         this.inventory.mainInventory[this.inventory.currentItem] = itemstack;
/*      */         
/*  497 */         if (itemstack.stackSize == 0)
/*      */         {
/*  499 */           this.inventory.mainInventory[this.inventory.currentItem] = null;
/*      */         }
/*      */       }
/*      */       
/*  503 */       clearItemInUse();
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleStatusUpdate(byte id)
/*      */   {
/*  509 */     if (id == 9)
/*      */     {
/*  511 */       onItemUseFinish();
/*      */     }
/*  513 */     else if (id == 23)
/*      */     {
/*  515 */       this.hasReducedDebug = false;
/*      */     }
/*  517 */     else if (id == 22)
/*      */     {
/*  519 */       this.hasReducedDebug = true;
/*      */     }
/*      */     else
/*      */     {
/*  523 */       super.handleStatusUpdate(id);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isMovementBlocked()
/*      */   {
/*  532 */     return (getHealth() <= 0.0F) || (isPlayerSleeping());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void closeScreen()
/*      */   {
/*  540 */     this.openContainer = this.inventoryContainer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateRidden()
/*      */   {
/*  548 */     if ((!this.worldObj.isRemote) && (isSneaking()))
/*      */     {
/*  550 */       mountEntity(null);
/*  551 */       setSneaking(false);
/*      */     }
/*      */     else
/*      */     {
/*  555 */       double d0 = this.posX;
/*  556 */       double d1 = this.posY;
/*  557 */       double d2 = this.posZ;
/*  558 */       float f = this.rotationYaw;
/*  559 */       float f1 = this.rotationPitch;
/*  560 */       super.updateRidden();
/*  561 */       this.prevCameraYaw = this.cameraYaw;
/*  562 */       this.cameraYaw = 0.0F;
/*  563 */       addMountedMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
/*      */       
/*  565 */       if ((this.ridingEntity instanceof EntityPig))
/*      */       {
/*  567 */         this.rotationPitch = f1;
/*  568 */         this.rotationYaw = f;
/*  569 */         this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void preparePlayerToSpawn()
/*      */   {
/*  580 */     setSize(0.6F, 1.8F);
/*  581 */     super.preparePlayerToSpawn();
/*  582 */     setHealth(getMaxHealth());
/*  583 */     this.deathTime = 0;
/*      */   }
/*      */   
/*      */   protected void updateEntityActionState()
/*      */   {
/*  588 */     super.updateEntityActionState();
/*  589 */     updateArmSwingProgress();
/*  590 */     this.rotationYawHead = this.rotationYaw;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onLivingUpdate()
/*      */   {
/*  599 */     if (this.flyToggleTimer > 0)
/*      */     {
/*  601 */       this.flyToggleTimer -= 1;
/*      */     }
/*      */     
/*  604 */     if ((this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) && (this.worldObj.getGameRules().getBoolean("naturalRegeneration")))
/*      */     {
/*  606 */       if ((getHealth() < getMaxHealth()) && (this.ticksExisted % 20 == 0))
/*      */       {
/*  608 */         heal(1.0F);
/*      */       }
/*      */       
/*  611 */       if ((this.foodStats.needFood()) && (this.ticksExisted % 10 == 0))
/*      */       {
/*  613 */         this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
/*      */       }
/*      */     }
/*      */     
/*  617 */     this.inventory.decrementAnimations();
/*  618 */     this.prevCameraYaw = this.cameraYaw;
/*  619 */     super.onLivingUpdate();
/*  620 */     IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/*      */     
/*  622 */     if (!this.worldObj.isRemote)
/*      */     {
/*  624 */       iattributeinstance.setBaseValue(this.capabilities.getWalkSpeed());
/*      */     }
/*      */     
/*  627 */     this.jumpMovementFactor = this.speedInAir;
/*      */     
/*  629 */     if (isSprinting())
/*      */     {
/*  631 */       this.jumpMovementFactor = ((float)(this.jumpMovementFactor + this.speedInAir * 0.3D));
/*      */     }
/*      */     
/*  634 */     setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
/*  635 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*  636 */     float f1 = (float)(Math.atan(-this.motionY * 0.20000000298023224D) * 15.0D);
/*      */     
/*  638 */     if (f > 0.1F)
/*      */     {
/*  640 */       f = 0.1F;
/*      */     }
/*      */     
/*  643 */     if ((!this.onGround) || (getHealth() <= 0.0F))
/*      */     {
/*  645 */       f = 0.0F;
/*      */     }
/*      */     
/*  648 */     if ((this.onGround) || (getHealth() <= 0.0F))
/*      */     {
/*  650 */       f1 = 0.0F;
/*      */     }
/*      */     
/*  653 */     this.cameraYaw += (f - this.cameraYaw) * 0.4F;
/*  654 */     this.cameraPitch += (f1 - this.cameraPitch) * 0.8F;
/*      */     
/*  656 */     if ((getHealth() > 0.0F) && (!isSpectator()))
/*      */     {
/*  658 */       AxisAlignedBB axisalignedbb = null;
/*      */       
/*  660 */       if ((this.ridingEntity != null) && (!this.ridingEntity.isDead))
/*      */       {
/*  662 */         axisalignedbb = getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0D, 0.0D, 1.0D);
/*      */       }
/*      */       else
/*      */       {
/*  666 */         axisalignedbb = getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
/*      */       }
/*      */       
/*  669 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);
/*      */       
/*  671 */       for (int i = 0; i < list.size(); i++)
/*      */       {
/*  673 */         Entity entity = (Entity)list.get(i);
/*      */         
/*  675 */         if (!entity.isDead)
/*      */         {
/*  677 */           collideWithPlayer(entity);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void collideWithPlayer(Entity p_71044_1_)
/*      */   {
/*  685 */     p_71044_1_.onCollideWithPlayer(this);
/*      */   }
/*      */   
/*      */   public int getScore()
/*      */   {
/*  690 */     return this.dataWatcher.getWatchableObjectInt(18);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScore(int p_85040_1_)
/*      */   {
/*  698 */     this.dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addScore(int p_85039_1_)
/*      */   {
/*  706 */     int i = getScore();
/*  707 */     this.dataWatcher.updateObject(18, Integer.valueOf(i + p_85039_1_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDeath(DamageSource cause)
/*      */   {
/*  715 */     super.onDeath(cause);
/*  716 */     setSize(0.2F, 0.2F);
/*  717 */     setPosition(this.posX, this.posY, this.posZ);
/*  718 */     this.motionY = 0.10000000149011612D;
/*      */     
/*  720 */     if (getName().equals("Notch"))
/*      */     {
/*  722 */       dropItem(new ItemStack(Items.apple, 1), true, false);
/*      */     }
/*      */     
/*  725 */     if (!this.worldObj.getGameRules().getBoolean("keepInventory"))
/*      */     {
/*  727 */       this.inventory.dropAllItems();
/*      */     }
/*      */     
/*  730 */     if (cause != null)
/*      */     {
/*  732 */       this.motionX = (-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
/*  733 */       this.motionZ = (-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
/*      */     }
/*      */     else
/*      */     {
/*  737 */       this.motionX = (this.motionZ = 0.0D);
/*      */     }
/*      */     
/*  740 */     triggerAchievement(StatList.deathsStat);
/*  741 */     func_175145_a(StatList.timeSinceDeathStat);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getHurtSound()
/*      */   {
/*  749 */     return "game.player.hurt";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDeathSound()
/*      */   {
/*  757 */     return "game.player.die";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addToPlayerScore(Entity entityIn, int amount)
/*      */   {
/*  766 */     addScore(amount);
/*  767 */     Collection<ScoreObjective> collection = getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.totalKillCount);
/*      */     
/*  769 */     if ((entityIn instanceof EntityPlayer))
/*      */     {
/*  771 */       triggerAchievement(StatList.playerKillsStat);
/*  772 */       collection.addAll(getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.playerKillCount));
/*  773 */       collection.addAll(func_175137_e(entityIn));
/*      */     }
/*      */     else
/*      */     {
/*  777 */       triggerAchievement(StatList.mobKillsStat);
/*      */     }
/*      */     
/*  780 */     for (ScoreObjective scoreobjective : collection)
/*      */     {
/*  782 */       Score score = getWorldScoreboard().getValueFromObjective(getName(), scoreobjective);
/*  783 */       score.func_96648_a();
/*      */     }
/*      */   }
/*      */   
/*      */   private Collection<ScoreObjective> func_175137_e(Entity p_175137_1_)
/*      */   {
/*  789 */     ScorePlayerTeam scoreplayerteam = getWorldScoreboard().getPlayersTeam(getName());
/*      */     
/*  791 */     if (scoreplayerteam != null)
/*      */     {
/*  793 */       int i = scoreplayerteam.getChatFormat().getColorIndex();
/*      */       
/*  795 */       if ((i >= 0) && (i < IScoreObjectiveCriteria.field_178793_i.length))
/*      */       {
/*  797 */         for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178793_i[i]))
/*      */         {
/*  799 */           Score score = getWorldScoreboard().getValueFromObjective(p_175137_1_.getName(), scoreobjective);
/*  800 */           score.func_96648_a();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  805 */     ScorePlayerTeam scoreplayerteam1 = getWorldScoreboard().getPlayersTeam(p_175137_1_.getName());
/*      */     
/*  807 */     if (scoreplayerteam1 != null)
/*      */     {
/*  809 */       int j = scoreplayerteam1.getChatFormat().getColorIndex();
/*      */       
/*  811 */       if ((j >= 0) && (j < IScoreObjectiveCriteria.field_178792_h.length))
/*      */       {
/*  813 */         return getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178792_h[j]);
/*      */       }
/*      */     }
/*      */     
/*  817 */     return Lists.newArrayList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityItem dropOneItem(boolean dropAll)
/*      */   {
/*  825 */     return dropItem(this.inventory.decrStackSize(this.inventory.currentItem, (dropAll) && (this.inventory.getCurrentItem() != null) ? this.inventory.getCurrentItem().stackSize : 1), false, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStackIn, boolean unused)
/*      */   {
/*  833 */     return dropItem(itemStackIn, false, false);
/*      */   }
/*      */   
/*      */   public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem)
/*      */   {
/*  838 */     if (droppedItem == null)
/*      */     {
/*  840 */       return null;
/*      */     }
/*  842 */     if (droppedItem.stackSize == 0)
/*      */     {
/*  844 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  848 */     double d0 = this.posY - 0.30000001192092896D + getEyeHeight();
/*  849 */     EntityItem entityitem = new EntityItem(this.worldObj, this.posX, d0, this.posZ, droppedItem);
/*  850 */     entityitem.setPickupDelay(40);
/*      */     
/*  852 */     if (traceItem)
/*      */     {
/*  854 */       entityitem.setThrower(getName());
/*      */     }
/*      */     
/*  857 */     if (dropAround)
/*      */     {
/*  859 */       float f = this.rand.nextFloat() * 0.5F;
/*  860 */       float f1 = this.rand.nextFloat() * 3.1415927F * 2.0F;
/*  861 */       entityitem.motionX = (-MathHelper.sin(f1) * f);
/*  862 */       entityitem.motionZ = (MathHelper.cos(f1) * f);
/*  863 */       entityitem.motionY = 0.20000000298023224D;
/*      */     }
/*      */     else
/*      */     {
/*  867 */       float f2 = 0.3F;
/*  868 */       entityitem.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f2);
/*  869 */       entityitem.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f2);
/*  870 */       entityitem.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f2 + 0.1F);
/*  871 */       float f3 = this.rand.nextFloat() * 3.1415927F * 2.0F;
/*  872 */       f2 = 0.02F * this.rand.nextFloat();
/*  873 */       entityitem.motionX += Math.cos(f3) * f2;
/*  874 */       entityitem.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
/*  875 */       entityitem.motionZ += Math.sin(f3) * f2;
/*      */     }
/*      */     
/*  878 */     joinEntityItemWithWorld(entityitem);
/*      */     
/*  880 */     if (traceItem)
/*      */     {
/*  882 */       triggerAchievement(StatList.dropStat);
/*      */     }
/*      */     
/*  885 */     return entityitem;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void joinEntityItemWithWorld(EntityItem itemIn)
/*      */   {
/*  894 */     this.worldObj.spawnEntityInWorld(itemIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getToolDigEfficiency(Block p_180471_1_)
/*      */   {
/*  902 */     float f = this.inventory.getStrVsBlock(p_180471_1_);
/*      */     
/*  904 */     if (f > 1.0F)
/*      */     {
/*  906 */       int i = EnchantmentHelper.getEfficiencyModifier(this);
/*  907 */       ItemStack itemstack = this.inventory.getCurrentItem();
/*      */       
/*  909 */       if ((i > 0) && (itemstack != null))
/*      */       {
/*  911 */         f += i * i + 1;
/*      */       }
/*      */     }
/*      */     
/*  915 */     if (isPotionActive(Potion.digSpeed))
/*      */     {
/*  917 */       f *= (1.0F + (getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F);
/*      */     }
/*      */     
/*  920 */     if (isPotionActive(Potion.digSlowdown))
/*      */     {
/*  922 */       float f1 = 1.0F;
/*      */       
/*  924 */       switch (getActivePotionEffect(Potion.digSlowdown).getAmplifier())
/*      */       {
/*      */       case 0: 
/*  927 */         f1 = 0.3F;
/*  928 */         break;
/*      */       
/*      */       case 1: 
/*  931 */         f1 = 0.09F;
/*  932 */         break;
/*      */       
/*      */       case 2: 
/*  935 */         f1 = 0.0027F;
/*  936 */         break;
/*      */       
/*      */       case 3: 
/*      */       default: 
/*  940 */         f1 = 8.1E-4F;
/*      */       }
/*      */       
/*  943 */       f *= f1;
/*      */     }
/*      */     
/*  946 */     if ((isInsideOfMaterial(Material.water)) && (!EnchantmentHelper.getAquaAffinityModifier(this)))
/*      */     {
/*  948 */       f /= 5.0F;
/*      */     }
/*      */     
/*  951 */     if (!this.onGround)
/*      */     {
/*  953 */       f /= 5.0F;
/*      */     }
/*      */     
/*  956 */     return f;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canHarvestBlock(Block blockToHarvest)
/*      */   {
/*  964 */     return this.inventory.canHeldItemHarvest(blockToHarvest);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*      */   {
/*  972 */     super.readEntityFromNBT(tagCompund);
/*  973 */     this.entityUniqueID = getUUID(this.gameProfile);
/*  974 */     NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
/*  975 */     this.inventory.readFromNBT(nbttaglist);
/*  976 */     this.inventory.currentItem = tagCompund.getInteger("SelectedItemSlot");
/*  977 */     this.sleeping = tagCompund.getBoolean("Sleeping");
/*  978 */     this.sleepTimer = tagCompund.getShort("SleepTimer");
/*  979 */     this.experience = tagCompund.getFloat("XpP");
/*  980 */     this.experienceLevel = tagCompund.getInteger("XpLevel");
/*  981 */     this.experienceTotal = tagCompund.getInteger("XpTotal");
/*  982 */     this.xpSeed = tagCompund.getInteger("XpSeed");
/*      */     
/*  984 */     if (this.xpSeed == 0)
/*      */     {
/*  986 */       this.xpSeed = this.rand.nextInt();
/*      */     }
/*      */     
/*  989 */     setScore(tagCompund.getInteger("Score"));
/*      */     
/*  991 */     if (this.sleeping)
/*      */     {
/*  993 */       this.playerLocation = new BlockPos(this);
/*  994 */       wakeUpPlayer(true, true, false);
/*      */     }
/*      */     
/*  997 */     if ((tagCompund.hasKey("SpawnX", 99)) && (tagCompund.hasKey("SpawnY", 99)) && (tagCompund.hasKey("SpawnZ", 99)))
/*      */     {
/*  999 */       this.spawnChunk = new BlockPos(tagCompund.getInteger("SpawnX"), tagCompund.getInteger("SpawnY"), tagCompund.getInteger("SpawnZ"));
/* 1000 */       this.spawnForced = tagCompund.getBoolean("SpawnForced");
/*      */     }
/*      */     
/* 1003 */     this.foodStats.readNBT(tagCompund);
/* 1004 */     this.capabilities.readCapabilitiesFromNBT(tagCompund);
/*      */     
/* 1006 */     if (tagCompund.hasKey("EnderItems", 9))
/*      */     {
/* 1008 */       NBTTagList nbttaglist1 = tagCompund.getTagList("EnderItems", 10);
/* 1009 */       this.theInventoryEnderChest.loadInventoryFromNBT(nbttaglist1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*      */   {
/* 1018 */     super.writeEntityToNBT(tagCompound);
/* 1019 */     tagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
/* 1020 */     tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
/* 1021 */     tagCompound.setBoolean("Sleeping", this.sleeping);
/* 1022 */     tagCompound.setShort("SleepTimer", (short)this.sleepTimer);
/* 1023 */     tagCompound.setFloat("XpP", this.experience);
/* 1024 */     tagCompound.setInteger("XpLevel", this.experienceLevel);
/* 1025 */     tagCompound.setInteger("XpTotal", this.experienceTotal);
/* 1026 */     tagCompound.setInteger("XpSeed", this.xpSeed);
/* 1027 */     tagCompound.setInteger("Score", getScore());
/*      */     
/* 1029 */     if (this.spawnChunk != null)
/*      */     {
/* 1031 */       tagCompound.setInteger("SpawnX", this.spawnChunk.getX());
/* 1032 */       tagCompound.setInteger("SpawnY", this.spawnChunk.getY());
/* 1033 */       tagCompound.setInteger("SpawnZ", this.spawnChunk.getZ());
/* 1034 */       tagCompound.setBoolean("SpawnForced", this.spawnForced);
/*      */     }
/*      */     
/* 1037 */     this.foodStats.writeNBT(tagCompound);
/* 1038 */     this.capabilities.writeCapabilitiesToNBT(tagCompound);
/* 1039 */     tagCompound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
/* 1040 */     ItemStack itemstack = this.inventory.getCurrentItem();
/*      */     
/* 1042 */     if ((itemstack != null) && (itemstack.getItem() != null))
/*      */     {
/* 1044 */       tagCompound.setTag("SelectedItem", itemstack.writeToNBT(new NBTTagCompound()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean attackEntityFrom(DamageSource source, float amount)
/*      */   {
/* 1053 */     if (isEntityInvulnerable(source))
/*      */     {
/* 1055 */       return false;
/*      */     }
/* 1057 */     if ((this.capabilities.disableDamage) && (!source.canHarmInCreative()))
/*      */     {
/* 1059 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1063 */     this.entityAge = 0;
/*      */     
/* 1065 */     if (getHealth() <= 0.0F)
/*      */     {
/* 1067 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1071 */     if ((isPlayerSleeping()) && (!this.worldObj.isRemote))
/*      */     {
/* 1073 */       wakeUpPlayer(true, true, false);
/*      */     }
/*      */     
/* 1076 */     if (source.isDifficultyScaled())
/*      */     {
/* 1078 */       if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
/*      */       {
/* 1080 */         amount = 0.0F;
/*      */       }
/*      */       
/* 1083 */       if (this.worldObj.getDifficulty() == EnumDifficulty.EASY)
/*      */       {
/* 1085 */         amount = amount / 2.0F + 1.0F;
/*      */       }
/*      */       
/* 1088 */       if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
/*      */       {
/* 1090 */         amount = amount * 3.0F / 2.0F;
/*      */       }
/*      */     }
/*      */     
/* 1094 */     if (amount == 0.0F)
/*      */     {
/* 1096 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1100 */     Entity entity = source.getEntity();
/*      */     
/* 1102 */     if (((entity instanceof EntityArrow)) && (((EntityArrow)entity).shootingEntity != null))
/*      */     {
/* 1104 */       entity = ((EntityArrow)entity).shootingEntity;
/*      */     }
/*      */     
/* 1107 */     return super.attackEntityFrom(source, amount);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canAttackPlayer(EntityPlayer other)
/*      */   {
/* 1115 */     Team team = getTeam();
/* 1116 */     Team team1 = other.getTeam();
/* 1117 */     return !team.isSameTeam(team1) ? true : team == null ? true : team.getAllowFriendlyFire();
/*      */   }
/*      */   
/*      */   protected void damageArmor(float p_70675_1_)
/*      */   {
/* 1122 */     this.inventory.damageArmor(p_70675_1_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTotalArmorValue()
/*      */   {
/* 1130 */     return this.inventory.getTotalArmorValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getArmorVisibility()
/*      */   {
/* 1139 */     int i = 0;
/*      */     ItemStack[] arrayOfItemStack;
/* 1141 */     int j = (arrayOfItemStack = this.inventory.armorInventory).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*      */       
/* 1143 */       if (itemstack != null)
/*      */       {
/* 1145 */         i++;
/*      */       }
/*      */     }
/*      */     
/* 1149 */     return i / this.inventory.armorInventory.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void damageEntity(DamageSource damageSrc, float damageAmount)
/*      */   {
/* 1158 */     if (!isEntityInvulnerable(damageSrc))
/*      */     {
/* 1160 */       if ((!damageSrc.isUnblockable()) && (isBlocking()) && (damageAmount > 0.0F))
/*      */       {
/* 1162 */         damageAmount = (1.0F + damageAmount) * 0.5F;
/*      */       }
/*      */       
/* 1165 */       damageAmount = applyArmorCalculations(damageSrc, damageAmount);
/* 1166 */       damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
/* 1167 */       float f = damageAmount;
/* 1168 */       damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
/* 1169 */       setAbsorptionAmount(getAbsorptionAmount() - (f - damageAmount));
/*      */       
/* 1171 */       if (damageAmount != 0.0F)
/*      */       {
/* 1173 */         addExhaustion(damageSrc.getHungerDamage());
/* 1174 */         float f1 = getHealth();
/* 1175 */         setHealth(getHealth() - damageAmount);
/* 1176 */         getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
/*      */         
/* 1178 */         if (damageAmount < 3.4028235E37F)
/*      */         {
/* 1180 */           addStat(StatList.damageTakenStat, Math.round(damageAmount * 10.0F));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void openEditSign(TileEntitySign signTile) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayVillagerTradeGui(IMerchant villager) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayGUIChest(IInventory chestInventory) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void displayGui(IInteractionObject guiOwner) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void displayGUIBook(ItemStack bookStack) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean interactWith(Entity p_70998_1_)
/*      */   {
/* 1222 */     if (isSpectator())
/*      */     {
/* 1224 */       if ((p_70998_1_ instanceof IInventory))
/*      */       {
/* 1226 */         displayGUIChest((IInventory)p_70998_1_);
/*      */       }
/*      */       
/* 1229 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1233 */     ItemStack itemstack = getCurrentEquippedItem();
/* 1234 */     ItemStack itemstack1 = itemstack != null ? itemstack.copy() : null;
/*      */     
/* 1236 */     if (!p_70998_1_.interactFirst(this))
/*      */     {
/* 1238 */       if ((itemstack != null) && ((p_70998_1_ instanceof EntityLivingBase)))
/*      */       {
/* 1240 */         if (this.capabilities.isCreativeMode)
/*      */         {
/* 1242 */           itemstack = itemstack1;
/*      */         }
/*      */         
/* 1245 */         if (itemstack.interactWithEntity(this, (EntityLivingBase)p_70998_1_))
/*      */         {
/* 1247 */           if ((itemstack.stackSize <= 0) && (!this.capabilities.isCreativeMode))
/*      */           {
/* 1249 */             destroyCurrentEquippedItem();
/*      */           }
/*      */           
/* 1252 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 1256 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1260 */     if ((itemstack != null) && (itemstack == getCurrentEquippedItem()))
/*      */     {
/* 1262 */       if ((itemstack.stackSize <= 0) && (!this.capabilities.isCreativeMode))
/*      */       {
/* 1264 */         destroyCurrentEquippedItem();
/*      */       }
/* 1266 */       else if ((itemstack.stackSize < itemstack1.stackSize) && (this.capabilities.isCreativeMode))
/*      */       {
/* 1268 */         itemstack.stackSize = itemstack1.stackSize;
/*      */       }
/*      */     }
/*      */     
/* 1272 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack getCurrentEquippedItem()
/*      */   {
/* 1282 */     return this.inventory.getCurrentItem();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroyCurrentEquippedItem()
/*      */   {
/* 1290 */     this.inventory.setInventorySlotContents(this.inventory.currentItem, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getYOffset()
/*      */   {
/* 1298 */     return -0.35D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void attackTargetEntityWithCurrentItem(Entity targetEntity)
/*      */   {
/* 1307 */     if (targetEntity.canAttackWithItem())
/*      */     {
/* 1309 */       if (!targetEntity.hitByEntity(this))
/*      */       {
/* 1311 */         float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
/* 1312 */         int i = 0;
/* 1313 */         float f1 = 0.0F;
/*      */         
/* 1315 */         if ((targetEntity instanceof EntityLivingBase))
/*      */         {
/* 1317 */           f1 = EnchantmentHelper.func_152377_a(getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
/*      */         }
/*      */         else
/*      */         {
/* 1321 */           f1 = EnchantmentHelper.func_152377_a(getHeldItem(), net.minecraft.entity.EnumCreatureAttribute.UNDEFINED);
/*      */         }
/*      */         
/* 1324 */         i += EnchantmentHelper.getKnockbackModifier(this);
/*      */         
/* 1326 */         if (isSprinting())
/*      */         {
/* 1328 */           i++;
/*      */         }
/*      */         
/* 1331 */         if ((f > 0.0F) || (f1 > 0.0F))
/*      */         {
/* 1333 */           boolean flag = (this.fallDistance > 0.0F) && (!this.onGround) && (!isOnLadder()) && (!isInWater()) && (!isPotionActive(Potion.blindness)) && (this.ridingEntity == null) && ((targetEntity instanceof EntityLivingBase));
/*      */           
/* 1335 */           if ((flag) && (f > 0.0F))
/*      */           {
/* 1337 */             f *= 1.5F;
/*      */           }
/*      */           
/* 1340 */           f += f1;
/* 1341 */           boolean flag1 = false;
/* 1342 */           int j = EnchantmentHelper.getFireAspectModifier(this);
/*      */           
/* 1344 */           if (((targetEntity instanceof EntityLivingBase)) && (j > 0) && (!targetEntity.isBurning()))
/*      */           {
/* 1346 */             flag1 = true;
/* 1347 */             targetEntity.setFire(1);
/*      */           }
/*      */           
/* 1350 */           double d0 = targetEntity.motionX;
/* 1351 */           double d1 = targetEntity.motionY;
/* 1352 */           double d2 = targetEntity.motionZ;
/* 1353 */           boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
/*      */           
/* 1355 */           if (flag2)
/*      */           {
/* 1357 */             if (i > 0)
/*      */             {
/* 1359 */               targetEntity.addVelocity(-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F);
/* 1360 */               this.motionX *= 0.6D;
/* 1361 */               this.motionZ *= 0.6D;
/* 1362 */               setSprinting(false);
/*      */             }
/*      */             
/* 1365 */             if (((targetEntity instanceof EntityPlayerMP)) && (targetEntity.velocityChanged))
/*      */             {
/* 1367 */               ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S12PacketEntityVelocity(targetEntity));
/* 1368 */               targetEntity.velocityChanged = false;
/* 1369 */               targetEntity.motionX = d0;
/* 1370 */               targetEntity.motionY = d1;
/* 1371 */               targetEntity.motionZ = d2;
/*      */             }
/*      */             
/* 1374 */             if (flag)
/*      */             {
/* 1376 */               onCriticalHit(targetEntity);
/*      */             }
/*      */             
/* 1379 */             if (f1 > 0.0F)
/*      */             {
/* 1381 */               onEnchantmentCritical(targetEntity);
/*      */             }
/*      */             
/* 1384 */             if (f >= 18.0F)
/*      */             {
/* 1386 */               triggerAchievement(AchievementList.overkill);
/*      */             }
/*      */             
/* 1389 */             setLastAttacker(targetEntity);
/*      */             
/* 1391 */             if ((targetEntity instanceof EntityLivingBase))
/*      */             {
/* 1393 */               EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, this);
/*      */             }
/*      */             
/* 1396 */             EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
/* 1397 */             ItemStack itemstack = getCurrentEquippedItem();
/* 1398 */             Entity entity = targetEntity;
/*      */             
/* 1400 */             if ((targetEntity instanceof EntityDragonPart))
/*      */             {
/* 1402 */               IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).entityDragonObj;
/*      */               
/* 1404 */               if ((ientitymultipart instanceof EntityLivingBase))
/*      */               {
/* 1406 */                 entity = (EntityLivingBase)ientitymultipart;
/*      */               }
/*      */             }
/*      */             
/* 1410 */             if ((itemstack != null) && ((entity instanceof EntityLivingBase)))
/*      */             {
/* 1412 */               itemstack.hitEntity((EntityLivingBase)entity, this);
/*      */               
/* 1414 */               if (itemstack.stackSize <= 0)
/*      */               {
/* 1416 */                 destroyCurrentEquippedItem();
/*      */               }
/*      */             }
/*      */             
/* 1420 */             if ((targetEntity instanceof EntityLivingBase))
/*      */             {
/* 1422 */               addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
/*      */               
/* 1424 */               if (j > 0)
/*      */               {
/* 1426 */                 targetEntity.setFire(j * 4);
/*      */               }
/*      */             }
/*      */             
/* 1430 */             addExhaustion(0.3F);
/*      */           }
/* 1432 */           else if (flag1)
/*      */           {
/* 1434 */             targetEntity.extinguish();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onCriticalHit(Entity entityHit) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEnchantmentCritical(Entity entityHit) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void respawnPlayer() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDead()
/*      */   {
/* 1461 */     super.setDead();
/* 1462 */     this.inventoryContainer.onContainerClosed(this);
/*      */     
/* 1464 */     if (this.openContainer != null)
/*      */     {
/* 1466 */       this.openContainer.onContainerClosed(this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEntityInsideOpaqueBlock()
/*      */   {
/* 1475 */     return (!this.sleeping) && (super.isEntityInsideOpaqueBlock());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUser()
/*      */   {
/* 1483 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public GameProfile getGameProfile()
/*      */   {
/* 1491 */     return this.gameProfile;
/*      */   }
/*      */   
/*      */   public EnumStatus trySleep(BlockPos bedLocation)
/*      */   {
/* 1496 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1498 */       if ((isPlayerSleeping()) || (!isEntityAlive()))
/*      */       {
/* 1500 */         return EnumStatus.OTHER_PROBLEM;
/*      */       }
/*      */       
/* 1503 */       if (!this.worldObj.provider.isSurfaceWorld())
/*      */       {
/* 1505 */         return EnumStatus.NOT_POSSIBLE_HERE;
/*      */       }
/*      */       
/* 1508 */       if (this.worldObj.isDaytime())
/*      */       {
/* 1510 */         return EnumStatus.NOT_POSSIBLE_NOW;
/*      */       }
/*      */       
/* 1513 */       if ((Math.abs(this.posX - bedLocation.getX()) > 3.0D) || (Math.abs(this.posY - bedLocation.getY()) > 2.0D) || (Math.abs(this.posZ - bedLocation.getZ()) > 3.0D))
/*      */       {
/* 1515 */         return EnumStatus.TOO_FAR_AWAY;
/*      */       }
/*      */       
/* 1518 */       double d0 = 8.0D;
/* 1519 */       double d1 = 5.0D;
/* 1520 */       List<EntityMob> list = this.worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(bedLocation.getX() - d0, bedLocation.getY() - d1, bedLocation.getZ() - d0, bedLocation.getX() + d0, bedLocation.getY() + d1, bedLocation.getZ() + d0));
/*      */       
/* 1522 */       if (!list.isEmpty())
/*      */       {
/* 1524 */         return EnumStatus.NOT_SAFE;
/*      */       }
/*      */     }
/*      */     
/* 1528 */     if (isRiding())
/*      */     {
/* 1530 */       mountEntity(null);
/*      */     }
/*      */     
/* 1533 */     setSize(0.2F, 0.2F);
/*      */     
/* 1535 */     if (this.worldObj.isBlockLoaded(bedLocation))
/*      */     {
/* 1537 */       EnumFacing enumfacing = (EnumFacing)this.worldObj.getBlockState(bedLocation).getValue(BlockDirectional.FACING);
/* 1538 */       float f = 0.5F;
/* 1539 */       float f1 = 0.5F;
/*      */       
/* 1541 */       switch (enumfacing)
/*      */       {
/*      */       case SOUTH: 
/* 1544 */         f1 = 0.9F;
/* 1545 */         break;
/*      */       
/*      */       case NORTH: 
/* 1548 */         f1 = 0.1F;
/* 1549 */         break;
/*      */       
/*      */       case UP: 
/* 1552 */         f = 0.1F;
/* 1553 */         break;
/*      */       
/*      */       case WEST: 
/* 1556 */         f = 0.9F;
/*      */       }
/*      */       
/* 1559 */       func_175139_a(enumfacing);
/* 1560 */       setPosition(bedLocation.getX() + f, bedLocation.getY() + 0.6875F, bedLocation.getZ() + f1);
/*      */     }
/*      */     else
/*      */     {
/* 1564 */       setPosition(bedLocation.getX() + 0.5F, bedLocation.getY() + 0.6875F, bedLocation.getZ() + 0.5F);
/*      */     }
/*      */     
/* 1567 */     this.sleeping = true;
/* 1568 */     this.sleepTimer = 0;
/* 1569 */     this.playerLocation = bedLocation;
/* 1570 */     this.motionX = (this.motionZ = this.motionY = 0.0D);
/*      */     
/* 1572 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1574 */       this.worldObj.updateAllPlayersSleepingFlag();
/*      */     }
/*      */     
/* 1577 */     return EnumStatus.OK;
/*      */   }
/*      */   
/*      */   private void func_175139_a(EnumFacing p_175139_1_)
/*      */   {
/* 1582 */     this.renderOffsetX = 0.0F;
/* 1583 */     this.renderOffsetZ = 0.0F;
/*      */     
/* 1585 */     switch (p_175139_1_)
/*      */     {
/*      */     case SOUTH: 
/* 1588 */       this.renderOffsetZ = -1.8F;
/* 1589 */       break;
/*      */     
/*      */     case NORTH: 
/* 1592 */       this.renderOffsetZ = 1.8F;
/* 1593 */       break;
/*      */     
/*      */     case UP: 
/* 1596 */       this.renderOffsetX = 1.8F;
/* 1597 */       break;
/*      */     
/*      */     case WEST: 
/* 1600 */       this.renderOffsetX = -1.8F;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn)
/*      */   {
/* 1609 */     setSize(0.6F, 1.8F);
/* 1610 */     IBlockState iblockstate = this.worldObj.getBlockState(this.playerLocation);
/*      */     
/* 1612 */     if ((this.playerLocation != null) && (iblockstate.getBlock() == Blocks.bed))
/*      */     {
/* 1614 */       this.worldObj.setBlockState(this.playerLocation, iblockstate.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
/* 1615 */       BlockPos blockpos = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);
/*      */       
/* 1617 */       if (blockpos == null)
/*      */       {
/* 1619 */         blockpos = this.playerLocation.up();
/*      */       }
/*      */       
/* 1622 */       setPosition(blockpos.getX() + 0.5F, blockpos.getY() + 0.1F, blockpos.getZ() + 0.5F);
/*      */     }
/*      */     
/* 1625 */     this.sleeping = false;
/*      */     
/* 1627 */     if ((!this.worldObj.isRemote) && (updateWorldFlag))
/*      */     {
/* 1629 */       this.worldObj.updateAllPlayersSleepingFlag();
/*      */     }
/*      */     
/* 1632 */     this.sleepTimer = (p_70999_1_ ? 0 : 100);
/*      */     
/* 1634 */     if (setSpawn)
/*      */     {
/* 1636 */       setSpawnPoint(this.playerLocation, false);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isInBed()
/*      */   {
/* 1642 */     return this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static BlockPos getBedSpawnLocation(World worldIn, BlockPos bedLocation, boolean forceSpawn)
/*      */   {
/* 1650 */     Block block = worldIn.getBlockState(bedLocation).getBlock();
/*      */     
/* 1652 */     if (block != Blocks.bed)
/*      */     {
/* 1654 */       if (!forceSpawn)
/*      */       {
/* 1656 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 1660 */       boolean flag = block.func_181623_g();
/* 1661 */       boolean flag1 = worldIn.getBlockState(bedLocation.up()).getBlock().func_181623_g();
/* 1662 */       return (flag) && (flag1) ? bedLocation : null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1667 */     return BlockBed.getSafeExitLocation(worldIn, bedLocation, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getBedOrientationInDegrees()
/*      */   {
/* 1676 */     if (this.playerLocation != null)
/*      */     {
/* 1678 */       EnumFacing enumfacing = (EnumFacing)this.worldObj.getBlockState(this.playerLocation).getValue(BlockDirectional.FACING);
/*      */       
/* 1680 */       switch (enumfacing)
/*      */       {
/*      */       case SOUTH: 
/* 1683 */         return 90.0F;
/*      */       
/*      */       case NORTH: 
/* 1686 */         return 270.0F;
/*      */       
/*      */       case UP: 
/* 1689 */         return 0.0F;
/*      */       
/*      */       case WEST: 
/* 1692 */         return 180.0F;
/*      */       }
/*      */       
/*      */     }
/* 1696 */     return 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPlayerSleeping()
/*      */   {
/* 1704 */     return this.sleeping;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPlayerFullyAsleep()
/*      */   {
/* 1712 */     return (this.sleeping) && (this.sleepTimer >= 100);
/*      */   }
/*      */   
/*      */   public int getSleepTimer()
/*      */   {
/* 1717 */     return this.sleepTimer;
/*      */   }
/*      */   
/*      */ 
/*      */   public void addChatComponentMessage(IChatComponent chatComponent) {}
/*      */   
/*      */ 
/*      */   public BlockPos getBedLocation()
/*      */   {
/* 1726 */     return this.spawnChunk;
/*      */   }
/*      */   
/*      */   public boolean isSpawnForced()
/*      */   {
/* 1731 */     return this.spawnForced;
/*      */   }
/*      */   
/*      */   public void setSpawnPoint(BlockPos pos, boolean forced)
/*      */   {
/* 1736 */     if (pos != null)
/*      */     {
/* 1738 */       this.spawnChunk = pos;
/* 1739 */       this.spawnForced = forced;
/*      */     }
/*      */     else
/*      */     {
/* 1743 */       this.spawnChunk = null;
/* 1744 */       this.spawnForced = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void triggerAchievement(StatBase achievementIn)
/*      */   {
/* 1753 */     addStat(achievementIn, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addStat(StatBase stat, int amount) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void func_175145_a(StatBase p_175145_1_) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void jump()
/*      */   {
/* 1772 */     super.jump();
/* 1773 */     triggerAchievement(StatList.jumpStat);
/*      */     
/* 1775 */     if (isSprinting())
/*      */     {
/* 1777 */       addExhaustion(0.8F);
/*      */     }
/*      */     else
/*      */     {
/* 1781 */       addExhaustion(0.2F);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void moveEntityWithHeading(float strafe, float forward)
/*      */   {
/* 1790 */     double d0 = this.posX;
/* 1791 */     double d1 = this.posY;
/* 1792 */     double d2 = this.posZ;
/*      */     
/* 1794 */     if ((this.capabilities.isFlying) && (this.ridingEntity == null))
/*      */     {
/* 1796 */       double d3 = this.motionY;
/* 1797 */       float f = this.jumpMovementFactor;
/* 1798 */       this.jumpMovementFactor = (this.capabilities.getFlySpeed() * (isSprinting() ? 2 : 1));
/* 1799 */       super.moveEntityWithHeading(strafe, forward);
/* 1800 */       this.motionY = (d3 * 0.6D);
/* 1801 */       this.jumpMovementFactor = f;
/*      */     }
/*      */     else
/*      */     {
/* 1805 */       super.moveEntityWithHeading(strafe, forward);
/*      */     }
/*      */     
/* 1808 */     addMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getAIMoveSpeed()
/*      */   {
/* 1816 */     return (float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_)
/*      */   {
/* 1824 */     if (this.ridingEntity == null)
/*      */     {
/* 1826 */       if (isInsideOfMaterial(Material.water))
/*      */       {
/* 1828 */         int i = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1830 */         if (i > 0)
/*      */         {
/* 1832 */           addStat(StatList.distanceDoveStat, i);
/* 1833 */           addExhaustion(0.015F * i * 0.01F);
/*      */         }
/*      */       }
/* 1836 */       else if (isInWater())
/*      */       {
/* 1838 */         int j = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1840 */         if (j > 0)
/*      */         {
/* 1842 */           addStat(StatList.distanceSwumStat, j);
/* 1843 */           addExhaustion(0.015F * j * 0.01F);
/*      */         }
/*      */       }
/* 1846 */       else if (isOnLadder())
/*      */       {
/* 1848 */         if (p_71000_3_ > 0.0D)
/*      */         {
/* 1850 */           addStat(StatList.distanceClimbedStat, (int)Math.round(p_71000_3_ * 100.0D));
/*      */         }
/*      */       }
/* 1853 */       else if (this.onGround)
/*      */       {
/* 1855 */         int k = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1857 */         if (k > 0)
/*      */         {
/* 1859 */           addStat(StatList.distanceWalkedStat, k);
/*      */           
/* 1861 */           if (isSprinting())
/*      */           {
/* 1863 */             addStat(StatList.distanceSprintedStat, k);
/* 1864 */             addExhaustion(0.099999994F * k * 0.01F);
/*      */           }
/*      */           else
/*      */           {
/* 1868 */             if (isSneaking())
/*      */             {
/* 1870 */               addStat(StatList.distanceCrouchedStat, k);
/*      */             }
/*      */             
/* 1873 */             addExhaustion(0.01F * k * 0.01F);
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1879 */         int l = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1881 */         if (l > 25)
/*      */         {
/* 1883 */           addStat(StatList.distanceFlownStat, l);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_)
/*      */   {
/* 1894 */     if (this.ridingEntity != null)
/*      */     {
/* 1896 */       int i = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0F);
/*      */       
/* 1898 */       if (i > 0)
/*      */       {
/* 1900 */         if ((this.ridingEntity instanceof EntityMinecart))
/*      */         {
/* 1902 */           addStat(StatList.distanceByMinecartStat, i);
/*      */           
/* 1904 */           if (this.startMinecartRidingCoordinate == null)
/*      */           {
/* 1906 */             this.startMinecartRidingCoordinate = new BlockPos(this);
/*      */           }
/* 1908 */           else if (this.startMinecartRidingCoordinate.distanceSq(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0D)
/*      */           {
/* 1910 */             triggerAchievement(AchievementList.onARail);
/*      */           }
/*      */         }
/* 1913 */         else if ((this.ridingEntity instanceof net.minecraft.entity.item.EntityBoat))
/*      */         {
/* 1915 */           addStat(StatList.distanceByBoatStat, i);
/*      */         }
/* 1917 */         else if ((this.ridingEntity instanceof EntityPig))
/*      */         {
/* 1919 */           addStat(StatList.distanceByPigStat, i);
/*      */         }
/* 1921 */         else if ((this.ridingEntity instanceof EntityHorse))
/*      */         {
/* 1923 */           addStat(StatList.distanceByHorseStat, i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void fall(float distance, float damageMultiplier)
/*      */   {
/* 1931 */     if (!this.capabilities.allowFlying)
/*      */     {
/* 1933 */       if (distance >= 2.0F)
/*      */       {
/* 1935 */         addStat(StatList.distanceFallenStat, (int)Math.round(distance * 100.0D));
/*      */       }
/*      */       
/* 1938 */       super.fall(distance, damageMultiplier);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void resetHeight()
/*      */   {
/* 1947 */     if (!isSpectator())
/*      */     {
/* 1949 */       super.resetHeight();
/*      */     }
/*      */   }
/*      */   
/*      */   protected String getFallSoundString(int damageValue)
/*      */   {
/* 1955 */     return damageValue > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onKillEntity(EntityLivingBase entityLivingIn)
/*      */   {
/* 1963 */     if ((entityLivingIn instanceof net.minecraft.entity.monster.IMob))
/*      */     {
/* 1965 */       triggerAchievement(AchievementList.killEnemy);
/*      */     }
/*      */     
/* 1968 */     EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID(entityLivingIn)));
/*      */     
/* 1970 */     if (entitylist$entityegginfo != null)
/*      */     {
/* 1972 */       triggerAchievement(entitylist$entityegginfo.field_151512_d);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInWeb()
/*      */   {
/* 1981 */     if (!this.capabilities.isFlying)
/*      */     {
/* 1983 */       super.setInWeb();
/*      */     }
/*      */   }
/*      */   
/*      */   public ItemStack getCurrentArmor(int slotIn)
/*      */   {
/* 1989 */     return this.inventory.armorItemInSlot(slotIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addExperience(int amount)
/*      */   {
/* 1997 */     addScore(amount);
/* 1998 */     int i = Integer.MAX_VALUE - this.experienceTotal;
/*      */     
/* 2000 */     if (amount > i)
/*      */     {
/* 2002 */       amount = i;
/*      */     }
/*      */     
/* 2005 */     this.experience += amount / xpBarCap();
/*      */     
/* 2007 */     for (this.experienceTotal += amount; this.experience >= 1.0F; this.experience /= xpBarCap())
/*      */     {
/* 2009 */       this.experience = ((this.experience - 1.0F) * xpBarCap());
/* 2010 */       addExperienceLevel(1);
/*      */     }
/*      */   }
/*      */   
/*      */   public int getXPSeed()
/*      */   {
/* 2016 */     return this.xpSeed;
/*      */   }
/*      */   
/*      */   public void removeExperienceLevel(int levels)
/*      */   {
/* 2021 */     this.experienceLevel -= levels;
/*      */     
/* 2023 */     if (this.experienceLevel < 0)
/*      */     {
/* 2025 */       this.experienceLevel = 0;
/* 2026 */       this.experience = 0.0F;
/* 2027 */       this.experienceTotal = 0;
/*      */     }
/*      */     
/* 2030 */     this.xpSeed = this.rand.nextInt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addExperienceLevel(int levels)
/*      */   {
/* 2038 */     this.experienceLevel += levels;
/*      */     
/* 2040 */     if (this.experienceLevel < 0)
/*      */     {
/* 2042 */       this.experienceLevel = 0;
/* 2043 */       this.experience = 0.0F;
/* 2044 */       this.experienceTotal = 0;
/*      */     }
/*      */     
/* 2047 */     if ((levels > 0) && (this.experienceLevel % 5 == 0) && (this.lastXPSound < this.ticksExisted - 100.0F))
/*      */     {
/* 2049 */       float f = this.experienceLevel > 30 ? 1.0F : this.experienceLevel / 30.0F;
/* 2050 */       this.worldObj.playSoundAtEntity(this, "random.levelup", f * 0.75F, 1.0F);
/* 2051 */       this.lastXPSound = this.ticksExisted;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int xpBarCap()
/*      */   {
/* 2061 */     return this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : this.experienceLevel >= 30 ? 112 + (this.experienceLevel - 30) * 9 : 7 + this.experienceLevel * 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addExhaustion(float p_71020_1_)
/*      */   {
/* 2069 */     if (!this.capabilities.disableDamage)
/*      */     {
/* 2071 */       if (!this.worldObj.isRemote)
/*      */       {
/* 2073 */         this.foodStats.addExhaustion(p_71020_1_);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public FoodStats getFoodStats()
/*      */   {
/* 2083 */     return this.foodStats;
/*      */   }
/*      */   
/*      */   public boolean canEat(boolean ignoreHunger)
/*      */   {
/* 2088 */     return ((ignoreHunger) || (this.foodStats.needFood())) && (!this.capabilities.disableDamage);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean shouldHeal()
/*      */   {
/* 2096 */     return (getHealth() > 0.0F) && (getHealth() < getMaxHealth());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setItemInUse(ItemStack stack, int duration)
/*      */   {
/* 2104 */     if (stack != this.itemInUse)
/*      */     {
/* 2106 */       this.itemInUse = stack;
/* 2107 */       this.itemInUseCount = duration;
/*      */       
/* 2109 */       if (!this.worldObj.isRemote)
/*      */       {
/* 2111 */         setEating(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isAllowEdit()
/*      */   {
/* 2118 */     return this.capabilities.allowEdit;
/*      */   }
/*      */   
/*      */   public boolean canPlayerEdit(BlockPos p_175151_1_, EnumFacing p_175151_2_, ItemStack p_175151_3_)
/*      */   {
/* 2123 */     if (this.capabilities.allowEdit)
/*      */     {
/* 2125 */       return true;
/*      */     }
/* 2127 */     if (p_175151_3_ == null)
/*      */     {
/* 2129 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2133 */     BlockPos blockpos = p_175151_1_.offset(p_175151_2_.getOpposite());
/* 2134 */     Block block = this.worldObj.getBlockState(blockpos).getBlock();
/* 2135 */     return (p_175151_3_.canPlaceOn(block)) || (p_175151_3_.canEditBlocks());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getExperiencePoints(EntityPlayer player)
/*      */   {
/* 2144 */     if (this.worldObj.getGameRules().getBoolean("keepInventory"))
/*      */     {
/* 2146 */       return 0;
/*      */     }
/*      */     
/*      */ 
/* 2150 */     int i = this.experienceLevel * 7;
/* 2151 */     return i > 100 ? 100 : i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isPlayer()
/*      */   {
/* 2160 */     return true;
/*      */   }
/*      */   
/*      */   public boolean getAlwaysRenderNameTagForRender()
/*      */   {
/* 2165 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd)
/*      */   {
/* 2174 */     if (respawnFromEnd)
/*      */     {
/* 2176 */       this.inventory.copyInventory(oldPlayer.inventory);
/* 2177 */       setHealth(oldPlayer.getHealth());
/* 2178 */       this.foodStats = oldPlayer.foodStats;
/* 2179 */       this.experienceLevel = oldPlayer.experienceLevel;
/* 2180 */       this.experienceTotal = oldPlayer.experienceTotal;
/* 2181 */       this.experience = oldPlayer.experience;
/* 2182 */       setScore(oldPlayer.getScore());
/* 2183 */       this.field_181016_an = oldPlayer.field_181016_an;
/* 2184 */       this.field_181017_ao = oldPlayer.field_181017_ao;
/* 2185 */       this.field_181018_ap = oldPlayer.field_181018_ap;
/*      */     }
/* 2187 */     else if (this.worldObj.getGameRules().getBoolean("keepInventory"))
/*      */     {
/* 2189 */       this.inventory.copyInventory(oldPlayer.inventory);
/* 2190 */       this.experienceLevel = oldPlayer.experienceLevel;
/* 2191 */       this.experienceTotal = oldPlayer.experienceTotal;
/* 2192 */       this.experience = oldPlayer.experience;
/* 2193 */       setScore(oldPlayer.getScore());
/*      */     }
/*      */     
/* 2196 */     this.xpSeed = oldPlayer.xpSeed;
/* 2197 */     this.theInventoryEnderChest = oldPlayer.theInventoryEnderChest;
/* 2198 */     getDataWatcher().updateObject(10, Byte.valueOf(oldPlayer.getDataWatcher().getWatchableObjectByte(10)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean canTriggerWalking()
/*      */   {
/* 2207 */     return !this.capabilities.isFlying;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendPlayerAbilities() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGameType(WorldSettings.GameType gameType) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/* 2229 */     return this.gameProfile.getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public InventoryEnderChest getInventoryEnderChest()
/*      */   {
/* 2237 */     return this.theInventoryEnderChest;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack getEquipmentInSlot(int slotIn)
/*      */   {
/* 2245 */     return slotIn == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[(slotIn - 1)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack getHeldItem()
/*      */   {
/* 2253 */     return this.inventory.getCurrentItem();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
/*      */   {
/* 2261 */     this.inventory.armorInventory[slotIn] = stack;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isInvisibleToPlayer(EntityPlayer player)
/*      */   {
/* 2271 */     if (!isInvisible())
/*      */     {
/* 2273 */       return false;
/*      */     }
/* 2275 */     if (player.isSpectator())
/*      */     {
/* 2277 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2281 */     Team team = getTeam();
/* 2282 */     return (team == null) || (player == null) || (player.getTeam() != team) || (!team.getSeeFriendlyInvisiblesEnabled());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean isSpectator();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack[] getInventory()
/*      */   {
/* 2296 */     return this.inventory.armorInventory;
/*      */   }
/*      */   
/*      */   public boolean isPushedByWater()
/*      */   {
/* 2301 */     return !this.capabilities.isFlying;
/*      */   }
/*      */   
/*      */   public Scoreboard getWorldScoreboard()
/*      */   {
/* 2306 */     return this.worldObj.getScoreboard();
/*      */   }
/*      */   
/*      */   public Team getTeam()
/*      */   {
/* 2311 */     return getWorldScoreboard().getPlayersTeam(getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public IChatComponent getDisplayName()
/*      */   {
/* 2319 */     IChatComponent ichatcomponent = new net.minecraft.util.ChatComponentText(ScorePlayerTeam.formatPlayerName(getTeam(), getName()));
/* 2320 */     ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + getName() + " "));
/* 2321 */     ichatcomponent.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 2322 */     ichatcomponent.getChatStyle().setInsertion(getName());
/* 2323 */     return ichatcomponent;
/*      */   }
/*      */   
/*      */   public float getEyeHeight()
/*      */   {
/* 2328 */     float f = 1.62F;
/*      */     
/* 2330 */     if (isPlayerSleeping())
/*      */     {
/* 2332 */       f = 0.2F;
/*      */     }
/*      */     
/* 2335 */     if (isSneaking())
/*      */     {
/* 2337 */       f -= 0.08F;
/*      */     }
/*      */     
/* 2340 */     return f;
/*      */   }
/*      */   
/*      */   public void setAbsorptionAmount(float amount)
/*      */   {
/* 2345 */     if (amount < 0.0F)
/*      */     {
/* 2347 */       amount = 0.0F;
/*      */     }
/*      */     
/* 2350 */     getDataWatcher().updateObject(17, Float.valueOf(amount));
/*      */   }
/*      */   
/*      */   public float getAbsorptionAmount()
/*      */   {
/* 2355 */     return getDataWatcher().getWatchableObjectFloat(17);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UUID getUUID(GameProfile profile)
/*      */   {
/* 2363 */     UUID uuid = profile.getId();
/*      */     
/* 2365 */     if (uuid == null)
/*      */     {
/* 2367 */       uuid = getOfflineUUID(profile.getName());
/*      */     }
/*      */     
/* 2370 */     return uuid;
/*      */   }
/*      */   
/*      */   public static UUID getOfflineUUID(String username)
/*      */   {
/* 2375 */     return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canOpen(LockCode code)
/*      */   {
/* 2383 */     if (code.isEmpty())
/*      */     {
/* 2385 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2389 */     ItemStack itemstack = getCurrentEquippedItem();
/* 2390 */     return (itemstack != null) && (itemstack.hasDisplayName()) ? itemstack.getDisplayName().equals(code.getLock()) : false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isWearing(EnumPlayerModelParts p_175148_1_)
/*      */   {
/* 2396 */     return (getDataWatcher().getWatchableObjectByte(10) & p_175148_1_.getPartMask()) == p_175148_1_.getPartMask();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean sendCommandFeedback()
/*      */   {
/* 2404 */     return net.minecraft.server.MinecraftServer.getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
/*      */   }
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
/*      */   {
/* 2409 */     if ((inventorySlot >= 0) && (inventorySlot < this.inventory.mainInventory.length))
/*      */     {
/* 2411 */       this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
/* 2412 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2416 */     int i = inventorySlot - 100;
/*      */     
/* 2418 */     if ((i >= 0) && (i < this.inventory.armorInventory.length))
/*      */     {
/* 2420 */       int k = i + 1;
/*      */       
/* 2422 */       if ((itemStackIn != null) && (itemStackIn.getItem() != null))
/*      */       {
/* 2424 */         if ((itemStackIn.getItem() instanceof net.minecraft.item.ItemArmor))
/*      */         {
/* 2426 */           if (EntityLiving.getArmorPosition(itemStackIn) != k)
/*      */           {
/* 2428 */             return false;
/*      */           }
/*      */         }
/* 2431 */         else if ((k != 4) || ((itemStackIn.getItem() != Items.skull) && (!(itemStackIn.getItem() instanceof net.minecraft.item.ItemBlock))))
/*      */         {
/* 2433 */           return false;
/*      */         }
/*      */       }
/*      */       
/* 2437 */       this.inventory.setInventorySlotContents(i + this.inventory.mainInventory.length, itemStackIn);
/* 2438 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2442 */     int j = inventorySlot - 200;
/*      */     
/* 2444 */     if ((j >= 0) && (j < this.theInventoryEnderChest.getSizeInventory()))
/*      */     {
/* 2446 */       this.theInventoryEnderChest.setInventorySlotContents(j, itemStackIn);
/* 2447 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2451 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasReducedDebug()
/*      */   {
/* 2462 */     return this.hasReducedDebug;
/*      */   }
/*      */   
/*      */   public void setReducedDebug(boolean reducedDebug)
/*      */   {
/* 2467 */     this.hasReducedDebug = reducedDebug;
/*      */   }
/*      */   
/*      */   public static enum EnumChatVisibility
/*      */   {
/* 2472 */     FULL(0, "options.chat.visibility.full"), 
/* 2473 */     SYSTEM(1, "options.chat.visibility.system"), 
/* 2474 */     HIDDEN(2, "options.chat.visibility.hidden");
/*      */     
/*      */     private static final EnumChatVisibility[] ID_LOOKUP;
/*      */     private final int chatVisibility;
/*      */     private final String resourceKey;
/*      */     
/*      */     private EnumChatVisibility(int id, String resourceKey)
/*      */     {
/* 2482 */       this.chatVisibility = id;
/* 2483 */       this.resourceKey = resourceKey;
/*      */     }
/*      */     
/*      */     public int getChatVisibility()
/*      */     {
/* 2488 */       return this.chatVisibility;
/*      */     }
/*      */     
/*      */     public static EnumChatVisibility getEnumChatVisibility(int id)
/*      */     {
/* 2493 */       return ID_LOOKUP[(id % ID_LOOKUP.length)];
/*      */     }
/*      */     
/*      */     public String getResourceKey()
/*      */     {
/* 2498 */       return this.resourceKey;
/*      */     }
/*      */     
/*      */     static
/*      */     {
/* 2476 */       ID_LOOKUP = new EnumChatVisibility[values().length];
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
/*      */       EnumChatVisibility[] arrayOfEnumChatVisibility;
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
/* 2502 */       int j = (arrayOfEnumChatVisibility = values()).length; for (int i = 0; i < j; i++) { EnumChatVisibility entityplayer$enumchatvisibility = arrayOfEnumChatVisibility[i];
/*      */         
/* 2504 */         ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static enum EnumStatus
/*      */   {
/* 2511 */     OK, 
/* 2512 */     NOT_POSSIBLE_HERE, 
/* 2513 */     NOT_POSSIBLE_NOW, 
/* 2514 */     TOO_FAR_AWAY, 
/* 2515 */     OTHER_PROBLEM, 
/* 2516 */     NOT_SAFE;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\player\EntityPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */