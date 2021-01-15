/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIAttackOnCollide
/*     */   extends EntityAIBase
/*     */ {
/*     */   World worldObj;
/*     */   protected EntityCreature attacker;
/*     */   int attackTick;
/*     */   double speedTowardsTarget;
/*     */   boolean longMemory;
/*     */   PathEntity entityPathEntity;
/*     */   Class<? extends Entity> classTarget;
/*     */   private int delayCounter;
/*     */   private double targetX;
/*     */   private double targetY;
/*     */   private double targetZ;
/*     */   
/*     */   public EntityAIAttackOnCollide(EntityCreature creature, Class<? extends Entity> targetClass, double speedIn, boolean useLongMemory)
/*     */   {
/*  38 */     this(creature, speedIn, useLongMemory);
/*  39 */     this.classTarget = targetClass;
/*     */   }
/*     */   
/*     */   public EntityAIAttackOnCollide(EntityCreature creature, double speedIn, boolean useLongMemory)
/*     */   {
/*  44 */     this.attacker = creature;
/*  45 */     this.worldObj = creature.worldObj;
/*  46 */     this.speedTowardsTarget = speedIn;
/*  47 */     this.longMemory = useLongMemory;
/*  48 */     setMutexBits(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  56 */     EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
/*     */     
/*  58 */     if (entitylivingbase == null)
/*     */     {
/*  60 */       return false;
/*     */     }
/*  62 */     if (!entitylivingbase.isEntityAlive())
/*     */     {
/*  64 */       return false;
/*     */     }
/*  66 */     if ((this.classTarget != null) && (!this.classTarget.isAssignableFrom(entitylivingbase.getClass())))
/*     */     {
/*  68 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  72 */     this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
/*  73 */     return this.entityPathEntity != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  82 */     EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
/*  83 */     return !this.longMemory ? true : this.attacker.getNavigator().noPath() ? false : !entitylivingbase.isEntityAlive() ? false : entitylivingbase == null ? false : this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  91 */     this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
/*  92 */     this.delayCounter = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/* 100 */     this.attacker.getNavigator().clearPathEntity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 108 */     EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
/* 109 */     this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
/* 110 */     double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
/* 111 */     double d1 = func_179512_a(entitylivingbase);
/* 112 */     this.delayCounter -= 1;
/*     */     
/* 114 */     if (((this.longMemory) || (this.attacker.getEntitySenses().canSee(entitylivingbase))) && (this.delayCounter <= 0) && (((this.targetX == 0.0D) && (this.targetY == 0.0D) && (this.targetZ == 0.0D)) || (entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D) || (this.attacker.getRNG().nextFloat() < 0.05F)))
/*     */     {
/* 116 */       this.targetX = entitylivingbase.posX;
/* 117 */       this.targetY = entitylivingbase.getEntityBoundingBox().minY;
/* 118 */       this.targetZ = entitylivingbase.posZ;
/* 119 */       this.delayCounter = (4 + this.attacker.getRNG().nextInt(7));
/*     */       
/* 121 */       if (d0 > 1024.0D)
/*     */       {
/* 123 */         this.delayCounter += 10;
/*     */       }
/* 125 */       else if (d0 > 256.0D)
/*     */       {
/* 127 */         this.delayCounter += 5;
/*     */       }
/*     */       
/* 130 */       if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
/*     */       {
/* 132 */         this.delayCounter += 15;
/*     */       }
/*     */     }
/*     */     
/* 136 */     this.attackTick = Math.max(this.attackTick - 1, 0);
/*     */     
/* 138 */     if ((d0 <= d1) && (this.attackTick <= 0))
/*     */     {
/* 140 */       this.attackTick = 20;
/*     */       
/* 142 */       if (this.attacker.getHeldItem() != null)
/*     */       {
/* 144 */         this.attacker.swingItem();
/*     */       }
/*     */       
/* 147 */       this.attacker.attackEntityAsMob(entitylivingbase);
/*     */     }
/*     */   }
/*     */   
/*     */   protected double func_179512_a(EntityLivingBase attackTarget)
/*     */   {
/* 153 */     return this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIAttackOnCollide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */