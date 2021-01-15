package net.minecraft.world.chunk.storage;

import java.io.IOException;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract interface IChunkLoader
{
  public abstract Chunk loadChunk(World paramWorld, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract void saveChunk(World paramWorld, Chunk paramChunk)
    throws MinecraftException, IOException;
  
  public abstract void saveExtraChunkData(World paramWorld, Chunk paramChunk)
    throws IOException;
  
  public abstract void chunkTick();
  
  public abstract void saveExtraData();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\storage\IChunkLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */