package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import java.io.*;
import net.minecraft.network.*;

public class S03PacketTimeUpdate implements Packet<INetHandlerPlayClient>
{
    private long totalWorldTime;
    private long worldTime;
    
    public S03PacketTimeUpdate() {
    }
    
    public S03PacketTimeUpdate(final long totalWorldTimeIn, final long totalTimeIn, final boolean doDayLightCycle) {
        this.totalWorldTime = totalWorldTimeIn;
        this.worldTime = totalTimeIn;
        if (!doDayLightCycle) {
            this.worldTime = -this.worldTime;
            if (this.worldTime == 0L) {
                this.worldTime = -1L;
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.totalWorldTime = buf.readLong();
        this.worldTime = buf.readLong();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeLong(this.totalWorldTime);
        buf.writeLong(this.worldTime);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleTimeUpdate(this);
    }
    
    public long getTotalWorldTime() {
        return this.totalWorldTime;
    }
    
    public long getWorldTime() {
        return this.worldTime;
    }
}
