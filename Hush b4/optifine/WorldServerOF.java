// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.world.World;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class WorldServerOF extends WorldServer
{
    private MinecraftServer mcServer;
    
    public WorldServerOF(final MinecraftServer p_i98_1_, final ISaveHandler p_i98_2_, final WorldInfo p_i98_3_, final int p_i98_4_, final Profiler p_i98_5_) {
        super(p_i98_1_, p_i98_2_, p_i98_3_, p_i98_4_, p_i98_5_);
        this.mcServer = p_i98_1_;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!Config.isTimeDefault()) {
            this.fixWorldTime();
        }
        if (Config.waterOpacityChanged) {
            Config.waterOpacityChanged = false;
            ClearWater.updateWaterOpacity(Config.getGameSettings(), this);
        }
    }
    
    @Override
    protected void updateWeather() {
        if (!Config.isWeatherEnabled()) {
            this.fixWorldWeather();
        }
        super.updateWeather();
    }
    
    private void fixWorldWeather() {
        if (this.worldInfo.isRaining() || this.worldInfo.isThundering()) {
            this.worldInfo.setRainTime(0);
            this.worldInfo.setRaining(false);
            this.setRainStrength(0.0f);
            this.worldInfo.setThunderTime(0);
            this.worldInfo.setThundering(false);
            this.setThunderStrength(0.0f);
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0f));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0f));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0f));
        }
    }
    
    private void fixWorldTime() {
        if (this.worldInfo.getGameType().getID() == 1) {
            final long i = this.getWorldTime();
            final long j = i % 24000L;
            if (Config.isTimeDayOnly()) {
                if (j <= 1000L) {
                    this.setWorldTime(i - j + 1001L);
                }
                if (j >= 11000L) {
                    this.setWorldTime(i - j + 24001L);
                }
            }
            if (Config.isTimeNightOnly()) {
                if (j <= 14000L) {
                    this.setWorldTime(i - j + 14001L);
                }
                if (j >= 22000L) {
                    this.setWorldTime(i - j + 24000L + 14001L);
                }
            }
        }
    }
}
