/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityEndermite extends EntityMob
/*     */ {
/*  22 */   private int lifetime = 0;
/*  23 */   private boolean playerSpawned = false;
/*     */   
/*     */   public EntityEndermite(World worldIn)
/*     */   {
/*  27 */     super(worldIn);
/*  28 */     this.experienceValue = 3;
/*  29 */     setSize(0.4F, 0.3F);
/*  30 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  31 */     this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
/*  32 */     this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
/*  33 */     this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  34 */     this.tasks.addTask(8, new EntityAILookIdle(this));
/*  35 */     this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
/*  36 */     this.targetTasks.addTask(2, new net.minecraft.entity.ai.EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/*  41 */     return 0.1F;
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  46 */     super.applyEntityAttributes();
/*  47 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*  48 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*  49 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  58 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  66 */     return "mob.silverfish.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  74 */     return "mob.silverfish.hit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/*  82 */     return "mob.silverfish.kill";
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/*  87 */     playSound("mob.silverfish.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected net.minecraft.item.Item getDropItem()
/*     */   {
/*  92 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 100 */     super.readEntityFromNBT(tagCompund);
/* 101 */     this.lifetime = tagCompund.getInteger("Lifetime");
/* 102 */     this.playerSpawned = tagCompund.getBoolean("PlayerSpawned");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 110 */     super.writeEntityToNBT(tagCompound);
/* 111 */     tagCompound.setInteger("Lifetime", this.lifetime);
/* 112 */     tagCompound.setBoolean("PlayerSpawned", this.playerSpawned);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 120 */     this.renderYawOffset = this.rotationYaw;
/* 121 */     super.onUpdate();
/*     */   }
/*     */   
/*     */   public boolean isSpawnedByPlayer()
/*     */   {
/* 126 */     return this.playerSpawned;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawnedByPlayer(boolean spawnedByPlayer)
/*     */   {
/* 134 */     this.playerSpawned = spawnedByPlayer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 143 */     super.onLivingUpdate();
/*     */     
/* 145 */     if (this.worldObj.isRemote)
/*     */     {
/* 147 */       for (int i = 0; i < 2; i++)
/*     */       {
/* 149 */         this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 154 */       if (!isNoDespawnRequired())
/*     */       {
/* 156 */         this.lifetime += 1;
/*     */       }
/*     */       
/* 159 */       if (this.lifetime >= 2400)
/*     */       {
/* 161 */         setDead();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidLightLevel()
/*     */   {
/* 171 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 179 */     if (super.getCanSpawnHere())
/*     */     {
/* 181 */       EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
/* 182 */       return entityplayer == null;
/*     */     }
/*     */     
/*     */ 
/* 186 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumCreatureAttribute getCreatureAttribute()
/*     */   {
/* 195 */     return EnumCreatureAttribute.ARTHROPOD;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityEndermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */