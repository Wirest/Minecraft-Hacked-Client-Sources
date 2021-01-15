/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.StitcherException;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class Stitcher
/*     */ {
/*     */   private final int mipmapLevelStitcher;
/*  15 */   private final Set setStitchHolders = Sets.newHashSetWithExpectedSize(256);
/*  16 */   private final List stitchSlots = Lists.newArrayListWithCapacity(256);
/*     */   
/*     */   private int currentWidth;
/*     */   
/*     */   private int currentHeight;
/*     */   private final int maxWidth;
/*     */   private final int maxHeight;
/*     */   private final boolean forcePowerOf2;
/*     */   private final int maxTileDimension;
/*     */   private static final String __OBFID = "CL_00001054";
/*     */   
/*     */   public Stitcher(int maxTextureWidth, int maxTextureHeight, boolean p_i45095_3_, int p_i45095_4_, int mipmapLevel)
/*     */   {
/*  29 */     this.mipmapLevelStitcher = mipmapLevel;
/*  30 */     this.maxWidth = maxTextureWidth;
/*  31 */     this.maxHeight = maxTextureHeight;
/*  32 */     this.forcePowerOf2 = p_i45095_3_;
/*  33 */     this.maxTileDimension = p_i45095_4_;
/*     */   }
/*     */   
/*     */   public int getCurrentWidth()
/*     */   {
/*  38 */     return this.currentWidth;
/*     */   }
/*     */   
/*     */   public int getCurrentHeight()
/*     */   {
/*  43 */     return this.currentHeight;
/*     */   }
/*     */   
/*     */   public void addSprite(TextureAtlasSprite p_110934_1_)
/*     */   {
/*  48 */     Holder stitcher$holder = new Holder(p_110934_1_, this.mipmapLevelStitcher);
/*     */     
/*  50 */     if (this.maxTileDimension > 0)
/*     */     {
/*  52 */       stitcher$holder.setNewDimension(this.maxTileDimension);
/*     */     }
/*     */     
/*  55 */     this.setStitchHolders.add(stitcher$holder);
/*     */   }
/*     */   
/*     */   public void doStitch()
/*     */   {
/*  60 */     Holder[] astitcher$holder = (Holder[])this.setStitchHolders.toArray(new Holder[this.setStitchHolders.size()]);
/*  61 */     Arrays.sort(astitcher$holder);
/*     */     Holder[] arrayOfHolder1;
/*  63 */     int j = (arrayOfHolder1 = astitcher$holder).length; for (int i = 0; i < j; i++) { Holder stitcher$holder = arrayOfHolder1[i];
/*     */       
/*  65 */       if (!allocateSlot(stitcher$holder))
/*     */       {
/*  67 */         String s = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", new Object[] { stitcher$holder.getAtlasSprite().getIconName(), Integer.valueOf(stitcher$holder.getAtlasSprite().getIconWidth()), Integer.valueOf(stitcher$holder.getAtlasSprite().getIconHeight()), Integer.valueOf(this.currentWidth), Integer.valueOf(this.currentHeight), Integer.valueOf(this.maxWidth), Integer.valueOf(this.maxHeight) });
/*  68 */         throw new StitcherException(stitcher$holder, s);
/*     */       }
/*     */     }
/*     */     
/*  72 */     if (this.forcePowerOf2)
/*     */     {
/*  74 */       this.currentWidth = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
/*  75 */       this.currentHeight = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
/*     */     }
/*     */   }
/*     */   
/*     */   public List getStichSlots()
/*     */   {
/*  81 */     ArrayList arraylist = Lists.newArrayList();
/*     */     
/*  83 */     for (Object stitcher$slot : this.stitchSlots)
/*     */     {
/*  85 */       ((Slot)stitcher$slot).getAllStitchSlots(arraylist);
/*     */     }
/*     */     
/*  88 */     ArrayList arraylist1 = Lists.newArrayList();
/*     */     
/*  90 */     for (Object stitcher$slot10 : arraylist)
/*     */     {
/*  92 */       Slot stitcher$slot1 = (Slot)stitcher$slot10;
/*  93 */       Holder stitcher$holder = stitcher$slot1.getStitchHolder();
/*  94 */       TextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();
/*  95 */       textureatlassprite.initSprite(this.currentWidth, this.currentHeight, stitcher$slot1.getOriginX(), stitcher$slot1.getOriginY(), stitcher$holder.isRotated());
/*  96 */       arraylist1.add(textureatlassprite);
/*     */     }
/*     */     
/*  99 */     return arraylist1;
/*     */   }
/*     */   
/*     */   private static int getMipmapDimension(int p_147969_0_, int p_147969_1_)
/*     */   {
/* 104 */     return (p_147969_0_ >> p_147969_1_) + ((p_147969_0_ & (1 << p_147969_1_) - 1) == 0 ? 0 : 1) << p_147969_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean allocateSlot(Holder p_94310_1_)
/*     */   {
/* 112 */     for (int i = 0; i < this.stitchSlots.size(); i++)
/*     */     {
/* 114 */       if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_))
/*     */       {
/* 116 */         return true;
/*     */       }
/*     */       
/* 119 */       p_94310_1_.rotate();
/*     */       
/* 121 */       if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_))
/*     */       {
/* 123 */         return true;
/*     */       }
/*     */       
/* 126 */       p_94310_1_.rotate();
/*     */     }
/*     */     
/* 129 */     return expandAndAllocateSlot(p_94310_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean expandAndAllocateSlot(Holder p_94311_1_)
/*     */   {
/* 137 */     int i = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
/* 138 */     boolean flag = (this.currentWidth == 0) && (this.currentHeight == 0);
/*     */     boolean flag1;
/*     */     boolean flag1;
/* 141 */     if (this.forcePowerOf2)
/*     */     {
/* 143 */       int j = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
/* 144 */       int k = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
/* 145 */       int l = MathHelper.roundUpToPowerOfTwo(this.currentWidth + i);
/* 146 */       int i1 = MathHelper.roundUpToPowerOfTwo(this.currentHeight + i);
/* 147 */       boolean flag2 = l <= this.maxWidth;
/* 148 */       boolean flag3 = i1 <= this.maxHeight;
/*     */       
/* 150 */       if ((!flag2) && (!flag3))
/*     */       {
/* 152 */         return false;
/*     */       }
/*     */       
/* 155 */       boolean flag4 = j != l;
/* 156 */       boolean flag5 = k != i1;
/*     */       boolean flag1;
/* 158 */       if ((flag4 ^ flag5))
/*     */       {
/* 160 */         flag1 = !flag4;
/*     */       }
/*     */       else
/*     */       {
/* 164 */         flag1 = (flag2) && (j <= k);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 169 */       boolean flag6 = this.currentWidth + i <= this.maxWidth;
/* 170 */       boolean flag7 = this.currentHeight + i <= this.maxHeight;
/*     */       
/* 172 */       if ((!flag6) && (!flag7))
/*     */       {
/* 174 */         return false;
/*     */       }
/*     */       
/* 177 */       flag1 = (flag6) && ((flag) || (this.currentWidth <= this.currentHeight));
/*     */     }
/*     */     
/* 180 */     int j1 = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());
/*     */     
/* 182 */     if (MathHelper.roundUpToPowerOfTwo((!flag1 ? this.currentHeight : this.currentWidth) + j1) > (!flag1 ? this.maxHeight : this.maxWidth))
/*     */     {
/* 184 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     Slot stitcher$slot;
/*     */     
/* 190 */     if (flag1)
/*     */     {
/* 192 */       if (p_94311_1_.getWidth() > p_94311_1_.getHeight())
/*     */       {
/* 194 */         p_94311_1_.rotate();
/*     */       }
/*     */       
/* 197 */       if (this.currentHeight == 0)
/*     */       {
/* 199 */         this.currentHeight = p_94311_1_.getHeight();
/*     */       }
/*     */       
/* 202 */       Slot stitcher$slot = new Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
/* 203 */       this.currentWidth += p_94311_1_.getWidth();
/*     */     }
/*     */     else
/*     */     {
/* 207 */       stitcher$slot = new Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
/* 208 */       this.currentHeight += p_94311_1_.getHeight();
/*     */     }
/*     */     
/* 211 */     stitcher$slot.addSlot(p_94311_1_);
/* 212 */     this.stitchSlots.add(stitcher$slot);
/* 213 */     return true;
/*     */   }
/*     */   
/*     */   public static class Holder
/*     */     implements Comparable
/*     */   {
/*     */     private final TextureAtlasSprite theTexture;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private final int mipmapLevelHolder;
/*     */     private boolean rotated;
/* 224 */     private float scaleFactor = 1.0F;
/*     */     private static final String __OBFID = "CL_00001055";
/*     */     
/*     */     public Holder(TextureAtlasSprite p_i45094_1_, int p_i45094_2_)
/*     */     {
/* 229 */       this.theTexture = p_i45094_1_;
/* 230 */       this.width = p_i45094_1_.getIconWidth();
/* 231 */       this.height = p_i45094_1_.getIconHeight();
/* 232 */       this.mipmapLevelHolder = p_i45094_2_;
/* 233 */       this.rotated = (Stitcher.getMipmapDimension(this.height, p_i45094_2_) > Stitcher.getMipmapDimension(this.width, p_i45094_2_));
/*     */     }
/*     */     
/*     */     public TextureAtlasSprite getAtlasSprite()
/*     */     {
/* 238 */       return this.theTexture;
/*     */     }
/*     */     
/*     */     public int getWidth()
/*     */     {
/* 243 */       return this.rotated ? Stitcher.getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder);
/*     */     }
/*     */     
/*     */     public int getHeight()
/*     */     {
/* 248 */       return this.rotated ? Stitcher.getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder);
/*     */     }
/*     */     
/*     */     public void rotate()
/*     */     {
/* 253 */       this.rotated = (!this.rotated);
/*     */     }
/*     */     
/*     */     public boolean isRotated()
/*     */     {
/* 258 */       return this.rotated;
/*     */     }
/*     */     
/*     */     public void setNewDimension(int p_94196_1_)
/*     */     {
/* 263 */       if ((this.width > p_94196_1_) && (this.height > p_94196_1_))
/*     */       {
/* 265 */         this.scaleFactor = (p_94196_1_ / Math.min(this.width, this.height));
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 271 */       return "Holder{width=" + this.width + ", height=" + this.height + '}';
/*     */     }
/*     */     
/*     */     public int compareTo(Holder p_compareTo_1_)
/*     */     {
/*     */       int i;
/*     */       int i;
/* 278 */       if (getHeight() == p_compareTo_1_.getHeight())
/*     */       {
/* 280 */         if (getWidth() == p_compareTo_1_.getWidth())
/*     */         {
/* 282 */           if (this.theTexture.getIconName() == null)
/*     */           {
/* 284 */             return p_compareTo_1_.theTexture.getIconName() == null ? 0 : -1;
/*     */           }
/*     */           
/* 287 */           return this.theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
/*     */         }
/*     */         
/* 290 */         i = getWidth() < p_compareTo_1_.getWidth() ? 1 : -1;
/*     */       }
/*     */       else
/*     */       {
/* 294 */         i = getHeight() < p_compareTo_1_.getHeight() ? 1 : -1;
/*     */       }
/*     */       
/* 297 */       return i;
/*     */     }
/*     */     
/*     */     public int compareTo(Object p_compareTo_1_)
/*     */     {
/* 302 */       return compareTo((Holder)p_compareTo_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Slot
/*     */   {
/*     */     private final int originX;
/*     */     private final int originY;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private List subSlots;
/*     */     private Stitcher.Holder holder;
/*     */     private static final String __OBFID = "CL_00001056";
/*     */     
/*     */     public Slot(int p_i1277_1_, int p_i1277_2_, int widthIn, int heightIn)
/*     */     {
/* 318 */       this.originX = p_i1277_1_;
/* 319 */       this.originY = p_i1277_2_;
/* 320 */       this.width = widthIn;
/* 321 */       this.height = heightIn;
/*     */     }
/*     */     
/*     */     public Stitcher.Holder getStitchHolder()
/*     */     {
/* 326 */       return this.holder;
/*     */     }
/*     */     
/*     */     public int getOriginX()
/*     */     {
/* 331 */       return this.originX;
/*     */     }
/*     */     
/*     */     public int getOriginY()
/*     */     {
/* 336 */       return this.originY;
/*     */     }
/*     */     
/*     */     public boolean addSlot(Stitcher.Holder holderIn)
/*     */     {
/* 341 */       if (this.holder != null)
/*     */       {
/* 343 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 347 */       int i = holderIn.getWidth();
/* 348 */       int j = holderIn.getHeight();
/*     */       
/* 350 */       if ((i <= this.width) && (j <= this.height))
/*     */       {
/* 352 */         if ((i == this.width) && (j == this.height))
/*     */         {
/* 354 */           this.holder = holderIn;
/* 355 */           return true;
/*     */         }
/*     */         
/*     */         int l;
/* 359 */         if (this.subSlots == null)
/*     */         {
/* 361 */           this.subSlots = Lists.newArrayListWithCapacity(1);
/* 362 */           this.subSlots.add(new Slot(this.originX, this.originY, i, j));
/* 363 */           int k = this.width - i;
/* 364 */           l = this.height - j;
/*     */           
/* 366 */           if ((l > 0) && (k > 0))
/*     */           {
/* 368 */             int i1 = Math.max(this.height, k);
/* 369 */             int j1 = Math.max(this.width, l);
/*     */             
/* 371 */             if (i1 >= j1)
/*     */             {
/* 373 */               this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
/* 374 */               this.subSlots.add(new Slot(this.originX + i, this.originY, k, this.height));
/*     */             }
/*     */             else
/*     */             {
/* 378 */               this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
/* 379 */               this.subSlots.add(new Slot(this.originX, this.originY + j, this.width, l));
/*     */             }
/*     */           }
/* 382 */           else if (k == 0)
/*     */           {
/* 384 */             this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
/*     */           }
/* 386 */           else if (l == 0)
/*     */           {
/* 388 */             this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
/*     */           }
/*     */         }
/*     */         
/* 392 */         for (Object stitcher$slot : this.subSlots)
/*     */         {
/* 394 */           if (((Slot)stitcher$slot).addSlot(holderIn))
/*     */           {
/* 396 */             return true;
/*     */           }
/*     */         }
/*     */         
/* 400 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 405 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void getAllStitchSlots(List p_94184_1_)
/*     */     {
/* 412 */       if (this.holder != null)
/*     */       {
/* 414 */         p_94184_1_.add(this);
/*     */       }
/* 416 */       else if (this.subSlots != null)
/*     */       {
/* 418 */         for (Object stitcher$slot : this.subSlots)
/*     */         {
/* 420 */           ((Slot)stitcher$slot).getAllStitchSlots(p_94184_1_);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 427 */       return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\Stitcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */