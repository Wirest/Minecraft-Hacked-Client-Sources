// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.MathHelper;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.world.demo.DemoWorldManager;
import java.net.SocketAddress;
import net.minecraft.stats.StatList;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.Set;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityList;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import net.minecraft.network.NetworkManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.stats.StatisticsFile;
import java.util.UUID;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.Logger;
import java.io.File;

public abstract class ServerConfigurationManager
{
    public static final File FILE_PLAYERBANS;
    public static final File FILE_IPBANS;
    public static final File FILE_OPS;
    public static final File FILE_WHITELIST;
    private static final Logger logger;
    private static final SimpleDateFormat dateFormat;
    private final MinecraftServer mcServer;
    private final List<EntityPlayerMP> playerEntityList;
    private final Map<UUID, EntityPlayerMP> uuidToPlayerMap;
    private final UserListBans bannedPlayers;
    private final BanList bannedIPs;
    private final UserListOps ops;
    private final UserListWhitelist whiteListedPlayers;
    private final Map<UUID, StatisticsFile> playerStatFiles;
    private IPlayerFileData playerNBTManagerObj;
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private WorldSettings.GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;
    
    static {
        FILE_PLAYERBANS = new File("banned-players.json");
        FILE_IPBANS = new File("banned-ips.json");
        FILE_OPS = new File("ops.json");
        FILE_WHITELIST = new File("whitelist.json");
        logger = LogManager.getLogger();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    }
    
    public ServerConfigurationManager(final MinecraftServer server) {
        this.playerEntityList = (List<EntityPlayerMP>)Lists.newArrayList();
        this.uuidToPlayerMap = (Map<UUID, EntityPlayerMP>)Maps.newHashMap();
        this.bannedPlayers = new UserListBans(ServerConfigurationManager.FILE_PLAYERBANS);
        this.bannedIPs = new BanList(ServerConfigurationManager.FILE_IPBANS);
        this.ops = new UserListOps(ServerConfigurationManager.FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(ServerConfigurationManager.FILE_WHITELIST);
        this.playerStatFiles = (Map<UUID, StatisticsFile>)Maps.newHashMap();
        this.mcServer = server;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }
    
    public void initializeConnectionToPlayer(final NetworkManager netManager, final EntityPlayerMP playerIn) {
        final GameProfile gameprofile = playerIn.getGameProfile();
        final PlayerProfileCache playerprofilecache = this.mcServer.getPlayerProfileCache();
        final GameProfile gameprofile2 = playerprofilecache.getProfileByUUID(gameprofile.getId());
        final String s = (gameprofile2 == null) ? gameprofile.getName() : gameprofile2.getName();
        playerprofilecache.addEntry(gameprofile);
        final NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
        playerIn.theItemInWorldManager.setWorld((WorldServer)playerIn.worldObj);
        String s2 = "local";
        if (netManager.getRemoteAddress() != null) {
            s2 = netManager.getRemoteAddress().toString();
        }
        ServerConfigurationManager.logger.info(String.valueOf(playerIn.getName()) + "[" + s2 + "] logged in with entity id " + playerIn.getEntityId() + " at (" + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
        final WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        final WorldInfo worldinfo = worldserver.getWorldInfo();
        final BlockPos blockpos = worldserver.getSpawnPoint();
        this.setPlayerGameTypeBasedOnOther(playerIn, null, worldserver);
        final NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);
        nethandlerplayserver.sendPacket(new S01PacketJoinGame(playerIn.getEntityId(), playerIn.theItemInWorldManager.getGameType(), worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimensionId(), worldserver.getDifficulty(), this.getMaxPlayers(), worldinfo.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
        nethandlerplayserver.sendPacket(new S3FPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(this.getServerInstance().getServerModName())));
        nethandlerplayserver.sendPacket(new S41PacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        nethandlerplayserver.sendPacket(new S05PacketSpawnPosition(blockpos));
        nethandlerplayserver.sendPacket(new S39PacketPlayerAbilities(playerIn.capabilities));
        nethandlerplayserver.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
        playerIn.getStatFile().func_150877_d();
        playerIn.getStatFile().sendAchievements(playerIn);
        this.sendScoreboard((ServerScoreboard)worldserver.getScoreboard(), playerIn);
        this.mcServer.refreshStatusNextTick();
        ChatComponentTranslation chatcomponenttranslation;
        if (!playerIn.getName().equalsIgnoreCase(s)) {
            chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[] { playerIn.getDisplayName(), s });
        }
        else {
            chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined", new Object[] { playerIn.getDisplayName() });
        }
        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.sendChatMsg(chatcomponenttranslation);
        this.playerLoggedIn(playerIn);
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver);
        if (this.mcServer.getResourcePackUrl().length() > 0) {
            playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }
        for (final PotionEffect potioneffect : playerIn.getActivePotionEffects()) {
            nethandlerplayserver.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }
        playerIn.addSelfToInternalCraftingInventory();
        if (nbttagcompound != null && nbttagcompound.hasKey("Riding", 10)) {
            final Entity entity = EntityList.createEntityFromNBT(nbttagcompound.getCompoundTag("Riding"), worldserver);
            if (entity != null) {
                entity.forceSpawn = true;
                worldserver.spawnEntityInWorld(entity);
                playerIn.mountEntity(entity);
                entity.forceSpawn = false;
            }
        }
    }
    
    protected void sendScoreboard(final ServerScoreboard scoreboardIn, final EntityPlayerMP playerIn) {
        final Set<ScoreObjective> set = (Set<ScoreObjective>)Sets.newHashSet();
        for (final ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams()) {
            playerIn.playerNetServerHandler.sendPacket(new S3EPacketTeams(scoreplayerteam, 0));
        }
        for (int i = 0; i < 19; ++i) {
            final ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);
            if (scoreobjective != null && !set.contains(scoreobjective)) {
                for (final Packet packet : scoreboardIn.func_96550_d(scoreobjective)) {
                    playerIn.playerNetServerHandler.sendPacket(packet);
                }
                set.add(scoreobjective);
            }
        }
    }
    
    public void setPlayerManager(final WorldServer[] worldServers) {
        this.playerNBTManagerObj = worldServers[0].getSaveHandler().getPlayerNBTManager();
        worldServers[0].getWorldBorder().addListener(new IBorderListener() {
            @Override
            public void onSizeChanged(final WorldBorder border, final double newSize) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_SIZE));
            }
            
            @Override
            public void onTransitionStarted(final WorldBorder border, final double oldSize, final double newSize, final long time) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.LERP_SIZE));
            }
            
            @Override
            public void onCenterChanged(final WorldBorder border, final double x, final double z) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_CENTER));
            }
            
            @Override
            public void onWarningTimeChanged(final WorldBorder border, final int newTime) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_TIME));
            }
            
            @Override
            public void onWarningDistanceChanged(final WorldBorder border, final int newDistance) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
            }
            
            @Override
            public void onDamageAmountChanged(final WorldBorder border, final double newAmount) {
            }
            
            @Override
            public void onDamageBufferChanged(final WorldBorder border, final double newSize) {
            }
        });
    }
    
    public void preparePlayer(final EntityPlayerMP playerIn, final WorldServer worldIn) {
        final WorldServer worldserver = playerIn.getServerForPlayer();
        if (worldIn != null) {
            worldIn.getPlayerManager().removePlayer(playerIn);
        }
        worldserver.getPlayerManager().addPlayer(playerIn);
        worldserver.theChunkProviderServer.loadChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
    }
    
    public int getEntityViewDistance() {
        return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
    }
    
    public NBTTagCompound readPlayerDataFromFile(final EntityPlayerMP playerIn) {
        final NBTTagCompound nbttagcompound = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound nbttagcompound2;
        if (playerIn.getName().equals(this.mcServer.getServerOwner()) && nbttagcompound != null) {
            playerIn.readFromNBT(nbttagcompound);
            nbttagcompound2 = nbttagcompound;
            ServerConfigurationManager.logger.debug("loading single player");
        }
        else {
            nbttagcompound2 = this.playerNBTManagerObj.readPlayerData(playerIn);
        }
        return nbttagcompound2;
    }
    
    protected void writePlayerData(final EntityPlayerMP playerIn) {
        this.playerNBTManagerObj.writePlayerData(playerIn);
        final StatisticsFile statisticsfile = this.playerStatFiles.get(playerIn.getUniqueID());
        if (statisticsfile != null) {
            statisticsfile.saveStatFile();
        }
    }
    
    public void playerLoggedIn(final EntityPlayerMP playerIn) {
        this.playerEntityList.add(playerIn);
        this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { playerIn }));
        final WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        worldserver.spawnEntityInWorld(playerIn);
        this.preparePlayer(playerIn, null);
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            playerIn.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { entityplayermp }));
        }
    }
    
    public void serverUpdateMountedMovingPlayer(final EntityPlayerMP playerIn) {
        playerIn.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(playerIn);
    }
    
    public void playerLoggedOut(final EntityPlayerMP playerIn) {
        playerIn.triggerAchievement(StatList.leaveGameStat);
        this.writePlayerData(playerIn);
        final WorldServer worldserver = playerIn.getServerForPlayer();
        if (playerIn.ridingEntity != null) {
            worldserver.removePlayerEntityDangerously(playerIn.ridingEntity);
            ServerConfigurationManager.logger.debug("removing player mount");
        }
        worldserver.removeEntity(playerIn);
        worldserver.getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        final UUID uuid = playerIn.getUniqueID();
        final EntityPlayerMP entityplayermp = this.uuidToPlayerMap.get(uuid);
        if (entityplayermp == playerIn) {
            this.uuidToPlayerMap.remove(uuid);
            this.playerStatFiles.remove(uuid);
        }
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[] { playerIn }));
    }
    
    public String allowUserToConnect(final SocketAddress address, final GameProfile profile) {
        if (this.bannedPlayers.isBanned(profile)) {
            final UserListBansEntry userlistbansentry = this.bannedPlayers.getEntry(profile);
            String s1 = "You are banned from this server!\nReason: " + userlistbansentry.getBanReason();
            if (userlistbansentry.getBanEndDate() != null) {
                s1 = String.valueOf(s1) + "\nYour ban will be removed on " + ServerConfigurationManager.dateFormat.format(userlistbansentry.getBanEndDate());
            }
            return s1;
        }
        if (!this.canJoin(profile)) {
            return "You are not white-listed on this server!";
        }
        if (this.bannedIPs.isBanned(address)) {
            final IPBanEntry ipbanentry = this.bannedIPs.getBanEntry(address);
            String s2 = "Your IP address is banned from this server!\nReason: " + ipbanentry.getBanReason();
            if (ipbanentry.getBanEndDate() != null) {
                s2 = String.valueOf(s2) + "\nYour ban will be removed on " + ServerConfigurationManager.dateFormat.format(ipbanentry.getBanEndDate());
            }
            return s2;
        }
        return (this.playerEntityList.size() >= this.maxPlayers && !this.func_183023_f(profile)) ? "The server is full!" : null;
    }
    
    public EntityPlayerMP createPlayerForUser(final GameProfile profile) {
        final UUID uuid = EntityPlayer.getUUID(profile);
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList();
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp.getUniqueID().equals(uuid)) {
                list.add(entityplayermp);
            }
        }
        final EntityPlayerMP entityplayermp2 = this.uuidToPlayerMap.get(profile.getId());
        if (entityplayermp2 != null && !list.contains(entityplayermp2)) {
            list.add(entityplayermp2);
        }
        for (final EntityPlayerMP entityplayermp3 : list) {
            entityplayermp3.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
        }
        ItemInWorldManager iteminworldmanager;
        if (this.mcServer.isDemo()) {
            iteminworldmanager = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
        }
        else {
            iteminworldmanager = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
        }
        return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, iteminworldmanager);
    }
    
    public EntityPlayerMP recreatePlayerEntity(final EntityPlayerMP playerIn, final int dimension, final boolean conqueredEnd) {
        playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
        playerIn.getServerForPlayer().getEntityTracker().untrackEntity(playerIn);
        playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.mcServer.worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously(playerIn);
        final BlockPos blockpos = playerIn.getBedLocation();
        final boolean flag = playerIn.isSpawnForced();
        playerIn.dimension = dimension;
        ItemInWorldManager iteminworldmanager;
        if (this.mcServer.isDemo()) {
            iteminworldmanager = new DemoWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
        }
        else {
            iteminworldmanager = new ItemInWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
        }
        final EntityPlayerMP entityplayermp = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(playerIn.dimension), playerIn.getGameProfile(), iteminworldmanager);
        entityplayermp.playerNetServerHandler = playerIn.playerNetServerHandler;
        entityplayermp.clonePlayer(playerIn, conqueredEnd);
        entityplayermp.setEntityId(playerIn.getEntityId());
        entityplayermp.func_174817_o(playerIn);
        final WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        this.setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, worldserver);
        if (blockpos != null) {
            final BlockPos blockpos2 = EntityPlayer.getBedSpawnLocation(this.mcServer.worldServerForDimension(playerIn.dimension), blockpos, flag);
            if (blockpos2 != null) {
                entityplayermp.setLocationAndAngles(blockpos2.getX() + 0.5f, blockpos2.getY() + 0.1f, blockpos2.getZ() + 0.5f, 0.0f, 0.0f);
                entityplayermp.setSpawnPoint(blockpos, flag);
            }
            else {
                entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0f));
            }
        }
        worldserver.theChunkProviderServer.loadChunk((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);
        while (!worldserver.getCollidingBoundingBoxes(entityplayermp, entityplayermp.getEntityBoundingBox()).isEmpty() && entityplayermp.posY < 256.0) {
            entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0, entityplayermp.posZ);
        }
        entityplayermp.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityplayermp.dimension, entityplayermp.worldObj.getDifficulty(), entityplayermp.worldObj.getWorldInfo().getTerrainType(), entityplayermp.theItemInWorldManager.getGameType()));
        final BlockPos blockpos3 = worldserver.getSpawnPoint();
        entityplayermp.playerNetServerHandler.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        entityplayermp.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(blockpos3));
        entityplayermp.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(entityplayermp.experience, entityplayermp.experienceTotal, entityplayermp.experienceLevel));
        this.updateTimeAndWeatherForPlayer(entityplayermp, worldserver);
        worldserver.getPlayerManager().addPlayer(entityplayermp);
        worldserver.spawnEntityInWorld(entityplayermp);
        this.playerEntityList.add(entityplayermp);
        this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
        entityplayermp.addSelfToInternalCraftingInventory();
        entityplayermp.setHealth(entityplayermp.getHealth());
        return entityplayermp;
    }
    
    public void transferPlayerToDimension(final EntityPlayerMP playerIn, final int dimension) {
        final int i = playerIn.dimension;
        final WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.dimension = dimension;
        final WorldServer worldserver2 = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.playerNetServerHandler.sendPacket(new S07PacketRespawn(playerIn.dimension, playerIn.worldObj.getDifficulty(), playerIn.worldObj.getWorldInfo().getTerrainType(), playerIn.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(playerIn);
        playerIn.isDead = false;
        this.transferEntityToWorld(playerIn, i, worldserver, worldserver2);
        this.preparePlayer(playerIn, worldserver);
        playerIn.playerNetServerHandler.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        playerIn.theItemInWorldManager.setWorld(worldserver2);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver2);
        this.syncPlayerInventory(playerIn);
        for (final PotionEffect potioneffect : playerIn.getActivePotionEffects()) {
            playerIn.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }
    }
    
    public void transferEntityToWorld(final Entity entityIn, final int p_82448_2_, final WorldServer p_82448_3_, final WorldServer p_82448_4_) {
        double d0 = entityIn.posX;
        double d2 = entityIn.posZ;
        final double d3 = 8.0;
        final float f = entityIn.rotationYaw;
        p_82448_3_.theProfiler.startSection("moving");
        if (entityIn.dimension == -1) {
            d0 = MathHelper.clamp_double(d0 / d3, p_82448_4_.getWorldBorder().minX() + 16.0, p_82448_4_.getWorldBorder().maxX() - 16.0);
            d2 = MathHelper.clamp_double(d2 / d3, p_82448_4_.getWorldBorder().minZ() + 16.0, p_82448_4_.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(d0, entityIn.posY, d2, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else if (entityIn.dimension == 0) {
            d0 = MathHelper.clamp_double(d0 * d3, p_82448_4_.getWorldBorder().minX() + 16.0, p_82448_4_.getWorldBorder().maxX() - 16.0);
            d2 = MathHelper.clamp_double(d2 * d3, p_82448_4_.getWorldBorder().minZ() + 16.0, p_82448_4_.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(d0, entityIn.posY, d2, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else {
            BlockPos blockpos;
            if (p_82448_2_ == 1) {
                blockpos = p_82448_4_.getSpawnPoint();
            }
            else {
                blockpos = p_82448_4_.getSpawnCoordinate();
            }
            d0 = blockpos.getX();
            entityIn.posY = blockpos.getY();
            d2 = blockpos.getZ();
            entityIn.setLocationAndAngles(d0, entityIn.posY, d2, 90.0f, 0.0f);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        p_82448_3_.theProfiler.endSection();
        if (p_82448_2_ != 1) {
            p_82448_3_.theProfiler.startSection("placing");
            d0 = MathHelper.clamp_int((int)d0, -29999872, 29999872);
            d2 = MathHelper.clamp_int((int)d2, -29999872, 29999872);
            if (entityIn.isEntityAlive()) {
                entityIn.setLocationAndAngles(d0, entityIn.posY, d2, entityIn.rotationYaw, entityIn.rotationPitch);
                p_82448_4_.getDefaultTeleporter().placeInPortal(entityIn, f);
                p_82448_4_.spawnEntityInWorld(entityIn);
                p_82448_4_.updateEntityWithOptionalForce(entityIn, false);
            }
            p_82448_3_.theProfiler.endSection();
        }
        entityIn.setWorld(p_82448_4_);
    }
    
    public void onTick() {
        if (++this.playerPingIndex > 600) {
            this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
            this.playerPingIndex = 0;
        }
    }
    
    public void sendPacketToAllPlayers(final Packet packetIn) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            this.playerEntityList.get(i).playerNetServerHandler.sendPacket(packetIn);
        }
    }
    
    public void sendPacketToAllPlayersInDimension(final Packet packetIn, final int dimension) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp.dimension == dimension) {
                entityplayermp.playerNetServerHandler.sendPacket(packetIn);
            }
        }
    }
    
    public void sendMessageToAllTeamMembers(final EntityPlayer player, final IChatComponent message) {
        final Team team = player.getTeam();
        if (team != null) {
            for (final String s : team.getMembershipCollection()) {
                final EntityPlayerMP entityplayermp = this.getPlayerByUsername(s);
                if (entityplayermp != null && entityplayermp != player) {
                    entityplayermp.addChatMessage(message);
                }
            }
        }
    }
    
    public void sendMessageToTeamOrEvryPlayer(final EntityPlayer player, final IChatComponent message) {
        final Team team = player.getTeam();
        if (team == null) {
            this.sendChatMsg(message);
        }
        else {
            for (int i = 0; i < this.playerEntityList.size(); ++i) {
                final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
                if (entityplayermp.getTeam() != team) {
                    entityplayermp.addChatMessage(message);
                }
            }
        }
    }
    
    public String func_181058_b(final boolean p_181058_1_) {
        String s = "";
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList((Iterable<?>)this.playerEntityList);
        for (int i = 0; i < list.size(); ++i) {
            if (i > 0) {
                s = String.valueOf(s) + ", ";
            }
            s = String.valueOf(s) + list.get(i).getName();
            if (p_181058_1_) {
                s = String.valueOf(s) + " (" + list.get(i).getUniqueID().toString() + ")";
            }
        }
        return s;
    }
    
    public String[] getAllUsernames() {
        final String[] astring = new String[this.playerEntityList.size()];
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            astring[i] = this.playerEntityList.get(i).getName();
        }
        return astring;
    }
    
    public GameProfile[] getAllProfiles() {
        final GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            agameprofile[i] = this.playerEntityList.get(i).getGameProfile();
        }
        return agameprofile;
    }
    
    public UserListBans getBannedPlayers() {
        return this.bannedPlayers;
    }
    
    public BanList getBannedIPs() {
        return this.bannedIPs;
    }
    
    public void addOp(final GameProfile profile) {
        ((UserList<K, UserListOpsEntry>)this.ops).addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel(), this.ops.func_183026_b(profile)));
    }
    
    public void removeOp(final GameProfile profile) {
        ((UserList<GameProfile, V>)this.ops).removeEntry(profile);
    }
    
    public boolean canJoin(final GameProfile profile) {
        return !this.whiteListEnforced || ((UserList<GameProfile, V>)this.ops).hasEntry(profile) || ((UserList<GameProfile, V>)this.whiteListedPlayers).hasEntry(profile);
    }
    
    public boolean canSendCommands(final GameProfile profile) {
        return ((UserList<GameProfile, V>)this.ops).hasEntry(profile) || (this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName())) || this.commandsAllowedForAll;
    }
    
    public EntityPlayerMP getPlayerByUsername(final String username) {
        for (final EntityPlayerMP entityplayermp : this.playerEntityList) {
            if (entityplayermp.getName().equalsIgnoreCase(username)) {
                return entityplayermp;
            }
        }
        return null;
    }
    
    public void sendToAllNear(final double x, final double y, final double z, final double radius, final int dimension, final Packet packetIn) {
        this.sendToAllNearExcept(null, x, y, z, radius, dimension, packetIn);
    }
    
    public void sendToAllNearExcept(final EntityPlayer p_148543_1_, final double x, final double y, final double z, final double radius, final int dimension, final Packet p_148543_11_) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp != p_148543_1_ && entityplayermp.dimension == dimension) {
                final double d0 = x - entityplayermp.posX;
                final double d2 = y - entityplayermp.posY;
                final double d3 = z - entityplayermp.posZ;
                if (d0 * d0 + d2 * d2 + d3 * d3 < radius * radius) {
                    entityplayermp.playerNetServerHandler.sendPacket(p_148543_11_);
                }
            }
        }
    }
    
    public void saveAllPlayerData() {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            this.writePlayerData(this.playerEntityList.get(i));
        }
    }
    
    public void addWhitelistedPlayer(final GameProfile profile) {
        ((UserList<K, UserListWhitelistEntry>)this.whiteListedPlayers).addEntry(new UserListWhitelistEntry(profile));
    }
    
    public void removePlayerFromWhitelist(final GameProfile profile) {
        ((UserList<GameProfile, V>)this.whiteListedPlayers).removeEntry(profile);
    }
    
    public UserListWhitelist getWhitelistedPlayers() {
        return this.whiteListedPlayers;
    }
    
    public String[] getWhitelistedPlayerNames() {
        return this.whiteListedPlayers.getKeys();
    }
    
    public UserListOps getOppedPlayers() {
        return this.ops;
    }
    
    public String[] getOppedPlayerNames() {
        return this.ops.getKeys();
    }
    
    public void loadWhiteList() {
    }
    
    public void updateTimeAndWeatherForPlayer(final EntityPlayerMP playerIn, final WorldServer worldIn) {
        final WorldBorder worldborder = this.mcServer.worldServers[0].getWorldBorder();
        playerIn.playerNetServerHandler.sendPacket(new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.INITIALIZE));
        playerIn.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
        if (worldIn.isRaining()) {
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0f));
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0f)));
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, worldIn.getThunderStrength(1.0f)));
        }
    }
    
    public void syncPlayerInventory(final EntityPlayerMP playerIn) {
        playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
        playerIn.setPlayerHealthUpdated();
        playerIn.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
    }
    
    public int getCurrentPlayerCount() {
        return this.playerEntityList.size();
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    
    public String[] getAvailablePlayerDat() {
        return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }
    
    public void setWhiteListEnabled(final boolean whitelistEnabled) {
        this.whiteListEnforced = whitelistEnabled;
    }
    
    public List<EntityPlayerMP> getPlayersMatchingAddress(final String address) {
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList();
        for (final EntityPlayerMP entityplayermp : this.playerEntityList) {
            if (entityplayermp.getPlayerIP().equals(address)) {
                list.add(entityplayermp);
            }
        }
        return list;
    }
    
    public int getViewDistance() {
        return this.viewDistance;
    }
    
    public MinecraftServer getServerInstance() {
        return this.mcServer;
    }
    
    public NBTTagCompound getHostPlayerData() {
        return null;
    }
    
    public void setGameType(final WorldSettings.GameType p_152604_1_) {
        this.gameType = p_152604_1_;
    }
    
    private void setPlayerGameTypeBasedOnOther(final EntityPlayerMP p_72381_1_, final EntityPlayerMP p_72381_2_, final World worldIn) {
        if (p_72381_2_ != null) {
            p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
        }
        else if (this.gameType != null) {
            p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
        }
        p_72381_1_.theItemInWorldManager.initializeGameType(worldIn.getWorldInfo().getGameType());
    }
    
    public void setCommandsAllowedForAll(final boolean p_72387_1_) {
        this.commandsAllowedForAll = p_72387_1_;
    }
    
    public void removeAllPlayers() {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            this.playerEntityList.get(i).playerNetServerHandler.kickPlayerFromServer("Server closed");
        }
    }
    
    public void sendChatMsgImpl(final IChatComponent component, final boolean isChat) {
        this.mcServer.addChatMessage(component);
        final byte b0 = (byte)(isChat ? 1 : 0);
        this.sendPacketToAllPlayers(new S02PacketChat(component, b0));
    }
    
    public void sendChatMsg(final IChatComponent component) {
        this.sendChatMsgImpl(component, true);
    }
    
    public StatisticsFile getPlayerStatsFile(final EntityPlayer playerIn) {
        final UUID uuid = playerIn.getUniqueID();
        StatisticsFile statisticsfile = (uuid == null) ? null : this.playerStatFiles.get(uuid);
        if (statisticsfile == null) {
            final File file1 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
            final File file2 = new File(file1, String.valueOf(uuid.toString()) + ".json");
            if (!file2.exists()) {
                final File file3 = new File(file1, String.valueOf(playerIn.getName()) + ".json");
                if (file3.exists() && file3.isFile()) {
                    file3.renameTo(file2);
                }
            }
            statisticsfile = new StatisticsFile(this.mcServer, file2);
            statisticsfile.readStatFile();
            this.playerStatFiles.put(uuid, statisticsfile);
        }
        return statisticsfile;
    }
    
    public void setViewDistance(final int distance) {
        this.viewDistance = distance;
        if (this.mcServer.worldServers != null) {
            WorldServer[] worldServers;
            for (int length = (worldServers = this.mcServer.worldServers).length, i = 0; i < length; ++i) {
                final WorldServer worldserver = worldServers[i];
                if (worldserver != null) {
                    worldserver.getPlayerManager().setPlayerViewRadius(distance);
                }
            }
        }
    }
    
    public List<EntityPlayerMP> func_181057_v() {
        return this.playerEntityList;
    }
    
    public EntityPlayerMP getPlayerByUUID(final UUID playerUUID) {
        return this.uuidToPlayerMap.get(playerUUID);
    }
    
    public boolean func_183023_f(final GameProfile p_183023_1_) {
        return false;
    }
}
