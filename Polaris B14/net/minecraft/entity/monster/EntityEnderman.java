/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.management.ItemInWorldManager;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntityDamageSource;
/*     */ import net.minecraft.util.EntityDamageSourceIndirect;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityEnderman extends EntityMob
/*     */ {
/*  44 */   private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
/*  45 */   private static final AttributeModifier attackingSpeedBoostModifier = new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0).setSaved(false);
/*  46 */   private static final Set<Block> carriableBlocks = com.google.common.collect.Sets.newIdentityHashSet();
/*     */   private boolean isAggressive;
/*     */   
/*     */   public EntityEnderman(World worldIn)
/*     */   {
/*  51 */     super(worldIn);
/*  52 */     setSize(0.6F, 2.9F);
/*  53 */     this.stepHeight = 1.0F;
/*  54 */     this.tasks.addTask(0, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  55 */     this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
/*  56 */     this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
/*  57 */     this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  58 */     this.tasks.addTask(8, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  59 */     this.tasks.addTask(10, new AIPlaceBlock(this));
/*  60 */     this.tasks.addTask(11, new AITakeBlock(this));
/*  61 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, false, new Class[0]));
/*  62 */     this.targetTasks.addTask(2, new AIFindPlayer(this));
/*  63 */     this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new com.google.common.base.Predicate()
/*     */     {
/*     */       public boolean apply(EntityEndermite p_apply_1_)
/*     */       {
/*  67 */         return p_apply_1_.isSpawnedByPlayer();
/*     */       }
/*     */     }));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  74 */     super.applyEntityAttributes();
/*  75 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
/*  76 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*  77 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
/*  78 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  83 */     super.entityInit();
/*  84 */     this.dataWatcher.addObject(16, new Short((short)0));
/*  85 */     this.dataWatcher.addObject(17, new Byte((byte)0));
/*  86 */     this.dataWatcher.addObject(18, new Byte((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  94 */     super.writeEntityToNBT(tagCompound);
/*  95 */     IBlockState iblockstate = getHeldBlockState();
/*  96 */     tagCompound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
/*  97 */     tagCompound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 105 */     super.readEntityFromNBT(tagCompund);
/*     */     IBlockState iblockstate;
/*     */     IBlockState iblockstate;
/* 108 */     if (tagCompund.hasKey("carried", 8))
/*     */     {
/* 110 */       iblockstate = Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
/*     */     }
/*     */     else
/*     */     {
/* 114 */       iblockstate = Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
/*     */     }
/*     */     
/* 117 */     setHeldBlockState(iblockstate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean shouldAttackPlayer(EntityPlayer player)
/*     */   {
/* 125 */     ItemStack itemstack = player.inventory.armorInventory[3];
/*     */     
/* 127 */     if ((itemstack != null) && (itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)))
/*     */     {
/* 129 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 133 */     Vec3 vec3 = player.getLook(1.0F).normalize();
/* 134 */     Vec3 vec31 = new Vec3(this.posX - player.posX, getEntityBoundingBox().minY + this.height / 2.0F - (player.posY + player.getEyeHeight()), this.posZ - player.posZ);
/* 135 */     double d0 = vec31.lengthVector();
/* 136 */     vec31 = vec31.normalize();
/* 137 */     double d1 = vec3.dotProduct(vec31);
/* 138 */     return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
/*     */   }
/*     */   
/*     */ 
/*     */   public float getEyeHeight()
/*     */   {
/* 144 */     return 2.55F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 153 */     if (this.worldObj.isRemote)
/*     */     {
/* 155 */       for (int i = 0; i < 2; i++)
/*     */       {
/* 157 */         this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     
/* 161 */     this.isJumping = false;
/* 162 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/* 167 */     if (isWet())
/*     */     {
/* 169 */       attackEntityFrom(DamageSource.drown, 1.0F);
/*     */     }
/*     */     
/* 172 */     if ((isScreaming()) && (!this.isAggressive) && (this.rand.nextInt(100) == 0))
/*     */     {
/* 174 */       setScreaming(false);
/*     */     }
/*     */     
/* 177 */     if (this.worldObj.isDaytime())
/*     */     {
/* 179 */       float f = getBrightness(1.0F);
/*     */       
/* 181 */       if ((f > 0.5F) && (this.worldObj.canSeeSky(new BlockPos(this))) && (this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F))
/*     */       {
/* 183 */         setAttackTarget(null);
/* 184 */         setScreaming(false);
/* 185 */         this.isAggressive = false;
/* 186 */         teleportRandomly();
/*     */       }
/*     */     }
/*     */     
/* 190 */     super.updateAITasks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean teleportRandomly()
/*     */   {
/* 198 */     double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
/* 199 */     double d1 = this.posY + (this.rand.nextInt(64) - 32);
/* 200 */     double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
/* 201 */     return teleportTo(d0, d1, d2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean teleportToEntity(Entity p_70816_1_)
/*     */   {
/* 209 */     Vec3 vec3 = new Vec3(this.posX - p_70816_1_.posX, getEntityBoundingBox().minY + this.height / 2.0F - p_70816_1_.posY + p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
/* 210 */     vec3 = vec3.normalize();
/* 211 */     double d0 = 16.0D;
/* 212 */     double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
/* 213 */     double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3.yCoord * d0;
/* 214 */     double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
/* 215 */     return teleportTo(d1, d2, d3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean teleportTo(double x, double y, double z)
/*     */   {
/* 223 */     double d0 = this.posX;
/* 224 */     double d1 = this.posY;
/* 225 */     double d2 = this.posZ;
/* 226 */     this.posX = x;
/* 227 */     this.posY = y;
/* 228 */     this.posZ = z;
/* 229 */     boolean flag = false;
/* 230 */     BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
/*     */     
/* 232 */     if (this.worldObj.isBlockLoaded(blockpos))
/*     */     {
/* 234 */       boolean flag1 = false;
/*     */       
/* 236 */       while ((!flag1) && (blockpos.getY() > 0))
/*     */       {
/* 238 */         BlockPos blockpos1 = blockpos.down();
/* 239 */         Block block = this.worldObj.getBlockState(blockpos1).getBlock();
/*     */         
/* 241 */         if (block.getMaterial().blocksMovement())
/*     */         {
/* 243 */           flag1 = true;
/*     */         }
/*     */         else
/*     */         {
/* 247 */           this.posY -= 1.0D;
/* 248 */           blockpos = blockpos1;
/*     */         }
/*     */       }
/*     */       
/* 252 */       if (flag1)
/*     */       {
/* 254 */         super.setPositionAndUpdate(this.posX, this.posY, this.posZ);
/*     */         
/* 256 */         if ((this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) && (!this.worldObj.isAnyLiquid(getEntityBoundingBox())))
/*     */         {
/* 258 */           flag = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 263 */     if (!flag)
/*     */     {
/* 265 */       setPosition(d0, d1, d2);
/* 266 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 270 */     int i = 128;
/*     */     
/* 272 */     for (int j = 0; j < i; j++)
/*     */     {
/* 274 */       double d6 = j / (i - 1.0D);
/* 275 */       float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
/* 276 */       float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
/* 277 */       float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
/* 278 */       double d3 = d0 + (this.posX - d0) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
/* 279 */       double d4 = d1 + (this.posY - d1) * d6 + this.rand.nextDouble() * this.height;
/* 280 */       double d5 = d2 + (this.posZ - d2) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
/* 281 */       this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2, new int[0]);
/*     */     }
/*     */     
/* 284 */     this.worldObj.playSoundEffect(d0, d1, d2, "mob.endermen.portal", 1.0F, 1.0F);
/* 285 */     playSound("mob.endermen.portal", 1.0F, 1.0F);
/* 286 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 295 */     return isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 303 */     return "mob.endermen.hit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 311 */     return "mob.endermen.death";
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 316 */     return net.minecraft.init.Items.ender_pearl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 324 */     Item item = getDropItem();
/*     */     
/* 326 */     if (item != null)
/*     */     {
/* 328 */       int i = this.rand.nextInt(2 + p_70628_2_);
/*     */       
/* 330 */       for (int j = 0; j < i; j++)
/*     */       {
/* 332 */         dropItem(item, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeldBlockState(IBlockState state)
/*     */   {
/* 342 */     this.dataWatcher.updateObject(16, Short.valueOf((short)(Block.getStateId(state) & 0xFFFF)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getHeldBlockState()
/*     */   {
/* 350 */     return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 0xFFFF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 358 */     if (isEntityInvulnerable(source))
/*     */     {
/* 360 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 364 */     if ((source.getEntity() == null) || (!(source.getEntity() instanceof EntityEndermite)))
/*     */     {
/* 366 */       if (!this.worldObj.isRemote)
/*     */       {
/* 368 */         setScreaming(true);
/*     */       }
/*     */       
/* 371 */       if (((source instanceof EntityDamageSource)) && ((source.getEntity() instanceof EntityPlayer)))
/*     */       {
/* 373 */         if (((source.getEntity() instanceof EntityPlayerMP)) && (((EntityPlayerMP)source.getEntity()).theItemInWorldManager.isCreative()))
/*     */         {
/* 375 */           setScreaming(false);
/*     */         }
/*     */         else
/*     */         {
/* 379 */           this.isAggressive = true;
/*     */         }
/*     */       }
/*     */       
/* 383 */       if ((source instanceof EntityDamageSourceIndirect))
/*     */       {
/* 385 */         this.isAggressive = false;
/*     */         
/* 387 */         for (int i = 0; i < 64; i++)
/*     */         {
/* 389 */           if (teleportRandomly())
/*     */           {
/* 391 */             return true;
/*     */           }
/*     */         }
/*     */         
/* 395 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 399 */     boolean flag = super.attackEntityFrom(source, amount);
/*     */     
/* 401 */     if ((source.isUnblockable()) && (this.rand.nextInt(10) != 0))
/*     */     {
/* 403 */       teleportRandomly();
/*     */     }
/*     */     
/* 406 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isScreaming()
/*     */   {
/* 412 */     return this.dataWatcher.getWatchableObjectByte(18) > 0;
/*     */   }
/*     */   
/*     */   public void setScreaming(boolean screaming)
/*     */   {
/* 417 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)(screaming ? 1 : 0)));
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 422 */     carriableBlocks.add(Blocks.grass);
/* 423 */     carriableBlocks.add(Blocks.dirt);
/* 424 */     carriableBlocks.add(Blocks.sand);
/* 425 */     carriableBlocks.add(Blocks.gravel);
/* 426 */     carriableBlocks.add(Blocks.yellow_flower);
/* 427 */     carriableBlocks.add(Blocks.red_flower);
/* 428 */     carriableBlocks.add(Blocks.brown_mushroom);
/* 429 */     carriableBlocks.add(Blocks.red_mushroom);
/* 430 */     carriableBlocks.add(Blocks.tnt);
/* 431 */     carriableBlocks.add(Blocks.cactus);
/* 432 */     carriableBlocks.add(Blocks.clay);
/* 433 */     carriableBlocks.add(Blocks.pumpkin);
/* 434 */     carriableBlocks.add(Blocks.melon_block);
/* 435 */     carriableBlocks.add(Blocks.mycelium);
/*     */   }
/*     */   
/*     */   static class AIFindPlayer extends EntityAINearestAttackableTarget
/*     */   {
/*     */     private EntityPlayer player;
/*     */     private int field_179450_h;
/*     */     private int field_179451_i;
/*     */     private EntityEnderman enderman;
/*     */     
/*     */     public AIFindPlayer(EntityEnderman p_i45842_1_)
/*     */     {
/* 447 */       super(EntityPlayer.class, true);
/* 448 */       this.enderman = p_i45842_1_;
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 453 */       double d0 = getTargetDistance();
/* 454 */       List<EntityPlayer> list = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
/* 455 */       Collections.sort(list, this.theNearestAttackableTargetSorter);
/*     */       
/* 457 */       if (list.isEmpty())
/*     */       {
/* 459 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 463 */       this.player = ((EntityPlayer)list.get(0));
/* 464 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     public void startExecuting()
/*     */     {
/* 470 */       this.field_179450_h = 5;
/* 471 */       this.field_179451_i = 0;
/*     */     }
/*     */     
/*     */     public void resetTask()
/*     */     {
/* 476 */       this.player = null;
/* 477 */       this.enderman.setScreaming(false);
/* 478 */       IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 479 */       iattributeinstance.removeModifier(EntityEnderman.attackingSpeedBoostModifier);
/* 480 */       super.resetTask();
/*     */     }
/*     */     
/*     */     public boolean continueExecuting()
/*     */     {
/* 485 */       if (this.player != null)
/*     */       {
/* 487 */         if (!this.enderman.shouldAttackPlayer(this.player))
/*     */         {
/* 489 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 493 */         this.enderman.isAggressive = true;
/* 494 */         this.enderman.faceEntity(this.player, 10.0F, 10.0F);
/* 495 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 500 */       return super.continueExecuting();
/*     */     }
/*     */     
/*     */ 
/*     */     public void updateTask()
/*     */     {
/* 506 */       if (this.player != null)
/*     */       {
/* 508 */         if (--this.field_179450_h <= 0)
/*     */         {
/* 510 */           this.targetEntity = this.player;
/* 511 */           this.player = null;
/* 512 */           super.startExecuting();
/* 513 */           this.enderman.playSound("mob.endermen.stare", 1.0F, 1.0F);
/* 514 */           this.enderman.setScreaming(true);
/* 515 */           IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 516 */           iattributeinstance.applyModifier(EntityEnderman.attackingSpeedBoostModifier);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 521 */         if (this.targetEntity != null)
/*     */         {
/* 523 */           if (((this.targetEntity instanceof EntityPlayer)) && (this.enderman.shouldAttackPlayer((EntityPlayer)this.targetEntity)))
/*     */           {
/* 525 */             if (this.targetEntity.getDistanceSqToEntity(this.enderman) < 16.0D)
/*     */             {
/* 527 */               this.enderman.teleportRandomly();
/*     */             }
/*     */             
/* 530 */             this.field_179451_i = 0;
/*     */           }
/* 532 */           else if ((this.targetEntity.getDistanceSqToEntity(this.enderman) > 256.0D) && (this.field_179451_i++ >= 30) && (this.enderman.teleportToEntity(this.targetEntity)))
/*     */           {
/* 534 */             this.field_179451_i = 0;
/*     */           }
/*     */         }
/*     */         
/* 538 */         super.updateTask();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIPlaceBlock extends EntityAIBase
/*     */   {
/*     */     private EntityEnderman enderman;
/*     */     
/*     */     public AIPlaceBlock(EntityEnderman p_i45843_1_)
/*     */     {
/* 549 */       this.enderman = p_i45843_1_;
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 554 */       return this.enderman.worldObj.getGameRules().getBoolean("mobGriefing");
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 559 */       Random random = this.enderman.getRNG();
/* 560 */       World world = this.enderman.worldObj;
/* 561 */       int i = MathHelper.floor_double(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
/* 562 */       int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 2.0D);
/* 563 */       int k = MathHelper.floor_double(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
/* 564 */       BlockPos blockpos = new BlockPos(i, j, k);
/* 565 */       Block block = world.getBlockState(blockpos).getBlock();
/* 566 */       Block block1 = world.getBlockState(blockpos.down()).getBlock();
/*     */       
/* 568 */       if (func_179474_a(world, blockpos, this.enderman.getHeldBlockState().getBlock(), block, block1))
/*     */       {
/* 570 */         world.setBlockState(blockpos, this.enderman.getHeldBlockState(), 3);
/* 571 */         this.enderman.setHeldBlockState(Blocks.air.getDefaultState());
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean func_179474_a(World worldIn, BlockPos p_179474_2_, Block p_179474_3_, Block p_179474_4_, Block p_179474_5_)
/*     */     {
/* 577 */       return p_179474_5_.getMaterial() == Material.air ? false : p_179474_4_.getMaterial() != Material.air ? false : !p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) ? false : p_179474_5_.isFullCube();
/*     */     }
/*     */   }
/*     */   
/*     */   static class AITakeBlock extends EntityAIBase
/*     */   {
/*     */     private EntityEnderman enderman;
/*     */     
/*     */     public AITakeBlock(EntityEnderman p_i45841_1_)
/*     */     {
/* 587 */       this.enderman = p_i45841_1_;
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 592 */       return this.enderman.worldObj.getGameRules().getBoolean("mobGriefing");
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 597 */       Random random = this.enderman.getRNG();
/* 598 */       World world = this.enderman.worldObj;
/* 599 */       int i = MathHelper.floor_double(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
/* 600 */       int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 3.0D);
/* 601 */       int k = MathHelper.floor_double(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
/* 602 */       BlockPos blockpos = new BlockPos(i, j, k);
/* 603 */       IBlockState iblockstate = world.getBlockState(blockpos);
/* 604 */       Block block = iblockstate.getBlock();
/*     */       
/* 606 */       if (EntityEnderman.carriableBlocks.contains(block))
/*     */       {
/* 608 */         this.enderman.setHeldBlockState(iblockstate);
/* 609 */         world.setBlockState(blockpos, Blocks.air.getDefaultState());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityEnderman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */