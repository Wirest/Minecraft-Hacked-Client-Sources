/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityCreeper;
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.village.Village;
/*    */ 
/*    */ 
/*    */ public class EntityAIDefendVillage
/*    */   extends EntityAITarget
/*    */ {
/*    */   EntityIronGolem irongolem;
/*    */   EntityLivingBase villageAgressorTarget;
/*    */   
/*    */   public EntityAIDefendVillage(EntityIronGolem ironGolemIn)
/*    */   {
/* 19 */     super(ironGolemIn, false, true);
/* 20 */     this.irongolem = ironGolemIn;
/* 21 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 29 */     Village village = this.irongolem.getVillage();
/*    */     
/* 31 */     if (village == null)
/*    */     {
/* 33 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 37 */     this.villageAgressorTarget = village.findNearestVillageAggressor(this.irongolem);
/*    */     
/* 39 */     if ((this.villageAgressorTarget instanceof EntityCreeper))
/*    */     {
/* 41 */       return false;
/*    */     }
/* 43 */     if (!isSuitableTarget(this.villageAgressorTarget, false))
/*    */     {
/* 45 */       if (this.taskOwner.getRNG().nextInt(20) == 0)
/*    */       {
/* 47 */         this.villageAgressorTarget = village.getNearestTargetPlayer(this.irongolem);
/* 48 */         return isSuitableTarget(this.villageAgressorTarget, false);
/*    */       }
/*    */       
/*    */ 
/* 52 */       return false;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 57 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 67 */     this.irongolem.setAttackTarget(this.villageAgressorTarget);
/* 68 */     super.startExecuting();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIDefendVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */