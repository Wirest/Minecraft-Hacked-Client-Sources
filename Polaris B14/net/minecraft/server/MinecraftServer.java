/*      */ package net.minecraft.server;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import com.google.common.util.concurrent.ListenableFutureTask;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.authlib.GameProfileRepository;
/*      */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*      */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.net.Proxy;
/*      */ import java.security.KeyPair;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Queue;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.FutureTask;
/*      */ import net.minecraft.command.CommandBase;
/*      */ import net.minecraft.command.CommandResultStats.Type;
/*      */ import net.minecraft.command.ICommandManager;
/*      */ import net.minecraft.command.ICommandSender;
/*      */ import net.minecraft.command.ServerCommandManager;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityTracker;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.network.NetworkSystem;
/*      */ import net.minecraft.network.ServerStatusResponse;
/*      */ import net.minecraft.network.ServerStatusResponse.PlayerCountData;
/*      */ import net.minecraft.network.play.server.S03PacketTimeUpdate;
/*      */ import net.minecraft.profiler.IPlayerUsage;
/*      */ import net.minecraft.profiler.PlayerUsageSnooper;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.server.management.PlayerProfileCache;
/*      */ import net.minecraft.server.management.ServerConfigurationManager;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.IProgressUpdate;
/*      */ import net.minecraft.util.IThreadListener;
/*      */ import net.minecraft.util.ITickable;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.GameRules;
/*      */ import net.minecraft.world.MinecraftException;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldManager;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.WorldServerMulti;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ import net.minecraft.world.WorldSettings.GameType;
/*      */ import net.minecraft.world.WorldType;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.minecraft.world.chunk.storage.AnvilSaveConverter;
/*      */ import net.minecraft.world.demo.DemoWorldServer;
/*      */ import net.minecraft.world.gen.ChunkProviderServer;
/*      */ import net.minecraft.world.storage.ISaveFormat;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ 
/*      */ public abstract class MinecraftServer
/*      */   implements Runnable, ICommandSender, IThreadListener, IPlayerUsage
/*      */ {
/*   82 */   private static final Logger logger = ;
/*   83 */   public static final File USER_CACHE_FILE = new File("usercache.json");
/*      */   
/*      */ 
/*      */   private static MinecraftServer mcServer;
/*      */   
/*      */   private final ISaveFormat anvilConverterForAnvilFile;
/*      */   
/*   90 */   private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this, getCurrentTimeMillis());
/*      */   private final File anvilFile;
/*   92 */   private final List<ITickable> playersOnline = Lists.newArrayList();
/*      */   protected final ICommandManager commandManager;
/*   94 */   public final Profiler theProfiler = new Profiler();
/*      */   private final NetworkSystem networkSystem;
/*   96 */   private final ServerStatusResponse statusResponse = new ServerStatusResponse();
/*   97 */   private final Random random = new Random();
/*      */   
/*      */ 
/*  100 */   private int serverPort = -1;
/*      */   
/*      */ 
/*      */ 
/*      */   public WorldServer[] worldServers;
/*      */   
/*      */ 
/*      */ 
/*      */   private ServerConfigurationManager serverConfigManager;
/*      */   
/*      */ 
/*  111 */   private boolean serverRunning = true;
/*      */   
/*      */ 
/*      */   private boolean serverStopped;
/*      */   
/*      */ 
/*      */   private int tickCounter;
/*      */   
/*      */ 
/*      */   protected final Proxy serverProxy;
/*      */   
/*      */ 
/*      */   public String currentTask;
/*      */   
/*      */ 
/*      */   public int percentDone;
/*      */   
/*      */ 
/*      */   private boolean onlineMode;
/*      */   
/*      */ 
/*      */   private boolean canSpawnAnimals;
/*      */   
/*      */ 
/*      */   private boolean canSpawnNPCs;
/*      */   
/*      */ 
/*      */   private boolean pvpEnabled;
/*      */   
/*      */   private boolean allowFlight;
/*      */   
/*      */   private String motd;
/*      */   
/*      */   private int buildLimit;
/*      */   
/*  146 */   private int maxPlayerIdleMinutes = 0;
/*  147 */   public final long[] tickTimeArray = new long[100];
/*      */   
/*      */ 
/*      */   public long[][] timeOfLastDimensionTick;
/*      */   
/*      */   private KeyPair serverKeyPair;
/*      */   
/*      */   private String serverOwner;
/*      */   
/*      */   private String folderName;
/*      */   
/*      */   private String worldName;
/*      */   
/*      */   private boolean isDemo;
/*      */   
/*      */   private boolean enableBonusChest;
/*      */   
/*      */   private boolean worldIsBeingDeleted;
/*      */   
/*  166 */   private String resourcePackUrl = "";
/*  167 */   private String resourcePackHash = "";
/*      */   
/*      */   private boolean serverIsRunning;
/*      */   
/*      */   private long timeOfLastWarning;
/*      */   
/*      */   private String userMessage;
/*      */   
/*      */   private boolean startProfiling;
/*      */   private boolean isGamemodeForced;
/*      */   private final YggdrasilAuthenticationService authService;
/*      */   private final MinecraftSessionService sessionService;
/*  179 */   private long nanoTimeSinceStatusRefresh = 0L;
/*      */   private final GameProfileRepository profileRepo;
/*      */   private final PlayerProfileCache profileCache;
/*  182 */   protected final Queue<FutureTask<?>> futureTaskQueue = Queues.newArrayDeque();
/*      */   private Thread serverThread;
/*  184 */   private long currentTime = getCurrentTimeMillis();
/*      */   
/*      */   public MinecraftServer(Proxy proxy, File workDir)
/*      */   {
/*  188 */     this.serverProxy = proxy;
/*  189 */     mcServer = this;
/*  190 */     this.anvilFile = null;
/*  191 */     this.networkSystem = null;
/*  192 */     this.profileCache = new PlayerProfileCache(this, workDir);
/*  193 */     this.commandManager = null;
/*  194 */     this.anvilConverterForAnvilFile = null;
/*  195 */     this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
/*  196 */     this.sessionService = this.authService.createMinecraftSessionService();
/*  197 */     this.profileRepo = this.authService.createProfileRepository();
/*      */   }
/*      */   
/*      */   public MinecraftServer(File workDir, Proxy proxy, File profileCacheDir)
/*      */   {
/*  202 */     this.serverProxy = proxy;
/*  203 */     mcServer = this;
/*  204 */     this.anvilFile = workDir;
/*  205 */     this.networkSystem = new NetworkSystem(this);
/*  206 */     this.profileCache = new PlayerProfileCache(this, profileCacheDir);
/*  207 */     this.commandManager = createNewCommandManager();
/*  208 */     this.anvilConverterForAnvilFile = new AnvilSaveConverter(workDir);
/*  209 */     this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
/*  210 */     this.sessionService = this.authService.createMinecraftSessionService();
/*  211 */     this.profileRepo = this.authService.createProfileRepository();
/*      */   }
/*      */   
/*      */   protected ServerCommandManager createNewCommandManager()
/*      */   {
/*  216 */     return new ServerCommandManager();
/*      */   }
/*      */   
/*      */ 
/*      */   protected abstract boolean startServer()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */   protected void convertMapIfNeeded(String worldNameIn)
/*      */   {
/*  226 */     if (getActiveAnvilConverter().isOldMapFormat(worldNameIn))
/*      */     {
/*  228 */       logger.info("Converting map!");
/*  229 */       setUserMessage("menu.convertingLevel");
/*  230 */       getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate()
/*      */       {
/*  232 */         private long startTime = System.currentTimeMillis();
/*      */         
/*      */ 
/*      */         public void displaySavingString(String message) {}
/*      */         
/*      */         public void resetProgressAndMessage(String message) {}
/*      */         
/*      */         public void setLoadingProgress(int progress)
/*      */         {
/*  241 */           if (System.currentTimeMillis() - this.startTime >= 1000L)
/*      */           {
/*  243 */             this.startTime = System.currentTimeMillis();
/*  244 */             MinecraftServer.logger.info("Converting... " + progress + "%");
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         public void setDoneWorking() {}
/*      */         
/*      */ 
/*      */ 
/*      */         public void displayLoadingString(String message) {}
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected synchronized void setUserMessage(String message)
/*      */   {
/*  262 */     this.userMessage = message;
/*      */   }
/*      */   
/*      */   public synchronized String getUserMessage()
/*      */   {
/*  267 */     return this.userMessage;
/*      */   }
/*      */   
/*      */   protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_)
/*      */   {
/*  272 */     convertMapIfNeeded(p_71247_1_);
/*  273 */     setUserMessage("menu.loadingLevel");
/*  274 */     this.worldServers = new WorldServer[3];
/*  275 */     this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
/*  276 */     ISaveHandler isavehandler = this.anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
/*  277 */     setResourcePackFromWorld(getFolderName(), isavehandler);
/*  278 */     WorldInfo worldinfo = isavehandler.loadWorldInfo();
/*      */     
/*      */     WorldSettings worldsettings;
/*  281 */     if (worldinfo == null) { WorldSettings worldsettings;
/*      */       WorldSettings worldsettings;
/*  283 */       if (isDemo())
/*      */       {
/*  285 */         worldsettings = DemoWorldServer.demoWorldSettings;
/*      */       }
/*      */       else
/*      */       {
/*  289 */         worldsettings = new WorldSettings(seed, getGameType(), canStructuresSpawn(), isHardcore(), type);
/*  290 */         worldsettings.setWorldName(p_71247_6_);
/*      */         
/*  292 */         if (this.enableBonusChest)
/*      */         {
/*  294 */           worldsettings.enableBonusChest();
/*      */         }
/*      */       }
/*      */       
/*  298 */       worldinfo = new WorldInfo(worldsettings, p_71247_2_);
/*      */     }
/*      */     else
/*      */     {
/*  302 */       worldinfo.setWorldName(p_71247_2_);
/*  303 */       worldsettings = new WorldSettings(worldinfo);
/*      */     }
/*      */     
/*  306 */     for (int i = 0; i < this.worldServers.length; i++)
/*      */     {
/*  308 */       int j = 0;
/*      */       
/*  310 */       if (i == 1)
/*      */       {
/*  312 */         j = -1;
/*      */       }
/*      */       
/*  315 */       if (i == 2)
/*      */       {
/*  317 */         j = 1;
/*      */       }
/*      */       
/*  320 */       if (i == 0)
/*      */       {
/*  322 */         if (isDemo())
/*      */         {
/*  324 */           this.worldServers[i] = ((WorldServer)new DemoWorldServer(this, isavehandler, worldinfo, j, this.theProfiler).init());
/*      */         }
/*      */         else
/*      */         {
/*  328 */           this.worldServers[i] = ((WorldServer)new WorldServer(this, isavehandler, worldinfo, j, this.theProfiler).init());
/*      */         }
/*      */         
/*  331 */         this.worldServers[i].initialize(worldsettings);
/*      */       }
/*      */       else
/*      */       {
/*  335 */         this.worldServers[i] = ((WorldServer)new WorldServerMulti(this, isavehandler, j, this.worldServers[0], this.theProfiler).init());
/*      */       }
/*      */       
/*  338 */       this.worldServers[i].addWorldAccess(new WorldManager(this, this.worldServers[i]));
/*      */       
/*  340 */       if (!isSinglePlayer())
/*      */       {
/*  342 */         this.worldServers[i].getWorldInfo().setGameType(getGameType());
/*      */       }
/*      */     }
/*      */     
/*  346 */     this.serverConfigManager.setPlayerManager(this.worldServers);
/*  347 */     setDifficultyForAllWorlds(getDifficulty());
/*  348 */     initialWorldChunkLoad();
/*      */   }
/*      */   
/*      */   protected void initialWorldChunkLoad()
/*      */   {
/*  353 */     int i = 16;
/*  354 */     int j = 4;
/*  355 */     int k = 192;
/*  356 */     int l = 625;
/*  357 */     int i1 = 0;
/*  358 */     setUserMessage("menu.generatingTerrain");
/*  359 */     int j1 = 0;
/*  360 */     logger.info("Preparing start region for level " + j1);
/*  361 */     WorldServer worldserver = this.worldServers[j1];
/*  362 */     BlockPos blockpos = worldserver.getSpawnPoint();
/*  363 */     long k1 = getCurrentTimeMillis();
/*      */     
/*  365 */     for (int l1 = 65344; (l1 <= 192) && (isServerRunning()); l1 += 16)
/*      */     {
/*  367 */       for (int i2 = 65344; (i2 <= 192) && (isServerRunning()); i2 += 16)
/*      */       {
/*  369 */         long j2 = getCurrentTimeMillis();
/*      */         
/*  371 */         if (j2 - k1 > 1000L)
/*      */         {
/*  373 */           outputPercentRemaining("Preparing spawn area", i1 * 100 / 625);
/*  374 */           k1 = j2;
/*      */         }
/*      */         
/*  377 */         i1++;
/*  378 */         worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + l1 >> 4, blockpos.getZ() + i2 >> 4);
/*      */       }
/*      */     }
/*      */     
/*  382 */     clearCurrentTask();
/*      */   }
/*      */   
/*      */   protected void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn)
/*      */   {
/*  387 */     File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");
/*      */     
/*  389 */     if (file1.isFile())
/*      */     {
/*  391 */       setResourcePack("level://" + worldNameIn + "/" + file1.getName(), "");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract boolean canStructuresSpawn();
/*      */   
/*      */ 
/*      */   public abstract WorldSettings.GameType getGameType();
/*      */   
/*      */ 
/*      */   public abstract EnumDifficulty getDifficulty();
/*      */   
/*      */ 
/*      */   public abstract boolean isHardcore();
/*      */   
/*      */ 
/*      */   public abstract int getOpPermissionLevel();
/*      */   
/*      */ 
/*      */   public abstract boolean func_181034_q();
/*      */   
/*      */ 
/*      */   public abstract boolean func_183002_r();
/*      */   
/*      */ 
/*      */   protected void outputPercentRemaining(String message, int percent)
/*      */   {
/*  420 */     this.currentTask = message;
/*  421 */     this.percentDone = percent;
/*  422 */     logger.info(message + ": " + percent + "%");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void clearCurrentTask()
/*      */   {
/*  430 */     this.currentTask = null;
/*  431 */     this.percentDone = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void saveAllWorlds(boolean dontLog)
/*      */   {
/*  439 */     if (!this.worldIsBeingDeleted) {
/*      */       WorldServer[] arrayOfWorldServer;
/*  441 */       int j = (arrayOfWorldServer = this.worldServers).length; for (int i = 0; i < j; i++) { WorldServer worldserver = arrayOfWorldServer[i];
/*      */         
/*  443 */         if (worldserver != null)
/*      */         {
/*  445 */           if (!dontLog)
/*      */           {
/*  447 */             logger.info("Saving chunks for level '" + worldserver.getWorldInfo().getWorldName() + "'/" + worldserver.provider.getDimensionName());
/*      */           }
/*      */           
/*      */           try
/*      */           {
/*  452 */             worldserver.saveAllChunks(true, null);
/*      */           }
/*      */           catch (MinecraftException minecraftexception)
/*      */           {
/*  456 */             logger.warn(minecraftexception.getMessage());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void stopServer()
/*      */   {
/*  468 */     if (!this.worldIsBeingDeleted)
/*      */     {
/*  470 */       logger.info("Stopping server");
/*      */       
/*  472 */       if (getNetworkSystem() != null)
/*      */       {
/*  474 */         getNetworkSystem().terminateEndpoints();
/*      */       }
/*      */       
/*  477 */       if (this.serverConfigManager != null)
/*      */       {
/*  479 */         logger.info("Saving players");
/*  480 */         this.serverConfigManager.saveAllPlayerData();
/*  481 */         this.serverConfigManager.removeAllPlayers();
/*      */       }
/*      */       
/*  484 */       if (this.worldServers != null)
/*      */       {
/*  486 */         logger.info("Saving worlds");
/*  487 */         saveAllWorlds(false);
/*      */         
/*  489 */         for (int i = 0; i < this.worldServers.length; i++)
/*      */         {
/*  491 */           WorldServer worldserver = this.worldServers[i];
/*  492 */           worldserver.flush();
/*      */         }
/*      */       }
/*      */       
/*  496 */       if (this.usageSnooper.isSnooperRunning())
/*      */       {
/*  498 */         this.usageSnooper.stopSnooper();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isServerRunning()
/*      */   {
/*  505 */     return this.serverRunning;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void initiateShutdown()
/*      */   {
/*  513 */     this.serverRunning = false;
/*      */   }
/*      */   
/*      */   protected void setInstance()
/*      */   {
/*  518 */     mcServer = this;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void run()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 630	net/minecraft/server/MinecraftServer:startServer	()Z
/*      */     //   4: ifeq +242 -> 246
/*      */     //   7: aload_0
/*      */     //   8: invokestatic 150	net/minecraft/server/MinecraftServer:getCurrentTimeMillis	()J
/*      */     //   11: putfield 202	net/minecraft/server/MinecraftServer:currentTime	J
/*      */     //   14: lconst_0
/*      */     //   15: lstore_1
/*      */     //   16: aload_0
/*      */     //   17: getfield 171	net/minecraft/server/MinecraftServer:statusResponse	Lnet/minecraft/network/ServerStatusResponse;
/*      */     //   20: new 632	net/minecraft/util/ChatComponentText
/*      */     //   23: dup
/*      */     //   24: aload_0
/*      */     //   25: getfield 634	net/minecraft/server/MinecraftServer:motd	Ljava/lang/String;
/*      */     //   28: invokespecial 635	net/minecraft/util/ChatComponentText:<init>	(Ljava/lang/String;)V
/*      */     //   31: invokevirtual 639	net/minecraft/network/ServerStatusResponse:setServerDescription	(Lnet/minecraft/util/IChatComponent;)V
/*      */     //   34: aload_0
/*      */     //   35: getfield 171	net/minecraft/server/MinecraftServer:statusResponse	Lnet/minecraft/network/ServerStatusResponse;
/*      */     //   38: new 20	net/minecraft/network/ServerStatusResponse$MinecraftProtocolVersionIdentifier
/*      */     //   41: dup
/*      */     //   42: ldc_w 641
/*      */     //   45: bipush 47
/*      */     //   47: invokespecial 643	net/minecraft/network/ServerStatusResponse$MinecraftProtocolVersionIdentifier:<init>	(Ljava/lang/String;I)V
/*      */     //   50: invokevirtual 647	net/minecraft/network/ServerStatusResponse:setProtocolVersionInfo	(Lnet/minecraft/network/ServerStatusResponse$MinecraftProtocolVersionIdentifier;)V
/*      */     //   53: aload_0
/*      */     //   54: aload_0
/*      */     //   55: getfield 171	net/minecraft/server/MinecraftServer:statusResponse	Lnet/minecraft/network/ServerStatusResponse;
/*      */     //   58: invokespecial 651	net/minecraft/server/MinecraftServer:addFaviconToStatusResponse	(Lnet/minecraft/network/ServerStatusResponse;)V
/*      */     //   61: goto +175 -> 236
/*      */     //   64: invokestatic 150	net/minecraft/server/MinecraftServer:getCurrentTimeMillis	()J
/*      */     //   67: lstore_3
/*      */     //   68: lload_3
/*      */     //   69: aload_0
/*      */     //   70: getfield 202	net/minecraft/server/MinecraftServer:currentTime	J
/*      */     //   73: lsub
/*      */     //   74: lstore 5
/*      */     //   76: lload 5
/*      */     //   78: ldc2_w 652
/*      */     //   81: lcmp
/*      */     //   82: ifle +67 -> 149
/*      */     //   85: aload_0
/*      */     //   86: getfield 202	net/minecraft/server/MinecraftServer:currentTime	J
/*      */     //   89: aload_0
/*      */     //   90: getfield 655	net/minecraft/server/MinecraftServer:timeOfLastWarning	J
/*      */     //   93: lsub
/*      */     //   94: ldc2_w 656
/*      */     //   97: lcmp
/*      */     //   98: iflt +51 -> 149
/*      */     //   101: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   104: ldc_w 659
/*      */     //   107: iconst_2
/*      */     //   108: anewarray 4	java/lang/Object
/*      */     //   111: dup
/*      */     //   112: iconst_0
/*      */     //   113: lload 5
/*      */     //   115: invokestatic 664	java/lang/Long:valueOf	(J)Ljava/lang/Long;
/*      */     //   118: aastore
/*      */     //   119: dup
/*      */     //   120: iconst_1
/*      */     //   121: lload 5
/*      */     //   123: ldc2_w 665
/*      */     //   126: ldiv
/*      */     //   127: invokestatic 664	java/lang/Long:valueOf	(J)Ljava/lang/Long;
/*      */     //   130: aastore
/*      */     //   131: invokeinterface 669 3 0
/*      */     //   136: ldc2_w 652
/*      */     //   139: lstore 5
/*      */     //   141: aload_0
/*      */     //   142: aload_0
/*      */     //   143: getfield 202	net/minecraft/server/MinecraftServer:currentTime	J
/*      */     //   146: putfield 655	net/minecraft/server/MinecraftServer:timeOfLastWarning	J
/*      */     //   149: lload 5
/*      */     //   151: lconst_0
/*      */     //   152: lcmp
/*      */     //   153: ifge +17 -> 170
/*      */     //   156: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   159: ldc_w 671
/*      */     //   162: invokeinterface 589 2 0
/*      */     //   167: lconst_0
/*      */     //   168: lstore 5
/*      */     //   170: lload_1
/*      */     //   171: lload 5
/*      */     //   173: ladd
/*      */     //   174: lstore_1
/*      */     //   175: aload_0
/*      */     //   176: lload_3
/*      */     //   177: putfield 202	net/minecraft/server/MinecraftServer:currentTime	J
/*      */     //   180: aload_0
/*      */     //   181: getfield 319	net/minecraft/server/MinecraftServer:worldServers	[Lnet/minecraft/world/WorldServer;
/*      */     //   184: iconst_0
/*      */     //   185: aaload
/*      */     //   186: invokevirtual 674	net/minecraft/world/WorldServer:areAllPlayersAsleep	()Z
/*      */     //   189: ifeq +22 -> 211
/*      */     //   192: aload_0
/*      */     //   193: invokevirtual 677	net/minecraft/server/MinecraftServer:tick	()V
/*      */     //   196: lconst_0
/*      */     //   197: lstore_1
/*      */     //   198: goto +21 -> 219
/*      */     //   201: lload_1
/*      */     //   202: ldc2_w 665
/*      */     //   205: lsub
/*      */     //   206: lstore_1
/*      */     //   207: aload_0
/*      */     //   208: invokevirtual 677	net/minecraft/server/MinecraftServer:tick	()V
/*      */     //   211: lload_1
/*      */     //   212: ldc2_w 665
/*      */     //   215: lcmp
/*      */     //   216: ifgt -15 -> 201
/*      */     //   219: lconst_1
/*      */     //   220: ldc2_w 665
/*      */     //   223: lload_1
/*      */     //   224: lsub
/*      */     //   225: invokestatic 683	java/lang/Math:max	(JJ)J
/*      */     //   228: invokestatic 689	java/lang/Thread:sleep	(J)V
/*      */     //   231: aload_0
/*      */     //   232: iconst_1
/*      */     //   233: putfield 691	net/minecraft/server/MinecraftServer:serverIsRunning	Z
/*      */     //   236: aload_0
/*      */     //   237: getfield 180	net/minecraft/server/MinecraftServer:serverRunning	Z
/*      */     //   240: ifne -176 -> 64
/*      */     //   243: goto +285 -> 528
/*      */     //   246: aload_0
/*      */     //   247: aconst_null
/*      */     //   248: invokevirtual 695	net/minecraft/server/MinecraftServer:finalTick	(Lnet/minecraft/crash/CrashReport;)V
/*      */     //   251: goto +277 -> 528
/*      */     //   254: astore_1
/*      */     //   255: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   258: ldc_w 697
/*      */     //   261: aload_1
/*      */     //   262: invokeinterface 701 3 0
/*      */     //   267: aconst_null
/*      */     //   268: astore_2
/*      */     //   269: aload_1
/*      */     //   270: instanceof 703
/*      */     //   273: ifeq +18 -> 291
/*      */     //   276: aload_0
/*      */     //   277: aload_1
/*      */     //   278: checkcast 703	net/minecraft/util/ReportedException
/*      */     //   281: invokevirtual 707	net/minecraft/util/ReportedException:getCrashReport	()Lnet/minecraft/crash/CrashReport;
/*      */     //   284: invokevirtual 711	net/minecraft/server/MinecraftServer:addServerInfoToCrashReport	(Lnet/minecraft/crash/CrashReport;)Lnet/minecraft/crash/CrashReport;
/*      */     //   287: astore_2
/*      */     //   288: goto +19 -> 307
/*      */     //   291: aload_0
/*      */     //   292: new 713	net/minecraft/crash/CrashReport
/*      */     //   295: dup
/*      */     //   296: ldc_w 715
/*      */     //   299: aload_1
/*      */     //   300: invokespecial 717	net/minecraft/crash/CrashReport:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*      */     //   303: invokevirtual 711	net/minecraft/server/MinecraftServer:addServerInfoToCrashReport	(Lnet/minecraft/crash/CrashReport;)Lnet/minecraft/crash/CrashReport;
/*      */     //   306: astore_2
/*      */     //   307: new 131	java/io/File
/*      */     //   310: dup
/*      */     //   311: new 131	java/io/File
/*      */     //   314: dup
/*      */     //   315: aload_0
/*      */     //   316: invokevirtual 720	net/minecraft/server/MinecraftServer:getDataDirectory	()Ljava/io/File;
/*      */     //   319: ldc_w 722
/*      */     //   322: invokespecial 519	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
/*      */     //   325: new 453	java/lang/StringBuilder
/*      */     //   328: dup
/*      */     //   329: ldc_w 724
/*      */     //   332: invokespecial 456	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   335: new 726	java/text/SimpleDateFormat
/*      */     //   338: dup
/*      */     //   339: ldc_w 728
/*      */     //   342: invokespecial 729	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
/*      */     //   345: new 731	java/util/Date
/*      */     //   348: dup
/*      */     //   349: invokespecial 732	java/util/Date:<init>	()V
/*      */     //   352: invokevirtual 736	java/text/SimpleDateFormat:format	(Ljava/util/Date;)Ljava/lang/String;
/*      */     //   355: invokevirtual 527	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   358: ldc_w 738
/*      */     //   361: invokevirtual 527	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   364: invokevirtual 461	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   367: invokespecial 519	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
/*      */     //   370: astore_3
/*      */     //   371: aload_2
/*      */     //   372: aload_3
/*      */     //   373: invokevirtual 742	net/minecraft/crash/CrashReport:saveToFile	(Ljava/io/File;)Z
/*      */     //   376: ifeq +34 -> 410
/*      */     //   379: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   382: new 453	java/lang/StringBuilder
/*      */     //   385: dup
/*      */     //   386: ldc_w 744
/*      */     //   389: invokespecial 456	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   392: aload_3
/*      */     //   393: invokevirtual 747	java/io/File:getAbsolutePath	()Ljava/lang/String;
/*      */     //   396: invokevirtual 527	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   399: invokevirtual 461	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   402: invokeinterface 749 2 0
/*      */     //   407: goto +14 -> 421
/*      */     //   410: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   413: ldc_w 751
/*      */     //   416: invokeinterface 749 2 0
/*      */     //   421: aload_0
/*      */     //   422: aload_2
/*      */     //   423: invokevirtual 695	net/minecraft/server/MinecraftServer:finalTick	(Lnet/minecraft/crash/CrashReport;)V
/*      */     //   426: aload_0
/*      */     //   427: iconst_1
/*      */     //   428: putfield 753	net/minecraft/server/MinecraftServer:serverStopped	Z
/*      */     //   431: aload_0
/*      */     //   432: invokevirtual 755	net/minecraft/server/MinecraftServer:stopServer	()V
/*      */     //   435: goto +34 -> 469
/*      */     //   438: astore 8
/*      */     //   440: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   443: ldc_w 757
/*      */     //   446: aload 8
/*      */     //   448: invokeinterface 701 3 0
/*      */     //   453: aload_0
/*      */     //   454: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   457: goto +118 -> 575
/*      */     //   460: astore 9
/*      */     //   462: aload_0
/*      */     //   463: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   466: aload 9
/*      */     //   468: athrow
/*      */     //   469: aload_0
/*      */     //   470: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   473: goto +102 -> 575
/*      */     //   476: astore 7
/*      */     //   478: aload_0
/*      */     //   479: iconst_1
/*      */     //   480: putfield 753	net/minecraft/server/MinecraftServer:serverStopped	Z
/*      */     //   483: aload_0
/*      */     //   484: invokevirtual 755	net/minecraft/server/MinecraftServer:stopServer	()V
/*      */     //   487: goto +34 -> 521
/*      */     //   490: astore 8
/*      */     //   492: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   495: ldc_w 757
/*      */     //   498: aload 8
/*      */     //   500: invokeinterface 701 3 0
/*      */     //   505: aload_0
/*      */     //   506: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   509: goto +16 -> 525
/*      */     //   512: astore 9
/*      */     //   514: aload_0
/*      */     //   515: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   518: aload 9
/*      */     //   520: athrow
/*      */     //   521: aload_0
/*      */     //   522: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   525: aload 7
/*      */     //   527: athrow
/*      */     //   528: aload_0
/*      */     //   529: iconst_1
/*      */     //   530: putfield 753	net/minecraft/server/MinecraftServer:serverStopped	Z
/*      */     //   533: aload_0
/*      */     //   534: invokevirtual 755	net/minecraft/server/MinecraftServer:stopServer	()V
/*      */     //   537: goto +34 -> 571
/*      */     //   540: astore 8
/*      */     //   542: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   545: ldc_w 757
/*      */     //   548: aload 8
/*      */     //   550: invokeinterface 701 3 0
/*      */     //   555: aload_0
/*      */     //   556: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   559: goto +16 -> 575
/*      */     //   562: astore 9
/*      */     //   564: aload_0
/*      */     //   565: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   568: aload 9
/*      */     //   570: athrow
/*      */     //   571: aload_0
/*      */     //   572: invokevirtual 760	net/minecraft/server/MinecraftServer:systemExitNow	()V
/*      */     //   575: return
/*      */     // Line number table:
/*      */     //   Java source line #525	-> byte code offset #0
/*      */     //   Java source line #527	-> byte code offset #7
/*      */     //   Java source line #528	-> byte code offset #14
/*      */     //   Java source line #529	-> byte code offset #16
/*      */     //   Java source line #530	-> byte code offset #34
/*      */     //   Java source line #531	-> byte code offset #53
/*      */     //   Java source line #533	-> byte code offset #61
/*      */     //   Java source line #535	-> byte code offset #64
/*      */     //   Java source line #536	-> byte code offset #68
/*      */     //   Java source line #538	-> byte code offset #76
/*      */     //   Java source line #540	-> byte code offset #101
/*      */     //   Java source line #541	-> byte code offset #136
/*      */     //   Java source line #542	-> byte code offset #141
/*      */     //   Java source line #545	-> byte code offset #149
/*      */     //   Java source line #547	-> byte code offset #156
/*      */     //   Java source line #548	-> byte code offset #167
/*      */     //   Java source line #551	-> byte code offset #170
/*      */     //   Java source line #552	-> byte code offset #175
/*      */     //   Java source line #554	-> byte code offset #180
/*      */     //   Java source line #556	-> byte code offset #192
/*      */     //   Java source line #557	-> byte code offset #196
/*      */     //   Java source line #558	-> byte code offset #198
/*      */     //   Java source line #563	-> byte code offset #201
/*      */     //   Java source line #564	-> byte code offset #207
/*      */     //   Java source line #561	-> byte code offset #211
/*      */     //   Java source line #568	-> byte code offset #219
/*      */     //   Java source line #569	-> byte code offset #231
/*      */     //   Java source line #533	-> byte code offset #236
/*      */     //   Java source line #571	-> byte code offset #243
/*      */     //   Java source line #574	-> byte code offset #246
/*      */     //   Java source line #576	-> byte code offset #251
/*      */     //   Java source line #577	-> byte code offset #254
/*      */     //   Java source line #579	-> byte code offset #255
/*      */     //   Java source line #580	-> byte code offset #267
/*      */     //   Java source line #582	-> byte code offset #269
/*      */     //   Java source line #584	-> byte code offset #276
/*      */     //   Java source line #585	-> byte code offset #288
/*      */     //   Java source line #588	-> byte code offset #291
/*      */     //   Java source line #591	-> byte code offset #307
/*      */     //   Java source line #593	-> byte code offset #371
/*      */     //   Java source line #595	-> byte code offset #379
/*      */     //   Java source line #596	-> byte code offset #407
/*      */     //   Java source line #599	-> byte code offset #410
/*      */     //   Java source line #602	-> byte code offset #421
/*      */     //   Java source line #608	-> byte code offset #426
/*      */     //   Java source line #609	-> byte code offset #431
/*      */     //   Java source line #610	-> byte code offset #435
/*      */     //   Java source line #611	-> byte code offset #438
/*      */     //   Java source line #613	-> byte code offset #440
/*      */     //   Java source line #617	-> byte code offset #453
/*      */     //   Java source line #616	-> byte code offset #460
/*      */     //   Java source line #617	-> byte code offset #462
/*      */     //   Java source line #618	-> byte code offset #466
/*      */     //   Java source line #617	-> byte code offset #469
/*      */     //   Java source line #618	-> byte code offset #473
/*      */     //   Java source line #605	-> byte code offset #476
/*      */     //   Java source line #608	-> byte code offset #478
/*      */     //   Java source line #609	-> byte code offset #483
/*      */     //   Java source line #610	-> byte code offset #487
/*      */     //   Java source line #611	-> byte code offset #490
/*      */     //   Java source line #613	-> byte code offset #492
/*      */     //   Java source line #617	-> byte code offset #505
/*      */     //   Java source line #616	-> byte code offset #512
/*      */     //   Java source line #617	-> byte code offset #514
/*      */     //   Java source line #618	-> byte code offset #518
/*      */     //   Java source line #617	-> byte code offset #521
/*      */     //   Java source line #619	-> byte code offset #525
/*      */     //   Java source line #608	-> byte code offset #528
/*      */     //   Java source line #609	-> byte code offset #533
/*      */     //   Java source line #610	-> byte code offset #537
/*      */     //   Java source line #611	-> byte code offset #540
/*      */     //   Java source line #613	-> byte code offset #542
/*      */     //   Java source line #617	-> byte code offset #555
/*      */     //   Java source line #616	-> byte code offset #562
/*      */     //   Java source line #617	-> byte code offset #564
/*      */     //   Java source line #618	-> byte code offset #568
/*      */     //   Java source line #617	-> byte code offset #571
/*      */     //   Java source line #620	-> byte code offset #575
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	576	0	this	MinecraftServer
/*      */     //   15	209	1	i	long
/*      */     //   254	46	1	throwable1	Throwable
/*      */     //   268	155	2	crashreport	CrashReport
/*      */     //   67	110	3	k	long
/*      */     //   370	23	3	file1	File
/*      */     //   74	98	5	j	long
/*      */     //   476	50	7	localObject1	Object
/*      */     //   438	9	8	throwable	Throwable
/*      */     //   490	9	8	throwable	Throwable
/*      */     //   540	9	8	throwable	Throwable
/*      */     //   460	7	9	localObject2	Object
/*      */     //   512	7	9	localObject3	Object
/*      */     //   562	7	9	localObject4	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	251	254	java/lang/Throwable
/*      */     //   426	435	438	java/lang/Throwable
/*      */     //   426	453	460	finally
/*      */     //   0	426	476	finally
/*      */     //   478	487	490	java/lang/Throwable
/*      */     //   478	505	512	finally
/*      */     //   528	537	540	java/lang/Throwable
/*      */     //   528	555	562	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private void addFaviconToStatusResponse(ServerStatusResponse response)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: ldc_w 769
/*      */     //   4: invokevirtual 773	net/minecraft/server/MinecraftServer:getFile	(Ljava/lang/String;)Ljava/io/File;
/*      */     //   7: astore_2
/*      */     //   8: aload_2
/*      */     //   9: invokevirtual 522	java/io/File:isFile	()Z
/*      */     //   12: ifeq +155 -> 167
/*      */     //   15: invokestatic 779	io/netty/buffer/Unpooled:buffer	()Lio/netty/buffer/ByteBuf;
/*      */     //   18: astore_3
/*      */     //   19: aload_2
/*      */     //   20: invokestatic 785	javax/imageio/ImageIO:read	(Ljava/io/File;)Ljava/awt/image/BufferedImage;
/*      */     //   23: astore 4
/*      */     //   25: aload 4
/*      */     //   27: invokevirtual 790	java/awt/image/BufferedImage:getWidth	()I
/*      */     //   30: bipush 64
/*      */     //   32: if_icmpne +7 -> 39
/*      */     //   35: iconst_1
/*      */     //   36: goto +4 -> 40
/*      */     //   39: iconst_0
/*      */     //   40: ldc_w 794
/*      */     //   43: iconst_0
/*      */     //   44: anewarray 4	java/lang/Object
/*      */     //   47: invokestatic 800	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
/*      */     //   50: aload 4
/*      */     //   52: invokevirtual 803	java/awt/image/BufferedImage:getHeight	()I
/*      */     //   55: bipush 64
/*      */     //   57: if_icmpne +7 -> 64
/*      */     //   60: iconst_1
/*      */     //   61: goto +4 -> 65
/*      */     //   64: iconst_0
/*      */     //   65: ldc_w 805
/*      */     //   68: iconst_0
/*      */     //   69: anewarray 4	java/lang/Object
/*      */     //   72: invokestatic 800	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
/*      */     //   75: aload 4
/*      */     //   77: ldc_w 807
/*      */     //   80: new 809	io/netty/buffer/ByteBufOutputStream
/*      */     //   83: dup
/*      */     //   84: aload_3
/*      */     //   85: invokespecial 812	io/netty/buffer/ByteBufOutputStream:<init>	(Lio/netty/buffer/ByteBuf;)V
/*      */     //   88: invokestatic 816	javax/imageio/ImageIO:write	(Ljava/awt/image/RenderedImage;Ljava/lang/String;Ljava/io/OutputStream;)Z
/*      */     //   91: pop
/*      */     //   92: aload_3
/*      */     //   93: invokestatic 822	io/netty/handler/codec/base64/Base64:encode	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
/*      */     //   96: astore 5
/*      */     //   98: aload_1
/*      */     //   99: new 453	java/lang/StringBuilder
/*      */     //   102: dup
/*      */     //   103: ldc_w 824
/*      */     //   106: invokespecial 456	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   109: aload 5
/*      */     //   111: getstatic 830	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*      */     //   114: invokevirtual 833	io/netty/buffer/ByteBuf:toString	(Ljava/nio/charset/Charset;)Ljava/lang/String;
/*      */     //   117: invokevirtual 527	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   120: invokevirtual 461	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   123: invokevirtual 836	net/minecraft/network/ServerStatusResponse:setFavicon	(Ljava/lang/String;)V
/*      */     //   126: goto +36 -> 162
/*      */     //   129: astore 4
/*      */     //   131: getstatic 129	net/minecraft/server/MinecraftServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   134: ldc_w 838
/*      */     //   137: aload 4
/*      */     //   139: invokeinterface 701 3 0
/*      */     //   144: aload_3
/*      */     //   145: invokevirtual 841	io/netty/buffer/ByteBuf:release	()Z
/*      */     //   148: pop
/*      */     //   149: goto +18 -> 167
/*      */     //   152: astore 6
/*      */     //   154: aload_3
/*      */     //   155: invokevirtual 841	io/netty/buffer/ByteBuf:release	()Z
/*      */     //   158: pop
/*      */     //   159: aload 6
/*      */     //   161: athrow
/*      */     //   162: aload_3
/*      */     //   163: invokevirtual 841	io/netty/buffer/ByteBuf:release	()Z
/*      */     //   166: pop
/*      */     //   167: return
/*      */     // Line number table:
/*      */     //   Java source line #624	-> byte code offset #0
/*      */     //   Java source line #626	-> byte code offset #8
/*      */     //   Java source line #628	-> byte code offset #15
/*      */     //   Java source line #632	-> byte code offset #19
/*      */     //   Java source line #633	-> byte code offset #25
/*      */     //   Java source line #634	-> byte code offset #50
/*      */     //   Java source line #635	-> byte code offset #75
/*      */     //   Java source line #636	-> byte code offset #92
/*      */     //   Java source line #637	-> byte code offset #98
/*      */     //   Java source line #638	-> byte code offset #126
/*      */     //   Java source line #639	-> byte code offset #129
/*      */     //   Java source line #641	-> byte code offset #131
/*      */     //   Java source line #645	-> byte code offset #144
/*      */     //   Java source line #644	-> byte code offset #152
/*      */     //   Java source line #645	-> byte code offset #154
/*      */     //   Java source line #646	-> byte code offset #159
/*      */     //   Java source line #645	-> byte code offset #162
/*      */     //   Java source line #648	-> byte code offset #167
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	168	0	this	MinecraftServer
/*      */     //   0	168	1	response	ServerStatusResponse
/*      */     //   7	13	2	file1	File
/*      */     //   18	145	3	bytebuf	io.netty.buffer.ByteBuf
/*      */     //   23	53	4	bufferedimage	java.awt.image.BufferedImage
/*      */     //   129	9	4	exception	Exception
/*      */     //   96	14	5	bytebuf1	io.netty.buffer.ByteBuf
/*      */     //   152	8	6	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   19	126	129	java/lang/Exception
/*      */     //   19	144	152	finally
/*      */   }
/*      */   
/*      */   public File getDataDirectory()
/*      */   {
/*  652 */     return new File(".");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void finalTick(CrashReport report) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void systemExitNow() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void tick()
/*      */   {
/*  674 */     long i = System.nanoTime();
/*  675 */     this.tickCounter += 1;
/*      */     
/*  677 */     if (this.startProfiling)
/*      */     {
/*  679 */       this.startProfiling = false;
/*  680 */       this.theProfiler.profilingEnabled = true;
/*  681 */       this.theProfiler.clearProfiling();
/*      */     }
/*      */     
/*  684 */     this.theProfiler.startSection("root");
/*  685 */     updateTimeLightAndEntities();
/*      */     
/*  687 */     if (i - this.nanoTimeSinceStatusRefresh >= 5000000000L)
/*      */     {
/*  689 */       this.nanoTimeSinceStatusRefresh = i;
/*  690 */       this.statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(getMaxPlayers(), getCurrentPlayerCount()));
/*  691 */       GameProfile[] agameprofile = new GameProfile[Math.min(getCurrentPlayerCount(), 12)];
/*  692 */       int j = MathHelper.getRandomIntegerInRange(this.random, 0, getCurrentPlayerCount() - agameprofile.length);
/*      */       
/*  694 */       for (int k = 0; k < agameprofile.length; k++)
/*      */       {
/*  696 */         agameprofile[k] = ((EntityPlayerMP)this.serverConfigManager.func_181057_v().get(j + k)).getGameProfile();
/*      */       }
/*      */       
/*  699 */       Collections.shuffle(Arrays.asList(agameprofile));
/*  700 */       this.statusResponse.getPlayerCountData().setPlayers(agameprofile);
/*      */     }
/*      */     
/*  703 */     if (this.tickCounter % 900 == 0)
/*      */     {
/*  705 */       this.theProfiler.startSection("save");
/*  706 */       this.serverConfigManager.saveAllPlayerData();
/*  707 */       saveAllWorlds(true);
/*  708 */       this.theProfiler.endSection();
/*      */     }
/*      */     
/*  711 */     this.theProfiler.startSection("tallying");
/*  712 */     this.tickTimeArray[(this.tickCounter % 100)] = (System.nanoTime() - i);
/*  713 */     this.theProfiler.endSection();
/*  714 */     this.theProfiler.startSection("snooper");
/*      */     
/*  716 */     if ((!this.usageSnooper.isSnooperRunning()) && (this.tickCounter > 100))
/*      */     {
/*  718 */       this.usageSnooper.startSnooper();
/*      */     }
/*      */     
/*  721 */     if (this.tickCounter % 6000 == 0)
/*      */     {
/*  723 */       this.usageSnooper.addMemoryStatsToSnooper();
/*      */     }
/*      */     
/*  726 */     this.theProfiler.endSection();
/*  727 */     this.theProfiler.endSection();
/*      */   }
/*      */   
/*      */   public void updateTimeLightAndEntities()
/*      */   {
/*  732 */     this.theProfiler.startSection("jobs");
/*      */     
/*  734 */     synchronized (this.futureTaskQueue)
/*      */     {
/*  736 */       while (!this.futureTaskQueue.isEmpty())
/*      */       {
/*  738 */         Util.func_181617_a((FutureTask)this.futureTaskQueue.poll(), logger);
/*      */       }
/*      */     }
/*      */     
/*  742 */     this.theProfiler.endStartSection("levels");
/*      */     
/*  744 */     for (int j = 0; j < this.worldServers.length; j++)
/*      */     {
/*  746 */       long i = System.nanoTime();
/*      */       
/*  748 */       if ((j == 0) || (getAllowNether()))
/*      */       {
/*  750 */         WorldServer worldserver = this.worldServers[j];
/*  751 */         this.theProfiler.startSection(worldserver.getWorldInfo().getWorldName());
/*      */         
/*  753 */         if (this.tickCounter % 20 == 0)
/*      */         {
/*  755 */           this.theProfiler.startSection("timeSync");
/*  756 */           this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimensionId());
/*  757 */           this.theProfiler.endSection();
/*      */         }
/*      */         
/*  760 */         this.theProfiler.startSection("tick");
/*      */         
/*      */         try
/*      */         {
/*  764 */           worldserver.tick();
/*      */         }
/*      */         catch (Throwable throwable1)
/*      */         {
/*  768 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
/*  769 */           worldserver.addWorldInfoToCrashReport(crashreport);
/*  770 */           throw new ReportedException(crashreport);
/*      */         }
/*      */         
/*      */         try
/*      */         {
/*  775 */           worldserver.updateEntities();
/*      */         }
/*      */         catch (Throwable throwable)
/*      */         {
/*  779 */           CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
/*  780 */           worldserver.addWorldInfoToCrashReport(crashreport1);
/*  781 */           throw new ReportedException(crashreport1);
/*      */         }
/*      */         
/*  784 */         this.theProfiler.endSection();
/*  785 */         this.theProfiler.startSection("tracker");
/*  786 */         worldserver.getEntityTracker().updateTrackedEntities();
/*  787 */         this.theProfiler.endSection();
/*  788 */         this.theProfiler.endSection();
/*      */       }
/*      */       
/*  791 */       this.timeOfLastDimensionTick[j][(this.tickCounter % 100)] = (System.nanoTime() - i);
/*      */     }
/*      */     
/*  794 */     this.theProfiler.endStartSection("connection");
/*  795 */     getNetworkSystem().networkTick();
/*  796 */     this.theProfiler.endStartSection("players");
/*  797 */     this.serverConfigManager.onTick();
/*  798 */     this.theProfiler.endStartSection("tickables");
/*      */     
/*  800 */     for (int k = 0; k < this.playersOnline.size(); k++)
/*      */     {
/*  802 */       ((ITickable)this.playersOnline.get(k)).update();
/*      */     }
/*      */     
/*  805 */     this.theProfiler.endSection();
/*      */   }
/*      */   
/*      */   public boolean getAllowNether()
/*      */   {
/*  810 */     return true;
/*      */   }
/*      */   
/*      */   public void startServerThread()
/*      */   {
/*  815 */     this.serverThread = new Thread(this, "Server thread");
/*  816 */     this.serverThread.start();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public File getFile(String fileName)
/*      */   {
/*  824 */     return new File(getDataDirectory(), fileName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void logWarning(String msg)
/*      */   {
/*  832 */     logger.warn(msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public WorldServer worldServerForDimension(int dimension)
/*      */   {
/*  840 */     return dimension == 1 ? this.worldServers[2] : dimension == -1 ? this.worldServers[1] : this.worldServers[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getMinecraftVersion()
/*      */   {
/*  848 */     return "1.8.8";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCurrentPlayerCount()
/*      */   {
/*  856 */     return this.serverConfigManager.getCurrentPlayerCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxPlayers()
/*      */   {
/*  864 */     return this.serverConfigManager.getMaxPlayers();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getAllUsernames()
/*      */   {
/*  872 */     return this.serverConfigManager.getAllUsernames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public GameProfile[] getGameProfiles()
/*      */   {
/*  880 */     return this.serverConfigManager.getAllProfiles();
/*      */   }
/*      */   
/*      */   public String getServerModName()
/*      */   {
/*  885 */     return "vanilla";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public CrashReport addServerInfoToCrashReport(CrashReport report)
/*      */   {
/*  893 */     report.getCategory().addCrashSectionCallable("Profiler Position", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/*  897 */         return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
/*      */       }
/*      */     });
/*      */     
/*  901 */     if (this.serverConfigManager != null)
/*      */     {
/*  903 */       report.getCategory().addCrashSectionCallable("Player Count", new Callable()
/*      */       {
/*      */         public String call()
/*      */         {
/*  907 */           return MinecraftServer.this.serverConfigManager.getCurrentPlayerCount() + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.func_181057_v();
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*  912 */     return report;
/*      */   }
/*      */   
/*      */   public List<String> getTabCompletions(ICommandSender sender, String input, BlockPos pos)
/*      */   {
/*  917 */     List<String> list = Lists.newArrayList();
/*      */     
/*  919 */     if (input.startsWith("/"))
/*      */     {
/*  921 */       input = input.substring(1);
/*  922 */       boolean flag = !input.contains(" ");
/*  923 */       List<String> list1 = this.commandManager.getTabCompletionOptions(sender, input, pos);
/*      */       
/*  925 */       if (list1 != null)
/*      */       {
/*  927 */         for (String s2 : list1)
/*      */         {
/*  929 */           if (flag)
/*      */           {
/*  931 */             list.add("/" + s2);
/*      */           }
/*      */           else
/*      */           {
/*  935 */             list.add(s2);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  940 */       return list;
/*      */     }
/*      */     
/*      */ 
/*  944 */     String[] astring = input.split(" ", -1);
/*  945 */     String s = astring[(astring.length - 1)];
/*      */     String[] arrayOfString1;
/*  947 */     int j = (arrayOfString1 = this.serverConfigManager.getAllUsernames()).length; for (int i = 0; i < j; i++) { String s1 = arrayOfString1[i];
/*      */       
/*  949 */       if (CommandBase.doesStringStartWith(s, s1))
/*      */       {
/*  951 */         list.add(s1);
/*      */       }
/*      */     }
/*      */     
/*  955 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static MinecraftServer getServer()
/*      */   {
/*  964 */     return mcServer;
/*      */   }
/*      */   
/*      */   public boolean isAnvilFileSet()
/*      */   {
/*  969 */     return this.anvilFile != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  977 */     return "Server";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addChatMessage(IChatComponent component)
/*      */   {
/*  985 */     logger.info(component.getUnformattedText());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*      */   {
/*  993 */     return true;
/*      */   }
/*      */   
/*      */   public ICommandManager getCommandManager()
/*      */   {
/*  998 */     return this.commandManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public KeyPair getKeyPair()
/*      */   {
/* 1006 */     return this.serverKeyPair;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServerOwner()
/*      */   {
/* 1014 */     return this.serverOwner;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServerOwner(String owner)
/*      */   {
/* 1022 */     this.serverOwner = owner;
/*      */   }
/*      */   
/*      */   public boolean isSinglePlayer()
/*      */   {
/* 1027 */     return this.serverOwner != null;
/*      */   }
/*      */   
/*      */   public String getFolderName()
/*      */   {
/* 1032 */     return this.folderName;
/*      */   }
/*      */   
/*      */   public void setFolderName(String name)
/*      */   {
/* 1037 */     this.folderName = name;
/*      */   }
/*      */   
/*      */   public void setWorldName(String p_71246_1_)
/*      */   {
/* 1042 */     this.worldName = p_71246_1_;
/*      */   }
/*      */   
/*      */   public String getWorldName()
/*      */   {
/* 1047 */     return this.worldName;
/*      */   }
/*      */   
/*      */   public void setKeyPair(KeyPair keyPair)
/*      */   {
/* 1052 */     this.serverKeyPair = keyPair;
/*      */   }
/*      */   
/*      */   public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
/*      */   {
/* 1057 */     for (int i = 0; i < this.worldServers.length; i++)
/*      */     {
/* 1059 */       World world = this.worldServers[i];
/*      */       
/* 1061 */       if (world != null)
/*      */       {
/* 1063 */         if (world.getWorldInfo().isHardcoreModeEnabled())
/*      */         {
/* 1065 */           world.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
/* 1066 */           world.setAllowedSpawnTypes(true, true);
/*      */         }
/* 1068 */         else if (isSinglePlayer())
/*      */         {
/* 1070 */           world.getWorldInfo().setDifficulty(difficulty);
/* 1071 */           world.setAllowedSpawnTypes(world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
/*      */         }
/*      */         else
/*      */         {
/* 1075 */           world.getWorldInfo().setDifficulty(difficulty);
/* 1076 */           world.setAllowedSpawnTypes(allowSpawnMonsters(), this.canSpawnAnimals);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean allowSpawnMonsters()
/*      */   {
/* 1084 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isDemo()
/*      */   {
/* 1092 */     return this.isDemo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDemo(boolean demo)
/*      */   {
/* 1100 */     this.isDemo = demo;
/*      */   }
/*      */   
/*      */   public void canCreateBonusChest(boolean enable)
/*      */   {
/* 1105 */     this.enableBonusChest = enable;
/*      */   }
/*      */   
/*      */   public ISaveFormat getActiveAnvilConverter()
/*      */   {
/* 1110 */     return this.anvilConverterForAnvilFile;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void deleteWorldAndStopServer()
/*      */   {
/* 1119 */     this.worldIsBeingDeleted = true;
/* 1120 */     getActiveAnvilConverter().flushCache();
/*      */     
/* 1122 */     for (int i = 0; i < this.worldServers.length; i++)
/*      */     {
/* 1124 */       WorldServer worldserver = this.worldServers[i];
/*      */       
/* 1126 */       if (worldserver != null)
/*      */       {
/* 1128 */         worldserver.flush();
/*      */       }
/*      */     }
/*      */     
/* 1132 */     getActiveAnvilConverter().deleteWorldDirectory(this.worldServers[0].getSaveHandler().getWorldDirectoryName());
/* 1133 */     initiateShutdown();
/*      */   }
/*      */   
/*      */   public String getResourcePackUrl()
/*      */   {
/* 1138 */     return this.resourcePackUrl;
/*      */   }
/*      */   
/*      */   public String getResourcePackHash()
/*      */   {
/* 1143 */     return this.resourcePackHash;
/*      */   }
/*      */   
/*      */   public void setResourcePack(String url, String hash)
/*      */   {
/* 1148 */     this.resourcePackUrl = url;
/* 1149 */     this.resourcePackHash = hash;
/*      */   }
/*      */   
/*      */   public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper)
/*      */   {
/* 1154 */     playerSnooper.addClientStat("whitelist_enabled", Boolean.valueOf(false));
/* 1155 */     playerSnooper.addClientStat("whitelist_count", Integer.valueOf(0));
/*      */     
/* 1157 */     if (this.serverConfigManager != null)
/*      */     {
/* 1159 */       playerSnooper.addClientStat("players_current", Integer.valueOf(getCurrentPlayerCount()));
/* 1160 */       playerSnooper.addClientStat("players_max", Integer.valueOf(getMaxPlayers()));
/* 1161 */       playerSnooper.addClientStat("players_seen", Integer.valueOf(this.serverConfigManager.getAvailablePlayerDat().length));
/*      */     }
/*      */     
/* 1164 */     playerSnooper.addClientStat("uses_auth", Boolean.valueOf(this.onlineMode));
/* 1165 */     playerSnooper.addClientStat("gui_state", getGuiEnabled() ? "enabled" : "disabled");
/* 1166 */     playerSnooper.addClientStat("run_time", Long.valueOf((getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
/* 1167 */     playerSnooper.addClientStat("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(this.tickTimeArray) * 1.0E-6D)));
/* 1168 */     int i = 0;
/*      */     
/* 1170 */     if (this.worldServers != null)
/*      */     {
/* 1172 */       for (int j = 0; j < this.worldServers.length; j++)
/*      */       {
/* 1174 */         if (this.worldServers[j] != null)
/*      */         {
/* 1176 */           WorldServer worldserver = this.worldServers[j];
/* 1177 */           WorldInfo worldinfo = worldserver.getWorldInfo();
/* 1178 */           playerSnooper.addClientStat("world[" + i + "][dimension]", Integer.valueOf(worldserver.provider.getDimensionId()));
/* 1179 */           playerSnooper.addClientStat("world[" + i + "][mode]", worldinfo.getGameType());
/* 1180 */           playerSnooper.addClientStat("world[" + i + "][difficulty]", worldserver.getDifficulty());
/* 1181 */           playerSnooper.addClientStat("world[" + i + "][hardcore]", Boolean.valueOf(worldinfo.isHardcoreModeEnabled()));
/* 1182 */           playerSnooper.addClientStat("world[" + i + "][generator_name]", worldinfo.getTerrainType().getWorldTypeName());
/* 1183 */           playerSnooper.addClientStat("world[" + i + "][generator_version]", Integer.valueOf(worldinfo.getTerrainType().getGeneratorVersion()));
/* 1184 */           playerSnooper.addClientStat("world[" + i + "][height]", Integer.valueOf(this.buildLimit));
/* 1185 */           playerSnooper.addClientStat("world[" + i + "][chunks_loaded]", Integer.valueOf(worldserver.getChunkProvider().getLoadedChunkCount()));
/* 1186 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1191 */     playerSnooper.addClientStat("worlds", Integer.valueOf(i));
/*      */   }
/*      */   
/*      */   public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper)
/*      */   {
/* 1196 */     playerSnooper.addStatToSnooper("singleplayer", Boolean.valueOf(isSinglePlayer()));
/* 1197 */     playerSnooper.addStatToSnooper("server_brand", getServerModName());
/* 1198 */     playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
/* 1199 */     playerSnooper.addStatToSnooper("dedicated", Boolean.valueOf(isDedicatedServer()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSnooperEnabled()
/*      */   {
/* 1207 */     return true;
/*      */   }
/*      */   
/*      */   public abstract boolean isDedicatedServer();
/*      */   
/*      */   public boolean isServerInOnlineMode()
/*      */   {
/* 1214 */     return this.onlineMode;
/*      */   }
/*      */   
/*      */   public void setOnlineMode(boolean online)
/*      */   {
/* 1219 */     this.onlineMode = online;
/*      */   }
/*      */   
/*      */   public boolean getCanSpawnAnimals()
/*      */   {
/* 1224 */     return this.canSpawnAnimals;
/*      */   }
/*      */   
/*      */   public void setCanSpawnAnimals(boolean spawnAnimals)
/*      */   {
/* 1229 */     this.canSpawnAnimals = spawnAnimals;
/*      */   }
/*      */   
/*      */   public boolean getCanSpawnNPCs()
/*      */   {
/* 1234 */     return this.canSpawnNPCs;
/*      */   }
/*      */   
/*      */   public abstract boolean func_181035_ah();
/*      */   
/*      */   public void setCanSpawnNPCs(boolean spawnNpcs)
/*      */   {
/* 1241 */     this.canSpawnNPCs = spawnNpcs;
/*      */   }
/*      */   
/*      */   public boolean isPVPEnabled()
/*      */   {
/* 1246 */     return this.pvpEnabled;
/*      */   }
/*      */   
/*      */   public void setAllowPvp(boolean allowPvp)
/*      */   {
/* 1251 */     this.pvpEnabled = allowPvp;
/*      */   }
/*      */   
/*      */   public boolean isFlightAllowed()
/*      */   {
/* 1256 */     return this.allowFlight;
/*      */   }
/*      */   
/*      */   public void setAllowFlight(boolean allow)
/*      */   {
/* 1261 */     this.allowFlight = allow;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract boolean isCommandBlockEnabled();
/*      */   
/*      */ 
/*      */   public String getMOTD()
/*      */   {
/* 1271 */     return this.motd;
/*      */   }
/*      */   
/*      */   public void setMOTD(String motdIn)
/*      */   {
/* 1276 */     this.motd = motdIn;
/*      */   }
/*      */   
/*      */   public int getBuildLimit()
/*      */   {
/* 1281 */     return this.buildLimit;
/*      */   }
/*      */   
/*      */   public void setBuildLimit(int maxBuildHeight)
/*      */   {
/* 1286 */     this.buildLimit = maxBuildHeight;
/*      */   }
/*      */   
/*      */   public boolean isServerStopped()
/*      */   {
/* 1291 */     return this.serverStopped;
/*      */   }
/*      */   
/*      */   public ServerConfigurationManager getConfigurationManager()
/*      */   {
/* 1296 */     return this.serverConfigManager;
/*      */   }
/*      */   
/*      */   public void setConfigManager(ServerConfigurationManager configManager)
/*      */   {
/* 1301 */     this.serverConfigManager = configManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGameType(WorldSettings.GameType gameMode)
/*      */   {
/* 1309 */     for (int i = 0; i < this.worldServers.length; i++)
/*      */     {
/* 1311 */       getServer().worldServers[i].getWorldInfo().setGameType(gameMode);
/*      */     }
/*      */   }
/*      */   
/*      */   public NetworkSystem getNetworkSystem()
/*      */   {
/* 1317 */     return this.networkSystem;
/*      */   }
/*      */   
/*      */   public boolean serverIsInRunLoop()
/*      */   {
/* 1322 */     return this.serverIsRunning;
/*      */   }
/*      */   
/*      */   public boolean getGuiEnabled()
/*      */   {
/* 1327 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract String shareToLAN(WorldSettings.GameType paramGameType, boolean paramBoolean);
/*      */   
/*      */ 
/*      */   public int getTickCounter()
/*      */   {
/* 1337 */     return this.tickCounter;
/*      */   }
/*      */   
/*      */   public void enableProfiling()
/*      */   {
/* 1342 */     this.startProfiling = true;
/*      */   }
/*      */   
/*      */   public PlayerUsageSnooper getPlayerUsageSnooper()
/*      */   {
/* 1347 */     return this.usageSnooper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BlockPos getPosition()
/*      */   {
/* 1356 */     return BlockPos.ORIGIN;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getPositionVector()
/*      */   {
/* 1365 */     return new Vec3(0.0D, 0.0D, 0.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public World getEntityWorld()
/*      */   {
/* 1374 */     return this.worldServers[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Entity getCommandSenderEntity()
/*      */   {
/* 1382 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSpawnProtectionSize()
/*      */   {
/* 1390 */     return 16;
/*      */   }
/*      */   
/*      */   public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn)
/*      */   {
/* 1395 */     return false;
/*      */   }
/*      */   
/*      */   public boolean getForceGamemode()
/*      */   {
/* 1400 */     return this.isGamemodeForced;
/*      */   }
/*      */   
/*      */   public Proxy getServerProxy()
/*      */   {
/* 1405 */     return this.serverProxy;
/*      */   }
/*      */   
/*      */   public static long getCurrentTimeMillis()
/*      */   {
/* 1410 */     return System.currentTimeMillis();
/*      */   }
/*      */   
/*      */   public int getMaxPlayerIdleMinutes()
/*      */   {
/* 1415 */     return this.maxPlayerIdleMinutes;
/*      */   }
/*      */   
/*      */   public void setPlayerIdleTimeout(int idleTimeout)
/*      */   {
/* 1420 */     this.maxPlayerIdleMinutes = idleTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public IChatComponent getDisplayName()
/*      */   {
/* 1428 */     return new ChatComponentText(getName());
/*      */   }
/*      */   
/*      */   public boolean isAnnouncingPlayerAchievements()
/*      */   {
/* 1433 */     return true;
/*      */   }
/*      */   
/*      */   public MinecraftSessionService getMinecraftSessionService()
/*      */   {
/* 1438 */     return this.sessionService;
/*      */   }
/*      */   
/*      */   public GameProfileRepository getGameProfileRepository()
/*      */   {
/* 1443 */     return this.profileRepo;
/*      */   }
/*      */   
/*      */   public PlayerProfileCache getPlayerProfileCache()
/*      */   {
/* 1448 */     return this.profileCache;
/*      */   }
/*      */   
/*      */   public ServerStatusResponse getServerStatusResponse()
/*      */   {
/* 1453 */     return this.statusResponse;
/*      */   }
/*      */   
/*      */   public void refreshStatusNextTick()
/*      */   {
/* 1458 */     this.nanoTimeSinceStatusRefresh = 0L;
/*      */   }
/*      */   
/*      */   public Entity getEntityFromUuid(UUID uuid) {
/*      */     WorldServer[] arrayOfWorldServer;
/* 1463 */     int j = (arrayOfWorldServer = this.worldServers).length; for (int i = 0; i < j; i++) { WorldServer worldserver = arrayOfWorldServer[i];
/*      */       
/* 1465 */       if (worldserver != null)
/*      */       {
/* 1467 */         Entity entity = worldserver.getEntityFromUuid(uuid);
/*      */         
/* 1469 */         if (entity != null)
/*      */         {
/* 1471 */           return entity;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1476 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean sendCommandFeedback()
/*      */   {
/* 1484 */     return getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
/*      */   }
/*      */   
/*      */ 
/*      */   public void setCommandStat(CommandResultStats.Type type, int amount) {}
/*      */   
/*      */ 
/*      */   public int getMaxWorldSize()
/*      */   {
/* 1493 */     return 29999984;
/*      */   }
/*      */   
/*      */   public <V> ListenableFuture<V> callFromMainThread(Callable<V> callable)
/*      */   {
/* 1498 */     Validate.notNull(callable);
/*      */     
/* 1500 */     if ((!isCallingFromMinecraftThread()) && (!isServerStopped()))
/*      */     {
/* 1502 */       ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callable);
/*      */       
/* 1504 */       synchronized (this.futureTaskQueue)
/*      */       {
/* 1506 */         this.futureTaskQueue.add(listenablefuturetask);
/* 1507 */         return listenablefuturetask;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1514 */       return Futures.immediateFuture(callable.call());
/*      */     }
/*      */     catch (Exception exception)
/*      */     {
/* 1518 */       return Futures.immediateFailedCheckedFuture(exception);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
/*      */   {
/* 1525 */     Validate.notNull(runnableToSchedule);
/* 1526 */     return callFromMainThread(Executors.callable(runnableToSchedule));
/*      */   }
/*      */   
/*      */   public boolean isCallingFromMinecraftThread()
/*      */   {
/* 1531 */     return Thread.currentThread() == this.serverThread;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNetworkCompressionTreshold()
/*      */   {
/* 1539 */     return 256;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\MinecraftServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */