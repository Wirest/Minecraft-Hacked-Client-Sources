// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import com.google.common.collect.Lists;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S21PacketChunkData implements Packet<INetHandlerPlayClient>
{
    private int chunkX;
    private int chunkZ;
    private Extracted extractedData;
    private boolean field_149279_g;
    
    public S21PacketChunkData() {
    }
    
    public S21PacketChunkData(final Chunk chunkIn, final boolean p_i45196_2_, final int p_i45196_3_) {
        this.chunkX = chunkIn.xPosition;
        this.chunkZ = chunkIn.zPosition;
        this.field_149279_g = p_i45196_2_;
        this.extractedData = func_179756_a(chunkIn, p_i45196_2_, !chunkIn.getWorld().provider.getHasNoSky(), p_i45196_3_);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.field_149279_g = buf.readBoolean();
        this.extractedData = new Extracted();
        this.extractedData.dataSize = buf.readShort();
        this.extractedData.data = buf.readByteArray();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.field_149279_g);
        buf.writeShort((short)(this.extractedData.dataSize & 0xFFFF));
        buf.writeByteArray(this.extractedData.data);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChunkData(this);
    }
    
    public byte[] func_149272_d() {
        return this.extractedData.data;
    }
    
    protected static int func_180737_a(final int p_180737_0_, final boolean p_180737_1_, final boolean p_180737_2_) {
        final int i = p_180737_0_ * 2 * 16 * 16 * 16;
        final int j = p_180737_0_ * 16 * 16 * 16 / 2;
        final int k = p_180737_1_ ? (p_180737_0_ * 16 * 16 * 16 / 2) : 0;
        final int l = p_180737_2_ ? 256 : 0;
        return i + j + k + l;
    }
    
    public static Extracted func_179756_a(final Chunk p_179756_0_, final boolean p_179756_1_, final boolean p_179756_2_, final int p_179756_3_) {
        final ExtendedBlockStorage[] aextendedblockstorage = p_179756_0_.getBlockStorageArray();
        final Extracted s21packetchunkdata$extracted = new Extracted();
        final List<ExtendedBlockStorage> list = (List<ExtendedBlockStorage>)Lists.newArrayList();
        for (int i = 0; i < aextendedblockstorage.length; ++i) {
            final ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[i];
            if (extendedblockstorage != null && (!p_179756_1_ || !extendedblockstorage.isEmpty()) && (p_179756_3_ & 1 << i) != 0x0) {
                final Extracted extracted = s21packetchunkdata$extracted;
                extracted.dataSize |= 1 << i;
                list.add(extendedblockstorage);
            }
        }
        s21packetchunkdata$extracted.data = new byte[func_180737_a(Integer.bitCount(s21packetchunkdata$extracted.dataSize), p_179756_2_, p_179756_1_)];
        int j = 0;
        for (final ExtendedBlockStorage extendedblockstorage2 : list) {
            final char[] achar = extendedblockstorage2.getData();
            char[] array;
            for (int length = (array = achar).length, k = 0; k < length; ++k) {
                final char c0 = array[k];
                s21packetchunkdata$extracted.data[j++] = (byte)(c0 & '\u00ff');
                s21packetchunkdata$extracted.data[j++] = (byte)(c0 >> 8 & 0xFF);
            }
        }
        for (final ExtendedBlockStorage extendedblockstorage3 : list) {
            j = func_179757_a(extendedblockstorage3.getBlocklightArray().getData(), s21packetchunkdata$extracted.data, j);
        }
        if (p_179756_2_) {
            for (final ExtendedBlockStorage extendedblockstorage4 : list) {
                j = func_179757_a(extendedblockstorage4.getSkylightArray().getData(), s21packetchunkdata$extracted.data, j);
            }
        }
        if (p_179756_1_) {
            func_179757_a(p_179756_0_.getBiomeArray(), s21packetchunkdata$extracted.data, j);
        }
        return s21packetchunkdata$extracted;
    }
    
    private static int func_179757_a(final byte[] p_179757_0_, final byte[] p_179757_1_, final int p_179757_2_) {
        System.arraycopy(p_179757_0_, 0, p_179757_1_, p_179757_2_, p_179757_0_.length);
        return p_179757_2_ + p_179757_0_.length;
    }
    
    public int getChunkX() {
        return this.chunkX;
    }
    
    public int getChunkZ() {
        return this.chunkZ;
    }
    
    public int getExtractedSize() {
        return this.extractedData.dataSize;
    }
    
    public boolean func_149274_i() {
        return this.field_149279_g;
    }
    
    public static class Extracted
    {
        public byte[] data;
        public int dataSize;
    }
}
