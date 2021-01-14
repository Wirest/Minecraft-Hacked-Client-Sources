package optifine;

import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerOF extends WorldServer
{
    private MinecraftServer mcServer;

    public WorldServerOF(MinecraftServer par1MinecraftServer, ISaveHandler par2iSaveHandler, WorldInfo worldInfo, int par4, Profiler par6Profiler)
    {
        super(par1MinecraftServer, par2iSaveHandler, worldInfo, par4, par6Profiler);
        this.mcServer = par1MinecraftServer;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        super.tick();

        if (!Config.isTimeDefault())
        {
            this.fixWorldTime();
        }

        if (Config.waterOpacityChanged)
        {
            Config.waterOpacityChanged = false;
            ClearWater.updateWaterOpacity(Config.getGameSettings(), this);
        }
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
        if (!Config.isWeatherEnabled())
        {
            this.fixWorldWeather();
        }

        super.updateWeather();
    }

    private void fixWorldWeather()
    {
        if (this.worldInfo.isRaining() || this.worldInfo.isThundering())
        {
            this.worldInfo.setRainTime(0);
            this.worldInfo.setRaining(false);
            this.setRainStrength(0.0F);
            this.worldInfo.setThunderTime(0);
            this.worldInfo.setThundering(false);
            this.setThunderStrength(0.0F);
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0F));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0F));
        }
    }

    private void fixWorldTime()
    {
        if (this.worldInfo.getGameType().getID() == 1)
        {
            long time = this.getWorldTime();
            long timeOfDay = time % 24000L;

            if (Config.isTimeDayOnly())
            {
                if (timeOfDay <= 1000L)
                {
                    this.setWorldTime(time - timeOfDay + 1001L);
                }

                if (timeOfDay >= 11000L)
                {
                    this.setWorldTime(time - timeOfDay + 24001L);
                }
            }

            if (Config.isTimeNightOnly())
            {
                if (timeOfDay <= 14000L)
                {
                    this.setWorldTime(time - timeOfDay + 14001L);
                }

                if (timeOfDay >= 22000L)
                {
                    this.setWorldTime(time - timeOfDay + 24000L + 14001L);
                }
            }
        }
    }
}
