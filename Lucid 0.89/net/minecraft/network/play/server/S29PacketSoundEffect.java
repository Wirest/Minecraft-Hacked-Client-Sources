package net.minecraft.network.play.server;

import java.io.IOException;

import org.apache.commons.lang3.Validate;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S29PacketSoundEffect implements Packet
{
    private String field_149219_a;
    private int field_149217_b;
    private int field_149218_c = Integer.MAX_VALUE;
    private int field_149215_d;
    private float field_149216_e;
    private int field_149214_f;

    public S29PacketSoundEffect() {}

    public S29PacketSoundEffect(String p_i45200_1_, double p_i45200_2_, double p_i45200_4_, double p_i45200_6_, float p_i45200_8_, float p_i45200_9_)
    {
        Validate.notNull(p_i45200_1_, "name", new Object[0]);
        this.field_149219_a = p_i45200_1_;
        this.field_149217_b = (int)(p_i45200_2_ * 8.0D);
        this.field_149218_c = (int)(p_i45200_4_ * 8.0D);
        this.field_149215_d = (int)(p_i45200_6_ * 8.0D);
        this.field_149216_e = p_i45200_8_;
        this.field_149214_f = (int)(p_i45200_9_ * 63.0F);
        p_i45200_9_ = MathHelper.clamp_float(p_i45200_9_, 0.0F, 255.0F);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149219_a = buf.readStringFromBuffer(256);
        this.field_149217_b = buf.readInt();
        this.field_149218_c = buf.readInt();
        this.field_149215_d = buf.readInt();
        this.field_149216_e = buf.readFloat();
        this.field_149214_f = buf.readUnsignedByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.field_149219_a);
        buf.writeInt(this.field_149217_b);
        buf.writeInt(this.field_149218_c);
        buf.writeInt(this.field_149215_d);
        buf.writeFloat(this.field_149216_e);
        buf.writeByte(this.field_149214_f);
    }

    public String func_149212_c()
    {
        return this.field_149219_a;
    }

    public double func_149207_d()
    {
        return this.field_149217_b / 8.0F;
    }

    public double func_149211_e()
    {
        return this.field_149218_c / 8.0F;
    }

    public double func_149210_f()
    {
        return this.field_149215_d / 8.0F;
    }

    public float func_149208_g()
    {
        return this.field_149216_e;
    }

    public float func_149209_h()
    {
        return this.field_149214_f / 63.0F;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSoundEffect(this);
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
