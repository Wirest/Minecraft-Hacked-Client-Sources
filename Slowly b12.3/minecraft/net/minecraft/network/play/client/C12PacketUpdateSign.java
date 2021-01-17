package net.minecraft.network.play.client;

import net.minecraft.network.play.*;
import net.minecraft.util.*;
import java.io.*;
import net.minecraft.network.*;

public class C12PacketUpdateSign implements Packet<INetHandlerPlayServer>
{
    private BlockPos pos;
    private IChatComponent[] lines;
    
    public C12PacketUpdateSign() {
    }
    
    public C12PacketUpdateSign(final BlockPos pos, final IChatComponent[] lines) {
        this.pos = pos;
        this.lines = new IChatComponent[] { lines[0], lines[1], lines[2], lines[3] };
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.pos = buf.readBlockPos();
        this.lines = new IChatComponent[4];
        for (int i = 0; i < 4; ++i) {
            final String s = buf.readStringFromBuffer(384);
            final IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
            this.lines[i] = ichatcomponent;
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.pos);
        for (int i = 0; i < 4; ++i) {
            final IChatComponent ichatcomponent = this.lines[i];
            final String s = IChatComponent.Serializer.componentToJson(ichatcomponent);
            buf.writeString(s);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processUpdateSign(this);
    }
    
    public BlockPos getPosition() {
        return this.pos;
    }
    
    public IChatComponent[] getLines() {
        return this.lines;
    }
}
