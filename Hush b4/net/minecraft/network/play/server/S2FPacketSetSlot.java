// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S2FPacketSetSlot implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private int slot;
    private ItemStack item;
    
    public S2FPacketSetSlot() {
    }
    
    public S2FPacketSetSlot(final int windowIdIn, final int slotIn, final ItemStack itemIn) {
        this.windowId = windowIdIn;
        this.slot = slotIn;
        this.item = ((itemIn == null) ? null : itemIn.copy());
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSetSlot(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.slot = buf.readShort();
        this.item = buf.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slot);
        buf.writeItemStackToBuffer(this.item);
    }
    
    public int func_149175_c() {
        return this.windowId;
    }
    
    public int func_149173_d() {
        return this.slot;
    }
    
    public ItemStack func_149174_e() {
        return this.item;
    }
}
