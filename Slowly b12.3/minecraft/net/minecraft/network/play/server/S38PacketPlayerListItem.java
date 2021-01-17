package net.minecraft.network.play.server;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class S38PacketPlayerListItem implements Packet<INetHandlerPlayClient> {
   private S38PacketPlayerListItem.Action action;
   private final List players = Lists.newArrayList();
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action;

   public S38PacketPlayerListItem() {
   }

   public S38PacketPlayerListItem(S38PacketPlayerListItem.Action actionIn, EntityPlayerMP... players) {
      this.action = actionIn;
      EntityPlayerMP[] var6 = players;
      int var5 = players.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         EntityPlayerMP entityplayermp = var6[var4];
         this.players.add(new S38PacketPlayerListItem.AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
      }

   }

   public S38PacketPlayerListItem(S38PacketPlayerListItem.Action actionIn, Iterable players) {
      this.action = actionIn;
      Iterator var4 = players.iterator();

      while(var4.hasNext()) {
         EntityPlayerMP entityplayermp = (EntityPlayerMP)var4.next();
         this.players.add(new S38PacketPlayerListItem.AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
      }

   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.action = (S38PacketPlayerListItem.Action)buf.readEnumValue(S38PacketPlayerListItem.Action.class);
      int i = buf.readVarIntFromBuffer();

      for(int j = 0; j < i; ++j) {
         GameProfile gameprofile = null;
         int k = 0;
         WorldSettings.GameType worldsettings$gametype = null;
         IChatComponent ichatcomponent = null;
         switch($SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action()[this.action.ordinal()]) {
         case 1:
            gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
            int l = buf.readVarIntFromBuffer();
            int i1 = 0;

            for(; i1 < l; ++i1) {
               String s = buf.readStringFromBuffer(32767);
               String s1 = buf.readStringFromBuffer(32767);
               if (buf.readBoolean()) {
                  gameprofile.getProperties().put(s, new Property(s, s1, buf.readStringFromBuffer(32767)));
               } else {
                  gameprofile.getProperties().put(s, new Property(s, s1));
               }
            }

            worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
            k = buf.readVarIntFromBuffer();
            if (buf.readBoolean()) {
               ichatcomponent = buf.readChatComponent();
            }
            break;
         case 2:
            gameprofile = new GameProfile(buf.readUuid(), (String)null);
            worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
            break;
         case 3:
            gameprofile = new GameProfile(buf.readUuid(), (String)null);
            k = buf.readVarIntFromBuffer();
            break;
         case 4:
            gameprofile = new GameProfile(buf.readUuid(), (String)null);
            if (buf.readBoolean()) {
               ichatcomponent = buf.readChatComponent();
            }
            break;
         case 5:
            gameprofile = new GameProfile(buf.readUuid(), (String)null);
         }

         this.players.add(new S38PacketPlayerListItem.AddPlayerData(gameprofile, k, worldsettings$gametype, ichatcomponent));
      }

   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeEnumValue(this.action);
      buf.writeVarIntToBuffer(this.players.size());
      Iterator var3 = this.players.iterator();

      while(true) {
         while(var3.hasNext()) {
            S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata = (S38PacketPlayerListItem.AddPlayerData)var3.next();
            switch($SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action()[this.action.ordinal()]) {
            case 1:
               buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
               buf.writeString(s38packetplayerlistitem$addplayerdata.getProfile().getName());
               buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getProfile().getProperties().size());
               Iterator var5 = s38packetplayerlistitem$addplayerdata.getProfile().getProperties().values().iterator();

               while(var5.hasNext()) {
                  Property property = (Property)var5.next();
                  buf.writeString(property.getName());
                  buf.writeString(property.getValue());
                  if (property.hasSignature()) {
                     buf.writeBoolean(true);
                     buf.writeString(property.getSignature());
                  } else {
                     buf.writeBoolean(false);
                  }
               }

               buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
               buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
               if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
                  buf.writeBoolean(false);
               } else {
                  buf.writeBoolean(true);
                  buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
               }
               break;
            case 2:
               buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
               buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
               break;
            case 3:
               buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
               buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
               break;
            case 4:
               buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
               if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
                  buf.writeBoolean(false);
               } else {
                  buf.writeBoolean(true);
                  buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
               }
               break;
            case 5:
               buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
            }
         }

         return;
      }
   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handlePlayerListItem(this);
   }

   public List func_179767_a() {
      return this.players;
   }

   public S38PacketPlayerListItem.Action func_179768_b() {
      return this.action;
   }

   public String toString() {
      return Objects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action;
      if ($SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action != null) {
         return var10000;
      } else {
         int[] var0 = new int[S38PacketPlayerListItem.Action.values().length];

         try {
            var0[S38PacketPlayerListItem.Action.ADD_PLAYER.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[S38PacketPlayerListItem.Action.REMOVE_PLAYER.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[S38PacketPlayerListItem.Action.UPDATE_GAME_MODE.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[S38PacketPlayerListItem.Action.UPDATE_LATENCY.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action = var0;
         return var0;
      }
   }

   public static enum Action {
      ADD_PLAYER,
      UPDATE_GAME_MODE,
      UPDATE_LATENCY,
      UPDATE_DISPLAY_NAME,
      REMOVE_PLAYER;
   }

   public class AddPlayerData {
      private final int ping;
      private final WorldSettings.GameType gamemode;
      private final GameProfile profile;
      private final IChatComponent displayName;

      public AddPlayerData(GameProfile profile, int pingIn, WorldSettings.GameType gamemodeIn, IChatComponent displayNameIn) {
         this.profile = profile;
         this.ping = pingIn;
         this.gamemode = gamemodeIn;
         this.displayName = displayNameIn;
      }

      public GameProfile getProfile() {
         return this.profile;
      }

      public int getPing() {
         return this.ping;
      }

      public WorldSettings.GameType getGameMode() {
         return this.gamemode;
      }

      public IChatComponent getDisplayName() {
         return this.displayName;
      }

      public String toString() {
         return Objects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", this.displayName == null ? null : IChatComponent.Serializer.componentToJson(this.displayName)).toString();
      }
   }
}
