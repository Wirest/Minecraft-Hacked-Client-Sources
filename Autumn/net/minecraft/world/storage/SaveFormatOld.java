package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat {
   private static final Logger logger = LogManager.getLogger();
   protected final File savesDirectory;

   public SaveFormatOld(File p_i2147_1_) {
      if (!p_i2147_1_.exists()) {
         p_i2147_1_.mkdirs();
      }

      this.savesDirectory = p_i2147_1_;
   }

   public String getName() {
      return "Old Format";
   }

   public List getSaveList() throws AnvilConverterException {
      List list = Lists.newArrayList();

      for(int i = 0; i < 5; ++i) {
         String s = "World" + (i + 1);
         WorldInfo worldinfo = this.getWorldInfo(s);
         if (worldinfo != null) {
            list.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
         }
      }

      return list;
   }

   public void flushCache() {
   }

   public WorldInfo getWorldInfo(String saveName) {
      File file1 = new File(this.savesDirectory, saveName);
      if (!file1.exists()) {
         return null;
      } else {
         File file2 = new File(file1, "level.dat");
         NBTTagCompound nbttagcompound;
         NBTTagCompound nbttagcompound1;
         if (file2.exists()) {
            try {
               nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
               nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
               return new WorldInfo(nbttagcompound1);
            } catch (Exception var7) {
               logger.error("Exception reading " + file2, var7);
            }
         }

         file2 = new File(file1, "level.dat_old");
         if (file2.exists()) {
            try {
               nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
               nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
               return new WorldInfo(nbttagcompound1);
            } catch (Exception var6) {
               logger.error("Exception reading " + file2, var6);
            }
         }

         return null;
      }
   }

   public void renameWorld(String dirName, String newName) {
      File file1 = new File(this.savesDirectory, dirName);
      if (file1.exists()) {
         File file2 = new File(file1, "level.dat");
         if (file2.exists()) {
            try {
               NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
               NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
               nbttagcompound1.setString("LevelName", newName);
               CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public boolean func_154335_d(String p_154335_1_) {
      File file1 = new File(this.savesDirectory, p_154335_1_);
      if (file1.exists()) {
         return false;
      } else {
         try {
            file1.mkdir();
            file1.delete();
            return true;
         } catch (Throwable var4) {
            logger.warn("Couldn't make new level", var4);
            return false;
         }
      }
   }

   public boolean deleteWorldDirectory(String p_75802_1_) {
      File file1 = new File(this.savesDirectory, p_75802_1_);
      if (!file1.exists()) {
         return true;
      } else {
         logger.info("Deleting level " + p_75802_1_);

         for(int i = 1; i <= 5; ++i) {
            logger.info("Attempt " + i + "...");
            if (deleteFiles(file1.listFiles())) {
               break;
            }

            logger.warn("Unsuccessful in deleting contents.");
            if (i < 5) {
               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var5) {
               }
            }
         }

         return file1.delete();
      }
   }

   protected static boolean deleteFiles(File[] files) {
      for(int i = 0; i < files.length; ++i) {
         File file1 = files[i];
         logger.debug("Deleting " + file1);
         if (file1.isDirectory() && !deleteFiles(file1.listFiles())) {
            logger.warn("Couldn't delete directory " + file1);
            return false;
         }

         if (!file1.delete()) {
            logger.warn("Couldn't delete file " + file1);
            return false;
         }
      }

      return true;
   }

   public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata) {
      return new SaveHandler(this.savesDirectory, saveName, storePlayerdata);
   }

   public boolean func_154334_a(String saveName) {
      return false;
   }

   public boolean isOldMapFormat(String saveName) {
      return false;
   }

   public boolean convertMapFormat(String filename, IProgressUpdate progressCallback) {
      return false;
   }

   public boolean canLoadWorld(String p_90033_1_) {
      File file1 = new File(this.savesDirectory, p_90033_1_);
      return file1.isDirectory();
   }
}
