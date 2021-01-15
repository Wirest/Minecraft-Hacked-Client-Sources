package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public abstract interface ISaveHandler
{
  public abstract WorldInfo loadWorldInfo();
  
  public abstract void checkSessionLock()
    throws MinecraftException;
  
  public abstract IChunkLoader getChunkLoader(WorldProvider paramWorldProvider);
  
  public abstract void saveWorldInfoWithPlayer(WorldInfo paramWorldInfo, NBTTagCompound paramNBTTagCompound);
  
  public abstract void saveWorldInfo(WorldInfo paramWorldInfo);
  
  public abstract IPlayerFileData getPlayerNBTManager();
  
  public abstract void flush();
  
  public abstract File getWorldDirectory();
  
  public abstract File getMapFileFromName(String paramString);
  
  public abstract String getWorldDirectoryName();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\ISaveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */