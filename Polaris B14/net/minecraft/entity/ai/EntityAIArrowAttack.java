/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIArrowAttack
/*     */   extends EntityAIBase
/*     */ {
/*     */   private final EntityLiving entityHost;
/*     */   private final IRangedAttackMob rangedAttackEntityHost;
/*     */   private EntityLivingBase attackTarget;
/*     */   private int rangedAttackTime;
/*     */   private double entityMoveSpeed;
/*     */   private int field_75318_f;
/*     */   private int field_96561_g;
/*     */   private int maxRangedAttackTime;
/*     */   private float field_96562_i;
/*     */   private float maxAttackDistance;
/*     */   
/*     */   public EntityAIArrowAttack(IRangedAttackMob attacker, double movespeed, int p_i1649_4_, float p_i1649_5_)
/*     */   {
/*  37 */     this(attacker, movespeed, p_i1649_4_, p_i1649_4_, p_i1649_5_);
/*     */   }
/*     */   
/*     */   public EntityAIArrowAttack(IRangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn)
/*     */   {
/*  42 */     this.rangedAttackTime = -1;
/*     */     
/*  44 */     if (!(attacker instanceof EntityLivingBase))
/*     */     {
/*  46 */       throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
/*     */     }
/*     */     
/*     */ 
/*  50 */     this.rangedAttackEntityHost = attacker;
/*  51 */     this.entityHost = ((EntityLiving)attacker);
/*  52 */     this.entityMoveSpeed = movespeed;
/*  53 */     this.field_96561_g = p_i1650_4_;
/*  54 */     this.maxRangedAttackTime = maxAttackTime;
/*  55 */     this.field_96562_i = maxAttackDistanceIn;
/*  56 */     this.maxAttackDistance = (maxAttackDistanceIn * maxAttackDistanceIn);
/*  57 */     setMutexBits(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  66 */     EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
/*     */     
/*  68 */     if (entitylivingbase == null)
/*     */     {
/*  70 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  74 */     this.attackTarget = entitylivingbase;
/*  75 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  84 */     return (shouldExecute()) || (!this.entityHost.getNavigator().noPath());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  92 */     this.attackTarget = null;
/*  93 */     this.field_75318_f = 0;
/*  94 */     this.rangedAttackTime = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 102 */     double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
/* 103 */     boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
/*     */     
/* 105 */     if (flag)
/*     */     {
/* 107 */       this.field_75318_f += 1;
/*     */     }
/*     */     else
/*     */     {
/* 111 */       this.field_75318_f = 0;
/*     */     }
/*     */     
/* 114 */     if ((d0 <= this.maxAttackDistance) && (this.field_75318_f >= 20))
/*     */     {
/* 116 */       this.entityHost.getNavigator().clearPathEntity();
/*     */     }
/*     */     else
/*     */     {
/* 120 */       this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
/*     */     }
/*     */     
/* 123 */     this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
/*     */     
/* 125 */     if (--this.rangedAttackTime == 0)
/*     */     {
/* 127 */       if ((d0 > this.maxAttackDistance) || (!flag))
/*     */       {
/* 129 */         return;
/*     */       }
/*     */       
/* 132 */       float f = MathHelper.sqrt_double(d0) / this.field_96562_i;
/* 133 */       float lvt_5_1_ = MathHelper.clamp_float(f, 0.1F, 1.0F);
/* 134 */       this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
/* 135 */       this.rangedAttackTime = MathHelper.floor_float(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
/*     */     }
/* 137 */     else if (this.rangedAttackTime < 0)
/*     */     {
/* 139 */       float f2 = MathHelper.sqrt_double(d0) / this.field_96562_i;
/* 140 */       this.rangedAttackTime = MathHelper.floor_float(f2 * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIArrowAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */