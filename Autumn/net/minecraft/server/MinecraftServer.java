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
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
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

public abstract class MinecraftServer implements Runnable, ICommandSender, IThreadListener, IPlayerUsage {
   private static final Logger logger = LogManager.getLogger();
   public static final File USER_CACHE_FILE = new File("usercache.json");
   private static MinecraftServer mcServer;
   private final ISaveFormat anvilConverterForAnvilFile;
   private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this, getCurrentTimeMillis());
   private final File anvilFile;
   private final List playersOnline = Lists.newArrayList();
   protected final ICommandManager commandManager;
   public final Profiler theProfiler = new Profiler();
   private final NetworkSystem networkSystem;
   private final ServerStatusResponse statusResponse = new ServerStatusResponse();
   private final Random random = new Random();
   private int serverPort = -1;
   public WorldServer[] worldServers;
   private ServerConfigurationManager serverConfigManager;
   private boolean serverRunning = true;
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
   private int maxPlayerIdleMinutes = 0;
   public final long[] tickTimeArray = new long[100];
   public long[][] timeOfLastDimensionTick;
   private KeyPair serverKeyPair;
   private String serverOwner;
   private String folderName;
   private String worldName;
   private boolean isDemo;
   private boolean enableBonusChest;
   private boolean worldIsBeingDeleted;
   private String resourcePackUrl = "";
   private String resourcePackHash = "";
   private boolean serverIsRunning;
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
   private long currentTime = getCurrentTimeMillis();

   public MinecraftServer(Proxy proxy, File workDir) {
      this.serverProxy = proxy;
      mcServer = this;
      this.anvilFile = null;
      this.networkSystem = null;
      this.profileCache = new PlayerProfileCache(this, workDir);
      this.commandManager = null;
      this.anvilConverterForAnvilFile = null;
      this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
      this.sessionService = this.authService.createMinecraftSessionService();
      this.profileRepo = this.authService.createProfileRepository();
   }

   public MinecraftServer(File workDir, Proxy proxy, File profileCacheDir) {
      this.serverProxy = proxy;
      mcServer = this;
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

   protected void convertMapIfNeeded(String worldNameIn) {
      if (this.getActiveAnvilConverter().isOldMapFormat(worldNameIn)) {
         logger.info("Converting map!");
         this.setUserMessage("menu.convertingLevel");
         this.getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate() {
            private long startTime = System.currentTimeMillis();

            public void displaySavingString(String message) {
            }

            public void resetProgressAndMessage(String message) {
            }

            public void setLoadingProgress(int progress) {
               if (System.currentTimeMillis() - this.startTime >= 1000L) {
                  this.startTime = System.currentTimeMillis();
                  MinecraftServer.logger.info("Converting... " + progress + "%");
               }

            }

            public void setDoneWorking() {
            }

            public void displayLoadingString(String message) {
            }
         });
      }

   }

   protected synchronized void setUserMessage(String message) {
      this.userMessage = message;
   }

   public synchronized String getUserMessage() {
      return this.userMessage;
   }

   protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_) {
      this.convertMapIfNeeded(p_71247_1_);
      this.setUserMessage("menu.loadingLevel");
      this.worldServers = new WorldServer[3];
      this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
      ISaveHandler isavehandler = this.anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
      this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
      WorldInfo worldinfo = isavehandler.loadWorldInfo();
      WorldSettings worldsettings;
      if (worldinfo == null) {
         if (this.isDemo()) {
            worldsettings = DemoWorldServer.demoWorldSettings;
         } else {
            worldsettings = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
            worldsettings.setWorldName(p_71247_6_);
            if (this.enableBonusChest) {
               worldsettings.enableBonusChest();
            }
         }

         worldinfo = new WorldInfo(worldsettings, p_71247_2_);
      } else {
         worldinfo.setWorldName(p_71247_2_);
         worldsettings = new WorldSettings(worldinfo);
      }

      for(int i = 0; i < this.worldServers.length; ++i) {
         int j = 0;
         if (i == 1) {
            j = -1;
         }

         if (i == 2) {
            j = 1;
         }

         if (i == 0) {
            if (this.isDemo()) {
               this.worldServers[i] = (WorldServer)(new DemoWorldServer(this, isavehandler, worldinfo, j, this.theProfiler)).init();
            } else {
               this.worldServers[i] = (WorldServer)(new WorldServer(this, isavehandler, worldinfo, j, this.theProfiler)).init();
            }

            this.worldServers[i].initialize(worldsettings);
         } else {
            this.worldServers[i] = (WorldServer)(new WorldServerMulti(this, isavehandler, j, this.worldServers[0], this.theProfiler)).init();
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
      int i = true;
      int j = true;
      int k = true;
      int l = true;
      int i1 = 0;
      this.setUserMessage("menu.generatingTerrain");
      int j1 = 0;
      logger.info("Preparing start region for level " + j1);
      WorldServer worldserver = this.worldServers[j1];
      BlockPos blockpos = worldserver.getSpawnPoint();
      long k1 = getCurrentTimeMillis();

      for(int l1 = -192; l1 <= 192 && this.isServerRunning(); l1 += 16) {
         for(int i2 = -192; i2 <= 192 && this.isServerRunning(); i2 += 16) {
            long j2 = getCurrentTimeMillis();
            if (j2 - k1 > 1000L) {
               this.outputPercentRemaining("Preparing spawn area", i1 * 100 / 625);
               k1 = j2;
            }

            ++i1;
            worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + l1 >> 4, blockpos.getZ() + i2 >> 4);
         }
      }

      this.clearCurrentTask();
   }

   protected void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn) {
      File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");
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

   protected void outputPercentRemaining(String message, int percent) {
      this.currentTask = message;
      this.percentDone = percent;
      logger.info(message + ": " + percent + "%");
   }

   protected void clearCurrentTask() {
      this.currentTask = null;
      this.percentDone = 0;
   }

   protected void saveAllWorlds(boolean dontLog) {
      if (!this.worldIsBeingDeleted) {
         WorldServer[] var2 = this.worldServers;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WorldServer worldserver = var2[var4];
            if (worldserver != null) {
               if (!dontLog) {
                  logger.info("Saving chunks for level '" + worldserver.getWorldInfo().getWorldName() + "'/" + worldserver.provider.getDimensionName());
               }

               try {
                  worldserver.saveAllChunks(true, (IProgressUpdate)null);
               } catch (MinecraftException var7) {
                  logger.warn(var7.getMessage());
               }
            }
         }
      }

   }

   public void stopServer() {
      if (!this.worldIsBeingDeleted) {
         logger.info("Stopping server");
         if (this.getNetworkSystem() != null) {
            this.getNetworkSystem().terminateEndpoints();
         }

         if (this.serverConfigManager != null) {
            logger.info("Saving players");
            this.serverConfigManager.saveAllPlayerData();
            this.serverConfigManager.removeAllPlayers();
         }

         if (this.worldServers != null) {
            logger.info("Saving worlds");
            this.saveAllWorlds(false);

            for(int i = 0; i < this.worldServers.length; ++i) {
               WorldServer worldserver = this.worldServers[i];
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
      mcServer = this;
   }

   public void run() {
      try {
         if (this.startServer()) {
            this.currentTime = getCurrentTimeMillis();
            long i = 0L;
            this.statusResponse.setServerDescription(new ChatComponentText(this.motd));
            this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8.8", 47));
            this.addFaviconToStatusResponse(this.statusResponse);

            while(this.serverRunning) {
               long k = getCurrentTimeMillis();
               long j = k - this.currentTime;
               if (j > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
                  logger.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[]{j, j / 50L});
                  j = 2000L;
                  this.timeOfLastWarning = this.currentTime;
               }

               if (j < 0L) {
                  logger.warn("Time ran backwards! Did the system time change?");
                  j = 0L;
               }

               i += j;
               this.currentTime = k;
               if (this.worldServers[0].areAllPlayersAsleep()) {
                  this.tick();
                  i = 0L;
               } else {
                  while(i > 50L) {
                     i -= 50L;
                     this.tick();
                  }
               }

               Thread.sleep(Math.max(1L, 50L - i));
               this.serverIsRunning = true;
            }
         } else {
            this.finalTick((CrashReport)null);
         }
      } catch (Throwable var46) {
         logger.error("Encountered an unexpected exception", var46);
         CrashReport crashreport = null;
         if (var46 instanceof ReportedException) {
            crashreport = this.addServerInfoToCrashReport(((ReportedException)var46).getCrashReport());
         } else {
            crashreport = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var46));
         }

         File file1 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
         if (crashreport.saveToFile(file1)) {
            logger.error("This crash report has been saved to: " + file1.getAbsolutePath());
         } else {
            logger.error("We were unable to save this crash report to disk.");
         }

         this.finalTick(crashreport);
      } finally {
         try {
            this.serverStopped = true;
            this.stopServer();
         } catch (Throwable var44) {
            logger.error("Exception stopping the server", var44);
         } finally {
            this.systemExitNow();
         }

      }

   }

   private void addFaviconToStatusResponse(ServerStatusResponse response) {
      File file1 = this.getFile("server-icon.png");
      if (file1.isFile()) {
         ByteBuf bytebuf = Unpooled.buffer();

         try {
            BufferedImage bufferedimage = ImageIO.read(file1);
            Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
            Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
            ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
            ByteBuf bytebuf1 = Base64.encode(bytebuf);
            response.setFavicon("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
         } catch (Exception var9) {
            logger.error("Couldn't load server icon", var9);
         } finally {
            bytebuf.release();
         }
      }

   }

   public File getDataDirectory() {
      return new File(".");
   }

   protected void finalTick(CrashReport report) {
   }

   protected void systemExitNow() {
   }

   public void tick() {
      long i = System.nanoTime();
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
         GameProfile[] agameprofile = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
         int j = MathHelper.getRandomIntegerInRange(this.random, 0, this.getCurrentPlayerCount() - agameprofile.length);

         for(int k = 0; k < agameprofile.length; ++k) {
            agameprofile[k] = ((EntityPlayerMP)this.serverConfigManager.func_181057_v().get(j + k)).getGameProfile();
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
      synchronized(this.futureTaskQueue) {
         while(!this.futureTaskQueue.isEmpty()) {
            Util.func_181617_a((FutureTask)this.futureTaskQueue.poll(), logger);
         }
      }

      this.theProfiler.endStartSection("levels");

      int k;
      for(k = 0; k < this.worldServers.length; ++k) {
         long i = System.nanoTime();
         if (k == 0 || this.getAllowNether()) {
            WorldServer worldserver = this.worldServers[k];
            this.theProfiler.startSection(worldserver.getWorldInfo().getWorldName());
            if (this.tickCounter % 20 == 0) {
               this.theProfiler.startSection("timeSync");
               this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimensionId());
               this.theProfiler.endSection();
            }

            this.theProfiler.startSection("tick");

            CrashReport crashreport1;
            try {
               worldserver.tick();
            } catch (Throwable var8) {
               crashreport1 = CrashReport.makeCrashReport(var8, "Exception ticking world");
               worldserver.addWorldInfoToCrashReport(crashreport1);
               throw new ReportedException(crashreport1);
            }

            try {
               worldserver.updateEntities();
            } catch (Throwable var7) {
               crashreport1 = CrashReport.makeCrashReport(var7, "Exception ticking world entities");
               worldserver.addWorldInfoToCrashReport(crashreport1);
               throw new ReportedException(crashreport1);
            }

            this.theProfiler.endSection();
            this.theProfiler.startSection("tracker");
            worldserver.getEntityTracker().updateTrackedEntities();
            this.theProfiler.endSection();
            this.theProfiler.endSection();
         }

         this.timeOfLastDimensionTick[k][this.tickCounter % 100] = System.nanoTime() - i;
      }

      this.theProfiler.endStartSection("connection");
      this.getNetworkSystem().networkTick();
      this.theProfiler.endStartSection("players");
      this.serverConfigManager.onTick();
      this.theProfiler.endStartSection("tickables");

      for(k = 0; k < this.playersOnline.size(); ++k) {
         ((ITickable)this.playersOnline.get(k)).update();
      }

      this.theProfiler.endSection();
   }

   public boolean getAllowNether() {
      return true;
   }

   public void startServerThread() {
      this.serverThread = new Thread(this, "Server thread");
      this.serverThread.start();
   }

   public File getFile(String fileName) {
      return new File(this.getDataDirectory(), fileName);
   }

   public void logWarning(String msg) {
      logger.warn(msg);
   }

   public WorldServer worldServerForDimension(int dimension) {
      return dimension == -1 ? this.worldServers[1] : (dimension == 1 ? this.worldServers[2] : this.worldServers[0]);
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

   public CrashReport addServerInfoToCrashReport(CrashReport report) {
      report.getCategory().addCrashSectionCallable("Profiler Position", new Callable() {
         public String call() throws Exception {
            return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
         }
      });
      if (this.serverConfigManager != null) {
         report.getCategory().addCrashSectionCallable("Player Count", new Callable() {
            public String call() {
               return MinecraftServer.this.serverConfigManager.getCurrentPlayerCount() + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.func_181057_v();
            }
         });
      }

      return report;
   }

   public List getTabCompletions(ICommandSender sender, String input, BlockPos pos) {
      List list = Lists.newArrayList();
      if (input.startsWith("/")) {
         input = input.substring(1);
         boolean flag = !input.contains(" ");
         List list1 = this.commandManager.getTabCompletionOptions(sender, input, pos);
         if (list1 != null) {
            Iterator var13 = list1.iterator();

            while(var13.hasNext()) {
               String s2 = (String)var13.next();
               if (flag) {
                  list.add("/" + s2);
               } else {
                  list.add(s2);
               }
            }
         }

         return list;
      } else {
         String[] astring = input.split(" ", -1);
         String s = astring[astring.length - 1];
         String[] var7 = this.serverConfigManager.getAllUsernames();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String s1 = var7[var9];
            if (CommandBase.doesStringStartWith(s, s1)) {
               list.add(s1);
            }
         }

         return list;
      }
   }

   public static MinecraftServer getServer() {
      return mcServer;
   }

   public boolean isAnvilFileSet() {
      return this.anvilFile != null;
   }

   public String getName() {
      return "Server";
   }

   public void addChatMessage(IChatComponent component) {
      logger.info(component.getUnformattedText());
   }

   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
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

   public void setServerOwner(String owner) {
      this.serverOwner = owner;
   }

   public boolean isSinglePlayer() {
      return this.serverOwner != null;
   }

   public String getFolderName() {
      return this.folderName;
   }

   public void setFolderName(String name) {
      this.folderName = name;
   }

   public void setWorldName(String p_71246_1_) {
      this.worldName = p_71246_1_;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public void setKeyPair(KeyPair keyPair) {
      this.serverKeyPair = keyPair;
   }

   public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
      for(int i = 0; i < this.worldServers.length; ++i) {
         World world = this.worldServers[i];
         if (world != null) {
            if (world.getWorldInfo().isHardcoreModeEnabled()) {
               world.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
               world.setAllowedSpawnTypes(true, true);
            } else if (this.isSinglePlayer()) {
               world.getWorldInfo().setDifficulty(difficulty);
               world.setAllowedSpawnTypes(world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
            } else {
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

   public void setDemo(boolean demo) {
      this.isDemo = demo;
   }

   public void canCreateBonusChest(boolean enable) {
      this.enableBonusChest = enable;
   }

   public ISaveFormat getActiveAnvilConverter() {
      return this.anvilConverterForAnvilFile;
   }

   public void deleteWorldAndStopServer() {
      this.worldIsBeingDeleted = true;
      this.getActiveAnvilConverter().flushCache();

      for(int i = 0; i < this.worldServers.length; ++i) {
         WorldServer worldserver = this.worldServers[i];
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

   public void setResourcePack(String url, String hash) {
      this.resourcePackUrl = url;
      this.resourcePackHash = hash;
   }

   public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
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
      playerSnooper.addClientStat("avg_tick_ms", (int)(MathHelper.average(this.tickTimeArray) * 1.0E-6D));
      int i = 0;
      if (this.worldServers != null) {
         for(int j = 0; j < this.worldServers.length; ++j) {
            if (this.worldServers[j] != null) {
               WorldServer worldserver = this.worldServers[j];
               WorldInfo worldinfo = worldserver.getWorldInfo();
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

   public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper) {
      playerSnooper.addStatToSnooper("singleplayer", this.isSinglePlayer());
      playerSnooper.addStatToSnooper("server_brand", this.getServerModName());
      playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
      playerSnooper.addStatToSnooper("dedicated", this.isDedicatedServer());
   }

   public boolean isSnooperEnabled() {
      return true;
   }

   public abstract boolean isDedicatedServer();

   public boolean isServerInOnlineMode() {
      return this.onlineMode;
   }

   public void setOnlineMode(boolean online) {
      this.onlineMode = online;
   }

   public boolean getCanSpawnAnimals() {
      return this.canSpawnAnimals;
   }

   public void setCanSpawnAnimals(boolean spawnAnimals) {
      this.canSpawnAnimals = spawnAnimals;
   }

   public boolean getCanSpawnNPCs() {
      return this.canSpawnNPCs;
   }

   public abstract boolean func_181035_ah();

   public void setCanSpawnNPCs(boolean spawnNpcs) {
      this.canSpawnNPCs = spawnNpcs;
   }

   public boolean isPVPEnabled() {
      return this.pvpEnabled;
   }

   public void setAllowPvp(boolean allowPvp) {
      this.pvpEnabled = allowPvp;
   }

   public boolean isFlightAllowed() {
      return this.allowFlight;
   }

   public void setAllowFlight(boolean allow) {
      this.allowFlight = allow;
   }

   public abstract boolean isCommandBlockEnabled();

   public String getMOTD() {
      return this.motd;
   }

   public void setMOTD(String motdIn) {
      this.motd = motdIn;
   }

   public int getBuildLimit() {
      return this.buildLimit;
   }

   public void setBuildLimit(int maxBuildHeight) {
      this.buildLimit = maxBuildHeight;
   }

   public boolean isServerStopped() {
      return this.serverStopped;
   }

   public ServerConfigurationManager getConfigurationManager() {
      return this.serverConfigManager;
   }

   public void setConfigManager(ServerConfigurationManager configManager) {
      this.serverConfigManager = configManager;
   }

   public void setGameType(WorldSettings.GameType gameMode) {
      for(int i = 0; i < this.worldServers.length; ++i) {
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

   public abstract String shareToLAN(WorldSettings.GameType var1, boolean var2);

   public int getTickCounter() {
      return this.tickCounter;
   }

   public void enableProfiling() {
      this.startProfiling = true;
   }

   public PlayerUsageSnooper getPlayerUsageSnooper() {
      return this.usageSnooper;
   }

   public BlockPos getPosition() {
      return BlockPos.ORIGIN;
   }

   public Vec3 getPositionVector() {
      return new Vec3(0.0D, 0.0D, 0.0D);
   }

   public World getEntityWorld() {
      return this.worldServers[0];
   }

   public Entity getCommandSenderEntity() {
      return null;
   }

   public int getSpawnProtectionSize() {
      return 16;
   }

   public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn) {
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

   public void setPlayerIdleTimeout(int idleTimeout) {
      this.maxPlayerIdleMinutes = idleTimeout;
   }

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

   public Entity getEntityFromUuid(UUID uuid) {
      WorldServer[] var2 = this.worldServers;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WorldServer worldserver = var2[var4];
         if (worldserver != null) {
            Entity entity = worldserver.getEntityFromUuid(uuid);
            if (entity != null) {
               return entity;
            }
         }
      }

      return null;
   }

   public boolean sendCommandFeedback() {
      return getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
   }

   public void setCommandStat(CommandResultStats.Type type, int amount) {
   }

   public int getMaxWorldSize() {
      return 29999984;
   }

   public ListenableFuture callFromMainThread(Callable callable) {
      Validate.notNull(callable);
      if (!this.isCallingFromMinecraftThread() && !this.isServerStopped()) {
         ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);
         synchronized(this.futureTaskQueue) {
            this.futureTaskQueue.add(listenablefuturetask);
            return listenablefuturetask;
         }
      } else {
         try {
            return Futures.immediateFuture(callable.call());
         } catch (Exception var6) {
            return Futures.immediateFailedCheckedFuture(var6);
         }
      }
   }

   public ListenableFuture addScheduledTask(Runnable runnableToSchedule) {
      Validate.notNull(runnableToSchedule);
      return this.callFromMainThread(Executors.callable(runnableToSchedule));
   }

   public boolean isCallingFromMinecraftThread() {
      return Thread.currentThread() == this.serverThread;
   }

   public int getNetworkCompressionTreshold() {
      return 256;
   }
}
