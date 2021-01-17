// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.Packet;

public class S01PacketPong implements Packet<INetHandlerStatusClient>
{
    private long clientTime;
    
    public S01PacketPong() {
    }
    
    public S01PacketPong(final long time) {
        this.clientTime = time;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.clientTime = buf.readLong();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeLong(this.clientTime);
    }
    
    @Override
    public void processPacket(final INetHandlerStatusClient handler) {
        handler.handlePong(this);
    }
}
