package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S03PacketTimeUpdate implements Packet
{
    private long field_149369_a;
    private long field_149368_b;

    public S03PacketTimeUpdate() {}

    public S03PacketTimeUpdate(long p_i45230_1_, long p_i45230_3_, boolean p_i45230_5_)
    {
        this.field_149369_a = p_i45230_1_;
        this.field_149368_b = p_i45230_3_;

        if (!p_i45230_5_)
        {
            this.field_149368_b = -this.field_149368_b;

            if (this.field_149368_b == 0L)
            {
                this.field_149368_b = -1L;
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149369_a = buf.readLong();
        this.field_149368_b = buf.readLong();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeLong(this.field_149369_a);
        buf.writeLong(this.field_149368_b);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleTimeUpdate(this);
    }

    public long func_149366_c()
    {
        return this.field_149369_a;
    }

    public long func_149365_d()
    {
        return this.field_149368_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
