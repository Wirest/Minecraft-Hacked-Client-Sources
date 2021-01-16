package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1BPacketEntityAttach implements Packet
{
    private int field_149408_a;
    private int field_149406_b;
    private int field_149407_c;
    private static final String __OBFID = "CL_00001327";

    public S1BPacketEntityAttach() {}

    public S1BPacketEntityAttach(int p_i45218_1_, Entity p_i45218_2_, Entity p_i45218_3_)
    {
        this.field_149408_a = p_i45218_1_;
        this.field_149406_b = p_i45218_2_.getEntityId();
        this.field_149407_c = p_i45218_3_ != null ? p_i45218_3_.getEntityId() : -1;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149406_b = data.readInt();
        this.field_149407_c = data.readInt();
        this.field_149408_a = data.readUnsignedByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149406_b);
        data.writeInt(this.field_149407_c);
        data.writeByte(this.field_149408_a);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityAttach(this);
    }

    public int func_149404_c()
    {
        return this.field_149408_a;
    }

    public int func_149403_d()
    {
        return this.field_149406_b;
    }

    public int func_149402_e()
    {
        return this.field_149407_c;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
