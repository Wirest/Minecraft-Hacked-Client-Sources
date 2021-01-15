/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.feature.WorldGenDungeons;
/*     */ import net.minecraft.world.gen.feature.WorldGenLakes;
/*     */ import net.minecraft.world.gen.structure.MapGenMineshaft;
/*     */ import net.minecraft.world.gen.structure.MapGenScatteredFeature;
/*     */ import net.minecraft.world.gen.structure.MapGenStronghold;
/*     */ import net.minecraft.world.gen.structure.MapGenStructure;
/*     */ import net.minecraft.world.gen.structure.MapGenVillage;
/*     */ import net.minecraft.world.gen.structure.StructureOceanMonument;
/*     */ 
/*     */ public class ChunkProviderFlat implements IChunkProvider
/*     */ {
/*     */   private World worldObj;
/*     */   private Random random;
/*  31 */   private final IBlockState[] cachedBlockIDs = new IBlockState['Ā'];
/*     */   private final FlatGeneratorInfo flatWorldGenInfo;
/*  33 */   private final List<MapGenStructure> structureGenerators = com.google.common.collect.Lists.newArrayList();
/*     */   private final boolean hasDecoration;
/*     */   private final boolean hasDungeons;
/*     */   private WorldGenLakes waterLakeGenerator;
/*     */   private WorldGenLakes lavaLakeGenerator;
/*     */   
/*     */   public ChunkProviderFlat(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings)
/*     */   {
/*  41 */     this.worldObj = worldIn;
/*  42 */     this.random = new Random(seed);
/*  43 */     this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(flatGeneratorSettings);
/*     */     
/*  45 */     if (generateStructures)
/*     */     {
/*  47 */       Map<String, Map<String, String>> map = this.flatWorldGenInfo.getWorldFeatures();
/*     */       
/*  49 */       if (map.containsKey("village"))
/*     */       {
/*  51 */         Map<String, String> map1 = (Map)map.get("village");
/*     */         
/*  53 */         if (!map1.containsKey("size"))
/*     */         {
/*  55 */           map1.put("size", "1");
/*     */         }
/*     */         
/*  58 */         this.structureGenerators.add(new MapGenVillage(map1));
/*     */       }
/*     */       
/*  61 */       if (map.containsKey("biome_1"))
/*     */       {
/*  63 */         this.structureGenerators.add(new MapGenScatteredFeature((Map)map.get("biome_1")));
/*     */       }
/*     */       
/*  66 */       if (map.containsKey("mineshaft"))
/*     */       {
/*  68 */         this.structureGenerators.add(new MapGenMineshaft((Map)map.get("mineshaft")));
/*     */       }
/*     */       
/*  71 */       if (map.containsKey("stronghold"))
/*     */       {
/*  73 */         this.structureGenerators.add(new MapGenStronghold((Map)map.get("stronghold")));
/*     */       }
/*     */       
/*  76 */       if (map.containsKey("oceanmonument"))
/*     */       {
/*  78 */         this.structureGenerators.add(new StructureOceanMonument((Map)map.get("oceanmonument")));
/*     */       }
/*     */     }
/*     */     
/*  82 */     if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake"))
/*     */     {
/*  84 */       this.waterLakeGenerator = new WorldGenLakes(Blocks.water);
/*     */     }
/*     */     
/*  87 */     if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake"))
/*     */     {
/*  89 */       this.lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
/*     */     }
/*     */     
/*  92 */     this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
/*  93 */     int j = 0;
/*  94 */     int k = 0;
/*  95 */     boolean flag = true;
/*     */     
/*  97 */     for (FlatLayerInfo flatlayerinfo : this.flatWorldGenInfo.getFlatLayers())
/*     */     {
/*  99 */       for (int i = flatlayerinfo.getMinY(); i < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); i++)
/*     */       {
/* 101 */         IBlockState iblockstate = flatlayerinfo.func_175900_c();
/*     */         
/* 103 */         if (iblockstate.getBlock() != Blocks.air)
/*     */         {
/* 105 */           flag = false;
/* 106 */           this.cachedBlockIDs[i] = iblockstate;
/*     */         }
/*     */       }
/*     */       
/* 110 */       if (flatlayerinfo.func_175900_c().getBlock() == Blocks.air)
/*     */       {
/* 112 */         k += flatlayerinfo.getLayerCount();
/*     */       }
/*     */       else
/*     */       {
/* 116 */         j += flatlayerinfo.getLayerCount() + k;
/* 117 */         k = 0;
/*     */       }
/*     */     }
/*     */     
/* 121 */     worldIn.func_181544_b(j);
/* 122 */     this.hasDecoration = (flag ? false : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk provideChunk(int x, int z)
/*     */   {
/* 131 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/*     */     IBlockState iblockstate;
/* 133 */     for (int i = 0; i < this.cachedBlockIDs.length; i++)
/*     */     {
/* 135 */       iblockstate = this.cachedBlockIDs[i];
/*     */       
/* 137 */       if (iblockstate != null)
/*     */       {
/* 139 */         for (int j = 0; j < 16; j++)
/*     */         {
/* 141 */           for (int k = 0; k < 16; k++)
/*     */           {
/* 143 */             chunkprimer.setBlockState(j, i, k, iblockstate);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 149 */     for (MapGenBase mapgenbase : this.structureGenerators)
/*     */     {
/* 151 */       mapgenbase.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 154 */     Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
/* 155 */     BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
/* 156 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 158 */     for (int l = 0; l < abyte.length; l++)
/*     */     {
/* 160 */       abyte[l] = ((byte)abiomegenbase[l].biomeID);
/*     */     }
/*     */     
/* 163 */     chunk.generateSkylightMap();
/* 164 */     return chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean chunkExists(int x, int z)
/*     */   {
/* 172 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
/*     */   {
/* 180 */     int i = p_73153_2_ * 16;
/* 181 */     int j = p_73153_3_ * 16;
/* 182 */     BlockPos blockpos = new BlockPos(i, 0, j);
/* 183 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(i + 16, 0, j + 16));
/* 184 */     boolean flag = false;
/* 185 */     this.random.setSeed(this.worldObj.getSeed());
/* 186 */     long k = this.random.nextLong() / 2L * 2L + 1L;
/* 187 */     long l = this.random.nextLong() / 2L * 2L + 1L;
/* 188 */     this.random.setSeed(p_73153_2_ * k + p_73153_3_ * l ^ this.worldObj.getSeed());
/* 189 */     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
/*     */     
/* 191 */     for (MapGenStructure mapgenstructure : this.structureGenerators)
/*     */     {
/* 193 */       boolean flag1 = mapgenstructure.generateStructure(this.worldObj, this.random, chunkcoordintpair);
/*     */       
/* 195 */       if ((mapgenstructure instanceof MapGenVillage))
/*     */       {
/* 197 */         flag |= flag1;
/*     */       }
/*     */     }
/*     */     
/* 201 */     if ((this.waterLakeGenerator != null) && (!flag) && (this.random.nextInt(4) == 0))
/*     */     {
/* 203 */       this.waterLakeGenerator.generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
/*     */     }
/*     */     
/* 206 */     if ((this.lavaLakeGenerator != null) && (!flag) && (this.random.nextInt(8) == 0))
/*     */     {
/* 208 */       BlockPos blockpos1 = blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);
/*     */       
/* 210 */       if ((blockpos1.getY() < this.worldObj.func_181545_F()) || (this.random.nextInt(10) == 0))
/*     */       {
/* 212 */         this.lavaLakeGenerator.generate(this.worldObj, this.random, blockpos1);
/*     */       }
/*     */     }
/*     */     
/* 216 */     if (this.hasDungeons)
/*     */     {
/* 218 */       for (int i1 = 0; i1 < 8; i1++)
/*     */       {
/* 220 */         new WorldGenDungeons().generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
/*     */       }
/*     */     }
/*     */     
/* 224 */     if (this.hasDecoration)
/*     */     {
/* 226 */       biomegenbase.decorate(this.worldObj, this.random, blockpos);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
/*     */   {
/* 232 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
/*     */   {
/* 241 */     return true;
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
/* 257 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSave()
/*     */   {
/* 265 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String makeString()
/*     */   {
/* 273 */     return "FlatLevelSource";
/*     */   }
/*     */   
/*     */   public List<net.minecraft.world.biome.BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
/*     */   {
/* 278 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
/* 279 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
/*     */   {
/* 284 */     if ("Stronghold".equals(structureName))
/*     */     {
/* 286 */       for (MapGenStructure mapgenstructure : this.structureGenerators)
/*     */       {
/* 288 */         if ((mapgenstructure instanceof MapGenStronghold))
/*     */         {
/* 290 */           return mapgenstructure.getClosestStrongholdPos(worldIn, position);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 295 */     return null;
/*     */   }
/*     */   
/*     */   public int getLoadedChunkCount()
/*     */   {
/* 300 */     return 0;
/*     */   }
/*     */   
/*     */   public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
/*     */   {
/* 305 */     for (MapGenStructure mapgenstructure : this.structureGenerators)
/*     */     {
/* 307 */       mapgenstructure.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn)
/*     */   {
/* 313 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\ChunkProviderFlat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */