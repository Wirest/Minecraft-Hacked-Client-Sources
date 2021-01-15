package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S31PacketWindowProperty implements Packet
{
    private int field_149186_a;
    private int field_149184_b;
    private int field_149185_c;
    private static final String __OBFID = "CL_00001295";

    public S31PacketWindowProperty() {}

    public S31PacketWindowProperty(int p_i45187_1_, int p_i45187_2_, int p_i45187_3_)
    {
        this.field_149186_a = p_i45187_1_;
        this.field_149184_b = p_i45187_2_;
        this.field_149185_c = p_i45187_3_;
    }

    public void func_180733_a(INetHandlerPlayClient p_180733_1_)
    {
        p_180733_1_.handleWindowProperty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149186_a = data.readUnsignedByte();
        this.field_149184_b = data.readShort();
        this.field_149185_c = data.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.field_149186_a);
        data.writeShort(this.field_149184_b);
        data.writeShort(this.field_149185_c);
    }

    public int func_149182_c()
    {
        return this.field_149186_a;
    }

    public int func_149181_d()
    {
        return this.field_149184_b;
    }

    public int func_149180_e()
    {
        return this.field_149185_c;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180733_a((INetHandlerPlayClient)handler);
    }
}
