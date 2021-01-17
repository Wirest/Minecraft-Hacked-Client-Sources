// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C10PacketCreativeInventoryAction implements Packet<INetHandlerPlayServer>
{
    private int slotId;
    private ItemStack stack;
    
    public C10PacketCreativeInventoryAction() {
    }
    
    public C10PacketCreativeInventoryAction(final int slotIdIn, final ItemStack stackIn) {
        this.slotId = slotIdIn;
        this.stack = ((stackIn != null) ? stackIn.copy() : null);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processCreativeInventoryAction(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.slotId = buf.readShort();
        this.stack = buf.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeShort(this.slotId);
        buf.writeItemStackToBuffer(this.stack);
    }
    
    public int getSlotId() {
        return this.slotId;
    }
    
    public ItemStack getStack() {
        return this.stack;
    }
}
