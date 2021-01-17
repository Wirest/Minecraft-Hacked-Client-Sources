// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Iterator;
import java.io.IOException;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;

public class RegionFileCache
{
    private static final Map<File, RegionFile> regionsByFilename;
    
    static {
        regionsByFilename = Maps.newHashMap();
    }
    
    public static synchronized RegionFile createOrLoadRegionFile(final File worldDir, final int chunkX, final int chunkZ) {
        final File file1 = new File(worldDir, "region");
        final File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        final RegionFile regionfile = RegionFileCache.regionsByFilename.get(file2);
        if (regionfile != null) {
            return regionfile;
        }
        if (!file1.exists()) {
            file1.mkdirs();
        }
        if (RegionFileCache.regionsByFilename.size() >= 256) {
            clearRegionFileReferences();
        }
        final RegionFile regionfile2 = new RegionFile(file2);
        RegionFileCache.regionsByFilename.put(file2, regionfile2);
        return regionfile2;
    }
    
    public static synchronized void clearRegionFileReferences() {
        for (final RegionFile regionfile : RegionFileCache.regionsByFilename.values()) {
            try {
                if (regionfile == null) {
                    continue;
                }
                regionfile.close();
            }
            catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        RegionFileCache.regionsByFilename.clear();
    }
    
    public static DataInputStream getChunkInputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataInputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    public static DataOutputStream getChunkOutputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataOutputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
}
