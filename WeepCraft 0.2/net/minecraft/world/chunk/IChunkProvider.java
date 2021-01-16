package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider
{
    @Nullable
    Chunk getLoadedChunk(int x, int z);

    Chunk provideChunk(int x, int z);

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    boolean unloadQueuedChunks();

    /**
     * Converts the instance data to a readable string.
     */
    String makeString();

    boolean func_191062_e(int p_191062_1_, int p_191062_2_);
}
