/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ 
/*     */ public class ChunkProviderDebug implements IChunkProvider
/*     */ {
/*  20 */   private static final List<IBlockState> field_177464_a = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkProviderDebug(World worldIn)
/*     */   {
/*  27 */     this.world = worldIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk provideChunk(int x, int z)
/*     */   {
/*  36 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/*     */     
/*  38 */     for (int i = 0; i < 16; i++)
/*     */     {
/*  40 */       for (int j = 0; j < 16; j++)
/*     */       {
/*  42 */         int k = x * 16 + i;
/*  43 */         int l = z * 16 + j;
/*  44 */         chunkprimer.setBlockState(i, 60, j, net.minecraft.init.Blocks.barrier.getDefaultState());
/*  45 */         IBlockState iblockstate = func_177461_b(k, l);
/*     */         
/*  47 */         if (iblockstate != null)
/*     */         {
/*  49 */           chunkprimer.setBlockState(i, 70, j, iblockstate);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  54 */     Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
/*  55 */     chunk.generateSkylightMap();
/*  56 */     BiomeGenBase[] abiomegenbase = this.world.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
/*  57 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/*  59 */     for (int i1 = 0; i1 < abyte.length; i1++)
/*     */     {
/*  61 */       abyte[i1] = ((byte)abiomegenbase[i1].biomeID);
/*     */     }
/*     */     
/*  64 */     chunk.generateSkylightMap();
/*  65 */     return chunk;
/*     */   }
/*     */   
/*     */   public static IBlockState func_177461_b(int p_177461_0_, int p_177461_1_)
/*     */   {
/*  70 */     IBlockState iblockstate = null;
/*     */     
/*  72 */     if ((p_177461_0_ > 0) && (p_177461_1_ > 0) && (p_177461_0_ % 2 != 0) && (p_177461_1_ % 2 != 0))
/*     */     {
/*  74 */       p_177461_0_ /= 2;
/*  75 */       p_177461_1_ /= 2;
/*     */       
/*  77 */       if ((p_177461_0_ <= field_177462_b) && (p_177461_1_ <= field_181039_c))
/*     */       {
/*  79 */         int i = MathHelper.abs_int(p_177461_0_ * field_177462_b + p_177461_1_);
/*     */         
/*  81 */         if (i < field_177464_a.size())
/*     */         {
/*  83 */           iblockstate = (IBlockState)field_177464_a.get(i);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  88 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean chunkExists(int x, int z)
/*     */   {
/*  96 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
/*     */   {
/* 108 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
/*     */   {
/* 117 */     return true;
/*     */   }
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
/*     */ 
/*     */ 
/*     */   public boolean unloadQueuedChunks()
/*     */   {
/* 133 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSave()
/*     */   {
/* 141 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String makeString()
/*     */   {
/* 149 */     return "DebugLevelSource";
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
/*     */   {
/* 154 */     BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(pos);
/* 155 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
/*     */   {
/* 160 */     return null;
/*     */   }
/*     */   
/*     */   public int getLoadedChunkCount()
/*     */   {
/* 165 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk provideChunk(BlockPos blockPosIn)
/*     */   {
/* 174 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 179 */     for (Block block : Block.blockRegistry)
/*     */     {
/* 181 */       field_177464_a.addAll(block.getBlockState().getValidStates()); }
/*     */   }
/*     */   
/* 184 */   private static final int field_177462_b = MathHelper.ceiling_float_int(MathHelper.sqrt_float(field_177464_a.size()));
/* 185 */   private static final int field_181039_c = MathHelper.ceiling_float_int(field_177464_a.size() / field_177462_b);
/*     */   private final World world;
/*     */   
/*     */   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {}
/*     */   
/*     */   public void saveExtraData() {}
/*     */   
/*     */   public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\ChunkProviderDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */