/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityJumpHelper;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public class EntitySlime extends EntityLiving implements IMob
/*     */ {
/*     */   public float squishAmount;
/*     */   public float squishFactor;
/*     */   public float prevSquishFactor;
/*     */   private boolean wasOnGround;
/*     */   
/*     */   public EntitySlime(World worldIn)
/*     */   {
/*  37 */     super(worldIn);
/*  38 */     this.moveHelper = new SlimeMoveHelper(this);
/*  39 */     this.tasks.addTask(1, new AISlimeFloat(this));
/*  40 */     this.tasks.addTask(2, new AISlimeAttack(this));
/*  41 */     this.tasks.addTask(3, new AISlimeFaceRandom(this));
/*  42 */     this.tasks.addTask(5, new AISlimeHop(this));
/*  43 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer(this));
/*  44 */     this.targetTasks.addTask(3, new net.minecraft.entity.ai.EntityAIFindEntityNearest(this, EntityIronGolem.class));
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  49 */     super.entityInit();
/*  50 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)1));
/*     */   }
/*     */   
/*     */   protected void setSlimeSize(int size)
/*     */   {
/*  55 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)size));
/*  56 */     setSize(0.51000005F * size, 0.51000005F * size);
/*  57 */     setPosition(this.posX, this.posY, this.posZ);
/*  58 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(size * size);
/*  59 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2F + 0.1F * size);
/*  60 */     setHealth(getMaxHealth());
/*  61 */     this.experienceValue = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSlimeSize()
/*     */   {
/*  69 */     return this.dataWatcher.getWatchableObjectByte(16);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  77 */     super.writeEntityToNBT(tagCompound);
/*  78 */     tagCompound.setInteger("Size", getSlimeSize() - 1);
/*  79 */     tagCompound.setBoolean("wasOnGround", this.wasOnGround);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/*  87 */     super.readEntityFromNBT(tagCompund);
/*  88 */     int i = tagCompund.getInteger("Size");
/*     */     
/*  90 */     if (i < 0)
/*     */     {
/*  92 */       i = 0;
/*     */     }
/*     */     
/*  95 */     setSlimeSize(i + 1);
/*  96 */     this.wasOnGround = tagCompund.getBoolean("wasOnGround");
/*     */   }
/*     */   
/*     */   protected EnumParticleTypes getParticleType()
/*     */   {
/* 101 */     return EnumParticleTypes.SLIME;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getJumpSound()
/*     */   {
/* 109 */     return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 117 */     if ((!this.worldObj.isRemote) && (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) && (getSlimeSize() > 0))
/*     */     {
/* 119 */       this.isDead = true;
/*     */     }
/*     */     
/* 122 */     this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
/* 123 */     this.prevSquishFactor = this.squishFactor;
/* 124 */     super.onUpdate();
/*     */     
/* 126 */     if ((this.onGround) && (!this.wasOnGround))
/*     */     {
/* 128 */       int i = getSlimeSize();
/*     */       
/* 130 */       for (int j = 0; j < i * 8; j++)
/*     */       {
/* 132 */         float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
/* 133 */         float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
/* 134 */         float f2 = MathHelper.sin(f) * i * 0.5F * f1;
/* 135 */         float f3 = MathHelper.cos(f) * i * 0.5F * f1;
/* 136 */         World world = this.worldObj;
/* 137 */         EnumParticleTypes enumparticletypes = getParticleType();
/* 138 */         double d0 = this.posX + f2;
/* 139 */         double d1 = this.posZ + f3;
/* 140 */         world.spawnParticle(enumparticletypes, d0, getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */       
/* 143 */       if (makesSoundOnLand())
/*     */       {
/* 145 */         playSound(getJumpSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
/*     */       }
/*     */       
/* 148 */       this.squishAmount = -0.5F;
/*     */     }
/* 150 */     else if ((!this.onGround) && (this.wasOnGround))
/*     */     {
/* 152 */       this.squishAmount = 1.0F;
/*     */     }
/*     */     
/* 155 */     this.wasOnGround = this.onGround;
/* 156 */     alterSquishAmount();
/*     */   }
/*     */   
/*     */   protected void alterSquishAmount()
/*     */   {
/* 161 */     this.squishAmount *= 0.6F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getJumpDelay()
/*     */   {
/* 169 */     return this.rand.nextInt(20) + 10;
/*     */   }
/*     */   
/*     */   protected EntitySlime createInstance()
/*     */   {
/* 174 */     return new EntitySlime(this.worldObj);
/*     */   }
/*     */   
/*     */   public void onDataWatcherUpdate(int dataID)
/*     */   {
/* 179 */     if (dataID == 16)
/*     */     {
/* 181 */       int i = getSlimeSize();
/* 182 */       setSize(0.51000005F * i, 0.51000005F * i);
/* 183 */       this.rotationYaw = this.rotationYawHead;
/* 184 */       this.renderYawOffset = this.rotationYawHead;
/*     */       
/* 186 */       if ((isInWater()) && (this.rand.nextInt(20) == 0))
/*     */       {
/* 188 */         resetHeight();
/*     */       }
/*     */     }
/*     */     
/* 192 */     super.onDataWatcherUpdate(dataID);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDead()
/*     */   {
/* 200 */     int i = getSlimeSize();
/*     */     
/* 202 */     if ((!this.worldObj.isRemote) && (i > 1) && (getHealth() <= 0.0F))
/*     */     {
/* 204 */       int j = 2 + this.rand.nextInt(3);
/*     */       
/* 206 */       for (int k = 0; k < j; k++)
/*     */       {
/* 208 */         float f = (k % 2 - 0.5F) * i / 4.0F;
/* 209 */         float f1 = (k / 2 - 0.5F) * i / 4.0F;
/* 210 */         EntitySlime entityslime = createInstance();
/*     */         
/* 212 */         if (hasCustomName())
/*     */         {
/* 214 */           entityslime.setCustomNameTag(getCustomNameTag());
/*     */         }
/*     */         
/* 217 */         if (isNoDespawnRequired())
/*     */         {
/* 219 */           entityslime.enablePersistence();
/*     */         }
/*     */         
/* 222 */         entityslime.setSlimeSize(i / 2);
/* 223 */         entityslime.setLocationAndAngles(this.posX + f, this.posY + 0.5D, this.posZ + f1, this.rand.nextFloat() * 360.0F, 0.0F);
/* 224 */         this.worldObj.spawnEntityInWorld(entityslime);
/*     */       }
/*     */     }
/*     */     
/* 228 */     super.setDead();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void applyEntityCollision(Entity entityIn)
/*     */   {
/* 236 */     super.applyEntityCollision(entityIn);
/*     */     
/* 238 */     if (((entityIn instanceof EntityIronGolem)) && (canDamagePlayer()))
/*     */     {
/* 240 */       func_175451_e((EntityLivingBase)entityIn);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn)
/*     */   {
/* 249 */     if (canDamagePlayer())
/*     */     {
/* 251 */       func_175451_e(entityIn);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_175451_e(EntityLivingBase p_175451_1_)
/*     */   {
/* 257 */     int i = getSlimeSize();
/*     */     
/* 259 */     if ((canEntityBeSeen(p_175451_1_)) && (getDistanceSqToEntity(p_175451_1_) < 0.6D * i * 0.6D * i) && (p_175451_1_.attackEntityFrom(net.minecraft.util.DamageSource.causeMobDamage(this), getAttackStrength())))
/*     */     {
/* 261 */       playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/* 262 */       applyEnchantments(this, p_175451_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 268 */     return 0.625F * this.height;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDamagePlayer()
/*     */   {
/* 276 */     return getSlimeSize() > 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getAttackStrength()
/*     */   {
/* 284 */     return getSlimeSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 292 */     return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 300 */     return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
/*     */   }
/*     */   
/*     */   protected net.minecraft.item.Item getDropItem()
/*     */   {
/* 305 */     return getSlimeSize() == 1 ? net.minecraft.init.Items.slime_ball : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 313 */     BlockPos blockpos = new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ));
/* 314 */     Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
/*     */     
/* 316 */     if ((this.worldObj.getWorldInfo().getTerrainType() == net.minecraft.world.WorldType.FLAT) && (this.rand.nextInt(4) != 1))
/*     */     {
/* 318 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 322 */     if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL)
/*     */     {
/* 324 */       BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
/*     */       
/* 326 */       if ((biomegenbase == BiomeGenBase.swampland) && (this.posY > 50.0D) && (this.posY < 70.0D) && (this.rand.nextFloat() < 0.5F) && (this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor()) && (this.worldObj.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)))
/*     */       {
/* 328 */         return super.getCanSpawnHere();
/*     */       }
/*     */       
/* 331 */       if ((this.rand.nextInt(10) == 0) && (chunk.getRandomWithSeed(987234911L).nextInt(10) == 0) && (this.posY < 40.0D))
/*     */       {
/* 333 */         return super.getCanSpawnHere();
/*     */       }
/*     */     }
/*     */     
/* 337 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundVolume()
/*     */   {
/* 346 */     return 0.4F * getSlimeSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getVerticalFaceSpeed()
/*     */   {
/* 355 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean makesSoundOnJump()
/*     */   {
/* 363 */     return getSlimeSize() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean makesSoundOnLand()
/*     */   {
/* 371 */     return getSlimeSize() > 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void jump()
/*     */   {
/* 379 */     this.motionY = 0.41999998688697815D;
/* 380 */     this.isAirBorne = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 389 */     int i = this.rand.nextInt(3);
/*     */     
/* 391 */     if ((i < 2) && (this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty()))
/*     */     {
/* 393 */       i++;
/*     */     }
/*     */     
/* 396 */     int j = 1 << i;
/* 397 */     setSlimeSize(j);
/* 398 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   static class AISlimeAttack extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     private int field_179465_b;
/*     */     
/*     */     public AISlimeAttack(EntitySlime p_i45824_1_)
/*     */     {
/* 408 */       this.slime = p_i45824_1_;
/* 409 */       setMutexBits(2);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 414 */       EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
/* 415 */       return entitylivingbase != null;
/*     */     }
/*     */     
/*     */     public void startExecuting()
/*     */     {
/* 420 */       this.field_179465_b = 300;
/* 421 */       super.startExecuting();
/*     */     }
/*     */     
/*     */     public boolean continueExecuting()
/*     */     {
/* 426 */       EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
/* 427 */       return entitylivingbase != null;
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 432 */       this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
/* 433 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.rotationYaw, this.slime.canDamagePlayer());
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISlimeFaceRandom extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     private float field_179459_b;
/*     */     private int field_179460_c;
/*     */     
/*     */     public AISlimeFaceRandom(EntitySlime p_i45820_1_)
/*     */     {
/* 445 */       this.slime = p_i45820_1_;
/* 446 */       setMutexBits(2);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 451 */       return (this.slime.getAttackTarget() == null) && ((this.slime.onGround) || (this.slime.isInWater()) || (this.slime.isInLava()));
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 456 */       if (--this.field_179460_c <= 0)
/*     */       {
/* 458 */         this.field_179460_c = (40 + this.slime.getRNG().nextInt(60));
/* 459 */         this.field_179459_b = this.slime.getRNG().nextInt(360);
/*     */       }
/*     */       
/* 462 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISlimeFloat extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     
/*     */     public AISlimeFloat(EntitySlime p_i45823_1_)
/*     */     {
/* 472 */       this.slime = p_i45823_1_;
/* 473 */       setMutexBits(5);
/* 474 */       ((PathNavigateGround)p_i45823_1_.getNavigator()).setCanSwim(true);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 479 */       return (this.slime.isInWater()) || (this.slime.isInLava());
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 484 */       if (this.slime.getRNG().nextFloat() < 0.8F)
/*     */       {
/* 486 */         this.slime.getJumpHelper().setJumping();
/*     */       }
/*     */       
/* 489 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISlimeHop extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     
/*     */     public AISlimeHop(EntitySlime p_i45822_1_)
/*     */     {
/* 499 */       this.slime = p_i45822_1_;
/* 500 */       setMutexBits(5);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 505 */       return true;
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 510 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class SlimeMoveHelper extends EntityMoveHelper
/*     */   {
/*     */     private float field_179922_g;
/*     */     private int field_179924_h;
/*     */     private EntitySlime slime;
/*     */     private boolean field_179923_j;
/*     */     
/*     */     public SlimeMoveHelper(EntitySlime p_i45821_1_)
/*     */     {
/* 523 */       super();
/* 524 */       this.slime = p_i45821_1_;
/*     */     }
/*     */     
/*     */     public void func_179920_a(float p_179920_1_, boolean p_179920_2_)
/*     */     {
/* 529 */       this.field_179922_g = p_179920_1_;
/* 530 */       this.field_179923_j = p_179920_2_;
/*     */     }
/*     */     
/*     */     public void setSpeed(double speedIn)
/*     */     {
/* 535 */       this.speed = speedIn;
/* 536 */       this.update = true;
/*     */     }
/*     */     
/*     */     public void onUpdateMoveHelper()
/*     */     {
/* 541 */       this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0F);
/* 542 */       this.entity.rotationYawHead = this.entity.rotationYaw;
/* 543 */       this.entity.renderYawOffset = this.entity.rotationYaw;
/*     */       
/* 545 */       if (!this.update)
/*     */       {
/* 547 */         this.entity.setMoveForward(0.0F);
/*     */       }
/*     */       else
/*     */       {
/* 551 */         this.update = false;
/*     */         
/* 553 */         if (this.entity.onGround)
/*     */         {
/* 555 */           this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
/*     */           
/* 557 */           if (this.field_179924_h-- <= 0)
/*     */           {
/* 559 */             this.field_179924_h = this.slime.getJumpDelay();
/*     */             
/* 561 */             if (this.field_179923_j)
/*     */             {
/* 563 */               this.field_179924_h /= 3;
/*     */             }
/*     */             
/* 566 */             this.slime.getJumpHelper().setJumping();
/*     */             
/* 568 */             if (this.slime.makesSoundOnJump())
/*     */             {
/* 570 */               this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 575 */             this.slime.moveStrafing = (this.slime.moveForward = 0.0F);
/* 576 */             this.entity.setAIMoveSpeed(0.0F);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 581 */           this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntitySlime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */