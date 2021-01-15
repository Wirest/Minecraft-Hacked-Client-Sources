/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import rip.jutting.polaris.event.Event;
/*    */ 
/*    */ public class EventBoundingBox extends Event
/*    */ {
/*    */   private Block block;
/*    */   private BlockPos blockPos;
/*    */   private final double x;
/*    */   private final double y;
/*    */   private final double z;
/*    */   public AxisAlignedBB boundingBox;
/*    */   
/*    */   public EventBoundingBox(Block block, BlockPos pos, AxisAlignedBB boundingBox, double x, double y, double z)
/*    */   {
/* 19 */     this.block = block;
/* 20 */     this.blockPos = pos;
/* 21 */     this.boundingBox = boundingBox;
/* 22 */     this.x = x;
/* 23 */     this.y = y;
/* 24 */     this.z = z;
/*    */   }
/*    */   
/*    */   public Block getBlock() {
/* 28 */     return this.block;
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos() {
/* 32 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getBoundingBox() {
/* 36 */     return this.boundingBox;
/*    */   }
/*    */   
/*    */   public double getX() {
/* 40 */     return this.x;
/*    */   }
/*    */   
/* 43 */   public double getY() { return this.y; }
/*    */   
/*    */   public double getZ() {
/* 46 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setBlock(Block block) {
/* 50 */     this.block = block;
/*    */   }
/*    */   
/*    */   public void setBlockPos(BlockPos blockPos) {
/* 54 */     this.blockPos = blockPos;
/*    */   }
/*    */   
/*    */   public void setBoundingBox(AxisAlignedBB boundingBox) {
/* 58 */     this.boundingBox = boundingBox;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventBoundingBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */