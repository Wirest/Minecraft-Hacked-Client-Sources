/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NBTTagDouble
/*     */   extends NBTBase.NBTPrimitive
/*     */ {
/*     */   private double data;
/*     */   
/*     */   NBTTagDouble() {}
/*     */   
/*     */   public NBTTagDouble(double data)
/*     */   {
/*  19 */     this.data = data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void write(DataOutput output)
/*     */     throws IOException
/*     */   {
/*  27 */     output.writeDouble(this.data);
/*     */   }
/*     */   
/*     */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/*  32 */     sizeTracker.read(128L);
/*  33 */     this.data = input.readDouble();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getId()
/*     */   {
/*  41 */     return 6;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  46 */     return this.data + "d";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase copy()
/*     */   {
/*  54 */     return new NBTTagDouble(this.data);
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  59 */     if (super.equals(p_equals_1_))
/*     */     {
/*  61 */       NBTTagDouble nbttagdouble = (NBTTagDouble)p_equals_1_;
/*  62 */       return this.data == nbttagdouble.data;
/*     */     }
/*     */     
/*     */ 
/*  66 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  72 */     long i = Double.doubleToLongBits(this.data);
/*  73 */     return super.hashCode() ^ (int)(i ^ i >>> 32);
/*     */   }
/*     */   
/*     */   public long getLong()
/*     */   {
/*  78 */     return Math.floor(this.data);
/*     */   }
/*     */   
/*     */   public int getInt()
/*     */   {
/*  83 */     return MathHelper.floor_double(this.data);
/*     */   }
/*     */   
/*     */   public short getShort()
/*     */   {
/*  88 */     return (short)(MathHelper.floor_double(this.data) & 0xFFFF);
/*     */   }
/*     */   
/*     */   public byte getByte()
/*     */   {
/*  93 */     return (byte)(MathHelper.floor_double(this.data) & 0xFF);
/*     */   }
/*     */   
/*     */   public double getDouble()
/*     */   {
/*  98 */     return this.data;
/*     */   }
/*     */   
/*     */   public float getFloat()
/*     */   {
/* 103 */     return (float)this.data;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */