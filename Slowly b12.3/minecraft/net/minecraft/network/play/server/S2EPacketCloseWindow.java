package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import java.io.*;
import net.minecraft.network.*;

public class S2EPacketCloseWindow implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    
    public S2EPacketCloseWindow() {
    }
    
    public S2EPacketCloseWindow(final int windowIdIn) {
        this.windowId = windowIdIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCloseWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
    }
}
