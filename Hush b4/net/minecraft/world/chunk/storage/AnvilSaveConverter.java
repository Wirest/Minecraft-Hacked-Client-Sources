// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

import java.util.Collections;
import java.io.FilenameFilter;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutput;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import java.util.Iterator;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.WorldType;
import java.util.Collection;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.client.AnvilConverterException;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.storage.SaveFormatComparator;
import java.util.List;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.storage.SaveFormatOld;

public class AnvilSaveConverter extends SaveFormatOld
{
    private static final Logger logger;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public AnvilSaveConverter(final File p_i2144_1_) {
        super(p_i2144_1_);
    }
    
    @Override
    public String getName() {
        return "Anvil";
    }
    
    @Override
    public List<SaveFormatComparator> getSaveList() throws AnvilConverterException {
        if (this.savesDirectory != null && this.savesDirectory.exists() && this.savesDirectory.isDirectory()) {
            final List<SaveFormatComparator> list = (List<SaveFormatComparator>)Lists.newArrayList();
            final File[] afile = this.savesDirectory.listFiles();
            File[] array;
            for (int length = (array = afile).length, j = 0; j < length; ++j) {
                final File file1 = array[j];
                if (file1.isDirectory()) {
                    final String s = file1.getName();
                    final WorldInfo worldinfo = this.getWorldInfo(s);
                    if (worldinfo != null && (worldinfo.getSaveVersion() == 19132 || worldinfo.getSaveVersion() == 19133)) {
                        final boolean flag = worldinfo.getSaveVersion() != this.getSaveVersion();
                        String s2 = worldinfo.getWorldName();
                        if (StringUtils.isEmpty(s2)) {
                            s2 = s;
                        }
                        final long i = 0L;
                        list.add(new SaveFormatComparator(s, s2, worldinfo.getLastTimePlayed(), i, worldinfo.getGameType(), flag, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
                    }
                }
            }
            return list;
        }
        throw new AnvilConverterException("Unable to read or access folder where game worlds are saved!");
    }
    
    protected int getSaveVersion() {
        return 19133;
    }
    
    @Override
    public void flushCache() {
        RegionFileCache.clearRegionFileReferences();
    }
    
    @Override
    public ISaveHandler getSaveLoader(final String saveName, final boolean storePlayerdata) {
        return new AnvilSaveHandler(this.savesDirectory, saveName, storePlayerdata);
    }
    
    @Override
    public boolean func_154334_a(final String saveName) {
        final WorldInfo worldinfo = this.getWorldInfo(saveName);
        return worldinfo != null && worldinfo.getSaveVersion() == 19132;
    }
    
    @Override
    public boolean isOldMapFormat(final String saveName) {
        final WorldInfo worldinfo = this.getWorldInfo(saveName);
        return worldinfo != null && worldinfo.getSaveVersion() != this.getSaveVersion();
    }
    
    @Override
    public boolean convertMapFormat(final String filename, final IProgressUpdate progressCallback) {
        progressCallback.setLoadingProgress(0);
        final List<File> list = (List<File>)Lists.newArrayList();
        final List<File> list2 = (List<File>)Lists.newArrayList();
        final List<File> list3 = (List<File>)Lists.newArrayList();
        final File file1 = new File(this.savesDirectory, filename);
        final File file2 = new File(file1, "DIM-1");
        final File file3 = new File(file1, "DIM1");
        AnvilSaveConverter.logger.info("Scanning folders...");
        this.addRegionFilesToCollection(file1, list);
        if (file2.exists()) {
            this.addRegionFilesToCollection(file2, list2);
        }
        if (file3.exists()) {
            this.addRegionFilesToCollection(file3, list3);
        }
        final int i = list.size() + list2.size() + list3.size();
        AnvilSaveConverter.logger.info("Total conversion count is " + i);
        final WorldInfo worldinfo = this.getWorldInfo(filename);
        WorldChunkManager worldchunkmanager = null;
        if (worldinfo.getTerrainType() == WorldType.FLAT) {
            worldchunkmanager = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5f);
        }
        else {
            worldchunkmanager = new WorldChunkManager(worldinfo.getSeed(), worldinfo.getTerrainType(), worldinfo.getGeneratorOptions());
        }
        this.convertFile(new File(file1, "region"), list, worldchunkmanager, 0, i, progressCallback);
        this.convertFile(new File(file2, "region"), list2, new WorldChunkManagerHell(BiomeGenBase.hell, 0.0f), list.size(), i, progressCallback);
        this.convertFile(new File(file3, "region"), list3, new WorldChunkManagerHell(BiomeGenBase.sky, 0.0f), list.size() + list2.size(), i, progressCallback);
        worldinfo.setSaveVersion(19133);
        if (worldinfo.getTerrainType() == WorldType.DEFAULT_1_1) {
            worldinfo.setTerrainType(WorldType.DEFAULT);
        }
        this.createFile(filename);
        final ISaveHandler isavehandler = this.getSaveLoader(filename, false);
        isavehandler.saveWorldInfo(worldinfo);
        return true;
    }
    
    private void createFile(final String filename) {
        final File file1 = new File(this.savesDirectory, filename);
        if (!file1.exists()) {
            AnvilSaveConverter.logger.warn("Unable to create level.dat_mcr backup");
        }
        else {
            final File file2 = new File(file1, "level.dat");
            if (!file2.exists()) {
                AnvilSaveConverter.logger.warn("Unable to create level.dat_mcr backup");
            }
            else {
                final File file3 = new File(file1, "level.dat_mcr");
                if (!file2.renameTo(file3)) {
                    AnvilSaveConverter.logger.warn("Unable to create level.dat_mcr backup");
                }
            }
        }
    }
    
    private void convertFile(final File p_75813_1_, final Iterable<File> p_75813_2_, final WorldChunkManager p_75813_3_, int p_75813_4_, final int p_75813_5_, final IProgressUpdate p_75813_6_) {
        for (final File file1 : p_75813_2_) {
            this.convertChunks(p_75813_1_, file1, p_75813_3_, p_75813_4_, p_75813_5_, p_75813_6_);
            ++p_75813_4_;
            final int i = (int)Math.round(100.0 * p_75813_4_ / p_75813_5_);
            p_75813_6_.setLoadingProgress(i);
        }
    }
    
    private void convertChunks(final File p_75811_1_, final File p_75811_2_, final WorldChunkManager p_75811_3_, final int p_75811_4_, final int p_75811_5_, final IProgressUpdate progressCallback) {
        try {
            final String s = p_75811_2_.getName();
            final RegionFile regionfile = new RegionFile(p_75811_2_);
            final RegionFile regionfile2 = new RegionFile(new File(p_75811_1_, String.valueOf(s.substring(0, s.length() - ".mcr".length())) + ".mca"));
            for (int i = 0; i < 32; ++i) {
                for (int j = 0; j < 32; ++j) {
                    if (regionfile.isChunkSaved(i, j) && !regionfile2.isChunkSaved(i, j)) {
                        final DataInputStream datainputstream = regionfile.getChunkDataInputStream(i, j);
                        if (datainputstream == null) {
                            AnvilSaveConverter.logger.warn("Failed to fetch input stream");
                        }
                        else {
                            final NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
                            datainputstream.close();
                            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Level");
                            final ChunkLoader.AnvilConverterData chunkloader$anvilconverterdata = ChunkLoader.load(nbttagcompound2);
                            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
                            final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
                            nbttagcompound3.setTag("Level", nbttagcompound4);
                            ChunkLoader.convertToAnvilFormat(chunkloader$anvilconverterdata, nbttagcompound4, p_75811_3_);
                            final DataOutputStream dataoutputstream = regionfile2.getChunkDataOutputStream(i, j);
                            CompressedStreamTools.write(nbttagcompound3, dataoutputstream);
                            dataoutputstream.close();
                        }
                    }
                }
                final int k = (int)Math.round(100.0 * (p_75811_4_ * 1024) / (p_75811_5_ * 1024));
                final int l = (int)Math.round(100.0 * ((i + 1) * 32 + p_75811_4_ * 1024) / (p_75811_5_ * 1024));
                if (l > k) {
                    progressCallback.setLoadingProgress(l);
                }
            }
            regionfile.close();
            regionfile2.close();
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private void addRegionFilesToCollection(final File worldDir, final Collection<File> collection) {
        final File file1 = new File(worldDir, "region");
        final File[] afile = file1.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File p_accept_1_, final String p_accept_2_) {
                return p_accept_2_.endsWith(".mcr");
            }
        });
        if (afile != null) {
            Collections.addAll(collection, afile);
        }
    }
}
