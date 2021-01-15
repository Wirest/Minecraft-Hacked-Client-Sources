/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.EmptyChunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkProviderClient implements IChunkProvider
/*     */ {
/*  20 */   private static final Logger logger = ;
/*     */   
/*     */ 
/*     */ 
/*     */   private Chunk blankChunk;
/*     */   
/*     */ 
/*  27 */   private LongHashMap chunkMapping = new LongHashMap();
/*  28 */   private List<Chunk> chunkListing = Lists.newArrayList();
/*     */   
/*     */   private World worldObj;
/*     */   
/*     */ 
/*     */   public ChunkProviderClient(World worldIn)
/*     */   {
/*  35 */     this.blankChunk = new EmptyChunk(worldIn, 0, 0);
/*  36 */     this.worldObj = worldIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean chunkExists(int x, int z)
/*     */   {
/*  44 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unloadChunk(int p_73234_1_, int p_73234_2_)
/*     */   {
/*  53 */     Chunk chunk = provideChunk(p_73234_1_, p_73234_2_);
/*     */     
/*  55 */     if (!chunk.isEmpty())
/*     */     {
/*  57 */       chunk.onChunkUnload();
/*     */     }
/*     */     
/*  60 */     this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(p_73234_1_, p_73234_2_));
/*  61 */     this.chunkListing.remove(chunk);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk loadChunk(int p_73158_1_, int p_73158_2_)
/*     */   {
/*  69 */     Chunk chunk = new Chunk(this.worldObj, p_73158_1_, p_73158_2_);
/*  70 */     this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_), chunk);
/*  71 */     this.chunkListing.add(chunk);
/*  72 */     chunk.setChunkLoaded(true);
/*  73 */     return chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk provideChunk(int x, int z)
/*     */   {
/*  82 */     Chunk chunk = (Chunk)this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
/*  83 */     return chunk == null ? this.blankChunk : chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
/*     */   {
/*  92 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveExtraData() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean unloadQueuedChunks()
/*     */   {
/* 108 */     long i = System.currentTimeMillis();
/*     */     
/* 110 */     for (Chunk chunk : this.chunkListing)
/*     */     {
/* 112 */       chunk.func_150804_b(System.currentTimeMillis() - i > 5L);
/*     */     }
/*     */     
/* 115 */     if (System.currentTimeMillis() - i > 100L)
/*     */     {
/* 117 */       logger.info("Warning: Clientside chunk ticking took {} ms", new Object[] { Long.valueOf(System.currentTimeMillis() - i) });
/*     */     }
/*     */     
/* 120 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSave()
/*     */   {
/* 128 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
/*     */   {
/* 140 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String makeString()
/*     */   {
/* 148 */     return "MultiplayerChunkCache: " + this.chunkMapping.getNumHashElements() + ", " + this.chunkListing.size();
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
/*     */   {
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
/*     */   {
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   public int getLoadedChunkCount()
/*     */   {
/* 163 */     return this.chunkListing.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {}
/*     */   
/*     */ 
/*     */   public Chunk provideChunk(BlockPos blockPosIn)
/*     */   {
/* 172 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\ChunkProviderClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */