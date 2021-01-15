/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NextTickListEntry
/*    */   implements Comparable<NextTickListEntry>
/*    */ {
/*    */   private static long nextTickEntryID;
/*    */   private final Block block;
/*    */   public final BlockPos position;
/*    */   public long scheduledTime;
/*    */   public int priority;
/*    */   private long tickEntryID;
/*    */   
/*    */   public NextTickListEntry(BlockPos p_i45745_1_, Block p_i45745_2_)
/*    */   {
/* 22 */     this.tickEntryID = (nextTickEntryID++);
/* 23 */     this.position = p_i45745_1_;
/* 24 */     this.block = p_i45745_2_;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 29 */     if (!(p_equals_1_ instanceof NextTickListEntry))
/*    */     {
/* 31 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 35 */     NextTickListEntry nextticklistentry = (NextTickListEntry)p_equals_1_;
/* 36 */     return (this.position.equals(nextticklistentry.position)) && (Block.isEqualTo(this.block, nextticklistentry.block));
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 42 */     return this.position.hashCode();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NextTickListEntry setScheduledTime(long p_77176_1_)
/*    */   {
/* 50 */     this.scheduledTime = p_77176_1_;
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public void setPriority(int p_82753_1_)
/*    */   {
/* 56 */     this.priority = p_82753_1_;
/*    */   }
/*    */   
/*    */   public int compareTo(NextTickListEntry p_compareTo_1_)
/*    */   {
/* 61 */     return this.tickEntryID > p_compareTo_1_.tickEntryID ? 1 : this.tickEntryID < p_compareTo_1_.tickEntryID ? -1 : this.priority != p_compareTo_1_.priority ? this.priority - p_compareTo_1_.priority : this.scheduledTime > p_compareTo_1_.scheduledTime ? 1 : this.scheduledTime < p_compareTo_1_.scheduledTime ? -1 : 0;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 66 */     return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
/*    */   }
/*    */   
/*    */   public Block getBlock()
/*    */   {
/* 71 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\NextTickListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */