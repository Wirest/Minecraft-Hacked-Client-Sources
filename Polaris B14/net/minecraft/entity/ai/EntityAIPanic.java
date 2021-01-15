/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIPanic extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theEntityCreature;
/*    */   protected double speed;
/*    */   private double randPosX;
/*    */   private double randPosY;
/*    */   private double randPosZ;
/*    */   
/*    */   public EntityAIPanic(EntityCreature creature, double speedIn)
/*    */   {
/* 16 */     this.theEntityCreature = creature;
/* 17 */     this.speed = speedIn;
/* 18 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 26 */     if ((this.theEntityCreature.getAITarget() == null) && (!this.theEntityCreature.isBurning()))
/*    */     {
/* 28 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 32 */     Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);
/*    */     
/* 34 */     if (vec3 == null)
/*    */     {
/* 36 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 40 */     this.randPosX = vec3.xCoord;
/* 41 */     this.randPosY = vec3.yCoord;
/* 42 */     this.randPosZ = vec3.zCoord;
/* 43 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 53 */     this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 61 */     return !this.theEntityCreature.getNavigator().noPath();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIPanic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */