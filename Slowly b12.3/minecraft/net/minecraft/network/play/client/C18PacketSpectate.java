package net.minecraft.network.play.client;

import net.minecraft.network.play.*;
import java.util.*;
import java.io.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;

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
