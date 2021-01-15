package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

public abstract interface IChunkProvider
{
  public abstract boolean chunkExists(int paramInt1, int paramInt2);
  
  public abstract Chunk provideChunk(int paramInt1, int paramInt2);
  
  public abstract Chunk provideChunk(BlockPos paramBlockPos);
  
  public abstract void populate(IChunkProvider paramIChunkProvider, int paramInt1, int paramInt2);
  
  public abstract boolean func_177460_a(IChunkProvider paramIChunkProvider, Chunk paramChunk, int paramInt1, int paramInt2);
  
  public abstract boolean saveChunks(boolean paramBoolean, IProgressUpdate paramIProgressUpdate);
  
  public abstract boolean unloadQueuedChunks();
  
  public abstract boolean canSave();
  
  public abstract String makeString();
  
  public abstract List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType paramEnumCreatureType, BlockPos paramBlockPos);
  
  public abstract BlockPos getStrongholdGen(World paramWorld, String paramString, BlockPos paramBlockPos);
  
  public abstract int getLoadedChunkCount();
  
  public abstract void recreateStructures(Chunk paramChunk, int paramInt1, int paramInt2);
  
  public abstract void saveExtraData();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\IChunkProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */