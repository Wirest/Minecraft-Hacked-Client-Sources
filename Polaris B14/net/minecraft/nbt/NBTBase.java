/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class NBTBase
/*     */ {
/*   9 */   public static final String[] NBT_TYPES = { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };
/*     */   
/*     */ 
/*     */ 
/*     */   abstract void write(DataOutput paramDataOutput)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   abstract void read(DataInput paramDataInput, int paramInt, NBTSizeTracker paramNBTSizeTracker)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract String toString();
/*     */   
/*     */ 
/*     */   public abstract byte getId();
/*     */   
/*     */ 
/*     */   protected static NBTBase createNewByType(byte id)
/*     */   {
/*  30 */     switch (id)
/*     */     {
/*     */     case 0: 
/*  33 */       return new NBTTagEnd();
/*     */     
/*     */     case 1: 
/*  36 */       return new NBTTagByte();
/*     */     
/*     */     case 2: 
/*  39 */       return new NBTTagShort();
/*     */     
/*     */     case 3: 
/*  42 */       return new NBTTagInt();
/*     */     
/*     */     case 4: 
/*  45 */       return new NBTTagLong();
/*     */     
/*     */     case 5: 
/*  48 */       return new NBTTagFloat();
/*     */     
/*     */     case 6: 
/*  51 */       return new NBTTagDouble();
/*     */     
/*     */     case 7: 
/*  54 */       return new NBTTagByteArray();
/*     */     
/*     */     case 8: 
/*  57 */       return new NBTTagString();
/*     */     
/*     */     case 9: 
/*  60 */       return new NBTTagList();
/*     */     
/*     */     case 10: 
/*  63 */       return new NBTTagCompound();
/*     */     
/*     */     case 11: 
/*  66 */       return new NBTTagIntArray();
/*     */     }
/*     */     
/*  69 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract NBTBase copy();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNoTags()
/*     */   {
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  88 */     if (!(p_equals_1_ instanceof NBTBase))
/*     */     {
/*  90 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  94 */     NBTBase nbtbase = (NBTBase)p_equals_1_;
/*  95 */     return getId() == nbtbase.getId();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 101 */     return getId();
/*     */   }
/*     */   
/*     */   protected String getString()
/*     */   {
/* 106 */     return toString();
/*     */   }
/*     */   
/*     */   public static abstract class NBTPrimitive
/*     */     extends NBTBase
/*     */   {
/*     */     public abstract long getLong();
/*     */     
/*     */     public abstract int getInt();
/*     */     
/*     */     public abstract short getShort();
/*     */     
/*     */     public abstract byte getByte();
/*     */     
/*     */     public abstract double getDouble();
/*     */     
/*     */     public abstract float getFloat();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */