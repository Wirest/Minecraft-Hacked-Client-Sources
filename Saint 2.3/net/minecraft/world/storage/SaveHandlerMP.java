package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public class SaveHandlerMP implements ISaveHandler {
   private static final String __OBFID = "CL_00000602";

   public WorldInfo loadWorldInfo() {
      return null;
   }

   public void checkSessionLock() throws MinecraftException {
   }

   public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
      return null;
   }

   public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {
   }

   public void saveWorldInfo(WorldInfo p_75761_1_) {
   }

   public IPlayerFileData getPlayerNBTManager() {
      return null;
   }

   public void flush() {
   }

   public File getMapFileFromName(String p_75758_1_) {
      return null;
   }

   public String getWorldDirectoryName() {
      return "none";
   }

   public File getWorldDirectory() {
      return null;
   }
}
