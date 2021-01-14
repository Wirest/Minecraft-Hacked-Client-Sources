package net.minecraft.world.chunk.storage;

import java.io.IOException;

import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IChunkLoader {
    /**
     * Loads the specified(XZ) chunk into the specified world.
     */
    Chunk loadChunk(World var1, int var2, int var3) throws IOException;

    void saveChunk(World var1, Chunk var2) throws MinecraftException, IOException;

    /**
     * Save extra data associated with this Chunk not normally saved during autosave, only during chunk unload.
     * Currently unused.
     */
    void saveExtraChunkData(World var1, Chunk var2);

    /**
     * Called every World.tick()
     */
    void chunkTick();

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unused.
     */
    void saveExtraData();
}
