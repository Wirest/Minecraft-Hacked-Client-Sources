/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityPigZombie extends EntityZombie
/*     */ {
/*  24 */   private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
/*  25 */   private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, 0).setSaved(false);
/*     */   
/*     */   private int angerLevel;
/*     */   
/*     */   private int randomSoundDelay;
/*     */   
/*     */   private UUID angerTargetUUID;
/*     */   
/*     */ 
/*     */   public EntityPigZombie(World worldIn)
/*     */   {
/*  36 */     super(worldIn);
/*  37 */     this.isImmuneToFire = true;
/*     */   }
/*     */   
/*     */   public void setRevengeTarget(EntityLivingBase livingBase)
/*     */   {
/*  42 */     super.setRevengeTarget(livingBase);
/*     */     
/*  44 */     if (livingBase != null)
/*     */     {
/*  46 */       this.angerTargetUUID = livingBase.getUniqueID();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyEntityAI()
/*     */   {
/*  52 */     this.targetTasks.addTask(1, new AIHurtByAggressor(this));
/*  53 */     this.targetTasks.addTask(2, new AITargetAggressor(this));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  58 */     super.applyEntityAttributes();
/*  59 */     getEntityAttribute(reinforcementChance).setBaseValue(0.0D);
/*  60 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*  61 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  69 */     super.onUpdate();
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/*  74 */     IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/*     */     
/*  76 */     if (isAngry())
/*     */     {
/*  78 */       if ((!isChild()) && (!iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)))
/*     */       {
/*  80 */         iattributeinstance.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
/*     */       }
/*     */       
/*  83 */       this.angerLevel -= 1;
/*     */     }
/*  85 */     else if (iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
/*     */     {
/*  87 */       iattributeinstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
/*     */     }
/*     */     
/*  90 */     if ((this.randomSoundDelay > 0) && (--this.randomSoundDelay == 0))
/*     */     {
/*  92 */       playSound("mob.zombiepig.zpigangry", getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
/*     */     }
/*     */     
/*  95 */     if ((this.angerLevel > 0) && (this.angerTargetUUID != null) && (getAITarget() == null))
/*     */     {
/*  97 */       EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
/*  98 */       setRevengeTarget(entityplayer);
/*  99 */       this.attackingPlayer = entityplayer;
/* 100 */       this.recentlyHit = getRevengeTimer();
/*     */     }
/*     */     
/* 103 */     super.updateAITasks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 111 */     return this.worldObj.getDifficulty() != net.minecraft.world.EnumDifficulty.PEACEFUL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNotColliding()
/*     */   {
/* 119 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), this)) && (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) && (!this.worldObj.isAnyLiquid(getEntityBoundingBox()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 127 */     super.writeEntityToNBT(tagCompound);
/* 128 */     tagCompound.setShort("Anger", (short)this.angerLevel);
/*     */     
/* 130 */     if (this.angerTargetUUID != null)
/*     */     {
/* 132 */       tagCompound.setString("HurtBy", this.angerTargetUUID.toString());
/*     */     }
/*     */     else
/*     */     {
/* 136 */       tagCompound.setString("HurtBy", "");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 145 */     super.readEntityFromNBT(tagCompund);
/* 146 */     this.angerLevel = tagCompund.getShort("Anger");
/* 147 */     String s = tagCompund.getString("HurtBy");
/*     */     
/* 149 */     if (s.length() > 0)
/*     */     {
/* 151 */       this.angerTargetUUID = UUID.fromString(s);
/* 152 */       EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
/* 153 */       setRevengeTarget(entityplayer);
/*     */       
/* 155 */       if (entityplayer != null)
/*     */       {
/* 157 */         this.attackingPlayer = entityplayer;
/* 158 */         this.recentlyHit = getRevengeTimer();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 168 */     if (isEntityInvulnerable(source))
/*     */     {
/* 170 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 174 */     Entity entity = source.getEntity();
/*     */     
/* 176 */     if ((entity instanceof EntityPlayer))
/*     */     {
/* 178 */       becomeAngryAt(entity);
/*     */     }
/*     */     
/* 181 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void becomeAngryAt(Entity p_70835_1_)
/*     */   {
/* 190 */     this.angerLevel = (400 + this.rand.nextInt(400));
/* 191 */     this.randomSoundDelay = this.rand.nextInt(40);
/*     */     
/* 193 */     if ((p_70835_1_ instanceof EntityLivingBase))
/*     */     {
/* 195 */       setRevengeTarget((EntityLivingBase)p_70835_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAngry()
/*     */   {
/* 201 */     return this.angerLevel > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 209 */     return "mob.zombiepig.zpig";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 217 */     return "mob.zombiepig.zpighurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 225 */     return "mob.zombiepig.zpigdeath";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 233 */     int i = this.rand.nextInt(2 + p_70628_2_);
/*     */     
/* 235 */     for (int j = 0; j < i; j++)
/*     */     {
/* 237 */       dropItem(Items.rotten_flesh, 1);
/*     */     }
/*     */     
/* 240 */     i = this.rand.nextInt(2 + p_70628_2_);
/*     */     
/* 242 */     for (int k = 0; k < i; k++)
/*     */     {
/* 244 */       dropItem(Items.gold_nugget, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interact(EntityPlayer player)
/*     */   {
/* 253 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addRandomDrop()
/*     */   {
/* 261 */     dropItem(Items.gold_ingot, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
/*     */   {
/* 269 */     setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(Items.golden_sword));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 278 */     super.onInitialSpawn(difficulty, livingdata);
/* 279 */     setVillager(false);
/* 280 */     return livingdata;
/*     */   }
/*     */   
/*     */   static class AIHurtByAggressor extends EntityAIHurtByTarget
/*     */   {
/*     */     public AIHurtByAggressor(EntityPigZombie p_i45828_1_)
/*     */     {
/* 287 */       super(true, new Class[0]);
/*     */     }
/*     */     
/*     */     protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
/*     */     {
/* 292 */       super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
/*     */       
/* 294 */       if ((creatureIn instanceof EntityPigZombie))
/*     */       {
/* 296 */         ((EntityPigZombie)creatureIn).becomeAngryAt(entityLivingBaseIn);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
/*     */   {
/*     */     public AITargetAggressor(EntityPigZombie p_i45829_1_)
/*     */     {
/* 305 */       super(EntityPlayer.class, true);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 310 */       return (((EntityPigZombie)this.taskOwner).isAngry()) && (super.shouldExecute());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityPigZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */