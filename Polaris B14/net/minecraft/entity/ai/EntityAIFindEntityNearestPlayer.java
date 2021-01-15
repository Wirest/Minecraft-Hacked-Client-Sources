/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityAIFindEntityNearestPlayer extends EntityAIBase
/*     */ {
/*  20 */   private static final Logger field_179436_a = ;
/*     */   private EntityLiving field_179434_b;
/*     */   private final Predicate<Entity> field_179435_c;
/*     */   private final EntityAINearestAttackableTarget.Sorter field_179432_d;
/*     */   private EntityLivingBase field_179433_e;
/*     */   
/*     */   public EntityAIFindEntityNearestPlayer(EntityLiving p_i45882_1_)
/*     */   {
/*  28 */     this.field_179434_b = p_i45882_1_;
/*     */     
/*  30 */     if ((p_i45882_1_ instanceof EntityCreature))
/*     */     {
/*  32 */       field_179436_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
/*     */     }
/*     */     
/*  35 */     this.field_179435_c = new Predicate()
/*     */     {
/*     */       public boolean apply(Entity p_apply_1_)
/*     */       {
/*  39 */         if (!(p_apply_1_ instanceof EntityPlayer))
/*     */         {
/*  41 */           return false;
/*     */         }
/*  43 */         if (((EntityPlayer)p_apply_1_).capabilities.disableDamage)
/*     */         {
/*  45 */           return false;
/*     */         }
/*     */         
/*     */ 
/*  49 */         double d0 = EntityAIFindEntityNearestPlayer.this.func_179431_f();
/*     */         
/*  51 */         if (p_apply_1_.isSneaking())
/*     */         {
/*  53 */           d0 *= 0.800000011920929D;
/*     */         }
/*     */         
/*  56 */         if (p_apply_1_.isInvisible())
/*     */         {
/*  58 */           float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
/*     */           
/*  60 */           if (f < 0.1F)
/*     */           {
/*  62 */             f = 0.1F;
/*     */           }
/*     */           
/*  65 */           d0 *= 0.7F * f;
/*     */         }
/*     */         
/*  68 */         return p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.field_179434_b) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.field_179434_b, (EntityLivingBase)p_apply_1_, false, true);
/*     */       }
/*     */       
/*  71 */     };
/*  72 */     this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(p_i45882_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  80 */     double d0 = func_179431_f();
/*  81 */     List<EntityPlayer> list = this.field_179434_b.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.field_179434_b.getEntityBoundingBox().expand(d0, 4.0D, d0), this.field_179435_c);
/*  82 */     java.util.Collections.sort(list, this.field_179432_d);
/*     */     
/*  84 */     if (list.isEmpty())
/*     */     {
/*  86 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  90 */     this.field_179433_e = ((EntityLivingBase)list.get(0));
/*  91 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/* 100 */     EntityLivingBase entitylivingbase = this.field_179434_b.getAttackTarget();
/*     */     
/* 102 */     if (entitylivingbase == null)
/*     */     {
/* 104 */       return false;
/*     */     }
/* 106 */     if (!entitylivingbase.isEntityAlive())
/*     */     {
/* 108 */       return false;
/*     */     }
/* 110 */     if (((entitylivingbase instanceof EntityPlayer)) && (((EntityPlayer)entitylivingbase).capabilities.disableDamage))
/*     */     {
/* 112 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 116 */     Team team = this.field_179434_b.getTeam();
/* 117 */     Team team1 = entitylivingbase.getTeam();
/*     */     
/* 119 */     if ((team != null) && (team1 == team))
/*     */     {
/* 121 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 125 */     double d0 = func_179431_f();
/* 126 */     return this.field_179434_b.getDistanceSqToEntity(entitylivingbase) <= d0 * d0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/* 136 */     this.field_179434_b.setAttackTarget(this.field_179433_e);
/* 137 */     super.startExecuting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/* 145 */     this.field_179434_b.setAttackTarget(null);
/* 146 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   protected double func_179431_f()
/*     */   {
/* 151 */     IAttributeInstance iattributeinstance = this.field_179434_b.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.followRange);
/* 152 */     return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIFindEntityNearestPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */