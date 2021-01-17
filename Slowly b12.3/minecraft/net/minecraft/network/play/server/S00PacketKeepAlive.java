package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import java.io.*;
import net.minecraft.network.*;

public class S00PacketKeepAlive implements Packet<INetHandlerPlayClient>
{
    private int id;
    
    public S00PacketKeepAlive() {
    }
    
    public S00PacketKeepAlive(final int idIn) {
        this.id = idIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleKeepAlive(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.id = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.id);
    }
    
    public int func_149134_c() {
        return this.id;
    }
}
