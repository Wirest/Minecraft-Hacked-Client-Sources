package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RegionFileCache
{
    private static final Map<File, RegionFile> REGIONS_BY_FILE = Maps.<File, RegionFile>newHashMap();

    public static synchronized RegionFile createOrLoadRegionFile(File worldDir, int chunkX, int chunkZ)
    {
        File file1 = new File(worldDir, "region");
        File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        RegionFile regionfile = REGIONS_BY_FILE.get(file2);

        if (regionfile != null)
        {
            return regionfile;
        }
        else
        {
            if (!file1.exists())
            {
                file1.mkdirs();
            }

            if (REGIONS_BY_FILE.size() >= 256)
            {
                clearRegionFileReferences();
            }

            RegionFile regionfile1 = new RegionFile(file2);
            REGIONS_BY_FILE.put(file2, regionfile1);
            return regionfile1;
        }
    }

    public static synchronized RegionFile func_191065_b(File p_191065_0_, int p_191065_1_, int p_191065_2_)
    {
        File file1 = new File(p_191065_0_, "region");
        File file2 = new File(file1, "r." + (p_191065_1_ >> 5) + "." + (p_191065_2_ >> 5) + ".mca");
        RegionFile regionfile = REGIONS_BY_FILE.get(file2);

        if (regionfile != null)
        {
            return regionfile;
        }
        else if (file1.exists() && file2.exists())
        {
            if (REGIONS_BY_FILE.size() >= 256)
            {
                clearRegionFileReferences();
            }

            RegionFile regionfile1 = new RegionFile(file2);
            REGIONS_BY_FILE.put(file2, regionfile1);
            return regionfile1;
        }
        else
        {
            return null;
        }
    }

    /**
     * clears region file references
     */
    public static synchronized void clearRegionFileReferences()
    {
        for (RegionFile regionfile : REGIONS_BY_FILE.values())
        {
            try
            {
                if (regionfile != null)
                {
                    regionfile.close();
                }
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }

        REGIONS_BY_FILE.clear();
    }

    /**
     * Gets an input stream for the chunk at the specified location.
     */
    public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ)
    {
        RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataInputStream(chunkX & 31, chunkZ & 31);
    }

    /**
     * Gets an output stream for the specified chunk.
     */
    public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ)
    {
        RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataOutputStream(chunkX & 31, chunkZ & 31);
    }

    public static boolean func_191064_f(File p_191064_0_, int p_191064_1_, int p_191064_2_)
    {
        RegionFile regionfile = func_191065_b(p_191064_0_, p_191064_1_, p_191064_2_);
        return regionfile != null ? regionfile.isChunkSaved(p_191064_1_ & 31, p_191064_2_ & 31) : false;
    }
}
