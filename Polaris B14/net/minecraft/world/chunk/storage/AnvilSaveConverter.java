/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.AnvilConverterException;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*     */ import net.minecraft.world.storage.ISaveHandler;
/*     */ import net.minecraft.world.storage.SaveFormatComparator;
/*     */ import net.minecraft.world.storage.SaveFormatOld;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AnvilSaveConverter extends SaveFormatOld
/*     */ {
/*  30 */   private static final Logger logger = ;
/*     */   
/*     */   public AnvilSaveConverter(File p_i2144_1_)
/*     */   {
/*  34 */     super(p_i2144_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  42 */     return "Anvil";
/*     */   }
/*     */   
/*     */   public List<SaveFormatComparator> getSaveList() throws AnvilConverterException
/*     */   {
/*  47 */     if ((this.savesDirectory != null) && (this.savesDirectory.exists()) && (this.savesDirectory.isDirectory()))
/*     */     {
/*  49 */       List<SaveFormatComparator> list = Lists.newArrayList();
/*  50 */       File[] afile = this.savesDirectory.listFiles();
/*     */       File[] arrayOfFile1;
/*  52 */       int j = (arrayOfFile1 = afile).length; for (int i = 0; i < j; i++) { File file1 = arrayOfFile1[i];
/*     */         
/*  54 */         if (file1.isDirectory())
/*     */         {
/*  56 */           String s = file1.getName();
/*  57 */           WorldInfo worldinfo = getWorldInfo(s);
/*     */           
/*  59 */           if ((worldinfo != null) && ((worldinfo.getSaveVersion() == 19132) || (worldinfo.getSaveVersion() == 19133)))
/*     */           {
/*  61 */             boolean flag = worldinfo.getSaveVersion() != getSaveVersion();
/*  62 */             String s1 = worldinfo.getWorldName();
/*     */             
/*  64 */             if (StringUtils.isEmpty(s1))
/*     */             {
/*  66 */               s1 = s;
/*     */             }
/*     */             
/*  69 */             long i = 0L;
/*  70 */             list.add(new SaveFormatComparator(s, s1, worldinfo.getLastTimePlayed(), i, worldinfo.getGameType(), flag, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  75 */       return list;
/*     */     }
/*     */     
/*     */ 
/*  79 */     throw new AnvilConverterException("Unable to read or access folder where game worlds are saved!");
/*     */   }
/*     */   
/*     */ 
/*     */   protected int getSaveVersion()
/*     */   {
/*  85 */     return 19133;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushCache() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata)
/*     */   {
/*  98 */     return new AnvilSaveHandler(this.savesDirectory, saveName, storePlayerdata);
/*     */   }
/*     */   
/*     */   public boolean func_154334_a(String saveName)
/*     */   {
/* 103 */     WorldInfo worldinfo = getWorldInfo(saveName);
/* 104 */     return (worldinfo != null) && (worldinfo.getSaveVersion() == 19132);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOldMapFormat(String saveName)
/*     */   {
/* 112 */     WorldInfo worldinfo = getWorldInfo(saveName);
/* 113 */     return (worldinfo != null) && (worldinfo.getSaveVersion() != getSaveVersion());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean convertMapFormat(String filename, IProgressUpdate progressCallback)
/*     */   {
/* 121 */     progressCallback.setLoadingProgress(0);
/* 122 */     List<File> list = Lists.newArrayList();
/* 123 */     List<File> list1 = Lists.newArrayList();
/* 124 */     List<File> list2 = Lists.newArrayList();
/* 125 */     File file1 = new File(this.savesDirectory, filename);
/* 126 */     File file2 = new File(file1, "DIM-1");
/* 127 */     File file3 = new File(file1, "DIM1");
/* 128 */     logger.info("Scanning folders...");
/* 129 */     addRegionFilesToCollection(file1, list);
/*     */     
/* 131 */     if (file2.exists())
/*     */     {
/* 133 */       addRegionFilesToCollection(file2, list1);
/*     */     }
/*     */     
/* 136 */     if (file3.exists())
/*     */     {
/* 138 */       addRegionFilesToCollection(file3, list2);
/*     */     }
/*     */     
/* 141 */     int i = list.size() + list1.size() + list2.size();
/* 142 */     logger.info("Total conversion count is " + i);
/* 143 */     WorldInfo worldinfo = getWorldInfo(filename);
/* 144 */     WorldChunkManager worldchunkmanager = null;
/*     */     
/* 146 */     if (worldinfo.getTerrainType() == WorldType.FLAT)
/*     */     {
/* 148 */       worldchunkmanager = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F);
/*     */     }
/*     */     else
/*     */     {
/* 152 */       worldchunkmanager = new WorldChunkManager(worldinfo.getSeed(), worldinfo.getTerrainType(), worldinfo.getGeneratorOptions());
/*     */     }
/*     */     
/* 155 */     convertFile(new File(file1, "region"), list, worldchunkmanager, 0, i, progressCallback);
/* 156 */     convertFile(new File(file2, "region"), list1, new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F), list.size(), i, progressCallback);
/* 157 */     convertFile(new File(file3, "region"), list2, new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F), list.size() + list1.size(), i, progressCallback);
/* 158 */     worldinfo.setSaveVersion(19133);
/*     */     
/* 160 */     if (worldinfo.getTerrainType() == WorldType.DEFAULT_1_1)
/*     */     {
/* 162 */       worldinfo.setTerrainType(WorldType.DEFAULT);
/*     */     }
/*     */     
/* 165 */     createFile(filename);
/* 166 */     ISaveHandler isavehandler = getSaveLoader(filename, false);
/* 167 */     isavehandler.saveWorldInfo(worldinfo);
/* 168 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void createFile(String filename)
/*     */   {
/* 176 */     File file1 = new File(this.savesDirectory, filename);
/*     */     
/* 178 */     if (!file1.exists())
/*     */     {
/* 180 */       logger.warn("Unable to create level.dat_mcr backup");
/*     */     }
/*     */     else
/*     */     {
/* 184 */       File file2 = new File(file1, "level.dat");
/*     */       
/* 186 */       if (!file2.exists())
/*     */       {
/* 188 */         logger.warn("Unable to create level.dat_mcr backup");
/*     */       }
/*     */       else
/*     */       {
/* 192 */         File file3 = new File(file1, "level.dat_mcr");
/*     */         
/* 194 */         if (!file2.renameTo(file3))
/*     */         {
/* 196 */           logger.warn("Unable to create level.dat_mcr backup");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void convertFile(File p_75813_1_, Iterable<File> p_75813_2_, WorldChunkManager p_75813_3_, int p_75813_4_, int p_75813_5_, IProgressUpdate p_75813_6_)
/*     */   {
/* 204 */     for (File file1 : p_75813_2_)
/*     */     {
/* 206 */       convertChunks(p_75813_1_, file1, p_75813_3_, p_75813_4_, p_75813_5_, p_75813_6_);
/* 207 */       p_75813_4_++;
/* 208 */       int i = (int)Math.round(100.0D * p_75813_4_ / p_75813_5_);
/* 209 */       p_75813_6_.setLoadingProgress(i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void convertChunks(File p_75811_1_, File p_75811_2_, WorldChunkManager p_75811_3_, int p_75811_4_, int p_75811_5_, IProgressUpdate progressCallback)
/*     */   {
/*     */     try
/*     */     {
/* 220 */       String s = p_75811_2_.getName();
/* 221 */       RegionFile regionfile = new RegionFile(p_75811_2_);
/* 222 */       RegionFile regionfile1 = new RegionFile(new File(p_75811_1_, s.substring(0, s.length() - ".mcr".length()) + ".mca"));
/*     */       
/* 224 */       for (int i = 0; i < 32; i++)
/*     */       {
/* 226 */         for (int j = 0; j < 32; j++)
/*     */         {
/* 228 */           if ((regionfile.isChunkSaved(i, j)) && (!regionfile1.isChunkSaved(i, j)))
/*     */           {
/* 230 */             DataInputStream datainputstream = regionfile.getChunkDataInputStream(i, j);
/*     */             
/* 232 */             if (datainputstream == null)
/*     */             {
/* 234 */               logger.warn("Failed to fetch input stream");
/*     */             }
/*     */             else
/*     */             {
/* 238 */               NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
/* 239 */               datainputstream.close();
/* 240 */               NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");
/* 241 */               ChunkLoader.AnvilConverterData chunkloader$anvilconverterdata = ChunkLoader.load(nbttagcompound1);
/* 242 */               NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/* 243 */               NBTTagCompound nbttagcompound3 = new NBTTagCompound();
/* 244 */               nbttagcompound2.setTag("Level", nbttagcompound3);
/* 245 */               ChunkLoader.convertToAnvilFormat(chunkloader$anvilconverterdata, nbttagcompound3, p_75811_3_);
/* 246 */               DataOutputStream dataoutputstream = regionfile1.getChunkDataOutputStream(i, j);
/* 247 */               CompressedStreamTools.write(nbttagcompound2, dataoutputstream);
/* 248 */               dataoutputstream.close();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 253 */         int k = (int)Math.round(100.0D * (p_75811_4_ * 1024) / (p_75811_5_ * 1024));
/* 254 */         int l = (int)Math.round(100.0D * ((i + 1) * 32 + p_75811_4_ * 1024) / (p_75811_5_ * 1024));
/*     */         
/* 256 */         if (l > k)
/*     */         {
/* 258 */           progressCallback.setLoadingProgress(l);
/*     */         }
/*     */       }
/*     */       
/* 262 */       regionfile.close();
/* 263 */       regionfile1.close();
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 267 */       ioexception.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addRegionFilesToCollection(File worldDir, Collection<File> collection)
/*     */   {
/* 276 */     File file1 = new File(worldDir, "region");
/* 277 */     File[] afile = file1.listFiles(new FilenameFilter()
/*     */     {
/*     */       public boolean accept(File p_accept_1_, String p_accept_2_)
/*     */       {
/* 281 */         return p_accept_2_.endsWith(".mcr");
/*     */       }
/*     */     });
/*     */     
/* 285 */     if (afile != null)
/*     */     {
/* 287 */       Collections.addAll(collection, afile);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\storage\AnvilSaveConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */