// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.concurrent.Executors;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import java.awt.GraphicsEnvironment;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.command.CommandBase;
import java.util.concurrent.Callable;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.Util;
import java.util.Collections;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import com.mojang.authlib.GameProfile;
import java.awt.image.BufferedImage;
import io.netty.buffer.ByteBuf;
import com.google.common.base.Charsets;
import io.netty.handler.codec.base64.Base64;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import io.netty.buffer.ByteBufOutputStream;
import org.apache.commons.lang3.Validate;
import javax.imageio.ImageIO;
import io.netty.buffer.Unpooled;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.BlockPos;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.util.IProgressUpdate;
import java.io.IOException;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import java.util.UUID;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.FutureTask;
import java.util.Queue;
import net.minecraft.server.management.PlayerProfileCache;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.security.KeyPair;
import java.net.Proxy;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;
import java.util.Random;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.NetworkSystem;
import net.minecraft.profiler.Profiler;
import net.minecraft.command.ICommandManager;
import net.minecraft.util.ITickable;
import java.util.List;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.world.storage.ISaveFormat;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.util.IThreadListener;
import net.minecraft.command.ICommandSender;

public abstract class MinecraftServer implements Runnable, ICommandSender, IThreadListener, IPlayerUsage
{
    private static final Logger logger;
    public static final File USER_CACHE_FILE;
    private static MinecraftServer mcServer;
    private final ISaveFormat anvilConverterForAnvilFile;
    private final PlayerUsageSnooper usageSnooper;
    private final File anvilFile;
    private final List<ITickable> playersOnline;
    protected final ICommandManager commandManager;
    public final Profiler theProfiler;
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse;
    private final Random random;
    private int serverPort;
    public WorldServer[] worldServers;
    private ServerConfigurationManager serverConfigManager;
    private boolean serverRunning;
    private boolean serverStopped;
    private int tickCounter;
    protected final Proxy serverProxy;
    public String currentTask;
    public int percentDone;
    private boolean onlineMode;
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;
    private boolean pvpEnabled;
    private boolean allowFlight;
    private String motd;
    private int buildLimit;
    private int maxPlayerIdleMinutes;
    public final long[] tickTimeArray;
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;
    private boolean worldIsBeingDeleted;
    private String resourcePackUrl;
    private String resourcePackHash;
    private boolean serverIsRunning;
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private long nanoTimeSinceStatusRefresh;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    protected final Queue<FutureTask<?>> futureTaskQueue;
    private Thread serverThread;
    private long currentTime;
    
    static {
        logger = LogManager.getLogger();
        USER_CACHE_FILE = new File("usercache.json");
    }
    
    public MinecraftServer(final Proxy proxy, final File workDir) {
        this.usageSnooper = new PlayerUsageSnooper("server", this, getCurrentTimeMillis());
        this.playersOnline = (List<ITickable>)Lists.newArrayList();
        this.theProfiler = new Profiler();
        this.statusResponse = new ServerStatusResponse();
        this.random = new Random();
        this.serverPort = -1;
        this.serverRunning = true;
        this.maxPlayerIdleMinutes = 0;
        this.tickTimeArray = new long[100];
        this.resourcePackUrl = "";
        this.resourcePackHash = "";
        this.nanoTimeSinceStatusRefresh = 0L;
        this.futureTaskQueue = (Queue<FutureTask<?>>)Queues.newArrayDeque();
        this.currentTime = getCurrentTimeMillis();
        this.serverProxy = proxy;
        MinecraftServer.mcServer = this;
        this.anvilFile = null;
        this.networkSystem = null;
        this.profileCache = new PlayerProfileCache(this, workDir);
        this.commandManager = null;
        this.anvilConverterForAnvilFile = null;
        this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }
    
    public MinecraftServer(final File workDir, final Proxy proxy, final File profileCacheDir) {
        this.usageSnooper = new PlayerUsageSnooper("server", this, getCurrentTimeMillis());
        this.playersOnline = (List<ITickable>)Lists.newArrayList();
        this.theProfiler = new Profiler();
        this.statusResponse = new ServerStatusResponse();
        this.random = new Random();
        this.serverPort = -1;
        this.serverRunning = true;
        this.maxPlayerIdleMinutes = 0;
        this.tickTimeArray = new long[100];
        this.resourcePackUrl = "";
        this.resourcePackHash = "";
        this.nanoTimeSinceStatusRefresh = 0L;
        this.futureTaskQueue = (Queue<FutureTask<?>>)Queues.newArrayDeque();
        this.currentTime = getCurrentTimeMillis();
        this.serverProxy = proxy;
        MinecraftServer.mcServer = this;
        this.anvilFile = workDir;
        this.networkSystem = new NetworkSystem(this);
        this.profileCache = new PlayerProfileCache(this, profileCacheDir);
        this.commandManager = this.createNewCommandManager();
        this.anvilConverterForAnvilFile = new AnvilSaveConverter(workDir);
        this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }
    
    protected ServerCommandManager createNewCommandManager() {
        return new ServerCommandManager();
    }
    
    protected abstract boolean startServer() throws IOException;
    
    protected void convertMapIfNeeded(final String worldNameIn) {
        if (this.getActiveAnvilConverter().isOldMapFormat(worldNameIn)) {
            MinecraftServer.logger.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate() {
                private long startTime = System.currentTimeMillis();
                
                @Override
                public void displaySavingString(final String message) {
                }
                
                @Override
                public void resetProgressAndMessage(final String message) {
                }
                
                @Override
                public void setLoadingProgress(final int progress) {
                    if (System.currentTimeMillis() - this.startTime >= 1000L) {
                        this.startTime = System.currentTimeMillis();
                        MinecraftServer.logger.info("Converting... " + progress + "%");
                    }
                }
                
                @Override
                public void setDoneWorking() {
                }
                
                @Override
                public void displayLoadingString(final String message) {
                }
            });
        }
    }
    
    protected synchronized void setUserMessage(final String message) {
        this.userMessage = message;
    }
    
    public synchronized String getUserMessage() {
        return this.userMessage;
    }
    
    protected void loadAllWorlds(final String p_71247_1_, final String p_71247_2_, final long seed, final WorldType type, final String p_71247_6_) {
        this.convertMapIfNeeded(p_71247_1_);
        this.setUserMessage("menu.loadingLevel");
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        final ISaveHandler isavehandler = this.anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        WorldSettings worldsettings;
        if (worldinfo == null) {
            if (this.isDemo()) {
                worldsettings = DemoWorldServer.demoWorldSettings;
            }
            else {
                worldsettings = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
                worldsettings.setWorldName(p_71247_6_);
                if (this.enableBonusChest) {
                    worldsettings.enableBonusChest();
                }
            }
            worldinfo = new WorldInfo(worldsettings, p_71247_2_);
        }
        else {
            worldinfo.setWorldName(p_71247_2_);
            worldsettings = new WorldSettings(worldinfo);
        }
        for (int i = 0; i < this.worldServers.length; ++i) {
            int j = 0;
            if (i == 1) {
                j = -1;
            }
            if (i == 2) {
                j = 1;
            }
            if (i == 0) {
                if (this.isDemo()) {
                    this.worldServers[i] = (WorldServer)new DemoWorldServer(this, isavehandler, worldinfo, j, this.theProfiler).init();
                }
                else {
                    this.worldServers[i] = (WorldServer)new WorldServer(this, isavehandler, worldinfo, j, this.theProfiler).init();
                }
                this.worldServers[i].initialize(worldsettings);
            }
            else {
                this.worldServers[i] = (WorldServer)new WorldServerMulti(this, isavehandler, j, this.worldServers[0], this.theProfiler).init();
            }
            this.worldServers[i].addWorldAccess(new WorldManager(this, this.worldServers[i]));
            if (!this.isSinglePlayer()) {
                this.worldServers[i].getWorldInfo().setGameType(this.getGameType());
            }
        }
        this.serverConfigManager.setPlayerManager(this.worldServers);
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }
    
    protected void initialWorldChunkLoad() {
        final int i = 16;
        final int j = 4;
        final int k = 192;
        final int l = 625;
        int i2 = 0;
        this.setUserMessage("menu.generatingTerrain");
        final int j2 = 0;
        MinecraftServer.logger.info("Preparing start region for level " + j2);
        final WorldServer worldserver = this.worldServers[j2];
        final BlockPos blockpos = worldserver.getSpawnPoint();
        long k2 = getCurrentTimeMillis();
        for (int l2 = -192; l2 <= 192 && this.isServerRunning(); l2 += 16) {
            for (int i3 = -192; i3 <= 192 && this.isServerRunning(); i3 += 16) {
                final long j3 = getCurrentTimeMillis();
                if (j3 - k2 > 1000L) {
                    this.outputPercentRemaining("Preparing spawn area", i2 * 100 / 625);
                    k2 = j3;
                }
                ++i2;
                worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + l2 >> 4, blockpos.getZ() + i3 >> 4);
            }
        }
        this.clearCurrentTask();
    }
    
    protected void setResourcePackFromWorld(final String worldNameIn, final ISaveHandler saveHandlerIn) {
        final File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");
        if (file1.isFile()) {
            this.setResourcePack("level://" + worldNameIn + "/" + file1.getName(), "");
        }
    }
    
    public abstract boolean canStructuresSpawn();
    
    public abstract WorldSettings.GameType getGameType();
    
    public abstract EnumDifficulty getDifficulty();
    
    public abstract boolean isHardcore();
    
    public abstract int getOpPermissionLevel();
    
    public abstract boolean func_181034_q();
    
    public abstract boolean func_183002_r();
    
    protected void outputPercentRemaining(final String message, final int percent) {
        this.currentTask = message;
        this.percentDone = percent;
        MinecraftServer.logger.info(String.valueOf(message) + ": " + percent + "%");
    }
    
    protected void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
    }
    
    protected void saveAllWorlds(final boolean dontLog) {
        if (!this.worldIsBeingDeleted) {
            WorldServer[] worldServers;
            for (int length = (worldServers = this.worldServers).length, i = 0; i < length; ++i) {
                final WorldServer worldserver = worldServers[i];
                if (worldserver != null) {
                    if (!dontLog) {
                        MinecraftServer.logger.info("Saving chunks for level '" + worldserver.getWorldInfo().getWorldName() + "'/" + worldserver.provider.getDimensionName());
                    }
                    try {
                        worldserver.saveAllChunks(true, null);
                    }
                    catch (MinecraftException minecraftexception) {
                        MinecraftServer.logger.warn(minecraftexception.getMessage());
                    }
                }
            }
        }
    }
    
    public void stopServer() {
        if (!this.worldIsBeingDeleted) {
            MinecraftServer.logger.info("Stopping server");
            if (this.getNetworkSystem() != null) {
                this.getNetworkSystem().terminateEndpoints();
            }
            if (this.serverConfigManager != null) {
                MinecraftServer.logger.info("Saving players");
                this.serverConfigManager.saveAllPlayerData();
                this.serverConfigManager.removeAllPlayers();
            }
            if (this.worldServers != null) {
                MinecraftServer.logger.info("Saving worlds");
                this.saveAllWorlds(false);
                for (int i = 0; i < this.worldServers.length; ++i) {
                    final WorldServer worldserver = this.worldServers[i];
                    worldserver.flush();
                }
            }
            if (this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.stopSnooper();
            }
        }
    }
    
    public boolean isServerRunning() {
        return this.serverRunning;
    }
    
    public void initiateShutdown() {
        this.serverRunning = false;
    }
    
    protected void setInstance() {
        MinecraftServer.mcServer = this;
    }
    
    @Override
    public void run() {
        try {
            if (this.startServer()) {
                this.currentTime = getCurrentTimeMillis();
                long i = 0L;
                this.statusResponse.setServerDescription(new ChatComponentText(this.motd));
                this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8.8", 47));
                this.addFaviconToStatusResponse(this.statusResponse);
                while (this.serverRunning) {
                    final long k = getCurrentTimeMillis();
                    long j = k - this.currentTime;
                    if (j > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
                        MinecraftServer.logger.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", j, j / 50L);
                        j = 2000L;
                        this.timeOfLastWarning = this.currentTime;
                    }
                    if (j < 0L) {
                        MinecraftServer.logger.warn("Time ran backwards! Did the system time change?");
                        j = 0L;
                    }
                    i += j;
                    this.currentTime = k;
                    if (this.worldServers[0].areAllPlayersAsleep()) {
                        this.tick();
                        i = 0L;
                    }
                    else {
                        while (i > 50L) {
                            i -= 50L;
                            this.tick();
                        }
                    }
                    Thread.sleep(Math.max(1L, 50L - i));
                    this.serverIsRunning = true;
                }
            }
            else {
                this.finalTick(null);
            }
        }
        catch (Throwable throwable1) {
            MinecraftServer.logger.error("Encountered an unexpected exception", throwable1);
            CrashReport crashreport = null;
            if (throwable1 instanceof ReportedException) {
                crashreport = this.addServerInfoToCrashReport(((ReportedException)throwable1).getCrashReport());
            }
            else {
                crashreport = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable1));
            }
            final File file1 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
            if (crashreport.saveToFile(file1)) {
                MinecraftServer.logger.error("This crash report has been saved to: " + file1.getAbsolutePath());
            }
            else {
                MinecraftServer.logger.error("We were unable to save this crash report to disk.");
            }
            this.finalTick(crashreport);
            return;
        }
        finally {
            Label_0525: {
                try {
                    this.serverStopped = true;
                    this.stopServer();
                }
                catch (Throwable throwable2) {
                    MinecraftServer.logger.error("Exception stopping the server", throwable2);
                    this.systemExitNow();
                    break Label_0525;
                }
                finally {
                    this.systemExitNow();
                }
                this.systemExitNow();
            }
        }
        try {
            this.serverStopped = true;
            this.stopServer();
        }
        catch (Throwable throwable2) {
            MinecraftServer.logger.error("Exception stopping the server", throwable2);
            return;
        }
        finally {
            this.systemExitNow();
        }
        this.systemExitNow();
    }
    
    private void addFaviconToStatusResponse(final ServerStatusResponse response) {
        final File file1 = this.getFile("server-icon.png");
        if (file1.isFile()) {
            final ByteBuf bytebuf = Unpooled.buffer();
            try {
                final BufferedImage bufferedimage = ImageIO.read(file1);
                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                final ByteBuf bytebuf2 = Base64.encode(bytebuf);
                response.setFavicon("data:image/png;base64," + bytebuf2.toString(Charsets.UTF_8));
            }
            catch (Exception exception) {
                MinecraftServer.logger.error("Couldn't load server icon", exception);
                return;
            }
            finally {
                bytebuf.release();
            }
            bytebuf.release();
        }
    }
    
    public File getDataDirectory() {
        return new File(".");
    }
    
    protected void finalTick(final CrashReport report) {
    }
    
    protected void systemExitNow() {
    }
    
    public void tick() {
        final long i = System.nanoTime();
        ++this.tickCounter;
        if (this.startProfiling) {
            this.startProfiling = false;
            this.theProfiler.profilingEnabled = true;
            this.theProfiler.clearProfiling();
        }
        this.theProfiler.startSection("root");
        this.updateTimeLightAndEntities();
        if (i - this.nanoTimeSinceStatusRefresh >= 5000000000L) {
            this.nanoTimeSinceStatusRefresh = i;
            this.statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            final GameProfile[] agameprofile = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            final int j = MathHelper.getRandomIntegerInRange(this.random, 0, this.getCurrentPlayerCount() - agameprofile.length);
            for (int k = 0; k < agameprofile.length; ++k) {
                agameprofile[k] = this.serverConfigManager.func_181057_v().get(j + k).getGameProfile();
            }
            Collections.shuffle(Arrays.asList(agameprofile));
            this.statusResponse.getPlayerCountData().setPlayers(agameprofile);
        }
        if (this.tickCounter % 900 == 0) {
            this.theProfiler.startSection("save");
            this.serverConfigManager.saveAllPlayerData();
            this.saveAllWorlds(true);
            this.theProfiler.endSection();
        }
        this.theProfiler.startSection("tallying");
        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - i;
        this.theProfiler.endSection();
        this.theProfiler.startSection("snooper");
        if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100) {
            this.usageSnooper.startSnooper();
        }
        if (this.tickCounter % 6000 == 0) {
            this.usageSnooper.addMemoryStatsToSnooper();
        }
        this.theProfiler.endSection();
        this.theProfiler.endSection();
    }
    
    public void updateTimeLightAndEntities() {
        this.theProfiler.startSection("jobs");
        synchronized (this.futureTaskQueue) {
            while (!this.futureTaskQueue.isEmpty()) {
                Util.func_181617_a(this.futureTaskQueue.poll(), MinecraftServer.logger);
            }
        }
        // monitorexit(this.futureTaskQueue)
        this.theProfiler.endStartSection("levels");
        for (int j = 0; j < this.worldServers.length; ++j) {
            final long i = System.nanoTime();
            if (j == 0 || this.getAllowNether()) {
                final WorldServer worldserver = this.worldServers[j];
                this.theProfiler.startSection(worldserver.getWorldInfo().getWorldName());
                if (this.tickCounter % 20 == 0) {
                    this.theProfiler.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimensionId());
                    this.theProfiler.endSection();
                }
                this.theProfiler.startSection("tick");
                try {
                    worldserver.tick();
                }
                catch (Throwable throwable1) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }
                try {
                    worldserver.updateEntities();
                }
                catch (Throwable throwable2) {
                    final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception ticking world entities");
                    worldserver.addWorldInfoToCrashReport(crashreport2);
                    throw new ReportedException(crashreport2);
                }
                this.theProfiler.endSection();
                this.theProfiler.startSection("tracker");
                worldserver.getEntityTracker().updateTrackedEntities();
                this.theProfiler.endSection();
                this.theProfiler.endSection();
            }
            this.timeOfLastDimensionTick[j][this.tickCounter % 100] = System.nanoTime() - i;
        }
        this.theProfiler.endStartSection("connection");
        this.getNetworkSystem().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.onTick();
        this.theProfiler.endStartSection("tickables");
        for (int k = 0; k < this.playersOnline.size(); ++k) {
            this.playersOnline.get(k).update();
        }
        this.theProfiler.endSection();
    }
    
    public boolean getAllowNether() {
        return true;
    }
    
    public void startServerThread() {
        (this.serverThread = new Thread(this, "Server thread")).start();
    }
    
    public File getFile(final String fileName) {
        return new File(this.getDataDirectory(), fileName);
    }
    
    public void logWarning(final String msg) {
        MinecraftServer.logger.warn(msg);
    }
    
    public WorldServer worldServerForDimension(final int dimension) {
        return (dimension == -1) ? this.worldServers[1] : ((dimension == 1) ? this.worldServers[2] : this.worldServers[0]);
    }
    
    public String getMinecraftVersion() {
        return "1.8.8";
    }
    
    public int getCurrentPlayerCount() {
        return this.serverConfigManager.getCurrentPlayerCount();
    }
    
    public int getMaxPlayers() {
        return this.serverConfigManager.getMaxPlayers();
    }
    
    public String[] getAllUsernames() {
        return this.serverConfigManager.getAllUsernames();
    }
    
    public GameProfile[] getGameProfiles() {
        return this.serverConfigManager.getAllProfiles();
    }
    
    public String getServerModName() {
        return "vanilla";
    }
    
    public CrashReport addServerInfoToCrashReport(final CrashReport report) {
        report.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        if (this.serverConfigManager != null) {
            report.getCategory().addCrashSectionCallable("Player Count", new Callable<String>() {
                @Override
                public String call() {
                    return String.valueOf(MinecraftServer.this.serverConfigManager.getCurrentPlayerCount()) + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.func_181057_v();
                }
            });
        }
        return report;
    }
    
    public List<String> getTabCompletions(final ICommandSender sender, String input, final BlockPos pos) {
        final List<String> list = (List<String>)Lists.newArrayList();
        if (input.startsWith("/")) {
            input = input.substring(1);
            final boolean flag = !input.contains(" ");
            final List<String> list2 = this.commandManager.getTabCompletionOptions(sender, input, pos);
            if (list2 != null) {
                for (final String s2 : list2) {
                    if (flag) {
                        list.add("/" + s2);
                    }
                    else {
                        list.add(s2);
                    }
                }
            }
            return list;
        }
        final String[] astring = input.split(" ", -1);
        final String s3 = astring[astring.length - 1];
        String[] allUsernames;
        for (int length = (allUsernames = this.serverConfigManager.getAllUsernames()).length, i = 0; i < length; ++i) {
            final String s4 = allUsernames[i];
            if (CommandBase.doesStringStartWith(s3, s4)) {
                list.add(s4);
            }
        }
        return list;
    }
    
    public static MinecraftServer getServer() {
        return MinecraftServer.mcServer;
    }
    
    public boolean isAnvilFileSet() {
        return this.anvilFile != null;
    }
    
    @Override
    public String getName() {
        return "Server";
    }
    
    @Override
    public void addChatMessage(final IChatComponent component) {
        MinecraftServer.logger.info(component.getUnformattedText());
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
        return true;
    }
    
    public ICommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public KeyPair getKeyPair() {
        return this.serverKeyPair;
    }
    
    public String getServerOwner() {
        return this.serverOwner;
    }
    
    public void setServerOwner(final String owner) {
        this.serverOwner = owner;
    }
    
    public boolean isSinglePlayer() {
        return this.serverOwner != null;
    }
    
    public String getFolderName() {
        return this.folderName;
    }
    
    public void setFolderName(final String name) {
        this.folderName = name;
    }
    
    public void setWorldName(final String p_71246_1_) {
        this.worldName = p_71246_1_;
    }
    
    public String getWorldName() {
        return this.worldName;
    }
    
    public void setKeyPair(final KeyPair keyPair) {
        this.serverKeyPair = keyPair;
    }
    
    public void setDifficultyForAllWorlds(final EnumDifficulty difficulty) {
        for (int i = 0; i < this.worldServers.length; ++i) {
            final World world = this.worldServers[i];
            if (world != null) {
                if (world.getWorldInfo().isHardcoreModeEnabled()) {
                    world.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    world.setAllowedSpawnTypes(true, true);
                }
                else if (this.isSinglePlayer()) {
                    world.getWorldInfo().setDifficulty(difficulty);
                    world.setAllowedSpawnTypes(world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                }
                else {
                    world.getWorldInfo().setDifficulty(difficulty);
                    world.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
        }
    }
    
    protected boolean allowSpawnMonsters() {
        return true;
    }
    
    public boolean isDemo() {
        return this.isDemo;
    }
    
    public void setDemo(final boolean demo) {
        this.isDemo = demo;
    }
    
    public void canCreateBonusChest(final boolean enable) {
        this.enableBonusChest = enable;
    }
    
    public ISaveFormat getActiveAnvilConverter() {
        return this.anvilConverterForAnvilFile;
    }
    
    public void deleteWorldAndStopServer() {
        this.worldIsBeingDeleted = true;
        this.getActiveAnvilConverter().flushCache();
        for (int i = 0; i < this.worldServers.length; ++i) {
            final WorldServer worldserver = this.worldServers[i];
            if (worldserver != null) {
                worldserver.flush();
            }
        }
        this.getActiveAnvilConverter().deleteWorldDirectory(this.worldServers[0].getSaveHandler().getWorldDirectoryName());
        this.initiateShutdown();
    }
    
    public String getResourcePackUrl() {
        return this.resourcePackUrl;
    }
    
    public String getResourcePackHash() {
        return this.resourcePackHash;
    }
    
    public void setResourcePack(final String url, final String hash) {
        this.resourcePackUrl = url;
        this.resourcePackHash = hash;
    }
    
    @Override
    public void addServerStatsToSnooper(final PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("whitelist_enabled", false);
        playerSnooper.addClientStat("whitelist_count", 0);
        if (this.serverConfigManager != null) {
            playerSnooper.addClientStat("players_current", this.getCurrentPlayerCount());
            playerSnooper.addClientStat("players_max", this.getMaxPlayers());
            playerSnooper.addClientStat("players_seen", this.serverConfigManager.getAvailablePlayerDat().length);
        }
        playerSnooper.addClientStat("uses_auth", this.onlineMode);
        playerSnooper.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        playerSnooper.addClientStat("run_time", (getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerSnooper.addClientStat("avg_tick_ms", (int)(MathHelper.average(this.tickTimeArray) * 1.0E-6));
        int i = 0;
        if (this.worldServers != null) {
            for (int j = 0; j < this.worldServers.length; ++j) {
                if (this.worldServers[j] != null) {
                    final WorldServer worldserver = this.worldServers[j];
                    final WorldInfo worldinfo = worldserver.getWorldInfo();
                    playerSnooper.addClientStat("world[" + i + "][dimension]", worldserver.provider.getDimensionId());
                    playerSnooper.addClientStat("world[" + i + "][mode]", worldinfo.getGameType());
                    playerSnooper.addClientStat("world[" + i + "][difficulty]", worldserver.getDifficulty());
                    playerSnooper.addClientStat("world[" + i + "][hardcore]", worldinfo.isHardcoreModeEnabled());
                    playerSnooper.addClientStat("world[" + i + "][generator_name]", worldinfo.getTerrainType().getWorldTypeName());
                    playerSnooper.addClientStat("world[" + i + "][generator_version]", worldinfo.getTerrainType().getGeneratorVersion());
                    playerSnooper.addClientStat("world[" + i + "][height]", this.buildLimit);
                    playerSnooper.addClientStat("world[" + i + "][chunks_loaded]", worldserver.getChunkProvider().getLoadedChunkCount());
                    ++i;
                }
            }
        }
        playerSnooper.addClientStat("worlds", i);
    }
    
    @Override
    public void addServerTypeToSnooper(final PlayerUsageSnooper playerSnooper) {
        playerSnooper.addStatToSnooper("singleplayer", this.isSinglePlayer());
        playerSnooper.addStatToSnooper("server_brand", this.getServerModName());
        playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        playerSnooper.addStatToSnooper("dedicated", this.isDedicatedServer());
    }
    
    @Override
    public boolean isSnooperEnabled() {
        return true;
    }
    
    public abstract boolean isDedicatedServer();
    
    public boolean isServerInOnlineMode() {
        return this.onlineMode;
    }
    
    public void setOnlineMode(final boolean online) {
        this.onlineMode = online;
    }
    
    public boolean getCanSpawnAnimals() {
        return this.canSpawnAnimals;
    }
    
    public void setCanSpawnAnimals(final boolean spawnAnimals) {
        this.canSpawnAnimals = spawnAnimals;
    }
    
    public boolean getCanSpawnNPCs() {
        return this.canSpawnNPCs;
    }
    
    public abstract boolean func_181035_ah();
    
    public void setCanSpawnNPCs(final boolean spawnNpcs) {
        this.canSpawnNPCs = spawnNpcs;
    }
    
    public boolean isPVPEnabled() {
        return this.pvpEnabled;
    }
    
    public void setAllowPvp(final boolean allowPvp) {
        this.pvpEnabled = allowPvp;
    }
    
    public boolean isFlightAllowed() {
        return this.allowFlight;
    }
    
    public void setAllowFlight(final boolean allow) {
        this.allowFlight = allow;
    }
    
    public abstract boolean isCommandBlockEnabled();
    
    public String getMOTD() {
        return this.motd;
    }
    
    public void setMOTD(final String motdIn) {
        this.motd = motdIn;
    }
    
    public int getBuildLimit() {
        return this.buildLimit;
    }
    
    public void setBuildLimit(final int maxBuildHeight) {
        this.buildLimit = maxBuildHeight;
    }
    
    public boolean isServerStopped() {
        return this.serverStopped;
    }
    
    public ServerConfigurationManager getConfigurationManager() {
        return this.serverConfigManager;
    }
    
    public void setConfigManager(final ServerConfigurationManager configManager) {
        this.serverConfigManager = configManager;
    }
    
    public void setGameType(final WorldSettings.GameType gameMode) {
        for (int i = 0; i < this.worldServers.length; ++i) {
            getServer().worldServers[i].getWorldInfo().setGameType(gameMode);
        }
    }
    
    public NetworkSystem getNetworkSystem() {
        return this.networkSystem;
    }
    
    public boolean serverIsInRunLoop() {
        return this.serverIsRunning;
    }
    
    public boolean getGuiEnabled() {
        return false;
    }
    
    public abstract String shareToLAN(final WorldSettings.GameType p0, final boolean p1);
    
    public int getTickCounter() {
        return this.tickCounter;
    }
    
    public void enableProfiling() {
        this.startProfiling = true;
    }
    
    public PlayerUsageSnooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }
    
    @Override
    public BlockPos getPosition() {
        return BlockPos.ORIGIN;
    }
    
    @Override
    public Vec3 getPositionVector() {
        return new Vec3(0.0, 0.0, 0.0);
    }
    
    @Override
    public World getEntityWorld() {
        return this.worldServers[0];
    }
    
    @Override
    public Entity getCommandSenderEntity() {
        return null;
    }
    
    public int getSpawnProtectionSize() {
        return 16;
    }
    
    public boolean isBlockProtected(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        return false;
    }
    
    public boolean getForceGamemode() {
        return this.isGamemodeForced;
    }
    
    public Proxy getServerProxy() {
        return this.serverProxy;
    }
    
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    public int getMaxPlayerIdleMinutes() {
        return this.maxPlayerIdleMinutes;
    }
    
    public void setPlayerIdleTimeout(final int idleTimeout) {
        this.maxPlayerIdleMinutes = idleTimeout;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }
    
    public boolean isAnnouncingPlayerAchievements() {
        return true;
    }
    
    public MinecraftSessionService getMinecraftSessionService() {
        return this.sessionService;
    }
    
    public GameProfileRepository getGameProfileRepository() {
        return this.profileRepo;
    }
    
    public PlayerProfileCache getPlayerProfileCache() {
        return this.profileCache;
    }
    
    public ServerStatusResponse getServerStatusResponse() {
        return this.statusResponse;
    }
    
    public void refreshStatusNextTick() {
        this.nanoTimeSinceStatusRefresh = 0L;
    }
    
    public Entity getEntityFromUuid(final UUID uuid) {
        WorldServer[] worldServers;
        for (int length = (worldServers = this.worldServers).length, i = 0; i < length; ++i) {
            final WorldServer worldserver = worldServers[i];
            if (worldserver != null) {
                final Entity entity = worldserver.getEntityFromUuid(uuid);
                if (entity != null) {
                    return entity;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    }
    
    @Override
    public void setCommandStat(final CommandResultStats.Type type, final int amount) {
    }
    
    public int getMaxWorldSize() {
        return 29999984;
    }
    
    public <V> ListenableFuture<V> callFromMainThread(final Callable<V> callable) {
        Validate.notNull(callable);
        if (!this.isCallingFromMinecraftThread() && !this.isServerStopped()) {
            final ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callable);
            synchronized (this.futureTaskQueue) {
                this.futureTaskQueue.add(listenablefuturetask);
                // monitorexit(this.futureTaskQueue)
                return listenablefuturetask;
            }
        }
        try {
            return Futures.immediateFuture(callable.call());
        }
        catch (Exception exception) {
            return (ListenableFuture<V>)Futures.immediateFailedCheckedFuture(exception);
        }
    }
    
    @Override
    public ListenableFuture<Object> addScheduledTask(final Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.callFromMainThread(Executors.callable(runnableToSchedule));
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.serverThread;
    }
    
    public int getNetworkCompressionTreshold() {
        return 256;
    }
}
