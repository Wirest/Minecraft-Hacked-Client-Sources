package net.minecraft.world.chunk.storage;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IChunkLoader
{
    @Nullable

    /**
     * Loads the specified(XZ) chunk into the specified world.
     */
    Chunk loadChunk(World worldIn, int x, int z) throws IOException;

    void saveChunk(World worldIn, Chunk chunkIn) throws MinecraftException, IOException;

    /**
     * Save extra data associated with this Chunk not normally saved during autosave, only during chunk unload.
     * Currently unused.
     */
    void saveExtraChunkData(World worldIn, Chunk chunkIn) throws IOException;

    /**
     * Called every World.tick()
     */
    void chunkTick();

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unused.
     */
    void saveExtraData();

    boolean func_191063_a(int p_191063_1_, int p_191063_2_);
}
