/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.passive.EntityHorse;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIRunAroundLikeCrazy extends EntityAIBase
/*    */ {
/*    */   private EntityHorse horseHost;
/*    */   private double speed;
/*    */   private double targetX;
/*    */   private double targetY;
/*    */   private double targetZ;
/*    */   
/*    */   public EntityAIRunAroundLikeCrazy(EntityHorse horse, double speedIn)
/*    */   {
/* 18 */     this.horseHost = horse;
/* 19 */     this.speed = speedIn;
/* 20 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 28 */     if ((!this.horseHost.isTame()) && (this.horseHost.riddenByEntity != null))
/*    */     {
/* 30 */       Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.horseHost, 5, 4);
/*    */       
/* 32 */       if (vec3 == null)
/*    */       {
/* 34 */         return false;
/*    */       }
/*    */       
/*    */ 
/* 38 */       this.targetX = vec3.xCoord;
/* 39 */       this.targetY = vec3.yCoord;
/* 40 */       this.targetZ = vec3.zCoord;
/* 41 */       return true;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 46 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 55 */     this.horseHost.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 63 */     return (!this.horseHost.getNavigator().noPath()) && (this.horseHost.riddenByEntity != null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateTask()
/*    */   {
/* 71 */     if (this.horseHost.getRNG().nextInt(50) == 0)
/*    */     {
/* 73 */       if ((this.horseHost.riddenByEntity instanceof EntityPlayer))
/*    */       {
/* 75 */         int i = this.horseHost.getTemper();
/* 76 */         int j = this.horseHost.getMaxTemper();
/*    */         
/* 78 */         if ((j > 0) && (this.horseHost.getRNG().nextInt(j) < i))
/*    */         {
/* 80 */           this.horseHost.setTamedBy((EntityPlayer)this.horseHost.riddenByEntity);
/* 81 */           this.horseHost.worldObj.setEntityState(this.horseHost, (byte)7);
/* 82 */           return;
/*    */         }
/*    */         
/* 85 */         this.horseHost.increaseTemper(5);
/*    */       }
/*    */       
/* 88 */       this.horseHost.riddenByEntity.mountEntity(null);
/* 89 */       this.horseHost.riddenByEntity = null;
/* 90 */       this.horseHost.makeHorseRearWithSound();
/* 91 */       this.horseHost.worldObj.setEntityState(this.horseHost, (byte)6);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIRunAroundLikeCrazy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */