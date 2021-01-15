/*     */ package optfine;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ public class Mipmaps
/*     */ {
/*     */   private final String iconName;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final int[] data;
/*     */   private final boolean direct;
/*     */   private int[][] mipmapDatas;
/*     */   private IntBuffer[] mipmapBuffers;
/*     */   private Dimension[] mipmapDimensions;
/*     */   
/*     */   public Mipmaps(String p_i42_1_, int p_i42_2_, int p_i42_3_, int[] p_i42_4_, boolean p_i42_5_)
/*     */   {
/*  24 */     this.iconName = p_i42_1_;
/*  25 */     this.width = p_i42_2_;
/*  26 */     this.height = p_i42_3_;
/*  27 */     this.data = p_i42_4_;
/*  28 */     this.direct = p_i42_5_;
/*  29 */     this.mipmapDimensions = makeMipmapDimensions(p_i42_2_, p_i42_3_, p_i42_1_);
/*  30 */     this.mipmapDatas = generateMipMapData(p_i42_4_, p_i42_2_, p_i42_3_, this.mipmapDimensions);
/*     */     
/*  32 */     if (p_i42_5_)
/*     */     {
/*  34 */       this.mipmapBuffers = makeMipmapBuffers(this.mipmapDimensions, this.mipmapDatas);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Dimension[] makeMipmapDimensions(int p_makeMipmapDimensions_0_, int p_makeMipmapDimensions_1_, String p_makeMipmapDimensions_2_)
/*     */   {
/*  40 */     int i = TextureUtils.ceilPowerOfTwo(p_makeMipmapDimensions_0_);
/*  41 */     int j = TextureUtils.ceilPowerOfTwo(p_makeMipmapDimensions_1_);
/*     */     
/*  43 */     if ((i == p_makeMipmapDimensions_0_) && (j == p_makeMipmapDimensions_1_))
/*     */     {
/*  45 */       List list = new ArrayList();
/*  46 */       int k = i;
/*  47 */       int l = j;
/*     */       
/*     */       for (;;)
/*     */       {
/*  51 */         k /= 2;
/*  52 */         l /= 2;
/*     */         
/*  54 */         if ((k <= 0) && (l <= 0))
/*     */         {
/*  56 */           Dimension[] adimension = (Dimension[])list.toArray(new Dimension[list.size()]);
/*  57 */           return adimension;
/*     */         }
/*     */         
/*  60 */         if (k <= 0)
/*     */         {
/*  62 */           k = 1;
/*     */         }
/*     */         
/*  65 */         if (l <= 0)
/*     */         {
/*  67 */           l = 1;
/*     */         }
/*     */         
/*  70 */         int i1 = k * l * 4;
/*  71 */         Dimension dimension = new Dimension(k, l);
/*  72 */         list.add(dimension);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  77 */     Config.warn("Mipmaps not possible (power of 2 dimensions needed), texture: " + p_makeMipmapDimensions_2_ + ", dim: " + p_makeMipmapDimensions_0_ + "x" + p_makeMipmapDimensions_1_);
/*  78 */     return new Dimension[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public static int[][] generateMipMapData(int[] p_generateMipMapData_0_, int p_generateMipMapData_1_, int p_generateMipMapData_2_, Dimension[] p_generateMipMapData_3_)
/*     */   {
/*  84 */     int[] aint = p_generateMipMapData_0_;
/*  85 */     int i = p_generateMipMapData_1_;
/*  86 */     boolean flag = true;
/*  87 */     int[][] aint1 = new int[p_generateMipMapData_3_.length][];
/*     */     
/*  89 */     for (int j = 0; j < p_generateMipMapData_3_.length; j++)
/*     */     {
/*  91 */       Dimension dimension = p_generateMipMapData_3_[j];
/*  92 */       int k = dimension.width;
/*  93 */       int l = dimension.height;
/*  94 */       int[] aint2 = new int[k * l];
/*  95 */       aint1[j] = aint2;
/*  96 */       int i1 = j + 1;
/*     */       
/*  98 */       if (flag)
/*     */       {
/* 100 */         for (int j1 = 0; j1 < k; j1++)
/*     */         {
/* 102 */           for (int k1 = 0; k1 < l; k1++)
/*     */           {
/* 104 */             int l1 = aint[(j1 * 2 + 0 + (k1 * 2 + 0) * i)];
/* 105 */             int i2 = aint[(j1 * 2 + 1 + (k1 * 2 + 0) * i)];
/* 106 */             int j2 = aint[(j1 * 2 + 1 + (k1 * 2 + 1) * i)];
/* 107 */             int k2 = aint[(j1 * 2 + 0 + (k1 * 2 + 1) * i)];
/* 108 */             int l2 = alphaBlend(l1, i2, j2, k2);
/* 109 */             aint2[(j1 + k1 * k)] = l2;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 114 */       aint = aint2;
/* 115 */       i = k;
/*     */       
/* 117 */       if ((k <= 1) || (l <= 1))
/*     */       {
/* 119 */         flag = false;
/*     */       }
/*     */     }
/*     */     
/* 123 */     return aint1;
/*     */   }
/*     */   
/*     */   public static int alphaBlend(int p_alphaBlend_0_, int p_alphaBlend_1_, int p_alphaBlend_2_, int p_alphaBlend_3_)
/*     */   {
/* 128 */     int i = alphaBlend(p_alphaBlend_0_, p_alphaBlend_1_);
/* 129 */     int j = alphaBlend(p_alphaBlend_2_, p_alphaBlend_3_);
/* 130 */     int k = alphaBlend(i, j);
/* 131 */     return k;
/*     */   }
/*     */   
/*     */   private static int alphaBlend(int p_alphaBlend_0_, int p_alphaBlend_1_)
/*     */   {
/* 136 */     int i = (p_alphaBlend_0_ & 0xFF000000) >> 24 & 0xFF;
/* 137 */     int j = (p_alphaBlend_1_ & 0xFF000000) >> 24 & 0xFF;
/* 138 */     int k = (i + j) / 2;
/*     */     
/* 140 */     if ((i == 0) && (j == 0))
/*     */     {
/* 142 */       i = 1;
/* 143 */       j = 1;
/*     */     }
/*     */     else
/*     */     {
/* 147 */       if (i == 0)
/*     */       {
/* 149 */         p_alphaBlend_0_ = p_alphaBlend_1_;
/* 150 */         k /= 2;
/*     */       }
/*     */       
/* 153 */       if (j == 0)
/*     */       {
/* 155 */         p_alphaBlend_1_ = p_alphaBlend_0_;
/* 156 */         k /= 2;
/*     */       }
/*     */     }
/*     */     
/* 160 */     int l = (p_alphaBlend_0_ >> 16 & 0xFF) * i;
/* 161 */     int i1 = (p_alphaBlend_0_ >> 8 & 0xFF) * i;
/* 162 */     int j1 = (p_alphaBlend_0_ & 0xFF) * i;
/* 163 */     int k1 = (p_alphaBlend_1_ >> 16 & 0xFF) * j;
/* 164 */     int l1 = (p_alphaBlend_1_ >> 8 & 0xFF) * j;
/* 165 */     int i2 = (p_alphaBlend_1_ & 0xFF) * j;
/* 166 */     int j2 = (l + k1) / (i + j);
/* 167 */     int k2 = (i1 + l1) / (i + j);
/* 168 */     int l2 = (j1 + i2) / (i + j);
/* 169 */     return k << 24 | j2 << 16 | k2 << 8 | l2;
/*     */   }
/*     */   
/*     */   private int averageColor(int p_averageColor_1_, int p_averageColor_2_)
/*     */   {
/* 174 */     int i = (p_averageColor_1_ & 0xFF000000) >> 24 & 0xFF;
/* 175 */     int j = (p_averageColor_2_ & 0xFF000000) >> 24 & 0xFF;
/* 176 */     return (i + j >> 1 << 24) + ((p_averageColor_1_ & 0xFEFEFE) + (p_averageColor_2_ & 0xFEFEFE) >> 1);
/*     */   }
/*     */   
/*     */   public static IntBuffer[] makeMipmapBuffers(Dimension[] p_makeMipmapBuffers_0_, int[][] p_makeMipmapBuffers_1_)
/*     */   {
/* 181 */     if (p_makeMipmapBuffers_0_ == null)
/*     */     {
/* 183 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 187 */     IntBuffer[] aintbuffer = new IntBuffer[p_makeMipmapBuffers_0_.length];
/*     */     
/* 189 */     for (int i = 0; i < p_makeMipmapBuffers_0_.length; i++)
/*     */     {
/* 191 */       Dimension dimension = p_makeMipmapBuffers_0_[i];
/* 192 */       int j = dimension.width * dimension.height;
/* 193 */       IntBuffer intbuffer = GLAllocation.createDirectIntBuffer(j);
/* 194 */       int[] aint = p_makeMipmapBuffers_1_[i];
/* 195 */       intbuffer.clear();
/* 196 */       intbuffer.put(aint);
/* 197 */       intbuffer.clear();
/* 198 */       aintbuffer[i] = intbuffer;
/*     */     }
/*     */     
/* 201 */     return aintbuffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void allocateMipmapTextures(int p_allocateMipmapTextures_0_, int p_allocateMipmapTextures_1_, String p_allocateMipmapTextures_2_)
/*     */   {
/* 207 */     Dimension[] adimension = makeMipmapDimensions(p_allocateMipmapTextures_0_, p_allocateMipmapTextures_1_, p_allocateMipmapTextures_2_);
/*     */     
/* 209 */     for (int i = 0; i < adimension.length; i++)
/*     */     {
/* 211 */       Dimension dimension = adimension[i];
/* 212 */       int j = dimension.width;
/* 213 */       int k = dimension.height;
/* 214 */       int l = i + 1;
/* 215 */       GL11.glTexImage2D(3553, l, 6408, j, k, 0, 32993, 33639, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\Mipmaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */