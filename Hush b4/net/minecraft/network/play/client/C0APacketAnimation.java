// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C0APacketAnimation implements Packet<INetHandlerPlayServer>
{
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleAnimation(this);
    }
}
