// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S48PacketResourcePackSend implements Packet<INetHandlerPlayClient>
{
    private String url;
    private String hash;
    
    public S48PacketResourcePackSend() {
    }
    
    public S48PacketResourcePackSend(final String url, final String hash) {
        this.url = url;
        this.hash = hash;
        if (hash.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.url = buf.readStringFromBuffer(32767);
        this.hash = buf.readStringFromBuffer(40);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.url);
        buf.writeString(this.hash);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleResourcePack(this);
    }
    
    public String getURL() {
        return this.url;
    }
    
    public String getHash() {
        return this.hash;
    }
}
