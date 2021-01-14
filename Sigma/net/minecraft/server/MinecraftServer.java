package net.minecraft.server;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftServer implements ICommandSender, Runnable, IThreadListener, IPlayerUsage {
    private static final Logger logger = LogManager.getLogger();
    public static final File USER_CACHE_FILE = new File("usercache.json");

    /**
     * Instance of Minecraft Server.
     */
    private static MinecraftServer mcServer;
    private final ISaveFormat anvilConverterForAnvilFile;

    /**
     * The PlayerUsageSnooper instance.
     */
    private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this, MinecraftServer.getCurrentTimeMillis());
    private final File anvilFile;

    /**
     * List of names of players who are online.
     */
    private final List playersOnline = Lists.newArrayList();
    private final ICommandManager commandManager;
    public final Profiler theProfiler = new Profiler();
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse = new ServerStatusResponse();
    private final Random random = new Random();

    /**
     * The server's port.
     */
    private int serverPort = -1;

    /**
     * The server world instances.
     */
    public WorldServer[] worldServers;

    /**
     * The ServerConfigurationManager instance.
     */
    private ServerConfigurationManager serverConfigManager;

    /**
     * Indicates whether the server is running or not. Set to false to initiate
     * a shutdown.
     */
    private boolean serverRunning = true;

    /**
     * Indicates to other classes that the server is safely stopped.
     */
    private boolean serverStopped;

    /**
     * Incremented every tick.
     */
    private int tickCounter;
    protected final Proxy serverProxy;

    /**
     * The task the server is currently working on(and will output on
     * outputPercentRemaining).
     */
    public String currentTask;

    /**
     * The percentage of the current task finished so far.
     */
    public int percentDone;

    /**
     * True if the server is in online mode.
     */
    private boolean onlineMode;

    /**
     * True if the server has animals turned on.
     */
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;

    /**
     * Indicates whether PvP is active on the server or not.
     */
    private boolean pvpEnabled;

    /**
     * Determines if flight is allowed or not.
     */
    private boolean allowFlight;

    /**
     * The server MOTD string.
     */
    private String motd;

    /**
     * Maximum build height.
     */
    private int buildLimit;
    private int maxPlayerIdleMinutes = 0;
    public final long[] tickTimeArray = new long[100];

    /**
     * Stats are [dimension][tick%100] system.nanoTime is stored.
     */
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;

    /**
     * Username of the server owner (for integrated servers)
     */
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;

    /**
     * If true, there is no need to save chunks or stop the server, because that
     * is already being done.
     */
    private boolean worldIsBeingDeleted;

    /**
     * The texture pack for the server
     */
    private String resourcePackUrl = "";
    private String resourcePackHash = "";
    private boolean serverIsRunning;

    /**
     * Set when warned for "Can't keep up", which triggers again after 15
     * seconds.
     */
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private long nanoTimeSinceStatusRefresh = 0L;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    protected final Queue futureTaskQueue = Queues.newArrayDeque();
    private Thread serverThread;
    private long currentTime = MinecraftServer.getCurrentTimeMillis();
    private static final String __OBFID = "CL_00001462";

    public MinecraftServer(Proxy p_i46053_1_, File p_i46053_2_) {
        serverProxy = p_i46053_1_;
        MinecraftServer.mcServer = this;
        anvilFile = null;
        networkSystem = null;
        profileCache = new PlayerProfileCache(this, p_i46053_2_);
        commandManager = null;
        anvilConverterForAnvilFile = null;
        authService = new YggdrasilAuthenticationService(p_i46053_1_, UUID.randomUUID().toString());
        sessionService = authService.createMinecraftSessionService();
        profileRepo = authService.createProfileRepository();
    }

    public MinecraftServer(File workDir, Proxy proxy, File profileCacheDir) {
        serverProxy = proxy;
        MinecraftServer.mcServer = this;
        anvilFile = workDir;
        networkSystem = new NetworkSystem(this);
        profileCache = new PlayerProfileCache(this, profileCacheDir);
        commandManager = createNewCommandManager();
        anvilConverterForAnvilFile = new AnvilSaveConverter(workDir);
        authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        sessionService = authService.createMinecraftSessionService();
        profileRepo = authService.createProfileRepository();
    }

    protected ServerCommandManager createNewCommandManager() {
        return new ServerCommandManager();
    }

    /**
     * Initialises the server and starts it.
     */
    protected abstract boolean startServer() throws IOException;

    protected void convertMapIfNeeded(String worldNameIn) {
        if (getActiveAnvilConverter().isOldMapFormat(worldNameIn)) {
            MinecraftServer.logger.info("Converting map!");
            setUserMessage("menu.convertingLevel");
            getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate() {
                private long startTime = System.currentTimeMillis();
                private static final String __OBFID = "CL_00001417";

                @Override
                public void displaySavingString(String message) {
                }

                @Override
                public void resetProgressAndMessage(String p_73721_1_) {
                }

                @Override
                public void setLoadingProgress(int progress) {
                    if (System.currentTimeMillis() - startTime >= 1000L) {
                        startTime = System.currentTimeMillis();
                        MinecraftServer.logger.info("Converting... " + progress + "%");
                    }
                }

                @Override
                public void setDoneWorking() {
                }

                @Override
                public void displayLoadingString(String message) {
                }
            });
        }
    }

    /**
     * Typically "menu.convertingLevel", "menu.loadingLevel" or others.
     */
    protected synchronized void setUserMessage(String message) {
        userMessage = message;
    }

    public synchronized String getUserMessage() {
        return userMessage;
    }

    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_) {
        convertMapIfNeeded(p_71247_1_);
        setUserMessage("menu.loadingLevel");
        worldServers = new WorldServer[3];
        timeOfLastDimensionTick = new long[worldServers.length][100];
        ISaveHandler var7 = anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
        setResourcePackFromWorld(getFolderName(), var7);
        WorldInfo var9 = var7.loadWorldInfo();
        WorldSettings var8;

        if (var9 == null) {
            if (isDemo()) {
                var8 = DemoWorldServer.demoWorldSettings;
            } else {
                var8 = new WorldSettings(seed, getGameType(), canStructuresSpawn(), isHardcore(), type);
                var8.setWorldName(p_71247_6_);

                if (enableBonusChest) {
                    var8.enableBonusChest();
                }
            }

            var9 = new WorldInfo(var8, p_71247_2_);
        } else {
            var9.setWorldName(p_71247_2_);
            var8 = new WorldSettings(var9);
        }

        for (int var10 = 0; var10 < worldServers.length; ++var10) {
            byte var11 = 0;

            if (var10 == 1) {
                var11 = -1;
            }

            if (var10 == 2) {
                var11 = 1;
            }

            if (var10 == 0) {
                if (isDemo()) {
                    worldServers[var10] = (WorldServer) (new DemoWorldServer(this, var7, var9, var11, theProfiler)).init();
                } else {
                    worldServers[var10] = (WorldServer) (new WorldServer(this, var7, var9, var11, theProfiler)).init();
                }

                worldServers[var10].initialize(var8);
            } else {
                worldServers[var10] = (WorldServer) (new WorldServerMulti(this, var7, var11, worldServers[0], theProfiler)).init();
            }

            worldServers[var10].addWorldAccess(new WorldManager(this, worldServers[var10]));

            if (!isSinglePlayer()) {
                worldServers[var10].getWorldInfo().setGameType(getGameType());
            }
        }

        serverConfigManager.setPlayerManager(worldServers);
        setDifficultyForAllWorlds(getDifficulty());
        initialWorldChunkLoad();
    }

    protected void initialWorldChunkLoad() {
        boolean var1 = true;
        boolean var2 = true;
        boolean var3 = true;
        boolean var4 = true;
        int var5 = 0;
        setUserMessage("menu.generatingTerrain");
        byte var6 = 0;
        MinecraftServer.logger.info("Preparing start region for level " + var6);
        WorldServer var7 = worldServers[var6];
        BlockPos var8 = var7.getSpawnPoint();
        long var9 = MinecraftServer.getCurrentTimeMillis();

        for (int var11 = -192; var11 <= 192 && isServerRunning(); var11 += 16) {
            for (int var12 = -192; var12 <= 192 && isServerRunning(); var12 += 16) {
                long var13 = MinecraftServer.getCurrentTimeMillis();

                if (var13 - var9 > 1000L) {
                    outputPercentRemaining("Preparing spawn area", var5 * 100 / 625);
                    var9 = var13;
                }

                ++var5;
                var7.theChunkProviderServer.loadChunk(var8.getX() + var11 >> 4, var8.getZ() + var12 >> 4);
            }
        }

        clearCurrentTask();
    }

    protected void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn) {
        File var3 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");

        if (var3.isFile()) {
            setResourcePack("level://" + worldNameIn + "/" + var3.getName(), "");
        }
    }

    public abstract boolean canStructuresSpawn();

    public abstract WorldSettings.GameType getGameType();

    /**
     * Get the server's difficulty
     */
    public abstract EnumDifficulty getDifficulty();

    /**
     * Defaults to false.
     */
    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    /**
     * Used to display a percent remaining given text and the percentage.
     */
    protected void outputPercentRemaining(String message, int percent) {
        currentTask = message;
        percentDone = percent;
        MinecraftServer.logger.info(message + ": " + percent + "%");
    }

    /**
     * Set current task to null and set its percentage to 0.
     */
    protected void clearCurrentTask() {
        currentTask = null;
        percentDone = 0;
    }

    /**
     * par1 indicates if a log message should be output.
     */
    protected void saveAllWorlds(boolean dontLog) {
        if (!worldIsBeingDeleted) {
            WorldServer[] var2 = worldServers;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                WorldServer var5 = var2[var4];

                if (var5 != null) {
                    if (!dontLog) {
                        MinecraftServer.logger.info("Saving chunks for level \'" + var5.getWorldInfo().getWorldName() + "\'/" + var5.provider.getDimensionName());
                    }

                    try {
                        var5.saveAllChunks(true, (IProgressUpdate) null);
                    } catch (MinecraftException var7) {
                        MinecraftServer.logger.warn(var7.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer() {
        if (!worldIsBeingDeleted) {
            MinecraftServer.logger.info("Stopping server");

            if (getNetworkSystem() != null) {
                getNetworkSystem().terminateEndpoints();
            }

            if (serverConfigManager != null) {
                MinecraftServer.logger.info("Saving players");
                serverConfigManager.saveAllPlayerData();
                serverConfigManager.removeAllPlayers();
            }

            if (worldServers != null) {
                MinecraftServer.logger.info("Saving worlds");
                saveAllWorlds(false);

                for (WorldServer var2 : worldServers) {
                    var2.flush();
                }
            }

            if (usageSnooper.isSnooperRunning()) {
                usageSnooper.stopSnooper();
            }
        }
    }

    public boolean isServerRunning() {
        return serverRunning;
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to
     * shut down.
     */
    public void initiateShutdown() {
        serverRunning = false;
    }

    protected void func_175585_v() {
        MinecraftServer.mcServer = this;
    }

    @Override
    public void run() {
        try {
            if (startServer()) {
                currentTime = MinecraftServer.getCurrentTimeMillis();
                long var1 = 0L;
                statusResponse.setServerDescription(new ChatComponentText(motd));
                statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8", 47));
                addFaviconToStatusResponse(statusResponse);

                while (serverRunning) {
                    long var48 = MinecraftServer.getCurrentTimeMillis();
                    long var5 = var48 - currentTime;

                    if (var5 > 2000L && currentTime - timeOfLastWarning >= 15000L) {
                        MinecraftServer.logger.warn("Can\'t keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[]{Long.valueOf(var5), Long.valueOf(var5 / 50L)});
                        var5 = 2000L;
                        timeOfLastWarning = currentTime;
                    }

                    if (var5 < 0L) {
                        MinecraftServer.logger.warn("Time ran backwards! Did the system time change?");
                        var5 = 0L;
                    }

                    var1 += var5;
                    currentTime = var48;

                    if (worldServers[0].areAllPlayersAsleep()) {
                        tick();
                        var1 = 0L;
                    } else {
                        while (var1 > 50L) {
                            var1 -= 50L;
                            tick();
                        }
                    }

                    Thread.sleep(Math.max(1L, 50L - var1));
                    serverIsRunning = true;
                }
            } else {
                finalTick((CrashReport) null);
            }
        } catch (Throwable var46) {
            MinecraftServer.logger.error("Encountered an unexpected exception", var46);
            CrashReport var2 = null;

            if (var46 instanceof ReportedException) {
                var2 = addServerInfoToCrashReport(((ReportedException) var46).getCrashReport());
            } else {
                var2 = addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var46));
            }

            File var3 = new File(new File(getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (var2.saveToFile(var3)) {
                MinecraftServer.logger.error("This crash report has been saved to: " + var3.getAbsolutePath());
            } else {
                MinecraftServer.logger.error("We were unable to save this crash report to disk.");
            }

            finalTick(var2);
        } finally {
            try {
                stopServer();
                serverStopped = true;
            } catch (Throwable var44) {
                MinecraftServer.logger.error("Exception stopping the server", var44);
            } finally {
                systemExitNow();
            }
        }
    }

    private void addFaviconToStatusResponse(ServerStatusResponse response) {
        File var2 = getFile("server-icon.png");

        if (var2.isFile()) {
            ByteBuf var3 = Unpooled.buffer();

            try {
                BufferedImage var4 = ImageIO.read(var2);
                Validate.validState(var4.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(var4.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write(var4, "PNG", new ByteBufOutputStream(var3));
                ByteBuf var5 = Base64.encode(var3);
                response.setFavicon("data:image/png;base64," + var5.toString(Charsets.UTF_8));
            } catch (Exception var9) {
                MinecraftServer.logger.error("Couldn\'t load server icon", var9);
            } finally {
                var3.release();
            }
        }
    }

    public File getDataDirectory() {
        return new File(".");
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport report) {
    }

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    protected void systemExitNow() {
    }

    /**
     * f2BrgLb7AsArcLgVi0ch80DU4n8NaerMbAvpcHUxbImuOe47VJhWGb3Hk6BXRPnIJaXOOPBF6IpJI0A2pwqZqBigpIEkcaHVIB2i8pEgDELZkHiTg3rUkB6aqpSiZ3achvK4ii32LHuwTy7vJfuubxSGchqd11kbFVRGQRhgaVYulFUE8A8WiQNBo1cySBMilJJbXhVt function called by run() every loop.
     */
    public void tick() {
        long var1 = System.nanoTime();
        ++tickCounter;

        if (startProfiling) {
            startProfiling = false;
            theProfiler.profilingEnabled = true;
            theProfiler.clearProfiling();
        }

        theProfiler.startSection("root");
        updateTimeLightAndEntities();

        if (var1 - nanoTimeSinceStatusRefresh >= 5000000000L) {
            nanoTimeSinceStatusRefresh = var1;
            statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(getMaxPlayers(), getCurrentPlayerCount()));
            GameProfile[] var3 = new GameProfile[Math.min(getCurrentPlayerCount(), 12)];
            int var4 = MathHelper.getRandomIntegerInRange(random, 0, getCurrentPlayerCount() - var3.length);

            for (int var5 = 0; var5 < var3.length; ++var5) {
                var3[var5] = ((EntityPlayerMP) serverConfigManager.playerEntityList.get(var4 + var5)).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(var3));
            statusResponse.getPlayerCountData().setPlayers(var3);
        }

        if (tickCounter % 900 == 0) {
            theProfiler.startSection("save");
            serverConfigManager.saveAllPlayerData();
            saveAllWorlds(true);
            theProfiler.endSection();
        }

        theProfiler.startSection("tallying");
        tickTimeArray[tickCounter % 100] = System.nanoTime() - var1;
        theProfiler.endSection();
        theProfiler.startSection("snooper");

        if (!usageSnooper.isSnooperRunning() && tickCounter > 100) {
            usageSnooper.startSnooper();
        }

        if (tickCounter % 6000 == 0) {
            usageSnooper.addMemoryStatsToSnooper();
        }

        theProfiler.endSection();
        theProfiler.endSection();
    }

    public void updateTimeLightAndEntities() {
        theProfiler.startSection("jobs");
        Queue var1 = futureTaskQueue;

        synchronized (futureTaskQueue) {
            while (!futureTaskQueue.isEmpty()) {
                try {
                    ((FutureTask) futureTaskQueue.poll()).run();
                } catch (Throwable var9) {
                    MinecraftServer.logger.fatal(var9);
                }
            }
        }

        theProfiler.endStartSection("levels");
        int var11;

        for (var11 = 0; var11 < worldServers.length; ++var11) {
            long var2 = System.nanoTime();

            if (var11 == 0 || getAllowNether()) {
                WorldServer var4 = worldServers[var11];
                theProfiler.startSection(var4.getWorldInfo().getWorldName());

                if (tickCounter % 20 == 0) {
                    theProfiler.startSection("timeSync");
                    serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(var4.getTotalWorldTime(), var4.getWorldTime(), var4.getGameRules().getGameRuleBooleanValue("doDaylightCycle")), var4.provider.getDimensionId());
                    theProfiler.endSection();
                }

                theProfiler.startSection("tick");
                CrashReport var6;

                try {
                    var4.tick();
                } catch (Throwable var8) {
                    var6 = CrashReport.makeCrashReport(var8, "Exception ticking world");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }

                try {
                    var4.updateEntities();
                } catch (Throwable var7) {
                    var6 = CrashReport.makeCrashReport(var7, "Exception ticking world entities");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }

                theProfiler.endSection();
                theProfiler.startSection("tracker");
                var4.getEntityTracker().updateTrackedEntities();
                theProfiler.endSection();
                theProfiler.endSection();
            }

            timeOfLastDimensionTick[var11][tickCounter % 100] = System.nanoTime() - var2;
        }

        theProfiler.endStartSection("connection");
        getNetworkSystem().networkTick();
        theProfiler.endStartSection("players");
        serverConfigManager.onTick();
        theProfiler.endStartSection("tickables");

        for (var11 = 0; var11 < playersOnline.size(); ++var11) {
            ((IUpdatePlayerListBox) playersOnline.get(var11)).update();
        }

        theProfiler.endSection();
    }

    public boolean getAllowNether() {
        return true;
    }

    public void startServerThread() {
        serverThread = new Thread(this, "Server thread");
        serverThread.start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String fileName) {
        return new File(getDataDirectory(), fileName);
    }

    /**
     * Logs the message with a level of WARN.
     */
    public void logWarning(String msg) {
        MinecraftServer.logger.warn(msg);
    }

    /**
     * Gets the worldServer by the given dimension.
     */
    public WorldServer worldServerForDimension(int dimension) {
        return dimension == -1 ? worldServers[1] : (dimension == 1 ? worldServers[2] : worldServers[0]);
    }

    /**
     * Returns the server's Minecraft version as string.
     */
    public String getMinecraftVersion() {
        return "1.8";
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount() {
        return serverConfigManager.getCurrentPlayerCount();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers() {
        return serverConfigManager.getMaxPlayers();
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getAllUsernames() {
        return serverConfigManager.getAllUsernames();
    }

    /**
     * Returns an array of the GameProfiles of all the connected players
     */
    public GameProfile[] getGameProfiles() {
        return serverConfigManager.getAllProfiles();
    }

    public String getServerModName() {
        return "fml,forge";
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report.getCategory().addCrashSectionCallable("Profiler Position", new Callable() {
            private static final String __OBFID = "CL_00001418";

            public String func_179879_a() {
                return theProfiler.profilingEnabled ? theProfiler.getNameOfLastSection() : "N/A (disabled)";
            }

            @Override
            public Object call() {
                return func_179879_a();
            }
        });

        if (serverConfigManager != null) {
            report.getCategory().addCrashSectionCallable("Player Count", new Callable() {
                private static final String __OBFID = "CL_00001419";

                @Override
                public String call() {
                    return serverConfigManager.getCurrentPlayerCount() + " / " + serverConfigManager.getMaxPlayers() + "; " + serverConfigManager.playerEntityList;
                }
            });
        }

        return report;
    }

    public List func_180506_a(ICommandSender p_180506_1_, String p_180506_2_, BlockPos p_180506_3_) {
        ArrayList var4 = Lists.newArrayList();

        if (p_180506_2_.startsWith("/")) {
            p_180506_2_ = p_180506_2_.substring(1);
            boolean var11 = !p_180506_2_.contains(" ");
            List var12 = commandManager.getTabCompletionOptions(p_180506_1_, p_180506_2_, p_180506_3_);

            if (var12 != null) {
                Iterator var13 = var12.iterator();

                while (var13.hasNext()) {
                    String var14 = (String) var13.next();

                    if (var11) {
                        var4.add("/" + var14);
                    } else {
                        var4.add(var14);
                    }
                }
            }

            return var4;
        } else {
            String[] var5 = p_180506_2_.split(" ", -1);
            String var6 = var5[var5.length - 1];
            String[] var7 = serverConfigManager.getAllUsernames();
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                String var10 = var7[var9];

                if (CommandBase.doesStringStartWith(var6, var10)) {
                    var4.add(var10);
                }
            }

            return var4;
        }
    }

    /**
     * Gets mcServer.
     */
    public static MinecraftServer getServer() {
        return MinecraftServer.mcServer;
    }

    public boolean func_175578_N() {
        return anvilFile != null;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return "Server";
    }

    /**
     * Notifies this sender of some sort of information. This is for messages
     * intended to display to the user. Used for typical output (like
     * "you asked for whether or not this game rule is set, so here's your answer"
     * ), warnings (like "I fetched this block for you by ID, but I'd like you
     * to know that every time you do this, I die a little inside
     * "), and errors (like "it's not called iron_pixacke, silly").
     */
    @Override
    public void addChatMessage(IChatComponent message) {
        MinecraftServer.logger.info(message.getUnformattedText());
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    @Override
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return true;
    }

    public ICommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair() {
        return serverKeyPair;
    }

    /**
     * Returns the username of the server owner (for integrated servers)
     */
    public String getServerOwner() {
        return serverOwner;
    }

    /**
     * Sets the username of the owner of this server (in the case of an
     * integrated server)
     */
    public void setServerOwner(String owner) {
        serverOwner = owner;
    }

    public boolean isSinglePlayer() {
        return serverOwner != null;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String name) {
        folderName = name;
    }

    public void setWorldName(String p_71246_1_) {
        worldName = p_71246_1_;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setKeyPair(KeyPair keyPair) {
        serverKeyPair = keyPair;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
        for (WorldServer var3 : worldServers) {
            if (var3 != null) {
                if (var3.getWorldInfo().isHardcoreModeEnabled()) {
                    var3.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    var3.setAllowedSpawnTypes(true, true);
                } else if (isSinglePlayer()) {
                    var3.getWorldInfo().setDifficulty(difficulty);
                    var3.setAllowedSpawnTypes(var3.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                } else {
                    var3.getWorldInfo().setDifficulty(difficulty);
                    var3.setAllowedSpawnTypes(allowSpawnMonsters(), canSpawnAnimals);
                }
            }
        }
    }

    protected boolean allowSpawnMonsters() {
        return true;
    }

    /**
     * Gets whether this is a demo or not.
     */
    public boolean isDemo() {
        return isDemo;
    }

    /**
     * Sets whether this is a demo or not.
     */
    public void setDemo(boolean demo) {
        isDemo = demo;
    }

    public void canCreateBonusChest(boolean enable) {
        enableBonusChest = enable;
    }

    public ISaveFormat getActiveAnvilConverter() {
        return anvilConverterForAnvilFile;
    }

    /**
     * WARNING : directly calls
     * getActiveAnvilConverter().deleteWorldDirectory(theWorldServer[0].
     * getSaveHandler().getWorldDirectoryName());
     */
    public void deleteWorldAndStopServer() {
        worldIsBeingDeleted = true;
        getActiveAnvilConverter().flushCache();

        for (WorldServer var2 : worldServers) {
            if (var2 != null) {
                var2.flush();
            }
        }

        getActiveAnvilConverter().deleteWorldDirectory(worldServers[0].getSaveHandler().getWorldDirectoryName());
        initiateShutdown();
    }

    public String getResourcePackUrl() {
        return resourcePackUrl;
    }

    public String getResourcePackHash() {
        return resourcePackHash;
    }

    public void setResourcePack(String url, String hash) {
        resourcePackUrl = url;
        resourcePackHash = hash;
    }

    @Override
    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("whitelist_enabled", Boolean.valueOf(false));
        playerSnooper.addClientStat("whitelist_count", Integer.valueOf(0));

        if (serverConfigManager != null) {
            playerSnooper.addClientStat("players_current", Integer.valueOf(getCurrentPlayerCount()));
            playerSnooper.addClientStat("players_max", Integer.valueOf(getMaxPlayers()));
            playerSnooper.addClientStat("players_seen", Integer.valueOf(serverConfigManager.getAvailablePlayerDat().length));
        }

        playerSnooper.addClientStat("uses_auth", Boolean.valueOf(onlineMode));
        playerSnooper.addClientStat("gui_state", getGuiEnabled() ? "enabled" : "disabled");
        playerSnooper.addClientStat("run_time", Long.valueOf((MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        playerSnooper.addClientStat("avg_tick_ms", Integer.valueOf((int) (MathHelper.average(tickTimeArray) * 1.0E-6D)));
        int var2 = 0;

        if (worldServers != null) {
            for (WorldServer var4 : worldServers) {
                if (var4 != null) {
                    WorldInfo var5 = var4.getWorldInfo();
                    playerSnooper.addClientStat("world[" + var2 + "][dimension]", Integer.valueOf(var4.provider.getDimensionId()));
                    playerSnooper.addClientStat("world[" + var2 + "][mode]", var5.getGameType());
                    playerSnooper.addClientStat("world[" + var2 + "][difficulty]", var4.getDifficulty());
                    playerSnooper.addClientStat("world[" + var2 + "][hardcore]", Boolean.valueOf(var5.isHardcoreModeEnabled()));
                    playerSnooper.addClientStat("world[" + var2 + "][generator_name]", var5.getTerrainType().getWorldTypeName());
                    playerSnooper.addClientStat("world[" + var2 + "][generator_version]", Integer.valueOf(var5.getTerrainType().getGeneratorVersion()));
                    playerSnooper.addClientStat("world[" + var2 + "][height]", Integer.valueOf(buildLimit));
                    playerSnooper.addClientStat("world[" + var2 + "][chunks_loaded]", Integer.valueOf(var4.getChunkProvider().getLoadedChunkCount()));
                    ++var2;
                }
            }
        }

        playerSnooper.addClientStat("worlds", Integer.valueOf(var2));
    }

    @Override
    public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper) {
        playerSnooper.addStatToSnooper("singleplayer", Boolean.valueOf(isSinglePlayer()));
        playerSnooper.addStatToSnooper("server_brand", getServerModName());
        playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        playerSnooper.addStatToSnooper("dedicated", Boolean.valueOf(isDedicatedServer()));
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    @Override
    public boolean isSnooperEnabled() {
        return true;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean online) {
        onlineMode = online;
    }

    public boolean getCanSpawnAnimals() {
        return canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean spawnAnimals) {
        canSpawnAnimals = spawnAnimals;
    }

    public boolean getCanSpawnNPCs() {
        return canSpawnNPCs;
    }

    public void setCanSpawnNPCs(boolean spawnNpcs) {
        canSpawnNPCs = spawnNpcs;
    }

    public boolean isPVPEnabled() {
        return pvpEnabled;
    }

    public void setAllowPvp(boolean allowPvp) {
        pvpEnabled = allowPvp;
    }

    public boolean isFlightAllowed() {
        return allowFlight;
    }

    public void setAllowFlight(boolean allow) {
        allowFlight = allow;
    }

    /**
     * Return whether command blocks are enabled.
     */
    public abstract boolean isCommandBlockEnabled();

    public String getMOTD() {
        return motd;
    }

    public void setMOTD(String motdIn) {
        motd = motdIn;
    }

    public int getBuildLimit() {
        return buildLimit;
    }

    public void setBuildLimit(int maxBuildHeight) {
        buildLimit = maxBuildHeight;
    }

    public ServerConfigurationManager getConfigurationManager() {
        return serverConfigManager;
    }

    public void setConfigManager(ServerConfigurationManager configManager) {
        serverConfigManager = configManager;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(WorldSettings.GameType gameMode) {
        for (int var2 = 0; var2 < worldServers.length; ++var2) {
            MinecraftServer.getServer().worldServers[var2].getWorldInfo().setGameType(gameMode);
        }
    }

    public NetworkSystem getNetworkSystem() {
        return networkSystem;
    }

    public boolean serverIsInRunLoop() {
        return serverIsRunning;
    }

    public boolean getGuiEnabled() {
        return false;
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll,
     * gameType and allows external connections.
     */
    public abstract String shareToLAN(WorldSettings.GameType type, boolean allowCheats);

    public int getTickCounter() {
        return tickCounter;
    }

    public void enableProfiling() {
        startProfiling = true;
    }

    public PlayerUsageSnooper getPlayerUsageSnooper() {
        return usageSnooper;
    }

    @Override
    public BlockPos getPosition() {
        return BlockPos.ORIGIN;
    }

    @Override
    public Vec3 getPositionVector() {
        return new Vec3(0.0D, 0.0D, 0.0D);
    }

    @Override
    public World getEntityWorld() {
        return worldServers[0];
    }

    @Override
    public Entity getCommandSenderEntity() {
        return null;
    }

    /**
     * Return the spawn protection area's size.
     */
    public int getSpawnProtectionSize() {
        return 16;
    }

    public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        return false;
    }

    public boolean getForceGamemode() {
        return isGamemodeForced;
    }

    public Proxy getServerProxy() {
        return serverProxy;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public int getMaxPlayerIdleMinutes() {
        return maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int idleTimeout) {
        maxPlayerIdleMinutes = idleTimeout;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(getName());
    }

    public boolean isAnnouncingPlayerAchievements() {
        return true;
    }

    public MinecraftSessionService getMinecraftSessionService() {
        return sessionService;
    }

    public GameProfileRepository getGameProfileRepository() {
        return profileRepo;
    }

    public PlayerProfileCache getPlayerProfileCache() {
        return profileCache;
    }

    public ServerStatusResponse getServerStatusResponse() {
        return statusResponse;
    }

    public void refreshStatusNextTick() {
        nanoTimeSinceStatusRefresh = 0L;
    }

    public Entity getEntityFromUuid(UUID uuid) {
        WorldServer[] var2 = worldServers;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            WorldServer var5 = var2[var4];

            if (var5 != null) {
                Entity var6 = var5.getEntityFromUuid(uuid);

                if (var6 != null) {
                    return var6;
                }
            }
        }

        return null;
    }

    @Override
    public boolean sendCommandFeedback() {
        return MinecraftServer.getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");
    }

    @Override
    public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_) {
    }

    public int getMaxWorldSize() {
        return 29999984;
    }

    public ListenableFuture callFromMainThread(Callable callable) {
        Validate.notNull(callable);

        if (!isCallingFromMinecraftThread()) {
            ListenableFutureTask var2 = ListenableFutureTask.create(callable);
            Queue var3 = futureTaskQueue;

            synchronized (futureTaskQueue) {
                futureTaskQueue.add(var2);
                return var2;
            }
        } else {
            try {
                return Futures.immediateFuture(callable.call());
            } catch (Exception var6) {
                return Futures.immediateFailedCheckedFuture(var6);
            }
        }
    }

    @Override
    public ListenableFuture addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return callFromMainThread(Executors.callable(runnableToSchedule));
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == serverThread;
    }

    /**
     * The compression treshold. If the packet is larger than the specified
     * amount of bytes, it will be compressed
     */
    public int getNetworkCompressionTreshold() {
        return 256;
    }
}
