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

    public S26PacketMapChunkBulk() {}

    public S26PacketMapChunkBulk(List chunks)
    {
        int var2 = chunks.size();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_179755_c = new S21PacketChunkData.Extracted[var2];
        this.field_149267_h = !((Chunk)chunks.get(0)).getWorld().provider.getHasNoSky();

        for (int var3 = 0; var3 < var2; ++var3)
        {
            Chunk var4 = (Chunk)chunks.get(var3);
            S21PacketChunkData.Extracted var5 = S21PacketChunkData.func_179756_a(var4, true, this.field_149267_h, 65535);
            this.field_149266_a[var3] = var4.xPosition;
            this.field_149264_b[var3] = var4.zPosition;
            this.field_179755_c[var3] = var5;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149267_h = buf.readBoolean();
        int var2 = buf.readVarIntFromBuffer();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_179755_c = new S21PacketChunkData.Extracted[var2];
        int var3;

        for (var3 = 0; var3 < var2; ++var3)
        {
            this.field_149266_a[var3] = buf.readInt();
            this.field_149264_b[var3] = buf.readInt();
            this.field_179755_c[var3] = new S21PacketChunkData.Extracted();
            this.field_179755_c[var3].dataSize = buf.readShort() & 65535;
            this.field_179755_c[var3].data = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount(this.field_179755_c[var3].dataSize), this.field_149267_h, true)];
        }

        for (var3 = 0; var3 < var2; ++var3)
        {
            buf.readBytes(this.field_179755_c[var3].data);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBoolean(this.field_149267_h);
        buf.writeVarIntToBuffer(this.field_179755_c.length);
        int var2;

        for (var2 = 0; var2 < this.field_149266_a.length; ++var2)
        {
            buf.writeInt(this.field_149266_a[var2]);
            buf.writeInt(this.field_149264_b[var2]);
            buf.writeShort((short)(this.field_179755_c[var2].dataSize & 65535));
        }

        for (var2 = 0; var2 < this.field_149266_a.length; ++var2)
        {
            buf.writeBytes(this.field_179755_c[var2].data);
        }
    }

    public void func_180738_a(INetHandlerPlayClient handler)
    {
        handler.handleMapChunkBulk(this);
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
        return this.field_179755_c[p_149256_1_].data;
    }

    public int func_179754_d(int p_179754_1_)
    {
        return this.field_179755_c[p_179754_1_].dataSize;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180738_a((INetHandlerPlayClient)handler);
    }
}
