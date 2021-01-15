package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

public class S34PacketMaps implements Packet
{
    private int mapId;
    private byte field_179739_b;
    private Vec4b[] field_179740_c;
    private int field_179737_d;
    private int field_179738_e;
    private int field_179735_f;
    private int field_179736_g;
    private byte[] field_179741_h;

    public S34PacketMaps() {}

    public S34PacketMaps(int mapIdIn, byte p_i45975_2_, Collection p_i45975_3_, byte[] p_i45975_4_, int p_i45975_5_, int p_i45975_6_, int p_i45975_7_, int p_i45975_8_)
    {
        this.mapId = mapIdIn;
        this.field_179739_b = p_i45975_2_;
        this.field_179740_c = (Vec4b[])p_i45975_3_.toArray(new Vec4b[p_i45975_3_.size()]);
        this.field_179737_d = p_i45975_5_;
        this.field_179738_e = p_i45975_6_;
        this.field_179735_f = p_i45975_7_;
        this.field_179736_g = p_i45975_8_;
        this.field_179741_h = new byte[p_i45975_7_ * p_i45975_8_];

        for (int var9 = 0; var9 < p_i45975_7_; ++var9)
        {
            for (int var10 = 0; var10 < p_i45975_8_; ++var10)
            {
                this.field_179741_h[var9 + var10 * p_i45975_7_] = p_i45975_4_[p_i45975_5_ + var9 + (p_i45975_6_ + var10) * 128];
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.mapId = buf.readVarIntFromBuffer();
        this.field_179739_b = buf.readByte();
        this.field_179740_c = new Vec4b[buf.readVarIntFromBuffer()];

        for (int var2 = 0; var2 < this.field_179740_c.length; ++var2)
        {
            short var3 = buf.readByte();
            this.field_179740_c[var2] = new Vec4b((byte)(var3 >> 4 & 15), buf.readByte(), buf.readByte(), (byte)(var3 & 15));
        }

        this.field_179735_f = buf.readUnsignedByte();

        if (this.field_179735_f > 0)
        {
            this.field_179736_g = buf.readUnsignedByte();
            this.field_179737_d = buf.readUnsignedByte();
            this.field_179738_e = buf.readUnsignedByte();
            this.field_179741_h = buf.readByteArray();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.mapId);
        buf.writeByte(this.field_179739_b);
        buf.writeVarIntToBuffer(this.field_179740_c.length);
        Vec4b[] var2 = this.field_179740_c;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            Vec4b var5 = var2[var4];
            buf.writeByte((var5.func_176110_a() & 15) << 4 | var5.func_176111_d() & 15);
            buf.writeByte(var5.func_176112_b());
            buf.writeByte(var5.func_176113_c());
        }

        buf.writeByte(this.field_179735_f);

        if (this.field_179735_f > 0)
        {
            buf.writeByte(this.field_179736_g);
            buf.writeByte(this.field_179737_d);
            buf.writeByte(this.field_179738_e);
            buf.writeByteArray(this.field_179741_h);
        }
    }

    public void func_180741_a(INetHandlerPlayClient handler)
    {
        handler.handleMaps(this);
    }

    public int getMapId()
    {
        return this.mapId;
    }

    public void func_179734_a(MapData p_179734_1_)
    {
        p_179734_1_.scale = this.field_179739_b;
        p_179734_1_.playersVisibleOnMap.clear();
        int var2;

        for (var2 = 0; var2 < this.field_179740_c.length; ++var2)
        {
            Vec4b var3 = this.field_179740_c[var2];
            p_179734_1_.playersVisibleOnMap.put("icon-" + var2, var3);
        }

        for (var2 = 0; var2 < this.field_179735_f; ++var2)
        {
            for (int var4 = 0; var4 < this.field_179736_g; ++var4)
            {
                p_179734_1_.colors[this.field_179737_d + var2 + (this.field_179738_e + var4) * 128] = this.field_179741_h[var2 + var4 * this.field_179735_f];
            }
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180741_a((INetHandlerPlayClient)handler);
    }
}
