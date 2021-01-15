/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NBTTagInt
/*     */   extends NBTBase.NBTPrimitive
/*     */ {
/*     */   private int data;
/*     */   
/*     */   NBTTagInt() {}
/*     */   
/*     */   public NBTTagInt(int data)
/*     */   {
/*  18 */     this.data = data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void write(DataOutput output)
/*     */     throws IOException
/*     */   {
/*  26 */     output.writeInt(this.data);
/*     */   }
/*     */   
/*     */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/*  31 */     sizeTracker.read(96L);
/*  32 */     this.data = input.readInt();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getId()
/*     */   {
/*  40 */     return 3;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  45 */     return this.data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase copy()
/*     */   {
/*  53 */     return new NBTTagInt(this.data);
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  58 */     if (super.equals(p_equals_1_))
/*     */     {
/*  60 */       NBTTagInt nbttagint = (NBTTagInt)p_equals_1_;
/*  61 */       return this.data == nbttagint.data;
/*     */     }
/*     */     
/*     */ 
/*  65 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  71 */     return super.hashCode() ^ this.data;
/*     */   }
/*     */   
/*     */   public long getLong()
/*     */   {
/*  76 */     return this.data;
/*     */   }
/*     */   
/*     */   public int getInt()
/*     */   {
/*  81 */     return this.data;
/*     */   }
/*     */   
/*     */   public short getShort()
/*     */   {
/*  86 */     return (short)(this.data & 0xFFFF);
/*     */   }
/*     */   
/*     */   public byte getByte()
/*     */   {
/*  91 */     return (byte)(this.data & 0xFF);
/*     */   }
/*     */   
/*     */   public double getDouble()
/*     */   {
/*  96 */     return this.data;
/*     */   }
/*     */   
/*     */   public float getFloat()
/*     */   {
/* 101 */     return this.data;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */