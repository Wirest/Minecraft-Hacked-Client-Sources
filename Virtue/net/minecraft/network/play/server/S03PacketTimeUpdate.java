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
    private static final String __OBFID = "CL_00001337";

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
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149369_a = data.readLong();
        this.field_149368_b = data.readLong();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeLong(this.field_149369_a);
        data.writeLong(this.field_149368_b);
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
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
