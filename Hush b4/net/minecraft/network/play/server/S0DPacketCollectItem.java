// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S0DPacketCollectItem implements Packet<INetHandlerPlayClient>
{
    private int collectedItemEntityId;
    private int entityId;
    
    public S0DPacketCollectItem() {
    }
    
    public S0DPacketCollectItem(final int collectedItemEntityIdIn, final int entityIdIn) {
        this.collectedItemEntityId = collectedItemEntityIdIn;
        this.entityId = entityIdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.collectedItemEntityId = buf.readVarIntFromBuffer();
        this.entityId = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.collectedItemEntityId);
        buf.writeVarIntToBuffer(this.entityId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCollectItem(this);
    }
    
    public int getCollectedItemEntityID() {
        return this.collectedItemEntityId;
    }
    
    public int getEntityID() {
        return this.entityId;
    }
}
