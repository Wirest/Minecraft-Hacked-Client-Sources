// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C0DPacketCloseWindow implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    
    public C0DPacketCloseWindow() {
    }
    
    public C0DPacketCloseWindow(final int windowId) {
        this.windowId = windowId;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processCloseWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
    }
}
