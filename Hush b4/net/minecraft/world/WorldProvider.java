// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.world.border.WorldBorder;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.biome.WorldChunkManager;

public abstract class WorldProvider
{
    public static final float[] moonPhaseFactors;
    protected World worldObj;
    private WorldType terrainType;
    private String generatorSettings;
    protected WorldChunkManager worldChunkMgr;
    protected boolean isHellWorld;
    protected boolean hasNoSky;
    protected final float[] lightBrightnessTable;
    protected int dimensionId;
    private final float[] colorsSunriseSunset;
    
    static {
        moonPhaseFactors = new float[] { 1.0f, 0.75f, 0.5f, 0.25f, 0.0f, 0.25f, 0.5f, 0.75f };
    }
    
    public WorldProvider() {
        this.lightBrightnessTable = new float[16];
        this.colorsSunriseSunset = new float[4];
    }
    
    public final void registerWorld(final World worldIn) {
        this.worldObj = worldIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
        this.registerWorldChunkManager();
        this.generateLightBrightnessTable();
    }
    
    protected void generateLightBrightnessTable() {
        final float f = 0.0f;
        for (int i = 0; i <= 15; ++i) {
            final float f2 = 1.0f - i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * (1.0f - f) + f;
        }
    }
    
    protected void registerWorldChunkManager() {
        final WorldType worldtype = this.worldObj.getWorldInfo().getTerrainType();
        if (worldtype == WorldType.FLAT) {
            final FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
            this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.getBiomeFromBiomeList(flatgeneratorinfo.getBiome(), BiomeGenBase.field_180279_ad), 0.5f);
        }
        else if (worldtype == WorldType.DEBUG_WORLD) {
            this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.0f);
        }
        else {
            this.worldChunkMgr = new WorldChunkManager(this.worldObj);
        }
    }
    
    public IChunkProvider createChunkGenerator() {
        return (this.terrainType == WorldType.FLAT) ? new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : ((this.terrainType == WorldType.DEBUG_WORLD) ? new ChunkProviderDebug(this.worldObj) : ((this.terrainType == WorldType.CUSTOMIZED) ? new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings)));
    }
    
    public boolean canCoordinateBeSpawn(final int x, final int z) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == Blocks.grass;
    }
    
    public float calculateCelestialAngle(final long p_76563_1_, final float p_76563_3_) {
        final int i = (int)(p_76563_1_ % 24000L);
        float f = (i + p_76563_3_) / 24000.0f - 0.25f;
        if (f < 0.0f) {
            ++f;
        }
        if (f > 1.0f) {
            --f;
        }
        f = 1.0f - (float)((Math.cos(f * 3.141592653589793) + 1.0) / 2.0);
        f += (f - f) / 3.0f;
        return f;
    }
    
    public int getMoonPhase(final long p_76559_1_) {
        return (int)(p_76559_1_ / 24000L % 8L + 8L) % 8;
    }
    
    public boolean isSurfaceWorld() {
        return true;
    }
    
    public float[] calcSunriseSunsetColors(final float celestialAngle, final float partialTicks) {
        final float f = 0.4f;
        final float f2 = MathHelper.cos(celestialAngle * 3.1415927f * 2.0f) - 0.0f;
        final float f3 = -0.0f;
        if (f2 >= f3 - f && f2 <= f3 + f) {
            final float f4 = (f2 - f3) / f * 0.5f + 0.5f;
            float f5 = 1.0f - (1.0f - MathHelper.sin(f4 * 3.1415927f)) * 0.99f;
            f5 *= f5;
            this.colorsSunriseSunset[0] = f4 * 0.3f + 0.7f;
            this.colorsSunriseSunset[1] = f4 * f4 * 0.7f + 0.2f;
            this.colorsSunriseSunset[2] = f4 * f4 * 0.0f + 0.2f;
            this.colorsSunriseSunset[3] = f5;
            return this.colorsSunriseSunset;
        }
        return null;
    }
    
    public Vec3 getFogColor(final float p_76562_1_, final float p_76562_2_) {
        float f = MathHelper.cos(p_76562_1_ * 3.1415927f * 2.0f) * 2.0f + 0.5f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        float f2 = 0.7529412f;
        float f3 = 0.84705883f;
        float f4 = 1.0f;
        f2 *= f * 0.94f + 0.06f;
        f3 *= f * 0.94f + 0.06f;
        f4 *= f * 0.91f + 0.09f;
        return new Vec3(f2, f3, f4);
    }
    
    public boolean canRespawnHere() {
        return true;
    }
    
    public static WorldProvider getProviderForDimension(final int dimension) {
        return (dimension == -1) ? new WorldProviderHell() : ((dimension == 0) ? new WorldProviderSurface() : ((dimension == 1) ? new WorldProviderEnd() : null));
    }
    
    public float getCloudHeight() {
        return 128.0f;
    }
    
    public boolean isSkyColored() {
        return true;
    }
    
    public BlockPos getSpawnCoordinate() {
        return null;
    }
    
    public int getAverageGroundLevel() {
        return (this.terrainType == WorldType.FLAT) ? 4 : (this.worldObj.func_181545_F() + 1);
    }
    
    public double getVoidFogYFactor() {
        return (this.terrainType == WorldType.FLAT) ? 1.0 : 0.03125;
    }
    
    public boolean doesXZShowFog(final int x, final int z) {
        return false;
    }
    
    public abstract String getDimensionName();
    
    public abstract String getInternalNameSuffix();
    
    public WorldChunkManager getWorldChunkManager() {
        return this.worldChunkMgr;
    }
    
    public boolean doesWaterVaporize() {
        return this.isHellWorld;
    }
    
    public boolean getHasNoSky() {
        return this.hasNoSky;
    }
    
    public float[] getLightBrightnessTable() {
        return this.lightBrightnessTable;
    }
    
    public int getDimensionId() {
        return this.dimensionId;
    }
    
    public WorldBorder getWorldBorder() {
        return new WorldBorder();
    }
}
