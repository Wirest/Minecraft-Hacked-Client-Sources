// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S19PacketEntityHeadLook implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private byte yaw;
    
    public S19PacketEntityHeadLook() {
    }
    
    public S19PacketEntityHeadLook(final Entity entityIn, final byte p_i45214_2_) {
        this.entityId = entityIn.getEntityId();
        this.yaw = p_i45214_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.yaw = buf.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.yaw);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityHeadLook(this);
    }
    
    public Entity getEntity(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
    
    public byte getYaw() {
        return this.yaw;
    }
}
