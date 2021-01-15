/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAILeapAtTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySpider extends EntityMob
/*     */ {
/*     */   public EntitySpider(World worldIn)
/*     */   {
/*  34 */     super(worldIn);
/*  35 */     setSize(1.4F, 0.9F);
/*  36 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  37 */     this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
/*  38 */     this.tasks.addTask(4, new AISpiderAttack(this, EntityPlayer.class));
/*  39 */     this.tasks.addTask(4, new AISpiderAttack(this, EntityIronGolem.class));
/*  40 */     this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
/*  41 */     this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  42 */     this.tasks.addTask(6, new EntityAILookIdle(this));
/*  43 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, false, new Class[0]));
/*  44 */     this.targetTasks.addTask(2, new AISpiderTarget(this, EntityPlayer.class));
/*  45 */     this.targetTasks.addTask(3, new AISpiderTarget(this, EntityIronGolem.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getMountedYOffset()
/*     */   {
/*  53 */     return this.height * 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PathNavigate getNewNavigator(World worldIn)
/*     */   {
/*  61 */     return new net.minecraft.pathfinding.PathNavigateClimber(this, worldIn);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  66 */     super.entityInit();
/*  67 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  75 */     super.onUpdate();
/*     */     
/*  77 */     if (!this.worldObj.isRemote)
/*     */     {
/*  79 */       setBesideClimbableBlock(this.isCollidedHorizontally);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  85 */     super.applyEntityAttributes();
/*  86 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
/*  87 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  95 */     return "mob.spider.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 103 */     return "mob.spider.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 111 */     return "mob.spider.death";
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 116 */     playSound("mob.spider.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 121 */     return Items.string;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 129 */     super.dropFewItems(p_70628_1_, p_70628_2_);
/*     */     
/* 131 */     if ((p_70628_1_) && ((this.rand.nextInt(3) == 0) || (this.rand.nextInt(1 + p_70628_2_) > 0)))
/*     */     {
/* 133 */       dropItem(Items.spider_eye, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOnLadder()
/*     */   {
/* 142 */     return isBesideClimbableBlock();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInWeb() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumCreatureAttribute getCreatureAttribute()
/*     */   {
/* 157 */     return EnumCreatureAttribute.ARTHROPOD;
/*     */   }
/*     */   
/*     */   public boolean isPotionApplicable(PotionEffect potioneffectIn)
/*     */   {
/* 162 */     return potioneffectIn.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(potioneffectIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBesideClimbableBlock()
/*     */   {
/* 171 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBesideClimbableBlock(boolean p_70839_1_)
/*     */   {
/* 180 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 182 */     if (p_70839_1_)
/*     */     {
/* 184 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     else
/*     */     {
/* 188 */       b0 = (byte)(b0 & 0xFFFFFFFE);
/*     */     }
/*     */     
/* 191 */     this.dataWatcher.updateObject(16, Byte.valueOf(b0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 200 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*     */     
/* 202 */     if (this.worldObj.rand.nextInt(100) == 0)
/*     */     {
/* 204 */       EntitySkeleton entityskeleton = new EntitySkeleton(this.worldObj);
/* 205 */       entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 206 */       entityskeleton.onInitialSpawn(difficulty, null);
/* 207 */       this.worldObj.spawnEntityInWorld(entityskeleton);
/* 208 */       entityskeleton.mountEntity(this);
/*     */     }
/*     */     
/* 211 */     if (livingdata == null)
/*     */     {
/* 213 */       livingdata = new GroupData();
/*     */       
/* 215 */       if ((this.worldObj.getDifficulty() == net.minecraft.world.EnumDifficulty.HARD) && (this.worldObj.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty()))
/*     */       {
/* 217 */         ((GroupData)livingdata).func_111104_a(this.worldObj.rand);
/*     */       }
/*     */     }
/*     */     
/* 221 */     if ((livingdata instanceof GroupData))
/*     */     {
/* 223 */       int i = ((GroupData)livingdata).potionEffectId;
/*     */       
/* 225 */       if ((i > 0) && (Potion.potionTypes[i] != null))
/*     */       {
/* 227 */         addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
/*     */       }
/*     */     }
/*     */     
/* 231 */     return livingdata;
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 236 */     return 0.65F;
/*     */   }
/*     */   
/*     */   static class AISpiderAttack extends EntityAIAttackOnCollide
/*     */   {
/*     */     public AISpiderAttack(EntitySpider p_i45819_1_, Class<? extends Entity> targetClass)
/*     */     {
/* 243 */       super(targetClass, 1.0D, true);
/*     */     }
/*     */     
/*     */     public boolean continueExecuting()
/*     */     {
/* 248 */       float f = this.attacker.getBrightness(1.0F);
/*     */       
/* 250 */       if ((f >= 0.5F) && (this.attacker.getRNG().nextInt(100) == 0))
/*     */       {
/* 252 */         this.attacker.setAttackTarget(null);
/* 253 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 257 */       return super.continueExecuting();
/*     */     }
/*     */     
/*     */ 
/*     */     protected double func_179512_a(EntityLivingBase attackTarget)
/*     */     {
/* 263 */       return 4.0F + attackTarget.width;
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget
/*     */   {
/*     */     public AISpiderTarget(EntitySpider p_i45818_1_, Class<T> classTarget)
/*     */     {
/* 271 */       super(classTarget, true);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 276 */       float f = this.taskOwner.getBrightness(1.0F);
/* 277 */       return f >= 0.5F ? false : super.shouldExecute();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GroupData implements IEntityLivingData
/*     */   {
/*     */     public int potionEffectId;
/*     */     
/*     */     public void func_111104_a(Random rand)
/*     */     {
/* 287 */       int i = rand.nextInt(5);
/*     */       
/* 289 */       if (i <= 1)
/*     */       {
/* 291 */         this.potionEffectId = Potion.moveSpeed.id;
/*     */       }
/* 293 */       else if (i <= 2)
/*     */       {
/* 295 */         this.potionEffectId = Potion.damageBoost.id;
/*     */       }
/* 297 */       else if (i <= 3)
/*     */       {
/* 299 */         this.potionEffectId = Potion.regeneration.id;
/*     */       }
/* 301 */       else if (i <= 4)
/*     */       {
/* 303 */         this.potionEffectId = Potion.invisibility.id;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntitySpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */