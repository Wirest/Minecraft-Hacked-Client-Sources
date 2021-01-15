/*    */ package net.minecraft.world.chunk.storage;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class RegionFileCache
/*    */ {
/* 12 */   private static final Map<File, RegionFile> regionsByFilename = ;
/*    */   
/*    */   public static synchronized RegionFile createOrLoadRegionFile(File worldDir, int chunkX, int chunkZ)
/*    */   {
/* 16 */     File file1 = new File(worldDir, "region");
/* 17 */     File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
/* 18 */     RegionFile regionfile = (RegionFile)regionsByFilename.get(file2);
/*    */     
/* 20 */     if (regionfile != null)
/*    */     {
/* 22 */       return regionfile;
/*    */     }
/*    */     
/*    */ 
/* 26 */     if (!file1.exists())
/*    */     {
/* 28 */       file1.mkdirs();
/*    */     }
/*    */     
/* 31 */     if (regionsByFilename.size() >= 256)
/*    */     {
/* 33 */       clearRegionFileReferences();
/*    */     }
/*    */     
/* 36 */     RegionFile regionfile1 = new RegionFile(file2);
/* 37 */     regionsByFilename.put(file2, regionfile1);
/* 38 */     return regionfile1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized void clearRegionFileReferences()
/*    */   {
/* 47 */     for (RegionFile regionfile : regionsByFilename.values())
/*    */     {
/*    */       try
/*    */       {
/* 51 */         if (regionfile != null)
/*    */         {
/* 53 */           regionfile.close();
/*    */         }
/*    */       }
/*    */       catch (IOException ioexception)
/*    */       {
/* 58 */         ioexception.printStackTrace();
/*    */       }
/*    */     }
/*    */     
/* 62 */     regionsByFilename.clear();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ)
/*    */   {
/* 70 */     RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
/* 71 */     return regionfile.getChunkDataInputStream(chunkX & 0x1F, chunkZ & 0x1F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ)
/*    */   {
/* 79 */     RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
/* 80 */     return regionfile.getChunkDataOutputStream(chunkX & 0x1F, chunkZ & 0x1F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\storage\RegionFileCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */