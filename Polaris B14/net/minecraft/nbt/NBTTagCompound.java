/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.ReportedException;
/*     */ 
/*     */ public class NBTTagCompound extends NBTBase
/*     */ {
/*  17 */   private Map<String, NBTBase> tagMap = Maps.newHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   void write(DataOutput output)
/*     */     throws IOException
/*     */   {
/*  24 */     for (String s : this.tagMap.keySet())
/*     */     {
/*  26 */       NBTBase nbtbase = (NBTBase)this.tagMap.get(s);
/*  27 */       writeEntry(s, nbtbase, output);
/*     */     }
/*     */     
/*  30 */     output.writeByte(0);
/*     */   }
/*     */   
/*     */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/*  35 */     sizeTracker.read(384L);
/*     */     
/*  37 */     if (depth > 512)
/*     */     {
/*  39 */       throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */     }
/*     */     
/*     */ 
/*  43 */     this.tagMap.clear();
/*     */     
/*     */     byte b0;
/*  46 */     while ((b0 = readType(input, sizeTracker)) != 0) {
/*     */       byte b0;
/*  48 */       String s = readKey(input, sizeTracker);
/*  49 */       sizeTracker.read(224 + 16 * s.length());
/*  50 */       NBTBase nbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);
/*     */       
/*  52 */       if (this.tagMap.put(s, nbtbase) != null)
/*     */       {
/*  54 */         sizeTracker.read(288L);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> getKeySet()
/*     */   {
/*  62 */     return this.tagMap.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getId()
/*     */   {
/*  70 */     return 10;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTag(String key, NBTBase value)
/*     */   {
/*  78 */     this.tagMap.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByte(String key, byte value)
/*     */   {
/*  86 */     this.tagMap.put(key, new NBTTagByte(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShort(String key, short value)
/*     */   {
/*  94 */     this.tagMap.put(key, new NBTTagShort(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInteger(String key, int value)
/*     */   {
/* 102 */     this.tagMap.put(key, new NBTTagInt(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLong(String key, long value)
/*     */   {
/* 110 */     this.tagMap.put(key, new NBTTagLong(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFloat(String key, float value)
/*     */   {
/* 118 */     this.tagMap.put(key, new NBTTagFloat(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDouble(String key, double value)
/*     */   {
/* 126 */     this.tagMap.put(key, new NBTTagDouble(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setString(String key, String value)
/*     */   {
/* 134 */     this.tagMap.put(key, new NBTTagString(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByteArray(String key, byte[] value)
/*     */   {
/* 142 */     this.tagMap.put(key, new NBTTagByteArray(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIntArray(String key, int[] value)
/*     */   {
/* 150 */     this.tagMap.put(key, new NBTTagIntArray(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBoolean(String key, boolean value)
/*     */   {
/* 158 */     setByte(key, (byte)(value ? 1 : 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase getTag(String key)
/*     */   {
/* 166 */     return (NBTBase)this.tagMap.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getTagId(String key)
/*     */   {
/* 174 */     NBTBase nbtbase = (NBTBase)this.tagMap.get(key);
/* 175 */     return nbtbase != null ? nbtbase.getId() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasKey(String key)
/*     */   {
/* 183 */     return this.tagMap.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean hasKey(String key, int type)
/*     */   {
/* 188 */     int i = getTagId(key);
/*     */     
/* 190 */     if (i == type)
/*     */     {
/* 192 */       return true;
/*     */     }
/* 194 */     if (type != 99)
/*     */     {
/* 196 */       if (i > 0) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 201 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 205 */     return (i == 1) || (i == 2) || (i == 3) || (i == 4) || (i == 5) || (i == 6);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getByte(String key)
/*     */   {
/*     */     try
/*     */     {
/* 216 */       return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getByte();
/*     */     }
/*     */     catch (ClassCastException var3) {}
/*     */     
/* 220 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public short getShort(String key)
/*     */   {
/*     */     try
/*     */     {
/* 231 */       return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getShort();
/*     */     }
/*     */     catch (ClassCastException var3) {}
/*     */     
/* 235 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInteger(String key)
/*     */   {
/*     */     try
/*     */     {
/* 246 */       return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getInt();
/*     */     }
/*     */     catch (ClassCastException var3) {}
/*     */     
/* 250 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLong(String key)
/*     */   {
/*     */     try
/*     */     {
/* 261 */       return !hasKey(key, 99) ? 0L : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getLong();
/*     */     }
/*     */     catch (ClassCastException var3) {}
/*     */     
/* 265 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getFloat(String key)
/*     */   {
/*     */     try
/*     */     {
/* 276 */       return !hasKey(key, 99) ? 0.0F : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getFloat();
/*     */     }
/*     */     catch (ClassCastException var3) {}
/*     */     
/* 280 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getDouble(String key)
/*     */   {
/*     */     try
/*     */     {
/* 291 */       return !hasKey(key, 99) ? 0.0D : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getDouble();
/*     */     }
/*     */     catch (ClassCastException var3) {}
/*     */     
/* 295 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getString(String key)
/*     */   {
/*     */     try
/*     */     {
/* 306 */       return !hasKey(key, 8) ? "" : ((NBTBase)this.tagMap.get(key)).getString();
/*     */     }
/*     */     catch (ClassCastException var3) {}
/*     */     
/* 310 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getByteArray(String key)
/*     */   {
/*     */     try
/*     */     {
/* 321 */       return !hasKey(key, 7) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
/*     */     }
/*     */     catch (ClassCastException classcastexception)
/*     */     {
/* 325 */       throw new ReportedException(createCrashReport(key, 7, classcastexception));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getIntArray(String key)
/*     */   {
/*     */     try
/*     */     {
/* 336 */       return !hasKey(key, 11) ? new int[0] : ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
/*     */     }
/*     */     catch (ClassCastException classcastexception)
/*     */     {
/* 340 */       throw new ReportedException(createCrashReport(key, 11, classcastexception));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getCompoundTag(String key)
/*     */   {
/*     */     try
/*     */     {
/* 352 */       return !hasKey(key, 10) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(key);
/*     */     }
/*     */     catch (ClassCastException classcastexception)
/*     */     {
/* 356 */       throw new ReportedException(createCrashReport(key, 10, classcastexception));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagList getTagList(String key, int type)
/*     */   {
/*     */     try
/*     */     {
/* 367 */       if (getTagId(key) != 9)
/*     */       {
/* 369 */         return new NBTTagList();
/*     */       }
/*     */       
/*     */ 
/* 373 */       NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);
/* 374 */       return (nbttaglist.tagCount() > 0) && (nbttaglist.getTagType() != type) ? new NBTTagList() : nbttaglist;
/*     */ 
/*     */     }
/*     */     catch (ClassCastException classcastexception)
/*     */     {
/* 379 */       throw new ReportedException(createCrashReport(key, 9, classcastexception));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getBoolean(String key)
/*     */   {
/* 389 */     return getByte(key) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeTag(String key)
/*     */   {
/* 397 */     this.tagMap.remove(key);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 402 */     StringBuilder stringbuilder = new StringBuilder("{");
/*     */     
/* 404 */     for (Map.Entry<String, NBTBase> entry : this.tagMap.entrySet())
/*     */     {
/* 406 */       if (stringbuilder.length() != 1)
/*     */       {
/* 408 */         stringbuilder.append(',');
/*     */       }
/*     */       
/* 411 */       stringbuilder.append((String)entry.getKey()).append(':').append(entry.getValue());
/*     */     }
/*     */     
/* 414 */     return '}';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNoTags()
/*     */   {
/* 422 */     return this.tagMap.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex)
/*     */   {
/* 430 */     CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
/* 431 */     CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
/* 432 */     crashreportcategory.addCrashSectionCallable("Tag type found", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 436 */         return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.this.tagMap.get(key)).getId()];
/*     */       }
/* 438 */     });
/* 439 */     crashreportcategory.addCrashSectionCallable("Tag type expected", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 443 */         return NBTBase.NBT_TYPES[expectedType];
/*     */       }
/* 445 */     });
/* 446 */     crashreportcategory.addCrashSection("Tag name", key);
/* 447 */     return crashreport;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase copy()
/*     */   {
/* 455 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */     
/* 457 */     for (String s : this.tagMap.keySet())
/*     */     {
/* 459 */       nbttagcompound.setTag(s, ((NBTBase)this.tagMap.get(s)).copy());
/*     */     }
/*     */     
/* 462 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 467 */     if (super.equals(p_equals_1_))
/*     */     {
/* 469 */       NBTTagCompound nbttagcompound = (NBTTagCompound)p_equals_1_;
/* 470 */       return this.tagMap.entrySet().equals(nbttagcompound.tagMap.entrySet());
/*     */     }
/*     */     
/*     */ 
/* 474 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 480 */     return super.hashCode() ^ this.tagMap.hashCode();
/*     */   }
/*     */   
/*     */   private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException
/*     */   {
/* 485 */     output.writeByte(data.getId());
/*     */     
/* 487 */     if (data.getId() != 0)
/*     */     {
/* 489 */       output.writeUTF(name);
/* 490 */       data.write(output);
/*     */     }
/*     */   }
/*     */   
/*     */   private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/* 496 */     return input.readByte();
/*     */   }
/*     */   
/*     */   private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/* 501 */     return input.readUTF();
/*     */   }
/*     */   
/*     */   static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/* 506 */     NBTBase nbtbase = NBTBase.createNewByType(id);
/*     */     
/*     */     try
/*     */     {
/* 510 */       nbtbase.read(input, depth, sizeTracker);
/* 511 */       return nbtbase;
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 515 */       CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
/* 516 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
/* 517 */       crashreportcategory.addCrashSection("Tag name", key);
/* 518 */       crashreportcategory.addCrashSection("Tag type", Byte.valueOf(id));
/* 519 */       throw new ReportedException(crashreport);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void merge(NBTTagCompound other)
/*     */   {
/* 529 */     for (String s : other.tagMap.keySet())
/*     */     {
/* 531 */       NBTBase nbtbase = (NBTBase)other.tagMap.get(s);
/*     */       
/* 533 */       if (nbtbase.getId() == 10)
/*     */       {
/* 535 */         if (hasKey(s, 10))
/*     */         {
/* 537 */           NBTTagCompound nbttagcompound = getCompoundTag(s);
/* 538 */           nbttagcompound.merge((NBTTagCompound)nbtbase);
/*     */         }
/*     */         else
/*     */         {
/* 542 */           setTag(s, nbtbase.copy());
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/* 547 */         setTag(s, nbtbase.copy());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagCompound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */