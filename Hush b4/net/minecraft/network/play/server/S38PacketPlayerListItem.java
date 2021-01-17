// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import com.google.common.collect.ForwardingMultimap;
import net.minecraft.network.INetHandler;
import com.google.common.base.Objects;
import java.io.IOException;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S38PacketPlayerListItem implements Packet<INetHandlerPlayClient>
{
    private Action action;
    private final List<AddPlayerData> players;
    
    public S38PacketPlayerListItem() {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
    }
    
    public S38PacketPlayerListItem(final Action actionIn, final EntityPlayerMP... players) {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
        this.action = actionIn;
        for (final EntityPlayerMP entityplayermp : players) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }
    
    public S38PacketPlayerListItem(final Action actionIn, final Iterable<EntityPlayerMP> players) {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
        this.action = actionIn;
        for (final EntityPlayerMP entityplayermp : players) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        for (int i = buf.readVarIntFromBuffer(), j = 0; j < i; ++j) {
            GameProfile gameprofile = null;
            int k = 0;
            WorldSettings.GameType worldsettings$gametype = null;
            IChatComponent ichatcomponent = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
                    for (int l = buf.readVarIntFromBuffer(), i2 = 0; i2 < l; ++i2) {
                        final String s = buf.readStringFromBuffer(32767);
                        final String s2 = buf.readStringFromBuffer(32767);
                        if (buf.readBoolean()) {
                            gameprofile.getProperties().put(s, new Property(s, s2, buf.readStringFromBuffer(32767)));
                        }
                        else {
                            gameprofile.getProperties().put(s, new Property(s, s2));
                        }
                    }
                    worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    k = buf.readVarIntFromBuffer();
                    if (buf.readBoolean()) {
                        ichatcomponent = buf.readChatComponent();
                        break;
                    }
                    break;
                }
                case UPDATE_GAME_MODE: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    break;
                }
                case UPDATE_LATENCY: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    k = buf.readVarIntFromBuffer();
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    if (buf.readBoolean()) {
                        ichatcomponent = buf.readChatComponent();
                        break;
                    }
                    break;
                }
                case REMOVE_PLAYER: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    break;
                }
            }
            this.players.add(new AddPlayerData(gameprofile, k, worldsettings$gametype, ichatcomponent));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.players.size());
        for (final AddPlayerData s38packetplayerlistitem$addplayerdata : this.players) {
            switch (this.action) {
                case ADD_PLAYER: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeString(s38packetplayerlistitem$addplayerdata.getProfile().getName());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getProfile().getProperties().size());
                    for (final Property property : ((ForwardingMultimap<K, Property>)s38packetplayerlistitem$addplayerdata.getProfile().getProperties()).values()) {
                        buf.writeString(property.getName());
                        buf.writeString(property.getValue());
                        if (property.hasSignature()) {
                            buf.writeBoolean(true);
                            buf.writeString(property.getSignature());
                        }
                        else {
                            buf.writeBoolean(false);
                        }
                    }
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
                    if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        continue;
                    }
                    buf.writeBoolean(true);
                    buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
                    continue;
                }
                case UPDATE_DISPLAY_NAME: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        continue;
                    }
                    buf.writeBoolean(true);
                    buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
                    continue;
                }
                default: {
                    continue;
                }
                case UPDATE_GAME_MODE: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
                    continue;
                }
                case UPDATE_LATENCY: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
                    continue;
                }
                case REMOVE_PLAYER: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    continue;
                }
            }
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handlePlayerListItem(this);
    }
    
    public List<AddPlayerData> func_179767_a() {
        return this.players;
    }
    
    public Action func_179768_b() {
        return this.action;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
    }
    
    public enum Action
    {
        ADD_PLAYER("ADD_PLAYER", 0), 
        UPDATE_GAME_MODE("UPDATE_GAME_MODE", 1), 
        UPDATE_LATENCY("UPDATE_LATENCY", 2), 
        UPDATE_DISPLAY_NAME("UPDATE_DISPLAY_NAME", 3), 
        REMOVE_PLAYER("REMOVE_PLAYER", 4);
        
        private Action(final String name, final int ordinal) {
        }
    }
    
    public class AddPlayerData
    {
        private final int ping;
        private final WorldSettings.GameType gamemode;
        private final GameProfile profile;
        private final IChatComponent displayName;
        
        public AddPlayerData(final GameProfile profile, final int pingIn, final WorldSettings.GameType gamemodeIn, final IChatComponent displayNameIn) {
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
        
        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", (this.displayName == null) ? null : IChatComponent.Serializer.componentToJson(this.displayName)).toString();
        }
    }
}
