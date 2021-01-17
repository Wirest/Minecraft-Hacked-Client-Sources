// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import io.netty.util.ReferenceCounted;
import java.nio.charset.Charset;
import io.netty.buffer.ByteBufProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.buffer.ByteBufAllocator;
import com.google.common.base.Charsets;
import io.netty.handler.codec.DecoderException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.io.DataInput;
import net.minecraft.nbt.NBTSizeTracker;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.EncoderException;
import java.io.DataOutput;
import net.minecraft.nbt.CompressedStreamTools;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import java.util.UUID;
import java.io.IOException;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.BlockPos;
import io.netty.buffer.ByteBuf;

public class PacketBuffer extends ByteBuf
{
    private final ByteBuf buf;
    
    public PacketBuffer(final ByteBuf wrapped) {
        this.buf = wrapped;
    }
    
    public static int getVarIntSize(final int input) {
        for (int i = 1; i < 5; ++i) {
            if ((input & -1 << i * 7) == 0x0) {
                return i;
            }
        }
        return 5;
    }
    
    public void writeByteArray(final byte[] array) {
        this.writeVarIntToBuffer(array.length);
        this.writeBytes(array);
    }
    
    public byte[] readByteArray() {
        final byte[] abyte = new byte[this.readVarIntFromBuffer()];
        this.readBytes(abyte);
        return abyte;
    }
    
    public BlockPos readBlockPos() {
        return BlockPos.fromLong(this.readLong());
    }
    
    public void writeBlockPos(final BlockPos pos) {
        this.writeLong(pos.toLong());
    }
    
    public IChatComponent readChatComponent() throws IOException {
        return IChatComponent.Serializer.jsonToComponent(this.readStringFromBuffer(32767));
    }
    
    public void writeChatComponent(final IChatComponent component) throws IOException {
        this.writeString(IChatComponent.Serializer.componentToJson(component));
    }
    
    public <T extends Enum<T>> T readEnumValue(final Class<T> enumClass) {
        return enumClass.getEnumConstants()[this.readVarIntFromBuffer()];
    }
    
    public void writeEnumValue(final Enum<?> value) {
        this.writeVarIntToBuffer(value.ordinal());
    }
    
    public int readVarIntFromBuffer() {
        int i = 0;
        int j = 0;
        byte b0;
        do {
            b0 = this.readByte();
            i |= (b0 & 0x7F) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 0x80) == 0x80);
        return i;
    }
    
    public long readVarLong() {
        long i = 0L;
        int j = 0;
        byte b0;
        do {
            b0 = this.readByte();
            i |= (long)(b0 & 0x7F) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b0 & 0x80) == 0x80);
        return i;
    }
    
    public void writeUuid(final UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }
    
    public UUID readUuid() {
        return new UUID(this.readLong(), this.readLong());
    }
    
    public void writeVarIntToBuffer(int input) {
        while ((input & 0xFFFFFF80) != 0x0) {
            this.writeByte((input & 0x7F) | 0x80);
            input >>>= 7;
        }
        this.writeByte(input);
    }
    
    public void writeVarLong(long value) {
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
            this.writeByte((int)(value & 0x7FL) | 0x80);
            value >>>= 7;
        }
        this.writeByte((int)value);
    }
    
    public void writeNBTTagCompoundToBuffer(final NBTTagCompound nbt) {
        if (nbt == null) {
            this.writeByte(0);
        }
        else {
            try {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(this));
            }
            catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }
    
    public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
        final int i = this.readerIndex();
        final byte b0 = this.readByte();
        if (b0 == 0) {
            return null;
        }
        this.readerIndex(i);
        return CompressedStreamTools.read(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
    }
    
    public void writeItemStackToBuffer(final ItemStack stack) {
        if (stack == null) {
            this.writeShort(-1);
        }
        else {
            this.writeShort(Item.getIdFromItem(stack.getItem()));
            this.writeByte(stack.stackSize);
            this.writeShort(stack.getMetadata());
            NBTTagCompound nbttagcompound = null;
            if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                nbttagcompound = stack.getTagCompound();
            }
            this.writeNBTTagCompoundToBuffer(nbttagcompound);
        }
    }
    
    public ItemStack readItemStackFromBuffer() throws IOException {
        ItemStack itemstack = null;
        final int i = this.readShort();
        if (i >= 0) {
            final int j = this.readByte();
            final int k = this.readShort();
            itemstack = new ItemStack(Item.getItemById(i), j, k);
            itemstack.setTagCompound(this.readNBTTagCompoundFromBuffer());
        }
        return itemstack;
    }
    
    public String readStringFromBuffer(final int maxLength) {
        final int i = this.readVarIntFromBuffer();
        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        }
        if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        final String s = new String(this.readBytes(i).array(), Charsets.UTF_8);
        if (s.length() > maxLength) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
        }
        return s;
    }
    
    public PacketBuffer writeString(final String string) {
        final byte[] abyte = string.getBytes(Charsets.UTF_8);
        if (abyte.length > 32767) {
            throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + 32767 + ")");
        }
        this.writeVarIntToBuffer(abyte.length);
        this.writeBytes(abyte);
        return this;
    }
    
    @Override
    public int capacity() {
        return this.buf.capacity();
    }
    
    @Override
    public ByteBuf capacity(final int p_capacity_1_) {
        return this.buf.capacity(p_capacity_1_);
    }
    
    @Override
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }
    
    @Override
    public ByteOrder order() {
        return this.buf.order();
    }
    
    @Override
    public ByteBuf order(final ByteOrder p_order_1_) {
        return this.buf.order(p_order_1_);
    }
    
    @Override
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }
    
    @Override
    public boolean isDirect() {
        return this.buf.isDirect();
    }
    
    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }
    
    @Override
    public ByteBuf readerIndex(final int p_readerIndex_1_) {
        return this.buf.readerIndex(p_readerIndex_1_);
    }
    
    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }
    
    @Override
    public ByteBuf writerIndex(final int p_writerIndex_1_) {
        return this.buf.writerIndex(p_writerIndex_1_);
    }
    
    @Override
    public ByteBuf setIndex(final int p_setIndex_1_, final int p_setIndex_2_) {
        return this.buf.setIndex(p_setIndex_1_, p_setIndex_2_);
    }
    
    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }
    
    @Override
    public int writableBytes() {
        return this.buf.writableBytes();
    }
    
    @Override
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }
    
    @Override
    public boolean isReadable() {
        return this.buf.isReadable();
    }
    
    @Override
    public boolean isReadable(final int p_isReadable_1_) {
        return this.buf.isReadable(p_isReadable_1_);
    }
    
    @Override
    public boolean isWritable() {
        return this.buf.isWritable();
    }
    
    @Override
    public boolean isWritable(final int p_isWritable_1_) {
        return this.buf.isWritable(p_isWritable_1_);
    }
    
    @Override
    public ByteBuf clear() {
        return this.buf.clear();
    }
    
    @Override
    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }
    
    @Override
    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }
    
    @Override
    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }
    
    @Override
    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }
    
    @Override
    public ByteBuf ensureWritable(final int p_ensureWritable_1_) {
        return this.buf.ensureWritable(p_ensureWritable_1_);
    }
    
    @Override
    public int ensureWritable(final int p_ensureWritable_1_, final boolean p_ensureWritable_2_) {
        return this.buf.ensureWritable(p_ensureWritable_1_, p_ensureWritable_2_);
    }
    
    @Override
    public boolean getBoolean(final int p_getBoolean_1_) {
        return this.buf.getBoolean(p_getBoolean_1_);
    }
    
    @Override
    public byte getByte(final int p_getByte_1_) {
        return this.buf.getByte(p_getByte_1_);
    }
    
    @Override
    public short getUnsignedByte(final int p_getUnsignedByte_1_) {
        return this.buf.getUnsignedByte(p_getUnsignedByte_1_);
    }
    
    @Override
    public short getShort(final int p_getShort_1_) {
        return this.buf.getShort(p_getShort_1_);
    }
    
    @Override
    public int getUnsignedShort(final int p_getUnsignedShort_1_) {
        return this.buf.getUnsignedShort(p_getUnsignedShort_1_);
    }
    
    @Override
    public int getMedium(final int p_getMedium_1_) {
        return this.buf.getMedium(p_getMedium_1_);
    }
    
    @Override
    public int getUnsignedMedium(final int p_getUnsignedMedium_1_) {
        return this.buf.getUnsignedMedium(p_getUnsignedMedium_1_);
    }
    
    @Override
    public int getInt(final int p_getInt_1_) {
        return this.buf.getInt(p_getInt_1_);
    }
    
    @Override
    public long getUnsignedInt(final int p_getUnsignedInt_1_) {
        return this.buf.getUnsignedInt(p_getUnsignedInt_1_);
    }
    
    @Override
    public long getLong(final int p_getLong_1_) {
        return this.buf.getLong(p_getLong_1_);
    }
    
    @Override
    public char getChar(final int p_getChar_1_) {
        return this.buf.getChar(p_getChar_1_);
    }
    
    @Override
    public float getFloat(final int p_getFloat_1_) {
        return this.buf.getFloat(p_getFloat_1_);
    }
    
    @Override
    public double getDouble(final int p_getDouble_1_) {
        return this.buf.getDouble(p_getDouble_1_);
    }
    
    @Override
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuf p_getBytes_2_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }
    
    @Override
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuf p_getBytes_2_, final int p_getBytes_3_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }
    
    @Override
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuf p_getBytes_2_, final int p_getBytes_3_, final int p_getBytes_4_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }
    
    @Override
    public ByteBuf getBytes(final int p_getBytes_1_, final byte[] p_getBytes_2_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }
    
    @Override
    public ByteBuf getBytes(final int p_getBytes_1_, final byte[] p_getBytes_2_, final int p_getBytes_3_, final int p_getBytes_4_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }
    
    @Override
    public ByteBuf getBytes(final int p_getBytes_1_, final ByteBuffer p_getBytes_2_) {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }
    
    @Override
    public ByteBuf getBytes(final int p_getBytes_1_, final OutputStream p_getBytes_2_, final int p_getBytes_3_) throws IOException {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }
    
    @Override
    public int getBytes(final int p_getBytes_1_, final GatheringByteChannel p_getBytes_2_, final int p_getBytes_3_) throws IOException {
        return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }
    
    @Override
    public ByteBuf setBoolean(final int p_setBoolean_1_, final boolean p_setBoolean_2_) {
        return this.buf.setBoolean(p_setBoolean_1_, p_setBoolean_2_);
    }
    
    @Override
    public ByteBuf setByte(final int p_setByte_1_, final int p_setByte_2_) {
        return this.buf.setByte(p_setByte_1_, p_setByte_2_);
    }
    
    @Override
    public ByteBuf setShort(final int p_setShort_1_, final int p_setShort_2_) {
        return this.buf.setShort(p_setShort_1_, p_setShort_2_);
    }
    
    @Override
    public ByteBuf setMedium(final int p_setMedium_1_, final int p_setMedium_2_) {
        return this.buf.setMedium(p_setMedium_1_, p_setMedium_2_);
    }
    
    @Override
    public ByteBuf setInt(final int p_setInt_1_, final int p_setInt_2_) {
        return this.buf.setInt(p_setInt_1_, p_setInt_2_);
    }
    
    @Override
    public ByteBuf setLong(final int p_setLong_1_, final long p_setLong_2_) {
        return this.buf.setLong(p_setLong_1_, p_setLong_2_);
    }
    
    @Override
    public ByteBuf setChar(final int p_setChar_1_, final int p_setChar_2_) {
        return this.buf.setChar(p_setChar_1_, p_setChar_2_);
    }
    
    @Override
    public ByteBuf setFloat(final int p_setFloat_1_, final float p_setFloat_2_) {
        return this.buf.setFloat(p_setFloat_1_, p_setFloat_2_);
    }
    
    @Override
    public ByteBuf setDouble(final int p_setDouble_1_, final double p_setDouble_2_) {
        return this.buf.setDouble(p_setDouble_1_, p_setDouble_2_);
    }
    
    @Override
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuf p_setBytes_2_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }
    
    @Override
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuf p_setBytes_2_, final int p_setBytes_3_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }
    
    @Override
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuf p_setBytes_2_, final int p_setBytes_3_, final int p_setBytes_4_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }
    
    @Override
    public ByteBuf setBytes(final int p_setBytes_1_, final byte[] p_setBytes_2_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }
    
    @Override
    public ByteBuf setBytes(final int p_setBytes_1_, final byte[] p_setBytes_2_, final int p_setBytes_3_, final int p_setBytes_4_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }
    
    @Override
    public ByteBuf setBytes(final int p_setBytes_1_, final ByteBuffer p_setBytes_2_) {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }
    
    @Override
    public int setBytes(final int p_setBytes_1_, final InputStream p_setBytes_2_, final int p_setBytes_3_) throws IOException {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }
    
    @Override
    public int setBytes(final int p_setBytes_1_, final ScatteringByteChannel p_setBytes_2_, final int p_setBytes_3_) throws IOException {
        return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }
    
    @Override
    public ByteBuf setZero(final int p_setZero_1_, final int p_setZero_2_) {
        return this.buf.setZero(p_setZero_1_, p_setZero_2_);
    }
    
    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }
    
    @Override
    public byte readByte() {
        return this.buf.readByte();
    }
    
    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }
    
    @Override
    public short readShort() {
        return this.buf.readShort();
    }
    
    @Override
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }
    
    @Override
    public int readMedium() {
        return this.buf.readMedium();
    }
    
    @Override
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }
    
    @Override
    public int readInt() {
        return this.buf.readInt();
    }
    
    @Override
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }
    
    @Override
    public long readLong() {
        return this.buf.readLong();
    }
    
    @Override
    public char readChar() {
        return this.buf.readChar();
    }
    
    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }
    
    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }
    
    @Override
    public ByteBuf readBytes(final int p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    @Override
    public ByteBuf readSlice(final int p_readSlice_1_) {
        return this.buf.readSlice(p_readSlice_1_);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf p_readBytes_1_, final int p_readBytes_2_) {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf p_readBytes_1_, final int p_readBytes_2_, final int p_readBytes_3_) {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] p_readBytes_1_, final int p_readBytes_2_, final int p_readBytes_3_) {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer p_readBytes_1_) {
        return this.buf.readBytes(p_readBytes_1_);
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream p_readBytes_1_, final int p_readBytes_2_) throws IOException {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel p_readBytes_1_, final int p_readBytes_2_) throws IOException {
        return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }
    
    @Override
    public ByteBuf skipBytes(final int p_skipBytes_1_) {
        return this.buf.skipBytes(p_skipBytes_1_);
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean p_writeBoolean_1_) {
        return this.buf.writeBoolean(p_writeBoolean_1_);
    }
    
    @Override
    public ByteBuf writeByte(final int p_writeByte_1_) {
        return this.buf.writeByte(p_writeByte_1_);
    }
    
    @Override
    public ByteBuf writeShort(final int p_writeShort_1_) {
        return this.buf.writeShort(p_writeShort_1_);
    }
    
    @Override
    public ByteBuf writeMedium(final int p_writeMedium_1_) {
        return this.buf.writeMedium(p_writeMedium_1_);
    }
    
    @Override
    public ByteBuf writeInt(final int p_writeInt_1_) {
        return this.buf.writeInt(p_writeInt_1_);
    }
    
    @Override
    public ByteBuf writeLong(final long p_writeLong_1_) {
        return this.buf.writeLong(p_writeLong_1_);
    }
    
    @Override
    public ByteBuf writeChar(final int p_writeChar_1_) {
        return this.buf.writeChar(p_writeChar_1_);
    }
    
    @Override
    public ByteBuf writeFloat(final float p_writeFloat_1_) {
        return this.buf.writeFloat(p_writeFloat_1_);
    }
    
    @Override
    public ByteBuf writeDouble(final double p_writeDouble_1_) {
        return this.buf.writeDouble(p_writeDouble_1_);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf p_writeBytes_1_) {
        return this.buf.writeBytes(p_writeBytes_1_);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf p_writeBytes_1_, final int p_writeBytes_2_) {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf p_writeBytes_1_, final int p_writeBytes_2_, final int p_writeBytes_3_) {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] p_writeBytes_1_) {
        return this.buf.writeBytes(p_writeBytes_1_);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] p_writeBytes_1_, final int p_writeBytes_2_, final int p_writeBytes_3_) {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer p_writeBytes_1_) {
        return this.buf.writeBytes(p_writeBytes_1_);
    }
    
    @Override
    public int writeBytes(final InputStream p_writeBytes_1_, final int p_writeBytes_2_) throws IOException {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel p_writeBytes_1_, final int p_writeBytes_2_) throws IOException {
        return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }
    
    @Override
    public ByteBuf writeZero(final int p_writeZero_1_) {
        return this.buf.writeZero(p_writeZero_1_);
    }
    
    @Override
    public int indexOf(final int p_indexOf_1_, final int p_indexOf_2_, final byte p_indexOf_3_) {
        return this.buf.indexOf(p_indexOf_1_, p_indexOf_2_, p_indexOf_3_);
    }
    
    @Override
    public int bytesBefore(final byte p_bytesBefore_1_) {
        return this.buf.bytesBefore(p_bytesBefore_1_);
    }
    
    @Override
    public int bytesBefore(final int p_bytesBefore_1_, final byte p_bytesBefore_2_) {
        return this.buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_);
    }
    
    @Override
    public int bytesBefore(final int p_bytesBefore_1_, final int p_bytesBefore_2_, final byte p_bytesBefore_3_) {
        return this.buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_, p_bytesBefore_3_);
    }
    
    @Override
    public int forEachByte(final ByteBufProcessor p_forEachByte_1_) {
        return this.buf.forEachByte(p_forEachByte_1_);
    }
    
    @Override
    public int forEachByte(final int p_forEachByte_1_, final int p_forEachByte_2_, final ByteBufProcessor p_forEachByte_3_) {
        return this.buf.forEachByte(p_forEachByte_1_, p_forEachByte_2_, p_forEachByte_3_);
    }
    
    @Override
    public int forEachByteDesc(final ByteBufProcessor p_forEachByteDesc_1_) {
        return this.buf.forEachByteDesc(p_forEachByteDesc_1_);
    }
    
    @Override
    public int forEachByteDesc(final int p_forEachByteDesc_1_, final int p_forEachByteDesc_2_, final ByteBufProcessor p_forEachByteDesc_3_) {
        return this.buf.forEachByteDesc(p_forEachByteDesc_1_, p_forEachByteDesc_2_, p_forEachByteDesc_3_);
    }
    
    @Override
    public ByteBuf copy() {
        return this.buf.copy();
    }
    
    @Override
    public ByteBuf copy(final int p_copy_1_, final int p_copy_2_) {
        return this.buf.copy(p_copy_1_, p_copy_2_);
    }
    
    @Override
    public ByteBuf slice() {
        return this.buf.slice();
    }
    
    @Override
    public ByteBuf slice(final int p_slice_1_, final int p_slice_2_) {
        return this.buf.slice(p_slice_1_, p_slice_2_);
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }
    
    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int p_nioBuffer_1_, final int p_nioBuffer_2_) {
        return this.buf.nioBuffer(p_nioBuffer_1_, p_nioBuffer_2_);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int p_internalNioBuffer_1_, final int p_internalNioBuffer_2_) {
        return this.buf.internalNioBuffer(p_internalNioBuffer_1_, p_internalNioBuffer_2_);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int p_nioBuffers_1_, final int p_nioBuffers_2_) {
        return this.buf.nioBuffers(p_nioBuffers_1_, p_nioBuffers_2_);
    }
    
    @Override
    public boolean hasArray() {
        return this.buf.hasArray();
    }
    
    @Override
    public byte[] array() {
        return this.buf.array();
    }
    
    @Override
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }
    
    @Override
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }
    
    @Override
    public String toString(final Charset p_toString_1_) {
        return this.buf.toString(p_toString_1_);
    }
    
    @Override
    public String toString(final int p_toString_1_, final int p_toString_2_, final Charset p_toString_3_) {
        return this.buf.toString(p_toString_1_, p_toString_2_, p_toString_3_);
    }
    
    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return this.buf.equals(p_equals_1_);
    }
    
    @Override
    public int compareTo(final ByteBuf p_compareTo_1_) {
        return this.buf.compareTo(p_compareTo_1_);
    }
    
    @Override
    public String toString() {
        return this.buf.toString();
    }
    
    @Override
    public ByteBuf retain(final int p_retain_1_) {
        return this.buf.retain(p_retain_1_);
    }
    
    @Override
    public ByteBuf retain() {
        return this.buf.retain();
    }
    
    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }
    
    @Override
    public boolean release() {
        return this.buf.release();
    }
    
    @Override
    public boolean release(final int p_release_1_) {
        return this.buf.release(p_release_1_);
    }
}
