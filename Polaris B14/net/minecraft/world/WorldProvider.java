/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.ChunkProviderDebug;
/*     */ import net.minecraft.world.gen.ChunkProviderFlat;
/*     */ import net.minecraft.world.gen.ChunkProviderGenerate;
/*     */ import net.minecraft.world.gen.FlatGeneratorInfo;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public abstract class WorldProvider
/*     */ {
/*  19 */   public static final float[] moonPhaseFactors = { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
/*     */   
/*     */ 
/*     */   protected World worldObj;
/*     */   
/*     */ 
/*     */   private WorldType terrainType;
/*     */   
/*     */ 
/*     */   private String generatorSettings;
/*     */   
/*     */ 
/*     */   protected WorldChunkManager worldChunkMgr;
/*     */   
/*     */ 
/*     */   protected boolean isHellWorld;
/*     */   
/*     */ 
/*     */   protected boolean hasNoSky;
/*     */   
/*     */ 
/*  40 */   protected final float[] lightBrightnessTable = new float[16];
/*     */   
/*     */ 
/*     */   protected int dimensionId;
/*     */   
/*     */ 
/*  46 */   private final float[] colorsSunriseSunset = new float[4];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void registerWorld(World worldIn)
/*     */   {
/*  53 */     this.worldObj = worldIn;
/*  54 */     this.terrainType = worldIn.getWorldInfo().getTerrainType();
/*  55 */     this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
/*  56 */     registerWorldChunkManager();
/*  57 */     generateLightBrightnessTable();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void generateLightBrightnessTable()
/*     */   {
/*  65 */     float f = 0.0F;
/*     */     
/*  67 */     for (int i = 0; i <= 15; i++)
/*     */     {
/*  69 */       float f1 = 1.0F - i / 15.0F;
/*  70 */       this.lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void registerWorldChunkManager()
/*     */   {
/*  79 */     WorldType worldtype = this.worldObj.getWorldInfo().getTerrainType();
/*     */     
/*  81 */     if (worldtype == WorldType.FLAT)
/*     */     {
/*  83 */       FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
/*  84 */       this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.getBiomeFromBiomeList(flatgeneratorinfo.getBiome(), BiomeGenBase.field_180279_ad), 0.5F);
/*     */     }
/*  86 */     else if (worldtype == WorldType.DEBUG_WORLD)
/*     */     {
/*  88 */       this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.0F);
/*     */     }
/*     */     else
/*     */     {
/*  92 */       this.worldChunkMgr = new WorldChunkManager(this.worldObj);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChunkProvider createChunkGenerator()
/*     */   {
/* 101 */     return this.terrainType == WorldType.CUSTOMIZED ? new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : this.terrainType == WorldType.DEBUG_WORLD ? new ChunkProviderDebug(this.worldObj) : this.terrainType == WorldType.FLAT ? new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCoordinateBeSpawn(int x, int z)
/*     */   {
/* 109 */     return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == net.minecraft.init.Blocks.grass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_)
/*     */   {
/* 117 */     int i = (int)(p_76563_1_ % 24000L);
/* 118 */     float f = (i + p_76563_3_) / 24000.0F - 0.25F;
/*     */     
/* 120 */     if (f < 0.0F)
/*     */     {
/* 122 */       f += 1.0F;
/*     */     }
/*     */     
/* 125 */     if (f > 1.0F)
/*     */     {
/* 127 */       f -= 1.0F;
/*     */     }
/*     */     
/* 130 */     f = 1.0F - (float)((Math.cos(f * 3.141592653589793D) + 1.0D) / 2.0D);
/* 131 */     f += (f - f) / 3.0F;
/* 132 */     return f;
/*     */   }
/*     */   
/*     */   public int getMoonPhase(long p_76559_1_)
/*     */   {
/* 137 */     return (int)(p_76559_1_ / 24000L % 8L + 8L) % 8;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSurfaceWorld()
/*     */   {
/* 145 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
/*     */   {
/* 153 */     float f = 0.4F;
/* 154 */     float f1 = MathHelper.cos(celestialAngle * 3.1415927F * 2.0F) - 0.0F;
/* 155 */     float f2 = -0.0F;
/*     */     
/* 157 */     if ((f1 >= f2 - f) && (f1 <= f2 + f))
/*     */     {
/* 159 */       float f3 = (f1 - f2) / f * 0.5F + 0.5F;
/* 160 */       float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * 3.1415927F)) * 0.99F;
/* 161 */       f4 *= f4;
/* 162 */       this.colorsSunriseSunset[0] = (f3 * 0.3F + 0.7F);
/* 163 */       this.colorsSunriseSunset[1] = (f3 * f3 * 0.7F + 0.2F);
/* 164 */       this.colorsSunriseSunset[2] = (f3 * f3 * 0.0F + 0.2F);
/* 165 */       this.colorsSunriseSunset[3] = f4;
/* 166 */       return this.colorsSunriseSunset;
/*     */     }
/*     */     
/*     */ 
/* 170 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
/*     */   {
/* 179 */     float f = MathHelper.cos(p_76562_1_ * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 180 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 181 */     float f1 = 0.7529412F;
/* 182 */     float f2 = 0.84705883F;
/* 183 */     float f3 = 1.0F;
/* 184 */     f1 *= (f * 0.94F + 0.06F);
/* 185 */     f2 *= (f * 0.94F + 0.06F);
/* 186 */     f3 *= (f * 0.91F + 0.09F);
/* 187 */     return new Vec3(f1, f2, f3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canRespawnHere()
/*     */   {
/* 195 */     return true;
/*     */   }
/*     */   
/*     */   public static WorldProvider getProviderForDimension(int dimension)
/*     */   {
/* 200 */     return dimension == 1 ? new WorldProviderEnd() : dimension == 0 ? new WorldProviderSurface() : dimension == -1 ? new WorldProviderHell() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getCloudHeight()
/*     */   {
/* 208 */     return 128.0F;
/*     */   }
/*     */   
/*     */   public boolean isSkyColored()
/*     */   {
/* 213 */     return true;
/*     */   }
/*     */   
/*     */   public BlockPos getSpawnCoordinate()
/*     */   {
/* 218 */     return null;
/*     */   }
/*     */   
/*     */   public int getAverageGroundLevel()
/*     */   {
/* 223 */     return this.terrainType == WorldType.FLAT ? 4 : this.worldObj.func_181545_F() + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getVoidFogYFactor()
/*     */   {
/* 233 */     return this.terrainType == WorldType.FLAT ? 1.0D : 0.03125D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesXZShowFog(int x, int z)
/*     */   {
/* 241 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract String getDimensionName();
/*     */   
/*     */ 
/*     */   public abstract String getInternalNameSuffix();
/*     */   
/*     */ 
/*     */   public WorldChunkManager getWorldChunkManager()
/*     */   {
/* 253 */     return this.worldChunkMgr;
/*     */   }
/*     */   
/*     */   public boolean doesWaterVaporize()
/*     */   {
/* 258 */     return this.isHellWorld;
/*     */   }
/*     */   
/*     */   public boolean getHasNoSky()
/*     */   {
/* 263 */     return this.hasNoSky;
/*     */   }
/*     */   
/*     */   public float[] getLightBrightnessTable()
/*     */   {
/* 268 */     return this.lightBrightnessTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDimensionId()
/*     */   {
/* 276 */     return this.dimensionId;
/*     */   }
/*     */   
/*     */   public WorldBorder getWorldBorder()
/*     */   {
/* 281 */     return new WorldBorder();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */