package net.minecraft.client.network;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.MetadataAchievement;
import net.minecraft.client.stream.MetadataCombat;
import net.minecraft.client.stream.MetadataPlayerDeath;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
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
import net.minecraft.potion.PotionEffect;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rip.autumn.core.Autumn;
import rip.autumn.events.packet.SendPacketEvent;
import rip.autumn.menu.AutumnMainMenu;

public class NetHandlerPlayClient implements INetHandlerPlayClient {
   private static final Logger logger = LogManager.getLogger();
   private final NetworkManager netManager;
   private final GameProfile profile;
   private final GuiScreen guiScreenServer;
   private final Map playerInfoMap = Maps.newHashMap();
   private final Random avRandomizer = new Random();
   public int currentServerMaxPlayers = 20;
   private Minecraft gameController;
   private WorldClient clientWorldController;
   private boolean doneLoadingTerrain;
   private boolean field_147308_k = false;

   public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager p_i46300_3_, GameProfile p_i46300_4_) {
      this.gameController = mcIn;
      this.guiScreenServer = p_i46300_2_;
      this.netManager = p_i46300_3_;
      this.profile = p_i46300_4_;
   }

   public void cleanup() {
      this.clientWorldController = null;
   }

   public void handleJoinGame(S01PacketJoinGame packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
      this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
      this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
      this.gameController.loadWorld(this.clientWorldController);
      this.gameController.thePlayer.dimension = packetIn.getDimension();
      this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
      this.gameController.thePlayer.setEntityId(packetIn.getEntityId());
      this.currentServerMaxPlayers = packetIn.getMaxPlayers();
      this.gameController.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
      this.gameController.playerController.setGameType(packetIn.getGameType());
      this.gameController.gameSettings.sendSettingsToServer();
      this.netManager.sendPacket(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
   }

   public void handleSpawnObject(S0EPacketSpawnObject packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      double d0 = (double)packetIn.getX() / 32.0D;
      double d1 = (double)packetIn.getY() / 32.0D;
      double d2 = (double)packetIn.getZ() / 32.0D;
      Entity entity = null;
      WorldClient world = this.clientWorldController;
      double velX = (double)packetIn.getSpeedX() / 8000.0D;
      double velY = (double)packetIn.getSpeedY() / 8000.0D;
      double velZ = (double)packetIn.getSpeedZ() / 8000.0D;
      switch(packetIn.getType()) {
      case 1:
         entity = new EntityBoat(world, d0, d1, d2);
         break;
      case 2:
         entity = new EntityItem(world, d0, d1, d2);
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 67:
      case 68:
      case 69:
      case 74:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      default:
         break;
      case 10:
         entity = EntityMinecart.func_180458_a(world, d0, d1, d2, EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
         break;
      case 50:
         entity = new EntityTNTPrimed(world, d0, d1, d2, (EntityLivingBase)null);
         break;
      case 51:
         entity = new EntityEnderCrystal(world, d0, d1, d2);
         break;
      case 60:
         entity = new EntityArrow(world, d0, d1, d2);
         break;
      case 61:
         entity = new EntitySnowball(world, d0, d1, d2);
         break;
      case 62:
         entity = new EntityEgg(world, d0, d1, d2);
         break;
      case 63:
         entity = new EntityLargeFireball(world, d0, d1, d2, velX, velY, velZ);
         packetIn.func_149002_g(0);
         break;
      case 64:
         entity = new EntitySmallFireball(world, d0, d1, d2, velX, velY, velZ);
         packetIn.func_149002_g(0);
         break;
      case 65:
         entity = new EntityEnderPearl(world, d0, d1, d2);
         break;
      case 66:
         entity = new EntityWitherSkull(world, d0, d1, d2, velX, velY, velZ);
         packetIn.func_149002_g(0);
         break;
      case 70:
         entity = new EntityFallingBlock(world, d0, d1, d2, Block.getStateById(packetIn.func_149009_m() & '\uffff'));
         packetIn.func_149002_g(0);
         break;
      case 71:
         entity = new EntityItemFrame(world, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)), EnumFacing.getHorizontal(packetIn.func_149009_m()));
         packetIn.func_149002_g(0);
         break;
      case 72:
         entity = new EntityEnderEye(world, d0, d1, d2);
         break;
      case 73:
         entity = new EntityPotion(world, d0, d1, d2, packetIn.func_149009_m());
         packetIn.func_149002_g(0);
         break;
      case 75:
         entity = new EntityExpBottle(world, d0, d1, d2);
         packetIn.func_149002_g(0);
         break;
      case 76:
         entity = new EntityFireworkRocket(world, d0, d1, d2, (ItemStack)null);
         break;
      case 77:
         entity = new EntityLeashKnot(world, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)));
         packetIn.func_149002_g(0);
         break;
      case 78:
         entity = new EntityArmorStand(world, d0, d1, d2);
         break;
      case 90:
         Entity entity1 = world.getEntityByID(packetIn.func_149009_m());
         if (entity1 instanceof EntityPlayer) {
            entity = new EntityFishHook(world, d0, d1, d2, (EntityPlayer)entity1);
         }

         packetIn.func_149002_g(0);
      }

      if (entity != null) {
         ((Entity)entity).serverPosX = packetIn.getX();
         ((Entity)entity).serverPosY = packetIn.getY();
         ((Entity)entity).serverPosZ = packetIn.getZ();
         ((Entity)entity).rotationPitch = (float)(packetIn.getPitch() * 360) / 256.0F;
         ((Entity)entity).rotationYaw = (float)(packetIn.getYaw() * 360) / 256.0F;
         Entity[] aentity = ((Entity)entity).getParts();
         if (aentity != null) {
            int i = packetIn.getEntityID() - ((Entity)entity).getEntityId();
            int i1 = 0;

            for(int aentityLength = aentity.length; i1 < aentityLength; ++i1) {
               Entity value = aentity[i1];
               value.setEntityId(value.getEntityId() + i);
            }
         }

         ((Entity)entity).setEntityId(packetIn.getEntityID());
         this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entity);
         if (packetIn.func_149009_m() > 0) {
            if (packetIn.getType() == 60) {
               Entity entity2 = world.getEntityByID(packetIn.func_149009_m());
               if (entity2 instanceof EntityLivingBase && entity instanceof EntityArrow) {
                  ((EntityArrow)entity).shootingEntity = entity2;
               }
            }

            ((Entity)entity).setVelocity(velX, velY, velZ);
         }
      }

   }

   public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      WorldClient world = this.clientWorldController;
      Entity entity = new EntityXPOrb(world, (double)packetIn.getX() / 32.0D, (double)packetIn.getY() / 32.0D, (double)packetIn.getZ() / 32.0D, packetIn.getXPValue());
      entity.serverPosX = packetIn.getX();
      entity.serverPosY = packetIn.getY();
      entity.serverPosZ = packetIn.getZ();
      entity.rotationYaw = 0.0F;
      entity.rotationPitch = 0.0F;
      entity.setEntityId(packetIn.getEntityID());
      world.addEntityToWorld(packetIn.getEntityID(), entity);
   }

   public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      double d0 = (double)packetIn.func_149051_d() / 32.0D;
      double d1 = (double)packetIn.func_149050_e() / 32.0D;
      double d2 = (double)packetIn.func_149049_f() / 32.0D;
      Entity entity = null;
      WorldClient world = this.clientWorldController;
      if (packetIn.func_149053_g() == 1) {
         entity = new EntityLightningBolt(world, d0, d1, d2);
      }

      if (entity != null) {
         entity.serverPosX = packetIn.func_149051_d();
         entity.serverPosY = packetIn.func_149050_e();
         entity.serverPosZ = packetIn.func_149049_f();
         entity.rotationYaw = 0.0F;
         entity.rotationPitch = 0.0F;
         entity.setEntityId(packetIn.func_149052_c());
         world.addWeatherEffect(entity);
      }

   }

   public void handleSpawnPainting(S10PacketSpawnPainting packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      WorldClient world = this.clientWorldController;
      EntityPainting entitypainting = new EntityPainting(world, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
      world.addEntityToWorld(packetIn.getEntityID(), entitypainting);
   }

   public void handleEntityVelocity(S12PacketEntityVelocity packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
      if (entity != null) {
         entity.setVelocity((double)packetIn.getMotionX() / 8000.0D, (double)packetIn.getMotionY() / 8000.0D, (double)packetIn.getMotionZ() / 8000.0D);
      }

   }

   public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
      if (entity != null && packetIn.func_149376_c() != null) {
         entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
      }

   }

   public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      double d0 = (double)packetIn.getX() / 32.0D;
      double d1 = (double)packetIn.getY() / 32.0D;
      double d2 = (double)packetIn.getZ() / 32.0D;
      float f = (float)(packetIn.getYaw() * 360) / 256.0F;
      float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;
      EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.gameController.theWorld, this.getPlayerInfo(packetIn.getPlayer()).getGameProfile());
      entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = (double)(entityotherplayermp.serverPosX = packetIn.getX());
      entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = (double)(entityotherplayermp.serverPosY = packetIn.getY());
      entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = (double)(entityotherplayermp.serverPosZ = packetIn.getZ());
      int i = packetIn.getCurrentItemID();
      int currentItem = entityotherplayermp.inventory.currentItem;
      if (i == 0) {
         entityotherplayermp.inventory.mainInventory[currentItem] = null;
      } else {
         entityotherplayermp.inventory.mainInventory[currentItem] = new ItemStack(Item.getItemById(i), 1, 0);
      }

      entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
      this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
      List list = packetIn.func_148944_c();
      if (list != null) {
         entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
      }

   }

   public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
      if (entity != null) {
         entity.serverPosX = packetIn.getX();
         entity.serverPosY = packetIn.getY();
         entity.serverPosZ = packetIn.getZ();
         double d0 = (double)entity.serverPosX / 32.0D;
         double d1 = (double)entity.serverPosY / 32.0D;
         double d2 = (double)entity.serverPosZ / 32.0D;
         float f = (float)(packetIn.getYaw() * 360) / 256.0F;
         float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;
         if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D) {
            entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
         } else {
            entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
         }

         entity.onGround = packetIn.getOnGround();
      }

   }

   public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      if (packetIn.getHeldItemHotbarIndex() >= 0 && packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize()) {
         this.gameController.thePlayer.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
      }

   }

   public void handleEntityMovement(S14PacketEntity packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = packetIn.getEntity(this.clientWorldController);
      if (entity != null) {
         entity.serverPosX += packetIn.func_149062_c();
         entity.serverPosY += packetIn.func_149061_d();
         entity.serverPosZ += packetIn.func_149064_e();
         double d0 = (double)entity.serverPosX / 32.0D;
         double d1 = (double)entity.serverPosY / 32.0D;
         double d2 = (double)entity.serverPosZ / 32.0D;
         float f = packetIn.func_149060_h() ? (float)(packetIn.func_149066_f() * 360) / 256.0F : entity.rotationYaw;
         float f1 = packetIn.func_149060_h() ? (float)(packetIn.func_149063_g() * 360) / 256.0F : entity.rotationPitch;
         entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
         entity.onGround = packetIn.getOnGround();
      }

   }

   public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = packetIn.getEntity(this.clientWorldController);
      if (entity != null) {
         float f = (float)(packetIn.getYaw() * 360) / 256.0F;
         entity.setRotationYawHead(f);
      }

   }

   public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

      for(int i = 0; i < packetIn.getEntityIDs().length; ++i) {
         this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
      }

   }

   public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      EntityPlayer entityplayer = this.gameController.thePlayer;
      double d0 = packetIn.getX();
      double d1 = packetIn.getY();
      double d2 = packetIn.getZ();
      float f = packetIn.getYaw();
      float f1 = packetIn.getPitch();
      if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
         d0 += entityplayer.posX;
      } else {
         entityplayer.motionX = 0.0D;
      }

      if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
         d1 += entityplayer.posY;
      } else {
         entityplayer.motionY = 0.0D;
      }

      if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
         d2 += entityplayer.posZ;
      } else {
         entityplayer.motionZ = 0.0D;
      }

      if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
         f1 += entityplayer.rotationPitch;
      }

      if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
         f += entityplayer.rotationYaw;
      }

      entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
      this.netManager.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
      if (!this.doneLoadingTerrain) {
         entityplayer.prevPosX = entityplayer.posX;
         entityplayer.prevPosY = entityplayer.posY;
         entityplayer.prevPosZ = entityplayer.posZ;
         this.doneLoadingTerrain = true;
         this.gameController.displayGuiScreen((GuiScreen)null);
      }

   }

   public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks = packetIn.getChangedBlocks();
      int i = 0;

      for(int changedBlocksLength = changedBlocks.length; i < changedBlocksLength; ++i) {
         S22PacketMultiBlockChange.BlockUpdateData s22packetmultiblockchange$blockupdatedata = changedBlocks[i];
         this.clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(), s22packetmultiblockchange$blockupdatedata.getBlockState());
      }

   }

   public void handleChunkData(S21PacketChunkData packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      WorldClient world = this.clientWorldController;
      if (packetIn.func_149274_i()) {
         if (packetIn.getExtractedSize() == 0) {
            world.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), false);
            return;
         }

         world.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
      }

      world.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
      Chunk chunk = world.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
      chunk.fillChunk(packetIn.func_149272_d(), packetIn.getExtractedSize(), packetIn.func_149274_i());
      world.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
      if (!packetIn.func_149274_i() || !(world.provider instanceof WorldProviderSurface)) {
         chunk.resetRelightChecks();
      }

   }

   public void handleBlockChange(S23PacketBlockChange packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
   }

   public void handleDisconnect(S40PacketDisconnect packetIn) {
      this.netManager.closeChannel(packetIn.getReason());
   }

   public void onDisconnect(IChatComponent reason) {
      this.gameController.loadWorld((WorldClient)null);
      Minecraft mc = this.gameController;
      GuiScreen screen = this.guiScreenServer;
      if (screen != null) {
         if (screen instanceof GuiScreenRealmsProxy) {
            mc.displayGuiScreen((new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)screen).func_154321_a(), "disconnect.lost", reason)).getProxy());
         } else {
            mc.displayGuiScreen(new GuiDisconnected(screen, "disconnect.lost", reason));
         }
      } else {
         mc.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new AutumnMainMenu()), "disconnect.lost", reason));
      }

   }

   public void addToSendQueue(Packet p_147297_1_) {
      SendPacketEvent event = new SendPacketEvent(p_147297_1_);
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(event);
      if (!event.isCancelled()) {
         this.netManager.sendPacket(p_147297_1_);
      }
   }

   public void addToSendQueueSilent(Packet p_147297_1_) {
      this.netManager.sendPacket(p_147297_1_);
   }

   public void handleCollectItem(S0DPacketCollectItem packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      WorldClient world = this.clientWorldController;
      Entity entity = world.getEntityByID(packetIn.getCollectedItemEntityID());
      EntityLivingBase entitylivingbase = (EntityLivingBase)world.getEntityByID(packetIn.getEntityID());
      if (entitylivingbase == null) {
         entitylivingbase = this.gameController.thePlayer;
      }

      if (entity != null) {
         if (entity instanceof EntityXPOrb) {
            world.playSoundAtEntity(entity, "random.orb", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         } else {
            world.playSoundAtEntity(entity, "random.pop", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         }

         this.gameController.effectRenderer.addEffect(new EntityPickupFX(world, entity, (Entity)entitylivingbase, 0.5F));
         world.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
      }

   }

   public void handleChat(S02PacketChat packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      if (packetIn.getType() == 2) {
         this.gameController.ingameGUI.setRecordPlaying(packetIn.getChatComponent(), false);
      } else {
         this.gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
      }

   }

   public void handleAnimation(S0BPacketAnimation packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
      if (entity != null) {
         EffectRenderer render = this.gameController.effectRenderer;
         switch(packetIn.getAnimationType()) {
         case 0:
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            entitylivingbase.swingItem();
            break;
         case 1:
            entity.performHurtAnimation();
            break;
         case 2:
            EntityPlayer entityplayer = (EntityPlayer)entity;
            entityplayer.wakeUpPlayer(false, false, false);
         case 3:
         default:
            break;
         case 4:
            render.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            break;
         case 5:
            render.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
         }
      }

   }

   public void handleUseBed(S0APacketUseBed packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      packetIn.getPlayer(this.clientWorldController).trySleep(packetIn.getBedPosition());
   }

   public void handleSpawnMob(S0FPacketSpawnMob packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      double d0 = (double)packetIn.getX() / 32.0D;
      double d1 = (double)packetIn.getY() / 32.0D;
      double d2 = (double)packetIn.getZ() / 32.0D;
      float f = (float)(packetIn.getYaw() * 360) / 256.0F;
      float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;
      EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), this.gameController.theWorld);
      entitylivingbase.serverPosX = packetIn.getX();
      entitylivingbase.serverPosY = packetIn.getY();
      entitylivingbase.serverPosZ = packetIn.getZ();
      entitylivingbase.renderYawOffset = entitylivingbase.rotationYawHead = (float)(packetIn.getHeadPitch() * 360) / 256.0F;
      Entity[] aentity = entitylivingbase.getParts();
      if (aentity != null) {
         int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
         int i1 = 0;

         for(int aentityLength = aentity.length; i1 < aentityLength; ++i1) {
            Entity entity = aentity[i1];
            entity.setEntityId(entity.getEntityId() + i);
         }
      }

      entitylivingbase.setEntityId(packetIn.getEntityID());
      entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
      entitylivingbase.motionX = (double)((float)packetIn.getVelocityX() / 8000.0F);
      entitylivingbase.motionY = (double)((float)packetIn.getVelocityY() / 8000.0F);
      entitylivingbase.motionZ = (double)((float)packetIn.getVelocityZ() / 8000.0F);
      this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
      List list = packetIn.func_149027_c();
      if (list != null) {
         entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
      }

   }

   public void handleTimeUpdate(S03PacketTimeUpdate packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      mc.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
      mc.theWorld.setWorldTime(packetIn.getWorldTime());
   }

   public void handleSpawnPosition(S05PacketSpawnPosition packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      mc.thePlayer.setSpawnPoint(packetIn.getSpawnPos(), true);
      mc.theWorld.getWorldInfo().setSpawn(packetIn.getSpawnPos());
   }

   public void handleEntityAttach(S1BPacketEntityAttach packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
      Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
      if (packetIn.getLeash() == 0) {
         boolean flag = false;
         if (packetIn.getEntityId() == mc.thePlayer.getEntityId()) {
            entity = mc.thePlayer;
            if (entity1 instanceof EntityBoat) {
               ((EntityBoat)entity1).setIsBoatEmpty(false);
            }

            flag = ((Entity)entity).ridingEntity == null && entity1 != null;
         } else if (entity1 instanceof EntityBoat) {
            ((EntityBoat)entity1).setIsBoatEmpty(true);
         }

         if (entity == null) {
            return;
         }

         ((Entity)entity).mountEntity(entity1);
         if (flag) {
            GameSettings gamesettings = this.gameController.gameSettings;
            this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode())), false);
         }
      } else if (packetIn.getLeash() == 1 && entity instanceof EntityLiving) {
         if (entity1 != null) {
            ((EntityLiving)entity).setLeashedToEntity(entity1, false);
         } else {
            ((EntityLiving)entity).clearLeashed(false, false);
         }
      }

   }

   public void handleEntityStatus(S19PacketEntityStatus packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      Entity entity = packetIn.getEntity(this.clientWorldController);
      if (entity != null) {
         if (packetIn.getOpCode() == 21) {
            mc.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
         } else {
            entity.handleStatusUpdate(packetIn.getOpCode());
         }
      }

   }

   public void handleUpdateHealth(S06PacketUpdateHealth packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      mc.thePlayer.setPlayerSPHealth(packetIn.getHealth());
      mc.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
      mc.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
   }

   public void handleSetExperience(S1FPacketSetExperience packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      mc.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(), packetIn.getLevel());
   }

   public void handleRespawn(S07PacketRespawn packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      if (packetIn.getDimensionID() != mc.thePlayer.dimension) {
         this.doneLoadingTerrain = false;
         Scoreboard scoreboard = this.clientWorldController.getScoreboard();
         this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, mc.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.gameController.mcProfiler);
         this.clientWorldController.setWorldScoreboard(scoreboard);
         mc.loadWorld(this.clientWorldController);
         mc.thePlayer.dimension = packetIn.getDimensionID();
         mc.displayGuiScreen(new GuiDownloadTerrain(this));
      }

      mc.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
      mc.playerController.setGameType(packetIn.getGameType());
   }

   public void handleExplosion(S27PacketExplosion packetIn) {
      Minecraft mc = this.gameController;
      EntityPlayerSP player = mc.thePlayer;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      Explosion explosion = new Explosion(this.gameController.theWorld, (Entity)null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
      explosion.doExplosionB(true);
      player.motionX += (double)packetIn.func_149149_c();
      player.motionY += (double)packetIn.func_149144_d();
      player.motionZ += (double)packetIn.func_149147_e();
   }

   public void handleOpenWindow(S2DPacketOpenWindow packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      EntityPlayerSP entityplayersp = this.gameController.thePlayer;
      if ("minecraft:container".equals(packetIn.getGuiId())) {
         entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
         entityplayersp.openContainer.windowId = packetIn.getWindowId();
      } else if ("minecraft:villager".equals(packetIn.getGuiId())) {
         entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
         entityplayersp.openContainer.windowId = packetIn.getWindowId();
      } else if ("EntityHorse".equals(packetIn.getGuiId())) {
         Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
         if (entity instanceof EntityHorse) {
            entityplayersp.displayGUIHorse((EntityHorse)entity, new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
         }
      } else if (!packetIn.hasSlots()) {
         entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
         entityplayersp.openContainer.windowId = packetIn.getWindowId();
      } else {
         ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
         entityplayersp.displayGUIChest(containerlocalmenu);
         entityplayersp.openContainer.windowId = packetIn.getWindowId();
      }

   }

   public void handleSetSlot(S2FPacketSetSlot packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      EntityPlayer entityplayer = this.gameController.thePlayer;
      if (packetIn.func_149175_c() == -1) {
         entityplayer.inventory.setItemStack(packetIn.func_149174_e());
      } else {
         boolean flag = false;
         if (this.gameController.currentScreen instanceof GuiContainerCreative) {
            GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
            flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex();
         }

         if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45) {
            ItemStack itemstack = entityplayer.inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
            if (packetIn.func_149174_e() != null && (itemstack == null || itemstack.stackSize < packetIn.func_149174_e().stackSize)) {
               packetIn.func_149174_e().animationsToGo = 5;
            }

            entityplayer.inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
         } else if (packetIn.func_149175_c() == entityplayer.openContainer.windowId && (packetIn.func_149175_c() != 0 || !flag)) {
            entityplayer.openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
         }
      }

   }

   public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Container container = null;
      EntityPlayer entityplayer = this.gameController.thePlayer;
      if (packetIn.getWindowId() == 0) {
         container = entityplayer.inventoryContainer;
      } else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
         container = entityplayer.openContainer;
      }

      if (container != null && !packetIn.func_148888_e()) {
         this.addToSendQueue(new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
      }

   }

   public void handleWindowItems(S30PacketWindowItems packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      EntityPlayer entityplayer = this.gameController.thePlayer;
      if (packetIn.func_148911_c() == 0) {
         entityplayer.inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
      } else if (packetIn.func_148911_c() == entityplayer.openContainer.windowId) {
         entityplayer.openContainer.putStacksInSlots(packetIn.getItemStacks());
      }

   }

   public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
      if (!(tileentity instanceof TileEntitySign)) {
         tileentity = new TileEntitySign();
         ((TileEntity)tileentity).setWorldObj(this.clientWorldController);
         ((TileEntity)tileentity).setPos(packetIn.getSignPosition());
      }

      this.gameController.thePlayer.openEditSign((TileEntitySign)tileentity);
   }

   public void handleUpdateSign(S33PacketUpdateSign packetIn) {
      Minecraft mc = this.gameController;
      WorldClient world = mc.theWorld;
      EntityPlayerSP player = mc.thePlayer;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      boolean flag = false;
      if (world.isBlockLoaded(packetIn.getPos())) {
         TileEntity tileentity = world.getTileEntity(packetIn.getPos());
         if (tileentity instanceof TileEntitySign) {
            TileEntitySign tileentitysign = (TileEntitySign)tileentity;
            if (tileentitysign.getIsEditable()) {
               System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
               tileentitysign.markDirty();
            }

            flag = true;
         }
      }

      if (!flag && player != null) {
         player.addChatMessage(new ChatComponentText("Unable to locate sign at " + packetIn.getPos().getX() + ", " + packetIn.getPos().getY() + ", " + packetIn.getPos().getZ()));
      }

   }

   public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
         TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
         int i = packetIn.getTileEntityType();
         if (i == 1 && tileentity instanceof TileEntityMobSpawner || i == 2 && tileentity instanceof TileEntityCommandBlock || i == 3 && tileentity instanceof TileEntityBeacon || i == 4 && tileentity instanceof TileEntitySkull || i == 5 && tileentity instanceof TileEntityFlowerPot || i == 6 && tileentity instanceof TileEntityBanner) {
            tileentity.readFromNBT(packetIn.getNbtCompound());
         }
      }

   }

   public void handleWindowProperty(S31PacketWindowProperty packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      EntityPlayer entityplayer = this.gameController.thePlayer;
      if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == packetIn.getWindowId()) {
         entityplayer.openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
      }

   }

   public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
      if (entity != null) {
         entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
      }

   }

   public void handleCloseWindow(S2EPacketCloseWindow packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      this.gameController.thePlayer.closeScreenAndDropStack();
   }

   public void handleBlockAction(S24PacketBlockAction packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      this.gameController.theWorld.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
   }

   public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      this.gameController.theWorld.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
   }

   public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

      for(int i = packetIn.getChunkCount() - 1; i >= 0; --i) {
         int j = packetIn.getChunkX(i);
         int k = packetIn.getChunkZ(i);
         this.clientWorldController.doPreChunk(j, k, true);
         this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
         Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
         chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
         this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
         if (!(this.clientWorldController.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
         }
      }

   }

   public void handleChangeGameState(S2BPacketChangeGameState packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      EntityPlayer entityplayer = this.gameController.thePlayer;
      int i = packetIn.getGameState();
      float f = packetIn.func_149137_d();
      int j = MathHelper.floor_float(f + 0.5F);
      if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[i] != null) {
         entityplayer.addChatComponentMessage(new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]));
      }

      if (i == 1) {
         this.clientWorldController.getWorldInfo().setRaining(true);
         this.clientWorldController.setRainStrength(0.0F);
      } else if (i == 2) {
         this.clientWorldController.getWorldInfo().setRaining(false);
         this.clientWorldController.setRainStrength(1.0F);
      } else if (i == 3) {
         this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
      } else if (i == 4) {
         this.gameController.displayGuiScreen(new GuiWinGame());
      } else if (i == 5) {
         GameSettings gamesettings = this.gameController.gameSettings;
         if (f == 0.0F) {
            this.gameController.displayGuiScreen(new GuiScreenDemo());
         } else if (f == 101.0F) {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.movement", new Object[]{GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode())}));
         } else if (f == 102.0F) {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.jump", new Object[]{GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode())}));
         } else if (f == 103.0F) {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.inventory", new Object[]{GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode())}));
         }
      } else if (i == 6) {
         this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ, "random.successful_hit", 0.18F, 0.45F, false);
      } else if (i == 7) {
         this.clientWorldController.setRainStrength(f);
      } else if (i == 8) {
         this.clientWorldController.setThunderStrength(f);
      } else if (i == 10) {
         this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "mob.guardian.curse", 1.0F, 1.0F, false);
      }

   }

   public void handleMaps(S34PacketMaps packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), this.gameController.theWorld);
      packetIn.setMapdataTo(mapdata);
      this.gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapdata);
   }

   public void handleEffect(S28PacketEffect packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      if (packetIn.isSoundServerwide()) {
         this.gameController.theWorld.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
      } else {
         this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
      }

   }

   public void handleStatistics(S37PacketStatistics packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      boolean flag = false;

      StatBase statbase;
      int i;
      for(Iterator var3 = packetIn.func_148974_c().entrySet().iterator(); var3.hasNext(); this.gameController.thePlayer.getStatFileWriter().unlockAchievement(this.gameController.thePlayer, statbase, i)) {
         Entry entry = (Entry)var3.next();
         statbase = (StatBase)entry.getKey();
         i = (Integer)entry.getValue();
         if (statbase.isAchievement() && i > 0) {
            if (this.field_147308_k && this.gameController.thePlayer.getStatFileWriter().readStat(statbase) == 0) {
               Achievement achievement = (Achievement)statbase;
               this.gameController.guiAchievement.displayAchievement(achievement);
               this.gameController.getTwitchStream().func_152911_a(new MetadataAchievement(achievement), 0L);
               if (statbase == AchievementList.openInventory) {
                  this.gameController.gameSettings.showInventoryAchievementHint = false;
                  this.gameController.gameSettings.saveOptions();
               }
            }

            flag = true;
         }
      }

      if (!this.field_147308_k && !flag && this.gameController.gameSettings.showInventoryAchievementHint) {
         this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
      }

      this.field_147308_k = true;
      if (this.gameController.currentScreen instanceof IProgressMeter) {
         ((IProgressMeter)this.gameController.currentScreen).doneLoading();
      }

   }

   public void handleEntityEffect(S1DPacketEntityEffect packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
      if (entity instanceof EntityLivingBase) {
         PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(), packetIn.getAmplifier(), false, packetIn.func_179707_f());
         potioneffect.setPotionDurationMax(packetIn.func_149429_c());
         ((EntityLivingBase)entity).addPotionEffect(potioneffect);
      }

   }

   public void handleCombatEvent(S42PacketCombatEvent packetIn) {
      Minecraft mc = this.gameController;
      WorldClient world = this.clientWorldController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      Entity entity = world.getEntityByID(packetIn.field_179775_c);
      EntityLivingBase entitylivingbase = entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null;
      if (packetIn.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
         long i = (long)(1000 * packetIn.field_179772_d / 20);
         MetadataCombat metadatacombat = new MetadataCombat(mc.thePlayer, entitylivingbase);
         mc.getTwitchStream().func_176026_a(metadatacombat, -i, 0L);
      } else if (packetIn.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
         Entity entity1 = world.getEntityByID(packetIn.field_179774_b);
         if (entity1 instanceof EntityPlayer) {
            MetadataPlayerDeath metadataplayerdeath = new MetadataPlayerDeath((EntityPlayer)entity1, entitylivingbase);
            metadataplayerdeath.func_152807_a(packetIn.deathMessage);
            mc.getTwitchStream().func_152911_a(metadataplayerdeath, 0L);
         }
      }

   }

   public void handleServerDifficulty(S41PacketServerDifficulty packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      mc.theWorld.getWorldInfo().setDifficulty(packetIn.getDifficulty());
      mc.theWorld.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
   }

   public void handleCamera(S43PacketCamera packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = packetIn.getEntity(this.clientWorldController);
      if (entity != null) {
         this.gameController.setRenderViewEntity(entity);
      }

   }

   public void handleWorldBorder(S44PacketWorldBorder packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      packetIn.func_179788_a(this.clientWorldController.getWorldBorder());
   }

   public void handleTitle(S45PacketTitle packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      S45PacketTitle.Type s45packettitle$type = packetIn.getType();
      String s = null;
      String s1 = null;
      String s2 = packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : "";
      switch(s45packettitle$type) {
      case TITLE:
         s = s2;
         break;
      case SUBTITLE:
         s1 = s2;
         break;
      case RESET:
         mc.ingameGUI.displayTitle("", "", -1, -1, -1);
         mc.ingameGUI.func_175177_a();
         return;
      }

      mc.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
   }

   public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {
      if (!this.netManager.isLocalChannel()) {
         this.netManager.setCompressionTreshold(packetIn.func_179760_a());
      }

   }

   public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) {
      this.gameController.ingameGUI.getTabList().setHeader(packetIn.getHeader().getFormattedText().length() == 0 ? null : packetIn.getHeader());
      this.gameController.ingameGUI.getTabList().setFooter(packetIn.getFooter().getFormattedText().length() == 0 ? null : packetIn.getFooter());
   }

   public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
      if (entity instanceof EntityLivingBase) {
         ((EntityLivingBase)entity).removePotionEffectClient(packetIn.getEffectId());
      }

   }

   public void handlePlayerListItem(S38PacketPlayerListItem packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Map infoMap = this.playerInfoMap;
      List func_179767_a = packetIn.func_179767_a();
      int i = 0;

      for(int func_179767_aSize = func_179767_a.size(); i < func_179767_aSize; ++i) {
         S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata = (S38PacketPlayerListItem.AddPlayerData)func_179767_a.get(i);
         if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
            infoMap.remove(s38packetplayerlistitem$addplayerdata.getProfile().getId());
         } else {
            NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)infoMap.get(s38packetplayerlistitem$addplayerdata.getProfile().getId());
            if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
               networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
               infoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
            }

            if (networkplayerinfo != null) {
               switch(packetIn.func_179768_b()) {
               case ADD_PLAYER:
                  networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                  networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                  break;
               case UPDATE_GAME_MODE:
                  networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                  break;
               case UPDATE_LATENCY:
                  networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                  break;
               case UPDATE_DISPLAY_NAME:
                  networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
               }
            }
         }
      }

   }

   public void handleKeepAlive(S00PacketKeepAlive packetIn) {
      this.addToSendQueue(new C00PacketKeepAlive(packetIn.func_149134_c()));
   }

   public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      EntityPlayer entityplayer = this.gameController.thePlayer;
      entityplayer.capabilities.isFlying = packetIn.isFlying();
      entityplayer.capabilities.isCreativeMode = packetIn.isCreativeMode();
      entityplayer.capabilities.disableDamage = packetIn.isInvulnerable();
      entityplayer.capabilities.allowFlying = packetIn.isAllowFlying();
      entityplayer.capabilities.setFlySpeed(packetIn.getFlySpeed());
      entityplayer.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
   }

   public void handleTabComplete(S3APacketTabComplete packetIn) {
      Minecraft mc = this.gameController;
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, mc);
      String[] astring = packetIn.func_149630_c();
      if (mc.currentScreen instanceof GuiChat) {
         GuiChat guichat = (GuiChat)mc.currentScreen;
         guichat.onAutocompleteResponse(astring);
      }

   }

   public void handleSoundEffect(S29PacketSoundEffect packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      this.gameController.theWorld.playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSoundName(), packetIn.getVolume(), packetIn.getPitch(), false);
   }

   public void handleResourcePack(S48PacketResourcePackSend packetIn) {
      String s = packetIn.getURL();
      final String s1 = packetIn.getHash();
      Minecraft mc = this.gameController;
      final NetworkManager networkManager = this.netManager;
      if (s.startsWith("level://")) {
         String s2 = s.substring("level://".length());
         File file1 = new File(mc.mcDataDir, "saves");
         File file2 = new File(file1, s2);
         if (file2.isFile()) {
            networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
            Futures.addCallback(mc.getResourcePackRepository().setResourcePackInstance(file2), new FutureCallback() {
               public void onSuccess(Object p_onSuccess_1_) {
                  networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
               }

               public void onFailure(Throwable p_onFailure_1_) {
                  networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
               }
            });
         } else {
            networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
         }
      } else if (mc.getCurrentServerData() != null && mc.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
         networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
         Futures.addCallback(mc.getResourcePackRepository().downloadResourcePack(s, s1), new FutureCallback() {
            public void onSuccess(Object p_onSuccess_1_) {
               networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
            }

            public void onFailure(Throwable p_onFailure_1_) {
               networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
         });
      } else if (mc.getCurrentServerData() != null && mc.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
         this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
      } else {
         mc.addScheduledTask(() -> {
            mc.displayGuiScreen(new GuiYesNo((result, id) -> {
               if (result) {
                  if (mc.getCurrentServerData() != null) {
                     mc.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
                  }

                  networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                  Futures.addCallback(mc.getResourcePackRepository().downloadResourcePack(s, s1), new FutureCallback() {
                     public void onSuccess(Object p_onSuccess_1_) {
                        networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                     }

                     public void onFailure(Throwable p_onFailure_1_) {
                        networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                     }
                  });
               } else {
                  if (mc.getCurrentServerData() != null) {
                     mc.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
                  }

                  networkManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
               }

               ServerList.func_147414_b(mc.getCurrentServerData());
               mc.displayGuiScreen((GuiScreen)null);
            }, I18n.format("multiplayer.texturePrompt.line1"), I18n.format("multiplayer.texturePrompt.line2"), 0));
         });
      }

   }

   public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = packetIn.getEntity(this.clientWorldController);
      if (entity != null) {
         entity.clientUpdateEntityNBT(packetIn.getTagCompound());
      }

   }

   public void handleCustomPayload(S3FPacketCustomPayload packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      if ("MC|TrList".equals(packetIn.getChannelName())) {
         PacketBuffer packetbuffer = packetIn.getBufferData();

         try {
            int i = packetbuffer.readInt();
            GuiScreen guiscreen = this.gameController.currentScreen;
            if (guiscreen != null && guiscreen instanceof GuiMerchant && i == this.gameController.thePlayer.openContainer.windowId) {
               IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
               MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
               imerchant.setRecipes(merchantrecipelist);
            }
         } catch (IOException var10) {
            logger.error("Couldn't load trade info", var10);
         } finally {
            packetbuffer.release();
         }
      } else if ("MC|Brand".equals(packetIn.getChannelName())) {
         this.gameController.thePlayer.setClientBrand(packetIn.getBufferData().readStringFromBuffer(32767));
      } else if ("MC|BOpen".equals(packetIn.getChannelName())) {
         ItemStack itemstack = this.gameController.thePlayer.getCurrentEquippedItem();
         if (itemstack != null && itemstack.getItem() == Items.written_book) {
            this.gameController.displayGuiScreen(new GuiScreenBook(this.gameController.thePlayer, itemstack, false));
         }
      }

   }

   public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Scoreboard scoreboard = this.clientWorldController.getScoreboard();
      ScoreObjective scoreobjective1;
      if (packetIn.func_149338_e() == 0) {
         scoreobjective1 = scoreboard.addScoreObjective(packetIn.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
         scoreobjective1.setDisplayName(packetIn.func_149337_d());
         scoreobjective1.setRenderType(packetIn.func_179817_d());
      } else {
         scoreobjective1 = scoreboard.getObjective(packetIn.func_149339_c());
         if (packetIn.func_149338_e() == 1) {
            scoreboard.removeObjective(scoreobjective1);
         } else if (packetIn.func_149338_e() == 2) {
            scoreobjective1.setDisplayName(packetIn.func_149337_d());
            scoreobjective1.setRenderType(packetIn.func_179817_d());
         }
      }

   }

   public void handleUpdateScore(S3CPacketUpdateScore packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Scoreboard scoreboard = this.clientWorldController.getScoreboard();
      ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
      if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE) {
         Score score = scoreboard.getValueFromObjective(packetIn.getPlayerName(), scoreobjective);
         score.setScorePoints(packetIn.getScoreValue());
      } else if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE) {
         if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
            scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), (ScoreObjective)null);
         } else if (scoreobjective != null) {
            scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
         }
      }

   }

   public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Scoreboard scoreboard = this.clientWorldController.getScoreboard();
      if (packetIn.func_149370_d().length() == 0) {
         scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), (ScoreObjective)null);
      } else {
         ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149370_d());
         scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
      }

   }

   public void handleTeams(S3EPacketTeams packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Scoreboard scoreboard = this.clientWorldController.getScoreboard();
      ScorePlayerTeam scoreplayerteam;
      if (packetIn.func_149307_h() == 0) {
         scoreplayerteam = scoreboard.createTeam(packetIn.func_149312_c());
      } else {
         scoreplayerteam = scoreboard.getTeam(packetIn.func_149312_c());
      }

      if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 2) {
         scoreplayerteam.setTeamName(packetIn.func_149306_d());
         scoreplayerteam.setNamePrefix(packetIn.func_149311_e());
         scoreplayerteam.setNameSuffix(packetIn.func_149309_f());
         scoreplayerteam.setChatFormat(EnumChatFormatting.func_175744_a(packetIn.func_179813_h()));
         scoreplayerteam.func_98298_a(packetIn.func_149308_i());
         Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());
         if (team$enumvisible != null) {
            scoreplayerteam.setNameTagVisibility(team$enumvisible);
         }
      }

      String s1;
      Iterator var6;
      if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 3) {
         var6 = packetIn.func_149310_g().iterator();

         while(var6.hasNext()) {
            s1 = (String)var6.next();
            scoreboard.addPlayerToTeam(s1, packetIn.func_149312_c());
         }
      }

      if (packetIn.func_149307_h() == 4) {
         var6 = packetIn.func_149310_g().iterator();

         while(var6.hasNext()) {
            s1 = (String)var6.next();
            scoreboard.removePlayerFromTeam(s1, scoreplayerteam);
         }
      }

      if (packetIn.func_149307_h() == 1) {
         scoreboard.removeTeam(scoreplayerteam);
      }

   }

   public void handleParticles(S2APacketParticles packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      if (packetIn.getParticleCount() == 0) {
         double d0 = (double)(packetIn.getParticleSpeed() * packetIn.getXOffset());
         double d2 = (double)(packetIn.getParticleSpeed() * packetIn.getYOffset());
         double d4 = (double)(packetIn.getParticleSpeed() * packetIn.getZOffset());

         try {
            this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
         } catch (Throwable var17) {
            logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
         }
      } else {
         for(int i = 0; i < packetIn.getParticleCount(); ++i) {
            double d1 = this.avRandomizer.nextGaussian() * (double)packetIn.getXOffset();
            double d3 = this.avRandomizer.nextGaussian() * (double)packetIn.getYOffset();
            double d5 = this.avRandomizer.nextGaussian() * (double)packetIn.getZOffset();
            double d6 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
            double d7 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
            double d8 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();

            try {
               this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
            } catch (Throwable var16) {
               logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
               return;
            }
         }
      }

   }

   public void handleEntityProperties(S20PacketEntityProperties packetIn) {
      PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
      if (entity != null) {
         if (!(entity instanceof EntityLivingBase)) {
            throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
         }

         BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();
         Iterator var4 = packetIn.func_149441_d().iterator();

         while(var4.hasNext()) {
            S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot = (S20PacketEntityProperties.Snapshot)var4.next();
            IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());
            if (iattributeinstance == null) {
               iattributeinstance = baseattributemap.registerAttribute(new RangedAttribute((IAttribute)null, s20packetentityproperties$snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE));
            }

            iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
            iattributeinstance.removeAllModifiers();
            Iterator var7 = s20packetentityproperties$snapshot.func_151408_c().iterator();

            while(var7.hasNext()) {
               AttributeModifier attributemodifier = (AttributeModifier)var7.next();
               iattributeinstance.applyModifier(attributemodifier);
            }
         }
      }

   }

   public NetworkManager getNetworkManager() {
      return this.netManager;
   }

   public Collection getPlayerInfoMap() {
      return this.playerInfoMap.values();
   }

   public NetworkPlayerInfo getPlayerInfo(UUID p_175102_1_) {
      return (NetworkPlayerInfo)this.playerInfoMap.get(p_175102_1_);
   }

   public NetworkPlayerInfo getPlayerInfo(String p_175104_1_) {
      Iterator var2 = this.playerInfoMap.values().iterator();

      NetworkPlayerInfo networkplayerinfo;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         networkplayerinfo = (NetworkPlayerInfo)var2.next();
      } while(!networkplayerinfo.getGameProfile().getName().equals(p_175104_1_));

      return networkplayerinfo;
   }

   public GameProfile getGameProfile() {
      return this.profile;
   }
}
