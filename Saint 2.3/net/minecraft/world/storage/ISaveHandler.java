package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public interface ISaveHandler {
   WorldInfo loadWorldInfo();

   void checkSessionLock() throws MinecraftException;

   IChunkLoader getChunkLoader(WorldProvider var1);

   void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2);

   void saveWorldInfo(WorldInfo var1);

   IPlayerFileData getPlayerNBTManager();

   void flush();

   File getWorldDirectory();

   File getMapFileFromName(String var1);

   String getWorldDirectoryName();
}
