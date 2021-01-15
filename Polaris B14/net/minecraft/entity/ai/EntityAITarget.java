/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityOwnable;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EntityAITarget
/*     */   extends EntityAIBase
/*     */ {
/*     */   protected final EntityCreature taskOwner;
/*     */   protected boolean shouldCheckSight;
/*     */   private boolean nearbyOnly;
/*     */   private int targetSearchStatus;
/*     */   private int targetSearchDelay;
/*     */   private int targetUnseenTicks;
/*     */   
/*     */   public EntityAITarget(EntityCreature creature, boolean checkSight)
/*     */   {
/*  50 */     this(creature, checkSight, false);
/*     */   }
/*     */   
/*     */   public EntityAITarget(EntityCreature creature, boolean checkSight, boolean onlyNearby)
/*     */   {
/*  55 */     this.taskOwner = creature;
/*  56 */     this.shouldCheckSight = checkSight;
/*  57 */     this.nearbyOnly = onlyNearby;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  65 */     EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
/*     */     
/*  67 */     if (entitylivingbase == null)
/*     */     {
/*  69 */       return false;
/*     */     }
/*  71 */     if (!entitylivingbase.isEntityAlive())
/*     */     {
/*  73 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  77 */     Team team = this.taskOwner.getTeam();
/*  78 */     Team team1 = entitylivingbase.getTeam();
/*     */     
/*  80 */     if ((team != null) && (team1 == team))
/*     */     {
/*  82 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  86 */     double d0 = getTargetDistance();
/*     */     
/*  88 */     if (this.taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
/*     */     {
/*  90 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  94 */     if (this.shouldCheckSight)
/*     */     {
/*  96 */       if (this.taskOwner.getEntitySenses().canSee(entitylivingbase))
/*     */       {
/*  98 */         this.targetUnseenTicks = 0;
/*     */       }
/* 100 */       else if (++this.targetUnseenTicks > 60)
/*     */       {
/* 102 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 106 */     return (!(entitylivingbase instanceof EntityPlayer)) || (!((EntityPlayer)entitylivingbase).capabilities.disableDamage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected double getTargetDistance()
/*     */   {
/* 114 */     IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
/* 115 */     return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/* 123 */     this.targetSearchStatus = 0;
/* 124 */     this.targetSearchDelay = 0;
/* 125 */     this.targetUnseenTicks = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/* 133 */     this.taskOwner.setAttackTarget(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isSuitableTarget(EntityLiving attacker, EntityLivingBase target, boolean includeInvincibles, boolean checkSight)
/*     */   {
/* 141 */     if (target == null)
/*     */     {
/* 143 */       return false;
/*     */     }
/* 145 */     if (target == attacker)
/*     */     {
/* 147 */       return false;
/*     */     }
/* 149 */     if (!target.isEntityAlive())
/*     */     {
/* 151 */       return false;
/*     */     }
/* 153 */     if (!attacker.canAttackClass(target.getClass()))
/*     */     {
/* 155 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 159 */     Team team = attacker.getTeam();
/* 160 */     Team team1 = target.getTeam();
/*     */     
/* 162 */     if ((team != null) && (team1 == team))
/*     */     {
/* 164 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 168 */     if (((attacker instanceof IEntityOwnable)) && (StringUtils.isNotEmpty(((IEntityOwnable)attacker).getOwnerId())))
/*     */     {
/* 170 */       if (((target instanceof IEntityOwnable)) && (((IEntityOwnable)attacker).getOwnerId().equals(((IEntityOwnable)target).getOwnerId())))
/*     */       {
/* 172 */         return false;
/*     */       }
/*     */       
/* 175 */       if (target == ((IEntityOwnable)attacker).getOwner())
/*     */       {
/* 177 */         return false;
/*     */       }
/*     */     }
/* 180 */     else if (((target instanceof EntityPlayer)) && (!includeInvincibles) && (((EntityPlayer)target).capabilities.disableDamage))
/*     */     {
/* 182 */       return false;
/*     */     }
/*     */     
/* 185 */     return (!checkSight) || (attacker.getEntitySenses().canSee(target));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isSuitableTarget(EntityLivingBase target, boolean includeInvincibles)
/*     */   {
/* 196 */     if (!isSuitableTarget(this.taskOwner, target, includeInvincibles, this.shouldCheckSight))
/*     */     {
/* 198 */       return false;
/*     */     }
/* 200 */     if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(target)))
/*     */     {
/* 202 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 206 */     if (this.nearbyOnly)
/*     */     {
/* 208 */       if (--this.targetSearchDelay <= 0)
/*     */       {
/* 210 */         this.targetSearchStatus = 0;
/*     */       }
/*     */       
/* 213 */       if (this.targetSearchStatus == 0)
/*     */       {
/* 215 */         this.targetSearchStatus = (canEasilyReach(target) ? 1 : 2);
/*     */       }
/*     */       
/* 218 */       if (this.targetSearchStatus == 2)
/*     */       {
/* 220 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 224 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean canEasilyReach(EntityLivingBase p_75295_1_)
/*     */   {
/* 233 */     this.targetSearchDelay = (10 + this.taskOwner.getRNG().nextInt(5));
/* 234 */     PathEntity pathentity = this.taskOwner.getNavigator().getPathToEntityLiving(p_75295_1_);
/*     */     
/* 236 */     if (pathentity == null)
/*     */     {
/* 238 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 242 */     PathPoint pathpoint = pathentity.getFinalPathPoint();
/*     */     
/* 244 */     if (pathpoint == null)
/*     */     {
/* 246 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 250 */     int i = pathpoint.xCoord - MathHelper.floor_double(p_75295_1_.posX);
/* 251 */     int j = pathpoint.zCoord - MathHelper.floor_double(p_75295_1_.posZ);
/* 252 */     return i * i + j * j <= 2.25D;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAITarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */