package net.minecraft.server.management;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.demo.DemoWorldManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ServerConfigurationManager {
    public static final File FILE_PLAYERBANS = new File("banned-players.json");
    public static final File FILE_IPBANS = new File("banned-ips.json");
    public static final File FILE_OPS = new File("ops.json");
    public static final File FILE_WHITELIST = new File("whitelist.json");
    private static final Logger logger = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");

    /**
     * Reference to the MinecraftServer object.
     */
    private final MinecraftServer mcServer;

    /**
     * A list of player entities that exist on this server.
     */
    public final List playerEntityList = Lists.newArrayList();
    public final Map field_177454_f = Maps.newHashMap();
    private final UserListBans bannedPlayers;
    private final BanList bannedIPs;

    /**
     * A set containing the OPs.
     */
    private final UserListOps ops;

    /**
     * The Set of all whitelisted players.
     */
    private final UserListWhitelist whiteListedPlayers;
    private final Map playerStatFiles;

    /**
     * Reference to the PlayerNBTManager object.
     */
    private IPlayerFileData playerNBTManagerObj;

    /**
     * Server setting to only allow OPs and whitelisted players to join the server.
     */
    private boolean whiteListEnforced;

    /**
     * The maximum number of players that can be connected at a time.
     */
    protected int maxPlayers;
    private int viewDistance;
    private WorldSettings.GameType gameType;

    /**
     * True if all players are allowed to use commands (cheats).
     */
    private boolean commandsAllowedForAll;

    /**
     * index into playerEntities of player to ping, updated every tick; currently hardcoded to max at 200 players
     */
    private int playerPingIndex;
    private static final String __OBFID = "CL_00001423";

    public ServerConfigurationManager(MinecraftServer server) {
        this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
        this.bannedIPs = new BanList(FILE_IPBANS);
        this.ops = new UserListOps(FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
        this.playerStatFiles = Maps.newHashMap();
        this.mcServer = server;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }

    public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn) {
        GameProfile var3 = playerIn.getGameProfile();
        PlayerProfileCache var4 = this.mcServer.getPlayerProfileCache();
        GameProfile var5 = var4.func_152652_a(var3.getId());
        String var6 = var5 == null ? var3.getName() : var5.getName();
        var4.func_152649_a(var3);
        NBTTagCompound var7 = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
        playerIn.theItemInWorldManager.setWorld((WorldServer) playerIn.worldObj);
        String var8 = "local";

        if (netManager.getRemoteAddress() != null) {
            var8 = netManager.getRemoteAddress().toString();
        }

        logger.info(playerIn.getName() + "[" + var8 + "] logged in with entity id " + playerIn.getEntityId() + " at (" + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
        WorldServer var9 = this.mcServer.worldServerForDimension(playerIn.dimension);
        WorldInfo var10 = var9.getWorldInfo();
        BlockPos var11 = var9.getSpawnPoint();
        this.func_72381_a(playerIn, (EntityPlayerMP) null, var9);
        NetHandlerPlayServer var12 = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);
        var12.sendPacket(new S01PacketJoinGame(playerIn.getEntityId(), playerIn.theItemInWorldManager.getGameType(), var10.isHardcoreModeEnabled(), var9.provider.getDimensionId(), var9.getDifficulty(), this.getMaxPlayers(), var10.getTerrainType(), var9.getGameRules().getGameRuleBooleanValue("reducedDebugInfo")));
        var12.sendPacket(new S3FPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(this.getServerInstance().getServerModName())));
        var12.sendPacket(new S41PacketServerDifficulty(var10.getDifficulty(), var10.isDifficultyLocked()));
        var12.sendPacket(new S05PacketSpawnPosition(var11));
        var12.sendPacket(new S39PacketPlayerAbilities(playerIn.capabilities));
        var12.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
        playerIn.getStatFile().func_150877_d();
        playerIn.getStatFile().func_150884_b(playerIn);
        this.func_96456_a((ServerScoreboard) var9.getScoreboard(), playerIn);
        this.mcServer.refreshStatusNextTick();
        ChatComponentTranslation var13;

        if (!playerIn.getName().equalsIgnoreCase(var6)) {
            var13 = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[]{playerIn.getDisplayName(), var6});
        } else {
            var13 = new ChatComponentTranslation("multiplayer.player.joined", new Object[]{playerIn.getDisplayName()});
        }

        var13.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.sendChatMsg(var13);
        this.playerLoggedIn(playerIn);
        var12.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, var9);

        if (this.mcServer.getResourcePackUrl().length() > 0) {
            playerIn.func_175397_a(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }

        Iterator var14 = playerIn.getActivePotionEffects().iterator();

        while (var14.hasNext()) {
            PotionEffect var15 = (PotionEffect) var14.next();
            var12.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), var15));
        }

        playerIn.addSelfToInternalCraftingInventory();

        if (var7 != null && var7.hasKey("Riding", 10)) {
            Entity var16 = EntityList.createEntityFromNBT(var7.getCompoundTag("Riding"), var9);

            if (var16 != null) {
                var16.forceSpawn = true;
                var9.spawnEntityInWorld(var16);
                playerIn.mountEntity(var16);
                var16.forceSpawn = false;
            }
        }
    }

    protected void func_96456_a(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn) {
        HashSet var3 = Sets.newHashSet();
        Iterator var4 = scoreboardIn.getTeams().iterator();

        while (var4.hasNext()) {
            ScorePlayerTeam var5 = (ScorePlayerTeam) var4.next();
            playerIn.playerNetServerHandler.sendPacket(new S3EPacketTeams(var5, 0));
        }

        for (int var9 = 0; var9 < 19; ++var9) {
            ScoreObjective var10 = scoreboardIn.getObjectiveInDisplaySlot(var9);

            if (var10 != null && !var3.contains(var10)) {
                List var6 = scoreboardIn.func_96550_d(var10);
                Iterator var7 = var6.iterator();

                while (var7.hasNext()) {
                    Packet var8 = (Packet) var7.next();
                    playerIn.playerNetServerHandler.sendPacket(var8);
                }

                var3.add(var10);
            }
        }
    }

    /**
     * Sets the NBT manager to the one for the WorldServer given.
     */
    public void setPlayerManager(WorldServer[] p_72364_1_) {
        this.playerNBTManagerObj = p_72364_1_[0].getSaveHandler().getPlayerNBTManager();
        p_72364_1_[0].getWorldBorder().addListener(new IBorderListener() {
            private static final String __OBFID = "CL_00002267";

            public void onSizeChanged(WorldBorder border, double newSize) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_SIZE));
            }

            public void func_177692_a(WorldBorder border, double p_177692_2_, double p_177692_4_, long p_177692_6_) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.LERP_SIZE));
            }

            public void onCenterChanged(WorldBorder border, double x, double z) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_CENTER));
            }

            public void onWarningTimeChanged(WorldBorder border, int p_177691_2_) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_TIME));
            }

            public void onWarningDistanceChanged(WorldBorder border, int p_177690_2_) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
            }

            public void func_177696_b(WorldBorder border, double p_177696_2_) {
            }

            public void func_177695_c(WorldBorder border, double p_177695_2_) {
            }
        });
    }

    public void func_72375_a(EntityPlayerMP playerIn, WorldServer worldIn) {
        WorldServer var3 = playerIn.getServerForPlayer();

        if (worldIn != null) {
            worldIn.getPlayerManager().removePlayer(playerIn);
        }

        var3.getPlayerManager().addPlayer(playerIn);
        var3.theChunkProviderServer.loadChunk((int) playerIn.posX >> 4, (int) playerIn.posZ >> 4);
    }

    public int getEntityViewDistance() {
        return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
    }

    /**
     * called during player login. reads the player information from disk.
     */
    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn) {
        NBTTagCompound var2 = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound var3;

        if (playerIn.getName().equals(this.mcServer.getServerOwner()) && var2 != null) {
            playerIn.readFromNBT(var2);
            var3 = var2;
            logger.debug("loading single player");
        } else {
            var3 = this.playerNBTManagerObj.readPlayerData(playerIn);
        }

        return var3;
    }

    /**
     * also stores the NBTTags if this is an intergratedPlayerList
     */
    protected void writePlayerData(EntityPlayerMP playerIn) {
        this.playerNBTManagerObj.writePlayerData(playerIn);
        StatisticsFile var2 = (StatisticsFile) this.playerStatFiles.get(playerIn.getUniqueID());

        if (var2 != null) {
            var2.func_150883_b();
        }
    }

    /**
     * Called when a player successfully logs in. Reads player data from disk and inserts the player into the world.
     */
    public void playerLoggedIn(EntityPlayerMP playerIn) {
        this.playerEntityList.add(playerIn);
        this.field_177454_f.put(playerIn.getUniqueID(), playerIn);
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[]{playerIn}));
        WorldServer var2 = this.mcServer.worldServerForDimension(playerIn.dimension);
        var2.spawnEntityInWorld(playerIn);
        this.func_72375_a(playerIn, (WorldServer) null);

        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3) {
            EntityPlayerMP var4 = (EntityPlayerMP) this.playerEntityList.get(var3);
            playerIn.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[]{var4}));
        }
    }

    /**
     * using player's dimension, update their movement when in a vehicle (e.g. cart, boat)
     */
    public void serverUpdateMountedMovingPlayer(EntityPlayerMP playerIn) {
        playerIn.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(playerIn);
    }

    /**
     * Called when a player disconnects from the game. Writes player data to disk and removes them from the world.
     */
    public void playerLoggedOut(EntityPlayerMP playerIn) {
        playerIn.triggerAchievement(StatList.leaveGameStat);
        this.writePlayerData(playerIn);
        WorldServer var2 = playerIn.getServerForPlayer();

        if (playerIn.ridingEntity != null) {
            var2.removePlayerEntityDangerously(playerIn.ridingEntity);
            logger.debug("removing player mount");
        }

        var2.removeEntity(playerIn);
        var2.getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.field_177454_f.remove(playerIn.getUniqueID());
        this.playerStatFiles.remove(playerIn.getUniqueID());
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[]{playerIn}));
    }

    /**
     * checks ban-lists, then white-lists, then space for the server. Returns null on success, or an error message
     */
    public String allowUserToConnect(SocketAddress address, GameProfile profile) {
        String var4;

        if (this.bannedPlayers.isBanned(profile)) {
            UserListBansEntry var5 = (UserListBansEntry) this.bannedPlayers.getEntry(profile);
            var4 = "You are banned from this server!\nReason: " + var5.getBanReason();

            if (var5.getBanEndDate() != null) {
                var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var5.getBanEndDate());
            }

            return var4;
        } else if (!this.canJoin(profile)) {
            return "You are not white-listed on this server!";
        } else if (this.bannedIPs.isBanned(address)) {
            IPBanEntry var3 = this.bannedIPs.getBanEntry(address);
            var4 = "Your IP address is banned from this server!\nReason: " + var3.getBanReason();

            if (var3.getBanEndDate() != null) {
                var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var3.getBanEndDate());
            }

            return var4;
        } else {
            return this.playerEntityList.size() >= this.maxPlayers ? "The server is full!" : null;
        }
    }

    /**
     * also checks for multiple logins across servers
     */
    public EntityPlayerMP createPlayerForUser(GameProfile profile) {
        UUID var2 = EntityPlayer.getUUID(profile);
        ArrayList var3 = Lists.newArrayList();
        EntityPlayerMP var5;

        for (int var4 = 0; var4 < this.playerEntityList.size(); ++var4) {
            var5 = (EntityPlayerMP) this.playerEntityList.get(var4);

            if (var5.getUniqueID().equals(var2)) {
                var3.add(var5);
            }
        }

        Iterator var6 = var3.iterator();

        while (var6.hasNext()) {
            var5 = (EntityPlayerMP) var6.next();
            var5.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
        }

        Object var7;

        if (this.mcServer.isDemo()) {
            var7 = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
        } else {
            var7 = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
        }

        return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, (ItemInWorldManager) var7);
    }

    /**
     * Called on respawn
     */
    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd) {
        playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
        playerIn.getServerForPlayer().getEntityTracker().untrackEntity(playerIn);
        playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.mcServer.worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously(playerIn);
        BlockPos var4 = playerIn.func_180470_cg();
        boolean var5 = playerIn.isSpawnForced();
        playerIn.dimension = dimension;
        Object var6;

        if (this.mcServer.isDemo()) {
            var6 = new DemoWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
        } else {
            var6 = new ItemInWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
        }

        EntityPlayerMP var7 = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(playerIn.dimension), playerIn.getGameProfile(), (ItemInWorldManager) var6);
        var7.playerNetServerHandler = playerIn.playerNetServerHandler;
        var7.clonePlayer(playerIn, conqueredEnd);
        var7.setEntityId(playerIn.getEntityId());
        var7.func_174817_o(playerIn);
        WorldServer var8 = this.mcServer.worldServerForDimension(playerIn.dimension);
        this.func_72381_a(var7, playerIn, var8);
        BlockPos var9;

        if (var4 != null) {
            var9 = EntityPlayer.func_180467_a(this.mcServer.worldServerForDimension(playerIn.dimension), var4, var5);

            if (var9 != null) {
                var7.setLocationAndAngles((double) ((float) var9.getX() + 0.5F), (double) ((float) var9.getY() + 0.1F), (double) ((float) var9.getZ() + 0.5F), 0.0F, 0.0F);
                var7.func_180473_a(var4, var5);
            } else {
                var7.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0F));
            }
        }

        var8.theChunkProviderServer.loadChunk((int) var7.posX >> 4, (int) var7.posZ >> 4);

        while (!var8.getCollidingBoundingBoxes(var7, var7.getEntityBoundingBox()).isEmpty() && var7.posY < 256.0D) {
            var7.setPosition(var7.posX, var7.posY + 1.0D, var7.posZ);
        }

        var7.playerNetServerHandler.sendPacket(new S07PacketRespawn(var7.dimension, var7.worldObj.getDifficulty(), var7.worldObj.getWorldInfo().getTerrainType(), var7.theItemInWorldManager.getGameType()));
        var9 = var8.getSpawnPoint();
        var7.playerNetServerHandler.setPlayerLocation(var7.posX, var7.posY, var7.posZ, var7.rotationYaw, var7.rotationPitch);
        var7.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(var9));
        var7.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(var7.experience, var7.experienceTotal, var7.experienceLevel));
        this.updateTimeAndWeatherForPlayer(var7, var8);
        var8.getPlayerManager().addPlayer(var7);
        var8.spawnEntityInWorld(var7);
        this.playerEntityList.add(var7);
        this.field_177454_f.put(var7.getUniqueID(), var7);
        var7.addSelfToInternalCraftingInventory();
        var7.setHealth(var7.getHealth());
        return var7;
    }

    /**
     * moves provided player from overworld to nether or vice versa
     */
    public void transferPlayerToDimension(EntityPlayerMP playerIn, int dimension) {
        int var3 = playerIn.dimension;
        WorldServer var4 = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.dimension = dimension;
        WorldServer var5 = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.playerNetServerHandler.sendPacket(new S07PacketRespawn(playerIn.dimension, playerIn.worldObj.getDifficulty(), playerIn.worldObj.getWorldInfo().getTerrainType(), playerIn.theItemInWorldManager.getGameType()));
        var4.removePlayerEntityDangerously(playerIn);
        playerIn.isDead = false;
        this.transferEntityToWorld(playerIn, var3, var4, var5);
        this.func_72375_a(playerIn, var4);
        playerIn.playerNetServerHandler.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        playerIn.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeatherForPlayer(playerIn, var5);
        this.syncPlayerInventory(playerIn);
        Iterator var6 = playerIn.getActivePotionEffects().iterator();

        while (var6.hasNext()) {
            PotionEffect var7 = (PotionEffect) var6.next();
            playerIn.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), var7));
        }
    }

    /**
     * Transfers an entity from a world to another world.
     */
    public void transferEntityToWorld(Entity entityIn, int p_82448_2_, WorldServer p_82448_3_, WorldServer p_82448_4_) {
        double var5 = entityIn.posX;
        double var7 = entityIn.posZ;
        double var9 = 8.0D;
        float var11 = entityIn.rotationYaw;
        p_82448_3_.theProfiler.startSection("moving");

        if (entityIn.dimension == -1) {
            var5 = MathHelper.clamp_double(var5 / var9, p_82448_4_.getWorldBorder().minX() + 16.0D, p_82448_4_.getWorldBorder().maxX() - 16.0D);
            var7 = MathHelper.clamp_double(var7 / var9, p_82448_4_.getWorldBorder().minZ() + 16.0D, p_82448_4_.getWorldBorder().maxZ() - 16.0D);
            entityIn.setLocationAndAngles(var5, entityIn.posY, var7, entityIn.rotationYaw, entityIn.rotationPitch);

            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        } else if (entityIn.dimension == 0) {
            var5 = MathHelper.clamp_double(var5 * var9, p_82448_4_.getWorldBorder().minX() + 16.0D, p_82448_4_.getWorldBorder().maxX() - 16.0D);
            var7 = MathHelper.clamp_double(var7 * var9, p_82448_4_.getWorldBorder().minZ() + 16.0D, p_82448_4_.getWorldBorder().maxZ() - 16.0D);
            entityIn.setLocationAndAngles(var5, entityIn.posY, var7, entityIn.rotationYaw, entityIn.rotationPitch);

            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        } else {
            BlockPos var12;

            if (p_82448_2_ == 1) {
                var12 = p_82448_4_.getSpawnPoint();
            } else {
                var12 = p_82448_4_.func_180504_m();
            }

            var5 = (double) var12.getX();
            entityIn.posY = (double) var12.getY();
            var7 = (double) var12.getZ();
            entityIn.setLocationAndAngles(var5, entityIn.posY, var7, 90.0F, 0.0F);

            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }

        p_82448_3_.theProfiler.endSection();

        if (p_82448_2_ != 1) {
            p_82448_3_.theProfiler.startSection("placing");
            var5 = (double) MathHelper.clamp_int((int) var5, -29999872, 29999872);
            var7 = (double) MathHelper.clamp_int((int) var7, -29999872, 29999872);

            if (entityIn.isEntityAlive()) {
                entityIn.setLocationAndAngles(var5, entityIn.posY, var7, entityIn.rotationYaw, entityIn.rotationPitch);
                p_82448_4_.getDefaultTeleporter().func_180266_a(entityIn, var11);
                p_82448_4_.spawnEntityInWorld(entityIn);
                p_82448_4_.updateEntityWithOptionalForce(entityIn, false);
            }

            p_82448_3_.theProfiler.endSection();
        }

        entityIn.setWorld(p_82448_4_);
    }

    /**
     * self explanitory
     */
    public void onTick() {
        if (++this.playerPingIndex > 600) {
            this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
            this.playerPingIndex = 0;
        }
    }

    public void sendPacketToAllPlayers(Packet packetIn) {
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            ((EntityPlayerMP) this.playerEntityList.get(var2)).playerNetServerHandler.sendPacket(packetIn);
        }
    }

    public void sendPacketToAllPlayersInDimension(Packet packetIn, int dimension) {
        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3) {
            EntityPlayerMP var4 = (EntityPlayerMP) this.playerEntityList.get(var3);

            if (var4.dimension == dimension) {
                var4.playerNetServerHandler.sendPacket(packetIn);
            }
        }
    }

    public void func_177453_a(EntityPlayer p_177453_1_, IChatComponent p_177453_2_) {
        Team var3 = p_177453_1_.getTeam();

        if (var3 != null) {
            Collection var4 = var3.getMembershipCollection();
            Iterator var5 = var4.iterator();

            while (var5.hasNext()) {
                String var6 = (String) var5.next();
                EntityPlayerMP var7 = this.getPlayerByUsername(var6);

                if (var7 != null && var7 != p_177453_1_) {
                    var7.addChatMessage(p_177453_2_);
                }
            }
        }
    }

    public void func_177452_b(EntityPlayer p_177452_1_, IChatComponent p_177452_2_) {
        Team var3 = p_177452_1_.getTeam();

        if (var3 == null) {
            this.sendChatMsg(p_177452_2_);
        } else {
            for (int var4 = 0; var4 < this.playerEntityList.size(); ++var4) {
                EntityPlayerMP var5 = (EntityPlayerMP) this.playerEntityList.get(var4);

                if (var5.getTeam() != var3) {
                    var5.addChatMessage(p_177452_2_);
                }
            }
        }
    }

    public String func_180602_f() {
        String var1 = "";

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            if (var2 > 0) {
                var1 = var1 + ", ";
            }

            var1 = var1 + ((EntityPlayerMP) this.playerEntityList.get(var2)).getName();
        }

        return var1;
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getAllUsernames() {
        String[] var1 = new String[this.playerEntityList.size()];

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            var1[var2] = ((EntityPlayerMP) this.playerEntityList.get(var2)).getName();
        }

        return var1;
    }

    public GameProfile[] getAllProfiles() {
        GameProfile[] var1 = new GameProfile[this.playerEntityList.size()];

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            var1[var2] = ((EntityPlayerMP) this.playerEntityList.get(var2)).getGameProfile();
        }

        return var1;
    }

    public UserListBans getBannedPlayers() {
        return this.bannedPlayers;
    }

    public BanList getBannedIPs() {
        return this.bannedIPs;
    }

    public void addOp(GameProfile profile) {
        this.ops.addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel()));
    }

    public void removeOp(GameProfile profile) {
        this.ops.removeEntry(profile);
    }

    public boolean canJoin(GameProfile profile) {
        return !this.whiteListEnforced || this.ops.hasEntry(profile) || this.whiteListedPlayers.hasEntry(profile);
    }

    public boolean canSendCommands(GameProfile profile) {
        return this.ops.hasEntry(profile) || this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName()) || this.commandsAllowedForAll;
    }

    public EntityPlayerMP getPlayerByUsername(String username) {
        Iterator var2 = this.playerEntityList.iterator();
        EntityPlayerMP var3;

        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (EntityPlayerMP) var2.next();
        }
        while (!var3.getName().equalsIgnoreCase(username));

        return var3;
    }

    /**
     * params: x,y,z,r,dimension. The packet is sent to all players within r radius of x,y,z (r^2>x^2+y^2+z^2)
     */
    public void sendToAllNear(double x, double y, double z, double radius, int dimension, Packet packetIn) {
        this.sendToAllNearExcept((EntityPlayer) null, x, y, z, radius, dimension, packetIn);
    }

    /**
     * params: srcPlayer,x,y,z,r,dimension. The packet is not sent to the srcPlayer, but all other players within the
     * search radius
     */
    public void sendToAllNearExcept(EntityPlayer p_148543_1_, double x, double y, double z, double radius, int dimension, Packet p_148543_11_) {
        for (int var12 = 0; var12 < this.playerEntityList.size(); ++var12) {
            EntityPlayerMP var13 = (EntityPlayerMP) this.playerEntityList.get(var12);

            if (var13 != p_148543_1_ && var13.dimension == dimension) {
                double var14 = x - var13.posX;
                double var16 = y - var13.posY;
                double var18 = z - var13.posZ;

                if (var14 * var14 + var16 * var16 + var18 * var18 < radius * radius) {
                    var13.playerNetServerHandler.sendPacket(p_148543_11_);
                }
            }
        }
    }

    /**
     * Saves all of the players' current states.
     */
    public void saveAllPlayerData() {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1) {
            this.writePlayerData((EntityPlayerMP) this.playerEntityList.get(var1));
        }
    }

    public void addWhitelistedPlayer(GameProfile profile) {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(profile));
    }

    public void removePlayerFromWhitelist(GameProfile profile) {
        this.whiteListedPlayers.removeEntry(profile);
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

    /**
     * Either does nothing, or calls readWhiteList.
     */
    public void loadWhiteList() {
    }

    /**
     * Updates the time and weather for the given player to those of the given world
     */
    public void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn) {
        WorldBorder var3 = this.mcServer.worldServers[0].getWorldBorder();
        playerIn.playerNetServerHandler.sendPacket(new S44PacketWorldBorder(var3, S44PacketWorldBorder.Action.INITIALIZE));
        playerIn.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getGameRuleBooleanValue("doDaylightCycle")));

        if (worldIn.isRaining()) {
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0F));
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0F)));
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, worldIn.getWeightedThunderStrength(1.0F)));
        }
    }

    /**
     * sends the players inventory to himself
     */
    public void syncPlayerInventory(EntityPlayerMP playerIn) {
        playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
        playerIn.setPlayerHealthUpdated();
        playerIn.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount() {
        return this.playerEntityList.size();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Returns an array of usernames for which player.dat exists for.
     */
    public String[] getAvailablePlayerDat() {
        return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }

    public void setWhiteListEnabled(boolean whitelistEnabled) {
        this.whiteListEnforced = whitelistEnabled;
    }

    public List getPlayersMatchingAddress(String address) {
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = this.playerEntityList.iterator();

        while (var3.hasNext()) {
            EntityPlayerMP var4 = (EntityPlayerMP) var3.next();

            if (var4.getPlayerIP().equals(address)) {
                var2.add(var4);
            }
        }

        return var2;
    }

    /**
     * Gets the View Distance.
     */
    public int getViewDistance() {
        return this.viewDistance;
    }

    public MinecraftServer getServerInstance() {
        return this.mcServer;
    }

    /**
     * On integrated servers, returns the host's player data to be written to level.dat.
     */
    public NBTTagCompound getHostPlayerData() {
        return null;
    }

    public void func_152604_a(WorldSettings.GameType p_152604_1_) {
        this.gameType = p_152604_1_;
    }

    private void func_72381_a(EntityPlayerMP p_72381_1_, EntityPlayerMP p_72381_2_, World worldIn) {
        if (p_72381_2_ != null) {
            p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
        } else if (this.gameType != null) {
            p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
        }

        p_72381_1_.theItemInWorldManager.initializeGameType(worldIn.getWorldInfo().getGameType());
    }

    /**
     * Sets whether all players are allowed to use commands (cheats) on the server.
     */
    public void setCommandsAllowedForAll(boolean p_72387_1_) {
        this.commandsAllowedForAll = p_72387_1_;
    }

    /**
     * Kicks everyone with "Server closed" as reason.
     */
    public void removeAllPlayers() {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1) {
            ((EntityPlayerMP) this.playerEntityList.get(var1)).playerNetServerHandler.kickPlayerFromServer("Server closed");
        }
    }

    public void sendChatMsgImpl(IChatComponent component, boolean isChat) {
        this.mcServer.addChatMessage(component);
        int var3 = isChat ? 1 : 0;
        this.sendPacketToAllPlayers(new S02PacketChat(component, (byte) var3));
    }

    /**
     * Sends the given string to every player as chat message.
     */
    public void sendChatMsg(IChatComponent component) {
        this.sendChatMsgImpl(component, true);
    }

    public StatisticsFile getPlayerStatsFile(EntityPlayer playerIn) {
        UUID var2 = playerIn.getUniqueID();
        StatisticsFile var3 = var2 == null ? null : (StatisticsFile) this.playerStatFiles.get(var2);

        if (var3 == null) {
            File var4 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
            File var5 = new File(var4, var2.toString() + ".json");

            if (!var5.exists()) {
                File var6 = new File(var4, playerIn.getName() + ".json");

                if (var6.exists() && var6.isFile()) {
                    var6.renameTo(var5);
                }
            }

            var3 = new StatisticsFile(this.mcServer, var5);
            var3.func_150882_a();
            this.playerStatFiles.put(var2, var3);
        }

        return var3;
    }

    public void setViewDistance(int distance) {
        this.viewDistance = distance;

        if (this.mcServer.worldServers != null) {
            WorldServer[] var2 = this.mcServer.worldServers;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                WorldServer var5 = var2[var4];

                if (var5 != null) {
                    var5.getPlayerManager().func_152622_a(distance);
                }
            }
        }
    }

    public EntityPlayerMP func_177451_a(UUID p_177451_1_) {
        return (EntityPlayerMP) this.field_177454_f.get(p_177451_1_);
    }
}
