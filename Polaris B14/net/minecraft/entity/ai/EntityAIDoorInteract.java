/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public abstract class EntityAIDoorInteract extends EntityAIBase
/*     */ {
/*     */   protected EntityLiving theEntity;
/*  15 */   protected BlockPos doorPosition = BlockPos.ORIGIN;
/*     */   
/*     */ 
/*     */   protected BlockDoor doorBlock;
/*     */   
/*     */   boolean hasStoppedDoorInteraction;
/*     */   
/*     */   float entityPositionX;
/*     */   
/*     */   float entityPositionZ;
/*     */   
/*     */ 
/*     */   public EntityAIDoorInteract(EntityLiving entityIn)
/*     */   {
/*  29 */     this.theEntity = entityIn;
/*     */     
/*  31 */     if (!(entityIn.getNavigator() instanceof PathNavigateGround))
/*     */     {
/*  33 */       throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  42 */     if (!this.theEntity.isCollidedHorizontally)
/*     */     {
/*  44 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  48 */     PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
/*  49 */     PathEntity pathentity = pathnavigateground.getPath();
/*     */     
/*  51 */     if ((pathentity != null) && (!pathentity.isFinished()) && (pathnavigateground.getEnterDoors()))
/*     */     {
/*  53 */       for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); i++)
/*     */       {
/*  55 */         PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
/*  56 */         this.doorPosition = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
/*     */         
/*  58 */         if (this.theEntity.getDistanceSq(this.doorPosition.getX(), this.theEntity.posY, this.doorPosition.getZ()) <= 2.25D)
/*     */         {
/*  60 */           this.doorBlock = getBlockDoor(this.doorPosition);
/*     */           
/*  62 */           if (this.doorBlock != null)
/*     */           {
/*  64 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  69 */       this.doorPosition = new BlockPos(this.theEntity).up();
/*  70 */       this.doorBlock = getBlockDoor(this.doorPosition);
/*  71 */       return this.doorBlock != null;
/*     */     }
/*     */     
/*     */ 
/*  75 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  85 */     return !this.hasStoppedDoorInteraction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  93 */     this.hasStoppedDoorInteraction = false;
/*  94 */     this.entityPositionX = ((float)(this.doorPosition.getX() + 0.5F - this.theEntity.posX));
/*  95 */     this.entityPositionZ = ((float)(this.doorPosition.getZ() + 0.5F - this.theEntity.posZ));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 103 */     float f = (float)(this.doorPosition.getX() + 0.5F - this.theEntity.posX);
/* 104 */     float f1 = (float)(this.doorPosition.getZ() + 0.5F - this.theEntity.posZ);
/* 105 */     float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
/*     */     
/* 107 */     if (f2 < 0.0F)
/*     */     {
/* 109 */       this.hasStoppedDoorInteraction = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private BlockDoor getBlockDoor(BlockPos pos)
/*     */   {
/* 115 */     Block block = this.theEntity.worldObj.getBlockState(pos).getBlock();
/* 116 */     return ((block instanceof BlockDoor)) && (block.getMaterial() == net.minecraft.block.material.Material.wood) ? (BlockDoor)block : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIDoorInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */