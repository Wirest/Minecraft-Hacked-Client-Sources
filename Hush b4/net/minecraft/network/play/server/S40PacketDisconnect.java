// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S40PacketDisconnect implements Packet<INetHandlerPlayClient>
{
    private IChatComponent reason;
    
    public S40PacketDisconnect() {
    }
    
    public S40PacketDisconnect(final IChatComponent reasonIn) {
        this.reason = reasonIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.reason = buf.readChatComponent();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeChatComponent(this.reason);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDisconnect(this);
    }
    
    public IChatComponent getReason() {
        return this.reason;
    }
}
