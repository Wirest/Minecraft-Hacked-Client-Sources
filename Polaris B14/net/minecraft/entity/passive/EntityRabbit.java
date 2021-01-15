/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCarrot;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIMoveToBlock;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIPanic;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.EntityJumpHelper;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityRabbit extends EntityAnimal
/*     */ {
/*     */   private AIAvoidEntity<EntityWolf> aiAvoidWolves;
/*  44 */   private int field_175540_bm = 0;
/*  45 */   private int field_175535_bn = 0;
/*  46 */   private boolean field_175536_bo = false;
/*  47 */   private boolean field_175537_bp = false;
/*  48 */   private int currentMoveTypeDuration = 0;
/*  49 */   private EnumMoveType moveType = EnumMoveType.HOP;
/*  50 */   private int carrotTicks = 0;
/*  51 */   private EntityPlayer field_175543_bt = null;
/*     */   
/*     */   public EntityRabbit(World worldIn)
/*     */   {
/*  55 */     super(worldIn);
/*  56 */     setSize(0.6F, 0.7F);
/*  57 */     this.jumpHelper = new RabbitJumpHelper(this);
/*  58 */     this.moveHelper = new RabbitMoveHelper(this);
/*  59 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  60 */     this.navigator.setHeightRequirement(2.5F);
/*  61 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  62 */     this.tasks.addTask(1, new AIPanic(this, 1.33D));
/*  63 */     this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.carrot, false));
/*  64 */     this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.golden_carrot, false));
/*  65 */     this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Item.getItemFromBlock(Blocks.yellow_flower), false));
/*  66 */     this.tasks.addTask(3, new EntityAIMate(this, 0.8D));
/*  67 */     this.tasks.addTask(5, new AIRaidFarm(this));
/*  68 */     this.tasks.addTask(5, new EntityAIWander(this, 0.6D));
/*  69 */     this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
/*  70 */     this.aiAvoidWolves = new AIAvoidEntity(this, EntityWolf.class, 16.0F, 1.33D, 1.33D);
/*  71 */     this.tasks.addTask(4, this.aiAvoidWolves);
/*  72 */     setMovementSpeed(0.0D);
/*     */   }
/*     */   
/*     */   protected float getJumpUpwardsMotion()
/*     */   {
/*  77 */     return (this.moveHelper.isUpdating()) && (this.moveHelper.getY() > this.posY + 0.5D) ? 0.5F : this.moveType.func_180074_b();
/*     */   }
/*     */   
/*     */   public void setMoveType(EnumMoveType type)
/*     */   {
/*  82 */     this.moveType = type;
/*     */   }
/*     */   
/*     */   public float func_175521_o(float p_175521_1_)
/*     */   {
/*  87 */     return this.field_175535_bn == 0 ? 0.0F : (this.field_175540_bm + p_175521_1_) / this.field_175535_bn;
/*     */   }
/*     */   
/*     */   public void setMovementSpeed(double newSpeed)
/*     */   {
/*  92 */     getNavigator().setSpeed(newSpeed);
/*  93 */     this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
/*     */   }
/*     */   
/*     */   public void setJumping(boolean jump, EnumMoveType moveTypeIn)
/*     */   {
/*  98 */     super.setJumping(jump);
/*     */     
/* 100 */     if (!jump)
/*     */     {
/* 102 */       if (this.moveType == EnumMoveType.ATTACK)
/*     */       {
/* 104 */         this.moveType = EnumMoveType.HOP;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 109 */       setMovementSpeed(1.5D * moveTypeIn.getSpeed());
/* 110 */       playSound(getJumpingSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
/*     */     }
/*     */     
/* 113 */     this.field_175536_bo = jump;
/*     */   }
/*     */   
/*     */   public void doMovementAction(EnumMoveType movetype)
/*     */   {
/* 118 */     setJumping(true, movetype);
/* 119 */     this.field_175535_bn = movetype.func_180073_d();
/* 120 */     this.field_175540_bm = 0;
/*     */   }
/*     */   
/*     */   public boolean func_175523_cj()
/*     */   {
/* 125 */     return this.field_175536_bo;
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 130 */     super.entityInit();
/* 131 */     this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public void updateAITasks()
/*     */   {
/* 136 */     if (this.moveHelper.getSpeed() > 0.8D)
/*     */     {
/* 138 */       setMoveType(EnumMoveType.SPRINT);
/*     */     }
/* 140 */     else if (this.moveType != EnumMoveType.ATTACK)
/*     */     {
/* 142 */       setMoveType(EnumMoveType.HOP);
/*     */     }
/*     */     
/* 145 */     if (this.currentMoveTypeDuration > 0)
/*     */     {
/* 147 */       this.currentMoveTypeDuration -= 1;
/*     */     }
/*     */     
/* 150 */     if (this.carrotTicks > 0)
/*     */     {
/* 152 */       this.carrotTicks -= this.rand.nextInt(3);
/*     */       
/* 154 */       if (this.carrotTicks < 0)
/*     */       {
/* 156 */         this.carrotTicks = 0;
/*     */       }
/*     */     }
/*     */     
/* 160 */     if (this.onGround)
/*     */     {
/* 162 */       if (!this.field_175537_bp)
/*     */       {
/* 164 */         setJumping(false, EnumMoveType.NONE);
/* 165 */         func_175517_cu();
/*     */       }
/*     */       
/* 168 */       if ((getRabbitType() == 99) && (this.currentMoveTypeDuration == 0))
/*     */       {
/* 170 */         EntityLivingBase entitylivingbase = getAttackTarget();
/*     */         
/* 172 */         if ((entitylivingbase != null) && (getDistanceSqToEntity(entitylivingbase) < 16.0D))
/*     */         {
/* 174 */           calculateRotationYaw(entitylivingbase.posX, entitylivingbase.posZ);
/* 175 */           this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, this.moveHelper.getSpeed());
/* 176 */           doMovementAction(EnumMoveType.ATTACK);
/* 177 */           this.field_175537_bp = true;
/*     */         }
/*     */       }
/*     */       
/* 181 */       RabbitJumpHelper entityrabbit$rabbitjumphelper = (RabbitJumpHelper)this.jumpHelper;
/*     */       
/* 183 */       if (!entityrabbit$rabbitjumphelper.getIsJumping())
/*     */       {
/* 185 */         if ((this.moveHelper.isUpdating()) && (this.currentMoveTypeDuration == 0))
/*     */         {
/* 187 */           PathEntity pathentity = this.navigator.getPath();
/* 188 */           Vec3 vec3 = new Vec3(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
/*     */           
/* 190 */           if ((pathentity != null) && (pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength()))
/*     */           {
/* 192 */             vec3 = pathentity.getPosition(this);
/*     */           }
/*     */           
/* 195 */           calculateRotationYaw(vec3.xCoord, vec3.zCoord);
/* 196 */           doMovementAction(this.moveType);
/*     */         }
/*     */       }
/* 199 */       else if (!entityrabbit$rabbitjumphelper.func_180065_d())
/*     */       {
/* 201 */         func_175518_cr();
/*     */       }
/*     */     }
/*     */     
/* 205 */     this.field_175537_bp = this.onGround;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void spawnRunningParticles() {}
/*     */   
/*     */ 
/*     */ 
/*     */   private void calculateRotationYaw(double x, double z)
/*     */   {
/* 217 */     this.rotationYaw = ((float)(net.minecraft.util.MathHelper.func_181159_b(z - this.posZ, x - this.posX) * 180.0D / 3.141592653589793D) - 90.0F);
/*     */   }
/*     */   
/*     */   private void func_175518_cr()
/*     */   {
/* 222 */     ((RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
/*     */   }
/*     */   
/*     */   private void func_175520_cs()
/*     */   {
/* 227 */     ((RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
/*     */   }
/*     */   
/*     */   private void updateMoveTypeDuration()
/*     */   {
/* 232 */     this.currentMoveTypeDuration = getMoveTypeDuration();
/*     */   }
/*     */   
/*     */   private void func_175517_cu()
/*     */   {
/* 237 */     updateMoveTypeDuration();
/* 238 */     func_175520_cs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 247 */     super.onLivingUpdate();
/*     */     
/* 249 */     if (this.field_175540_bm != this.field_175535_bn)
/*     */     {
/* 251 */       if ((this.field_175540_bm == 0) && (!this.worldObj.isRemote))
/*     */       {
/* 253 */         this.worldObj.setEntityState(this, (byte)1);
/*     */       }
/*     */       
/* 256 */       this.field_175540_bm += 1;
/*     */     }
/* 258 */     else if (this.field_175535_bn != 0)
/*     */     {
/* 260 */       this.field_175540_bm = 0;
/* 261 */       this.field_175535_bn = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/* 267 */     super.applyEntityAttributes();
/* 268 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/* 269 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 277 */     super.writeEntityToNBT(tagCompound);
/* 278 */     tagCompound.setInteger("RabbitType", getRabbitType());
/* 279 */     tagCompound.setInteger("MoreCarrotTicks", this.carrotTicks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 287 */     super.readEntityFromNBT(tagCompund);
/* 288 */     setRabbitType(tagCompund.getInteger("RabbitType"));
/* 289 */     this.carrotTicks = tagCompund.getInteger("MoreCarrotTicks");
/*     */   }
/*     */   
/*     */   protected String getJumpingSound()
/*     */   {
/* 294 */     return "mob.rabbit.hop";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 302 */     return "mob.rabbit.idle";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 310 */     return "mob.rabbit.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 318 */     return "mob.rabbit.death";
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 323 */     if (getRabbitType() == 99)
/*     */     {
/* 325 */       playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/* 326 */       return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
/*     */     }
/*     */     
/*     */ 
/* 330 */     return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTotalArmorValue()
/*     */   {
/* 339 */     return getRabbitType() == 99 ? 8 : super.getTotalArmorValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 347 */     return isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addRandomDrop()
/*     */   {
/* 355 */     entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 363 */     int i = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
/*     */     
/* 365 */     for (int j = 0; j < i; j++)
/*     */     {
/* 367 */       dropItem(Items.rabbit_hide, 1);
/*     */     }
/*     */     
/* 370 */     i = this.rand.nextInt(2);
/*     */     
/* 372 */     for (int k = 0; k < i; k++)
/*     */     {
/* 374 */       if (isBurning())
/*     */       {
/* 376 */         dropItem(Items.cooked_rabbit, 1);
/*     */       }
/*     */       else
/*     */       {
/* 380 */         dropItem(Items.rabbit, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isRabbitBreedingItem(Item itemIn)
/*     */   {
/* 387 */     return (itemIn == Items.carrot) || (itemIn == Items.golden_carrot) || (itemIn == Item.getItemFromBlock(Blocks.yellow_flower));
/*     */   }
/*     */   
/*     */   public EntityRabbit createChild(EntityAgeable ageable)
/*     */   {
/* 392 */     EntityRabbit entityrabbit = new EntityRabbit(this.worldObj);
/*     */     
/* 394 */     if ((ageable instanceof EntityRabbit))
/*     */     {
/* 396 */       entityrabbit.setRabbitType(this.rand.nextBoolean() ? getRabbitType() : ((EntityRabbit)ageable).getRabbitType());
/*     */     }
/*     */     
/* 399 */     return entityrabbit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBreedingItem(ItemStack stack)
/*     */   {
/* 408 */     return (stack != null) && (isRabbitBreedingItem(stack.getItem()));
/*     */   }
/*     */   
/*     */   public int getRabbitType()
/*     */   {
/* 413 */     return this.dataWatcher.getWatchableObjectByte(18);
/*     */   }
/*     */   
/*     */   public void setRabbitType(int rabbitTypeId)
/*     */   {
/* 418 */     if (rabbitTypeId == 99)
/*     */     {
/* 420 */       this.tasks.removeTask(this.aiAvoidWolves);
/* 421 */       this.tasks.addTask(4, new AIEvilAttack(this));
/* 422 */       this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, false, new Class[0]));
/* 423 */       this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/* 424 */       this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));
/*     */       
/* 426 */       if (!hasCustomName())
/*     */       {
/* 428 */         setCustomNameTag(net.minecraft.util.StatCollector.translateToLocal("entity.KillerBunny.name"));
/*     */       }
/*     */     }
/*     */     
/* 432 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)rabbitTypeId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 441 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 442 */     int i = this.rand.nextInt(6);
/* 443 */     boolean flag = false;
/*     */     
/* 445 */     if ((livingdata instanceof RabbitTypeData))
/*     */     {
/* 447 */       i = ((RabbitTypeData)livingdata).typeData;
/* 448 */       flag = true;
/*     */     }
/*     */     else
/*     */     {
/* 452 */       livingdata = new RabbitTypeData(i);
/*     */     }
/*     */     
/* 455 */     setRabbitType(i);
/*     */     
/* 457 */     if (flag)
/*     */     {
/* 459 */       setGrowingAge(41536);
/*     */     }
/*     */     
/* 462 */     return livingdata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isCarrotEaten()
/*     */   {
/* 470 */     return this.carrotTicks == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getMoveTypeDuration()
/*     */   {
/* 478 */     return this.moveType.getDuration();
/*     */   }
/*     */   
/*     */   protected void createEatingParticles()
/*     */   {
/* 483 */     this.worldObj.spawnParticle(net.minecraft.util.EnumParticleTypes.BLOCK_DUST, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, 0.0D, 0.0D, 0.0D, new int[] { Block.getStateId(Blocks.carrots.getStateFromMeta(7)) });
/* 484 */     this.carrotTicks = 100;
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 489 */     if (id == 1)
/*     */     {
/* 491 */       createRunningParticles();
/* 492 */       this.field_175535_bn = 10;
/* 493 */       this.field_175540_bm = 0;
/*     */     }
/*     */     else
/*     */     {
/* 497 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T>
/*     */   {
/*     */     private EntityRabbit entityInstance;
/*     */     
/*     */     public AIAvoidEntity(EntityRabbit p_i46403_1_, Class<T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_)
/*     */     {
/* 507 */       super(p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
/* 508 */       this.entityInstance = p_i46403_1_;
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 513 */       super.updateTask();
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIEvilAttack extends EntityAIAttackOnCollide
/*     */   {
/*     */     public AIEvilAttack(EntityRabbit p_i45867_1_)
/*     */     {
/* 521 */       super(EntityLivingBase.class, 1.4D, true);
/*     */     }
/*     */     
/*     */     protected double func_179512_a(EntityLivingBase attackTarget)
/*     */     {
/* 526 */       return 4.0F + attackTarget.width;
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIPanic extends EntityAIPanic
/*     */   {
/*     */     private EntityRabbit theEntity;
/*     */     
/*     */     public AIPanic(EntityRabbit p_i45861_1_, double speedIn)
/*     */     {
/* 536 */       super(speedIn);
/* 537 */       this.theEntity = p_i45861_1_;
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 542 */       super.updateTask();
/* 543 */       this.theEntity.setMovementSpeed(this.speed);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIRaidFarm extends EntityAIMoveToBlock
/*     */   {
/*     */     private final EntityRabbit field_179500_c;
/*     */     private boolean field_179498_d;
/* 551 */     private boolean field_179499_e = false;
/*     */     
/*     */     public AIRaidFarm(EntityRabbit p_i45860_1_)
/*     */     {
/* 555 */       super(0.699999988079071D, 16);
/* 556 */       this.field_179500_c = p_i45860_1_;
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 561 */       if (this.runDelay <= 0)
/*     */       {
/* 563 */         if (!this.field_179500_c.worldObj.getGameRules().getBoolean("mobGriefing"))
/*     */         {
/* 565 */           return false;
/*     */         }
/*     */         
/* 568 */         this.field_179499_e = false;
/* 569 */         this.field_179498_d = this.field_179500_c.isCarrotEaten();
/*     */       }
/*     */       
/* 572 */       return super.shouldExecute();
/*     */     }
/*     */     
/*     */     public boolean continueExecuting()
/*     */     {
/* 577 */       return (this.field_179499_e) && (super.continueExecuting());
/*     */     }
/*     */     
/*     */     public void startExecuting()
/*     */     {
/* 582 */       super.startExecuting();
/*     */     }
/*     */     
/*     */     public void resetTask()
/*     */     {
/* 587 */       super.resetTask();
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 592 */       super.updateTask();
/* 593 */       this.field_179500_c.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.field_179500_c.getVerticalFaceSpeed());
/*     */       
/* 595 */       if (getIsAboveDestination())
/*     */       {
/* 597 */         World world = this.field_179500_c.worldObj;
/* 598 */         BlockPos blockpos = this.destinationBlock.up();
/* 599 */         IBlockState iblockstate = world.getBlockState(blockpos);
/* 600 */         Block block = iblockstate.getBlock();
/*     */         
/* 602 */         if ((this.field_179499_e) && ((block instanceof BlockCarrot)) && (((Integer)iblockstate.getValue(BlockCarrot.AGE)).intValue() == 7))
/*     */         {
/* 604 */           world.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
/* 605 */           world.destroyBlock(blockpos, true);
/* 606 */           this.field_179500_c.createEatingParticles();
/*     */         }
/*     */         
/* 609 */         this.field_179499_e = false;
/* 610 */         this.runDelay = 10;
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean shouldMoveTo(World worldIn, BlockPos pos)
/*     */     {
/* 616 */       Block block = worldIn.getBlockState(pos).getBlock();
/*     */       
/* 618 */       if (block == Blocks.farmland)
/*     */       {
/* 620 */         pos = pos.up();
/* 621 */         IBlockState iblockstate = worldIn.getBlockState(pos);
/* 622 */         block = iblockstate.getBlock();
/*     */         
/* 624 */         if (((block instanceof BlockCarrot)) && (((Integer)iblockstate.getValue(BlockCarrot.AGE)).intValue() == 7) && (this.field_179498_d) && (!this.field_179499_e))
/*     */         {
/* 626 */           this.field_179499_e = true;
/* 627 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 631 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static enum EnumMoveType
/*     */   {
/* 637 */     NONE(0.0F, 0.0F, 30, 1), 
/* 638 */     HOP(0.8F, 0.2F, 20, 10), 
/* 639 */     STEP(1.0F, 0.45F, 14, 14), 
/* 640 */     SPRINT(1.75F, 0.4F, 1, 8), 
/* 641 */     ATTACK(2.0F, 0.7F, 7, 8);
/*     */     
/*     */     private final float speed;
/*     */     private final float field_180077_g;
/*     */     private final int duration;
/*     */     private final int field_180085_i;
/*     */     
/*     */     private EnumMoveType(float typeSpeed, float p_i45866_4_, int typeDuration, int p_i45866_6_)
/*     */     {
/* 650 */       this.speed = typeSpeed;
/* 651 */       this.field_180077_g = p_i45866_4_;
/* 652 */       this.duration = typeDuration;
/* 653 */       this.field_180085_i = p_i45866_6_;
/*     */     }
/*     */     
/*     */     public float getSpeed()
/*     */     {
/* 658 */       return this.speed;
/*     */     }
/*     */     
/*     */     public float func_180074_b()
/*     */     {
/* 663 */       return this.field_180077_g;
/*     */     }
/*     */     
/*     */     public int getDuration()
/*     */     {
/* 668 */       return this.duration;
/*     */     }
/*     */     
/*     */     public int func_180073_d()
/*     */     {
/* 673 */       return this.field_180085_i;
/*     */     }
/*     */   }
/*     */   
/*     */   public class RabbitJumpHelper extends EntityJumpHelper
/*     */   {
/*     */     private EntityRabbit theEntity;
/* 680 */     private boolean field_180068_d = false;
/*     */     
/*     */     public RabbitJumpHelper(EntityRabbit rabbit)
/*     */     {
/* 684 */       super();
/* 685 */       this.theEntity = rabbit;
/*     */     }
/*     */     
/*     */     public boolean getIsJumping()
/*     */     {
/* 690 */       return this.isJumping;
/*     */     }
/*     */     
/*     */     public boolean func_180065_d()
/*     */     {
/* 695 */       return this.field_180068_d;
/*     */     }
/*     */     
/*     */     public void func_180066_a(boolean p_180066_1_)
/*     */     {
/* 700 */       this.field_180068_d = p_180066_1_;
/*     */     }
/*     */     
/*     */     public void doJump()
/*     */     {
/* 705 */       if (this.isJumping)
/*     */       {
/* 707 */         this.theEntity.doMovementAction(EntityRabbit.EnumMoveType.STEP);
/* 708 */         this.isJumping = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class RabbitMoveHelper extends EntityMoveHelper
/*     */   {
/*     */     private EntityRabbit theEntity;
/*     */     
/*     */     public RabbitMoveHelper(EntityRabbit p_i45862_1_)
/*     */     {
/* 719 */       super();
/* 720 */       this.theEntity = p_i45862_1_;
/*     */     }
/*     */     
/*     */     public void onUpdateMoveHelper()
/*     */     {
/* 725 */       if ((this.theEntity.onGround) && (!this.theEntity.func_175523_cj()))
/*     */       {
/* 727 */         this.theEntity.setMovementSpeed(0.0D);
/*     */       }
/*     */       
/* 730 */       super.onUpdateMoveHelper();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RabbitTypeData implements IEntityLivingData
/*     */   {
/*     */     public int typeData;
/*     */     
/*     */     public RabbitTypeData(int type)
/*     */     {
/* 740 */       this.typeData = type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */