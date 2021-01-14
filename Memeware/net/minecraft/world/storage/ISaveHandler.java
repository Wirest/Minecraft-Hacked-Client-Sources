package net.minecraft.world.storage;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public interface ISaveHandler {
    /**
     * Loads and returns the world info
     */
    WorldInfo loadWorldInfo();

    /**
     * Checks the session lock to prevent save collisions
     */
    void checkSessionLock() throws MinecraftException;

    /**
     * initializes and returns the chunk loader for the specified world provider
     */
    IChunkLoader getChunkLoader(WorldProvider var1);

    /**
     * Saves the given World Info with the given NBTTagCompound as the Player.
     */
    void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2);

    /**
     * used to update level.dat from old format to MCRegion format
     */
    void saveWorldInfo(WorldInfo var1);

    IPlayerFileData getPlayerNBTManager();

    /**
     * Called to flush all changes to disk, waiting for them to complete.
     */
    void flush();

    /**
     * Gets the File object corresponding to the base directory of this world.
     */
    File getWorldDirectory();

    /**
     * Gets the file location of the given map
     */
    File getMapFileFromName(String var1);

    /**
     * Returns the name of the directory where world information is saved.
     */
    String getWorldDirectoryName();
}
