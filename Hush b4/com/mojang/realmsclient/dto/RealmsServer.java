// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.Collections;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import java.util.ArrayList;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import net.minecraft.realms.RealmsServerPing;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RealmsServer extends ValueObject
{
    private static final Logger LOGGER;
    public long id;
    public String remoteSubscriptionId;
    public String name;
    public String motd;
    public State state;
    public String owner;
    public String ownerUUID;
    public List<PlayerInfo> players;
    public Map<Integer, RealmsOptions> slots;
    public String ip;
    public boolean expired;
    public int daysLeft;
    public WorldType worldType;
    public int activeSlot;
    public String minigameName;
    public int minigameId;
    public int protocol;
    public String status;
    public String minigameImage;
    public String resourcePackUrl;
    public String resourcePackHash;
    public RealmsServerPing serverPing;
    
    public RealmsServer() {
        this.status = "";
        this.serverPing = new RealmsServerPing();
    }
    
    public String getDescription() {
        return this.motd;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getMinigameName() {
        return this.minigameName;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setDescription(final String motd) {
        this.motd = motd;
    }
    
    public void latestStatFrom(final RealmsServer oldServer) {
        this.status = oldServer.status;
        this.protocol = oldServer.protocol;
        this.serverPing.nrOfPlayers = oldServer.serverPing.nrOfPlayers;
        this.serverPing.lastPingSnapshot = oldServer.serverPing.lastPingSnapshot;
        this.serverPing.playerList = oldServer.serverPing.playerList;
    }
    
    public static RealmsServer parse(final JsonObject node) {
        final RealmsServer server = new RealmsServer();
        try {
            server.id = JsonUtils.getLongOr("id", node, -1L);
            server.remoteSubscriptionId = JsonUtils.getStringOr("remoteSubscriptionId", node, null);
            server.name = JsonUtils.getStringOr("name", node, null);
            server.motd = JsonUtils.getStringOr("motd", node, null);
            server.state = getState(JsonUtils.getStringOr("state", node, State.CLOSED.name()));
            server.owner = JsonUtils.getStringOr("owner", node, null);
            if (node.get("players") != null && node.get("players").isJsonArray()) {
                server.players = parseInvited(node.get("players").getAsJsonArray());
                sortInvited(server);
            }
            else {
                server.players = new ArrayList<PlayerInfo>();
            }
            server.daysLeft = JsonUtils.getIntOr("daysLeft", node, 0);
            server.ip = JsonUtils.getStringOr("ip", node, null);
            server.expired = JsonUtils.getBooleanOr("expired", node, false);
            server.worldType = getWorldType(JsonUtils.getStringOr("worldType", node, WorldType.NORMAL.name()));
            server.ownerUUID = JsonUtils.getStringOr("ownerUUID", node, "");
            if (node.get("slots") != null && node.get("slots").isJsonArray()) {
                server.slots = parseSlots(node.get("slots").getAsJsonArray());
            }
            else {
                server.slots = getEmptySlots();
            }
            server.minigameName = JsonUtils.getStringOr("minigameName", node, null);
            server.activeSlot = JsonUtils.getIntOr("activeSlot", node, -1);
            server.minigameId = JsonUtils.getIntOr("minigameId", node, -1);
            server.minigameImage = JsonUtils.getStringOr("minigameImage", node, null);
            server.resourcePackUrl = JsonUtils.getStringOr("resourcePackUrl", node, null);
            server.resourcePackHash = JsonUtils.getStringOr("resourcePackHash", node, null);
        }
        catch (Exception e) {
            RealmsServer.LOGGER.error("Could not parse McoServer: " + e.getMessage());
        }
        return server;
    }
    
    private static void sortInvited(final RealmsServer server) {
        Collections.sort(server.players, new Comparator<PlayerInfo>() {
            @Override
            public int compare(final PlayerInfo o1, final PlayerInfo o2) {
                return ComparisonChain.start().compare(o2.getAccepted(), o1.getAccepted()).compare(o1.getName().toLowerCase(), o2.getName().toLowerCase()).result();
            }
        });
    }
    
    private static List<PlayerInfo> parseInvited(final JsonArray jsonArray) {
        final ArrayList<PlayerInfo> invited = new ArrayList<PlayerInfo>();
        for (final JsonElement aJsonArray : jsonArray) {
            try {
                final JsonObject node = aJsonArray.getAsJsonObject();
                final PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setName(JsonUtils.getStringOr("name", node, null));
                playerInfo.setUuid(JsonUtils.getStringOr("uuid", node, null));
                playerInfo.setOperator(JsonUtils.getBooleanOr("operator", node, false));
                playerInfo.setAccepted(JsonUtils.getBooleanOr("accepted", node, false));
                invited.add(playerInfo);
            }
            catch (Exception ex) {}
        }
        return invited;
    }
    
    private static Map<Integer, RealmsOptions> parseSlots(final JsonArray jsonArray) {
        final Map<Integer, RealmsOptions> slots = new HashMap<Integer, RealmsOptions>();
        for (final JsonElement aJsonArray : jsonArray) {
            try {
                final JsonObject node = aJsonArray.getAsJsonObject();
                final JsonParser parser = new JsonParser();
                final JsonElement element = parser.parse(node.get("options").getAsString());
                RealmsOptions options;
                if (element == null) {
                    options = RealmsOptions.getDefaults();
                }
                else {
                    options = RealmsOptions.parse(element.getAsJsonObject());
                }
                final int slot = JsonUtils.getIntOr("slotId", node, -1);
                slots.put(slot, options);
            }
            catch (Exception ex) {}
        }
        for (int i = 1; i <= 3; ++i) {
            if (!slots.containsKey(i)) {
                slots.put(i, RealmsOptions.getEmptyDefaults());
            }
        }
        return slots;
    }
    
    private static Map<Integer, RealmsOptions> getEmptySlots() {
        final HashMap slots = new HashMap();
        slots.put(1, RealmsOptions.getEmptyDefaults());
        slots.put(2, RealmsOptions.getEmptyDefaults());
        slots.put(3, RealmsOptions.getEmptyDefaults());
        return (Map<Integer, RealmsOptions>)slots;
    }
    
    public static RealmsServer parse(final String json) {
        RealmsServer server = new RealmsServer();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(json).getAsJsonObject();
            server = parse(object);
        }
        catch (Exception e) {
            RealmsServer.LOGGER.error("Could not parse McoServer: " + e.getMessage());
        }
        return server;
    }
    
    private static State getState(final String state) {
        try {
            return State.valueOf(state);
        }
        catch (Exception e) {
            return State.CLOSED;
        }
    }
    
    private static WorldType getWorldType(final String state) {
        try {
            return WorldType.valueOf(state);
        }
        catch (Exception e) {
            return WorldType.NORMAL;
        }
    }
    
    public boolean shouldPing(final long now) {
        return now - this.serverPing.lastPingSnapshot >= 6000L;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.motd).append(this.state).append(this.owner).append(this.expired).toHashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final RealmsServer rhs = (RealmsServer)obj;
        return new EqualsBuilder().append(this.id, rhs.id).append(this.name, rhs.name).append(this.motd, rhs.motd).append(this.state, rhs.state).append(this.owner, rhs.owner).append(this.expired, rhs.expired).append(this.worldType, this.worldType).isEquals();
    }
    
    public RealmsServer clone() {
        final RealmsServer server = new RealmsServer();
        server.id = this.id;
        server.remoteSubscriptionId = this.remoteSubscriptionId;
        server.name = this.name;
        server.motd = this.motd;
        server.state = this.state;
        server.owner = this.owner;
        server.players = this.players;
        server.slots = this.cloneSlots(this.slots);
        server.ip = this.ip;
        server.expired = this.expired;
        server.daysLeft = this.daysLeft;
        server.protocol = this.protocol;
        server.status = this.status;
        server.serverPing = new RealmsServerPing();
        server.serverPing.nrOfPlayers = this.serverPing.nrOfPlayers;
        server.serverPing.lastPingSnapshot = this.serverPing.lastPingSnapshot;
        server.serverPing.playerList = this.serverPing.playerList;
        server.worldType = this.worldType;
        server.ownerUUID = this.ownerUUID;
        server.minigameName = this.minigameName;
        server.activeSlot = this.activeSlot;
        server.minigameId = this.minigameId;
        server.minigameImage = this.minigameImage;
        server.resourcePackUrl = this.resourcePackUrl;
        server.resourcePackHash = this.resourcePackHash;
        return server;
    }
    
    public Map<Integer, RealmsOptions> cloneSlots(final Map<Integer, RealmsOptions> slots) {
        final Map<Integer, RealmsOptions> newSlots = new HashMap<Integer, RealmsOptions>();
        for (final Map.Entry<Integer, RealmsOptions> entry : slots.entrySet()) {
            newSlots.put(entry.getKey(), entry.getValue().clone());
        }
        return newSlots;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class McoServerComparator implements Comparator<RealmsServer>
    {
        private final String refOwner;
        
        public McoServerComparator(final String owner) {
            this.refOwner = owner;
        }
        
        @Override
        public int compare(final RealmsServer server1, final RealmsServer server2) {
            return ComparisonChain.start().compareTrueFirst(server1.state.equals(State.UNINITIALIZED), server2.state.equals(State.UNINITIALIZED)).compareFalseFirst(server1.expired, server2.expired).compareTrueFirst(server1.owner.equals(this.refOwner), server2.owner.equals(this.refOwner)).compareTrueFirst(server1.state.equals(State.OPEN), server2.state.equals(State.OPEN)).compare(server1.id, server2.id).result();
        }
    }
    
    public enum State
    {
        CLOSED, 
        OPEN, 
        ADMIN_LOCK, 
        UNINITIALIZED;
    }
    
    public enum WorldType
    {
        NORMAL, 
        MINIGAME, 
        ADVENTUREMAP;
    }
}
