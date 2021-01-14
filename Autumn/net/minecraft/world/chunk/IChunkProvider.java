package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;

public interface IChunkProvider {
   boolean chunkExists(int var1, int var2);

   Chunk provideChunk(int var1, int var2);

   Chunk provideChunk(BlockPos var1);

   void populate(IChunkProvider var1, int var2, int var3);

   boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4);

   boolean saveChunks(boolean var1, IProgressUpdate var2);

   boolean unloadQueuedChunks();

   boolean canSave();

   String makeString();

   List getPossibleCreatures(EnumCreatureType var1, BlockPos var2);

   BlockPos getStrongholdGen(World var1, String var2, BlockPos var3);

   int getLoadedChunkCount();

   void recreateStructures(Chunk var1, int var2, int var3);

   void saveExtraData();
}
