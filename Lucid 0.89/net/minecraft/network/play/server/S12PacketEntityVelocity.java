package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S12PacketEntityVelocity implements Packet
{
    private int field_149417_a;
    public int field_149415_b;
    public int field_149416_c;
    public int field_149414_d;
    
    public S12PacketEntityVelocity()
    {
    }
    
    public S12PacketEntityVelocity(Entity entityIn)
    {
	this(entityIn.getEntityId(), entityIn.motionX, entityIn.motionY, entityIn.motionZ);
    }
    
    public S12PacketEntityVelocity(int p_i45220_1_, double p_i45220_2_, double p_i45220_4_, double p_i45220_6_)
    {
	this.field_149417_a = p_i45220_1_;
	double var8 = 3.9D;
	
	if (p_i45220_2_ < -var8)
	{
	    p_i45220_2_ = -var8;
	}
	
	if (p_i45220_4_ < -var8)
	{
	    p_i45220_4_ = -var8;
	}
	
	if (p_i45220_6_ < -var8)
	{
	    p_i45220_6_ = -var8;
	}
	
	if (p_i45220_2_ > var8)
	{
	    p_i45220_2_ = var8;
	}
	
	if (p_i45220_4_ > var8)
	{
	    p_i45220_4_ = var8;
	}
	
	if (p_i45220_6_ > var8)
	{
	    p_i45220_6_ = var8;
	}
	
	this.field_149415_b = (int) (p_i45220_2_ * 8000.0D);
	this.field_149416_c = (int) (p_i45220_4_ * 8000.0D);
	this.field_149414_d = (int) (p_i45220_6_ * 8000.0D);
    }
    
    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
    public void readPacketData(PacketBuffer buf) throws IOException
    {
	this.field_149417_a = buf.readVarIntFromBuffer();
	this.field_149415_b = buf.readShort();
	this.field_149416_c = buf.readShort();
	this.field_149414_d = buf.readShort();
    }
    
    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
    public void writePacketData(PacketBuffer buf) throws IOException
    {
	buf.writeVarIntToBuffer(this.field_149417_a);
	buf.writeShort(this.field_149415_b);
	buf.writeShort(this.field_149416_c);
	buf.writeShort(this.field_149414_d);
    }
    
    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
	handler.handleEntityVelocity(this);
    }
    
    public int func_149412_c()
    {
	return this.field_149417_a;
    }
    
    public int func_149411_d()
    {
	return this.field_149415_b;
    }
    
    public int func_149410_e()
    {
	return this.field_149416_c;
    }
    
    public int func_149409_f()
    {
	return this.field_149414_d;
    }
    
    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
    public void processPacket(INetHandler handler)
    {
	this.processPacket((INetHandlerPlayClient) handler);
    }
}
