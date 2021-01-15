/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BiomeCache
/*     */ {
/*     */   private final WorldChunkManager chunkManager;
/*     */   private long lastCleanupTime;
/*  15 */   private LongHashMap cacheMap = new LongHashMap();
/*  16 */   private List<Block> cache = Lists.newArrayList();
/*     */   
/*     */   public BiomeCache(WorldChunkManager chunkManagerIn)
/*     */   {
/*  20 */     this.chunkManager = chunkManagerIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Block getBiomeCacheBlock(int x, int z)
/*     */   {
/*  28 */     x >>= 4;
/*  29 */     z >>= 4;
/*  30 */     long i = x & 0xFFFFFFFF | (z & 0xFFFFFFFF) << 32;
/*  31 */     Block biomecache$block = (Block)this.cacheMap.getValueByKey(i);
/*     */     
/*  33 */     if (biomecache$block == null)
/*     */     {
/*  35 */       biomecache$block = new Block(x, z);
/*  36 */       this.cacheMap.add(i, biomecache$block);
/*  37 */       this.cache.add(biomecache$block);
/*     */     }
/*     */     
/*  40 */     biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
/*  41 */     return biomecache$block;
/*     */   }
/*     */   
/*     */   public BiomeGenBase func_180284_a(int x, int z, BiomeGenBase p_180284_3_)
/*     */   {
/*  46 */     BiomeGenBase biomegenbase = getBiomeCacheBlock(x, z).getBiomeGenAt(x, z);
/*  47 */     return biomegenbase == null ? p_180284_3_ : biomegenbase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void cleanupCache()
/*     */   {
/*  55 */     long i = MinecraftServer.getCurrentTimeMillis();
/*  56 */     long j = i - this.lastCleanupTime;
/*     */     
/*  58 */     if ((j > 7500L) || (j < 0L))
/*     */     {
/*  60 */       this.lastCleanupTime = i;
/*     */       
/*  62 */       for (int k = 0; k < this.cache.size(); k++)
/*     */       {
/*  64 */         Block biomecache$block = (Block)this.cache.get(k);
/*  65 */         long l = i - biomecache$block.lastAccessTime;
/*     */         
/*  67 */         if ((l > 30000L) || (l < 0L))
/*     */         {
/*  69 */           this.cache.remove(k--);
/*  70 */           long i1 = biomecache$block.xPosition & 0xFFFFFFFF | (biomecache$block.zPosition & 0xFFFFFFFF) << 32;
/*  71 */           this.cacheMap.remove(i1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BiomeGenBase[] getCachedBiomes(int x, int z)
/*     */   {
/*  82 */     return getBiomeCacheBlock(x, z).biomes;
/*     */   }
/*     */   
/*     */   public class Block
/*     */   {
/*  87 */     public float[] rainfallValues = new float['Ā'];
/*  88 */     public BiomeGenBase[] biomes = new BiomeGenBase['Ā'];
/*     */     public int xPosition;
/*     */     public int zPosition;
/*     */     public long lastAccessTime;
/*     */     
/*     */     public Block(int x, int z)
/*     */     {
/*  95 */       this.xPosition = x;
/*  96 */       this.zPosition = z;
/*  97 */       BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, x << 4, z << 4, 16, 16);
/*  98 */       BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, x << 4, z << 4, 16, 16, false);
/*     */     }
/*     */     
/*     */     public BiomeGenBase getBiomeGenAt(int x, int z)
/*     */     {
/* 103 */       return this.biomes[(x & 0xF | (z & 0xF) << 4)];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */