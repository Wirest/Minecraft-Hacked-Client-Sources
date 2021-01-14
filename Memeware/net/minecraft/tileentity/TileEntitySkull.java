package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity {
    private int skullType;
    private int skullRotation;
    private GameProfile playerProfile = null;
    private static final String __OBFID = "CL_00000364";

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte) (this.skullType & 255));
        compound.setByte("Rot", (byte) (this.skullRotation & 255));

        if (this.playerProfile != null) {
            NBTTagCompound var2 = new NBTTagCompound();
            NBTUtil.writeGameProfile(var2, this.playerProfile);
            compound.setTag("Owner", var2);
        }
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.skullType = compound.getByte("SkullType");
        this.skullRotation = compound.getByte("Rot");

        if (this.skullType == 3) {
            if (compound.hasKey("Owner", 10)) {
                this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
            } else if (compound.hasKey("ExtraType", 8)) {
                String var2 = compound.getString("ExtraType");

                if (!StringUtils.isNullOrEmpty(var2)) {
                    this.playerProfile = new GameProfile((UUID) null, var2);
                    this.func_152109_d();
                }
            }
        }
    }

    public GameProfile getPlayerProfile() {
        return this.playerProfile;
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.pos, 4, var1);
    }

    public void setType(int type) {
        this.skullType = type;
        this.playerProfile = null;
    }

    public void setPlayerProfile(GameProfile playerProfile) {
        this.skullType = 3;
        this.playerProfile = playerProfile;
        this.func_152109_d();
    }

    private void func_152109_d() {
        this.playerProfile = updateGameprofile(this.playerProfile);
        this.markDirty();
    }

    public static GameProfile updateGameprofile(GameProfile input) {
        if (input != null && !StringUtils.isNullOrEmpty(input.getName())) {
            if (input.isComplete() && input.getProperties().containsKey("textures")) {
                return input;
            } else if (MinecraftServer.getServer() == null) {
                return input;
            } else {
                GameProfile var1 = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(input.getName());

                if (var1 == null) {
                    return input;
                } else {
                    Property var2 = (Property) Iterables.getFirst(var1.getProperties().get("textures"), (Object) null);

                    if (var2 == null) {
                        var1 = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(var1, true);
                    }

                    return var1;
                }
            }
        } else {
            return input;
        }
    }

    public int getSkullType() {
        return this.skullType;
    }

    public int getSkullRotation() {
        return this.skullRotation;
    }

    public void setSkullRotation(int rotation) {
        this.skullRotation = rotation;
    }
}
