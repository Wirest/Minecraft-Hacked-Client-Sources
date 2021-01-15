/*     */ package net.minecraft.village;
/*     */ 
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillageDoorInfo
/*     */ {
/*     */   private final BlockPos doorBlockPos;
/*     */   private final BlockPos insideBlock;
/*     */   private final EnumFacing insideDirection;
/*     */   private int lastActivityTimestamp;
/*     */   private boolean isDetachedFromVillageFlag;
/*     */   private int doorOpeningRestrictionCounter;
/*     */   
/*     */   public VillageDoorInfo(BlockPos p_i45871_1_, int p_i45871_2_, int p_i45871_3_, int p_i45871_4_)
/*     */   {
/*  20 */     this(p_i45871_1_, getFaceDirection(p_i45871_2_, p_i45871_3_), p_i45871_4_);
/*     */   }
/*     */   
/*     */   private static EnumFacing getFaceDirection(int deltaX, int deltaZ)
/*     */   {
/*  25 */     return deltaZ < 0 ? EnumFacing.NORTH : deltaX > 0 ? EnumFacing.EAST : deltaX < 0 ? EnumFacing.WEST : EnumFacing.SOUTH;
/*     */   }
/*     */   
/*     */   public VillageDoorInfo(BlockPos p_i45872_1_, EnumFacing p_i45872_2_, int p_i45872_3_)
/*     */   {
/*  30 */     this.doorBlockPos = p_i45872_1_;
/*  31 */     this.insideDirection = p_i45872_2_;
/*  32 */     this.insideBlock = p_i45872_1_.offset(p_i45872_2_, 2);
/*  33 */     this.lastActivityTimestamp = p_i45872_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDistanceSquared(int p_75474_1_, int p_75474_2_, int p_75474_3_)
/*     */   {
/*  41 */     return (int)this.doorBlockPos.distanceSq(p_75474_1_, p_75474_2_, p_75474_3_);
/*     */   }
/*     */   
/*     */   public int getDistanceToDoorBlockSq(BlockPos p_179848_1_)
/*     */   {
/*  46 */     return (int)p_179848_1_.distanceSq(getDoorBlockPos());
/*     */   }
/*     */   
/*     */   public int getDistanceToInsideBlockSq(BlockPos p_179846_1_)
/*     */   {
/*  51 */     return (int)this.insideBlock.distanceSq(p_179846_1_);
/*     */   }
/*     */   
/*     */   public boolean func_179850_c(BlockPos p_179850_1_)
/*     */   {
/*  56 */     int i = p_179850_1_.getX() - this.doorBlockPos.getX();
/*  57 */     int j = p_179850_1_.getZ() - this.doorBlockPos.getY();
/*  58 */     return i * this.insideDirection.getFrontOffsetX() + j * this.insideDirection.getFrontOffsetZ() >= 0;
/*     */   }
/*     */   
/*     */   public void resetDoorOpeningRestrictionCounter()
/*     */   {
/*  63 */     this.doorOpeningRestrictionCounter = 0;
/*     */   }
/*     */   
/*     */   public void incrementDoorOpeningRestrictionCounter()
/*     */   {
/*  68 */     this.doorOpeningRestrictionCounter += 1;
/*     */   }
/*     */   
/*     */   public int getDoorOpeningRestrictionCounter()
/*     */   {
/*  73 */     return this.doorOpeningRestrictionCounter;
/*     */   }
/*     */   
/*     */   public BlockPos getDoorBlockPos()
/*     */   {
/*  78 */     return this.doorBlockPos;
/*     */   }
/*     */   
/*     */   public BlockPos getInsideBlockPos()
/*     */   {
/*  83 */     return this.insideBlock;
/*     */   }
/*     */   
/*     */   public int getInsideOffsetX()
/*     */   {
/*  88 */     return this.insideDirection.getFrontOffsetX() * 2;
/*     */   }
/*     */   
/*     */   public int getInsideOffsetZ()
/*     */   {
/*  93 */     return this.insideDirection.getFrontOffsetZ() * 2;
/*     */   }
/*     */   
/*     */   public int getInsidePosY()
/*     */   {
/*  98 */     return this.lastActivityTimestamp;
/*     */   }
/*     */   
/*     */   public void func_179849_a(int p_179849_1_)
/*     */   {
/* 103 */     this.lastActivityTimestamp = p_179849_1_;
/*     */   }
/*     */   
/*     */   public boolean getIsDetachedFromVillageFlag()
/*     */   {
/* 108 */     return this.isDetachedFromVillageFlag;
/*     */   }
/*     */   
/*     */   public void setIsDetachedFromVillageFlag(boolean p_179853_1_)
/*     */   {
/* 113 */     this.isDetachedFromVillageFlag = p_179853_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\village\VillageDoorInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */