package net.minecraft.network.play.client;

import net.minecraft.network.play.*;
import java.io.*;
import net.minecraft.network.*;

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
