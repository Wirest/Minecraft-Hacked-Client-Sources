package net.minecraft.server.integrated;

import java.net.SocketAddress;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class IntegratedPlayerList extends ServerConfigurationManager
{
    /**
     * Holds the NBT data for the host player's save file, so this can be written to level.dat.
     */
    private NBTTagCompound hostPlayerData;

    public IntegratedPlayerList(IntegratedServer p_i1314_1_)
    {
        super(p_i1314_1_);
        this.setViewDistance(10);
    }

    /**
     * also stores the NBTTags if this is an intergratedPlayerList
     */
    @Override
	protected void writePlayerData(EntityPlayerMP playerIn)
    {
        if (playerIn.getCommandSenderName().equals(this.getIntegratedServerInstance().getServerOwner()))
        {
            this.hostPlayerData = new NBTTagCompound();
            playerIn.writeToNBT(this.hostPlayerData);
        }

        super.writePlayerData(playerIn);
    }

    /**
     * checks ban-lists, then white-lists, then space for the server. Returns null on success, or an error message
     */
    @Override
	public String allowUserToConnect(SocketAddress address, GameProfile profile)
    {
        return profile.getName().equalsIgnoreCase(this.getIntegratedServerInstance().getServerOwner()) && this.getPlayerByUsername(profile.getName()) != null ? "That name is already taken." : super.allowUserToConnect(address, profile);
    }

    public IntegratedServer getIntegratedServerInstance()
    {
        return (IntegratedServer)super.getServerInstance();
    }

    /**
     * On integrated servers, returns the host's player data to be written to level.dat.
     */
    @Override
	public NBTTagCompound getHostPlayerData()
    {
        return this.hostPlayerData;
    }

    @Override
	public MinecraftServer getServerInstance()
    {
        return this.getIntegratedServerInstance();
    }
}
