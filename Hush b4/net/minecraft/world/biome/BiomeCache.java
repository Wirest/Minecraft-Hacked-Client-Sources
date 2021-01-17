// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.server.MinecraftServer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.LongHashMap;

public class BiomeCache
{
    private final WorldChunkManager chunkManager;
    private long lastCleanupTime;
    private LongHashMap cacheMap;
    private List<Block> cache;
    
    public BiomeCache(final WorldChunkManager chunkManagerIn) {
        this.cacheMap = new LongHashMap();
        this.cache = (List<Block>)Lists.newArrayList();
        this.chunkManager = chunkManagerIn;
    }
    
    public Block getBiomeCacheBlock(int x, int z) {
        x >>= 4;
        z >>= 4;
        final long i = ((long)x & 0xFFFFFFFFL) | ((long)z & 0xFFFFFFFFL) << 32;
        Block biomecache$block = (Block)this.cacheMap.getValueByKey(i);
        if (biomecache$block == null) {
            biomecache$block = new Block(x, z);
            this.cacheMap.add(i, biomecache$block);
            this.cache.add(biomecache$block);
        }
        biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return biomecache$block;
    }
    
    public BiomeGenBase func_180284_a(final int x, final int z, final BiomeGenBase p_180284_3_) {
        final BiomeGenBase biomegenbase = this.getBiomeCacheBlock(x, z).getBiomeGenAt(x, z);
        return (biomegenbase == null) ? p_180284_3_ : biomegenbase;
    }
    
    public void cleanupCache() {
        final long i = MinecraftServer.getCurrentTimeMillis();
        final long j = i - this.lastCleanupTime;
        if (j > 7500L || j < 0L) {
            this.lastCleanupTime = i;
            for (int k = 0; k < this.cache.size(); ++k) {
                final Block biomecache$block = this.cache.get(k);
                final long l = i - biomecache$block.lastAccessTime;
                if (l > 30000L || l < 0L) {
                    this.cache.remove(k--);
                    final long i2 = ((long)biomecache$block.xPosition & 0xFFFFFFFFL) | ((long)biomecache$block.zPosition & 0xFFFFFFFFL) << 32;
                    this.cacheMap.remove(i2);
                }
            }
        }
    }
    
    public BiomeGenBase[] getCachedBiomes(final int x, final int z) {
        return this.getBiomeCacheBlock(x, z).biomes;
    }
    
    public class Block
    {
        public float[] rainfallValues;
        public BiomeGenBase[] biomes;
        public int xPosition;
        public int zPosition;
        public long lastAccessTime;
        
        public Block(final int x, final int z) {
            this.rainfallValues = new float[256];
            this.biomes = new BiomeGenBase[256];
            this.xPosition = x;
            this.zPosition = z;
            BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, x << 4, z << 4, 16, 16);
            BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, x << 4, z << 4, 16, 16, false);
        }
        
        public BiomeGenBase getBiomeGenAt(final int x, final int z) {
            return this.biomes[(x & 0xF) | (z & 0xF) << 4];
        }
    }
}
