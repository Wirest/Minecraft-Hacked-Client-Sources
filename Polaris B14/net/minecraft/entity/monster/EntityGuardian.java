/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.EntityLookHelper;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemFishFood.FishType;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.WeightedRandomFishable;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityGuardian extends EntityMob
/*     */ {
/*     */   private float field_175482_b;
/*     */   private float field_175484_c;
/*     */   private float field_175483_bk;
/*     */   private float field_175485_bl;
/*     */   private float field_175486_bm;
/*     */   private EntityLivingBase targetedEntity;
/*     */   private int field_175479_bo;
/*     */   private boolean field_175480_bp;
/*     */   private EntityAIWander wander;
/*     */   
/*     */   public EntityGuardian(World worldIn)
/*     */   {
/*  54 */     super(worldIn);
/*  55 */     this.experienceValue = 10;
/*  56 */     setSize(0.85F, 0.85F);
/*  57 */     this.tasks.addTask(4, new AIGuardianAttack(this));
/*     */     EntityAIMoveTowardsRestriction entityaimovetowardsrestriction;
/*  59 */     this.tasks.addTask(5, entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D));
/*  60 */     this.tasks.addTask(7, this.wander = new EntityAIWander(this, 1.0D, 80));
/*  61 */     this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  62 */     this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
/*  63 */     this.tasks.addTask(9, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  64 */     this.wander.setMutexBits(3);
/*  65 */     entityaimovetowardsrestriction.setMutexBits(3);
/*  66 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector(this)));
/*  67 */     this.moveHelper = new GuardianMoveHelper(this);
/*  68 */     this.field_175484_c = (this.field_175482_b = this.rand.nextFloat());
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  73 */     super.applyEntityAttributes();
/*  74 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
/*  75 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
/*  76 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
/*  77 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/*  85 */     super.readEntityFromNBT(tagCompund);
/*  86 */     setElder(tagCompund.getBoolean("Elder"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  94 */     super.writeEntityToNBT(tagCompound);
/*  95 */     tagCompound.setBoolean("Elder", isElder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PathNavigate getNewNavigator(World worldIn)
/*     */   {
/* 103 */     return new net.minecraft.pathfinding.PathNavigateSwimmer(this, worldIn);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 108 */     super.entityInit();
/* 109 */     this.dataWatcher.addObject(16, Integer.valueOf(0));
/* 110 */     this.dataWatcher.addObject(17, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isSyncedFlagSet(int flagId)
/*     */   {
/* 118 */     return (this.dataWatcher.getWatchableObjectInt(16) & flagId) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setSyncedFlag(int flagId, boolean state)
/*     */   {
/* 126 */     int i = this.dataWatcher.getWatchableObjectInt(16);
/*     */     
/* 128 */     if (state)
/*     */     {
/* 130 */       this.dataWatcher.updateObject(16, Integer.valueOf(i | flagId));
/*     */     }
/*     */     else
/*     */     {
/* 134 */       this.dataWatcher.updateObject(16, Integer.valueOf(i & (flagId ^ 0xFFFFFFFF)));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_175472_n()
/*     */   {
/* 140 */     return isSyncedFlagSet(2);
/*     */   }
/*     */   
/*     */   private void func_175476_l(boolean p_175476_1_)
/*     */   {
/* 145 */     setSyncedFlag(2, p_175476_1_);
/*     */   }
/*     */   
/*     */   public int func_175464_ck()
/*     */   {
/* 150 */     return isElder() ? 60 : 80;
/*     */   }
/*     */   
/*     */   public boolean isElder()
/*     */   {
/* 155 */     return isSyncedFlagSet(4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setElder(boolean elder)
/*     */   {
/* 163 */     setSyncedFlag(4, elder);
/*     */     
/* 165 */     if (elder)
/*     */     {
/* 167 */       setSize(1.9975F, 1.9975F);
/* 168 */       getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/* 169 */       getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
/* 170 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
/* 171 */       enablePersistence();
/* 172 */       this.wander.setExecutionChance(400);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setElder()
/*     */   {
/* 178 */     setElder(true);
/* 179 */     this.field_175486_bm = (this.field_175485_bl = 1.0F);
/*     */   }
/*     */   
/*     */   private void setTargetedEntity(int entityId)
/*     */   {
/* 184 */     this.dataWatcher.updateObject(17, Integer.valueOf(entityId));
/*     */   }
/*     */   
/*     */   public boolean hasTargetedEntity()
/*     */   {
/* 189 */     return this.dataWatcher.getWatchableObjectInt(17) != 0;
/*     */   }
/*     */   
/*     */   public EntityLivingBase getTargetedEntity()
/*     */   {
/* 194 */     if (!hasTargetedEntity())
/*     */     {
/* 196 */       return null;
/*     */     }
/* 198 */     if (this.worldObj.isRemote)
/*     */     {
/* 200 */       if (this.targetedEntity != null)
/*     */       {
/* 202 */         return this.targetedEntity;
/*     */       }
/*     */       
/*     */ 
/* 206 */       Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
/*     */       
/* 208 */       if ((entity instanceof EntityLivingBase))
/*     */       {
/* 210 */         this.targetedEntity = ((EntityLivingBase)entity);
/* 211 */         return this.targetedEntity;
/*     */       }
/*     */       
/*     */ 
/* 215 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 221 */     return getAttackTarget();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDataWatcherUpdate(int dataID)
/*     */   {
/* 227 */     super.onDataWatcherUpdate(dataID);
/*     */     
/* 229 */     if (dataID == 16)
/*     */     {
/* 231 */       if ((isElder()) && (this.width < 1.0F))
/*     */       {
/* 233 */         setSize(1.9975F, 1.9975F);
/*     */       }
/*     */     }
/* 236 */     else if (dataID == 17)
/*     */     {
/* 238 */       this.field_175479_bo = 0;
/* 239 */       this.targetedEntity = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTalkInterval()
/*     */   {
/* 248 */     return 160;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 256 */     return isElder() ? "mob.guardian.elder.idle" : !isInWater() ? "mob.guardian.land.idle" : "mob.guardian.idle";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 264 */     return isElder() ? "mob.guardian.elder.hit" : !isInWater() ? "mob.guardian.land.hit" : "mob.guardian.hit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 272 */     return isElder() ? "mob.guardian.elder.death" : !isInWater() ? "mob.guardian.land.death" : "mob.guardian.death";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/* 281 */     return false;
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 286 */     return this.height * 0.5F;
/*     */   }
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos)
/*     */   {
/* 291 */     return this.worldObj.getBlockState(pos).getBlock().getMaterial() == Material.water ? 10.0F + this.worldObj.getLightBrightness(pos) - 0.5F : super.getBlockPathWeight(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 300 */     if (this.worldObj.isRemote)
/*     */     {
/* 302 */       this.field_175484_c = this.field_175482_b;
/*     */       
/* 304 */       if (!isInWater())
/*     */       {
/* 306 */         this.field_175483_bk = 2.0F;
/*     */         
/* 308 */         if ((this.motionY > 0.0D) && (this.field_175480_bp) && (!isSilent()))
/*     */         {
/* 310 */           this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false);
/*     */         }
/*     */         
/* 313 */         this.field_175480_bp = ((this.motionY < 0.0D) && (this.worldObj.isBlockNormalCube(new BlockPos(this).down(), false)));
/*     */       }
/* 315 */       else if (func_175472_n())
/*     */       {
/* 317 */         if (this.field_175483_bk < 0.5F)
/*     */         {
/* 319 */           this.field_175483_bk = 4.0F;
/*     */         }
/*     */         else
/*     */         {
/* 323 */           this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 328 */         this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
/*     */       }
/*     */       
/* 331 */       this.field_175482_b += this.field_175483_bk;
/* 332 */       this.field_175486_bm = this.field_175485_bl;
/*     */       
/* 334 */       if (!isInWater())
/*     */       {
/* 336 */         this.field_175485_bl = this.rand.nextFloat();
/*     */       }
/* 338 */       else if (func_175472_n())
/*     */       {
/* 340 */         this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
/*     */       }
/*     */       else
/*     */       {
/* 344 */         this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
/*     */       }
/*     */       
/* 347 */       if ((func_175472_n()) && (isInWater()))
/*     */       {
/* 349 */         Vec3 vec3 = getLook(0.0F);
/*     */         
/* 351 */         for (int i = 0; i < 2; i++)
/*     */         {
/* 353 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width - vec3.xCoord * 1.5D, this.posY + this.rand.nextDouble() * this.height - vec3.yCoord * 1.5D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width - vec3.zCoord * 1.5D, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       }
/*     */       
/* 357 */       if (hasTargetedEntity())
/*     */       {
/* 359 */         if (this.field_175479_bo < func_175464_ck())
/*     */         {
/* 361 */           this.field_175479_bo += 1;
/*     */         }
/*     */         
/* 364 */         EntityLivingBase entitylivingbase = getTargetedEntity();
/*     */         
/* 366 */         if (entitylivingbase != null)
/*     */         {
/* 368 */           getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
/* 369 */           getLookHelper().onUpdateLook();
/* 370 */           double d5 = func_175477_p(0.0F);
/* 371 */           double d0 = entitylivingbase.posX - this.posX;
/* 372 */           double d1 = entitylivingbase.posY + entitylivingbase.height * 0.5F - (this.posY + getEyeHeight());
/* 373 */           double d2 = entitylivingbase.posZ - this.posZ;
/* 374 */           double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
/* 375 */           d0 /= d3;
/* 376 */           d1 /= d3;
/* 377 */           d2 /= d3;
/* 378 */           double d4 = this.rand.nextDouble();
/*     */           
/* 380 */           while (d4 < d3)
/*     */           {
/* 382 */             d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
/* 383 */             this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4, this.posY + d1 * d4 + getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 389 */     if (this.inWater)
/*     */     {
/* 391 */       setAir(300);
/*     */     }
/* 393 */     else if (this.onGround)
/*     */     {
/* 395 */       this.motionY += 0.5D;
/* 396 */       this.motionX += (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
/* 397 */       this.motionZ += (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
/* 398 */       this.rotationYaw = (this.rand.nextFloat() * 360.0F);
/* 399 */       this.onGround = false;
/* 400 */       this.isAirBorne = true;
/*     */     }
/*     */     
/* 403 */     if (hasTargetedEntity())
/*     */     {
/* 405 */       this.rotationYaw = this.rotationYawHead;
/*     */     }
/*     */     
/* 408 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   public float func_175471_a(float p_175471_1_)
/*     */   {
/* 413 */     return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
/*     */   }
/*     */   
/*     */   public float func_175469_o(float p_175469_1_)
/*     */   {
/* 418 */     return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
/*     */   }
/*     */   
/*     */   public float func_175477_p(float p_175477_1_)
/*     */   {
/* 423 */     return (this.field_175479_bo + p_175477_1_) / func_175464_ck();
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/* 428 */     super.updateAITasks();
/*     */     
/* 430 */     if (isElder())
/*     */     {
/* 432 */       int i = 1200;
/* 433 */       int j = 1200;
/* 434 */       int k = 6000;
/* 435 */       int l = 2;
/*     */       
/* 437 */       if ((this.ticksExisted + getEntityId()) % 1200 == 0)
/*     */       {
/* 439 */         Potion potion = Potion.digSlowdown;
/*     */         
/* 441 */         for (EntityPlayerMP entityplayermp : this.worldObj.getPlayers(EntityPlayerMP.class, new Predicate()
/*     */         {
/*     */           public boolean apply(EntityPlayerMP p_apply_1_)
/*     */           {
/* 445 */             return (EntityGuardian.this.getDistanceSqToEntity(p_apply_1_) < 2500.0D) && (p_apply_1_.theItemInWorldManager.survivalOrAdventure());
/*     */           }
/* 441 */         }))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 449 */           if ((!entityplayermp.isPotionActive(potion)) || (entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2) || (entityplayermp.getActivePotionEffect(potion).getDuration() < 1200))
/*     */           {
/* 451 */             entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(10, 0.0F));
/* 452 */             entityplayermp.addPotionEffect(new PotionEffect(potion.id, 6000, 2));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 457 */       if (!hasHome())
/*     */       {
/* 459 */         setHomePosAndDistance(new BlockPos(this), 16);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 469 */     int i = this.rand.nextInt(3) + this.rand.nextInt(p_70628_2_ + 1);
/*     */     
/* 471 */     if (i > 0)
/*     */     {
/* 473 */       entityDropItem(new ItemStack(Items.prismarine_shard, i, 0), 1.0F);
/*     */     }
/*     */     
/* 476 */     if (this.rand.nextInt(3 + p_70628_2_) > 1)
/*     */     {
/* 478 */       entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0F);
/*     */     }
/* 480 */     else if (this.rand.nextInt(3 + p_70628_2_) > 1)
/*     */     {
/* 482 */       entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0F);
/*     */     }
/*     */     
/* 485 */     if ((p_70628_1_) && (isElder()))
/*     */     {
/* 487 */       entityDropItem(new ItemStack(net.minecraft.init.Blocks.sponge, 1, 1), 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addRandomDrop()
/*     */   {
/* 496 */     ItemStack itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, net.minecraft.entity.projectile.EntityFishHook.func_174855_j())).getItemStack(this.rand);
/* 497 */     entityDropItem(itemstack, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidLightLevel()
/*     */   {
/* 505 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNotColliding()
/*     */   {
/* 513 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), this)) && (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 521 */     return ((this.rand.nextInt(20) == 0) || (!this.worldObj.canBlockSeeSky(new BlockPos(this)))) && (super.getCanSpawnHere());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 529 */     if ((!func_175472_n()) && (!source.isMagicDamage()) && ((source.getSourceOfDamage() instanceof EntityLivingBase)))
/*     */     {
/* 531 */       EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();
/*     */       
/* 533 */       if (!source.isExplosion())
/*     */       {
/* 535 */         entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
/* 536 */         entitylivingbase.playSound("damage.thorns", 0.5F, 1.0F);
/*     */       }
/*     */     }
/*     */     
/* 540 */     this.wander.makeUpdate();
/* 541 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getVerticalFaceSpeed()
/*     */   {
/* 550 */     return 180;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveEntityWithHeading(float strafe, float forward)
/*     */   {
/* 558 */     if (isServerWorld())
/*     */     {
/* 560 */       if (isInWater())
/*     */       {
/* 562 */         moveFlying(strafe, forward, 0.1F);
/* 563 */         moveEntity(this.motionX, this.motionY, this.motionZ);
/* 564 */         this.motionX *= 0.8999999761581421D;
/* 565 */         this.motionY *= 0.8999999761581421D;
/* 566 */         this.motionZ *= 0.8999999761581421D;
/*     */         
/* 568 */         if ((!func_175472_n()) && (getAttackTarget() == null))
/*     */         {
/* 570 */           this.motionY -= 0.005D;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 575 */         super.moveEntityWithHeading(strafe, forward);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 580 */       super.moveEntityWithHeading(strafe, forward);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIGuardianAttack extends EntityAIBase
/*     */   {
/*     */     private EntityGuardian theEntity;
/*     */     private int tickCounter;
/*     */     
/*     */     public AIGuardianAttack(EntityGuardian p_i45833_1_)
/*     */     {
/* 591 */       this.theEntity = p_i45833_1_;
/* 592 */       setMutexBits(3);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 597 */       EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
/* 598 */       return (entitylivingbase != null) && (entitylivingbase.isEntityAlive());
/*     */     }
/*     */     
/*     */     public boolean continueExecuting()
/*     */     {
/* 603 */       return (super.continueExecuting()) && ((this.theEntity.isElder()) || (this.theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) > 9.0D));
/*     */     }
/*     */     
/*     */     public void startExecuting()
/*     */     {
/* 608 */       this.tickCounter = -10;
/* 609 */       this.theEntity.getNavigator().clearPathEntity();
/* 610 */       this.theEntity.getLookHelper().setLookPositionWithEntity(this.theEntity.getAttackTarget(), 90.0F, 90.0F);
/* 611 */       this.theEntity.isAirBorne = true;
/*     */     }
/*     */     
/*     */     public void resetTask()
/*     */     {
/* 616 */       this.theEntity.setTargetedEntity(0);
/* 617 */       this.theEntity.setAttackTarget(null);
/* 618 */       this.theEntity.wander.makeUpdate();
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 623 */       EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
/* 624 */       this.theEntity.getNavigator().clearPathEntity();
/* 625 */       this.theEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
/*     */       
/* 627 */       if (!this.theEntity.canEntityBeSeen(entitylivingbase))
/*     */       {
/* 629 */         this.theEntity.setAttackTarget(null);
/*     */       }
/*     */       else
/*     */       {
/* 633 */         this.tickCounter += 1;
/*     */         
/* 635 */         if (this.tickCounter == 0)
/*     */         {
/* 637 */           this.theEntity.setTargetedEntity(this.theEntity.getAttackTarget().getEntityId());
/* 638 */           this.theEntity.worldObj.setEntityState(this.theEntity, (byte)21);
/*     */         }
/* 640 */         else if (this.tickCounter >= this.theEntity.func_175464_ck())
/*     */         {
/* 642 */           float f = 1.0F;
/*     */           
/* 644 */           if (this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD)
/*     */           {
/* 646 */             f += 2.0F;
/*     */           }
/*     */           
/* 649 */           if (this.theEntity.isElder())
/*     */           {
/* 651 */             f += 2.0F;
/*     */           }
/*     */           
/* 654 */           entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.theEntity, this.theEntity), f);
/* 655 */           entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), (float)this.theEntity.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
/* 656 */           this.theEntity.setAttackTarget(null);
/*     */         }
/* 658 */         else if ((this.tickCounter < 60) || (this.tickCounter % 20 != 0)) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 663 */         super.updateTask();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class GuardianMoveHelper extends EntityMoveHelper
/*     */   {
/*     */     private EntityGuardian entityGuardian;
/*     */     
/*     */     public GuardianMoveHelper(EntityGuardian p_i45831_1_)
/*     */     {
/* 674 */       super();
/* 675 */       this.entityGuardian = p_i45831_1_;
/*     */     }
/*     */     
/*     */     public void onUpdateMoveHelper()
/*     */     {
/* 680 */       if ((this.update) && (!this.entityGuardian.getNavigator().noPath()))
/*     */       {
/* 682 */         double d0 = this.posX - this.entityGuardian.posX;
/* 683 */         double d1 = this.posY - this.entityGuardian.posY;
/* 684 */         double d2 = this.posZ - this.entityGuardian.posZ;
/* 685 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/* 686 */         d3 = MathHelper.sqrt_double(d3);
/* 687 */         d1 /= d3;
/* 688 */         float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
/* 689 */         this.entityGuardian.rotationYaw = limitAngle(this.entityGuardian.rotationYaw, f, 30.0F);
/* 690 */         this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
/* 691 */         float f1 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
/* 692 */         this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125F);
/* 693 */         double d4 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5D) * 0.05D;
/* 694 */         double d5 = Math.cos(this.entityGuardian.rotationYaw * 3.1415927F / 180.0F);
/* 695 */         double d6 = Math.sin(this.entityGuardian.rotationYaw * 3.1415927F / 180.0F);
/* 696 */         this.entityGuardian.motionX += d4 * d5;
/* 697 */         this.entityGuardian.motionZ += d4 * d6;
/* 698 */         d4 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75D) * 0.05D;
/* 699 */         this.entityGuardian.motionY += d4 * (d6 + d5) * 0.25D;
/* 700 */         this.entityGuardian.motionY += this.entityGuardian.getAIMoveSpeed() * d1 * 0.1D;
/* 701 */         EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
/* 702 */         double d7 = this.entityGuardian.posX + d0 / d3 * 2.0D;
/* 703 */         double d8 = this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3 * 1.0D;
/* 704 */         double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0D;
/* 705 */         double d10 = entitylookhelper.getLookPosX();
/* 706 */         double d11 = entitylookhelper.getLookPosY();
/* 707 */         double d12 = entitylookhelper.getLookPosZ();
/*     */         
/* 709 */         if (!entitylookhelper.getIsLooking())
/*     */         {
/* 711 */           d10 = d7;
/* 712 */           d11 = d8;
/* 713 */           d12 = d9;
/*     */         }
/*     */         
/* 716 */         this.entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
/* 717 */         this.entityGuardian.func_175476_l(true);
/*     */       }
/*     */       else
/*     */       {
/* 721 */         this.entityGuardian.setAIMoveSpeed(0.0F);
/* 722 */         this.entityGuardian.func_175476_l(false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class GuardianTargetSelector implements Predicate<EntityLivingBase>
/*     */   {
/*     */     private EntityGuardian parentEntity;
/*     */     
/*     */     public GuardianTargetSelector(EntityGuardian p_i45832_1_)
/*     */     {
/* 733 */       this.parentEntity = p_i45832_1_;
/*     */     }
/*     */     
/*     */     public boolean apply(EntityLivingBase p_apply_1_)
/*     */     {
/* 738 */       return (((p_apply_1_ instanceof EntityPlayer)) || ((p_apply_1_ instanceof EntitySquid))) && (p_apply_1_.getDistanceSqToEntity(this.parentEntity) > 9.0D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */