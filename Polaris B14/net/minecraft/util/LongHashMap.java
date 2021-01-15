/*     */ package net.minecraft.util;
/*     */ 
/*     */ 
/*     */ public class LongHashMap
/*     */ {
/*   6 */   private transient Entry[] hashArray = new Entry['က'];
/*     */   
/*     */ 
/*     */   private transient int numHashElements;
/*     */   
/*     */ 
/*     */   private int mask;
/*     */   
/*     */ 
/*  15 */   private int capacity = 3072;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  20 */   private final float percentUseable = 0.75F;
/*     */   
/*     */   private volatile transient int modCount;
/*     */   
/*     */   private static final String __OBFID = "CL_00001492";
/*     */   
/*     */   public LongHashMap()
/*     */   {
/*  28 */     this.mask = (this.hashArray.length - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getHashedKey(long originalKey)
/*     */   {
/*  36 */     return (int)(originalKey ^ originalKey >>> 27);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int hash(int integer)
/*     */   {
/*  44 */     integer = integer ^ integer >>> 20 ^ integer >>> 12;
/*  45 */     return integer ^ integer >>> 7 ^ integer >>> 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getHashIndex(int p_76158_0_, int p_76158_1_)
/*     */   {
/*  53 */     return p_76158_0_ & p_76158_1_;
/*     */   }
/*     */   
/*     */   public int getNumHashElements()
/*     */   {
/*  58 */     return this.numHashElements;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getValueByKey(long p_76164_1_)
/*     */   {
/*  66 */     int i = getHashedKey(p_76164_1_);
/*     */     
/*  68 */     for (Entry longhashmap$entry = this.hashArray[getHashIndex(i, this.mask)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry)
/*     */     {
/*  70 */       if (longhashmap$entry.key == p_76164_1_)
/*     */       {
/*  72 */         return longhashmap$entry.value;
/*     */       }
/*     */     }
/*     */     
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public boolean containsItem(long p_76161_1_)
/*     */   {
/*  81 */     return getEntry(p_76161_1_) != null;
/*     */   }
/*     */   
/*     */   final Entry getEntry(long p_76160_1_)
/*     */   {
/*  86 */     int i = getHashedKey(p_76160_1_);
/*     */     
/*  88 */     for (Entry longhashmap$entry = this.hashArray[getHashIndex(i, this.mask)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry)
/*     */     {
/*  90 */       if (longhashmap$entry.key == p_76160_1_)
/*     */       {
/*  92 */         return longhashmap$entry;
/*     */       }
/*     */     }
/*     */     
/*  96 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(long p_76163_1_, Object p_76163_3_)
/*     */   {
/* 104 */     int i = getHashedKey(p_76163_1_);
/* 105 */     int j = getHashIndex(i, this.mask);
/*     */     
/* 107 */     for (Entry longhashmap$entry = this.hashArray[j]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry)
/*     */     {
/* 109 */       if (longhashmap$entry.key == p_76163_1_)
/*     */       {
/* 111 */         longhashmap$entry.value = p_76163_3_;
/* 112 */         return;
/*     */       }
/*     */     }
/*     */     
/* 116 */     this.modCount += 1;
/* 117 */     createKey(i, p_76163_1_, p_76163_3_, j);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void resizeTable(int p_76153_1_)
/*     */   {
/* 125 */     Entry[] alonghashmap$entry = this.hashArray;
/* 126 */     int i = alonghashmap$entry.length;
/*     */     
/* 128 */     if (i == 1073741824)
/*     */     {
/* 130 */       this.capacity = Integer.MAX_VALUE;
/*     */     }
/*     */     else
/*     */     {
/* 134 */       Entry[] alonghashmap$entry1 = new Entry[p_76153_1_];
/* 135 */       copyHashTableTo(alonghashmap$entry1);
/* 136 */       this.hashArray = alonghashmap$entry1;
/* 137 */       this.mask = (this.hashArray.length - 1);
/* 138 */       float f = p_76153_1_;
/* 139 */       getClass();
/* 140 */       this.capacity = ((int)(f * 0.75F));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void copyHashTableTo(Entry[] p_76154_1_)
/*     */   {
/* 149 */     Entry[] alonghashmap$entry = this.hashArray;
/* 150 */     int i = p_76154_1_.length;
/*     */     
/* 152 */     for (int j = 0; j < alonghashmap$entry.length; j++)
/*     */     {
/* 154 */       Entry longhashmap$entry = alonghashmap$entry[j];
/*     */       
/* 156 */       if (longhashmap$entry != null)
/*     */       {
/* 158 */         alonghashmap$entry[j] = null;
/*     */         Entry longhashmap$entry1;
/*     */         do
/*     */         {
/* 162 */           longhashmap$entry1 = longhashmap$entry.nextEntry;
/* 163 */           int k = getHashIndex(longhashmap$entry.hash, i - 1);
/* 164 */           longhashmap$entry.nextEntry = p_76154_1_[k];
/* 165 */           p_76154_1_[k] = longhashmap$entry;
/* 166 */           longhashmap$entry = longhashmap$entry1;
/*     */         }
/* 168 */         while (longhashmap$entry1 != null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object remove(long p_76159_1_)
/*     */   {
/* 182 */     Entry longhashmap$entry = removeKey(p_76159_1_);
/* 183 */     return longhashmap$entry == null ? null : longhashmap$entry.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final Entry removeKey(long p_76152_1_)
/*     */   {
/* 191 */     int i = getHashedKey(p_76152_1_);
/* 192 */     int j = getHashIndex(i, this.mask);
/* 193 */     Entry longhashmap$entry = this.hashArray[j];
/*     */     
/*     */     Entry longhashmap$entry2;
/*     */     
/* 197 */     for (Entry longhashmap$entry1 = longhashmap$entry; longhashmap$entry1 != null; longhashmap$entry1 = longhashmap$entry2)
/*     */     {
/* 199 */       longhashmap$entry2 = longhashmap$entry1.nextEntry;
/*     */       
/* 201 */       if (longhashmap$entry1.key == p_76152_1_)
/*     */       {
/* 203 */         this.modCount += 1;
/* 204 */         this.numHashElements -= 1;
/*     */         
/* 206 */         if (longhashmap$entry == longhashmap$entry1)
/*     */         {
/* 208 */           this.hashArray[j] = longhashmap$entry2;
/*     */         }
/*     */         else
/*     */         {
/* 212 */           longhashmap$entry.nextEntry = longhashmap$entry2;
/*     */         }
/*     */         
/* 215 */         return longhashmap$entry1;
/*     */       }
/*     */       
/* 218 */       longhashmap$entry = longhashmap$entry1;
/*     */     }
/*     */     
/* 221 */     return longhashmap$entry1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void createKey(int p_76156_1_, long p_76156_2_, Object p_76156_4_, int p_76156_5_)
/*     */   {
/* 229 */     Entry longhashmap$entry = this.hashArray[p_76156_5_];
/* 230 */     this.hashArray[p_76156_5_] = new Entry(p_76156_1_, p_76156_2_, p_76156_4_, longhashmap$entry);
/*     */     
/* 232 */     if (this.numHashElements++ >= this.capacity)
/*     */     {
/* 234 */       resizeTable(2 * this.hashArray.length);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getKeyDistribution()
/*     */   {
/* 240 */     int i = 0;
/*     */     
/* 242 */     for (int j = 0; j < this.hashArray.length; j++)
/*     */     {
/* 244 */       if (this.hashArray[j] != null)
/*     */       {
/* 246 */         i++;
/*     */       }
/*     */     }
/*     */     
/* 250 */     return 1.0D * i / this.numHashElements;
/*     */   }
/*     */   
/*     */   static class Entry
/*     */   {
/*     */     final long key;
/*     */     Object value;
/*     */     Entry nextEntry;
/*     */     final int hash;
/*     */     private static final String __OBFID = "CL_00001493";
/*     */     
/*     */     Entry(int p_i1553_1_, long p_i1553_2_, Object p_i1553_4_, Entry p_i1553_5_)
/*     */     {
/* 263 */       this.value = p_i1553_4_;
/* 264 */       this.nextEntry = p_i1553_5_;
/* 265 */       this.key = p_i1553_2_;
/* 266 */       this.hash = p_i1553_1_;
/*     */     }
/*     */     
/*     */     public final long getKey()
/*     */     {
/* 271 */       return this.key;
/*     */     }
/*     */     
/*     */     public final Object getValue()
/*     */     {
/* 276 */       return this.value;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object p_equals_1_)
/*     */     {
/* 281 */       if (!(p_equals_1_ instanceof Entry))
/*     */       {
/* 283 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 287 */       Entry longhashmap$entry = (Entry)p_equals_1_;
/* 288 */       Long olong = Long.valueOf(getKey());
/* 289 */       Long olong1 = Long.valueOf(longhashmap$entry.getKey());
/*     */       
/* 291 */       if ((olong == olong1) || ((olong != null) && (olong.equals(olong1))))
/*     */       {
/* 293 */         Object object = getValue();
/* 294 */         Object object1 = longhashmap$entry.getValue();
/*     */         
/* 296 */         if ((object == object1) || ((object != null) && (object.equals(object1))))
/*     */         {
/* 298 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 302 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 308 */       return LongHashMap.getHashedKey(this.key);
/*     */     }
/*     */     
/*     */     public final String toString()
/*     */     {
/* 313 */       return getKey() + "=" + getValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\LongHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */