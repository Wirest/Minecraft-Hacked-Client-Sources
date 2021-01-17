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

public class S19PacketEntityStatus implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private byte logicOpcode;
    
    public S19PacketEntityStatus() {
    }
    
    public S19PacketEntityStatus(final Entity entityIn, final byte opCodeIn) {
        this.entityId = entityIn.getEntityId();
        this.logicOpcode = opCodeIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readInt();
        this.logicOpcode = buf.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.entityId);
        buf.writeByte(this.logicOpcode);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityStatus(this);
    }
    
    public Entity getEntity(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
    
    public byte getOpCode() {
        return this.logicOpcode;
    }
}
