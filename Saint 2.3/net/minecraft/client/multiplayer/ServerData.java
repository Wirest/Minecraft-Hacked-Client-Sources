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
   public String gameVersion = "1.8";
   public boolean field_78841_f;
   public String playerList;
   private ServerData.ServerResourceMode resourceMode;
   private String serverIcon;
   private static final String __OBFID = "CL_00000890";

   public ServerData(String p_i1193_1_, String p_i1193_2_) {
      this.resourceMode = ServerData.ServerResourceMode.PROMPT;
      this.serverName = p_i1193_1_;
      this.serverIP = p_i1193_2_;
   }

   public NBTTagCompound getNBTCompound() {
      NBTTagCompound var1 = new NBTTagCompound();
      var1.setString("name", this.serverName);
      var1.setString("ip", this.serverIP);
      if (this.serverIcon != null) {
         var1.setString("icon", this.serverIcon);
      }

      if (this.resourceMode == ServerData.ServerResourceMode.ENABLED) {
         var1.setBoolean("acceptTextures", true);
      } else if (this.resourceMode == ServerData.ServerResourceMode.DISABLED) {
         var1.setBoolean("acceptTextures", false);
      }

      return var1;
   }

   public ServerData.ServerResourceMode getResourceMode() {
      return this.resourceMode;
   }

   public void setResourceMode(ServerData.ServerResourceMode mode) {
      this.resourceMode = mode;
   }

   public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound) {
      ServerData var1 = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"));
      if (nbtCompound.hasKey("icon", 8)) {
         var1.setBase64EncodedIconData(nbtCompound.getString("icon"));
      }

      if (nbtCompound.hasKey("acceptTextures", 1)) {
         if (nbtCompound.getBoolean("acceptTextures")) {
            var1.setResourceMode(ServerData.ServerResourceMode.ENABLED);
         } else {
            var1.setResourceMode(ServerData.ServerResourceMode.DISABLED);
         }
      } else {
         var1.setResourceMode(ServerData.ServerResourceMode.PROMPT);
      }

      return var1;
   }

   public String getBase64EncodedIconData() {
      return this.serverIcon;
   }

   public void setBase64EncodedIconData(String icon) {
      this.serverIcon = icon;
   }

   public void copyFrom(ServerData serverDataIn) {
      this.serverIP = serverDataIn.serverIP;
      this.serverName = serverDataIn.serverName;
      this.setResourceMode(serverDataIn.getResourceMode());
      this.serverIcon = serverDataIn.serverIcon;
   }

   public static enum ServerResourceMode {
      ENABLED("ENABLED", 0, "enabled"),
      DISABLED("DISABLED", 1, "disabled"),
      PROMPT("PROMPT", 2, "prompt");

      private final IChatComponent motd;
      private static final ServerData.ServerResourceMode[] $VALUES = new ServerData.ServerResourceMode[]{ENABLED, DISABLED, PROMPT};
      private static final String __OBFID = "CL_00001833";

      private ServerResourceMode(String p_i1053_1_, int p_i1053_2_, String p_i1053_3_) {
         this.motd = new ChatComponentTranslation("addServer.resourcePack." + p_i1053_3_, new Object[0]);
      }

      public IChatComponent getMotd() {
         return this.motd;
      }
   }
}
