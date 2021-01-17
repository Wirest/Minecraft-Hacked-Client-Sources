// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.biome.BiomeGenBase;

public class WorldProviderEnd extends WorldProvider
{
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0f);
        this.dimensionId = 1;
        this.hasNoSky = true;
    }
    
    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
    }
    
    @Override
    public float calculateCelestialAngle(final long p_76563_1_, final float p_76563_3_) {
        return 0.0f;
    }
    
    @Override
    public float[] calcSunriseSunsetColors(final float celestialAngle, final float partialTicks) {
        return null;
    }
    
    @Override
    public Vec3 getFogColor(final float p_76562_1_, final float p_76562_2_) {
        final int i = 10518688;
        float f = MathHelper.cos(p_76562_1_ * 3.1415927f * 2.0f) * 2.0f + 0.5f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        float f2 = (i >> 16 & 0xFF) / 255.0f;
        float f3 = (i >> 8 & 0xFF) / 255.0f;
        float f4 = (i & 0xFF) / 255.0f;
        f2 *= f * 0.0f + 0.15f;
        f3 *= f * 0.0f + 0.15f;
        f4 *= f * 0.0f + 0.15f;
        return new Vec3(f2, f3, f4);
    }
    
    @Override
    public boolean isSkyColored() {
        return false;
    }
    
    @Override
    public boolean canRespawnHere() {
        return false;
    }
    
    @Override
    public boolean isSurfaceWorld() {
        return false;
    }
    
    @Override
    public float getCloudHeight() {
        return 8.0f;
    }
    
    @Override
    public boolean canCoordinateBeSpawn(final int x, final int z) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
    }
    
    @Override
    public BlockPos getSpawnCoordinate() {
        return new BlockPos(100, 50, 0);
    }
    
    @Override
    public int getAverageGroundLevel() {
        return 50;
    }
    
    @Override
    public boolean doesXZShowFog(final int x, final int z) {
        return true;
    }
    
    @Override
    public String getDimensionName() {
        return "The End";
    }
    
    @Override
    public String getInternalNameSuffix() {
        return "_end";
    }
}
