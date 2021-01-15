/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.login.client.C00PacketLoginStart;
/*     */ import net.minecraft.network.login.client.C01PacketEncryptionResponse;
/*     */ import net.minecraft.network.login.server.S00PacketDisconnect;
/*     */ import net.minecraft.network.login.server.S01PacketEncryptionRequest;
/*     */ import net.minecraft.network.login.server.S02PacketLoginSuccess;
/*     */ import net.minecraft.network.login.server.S03PacketEnableCompression;
/*     */ import net.minecraft.network.play.client.C00PacketKeepAlive;
/*     */ import net.minecraft.network.play.client.C01PacketChatMessage;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
/*     */ import net.minecraft.network.play.client.C09PacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.C0APacketAnimation;
/*     */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*     */ import net.minecraft.network.play.client.C0CPacketInput;
/*     */ import net.minecraft.network.play.client.C0DPacketCloseWindow;
/*     */ import net.minecraft.network.play.client.C0EPacketClickWindow;
/*     */ import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
/*     */ import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
/*     */ import net.minecraft.network.play.client.C11PacketEnchantItem;
/*     */ import net.minecraft.network.play.client.C12PacketUpdateSign;
/*     */ import net.minecraft.network.play.client.C13PacketPlayerAbilities;
/*     */ import net.minecraft.network.play.client.C14PacketTabComplete;
/*     */ import net.minecraft.network.play.client.C15PacketClientSettings;
/*     */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.network.play.client.C18PacketSpectate;
/*     */ import net.minecraft.network.play.client.C19PacketResourcePackStatus;
/*     */ import net.minecraft.network.play.server.S00PacketKeepAlive;
/*     */ import net.minecraft.network.play.server.S01PacketJoinGame;
/*     */ import net.minecraft.network.play.server.S02PacketChat;
/*     */ import net.minecraft.network.play.server.S03PacketTimeUpdate;
/*     */ import net.minecraft.network.play.server.S04PacketEntityEquipment;
/*     */ import net.minecraft.network.play.server.S05PacketSpawnPosition;
/*     */ import net.minecraft.network.play.server.S06PacketUpdateHealth;
/*     */ import net.minecraft.network.play.server.S07PacketRespawn;
/*     */ import net.minecraft.network.play.server.S08PacketPlayerPosLook;
/*     */ import net.minecraft.network.play.server.S09PacketHeldItemChange;
/*     */ import net.minecraft.network.play.server.S0APacketUseBed;
/*     */ import net.minecraft.network.play.server.S0BPacketAnimation;
/*     */ import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
/*     */ import net.minecraft.network.play.server.S0DPacketCollectItem;
/*     */ import net.minecraft.network.play.server.S0FPacketSpawnMob;
/*     */ import net.minecraft.network.play.server.S10PacketSpawnPainting;
/*     */ import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
/*     */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*     */ import net.minecraft.network.play.server.S13PacketDestroyEntities;
/*     */ import net.minecraft.network.play.server.S14PacketEntity;
/*     */ import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
/*     */ import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
/*     */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*     */ import net.minecraft.network.play.server.S19PacketEntityHeadLook;
/*     */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*     */ import net.minecraft.network.play.server.S1CPacketEntityMetadata;
/*     */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*     */ import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
/*     */ import net.minecraft.network.play.server.S1FPacketSetExperience;
/*     */ import net.minecraft.network.play.server.S20PacketEntityProperties;
/*     */ import net.minecraft.network.play.server.S21PacketChunkData;
/*     */ import net.minecraft.network.play.server.S23PacketBlockChange;
/*     */ import net.minecraft.network.play.server.S24PacketBlockAction;
/*     */ import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
/*     */ import net.minecraft.network.play.server.S26PacketMapChunkBulk;
/*     */ import net.minecraft.network.play.server.S27PacketExplosion;
/*     */ import net.minecraft.network.play.server.S28PacketEffect;
/*     */ import net.minecraft.network.play.server.S29PacketSoundEffect;
/*     */ import net.minecraft.network.play.server.S2APacketParticles;
/*     */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*     */ import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
/*     */ import net.minecraft.network.play.server.S2DPacketOpenWindow;
/*     */ import net.minecraft.network.play.server.S2EPacketCloseWindow;
/*     */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*     */ import net.minecraft.network.play.server.S30PacketWindowItems;
/*     */ import net.minecraft.network.play.server.S32PacketConfirmTransaction;
/*     */ import net.minecraft.network.play.server.S33PacketUpdateSign;
/*     */ import net.minecraft.network.play.server.S34PacketMaps;
/*     */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*     */ import net.minecraft.network.play.server.S36PacketSignEditorOpen;
/*     */ import net.minecraft.network.play.server.S37PacketStatistics;
/*     */ import net.minecraft.network.play.server.S39PacketPlayerAbilities;
/*     */ import net.minecraft.network.play.server.S3APacketTabComplete;
/*     */ import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
/*     */ import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
/*     */ import net.minecraft.network.play.server.S3EPacketTeams;
/*     */ import net.minecraft.network.play.server.S3FPacketCustomPayload;
/*     */ import net.minecraft.network.play.server.S40PacketDisconnect;
/*     */ import net.minecraft.network.play.server.S41PacketServerDifficulty;
/*     */ import net.minecraft.network.play.server.S42PacketCombatEvent;
/*     */ import net.minecraft.network.play.server.S44PacketWorldBorder;
/*     */ import net.minecraft.network.play.server.S45PacketTitle;
/*     */ import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
/*     */ import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
/*     */ import net.minecraft.network.play.server.S48PacketResourcePackSend;
/*     */ import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
/*     */ import net.minecraft.network.status.client.C00PacketServerQuery;
/*     */ import net.minecraft.network.status.client.C01PacketPing;
/*     */ import net.minecraft.network.status.server.S00PacketServerInfo;
/*     */ import net.minecraft.network.status.server.S01PacketPong;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public enum EnumConnectionState
/*     */ {
/* 116 */   HANDSHAKING(-1), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */   PLAY(0), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 227 */   STATUS(1), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 236 */   LOGIN(2);
/*     */   
/*     */ 
/*     */ 
/*     */   private static int field_181136_e;
/*     */   
/*     */ 
/*     */   private static int field_181137_f;
/*     */   
/*     */ 
/*     */   private static final EnumConnectionState[] STATES_BY_ID;
/*     */   
/*     */   private static final Map<Class<? extends Packet>, EnumConnectionState> STATES_BY_CLASS;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet>>> directionMaps;
/*     */   
/*     */ 
/*     */   private EnumConnectionState(int protocolId)
/*     */   {
/* 257 */     this.directionMaps = Maps.newEnumMap(EnumPacketDirection.class);
/* 258 */     this.id = protocolId;
/*     */   }
/*     */   
/*     */   protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet> packetClass)
/*     */   {
/* 263 */     BiMap<Integer, Class<? extends Packet>> bimap = (BiMap)this.directionMaps.get(direction);
/*     */     
/* 265 */     if (bimap == null)
/*     */     {
/* 267 */       bimap = HashBiMap.create();
/* 268 */       this.directionMaps.put(direction, bimap);
/*     */     }
/*     */     
/* 271 */     if (bimap.containsValue(packetClass))
/*     */     {
/* 273 */       String s = direction + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
/* 274 */       LogManager.getLogger().fatal(s);
/* 275 */       throw new IllegalArgumentException(s);
/*     */     }
/*     */     
/*     */ 
/* 279 */     bimap.put(Integer.valueOf(bimap.size()), packetClass);
/* 280 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer getPacketId(EnumPacketDirection direction, Packet packetIn)
/*     */   {
/* 286 */     return (Integer)((BiMap)this.directionMaps.get(direction)).inverse().get(packetIn.getClass());
/*     */   }
/*     */   
/*     */   public Packet getPacket(EnumPacketDirection direction, int packetId) throws InstantiationException, IllegalAccessException {
/* 290 */     Class<? extends Packet> oclass = (Class)((BiMap)this.directionMaps.get(direction)).get(Integer.valueOf(packetId));
/* 291 */     return oclass == null ? null : (Packet)oclass.newInstance();
/*     */   }
/*     */   
/*     */   public int getId()
/*     */   {
/* 296 */     return this.id;
/*     */   }
/*     */   
/*     */   public static EnumConnectionState getById(int stateId)
/*     */   {
/* 301 */     return (stateId >= field_181136_e) && (stateId <= field_181137_f) ? STATES_BY_ID[(stateId - field_181136_e)] : null;
/*     */   }
/*     */   
/*     */   public static EnumConnectionState getFromPacket(Packet packetIn)
/*     */   {
/* 306 */     return (EnumConnectionState)STATES_BY_CLASS.get(packetIn.getClass());
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 248 */     field_181136_e = -1;
/* 249 */     field_181137_f = 2;
/* 250 */     STATES_BY_ID = new EnumConnectionState[field_181137_f - field_181136_e + 1];
/* 251 */     STATES_BY_CLASS = Maps.newHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     EnumConnectionState[] arrayOfEnumConnectionState;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */     int j = (arrayOfEnumConnectionState = values()).length; for (int i = 0; i < j; i++) { EnumConnectionState enumconnectionstate = arrayOfEnumConnectionState[i];
/*     */       
/* 312 */       int i = enumconnectionstate.getId();
/*     */       
/* 314 */       if ((i < field_181136_e) || (i > field_181137_f))
/*     */       {
/* 316 */         throw new Error("Invalid protocol ID " + Integer.toString(i));
/*     */       }
/*     */       
/* 319 */       STATES_BY_ID[(i - field_181136_e)] = enumconnectionstate;
/*     */       Iterator localIterator2;
/* 321 */       for (Iterator localIterator1 = enumconnectionstate.directionMaps.keySet().iterator(); localIterator1.hasNext(); 
/*     */           
/* 323 */           localIterator2.hasNext())
/*     */       {
/* 321 */         EnumPacketDirection enumpacketdirection = (EnumPacketDirection)localIterator1.next();
/*     */         
/* 323 */         localIterator2 = ((BiMap)enumconnectionstate.directionMaps.get(enumpacketdirection)).values().iterator(); continue;Class<? extends Packet> oclass = (Class)localIterator2.next();
/*     */         
/* 325 */         if ((STATES_BY_CLASS.containsKey(oclass)) && (STATES_BY_CLASS.get(oclass) != enumconnectionstate))
/*     */         {
/* 327 */           throw new Error("Packet " + oclass + " is already assigned to protocol " + STATES_BY_CLASS.get(oclass) + " - can't reassign to " + enumconnectionstate);
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 332 */           oclass.newInstance();
/*     */         }
/*     */         catch (Throwable var10)
/*     */         {
/* 336 */           throw new Error("Packet " + oclass + " fails instantiation checks! " + oclass);
/*     */         }
/*     */         
/* 339 */         STATES_BY_CLASS.put(oclass, enumconnectionstate);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\EnumConnectionState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */