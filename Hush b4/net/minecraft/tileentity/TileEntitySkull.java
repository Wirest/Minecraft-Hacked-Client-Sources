// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import java.util.UUID;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.GameProfile;

public class TileEntitySkull extends TileEntity
{
    private int skullType;
    private int skullRotation;
    private GameProfile playerProfile;
    
    public TileEntitySkull() {
        this.playerProfile = null;
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte)(this.skullType & 0xFF));
        compound.setByte("Rot", (byte)(this.skullRotation & 0xFF));
        if (this.playerProfile != null) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
            compound.setTag("Owner", nbttagcompound);
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.skullType = compound.getByte("SkullType");
        this.skullRotation = compound.getByte("Rot");
        if (this.skullType == 3) {
            if (compound.hasKey("Owner", 10)) {
                this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
            }
            else if (compound.hasKey("ExtraType", 8)) {
                final String s = compound.getString("ExtraType");
                if (!StringUtils.isNullOrEmpty(s)) {
                    this.playerProfile = new GameProfile(null, s);
                    this.updatePlayerProfile();
                }
            }
        }
    }
    
    public GameProfile getPlayerProfile() {
        return this.playerProfile;
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 4, nbttagcompound);
    }
    
    public void setType(final int type) {
        this.skullType = type;
        this.playerProfile = null;
    }
    
    public void setPlayerProfile(final GameProfile playerProfile) {
        this.skullType = 3;
        this.playerProfile = playerProfile;
        this.updatePlayerProfile();
    }
    
    private void updatePlayerProfile() {
        this.playerProfile = updateGameprofile(this.playerProfile);
        this.markDirty();
    }
    
    public static GameProfile updateGameprofile(final GameProfile input) {
        if (input == null || StringUtils.isNullOrEmpty(input.getName())) {
            return input;
        }
        if (input.isComplete() && input.getProperties().containsKey("textures")) {
            return input;
        }
        if (MinecraftServer.getServer() == null) {
            return input;
        }
        GameProfile gameprofile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(input.getName());
        if (gameprofile == null) {
            return input;
        }
        final Property property = Iterables.getFirst((Iterable<? extends Property>)((ForwardingMultimap<String, Object>)gameprofile.getProperties()).get("textures"), (Property)null);
        if (property == null) {
            gameprofile = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(gameprofile, true);
        }
        return gameprofile;
    }
    
    public int getSkullType() {
        return this.skullType;
    }
    
    public int getSkullRotation() {
        return this.skullRotation;
    }
    
    public void setSkullRotation(final int rotation) {
        this.skullRotation = rotation;
    }
}
