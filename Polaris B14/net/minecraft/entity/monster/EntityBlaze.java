/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityBlaze extends EntityMob
/*     */ {
/*  25 */   private float heightOffset = 0.5F;
/*     */   
/*     */   private int heightOffsetUpdateTime;
/*     */   
/*     */ 
/*     */   public EntityBlaze(World worldIn)
/*     */   {
/*  32 */     super(worldIn);
/*  33 */     this.isImmuneToFire = true;
/*  34 */     this.experienceValue = 10;
/*  35 */     this.tasks.addTask(4, new AIFireballAttack(this));
/*  36 */     this.tasks.addTask(5, new net.minecraft.entity.ai.EntityAIMoveTowardsRestriction(this, 1.0D));
/*  37 */     this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
/*  38 */     this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  39 */     this.tasks.addTask(8, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  40 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, true, new Class[0]));
/*  41 */     this.targetTasks.addTask(2, new net.minecraft.entity.ai.EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  46 */     super.applyEntityAttributes();
/*  47 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
/*  48 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*  49 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0D);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  54 */     super.entityInit();
/*  55 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  63 */     return "mob.blaze.breathe";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  71 */     return "mob.blaze.hit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/*  79 */     return "mob.blaze.death";
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/*  84 */     return 15728880;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBrightness(float partialTicks)
/*     */   {
/*  92 */     return 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 101 */     if ((!this.onGround) && (this.motionY < 0.0D))
/*     */     {
/* 103 */       this.motionY *= 0.6D;
/*     */     }
/*     */     
/* 106 */     if (this.worldObj.isRemote)
/*     */     {
/* 108 */       if ((this.rand.nextInt(24) == 0) && (!isSilent()))
/*     */       {
/* 110 */         this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
/*     */       }
/*     */       
/* 113 */       for (int i = 0; i < 2; i++)
/*     */       {
/* 115 */         this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     
/* 119 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/* 124 */     if (isWet())
/*     */     {
/* 126 */       attackEntityFrom(DamageSource.drown, 1.0F);
/*     */     }
/*     */     
/* 129 */     this.heightOffsetUpdateTime -= 1;
/*     */     
/* 131 */     if (this.heightOffsetUpdateTime <= 0)
/*     */     {
/* 133 */       this.heightOffsetUpdateTime = 100;
/* 134 */       this.heightOffset = (0.5F + (float)this.rand.nextGaussian() * 3.0F);
/*     */     }
/*     */     
/* 137 */     EntityLivingBase entitylivingbase = getAttackTarget();
/*     */     
/* 139 */     if ((entitylivingbase != null) && (entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + getEyeHeight() + this.heightOffset))
/*     */     {
/* 141 */       this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
/* 142 */       this.isAirBorne = true;
/*     */     }
/*     */     
/* 145 */     super.updateAITasks();
/*     */   }
/*     */   
/*     */ 
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */   
/*     */ 
/*     */   protected Item getDropItem()
/*     */   {
/* 154 */     return Items.blaze_rod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBurning()
/*     */   {
/* 162 */     return func_70845_n();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 170 */     if (p_70628_1_)
/*     */     {
/* 172 */       int i = this.rand.nextInt(2 + p_70628_2_);
/*     */       
/* 174 */       for (int j = 0; j < i; j++)
/*     */       {
/* 176 */         dropItem(Items.blaze_rod, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_70845_n()
/*     */   {
/* 183 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
/*     */   }
/*     */   
/*     */   public void setOnFire(boolean onFire)
/*     */   {
/* 188 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 190 */     if (onFire)
/*     */     {
/* 192 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     else
/*     */     {
/* 196 */       b0 = (byte)(b0 & 0xFFFFFFFE);
/*     */     }
/*     */     
/* 199 */     this.dataWatcher.updateObject(16, Byte.valueOf(b0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidLightLevel()
/*     */   {
/* 207 */     return true;
/*     */   }
/*     */   
/*     */   static class AIFireballAttack extends EntityAIBase
/*     */   {
/*     */     private EntityBlaze blaze;
/*     */     private int field_179467_b;
/*     */     private int field_179468_c;
/*     */     
/*     */     public AIFireballAttack(EntityBlaze p_i45846_1_)
/*     */     {
/* 218 */       this.blaze = p_i45846_1_;
/* 219 */       setMutexBits(3);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 224 */       EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
/* 225 */       return (entitylivingbase != null) && (entitylivingbase.isEntityAlive());
/*     */     }
/*     */     
/*     */     public void startExecuting()
/*     */     {
/* 230 */       this.field_179467_b = 0;
/*     */     }
/*     */     
/*     */     public void resetTask()
/*     */     {
/* 235 */       this.blaze.setOnFire(false);
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 240 */       this.field_179468_c -= 1;
/* 241 */       EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
/* 242 */       double d0 = this.blaze.getDistanceSqToEntity(entitylivingbase);
/*     */       
/* 244 */       if (d0 < 4.0D)
/*     */       {
/* 246 */         if (this.field_179468_c <= 0)
/*     */         {
/* 248 */           this.field_179468_c = 20;
/* 249 */           this.blaze.attackEntityAsMob(entitylivingbase);
/*     */         }
/*     */         
/* 252 */         this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
/*     */       }
/* 254 */       else if (d0 < 256.0D)
/*     */       {
/* 256 */         double d1 = entitylivingbase.posX - this.blaze.posX;
/* 257 */         double d2 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0F - (this.blaze.posY + this.blaze.height / 2.0F);
/* 258 */         double d3 = entitylivingbase.posZ - this.blaze.posZ;
/*     */         
/* 260 */         if (this.field_179468_c <= 0)
/*     */         {
/* 262 */           this.field_179467_b += 1;
/*     */           
/* 264 */           if (this.field_179467_b == 1)
/*     */           {
/* 266 */             this.field_179468_c = 60;
/* 267 */             this.blaze.setOnFire(true);
/*     */           }
/* 269 */           else if (this.field_179467_b <= 4)
/*     */           {
/* 271 */             this.field_179468_c = 6;
/*     */           }
/*     */           else
/*     */           {
/* 275 */             this.field_179468_c = 100;
/* 276 */             this.field_179467_b = 0;
/* 277 */             this.blaze.setOnFire(false);
/*     */           }
/*     */           
/* 280 */           if (this.field_179467_b > 1)
/*     */           {
/* 282 */             float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5F;
/* 283 */             this.blaze.worldObj.playAuxSFXAtEntity(null, 1009, new net.minecraft.util.BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
/*     */             
/* 285 */             for (int i = 0; i < 1; i++)
/*     */             {
/* 287 */               EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.blaze.worldObj, this.blaze, d1 + this.blaze.getRNG().nextGaussian() * f, d2, d3 + this.blaze.getRNG().nextGaussian() * f);
/* 288 */               entitysmallfireball.posY = (this.blaze.posY + this.blaze.height / 2.0F + 0.5D);
/* 289 */               this.blaze.worldObj.spawnEntityInWorld(entitysmallfireball);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 294 */         this.blaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
/*     */       }
/*     */       else
/*     */       {
/* 298 */         this.blaze.getNavigator().clearPathEntity();
/* 299 */         this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
/*     */       }
/*     */       
/* 302 */       super.updateTask();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */