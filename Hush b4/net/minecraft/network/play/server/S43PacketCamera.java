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

public class S43PacketCamera implements Packet<INetHandlerPlayClient>
{
    public int entityId;
    
    public S43PacketCamera() {
    }
    
    public S43PacketCamera(final Entity entityIn) {
        this.entityId = entityIn.getEntityId();
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCamera(this);
    }
    
    public Entity getEntity(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
}
