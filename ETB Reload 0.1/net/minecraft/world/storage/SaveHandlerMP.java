package net.minecraft.world.storage;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public class SaveHandlerMP implements ISaveHandler
{
    /**
     * Loads and returns the world info
     */
    @Override
	public WorldInfo loadWorldInfo()
    {
        return null;
    }

    /**
     * Checks the session lock to prevent save collisions
     */
    @Override
	public void checkSessionLock() throws MinecraftException
    {
    }

    /**
     * initializes and returns the chunk loader for the specified world provider
     */
    @Override
	public IChunkLoader getChunkLoader(WorldProvider provider)
    {
        return null;
    }

    /**
     * Saves the given World Info with the given NBTTagCompound as the Player.
     */
    @Override
	public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound)
    {
    }

    /**
     * used to update level.dat from old format to MCRegion format
     */
    @Override
	public void saveWorldInfo(WorldInfo worldInformation)
    {
    }

    @Override
	public IPlayerFileData getPlayerNBTManager()
    {
        return null;
    }

    /**
     * Called to flush all changes to disk, waiting for them to complete.
     */
    @Override
	public void flush()
    {
    }

    /**
     * Gets the file location of the given map
     */
    @Override
	public File getMapFileFromName(String mapName)
    {
        return null;
    }

    /**
     * Returns the name of the directory where world information is saved.
     */
    @Override
	public String getWorldDirectoryName()
    {
        return "none";
    }

    /**
     * Gets the File object corresponding to the base directory of this world.
     */
    @Override
	public File getWorldDirectory()
    {
        return null;
    }
}
