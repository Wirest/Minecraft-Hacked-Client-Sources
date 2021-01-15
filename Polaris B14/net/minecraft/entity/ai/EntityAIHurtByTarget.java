/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIHurtByTarget
/*    */   extends EntityAITarget
/*    */ {
/*    */   private boolean entityCallsForHelp;
/*    */   private int revengeTimerOld;
/*    */   private final Class[] targetClasses;
/*    */   
/*    */   public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class... targetClassesIn)
/*    */   {
/* 17 */     super(creatureIn, false);
/* 18 */     this.entityCallsForHelp = entityCallsForHelpIn;
/* 19 */     this.targetClasses = targetClassesIn;
/* 20 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 28 */     int i = this.taskOwner.getRevengeTimer();
/* 29 */     return (i != this.revengeTimerOld) && (isSuitableTarget(this.taskOwner.getAITarget(), false));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 37 */     this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
/* 38 */     this.revengeTimerOld = this.taskOwner.getRevengeTimer();
/*    */     
/* 40 */     if (this.entityCallsForHelp)
/*    */     {
/* 42 */       double d0 = getTargetDistance();
/*    */       
/* 44 */       for (EntityCreature entitycreature : this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).expand(d0, 10.0D, d0)))
/*    */       {
/* 46 */         if ((this.taskOwner != entitycreature) && (entitycreature.getAttackTarget() == null) && (!entitycreature.isOnSameTeam(this.taskOwner.getAITarget())))
/*    */         {
/* 48 */           boolean flag = false;
/*    */           Class[] arrayOfClass;
/* 50 */           int j = (arrayOfClass = this.targetClasses).length; for (int i = 0; i < j; i++) { Class oclass = arrayOfClass[i];
/*    */             
/* 52 */             if (entitycreature.getClass() == oclass)
/*    */             {
/* 54 */               flag = true;
/* 55 */               break;
/*    */             }
/*    */           }
/*    */           
/* 59 */           if (!flag)
/*    */           {
/* 61 */             setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 67 */     super.startExecuting();
/*    */   }
/*    */   
/*    */   protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
/*    */   {
/* 72 */     creatureIn.setAttackTarget(entityLivingBaseIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIHurtByTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */