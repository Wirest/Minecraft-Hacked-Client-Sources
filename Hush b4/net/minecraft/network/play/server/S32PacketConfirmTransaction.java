// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S32PacketConfirmTransaction implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private short actionNumber;
    private boolean field_148893_c;
    
    public S32PacketConfirmTransaction() {
    }
    
    public S32PacketConfirmTransaction(final int windowIdIn, final short actionNumberIn, final boolean p_i45182_3_) {
        this.windowId = windowIdIn;
        this.actionNumber = actionNumberIn;
        this.field_148893_c = p_i45182_3_;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleConfirmTransaction(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.actionNumber = buf.readShort();
        this.field_148893_c = buf.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.actionNumber);
        buf.writeBoolean(this.field_148893_c);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public short getActionNumber() {
        return this.actionNumber;
    }
    
    public boolean func_148888_e() {
        return this.field_148893_c;
    }
}
