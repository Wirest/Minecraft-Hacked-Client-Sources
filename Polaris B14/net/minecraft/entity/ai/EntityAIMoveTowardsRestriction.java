/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIMoveTowardsRestriction extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theEntity;
/*    */   private double movePosX;
/*    */   private double movePosY;
/*    */   private double movePosZ;
/*    */   private double movementSpeed;
/*    */   
/*    */   public EntityAIMoveTowardsRestriction(EntityCreature creatureIn, double speedIn)
/*    */   {
/* 17 */     this.theEntity = creatureIn;
/* 18 */     this.movementSpeed = speedIn;
/* 19 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 27 */     if (this.theEntity.isWithinHomeDistanceCurrentPosition())
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 33 */     BlockPos blockpos = this.theEntity.getHomePosition();
/* 34 */     Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
/*    */     
/* 36 */     if (vec3 == null)
/*    */     {
/* 38 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 42 */     this.movePosX = vec3.xCoord;
/* 43 */     this.movePosY = vec3.yCoord;
/* 44 */     this.movePosZ = vec3.zCoord;
/* 45 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 55 */     return !this.theEntity.getNavigator().noPath();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 63 */     this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIMoveTowardsRestriction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */