// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C09PacketHeldItemChange implements Packet<INetHandlerPlayServer>
{
    private int slotId;
    
    public C09PacketHeldItemChange() {
    }
    
    public C09PacketHeldItemChange(final int slotId) {
        this.slotId = slotId;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.slotId = buf.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeShort(this.slotId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processHeldItemChange(this);
    }
    
    public int getSlotId() {
        return this.slotId;
    }
}
