/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget
/*     */ {
/*     */   protected final Class<T> targetClass;
/*     */   private final int targetChance;
/*     */   protected final Sorter theNearestAttackableTargetSorter;
/*     */   protected Predicate<? super T> targetEntitySelector;
/*     */   protected EntityLivingBase targetEntity;
/*     */   
/*     */   public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight)
/*     */   {
/*  26 */     this(creature, classTarget, checkSight, false);
/*     */   }
/*     */   
/*     */   public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby)
/*     */   {
/*  31 */     this(creature, classTarget, 10, checkSight, onlyNearby, null);
/*     */   }
/*     */   
/*     */   public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, final Predicate<? super T> targetSelector)
/*     */   {
/*  36 */     super(creature, checkSight, onlyNearby);
/*  37 */     this.targetClass = classTarget;
/*  38 */     this.targetChance = chance;
/*  39 */     this.theNearestAttackableTargetSorter = new Sorter(creature);
/*  40 */     setMutexBits(1);
/*  41 */     this.targetEntitySelector = new Predicate()
/*     */     {
/*     */       public boolean apply(T p_apply_1_)
/*     */       {
/*  45 */         if ((targetSelector != null) && (!targetSelector.apply(p_apply_1_)))
/*     */         {
/*  47 */           return false;
/*     */         }
/*     */         
/*     */ 
/*  51 */         if ((p_apply_1_ instanceof EntityPlayer))
/*     */         {
/*  53 */           double d0 = EntityAINearestAttackableTarget.this.getTargetDistance();
/*     */           
/*  55 */           if (p_apply_1_.isSneaking())
/*     */           {
/*  57 */             d0 *= 0.800000011920929D;
/*     */           }
/*     */           
/*  60 */           if (p_apply_1_.isInvisible())
/*     */           {
/*  62 */             float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
/*     */             
/*  64 */             if (f < 0.1F)
/*     */             {
/*  66 */               f = 0.1F;
/*     */             }
/*     */             
/*  69 */             d0 *= 0.7F * f;
/*     */           }
/*     */           
/*  72 */           if (p_apply_1_.getDistanceToEntity(EntityAINearestAttackableTarget.this.taskOwner) > d0)
/*     */           {
/*  74 */             return false;
/*     */           }
/*     */         }
/*     */         
/*  78 */         return EntityAINearestAttackableTarget.this.isSuitableTarget(p_apply_1_, false);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  89 */     if ((this.targetChance > 0) && (this.taskOwner.getRNG().nextInt(this.targetChance) != 0))
/*     */     {
/*  91 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  95 */     double d0 = getTargetDistance();
/*  96 */     List<T> list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), Predicates.and(this.targetEntitySelector, EntitySelectors.NOT_SPECTATING));
/*  97 */     Collections.sort(list, this.theNearestAttackableTargetSorter);
/*     */     
/*  99 */     if (list.isEmpty())
/*     */     {
/* 101 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 105 */     this.targetEntity = ((EntityLivingBase)list.get(0));
/* 106 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/* 116 */     this.taskOwner.setAttackTarget(this.targetEntity);
/* 117 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   public static class Sorter implements java.util.Comparator<Entity>
/*     */   {
/*     */     private final Entity theEntity;
/*     */     
/*     */     public Sorter(Entity theEntityIn)
/*     */     {
/* 126 */       this.theEntity = theEntityIn;
/*     */     }
/*     */     
/*     */     public int compare(Entity p_compare_1_, Entity p_compare_2_)
/*     */     {
/* 131 */       double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
/* 132 */       double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
/* 133 */       return d0 > d1 ? 1 : d0 < d1 ? -1 : 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAINearestAttackableTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */