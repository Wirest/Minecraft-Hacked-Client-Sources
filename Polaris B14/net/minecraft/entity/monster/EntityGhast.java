/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.EntityFlying;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityLargeFireball;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityGhast extends EntityFlying implements IMob
/*     */ {
/*  27 */   private int explosionStrength = 1;
/*     */   
/*     */   public EntityGhast(World worldIn)
/*     */   {
/*  31 */     super(worldIn);
/*  32 */     setSize(4.0F, 4.0F);
/*  33 */     this.isImmuneToFire = true;
/*  34 */     this.experienceValue = 5;
/*  35 */     this.moveHelper = new GhastMoveHelper(this);
/*  36 */     this.tasks.addTask(5, new AIRandomFly(this));
/*  37 */     this.tasks.addTask(7, new AILookAround(this));
/*  38 */     this.tasks.addTask(7, new AIFireballAttack(this));
/*  39 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer(this));
/*     */   }
/*     */   
/*     */   public boolean isAttacking()
/*     */   {
/*  44 */     return this.dataWatcher.getWatchableObjectByte(16) != 0;
/*     */   }
/*     */   
/*     */   public void setAttacking(boolean p_175454_1_)
/*     */   {
/*  49 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)(p_175454_1_ ? 1 : 0)));
/*     */   }
/*     */   
/*     */   public int getFireballStrength()
/*     */   {
/*  54 */     return this.explosionStrength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  62 */     super.onUpdate();
/*     */     
/*  64 */     if ((!this.worldObj.isRemote) && (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL))
/*     */     {
/*  66 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  75 */     if (isEntityInvulnerable(source))
/*     */     {
/*  77 */       return false;
/*     */     }
/*  79 */     if (("fireball".equals(source.getDamageType())) && ((source.getEntity() instanceof EntityPlayer)))
/*     */     {
/*  81 */       super.attackEntityFrom(source, 1000.0F);
/*  82 */       ((EntityPlayer)source.getEntity()).triggerAchievement(net.minecraft.stats.AchievementList.ghast);
/*  83 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  87 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void entityInit()
/*     */   {
/*  93 */     super.entityInit();
/*  94 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  99 */     super.applyEntityAttributes();
/* 100 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/* 101 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 109 */     return "mob.ghast.moan";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 117 */     return "mob.ghast.scream";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 125 */     return "mob.ghast.death";
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 130 */     return Items.gunpowder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 138 */     int i = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
/*     */     
/* 140 */     for (int j = 0; j < i; j++)
/*     */     {
/* 142 */       dropItem(Items.ghast_tear, 1);
/*     */     }
/*     */     
/* 145 */     i = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
/*     */     
/* 147 */     for (int k = 0; k < i; k++)
/*     */     {
/* 149 */       dropItem(Items.gunpowder, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundVolume()
/*     */   {
/* 158 */     return 10.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 166 */     return (this.rand.nextInt(20) == 0) && (super.getCanSpawnHere()) && (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxSpawnedInChunk()
/*     */   {
/* 174 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 182 */     super.writeEntityToNBT(tagCompound);
/* 183 */     tagCompound.setInteger("ExplosionPower", this.explosionStrength);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 191 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 193 */     if (tagCompund.hasKey("ExplosionPower", 99))
/*     */     {
/* 195 */       this.explosionStrength = tagCompund.getInteger("ExplosionPower");
/*     */     }
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 201 */     return 2.6F;
/*     */   }
/*     */   
/*     */   static class AIFireballAttack extends EntityAIBase
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     public int attackTimer;
/*     */     
/*     */     public AIFireballAttack(EntityGhast p_i45837_1_)
/*     */     {
/* 211 */       this.parentEntity = p_i45837_1_;
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 216 */       return this.parentEntity.getAttackTarget() != null;
/*     */     }
/*     */     
/*     */     public void startExecuting()
/*     */     {
/* 221 */       this.attackTimer = 0;
/*     */     }
/*     */     
/*     */     public void resetTask()
/*     */     {
/* 226 */       this.parentEntity.setAttacking(false);
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 231 */       EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
/* 232 */       double d0 = 64.0D;
/*     */       
/* 234 */       if ((entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0) && (this.parentEntity.canEntityBeSeen(entitylivingbase)))
/*     */       {
/* 236 */         World world = this.parentEntity.worldObj;
/* 237 */         this.attackTimer += 1;
/*     */         
/* 239 */         if (this.attackTimer == 10)
/*     */         {
/* 241 */           world.playAuxSFXAtEntity(null, 1007, new BlockPos(this.parentEntity), 0);
/*     */         }
/*     */         
/* 244 */         if (this.attackTimer == 20)
/*     */         {
/* 246 */           double d1 = 4.0D;
/* 247 */           Vec3 vec3 = this.parentEntity.getLook(1.0F);
/* 248 */           double d2 = entitylivingbase.posX - (this.parentEntity.posX + vec3.xCoord * d1);
/* 249 */           double d3 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0F - (0.5D + this.parentEntity.posY + this.parentEntity.height / 2.0F);
/* 250 */           double d4 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3.zCoord * d1);
/* 251 */           world.playAuxSFXAtEntity(null, 1008, new BlockPos(this.parentEntity), 0);
/* 252 */           EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this.parentEntity, d2, d3, d4);
/* 253 */           entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
/* 254 */           entitylargefireball.posX = (this.parentEntity.posX + vec3.xCoord * d1);
/* 255 */           entitylargefireball.posY = (this.parentEntity.posY + this.parentEntity.height / 2.0F + 0.5D);
/* 256 */           entitylargefireball.posZ = (this.parentEntity.posZ + vec3.zCoord * d1);
/* 257 */           world.spawnEntityInWorld(entitylargefireball);
/* 258 */           this.attackTimer = -40;
/*     */         }
/*     */       }
/* 261 */       else if (this.attackTimer > 0)
/*     */       {
/* 263 */         this.attackTimer -= 1;
/*     */       }
/*     */       
/* 266 */       this.parentEntity.setAttacking(this.attackTimer > 10);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AILookAround extends EntityAIBase
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     
/*     */     public AILookAround(EntityGhast p_i45839_1_)
/*     */     {
/* 276 */       this.parentEntity = p_i45839_1_;
/* 277 */       setMutexBits(2);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 282 */       return true;
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 287 */       if (this.parentEntity.getAttackTarget() == null)
/*     */       {
/* 289 */         this.parentEntity.renderYawOffset = (this.parentEntity.rotationYaw = -(float)MathHelper.func_181159_b(this.parentEntity.motionX, this.parentEntity.motionZ) * 180.0F / 3.1415927F);
/*     */       }
/*     */       else
/*     */       {
/* 293 */         EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
/* 294 */         double d0 = 64.0D;
/*     */         
/* 296 */         if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0)
/*     */         {
/* 298 */           double d1 = entitylivingbase.posX - this.parentEntity.posX;
/* 299 */           double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
/* 300 */           this.parentEntity.renderYawOffset = (this.parentEntity.rotationYaw = -(float)MathHelper.func_181159_b(d1, d2) * 180.0F / 3.1415927F);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIRandomFly extends EntityAIBase
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     
/*     */     public AIRandomFly(EntityGhast p_i45836_1_)
/*     */     {
/* 312 */       this.parentEntity = p_i45836_1_;
/* 313 */       setMutexBits(1);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 318 */       EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
/*     */       
/* 320 */       if (!entitymovehelper.isUpdating())
/*     */       {
/* 322 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 326 */       double d0 = entitymovehelper.getX() - this.parentEntity.posX;
/* 327 */       double d1 = entitymovehelper.getY() - this.parentEntity.posY;
/* 328 */       double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
/* 329 */       double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/* 330 */       return (d3 < 1.0D) || (d3 > 3600.0D);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean continueExecuting()
/*     */     {
/* 336 */       return false;
/*     */     }
/*     */     
/*     */     public void startExecuting()
/*     */     {
/* 341 */       Random random = this.parentEntity.getRNG();
/* 342 */       double d0 = this.parentEntity.posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
/* 343 */       double d1 = this.parentEntity.posY + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
/* 344 */       double d2 = this.parentEntity.posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
/* 345 */       this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class GhastMoveHelper extends EntityMoveHelper
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     private int courseChangeCooldown;
/*     */     
/*     */     public GhastMoveHelper(EntityGhast p_i45838_1_)
/*     */     {
/* 356 */       super();
/* 357 */       this.parentEntity = p_i45838_1_;
/*     */     }
/*     */     
/*     */     public void onUpdateMoveHelper()
/*     */     {
/* 362 */       if (this.update)
/*     */       {
/* 364 */         double d0 = this.posX - this.parentEntity.posX;
/* 365 */         double d1 = this.posY - this.parentEntity.posY;
/* 366 */         double d2 = this.posZ - this.parentEntity.posZ;
/* 367 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*     */         
/* 369 */         if (this.courseChangeCooldown-- <= 0)
/*     */         {
/* 371 */           this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
/* 372 */           d3 = MathHelper.sqrt_double(d3);
/*     */           
/* 374 */           if (isNotColliding(this.posX, this.posY, this.posZ, d3))
/*     */           {
/* 376 */             this.parentEntity.motionX += d0 / d3 * 0.1D;
/* 377 */             this.parentEntity.motionY += d1 / d3 * 0.1D;
/* 378 */             this.parentEntity.motionZ += d2 / d3 * 0.1D;
/*     */           }
/*     */           else
/*     */           {
/* 382 */             this.update = false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean isNotColliding(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_)
/*     */     {
/* 390 */       double d0 = (p_179926_1_ - this.parentEntity.posX) / p_179926_7_;
/* 391 */       double d1 = (p_179926_3_ - this.parentEntity.posY) / p_179926_7_;
/* 392 */       double d2 = (p_179926_5_ - this.parentEntity.posZ) / p_179926_7_;
/* 393 */       AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
/*     */       
/* 395 */       for (int i = 1; i < p_179926_7_; i++)
/*     */       {
/* 397 */         axisalignedbb = axisalignedbb.offset(d0, d1, d2);
/*     */         
/* 399 */         if (!this.parentEntity.worldObj.getCollidingBoundingBoxes(this.parentEntity, axisalignedbb).isEmpty())
/*     */         {
/* 401 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 405 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */