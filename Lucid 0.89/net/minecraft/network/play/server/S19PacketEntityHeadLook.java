package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S19PacketEntityHeadLook implements Packet
{
    private int entityId;
    private byte field_149383_b;

    public S19PacketEntityHeadLook() {}

    public S19PacketEntityHeadLook(Entity entityIn, byte p_i45214_2_)
    {
        this.entityId = entityIn.getEntityId();
        this.field_149383_b = p_i45214_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarIntFromBuffer();
        this.field_149383_b = buf.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.field_149383_b);
    }

    public void func_180745_a(INetHandlerPlayClient handler)
    {
        handler.handleEntityHeadLook(this);
    }

    public Entity getEntity(World worldIn)
    {
        return worldIn.getEntityByID(this.entityId);
    }

    public byte func_149380_c()
    {
        return this.field_149383_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180745_a((INetHandlerPlayClient)handler);
    }
}
