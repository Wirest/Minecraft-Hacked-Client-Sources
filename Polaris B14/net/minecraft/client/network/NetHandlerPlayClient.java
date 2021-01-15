/*      */ package net.minecraft.client.network;
/*      */ 
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.util.concurrent.FutureCallback;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import java.io.File;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.client.ClientBrandRetriever;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.audio.GuardianSound;
/*      */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.gui.GuiChat;
/*      */ import net.minecraft.client.gui.GuiDisconnected;
/*      */ import net.minecraft.client.gui.GuiDownloadTerrain;
/*      */ import net.minecraft.client.gui.GuiIngame;
/*      */ import net.minecraft.client.gui.GuiMainMenu;
/*      */ import net.minecraft.client.gui.GuiMultiplayer;
/*      */ import net.minecraft.client.gui.GuiNewChat;
/*      */ import net.minecraft.client.gui.GuiPlayerTabOverlay;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.gui.GuiScreenDemo;
/*      */ import net.minecraft.client.gui.GuiScreenRealmsProxy;
/*      */ import net.minecraft.client.gui.GuiWinGame;
/*      */ import net.minecraft.client.gui.GuiYesNo;
/*      */ import net.minecraft.client.gui.GuiYesNoCallback;
/*      */ import net.minecraft.client.gui.IProgressMeter;
/*      */ import net.minecraft.client.gui.MapItemRenderer;
/*      */ import net.minecraft.client.gui.achievement.GuiAchievement;
/*      */ import net.minecraft.client.gui.inventory.GuiContainerCreative;
/*      */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*      */ import net.minecraft.client.multiplayer.ServerData;
/*      */ import net.minecraft.client.multiplayer.ServerData.ServerResourceMode;
/*      */ import net.minecraft.client.multiplayer.ServerList;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.particle.EffectRenderer;
/*      */ import net.minecraft.client.particle.EntityPickupFX;
/*      */ import net.minecraft.client.player.inventory.ContainerLocalMenu;
/*      */ import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
/*      */ import net.minecraft.client.renderer.EntityRenderer;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.resources.ResourcePackRepository;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.settings.KeyBinding;
/*      */ import net.minecraft.client.stream.IStream;
/*      */ import net.minecraft.client.stream.MetadataAchievement;
/*      */ import net.minecraft.client.stream.MetadataCombat;
/*      */ import net.minecraft.client.stream.MetadataPlayerDeath;
/*      */ import net.minecraft.creativetab.CreativeTabs;
/*      */ import net.minecraft.entity.DataWatcher;
/*      */ import net.minecraft.entity.DataWatcher.WatchableObject;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.item.EntityArmorStand;
/*      */ import net.minecraft.entity.item.EntityBoat;
/*      */ import net.minecraft.entity.item.EntityEnderCrystal;
/*      */ import net.minecraft.entity.item.EntityEnderEye;
/*      */ import net.minecraft.entity.item.EntityEnderPearl;
/*      */ import net.minecraft.entity.item.EntityExpBottle;
/*      */ import net.minecraft.entity.item.EntityFallingBlock;
/*      */ import net.minecraft.entity.item.EntityFireworkRocket;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
/*      */ import net.minecraft.entity.item.EntityPainting;
/*      */ import net.minecraft.entity.item.EntityTNTPrimed;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.monster.EntityGuardian;
/*      */ import net.minecraft.entity.passive.EntityHorse;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.entity.player.PlayerCapabilities;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.entity.projectile.EntityEgg;
/*      */ import net.minecraft.entity.projectile.EntityFishHook;
/*      */ import net.minecraft.entity.projectile.EntityLargeFireball;
/*      */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*      */ import net.minecraft.inventory.AnimalChest;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.network.NetworkManager;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.PacketBuffer;
/*      */ import net.minecraft.network.PacketThreadUtil;
/*      */ import net.minecraft.network.play.INetHandlerPlayClient;
/*      */ import net.minecraft.network.play.client.C00PacketKeepAlive;
/*      */ import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
/*      */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*      */ import net.minecraft.network.play.client.C19PacketResourcePackStatus;
/*      */ import net.minecraft.network.play.client.C19PacketResourcePackStatus.Action;
/*      */ import net.minecraft.network.play.server.S00PacketKeepAlive;
/*      */ import net.minecraft.network.play.server.S01PacketJoinGame;
/*      */ import net.minecraft.network.play.server.S02PacketChat;
/*      */ import net.minecraft.network.play.server.S03PacketTimeUpdate;
/*      */ import net.minecraft.network.play.server.S04PacketEntityEquipment;
/*      */ import net.minecraft.network.play.server.S05PacketSpawnPosition;
/*      */ import net.minecraft.network.play.server.S06PacketUpdateHealth;
/*      */ import net.minecraft.network.play.server.S07PacketRespawn;
/*      */ import net.minecraft.network.play.server.S08PacketPlayerPosLook;
/*      */ import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;
/*      */ import net.minecraft.network.play.server.S09PacketHeldItemChange;
/*      */ import net.minecraft.network.play.server.S0APacketUseBed;
/*      */ import net.minecraft.network.play.server.S0BPacketAnimation;
/*      */ import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
/*      */ import net.minecraft.network.play.server.S0DPacketCollectItem;
/*      */ import net.minecraft.network.play.server.S0EPacketSpawnObject;
/*      */ import net.minecraft.network.play.server.S0FPacketSpawnMob;
/*      */ import net.minecraft.network.play.server.S10PacketSpawnPainting;
/*      */ import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
/*      */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*      */ import net.minecraft.network.play.server.S13PacketDestroyEntities;
/*      */ import net.minecraft.network.play.server.S14PacketEntity;
/*      */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*      */ import net.minecraft.network.play.server.S19PacketEntityHeadLook;
/*      */ import net.minecraft.network.play.server.S19PacketEntityStatus;
/*      */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*      */ import net.minecraft.network.play.server.S1CPacketEntityMetadata;
/*      */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*      */ import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
/*      */ import net.minecraft.network.play.server.S1FPacketSetExperience;
/*      */ import net.minecraft.network.play.server.S20PacketEntityProperties;
/*      */ import net.minecraft.network.play.server.S20PacketEntityProperties.Snapshot;
/*      */ import net.minecraft.network.play.server.S21PacketChunkData;
/*      */ import net.minecraft.network.play.server.S22PacketMultiBlockChange;
/*      */ import net.minecraft.network.play.server.S22PacketMultiBlockChange.BlockUpdateData;
/*      */ import net.minecraft.network.play.server.S23PacketBlockChange;
/*      */ import net.minecraft.network.play.server.S24PacketBlockAction;
/*      */ import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
/*      */ import net.minecraft.network.play.server.S26PacketMapChunkBulk;
/*      */ import net.minecraft.network.play.server.S27PacketExplosion;
/*      */ import net.minecraft.network.play.server.S28PacketEffect;
/*      */ import net.minecraft.network.play.server.S29PacketSoundEffect;
/*      */ import net.minecraft.network.play.server.S2APacketParticles;
/*      */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*      */ import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
/*      */ import net.minecraft.network.play.server.S2DPacketOpenWindow;
/*      */ import net.minecraft.network.play.server.S2EPacketCloseWindow;
/*      */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*      */ import net.minecraft.network.play.server.S30PacketWindowItems;
/*      */ import net.minecraft.network.play.server.S31PacketWindowProperty;
/*      */ import net.minecraft.network.play.server.S32PacketConfirmTransaction;
/*      */ import net.minecraft.network.play.server.S33PacketUpdateSign;
/*      */ import net.minecraft.network.play.server.S34PacketMaps;
/*      */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*      */ import net.minecraft.network.play.server.S36PacketSignEditorOpen;
/*      */ import net.minecraft.network.play.server.S37PacketStatistics;
/*      */ import net.minecraft.network.play.server.S38PacketPlayerListItem;
/*      */ import net.minecraft.network.play.server.S38PacketPlayerListItem.Action;
/*      */ import net.minecraft.network.play.server.S38PacketPlayerListItem.AddPlayerData;
/*      */ import net.minecraft.network.play.server.S39PacketPlayerAbilities;
/*      */ import net.minecraft.network.play.server.S3APacketTabComplete;
/*      */ import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
/*      */ import net.minecraft.network.play.server.S3CPacketUpdateScore;
/*      */ import net.minecraft.network.play.server.S3CPacketUpdateScore.Action;
/*      */ import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
/*      */ import net.minecraft.network.play.server.S3EPacketTeams;
/*      */ import net.minecraft.network.play.server.S40PacketDisconnect;
/*      */ import net.minecraft.network.play.server.S41PacketServerDifficulty;
/*      */ import net.minecraft.network.play.server.S42PacketCombatEvent;
/*      */ import net.minecraft.network.play.server.S42PacketCombatEvent.Event;
/*      */ import net.minecraft.network.play.server.S43PacketCamera;
/*      */ import net.minecraft.network.play.server.S44PacketWorldBorder;
/*      */ import net.minecraft.network.play.server.S45PacketTitle;
/*      */ import net.minecraft.network.play.server.S45PacketTitle.Type;
/*      */ import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
/*      */ import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
/*      */ import net.minecraft.network.play.server.S48PacketResourcePackSend;
/*      */ import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.realms.DisconnectedRealmsScreen;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.Team.EnumVisible;
/*      */ import net.minecraft.stats.Achievement;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.stats.StatFileWriter;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntityBeacon;
/*      */ import net.minecraft.tileentity.TileEntityCommandBlock;
/*      */ import net.minecraft.tileentity.TileEntityFlowerPot;
/*      */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.tileentity.TileEntitySkull;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.FoodStats;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.Explosion;
/*      */ import net.minecraft.world.WorldProviderSurface;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ import net.minecraft.world.WorldSettings.GameType;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import net.minecraft.world.storage.MapData;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import rip.jutting.polaris.Polaris;
/*      */ import rip.jutting.polaris.module.ModuleManager;
/*      */ 
/*      */ public class NetHandlerPlayClient implements INetHandlerPlayClient
/*      */ {
/*  225 */   private static final Logger logger = ;
/*      */   
/*      */ 
/*      */ 
/*      */   private final NetworkManager netManager;
/*      */   
/*      */ 
/*      */ 
/*      */   private final GameProfile profile;
/*      */   
/*      */ 
/*      */ 
/*      */   public static Entity entity;
/*      */   
/*      */ 
/*      */ 
/*      */   private final GuiScreen guiScreenServer;
/*      */   
/*      */ 
/*      */ 
/*      */   private Minecraft gameController;
/*      */   
/*      */ 
/*      */ 
/*      */   private WorldClient clientWorldController;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean doneLoadingTerrain;
/*      */   
/*      */ 
/*  256 */   private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
/*  257 */   public int currentServerMaxPlayers = 20;
/*  258 */   private boolean field_147308_k = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */   private final Random avRandomizer = new Random();
/*      */   
/*      */   public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager p_i46300_3_, GameProfile p_i46300_4_)
/*      */   {
/*  268 */     this.gameController = mcIn;
/*  269 */     this.guiScreenServer = p_i46300_2_;
/*  270 */     this.netManager = p_i46300_3_;
/*  271 */     this.profile = p_i46300_4_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void cleanup()
/*      */   {
/*  279 */     this.clientWorldController = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleJoinGame(S01PacketJoinGame packetIn)
/*      */   {
/*  288 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  289 */     this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
/*  290 */     this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
/*  291 */     this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
/*  292 */     this.gameController.loadWorld(this.clientWorldController);
/*  293 */     this.gameController.thePlayer.dimension = packetIn.getDimension();
/*  294 */     this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
/*  295 */     this.gameController.thePlayer.setEntityId(packetIn.getEntityId());
/*  296 */     this.currentServerMaxPlayers = packetIn.getMaxPlayers();
/*  297 */     this.gameController.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
/*  298 */     this.gameController.playerController.setGameType(packetIn.getGameType());
/*  299 */     this.gameController.gameSettings.sendSettingsToServer();
/*  300 */     if (Polaris.instance.moduleManager.getModuleByName("CBSpoof").isToggled()) {
/*  301 */       this.netManager.sendPacket(new C17PacketCustomPayload("CB|INIT", new PacketBuffer(Unpooled.buffer()).writeString("CB|INIT")));
/*  302 */       this.netManager.sendPacket(new C17PacketCustomPayload("CB-Binary", new PacketBuffer(Unpooled.buffer()).writeString("CB-Binary")));
/*  303 */       System.out.println("NIGGER");
/*      */     }
/*      */     else {
/*  306 */       this.netManager.sendPacket(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSpawnObject(S0EPacketSpawnObject packetIn)
/*      */   {
/*  315 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  316 */     double d0 = packetIn.getX() / 32.0D;
/*  317 */     double d1 = packetIn.getY() / 32.0D;
/*  318 */     double d2 = packetIn.getZ() / 32.0D;
/*  319 */     Entity entity = null;
/*      */     
/*  321 */     if (packetIn.getType() == 10)
/*      */     {
/*  323 */       entity = net.minecraft.entity.item.EntityMinecart.func_180458_a(this.clientWorldController, d0, d1, d2, EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
/*      */     }
/*  325 */     else if (packetIn.getType() == 90)
/*      */     {
/*  327 */       Entity entity1 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
/*      */       
/*  329 */       if ((entity1 instanceof EntityPlayer))
/*      */       {
/*  331 */         entity = new EntityFishHook(this.clientWorldController, d0, d1, d2, (EntityPlayer)entity1);
/*      */       }
/*      */       
/*  334 */       packetIn.func_149002_g(0);
/*      */     }
/*  336 */     else if (packetIn.getType() == 60)
/*      */     {
/*  338 */       entity = new EntityArrow(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  340 */     else if (packetIn.getType() == 61)
/*      */     {
/*  342 */       entity = new net.minecraft.entity.projectile.EntitySnowball(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  344 */     else if (packetIn.getType() == 71)
/*      */     {
/*  346 */       entity = new net.minecraft.entity.item.EntityItemFrame(this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)), net.minecraft.util.EnumFacing.getHorizontal(packetIn.func_149009_m()));
/*  347 */       packetIn.func_149002_g(0);
/*      */     }
/*  349 */     else if (packetIn.getType() == 77)
/*      */     {
/*  351 */       entity = new net.minecraft.entity.EntityLeashKnot(this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)));
/*  352 */       packetIn.func_149002_g(0);
/*      */     }
/*  354 */     else if (packetIn.getType() == 65)
/*      */     {
/*  356 */       entity = new EntityEnderPearl(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  358 */     else if (packetIn.getType() == 72)
/*      */     {
/*  360 */       entity = new EntityEnderEye(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  362 */     else if (packetIn.getType() == 76)
/*      */     {
/*  364 */       entity = new EntityFireworkRocket(this.clientWorldController, d0, d1, d2, null);
/*      */     }
/*  366 */     else if (packetIn.getType() == 63)
/*      */     {
/*  368 */       entity = new EntityLargeFireball(this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*  369 */       packetIn.func_149002_g(0);
/*      */     }
/*  371 */     else if (packetIn.getType() == 64)
/*      */     {
/*  373 */       entity = new EntitySmallFireball(this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*  374 */       packetIn.func_149002_g(0);
/*      */     }
/*  376 */     else if (packetIn.getType() == 66)
/*      */     {
/*  378 */       entity = new net.minecraft.entity.projectile.EntityWitherSkull(this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*  379 */       packetIn.func_149002_g(0);
/*      */     }
/*  381 */     else if (packetIn.getType() == 62)
/*      */     {
/*  383 */       entity = new EntityEgg(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  385 */     else if (packetIn.getType() == 73)
/*      */     {
/*  387 */       entity = new net.minecraft.entity.projectile.EntityPotion(this.clientWorldController, d0, d1, d2, packetIn.func_149009_m());
/*  388 */       packetIn.func_149002_g(0);
/*      */     }
/*  390 */     else if (packetIn.getType() == 75)
/*      */     {
/*  392 */       entity = new EntityExpBottle(this.clientWorldController, d0, d1, d2);
/*  393 */       packetIn.func_149002_g(0);
/*      */     }
/*  395 */     else if (packetIn.getType() == 1)
/*      */     {
/*  397 */       entity = new EntityBoat(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  399 */     else if (packetIn.getType() == 50)
/*      */     {
/*  401 */       entity = new EntityTNTPrimed(this.clientWorldController, d0, d1, d2, null);
/*      */     }
/*  403 */     else if (packetIn.getType() == 78)
/*      */     {
/*  405 */       entity = new EntityArmorStand(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  407 */     else if (packetIn.getType() == 51)
/*      */     {
/*  409 */       entity = new EntityEnderCrystal(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  411 */     else if (packetIn.getType() == 2)
/*      */     {
/*  413 */       entity = new EntityItem(this.clientWorldController, d0, d1, d2);
/*      */     }
/*  415 */     else if (packetIn.getType() == 70)
/*      */     {
/*  417 */       entity = new EntityFallingBlock(this.clientWorldController, d0, d1, d2, Block.getStateById(packetIn.func_149009_m() & 0xFFFF));
/*  418 */       packetIn.func_149002_g(0);
/*      */     }
/*      */     
/*  421 */     if (entity != null)
/*      */     {
/*  423 */       entity.serverPosX = packetIn.getX();
/*  424 */       entity.serverPosY = packetIn.getY();
/*  425 */       entity.serverPosZ = packetIn.getZ();
/*  426 */       entity.rotationPitch = (packetIn.getPitch() * 360 / 256.0F);
/*  427 */       entity.rotationYaw = (packetIn.getYaw() * 360 / 256.0F);
/*  428 */       Entity[] aentity = entity.getParts();
/*      */       
/*  430 */       if (aentity != null)
/*      */       {
/*  432 */         int i = packetIn.getEntityID() - entity.getEntityId();
/*      */         
/*  434 */         for (int j = 0; j < aentity.length; j++)
/*      */         {
/*  436 */           aentity[j].setEntityId(aentity[j].getEntityId() + i);
/*      */         }
/*      */       }
/*      */       
/*  440 */       entity.setEntityId(packetIn.getEntityID());
/*  441 */       this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
/*      */       
/*  443 */       if (packetIn.func_149009_m() > 0)
/*      */       {
/*  445 */         if (packetIn.getType() == 60)
/*      */         {
/*  447 */           Entity entity2 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
/*      */           
/*  449 */           if (((entity2 instanceof EntityLivingBase)) && ((entity instanceof EntityArrow)))
/*      */           {
/*  451 */             ((EntityArrow)entity).shootingEntity = entity2;
/*      */           }
/*      */         }
/*      */         
/*  455 */         entity.setVelocity(packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn)
/*      */   {
/*  465 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  466 */     Entity entity = new EntityXPOrb(this.clientWorldController, packetIn.getX() / 32.0D, packetIn.getY() / 32.0D, packetIn.getZ() / 32.0D, packetIn.getXPValue());
/*  467 */     entity.serverPosX = packetIn.getX();
/*  468 */     entity.serverPosY = packetIn.getY();
/*  469 */     entity.serverPosZ = packetIn.getZ();
/*  470 */     entity.rotationYaw = 0.0F;
/*  471 */     entity.rotationPitch = 0.0F;
/*  472 */     entity.setEntityId(packetIn.getEntityID());
/*  473 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn)
/*      */   {
/*  481 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  482 */     double d0 = packetIn.func_149051_d() / 32.0D;
/*  483 */     double d1 = packetIn.func_149050_e() / 32.0D;
/*  484 */     double d2 = packetIn.func_149049_f() / 32.0D;
/*  485 */     Entity entity = null;
/*      */     
/*  487 */     if (packetIn.func_149053_g() == 1)
/*      */     {
/*  489 */       entity = new net.minecraft.entity.effect.EntityLightningBolt(this.clientWorldController, d0, d1, d2);
/*      */     }
/*      */     
/*  492 */     if (entity != null)
/*      */     {
/*  494 */       entity.serverPosX = packetIn.func_149051_d();
/*  495 */       entity.serverPosY = packetIn.func_149050_e();
/*  496 */       entity.serverPosZ = packetIn.func_149049_f();
/*  497 */       entity.rotationYaw = 0.0F;
/*  498 */       entity.rotationPitch = 0.0F;
/*  499 */       entity.setEntityId(packetIn.func_149052_c());
/*  500 */       this.clientWorldController.addWeatherEffect(entity);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSpawnPainting(S10PacketSpawnPainting packetIn)
/*      */   {
/*  509 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  510 */     EntityPainting entitypainting = new EntityPainting(this.clientWorldController, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
/*  511 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitypainting);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleEntityVelocity(S12PacketEntityVelocity packetIn)
/*      */   {
/*  519 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  520 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/*  522 */     if (entity != null)
/*      */     {
/*  524 */       entity.setVelocity(packetIn.getMotionX() / 8000.0D, packetIn.getMotionY() / 8000.0D, packetIn.getMotionZ() / 8000.0D);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleEntityMetadata(S1CPacketEntityMetadata packetIn)
/*      */   {
/*  534 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  535 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/*  537 */     if ((entity != null) && (packetIn.func_149376_c() != null))
/*      */     {
/*  539 */       entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn)
/*      */   {
/*  548 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  549 */     double d0 = packetIn.getX() / 32.0D;
/*  550 */     double d1 = packetIn.getY() / 32.0D;
/*  551 */     double d2 = packetIn.getZ() / 32.0D;
/*  552 */     float f = packetIn.getYaw() * 360 / 256.0F;
/*  553 */     float f1 = packetIn.getPitch() * 360 / 256.0F;
/*  554 */     EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.gameController.theWorld, getPlayerInfo(packetIn.getPlayer()).getGameProfile());
/*  555 */     entityotherplayermp.prevPosX = (entityotherplayermp.lastTickPosX = entityotherplayermp.serverPosX = packetIn.getX());
/*  556 */     entityotherplayermp.prevPosY = (entityotherplayermp.lastTickPosY = entityotherplayermp.serverPosY = packetIn.getY());
/*  557 */     entityotherplayermp.prevPosZ = (entityotherplayermp.lastTickPosZ = entityotherplayermp.serverPosZ = packetIn.getZ());
/*  558 */     int i = packetIn.getCurrentItemID();
/*      */     
/*  560 */     if (i == 0)
/*      */     {
/*  562 */       entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
/*      */     }
/*      */     else
/*      */     {
/*  566 */       entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(net.minecraft.item.Item.getItemById(i), 1, 0);
/*      */     }
/*      */     
/*  569 */     entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
/*  570 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
/*  571 */     List<DataWatcher.WatchableObject> list = packetIn.func_148944_c();
/*      */     
/*  573 */     if (list != null)
/*      */     {
/*  575 */       entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleEntityTeleport(S18PacketEntityTeleport packetIn)
/*      */   {
/*  584 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  585 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/*  587 */     if (entity != null)
/*      */     {
/*  589 */       entity.serverPosX = packetIn.getX();
/*  590 */       entity.serverPosY = packetIn.getY();
/*  591 */       entity.serverPosZ = packetIn.getZ();
/*  592 */       double d0 = entity.serverPosX / 32.0D;
/*  593 */       double d1 = entity.serverPosY / 32.0D;
/*  594 */       double d2 = entity.serverPosZ / 32.0D;
/*  595 */       float f = packetIn.getYaw() * 360 / 256.0F;
/*  596 */       float f1 = packetIn.getPitch() * 360 / 256.0F;
/*      */       
/*  598 */       if ((Math.abs(entity.posX - d0) < 0.03125D) && (Math.abs(entity.posY - d1) < 0.015625D) && (Math.abs(entity.posZ - d2) < 0.03125D))
/*      */       {
/*  600 */         entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
/*      */       }
/*      */       else
/*      */       {
/*  604 */         entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
/*      */       }
/*      */       
/*  607 */       entity.onGround = packetIn.getOnGround();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleHeldItemChange(S09PacketHeldItemChange packetIn)
/*      */   {
/*  616 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/*  618 */     if ((packetIn.getHeldItemHotbarIndex() >= 0) && (packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize()))
/*      */     {
/*  620 */       this.gameController.thePlayer.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleEntityMovement(S14PacketEntity packetIn)
/*      */   {
/*  631 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  632 */     Entity entity = packetIn.getEntity(this.clientWorldController);
/*      */     
/*  634 */     if (entity != null)
/*      */     {
/*  636 */       entity.serverPosX += packetIn.func_149062_c();
/*  637 */       entity.serverPosY += packetIn.func_149061_d();
/*  638 */       entity.serverPosZ += packetIn.func_149064_e();
/*  639 */       double d0 = entity.serverPosX / 32.0D;
/*  640 */       double d1 = entity.serverPosY / 32.0D;
/*  641 */       double d2 = entity.serverPosZ / 32.0D;
/*  642 */       float f = packetIn.func_149060_h() ? packetIn.func_149066_f() * 360 / 256.0F : entity.rotationYaw;
/*  643 */       float f1 = packetIn.func_149060_h() ? packetIn.func_149063_g() * 360 / 256.0F : entity.rotationPitch;
/*  644 */       entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
/*  645 */       entity.onGround = packetIn.getOnGround();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn)
/*      */   {
/*  655 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  656 */     Entity entity = packetIn.getEntity(this.clientWorldController);
/*      */     
/*  658 */     if (entity != null)
/*      */     {
/*  660 */       float f = packetIn.getYaw() * 360 / 256.0F;
/*  661 */       entity.setRotationYawHead(f);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleDestroyEntities(S13PacketDestroyEntities packetIn)
/*      */   {
/*  672 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/*  674 */     for (int i = 0; i < packetIn.getEntityIDs().length; i++)
/*      */     {
/*  676 */       this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn)
/*      */   {
/*  687 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  688 */     EntityPlayer entityplayer = this.gameController.thePlayer;
/*  689 */     double d0 = packetIn.getX();
/*  690 */     double d1 = packetIn.getY();
/*  691 */     double d2 = packetIn.getZ();
/*  692 */     float f = packetIn.getYaw();
/*  693 */     float f1 = packetIn.getPitch();
/*      */     
/*  695 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X))
/*      */     {
/*  697 */       d0 += entityplayer.posX;
/*      */     }
/*      */     else
/*      */     {
/*  701 */       entityplayer.motionX = 0.0D;
/*      */     }
/*      */     
/*  704 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y))
/*      */     {
/*  706 */       d1 += entityplayer.posY;
/*      */     }
/*      */     else
/*      */     {
/*  710 */       entityplayer.motionY = 0.0D;
/*      */     }
/*      */     
/*  713 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z))
/*      */     {
/*  715 */       d2 += entityplayer.posZ;
/*      */     }
/*      */     else
/*      */     {
/*  719 */       entityplayer.motionZ = 0.0D;
/*      */     }
/*      */     
/*  722 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
/*      */     {
/*  724 */       f1 += entityplayer.rotationPitch;
/*      */     }
/*      */     
/*  727 */     if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
/*      */     {
/*  729 */       f += entityplayer.rotationYaw;
/*      */     }
/*      */     
/*  732 */     entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
/*  733 */     this.netManager.sendPacket(new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
/*      */     
/*  735 */     if (!this.doneLoadingTerrain)
/*      */     {
/*  737 */       this.gameController.thePlayer.prevPosX = this.gameController.thePlayer.posX;
/*  738 */       this.gameController.thePlayer.prevPosY = this.gameController.thePlayer.posY;
/*  739 */       this.gameController.thePlayer.prevPosZ = this.gameController.thePlayer.posZ;
/*  740 */       this.doneLoadingTerrain = true;
/*  741 */       this.gameController.displayGuiScreen(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn)
/*      */   {
/*  752 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     S22PacketMultiBlockChange.BlockUpdateData[] arrayOfBlockUpdateData;
/*  754 */     int j = (arrayOfBlockUpdateData = packetIn.getChangedBlocks()).length; for (int i = 0; i < j; i++) { S22PacketMultiBlockChange.BlockUpdateData s22packetmultiblockchange$blockupdatedata = arrayOfBlockUpdateData[i];
/*      */       
/*  756 */       this.clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(), s22packetmultiblockchange$blockupdatedata.getBlockState());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleChunkData(S21PacketChunkData packetIn)
/*      */   {
/*  765 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/*  767 */     if (packetIn.func_149274_i())
/*      */     {
/*  769 */       if (packetIn.getExtractedSize() == 0)
/*      */       {
/*  771 */         this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), false);
/*  772 */         return;
/*      */       }
/*      */       
/*  775 */       this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
/*      */     }
/*      */     
/*  778 */     this.clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
/*  779 */     Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
/*  780 */     chunk.fillChunk(packetIn.func_149272_d(), packetIn.getExtractedSize(), packetIn.func_149274_i());
/*  781 */     this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
/*      */     
/*  783 */     if ((!packetIn.func_149274_i()) || (!(this.clientWorldController.provider instanceof WorldProviderSurface)))
/*      */     {
/*  785 */       chunk.resetRelightChecks();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleBlockChange(S23PacketBlockChange packetIn)
/*      */   {
/*  794 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  795 */     this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleDisconnect(S40PacketDisconnect packetIn)
/*      */   {
/*  803 */     this.netManager.closeChannel(packetIn.getReason());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDisconnect(IChatComponent reason)
/*      */   {
/*  811 */     this.gameController.loadWorld(null);
/*      */     
/*  813 */     if (this.guiScreenServer != null)
/*      */     {
/*  815 */       if ((this.guiScreenServer instanceof GuiScreenRealmsProxy))
/*      */       {
/*  817 */         this.gameController.displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).func_154321_a(), "disconnect.lost", reason).getProxy());
/*      */       }
/*      */       else
/*      */       {
/*  821 */         this.gameController.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/*  826 */       this.gameController.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", reason));
/*      */     }
/*      */   }
/*      */   
/*      */   public void addToSendQueue(Packet p_147297_1_)
/*      */   {
/*  832 */     this.netManager.sendPacket(p_147297_1_);
/*      */   }
/*      */   
/*      */   public Entity closeEntity() {
/*  836 */     Entity close = null;
/*  837 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
/*  838 */       Entity e = (Entity)o;
/*  839 */       if (((e instanceof EntityOtherPlayerMP)) && (e.isEntityAlive()) && (
/*  840 */         (close == null) || (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) < Minecraft.getMinecraft().thePlayer.getDistanceToEntity(close)))) {
/*  841 */         close = e;
/*      */       }
/*      */     }
/*      */     
/*  845 */     return close;
/*      */   }
/*      */   
/*      */   public void addToSendAuraQueue(Packet p_147297_1_)
/*      */   {
/*  850 */     this.netManager.sendPacket(p_147297_1_);
/*      */   }
/*      */   
/*      */   public void handleCollectItem(S0DPacketCollectItem packetIn)
/*      */   {
/*  855 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  856 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
/*  857 */     EntityLivingBase entitylivingbase = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/*  859 */     if (entitylivingbase == null)
/*      */     {
/*  861 */       entitylivingbase = this.gameController.thePlayer;
/*      */     }
/*      */     
/*  864 */     if (entity != null)
/*      */     {
/*  866 */       if ((entity instanceof EntityXPOrb))
/*      */       {
/*  868 */         this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*      */       }
/*      */       else
/*      */       {
/*  872 */         this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*      */       }
/*      */       
/*  875 */       this.gameController.effectRenderer.addEffect(new EntityPickupFX(this.clientWorldController, entity, entitylivingbase, 0.5F));
/*  876 */       this.clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleChat(S02PacketChat packetIn)
/*      */   {
/*  885 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/*  887 */     if (packetIn.getType() == 2)
/*      */     {
/*  889 */       this.gameController.ingameGUI.setRecordPlaying(packetIn.getChatComponent(), false);
/*      */     }
/*      */     else
/*      */     {
/*  893 */       this.gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleAnimation(S0BPacketAnimation packetIn)
/*      */   {
/*  903 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  904 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/*  906 */     if (entity != null)
/*      */     {
/*  908 */       if (packetIn.getAnimationType() == 0)
/*      */       {
/*  910 */         EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
/*  911 */         entitylivingbase.swingItem();
/*      */       }
/*  913 */       else if (packetIn.getAnimationType() == 1)
/*      */       {
/*  915 */         entity.performHurtAnimation();
/*      */       }
/*  917 */       else if (packetIn.getAnimationType() == 2)
/*      */       {
/*  919 */         EntityPlayer entityplayer = (EntityPlayer)entity;
/*  920 */         entityplayer.wakeUpPlayer(false, false, false);
/*      */       }
/*  922 */       else if (packetIn.getAnimationType() == 4)
/*      */       {
/*  924 */         this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
/*      */       }
/*  926 */       else if (packetIn.getAnimationType() == 5)
/*      */       {
/*  928 */         this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleUseBed(S0APacketUseBed packetIn)
/*      */   {
/*  939 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  940 */     packetIn.getPlayer(this.clientWorldController).trySleep(packetIn.getBedPosition());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSpawnMob(S0FPacketSpawnMob packetIn)
/*      */   {
/*  949 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  950 */     double d0 = packetIn.getX() / 32.0D;
/*  951 */     double d1 = packetIn.getY() / 32.0D;
/*  952 */     double d2 = packetIn.getZ() / 32.0D;
/*  953 */     float f = packetIn.getYaw() * 360 / 256.0F;
/*  954 */     float f1 = packetIn.getPitch() * 360 / 256.0F;
/*  955 */     EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), this.gameController.theWorld);
/*  956 */     entitylivingbase.serverPosX = packetIn.getX();
/*  957 */     entitylivingbase.serverPosY = packetIn.getY();
/*  958 */     entitylivingbase.serverPosZ = packetIn.getZ();
/*  959 */     entitylivingbase.renderYawOffset = (entitylivingbase.rotationYawHead = packetIn.getHeadPitch() * 360 / 256.0F);
/*  960 */     Entity[] aentity = entitylivingbase.getParts();
/*      */     
/*  962 */     if (aentity != null)
/*      */     {
/*  964 */       int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
/*      */       
/*  966 */       for (int j = 0; j < aentity.length; j++)
/*      */       {
/*  968 */         aentity[j].setEntityId(aentity[j].getEntityId() + i);
/*      */       }
/*      */     }
/*      */     
/*  972 */     entitylivingbase.setEntityId(packetIn.getEntityID());
/*  973 */     entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
/*  974 */     entitylivingbase.motionX = (packetIn.getVelocityX() / 8000.0F);
/*  975 */     entitylivingbase.motionY = (packetIn.getVelocityY() / 8000.0F);
/*  976 */     entitylivingbase.motionZ = (packetIn.getVelocityZ() / 8000.0F);
/*  977 */     this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
/*  978 */     List<DataWatcher.WatchableObject> list = packetIn.func_149027_c();
/*      */     
/*  980 */     if (list != null)
/*      */     {
/*  982 */       entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleTimeUpdate(S03PacketTimeUpdate packetIn)
/*      */   {
/*  988 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  989 */     this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
/*  990 */     this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
/*      */   }
/*      */   
/*      */   public void handleSpawnPosition(S05PacketSpawnPosition packetIn)
/*      */   {
/*  995 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*  996 */     this.gameController.thePlayer.setSpawnPoint(packetIn.getSpawnPos(), true);
/*  997 */     this.gameController.theWorld.getWorldInfo().setSpawn(packetIn.getSpawnPos());
/*      */   }
/*      */   
/*      */   public void handleEntityAttach(S1BPacketEntityAttach packetIn)
/*      */   {
/* 1002 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1003 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/* 1004 */     Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
/*      */     
/* 1006 */     if (packetIn.getLeash() == 0)
/*      */     {
/* 1008 */       boolean flag = false;
/*      */       
/* 1010 */       if (packetIn.getEntityId() == this.gameController.thePlayer.getEntityId())
/*      */       {
/* 1012 */         entity = this.gameController.thePlayer;
/*      */         
/* 1014 */         if ((entity1 instanceof EntityBoat))
/*      */         {
/* 1016 */           ((EntityBoat)entity1).setIsBoatEmpty(false);
/*      */         }
/*      */         
/* 1019 */         flag = (entity.ridingEntity == null) && (entity1 != null);
/*      */       }
/* 1021 */       else if ((entity1 instanceof EntityBoat))
/*      */       {
/* 1023 */         ((EntityBoat)entity1).setIsBoatEmpty(true);
/*      */       }
/*      */       
/* 1026 */       if (entity == null)
/*      */       {
/* 1028 */         return;
/*      */       }
/*      */       
/* 1031 */       entity.mountEntity(entity1);
/*      */       
/* 1033 */       if (flag)
/*      */       {
/* 1035 */         GameSettings gamesettings = this.gameController.gameSettings;
/* 1036 */         this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode()) }), false);
/*      */       }
/*      */     }
/* 1039 */     else if ((packetIn.getLeash() == 1) && ((entity instanceof EntityLiving)))
/*      */     {
/* 1041 */       if (entity1 != null)
/*      */       {
/* 1043 */         ((EntityLiving)entity).setLeashedToEntity(entity1, false);
/*      */       }
/*      */       else
/*      */       {
/* 1047 */         ((EntityLiving)entity).clearLeashed(false, false);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleEntityStatus(S19PacketEntityStatus packetIn)
/*      */   {
/* 1060 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1061 */     Entity entity = packetIn.getEntity(this.clientWorldController);
/*      */     
/* 1063 */     if (entity != null)
/*      */     {
/* 1065 */       if (packetIn.getOpCode() == 21)
/*      */       {
/* 1067 */         this.gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
/*      */       }
/*      */       else
/*      */       {
/* 1071 */         entity.handleStatusUpdate(packetIn.getOpCode());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleUpdateHealth(S06PacketUpdateHealth packetIn)
/*      */   {
/* 1078 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1079 */     this.gameController.thePlayer.setPlayerSPHealth(packetIn.getHealth());
/* 1080 */     this.gameController.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
/* 1081 */     this.gameController.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
/*      */   }
/*      */   
/*      */   public void handleSetExperience(S1FPacketSetExperience packetIn)
/*      */   {
/* 1086 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1087 */     this.gameController.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(), packetIn.getLevel());
/*      */   }
/*      */   
/*      */   public void handleRespawn(S07PacketRespawn packetIn)
/*      */   {
/* 1092 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/* 1094 */     if (packetIn.getDimensionID() != this.gameController.thePlayer.dimension)
/*      */     {
/* 1096 */       this.doneLoadingTerrain = false;
/* 1097 */       Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/* 1098 */       this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, this.gameController.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.gameController.mcProfiler);
/* 1099 */       this.clientWorldController.setWorldScoreboard(scoreboard);
/* 1100 */       this.gameController.loadWorld(this.clientWorldController);
/* 1101 */       this.gameController.thePlayer.dimension = packetIn.getDimensionID();
/* 1102 */       this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
/*      */     }
/*      */     
/* 1105 */     this.gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
/* 1106 */     this.gameController.playerController.setGameType(packetIn.getGameType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleExplosion(S27PacketExplosion packetIn)
/*      */   {
/* 1114 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1115 */     Explosion explosion = new Explosion(this.gameController.theWorld, null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
/* 1116 */     explosion.doExplosionB(true);
/* 1117 */     this.gameController.thePlayer.motionX += packetIn.func_149149_c();
/* 1118 */     this.gameController.thePlayer.motionY += packetIn.func_149144_d();
/* 1119 */     this.gameController.thePlayer.motionZ += packetIn.func_149147_e();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleOpenWindow(S2DPacketOpenWindow packetIn)
/*      */   {
/* 1128 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1129 */     EntityPlayerSP entityplayersp = this.gameController.thePlayer;
/*      */     
/* 1131 */     if ("minecraft:container".equals(packetIn.getGuiId()))
/*      */     {
/* 1133 */       entityplayersp.displayGUIChest(new net.minecraft.inventory.InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
/* 1134 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     }
/* 1136 */     else if ("minecraft:villager".equals(packetIn.getGuiId()))
/*      */     {
/* 1138 */       entityplayersp.displayVillagerTradeGui(new net.minecraft.entity.NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
/* 1139 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     }
/* 1141 */     else if ("EntityHorse".equals(packetIn.getGuiId()))
/*      */     {
/* 1143 */       Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */       
/* 1145 */       if ((entity instanceof EntityHorse))
/*      */       {
/* 1147 */         entityplayersp.displayGUIHorse((EntityHorse)entity, new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
/* 1148 */         entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */       }
/*      */     }
/* 1151 */     else if (!packetIn.hasSlots())
/*      */     {
/* 1153 */       entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
/* 1154 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     }
/*      */     else
/*      */     {
/* 1158 */       ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
/* 1159 */       entityplayersp.displayGUIChest(containerlocalmenu);
/* 1160 */       entityplayersp.openContainer.windowId = packetIn.getWindowId();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSetSlot(S2FPacketSetSlot packetIn)
/*      */   {
/* 1169 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1170 */     EntityPlayer entityplayer = this.gameController.thePlayer;
/*      */     
/* 1172 */     if (packetIn.func_149175_c() == -1)
/*      */     {
/* 1174 */       entityplayer.inventory.setItemStack(packetIn.func_149174_e());
/*      */     }
/*      */     else
/*      */     {
/* 1178 */       boolean flag = false;
/*      */       
/* 1180 */       if ((this.gameController.currentScreen instanceof GuiContainerCreative))
/*      */       {
/* 1182 */         GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
/* 1183 */         flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex();
/*      */       }
/*      */       
/* 1186 */       if ((packetIn.func_149175_c() == 0) && (packetIn.func_149173_d() >= 36) && (packetIn.func_149173_d() < 45))
/*      */       {
/* 1188 */         ItemStack itemstack = entityplayer.inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
/*      */         
/* 1190 */         if ((packetIn.func_149174_e() != null) && ((itemstack == null) || (itemstack.stackSize < packetIn.func_149174_e().stackSize)))
/*      */         {
/* 1192 */           packetIn.func_149174_e().animationsToGo = 5;
/*      */         }
/*      */         
/* 1195 */         entityplayer.inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
/*      */       }
/* 1197 */       else if ((packetIn.func_149175_c() == entityplayer.openContainer.windowId) && ((packetIn.func_149175_c() != 0) || (!flag)))
/*      */       {
/* 1199 */         entityplayer.openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn)
/*      */   {
/* 1210 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1211 */     Container container = null;
/* 1212 */     EntityPlayer entityplayer = this.gameController.thePlayer;
/*      */     
/* 1214 */     if (packetIn.getWindowId() == 0)
/*      */     {
/* 1216 */       container = entityplayer.inventoryContainer;
/*      */     }
/* 1218 */     else if (packetIn.getWindowId() == entityplayer.openContainer.windowId)
/*      */     {
/* 1220 */       container = entityplayer.openContainer;
/*      */     }
/*      */     
/* 1223 */     if ((container != null) && (!packetIn.func_148888_e()))
/*      */     {
/* 1225 */       addToSendQueue(new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleWindowItems(S30PacketWindowItems packetIn)
/*      */   {
/* 1234 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1235 */     EntityPlayer entityplayer = this.gameController.thePlayer;
/*      */     
/* 1237 */     if (packetIn.func_148911_c() == 0)
/*      */     {
/* 1239 */       entityplayer.inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
/*      */     }
/* 1241 */     else if (packetIn.func_148911_c() == entityplayer.openContainer.windowId)
/*      */     {
/* 1243 */       entityplayer.openContainer.putStacksInSlots(packetIn.getItemStacks());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn)
/*      */   {
/* 1252 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1253 */     TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
/*      */     
/* 1255 */     if (!(tileentity instanceof TileEntitySign))
/*      */     {
/* 1257 */       tileentity = new TileEntitySign();
/* 1258 */       tileentity.setWorldObj(this.clientWorldController);
/* 1259 */       tileentity.setPos(packetIn.getSignPosition());
/*      */     }
/*      */     
/* 1262 */     this.gameController.thePlayer.openEditSign((TileEntitySign)tileentity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleUpdateSign(S33PacketUpdateSign packetIn)
/*      */   {
/* 1270 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1271 */     boolean flag = false;
/*      */     
/* 1273 */     if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos()))
/*      */     {
/* 1275 */       TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
/*      */       
/* 1277 */       if ((tileentity instanceof TileEntitySign))
/*      */       {
/* 1279 */         TileEntitySign tileentitysign = (TileEntitySign)tileentity;
/*      */         
/* 1281 */         if (tileentitysign.getIsEditable())
/*      */         {
/* 1283 */           System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
/* 1284 */           tileentitysign.markDirty();
/*      */         }
/*      */         
/* 1287 */         flag = true;
/*      */       }
/*      */     }
/*      */     
/* 1291 */     if ((!flag) && (this.gameController.thePlayer != null))
/*      */     {
/* 1293 */       this.gameController.thePlayer.addChatMessage(new ChatComponentText("Unable to locate sign at " + packetIn.getPos().getX() + ", " + packetIn.getPos().getY() + ", " + packetIn.getPos().getZ()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn)
/*      */   {
/* 1303 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/* 1305 */     if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos()))
/*      */     {
/* 1307 */       TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
/* 1308 */       int i = packetIn.getTileEntityType();
/*      */       
/* 1310 */       if (((i == 1) && ((tileentity instanceof TileEntityMobSpawner))) || ((i == 2) && ((tileentity instanceof TileEntityCommandBlock))) || ((i == 3) && ((tileentity instanceof TileEntityBeacon))) || ((i == 4) && ((tileentity instanceof TileEntitySkull))) || ((i == 5) && ((tileentity instanceof TileEntityFlowerPot))) || ((i == 6) && ((tileentity instanceof net.minecraft.tileentity.TileEntityBanner))))
/*      */       {
/* 1312 */         tileentity.readFromNBT(packetIn.getNbtCompound());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleWindowProperty(S31PacketWindowProperty packetIn)
/*      */   {
/* 1322 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1323 */     EntityPlayer entityplayer = this.gameController.thePlayer;
/*      */     
/* 1325 */     if ((entityplayer.openContainer != null) && (entityplayer.openContainer.windowId == packetIn.getWindowId()))
/*      */     {
/* 1327 */       entityplayer.openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleEntityEquipment(S04PacketEntityEquipment packetIn)
/*      */   {
/* 1333 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1334 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
/*      */     
/* 1336 */     if (entity != null)
/*      */     {
/* 1338 */       entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleCloseWindow(S2EPacketCloseWindow packetIn)
/*      */   {
/* 1347 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1348 */     this.gameController.thePlayer.closeScreenAndDropStack();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleBlockAction(S24PacketBlockAction packetIn)
/*      */   {
/* 1358 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1359 */     this.gameController.theWorld.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn)
/*      */   {
/* 1367 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1368 */     this.gameController.theWorld.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
/*      */   }
/*      */   
/*      */   public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn)
/*      */   {
/* 1373 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/* 1375 */     for (int i = 0; i < packetIn.getChunkCount(); i++)
/*      */     {
/* 1377 */       int j = packetIn.getChunkX(i);
/* 1378 */       int k = packetIn.getChunkZ(i);
/* 1379 */       this.clientWorldController.doPreChunk(j, k, true);
/* 1380 */       this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
/* 1381 */       Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
/* 1382 */       chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
/* 1383 */       this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
/*      */       
/* 1385 */       if (!(this.clientWorldController.provider instanceof WorldProviderSurface))
/*      */       {
/* 1387 */         chunk.resetRelightChecks();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleChangeGameState(S2BPacketChangeGameState packetIn)
/*      */   {
/* 1394 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1395 */     EntityPlayer entityplayer = this.gameController.thePlayer;
/* 1396 */     int i = packetIn.getGameState();
/* 1397 */     float f = packetIn.func_149137_d();
/* 1398 */     int j = MathHelper.floor_float(f + 0.5F);
/*      */     
/* 1400 */     if ((i >= 0) && (i < S2BPacketChangeGameState.MESSAGE_NAMES.length) && (S2BPacketChangeGameState.MESSAGE_NAMES[i] != null))
/*      */     {
/* 1402 */       entityplayer.addChatComponentMessage(new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]));
/*      */     }
/*      */     
/* 1405 */     if (i == 1)
/*      */     {
/* 1407 */       this.clientWorldController.getWorldInfo().setRaining(true);
/* 1408 */       this.clientWorldController.setRainStrength(0.0F);
/*      */     }
/* 1410 */     else if (i == 2)
/*      */     {
/* 1412 */       this.clientWorldController.getWorldInfo().setRaining(false);
/* 1413 */       this.clientWorldController.setRainStrength(1.0F);
/*      */     }
/* 1415 */     else if (i == 3)
/*      */     {
/* 1417 */       this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
/*      */     }
/* 1419 */     else if (i == 4)
/*      */     {
/* 1421 */       this.gameController.displayGuiScreen(new GuiWinGame());
/*      */     }
/* 1423 */     else if (i == 5)
/*      */     {
/* 1425 */       GameSettings gamesettings = this.gameController.gameSettings;
/*      */       
/* 1427 */       if (f == 0.0F)
/*      */       {
/* 1429 */         this.gameController.displayGuiScreen(new GuiScreenDemo());
/*      */       }
/* 1431 */       else if (f == 101.0F)
/*      */       {
/* 1433 */         this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.movement", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }));
/*      */       }
/* 1435 */       else if (f == 102.0F)
/*      */       {
/* 1437 */         this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }));
/*      */       }
/* 1439 */       else if (f == 103.0F)
/*      */       {
/* 1441 */         this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }));
/*      */       }
/*      */     }
/* 1444 */     else if (i == 6)
/*      */     {
/* 1446 */       this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ, "random.successful_hit", 0.18F, 0.45F, false);
/*      */     }
/* 1448 */     else if (i == 7)
/*      */     {
/* 1450 */       this.clientWorldController.setRainStrength(f);
/*      */     }
/* 1452 */     else if (i == 8)
/*      */     {
/* 1454 */       this.clientWorldController.setThunderStrength(f);
/*      */     }
/* 1456 */     else if (i == 10)
/*      */     {
/* 1458 */       this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 1459 */       this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "mob.guardian.curse", 1.0F, 1.0F, false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleMaps(S34PacketMaps packetIn)
/*      */   {
/* 1469 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1470 */     MapData mapdata = net.minecraft.item.ItemMap.loadMapData(packetIn.getMapId(), this.gameController.theWorld);
/* 1471 */     packetIn.setMapdataTo(mapdata);
/* 1472 */     this.gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapdata);
/*      */   }
/*      */   
/*      */   public void handleEffect(S28PacketEffect packetIn)
/*      */   {
/* 1477 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/* 1479 */     if (packetIn.isSoundServerwide())
/*      */     {
/* 1481 */       this.gameController.theWorld.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
/*      */     }
/*      */     else
/*      */     {
/* 1485 */       this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleStatistics(S37PacketStatistics packetIn)
/*      */   {
/* 1494 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1495 */     boolean flag = false;
/*      */     
/* 1497 */     for (Map.Entry<StatBase, Integer> entry : packetIn.func_148974_c().entrySet())
/*      */     {
/* 1499 */       StatBase statbase = (StatBase)entry.getKey();
/* 1500 */       int i = ((Integer)entry.getValue()).intValue();
/*      */       
/* 1502 */       if ((statbase.isAchievement()) && (i > 0))
/*      */       {
/* 1504 */         if ((this.field_147308_k) && (this.gameController.thePlayer.getStatFileWriter().readStat(statbase) == 0))
/*      */         {
/* 1506 */           Achievement achievement = (Achievement)statbase;
/* 1507 */           this.gameController.guiAchievement.displayAchievement(achievement);
/* 1508 */           this.gameController.getTwitchStream().func_152911_a(new MetadataAchievement(achievement), 0L);
/*      */           
/* 1510 */           if (statbase == AchievementList.openInventory)
/*      */           {
/* 1512 */             this.gameController.gameSettings.showInventoryAchievementHint = false;
/* 1513 */             this.gameController.gameSettings.saveOptions();
/*      */           }
/*      */         }
/*      */         
/* 1517 */         flag = true;
/*      */       }
/*      */       
/* 1520 */       this.gameController.thePlayer.getStatFileWriter().unlockAchievement(this.gameController.thePlayer, statbase, i);
/*      */     }
/*      */     
/* 1523 */     if ((!this.field_147308_k) && (!flag) && (this.gameController.gameSettings.showInventoryAchievementHint))
/*      */     {
/* 1525 */       this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
/*      */     }
/*      */     
/* 1528 */     this.field_147308_k = true;
/*      */     
/* 1530 */     if ((this.gameController.currentScreen instanceof IProgressMeter))
/*      */     {
/* 1532 */       ((IProgressMeter)this.gameController.currentScreen).doneLoading();
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleEntityEffect(S1DPacketEntityEffect packetIn)
/*      */   {
/* 1538 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1539 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/* 1541 */     if ((entity instanceof EntityLivingBase))
/*      */     {
/* 1543 */       PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(), packetIn.getAmplifier(), false, packetIn.func_179707_f());
/* 1544 */       potioneffect.setPotionDurationMax(packetIn.func_149429_c());
/* 1545 */       ((EntityLivingBase)entity).addPotionEffect(potioneffect);
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleCombatEvent(S42PacketCombatEvent packetIn)
/*      */   {
/* 1551 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1552 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.field_179775_c);
/* 1553 */     EntityLivingBase entitylivingbase = (entity instanceof EntityLivingBase) ? (EntityLivingBase)entity : null;
/*      */     
/* 1555 */     if (packetIn.eventType == S42PacketCombatEvent.Event.END_COMBAT)
/*      */     {
/* 1557 */       long i = 1000 * packetIn.field_179772_d / 20;
/* 1558 */       MetadataCombat metadatacombat = new MetadataCombat(this.gameController.thePlayer, entitylivingbase);
/* 1559 */       this.gameController.getTwitchStream().func_176026_a(metadatacombat, 0L - i, 0L);
/*      */     }
/* 1561 */     else if (packetIn.eventType == S42PacketCombatEvent.Event.ENTITY_DIED)
/*      */     {
/* 1563 */       Entity entity1 = this.clientWorldController.getEntityByID(packetIn.field_179774_b);
/*      */       
/* 1565 */       if ((entity1 instanceof EntityPlayer))
/*      */       {
/* 1567 */         MetadataPlayerDeath metadataplayerdeath = new MetadataPlayerDeath((EntityPlayer)entity1, entitylivingbase);
/* 1568 */         metadataplayerdeath.func_152807_a(packetIn.deathMessage);
/* 1569 */         this.gameController.getTwitchStream().func_152911_a(metadataplayerdeath, 0L);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleServerDifficulty(S41PacketServerDifficulty packetIn)
/*      */   {
/* 1576 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1577 */     this.gameController.theWorld.getWorldInfo().setDifficulty(packetIn.getDifficulty());
/* 1578 */     this.gameController.theWorld.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
/*      */   }
/*      */   
/*      */   public void handleCamera(S43PacketCamera packetIn)
/*      */   {
/* 1583 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1584 */     Entity entity = packetIn.getEntity(this.clientWorldController);
/*      */     
/* 1586 */     if (entity != null)
/*      */     {
/* 1588 */       this.gameController.setRenderViewEntity(entity);
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleWorldBorder(S44PacketWorldBorder packetIn)
/*      */   {
/* 1594 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1595 */     packetIn.func_179788_a(this.clientWorldController.getWorldBorder());
/*      */   }
/*      */   
/*      */ 
/*      */   public void handleTitle(S45PacketTitle packetIn)
/*      */   {
/* 1601 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1602 */     S45PacketTitle.Type s45packettitle$type = packetIn.getType();
/* 1603 */     String s = null;
/* 1604 */     String s1 = null;
/* 1605 */     String s2 = packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : "";
/*      */     
/* 1607 */     switch (s45packettitle$type)
/*      */     {
/*      */     case CLEAR: 
/* 1610 */       s = s2;
/* 1611 */       break;
/*      */     
/*      */     case RESET: 
/* 1614 */       s1 = s2;
/* 1615 */       break;
/*      */     
/*      */     case TITLE: 
/* 1618 */       this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
/* 1619 */       this.gameController.ingameGUI.func_175177_a();
/* 1620 */       return;
/*      */     }
/*      */     
/* 1623 */     this.gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
/*      */   }
/*      */   
/*      */   public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn)
/*      */   {
/* 1628 */     if (!this.netManager.isLocalChannel())
/*      */     {
/* 1630 */       this.netManager.setCompressionTreshold(packetIn.func_179760_a());
/*      */     }
/*      */   }
/*      */   
/*      */   public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn)
/*      */   {
/* 1636 */     this.gameController.ingameGUI.getTabList().setHeader(packetIn.getHeader().getFormattedText().length() == 0 ? null : packetIn.getHeader());
/* 1637 */     this.gameController.ingameGUI.getTabList().setFooter(packetIn.getFooter().getFormattedText().length() == 0 ? null : packetIn.getFooter());
/*      */   }
/*      */   
/*      */   public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn)
/*      */   {
/* 1642 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1643 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/* 1645 */     if ((entity instanceof EntityLivingBase))
/*      */     {
/* 1647 */       ((EntityLivingBase)entity).removePotionEffectClient(packetIn.getEffectId());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void handlePlayerListItem(S38PacketPlayerListItem packetIn)
/*      */   {
/* 1654 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/* 1656 */     for (S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : packetIn.func_179767_a())
/*      */     {
/* 1658 */       if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER)
/*      */       {
/* 1660 */         this.playerInfoMap.remove(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/*      */       }
/*      */       else
/*      */       {
/* 1664 */         NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)this.playerInfoMap.get(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/*      */         
/* 1666 */         if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER)
/*      */         {
/* 1668 */           networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
/* 1669 */           this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
/*      */         }
/*      */         
/* 1672 */         if (networkplayerinfo != null)
/*      */         {
/* 1674 */           switch (packetIn.func_179768_b())
/*      */           {
/*      */           case ADD_PLAYER: 
/* 1677 */             networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
/* 1678 */             networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
/* 1679 */             break;
/*      */           
/*      */           case REMOVE_PLAYER: 
/* 1682 */             networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
/* 1683 */             break;
/*      */           
/*      */           case UPDATE_DISPLAY_NAME: 
/* 1686 */             networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
/* 1687 */             break;
/*      */           
/*      */           case UPDATE_GAME_MODE: 
/* 1690 */             networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleKeepAlive(S00PacketKeepAlive packetIn)
/*      */   {
/* 1699 */     addToSendQueue(new C00PacketKeepAlive(packetIn.func_149134_c()));
/*      */   }
/*      */   
/*      */   public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn)
/*      */   {
/* 1704 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1705 */     EntityPlayer entityplayer = this.gameController.thePlayer;
/* 1706 */     entityplayer.capabilities.isFlying = packetIn.isFlying();
/* 1707 */     entityplayer.capabilities.isCreativeMode = packetIn.isCreativeMode();
/* 1708 */     entityplayer.capabilities.disableDamage = packetIn.isInvulnerable();
/* 1709 */     entityplayer.capabilities.allowFlying = packetIn.isAllowFlying();
/* 1710 */     entityplayer.capabilities.setFlySpeed(packetIn.getFlySpeed());
/* 1711 */     entityplayer.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleTabComplete(S3APacketTabComplete packetIn)
/*      */   {
/* 1719 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1720 */     String[] astring = packetIn.func_149630_c();
/*      */     
/* 1722 */     if ((this.gameController.currentScreen instanceof GuiChat))
/*      */     {
/* 1724 */       GuiChat guichat = (GuiChat)this.gameController.currentScreen;
/* 1725 */       guichat.onAutocompleteResponse(astring);
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleSoundEffect(S29PacketSoundEffect packetIn)
/*      */   {
/* 1731 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1732 */     this.gameController.theWorld.playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSoundName(), packetIn.getVolume(), packetIn.getPitch(), false);
/*      */   }
/*      */   
/*      */   public void handleResourcePack(S48PacketResourcePackSend packetIn)
/*      */   {
/* 1737 */     final String s = packetIn.getURL();
/* 1738 */     final String s1 = packetIn.getHash();
/*      */     
/* 1740 */     if (s.startsWith("level://"))
/*      */     {
/* 1742 */       String s2 = s.substring("level://".length());
/* 1743 */       File file1 = new File(this.gameController.mcDataDir, "saves");
/* 1744 */       File file2 = new File(file1, s2);
/*      */       
/* 1746 */       if (file2.isFile())
/*      */       {
/* 1748 */         this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
/* 1749 */         Futures.addCallback(this.gameController.getResourcePackRepository().setResourcePackInstance(file2), new FutureCallback()
/*      */         {
/*      */           public void onSuccess(Object p_onSuccess_1_)
/*      */           {
/* 1753 */             NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
/*      */           }
/*      */           
/*      */           public void onFailure(Throwable p_onFailure_1_) {
/* 1757 */             NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */           }
/*      */         });
/*      */       }
/*      */       else
/*      */       {
/* 1763 */         this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */       }
/*      */       
/*      */ 
/*      */     }
/* 1768 */     else if ((this.gameController.getCurrentServerData() != null) && (this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED))
/*      */     {
/* 1770 */       this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
/* 1771 */       Futures.addCallback(this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), new FutureCallback()
/*      */       {
/*      */         public void onSuccess(Object p_onSuccess_1_)
/*      */         {
/* 1775 */           NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
/*      */         }
/*      */         
/*      */         public void onFailure(Throwable p_onFailure_1_) {
/* 1779 */           NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */         }
/*      */       });
/*      */     }
/* 1783 */     else if ((this.gameController.getCurrentServerData() != null) && (this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT))
/*      */     {
/* 1785 */       this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
/*      */     }
/*      */     else
/*      */     {
/* 1789 */       this.gameController.addScheduledTask(new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/* 1793 */           NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
/*      */           {
/*      */             public void confirmClicked(boolean result, int id)
/*      */             {
/* 1797 */               NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
/*      */               
/* 1799 */               if (result)
/*      */               {
/* 1801 */                 if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
/*      */                 {
/* 1803 */                   NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
/*      */                 }
/*      */                 
/* 1806 */                 NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(this.val$s1, C19PacketResourcePackStatus.Action.ACCEPTED));
/* 1807 */                 Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(this.val$s, this.val$s1), new FutureCallback()
/*      */                 {
/*      */                   public void onSuccess(Object p_onSuccess_1_)
/*      */                   {
/* 1811 */                     NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(this.val$s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
/*      */                   }
/*      */                   
/*      */                   public void onFailure(Throwable p_onFailure_1_) {
/* 1815 */                     NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(this.val$s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
/*      */                   }
/*      */                 });
/*      */               }
/*      */               else
/*      */               {
/* 1821 */                 if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
/*      */                 {
/* 1823 */                   NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
/*      */                 }
/*      */                 
/* 1826 */                 NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(this.val$s1, C19PacketResourcePackStatus.Action.DECLINED));
/*      */               }
/*      */               
/* 1829 */               ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
/* 1830 */               NetHandlerPlayClient.this.gameController.displayGuiScreen(null);
/*      */             }
/* 1832 */           }, I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn)
/*      */   {
/* 1841 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1842 */     Entity entity = packetIn.getEntity(this.clientWorldController);
/*      */     
/* 1844 */     if (entity != null)
/*      */     {
/* 1846 */       entity.clientUpdateEntityNBT(packetIn.getTagCompound());
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void handleCustomPayload(net.minecraft.network.play.server.S3FPacketCustomPayload packetIn)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: aload_0
/*      */     //   2: aload_0
/*      */     //   3: getfield 159	net/minecraft/client/network/NetHandlerPlayClient:gameController	Lnet/minecraft/client/Minecraft;
/*      */     //   6: invokestatic 182	net/minecraft/network/PacketThreadUtil:checkThreadAndEnqueue	(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V
/*      */     //   9: ldc_w 2642
/*      */     //   12: aload_1
/*      */     //   13: invokevirtual 2647	net/minecraft/network/play/server/S3FPacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   16: invokevirtual 1537	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   19: ifeq +119 -> 138
/*      */     //   22: aload_1
/*      */     //   23: invokevirtual 2651	net/minecraft/network/play/server/S3FPacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   26: astore_2
/*      */     //   27: aload_2
/*      */     //   28: invokevirtual 2654	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   31: istore_3
/*      */     //   32: aload_0
/*      */     //   33: getfield 159	net/minecraft/client/network/NetHandlerPlayClient:gameController	Lnet/minecraft/client/Minecraft;
/*      */     //   36: getfield 1627	net/minecraft/client/Minecraft:currentScreen	Lnet/minecraft/client/gui/GuiScreen;
/*      */     //   39: astore 4
/*      */     //   41: aload 4
/*      */     //   43: ifnull +87 -> 130
/*      */     //   46: aload 4
/*      */     //   48: instanceof 2656
/*      */     //   51: ifeq +79 -> 130
/*      */     //   54: iload_3
/*      */     //   55: aload_0
/*      */     //   56: getfield 159	net/minecraft/client/network/NetHandlerPlayClient:gameController	Lnet/minecraft/client/Minecraft;
/*      */     //   59: getfield 245	net/minecraft/client/Minecraft:thePlayer	Lnet/minecraft/client/entity/EntityPlayerSP;
/*      */     //   62: getfield 1556	net/minecraft/client/entity/EntityPlayerSP:openContainer	Lnet/minecraft/inventory/Container;
/*      */     //   65: getfield 1564	net/minecraft/inventory/Container:windowId	I
/*      */     //   68: if_icmpne +62 -> 130
/*      */     //   71: aload 4
/*      */     //   73: checkcast 2656	net/minecraft/client/gui/GuiMerchant
/*      */     //   76: invokevirtual 2660	net/minecraft/client/gui/GuiMerchant:getMerchant	()Lnet/minecraft/entity/IMerchant;
/*      */     //   79: astore 5
/*      */     //   81: aload_2
/*      */     //   82: invokestatic 2666	net/minecraft/village/MerchantRecipeList:readFromBuf	(Lnet/minecraft/network/PacketBuffer;)Lnet/minecraft/village/MerchantRecipeList;
/*      */     //   85: astore 6
/*      */     //   87: aload 5
/*      */     //   89: aload 6
/*      */     //   91: invokeinterface 2672 2 0
/*      */     //   96: goto +34 -> 130
/*      */     //   99: astore_3
/*      */     //   100: getstatic 136	net/minecraft/client/network/NetHandlerPlayClient:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   103: ldc_w 2674
/*      */     //   106: aload_3
/*      */     //   107: invokeinterface 2680 3 0
/*      */     //   112: aload_2
/*      */     //   113: invokevirtual 2683	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   116: pop
/*      */     //   117: goto +118 -> 235
/*      */     //   120: astore 7
/*      */     //   122: aload_2
/*      */     //   123: invokevirtual 2683	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   126: pop
/*      */     //   127: aload 7
/*      */     //   129: athrow
/*      */     //   130: aload_2
/*      */     //   131: invokevirtual 2683	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   134: pop
/*      */     //   135: goto +100 -> 235
/*      */     //   138: ldc_w 352
/*      */     //   141: aload_1
/*      */     //   142: invokevirtual 2647	net/minecraft/network/play/server/S3FPacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   145: invokevirtual 1537	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   148: ifeq +26 -> 174
/*      */     //   151: aload_0
/*      */     //   152: getfield 159	net/minecraft/client/network/NetHandlerPlayClient:gameController	Lnet/minecraft/client/Minecraft;
/*      */     //   155: getfield 245	net/minecraft/client/Minecraft:thePlayer	Lnet/minecraft/client/entity/EntityPlayerSP;
/*      */     //   158: aload_1
/*      */     //   159: invokevirtual 2651	net/minecraft/network/play/server/S3FPacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   162: sipush 32767
/*      */     //   165: invokevirtual 2688	net/minecraft/network/PacketBuffer:readStringFromBuffer	(I)Ljava/lang/String;
/*      */     //   168: invokevirtual 2691	net/minecraft/client/entity/EntityPlayerSP:setClientBrand	(Ljava/lang/String;)V
/*      */     //   171: goto +64 -> 235
/*      */     //   174: ldc_w 2693
/*      */     //   177: aload_1
/*      */     //   178: invokevirtual 2647	net/minecraft/network/play/server/S3FPacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   181: invokevirtual 1537	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   184: ifeq +51 -> 235
/*      */     //   187: aload_0
/*      */     //   188: getfield 159	net/minecraft/client/network/NetHandlerPlayClient:gameController	Lnet/minecraft/client/Minecraft;
/*      */     //   191: getfield 245	net/minecraft/client/Minecraft:thePlayer	Lnet/minecraft/client/entity/EntityPlayerSP;
/*      */     //   194: invokevirtual 2696	net/minecraft/client/entity/EntityPlayerSP:getCurrentEquippedItem	()Lnet/minecraft/item/ItemStack;
/*      */     //   197: astore_2
/*      */     //   198: aload_2
/*      */     //   199: ifnull +36 -> 235
/*      */     //   202: aload_2
/*      */     //   203: invokevirtual 2700	net/minecraft/item/ItemStack:getItem	()Lnet/minecraft/item/Item;
/*      */     //   206: getstatic 2706	net/minecraft/init/Items:written_book	Lnet/minecraft/item/Item;
/*      */     //   209: if_acmpne +26 -> 235
/*      */     //   212: aload_0
/*      */     //   213: getfield 159	net/minecraft/client/network/NetHandlerPlayClient:gameController	Lnet/minecraft/client/Minecraft;
/*      */     //   216: new 2708	net/minecraft/client/gui/GuiScreenBook
/*      */     //   219: dup
/*      */     //   220: aload_0
/*      */     //   221: getfield 159	net/minecraft/client/network/NetHandlerPlayClient:gameController	Lnet/minecraft/client/Minecraft;
/*      */     //   224: getfield 245	net/minecraft/client/Minecraft:thePlayer	Lnet/minecraft/client/entity/EntityPlayerSP;
/*      */     //   227: aload_2
/*      */     //   228: iconst_0
/*      */     //   229: invokespecial 2711	net/minecraft/client/gui/GuiScreenBook:<init>	(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Z)V
/*      */     //   232: invokevirtual 259	net/minecraft/client/Minecraft:displayGuiScreen	(Lnet/minecraft/client/gui/GuiScreen;)V
/*      */     //   235: return
/*      */     // Line number table:
/*      */     //   Java source line #1858	-> byte code offset #0
/*      */     //   Java source line #1860	-> byte code offset #9
/*      */     //   Java source line #1862	-> byte code offset #22
/*      */     //   Java source line #1866	-> byte code offset #27
/*      */     //   Java source line #1867	-> byte code offset #32
/*      */     //   Java source line #1869	-> byte code offset #41
/*      */     //   Java source line #1871	-> byte code offset #71
/*      */     //   Java source line #1872	-> byte code offset #81
/*      */     //   Java source line #1873	-> byte code offset #87
/*      */     //   Java source line #1875	-> byte code offset #96
/*      */     //   Java source line #1876	-> byte code offset #99
/*      */     //   Java source line #1878	-> byte code offset #100
/*      */     //   Java source line #1882	-> byte code offset #112
/*      */     //   Java source line #1881	-> byte code offset #120
/*      */     //   Java source line #1882	-> byte code offset #122
/*      */     //   Java source line #1883	-> byte code offset #127
/*      */     //   Java source line #1882	-> byte code offset #130
/*      */     //   Java source line #1884	-> byte code offset #135
/*      */     //   Java source line #1885	-> byte code offset #138
/*      */     //   Java source line #1887	-> byte code offset #151
/*      */     //   Java source line #1888	-> byte code offset #171
/*      */     //   Java source line #1889	-> byte code offset #174
/*      */     //   Java source line #1891	-> byte code offset #187
/*      */     //   Java source line #1893	-> byte code offset #198
/*      */     //   Java source line #1895	-> byte code offset #212
/*      */     //   Java source line #1898	-> byte code offset #235
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	236	0	this	NetHandlerPlayClient
/*      */     //   0	236	1	packetIn	net.minecraft.network.play.server.S3FPacketCustomPayload
/*      */     //   26	105	2	packetbuffer	PacketBuffer
/*      */     //   197	31	2	itemstack	ItemStack
/*      */     //   31	24	3	i	int
/*      */     //   99	8	3	ioexception	java.io.IOException
/*      */     //   39	33	4	guiscreen	GuiScreen
/*      */     //   79	9	5	imerchant	net.minecraft.entity.IMerchant
/*      */     //   85	5	6	merchantrecipelist	net.minecraft.village.MerchantRecipeList
/*      */     //   120	8	7	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   27	96	99	java/io/IOException
/*      */     //   27	112	120	finally
/*      */   }
/*      */   
/*      */   public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn)
/*      */   {
/* 1905 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1906 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/*      */     
/* 1908 */     if (packetIn.func_149338_e() == 0)
/*      */     {
/* 1910 */       ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.func_149339_c(), net.minecraft.scoreboard.IScoreObjectiveCriteria.DUMMY);
/* 1911 */       scoreobjective.setDisplayName(packetIn.func_149337_d());
/* 1912 */       scoreobjective.setRenderType(packetIn.func_179817_d());
/*      */     }
/*      */     else
/*      */     {
/* 1916 */       ScoreObjective scoreobjective1 = scoreboard.getObjective(packetIn.func_149339_c());
/*      */       
/* 1918 */       if (packetIn.func_149338_e() == 1)
/*      */       {
/* 1920 */         scoreboard.removeObjective(scoreobjective1);
/*      */       }
/* 1922 */       else if (packetIn.func_149338_e() == 2)
/*      */       {
/* 1924 */         scoreobjective1.setDisplayName(packetIn.func_149337_d());
/* 1925 */         scoreobjective1.setRenderType(packetIn.func_179817_d());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleUpdateScore(S3CPacketUpdateScore packetIn)
/*      */   {
/* 1935 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1936 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/* 1937 */     ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
/*      */     
/* 1939 */     if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE)
/*      */     {
/* 1941 */       Score score = scoreboard.getValueFromObjective(packetIn.getPlayerName(), scoreobjective);
/* 1942 */       score.setScorePoints(packetIn.getScoreValue());
/*      */     }
/* 1944 */     else if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE)
/*      */     {
/* 1946 */       if (net.minecraft.util.StringUtils.isNullOrEmpty(packetIn.getObjectiveName()))
/*      */       {
/* 1948 */         scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), null);
/*      */       }
/* 1950 */       else if (scoreobjective != null)
/*      */       {
/* 1952 */         scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn)
/*      */   {
/* 1963 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1964 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/*      */     
/* 1966 */     if (packetIn.func_149370_d().length() == 0)
/*      */     {
/* 1968 */       scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), null);
/*      */     }
/*      */     else
/*      */     {
/* 1972 */       ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149370_d());
/* 1973 */       scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleTeams(S3EPacketTeams packetIn)
/*      */   {
/* 1983 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 1984 */     Scoreboard scoreboard = this.clientWorldController.getScoreboard();
/*      */     ScorePlayerTeam scoreplayerteam;
/*      */     ScorePlayerTeam scoreplayerteam;
/* 1987 */     if (packetIn.func_149307_h() == 0)
/*      */     {
/* 1989 */       scoreplayerteam = scoreboard.createTeam(packetIn.func_149312_c());
/*      */     }
/*      */     else
/*      */     {
/* 1993 */       scoreplayerteam = scoreboard.getTeam(packetIn.func_149312_c());
/*      */     }
/*      */     
/* 1996 */     if ((packetIn.func_149307_h() == 0) || (packetIn.func_149307_h() == 2))
/*      */     {
/* 1998 */       scoreplayerteam.setTeamName(packetIn.func_149306_d());
/* 1999 */       scoreplayerteam.setNamePrefix(packetIn.func_149311_e());
/* 2000 */       scoreplayerteam.setNameSuffix(packetIn.func_149309_f());
/* 2001 */       scoreplayerteam.setChatFormat(EnumChatFormatting.func_175744_a(packetIn.func_179813_h()));
/* 2002 */       scoreplayerteam.func_98298_a(packetIn.func_149308_i());
/* 2003 */       Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());
/*      */       
/* 2005 */       if (team$enumvisible != null)
/*      */       {
/* 2007 */         scoreplayerteam.setNameTagVisibility(team$enumvisible);
/*      */       }
/*      */     }
/*      */     
/* 2011 */     if ((packetIn.func_149307_h() == 0) || (packetIn.func_149307_h() == 3))
/*      */     {
/* 2013 */       for (String s : packetIn.func_149310_g())
/*      */       {
/* 2015 */         scoreboard.addPlayerToTeam(s, packetIn.func_149312_c());
/*      */       }
/*      */     }
/*      */     
/* 2019 */     if (packetIn.func_149307_h() == 4)
/*      */     {
/* 2021 */       for (String s1 : packetIn.func_149310_g())
/*      */       {
/* 2023 */         scoreboard.removePlayerFromTeam(s1, scoreplayerteam);
/*      */       }
/*      */     }
/*      */     
/* 2027 */     if (packetIn.func_149307_h() == 1)
/*      */     {
/* 2029 */       scoreboard.removeTeam(scoreplayerteam);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleParticles(S2APacketParticles packetIn)
/*      */   {
/* 2039 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/*      */     
/* 2041 */     if (packetIn.getParticleCount() == 0)
/*      */     {
/* 2043 */       double d0 = packetIn.getParticleSpeed() * packetIn.getXOffset();
/* 2044 */       double d2 = packetIn.getParticleSpeed() * packetIn.getYOffset();
/* 2045 */       double d4 = packetIn.getParticleSpeed() * packetIn.getZOffset();
/*      */       
/*      */       try
/*      */       {
/* 2049 */         this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
/*      */       }
/*      */       catch (Throwable var17)
/*      */       {
/* 2053 */         logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2058 */       for (int i = 0; i < packetIn.getParticleCount(); i++)
/*      */       {
/* 2060 */         double d1 = this.avRandomizer.nextGaussian() * packetIn.getXOffset();
/* 2061 */         double d3 = this.avRandomizer.nextGaussian() * packetIn.getYOffset();
/* 2062 */         double d5 = this.avRandomizer.nextGaussian() * packetIn.getZOffset();
/* 2063 */         double d6 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
/* 2064 */         double d7 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
/* 2065 */         double d8 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
/*      */         
/*      */         try
/*      */         {
/* 2069 */           this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
/*      */         }
/*      */         catch (Throwable var16)
/*      */         {
/* 2073 */           logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
/* 2074 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleEntityProperties(S20PacketEntityProperties packetIn)
/*      */   {
/* 2087 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
/* 2088 */     Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
/*      */     
/* 2090 */     if (entity != null)
/*      */     {
/* 2092 */       if (!(entity instanceof EntityLivingBase))
/*      */       {
/* 2094 */         throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
/*      */       }
/*      */       
/*      */ 
/* 2098 */       BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();
/*      */       Iterator localIterator2;
/* 2100 */       for (Iterator localIterator1 = packetIn.func_149441_d().iterator(); localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2112 */           localIterator2.hasNext())
/*      */       {
/* 2100 */         S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot = (S20PacketEntityProperties.Snapshot)localIterator1.next();
/*      */         
/* 2102 */         IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());
/*      */         
/* 2104 */         if (iattributeinstance == null)
/*      */         {
/* 2106 */           iattributeinstance = baseattributemap.registerAttribute(new net.minecraft.entity.ai.attributes.RangedAttribute(null, s20packetentityproperties$snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE));
/*      */         }
/*      */         
/* 2109 */         iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
/* 2110 */         iattributeinstance.removeAllModifiers();
/*      */         
/* 2112 */         localIterator2 = s20packetentityproperties$snapshot.func_151408_c().iterator(); continue;AttributeModifier attributemodifier = (AttributeModifier)localIterator2.next();
/*      */         
/* 2114 */         iattributeinstance.applyModifier(attributemodifier);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public NetworkManager getNetworkManager()
/*      */   {
/* 2126 */     return this.netManager;
/*      */   }
/*      */   
/*      */   public Collection<NetworkPlayerInfo> getPlayerInfoMap()
/*      */   {
/* 2131 */     return this.playerInfoMap.values();
/*      */   }
/*      */   
/*      */   public NetworkPlayerInfo getPlayerInfo(UUID p_175102_1_)
/*      */   {
/* 2136 */     return (NetworkPlayerInfo)this.playerInfoMap.get(p_175102_1_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public NetworkPlayerInfo getPlayerInfo(String p_175104_1_)
/*      */   {
/* 2144 */     for (NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values())
/*      */     {
/* 2146 */       if (networkplayerinfo.getGameProfile().getName().equals(p_175104_1_))
/*      */       {
/* 2148 */         return networkplayerinfo;
/*      */       }
/*      */     }
/*      */     
/* 2152 */     return null;
/*      */   }
/*      */   
/*      */   public GameProfile getGameProfile()
/*      */   {
/* 2157 */     return this.profile;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\network\NetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */