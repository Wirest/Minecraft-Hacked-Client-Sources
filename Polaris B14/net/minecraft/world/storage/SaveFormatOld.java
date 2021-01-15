/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.AnvilConverterException;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SaveFormatOld implements ISaveFormat
/*     */ {
/*  17 */   private static final Logger logger = ;
/*     */   
/*     */ 
/*     */   protected final File savesDirectory;
/*     */   
/*     */ 
/*     */ 
/*     */   public SaveFormatOld(File p_i2147_1_)
/*     */   {
/*  26 */     if (!p_i2147_1_.exists())
/*     */     {
/*  28 */       p_i2147_1_.mkdirs();
/*     */     }
/*     */     
/*  31 */     this.savesDirectory = p_i2147_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  39 */     return "Old Format";
/*     */   }
/*     */   
/*     */   public List<SaveFormatComparator> getSaveList() throws AnvilConverterException
/*     */   {
/*  44 */     List<SaveFormatComparator> list = Lists.newArrayList();
/*     */     
/*  46 */     for (int i = 0; i < 5; i++)
/*     */     {
/*  48 */       String s = "World" + (i + 1);
/*  49 */       WorldInfo worldinfo = getWorldInfo(s);
/*     */       
/*  51 */       if (worldinfo != null)
/*     */       {
/*  53 */         list.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
/*     */       }
/*     */     }
/*     */     
/*  57 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushCache() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public WorldInfo getWorldInfo(String saveName)
/*     */   {
/*  69 */     File file1 = new File(this.savesDirectory, saveName);
/*     */     
/*  71 */     if (!file1.exists())
/*     */     {
/*  73 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  77 */     File file2 = new File(file1, "level.dat");
/*     */     
/*  79 */     if (file2.exists())
/*     */     {
/*     */       try
/*     */       {
/*  83 */         NBTTagCompound nbttagcompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file2));
/*  84 */         NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
/*  85 */         return new WorldInfo(nbttagcompound3);
/*     */       }
/*     */       catch (Exception exception1)
/*     */       {
/*  89 */         logger.error("Exception reading " + file2, exception1);
/*     */       }
/*     */     }
/*     */     
/*  93 */     file2 = new File(file1, "level.dat_old");
/*     */     
/*  95 */     if (file2.exists())
/*     */     {
/*     */       try
/*     */       {
/*  99 */         NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
/* 100 */         NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
/* 101 */         return new WorldInfo(nbttagcompound1);
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 105 */         logger.error("Exception reading " + file2, exception);
/*     */       }
/*     */     }
/*     */     
/* 109 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renameWorld(String dirName, String newName)
/*     */   {
/* 119 */     File file1 = new File(this.savesDirectory, dirName);
/*     */     
/* 121 */     if (file1.exists())
/*     */     {
/* 123 */       File file2 = new File(file1, "level.dat");
/*     */       
/* 125 */       if (file2.exists())
/*     */       {
/*     */         try
/*     */         {
/* 129 */           NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
/* 130 */           NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
/* 131 */           nbttagcompound1.setString("LevelName", newName);
/* 132 */           CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/* 136 */           exception.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_154335_d(String p_154335_1_)
/*     */   {
/* 144 */     File file1 = new File(this.savesDirectory, p_154335_1_);
/*     */     
/* 146 */     if (file1.exists())
/*     */     {
/* 148 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 154 */       file1.mkdir();
/* 155 */       file1.delete();
/* 156 */       return true;
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 160 */       logger.warn("Couldn't make new level", throwable); }
/* 161 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean deleteWorldDirectory(String p_75802_1_)
/*     */   {
/* 172 */     File file1 = new File(this.savesDirectory, p_75802_1_);
/*     */     
/* 174 */     if (!file1.exists())
/*     */     {
/* 176 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 180 */     logger.info("Deleting level " + p_75802_1_);
/*     */     
/* 182 */     for (int i = 1; i <= 5; i++)
/*     */     {
/* 184 */       logger.info("Attempt " + i + "...");
/*     */       
/* 186 */       if (deleteFiles(file1.listFiles())) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 191 */       logger.warn("Unsuccessful in deleting contents.");
/*     */       
/* 193 */       if (i < 5)
/*     */       {
/*     */         try
/*     */         {
/* 197 */           Thread.sleep(500L);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 206 */     return file1.delete();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static boolean deleteFiles(File[] files)
/*     */   {
/* 216 */     for (int i = 0; i < files.length; i++)
/*     */     {
/* 218 */       File file1 = files[i];
/* 219 */       logger.debug("Deleting " + file1);
/*     */       
/* 221 */       if ((file1.isDirectory()) && (!deleteFiles(file1.listFiles())))
/*     */       {
/* 223 */         logger.warn("Couldn't delete directory " + file1);
/* 224 */         return false;
/*     */       }
/*     */       
/* 227 */       if (!file1.delete())
/*     */       {
/* 229 */         logger.warn("Couldn't delete file " + file1);
/* 230 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 234 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata)
/*     */   {
/* 242 */     return new SaveHandler(this.savesDirectory, saveName, storePlayerdata);
/*     */   }
/*     */   
/*     */   public boolean func_154334_a(String saveName)
/*     */   {
/* 247 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOldMapFormat(String saveName)
/*     */   {
/* 255 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean convertMapFormat(String filename, IProgressUpdate progressCallback)
/*     */   {
/* 263 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canLoadWorld(String p_90033_1_)
/*     */   {
/* 271 */     File file1 = new File(this.savesDirectory, p_90033_1_);
/* 272 */     return file1.isDirectory();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\SaveFormatOld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */