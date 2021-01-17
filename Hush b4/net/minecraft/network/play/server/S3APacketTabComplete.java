// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S3APacketTabComplete implements Packet<INetHandlerPlayClient>
{
    private String[] matches;
    
    public S3APacketTabComplete() {
    }
    
    public S3APacketTabComplete(final String[] matchesIn) {
        this.matches = matchesIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.matches = new String[buf.readVarIntFromBuffer()];
        for (int i = 0; i < this.matches.length; ++i) {
            this.matches[i] = buf.readStringFromBuffer(32767);
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.matches.length);
        String[] matches;
        for (int length = (matches = this.matches).length, i = 0; i < length; ++i) {
            final String s = matches[i];
            buf.writeString(s);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleTabComplete(this);
    }
    
    public String[] func_149630_c() {
        return this.matches;
    }
}
