/*      */ package net.minecraft.server.management;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import java.io.File;
/*      */ import java.net.SocketAddress;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityTracker;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.network.NetHandlerPlayServer;
/*      */ import net.minecraft.network.NetworkManager;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.PacketBuffer;
/*      */ import net.minecraft.network.play.server.S03PacketTimeUpdate;
/*      */ import net.minecraft.network.play.server.S05PacketSpawnPosition;
/*      */ import net.minecraft.network.play.server.S07PacketRespawn;
/*      */ import net.minecraft.network.play.server.S09PacketHeldItemChange;
/*      */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*      */ import net.minecraft.network.play.server.S1FPacketSetExperience;
/*      */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*      */ import net.minecraft.network.play.server.S38PacketPlayerListItem;
/*      */ import net.minecraft.network.play.server.S38PacketPlayerListItem.Action;
/*      */ import net.minecraft.network.play.server.S41PacketServerDifficulty;
/*      */ import net.minecraft.network.play.server.S44PacketWorldBorder;
/*      */ import net.minecraft.network.play.server.S44PacketWorldBorder.Action;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.ServerScoreboard;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.stats.StatisticsFile;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.GameRules;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.WorldSettings.GameType;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.demo.DemoWorldManager;
/*      */ import net.minecraft.world.gen.ChunkProviderServer;
/*      */ import net.minecraft.world.storage.IPlayerFileData;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public abstract class ServerConfigurationManager
/*      */ {
/*   65 */   public static final File FILE_PLAYERBANS = new File("banned-players.json");
/*   66 */   public static final File FILE_IPBANS = new File("banned-ips.json");
/*   67 */   public static final File FILE_OPS = new File("ops.json");
/*   68 */   public static final File FILE_WHITELIST = new File("whitelist.json");
/*   69 */   private static final Logger logger = LogManager.getLogger();
/*   70 */   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
/*      */   
/*      */   private final MinecraftServer mcServer;
/*      */   
/*   74 */   private final List<EntityPlayerMP> playerEntityList = Lists.newArrayList();
/*   75 */   private final Map<UUID, EntityPlayerMP> uuidToPlayerMap = Maps.newHashMap();
/*      */   
/*      */ 
/*      */   private final UserListBans bannedPlayers;
/*      */   
/*      */ 
/*      */   private final BanList bannedIPs;
/*      */   
/*      */ 
/*      */   private final UserListOps ops;
/*      */   
/*      */ 
/*      */   private final UserListWhitelist whiteListedPlayers;
/*      */   
/*      */ 
/*      */   private final Map<UUID, StatisticsFile> playerStatFiles;
/*      */   
/*      */   private IPlayerFileData playerNBTManagerObj;
/*      */   
/*      */   private boolean whiteListEnforced;
/*      */   
/*      */   protected int maxPlayers;
/*      */   
/*      */   private int viewDistance;
/*      */   
/*      */   private WorldSettings.GameType gameType;
/*      */   
/*      */   private boolean commandsAllowedForAll;
/*      */   
/*      */   private int playerPingIndex;
/*      */   
/*      */ 
/*      */   public ServerConfigurationManager(MinecraftServer server)
/*      */   {
/*  109 */     this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
/*  110 */     this.bannedIPs = new BanList(FILE_IPBANS);
/*  111 */     this.ops = new UserListOps(FILE_OPS);
/*  112 */     this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
/*  113 */     this.playerStatFiles = Maps.newHashMap();
/*  114 */     this.mcServer = server;
/*  115 */     this.bannedPlayers.setLanServer(false);
/*  116 */     this.bannedIPs.setLanServer(false);
/*  117 */     this.maxPlayers = 8;
/*      */   }
/*      */   
/*      */   public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn)
/*      */   {
/*  122 */     GameProfile gameprofile = playerIn.getGameProfile();
/*  123 */     PlayerProfileCache playerprofilecache = this.mcServer.getPlayerProfileCache();
/*  124 */     GameProfile gameprofile1 = playerprofilecache.getProfileByUUID(gameprofile.getId());
/*  125 */     String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();
/*  126 */     playerprofilecache.addEntry(gameprofile);
/*  127 */     NBTTagCompound nbttagcompound = readPlayerDataFromFile(playerIn);
/*  128 */     playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
/*  129 */     playerIn.theItemInWorldManager.setWorld((WorldServer)playerIn.worldObj);
/*  130 */     String s1 = "local";
/*      */     
/*  132 */     if (netManager.getRemoteAddress() != null)
/*      */     {
/*  134 */       s1 = netManager.getRemoteAddress().toString();
/*      */     }
/*      */     
/*  137 */     logger.info(playerIn.getName() + "[" + s1 + "] logged in with entity id " + playerIn.getEntityId() + " at (" + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
/*  138 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/*  139 */     WorldInfo worldinfo = worldserver.getWorldInfo();
/*  140 */     BlockPos blockpos = worldserver.getSpawnPoint();
/*  141 */     setPlayerGameTypeBasedOnOther(playerIn, null, worldserver);
/*  142 */     NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);
/*  143 */     nethandlerplayserver.sendPacket(new net.minecraft.network.play.server.S01PacketJoinGame(playerIn.getEntityId(), playerIn.theItemInWorldManager.getGameType(), worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimensionId(), worldserver.getDifficulty(), getMaxPlayers(), worldinfo.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
/*  144 */     nethandlerplayserver.sendPacket(new net.minecraft.network.play.server.S3FPacketCustomPayload("MC|Brand", new PacketBuffer(io.netty.buffer.Unpooled.buffer()).writeString(getServerInstance().getServerModName())));
/*  145 */     nethandlerplayserver.sendPacket(new S41PacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
/*  146 */     nethandlerplayserver.sendPacket(new S05PacketSpawnPosition(blockpos));
/*  147 */     nethandlerplayserver.sendPacket(new net.minecraft.network.play.server.S39PacketPlayerAbilities(playerIn.capabilities));
/*  148 */     nethandlerplayserver.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
/*  149 */     playerIn.getStatFile().func_150877_d();
/*  150 */     playerIn.getStatFile().sendAchievements(playerIn);
/*  151 */     sendScoreboard((ServerScoreboard)worldserver.getScoreboard(), playerIn);
/*  152 */     this.mcServer.refreshStatusNextTick();
/*      */     ChatComponentTranslation chatcomponenttranslation;
/*      */     ChatComponentTranslation chatcomponenttranslation;
/*  155 */     if (!playerIn.getName().equalsIgnoreCase(s))
/*      */     {
/*  157 */       chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[] { playerIn.getDisplayName(), s });
/*      */     }
/*      */     else
/*      */     {
/*  161 */       chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined", new Object[] { playerIn.getDisplayName() });
/*      */     }
/*      */     
/*  164 */     chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
/*  165 */     sendChatMsg(chatcomponenttranslation);
/*  166 */     playerLoggedIn(playerIn);
/*  167 */     nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
/*  168 */     updateTimeAndWeatherForPlayer(playerIn, worldserver);
/*      */     
/*  170 */     if (this.mcServer.getResourcePackUrl().length() > 0)
/*      */     {
/*  172 */       playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
/*      */     }
/*      */     
/*  175 */     for (PotionEffect potioneffect : playerIn.getActivePotionEffects())
/*      */     {
/*  177 */       nethandlerplayserver.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
/*      */     }
/*      */     
/*  180 */     playerIn.addSelfToInternalCraftingInventory();
/*      */     
/*  182 */     if ((nbttagcompound != null) && (nbttagcompound.hasKey("Riding", 10)))
/*      */     {
/*  184 */       Entity entity = net.minecraft.entity.EntityList.createEntityFromNBT(nbttagcompound.getCompoundTag("Riding"), worldserver);
/*      */       
/*  186 */       if (entity != null)
/*      */       {
/*  188 */         entity.forceSpawn = true;
/*  189 */         worldserver.spawnEntityInWorld(entity);
/*  190 */         playerIn.mountEntity(entity);
/*  191 */         entity.forceSpawn = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void sendScoreboard(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn)
/*      */   {
/*  198 */     Set<ScoreObjective> set = com.google.common.collect.Sets.newHashSet();
/*      */     
/*  200 */     for (ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams())
/*      */     {
/*  202 */       playerIn.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S3EPacketTeams(scoreplayerteam, 0));
/*      */     }
/*      */     
/*  205 */     for (int i = 0; i < 19; i++)
/*      */     {
/*  207 */       ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);
/*      */       
/*  209 */       if ((scoreobjective != null) && (!set.contains(scoreobjective)))
/*      */       {
/*  211 */         for (Packet packet : scoreboardIn.func_96550_d(scoreobjective))
/*      */         {
/*  213 */           playerIn.playerNetServerHandler.sendPacket(packet);
/*      */         }
/*      */         
/*  216 */         set.add(scoreobjective);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPlayerManager(WorldServer[] worldServers)
/*      */   {
/*  226 */     this.playerNBTManagerObj = worldServers[0].getSaveHandler().getPlayerNBTManager();
/*  227 */     worldServers[0].getWorldBorder().addListener(new net.minecraft.world.border.IBorderListener()
/*      */     {
/*      */       public void onSizeChanged(WorldBorder border, double newSize)
/*      */       {
/*  231 */         ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_SIZE));
/*      */       }
/*      */       
/*      */       public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time) {
/*  235 */         ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.LERP_SIZE));
/*      */       }
/*      */       
/*      */       public void onCenterChanged(WorldBorder border, double x, double z) {
/*  239 */         ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_CENTER));
/*      */       }
/*      */       
/*      */       public void onWarningTimeChanged(WorldBorder border, int newTime) {
/*  243 */         ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_TIME));
/*      */       }
/*      */       
/*      */       public void onWarningDistanceChanged(WorldBorder border, int newDistance) {
/*  247 */         ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
/*      */       }
/*      */       
/*      */ 
/*      */       public void onDamageAmountChanged(WorldBorder border, double newAmount) {}
/*      */       
/*      */ 
/*      */       public void onDamageBufferChanged(WorldBorder border, double newSize) {}
/*      */     });
/*      */   }
/*      */   
/*      */   public void preparePlayer(EntityPlayerMP playerIn, WorldServer worldIn)
/*      */   {
/*  260 */     WorldServer worldserver = playerIn.getServerForPlayer();
/*      */     
/*  262 */     if (worldIn != null)
/*      */     {
/*  264 */       worldIn.getPlayerManager().removePlayer(playerIn);
/*      */     }
/*      */     
/*  267 */     worldserver.getPlayerManager().addPlayer(playerIn);
/*  268 */     worldserver.theChunkProviderServer.loadChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
/*      */   }
/*      */   
/*      */   public int getEntityViewDistance()
/*      */   {
/*  273 */     return PlayerManager.getFurthestViewableBlock(getViewDistance());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn)
/*      */   {
/*  281 */     NBTTagCompound nbttagcompound = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
/*      */     
/*      */     NBTTagCompound nbttagcompound1;
/*  284 */     if ((playerIn.getName().equals(this.mcServer.getServerOwner())) && (nbttagcompound != null))
/*      */     {
/*  286 */       playerIn.readFromNBT(nbttagcompound);
/*  287 */       NBTTagCompound nbttagcompound1 = nbttagcompound;
/*  288 */       logger.debug("loading single player");
/*      */     }
/*      */     else
/*      */     {
/*  292 */       nbttagcompound1 = this.playerNBTManagerObj.readPlayerData(playerIn);
/*      */     }
/*      */     
/*  295 */     return nbttagcompound1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void writePlayerData(EntityPlayerMP playerIn)
/*      */   {
/*  303 */     this.playerNBTManagerObj.writePlayerData(playerIn);
/*  304 */     StatisticsFile statisticsfile = (StatisticsFile)this.playerStatFiles.get(playerIn.getUniqueID());
/*      */     
/*  306 */     if (statisticsfile != null)
/*      */     {
/*  308 */       statisticsfile.saveStatFile();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playerLoggedIn(EntityPlayerMP playerIn)
/*      */   {
/*  317 */     this.playerEntityList.add(playerIn);
/*  318 */     this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
/*  319 */     sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { playerIn }));
/*  320 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/*  321 */     worldserver.spawnEntityInWorld(playerIn);
/*  322 */     preparePlayer(playerIn, null);
/*      */     
/*  324 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  326 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntityList.get(i);
/*  327 */       playerIn.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { entityplayermp }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void serverUpdateMountedMovingPlayer(EntityPlayerMP playerIn)
/*      */   {
/*  336 */     playerIn.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(playerIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playerLoggedOut(EntityPlayerMP playerIn)
/*      */   {
/*  344 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.leaveGameStat);
/*  345 */     writePlayerData(playerIn);
/*  346 */     WorldServer worldserver = playerIn.getServerForPlayer();
/*      */     
/*  348 */     if (playerIn.ridingEntity != null)
/*      */     {
/*  350 */       worldserver.removePlayerEntityDangerously(playerIn.ridingEntity);
/*  351 */       logger.debug("removing player mount");
/*      */     }
/*      */     
/*  354 */     worldserver.removeEntity(playerIn);
/*  355 */     worldserver.getPlayerManager().removePlayer(playerIn);
/*  356 */     this.playerEntityList.remove(playerIn);
/*  357 */     UUID uuid = playerIn.getUniqueID();
/*  358 */     EntityPlayerMP entityplayermp = (EntityPlayerMP)this.uuidToPlayerMap.get(uuid);
/*      */     
/*  360 */     if (entityplayermp == playerIn)
/*      */     {
/*  362 */       this.uuidToPlayerMap.remove(uuid);
/*  363 */       this.playerStatFiles.remove(uuid);
/*      */     }
/*      */     
/*  366 */     sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[] { playerIn }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String allowUserToConnect(SocketAddress address, GameProfile profile)
/*      */   {
/*  374 */     if (this.bannedPlayers.isBanned(profile))
/*      */     {
/*  376 */       UserListBansEntry userlistbansentry = (UserListBansEntry)this.bannedPlayers.getEntry(profile);
/*  377 */       String s1 = "You are banned from this server!\nReason: " + userlistbansentry.getBanReason();
/*      */       
/*  379 */       if (userlistbansentry.getBanEndDate() != null)
/*      */       {
/*  381 */         s1 = s1 + "\nYour ban will be removed on " + dateFormat.format(userlistbansentry.getBanEndDate());
/*      */       }
/*      */       
/*  384 */       return s1;
/*      */     }
/*  386 */     if (!canJoin(profile))
/*      */     {
/*  388 */       return "You are not white-listed on this server!";
/*      */     }
/*  390 */     if (this.bannedIPs.isBanned(address))
/*      */     {
/*  392 */       IPBanEntry ipbanentry = this.bannedIPs.getBanEntry(address);
/*  393 */       String s = "Your IP address is banned from this server!\nReason: " + ipbanentry.getBanReason();
/*      */       
/*  395 */       if (ipbanentry.getBanEndDate() != null)
/*      */       {
/*  397 */         s = s + "\nYour ban will be removed on " + dateFormat.format(ipbanentry.getBanEndDate());
/*      */       }
/*      */       
/*  400 */       return s;
/*      */     }
/*      */     
/*      */ 
/*  404 */     return (this.playerEntityList.size() >= this.maxPlayers) && (!func_183023_f(profile)) ? "The server is full!" : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityPlayerMP createPlayerForUser(GameProfile profile)
/*      */   {
/*  413 */     UUID uuid = EntityPlayer.getUUID(profile);
/*  414 */     List<EntityPlayerMP> list = Lists.newArrayList();
/*      */     
/*  416 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  418 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntityList.get(i);
/*      */       
/*  420 */       if (entityplayermp.getUniqueID().equals(uuid))
/*      */       {
/*  422 */         list.add(entityplayermp);
/*      */       }
/*      */     }
/*      */     
/*  426 */     EntityPlayerMP entityplayermp2 = (EntityPlayerMP)this.uuidToPlayerMap.get(profile.getId());
/*      */     
/*  428 */     if ((entityplayermp2 != null) && (!list.contains(entityplayermp2)))
/*      */     {
/*  430 */       list.add(entityplayermp2);
/*      */     }
/*      */     
/*  433 */     for (EntityPlayerMP entityplayermp1 : list)
/*      */     {
/*  435 */       entityplayermp1.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
/*      */     }
/*      */     
/*      */     ItemInWorldManager iteminworldmanager;
/*      */     ItemInWorldManager iteminworldmanager;
/*  440 */     if (this.mcServer.isDemo())
/*      */     {
/*  442 */       iteminworldmanager = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
/*      */     }
/*      */     else
/*      */     {
/*  446 */       iteminworldmanager = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
/*      */     }
/*      */     
/*  449 */     return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, iteminworldmanager);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd)
/*      */   {
/*  457 */     playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
/*  458 */     playerIn.getServerForPlayer().getEntityTracker().untrackEntity(playerIn);
/*  459 */     playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
/*  460 */     this.playerEntityList.remove(playerIn);
/*  461 */     this.mcServer.worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously(playerIn);
/*  462 */     BlockPos blockpos = playerIn.getBedLocation();
/*  463 */     boolean flag = playerIn.isSpawnForced();
/*  464 */     playerIn.dimension = dimension;
/*      */     ItemInWorldManager iteminworldmanager;
/*      */     ItemInWorldManager iteminworldmanager;
/*  467 */     if (this.mcServer.isDemo())
/*      */     {
/*  469 */       iteminworldmanager = new DemoWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
/*      */     }
/*      */     else
/*      */     {
/*  473 */       iteminworldmanager = new ItemInWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
/*      */     }
/*      */     
/*  476 */     EntityPlayerMP entityplayermp = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(playerIn.dimension), playerIn.getGameProfile(), iteminworldmanager);
/*  477 */     entityplayermp.playerNetServerHandler = playerIn.playerNetServerHandler;
/*  478 */     entityplayermp.clonePlayer(playerIn, conqueredEnd);
/*  479 */     entityplayermp.setEntityId(playerIn.getEntityId());
/*  480 */     entityplayermp.func_174817_o(playerIn);
/*  481 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/*  482 */     setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, worldserver);
/*      */     
/*  484 */     if (blockpos != null)
/*      */     {
/*  486 */       BlockPos blockpos1 = EntityPlayer.getBedSpawnLocation(this.mcServer.worldServerForDimension(playerIn.dimension), blockpos, flag);
/*      */       
/*  488 */       if (blockpos1 != null)
/*      */       {
/*  490 */         entityplayermp.setLocationAndAngles(blockpos1.getX() + 0.5F, blockpos1.getY() + 0.1F, blockpos1.getZ() + 0.5F, 0.0F, 0.0F);
/*  491 */         entityplayermp.setSpawnPoint(blockpos, flag);
/*      */       }
/*      */       else
/*      */       {
/*  495 */         entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0F));
/*      */       }
/*      */     }
/*      */     
/*  499 */     worldserver.theChunkProviderServer.loadChunk((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);
/*      */     
/*  501 */     while ((!worldserver.getCollidingBoundingBoxes(entityplayermp, entityplayermp.getEntityBoundingBox()).isEmpty()) && (entityplayermp.posY < 256.0D))
/*      */     {
/*  503 */       entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0D, entityplayermp.posZ);
/*      */     }
/*      */     
/*  506 */     entityplayermp.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityplayermp.dimension, entityplayermp.worldObj.getDifficulty(), entityplayermp.worldObj.getWorldInfo().getTerrainType(), entityplayermp.theItemInWorldManager.getGameType()));
/*  507 */     BlockPos blockpos2 = worldserver.getSpawnPoint();
/*  508 */     entityplayermp.playerNetServerHandler.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
/*  509 */     entityplayermp.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(blockpos2));
/*  510 */     entityplayermp.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(entityplayermp.experience, entityplayermp.experienceTotal, entityplayermp.experienceLevel));
/*  511 */     updateTimeAndWeatherForPlayer(entityplayermp, worldserver);
/*  512 */     worldserver.getPlayerManager().addPlayer(entityplayermp);
/*  513 */     worldserver.spawnEntityInWorld(entityplayermp);
/*  514 */     this.playerEntityList.add(entityplayermp);
/*  515 */     this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
/*  516 */     entityplayermp.addSelfToInternalCraftingInventory();
/*  517 */     entityplayermp.setHealth(entityplayermp.getHealth());
/*  518 */     return entityplayermp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void transferPlayerToDimension(EntityPlayerMP playerIn, int dimension)
/*      */   {
/*  526 */     int i = playerIn.dimension;
/*  527 */     WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
/*  528 */     playerIn.dimension = dimension;
/*  529 */     WorldServer worldserver1 = this.mcServer.worldServerForDimension(playerIn.dimension);
/*  530 */     playerIn.playerNetServerHandler.sendPacket(new S07PacketRespawn(playerIn.dimension, playerIn.worldObj.getDifficulty(), playerIn.worldObj.getWorldInfo().getTerrainType(), playerIn.theItemInWorldManager.getGameType()));
/*  531 */     worldserver.removePlayerEntityDangerously(playerIn);
/*  532 */     playerIn.isDead = false;
/*  533 */     transferEntityToWorld(playerIn, i, worldserver, worldserver1);
/*  534 */     preparePlayer(playerIn, worldserver);
/*  535 */     playerIn.playerNetServerHandler.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
/*  536 */     playerIn.theItemInWorldManager.setWorld(worldserver1);
/*  537 */     updateTimeAndWeatherForPlayer(playerIn, worldserver1);
/*  538 */     syncPlayerInventory(playerIn);
/*      */     
/*  540 */     for (PotionEffect potioneffect : playerIn.getActivePotionEffects())
/*      */     {
/*  542 */       playerIn.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void transferEntityToWorld(Entity entityIn, int p_82448_2_, WorldServer p_82448_3_, WorldServer p_82448_4_)
/*      */   {
/*  551 */     double d0 = entityIn.posX;
/*  552 */     double d1 = entityIn.posZ;
/*  553 */     double d2 = 8.0D;
/*  554 */     float f = entityIn.rotationYaw;
/*  555 */     p_82448_3_.theProfiler.startSection("moving");
/*      */     
/*  557 */     if (entityIn.dimension == -1)
/*      */     {
/*  559 */       d0 = MathHelper.clamp_double(d0 / d2, p_82448_4_.getWorldBorder().minX() + 16.0D, p_82448_4_.getWorldBorder().maxX() - 16.0D);
/*  560 */       d1 = MathHelper.clamp_double(d1 / d2, p_82448_4_.getWorldBorder().minZ() + 16.0D, p_82448_4_.getWorldBorder().maxZ() - 16.0D);
/*  561 */       entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
/*      */       
/*  563 */       if (entityIn.isEntityAlive())
/*      */       {
/*  565 */         p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
/*      */       }
/*      */     }
/*  568 */     else if (entityIn.dimension == 0)
/*      */     {
/*  570 */       d0 = MathHelper.clamp_double(d0 * d2, p_82448_4_.getWorldBorder().minX() + 16.0D, p_82448_4_.getWorldBorder().maxX() - 16.0D);
/*  571 */       d1 = MathHelper.clamp_double(d1 * d2, p_82448_4_.getWorldBorder().minZ() + 16.0D, p_82448_4_.getWorldBorder().maxZ() - 16.0D);
/*  572 */       entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
/*      */       
/*  574 */       if (entityIn.isEntityAlive())
/*      */       {
/*  576 */         p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       BlockPos blockpos;
/*      */       BlockPos blockpos;
/*  583 */       if (p_82448_2_ == 1)
/*      */       {
/*  585 */         blockpos = p_82448_4_.getSpawnPoint();
/*      */       }
/*      */       else
/*      */       {
/*  589 */         blockpos = p_82448_4_.getSpawnCoordinate();
/*      */       }
/*      */       
/*  592 */       d0 = blockpos.getX();
/*  593 */       entityIn.posY = blockpos.getY();
/*  594 */       d1 = blockpos.getZ();
/*  595 */       entityIn.setLocationAndAngles(d0, entityIn.posY, d1, 90.0F, 0.0F);
/*      */       
/*  597 */       if (entityIn.isEntityAlive())
/*      */       {
/*  599 */         p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
/*      */       }
/*      */     }
/*      */     
/*  603 */     p_82448_3_.theProfiler.endSection();
/*      */     
/*  605 */     if (p_82448_2_ != 1)
/*      */     {
/*  607 */       p_82448_3_.theProfiler.startSection("placing");
/*  608 */       d0 = MathHelper.clamp_int((int)d0, -29999872, 29999872);
/*  609 */       d1 = MathHelper.clamp_int((int)d1, -29999872, 29999872);
/*      */       
/*  611 */       if (entityIn.isEntityAlive())
/*      */       {
/*  613 */         entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
/*  614 */         p_82448_4_.getDefaultTeleporter().placeInPortal(entityIn, f);
/*  615 */         p_82448_4_.spawnEntityInWorld(entityIn);
/*  616 */         p_82448_4_.updateEntityWithOptionalForce(entityIn, false);
/*      */       }
/*      */       
/*  619 */       p_82448_3_.theProfiler.endSection();
/*      */     }
/*      */     
/*  622 */     entityIn.setWorld(p_82448_4_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onTick()
/*      */   {
/*  630 */     if (++this.playerPingIndex > 600)
/*      */     {
/*  632 */       sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
/*  633 */       this.playerPingIndex = 0;
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendPacketToAllPlayers(Packet packetIn)
/*      */   {
/*  639 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  641 */       ((EntityPlayerMP)this.playerEntityList.get(i)).playerNetServerHandler.sendPacket(packetIn);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendPacketToAllPlayersInDimension(Packet packetIn, int dimension)
/*      */   {
/*  647 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  649 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntityList.get(i);
/*      */       
/*  651 */       if (entityplayermp.dimension == dimension)
/*      */       {
/*  653 */         entityplayermp.playerNetServerHandler.sendPacket(packetIn);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendMessageToAllTeamMembers(EntityPlayer player, IChatComponent message)
/*      */   {
/*  660 */     Team team = player.getTeam();
/*      */     
/*  662 */     if (team != null)
/*      */     {
/*  664 */       for (String s : team.getMembershipCollection())
/*      */       {
/*  666 */         EntityPlayerMP entityplayermp = getPlayerByUsername(s);
/*      */         
/*  668 */         if ((entityplayermp != null) && (entityplayermp != player))
/*      */         {
/*  670 */           entityplayermp.addChatMessage(message);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendMessageToTeamOrEvryPlayer(EntityPlayer player, IChatComponent message)
/*      */   {
/*  678 */     Team team = player.getTeam();
/*      */     
/*  680 */     if (team == null)
/*      */     {
/*  682 */       sendChatMsg(message);
/*      */     }
/*      */     else
/*      */     {
/*  686 */       for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */       {
/*  688 */         EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntityList.get(i);
/*      */         
/*  690 */         if (entityplayermp.getTeam() != team)
/*      */         {
/*  692 */           entityplayermp.addChatMessage(message);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public String func_181058_b(boolean p_181058_1_)
/*      */   {
/*  700 */     String s = "";
/*  701 */     List<EntityPlayerMP> list = Lists.newArrayList(this.playerEntityList);
/*      */     
/*  703 */     for (int i = 0; i < list.size(); i++)
/*      */     {
/*  705 */       if (i > 0)
/*      */       {
/*  707 */         s = s + ", ";
/*      */       }
/*      */       
/*  710 */       s = s + ((EntityPlayerMP)list.get(i)).getName();
/*      */       
/*  712 */       if (p_181058_1_)
/*      */       {
/*  714 */         s = s + " (" + ((EntityPlayerMP)list.get(i)).getUniqueID().toString() + ")";
/*      */       }
/*      */     }
/*      */     
/*  718 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getAllUsernames()
/*      */   {
/*  726 */     String[] astring = new String[this.playerEntityList.size()];
/*      */     
/*  728 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  730 */       astring[i] = ((EntityPlayerMP)this.playerEntityList.get(i)).getName();
/*      */     }
/*      */     
/*  733 */     return astring;
/*      */   }
/*      */   
/*      */   public GameProfile[] getAllProfiles()
/*      */   {
/*  738 */     GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];
/*      */     
/*  740 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  742 */       agameprofile[i] = ((EntityPlayerMP)this.playerEntityList.get(i)).getGameProfile();
/*      */     }
/*      */     
/*  745 */     return agameprofile;
/*      */   }
/*      */   
/*      */   public UserListBans getBannedPlayers()
/*      */   {
/*  750 */     return this.bannedPlayers;
/*      */   }
/*      */   
/*      */   public BanList getBannedIPs()
/*      */   {
/*  755 */     return this.bannedIPs;
/*      */   }
/*      */   
/*      */   public void addOp(GameProfile profile)
/*      */   {
/*  760 */     this.ops.addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel(), this.ops.func_183026_b(profile)));
/*      */   }
/*      */   
/*      */   public void removeOp(GameProfile profile)
/*      */   {
/*  765 */     this.ops.removeEntry(profile);
/*      */   }
/*      */   
/*      */   public boolean canJoin(GameProfile profile)
/*      */   {
/*  770 */     return (!this.whiteListEnforced) || (this.ops.hasEntry(profile)) || (this.whiteListedPlayers.hasEntry(profile));
/*      */   }
/*      */   
/*      */   public boolean canSendCommands(GameProfile profile)
/*      */   {
/*  775 */     return (this.ops.hasEntry(profile)) || ((this.mcServer.isSinglePlayer()) && (this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed()) && (this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName()))) || (this.commandsAllowedForAll);
/*      */   }
/*      */   
/*      */   public EntityPlayerMP getPlayerByUsername(String username)
/*      */   {
/*  780 */     for (EntityPlayerMP entityplayermp : this.playerEntityList)
/*      */     {
/*  782 */       if (entityplayermp.getName().equalsIgnoreCase(username))
/*      */       {
/*  784 */         return entityplayermp;
/*      */       }
/*      */     }
/*      */     
/*  788 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendToAllNear(double x, double y, double z, double radius, int dimension, Packet packetIn)
/*      */   {
/*  796 */     sendToAllNearExcept(null, x, y, z, radius, dimension, packetIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendToAllNearExcept(EntityPlayer p_148543_1_, double x, double y, double z, double radius, int dimension, Packet p_148543_11_)
/*      */   {
/*  805 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  807 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntityList.get(i);
/*      */       
/*  809 */       if ((entityplayermp != p_148543_1_) && (entityplayermp.dimension == dimension))
/*      */       {
/*  811 */         double d0 = x - entityplayermp.posX;
/*  812 */         double d1 = y - entityplayermp.posY;
/*  813 */         double d2 = z - entityplayermp.posZ;
/*      */         
/*  815 */         if (d0 * d0 + d1 * d1 + d2 * d2 < radius * radius)
/*      */         {
/*  817 */           entityplayermp.playerNetServerHandler.sendPacket(p_148543_11_);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void saveAllPlayerData()
/*      */   {
/*  828 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  830 */       writePlayerData((EntityPlayerMP)this.playerEntityList.get(i));
/*      */     }
/*      */   }
/*      */   
/*      */   public void addWhitelistedPlayer(GameProfile profile)
/*      */   {
/*  836 */     this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(profile));
/*      */   }
/*      */   
/*      */   public void removePlayerFromWhitelist(GameProfile profile)
/*      */   {
/*  841 */     this.whiteListedPlayers.removeEntry(profile);
/*      */   }
/*      */   
/*      */   public UserListWhitelist getWhitelistedPlayers()
/*      */   {
/*  846 */     return this.whiteListedPlayers;
/*      */   }
/*      */   
/*      */   public String[] getWhitelistedPlayerNames()
/*      */   {
/*  851 */     return this.whiteListedPlayers.getKeys();
/*      */   }
/*      */   
/*      */   public UserListOps getOppedPlayers()
/*      */   {
/*  856 */     return this.ops;
/*      */   }
/*      */   
/*      */   public String[] getOppedPlayerNames()
/*      */   {
/*  861 */     return this.ops.getKeys();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void loadWhiteList() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn)
/*      */   {
/*  876 */     WorldBorder worldborder = this.mcServer.worldServers[0].getWorldBorder();
/*  877 */     playerIn.playerNetServerHandler.sendPacket(new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.INITIALIZE));
/*  878 */     playerIn.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
/*      */     
/*  880 */     if (worldIn.isRaining())
/*      */     {
/*  882 */       playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0F));
/*  883 */       playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0F)));
/*  884 */       playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, worldIn.getThunderStrength(1.0F)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void syncPlayerInventory(EntityPlayerMP playerIn)
/*      */   {
/*  893 */     playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
/*  894 */     playerIn.setPlayerHealthUpdated();
/*  895 */     playerIn.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCurrentPlayerCount()
/*      */   {
/*  903 */     return this.playerEntityList.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxPlayers()
/*      */   {
/*  911 */     return this.maxPlayers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getAvailablePlayerDat()
/*      */   {
/*  919 */     return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
/*      */   }
/*      */   
/*      */   public void setWhiteListEnabled(boolean whitelistEnabled)
/*      */   {
/*  924 */     this.whiteListEnforced = whitelistEnabled;
/*      */   }
/*      */   
/*      */   public List<EntityPlayerMP> getPlayersMatchingAddress(String address)
/*      */   {
/*  929 */     List<EntityPlayerMP> list = Lists.newArrayList();
/*      */     
/*  931 */     for (EntityPlayerMP entityplayermp : this.playerEntityList)
/*      */     {
/*  933 */       if (entityplayermp.getPlayerIP().equals(address))
/*      */       {
/*  935 */         list.add(entityplayermp);
/*      */       }
/*      */     }
/*      */     
/*  939 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getViewDistance()
/*      */   {
/*  947 */     return this.viewDistance;
/*      */   }
/*      */   
/*      */   public MinecraftServer getServerInstance()
/*      */   {
/*  952 */     return this.mcServer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public NBTTagCompound getHostPlayerData()
/*      */   {
/*  960 */     return null;
/*      */   }
/*      */   
/*      */   public void setGameType(WorldSettings.GameType p_152604_1_)
/*      */   {
/*  965 */     this.gameType = p_152604_1_;
/*      */   }
/*      */   
/*      */   private void setPlayerGameTypeBasedOnOther(EntityPlayerMP p_72381_1_, EntityPlayerMP p_72381_2_, World worldIn)
/*      */   {
/*  970 */     if (p_72381_2_ != null)
/*      */     {
/*  972 */       p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
/*      */     }
/*  974 */     else if (this.gameType != null)
/*      */     {
/*  976 */       p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
/*      */     }
/*      */     
/*  979 */     p_72381_1_.theItemInWorldManager.initializeGameType(worldIn.getWorldInfo().getGameType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCommandsAllowedForAll(boolean p_72387_1_)
/*      */   {
/*  987 */     this.commandsAllowedForAll = p_72387_1_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeAllPlayers()
/*      */   {
/*  995 */     for (int i = 0; i < this.playerEntityList.size(); i++)
/*      */     {
/*  997 */       ((EntityPlayerMP)this.playerEntityList.get(i)).playerNetServerHandler.kickPlayerFromServer("Server closed");
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendChatMsgImpl(IChatComponent component, boolean isChat)
/*      */   {
/* 1003 */     this.mcServer.addChatMessage(component);
/* 1004 */     byte b0 = (byte)(isChat ? 1 : 0);
/* 1005 */     sendPacketToAllPlayers(new net.minecraft.network.play.server.S02PacketChat(component, b0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendChatMsg(IChatComponent component)
/*      */   {
/* 1013 */     sendChatMsgImpl(component, true);
/*      */   }
/*      */   
/*      */   public StatisticsFile getPlayerStatsFile(EntityPlayer playerIn)
/*      */   {
/* 1018 */     UUID uuid = playerIn.getUniqueID();
/* 1019 */     StatisticsFile statisticsfile = uuid == null ? null : (StatisticsFile)this.playerStatFiles.get(uuid);
/*      */     
/* 1021 */     if (statisticsfile == null)
/*      */     {
/* 1023 */       File file1 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
/* 1024 */       File file2 = new File(file1, uuid.toString() + ".json");
/*      */       
/* 1026 */       if (!file2.exists())
/*      */       {
/* 1028 */         File file3 = new File(file1, playerIn.getName() + ".json");
/*      */         
/* 1030 */         if ((file3.exists()) && (file3.isFile()))
/*      */         {
/* 1032 */           file3.renameTo(file2);
/*      */         }
/*      */       }
/*      */       
/* 1036 */       statisticsfile = new StatisticsFile(this.mcServer, file2);
/* 1037 */       statisticsfile.readStatFile();
/* 1038 */       this.playerStatFiles.put(uuid, statisticsfile);
/*      */     }
/*      */     
/* 1041 */     return statisticsfile;
/*      */   }
/*      */   
/*      */   public void setViewDistance(int distance)
/*      */   {
/* 1046 */     this.viewDistance = distance;
/*      */     
/* 1048 */     if (this.mcServer.worldServers != null) {
/*      */       WorldServer[] arrayOfWorldServer;
/* 1050 */       int j = (arrayOfWorldServer = this.mcServer.worldServers).length; for (int i = 0; i < j; i++) { WorldServer worldserver = arrayOfWorldServer[i];
/*      */         
/* 1052 */         if (worldserver != null)
/*      */         {
/* 1054 */           worldserver.getPlayerManager().setPlayerViewRadius(distance);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public List<EntityPlayerMP> func_181057_v()
/*      */   {
/* 1062 */     return this.playerEntityList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityPlayerMP getPlayerByUUID(UUID playerUUID)
/*      */   {
/* 1070 */     return (EntityPlayerMP)this.uuidToPlayerMap.get(playerUUID);
/*      */   }
/*      */   
/*      */   public boolean func_183023_f(GameProfile p_183023_1_)
/*      */   {
/* 1075 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\ServerConfigurationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */