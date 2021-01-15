/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ public class GenLayerAddIsland extends GenLayer
/*     */ {
/*     */   public GenLayerAddIsland(long p_i2119_1_, GenLayer p_i2119_3_)
/*     */   {
/*   7 */     super(p_i2119_1_);
/*   8 */     this.parent = p_i2119_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*     */   {
/*  17 */     int i = areaX - 1;
/*  18 */     int j = areaY - 1;
/*  19 */     int k = areaWidth + 2;
/*  20 */     int l = areaHeight + 2;
/*  21 */     int[] aint = this.parent.getInts(i, j, k, l);
/*  22 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  24 */     for (int i1 = 0; i1 < areaHeight; i1++)
/*     */     {
/*  26 */       for (int j1 = 0; j1 < areaWidth; j1++)
/*     */       {
/*  28 */         int k1 = aint[(j1 + 0 + (i1 + 0) * k)];
/*  29 */         int l1 = aint[(j1 + 2 + (i1 + 0) * k)];
/*  30 */         int i2 = aint[(j1 + 0 + (i1 + 2) * k)];
/*  31 */         int j2 = aint[(j1 + 2 + (i1 + 2) * k)];
/*  32 */         int k2 = aint[(j1 + 1 + (i1 + 1) * k)];
/*  33 */         initChunkSeed(j1 + areaX, i1 + areaY);
/*     */         
/*  35 */         if ((k2 != 0) || ((k1 == 0) && (l1 == 0) && (i2 == 0) && (j2 == 0)))
/*     */         {
/*  37 */           if ((k2 > 0) && ((k1 == 0) || (l1 == 0) || (i2 == 0) || (j2 == 0)))
/*     */           {
/*  39 */             if (nextInt(5) == 0)
/*     */             {
/*  41 */               if (k2 == 4)
/*     */               {
/*  43 */                 aint1[(j1 + i1 * areaWidth)] = 4;
/*     */               }
/*     */               else
/*     */               {
/*  47 */                 aint1[(j1 + i1 * areaWidth)] = 0;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/*  52 */               aint1[(j1 + i1 * areaWidth)] = k2;
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/*  57 */             aint1[(j1 + i1 * areaWidth)] = k2;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  62 */           int l2 = 1;
/*  63 */           int i3 = 1;
/*     */           
/*  65 */           if ((k1 != 0) && (nextInt(l2++) == 0))
/*     */           {
/*  67 */             i3 = k1;
/*     */           }
/*     */           
/*  70 */           if ((l1 != 0) && (nextInt(l2++) == 0))
/*     */           {
/*  72 */             i3 = l1;
/*     */           }
/*     */           
/*  75 */           if ((i2 != 0) && (nextInt(l2++) == 0))
/*     */           {
/*  77 */             i3 = i2;
/*     */           }
/*     */           
/*  80 */           if ((j2 != 0) && (nextInt(l2++) == 0))
/*     */           {
/*  82 */             i3 = j2;
/*     */           }
/*     */           
/*  85 */           if (nextInt(3) == 0)
/*     */           {
/*  87 */             aint1[(j1 + i1 * areaWidth)] = i3;
/*     */           }
/*  89 */           else if (i3 == 4)
/*     */           {
/*  91 */             aint1[(j1 + i1 * areaWidth)] = 4;
/*     */           }
/*     */           else
/*     */           {
/*  95 */             aint1[(j1 + i1 * areaWidth)] = 0;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 101 */     return aint1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerAddIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */