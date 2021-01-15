/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NBTTagList extends NBTBase
/*     */ {
/*  13 */   private static final Logger LOGGER = ;
/*  14 */   private List<NBTBase> tagList = Lists.newArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  19 */   private byte tagType = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   void write(DataOutput output)
/*     */     throws IOException
/*     */   {
/*  26 */     if (!this.tagList.isEmpty())
/*     */     {
/*  28 */       this.tagType = ((NBTBase)this.tagList.get(0)).getId();
/*     */     }
/*     */     else
/*     */     {
/*  32 */       this.tagType = 0;
/*     */     }
/*     */     
/*  35 */     output.writeByte(this.tagType);
/*  36 */     output.writeInt(this.tagList.size());
/*     */     
/*  38 */     for (int i = 0; i < this.tagList.size(); i++)
/*     */     {
/*  40 */       ((NBTBase)this.tagList.get(i)).write(output);
/*     */     }
/*     */   }
/*     */   
/*     */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
/*     */   {
/*  46 */     sizeTracker.read(296L);
/*     */     
/*  48 */     if (depth > 512)
/*     */     {
/*  50 */       throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */     }
/*     */     
/*     */ 
/*  54 */     this.tagType = input.readByte();
/*  55 */     int i = input.readInt();
/*     */     
/*  57 */     if ((this.tagType == 0) && (i > 0))
/*     */     {
/*  59 */       throw new RuntimeException("Missing type on ListTag");
/*     */     }
/*     */     
/*     */ 
/*  63 */     sizeTracker.read(32L * i);
/*  64 */     this.tagList = Lists.newArrayListWithCapacity(i);
/*     */     
/*  66 */     for (int j = 0; j < i; j++)
/*     */     {
/*  68 */       NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
/*  69 */       nbtbase.read(input, depth + 1, sizeTracker);
/*  70 */       this.tagList.add(nbtbase);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getId()
/*     */   {
/*  81 */     return 9;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  86 */     StringBuilder stringbuilder = new StringBuilder("[");
/*     */     
/*  88 */     for (int i = 0; i < this.tagList.size(); i++)
/*     */     {
/*  90 */       if (i != 0)
/*     */       {
/*  92 */         stringbuilder.append(',');
/*     */       }
/*     */       
/*  95 */       stringbuilder.append(i).append(':').append(this.tagList.get(i));
/*     */     }
/*     */     
/*  98 */     return ']';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendTag(NBTBase nbt)
/*     */   {
/* 107 */     if (nbt.getId() == 0)
/*     */     {
/* 109 */       LOGGER.warn("Invalid TagEnd added to ListTag");
/*     */     }
/*     */     else
/*     */     {
/* 113 */       if (this.tagType == 0)
/*     */       {
/* 115 */         this.tagType = nbt.getId();
/*     */       }
/* 117 */       else if (this.tagType != nbt.getId())
/*     */       {
/* 119 */         LOGGER.warn("Adding mismatching tag types to tag list");
/* 120 */         return;
/*     */       }
/*     */       
/* 123 */       this.tagList.add(nbt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void set(int idx, NBTBase nbt)
/*     */   {
/* 132 */     if (nbt.getId() == 0)
/*     */     {
/* 134 */       LOGGER.warn("Invalid TagEnd added to ListTag");
/*     */     }
/* 136 */     else if ((idx >= 0) && (idx < this.tagList.size()))
/*     */     {
/* 138 */       if (this.tagType == 0)
/*     */       {
/* 140 */         this.tagType = nbt.getId();
/*     */       }
/* 142 */       else if (this.tagType != nbt.getId())
/*     */       {
/* 144 */         LOGGER.warn("Adding mismatching tag types to tag list");
/* 145 */         return;
/*     */       }
/*     */       
/* 148 */       this.tagList.set(idx, nbt);
/*     */     }
/*     */     else
/*     */     {
/* 152 */       LOGGER.warn("index out of bounds to set tag in tag list");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase removeTag(int i)
/*     */   {
/* 161 */     return (NBTBase)this.tagList.remove(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNoTags()
/*     */   {
/* 169 */     return this.tagList.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getCompoundTagAt(int i)
/*     */   {
/* 177 */     if ((i >= 0) && (i < this.tagList.size()))
/*     */     {
/* 179 */       NBTBase nbtbase = (NBTBase)this.tagList.get(i);
/* 180 */       return nbtbase.getId() == 10 ? (NBTTagCompound)nbtbase : new NBTTagCompound();
/*     */     }
/*     */     
/*     */ 
/* 184 */     return new NBTTagCompound();
/*     */   }
/*     */   
/*     */ 
/*     */   public int[] getIntArrayAt(int i)
/*     */   {
/* 190 */     if ((i >= 0) && (i < this.tagList.size()))
/*     */     {
/* 192 */       NBTBase nbtbase = (NBTBase)this.tagList.get(i);
/* 193 */       return nbtbase.getId() == 11 ? ((NBTTagIntArray)nbtbase).getIntArray() : new int[0];
/*     */     }
/*     */     
/*     */ 
/* 197 */     return new int[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public double getDoubleAt(int i)
/*     */   {
/* 203 */     if ((i >= 0) && (i < this.tagList.size()))
/*     */     {
/* 205 */       NBTBase nbtbase = (NBTBase)this.tagList.get(i);
/* 206 */       return nbtbase.getId() == 6 ? ((NBTTagDouble)nbtbase).getDouble() : 0.0D;
/*     */     }
/*     */     
/*     */ 
/* 210 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public float getFloatAt(int i)
/*     */   {
/* 216 */     if ((i >= 0) && (i < this.tagList.size()))
/*     */     {
/* 218 */       NBTBase nbtbase = (NBTBase)this.tagList.get(i);
/* 219 */       return nbtbase.getId() == 5 ? ((NBTTagFloat)nbtbase).getFloat() : 0.0F;
/*     */     }
/*     */     
/*     */ 
/* 223 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStringTagAt(int i)
/*     */   {
/* 232 */     if ((i >= 0) && (i < this.tagList.size()))
/*     */     {
/* 234 */       NBTBase nbtbase = (NBTBase)this.tagList.get(i);
/* 235 */       return nbtbase.getId() == 8 ? nbtbase.getString() : nbtbase.toString();
/*     */     }
/*     */     
/*     */ 
/* 239 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase get(int idx)
/*     */   {
/* 248 */     return (idx >= 0) && (idx < this.tagList.size()) ? (NBTBase)this.tagList.get(idx) : new NBTTagEnd();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tagCount()
/*     */   {
/* 256 */     return this.tagList.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTBase copy()
/*     */   {
/* 264 */     NBTTagList nbttaglist = new NBTTagList();
/* 265 */     nbttaglist.tagType = this.tagType;
/*     */     
/* 267 */     for (NBTBase nbtbase : this.tagList)
/*     */     {
/* 269 */       NBTBase nbtbase1 = nbtbase.copy();
/* 270 */       nbttaglist.tagList.add(nbtbase1);
/*     */     }
/*     */     
/* 273 */     return nbttaglist;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 278 */     if (super.equals(p_equals_1_))
/*     */     {
/* 280 */       NBTTagList nbttaglist = (NBTTagList)p_equals_1_;
/*     */       
/* 282 */       if (this.tagType == nbttaglist.tagType)
/*     */       {
/* 284 */         return this.tagList.equals(nbttaglist.tagList);
/*     */       }
/*     */     }
/*     */     
/* 288 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 293 */     return super.hashCode() ^ this.tagList.hashCode();
/*     */   }
/*     */   
/*     */   public int getTagType()
/*     */   {
/* 298 */     return this.tagType;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTTagList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */