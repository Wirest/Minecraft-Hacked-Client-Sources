/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public abstract class EntityAIMoveToBlock
/*     */   extends EntityAIBase
/*     */ {
/*     */   private final EntityCreature theEntity;
/*     */   private final double movementSpeed;
/*     */   protected int runDelay;
/*     */   private int timeoutCounter;
/*     */   private int field_179490_f;
/*  18 */   protected BlockPos destinationBlock = BlockPos.ORIGIN;
/*     */   private boolean isAboveDestination;
/*     */   private int searchLength;
/*     */   
/*     */   public EntityAIMoveToBlock(EntityCreature creature, double speedIn, int length)
/*     */   {
/*  24 */     this.theEntity = creature;
/*  25 */     this.movementSpeed = speedIn;
/*  26 */     this.searchLength = length;
/*  27 */     setMutexBits(5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  35 */     if (this.runDelay > 0)
/*     */     {
/*  37 */       this.runDelay -= 1;
/*  38 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  42 */     this.runDelay = (200 + this.theEntity.getRNG().nextInt(200));
/*  43 */     return searchForDestination();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  52 */     return (this.timeoutCounter >= -this.field_179490_f) && (this.timeoutCounter <= 1200) && (shouldMoveTo(this.theEntity.worldObj, this.destinationBlock));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  60 */     this.theEntity.getNavigator().tryMoveToXYZ(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, this.movementSpeed);
/*  61 */     this.timeoutCounter = 0;
/*  62 */     this.field_179490_f = (this.theEntity.getRNG().nextInt(this.theEntity.getRNG().nextInt(1200) + 1200) + 1200);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/*  77 */     if (this.theEntity.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D)
/*     */     {
/*  79 */       this.isAboveDestination = false;
/*  80 */       this.timeoutCounter += 1;
/*     */       
/*  82 */       if (this.timeoutCounter % 40 == 0)
/*     */       {
/*  84 */         this.theEntity.getNavigator().tryMoveToXYZ(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, this.movementSpeed);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  89 */       this.isAboveDestination = true;
/*  90 */       this.timeoutCounter -= 1;
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean getIsAboveDestination()
/*     */   {
/*  96 */     return this.isAboveDestination;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean searchForDestination()
/*     */   {
/* 106 */     int i = this.searchLength;
/* 107 */     int j = 1;
/* 108 */     BlockPos blockpos = new BlockPos(this.theEntity);
/*     */     
/* 110 */     for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k)
/*     */     {
/* 112 */       for (int l = 0; l < i; l++)
/*     */       {
/* 114 */         for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1)
/*     */         {
/* 116 */           for (int j1 = (i1 < l) && (i1 > -l) ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1)
/*     */           {
/* 118 */             BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);
/*     */             
/* 120 */             if ((this.theEntity.isWithinHomeDistanceFromPosition(blockpos1)) && (shouldMoveTo(this.theEntity.worldObj, blockpos1)))
/*     */             {
/* 122 */               this.destinationBlock = blockpos1;
/* 123 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 130 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract boolean shouldMoveTo(World paramWorld, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIMoveToBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */