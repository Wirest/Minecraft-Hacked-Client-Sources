/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.pathfinding.PathNavigate;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityAIMoveTowardsTarget
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theEntity;
/*    */   private EntityLivingBase targetEntity;
/*    */   private double movePosX;
/*    */   private double movePosY;
/*    */   private double movePosZ;
/*    */   private double speed;
/*    */   private float maxTargetDistance;
/*    */   
/*    */   public EntityAIMoveTowardsTarget(EntityCreature creature, double speedIn, float targetMaxDistance)
/*    */   {
/* 23 */     this.theEntity = creature;
/* 24 */     this.speed = speedIn;
/* 25 */     this.maxTargetDistance = targetMaxDistance;
/* 26 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 34 */     this.targetEntity = this.theEntity.getAttackTarget();
/*    */     
/* 36 */     if (this.targetEntity == null)
/*    */     {
/* 38 */       return false;
/*    */     }
/* 40 */     if (this.targetEntity.getDistanceSqToEntity(this.theEntity) > this.maxTargetDistance * this.maxTargetDistance)
/*    */     {
/* 42 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 46 */     Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
/*    */     
/* 48 */     if (vec3 == null)
/*    */     {
/* 50 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 54 */     this.movePosX = vec3.xCoord;
/* 55 */     this.movePosY = vec3.yCoord;
/* 56 */     this.movePosZ = vec3.zCoord;
/* 57 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 67 */     return (!this.theEntity.getNavigator().noPath()) && (this.targetEntity.isEntityAlive()) && (this.targetEntity.getDistanceSqToEntity(this.theEntity) < this.maxTargetDistance * this.maxTargetDistance);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void resetTask()
/*    */   {
/* 75 */     this.targetEntity = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 83 */     this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIMoveTowardsTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */