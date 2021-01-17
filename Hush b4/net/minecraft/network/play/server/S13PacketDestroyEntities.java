// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S13PacketDestroyEntities implements Packet<INetHandlerPlayClient>
{
    private int[] entityIDs;
    
    public S13PacketDestroyEntities() {
    }
    
    public S13PacketDestroyEntities(final int... entityIDsIn) {
        this.entityIDs = entityIDsIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityIDs = new int[buf.readVarIntFromBuffer()];
        for (int i = 0; i < this.entityIDs.length; ++i) {
            this.entityIDs[i] = buf.readVarIntFromBuffer();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityIDs.length);
        for (int i = 0; i < this.entityIDs.length; ++i) {
            buf.writeVarIntToBuffer(this.entityIDs[i]);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDestroyEntities(this);
    }
    
    public int[] getEntityIDs() {
        return this.entityIDs;
    }
}
