// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.UUID;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C18PacketSpectate implements Packet<INetHandlerPlayServer>
{
    private UUID id;
    
    public C18PacketSpectate() {
    }
    
    public C18PacketSpectate(final UUID id) {
        this.id = id;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.id = buf.readUuid();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeUuid(this.id);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleSpectate(this);
    }
    
    public Entity getEntity(final WorldServer worldIn) {
        return worldIn.getEntityFromUuid(this.id);
    }
}
