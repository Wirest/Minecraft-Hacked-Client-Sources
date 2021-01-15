package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.collect.Lists;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class S21PacketChunkData implements Packet
{
    private int chunkX;
    private int chunkZ;
    private S21PacketChunkData.Extracted field_179758_c;
    private boolean field_149279_g;

    public S21PacketChunkData() {}

    public S21PacketChunkData(Chunk chunkIn, boolean p_i45196_2_, int p_i45196_3_)
    {
        this.chunkX = chunkIn.xPosition;
        this.chunkZ = chunkIn.zPosition;
        this.field_149279_g = p_i45196_2_;
        this.field_179758_c = func_179756_a(chunkIn, p_i45196_2_, !chunkIn.getWorld().provider.getHasNoSky(), p_i45196_3_);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.field_149279_g = buf.readBoolean();
        this.field_179758_c = new S21PacketChunkData.Extracted();
        this.field_179758_c.dataSize = buf.readShort();
        this.field_179758_c.data = buf.readByteArray();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.field_149279_g);
        buf.writeShort((short)(this.field_179758_c.dataSize & 65535));
        buf.writeByteArray(this.field_179758_c.data);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChunkData(this);
    }

    public byte[] func_149272_d()
    {
        return this.field_179758_c.data;
    }

    protected static int func_180737_a(int p_180737_0_, boolean p_180737_1_, boolean p_180737_2_)
    {
        int var3 = p_180737_0_ * 2 * 16 * 16 * 16;
        int var4 = p_180737_0_ * 16 * 16 * 16 / 2;
        int var5 = p_180737_1_ ? p_180737_0_ * 16 * 16 * 16 / 2 : 0;
        int var6 = p_180737_2_ ? 256 : 0;
        return var3 + var4 + var5 + var6;
    }

    public static S21PacketChunkData.Extracted func_179756_a(Chunk p_179756_0_, boolean p_179756_1_, boolean p_179756_2_, int p_179756_3_)
    {
        ExtendedBlockStorage[] var4 = p_179756_0_.getBlockStorageArray();
        S21PacketChunkData.Extracted var5 = new S21PacketChunkData.Extracted();
        ArrayList var6 = Lists.newArrayList();
        int var7;

        for (var7 = 0; var7 < var4.length; ++var7)
        {
            ExtendedBlockStorage var8 = var4[var7];

            if (var8 != null && (!p_179756_1_ || !var8.isEmpty()) && (p_179756_3_ & 1 << var7) != 0)
            {
                var5.dataSize |= 1 << var7;
                var6.add(var8);
            }
        }

        var5.data = new byte[func_180737_a(Integer.bitCount(var5.dataSize), p_179756_2_, p_179756_1_)];
        var7 = 0;
        Iterator var15 = var6.iterator();
        ExtendedBlockStorage var9;

        while (var15.hasNext())
        {
            var9 = (ExtendedBlockStorage)var15.next();
            char[] var10 = var9.getData();
            char[] var11 = var10;
            int var12 = var10.length;

            for (int var13 = 0; var13 < var12; ++var13)
            {
                char var14 = var11[var13];
                var5.data[var7++] = (byte)(var14 & 255);
                var5.data[var7++] = (byte)(var14 >> 8 & 255);
            }
        }

        for (var15 = var6.iterator(); var15.hasNext(); var7 = func_179757_a(var9.getBlocklightArray().getData(), var5.data, var7))
        {
            var9 = (ExtendedBlockStorage)var15.next();
        }

        if (p_179756_2_)
        {
            for (var15 = var6.iterator(); var15.hasNext(); var7 = func_179757_a(var9.getSkylightArray().getData(), var5.data, var7))
            {
                var9 = (ExtendedBlockStorage)var15.next();
            }
        }

        if (p_179756_1_)
        {
            func_179757_a(p_179756_0_.getBiomeArray(), var5.data, var7);
        }

        return var5;
    }

    private static int func_179757_a(byte[] p_179757_0_, byte[] p_179757_1_, int p_179757_2_)
    {
        System.arraycopy(p_179757_0_, 0, p_179757_1_, p_179757_2_, p_179757_0_.length);
        return p_179757_2_ + p_179757_0_.length;
    }

    public int getChunkX()
    {
        return this.chunkX;
    }

    public int getChunkZ()
    {
        return this.chunkZ;
    }

    public int func_149276_g()
    {
        return this.field_179758_c.dataSize;
    }

    public boolean func_149274_i()
    {
        return this.field_149279_g;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    public static class Extracted
    {
        public byte[] data;
        public int dataSize;
    }
}
