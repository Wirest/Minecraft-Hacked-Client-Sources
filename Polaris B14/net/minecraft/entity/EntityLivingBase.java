/*      */ package net.minecraft.entity;
/*      */ 
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.collect.Maps;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.Block.SoundType;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*      */ import net.minecraft.entity.ai.attributes.IAttribute;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.passive.EntityWolf;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.PlayerCapabilities;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemArmor;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagFloat;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.nbt.NBTTagShort;
/*      */ import net.minecraft.network.play.server.S04PacketEntityEquipment;
/*      */ import net.minecraft.network.play.server.S0DPacketCollectItem;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.potion.PotionHelper;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.CombatTracker;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.GameRules;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ 
/*      */ public abstract class EntityLivingBase extends Entity
/*      */ {
/*   56 */   private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
/*   57 */   private static final AttributeModifier sprintingSpeedBoostModifier = new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2).setSaved(false);
/*      */   private BaseAttributeMap attributeMap;
/*   59 */   private final CombatTracker _combatTracker = new CombatTracker(this);
/*   60 */   private final Map<Integer, PotionEffect> activePotionsMap = Maps.newHashMap();
/*      */   
/*      */ 
/*   63 */   private final ItemStack[] previousEquipment = new ItemStack[5];
/*      */   
/*      */ 
/*      */   public boolean isSwingInProgress;
/*      */   
/*      */ 
/*      */   public int swingProgressInt;
/*      */   
/*      */ 
/*      */   public int arrowHitTimer;
/*      */   
/*      */ 
/*      */   public int hurtTime;
/*      */   
/*      */ 
/*      */   public int maxHurtTime;
/*      */   
/*      */ 
/*      */   public float attackedAtYaw;
/*      */   
/*      */   public int deathTime;
/*      */   
/*      */   public float prevSwingProgress;
/*      */   
/*      */   public float swingProgress;
/*      */   
/*      */   public float prevLimbSwingAmount;
/*      */   
/*      */   public float limbSwingAmount;
/*      */   
/*      */   public float limbSwing;
/*      */   
/*   95 */   public int maxHurtResistantTime = 20;
/*      */   
/*      */   public float prevCameraPitch;
/*      */   
/*      */   public float cameraPitch;
/*      */   
/*      */   public float field_70769_ao;
/*      */   
/*      */   public float field_70770_ap;
/*      */   
/*      */   public float renderYawOffset;
/*      */   
/*      */   public float prevRenderYawOffset;
/*      */   
/*      */   public float rotationYawHead;
/*      */   
/*      */   public float prevRotationYawHead;
/*  112 */   public float jumpMovementFactor = 0.02F;
/*      */   
/*      */ 
/*      */   protected EntityPlayer attackingPlayer;
/*      */   
/*      */ 
/*      */   protected int recentlyHit;
/*      */   
/*      */ 
/*      */   protected boolean dead;
/*      */   
/*      */ 
/*      */   protected int entityAge;
/*      */   
/*      */ 
/*      */   protected float prevOnGroundSpeedFactor;
/*      */   
/*      */ 
/*      */   protected float onGroundSpeedFactor;
/*      */   
/*      */ 
/*      */   protected float movedDistance;
/*      */   
/*      */ 
/*      */   protected float prevMovedDistance;
/*      */   
/*      */ 
/*      */   protected float field_70741_aB;
/*      */   
/*      */ 
/*      */   protected int scoreValue;
/*      */   
/*      */ 
/*      */   protected float lastDamage;
/*      */   
/*      */ 
/*      */   protected boolean isJumping;
/*      */   
/*      */ 
/*      */   public float moveStrafing;
/*      */   
/*      */   public float moveForward;
/*      */   
/*      */   protected float randomYawVelocity;
/*      */   
/*      */   protected int newPosRotationIncrements;
/*      */   
/*      */   protected double newPosX;
/*      */   
/*      */   protected double newPosY;
/*      */   
/*      */   protected double newPosZ;
/*      */   
/*      */   protected double newRotationYaw;
/*      */   
/*      */   protected double newRotationPitch;
/*      */   
/*  169 */   private boolean potionsNeedUpdate = true;
/*      */   
/*      */ 
/*      */   private EntityLivingBase entityLivingToAttack;
/*      */   
/*      */ 
/*      */   private int revengeTimer;
/*      */   
/*      */ 
/*      */   private EntityLivingBase lastAttacker;
/*      */   
/*      */ 
/*      */   private int lastAttackerTime;
/*      */   
/*      */ 
/*      */   private float landMovementFactor;
/*      */   
/*      */ 
/*      */   private int jumpTicks;
/*      */   
/*      */   private float absorptionAmount;
/*      */   
/*      */ 
/*      */   public void onKillCommand()
/*      */   {
/*  194 */     attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
/*      */   }
/*      */   
/*      */   public EntityLivingBase(World worldIn)
/*      */   {
/*  199 */     super(worldIn);
/*  200 */     applyEntityAttributes();
/*  201 */     setHealth(getMaxHealth());
/*  202 */     this.preventEntitySpawning = true;
/*  203 */     this.field_70770_ap = ((float)((Math.random() + 1.0D) * 0.009999999776482582D));
/*  204 */     setPosition(this.posX, this.posY, this.posZ);
/*  205 */     this.field_70769_ao = ((float)Math.random() * 12398.0F);
/*  206 */     this.rotationYaw = ((float)(Math.random() * 3.141592653589793D * 2.0D));
/*  207 */     this.rotationYawHead = this.rotationYaw;
/*  208 */     this.stepHeight = 0.6F;
/*      */   }
/*      */   
/*      */   protected void entityInit()
/*      */   {
/*  213 */     this.dataWatcher.addObject(7, Integer.valueOf(0));
/*  214 */     this.dataWatcher.addObject(8, Byte.valueOf((byte)0));
/*  215 */     this.dataWatcher.addObject(9, Byte.valueOf((byte)0));
/*  216 */     this.dataWatcher.addObject(6, Float.valueOf(1.0F));
/*      */   }
/*      */   
/*      */   protected void applyEntityAttributes()
/*      */   {
/*  221 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
/*  222 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
/*  223 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
/*      */   }
/*      */   
/*      */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos)
/*      */   {
/*  228 */     if (!isInWater())
/*      */     {
/*  230 */       handleWaterMovement();
/*      */     }
/*      */     
/*  233 */     if ((!this.worldObj.isRemote) && (this.fallDistance > 3.0F) && (onGroundIn))
/*      */     {
/*  235 */       IBlockState iblockstate = this.worldObj.getBlockState(pos);
/*  236 */       Block block = iblockstate.getBlock();
/*  237 */       float f = MathHelper.ceiling_float_int(this.fallDistance - 3.0F);
/*      */       
/*  239 */       if (block.getMaterial() != Material.air)
/*      */       {
/*  241 */         double d0 = Math.min(0.2F + f / 15.0F, 10.0F);
/*      */         
/*  243 */         if (d0 > 2.5D)
/*      */         {
/*  245 */           d0 = 2.5D;
/*      */         }
/*      */         
/*  248 */         int i = (int)(150.0D * d0);
/*  249 */         ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(iblockstate) });
/*      */       }
/*      */     }
/*      */     
/*  253 */     super.updateFallState(y, onGroundIn, blockIn, pos);
/*      */   }
/*      */   
/*      */   public boolean canBreatheUnderwater()
/*      */   {
/*  258 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEntityUpdate()
/*      */   {
/*  266 */     this.prevSwingProgress = this.swingProgress;
/*  267 */     super.onEntityUpdate();
/*  268 */     this.worldObj.theProfiler.startSection("livingEntityBaseTick");
/*  269 */     boolean flag = this instanceof EntityPlayer;
/*      */     
/*  271 */     if (isEntityAlive())
/*      */     {
/*  273 */       if (isEntityInsideOpaqueBlock())
/*      */       {
/*  275 */         attackEntityFrom(DamageSource.inWall, 1.0F);
/*      */       }
/*  277 */       else if ((flag) && (!this.worldObj.getWorldBorder().contains(getEntityBoundingBox())))
/*      */       {
/*  279 */         double d0 = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer();
/*      */         
/*  281 */         if (d0 < 0.0D)
/*      */         {
/*  283 */           attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor_double(-d0 * this.worldObj.getWorldBorder().getDamageAmount())));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  288 */     if ((isImmuneToFire()) || (this.worldObj.isRemote))
/*      */     {
/*  290 */       extinguish();
/*      */     }
/*      */     
/*  293 */     boolean flag1 = (flag) && (((EntityPlayer)this).capabilities.disableDamage);
/*      */     
/*  295 */     if (isEntityAlive())
/*      */     {
/*  297 */       if (isInsideOfMaterial(Material.water))
/*      */       {
/*  299 */         if ((!canBreatheUnderwater()) && (!isPotionActive(Potion.waterBreathing.id)) && (!flag1))
/*      */         {
/*  301 */           setAir(decreaseAirSupply(getAir()));
/*      */           
/*  303 */           if (getAir() == -20)
/*      */           {
/*  305 */             setAir(0);
/*      */             
/*  307 */             for (int i = 0; i < 8; i++)
/*      */             {
/*  309 */               float f = this.rand.nextFloat() - this.rand.nextFloat();
/*  310 */               float f1 = this.rand.nextFloat() - this.rand.nextFloat();
/*  311 */               float f2 = this.rand.nextFloat() - this.rand.nextFloat();
/*  312 */               this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f, this.posY + f1, this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
/*      */             }
/*      */             
/*  315 */             attackEntityFrom(DamageSource.drown, 2.0F);
/*      */           }
/*      */         }
/*      */         
/*  319 */         if ((!this.worldObj.isRemote) && (isRiding()) && ((this.ridingEntity instanceof EntityLivingBase)))
/*      */         {
/*  321 */           mountEntity(null);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  326 */         setAir(300);
/*      */       }
/*      */     }
/*      */     
/*  330 */     if ((isEntityAlive()) && (isWet()))
/*      */     {
/*  332 */       extinguish();
/*      */     }
/*      */     
/*  335 */     this.prevCameraPitch = this.cameraPitch;
/*      */     
/*  337 */     if (this.hurtTime > 0)
/*      */     {
/*  339 */       this.hurtTime -= 1;
/*      */     }
/*      */     
/*  342 */     if ((this.hurtResistantTime > 0) && (!(this instanceof net.minecraft.entity.player.EntityPlayerMP)))
/*      */     {
/*  344 */       this.hurtResistantTime -= 1;
/*      */     }
/*      */     
/*  347 */     if (getHealth() <= 0.0F)
/*      */     {
/*  349 */       onDeathUpdate();
/*      */     }
/*      */     
/*  352 */     if (this.recentlyHit > 0)
/*      */     {
/*  354 */       this.recentlyHit -= 1;
/*      */     }
/*      */     else
/*      */     {
/*  358 */       this.attackingPlayer = null;
/*      */     }
/*      */     
/*  361 */     if ((this.lastAttacker != null) && (!this.lastAttacker.isEntityAlive()))
/*      */     {
/*  363 */       this.lastAttacker = null;
/*      */     }
/*      */     
/*  366 */     if (this.entityLivingToAttack != null)
/*      */     {
/*  368 */       if (!this.entityLivingToAttack.isEntityAlive())
/*      */       {
/*  370 */         setRevengeTarget(null);
/*      */       }
/*  372 */       else if (this.ticksExisted - this.revengeTimer > 100)
/*      */       {
/*  374 */         setRevengeTarget(null);
/*      */       }
/*      */     }
/*      */     
/*  378 */     updatePotionEffects();
/*  379 */     this.prevMovedDistance = this.movedDistance;
/*  380 */     this.prevRenderYawOffset = this.renderYawOffset;
/*  381 */     this.prevRotationYawHead = this.rotationYawHead;
/*  382 */     this.prevRotationYaw = this.rotationYaw;
/*  383 */     this.prevRotationPitch = this.rotationPitch;
/*  384 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isChild()
/*      */   {
/*  392 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void onDeathUpdate()
/*      */   {
/*  400 */     this.deathTime += 1;
/*      */     
/*  402 */     if (this.deathTime == 20)
/*      */     {
/*  404 */       if ((!this.worldObj.isRemote) && ((this.recentlyHit > 0) || (isPlayer())) && (canDropLoot()) && (this.worldObj.getGameRules().getBoolean("doMobLoot")))
/*      */       {
/*  406 */         int i = getExperiencePoints(this.attackingPlayer);
/*      */         
/*  408 */         while (i > 0)
/*      */         {
/*  410 */           int j = EntityXPOrb.getXPSplit(i);
/*  411 */           i -= j;
/*  412 */           this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
/*      */         }
/*      */       }
/*      */       
/*  416 */       setDead();
/*      */       
/*  418 */       for (int k = 0; k < 20; k++)
/*      */       {
/*  420 */         double d2 = this.rand.nextGaussian() * 0.02D;
/*  421 */         double d0 = this.rand.nextGaussian() * 0.02D;
/*  422 */         double d1 = this.rand.nextGaussian() * 0.02D;
/*  423 */         this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, d0, d1, new int[0]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean canDropLoot()
/*      */   {
/*  433 */     return !isChild();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int decreaseAirSupply(int p_70682_1_)
/*      */   {
/*  441 */     int i = EnchantmentHelper.getRespiration(this);
/*  442 */     return (i > 0) && (this.rand.nextInt(i + 1) > 0) ? p_70682_1_ : p_70682_1_ - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getExperiencePoints(EntityPlayer player)
/*      */   {
/*  450 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isPlayer()
/*      */   {
/*  458 */     return false;
/*      */   }
/*      */   
/*      */   public Random getRNG()
/*      */   {
/*  463 */     return this.rand;
/*      */   }
/*      */   
/*      */   public EntityLivingBase getAITarget()
/*      */   {
/*  468 */     return this.entityLivingToAttack;
/*      */   }
/*      */   
/*      */   public int getRevengeTimer()
/*      */   {
/*  473 */     return this.revengeTimer;
/*      */   }
/*      */   
/*      */   public void setRevengeTarget(EntityLivingBase livingBase)
/*      */   {
/*  478 */     this.entityLivingToAttack = livingBase;
/*  479 */     this.revengeTimer = this.ticksExisted;
/*      */   }
/*      */   
/*      */   public EntityLivingBase getLastAttacker()
/*      */   {
/*  484 */     return this.lastAttacker;
/*      */   }
/*      */   
/*      */   public int getLastAttackerTime()
/*      */   {
/*  489 */     return this.lastAttackerTime;
/*      */   }
/*      */   
/*      */   public void setLastAttacker(Entity entityIn)
/*      */   {
/*  494 */     if ((entityIn instanceof EntityLivingBase))
/*      */     {
/*  496 */       this.lastAttacker = ((EntityLivingBase)entityIn);
/*      */     }
/*      */     else
/*      */     {
/*  500 */       this.lastAttacker = null;
/*      */     }
/*      */     
/*  503 */     this.lastAttackerTime = this.ticksExisted;
/*      */   }
/*      */   
/*      */   public int getAge()
/*      */   {
/*  508 */     return this.entityAge;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*      */   {
/*  516 */     tagCompound.setFloat("HealF", getHealth());
/*  517 */     tagCompound.setShort("Health", (short)(int)Math.ceil(getHealth()));
/*  518 */     tagCompound.setShort("HurtTime", (short)this.hurtTime);
/*  519 */     tagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
/*  520 */     tagCompound.setShort("DeathTime", (short)this.deathTime);
/*  521 */     tagCompound.setFloat("AbsorptionAmount", getAbsorptionAmount());
/*      */     ItemStack[] arrayOfItemStack;
/*  523 */     int j = (arrayOfItemStack = getInventory()).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*      */       
/*  525 */       if (itemstack != null)
/*      */       {
/*  527 */         this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
/*      */       }
/*      */     }
/*      */     
/*  531 */     tagCompound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(getAttributeMap()));
/*      */     
/*  533 */     j = (arrayOfItemStack = getInventory()).length; for (i = 0; i < j; i++) { ItemStack itemstack1 = arrayOfItemStack[i];
/*      */       
/*  535 */       if (itemstack1 != null)
/*      */       {
/*  537 */         this.attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers());
/*      */       }
/*      */     }
/*      */     
/*  541 */     if (!this.activePotionsMap.isEmpty())
/*      */     {
/*  543 */       NBTTagList nbttaglist = new NBTTagList();
/*      */       
/*  545 */       for (PotionEffect potioneffect : this.activePotionsMap.values())
/*      */       {
/*  547 */         nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
/*      */       }
/*      */       
/*  550 */       tagCompound.setTag("ActiveEffects", nbttaglist);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*      */   {
/*  559 */     setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
/*      */     
/*  561 */     if ((tagCompund.hasKey("Attributes", 9)) && (this.worldObj != null) && (!this.worldObj.isRemote))
/*      */     {
/*  563 */       SharedMonsterAttributes.func_151475_a(getAttributeMap(), tagCompund.getTagList("Attributes", 10));
/*      */     }
/*      */     
/*  566 */     if (tagCompund.hasKey("ActiveEffects", 9))
/*      */     {
/*  568 */       NBTTagList nbttaglist = tagCompund.getTagList("ActiveEffects", 10);
/*      */       
/*  570 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*      */       {
/*  572 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*  573 */         PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
/*      */         
/*  575 */         if (potioneffect != null)
/*      */         {
/*  577 */           this.activePotionsMap.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  582 */     if (tagCompund.hasKey("HealF", 99))
/*      */     {
/*  584 */       setHealth(tagCompund.getFloat("HealF"));
/*      */     }
/*      */     else
/*      */     {
/*  588 */       NBTBase nbtbase = tagCompund.getTag("Health");
/*      */       
/*  590 */       if (nbtbase == null)
/*      */       {
/*  592 */         setHealth(getMaxHealth());
/*      */       }
/*  594 */       else if (nbtbase.getId() == 5)
/*      */       {
/*  596 */         setHealth(((NBTTagFloat)nbtbase).getFloat());
/*      */       }
/*  598 */       else if (nbtbase.getId() == 2)
/*      */       {
/*  600 */         setHealth(((NBTTagShort)nbtbase).getShort());
/*      */       }
/*      */     }
/*      */     
/*  604 */     this.hurtTime = tagCompund.getShort("HurtTime");
/*  605 */     this.deathTime = tagCompund.getShort("DeathTime");
/*  606 */     this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
/*      */   }
/*      */   
/*      */   protected void updatePotionEffects()
/*      */   {
/*  611 */     Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
/*      */     
/*  613 */     while (iterator.hasNext())
/*      */     {
/*  615 */       Integer integer = (Integer)iterator.next();
/*  616 */       PotionEffect potioneffect = (PotionEffect)this.activePotionsMap.get(integer);
/*      */       
/*  618 */       if (!potioneffect.onUpdate(this))
/*      */       {
/*  620 */         if (!this.worldObj.isRemote)
/*      */         {
/*  622 */           iterator.remove();
/*  623 */           onFinishedPotionEffect(potioneffect);
/*      */         }
/*      */       }
/*  626 */       else if (potioneffect.getDuration() % 600 == 0)
/*      */       {
/*  628 */         onChangedPotionEffect(potioneffect, false);
/*      */       }
/*      */     }
/*      */     
/*  632 */     if (this.potionsNeedUpdate)
/*      */     {
/*  634 */       if (!this.worldObj.isRemote)
/*      */       {
/*  636 */         updatePotionMetadata();
/*      */       }
/*      */       
/*  639 */       this.potionsNeedUpdate = false;
/*      */     }
/*      */     
/*  642 */     int i = this.dataWatcher.getWatchableObjectInt(7);
/*  643 */     boolean flag1 = this.dataWatcher.getWatchableObjectByte(8) > 0;
/*      */     
/*  645 */     if (i > 0)
/*      */     {
/*  647 */       boolean flag = false;
/*      */       
/*  649 */       if (!isInvisible())
/*      */       {
/*  651 */         flag = this.rand.nextBoolean();
/*      */       }
/*      */       else
/*      */       {
/*  655 */         flag = this.rand.nextInt(15) == 0;
/*      */       }
/*      */       
/*  658 */       if (flag1)
/*      */       {
/*  660 */         flag &= this.rand.nextInt(5) == 0;
/*      */       }
/*      */       
/*  663 */       if ((flag) && (i > 0))
/*      */       {
/*  665 */         double d0 = (i >> 16 & 0xFF) / 255.0D;
/*  666 */         double d1 = (i >> 8 & 0xFF) / 255.0D;
/*  667 */         double d2 = (i >> 0 & 0xFF) / 255.0D;
/*  668 */         this.worldObj.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, d0, d1, d2, new int[0]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updatePotionMetadata()
/*      */   {
/*  679 */     if (this.activePotionsMap.isEmpty())
/*      */     {
/*  681 */       resetPotionEffectMetadata();
/*  682 */       setInvisible(false);
/*      */     }
/*      */     else
/*      */     {
/*  686 */       int i = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
/*  687 */       this.dataWatcher.updateObject(8, Byte.valueOf((byte)(PotionHelper.getAreAmbient(this.activePotionsMap.values()) ? 1 : 0)));
/*  688 */       this.dataWatcher.updateObject(7, Integer.valueOf(i));
/*  689 */       setInvisible(isPotionActive(Potion.invisibility.id));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void resetPotionEffectMetadata()
/*      */   {
/*  698 */     this.dataWatcher.updateObject(8, Byte.valueOf((byte)0));
/*  699 */     this.dataWatcher.updateObject(7, Integer.valueOf(0));
/*      */   }
/*      */   
/*      */   public void clearActivePotions()
/*      */   {
/*  704 */     Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
/*      */     
/*  706 */     while (iterator.hasNext())
/*      */     {
/*  708 */       Integer integer = (Integer)iterator.next();
/*  709 */       PotionEffect potioneffect = (PotionEffect)this.activePotionsMap.get(integer);
/*      */       
/*  711 */       if (!this.worldObj.isRemote)
/*      */       {
/*  713 */         iterator.remove();
/*  714 */         onFinishedPotionEffect(potioneffect);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Collection<PotionEffect> getActivePotionEffects()
/*      */   {
/*  721 */     return this.activePotionsMap.values();
/*      */   }
/*      */   
/*      */   public boolean isPotionActive(int potionId)
/*      */   {
/*  726 */     return this.activePotionsMap.containsKey(Integer.valueOf(potionId));
/*      */   }
/*      */   
/*      */   public boolean isPotionActive(Potion potionIn)
/*      */   {
/*  731 */     return this.activePotionsMap.containsKey(Integer.valueOf(potionIn.id));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PotionEffect getActivePotionEffect(Potion potionIn)
/*      */   {
/*  739 */     return (PotionEffect)this.activePotionsMap.get(Integer.valueOf(potionIn.id));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addPotionEffect(PotionEffect potioneffectIn)
/*      */   {
/*  747 */     if (isPotionApplicable(potioneffectIn))
/*      */     {
/*  749 */       if (this.activePotionsMap.containsKey(Integer.valueOf(potioneffectIn.getPotionID())))
/*      */       {
/*  751 */         ((PotionEffect)this.activePotionsMap.get(Integer.valueOf(potioneffectIn.getPotionID()))).combine(potioneffectIn);
/*  752 */         onChangedPotionEffect((PotionEffect)this.activePotionsMap.get(Integer.valueOf(potioneffectIn.getPotionID())), true);
/*      */       }
/*      */       else
/*      */       {
/*  756 */         this.activePotionsMap.put(Integer.valueOf(potioneffectIn.getPotionID()), potioneffectIn);
/*  757 */         onNewPotionEffect(potioneffectIn);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isPotionApplicable(PotionEffect potioneffectIn)
/*      */   {
/*  764 */     if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
/*      */     {
/*  766 */       int i = potioneffectIn.getPotionID();
/*      */       
/*  768 */       if ((i == Potion.regeneration.id) || (i == Potion.poison.id))
/*      */       {
/*  770 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  774 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEntityUndead()
/*      */   {
/*  782 */     return getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removePotionEffectClient(int potionId)
/*      */   {
/*  790 */     this.activePotionsMap.remove(Integer.valueOf(potionId));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removePotionEffect(int potionId)
/*      */   {
/*  798 */     PotionEffect potioneffect = (PotionEffect)this.activePotionsMap.remove(Integer.valueOf(potionId));
/*      */     
/*  800 */     if (potioneffect != null)
/*      */     {
/*  802 */       onFinishedPotionEffect(potioneffect);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void onNewPotionEffect(PotionEffect id)
/*      */   {
/*  808 */     this.potionsNeedUpdate = true;
/*      */     
/*  810 */     if (!this.worldObj.isRemote)
/*      */     {
/*  812 */       Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
/*      */     }
/*      */   }
/*      */   
/*      */   protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_)
/*      */   {
/*  818 */     this.potionsNeedUpdate = true;
/*      */     
/*  820 */     if ((p_70695_2_) && (!this.worldObj.isRemote))
/*      */     {
/*  822 */       Potion.potionTypes[id.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), id.getAmplifier());
/*  823 */       Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
/*      */     }
/*      */   }
/*      */   
/*      */   protected void onFinishedPotionEffect(PotionEffect p_70688_1_)
/*      */   {
/*  829 */     this.potionsNeedUpdate = true;
/*      */     
/*  831 */     if (!this.worldObj.isRemote)
/*      */     {
/*  833 */       Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), p_70688_1_.getAmplifier());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void heal(float healAmount)
/*      */   {
/*  842 */     float f = getHealth();
/*      */     
/*  844 */     if (f > 0.0F)
/*      */     {
/*  846 */       setHealth(f + healAmount);
/*      */     }
/*      */   }
/*      */   
/*      */   public final float getHealth()
/*      */   {
/*  852 */     return this.dataWatcher.getWatchableObjectFloat(6);
/*      */   }
/*      */   
/*      */   public void setHealth(float health)
/*      */   {
/*  857 */     this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(health, 0.0F, getMaxHealth())));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean attackEntityFrom(DamageSource source, float amount)
/*      */   {
/*  865 */     if (isEntityInvulnerable(source))
/*      */     {
/*  867 */       return false;
/*      */     }
/*  869 */     if (this.worldObj.isRemote)
/*      */     {
/*  871 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  875 */     this.entityAge = 0;
/*      */     
/*  877 */     if (getHealth() <= 0.0F)
/*      */     {
/*  879 */       return false;
/*      */     }
/*  881 */     if ((source.isFireDamage()) && (isPotionActive(Potion.fireResistance)))
/*      */     {
/*  883 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  887 */     if (((source == DamageSource.anvil) || (source == DamageSource.fallingBlock)) && (getEquipmentInSlot(4) != null))
/*      */     {
/*  889 */       getEquipmentInSlot(4).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this);
/*  890 */       amount *= 0.75F;
/*      */     }
/*      */     
/*  893 */     this.limbSwingAmount = 1.5F;
/*  894 */     boolean flag = true;
/*      */     
/*  896 */     if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F)
/*      */     {
/*  898 */       if (amount <= this.lastDamage)
/*      */       {
/*  900 */         return false;
/*      */       }
/*      */       
/*  903 */       damageEntity(source, amount - this.lastDamage);
/*  904 */       this.lastDamage = amount;
/*  905 */       flag = false;
/*      */     }
/*      */     else
/*      */     {
/*  909 */       this.lastDamage = amount;
/*  910 */       this.hurtResistantTime = this.maxHurtResistantTime;
/*  911 */       damageEntity(source, amount);
/*  912 */       this.hurtTime = (this.maxHurtTime = 10);
/*      */     }
/*      */     
/*  915 */     this.attackedAtYaw = 0.0F;
/*  916 */     Entity entity = source.getEntity();
/*      */     
/*  918 */     if (entity != null)
/*      */     {
/*  920 */       if ((entity instanceof EntityLivingBase))
/*      */       {
/*  922 */         setRevengeTarget((EntityLivingBase)entity);
/*      */       }
/*      */       
/*  925 */       if ((entity instanceof EntityPlayer))
/*      */       {
/*  927 */         this.recentlyHit = 100;
/*  928 */         this.attackingPlayer = ((EntityPlayer)entity);
/*      */       }
/*  930 */       else if ((entity instanceof EntityWolf))
/*      */       {
/*  932 */         EntityWolf entitywolf = (EntityWolf)entity;
/*      */         
/*  934 */         if (entitywolf.isTamed())
/*      */         {
/*  936 */           this.recentlyHit = 100;
/*  937 */           this.attackingPlayer = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  942 */     if (flag)
/*      */     {
/*  944 */       this.worldObj.setEntityState(this, (byte)2);
/*      */       
/*  946 */       if (source != DamageSource.drown)
/*      */       {
/*  948 */         setBeenAttacked();
/*      */       }
/*      */       
/*  951 */       if (entity != null)
/*      */       {
/*  953 */         double d1 = entity.posX - this.posX;
/*      */         
/*      */ 
/*  956 */         for (double d0 = entity.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
/*      */         {
/*  958 */           d1 = (Math.random() - Math.random()) * 0.01D;
/*      */         }
/*      */         
/*  961 */         this.attackedAtYaw = ((float)(MathHelper.func_181159_b(d0, d1) * 180.0D / 3.141592653589793D - this.rotationYaw));
/*  962 */         knockBack(entity, amount, d1, d0);
/*      */       }
/*      */       else
/*      */       {
/*  966 */         this.attackedAtYaw = ((int)(Math.random() * 2.0D) * 180);
/*      */       }
/*      */     }
/*      */     
/*  970 */     if (getHealth() <= 0.0F)
/*      */     {
/*  972 */       String s = getDeathSound();
/*      */       
/*  974 */       if ((flag) && (s != null))
/*      */       {
/*  976 */         playSound(s, getSoundVolume(), getSoundPitch());
/*      */       }
/*      */       
/*  979 */       onDeath(source);
/*      */     }
/*      */     else
/*      */     {
/*  983 */       String s1 = getHurtSound();
/*      */       
/*  985 */       if ((flag) && (s1 != null))
/*      */       {
/*  987 */         playSound(s1, getSoundVolume(), getSoundPitch());
/*      */       }
/*      */     }
/*      */     
/*  991 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void renderBrokenItemStack(ItemStack stack)
/*      */   {
/* 1001 */     playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
/*      */     
/* 1003 */     for (int i = 0; i < 5; i++)
/*      */     {
/* 1005 */       Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
/* 1006 */       vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/* 1007 */       vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/* 1008 */       double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
/* 1009 */       Vec3 vec31 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
/* 1010 */       vec31 = vec31.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/* 1011 */       vec31 = vec31.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/* 1012 */       vec31 = vec31.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
/* 1013 */       this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(stack.getItem()) });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDeath(DamageSource cause)
/*      */   {
/* 1022 */     Entity entity = cause.getEntity();
/* 1023 */     EntityLivingBase entitylivingbase = func_94060_bK();
/*      */     
/* 1025 */     if ((this.scoreValue >= 0) && (entitylivingbase != null))
/*      */     {
/* 1027 */       entitylivingbase.addToPlayerScore(this, this.scoreValue);
/*      */     }
/*      */     
/* 1030 */     if (entity != null)
/*      */     {
/* 1032 */       entity.onKillEntity(this);
/*      */     }
/*      */     
/* 1035 */     this.dead = true;
/* 1036 */     getCombatTracker().reset();
/*      */     
/* 1038 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1040 */       int i = 0;
/*      */       
/* 1042 */       if ((entity instanceof EntityPlayer))
/*      */       {
/* 1044 */         i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
/*      */       }
/*      */       
/* 1047 */       if ((canDropLoot()) && (this.worldObj.getGameRules().getBoolean("doMobLoot")))
/*      */       {
/* 1049 */         dropFewItems(this.recentlyHit > 0, i);
/* 1050 */         dropEquipment(this.recentlyHit > 0, i);
/*      */         
/* 1052 */         if ((this.recentlyHit > 0) && (this.rand.nextFloat() < 0.025F + i * 0.01F))
/*      */         {
/* 1054 */           addRandomDrop();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1059 */     this.worldObj.setEntityState(this, (byte)3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void knockBack(Entity entityIn, float p_70653_2_, double p_70653_3_, double p_70653_5_)
/*      */   {
/* 1074 */     if (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue())
/*      */     {
/* 1076 */       this.isAirBorne = true;
/* 1077 */       float f = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
/* 1078 */       float f1 = 0.4F;
/* 1079 */       this.motionX /= 2.0D;
/* 1080 */       this.motionY /= 2.0D;
/* 1081 */       this.motionZ /= 2.0D;
/* 1082 */       this.motionX -= p_70653_3_ / f * f1;
/* 1083 */       this.motionY += f1;
/* 1084 */       this.motionZ -= p_70653_5_ / f * f1;
/*      */       
/* 1086 */       if (this.motionY > 0.4000000059604645D)
/*      */       {
/* 1088 */         this.motionY = 0.4000000059604645D;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getHurtSound()
/*      */   {
/* 1098 */     return "game.neutral.hurt";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDeathSound()
/*      */   {
/* 1106 */     return "game.neutral.die";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void addRandomDrop() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOnLadder()
/*      */   {
/* 1128 */     int i = MathHelper.floor_double(this.posX);
/* 1129 */     int j = MathHelper.floor_double(getEntityBoundingBox().minY);
/* 1130 */     int k = MathHelper.floor_double(this.posZ);
/* 1131 */     Block block = this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();
/* 1132 */     return ((block == Blocks.ladder) || (block == Blocks.vine)) && ((!(this instanceof EntityPlayer)) || (!((EntityPlayer)this).isSpectator()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEntityAlive()
/*      */   {
/* 1140 */     return (!this.isDead) && (getHealth() > 0.0F);
/*      */   }
/*      */   
/*      */   public void fall(float distance, float damageMultiplier)
/*      */   {
/* 1145 */     super.fall(distance, damageMultiplier);
/* 1146 */     PotionEffect potioneffect = getActivePotionEffect(Potion.jump);
/* 1147 */     float f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;
/* 1148 */     int i = MathHelper.ceiling_float_int((distance - 3.0F - f) * damageMultiplier);
/*      */     
/* 1150 */     if (i > 0)
/*      */     {
/* 1152 */       playSound(getFallSoundString(i), 1.0F, 1.0F);
/* 1153 */       attackEntityFrom(DamageSource.fall, i);
/* 1154 */       int j = MathHelper.floor_double(this.posX);
/* 1155 */       int k = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/* 1156 */       int l = MathHelper.floor_double(this.posZ);
/* 1157 */       Block block = this.worldObj.getBlockState(new BlockPos(j, k, l)).getBlock();
/*      */       
/* 1159 */       if (block.getMaterial() != Material.air)
/*      */       {
/* 1161 */         Block.SoundType block$soundtype = block.stepSound;
/* 1162 */         playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.5F, block$soundtype.getFrequency() * 0.75F);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected String getFallSoundString(int damageValue)
/*      */   {
/* 1169 */     return damageValue > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void performHurtAnimation()
/*      */   {
/* 1177 */     this.hurtTime = (this.maxHurtTime = 10);
/* 1178 */     this.attackedAtYaw = 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTotalArmorValue()
/*      */   {
/* 1186 */     int i = 0;
/*      */     ItemStack[] arrayOfItemStack;
/* 1188 */     int j = (arrayOfItemStack = getInventory()).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*      */       
/* 1190 */       if ((itemstack != null) && ((itemstack.getItem() instanceof ItemArmor)))
/*      */       {
/* 1192 */         int j = ((ItemArmor)itemstack.getItem()).damageReduceAmount;
/* 1193 */         i += j;
/*      */       }
/*      */     }
/*      */     
/* 1197 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void damageArmor(float p_70675_1_) {}
/*      */   
/*      */ 
/*      */ 
/*      */   protected float applyArmorCalculations(DamageSource source, float damage)
/*      */   {
/* 1209 */     if (!source.isUnblockable())
/*      */     {
/* 1211 */       int i = 25 - getTotalArmorValue();
/* 1212 */       float f = damage * i;
/* 1213 */       damageArmor(damage);
/* 1214 */       damage = f / 25.0F;
/*      */     }
/*      */     
/* 1217 */     return damage;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float applyPotionDamageCalculations(DamageSource source, float damage)
/*      */   {
/* 1225 */     if (source.isDamageAbsolute())
/*      */     {
/* 1227 */       return damage;
/*      */     }
/*      */     
/*      */ 
/* 1231 */     if ((isPotionActive(Potion.resistance)) && (source != DamageSource.outOfWorld))
/*      */     {
/* 1233 */       int i = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
/* 1234 */       int j = 25 - i;
/* 1235 */       float f = damage * j;
/* 1236 */       damage = f / 25.0F;
/*      */     }
/*      */     
/* 1239 */     if (damage <= 0.0F)
/*      */     {
/* 1241 */       return 0.0F;
/*      */     }
/*      */     
/*      */ 
/* 1245 */     int k = EnchantmentHelper.getEnchantmentModifierDamage(getInventory(), source);
/*      */     
/* 1247 */     if (k > 20)
/*      */     {
/* 1249 */       k = 20;
/*      */     }
/*      */     
/* 1252 */     if ((k > 0) && (k <= 20))
/*      */     {
/* 1254 */       int l = 25 - k;
/* 1255 */       float f1 = damage * l;
/* 1256 */       damage = f1 / 25.0F;
/*      */     }
/*      */     
/* 1259 */     return damage;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void damageEntity(DamageSource damageSrc, float damageAmount)
/*      */   {
/* 1270 */     if (!isEntityInvulnerable(damageSrc))
/*      */     {
/* 1272 */       damageAmount = applyArmorCalculations(damageSrc, damageAmount);
/* 1273 */       damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
/* 1274 */       float f = damageAmount;
/* 1275 */       damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
/* 1276 */       setAbsorptionAmount(getAbsorptionAmount() - (f - damageAmount));
/*      */       
/* 1278 */       if (damageAmount != 0.0F)
/*      */       {
/* 1280 */         float f1 = getHealth();
/* 1281 */         setHealth(f1 - damageAmount);
/* 1282 */         getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
/* 1283 */         setAbsorptionAmount(getAbsorptionAmount() - damageAmount);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public CombatTracker getCombatTracker()
/*      */   {
/* 1290 */     return this._combatTracker;
/*      */   }
/*      */   
/*      */   public EntityLivingBase func_94060_bK()
/*      */   {
/* 1295 */     return this.entityLivingToAttack != null ? this.entityLivingToAttack : this.attackingPlayer != null ? this.attackingPlayer : this._combatTracker.func_94550_c() != null ? this._combatTracker.func_94550_c() : null;
/*      */   }
/*      */   
/*      */   public final float getMaxHealth()
/*      */   {
/* 1300 */     return (float)getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int getArrowCountInEntity()
/*      */   {
/* 1308 */     return this.dataWatcher.getWatchableObjectByte(9);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void setArrowCountInEntity(int count)
/*      */   {
/* 1316 */     this.dataWatcher.updateObject(9, Byte.valueOf((byte)count));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getArmSwingAnimationEnd()
/*      */   {
/* 1325 */     return isPotionActive(Potion.digSlowdown) ? 6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : isPotionActive(Potion.digSpeed) ? 6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : 6;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void swingItem()
/*      */   {
/* 1333 */     if ((!this.isSwingInProgress) || (this.swingProgressInt >= getArmSwingAnimationEnd() / 2) || (this.swingProgressInt < 0))
/*      */     {
/* 1335 */       this.swingProgressInt = -1;
/* 1336 */       this.isSwingInProgress = true;
/*      */       
/* 1338 */       if ((this.worldObj instanceof WorldServer))
/*      */       {
/* 1340 */         ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new net.minecraft.network.play.server.S0BPacketAnimation(this, 0));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleStatusUpdate(byte id)
/*      */   {
/* 1347 */     if (id == 2)
/*      */     {
/* 1349 */       this.limbSwingAmount = 1.5F;
/* 1350 */       this.hurtResistantTime = this.maxHurtResistantTime;
/* 1351 */       this.hurtTime = (this.maxHurtTime = 10);
/* 1352 */       this.attackedAtYaw = 0.0F;
/* 1353 */       String s = getHurtSound();
/*      */       
/* 1355 */       if (s != null)
/*      */       {
/* 1357 */         playSound(getHurtSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*      */       }
/*      */       
/* 1360 */       attackEntityFrom(DamageSource.generic, 0.0F);
/*      */     }
/* 1362 */     else if (id == 3)
/*      */     {
/* 1364 */       String s1 = getDeathSound();
/*      */       
/* 1366 */       if (s1 != null)
/*      */       {
/* 1368 */         playSound(getDeathSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*      */       }
/*      */       
/* 1371 */       setHealth(0.0F);
/* 1372 */       onDeath(DamageSource.generic);
/*      */     }
/*      */     else
/*      */     {
/* 1376 */       super.handleStatusUpdate(id);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void kill()
/*      */   {
/* 1385 */     attackEntityFrom(DamageSource.outOfWorld, 4.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateArmSwingProgress()
/*      */   {
/* 1393 */     int i = getArmSwingAnimationEnd();
/*      */     
/* 1395 */     if (this.isSwingInProgress)
/*      */     {
/* 1397 */       this.swingProgressInt += 1;
/*      */       
/* 1399 */       if (this.swingProgressInt >= i)
/*      */       {
/* 1401 */         this.swingProgressInt = 0;
/* 1402 */         this.isSwingInProgress = false;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1407 */       this.swingProgressInt = 0;
/*      */     }
/*      */     
/* 1410 */     this.swingProgress = (this.swingProgressInt / i);
/*      */   }
/*      */   
/*      */   public IAttributeInstance getEntityAttribute(IAttribute attribute)
/*      */   {
/* 1415 */     return getAttributeMap().getAttributeInstance(attribute);
/*      */   }
/*      */   
/*      */   public BaseAttributeMap getAttributeMap()
/*      */   {
/* 1420 */     if (this.attributeMap == null)
/*      */     {
/* 1422 */       this.attributeMap = new net.minecraft.entity.ai.attributes.ServersideAttributeMap();
/*      */     }
/*      */     
/* 1425 */     return this.attributeMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EnumCreatureAttribute getCreatureAttribute()
/*      */   {
/* 1433 */     return EnumCreatureAttribute.UNDEFINED;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract ItemStack getHeldItem();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract ItemStack getEquipmentInSlot(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract ItemStack getCurrentArmor(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract void setCurrentItemOrArmor(int paramInt, ItemStack paramItemStack);
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSprinting(boolean sprinting)
/*      */   {
/* 1458 */     super.setSprinting(sprinting);
/* 1459 */     IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/*      */     
/* 1461 */     if (iattributeinstance.getModifier(sprintingSpeedBoostModifierUUID) != null)
/*      */     {
/* 1463 */       iattributeinstance.removeModifier(sprintingSpeedBoostModifier);
/*      */     }
/*      */     
/* 1466 */     if (sprinting)
/*      */     {
/* 1468 */       iattributeinstance.applyModifier(sprintingSpeedBoostModifier);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract ItemStack[] getInventory();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float getSoundVolume()
/*      */   {
/* 1482 */     return 1.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float getSoundPitch()
/*      */   {
/* 1490 */     return isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isMovementBlocked()
/*      */   {
/* 1498 */     return getHealth() <= 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void dismountEntity(Entity p_110145_1_)
/*      */   {
/* 1506 */     double d0 = p_110145_1_.posX;
/* 1507 */     double d1 = p_110145_1_.getEntityBoundingBox().minY + p_110145_1_.height;
/* 1508 */     double d2 = p_110145_1_.posZ;
/* 1509 */     int i = 1;
/*      */     
/* 1511 */     for (int j = -i; j <= i; j++)
/*      */     {
/* 1513 */       for (int k = -i; k < i; k++)
/*      */       {
/* 1515 */         if ((j != 0) || (k != 0))
/*      */         {
/* 1517 */           int l = (int)(this.posX + j);
/* 1518 */           int i1 = (int)(this.posZ + k);
/* 1519 */           AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(j, 1.0D, k);
/*      */           
/* 1521 */           if (this.worldObj.func_147461_a(axisalignedbb).isEmpty())
/*      */           {
/* 1523 */             if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(l, (int)this.posY, i1)))
/*      */             {
/* 1525 */               setPositionAndUpdate(this.posX + j, this.posY + 1.0D, this.posZ + k);
/* 1526 */               return;
/*      */             }
/*      */             
/* 1529 */             if ((World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(l, (int)this.posY - 1, i1))) || (this.worldObj.getBlockState(new BlockPos(l, (int)this.posY - 1, i1)).getBlock().getMaterial() == Material.water))
/*      */             {
/* 1531 */               d0 = this.posX + j;
/* 1532 */               d1 = this.posY + 1.0D;
/* 1533 */               d2 = this.posZ + k;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1540 */     setPositionAndUpdate(d0, d1, d2);
/*      */   }
/*      */   
/*      */   public boolean getAlwaysRenderNameTagForRender()
/*      */   {
/* 1545 */     return false;
/*      */   }
/*      */   
/*      */   protected float getJumpUpwardsMotion()
/*      */   {
/* 1550 */     return 0.42F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void jump()
/*      */   {
/* 1558 */     this.motionY = getJumpUpwardsMotion();
/*      */     
/* 1560 */     if (isPotionActive(Potion.jump))
/*      */     {
/* 1562 */       this.motionY += (getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
/*      */     }
/*      */     
/* 1565 */     if (isSprinting())
/*      */     {
/* 1567 */       float f = this.rotationYaw * 0.017453292F;
/* 1568 */       this.motionX -= MathHelper.sin(f) * 0.2F;
/* 1569 */       this.motionZ += MathHelper.cos(f) * 0.2F;
/*      */     }
/*      */     
/* 1572 */     this.isAirBorne = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateAITick()
/*      */   {
/* 1580 */     this.motionY += 0.03999999910593033D;
/*      */   }
/*      */   
/*      */   protected void handleJumpLava()
/*      */   {
/* 1585 */     this.motionY += 0.03999999910593033D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void moveEntityWithHeading(float strafe, float forward)
/*      */   {
/* 1593 */     if (isServerWorld())
/*      */     {
/* 1595 */       if ((!isInWater()) || (((this instanceof EntityPlayer)) && (((EntityPlayer)this).capabilities.isFlying)))
/*      */       {
/* 1597 */         if ((!isInLava()) || (((this instanceof EntityPlayer)) && (((EntityPlayer)this).capabilities.isFlying)))
/*      */         {
/* 1599 */           float f4 = 0.91F;
/*      */           
/* 1601 */           if (this.onGround)
/*      */           {
/* 1603 */             f4 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
/*      */           }
/*      */           
/* 1606 */           float f = 0.16277136F / (f4 * f4 * f4);
/*      */           float f5;
/*      */           float f5;
/* 1609 */           if (this.onGround)
/*      */           {
/* 1611 */             f5 = getAIMoveSpeed() * f;
/*      */           }
/*      */           else
/*      */           {
/* 1615 */             f5 = this.jumpMovementFactor;
/*      */           }
/*      */           
/* 1618 */           moveFlying(strafe, forward, f5);
/* 1619 */           f4 = 0.91F;
/*      */           
/* 1621 */           if (this.onGround)
/*      */           {
/* 1623 */             f4 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
/*      */           }
/*      */           
/* 1626 */           if (isOnLadder())
/*      */           {
/* 1628 */             float f6 = 0.15F;
/* 1629 */             this.motionX = MathHelper.clamp_double(this.motionX, -f6, f6);
/* 1630 */             this.motionZ = MathHelper.clamp_double(this.motionZ, -f6, f6);
/* 1631 */             this.fallDistance = 0.0F;
/*      */             
/* 1633 */             if (this.motionY < -0.15D)
/*      */             {
/* 1635 */               this.motionY = -0.15D;
/*      */             }
/*      */             
/* 1638 */             boolean flag = (isSneaking()) && ((this instanceof EntityPlayer));
/*      */             
/* 1640 */             if ((flag) && (this.motionY < 0.0D))
/*      */             {
/* 1642 */               this.motionY = 0.0D;
/*      */             }
/*      */           }
/*      */           
/* 1646 */           moveEntity(this.motionX, this.motionY, this.motionZ);
/*      */           
/* 1648 */           if ((this.isCollidedHorizontally) && (isOnLadder()))
/*      */           {
/* 1650 */             this.motionY = 0.2D;
/*      */           }
/*      */           
/* 1653 */           if ((this.worldObj.isRemote) && ((!this.worldObj.isBlockLoaded(new BlockPos((int)this.posX, 0, (int)this.posZ))) || (!this.worldObj.getChunkFromBlockCoords(new BlockPos((int)this.posX, 0, (int)this.posZ)).isLoaded())))
/*      */           {
/* 1655 */             if (this.posY > 0.0D)
/*      */             {
/* 1657 */               this.motionY = -0.1D;
/*      */             }
/*      */             else
/*      */             {
/* 1661 */               this.motionY = 0.0D;
/*      */             }
/*      */             
/*      */           }
/*      */           else {
/* 1666 */             this.motionY -= 0.08D;
/*      */           }
/*      */           
/* 1669 */           this.motionY *= 0.9800000190734863D;
/* 1670 */           this.motionX *= f4;
/* 1671 */           this.motionZ *= f4;
/*      */         }
/*      */         else
/*      */         {
/* 1675 */           double d1 = this.posY;
/* 1676 */           moveFlying(strafe, forward, 0.02F);
/* 1677 */           moveEntity(this.motionX, this.motionY, this.motionZ);
/* 1678 */           this.motionX *= 0.5D;
/* 1679 */           this.motionY *= 0.5D;
/* 1680 */           this.motionZ *= 0.5D;
/* 1681 */           this.motionY -= 0.02D;
/*      */           
/* 1683 */           if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d1, this.motionZ)))
/*      */           {
/* 1685 */             this.motionY = 0.30000001192092896D;
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1691 */         double d0 = this.posY;
/* 1692 */         float f1 = 0.8F;
/* 1693 */         float f2 = 0.02F;
/* 1694 */         float f3 = EnchantmentHelper.getDepthStriderModifier(this);
/*      */         
/* 1696 */         if (f3 > 3.0F)
/*      */         {
/* 1698 */           f3 = 3.0F;
/*      */         }
/*      */         
/* 1701 */         if (!this.onGround)
/*      */         {
/* 1703 */           f3 *= 0.5F;
/*      */         }
/*      */         
/* 1706 */         if (f3 > 0.0F)
/*      */         {
/* 1708 */           f1 += (0.54600006F - f1) * f3 / 3.0F;
/* 1709 */           f2 += (getAIMoveSpeed() * 1.0F - f2) * f3 / 3.0F;
/*      */         }
/*      */         
/* 1712 */         moveFlying(strafe, forward, f2);
/* 1713 */         moveEntity(this.motionX, this.motionY, this.motionZ);
/* 1714 */         this.motionX *= f1;
/* 1715 */         this.motionY *= 0.800000011920929D;
/* 1716 */         this.motionZ *= f1;
/* 1717 */         this.motionY -= 0.02D;
/*      */         
/* 1719 */         if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)))
/*      */         {
/* 1721 */           this.motionY = 0.30000001192092896D;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1726 */     this.prevLimbSwingAmount = this.limbSwingAmount;
/* 1727 */     double d2 = this.posX - this.prevPosX;
/* 1728 */     double d3 = this.posZ - this.prevPosZ;
/* 1729 */     float f7 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4.0F;
/*      */     
/* 1731 */     if (f7 > 1.0F)
/*      */     {
/* 1733 */       f7 = 1.0F;
/*      */     }
/*      */     
/* 1736 */     this.limbSwingAmount += (f7 - this.limbSwingAmount) * 0.4F;
/* 1737 */     this.limbSwing += this.limbSwingAmount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getAIMoveSpeed()
/*      */   {
/* 1745 */     return this.landMovementFactor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAIMoveSpeed(float speedIn)
/*      */   {
/* 1753 */     this.landMovementFactor = speedIn;
/*      */   }
/*      */   
/*      */   public boolean attackEntityAsMob(Entity entityIn)
/*      */   {
/* 1758 */     setLastAttacker(entityIn);
/* 1759 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPlayerSleeping()
/*      */   {
/* 1767 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate()
/*      */   {
/* 1775 */     super.onUpdate();
/*      */     
/* 1777 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1779 */       int i = getArrowCountInEntity();
/*      */       
/* 1781 */       if (i > 0)
/*      */       {
/* 1783 */         if (this.arrowHitTimer <= 0)
/*      */         {
/* 1785 */           this.arrowHitTimer = (20 * (30 - i));
/*      */         }
/*      */         
/* 1788 */         this.arrowHitTimer -= 1;
/*      */         
/* 1790 */         if (this.arrowHitTimer <= 0)
/*      */         {
/* 1792 */           setArrowCountInEntity(i - 1);
/*      */         }
/*      */       }
/*      */       
/* 1796 */       for (int j = 0; j < 5; j++)
/*      */       {
/* 1798 */         ItemStack itemstack = this.previousEquipment[j];
/* 1799 */         ItemStack itemstack1 = getEquipmentInSlot(j);
/*      */         
/* 1801 */         if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
/*      */         {
/* 1803 */           ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S04PacketEntityEquipment(getEntityId(), j, itemstack1));
/*      */           
/* 1805 */           if (itemstack != null)
/*      */           {
/* 1807 */             this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
/*      */           }
/*      */           
/* 1810 */           if (itemstack1 != null)
/*      */           {
/* 1812 */             this.attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers());
/*      */           }
/*      */           
/* 1815 */           this.previousEquipment[j] = (itemstack1 == null ? null : itemstack1.copy());
/*      */         }
/*      */       }
/*      */       
/* 1819 */       if (this.ticksExisted % 20 == 0)
/*      */       {
/* 1821 */         getCombatTracker().reset();
/*      */       }
/*      */     }
/*      */     
/* 1825 */     onLivingUpdate();
/* 1826 */     double d0 = this.posX - this.prevPosX;
/* 1827 */     double d1 = this.posZ - this.prevPosZ;
/* 1828 */     float f = (float)(d0 * d0 + d1 * d1);
/* 1829 */     float f1 = this.renderYawOffset;
/* 1830 */     float f2 = 0.0F;
/* 1831 */     this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
/* 1832 */     float f3 = 0.0F;
/*      */     
/* 1834 */     if (f > 0.0025000002F)
/*      */     {
/* 1836 */       f3 = 1.0F;
/* 1837 */       f2 = (float)Math.sqrt(f) * 3.0F;
/* 1838 */       f1 = (float)MathHelper.func_181159_b(d1, d0) * 180.0F / 3.1415927F - 90.0F;
/*      */     }
/*      */     
/* 1841 */     if (this.swingProgress > 0.0F)
/*      */     {
/* 1843 */       f1 = this.rotationYaw;
/*      */     }
/*      */     
/* 1846 */     if (!this.onGround)
/*      */     {
/* 1848 */       f3 = 0.0F;
/*      */     }
/*      */     
/* 1851 */     this.onGroundSpeedFactor += (f3 - this.onGroundSpeedFactor) * 0.3F;
/* 1852 */     this.worldObj.theProfiler.startSection("headTurn");
/* 1853 */     f2 = func_110146_f(f1, f2);
/* 1854 */     this.worldObj.theProfiler.endSection();
/* 1855 */     this.worldObj.theProfiler.startSection("rangeChecks");
/*      */     
/* 1857 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*      */     {
/* 1859 */       this.prevRotationYaw -= 360.0F;
/*      */     }
/*      */     
/* 1862 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*      */     {
/* 1864 */       this.prevRotationYaw += 360.0F;
/*      */     }
/*      */     
/* 1867 */     while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F)
/*      */     {
/* 1869 */       this.prevRenderYawOffset -= 360.0F;
/*      */     }
/*      */     
/* 1872 */     while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F)
/*      */     {
/* 1874 */       this.prevRenderYawOffset += 360.0F;
/*      */     }
/*      */     
/* 1877 */     while (this.rotationPitch - this.prevRotationPitch < -180.0F)
/*      */     {
/* 1879 */       this.prevRotationPitch -= 360.0F;
/*      */     }
/*      */     
/* 1882 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*      */     {
/* 1884 */       this.prevRotationPitch += 360.0F;
/*      */     }
/*      */     
/* 1887 */     while (this.rotationYawHead - this.prevRotationYawHead < -180.0F)
/*      */     {
/* 1889 */       this.prevRotationYawHead -= 360.0F;
/*      */     }
/*      */     
/* 1892 */     while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F)
/*      */     {
/* 1894 */       this.prevRotationYawHead += 360.0F;
/*      */     }
/*      */     
/* 1897 */     this.worldObj.theProfiler.endSection();
/* 1898 */     this.movedDistance += f2;
/*      */   }
/*      */   
/*      */   protected float func_110146_f(float p_110146_1_, float p_110146_2_)
/*      */   {
/* 1903 */     float f = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
/* 1904 */     this.renderYawOffset += f * 0.3F;
/* 1905 */     float f1 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
/* 1906 */     boolean flag = (f1 < -90.0F) || (f1 >= 90.0F);
/*      */     
/* 1908 */     if (f1 < -75.0F)
/*      */     {
/* 1910 */       f1 = -75.0F;
/*      */     }
/*      */     
/* 1913 */     if (f1 >= 75.0F)
/*      */     {
/* 1915 */       f1 = 75.0F;
/*      */     }
/*      */     
/* 1918 */     this.renderYawOffset = (this.rotationYaw - f1);
/*      */     
/* 1920 */     if (f1 * f1 > 2500.0F)
/*      */     {
/* 1922 */       this.renderYawOffset += f1 * 0.2F;
/*      */     }
/*      */     
/* 1925 */     if (flag)
/*      */     {
/* 1927 */       p_110146_2_ *= -1.0F;
/*      */     }
/*      */     
/* 1930 */     return p_110146_2_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onLivingUpdate()
/*      */   {
/* 1939 */     if (this.jumpTicks > 0)
/*      */     {
/* 1941 */       this.jumpTicks -= 1;
/*      */     }
/*      */     
/* 1944 */     if (this.newPosRotationIncrements > 0)
/*      */     {
/* 1946 */       double d0 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
/* 1947 */       double d1 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
/* 1948 */       double d2 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
/* 1949 */       double d3 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
/* 1950 */       this.rotationYaw = ((float)(this.rotationYaw + d3 / this.newPosRotationIncrements));
/* 1951 */       this.rotationPitch = ((float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements));
/* 1952 */       this.newPosRotationIncrements -= 1;
/* 1953 */       setPosition(d0, d1, d2);
/* 1954 */       setRotation(this.rotationYaw, this.rotationPitch);
/*      */     }
/* 1956 */     else if (!isServerWorld())
/*      */     {
/* 1958 */       this.motionX *= 0.98D;
/* 1959 */       this.motionY *= 0.98D;
/* 1960 */       this.motionZ *= 0.98D;
/*      */     }
/*      */     
/* 1963 */     if (Math.abs(this.motionX) < 0.005D)
/*      */     {
/* 1965 */       this.motionX = 0.0D;
/*      */     }
/*      */     
/* 1968 */     if (Math.abs(this.motionY) < 0.005D)
/*      */     {
/* 1970 */       this.motionY = 0.0D;
/*      */     }
/*      */     
/* 1973 */     if (Math.abs(this.motionZ) < 0.005D)
/*      */     {
/* 1975 */       this.motionZ = 0.0D;
/*      */     }
/*      */     
/* 1978 */     this.worldObj.theProfiler.startSection("ai");
/*      */     
/* 1980 */     if (isMovementBlocked())
/*      */     {
/* 1982 */       this.isJumping = false;
/* 1983 */       this.moveStrafing = 0.0F;
/* 1984 */       this.moveForward = 0.0F;
/* 1985 */       this.randomYawVelocity = 0.0F;
/*      */     }
/* 1987 */     else if (isServerWorld())
/*      */     {
/* 1989 */       this.worldObj.theProfiler.startSection("newAi");
/* 1990 */       updateEntityActionState();
/* 1991 */       this.worldObj.theProfiler.endSection();
/*      */     }
/*      */     
/* 1994 */     this.worldObj.theProfiler.endSection();
/* 1995 */     this.worldObj.theProfiler.startSection("jump");
/*      */     
/* 1997 */     if (this.isJumping)
/*      */     {
/* 1999 */       if (isInWater())
/*      */       {
/* 2001 */         updateAITick();
/*      */       }
/* 2003 */       else if (isInLava())
/*      */       {
/* 2005 */         handleJumpLava();
/*      */       }
/* 2007 */       else if ((this.onGround) && (this.jumpTicks == 0))
/*      */       {
/* 2009 */         jump();
/* 2010 */         this.jumpTicks = 10;
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/* 2015 */       this.jumpTicks = 0;
/*      */     }
/*      */     
/* 2018 */     this.worldObj.theProfiler.endSection();
/* 2019 */     this.worldObj.theProfiler.startSection("travel");
/* 2020 */     this.moveStrafing *= 0.98F;
/* 2021 */     this.moveForward *= 0.98F;
/* 2022 */     this.randomYawVelocity *= 0.9F;
/* 2023 */     moveEntityWithHeading(this.moveStrafing, this.moveForward);
/* 2024 */     this.worldObj.theProfiler.endSection();
/* 2025 */     this.worldObj.theProfiler.startSection("push");
/*      */     
/* 2027 */     if (!this.worldObj.isRemote)
/*      */     {
/* 2029 */       collideWithNearbyEntities();
/*      */     }
/*      */     
/* 2032 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void updateEntityActionState() {}
/*      */   
/*      */ 
/*      */   protected void collideWithNearbyEntities()
/*      */   {
/* 2041 */     List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D), Predicates.and(net.minecraft.util.EntitySelectors.NOT_SPECTATING, new com.google.common.base.Predicate()
/*      */     {
/*      */       public boolean apply(Entity p_apply_1_)
/*      */       {
/* 2045 */         return p_apply_1_.canBePushed();
/*      */       }
/*      */     }));
/*      */     
/* 2049 */     if (!list.isEmpty())
/*      */     {
/* 2051 */       for (int i = 0; i < list.size(); i++)
/*      */       {
/* 2053 */         Entity entity = (Entity)list.get(i);
/* 2054 */         collideWithEntity(entity);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void collideWithEntity(Entity p_82167_1_)
/*      */   {
/* 2061 */     p_82167_1_.applyEntityCollision(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void mountEntity(Entity entityIn)
/*      */   {
/* 2069 */     if ((this.ridingEntity != null) && (entityIn == null))
/*      */     {
/* 2071 */       if (!this.worldObj.isRemote)
/*      */       {
/* 2073 */         dismountEntity(this.ridingEntity);
/*      */       }
/*      */       
/* 2076 */       if (this.ridingEntity != null)
/*      */       {
/* 2078 */         this.ridingEntity.riddenByEntity = null;
/*      */       }
/*      */       
/* 2081 */       this.ridingEntity = null;
/*      */     }
/*      */     else
/*      */     {
/* 2085 */       super.mountEntity(entityIn);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateRidden()
/*      */   {
/* 2094 */     super.updateRidden();
/* 2095 */     this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
/* 2096 */     this.onGroundSpeedFactor = 0.0F;
/* 2097 */     this.fallDistance = 0.0F;
/*      */   }
/*      */   
/*      */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
/*      */   {
/* 2102 */     this.newPosX = x;
/* 2103 */     this.newPosY = y;
/* 2104 */     this.newPosZ = z;
/* 2105 */     this.newRotationYaw = yaw;
/* 2106 */     this.newRotationPitch = pitch;
/* 2107 */     this.newPosRotationIncrements = posRotationIncrements;
/*      */   }
/*      */   
/*      */   public void setJumping(boolean p_70637_1_)
/*      */   {
/* 2112 */     this.isJumping = p_70637_1_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onItemPickup(Entity p_71001_1_, int p_71001_2_)
/*      */   {
/* 2120 */     if ((!p_71001_1_.isDead) && (!this.worldObj.isRemote))
/*      */     {
/* 2122 */       EntityTracker entitytracker = ((WorldServer)this.worldObj).getEntityTracker();
/*      */       
/* 2124 */       if ((p_71001_1_ instanceof EntityItem))
/*      */       {
/* 2126 */         entitytracker.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
/*      */       }
/*      */       
/* 2129 */       if ((p_71001_1_ instanceof net.minecraft.entity.projectile.EntityArrow))
/*      */       {
/* 2131 */         entitytracker.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
/*      */       }
/*      */       
/* 2134 */       if ((p_71001_1_ instanceof EntityXPOrb))
/*      */       {
/* 2136 */         entitytracker.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canEntityBeSeen(Entity entityIn)
/*      */   {
/* 2146 */     return this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + getEyeHeight(), this.posZ), new Vec3(entityIn.posX, entityIn.posY + entityIn.getEyeHeight(), entityIn.posZ)) == null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getLookVec()
/*      */   {
/* 2154 */     return getLook(1.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getLook(float partialTicks)
/*      */   {
/* 2162 */     if (partialTicks == 1.0F)
/*      */     {
/* 2164 */       return getVectorForRotation(this.rotationPitch, this.rotationYawHead);
/*      */     }
/*      */     
/*      */ 
/* 2168 */     float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
/* 2169 */     float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
/* 2170 */     return getVectorForRotation(f, f1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getSwingProgress(float partialTickTime)
/*      */   {
/* 2179 */     float f = this.swingProgress - this.prevSwingProgress;
/*      */     
/* 2181 */     if (f < 0.0F)
/*      */     {
/* 2183 */       f += 1.0F;
/*      */     }
/*      */     
/* 2186 */     return this.prevSwingProgress + f * partialTickTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isServerWorld()
/*      */   {
/* 2194 */     return !this.worldObj.isRemote;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBeCollidedWith()
/*      */   {
/* 2202 */     return !this.isDead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBePushed()
/*      */   {
/* 2210 */     return !this.isDead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setBeenAttacked()
/*      */   {
/* 2218 */     this.velocityChanged = (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue());
/*      */   }
/*      */   
/*      */   public float getRotationYawHead()
/*      */   {
/* 2223 */     return this.rotationYawHead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRotationYawHead(float rotation)
/*      */   {
/* 2231 */     this.rotationYawHead = rotation;
/*      */   }
/*      */   
/*      */   public void func_181013_g(float p_181013_1_)
/*      */   {
/* 2236 */     this.renderYawOffset = p_181013_1_;
/*      */   }
/*      */   
/*      */   public float getAbsorptionAmount()
/*      */   {
/* 2241 */     return this.absorptionAmount;
/*      */   }
/*      */   
/*      */   public void setAbsorptionAmount(float amount)
/*      */   {
/* 2246 */     if (amount < 0.0F)
/*      */     {
/* 2248 */       amount = 0.0F;
/*      */     }
/*      */     
/* 2251 */     this.absorptionAmount = amount;
/*      */   }
/*      */   
/*      */   public Team getTeam()
/*      */   {
/* 2256 */     return this.worldObj.getScoreboard().getPlayersTeam(getUniqueID().toString());
/*      */   }
/*      */   
/*      */   public boolean isOnSameTeam(EntityLivingBase otherEntity)
/*      */   {
/* 2261 */     return isOnTeam(otherEntity.getTeam());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOnTeam(Team p_142012_1_)
/*      */   {
/* 2269 */     return getTeam() != null ? getTeam().isSameTeam(p_142012_1_) : false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendEnterCombat() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendEndCombat() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void markPotionsDirty()
/*      */   {
/* 2288 */     this.potionsNeedUpdate = true;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */