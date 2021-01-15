/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public class NBTSizeTracker
/*    */ {
/*  5 */   public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L)
/*    */   {
/*    */     public void read(long bits) {}
/*    */   };
/*    */   
/*    */   private final long max;
/*    */   
/*    */   private long read;
/*    */   
/*    */   public NBTSizeTracker(long max)
/*    */   {
/* 16 */     this.max = max;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void read(long bits)
/*    */   {
/* 24 */     this.read += bits / 8L;
/*    */     
/* 26 */     if (this.read > this.max)
/*    */     {
/* 28 */       throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.read + "bytes where max allowed: " + this.max);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTSizeTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */