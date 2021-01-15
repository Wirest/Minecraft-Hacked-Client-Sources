/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NBTTagFloat
/*     */   extends NBTBase.NBTPrimitive
/*     */ {
/*     */   private float data;
/*     */   
/*     */   NBTTagFloat() {}
/*     */   
/*     */   public NBTTagFloat(float data)
/*     */   {
/*  19 */     this.data = data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void write(DataOutput output)
/*     */     throws IOException
/*     */   {
/*  27 */     output.writeFloat(this.data);
/*     */   }
/*     */   
/*     */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/*  32 */     sizeTracker.read(96L);
/*  33 */     this.data = input.readFloat();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getId()
/*     */   {
/*  41 */     return 5;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  46 */     return this.data + "f";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase copy()
/*     */   {
/*  54 */     return new NBTTagFloat(this.data);
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  59 */     if (super.equals(p_equals_1_))
/*     */     {
/*  61 */       NBTTagFloat nbttagfloat = (NBTTagFloat)p_equals_1_;
/*  62 */       return this.data == nbttagfloat.data;
/*     */     }
/*     */     
/*     */ 
/*  66 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  72 */     return super.hashCode() ^ Float.floatToIntBits(this.data);
/*     */   }
/*     */   
/*     */   public long getLong()
/*     */   {
/*  77 */     return this.data;
/*     */   }
/*     */   
/*     */   public int getInt()
/*     */   {
/*  82 */     return MathHelper.floor_float(this.data);
/*     */   }
/*     */   
/*     */   public short getShort()
/*     */   {
/*  87 */     return (short)(MathHelper.floor_float(this.data) & 0xFFFF);
/*     */   }
/*     */   
/*     */   public byte getByte()
/*     */   {
/*  92 */     return (byte)(MathHelper.floor_float(this.data) & 0xFF);
/*     */   }
/*     */   
/*     */   public double getDouble()
/*     */   {
/*  97 */     return this.data;
/*     */   }
/*     */   
/*     */   public float getFloat()
/*     */   {
/* 102 */     return this.data;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagFloat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */