/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.village.Village;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIVillagerMate extends EntityAIBase
/*     */ {
/*     */   private EntityVillager villagerObj;
/*     */   private EntityVillager mate;
/*     */   private World worldObj;
/*     */   private int matingTimeout;
/*     */   Village villageObj;
/*     */   
/*     */   public EntityAIVillagerMate(EntityVillager villagerIn)
/*     */   {
/*  19 */     this.villagerObj = villagerIn;
/*  20 */     this.worldObj = villagerIn.worldObj;
/*  21 */     setMutexBits(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  29 */     if (this.villagerObj.getGrowingAge() != 0)
/*     */     {
/*  31 */       return false;
/*     */     }
/*  33 */     if (this.villagerObj.getRNG().nextInt(500) != 0)
/*     */     {
/*  35 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  39 */     this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new net.minecraft.util.BlockPos(this.villagerObj), 0);
/*     */     
/*  41 */     if (this.villageObj == null)
/*     */     {
/*  43 */       return false;
/*     */     }
/*  45 */     if ((checkSufficientDoorsPresentForNewVillager()) && (this.villagerObj.getIsWillingToMate(true)))
/*     */     {
/*  47 */       net.minecraft.entity.Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), this.villagerObj);
/*     */       
/*  49 */       if (entity == null)
/*     */       {
/*  51 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  55 */       this.mate = ((EntityVillager)entity);
/*  56 */       return (this.mate.getGrowingAge() == 0) && (this.mate.getIsWillingToMate(true));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  61 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  71 */     this.matingTimeout = 300;
/*  72 */     this.villagerObj.setMating(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  80 */     this.villageObj = null;
/*  81 */     this.mate = null;
/*  82 */     this.villagerObj.setMating(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  90 */     return (this.matingTimeout >= 0) && (checkSufficientDoorsPresentForNewVillager()) && (this.villagerObj.getGrowingAge() == 0) && (this.villagerObj.getIsWillingToMate(false));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/*  98 */     this.matingTimeout -= 1;
/*  99 */     this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
/*     */     
/* 101 */     if (this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25D)
/*     */     {
/* 103 */       this.villagerObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
/*     */     }
/* 105 */     else if ((this.matingTimeout == 0) && (this.mate.isMating()))
/*     */     {
/* 107 */       giveBirth();
/*     */     }
/*     */     
/* 110 */     if (this.villagerObj.getRNG().nextInt(35) == 0)
/*     */     {
/* 112 */       this.worldObj.setEntityState(this.villagerObj, (byte)12);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean checkSufficientDoorsPresentForNewVillager()
/*     */   {
/* 118 */     if (!this.villageObj.isMatingSeason())
/*     */     {
/* 120 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 124 */     int i = (int)(this.villageObj.getNumVillageDoors() * 0.35D);
/* 125 */     return this.villageObj.getNumVillagers() < i;
/*     */   }
/*     */   
/*     */ 
/*     */   private void giveBirth()
/*     */   {
/* 131 */     EntityVillager entityvillager = this.villagerObj.createChild(this.mate);
/* 132 */     this.mate.setGrowingAge(6000);
/* 133 */     this.villagerObj.setGrowingAge(6000);
/* 134 */     this.mate.setIsWillingToMate(false);
/* 135 */     this.villagerObj.setIsWillingToMate(false);
/* 136 */     entityvillager.setGrowingAge(41536);
/* 137 */     entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
/* 138 */     this.worldObj.spawnEntityInWorld(entityvillager);
/* 139 */     this.worldObj.setEntityState(entityvillager, (byte)12);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIVillagerMate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */