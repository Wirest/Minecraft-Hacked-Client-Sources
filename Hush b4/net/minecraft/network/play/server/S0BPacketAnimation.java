// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S0BPacketAnimation implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int type;
    
    public S0BPacketAnimation() {
    }
    
    public S0BPacketAnimation(final Entity ent, final int animationType) {
        this.entityId = ent.getEntityId();
        this.type = animationType;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.type = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.type);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleAnimation(this);
    }
    
    public int getEntityID() {
        return this.entityId;
    }
    
    public int getAnimationType() {
        return this.type;
    }
}
