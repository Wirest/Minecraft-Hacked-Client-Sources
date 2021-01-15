/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.MinecraftException;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.chunk.storage.IChunkLoader;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkProviderServer implements IChunkProvider
/*     */ {
/*  30 */   private static final Logger logger = ;
/*  31 */   private Set<Long> droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap());
/*     */   
/*     */ 
/*     */ 
/*     */   private Chunk dummyChunk;
/*     */   
/*     */ 
/*     */ 
/*     */   private IChunkProvider serverChunkGenerator;
/*     */   
/*     */ 
/*     */ 
/*     */   private IChunkLoader chunkLoader;
/*     */   
/*     */ 
/*  46 */   public boolean chunkLoadOverride = true;
/*  47 */   private LongHashMap id2ChunkMap = new LongHashMap();
/*  48 */   private List<Chunk> loadedChunks = Lists.newArrayList();
/*     */   private WorldServer worldObj;
/*     */   
/*     */   public ChunkProviderServer(WorldServer p_i1520_1_, IChunkLoader p_i1520_2_, IChunkProvider p_i1520_3_)
/*     */   {
/*  53 */     this.dummyChunk = new net.minecraft.world.chunk.EmptyChunk(p_i1520_1_, 0, 0);
/*  54 */     this.worldObj = p_i1520_1_;
/*  55 */     this.chunkLoader = p_i1520_2_;
/*  56 */     this.serverChunkGenerator = p_i1520_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean chunkExists(int x, int z)
/*     */   {
/*  64 */     return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(x, z));
/*     */   }
/*     */   
/*     */   public List<Chunk> func_152380_a()
/*     */   {
/*  69 */     return this.loadedChunks;
/*     */   }
/*     */   
/*     */   public void dropChunk(int p_73241_1_, int p_73241_2_)
/*     */   {
/*  74 */     if (this.worldObj.provider.canRespawnHere())
/*     */     {
/*  76 */       if (!this.worldObj.isSpawnChunk(p_73241_1_, p_73241_2_))
/*     */       {
/*  78 */         this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/*  83 */       this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unloadAllChunks()
/*     */   {
/*  92 */     for (Chunk chunk : this.loadedChunks)
/*     */     {
/*  94 */       dropChunk(chunk.xPosition, chunk.zPosition);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk loadChunk(int p_73158_1_, int p_73158_2_)
/*     */   {
/* 103 */     long i = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
/* 104 */     this.droppedChunksSet.remove(Long.valueOf(i));
/* 105 */     Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(i);
/*     */     
/* 107 */     if (chunk == null)
/*     */     {
/* 109 */       chunk = loadChunkFromFile(p_73158_1_, p_73158_2_);
/*     */       
/* 111 */       if (chunk == null)
/*     */       {
/* 113 */         if (this.serverChunkGenerator == null)
/*     */         {
/* 115 */           chunk = this.dummyChunk;
/*     */         }
/*     */         else
/*     */         {
/*     */           try
/*     */           {
/* 121 */             chunk = this.serverChunkGenerator.provideChunk(p_73158_1_, p_73158_2_);
/*     */           }
/*     */           catch (Throwable throwable)
/*     */           {
/* 125 */             CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
/* 126 */             CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
/* 127 */             crashreportcategory.addCrashSection("Location", String.format("%d,%d", new Object[] { Integer.valueOf(p_73158_1_), Integer.valueOf(p_73158_2_) }));
/* 128 */             crashreportcategory.addCrashSection("Position hash", Long.valueOf(i));
/* 129 */             crashreportcategory.addCrashSection("Generator", this.serverChunkGenerator.makeString());
/* 130 */             throw new net.minecraft.util.ReportedException(crashreport);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 135 */       this.id2ChunkMap.add(i, chunk);
/* 136 */       this.loadedChunks.add(chunk);
/* 137 */       chunk.onChunkLoad();
/* 138 */       chunk.populateChunk(this, this, p_73158_1_, p_73158_2_);
/*     */     }
/*     */     
/* 141 */     return chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk provideChunk(int x, int z)
/*     */   {
/* 150 */     Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
/* 151 */     return chunk == null ? loadChunk(x, z) : (!this.worldObj.isFindingSpawnPoint()) && (!this.chunkLoadOverride) ? this.dummyChunk : chunk;
/*     */   }
/*     */   
/*     */   private Chunk loadChunkFromFile(int x, int z)
/*     */   {
/* 156 */     if (this.chunkLoader == null)
/*     */     {
/* 158 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 164 */       Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, x, z);
/*     */       
/* 166 */       if (chunk != null)
/*     */       {
/* 168 */         chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
/*     */         
/* 170 */         if (this.serverChunkGenerator != null)
/*     */         {
/* 172 */           this.serverChunkGenerator.recreateStructures(chunk, x, z);
/*     */         }
/*     */       }
/*     */       
/* 176 */       return chunk;
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 180 */       logger.error("Couldn't load chunk", exception); }
/* 181 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void saveChunkExtraData(Chunk p_73243_1_)
/*     */   {
/* 188 */     if (this.chunkLoader != null)
/*     */     {
/*     */       try
/*     */       {
/* 192 */         this.chunkLoader.saveExtraChunkData(this.worldObj, p_73243_1_);
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 196 */         logger.error("Couldn't save entities", exception);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveChunkData(Chunk p_73242_1_)
/*     */   {
/* 203 */     if (this.chunkLoader != null)
/*     */     {
/*     */       try
/*     */       {
/* 207 */         p_73242_1_.setLastSaveTime(this.worldObj.getTotalWorldTime());
/* 208 */         this.chunkLoader.saveChunk(this.worldObj, p_73242_1_);
/*     */       }
/*     */       catch (IOException ioexception)
/*     */       {
/* 212 */         logger.error("Couldn't save chunk", ioexception);
/*     */       }
/*     */       catch (MinecraftException minecraftexception)
/*     */       {
/* 216 */         logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", minecraftexception);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
/*     */   {
/* 226 */     Chunk chunk = provideChunk(p_73153_2_, p_73153_3_);
/*     */     
/* 228 */     if (!chunk.isTerrainPopulated())
/*     */     {
/* 230 */       chunk.func_150809_p();
/*     */       
/* 232 */       if (this.serverChunkGenerator != null)
/*     */       {
/* 234 */         this.serverChunkGenerator.populate(p_73153_1_, p_73153_2_, p_73153_3_);
/* 235 */         chunk.setChunkModified();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
/*     */   {
/* 242 */     if ((this.serverChunkGenerator != null) && (this.serverChunkGenerator.func_177460_a(p_177460_1_, p_177460_2_, p_177460_3_, p_177460_4_)))
/*     */     {
/* 244 */       Chunk chunk = provideChunk(p_177460_3_, p_177460_4_);
/* 245 */       chunk.setChunkModified();
/* 246 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 250 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
/*     */   {
/* 260 */     int i = 0;
/* 261 */     List<Chunk> list = Lists.newArrayList(this.loadedChunks);
/*     */     
/* 263 */     for (int j = 0; j < list.size(); j++)
/*     */     {
/* 265 */       Chunk chunk = (Chunk)list.get(j);
/*     */       
/* 267 */       if (p_73151_1_)
/*     */       {
/* 269 */         saveChunkExtraData(chunk);
/*     */       }
/*     */       
/* 272 */       if (chunk.needsSaving(p_73151_1_))
/*     */       {
/* 274 */         saveChunkData(chunk);
/* 275 */         chunk.setModified(false);
/* 276 */         i++;
/*     */         
/* 278 */         if ((i == 24) && (!p_73151_1_))
/*     */         {
/* 280 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 285 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveExtraData()
/*     */   {
/* 294 */     if (this.chunkLoader != null)
/*     */     {
/* 296 */       this.chunkLoader.saveExtraData();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean unloadQueuedChunks()
/*     */   {
/* 305 */     if (!this.worldObj.disableLevelSaving)
/*     */     {
/* 307 */       for (int i = 0; i < 100; i++)
/*     */       {
/* 309 */         if (!this.droppedChunksSet.isEmpty())
/*     */         {
/* 311 */           Long olong = (Long)this.droppedChunksSet.iterator().next();
/* 312 */           Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(olong.longValue());
/*     */           
/* 314 */           if (chunk != null)
/*     */           {
/* 316 */             chunk.onChunkUnload();
/* 317 */             saveChunkData(chunk);
/* 318 */             saveChunkExtraData(chunk);
/* 319 */             this.id2ChunkMap.remove(olong.longValue());
/* 320 */             this.loadedChunks.remove(chunk);
/*     */           }
/*     */           
/* 323 */           this.droppedChunksSet.remove(olong);
/*     */         }
/*     */       }
/*     */       
/* 327 */       if (this.chunkLoader != null)
/*     */       {
/* 329 */         this.chunkLoader.chunkTick();
/*     */       }
/*     */     }
/*     */     
/* 333 */     return this.serverChunkGenerator.unloadQueuedChunks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSave()
/*     */   {
/* 341 */     return !this.worldObj.disableLevelSaving;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String makeString()
/*     */   {
/* 349 */     return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
/*     */   {
/* 354 */     return this.serverChunkGenerator.getPossibleCreatures(creatureType, pos);
/*     */   }
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
/*     */   {
/* 359 */     return this.serverChunkGenerator.getStrongholdGen(worldIn, structureName, position);
/*     */   }
/*     */   
/*     */   public int getLoadedChunkCount()
/*     */   {
/* 364 */     return this.id2ChunkMap.getNumHashElements();
/*     */   }
/*     */   
/*     */ 
/*     */   public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {}
/*     */   
/*     */ 
/*     */   public Chunk provideChunk(BlockPos blockPosIn)
/*     */   {
/* 373 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\ChunkProviderServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */