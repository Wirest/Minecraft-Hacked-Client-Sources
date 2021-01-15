/*    */ package net.minecraft.world.storage;
/*    */ 
/*    */ import java.io.File;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.MinecraftException;
/*    */ import net.minecraft.world.WorldProvider;
/*    */ import net.minecraft.world.chunk.storage.IChunkLoader;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SaveHandlerMP
/*    */   implements ISaveHandler
/*    */ {
/*    */   public WorldInfo loadWorldInfo()
/*    */   {
/* 16 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void checkSessionLock()
/*    */     throws MinecraftException
/*    */   {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IChunkLoader getChunkLoader(WorldProvider provider)
/*    */   {
/* 31 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void saveWorldInfo(WorldInfo worldInformation) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IPlayerFileData getPlayerNBTManager()
/*    */   {
/* 50 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void flush() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public File getMapFileFromName(String mapName)
/*    */   {
/* 65 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getWorldDirectoryName()
/*    */   {
/* 73 */     return "none";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public File getWorldDirectory()
/*    */   {
/* 81 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\SaveHandlerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */