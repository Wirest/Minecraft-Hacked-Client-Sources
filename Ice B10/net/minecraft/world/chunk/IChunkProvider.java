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
    boolean chunkExists(int p_73149_1_, int p_73149_2_);

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    Chunk provideChunk(int p_73154_1_, int p_73154_2_);

    Chunk func_177459_a(BlockPos p_177459_1_);

    /**
     * Populates chunk with ores etc etc
     */
    void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_);

    boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_);

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_);

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

    List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_);

    BlockPos func_180513_a(World worldIn, String p_180513_2_, BlockPos p_180513_3_);

    int getLoadedChunkCount();

    void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_);

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    void saveExtraData();
}
