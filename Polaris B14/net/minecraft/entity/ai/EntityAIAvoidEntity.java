/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIAvoidEntity<T extends Entity>
/*     */   extends EntityAIBase
/*     */ {
/*     */   private final Predicate<Entity> canBeSeenSelector;
/*     */   protected EntityCreature theEntity;
/*     */   private double farSpeed;
/*     */   private double nearSpeed;
/*     */   protected T closestLivingEntity;
/*     */   private float avoidDistance;
/*     */   private PathEntity entityPathEntity;
/*     */   private PathNavigate entityPathNavigate;
/*     */   private Class<T> field_181064_i;
/*     */   private Predicate<? super T> avoidTargetSelector;
/*     */   
/*     */   public EntityAIAvoidEntity(EntityCreature p_i46404_1_, Class<T> p_i46404_2_, float p_i46404_3_, double p_i46404_4_, double p_i46404_6_)
/*     */   {
/*  34 */     this(p_i46404_1_, p_i46404_2_, Predicates.alwaysTrue(), p_i46404_3_, p_i46404_4_, p_i46404_6_);
/*     */   }
/*     */   
/*     */   public EntityAIAvoidEntity(EntityCreature p_i46405_1_, Class<T> p_i46405_2_, Predicate<? super T> p_i46405_3_, float p_i46405_4_, double p_i46405_5_, double p_i46405_7_)
/*     */   {
/*  39 */     this.canBeSeenSelector = new Predicate()
/*     */     {
/*     */       public boolean apply(Entity p_apply_1_)
/*     */       {
/*  43 */         return (p_apply_1_.isEntityAlive()) && (EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(p_apply_1_));
/*     */       }
/*  45 */     };
/*  46 */     this.theEntity = p_i46405_1_;
/*  47 */     this.field_181064_i = p_i46405_2_;
/*  48 */     this.avoidTargetSelector = p_i46405_3_;
/*  49 */     this.avoidDistance = p_i46405_4_;
/*  50 */     this.farSpeed = p_i46405_5_;
/*  51 */     this.nearSpeed = p_i46405_7_;
/*  52 */     this.entityPathNavigate = p_i46405_1_.getNavigator();
/*  53 */     setMutexBits(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  61 */     List<T> list = this.theEntity.worldObj.getEntitiesWithinAABB(this.field_181064_i, this.theEntity.getEntityBoundingBox().expand(this.avoidDistance, 3.0D, this.avoidDistance), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector, this.avoidTargetSelector }));
/*     */     
/*  63 */     if (list.isEmpty())
/*     */     {
/*  65 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  69 */     this.closestLivingEntity = ((Entity)list.get(0));
/*  70 */     Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
/*     */     
/*  72 */     if (vec3 == null)
/*     */     {
/*  74 */       return false;
/*     */     }
/*  76 */     if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity))
/*     */     {
/*  78 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  82 */     this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
/*  83 */     return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  93 */     return !this.entityPathNavigate.noPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/* 101 */     this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/* 109 */     this.closestLivingEntity = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 117 */     if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0D)
/*     */     {
/* 119 */       this.theEntity.getNavigator().setSpeed(this.nearSpeed);
/*     */     }
/*     */     else
/*     */     {
/* 123 */       this.theEntity.getNavigator().setSpeed(this.farSpeed);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIAvoidEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */