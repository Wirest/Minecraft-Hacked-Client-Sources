package net.minecraft.world.chunk.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

public class AnvilSaveHandler extends SaveHandler {
   public AnvilSaveHandler(File savesDirectory, String p_i2142_2_, boolean storePlayerdata) {
      super(savesDirectory, p_i2142_2_, storePlayerdata);
   }

   public IChunkLoader getChunkLoader(WorldProvider provider) {
      File file1 = this.getWorldDirectory();
      File file2;
      if (provider instanceof WorldProviderHell) {
         file2 = new File(file1, "DIM-1");
         file2.mkdirs();
         return new AnvilChunkLoader(file2);
      } else if (provider instanceof WorldProviderEnd) {
         file2 = new File(file1, "DIM1");
         file2.mkdirs();
         return new AnvilChunkLoader(file2);
      } else {
         return new AnvilChunkLoader(file1);
      }
   }

   public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {
      worldInformation.setSaveVersion(19133);
      super.saveWorldInfoWithPlayer(worldInformation, tagCompound);
   }

   public void flush() {
      try {
         ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      RegionFileCache.clearRegionFileReferences();
   }
}
