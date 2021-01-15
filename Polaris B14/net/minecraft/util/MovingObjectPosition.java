/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MovingObjectPosition
/*    */ {
/*    */   private BlockPos blockPos;
/*    */   public MovingObjectType typeOfHit;
/*    */   public EnumFacing sideHit;
/*    */   public Vec3 hitVec;
/*    */   public Entity entityHit;
/*    */   
/*    */   public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, BlockPos blockPosIn)
/*    */   {
/* 21 */     this(MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
/*    */   }
/*    */   
/*    */   public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing facing)
/*    */   {
/* 26 */     this(MovingObjectType.BLOCK, p_i45552_1_, facing, BlockPos.ORIGIN);
/*    */   }
/*    */   
/*    */   public MovingObjectPosition(Entity p_i2304_1_)
/*    */   {
/* 31 */     this(p_i2304_1_, new Vec3(p_i2304_1_.posX, p_i2304_1_.posY, p_i2304_1_.posZ));
/*    */   }
/*    */   
/*    */   public MovingObjectPosition(MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn)
/*    */   {
/* 36 */     this.typeOfHit = typeOfHitIn;
/* 37 */     this.blockPos = blockPosIn;
/* 38 */     this.sideHit = sideHitIn;
/* 39 */     this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
/*    */   }
/*    */   
/*    */   public MovingObjectPosition(Entity entityHitIn, Vec3 hitVecIn)
/*    */   {
/* 44 */     this.typeOfHit = MovingObjectType.ENTITY;
/* 45 */     this.entityHit = entityHitIn;
/* 46 */     this.hitVec = hitVecIn;
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos()
/*    */   {
/* 51 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 56 */     return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
/*    */   }
/*    */   
/*    */   public static enum MovingObjectType
/*    */   {
/* 61 */     MISS, 
/* 62 */     BLOCK, 
/* 63 */     ENTITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MovingObjectPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */