// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import net.minecraft.util.IProgressUpdate;
import java.io.OutputStream;
import java.io.FileOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.FileInputStream;
import net.minecraft.client.AnvilConverterException;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat
{
    private static final Logger logger;
    protected final File savesDirectory;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public SaveFormatOld(final File p_i2147_1_) {
        if (!p_i2147_1_.exists()) {
            p_i2147_1_.mkdirs();
        }
        this.savesDirectory = p_i2147_1_;
    }
    
    @Override
    public String getName() {
        return "Old Format";
    }
    
    @Override
    public List<SaveFormatComparator> getSaveList() throws AnvilConverterException {
        final List<SaveFormatComparator> list = (List<SaveFormatComparator>)Lists.newArrayList();
        for (int i = 0; i < 5; ++i) {
            final String s = "World" + (i + 1);
            final WorldInfo worldinfo = this.getWorldInfo(s);
            if (worldinfo != null) {
                list.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
            }
        }
        return list;
    }
    
    @Override
    public void flushCache() {
    }
    
    @Override
    public WorldInfo getWorldInfo(final String saveName) {
        final File file1 = new File(this.savesDirectory, saveName);
        if (!file1.exists()) {
            return null;
        }
        File file2 = new File(file1, "level.dat");
        if (file2.exists()) {
            try {
                final NBTTagCompound nbttagcompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                final NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound3);
            }
            catch (Exception exception1) {
                SaveFormatOld.logger.error("Exception reading " + file2, exception1);
            }
        }
        file2 = new File(file1, "level.dat_old");
        if (file2.exists()) {
            try {
                final NBTTagCompound nbttagcompound4 = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                final NBTTagCompound nbttagcompound5 = nbttagcompound4.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound5);
            }
            catch (Exception exception2) {
                SaveFormatOld.logger.error("Exception reading " + file2, exception2);
            }
        }
        return null;
    }
    
    @Override
    public void renameWorld(final String dirName, final String newName) {
        final File file1 = new File(this.savesDirectory, dirName);
        if (file1.exists()) {
            final File file2 = new File(file1, "level.dat");
            if (file2.exists()) {
                try {
                    final NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
                    nbttagcompound2.setString("LevelName", newName);
                    CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public boolean func_154335_d(final String p_154335_1_) {
        final File file1 = new File(this.savesDirectory, p_154335_1_);
        if (file1.exists()) {
            return false;
        }
        try {
            file1.mkdir();
            file1.delete();
            return true;
        }
        catch (Throwable throwable) {
            SaveFormatOld.logger.warn("Couldn't make new level", throwable);
            return false;
        }
    }
    
    @Override
    public boolean deleteWorldDirectory(final String p_75802_1_) {
        final File file1 = new File(this.savesDirectory, p_75802_1_);
        if (!file1.exists()) {
            return true;
        }
        SaveFormatOld.logger.info("Deleting level " + p_75802_1_);
        for (int i = 1; i <= 5; ++i) {
            SaveFormatOld.logger.info("Attempt " + i + "...");
            if (deleteFiles(file1.listFiles())) {
                break;
            }
            SaveFormatOld.logger.warn("Unsuccessful in deleting contents.");
            if (i < 5) {
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ex) {}
            }
        }
        return file1.delete();
    }
    
    protected static boolean deleteFiles(final File[] files) {
        for (int i = 0; i < files.length; ++i) {
            final File file1 = files[i];
            SaveFormatOld.logger.debug("Deleting " + file1);
            if (file1.isDirectory() && !deleteFiles(file1.listFiles())) {
                SaveFormatOld.logger.warn("Couldn't delete directory " + file1);
                return false;
            }
            if (!file1.delete()) {
                SaveFormatOld.logger.warn("Couldn't delete file " + file1);
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ISaveHandler getSaveLoader(final String saveName, final boolean storePlayerdata) {
        return new SaveHandler(this.savesDirectory, saveName, storePlayerdata);
    }
    
    @Override
    public boolean func_154334_a(final String saveName) {
        return false;
    }
    
    @Override
    public boolean isOldMapFormat(final String saveName) {
        return false;
    }
    
    @Override
    public boolean convertMapFormat(final String filename, final IProgressUpdate progressCallback) {
        return false;
    }
    
    @Override
    public boolean canLoadWorld(final String p_90033_1_) {
        final File file1 = new File(this.savesDirectory, p_90033_1_);
        return file1.isDirectory();
    }
}
