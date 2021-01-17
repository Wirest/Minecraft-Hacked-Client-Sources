// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S06PacketUpdateHealth implements Packet<INetHandlerPlayClient>
{
    private float health;
    private int foodLevel;
    private float saturationLevel;
    
    public S06PacketUpdateHealth() {
    }
    
    public S06PacketUpdateHealth(final float healthIn, final int foodLevelIn, final float saturationIn) {
        this.health = healthIn;
        this.foodLevel = foodLevelIn;
        this.saturationLevel = saturationIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.health = buf.readFloat();
        this.foodLevel = buf.readVarIntFromBuffer();
        this.saturationLevel = buf.readFloat();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeFloat(this.health);
        buf.writeVarIntToBuffer(this.foodLevel);
        buf.writeFloat(this.saturationLevel);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUpdateHealth(this);
    }
    
    public float getHealth() {
        return this.health;
    }
    
    public int getFoodLevel() {
        return this.foodLevel;
    }
    
    public float getSaturationLevel() {
        return this.saturationLevel;
    }
}
