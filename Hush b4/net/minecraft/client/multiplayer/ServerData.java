// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.nbt.NBTTagCompound;

public class ServerData
{
    public String serverName;
    public String serverIP;
    public String populationInfo;
    public String serverMOTD;
    public long pingToServer;
    public int version;
    public String gameVersion;
    public boolean field_78841_f;
    public String playerList;
    private ServerResourceMode resourceMode;
    private String serverIcon;
    private boolean field_181042_l;
    
    public ServerData(final String p_i46420_1_, final String p_i46420_2_, final boolean p_i46420_3_) {
        this.version = 47;
        this.gameVersion = "1.8.8";
        this.resourceMode = ServerResourceMode.PROMPT;
        this.serverName = p_i46420_1_;
        this.serverIP = p_i46420_2_;
        this.field_181042_l = p_i46420_3_;
    }
    
    public NBTTagCompound getNBTCompound() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("name", this.serverName);
        nbttagcompound.setString("ip", this.serverIP);
        if (this.serverIcon != null) {
            nbttagcompound.setString("icon", this.serverIcon);
        }
        if (this.resourceMode == ServerResourceMode.ENABLED) {
            nbttagcompound.setBoolean("acceptTextures", true);
        }
        else if (this.resourceMode == ServerResourceMode.DISABLED) {
            nbttagcompound.setBoolean("acceptTextures", false);
        }
        return nbttagcompound;
    }
    
    public ServerResourceMode getResourceMode() {
        return this.resourceMode;
    }
    
    public void setResourceMode(final ServerResourceMode mode) {
        this.resourceMode = mode;
    }
    
    public static ServerData getServerDataFromNBTCompound(final NBTTagCompound nbtCompound) {
        final ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);
        if (nbtCompound.hasKey("icon", 8)) {
            serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
        }
        if (nbtCompound.hasKey("acceptTextures", 1)) {
            if (nbtCompound.getBoolean("acceptTextures")) {
                serverdata.setResourceMode(ServerResourceMode.ENABLED);
            }
            else {
                serverdata.setResourceMode(ServerResourceMode.DISABLED);
            }
        }
        else {
            serverdata.setResourceMode(ServerResourceMode.PROMPT);
        }
        return serverdata;
    }
    
    public String getBase64EncodedIconData() {
        return this.serverIcon;
    }
    
    public void setBase64EncodedIconData(final String icon) {
        this.serverIcon = icon;
    }
    
    public boolean func_181041_d() {
        return this.field_181042_l;
    }
    
    public void copyFrom(final ServerData serverDataIn) {
        this.serverIP = serverDataIn.serverIP;
        this.serverName = serverDataIn.serverName;
        this.setResourceMode(serverDataIn.getResourceMode());
        this.serverIcon = serverDataIn.serverIcon;
        this.field_181042_l = serverDataIn.field_181042_l;
    }
    
    public enum ServerResourceMode
    {
        ENABLED("ENABLED", 0, "enabled"), 
        DISABLED("DISABLED", 1, "disabled"), 
        PROMPT("PROMPT", 2, "prompt");
        
        private final IChatComponent motd;
        
        private ServerResourceMode(final String name, final int ordinal, final String p_i1053_3_) {
            this.motd = new ChatComponentTranslation("addServer.resourcePack." + p_i1053_3_, new Object[0]);
        }
        
        public IChatComponent getMotd() {
            return this.motd;
        }
    }
}
