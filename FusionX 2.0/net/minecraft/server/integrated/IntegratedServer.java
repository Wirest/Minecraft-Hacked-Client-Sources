package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import optifine.Reflector;
import optifine.WorldServerOF;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer
{
    private static final Logger logger = LogManager.getLogger();

    /** The Minecraft instance. */
    private final Minecraft mc;
    private final WorldSettings theWorldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;

    public IntegratedServer(Minecraft mcIn)
    {
        super(mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
        this.mc = mcIn;
        this.theWorldSettings = null;
    }

    public IntegratedServer(Minecraft mcIn, String folderName, String worldName, WorldSettings settings)
    {
        super(new File(mcIn.mcDataDir, "saves"), mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
        this.setServerOwner(mcIn.getSession().getUsername());
        this.setFolderName(folderName);
        this.setWorldName(worldName);
        this.setDemo(mcIn.isDemo());
        this.canCreateBonusChest(settings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigManager(new IntegratedPlayerList(this));
        this.mc = mcIn;
        this.theWorldSettings = this.isDemo() ? DemoWorldServer.demoWorldSettings : settings;
    }

    protected ServerCommandManager createNewCommandManager()
    {
        return new IntegratedServerCommandManager();
    }

    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_)
    {
        this.convertMapIfNeeded(p_71247_1_);
        ISaveHandler var7 = this.getActiveAnvilConverter().getSaveLoader(p_71247_1_, true);
        this.setResourcePackFromWorld(this.getFolderName(), var7);
        WorldInfo var8 = var7.loadWorldInfo();

        if (Reflector.DimensionManager.exists())
        {
            WorldServer var9 = this.isDemo() ? (WorldServer)((WorldServer)(new DemoWorldServer(this, var7, var8, 0, this.theProfiler)).init()) : (WorldServer)(new WorldServerOF(this, var7, var8, 0, this.theProfiler)).init();
            var9.initialize(this.theWorldSettings);
            Integer[] var10 = (Integer[])((Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]));
            Integer[] arr$ = var10;
            int len$ = var10.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                int dim = arr$[i$].intValue();
                WorldServer world = dim == 0 ? var9 : (WorldServer)((WorldServer)(new WorldServerMulti(this, var7, dim, var9, this.theProfiler)).init());
                world.addWorldAccess(new WorldManager(this, world));

                if (!this.isSinglePlayer())
                {
                    world.getWorldInfo().setGameType(this.getGameType());
                }

                if (Reflector.EventBus.exists())
                {
                    Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[] {world});
                }
            }

            this.getConfigurationManager().setPlayerManager(new WorldServer[] {var9});

            if (var9.getWorldInfo().getDifficulty() == null)
            {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }
        else
        {
            this.worldServers = new WorldServer[3];
            this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
            this.setResourcePackFromWorld(this.getFolderName(), var7);

            if (var8 == null)
            {
                var8 = new WorldInfo(this.theWorldSettings, p_71247_2_);
            }
            else
            {
                var8.setWorldName(p_71247_2_);
            }

            for (int var16 = 0; var16 < this.worldServers.length; ++var16)
            {
                byte var17 = 0;

                if (var16 == 1)
                {
                    var17 = -1;
                }

                if (var16 == 2)
                {
                    var17 = 1;
                }

                if (var16 == 0)
                {
                    if (this.isDemo())
                    {
                        this.worldServers[var16] = (WorldServer)(new DemoWorldServer(this, var7, var8, var17, this.theProfiler)).init();
                    }
                    else
                    {
                        this.worldServers[var16] = (WorldServer)(new WorldServerOF(this, var7, var8, var17, this.theProfiler)).init();
                    }

                    this.worldServers[var16].initialize(this.theWorldSettings);
                }
                else
                {
                    this.worldServers[var16] = (WorldServer)(new WorldServerMulti(this, var7, var17, this.worldServers[0], this.theProfiler)).init();
                }

                this.worldServers[var16].addWorldAccess(new WorldManager(this, this.worldServers[var16]));
            }

            this.getConfigurationManager().setPlayerManager(this.worldServers);

            if (this.worldServers[0].getWorldInfo().getDifficulty() == null)
            {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }

        this.initialWorldChunkLoad();
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        logger.info("Starting integrated minecraft server version 1.8");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        Object inst;

        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists())
        {
            inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);

            if (!Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerAboutToStart, new Object[] {this}))
            {
                return false;
            }
        }

        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
        this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());

        if (Reflector.FMLCommonHandler_handleServerStarting.exists())
        {
            inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);

            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE)
            {
                return Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerStarting, new Object[] {this});
            }

            Reflector.callVoid(inst, Reflector.FMLCommonHandler_handleServerStarting, new Object[] {this});
        }

        return true;
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        boolean var1 = this.isGamePaused;
        this.isGamePaused = Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused();

        if (!var1 && this.isGamePaused)
        {
            logger.info("Saving and pausing game...");
            this.getConfigurationManager().saveAllPlayerData();
            this.saveAllWorlds(false);
        }

        if (this.isGamePaused)
        {
            Queue var10 = this.futureTaskQueue;
            Queue var3 = this.futureTaskQueue;

            synchronized (this.futureTaskQueue)
            {
                while (!this.futureTaskQueue.isEmpty())
                {
                    try
                    {
                        if (Reflector.FMLCommonHandler_callFuture.exists())
                        {
                            Reflector.callVoid(Reflector.FMLCommonHandler_callFuture, new Object[] {(FutureTask)this.futureTaskQueue.poll()});
                        }
                        else
                        {
                            ((FutureTask)this.futureTaskQueue.poll()).run();
                        }
                    }
                    catch (Throwable var8)
                    {
                        logger.fatal(var8);
                    }
                }
            }
        }
        else
        {
            super.tick();

            if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance())
            {
                logger.info("Changing view distance to {}, from {}", new Object[] {Integer.valueOf(this.mc.gameSettings.renderDistanceChunks), Integer.valueOf(this.getConfigurationManager().getViewDistance())});
                this.getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
            }

            if (this.mc.theWorld != null)
            {
                WorldInfo var101 = this.worldServers[0].getWorldInfo();
                WorldInfo var11 = this.mc.theWorld.getWorldInfo();

                if (!var101.isDifficultyLocked() && var11.getDifficulty() != var101.getDifficulty())
                {
                    logger.info("Changing difficulty to {}, from {}", new Object[] {var11.getDifficulty(), var101.getDifficulty()});
                    this.setDifficultyForAllWorlds(var11.getDifficulty());
                }
                else if (var11.isDifficultyLocked() && !var101.isDifficultyLocked())
                {
                    logger.info("Locking difficulty to {}", new Object[] {var11.getDifficulty()});
                    WorldServer[] var4 = this.worldServers;
                    int var5 = var4.length;

                    for (int var6 = 0; var6 < var5; ++var6)
                    {
                        WorldServer var7 = var4[var6];

                        if (var7 != null)
                        {
                            var7.getWorldInfo().setDifficultyLocked(true);
                        }
                    }
                }
            }
        }
    }

    public boolean canStructuresSpawn()
    {
        return false;
    }

    public WorldSettings.GameType getGameType()
    {
        return this.theWorldSettings.getGameType();
    }

    /**
     * Get the server's difficulty
     */
    public EnumDifficulty getDifficulty()
    {
        return this.mc.theWorld == null ? this.mc.gameSettings.difficulty : this.mc.theWorld.getWorldInfo().getDifficulty();
    }

    /**
     * Defaults to false.
     */
    public boolean isHardcore()
    {
        return this.theWorldSettings.getHardcoreEnabled();
    }

    public File getDataDirectory()
    {
        return this.mc.mcDataDir;
    }

    public boolean isDedicatedServer()
    {
        return false;
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport report)
    {
        this.mc.crashed(report);
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addCrashSectionCallable("Type", new Callable()
        {
            public String call()
            {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addCrashSectionCallable("Is Modded", new Callable()
        {
            public String call()
            {
                String var1 = ClientBrandRetriever.getClientModName();

                if (!var1.equals("vanilla"))
                {
                    return "Definitely; Client brand changed to \'" + var1 + "\'";
                }
                else
                {
                    var1 = IntegratedServer.this.getServerModName();
                    return !var1.equals("vanilla") ? "Definitely; Server brand changed to \'" + var1 + "\'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.");
                }
            }
        });
        return report;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
    {
        super.setDifficultyForAllWorlds(difficulty);

        if (this.mc.theWorld != null)
        {
            this.mc.theWorld.getWorldInfo().setDifficulty(difficulty);
        }
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper)
    {
        super.addServerStatsToSnooper(playerSnooper);
        playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public String shareToLAN(WorldSettings.GameType type, boolean allowCheats)
    {
        try
        {
            int var6 = -1;

            try
            {
                var6 = HttpUtil.getSuitableLanPort();
            }
            catch (IOException var5)
            {
                ;
            }

            if (var6 <= 0)
            {
                var6 = 25564;
            }

            this.getNetworkSystem().addLanEndpoint((InetAddress)null, var6);
            logger.info("Started on " + var6);
            this.isPublic = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), var6 + "");
            this.lanServerPing.start();
            this.getConfigurationManager().func_152604_a(type);
            this.getConfigurationManager().setCommandsAllowedForAll(allowCheats);
            return var6 + "";
        }
        catch (IOException var61)
        {
            return null;
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        super.stopServer();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        Futures.getUnchecked(this.addScheduledTask(new Runnable()
        {
            public void run()
            {
                ArrayList var1 = Lists.newArrayList(IntegratedServer.this.getConfigurationManager().playerEntityList);
                Iterator var2 = var1.iterator();

                while (var2.hasNext())
                {
                    EntityPlayerMP var3 = (EntityPlayerMP)var2.next();
                    IntegratedServer.this.getConfigurationManager().playerLoggedOut(var3);
                }
            }
        }));
        super.initiateShutdown();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    public void func_175592_a()
    {
        this.func_175585_v();
    }

    /**
     * Returns true if this integrated server is open to LAN
     */
    public boolean getPublic()
    {
        return this.isPublic;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(WorldSettings.GameType gameMode)
    {
        this.getConfigurationManager().func_152604_a(gameMode);
    }

    /**
     * Return whether command blocks are enabled.
     */
    public boolean isCommandBlockEnabled()
    {
        return true;
    }

    public int getOpPermissionLevel()
    {
        return 4;
    }
}
