/*    */ package net.minecraft.pathfinding;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.ai.EntityMoveHelper;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class PathNavigateClimber extends PathNavigateGround
/*    */ {
/*    */   private BlockPos targetPosition;
/*    */   
/*    */   public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn)
/*    */   {
/* 16 */     super(entityLivingIn, worldIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public PathEntity getPathToPos(BlockPos pos)
/*    */   {
/* 24 */     this.targetPosition = pos;
/* 25 */     return super.getPathToPos(pos);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public PathEntity getPathToEntityLiving(Entity entityIn)
/*    */   {
/* 33 */     this.targetPosition = new BlockPos(entityIn);
/* 34 */     return super.getPathToEntityLiving(entityIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn)
/*    */   {
/* 42 */     PathEntity pathentity = getPathToEntityLiving(entityIn);
/*    */     
/* 44 */     if (pathentity != null)
/*    */     {
/* 46 */       return setPath(pathentity, speedIn);
/*    */     }
/*    */     
/*    */ 
/* 50 */     this.targetPosition = new BlockPos(entityIn);
/* 51 */     this.speed = speedIn;
/* 52 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public void onUpdateNavigation()
/*    */   {
/* 58 */     if (!noPath())
/*    */     {
/* 60 */       super.onUpdateNavigation();
/*    */ 
/*    */ 
/*    */     }
/* 64 */     else if (this.targetPosition != null)
/*    */     {
/* 66 */       double d0 = this.theEntity.width * this.theEntity.width;
/*    */       
/* 68 */       if ((this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0) && ((this.theEntity.posY <= this.targetPosition.getY()) || (this.theEntity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor_double(this.theEntity.posY), this.targetPosition.getZ())) >= d0)))
/*    */       {
/* 70 */         this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
/*    */       }
/*    */       else
/*    */       {
/* 74 */         this.targetPosition = null;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\pathfinding\PathNavigateClimber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */