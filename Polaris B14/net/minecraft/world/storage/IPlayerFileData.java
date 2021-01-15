package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract interface IPlayerFileData
{
  public abstract void writePlayerData(EntityPlayer paramEntityPlayer);
  
  public abstract NBTTagCompound readPlayerData(EntityPlayer paramEntityPlayer);
  
  public abstract String[] getAvailablePlayerDat();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\IPlayerFileData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */