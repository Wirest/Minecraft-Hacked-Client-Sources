package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public class SaveHandler implements ISaveHandler, IPlayerFileData
{
    private static final Logger logger = LogManager.getLogger();

    /** The directory in which to save world data. */
    private final File worldDirectory;

    /** The directory in which to save player data. */
    private final File playersDirectory;
    private final File mapDataDir;

    /**
     * The time in milliseconds when this field was initialized. Stored in the session lock file.
     */
    private final long initializationTime = MinecraftServer.getCurrentTimeMillis();

    /** The directory name of the world */
    private final String saveDirectoryName;

    public SaveHandler(File savesDirectory, String directoryName, boolean playersDirectoryIn)
    {
        this.worldDirectory = new File(savesDirectory, directoryName);
        this.worldDirectory.mkdirs();
        this.playersDirectory = new File(this.worldDirectory, "playerdata");
        this.mapDataDir = new File(this.worldDirectory, "data");
        this.mapDataDir.mkdirs();
        this.saveDirectoryName = directoryName;

        if (playersDirectoryIn)
        {
            this.playersDirectory.mkdirs();
        }

        this.setSessionLock();
    }

    /**
     * Creates a session lock file for this process
     */
    private void setSessionLock()
    {
        try
        {
            File var1 = new File(this.worldDirectory, "session.lock");
            DataOutputStream var2 = new DataOutputStream(new FileOutputStream(var1));

            try
            {
                var2.writeLong(this.initializationTime);
            }
            finally
            {
                var2.close();
            }
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    /**
     * Gets the File object corresponding to the base directory of this world.
     */
    @Override
	public File getWorldDirectory()
    {
        return this.worldDirectory;
    }

    /**
     * Checks the session lock to prevent save collisions
     */
    @Override
	public void checkSessionLock() throws MinecraftException
    {
        try
        {
            File var1 = new File(this.worldDirectory, "session.lock");
            DataInputStream var2 = new DataInputStream(new FileInputStream(var1));

            try
            {
                if (var2.readLong() != this.initializationTime)
                {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
            finally
            {
                var2.close();
            }
        }
        catch (IOException var7)
        {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    /**
     * initializes and returns the chunk loader for the specified world provider
     */
    @Override
	public IChunkLoader getChunkLoader(WorldProvider provider)
    {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }

    /**
     * Loads and returns the world info
     */
    @Override
	public WorldInfo loadWorldInfo()
    {
        File var1 = new File(this.worldDirectory, "level.dat");
        NBTTagCompound var2;
        NBTTagCompound var3;

        if (var1.exists())
        {
            try
            {
                var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
                var3 = var2.getCompoundTag("Data");
                return new WorldInfo(var3);
            }
            catch (Exception var5)
            {
                var5.printStackTrace();
            }
        }

        var1 = new File(this.worldDirectory, "level.dat_old");

        if (var1.exists())
        {
            try
            {
                var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
                var3 = var2.getCompoundTag("Data");
                return new WorldInfo(var3);
            }
            catch (Exception var4)
            {
                var4.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Saves the given World Info with the given NBTTagCompound as the Player.
     */
    @Override
	public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound)
    {
        NBTTagCompound var3 = worldInformation.cloneNBTCompound(tagCompound);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setTag("Data", var3);

        try
        {
            File var5 = new File(this.worldDirectory, "level.dat_new");
            File var6 = new File(this.worldDirectory, "level.dat_old");
            File var7 = new File(this.worldDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(var4, new FileOutputStream(var5));

            if (var6.exists())
            {
                var6.delete();
            }

            var7.renameTo(var6);

            if (var7.exists())
            {
                var7.delete();
            }

            var5.renameTo(var7);

            if (var5.exists())
            {
                var5.delete();
            }
        }
        catch (Exception var8)
        {
            var8.printStackTrace();
        }
    }

    /**
     * used to update level.dat from old format to MCRegion format
     */
    @Override
	public void saveWorldInfo(WorldInfo worldInformation)
    {
        NBTTagCompound var2 = worldInformation.getNBTTagCompound();
        NBTTagCompound var3 = new NBTTagCompound();
        var3.setTag("Data", var2);

        try
        {
            File var4 = new File(this.worldDirectory, "level.dat_new");
            File var5 = new File(this.worldDirectory, "level.dat_old");
            File var6 = new File(this.worldDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(var3, new FileOutputStream(var4));

            if (var5.exists())
            {
                var5.delete();
            }

            var6.renameTo(var5);

            if (var6.exists())
            {
                var6.delete();
            }

            var4.renameTo(var6);

            if (var4.exists())
            {
                var4.delete();
            }
        }
        catch (Exception var7)
        {
            var7.printStackTrace();
        }
    }

    /**
     * Writes the player data to disk from the specified PlayerEntityMP.
     */
    @Override
	public void writePlayerData(EntityPlayer player)
    {
        try
        {
            NBTTagCompound var2 = new NBTTagCompound();
            player.writeToNBT(var2);
            File var3 = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat.tmp");
            File var4 = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat");
            CompressedStreamTools.writeCompressed(var2, new FileOutputStream(var3));

            if (var4.exists())
            {
                var4.delete();
            }

            var3.renameTo(var4);
        }
        catch (Exception var5)
        {
            logger.warn("Failed to save player data for " + player.getCommandSenderName());
        }
    }

    /**
     * Reads the player data from disk into the specified PlayerEntityMP.
     */
    @Override
	public NBTTagCompound readPlayerData(EntityPlayer player)
    {
        NBTTagCompound var2 = null;

        try
        {
            File var3 = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat");

            if (var3.exists() && var3.isFile())
            {
                var2 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
            }
        }
        catch (Exception var4)
        {
            logger.warn("Failed to load player data for " + player.getCommandSenderName());
        }

        if (var2 != null)
        {
            player.readFromNBT(var2);
        }

        return var2;
    }

    @Override
	public IPlayerFileData getPlayerNBTManager()
    {
        return this;
    }

    /**
     * Returns an array of usernames for which player.dat exists for.
     */
    @Override
	public String[] getAvailablePlayerDat()
    {
        String[] var1 = this.playersDirectory.list();

        if (var1 == null)
        {
            var1 = new String[0];
        }

        for (int var2 = 0; var2 < var1.length; ++var2)
        {
            if (var1[var2].endsWith(".dat"))
            {
                var1[var2] = var1[var2].substring(0, var1[var2].length() - 4);
            }
        }

        return var1;
    }

    /**
     * Called to flush all changes to disk, waiting for them to complete.
     */
    @Override
	public void flush() {}

    /**
     * Gets the file location of the given map
     */
    @Override
	public File getMapFileFromName(String mapName)
    {
        return new File(this.mapDataDir, mapName + ".dat");
    }

    /**
     * Returns the name of the directory where world information is saved.
     */
    @Override
	public String getWorldDirectoryName()
    {
        return this.saveDirectoryName;
    }
}
