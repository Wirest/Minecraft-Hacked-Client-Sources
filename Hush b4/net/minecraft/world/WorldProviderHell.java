// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.biome.BiomeGenBase;

public class WorldProviderHell extends WorldProvider
{
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0f);
        this.isHellWorld = true;
        this.hasNoSky = true;
        this.dimensionId = -1;
    }
    
    @Override
    public Vec3 getFogColor(final float p_76562_1_, final float p_76562_2_) {
        return new Vec3(0.20000000298023224, 0.029999999329447746, 0.029999999329447746);
    }
    
    @Override
    protected void generateLightBrightnessTable() {
        final float f = 0.1f;
        for (int i = 0; i <= 15; ++i) {
            final float f2 = 1.0f - i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * (1.0f - f) + f;
        }
    }
    
    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
    }
    
    @Override
    public boolean isSurfaceWorld() {
        return false;
    }
    
    @Override
    public boolean canCoordinateBeSpawn(final int x, final int z) {
        return false;
    }
    
    @Override
    public float calculateCelestialAngle(final long p_76563_1_, final float p_76563_3_) {
        return 0.5f;
    }
    
    @Override
    public boolean canRespawnHere() {
        return false;
    }
    
    @Override
    public boolean doesXZShowFog(final int x, final int z) {
        return true;
    }
    
    @Override
    public String getDimensionName() {
        return "Nether";
    }
    
    @Override
    public String getInternalNameSuffix() {
        return "_nether";
    }
    
    @Override
    public WorldBorder getWorldBorder() {
        return new WorldBorder() {
            @Override
            public double getCenterX() {
                return super.getCenterX() / 8.0;
            }
            
            @Override
            public double getCenterZ() {
                return super.getCenterZ() / 8.0;
            }
        };
    }
}
