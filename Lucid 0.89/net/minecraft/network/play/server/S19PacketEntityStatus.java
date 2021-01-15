package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S19PacketEntityStatus implements Packet
{
    public int entityId;
    private byte field_149163_b;
    
    public S19PacketEntityStatus()
    {
    }
    
    public S19PacketEntityStatus(Entity entityIn, byte p_i46335_2_)
    {
	this.entityId = entityIn.getEntityId();
	this.field_149163_b = p_i46335_2_;
    }
    
    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
    public void readPacketData(PacketBuffer buf) throws IOException
    {
	this.entityId = buf.readInt();
	this.field_149163_b = buf.readByte();
    }
    
    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
    public void writePacketData(PacketBuffer buf) throws IOException
    {
	buf.writeInt(this.entityId);
	buf.writeByte(this.field_149163_b);
    }
    
    public void func_180736_a(INetHandlerPlayClient handler)
    {
	handler.handleEntityStatus(this);
    }
    
    public Entity getEntity(World worldIn)
    {
	return worldIn.getEntityByID(this.entityId);
    }
    
    public byte func_149160_c()
    {
	return this.field_149163_b;
    }
    
    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
    public void processPacket(INetHandler handler)
    {
	this.func_180736_a((INetHandlerPlayClient) handler);
    }
}
