package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import org.apache.logging.log4j.LogManager;

public enum EnumConnectionState {
    HANDSHAKING("HANDSHAKING", 0, -1, null) {
        private static final String __OBFID = "CL_00001246";

        {
            registerPacket(EnumPacketDirection.SERVERBOUND, C00Handshake.class);
        }
    },
    PLAY("PLAY", 1, 0, null) {
        private static final String __OBFID = "CL_00001250";

        {
            registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketKeepAlive.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketJoinGame.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketChat.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketTimeUpdate.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S04PacketEntityEquipment.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S05PacketSpawnPosition.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S06PacketUpdateHealth.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S07PacketRespawn.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S08PacketPlayerPosLook.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S09PacketHeldItemChange.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0APacketUseBed.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0BPacketAnimation.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0CPacketSpawnPlayer.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0DPacketCollectItem.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0EPacketSpawnObject.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0FPacketSpawnMob.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S10PacketSpawnPainting.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S11PacketSpawnExperienceOrb.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S12PacketEntityVelocity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S13PacketDestroyEntities.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S15PacketEntityRelMove.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S16PacketEntityLook.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S17PacketEntityLookMove.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S18PacketEntityTeleport.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityHeadLook.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityStatus.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1BPacketEntityAttach.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1CPacketEntityMetadata.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1DPacketEntityEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1EPacketRemoveEntityEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1FPacketSetExperience.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S20PacketEntityProperties.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S21PacketChunkData.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S22PacketMultiBlockChange.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S23PacketBlockChange.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S24PacketBlockAction.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S25PacketBlockBreakAnim.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S26PacketMapChunkBulk.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S27PacketExplosion.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S28PacketEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S29PacketSoundEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2APacketParticles.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2BPacketChangeGameState.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2CPacketSpawnGlobalEntity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2DPacketOpenWindow.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2EPacketCloseWindow.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2FPacketSetSlot.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S30PacketWindowItems.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S31PacketWindowProperty.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S32PacketConfirmTransaction.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S33PacketUpdateSign.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S34PacketMaps.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S35PacketUpdateTileEntity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S36PacketSignEditorOpen.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S37PacketStatistics.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S38PacketPlayerListItem.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S39PacketPlayerAbilities.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3APacketTabComplete.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3BPacketScoreboardObjective.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3CPacketUpdateScore.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3DPacketDisplayScoreboard.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3EPacketTeams.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3FPacketCustomPayload.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S40PacketDisconnect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S41PacketServerDifficulty.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S42PacketCombatEvent.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S43PacketCamera.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S44PacketWorldBorder.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S45PacketTitle.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S46PacketSetCompressionLevel.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S47PacketPlayerListHeaderFooter.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S48PacketResourcePackSend.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S49PacketUpdateEntityNBT.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketKeepAlive.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketChatMessage.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C02PacketUseEntity.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C04PacketPlayerPosition.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C05PacketPlayerLook.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C06PacketPlayerPosLook.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C07PacketPlayerDigging.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C08PacketPlayerBlockPlacement.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C09PacketHeldItemChange.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0APacketAnimation.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0BPacketEntityAction.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0CPacketInput.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0DPacketCloseWindow.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0EPacketClickWindow.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0FPacketConfirmTransaction.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C10PacketCreativeInventoryAction.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C11PacketEnchantItem.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C12PacketUpdateSign.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C13PacketPlayerAbilities.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C14PacketTabComplete.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C15PacketClientSettings.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C16PacketClientStatus.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C17PacketCustomPayload.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C18PacketSpectate.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C19PacketResourcePackStatus.class);
        }
    },
    STATUS("STATUS", 2, 1, null) {
        private static final String __OBFID = "CL_00001247";

        {
            registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketServerQuery.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketServerInfo.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketPing.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketPong.class);
        }
    },
    LOGIN("LOGIN", 3, 2, null) {
        private static final String __OBFID = "CL_00001249";

        {
            registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketDisconnect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketEncryptionRequest.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketLoginSuccess.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketEnableCompression.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketLoginStart.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketEncryptionResponse.class);
        }
    };
    private static final HashMap<Integer, EnumConnectionState> STATES_BY_ID = new HashMap<Integer, EnumConnectionState>();
    private static final Map STATES_BY_CLASS = Maps.newHashMap();
    private final int id;
    private final Map directionMaps;

    private static final EnumConnectionState[] $VALUES = new EnumConnectionState[]{HANDSHAKING, PLAY, STATUS, LOGIN};
    private static final String __OBFID = "CL_00001245";

    private EnumConnectionState(String p_i45152_1_, int p_i45152_2_, int protocolId) {
        directionMaps = Maps.newEnumMap(EnumPacketDirection.class);
        id = protocolId;
    }

    protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class packetClass) {
        Object var3 = directionMaps.get(direction);

        if (var3 == null) {
            var3 = HashBiMap.create();
            directionMaps.put(direction, var3);
        }

        if (((BiMap) var3).containsValue(packetClass)) {
            String var4 = direction + " packet " + packetClass + " is already known to ID " + ((BiMap) var3).inverse().get(packetClass);
            LogManager.getLogger().fatal(var4);
            throw new IllegalArgumentException(var4);
        } else {
            ((BiMap) var3).put(Integer.valueOf(((BiMap) var3).size()), packetClass);
            return this;
        }
    }

    public Integer getPacketId(EnumPacketDirection direction, Packet packetIn) {
        return (Integer) ((BiMap) directionMaps.get(direction)).inverse().get(packetIn.getClass());
    }

    public Packet getPacket(EnumPacketDirection direction, int packetId) throws InstantiationException, IllegalAccessException {
        Class var3 = (Class) ((BiMap) directionMaps.get(direction)).get(Integer.valueOf(packetId));
        return var3 == null ? null : (Packet) var3.newInstance();
    }

    public int getId() {
        return id;
    }

    public static EnumConnectionState getById(int stateId) {
        return (EnumConnectionState) EnumConnectionState.STATES_BY_ID.get(stateId);
    }

    public static EnumConnectionState getFromPacket(Packet packetIn) {
        return (EnumConnectionState) EnumConnectionState.STATES_BY_CLASS.get(packetIn.getClass());
    }

    EnumConnectionState(String p_i46000_1_, int p_i46000_2_, int p_i46000_3_, Object p_i46000_4_) {
        this(p_i46000_1_, p_i46000_2_, p_i46000_3_);
    }

    static {
        EnumConnectionState[] var0 = EnumConnectionState.values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            EnumConnectionState var3 = var0[var2];
            EnumConnectionState.STATES_BY_ID.put(var3.getId(), var3);
            Iterator var4 = var3.directionMaps.keySet().iterator();

            while (var4.hasNext()) {
                EnumPacketDirection var5 = (EnumPacketDirection) var4.next();
                Class var7;

                for (Iterator var6 = ((BiMap) var3.directionMaps.get(var5)).values().iterator(); var6.hasNext(); EnumConnectionState.STATES_BY_CLASS.put(var7, var3)) {
                    var7 = (Class) var6.next();

                    if (EnumConnectionState.STATES_BY_CLASS.containsKey(var7) && EnumConnectionState.STATES_BY_CLASS.get(var7) != var3) {
                        throw new Error("Packet " + var7 + " is already assigned to protocol " + EnumConnectionState.STATES_BY_CLASS.get(var7) + " - can\'t reassign to " + var3);
                    }

                    try {
                        var7.newInstance();
                    } catch (Throwable var9) {
                        throw new Error("Packet " + var7 + " fails instantiation checks! " + var7);
                    }
                }
            }
        }
    }
}
