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

    public S31PacketWindowProperty() {}

    public S31PacketWindowProperty(int p_i45187_1_, int p_i45187_2_, int p_i45187_3_)
    {
        this.field_149186_a = p_i45187_1_;
        this.field_149184_b = p_i45187_2_;
        this.field_149185_c = p_i45187_3_;
    }

    public void func_180733_a(INetHandlerPlayClient handler)
    {
        handler.handleWindowProperty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149186_a = buf.readUnsignedByte();
        this.field_149184_b = buf.readShort();
        this.field_149185_c = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.field_149186_a);
        buf.writeShort(this.field_149184_b);
        buf.writeShort(this.field_149185_c);
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
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180733_a((INetHandlerPlayClient)handler);
    }
}
