package net.minecraft.network.play.client;

import net.minecraft.network.play.*;
import java.io.*;
import io.netty.buffer.*;
import net.minecraft.network.*;

public class C17PacketCustomPayload implements Packet<INetHandlerPlayServer>
{
    private String channel;
    private PacketBuffer data;
    
    public C17PacketCustomPayload() {
    }
    
    public C17PacketCustomPayload(final String channelIn, final PacketBuffer dataIn) {
        this.channel = channelIn;
        this.data = dataIn;
        if (dataIn.writerIndex() > 32767) {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.channel = buf.readStringFromBuffer(20);
        final int i = buf.readableBytes();
        if (i >= 0 && i <= 32767) {
            this.data = new PacketBuffer(buf.readBytes(i));
            return;
        }
        throw new IOException("Payload may not be larger than 32767 bytes");
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.channel);
        buf.writeBytes(this.data);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processVanilla250Packet(this);
    }
    
    public String getChannelName() {
        return this.channel;
    }
    
    public PacketBuffer getBufferData() {
        return this.data;
    }
}
