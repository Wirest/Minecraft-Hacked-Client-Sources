/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ public class GenLayerEdge extends GenLayer
/*     */ {
/*     */   private final Mode field_151627_c;
/*     */   
/*     */   public GenLayerEdge(long p_i45474_1_, GenLayer p_i45474_3_, Mode p_i45474_4_)
/*     */   {
/*   9 */     super(p_i45474_1_);
/*  10 */     this.parent = p_i45474_3_;
/*  11 */     this.field_151627_c = p_i45474_4_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*     */   {
/*  20 */     switch (this.field_151627_c)
/*     */     {
/*     */     case COOL_WARM: 
/*     */     default: 
/*  24 */       return getIntsCoolWarm(areaX, areaY, areaWidth, areaHeight);
/*     */     
/*     */     case HEAT_ICE: 
/*  27 */       return getIntsHeatIce(areaX, areaY, areaWidth, areaHeight);
/*     */     }
/*     */     
/*  30 */     return getIntsSpecial(areaX, areaY, areaWidth, areaHeight);
/*     */   }
/*     */   
/*     */ 
/*     */   private int[] getIntsCoolWarm(int p_151626_1_, int p_151626_2_, int p_151626_3_, int p_151626_4_)
/*     */   {
/*  36 */     int i = p_151626_1_ - 1;
/*  37 */     int j = p_151626_2_ - 1;
/*  38 */     int k = 1 + p_151626_3_ + 1;
/*  39 */     int l = 1 + p_151626_4_ + 1;
/*  40 */     int[] aint = this.parent.getInts(i, j, k, l);
/*  41 */     int[] aint1 = IntCache.getIntCache(p_151626_3_ * p_151626_4_);
/*     */     
/*  43 */     for (int i1 = 0; i1 < p_151626_4_; i1++)
/*     */     {
/*  45 */       for (int j1 = 0; j1 < p_151626_3_; j1++)
/*     */       {
/*  47 */         initChunkSeed(j1 + p_151626_1_, i1 + p_151626_2_);
/*  48 */         int k1 = aint[(j1 + 1 + (i1 + 1) * k)];
/*     */         
/*  50 */         if (k1 == 1)
/*     */         {
/*  52 */           int l1 = aint[(j1 + 1 + (i1 + 1 - 1) * k)];
/*  53 */           int i2 = aint[(j1 + 1 + 1 + (i1 + 1) * k)];
/*  54 */           int j2 = aint[(j1 + 1 - 1 + (i1 + 1) * k)];
/*  55 */           int k2 = aint[(j1 + 1 + (i1 + 1 + 1) * k)];
/*  56 */           boolean flag = (l1 == 3) || (i2 == 3) || (j2 == 3) || (k2 == 3);
/*  57 */           boolean flag1 = (l1 == 4) || (i2 == 4) || (j2 == 4) || (k2 == 4);
/*     */           
/*  59 */           if ((flag) || (flag1))
/*     */           {
/*  61 */             k1 = 2;
/*     */           }
/*     */         }
/*     */         
/*  65 */         aint1[(j1 + i1 * p_151626_3_)] = k1;
/*     */       }
/*     */     }
/*     */     
/*  69 */     return aint1;
/*     */   }
/*     */   
/*     */   private int[] getIntsHeatIce(int p_151624_1_, int p_151624_2_, int p_151624_3_, int p_151624_4_)
/*     */   {
/*  74 */     int i = p_151624_1_ - 1;
/*  75 */     int j = p_151624_2_ - 1;
/*  76 */     int k = 1 + p_151624_3_ + 1;
/*  77 */     int l = 1 + p_151624_4_ + 1;
/*  78 */     int[] aint = this.parent.getInts(i, j, k, l);
/*  79 */     int[] aint1 = IntCache.getIntCache(p_151624_3_ * p_151624_4_);
/*     */     
/*  81 */     for (int i1 = 0; i1 < p_151624_4_; i1++)
/*     */     {
/*  83 */       for (int j1 = 0; j1 < p_151624_3_; j1++)
/*     */       {
/*  85 */         int k1 = aint[(j1 + 1 + (i1 + 1) * k)];
/*     */         
/*  87 */         if (k1 == 4)
/*     */         {
/*  89 */           int l1 = aint[(j1 + 1 + (i1 + 1 - 1) * k)];
/*  90 */           int i2 = aint[(j1 + 1 + 1 + (i1 + 1) * k)];
/*  91 */           int j2 = aint[(j1 + 1 - 1 + (i1 + 1) * k)];
/*  92 */           int k2 = aint[(j1 + 1 + (i1 + 1 + 1) * k)];
/*  93 */           boolean flag = (l1 == 2) || (i2 == 2) || (j2 == 2) || (k2 == 2);
/*  94 */           boolean flag1 = (l1 == 1) || (i2 == 1) || (j2 == 1) || (k2 == 1);
/*     */           
/*  96 */           if ((flag1) || (flag))
/*     */           {
/*  98 */             k1 = 3;
/*     */           }
/*     */         }
/*     */         
/* 102 */         aint1[(j1 + i1 * p_151624_3_)] = k1;
/*     */       }
/*     */     }
/*     */     
/* 106 */     return aint1;
/*     */   }
/*     */   
/*     */   private int[] getIntsSpecial(int p_151625_1_, int p_151625_2_, int p_151625_3_, int p_151625_4_)
/*     */   {
/* 111 */     int[] aint = this.parent.getInts(p_151625_1_, p_151625_2_, p_151625_3_, p_151625_4_);
/* 112 */     int[] aint1 = IntCache.getIntCache(p_151625_3_ * p_151625_4_);
/*     */     
/* 114 */     for (int i = 0; i < p_151625_4_; i++)
/*     */     {
/* 116 */       for (int j = 0; j < p_151625_3_; j++)
/*     */       {
/* 118 */         initChunkSeed(j + p_151625_1_, i + p_151625_2_);
/* 119 */         int k = aint[(j + i * p_151625_3_)];
/*     */         
/* 121 */         if ((k != 0) && (nextInt(13) == 0))
/*     */         {
/* 123 */           k |= 1 + nextInt(15) << 8 & 0xF00;
/*     */         }
/*     */         
/* 126 */         aint1[(j + i * p_151625_3_)] = k;
/*     */       }
/*     */     }
/*     */     
/* 130 */     return aint1;
/*     */   }
/*     */   
/*     */   public static enum Mode
/*     */   {
/* 135 */     COOL_WARM, 
/* 136 */     HEAT_ICE, 
/* 137 */     SPECIAL;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerEdge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */