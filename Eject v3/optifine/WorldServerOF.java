package optifine;

import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerOF
        extends WorldServer {
    private MinecraftServer mcServer;

    public WorldServerOF(MinecraftServer paramMinecraftServer, ISaveHandler paramISaveHandler, WorldInfo paramWorldInfo, int paramInt, Profiler paramProfiler) {
        super(paramMinecraftServer, paramISaveHandler, paramWorldInfo, paramInt, paramProfiler);
        this.mcServer = paramMinecraftServer;
    }

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

    protected void updateWeather() {
        if (!Config.isWeatherEnabled()) {
            fixWorldWeather();
        }
        super.updateWeather();
    }

    private void fixWorldWeather() {
        if ((this.worldInfo.isRaining()) || (this.worldInfo.isThundering())) {
            this.worldInfo.setRainTime(0);
            this.worldInfo.setRaining(false);
            setRainStrength(0.0F);
            this.worldInfo.setThunderTime(0);
            this.worldInfo.setThundering(false);
            setThunderStrength(0.0F);
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0F));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0F));
        }
    }

    private void fixWorldTime() {
        if (this.worldInfo.getGameType().getID() == 1) {
            long l1 = getWorldTime();
            long l2 = l1 % 24000L;
            if (Config.isTimeDayOnly()) {
                if (l2 <= 1000L) {
                    setWorldTime(l1 - l2 + 1001L);
                }
                if (l2 >= 11000L) {
                    setWorldTime(l1 - l2 + 24001L);
                }
            }
            if (Config.isTimeNightOnly()) {
                if (l2 <= 14000L) {
                    setWorldTime(l1 - l2 + 14001L);
                }
                if (l2 >= 22000L) {
                    setWorldTime(l1 - l2 + 24000L + 14001L);
                }
            }
        }
    }
}




