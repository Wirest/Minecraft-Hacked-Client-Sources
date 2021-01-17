package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import net.minecraft.entity.*;
import java.io.*;
import net.minecraft.world.*;
import net.minecraft.network.*;

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
