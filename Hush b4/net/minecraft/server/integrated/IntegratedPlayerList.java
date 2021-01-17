// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.ServerConfigurationManager;

public class IntegratedPlayerList extends ServerConfigurationManager
{
    private NBTTagCompound hostPlayerData;
    
    public IntegratedPlayerList(final IntegratedServer p_i1314_1_) {
        super(p_i1314_1_);
        this.setViewDistance(10);
    }
    
    @Override
    protected void writePlayerData(final EntityPlayerMP playerIn) {
        if (playerIn.getName().equals(this.getServerInstance().getServerOwner())) {
            playerIn.writeToNBT(this.hostPlayerData = new NBTTagCompound());
        }
        super.writePlayerData(playerIn);
    }
    
    @Override
    public String allowUserToConnect(final SocketAddress address, final GameProfile profile) {
        return (profile.getName().equalsIgnoreCase(this.getServerInstance().getServerOwner()) && this.getPlayerByUsername(profile.getName()) != null) ? "That name is already taken." : super.allowUserToConnect(address, profile);
    }
    
    @Override
    public IntegratedServer getServerInstance() {
        return (IntegratedServer)super.getServerInstance();
    }
    
    @Override
    public NBTTagCompound getHostPlayerData() {
        return this.hostPlayerData;
    }
}
