/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NBTTagEnd extends NBTBase
/*    */ {
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*    */   {
/* 11 */     sizeTracker.read(64L);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   void write(DataOutput output)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte getId()
/*    */   {
/* 26 */     return 0;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 31 */     return "END";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NBTBase copy()
/*    */   {
/* 39 */     return new NBTTagEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */