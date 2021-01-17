package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import java.io.*;
import net.minecraft.network.*;

public class S46PacketSetCompressionLevel implements Packet<INetHandlerPlayClient>
{
    private int field_179761_a;
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.field_179761_a = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.field_179761_a);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSetCompressionLevel(this);
    }
    
    public int func_179760_a() {
        return this.field_179761_a;
    }
}
