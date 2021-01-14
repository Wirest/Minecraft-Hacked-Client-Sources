package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class RegionFileCache {
   private static final Map regionsByFilename = Maps.newHashMap();

   public static synchronized RegionFile createOrLoadRegionFile(File worldDir, int chunkX, int chunkZ) {
      File file1 = new File(worldDir, "region");
      File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
      RegionFile regionfile = (RegionFile)regionsByFilename.get(file2);
      if (regionfile != null) {
         return regionfile;
      } else {
         if (!file1.exists()) {
            file1.mkdirs();
         }

         if (regionsByFilename.size() >= 256) {
            clearRegionFileReferences();
         }

         RegionFile regionfile1 = new RegionFile(file2);
         regionsByFilename.put(file2, regionfile1);
         return regionfile1;
      }
   }

   public static synchronized void clearRegionFileReferences() {
      Iterator var0 = regionsByFilename.values().iterator();

      while(var0.hasNext()) {
         RegionFile regionfile = (RegionFile)var0.next();

         try {
            if (regionfile != null) {
               regionfile.close();
            }
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      regionsByFilename.clear();
   }

   public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ) {
      RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
      return regionfile.getChunkDataInputStream(chunkX & 31, chunkZ & 31);
   }

   public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ) {
      RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
      return regionfile.getChunkDataOutputStream(chunkX & 31, chunkZ & 31);
   }
}
