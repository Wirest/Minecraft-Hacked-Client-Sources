package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData {
   void writePlayerData(EntityPlayer var1);

   NBTTagCompound readPlayerData(EntityPlayer var1);

   String[] getAvailablePlayerDat();
}
