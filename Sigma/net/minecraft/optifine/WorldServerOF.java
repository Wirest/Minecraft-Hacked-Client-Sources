package net.minecraft.optifine;

import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerOF extends WorldServer {
    private MinecraftServer mcServer;

    public WorldServerOF(MinecraftServer par1MinecraftServer, ISaveHandler par2iSaveHandler, WorldInfo worldInfo, int par4, Profiler par6Profiler) {
        super(par1MinecraftServer, par2iSaveHandler, worldInfo, par4, par6Profiler);
        mcServer = par1MinecraftServer;
    }

    /**
     * Runs a single tick for the world
     */
    @Override
    public void tick() {
        super.tick();

        if (!Config.isTimeDefault()) {
            fixWorldTime();
        }

        if (Config.waterOpacityChanged) {
            Config.waterOpacityChanged = false;
            ClearWater.updateWaterOpacity(Config.getGameSettings(), this);
        }
    }

    /**
     * Updates all weather states.
     */
    @Override
    protected void updateWeather() {
        if (!Config.isWeatherEnabled()) {
            fixWorldWeather();
        }

        super.updateWeather();
    }

    private void fixWorldWeather() {
        if (worldInfo.isRaining() || worldInfo.isThundering()) {
            worldInfo.setRainTime(0);
            worldInfo.setRaining(false);
            setRainStrength(0.0F);
            worldInfo.setThunderTime(0);
            worldInfo.setThundering(false);
            setThunderStrength(0.0F);
            mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
            mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0F));
            mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0F));
        }
    }

    private void fixWorldTime() {
        if (worldInfo.getGameType().getID() == 1) {
            long time = getWorldTime();
            long timeOfDay = time % 24000L;

            if (Config.isTimeDayOnly()) {
                if (timeOfDay <= 1000L) {
                    setWorldTime(time - timeOfDay + 1001L);
                }

                if (timeOfDay >= 11000L) {
                    setWorldTime(time - timeOfDay + 24001L);
                }
            }

            if (Config.isTimeNightOnly()) {
                if (timeOfDay <= 14000L) {
                    setWorldTime(time - timeOfDay + 14001L);
                }

                if (timeOfDay >= 22000L) {
                    setWorldTime(time - timeOfDay + 24000L + 14001L);
                }
            }
        }
    }
}
