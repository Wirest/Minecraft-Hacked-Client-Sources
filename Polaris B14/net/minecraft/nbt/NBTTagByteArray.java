/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTTagByteArray
/*    */   extends NBTBase
/*    */ {
/*    */   private byte[] data;
/*    */   
/*    */   NBTTagByteArray() {}
/*    */   
/*    */   public NBTTagByteArray(byte[] data)
/*    */   {
/* 19 */     this.data = data;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   void write(DataOutput output)
/*    */     throws IOException
/*    */   {
/* 27 */     output.writeInt(this.data.length);
/* 28 */     output.write(this.data);
/*    */   }
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*    */   {
/* 33 */     sizeTracker.read(192L);
/* 34 */     int i = input.readInt();
/* 35 */     sizeTracker.read(8 * i);
/* 36 */     this.data = new byte[i];
/* 37 */     input.readFully(this.data);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte getId()
/*    */   {
/* 45 */     return 7;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 50 */     return "[" + this.data.length + " bytes]";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NBTBase copy()
/*    */   {
/* 58 */     byte[] abyte = new byte[this.data.length];
/* 59 */     System.arraycopy(this.data, 0, abyte, 0, this.data.length);
/* 60 */     return new NBTTagByteArray(abyte);
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 65 */     return super.equals(p_equals_1_) ? Arrays.equals(this.data, ((NBTTagByteArray)p_equals_1_).data) : false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 70 */     return super.hashCode() ^ Arrays.hashCode(this.data);
/*    */   }
/*    */   
/*    */   public byte[] getByteArray()
/*    */   {
/* 75 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */