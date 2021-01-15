/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings.Factory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenLayer
/*     */ {
/*     */   private long worldGenSeed;
/*     */   protected GenLayer parent;
/*     */   private long chunkSeed;
/*     */   protected long baseSeed;
/*     */   
/*     */   public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType p_180781_2_, String p_180781_3_)
/*     */   {
/*  30 */     GenLayer genlayer = new GenLayerIsland(1L);
/*  31 */     genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
/*  32 */     GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
/*  33 */     GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
/*  34 */     GenLayerAddIsland genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
/*  35 */     genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
/*  36 */     genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
/*  37 */     GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
/*  38 */     GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
/*  39 */     GenLayerAddIsland genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
/*  40 */     GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
/*  41 */     genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
/*  42 */     genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
/*  43 */     GenLayerZoom genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
/*  44 */     genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
/*  45 */     GenLayerAddIsland genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
/*  46 */     GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
/*  47 */     GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
/*  48 */     GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
/*  49 */     ChunkProviderSettings chunkprovidersettings = null;
/*  50 */     int i = 4;
/*  51 */     int j = i;
/*     */     
/*  53 */     if ((p_180781_2_ == WorldType.CUSTOMIZED) && (p_180781_3_.length() > 0))
/*     */     {
/*  55 */       chunkprovidersettings = ChunkProviderSettings.Factory.jsonToFactory(p_180781_3_).func_177864_b();
/*  56 */       i = chunkprovidersettings.biomeSize;
/*  57 */       j = chunkprovidersettings.riverSize;
/*     */     }
/*     */     
/*  60 */     if (p_180781_2_ == WorldType.LARGE_BIOMES)
/*     */     {
/*  62 */       i = 6;
/*     */     }
/*     */     
/*  65 */     GenLayer lvt_8_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
/*  66 */     GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, lvt_8_1_);
/*  67 */     GenLayerBiome lvt_9_1_ = new GenLayerBiome(200L, genlayer4, p_180781_2_, p_180781_3_);
/*  68 */     GenLayer genlayer6 = GenLayerZoom.magnify(1000L, lvt_9_1_, 2);
/*  69 */     GenLayerBiomeEdge genlayerbiomeedge = new GenLayerBiomeEdge(1000L, genlayer6);
/*  70 */     GenLayer lvt_10_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
/*  71 */     GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_10_1_);
/*  72 */     GenLayer genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
/*  73 */     genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, j);
/*  74 */     GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer5);
/*  75 */     GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
/*  76 */     genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);
/*     */     
/*  78 */     for (int k = 0; k < i; k++)
/*     */     {
/*  80 */       genlayerhills = new GenLayerZoom(1000 + k, genlayerhills);
/*     */       
/*  82 */       if (k == 0)
/*     */       {
/*  84 */         genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
/*     */       }
/*     */       
/*  87 */       if ((k == 1) || (i == 1))
/*     */       {
/*  89 */         genlayerhills = new GenLayerShore(1000L, genlayerhills);
/*     */       }
/*     */     }
/*     */     
/*  93 */     GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, genlayerhills);
/*  94 */     GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
/*  95 */     GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
/*  96 */     genlayerrivermix.initWorldGenSeed(seed);
/*  97 */     genlayer3.initWorldGenSeed(seed);
/*  98 */     return new GenLayer[] { genlayerrivermix, genlayer3, genlayerrivermix };
/*     */   }
/*     */   
/*     */   public GenLayer(long p_i2125_1_)
/*     */   {
/* 103 */     this.baseSeed = p_i2125_1_;
/* 104 */     this.baseSeed *= (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
/* 105 */     this.baseSeed += p_i2125_1_;
/* 106 */     this.baseSeed *= (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
/* 107 */     this.baseSeed += p_i2125_1_;
/* 108 */     this.baseSeed *= (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
/* 109 */     this.baseSeed += p_i2125_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initWorldGenSeed(long seed)
/*     */   {
/* 118 */     this.worldGenSeed = seed;
/*     */     
/* 120 */     if (this.parent != null)
/*     */     {
/* 122 */       this.parent.initWorldGenSeed(seed);
/*     */     }
/*     */     
/* 125 */     this.worldGenSeed *= (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
/* 126 */     this.worldGenSeed += this.baseSeed;
/* 127 */     this.worldGenSeed *= (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
/* 128 */     this.worldGenSeed += this.baseSeed;
/* 129 */     this.worldGenSeed *= (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
/* 130 */     this.worldGenSeed += this.baseSeed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initChunkSeed(long p_75903_1_, long p_75903_3_)
/*     */   {
/* 138 */     this.chunkSeed = this.worldGenSeed;
/* 139 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 140 */     this.chunkSeed += p_75903_1_;
/* 141 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 142 */     this.chunkSeed += p_75903_3_;
/* 143 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 144 */     this.chunkSeed += p_75903_1_;
/* 145 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 146 */     this.chunkSeed += p_75903_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int nextInt(int p_75902_1_)
/*     */   {
/* 154 */     int i = (int)((this.chunkSeed >> 24) % p_75902_1_);
/*     */     
/* 156 */     if (i < 0)
/*     */     {
/* 158 */       i += p_75902_1_;
/*     */     }
/*     */     
/* 161 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 162 */     this.chunkSeed += this.worldGenSeed;
/* 163 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int[] getInts(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */ 
/*     */ 
/*     */   protected static boolean biomesEqualOrMesaPlateau(int biomeIDA, int biomeIDB)
/*     */   {
/* 174 */     if (biomeIDA == biomeIDB)
/*     */     {
/* 176 */       return true;
/*     */     }
/* 178 */     if ((biomeIDA != BiomeGenBase.mesaPlateau_F.biomeID) && (biomeIDA != BiomeGenBase.mesaPlateau.biomeID))
/*     */     {
/* 180 */       BiomeGenBase biomegenbase = BiomeGenBase.getBiome(biomeIDA);
/* 181 */       BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(biomeIDB);
/*     */       
/*     */       try
/*     */       {
/* 185 */         return (biomegenbase != null) && (biomegenbase1 != null) ? biomegenbase.isEqualTo(biomegenbase1) : false;
/*     */       }
/*     */       catch (Throwable throwable)
/*     */       {
/* 189 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
/* 190 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
/* 191 */         crashreportcategory.addCrashSection("Biome A ID", Integer.valueOf(biomeIDA));
/* 192 */         crashreportcategory.addCrashSection("Biome B ID", Integer.valueOf(biomeIDB));
/* 193 */         crashreportcategory.addCrashSectionCallable("Biome A", new Callable()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 197 */             return String.valueOf(GenLayer.this);
/*     */           }
/* 199 */         });
/* 200 */         crashreportcategory.addCrashSectionCallable("Biome B", new Callable()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 204 */             return String.valueOf(GenLayer.this);
/*     */           }
/* 206 */         });
/* 207 */         throw new ReportedException(crashreport);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 212 */     return (biomeIDB == BiomeGenBase.mesaPlateau_F.biomeID) || (biomeIDB == BiomeGenBase.mesaPlateau.biomeID);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static boolean isBiomeOceanic(int p_151618_0_)
/*     */   {
/* 221 */     return (p_151618_0_ == BiomeGenBase.ocean.biomeID) || (p_151618_0_ == BiomeGenBase.deepOcean.biomeID) || (p_151618_0_ == BiomeGenBase.frozenOcean.biomeID);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int selectRandom(int... p_151619_1_)
/*     */   {
/* 229 */     return p_151619_1_[nextInt(p_151619_1_.length)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_)
/*     */   {
/* 237 */     return (p_151617_3_ == p_151617_4_) && (p_151617_1_ != p_151617_2_) ? p_151617_3_ : (p_151617_2_ == p_151617_4_) && (p_151617_1_ != p_151617_3_) ? p_151617_2_ : (p_151617_2_ == p_151617_3_) && (p_151617_1_ != p_151617_4_) ? p_151617_2_ : (p_151617_1_ == p_151617_4_) && (p_151617_2_ != p_151617_3_) ? p_151617_1_ : (p_151617_1_ == p_151617_3_) && (p_151617_2_ != p_151617_4_) ? p_151617_1_ : (p_151617_1_ == p_151617_2_) && (p_151617_3_ != p_151617_4_) ? p_151617_1_ : (p_151617_1_ == p_151617_3_) && (p_151617_1_ == p_151617_4_) ? p_151617_1_ : (p_151617_1_ == p_151617_2_) && (p_151617_1_ == p_151617_4_) ? p_151617_1_ : (p_151617_1_ == p_151617_2_) && (p_151617_1_ == p_151617_3_) ? p_151617_1_ : (p_151617_2_ == p_151617_3_) && (p_151617_3_ == p_151617_4_) ? p_151617_2_ : selectRandom(new int[] { p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_ });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */