// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import java.util.Collection;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.StringUtils;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import java.io.IOException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import java.io.File;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.client.stream.MetadataPlayerDeath;
import net.minecraft.client.stream.MetadataCombat;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import java.util.Iterator;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.stats.AchievementList;
import net.minecraft.client.stream.Metadata;
import net.minecraft.client.stream.MetadataAchievement;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemMap;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.world.IInteractionObject;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.Explosion;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.entity.EntityList;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.EventManager;
import me.nico.hush.events.EventSendPacket;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.entity.DataWatcher;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import me.nico.hush.Client;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S01PacketJoinGame;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.util.Random;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.play.INetHandlerPlayClient;

public class NetHandlerPlayClient implements INetHandlerPlayClient
{
    private static final Logger logger;
    private final NetworkManager netManager;
    private final GameProfile profile;
    private final GuiScreen guiScreenServer;
    private Minecraft gameController;
    private WorldClient clientWorldController;
    private boolean doneLoadingTerrain;
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap;
    public int currentServerMaxPlayers;
    private boolean field_147308_k;
    private final Random avRandomizer;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public NetHandlerPlayClient(final Minecraft mcIn, final GuiScreen p_i46300_2_, final NetworkManager p_i46300_3_, final GameProfile p_i46300_4_) {
        this.playerInfoMap = (Map<UUID, NetworkPlayerInfo>)Maps.newHashMap();
        this.currentServerMaxPlayers = 20;
        this.field_147308_k = false;
        this.avRandomizer = new Random();
        this.gameController = mcIn;
        this.guiScreenServer = p_i46300_2_;
        this.netManager = p_i46300_3_;
        this.profile = p_i46300_4_;
    }
    
    public void cleanup() {
        this.clientWorldController = null;
    }
    
    @Override
    public void handleJoinGame(final S01PacketJoinGame packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
        this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
        this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
        this.gameController.loadWorld(this.clientWorldController);
        Minecraft.thePlayer.dimension = packetIn.getDimension();
        this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        Minecraft.thePlayer.setEntityId(packetIn.getEntityId());
        this.currentServerMaxPlayers = packetIn.getMaxPlayers();
        Minecraft.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
        this.gameController.playerController.setGameType(packetIn.getGameType());
        this.gameController.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
    }
    
    @Override
    public void handleSpawnObject(final S0EPacketSpawnObject packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final double d0 = packetIn.getX() / 32.0;
        final double d2 = packetIn.getY() / 32.0;
        final double d3 = packetIn.getZ() / 32.0;
        Entity entity = null;
        if (packetIn.getType() == 10) {
            entity = EntityMinecart.func_180458_a(this.clientWorldController, d0, d2, d3, EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
        }
        else if (packetIn.getType() == 90) {
            final Entity entity2 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
            if (entity2 instanceof EntityPlayer) {
                entity = new EntityFishHook(this.clientWorldController, d0, d2, d3, (EntityPlayer)entity2);
            }
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 60) {
            entity = new EntityArrow(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 61) {
            entity = new EntitySnowball(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 71) {
            entity = new EntityItemFrame(this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d2), MathHelper.floor_double(d3)), EnumFacing.getHorizontal(packetIn.func_149009_m()));
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 77) {
            entity = new EntityLeashKnot(this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d2), MathHelper.floor_double(d3)));
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 65) {
            entity = new EntityEnderPearl(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 72) {
            entity = new EntityEnderEye(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 76) {
            entity = new EntityFireworkRocket(this.clientWorldController, d0, d2, d3, null);
        }
        else if (packetIn.getType() == 63) {
            entity = new EntityLargeFireball(this.clientWorldController, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 64) {
            entity = new EntitySmallFireball(this.clientWorldController, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 66) {
            entity = new EntityWitherSkull(this.clientWorldController, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 62) {
            entity = new EntityEgg(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 73) {
            entity = new EntityPotion(this.clientWorldController, d0, d2, d3, packetIn.func_149009_m());
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 75) {
            entity = new EntityExpBottle(this.clientWorldController, d0, d2, d3);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.getType() == 1) {
            entity = new EntityBoat(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 50) {
            entity = new EntityTNTPrimed(this.clientWorldController, d0, d2, d3, null);
        }
        else if (packetIn.getType() == 78) {
            entity = new EntityArmorStand(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 51) {
            entity = new EntityEnderCrystal(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 2) {
            entity = new EntityItem(this.clientWorldController, d0, d2, d3);
        }
        else if (packetIn.getType() == 70) {
            entity = new EntityFallingBlock(this.clientWorldController, d0, d2, d3, Block.getStateById(packetIn.func_149009_m() & 0xFFFF));
            packetIn.func_149002_g(0);
        }
        if (entity != null) {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            entity.rotationPitch = packetIn.getPitch() * 360 / 256.0f;
            entity.rotationYaw = packetIn.getYaw() * 360 / 256.0f;
            final Entity[] aentity = entity.getParts();
            if (aentity != null) {
                final int i = packetIn.getEntityID() - entity.getEntityId();
                for (int j = 0; j < aentity.length; ++j) {
                    aentity[j].setEntityId(aentity[j].getEntityId() + i);
                }
            }
            entity.setEntityId(packetIn.getEntityID());
            this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
            if (packetIn.func_149009_m() > 0) {
                if (packetIn.getType() == 60) {
                    final Entity entity3 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
                    if (entity3 instanceof EntityLivingBase && entity instanceof EntityArrow) {
                        ((EntityArrow)entity).shootingEntity = entity3;
                    }
                }
                entity.setVelocity(packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            }
        }
    }
    
    @Override
    public void handleSpawnExperienceOrb(final S11PacketSpawnExperienceOrb packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = new EntityXPOrb(this.clientWorldController, packetIn.getX() / 32.0, packetIn.getY() / 32.0, packetIn.getZ() / 32.0, packetIn.getXPValue());
        entity.serverPosX = packetIn.getX();
        entity.serverPosY = packetIn.getY();
        entity.serverPosZ = packetIn.getZ();
        entity.rotationYaw = 0.0f;
        entity.rotationPitch = 0.0f;
        entity.setEntityId(packetIn.getEntityID());
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
    }
    
    @Override
    public void handleSpawnGlobalEntity(final S2CPacketSpawnGlobalEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final double d0 = packetIn.func_149051_d() / 32.0;
        final double d2 = packetIn.func_149050_e() / 32.0;
        final double d3 = packetIn.func_149049_f() / 32.0;
        Entity entity = null;
        if (packetIn.func_149053_g() == 1) {
            entity = new EntityLightningBolt(this.clientWorldController, d0, d2, d3);
        }
        if (entity != null) {
            entity.serverPosX = packetIn.func_149051_d();
            entity.serverPosY = packetIn.func_149050_e();
            entity.serverPosZ = packetIn.func_149049_f();
            entity.rotationYaw = 0.0f;
            entity.rotationPitch = 0.0f;
            entity.setEntityId(packetIn.func_149052_c());
            this.clientWorldController.addWeatherEffect(entity);
        }
    }
    
    @Override
    public void handleSpawnPainting(final S10PacketSpawnPainting packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPainting entitypainting = new EntityPainting(this.clientWorldController, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitypainting);
    }
    
    @Override
    public void handleEntityVelocity(final S12PacketEntityVelocity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            if (Client.instance.moduleManager.getModuleName("AntiKnockBack").isEnabled()) {
                return;
            }
            entity.setVelocity(packetIn.getMotionX() / 8000.0, packetIn.getMotionY() / 8000.0, packetIn.getMotionZ() / 8000.0);
        }
    }
    
    @Override
    public void handleEntityMetadata(final S1CPacketEntityMetadata packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null && packetIn.func_149376_c() != null) {
            entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
        }
    }
    
    @Override
    public void handleSpawnPlayer(final S0CPacketSpawnPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final double d0 = packetIn.getX() / 32.0;
        final double d2 = packetIn.getY() / 32.0;
        final double d3 = packetIn.getZ() / 32.0;
        final float f = packetIn.getYaw() * 360 / 256.0f;
        final float f2 = packetIn.getPitch() * 360 / 256.0f;
        final EntityOtherPlayerMP entityOtherPlayerMP3;
        final EntityOtherPlayerMP entityOtherPlayerMP2;
        final EntityOtherPlayerMP entityOtherPlayerMP;
        final EntityOtherPlayerMP entityotherplayermp = entityOtherPlayerMP = (entityOtherPlayerMP2 = (entityOtherPlayerMP3 = new EntityOtherPlayerMP(this.gameController.theWorld, this.getPlayerInfo(packetIn.getPlayer()).getGameProfile())));
        final int x = packetIn.getX();
        entityOtherPlayerMP.serverPosX = x;
        final double n = x;
        entityOtherPlayerMP2.lastTickPosX = n;
        entityOtherPlayerMP3.prevPosX = n;
        final EntityOtherPlayerMP entityOtherPlayerMP4 = entityotherplayermp;
        final EntityOtherPlayerMP entityOtherPlayerMP5 = entityotherplayermp;
        final EntityOtherPlayerMP entityOtherPlayerMP6 = entityotherplayermp;
        final int y = packetIn.getY();
        entityOtherPlayerMP6.serverPosY = y;
        final double n2 = y;
        entityOtherPlayerMP5.lastTickPosY = n2;
        entityOtherPlayerMP4.prevPosY = n2;
        final EntityOtherPlayerMP entityOtherPlayerMP7 = entityotherplayermp;
        final EntityOtherPlayerMP entityOtherPlayerMP8 = entityotherplayermp;
        final EntityOtherPlayerMP entityOtherPlayerMP9 = entityotherplayermp;
        final int z = packetIn.getZ();
        entityOtherPlayerMP9.serverPosZ = z;
        final double n3 = z;
        entityOtherPlayerMP8.lastTickPosZ = n3;
        entityOtherPlayerMP7.prevPosZ = n3;
        final int i = packetIn.getCurrentItemID();
        if (i == 0) {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
        }
        else {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(Item.getItemById(i), 1, 0);
        }
        entityotherplayermp.setPositionAndRotation(d0, d2, d3, f, f2);
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
        final List<DataWatcher.WatchableObject> list = packetIn.func_148944_c();
        if (list != null) {
            entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }
    
    @Override
    public void handleEntityTeleport(final S18PacketEntityTeleport packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            final double d0 = entity.serverPosX / 32.0;
            final double d2 = entity.serverPosY / 32.0;
            final double d3 = entity.serverPosZ / 32.0;
            final float f = packetIn.getYaw() * 360 / 256.0f;
            final float f2 = packetIn.getPitch() * 360 / 256.0f;
            if (Math.abs(entity.posX - d0) < 0.03125 && Math.abs(entity.posY - d2) < 0.015625 && Math.abs(entity.posZ - d3) < 0.03125) {
                entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f2, 3, true);
            }
            else {
                entity.setPositionAndRotation2(d0, d2, d3, f, f2, 3, true);
            }
            entity.onGround = packetIn.getOnGround();
        }
    }
    
    @Override
    public void handleHeldItemChange(final S09PacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if (packetIn.getHeldItemHotbarIndex() >= 0 && packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize()) {
            Minecraft.thePlayer.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
        }
    }
    
    @Override
    public void handleEntityMovement(final S14PacketEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            final Entity entity2 = entity;
            entity2.serverPosX += packetIn.func_149062_c();
            final Entity entity3 = entity;
            entity3.serverPosY += packetIn.func_149061_d();
            final Entity entity4 = entity;
            entity4.serverPosZ += packetIn.func_149064_e();
            final double d0 = entity.serverPosX / 32.0;
            final double d2 = entity.serverPosY / 32.0;
            final double d3 = entity.serverPosZ / 32.0;
            final float f = packetIn.func_149060_h() ? (packetIn.func_149066_f() * 360 / 256.0f) : entity.rotationYaw;
            final float f2 = packetIn.func_149060_h() ? (packetIn.func_149063_g() * 360 / 256.0f) : entity.rotationPitch;
            entity.setPositionAndRotation2(d0, d2, d3, f, f2, 3, false);
            entity.onGround = packetIn.getOnGround();
        }
    }
    
    @Override
    public void handleEntityHeadLook(final S19PacketEntityHeadLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            final float f = packetIn.getYaw() * 360 / 256.0f;
            entity.setRotationYawHead(f);
        }
    }
    
    @Override
    public void handleDestroyEntities(final S13PacketDestroyEntities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        for (int i = 0; i < packetIn.getEntityIDs().length; ++i) {
            this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
        }
    }
    
    @Override
    public void handlePlayerPosLook(final S08PacketPlayerPosLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPlayer entityplayer = Minecraft.thePlayer;
        double d0 = packetIn.getX();
        double d2 = packetIn.getY();
        double d3 = packetIn.getZ();
        float f = packetIn.getYaw();
        float f2 = packetIn.getPitch();
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
            d0 += entityplayer.posX;
        }
        else {
            entityplayer.motionX = 0.0;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
            d2 += entityplayer.posY;
        }
        else {
            entityplayer.motionY = 0.0;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
            d3 += entityplayer.posZ;
        }
        else {
            entityplayer.motionZ = 0.0;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            f2 += entityplayer.rotationPitch;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f += entityplayer.rotationYaw;
        }
        entityplayer.setPositionAndRotation(d0, d2, d3, f, f2);
        this.netManager.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
        if (!this.doneLoadingTerrain) {
            Minecraft.thePlayer.prevPosX = Minecraft.thePlayer.posX;
            Minecraft.thePlayer.prevPosY = Minecraft.thePlayer.posY;
            Minecraft.thePlayer.prevPosZ = Minecraft.thePlayer.posZ;
            this.doneLoadingTerrain = true;
            this.gameController.displayGuiScreen(null);
        }
    }
    
    @Override
    public void handleMultiBlockChange(final S22PacketMultiBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks;
        for (int length = (changedBlocks = packetIn.getChangedBlocks()).length, i = 0; i < length; ++i) {
            final S22PacketMultiBlockChange.BlockUpdateData s22packetmultiblockchange$blockupdatedata = changedBlocks[i];
            this.clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(), s22packetmultiblockchange$blockupdatedata.getBlockState());
        }
    }
    
    @Override
    public void handleChunkData(final S21PacketChunkData packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if (packetIn.func_149274_i()) {
            if (packetIn.getExtractedSize() == 0) {
                this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), false);
                return;
            }
            this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
        }
        this.clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        final Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
        chunk.fillChunk(packetIn.func_149272_d(), packetIn.getExtractedSize(), packetIn.func_149274_i());
        this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        if (!packetIn.func_149274_i() || !(this.clientWorldController.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
    }
    
    @Override
    public void handleBlockChange(final S23PacketBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
    }
    
    @Override
    public void handleDisconnect(final S40PacketDisconnect packetIn) {
        this.netManager.closeChannel(packetIn.getReason());
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
        this.gameController.loadWorld(null);
        if (this.guiScreenServer != null) {
            if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
                this.gameController.displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).func_154321_a(), "disconnect.lost", reason).getProxy());
            }
            else {
                this.gameController.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
            }
        }
        else {
            this.gameController.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", reason));
        }
    }
    
    public void addToSendQueue(final Packet p_147297_1_) {
        final EventSendPacket event = new EventSendPacket(p_147297_1_);
        EventManager.call(event);
        if (event.isCancelled()) {
            return;
        }
        this.netManager.sendPacket(event.getPacket());
    }
    
    @Override
    public void handleCollectItem(final S0DPacketCollectItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entitylivingbase == null) {
            entitylivingbase = Minecraft.thePlayer;
        }
        if (entity != null) {
            if (entity instanceof EntityXPOrb) {
                this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            }
            else {
                this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            }
            this.gameController.effectRenderer.addEffect(new EntityPickupFX(this.clientWorldController, entity, entitylivingbase, 0.5f));
            this.clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
        }
    }
    
    @Override
    public void handleChat(final S02PacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if (packetIn.getType() == 2) {
            this.gameController.ingameGUI.setRecordPlaying(packetIn.getChatComponent(), false);
        }
        else {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
        }
    }
    
    @Override
    public void handleAnimation(final S0BPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            if (packetIn.getAnimationType() == 0) {
                final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                entitylivingbase.swingItem();
            }
            else if (packetIn.getAnimationType() == 1) {
                entity.performHurtAnimation();
            }
            else if (packetIn.getAnimationType() == 2) {
                final EntityPlayer entityplayer = (EntityPlayer)entity;
                entityplayer.wakeUpPlayer(false, false, false);
            }
            else if (packetIn.getAnimationType() == 4) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            }
            else if (packetIn.getAnimationType() == 5) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }
    
    @Override
    public void handleUseBed(final S0APacketUseBed packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        packetIn.getPlayer(this.clientWorldController).trySleep(packetIn.getBedPosition());
    }
    
    @Override
    public void handleSpawnMob(final S0FPacketSpawnMob packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final double d0 = packetIn.getX() / 32.0;
        final double d2 = packetIn.getY() / 32.0;
        final double d3 = packetIn.getZ() / 32.0;
        final float f = packetIn.getYaw() * 360 / 256.0f;
        final float f2 = packetIn.getPitch() * 360 / 256.0f;
        final EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), this.gameController.theWorld);
        entitylivingbase.serverPosX = packetIn.getX();
        entitylivingbase.serverPosY = packetIn.getY();
        entitylivingbase.serverPosZ = packetIn.getZ();
        final EntityLivingBase entityLivingBase = entitylivingbase;
        final EntityLivingBase entityLivingBase2 = entitylivingbase;
        final float n = packetIn.getHeadPitch() * 360 / 256.0f;
        entityLivingBase2.rotationYawHead = n;
        entityLivingBase.renderYawOffset = n;
        final Entity[] aentity = entitylivingbase.getParts();
        if (aentity != null) {
            final int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
            for (int j = 0; j < aentity.length; ++j) {
                aentity[j].setEntityId(aentity[j].getEntityId() + i);
            }
        }
        entitylivingbase.setEntityId(packetIn.getEntityID());
        entitylivingbase.setPositionAndRotation(d0, d2, d3, f, f2);
        entitylivingbase.motionX = packetIn.getVelocityX() / 8000.0f;
        entitylivingbase.motionY = packetIn.getVelocityY() / 8000.0f;
        entitylivingbase.motionZ = packetIn.getVelocityZ() / 8000.0f;
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
        final List<DataWatcher.WatchableObject> list = packetIn.func_149027_c();
        if (list != null) {
            entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }
    
    @Override
    public void handleTimeUpdate(final S03PacketTimeUpdate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
        this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
    }
    
    @Override
    public void handleSpawnPosition(final S05PacketSpawnPosition packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        Minecraft.thePlayer.setSpawnPoint(packetIn.getSpawnPos(), true);
        this.gameController.theWorld.getWorldInfo().setSpawn(packetIn.getSpawnPos());
    }
    
    @Override
    public void handleEntityAttach(final S1BPacketEntityAttach packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        final Entity entity2 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
        if (packetIn.getLeash() == 0) {
            boolean flag = false;
            if (packetIn.getEntityId() == Minecraft.thePlayer.getEntityId()) {
                entity = Minecraft.thePlayer;
                if (entity2 instanceof EntityBoat) {
                    ((EntityBoat)entity2).setIsBoatEmpty(false);
                }
                flag = (entity.ridingEntity == null && entity2 != null);
            }
            else if (entity2 instanceof EntityBoat) {
                ((EntityBoat)entity2).setIsBoatEmpty(true);
            }
            if (entity == null) {
                return;
            }
            entity.mountEntity(entity2);
            if (flag) {
                final GameSettings gamesettings = this.gameController.gameSettings;
                this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode())), false);
            }
        }
        else if (packetIn.getLeash() == 1 && entity instanceof EntityLiving) {
            if (entity2 != null) {
                ((EntityLiving)entity).setLeashedToEntity(entity2, false);
            }
            else {
                ((EntityLiving)entity).clearLeashed(false, false);
            }
        }
    }
    
    @Override
    public void handleEntityStatus(final S19PacketEntityStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            if (packetIn.getOpCode() == 21) {
                this.gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
            }
            else {
                entity.handleStatusUpdate(packetIn.getOpCode());
            }
        }
    }
    
    @Override
    public void handleUpdateHealth(final S06PacketUpdateHealth packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        Minecraft.thePlayer.setPlayerSPHealth(packetIn.getHealth());
        Minecraft.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        Minecraft.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }
    
    @Override
    public void handleSetExperience(final S1FPacketSetExperience packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        Minecraft.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(), packetIn.getLevel());
    }
    
    @Override
    public void handleRespawn(final S07PacketRespawn packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if (packetIn.getDimensionID() != Minecraft.thePlayer.dimension) {
            this.doneLoadingTerrain = false;
            final Scoreboard scoreboard = this.clientWorldController.getScoreboard();
            (this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, this.gameController.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.gameController.mcProfiler)).setWorldScoreboard(scoreboard);
            this.gameController.loadWorld(this.clientWorldController);
            Minecraft.thePlayer.dimension = packetIn.getDimensionID();
            this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        }
        this.gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
        this.gameController.playerController.setGameType(packetIn.getGameType());
    }
    
    @Override
    public void handleExplosion(final S27PacketExplosion packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Explosion explosion = new Explosion(this.gameController.theWorld, null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        thePlayer.motionX += packetIn.func_149149_c();
        final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
        thePlayer2.motionY += packetIn.func_149144_d();
        final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
        thePlayer3.motionZ += packetIn.func_149147_e();
    }
    
    @Override
    public void handleOpenWindow(final S2DPacketOpenWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPlayerSP entityplayersp = Minecraft.thePlayer;
        if ("minecraft:container".equals(packetIn.getGuiId())) {
            entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("minecraft:villager".equals(packetIn.getGuiId())) {
            entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("EntityHorse".equals(packetIn.getGuiId())) {
            final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
            if (entity instanceof EntityHorse) {
                entityplayersp.displayGUIHorse((EntityHorse)entity, new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
                entityplayersp.openContainer.windowId = packetIn.getWindowId();
            }
        }
        else if (!packetIn.hasSlots()) {
            entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else {
            final ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
            entityplayersp.displayGUIChest(containerlocalmenu);
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
    }
    
    @Override
    public void handleSetSlot(final S2FPacketSetSlot packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPlayer entityplayer = Minecraft.thePlayer;
        if (packetIn.func_149175_c() == -1) {
            entityplayer.inventory.setItemStack(packetIn.func_149174_e());
        }
        else {
            boolean flag = false;
            if (this.gameController.currentScreen instanceof GuiContainerCreative) {
                final GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
                flag = (guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex());
            }
            if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45) {
                final ItemStack itemstack = entityplayer.inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
                if (packetIn.func_149174_e() != null && (itemstack == null || itemstack.stackSize < packetIn.func_149174_e().stackSize)) {
                    packetIn.func_149174_e().animationsToGo = 5;
                }
                entityplayer.inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            }
            else if (packetIn.func_149175_c() == entityplayer.openContainer.windowId && (packetIn.func_149175_c() != 0 || !flag)) {
                entityplayer.openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            }
        }
    }
    
    @Override
    public void handleConfirmTransaction(final S32PacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        Container container = null;
        final EntityPlayer entityplayer = Minecraft.thePlayer;
        if (packetIn.getWindowId() == 0) {
            container = entityplayer.inventoryContainer;
        }
        else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            container = entityplayer.openContainer;
        }
        if (container != null && !packetIn.func_148888_e()) {
            this.addToSendQueue(new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
        }
    }
    
    @Override
    public void handleWindowItems(final S30PacketWindowItems packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPlayer entityplayer = Minecraft.thePlayer;
        if (packetIn.func_148911_c() == 0) {
            entityplayer.inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
        }
        else if (packetIn.func_148911_c() == entityplayer.openContainer.windowId) {
            entityplayer.openContainer.putStacksInSlots(packetIn.getItemStacks());
        }
    }
    
    @Override
    public void handleSignEditorOpen(final S36PacketSignEditorOpen packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
        if (!(tileentity instanceof TileEntitySign)) {
            tileentity = new TileEntitySign();
            tileentity.setWorldObj(this.clientWorldController);
            tileentity.setPos(packetIn.getSignPosition());
        }
        Minecraft.thePlayer.openEditSign((TileEntitySign)tileentity);
    }
    
    @Override
    public void handleUpdateSign(final S33PacketUpdateSign packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        boolean flag = false;
        if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
            final TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
            if (tileentity instanceof TileEntitySign) {
                final TileEntitySign tileentitysign = (TileEntitySign)tileentity;
                if (tileentitysign.getIsEditable()) {
                    System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
                    tileentitysign.markDirty();
                }
                flag = true;
            }
        }
        if (!flag && Minecraft.thePlayer != null) {
            Minecraft.thePlayer.addChatMessage(new ChatComponentText("Unable to locate sign at " + packetIn.getPos().getX() + ", " + packetIn.getPos().getY() + ", " + packetIn.getPos().getZ()));
        }
    }
    
    @Override
    public void handleUpdateTileEntity(final S35PacketUpdateTileEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
            final TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
            final int i = packetIn.getTileEntityType();
            if ((i == 1 && tileentity instanceof TileEntityMobSpawner) || (i == 2 && tileentity instanceof TileEntityCommandBlock) || (i == 3 && tileentity instanceof TileEntityBeacon) || (i == 4 && tileentity instanceof TileEntitySkull) || (i == 5 && tileentity instanceof TileEntityFlowerPot) || (i == 6 && tileentity instanceof TileEntityBanner)) {
                tileentity.readFromNBT(packetIn.getNbtCompound());
            }
        }
    }
    
    @Override
    public void handleWindowProperty(final S31PacketWindowProperty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPlayer entityplayer = Minecraft.thePlayer;
        if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == packetIn.getWindowId()) {
            entityplayer.openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
        }
    }
    
    @Override
    public void handleEntityEquipment(final S04PacketEntityEquipment packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
        }
    }
    
    @Override
    public void handleCloseWindow(final S2EPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        Minecraft.thePlayer.closeScreenAndDropStack();
    }
    
    @Override
    public void handleBlockAction(final S24PacketBlockAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        this.gameController.theWorld.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }
    
    @Override
    public void handleBlockBreakAnim(final S25PacketBlockBreakAnim packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        this.gameController.theWorld.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
    }
    
    @Override
    public void handleMapChunkBulk(final S26PacketMapChunkBulk packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        for (int i = 0; i < packetIn.getChunkCount(); ++i) {
            final int j = packetIn.getChunkX(i);
            final int k = packetIn.getChunkZ(i);
            this.clientWorldController.doPreChunk(j, k, true);
            this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
            final Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
            chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
            this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
            if (!(this.clientWorldController.provider instanceof WorldProviderSurface)) {
                chunk.resetRelightChecks();
            }
        }
    }
    
    @Override
    public void handleChangeGameState(final S2BPacketChangeGameState packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPlayer entityplayer = Minecraft.thePlayer;
        final int i = packetIn.getGameState();
        final float f = packetIn.func_149137_d();
        final int j = MathHelper.floor_float(f + 0.5f);
        if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[i] != null) {
            entityplayer.addChatComponentMessage(new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]));
        }
        if (i == 1) {
            this.clientWorldController.getWorldInfo().setRaining(true);
            this.clientWorldController.setRainStrength(0.0f);
        }
        else if (i == 2) {
            this.clientWorldController.getWorldInfo().setRaining(false);
            this.clientWorldController.setRainStrength(1.0f);
        }
        else if (i == 3) {
            this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
        }
        else if (i == 4) {
            this.gameController.displayGuiScreen(new GuiWinGame());
        }
        else if (i == 5) {
            final GameSettings gamesettings = this.gameController.gameSettings;
            if (f == 0.0f) {
                this.gameController.displayGuiScreen(new GuiScreenDemo());
            }
            else if (f == 101.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.movement", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }));
            }
            else if (f == 102.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }));
            }
            else if (f == 103.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }));
            }
        }
        else if (i == 6) {
            this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ, "random.successful_hit", 0.18f, 0.45f, false);
        }
        else if (i == 7) {
            this.clientWorldController.setRainStrength(f);
        }
        else if (i == 8) {
            this.clientWorldController.setThunderStrength(f);
        }
        else if (i == 10) {
            this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "mob.guardian.curse", 1.0f, 1.0f, false);
        }
    }
    
    @Override
    public void handleMaps(final S34PacketMaps packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), this.gameController.theWorld);
        packetIn.setMapdataTo(mapdata);
        this.gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapdata);
    }
    
    @Override
    public void handleEffect(final S28PacketEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if (packetIn.isSoundServerwide()) {
            this.gameController.theWorld.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
        else {
            this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
    }
    
    @Override
    public void handleStatistics(final S37PacketStatistics packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        boolean flag = false;
        for (final Map.Entry<StatBase, Integer> entry : packetIn.func_148974_c().entrySet()) {
            final StatBase statbase = entry.getKey();
            final int i = entry.getValue();
            if (statbase.isAchievement() && i > 0) {
                if (this.field_147308_k && Minecraft.thePlayer.getStatFileWriter().readStat(statbase) == 0) {
                    final Achievement achievement = (Achievement)statbase;
                    this.gameController.guiAchievement.displayAchievement(achievement);
                    this.gameController.getTwitchStream().func_152911_a(new MetadataAchievement(achievement), 0L);
                    if (statbase == AchievementList.openInventory) {
                        this.gameController.gameSettings.showInventoryAchievementHint = false;
                        this.gameController.gameSettings.saveOptions();
                    }
                }
                flag = true;
            }
            Minecraft.thePlayer.getStatFileWriter().unlockAchievement(Minecraft.thePlayer, statbase, i);
        }
        if (!this.field_147308_k && !flag && this.gameController.gameSettings.showInventoryAchievementHint) {
            this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
        }
        this.field_147308_k = true;
        if (this.gameController.currentScreen instanceof IProgressMeter) {
            ((IProgressMeter)this.gameController.currentScreen).doneLoading();
        }
    }
    
    @Override
    public void handleEntityEffect(final S1DPacketEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity instanceof EntityLivingBase) {
            final PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(), packetIn.getAmplifier(), false, packetIn.func_179707_f());
            potioneffect.setPotionDurationMax(packetIn.func_149429_c());
            ((EntityLivingBase)entity).addPotionEffect(potioneffect);
        }
    }
    
    @Override
    public void handleCombatEvent(final S42PacketCombatEvent packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.field_179775_c);
        final EntityLivingBase entitylivingbase = (entity instanceof EntityLivingBase) ? ((EntityLivingBase)entity) : null;
        if (packetIn.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
            final long i = 1000 * packetIn.field_179772_d / 20;
            final MetadataCombat metadatacombat = new MetadataCombat(Minecraft.thePlayer, entitylivingbase);
            this.gameController.getTwitchStream().func_176026_a(metadatacombat, 0L - i, 0L);
        }
        else if (packetIn.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
            final Entity entity2 = this.clientWorldController.getEntityByID(packetIn.field_179774_b);
            if (entity2 instanceof EntityPlayer) {
                final MetadataPlayerDeath metadataplayerdeath = new MetadataPlayerDeath((EntityLivingBase)entity2, entitylivingbase);
                metadataplayerdeath.func_152807_a(packetIn.deathMessage);
                this.gameController.getTwitchStream().func_152911_a(metadataplayerdeath, 0L);
            }
        }
    }
    
    @Override
    public void handleServerDifficulty(final S41PacketServerDifficulty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        this.gameController.theWorld.getWorldInfo().setDifficulty(packetIn.getDifficulty());
        this.gameController.theWorld.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
    }
    
    @Override
    public void handleCamera(final S43PacketCamera packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            this.gameController.setRenderViewEntity(entity);
        }
    }
    
    @Override
    public void handleWorldBorder(final S44PacketWorldBorder packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        packetIn.func_179788_a(this.clientWorldController.getWorldBorder());
    }
    
    @Override
    public void handleTitle(final S45PacketTitle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final S45PacketTitle.Type s45packettitle$type = packetIn.getType();
        String s = null;
        String s2 = null;
        final String s3 = (packetIn.getMessage() != null) ? packetIn.getMessage().getFormattedText() : "";
        switch (s45packettitle$type) {
            case TITLE: {
                s = s3;
                break;
            }
            case SUBTITLE: {
                s2 = s3;
                break;
            }
            case RESET: {
                this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
                this.gameController.ingameGUI.func_175177_a();
                return;
            }
        }
        this.gameController.ingameGUI.displayTitle(s, s2, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
    }
    
    @Override
    public void handleSetCompressionLevel(final S46PacketSetCompressionLevel packetIn) {
        if (!this.netManager.isLocalChannel()) {
            this.netManager.setCompressionTreshold(packetIn.func_179760_a());
        }
    }
    
    @Override
    public void handlePlayerListHeaderFooter(final S47PacketPlayerListHeaderFooter packetIn) {
        this.gameController.ingameGUI.getTabList().setHeader((packetIn.getHeader().getFormattedText().length() == 0) ? null : packetIn.getHeader());
        this.gameController.ingameGUI.getTabList().setFooter((packetIn.getFooter().getFormattedText().length() == 0) ? null : packetIn.getFooter());
    }
    
    @Override
    public void handleRemoveEntityEffect(final S1EPacketRemoveEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).removePotionEffectClient(packetIn.getEffectId());
        }
    }
    
    @Override
    public void handlePlayerListItem(final S38PacketPlayerListItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        for (final S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : packetIn.func_179767_a()) {
            if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                this.playerInfoMap.remove(s38packetplayerlistitem$addplayerdata.getProfile().getId());
            }
            else {
                NetworkPlayerInfo networkplayerinfo = this.playerInfoMap.get(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                    networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
                    this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
                }
                if (networkplayerinfo == null) {
                    continue;
                }
                switch (packetIn.func_179768_b()) {
                    default: {
                        continue;
                    }
                    case ADD_PLAYER: {
                        networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                        networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                        continue;
                    }
                    case UPDATE_GAME_MODE: {
                        networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                        continue;
                    }
                    case UPDATE_LATENCY: {
                        networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                        continue;
                    }
                    case UPDATE_DISPLAY_NAME: {
                        networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public void handleKeepAlive(final S00PacketKeepAlive packetIn) {
        this.addToSendQueue(new C00PacketKeepAlive(packetIn.func_149134_c()));
    }
    
    @Override
    public void handlePlayerAbilities(final S39PacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final EntityPlayer entityplayer = Minecraft.thePlayer;
        entityplayer.capabilities.isFlying = packetIn.isFlying();
        entityplayer.capabilities.isCreativeMode = packetIn.isCreativeMode();
        entityplayer.capabilities.disableDamage = packetIn.isInvulnerable();
        entityplayer.capabilities.allowFlying = packetIn.isAllowFlying();
        entityplayer.capabilities.setFlySpeed(packetIn.getFlySpeed());
        entityplayer.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }
    
    @Override
    public void handleTabComplete(final S3APacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final String[] astring = packetIn.func_149630_c();
        if (this.gameController.currentScreen instanceof GuiChat) {
            final GuiChat guichat = (GuiChat)this.gameController.currentScreen;
            guichat.onAutocompleteResponse(astring);
        }
    }
    
    @Override
    public void handleSoundEffect(final S29PacketSoundEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        this.gameController.theWorld.playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSoundName(), packetIn.getVolume(), packetIn.getPitch(), false);
    }
    
    @Override
    public void handleResourcePack(final S48PacketResourcePackSend packetIn) {
        final String s = packetIn.getURL();
        final String s2 = packetIn.getHash();
        if (s.startsWith("level://")) {
            final String s3 = s.substring("level://".length());
            final File file1 = new File(this.gameController.mcDataDir, "saves");
            final File file2 = new File(file1, s3);
            if (file2.isFile()) {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback(this.gameController.getResourcePackRepository().setResourcePackInstance(file2), new FutureCallback<Object>() {
                    @Override
                    public void onSuccess(final Object p_onSuccess_1_) {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                    }
                    
                    @Override
                    public void onFailure(final Throwable p_onFailure_1_) {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                    }
                });
            }
            else {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        }
        else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
            this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.ACCEPTED));
            Futures.addCallback(this.gameController.getResourcePackRepository().downloadResourcePack(s, s2), new FutureCallback<Object>() {
                @Override
                public void onSuccess(final Object p_onSuccess_1_) {
                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                }
                
                @Override
                public void onFailure(final Throwable p_onFailure_1_) {
                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                }
            });
        }
        else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
            this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.DECLINED));
        }
        else {
            this.gameController.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                        @Override
                        public void confirmClicked(final boolean result, final int id) {
                            NetHandlerPlayClient.access$4(NetHandlerPlayClient.this, Minecraft.getMinecraft());
                            if (result) {
                                if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
                                    NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                }
                                NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.ACCEPTED));
                                Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s2), new FutureCallback<Object>() {
                                    @Override
                                    public void onSuccess(final Object p_onSuccess_1_) {
                                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                                    }
                                    
                                    @Override
                                    public void onFailure(final Throwable p_onFailure_1_) {
                                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                                    }
                                });
                            }
                            else {
                                if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
                                    NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                }
                                NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s2, C19PacketResourcePackStatus.Action.DECLINED));
                            }
                            ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
                            NetHandlerPlayClient.this.gameController.displayGuiScreen(null);
                        }
                    }, I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
                }
            });
        }
    }
    
    @Override
    public void handleEntityNBT(final S49PacketUpdateEntityNBT packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            entity.clientUpdateEntityNBT(packetIn.getTagCompound());
        }
    }
    
    @Override
    public void handleCustomPayload(final S3FPacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if ("MC|TrList".equals(packetIn.getChannelName())) {
            final PacketBuffer packetbuffer = packetIn.getBufferData();
            try {
                final int i = packetbuffer.readInt();
                final GuiScreen guiscreen = this.gameController.currentScreen;
                if (guiscreen != null && guiscreen instanceof GuiMerchant && i == Minecraft.thePlayer.openContainer.windowId) {
                    final IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
                    final MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
                    imerchant.setRecipes(merchantrecipelist);
                }
            }
            catch (IOException ioexception) {
                NetHandlerPlayClient.logger.error("Couldn't load trade info", ioexception);
                return;
            }
            finally {
                packetbuffer.release();
            }
            packetbuffer.release();
        }
        else if ("MC|Brand".equals(packetIn.getChannelName())) {
            Minecraft.thePlayer.setClientBrand(packetIn.getBufferData().readStringFromBuffer(32767));
        }
        else if ("MC|BOpen".equals(packetIn.getChannelName())) {
            final ItemStack itemstack = Minecraft.thePlayer.getCurrentEquippedItem();
            if (itemstack != null && itemstack.getItem() == Items.written_book) {
                this.gameController.displayGuiScreen(new GuiScreenBook(Minecraft.thePlayer, itemstack, false));
            }
        }
    }
    
    @Override
    public void handleScoreboardObjective(final S3BPacketScoreboardObjective packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (packetIn.func_149338_e() == 0) {
            final ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
            scoreobjective.setDisplayName(packetIn.func_149337_d());
            scoreobjective.setRenderType(packetIn.func_179817_d());
        }
        else {
            final ScoreObjective scoreobjective2 = scoreboard.getObjective(packetIn.func_149339_c());
            if (packetIn.func_149338_e() == 1) {
                scoreboard.removeObjective(scoreobjective2);
            }
            else if (packetIn.func_149338_e() == 2) {
                scoreobjective2.setDisplayName(packetIn.func_149337_d());
                scoreobjective2.setRenderType(packetIn.func_179817_d());
            }
        }
    }
    
    @Override
    public void handleUpdateScore(final S3CPacketUpdateScore packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        final ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
        if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE) {
            final Score score = scoreboard.getValueFromObjective(packetIn.getPlayerName(), scoreobjective);
            score.setScorePoints(packetIn.getScoreValue());
        }
        else if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE) {
            if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), null);
            }
            else if (scoreobjective != null) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
            }
        }
    }
    
    @Override
    public void handleDisplayScoreboard(final S3DPacketDisplayScoreboard packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (packetIn.func_149370_d().length() == 0) {
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), null);
        }
        else {
            final ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149370_d());
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
        }
    }
    
    @Override
    public void handleTeams(final S3EPacketTeams packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScorePlayerTeam scoreplayerteam;
        if (packetIn.func_149307_h() == 0) {
            scoreplayerteam = scoreboard.createTeam(packetIn.func_149312_c());
        }
        else {
            scoreplayerteam = scoreboard.getTeam(packetIn.func_149312_c());
        }
        if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 2) {
            scoreplayerteam.setTeamName(packetIn.func_149306_d());
            scoreplayerteam.setNamePrefix(packetIn.func_149311_e());
            scoreplayerteam.setNameSuffix(packetIn.func_149309_f());
            scoreplayerteam.setChatFormat(EnumChatFormatting.func_175744_a(packetIn.func_179813_h()));
            scoreplayerteam.func_98298_a(packetIn.func_149308_i());
            final Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());
            if (team$enumvisible != null) {
                scoreplayerteam.setNameTagVisibility(team$enumvisible);
            }
        }
        if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 3) {
            for (final String s : packetIn.func_149310_g()) {
                scoreboard.addPlayerToTeam(s, packetIn.func_149312_c());
            }
        }
        if (packetIn.func_149307_h() == 4) {
            for (final String s2 : packetIn.func_149310_g()) {
                scoreboard.removePlayerFromTeam(s2, scoreplayerteam);
            }
        }
        if (packetIn.func_149307_h() == 1) {
            scoreboard.removeTeam(scoreplayerteam);
        }
    }
    
    @Override
    public void handleParticles(final S2APacketParticles packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        if (packetIn.getParticleCount() == 0) {
            final double d0 = packetIn.getParticleSpeed() * packetIn.getXOffset();
            final double d2 = packetIn.getParticleSpeed() * packetIn.getYOffset();
            final double d3 = packetIn.getParticleSpeed() * packetIn.getZOffset();
            try {
                this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d3, packetIn.getParticleArgs());
            }
            catch (Throwable var17) {
                NetHandlerPlayClient.logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
            }
        }
        else {
            for (int i = 0; i < packetIn.getParticleCount(); ++i) {
                final double d4 = this.avRandomizer.nextGaussian() * packetIn.getXOffset();
                final double d5 = this.avRandomizer.nextGaussian() * packetIn.getYOffset();
                final double d6 = this.avRandomizer.nextGaussian() * packetIn.getZOffset();
                final double d7 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
                final double d8 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
                final double d9 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
                try {
                    this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d4, packetIn.getYCoordinate() + d5, packetIn.getZCoordinate() + d6, d7, d8, d9, packetIn.getParticleArgs());
                }
                catch (Throwable var18) {
                    NetHandlerPlayClient.logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
                    return;
                }
            }
        }
    }
    
    @Override
    public void handleEntityProperties(final S20PacketEntityProperties packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.gameController);
        final Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            final BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();
            for (final S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot : packetIn.func_149441_d()) {
                IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());
                if (iattributeinstance == null) {
                    iattributeinstance = baseattributemap.registerAttribute(new RangedAttribute(null, s20packetentityproperties$snapshot.func_151409_a(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
                }
                iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
                iattributeinstance.removeAllModifiers();
                for (final AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c()) {
                    iattributeinstance.applyModifier(attributemodifier);
                }
            }
        }
    }
    
    public NetworkManager getNetworkManager() {
        return this.netManager;
    }
    
    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return this.playerInfoMap.values();
    }
    
    public NetworkPlayerInfo getPlayerInfo(final UUID p_175102_1_) {
        return this.playerInfoMap.get(p_175102_1_);
    }
    
    public NetworkPlayerInfo getPlayerInfo(final String p_175104_1_) {
        for (final NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
            if (networkplayerinfo.getGameProfile().getName().equals(p_175104_1_)) {
                return networkplayerinfo;
            }
        }
        return null;
    }
    
    public GameProfile getGameProfile() {
        return this.profile;
    }
    
    static /* synthetic */ void access$4(final NetHandlerPlayClient netHandlerPlayClient, final Minecraft gameController) {
        netHandlerPlayClient.gameController = gameController;
    }
}
