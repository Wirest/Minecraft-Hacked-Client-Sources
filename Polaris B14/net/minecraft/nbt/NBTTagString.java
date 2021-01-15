/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NBTTagString
/*    */   extends NBTBase
/*    */ {
/*    */   private String data;
/*    */   
/*    */   public NBTTagString()
/*    */   {
/* 14 */     this.data = "";
/*    */   }
/*    */   
/*    */   public NBTTagString(String data)
/*    */   {
/* 19 */     this.data = data;
/*    */     
/* 21 */     if (data == null)
/*    */     {
/* 23 */       throw new IllegalArgumentException("Empty string not allowed");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   void write(DataOutput output)
/*    */     throws IOException
/*    */   {
/* 32 */     output.writeUTF(this.data);
/*    */   }
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*    */   {
/* 37 */     sizeTracker.read(288L);
/* 38 */     this.data = input.readUTF();
/* 39 */     sizeTracker.read(16 * this.data.length());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte getId()
/*    */   {
/* 47 */     return 8;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 52 */     return "\"" + this.data.replace("\"", "\\\"") + "\"";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NBTBase copy()
/*    */   {
/* 60 */     return new NBTTagString(this.data);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasNoTags()
/*    */   {
/* 68 */     return this.data.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 73 */     if (!super.equals(p_equals_1_))
/*    */     {
/* 75 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 79 */     NBTTagString nbttagstring = (NBTTagString)p_equals_1_;
/* 80 */     return ((this.data == null) && (nbttagstring.data == null)) || ((this.data != null) && (this.data.equals(nbttagstring.data)));
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 86 */     return super.hashCode() ^ this.data.hashCode();
/*    */   }
/*    */   
/*    */   public String getString()
/*    */   {
/* 91 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */