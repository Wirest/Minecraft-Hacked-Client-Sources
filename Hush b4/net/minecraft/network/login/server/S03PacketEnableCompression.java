// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.Packet;

public class S03PacketEnableCompression implements Packet<INetHandlerLoginClient>
{
    private int compressionTreshold;
    
    public S03PacketEnableCompression() {
    }
    
    public S03PacketEnableCompression(final int compressionTresholdIn) {
        this.compressionTreshold = compressionTresholdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.compressionTreshold = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.compressionTreshold);
    }
    
    @Override
    public void processPacket(final INetHandlerLoginClient handler) {
        handler.handleEnableCompression(this);
    }
    
    public int getCompressionTreshold() {
        return this.compressionTreshold;
    }
}
