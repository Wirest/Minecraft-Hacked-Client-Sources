package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ServerData {
   public String serverName;
   public String serverIP;
   public String populationInfo;
   public String serverMOTD;
   public long pingToServer;
   public int version = 47;
   public String gameVersion = "1.8.8";
   public boolean field_78841_f;
   public String playerList;
   private ServerData.ServerResourceMode resourceMode;
   private String serverIcon;
   private boolean field_181042_l;

   public ServerData(String p_i46420_1_, String p_i46420_2_, boolean p_i46420_3_) {
      this.resourceMode = ServerData.ServerResourceMode.PROMPT;
      this.serverName = p_i46420_1_;
      this.serverIP = p_i46420_2_;
      this.field_181042_l = p_i46420_3_;
   }

   public NBTTagCompound getNBTCompound() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setString("name", this.serverName);
      nbttagcompound.setString("ip", this.serverIP);
      if (this.serverIcon != null) {
         nbttagcompound.setString("icon", this.serverIcon);
      }

      if (this.resourceMode == ServerData.ServerResourceMode.ENABLED) {
         nbttagcompound.setBoolean("acceptTextures", true);
      } else if (this.resourceMode == ServerData.ServerResourceMode.DISABLED) {
         nbttagcompound.setBoolean("acceptTextures", false);
      }

      return nbttagcompound;
   }

   public ServerData.ServerResourceMode getResourceMode() {
      return this.resourceMode;
   }

   public void setResourceMode(ServerData.ServerResourceMode mode) {
      this.resourceMode = mode;
   }

   public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound) {
      ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);
      if (nbtCompound.hasKey("icon", 8)) {
         serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
      }

      if (nbtCompound.hasKey("acceptTextures", 1)) {
         if (nbtCompound.getBoolean("acceptTextures")) {
            serverdata.setResourceMode(ServerData.ServerResourceMode.ENABLED);
         } else {
            serverdata.setResourceMode(ServerData.ServerResourceMode.DISABLED);
         }
      } else {
         serverdata.setResourceMode(ServerData.ServerResourceMode.PROMPT);
      }

      return serverdata;
   }

   public String getBase64EncodedIconData() {
      return this.serverIcon;
   }

   public void setBase64EncodedIconData(String icon) {
      this.serverIcon = icon;
   }

   public boolean func_181041_d() {
      return this.field_181042_l;
   }

   public void copyFrom(ServerData serverDataIn) {
      this.serverIP = serverDataIn.serverIP;
      this.serverName = serverDataIn.serverName;
      this.setResourceMode(serverDataIn.getResourceMode());
      this.serverIcon = serverDataIn.serverIcon;
      this.field_181042_l = serverDataIn.field_181042_l;
   }

   public static enum ServerResourceMode {
      ENABLED("enabled"),
      DISABLED("disabled"),
      PROMPT("prompt");

      private final IChatComponent motd;

      private ServerResourceMode(String p_i1053_3_) {
         this.motd = new ChatComponentTranslation("addServer.resourcePack." + p_i1053_3_, new Object[0]);
      }

      public IChatComponent getMotd() {
         return this.motd;
      }
   }
}
