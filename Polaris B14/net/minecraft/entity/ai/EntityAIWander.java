/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIWander extends EntityAIBase
/*    */ {
/*    */   private EntityCreature entity;
/*    */   private double xPosition;
/*    */   private double yPosition;
/*    */   private double zPosition;
/*    */   private double speed;
/*    */   private int executionChance;
/*    */   private boolean mustUpdate;
/*    */   
/*    */   public EntityAIWander(EntityCreature creatureIn, double speedIn)
/*    */   {
/* 18 */     this(creatureIn, speedIn, 120);
/*    */   }
/*    */   
/*    */   public EntityAIWander(EntityCreature creatureIn, double speedIn, int chance)
/*    */   {
/* 23 */     this.entity = creatureIn;
/* 24 */     this.speed = speedIn;
/* 25 */     this.executionChance = chance;
/* 26 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 34 */     if (!this.mustUpdate)
/*    */     {
/* 36 */       if (this.entity.getAge() >= 100)
/*    */       {
/* 38 */         return false;
/*    */       }
/*    */       
/* 41 */       if (this.entity.getRNG().nextInt(this.executionChance) != 0)
/*    */       {
/* 43 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 47 */     Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
/*    */     
/* 49 */     if (vec3 == null)
/*    */     {
/* 51 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 55 */     this.xPosition = vec3.xCoord;
/* 56 */     this.yPosition = vec3.yCoord;
/* 57 */     this.zPosition = vec3.zCoord;
/* 58 */     this.mustUpdate = false;
/* 59 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 68 */     return !this.entity.getNavigator().noPath();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 76 */     this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void makeUpdate()
/*    */   {
/* 84 */     this.mustUpdate = true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setExecutionChance(int newchance)
/*    */   {
/* 92 */     this.executionChance = newchance;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIWander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */