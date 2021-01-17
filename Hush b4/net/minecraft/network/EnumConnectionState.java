// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import com.google.common.collect.Maps;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.handshake.client.C00Handshake;
import com.google.common.collect.BiMap;
import java.util.Map;

public enum EnumConnectionState
{
    HANDSHAKING(0, -1) {
        {
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C00Handshake.class);
        }
    }, 
    PLAY(1, 0) {
        {
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketKeepAlive.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketJoinGame.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketChat.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketTimeUpdate.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S04PacketEntityEquipment.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S05PacketSpawnPosition.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S06PacketUpdateHealth.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S07PacketRespawn.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S08PacketPlayerPosLook.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S09PacketHeldItemChange.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0APacketUseBed.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0BPacketAnimation.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0CPacketSpawnPlayer.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0DPacketCollectItem.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0EPacketSpawnObject.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0FPacketSpawnMob.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S10PacketSpawnPainting.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S11PacketSpawnExperienceOrb.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S12PacketEntityVelocity.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S13PacketDestroyEntities.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S15PacketEntityRelMove.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S16PacketEntityLook.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S17PacketEntityLookMove.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S18PacketEntityTeleport.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityHeadLook.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityStatus.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1BPacketEntityAttach.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1CPacketEntityMetadata.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1DPacketEntityEffect.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1EPacketRemoveEntityEffect.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1FPacketSetExperience.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S20PacketEntityProperties.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S21PacketChunkData.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S22PacketMultiBlockChange.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S23PacketBlockChange.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S24PacketBlockAction.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S25PacketBlockBreakAnim.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S26PacketMapChunkBulk.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S27PacketExplosion.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S28PacketEffect.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S29PacketSoundEffect.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2APacketParticles.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2BPacketChangeGameState.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2CPacketSpawnGlobalEntity.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2DPacketOpenWindow.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2EPacketCloseWindow.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2FPacketSetSlot.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S30PacketWindowItems.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S31PacketWindowProperty.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S32PacketConfirmTransaction.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S33PacketUpdateSign.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S34PacketMaps.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S35PacketUpdateTileEntity.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S36PacketSignEditorOpen.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S37PacketStatistics.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S38PacketPlayerListItem.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S39PacketPlayerAbilities.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3APacketTabComplete.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3BPacketScoreboardObjective.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3CPacketUpdateScore.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3DPacketDisplayScoreboard.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3EPacketTeams.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3FPacketCustomPayload.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S40PacketDisconnect.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S41PacketServerDifficulty.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S42PacketCombatEvent.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S43PacketCamera.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S44PacketWorldBorder.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S45PacketTitle.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S46PacketSetCompressionLevel.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S47PacketPlayerListHeaderFooter.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S48PacketResourcePackSend.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S49PacketUpdateEntityNBT.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketKeepAlive.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketChatMessage.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C02PacketUseEntity.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C04PacketPlayerPosition.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C05PacketPlayerLook.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C06PacketPlayerPosLook.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C07PacketPlayerDigging.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C08PacketPlayerBlockPlacement.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C09PacketHeldItemChange.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C0APacketAnimation.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C0BPacketEntityAction.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C0CPacketInput.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C0DPacketCloseWindow.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C0EPacketClickWindow.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C0FPacketConfirmTransaction.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C10PacketCreativeInventoryAction.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C11PacketEnchantItem.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C12PacketUpdateSign.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C13PacketPlayerAbilities.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C14PacketTabComplete.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C15PacketClientSettings.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C16PacketClientStatus.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C17PacketCustomPayload.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C18PacketSpectate.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C19PacketResourcePackStatus.class);
        }
    }, 
    STATUS(2, 1) {
        {
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketServerQuery.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketServerInfo.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketPing.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketPong.class);
        }
    }, 
    LOGIN(3, 2) {
        {
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketDisconnect.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketEncryptionRequest.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketLoginSuccess.class);
            this.registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketEnableCompression.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketLoginStart.class);
            this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketEncryptionResponse.class);
        }
    };
    
    private static int field_181136_e;
    private static int field_181137_f;
    private static final EnumConnectionState[] STATES_BY_ID;
    private static final Map<Class<? extends Packet>, EnumConnectionState> STATES_BY_CLASS;
    private final int id;
    private final Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet>>> directionMaps;
    
    static {
        EnumConnectionState.field_181136_e = -1;
        EnumConnectionState.field_181137_f = 2;
        STATES_BY_ID = new EnumConnectionState[EnumConnectionState.field_181137_f - EnumConnectionState.field_181136_e + 1];
        STATES_BY_CLASS = Maps.newHashMap();
        EnumConnectionState[] values;
        for (int length = (values = values()).length, j = 0; j < length; ++j) {
            final EnumConnectionState enumconnectionstate = values[j];
            final int i = enumconnectionstate.getId();
            if (i < EnumConnectionState.field_181136_e || i > EnumConnectionState.field_181137_f) {
                throw new Error("Invalid protocol ID " + Integer.toString(i));
            }
            EnumConnectionState.STATES_BY_ID[i - EnumConnectionState.field_181136_e] = enumconnectionstate;
            for (final EnumPacketDirection enumpacketdirection : enumconnectionstate.directionMaps.keySet()) {
                for (final Class<? extends Packet> oclass : enumconnectionstate.directionMaps.get(enumpacketdirection).values()) {
                    if (EnumConnectionState.STATES_BY_CLASS.containsKey(oclass) && EnumConnectionState.STATES_BY_CLASS.get(oclass) != enumconnectionstate) {
                        throw new Error("Packet " + oclass + " is already assigned to protocol " + EnumConnectionState.STATES_BY_CLASS.get(oclass) + " - can't reassign to " + enumconnectionstate);
                    }
                    try {
                        oclass.newInstance();
                    }
                    catch (Throwable var10) {
                        throw new Error("Packet " + oclass + " fails instantiation checks! " + oclass);
                    }
                    EnumConnectionState.STATES_BY_CLASS.put(oclass, enumconnectionstate);
                }
            }
        }
    }
    
    private EnumConnectionState(final String name, final int ordinal, final int protocolId) {
        this.directionMaps = (Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet>>>)Maps.newEnumMap(EnumPacketDirection.class);
        this.id = protocolId;
    }
    
    protected EnumConnectionState registerPacket(final EnumPacketDirection direction, final Class<? extends Packet> packetClass) {
        BiMap<Integer, Class<? extends Packet>> bimap = this.directionMaps.get(direction);
        if (bimap == null) {
            bimap = (BiMap<Integer, Class<? extends Packet>>)HashBiMap.create();
            this.directionMaps.put(direction, bimap);
        }
        if (bimap.containsValue(packetClass)) {
            final String s = direction + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
            LogManager.getLogger().fatal(s);
            throw new IllegalArgumentException(s);
        }
        bimap.put(bimap.size(), packetClass);
        return this;
    }
    
    public Integer getPacketId(final EnumPacketDirection direction, final Packet packetIn) {
        return this.directionMaps.get(direction).inverse().get(packetIn.getClass());
    }
    
    public Packet getPacket(final EnumPacketDirection direction, final int packetId) throws InstantiationException, IllegalAccessException {
        final Class<? extends Packet> oclass = this.directionMaps.get(direction).get(packetId);
        return (oclass == null) ? null : ((Packet)oclass.newInstance());
    }
    
    public int getId() {
        return this.id;
    }
    
    public static EnumConnectionState getById(final int stateId) {
        return (stateId >= EnumConnectionState.field_181136_e && stateId <= EnumConnectionState.field_181137_f) ? EnumConnectionState.STATES_BY_ID[stateId - EnumConnectionState.field_181136_e] : null;
    }
    
    public static EnumConnectionState getFromPacket(final Packet packetIn) {
        return EnumConnectionState.STATES_BY_CLASS.get(packetIn.getClass());
    }
}
