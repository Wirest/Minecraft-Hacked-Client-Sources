package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;

public interface IChunkProvider
{
    /**
     * Checks to see if a chunk exists at x, y
     */
    boolean chunkExists(int var1, int var2);

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    Chunk provideChunk(int var1, int var2);

    Chunk func_177459_a(BlockPos var1);

    /**
     * Populates chunk with ores etc etc
     */
    void populate(IChunkProvider var1, int var2, int var3);

    boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4);

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    boolean saveChunks(boolean var1, IProgressUpdate var2);

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    boolean unloadQueuedChunks();

    /**
     * Returns if the IChunkProvider supports saving.
     */
    boolean canSave();

    /**
     * Converts the instance data to a readable string.
     */
    String makeString();

    List func_177458_a(EnumCreatureType var1, BlockPos var2);

    BlockPos func_180513_a(World var1, String var2, BlockPos var3);

    int getLoadedChunkCount();

    void func_180514_a(Chunk var1, int var2, int var3);

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    void saveExtraData();
}
