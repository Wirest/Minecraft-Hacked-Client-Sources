// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S31PacketWindowProperty implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private int varIndex;
    private int varValue;
    
    public S31PacketWindowProperty() {
    }
    
    public S31PacketWindowProperty(final int windowIdIn, final int varIndexIn, final int varValueIn) {
        this.windowId = windowIdIn;
        this.varIndex = varIndexIn;
        this.varValue = varValueIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleWindowProperty(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.varIndex = buf.readShort();
        this.varValue = buf.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.varIndex);
        buf.writeShort(this.varValue);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getVarIndex() {
        return this.varIndex;
    }
    
    public int getVarValue() {
        return this.varValue;
    }
}
