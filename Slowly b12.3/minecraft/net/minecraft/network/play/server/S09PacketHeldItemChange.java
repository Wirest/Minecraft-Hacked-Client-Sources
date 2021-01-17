package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import java.io.*;
import net.minecraft.network.*;

public class S09PacketHeldItemChange implements Packet<INetHandlerPlayClient>
{
    private int heldItemHotbarIndex;
    
    public S09PacketHeldItemChange() {
    }
    
    public S09PacketHeldItemChange(final int hotbarIndexIn) {
        this.heldItemHotbarIndex = hotbarIndexIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.heldItemHotbarIndex = buf.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.heldItemHotbarIndex);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleHeldItemChange(this);
    }
    
    public int getHeldItemHotbarIndex() {
        return this.heldItemHotbarIndex;
    }
}
