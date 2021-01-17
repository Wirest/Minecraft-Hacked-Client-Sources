package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import net.minecraft.entity.*;
import java.io.*;
import net.minecraft.network.*;

public class S1BPacketEntityAttach implements Packet<INetHandlerPlayClient>
{
    private int leash;
    private int entityId;
    private int vehicleEntityId;
    
    public S1BPacketEntityAttach() {
    }
    
    public S1BPacketEntityAttach(final int leashIn, final Entity entityIn, final Entity vehicle) {
        this.leash = leashIn;
        this.entityId = entityIn.getEntityId();
        this.vehicleEntityId = ((vehicle != null) ? vehicle.getEntityId() : -1);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readInt();
        this.vehicleEntityId = buf.readInt();
        this.leash = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.entityId);
        buf.writeInt(this.vehicleEntityId);
        buf.writeByte(this.leash);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityAttach(this);
    }
    
    public int getLeash() {
        return this.leash;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public int getVehicleEntityId() {
        return this.vehicleEntityId;
    }
}
