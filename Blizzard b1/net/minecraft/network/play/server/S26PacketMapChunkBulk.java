package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;

public class S26PacketMapChunkBulk implements Packet
{
    private int[] field_149266_a;
    private int[] field_149264_b;
    private S21PacketChunkData.Extracted[] field_179755_c;
    private boolean field_149267_h;
    private static final String __OBFID = "CL_00001306";

    public S26PacketMapChunkBulk() {}

    public S26PacketMapChunkBulk(List p_i45197_1_)
    {
        int var2 = p_i45197_1_.size();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_179755_c = new S21PacketChunkData.Extracted[var2];
        this.field_149267_h = !((Chunk)p_i45197_1_.get(0)).getWorld().provider.getHasNoSky();

        for (int var3 = 0; var3 < var2; ++var3)
        {
            Chunk var4 = (Chunk)p_i45197_1_.get(var3);
            S21PacketChunkData.Extracted var5 = S21PacketChunkData.func_179756_a(var4, true, this.field_149267_h, 65535);
            this.field_149266_a[var3] = var4.xPosition;
            this.field_149264_b[var3] = var4.zPosition;
            this.field_179755_c[var3] = var5;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149267_h = data.readBoolean();
        int var2 = data.readVarIntFromBuffer();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_179755_c = new S21PacketChunkData.Extracted[var2];
        int var3;

        for (var3 = 0; var3 < var2; ++var3)
        {
            this.field_149266_a[var3] = data.readInt();
            this.field_149264_b[var3] = data.readInt();
            this.field_179755_c[var3] = new S21PacketChunkData.Extracted();
            this.field_179755_c[var3].field_150280_b = data.readShort() & 65535;
            this.field_179755_c[var3].field_150282_a = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount(this.field_179755_c[var3].field_150280_b), this.field_149267_h, true)];
        }

        for (var3 = 0; var3 < var2; ++var3)
        {
            data.readBytes(this.field_179755_c[var3].field_150282_a);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeBoolean(this.field_149267_h);
        data.writeVarIntToBuffer(this.field_179755_c.length);
        int var2;

        for (var2 = 0; var2 < this.field_149266_a.length; ++var2)
        {
            data.writeInt(this.field_149266_a[var2]);
            data.writeInt(this.field_149264_b[var2]);
            data.writeShort((short)(this.field_179755_c[var2].field_150280_b & 65535));
        }

        for (var2 = 0; var2 < this.field_149266_a.length; ++var2)
        {
            data.writeBytes(this.field_179755_c[var2].field_150282_a);
        }
    }

    public void func_180738_a(INetHandlerPlayClient p_180738_1_)
    {
        p_180738_1_.handleMapChunkBulk(this);
    }

    public int func_149255_a(int p_149255_1_)
    {
        return this.field_149266_a[p_149255_1_];
    }

    public int func_149253_b(int p_149253_1_)
    {
        return this.field_149264_b[p_149253_1_];
    }

    public int func_149254_d()
    {
        return this.field_149266_a.length;
    }

    public byte[] func_149256_c(int p_149256_1_)
    {
        return this.field_179755_c[p_149256_1_].field_150282_a;
    }

    public int func_179754_d(int p_179754_1_)
    {
        return this.field_179755_c[p_179754_1_].field_150280_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180738_a((INetHandlerPlayClient)handler);
    }
}
