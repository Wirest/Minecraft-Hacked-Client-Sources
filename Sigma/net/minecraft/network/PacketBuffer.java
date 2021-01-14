package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufProcessor;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.UUID;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class PacketBuffer extends ByteBuf {
    private final ByteBuf buf;
    private static final String __OBFID = "CL_00001251";

    public PacketBuffer(ByteBuf wrapped) {
        buf = wrapped;
    }

    /**
     * Calculates the number of bytes required to fit the supplied int (0-5) if
     * it were to be read/written using readVarIntFromBuffer or
     * writeVarIntToBuffer
     */
    public static int getVarIntSize(int input) {
        for (int var1 = 1; var1 < 5; ++var1) {
            if ((input & -1 << var1 * 7) == 0) {
                return var1;
            }
        }

        return 5;
    }

    public void writeByteArray(byte[] array) {
        writeVarIntToBuffer(array.length);
        this.writeBytes(array);
    }

    public byte[] readByteArray() {
        byte[] var1 = new byte[readVarIntFromBuffer()];
        this.readBytes(var1);
        return var1;
    }

    public BlockPos readBlockPos() {
        return BlockPos.fromLong(readLong());
    }

    public void writeBlockPos(BlockPos pos) {
        writeLong(pos.toLong());
    }

    public IChatComponent readChatComponent() {
        return IChatComponent.Serializer.jsonToComponent(readStringFromBuffer(32767));
    }

    public void writeChatComponent(IChatComponent component) {
        writeString(IChatComponent.Serializer.componentToJson(component));
    }

    public Enum readEnumValue(Class enumClass) {
        return ((Enum[]) enumClass.getEnumConstants())[readVarIntFromBuffer()];
    }

    public void writeEnumValue(Enum value) {
        writeVarIntToBuffer(value.ordinal());
    }

    /**
     * Reads a compressed int from the buffer. To do so it maximally reads 5
     * byte-sized chunks whose most significant bit dictates whether another
     * byte should be read.
     */
    public int readVarIntFromBuffer() {
        int var1 = 0;
        int var2 = 0;
        byte var3;

        do {
            var3 = readByte();
            var1 |= (var3 & 127) << var2++ * 7;

            if (var2 > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((var3 & 128) == 128);

        return var1;
    }

    public long readVarLong() {
        long var1 = 0L;
        int var3 = 0;
        byte var4;

        do {
            var4 = readByte();
            var1 |= (long) (var4 & 127) << var3++ * 7;

            if (var3 > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((var4 & 128) == 128);

        return var1;
    }

    public void writeUuid(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUuid() {
        return new UUID(readLong(), readLong());
    }

    /**
     * Writes a compressed int to the buffer. The smallest number of bytes to
     * fit the passed int will be written. Of each such byte only 7 bits will be
     * used to describe the actual value since its most significant bit dictates
     * whether the next byte is part of that same int. Micro-optimization for
     * int values that are expected to have values below 128.
     */
    public void writeVarIntToBuffer(int input) {
        while ((input & -128) != 0) {
            writeByte(input & 127 | 128);
            input >>>= 7;
        }

        writeByte(input);
    }

    public void writeVarLong(long value) {
        while ((value & -128L) != 0L) {
            writeByte((int) (value & 127L) | 128);
            value >>>= 7;
        }

        writeByte((int) value);
    }

    /**
     * Writes a compressed NBTTagCompound to this buffer
     */
    public void writeNBTTagCompoundToBuffer(NBTTagCompound nbt) {
        if (nbt == null) {
            writeByte(0);
        } else {
            try {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(this));
            } catch (IOException var3) {
                throw new EncoderException(var3);
            }
        }
    }

    /**
     * Reads a compressed NBTTagCompound from this buffer
     */
    public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
        int var1 = this.readerIndex();
        byte var2 = readByte();

        if (var2 == 0) {
            return null;
        } else {
            this.readerIndex(var1);
            return CompressedStreamTools.func_152456_a(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
        }
    }

    /**
     * Writes the ItemStack's ID (short), then size (byte), then damage. (short)
     */
    public void writeItemStackToBuffer(ItemStack stack) {
        if (stack == null) {
            writeShort(-1);
        } else {
            writeShort(Item.getIdFromItem(stack.getItem()));
            writeByte(stack.stackSize);
            writeShort(stack.getMetadata());
            NBTTagCompound var2 = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                var2 = stack.getTagCompound();
            }

            writeNBTTagCompoundToBuffer(var2);
        }
    }

    /**
     * Reads an ItemStack from this buffer
     */
    public ItemStack readItemStackFromBuffer() throws IOException {
        ItemStack var1 = null;
        short var2 = readShort();

        if (var2 >= 0) {
            byte var3 = readByte();
            short var4 = readShort();
            var1 = new ItemStack(Item.getItemById(var2), var3, var4);
            var1.setTagCompound(readNBTTagCompoundFromBuffer());
        }

        return var1;
    }

    /**
     * Reads a string from this buffer. Expected parameter is maximum allowed
     * string length. Will throw IOException if string length exceeds this
     * value!
     */
    public String readStringFromBuffer(int maxLength) {
        int var2 = readVarIntFromBuffer();

        if (var2 > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + var2 + " > " + maxLength * 4 + ")");
        } else if (var2 < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String var3 = new String(this.readBytes(var2).array(), Charsets.UTF_8);

            if (var3.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + var2 + " > " + maxLength + ")");
            } else {
                return var3;
            }
        }
    }

    public PacketBuffer writeString(String string) {
        byte[] var2 = string.getBytes(Charsets.UTF_8);

        if (var2.length > 32767) {
            throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + 32767 + ")");
        } else {
            writeVarIntToBuffer(var2.length);
            this.writeBytes(var2);
            return this;
        }
    }

    @Override
    public int capacity() {
        return buf.capacity();
    }

    @Override
    public ByteBuf capacity(int p_capacity_1_) {
        return buf.capacity(p_capacity_1_);
    }

    @Override
    public int maxCapacity() {
        return buf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return buf.alloc();
    }

    @Override
    public ByteOrder order() {
        return buf.order();
    }

    @Override
    public ByteBuf order(ByteOrder p_order_1_) {
        return buf.order(p_order_1_);
    }

    @Override
    public ByteBuf unwrap() {
        return buf.unwrap();
    }

    @Override
    public boolean isDirect() {
        return buf.isDirect();
    }

    @Override
    public int readerIndex() {
        return buf.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int p_readerIndex_1_) {
        return buf.readerIndex(p_readerIndex_1_);
    }

    @Override
    public int writerIndex() {
        return buf.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int p_writerIndex_1_) {
        return buf.writerIndex(p_writerIndex_1_);
    }

    @Override
    public ByteBuf setIndex(int p_setIndex_1_, int p_setIndex_2_) {
        return buf.setIndex(p_setIndex_1_, p_setIndex_2_);
    }

    @Override
    public int readableBytes() {
        return buf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return buf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return buf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return buf.isReadable();
    }

    @Override
    public boolean isReadable(int p_isReadable_1_) {
        return buf.isReadable(p_isReadable_1_);
    }

    @Override
    public boolean isWritable() {
        return buf.isWritable();
    }

    @Override
    public boolean isWritable(int p_isWritable_1_) {
        return buf.isWritable(p_isWritable_1_);
    }

    @Override
    public ByteBuf clear() {
        return buf.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return buf.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return buf.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return buf.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return buf.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return buf.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return buf.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int p_ensureWritable_1_) {
        return buf.ensureWritable(p_ensureWritable_1_);
    }

    @Override
    public int ensureWritable(int p_ensureWritable_1_, boolean p_ensureWritable_2_) {
        return buf.ensureWritable(p_ensureWritable_1_, p_ensureWritable_2_);
    }

    @Override
    public boolean getBoolean(int p_getBoolean_1_) {
        return buf.getBoolean(p_getBoolean_1_);
    }

    @Override
    public byte getByte(int p_getByte_1_) {
        return buf.getByte(p_getByte_1_);
    }

    @Override
    public short getUnsignedByte(int p_getUnsignedByte_1_) {
        return buf.getUnsignedByte(p_getUnsignedByte_1_);
    }

    @Override
    public short getShort(int p_getShort_1_) {
        return buf.getShort(p_getShort_1_);
    }

    @Override
    public int getUnsignedShort(int p_getUnsignedShort_1_) {
        return buf.getUnsignedShort(p_getUnsignedShort_1_);
    }

    @Override
    public int getMedium(int p_getMedium_1_) {
        return buf.getMedium(p_getMedium_1_);
    }

    @Override
    public int getUnsignedMedium(int p_getUnsignedMedium_1_) {
        return buf.getUnsignedMedium(p_getUnsignedMedium_1_);
    }

    @Override
    public int getInt(int p_getInt_1_) {
        return buf.getInt(p_getInt_1_);
    }

    @Override
    public long getUnsignedInt(int p_getUnsignedInt_1_) {
        return buf.getUnsignedInt(p_getUnsignedInt_1_);
    }

    @Override
    public long getLong(int p_getLong_1_) {
        return buf.getLong(p_getLong_1_);
    }

    @Override
    public char getChar(int p_getChar_1_) {
        return buf.getChar(p_getChar_1_);
    }

    @Override
    public float getFloat(int p_getFloat_1_) {
        return buf.getFloat(p_getFloat_1_);
    }

    @Override
    public double getDouble(int p_getDouble_1_) {
        return buf.getDouble(p_getDouble_1_);
    }

    @Override
    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_) {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    @Override
    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_) {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    @Override
    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_) {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }

    @Override
    public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_) {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    @Override
    public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_) {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }

    @Override
    public ByteBuf getBytes(int p_getBytes_1_, ByteBuffer p_getBytes_2_) {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    @Override
    public ByteBuf getBytes(int p_getBytes_1_, OutputStream p_getBytes_2_, int p_getBytes_3_) throws IOException {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    @Override
    public int getBytes(int p_getBytes_1_, GatheringByteChannel p_getBytes_2_, int p_getBytes_3_) throws IOException {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    @Override
    public ByteBuf setBoolean(int p_setBoolean_1_, boolean p_setBoolean_2_) {
        return buf.setBoolean(p_setBoolean_1_, p_setBoolean_2_);
    }

    @Override
    public ByteBuf setByte(int p_setByte_1_, int p_setByte_2_) {
        return buf.setByte(p_setByte_1_, p_setByte_2_);
    }

    @Override
    public ByteBuf setShort(int p_setShort_1_, int p_setShort_2_) {
        return buf.setShort(p_setShort_1_, p_setShort_2_);
    }

    @Override
    public ByteBuf setMedium(int p_setMedium_1_, int p_setMedium_2_) {
        return buf.setMedium(p_setMedium_1_, p_setMedium_2_);
    }

    @Override
    public ByteBuf setInt(int p_setInt_1_, int p_setInt_2_) {
        return buf.setInt(p_setInt_1_, p_setInt_2_);
    }

    @Override
    public ByteBuf setLong(int p_setLong_1_, long p_setLong_2_) {
        return buf.setLong(p_setLong_1_, p_setLong_2_);
    }

    @Override
    public ByteBuf setChar(int p_setChar_1_, int p_setChar_2_) {
        return buf.setChar(p_setChar_1_, p_setChar_2_);
    }

    @Override
    public ByteBuf setFloat(int p_setFloat_1_, float p_setFloat_2_) {
        return buf.setFloat(p_setFloat_1_, p_setFloat_2_);
    }

    @Override
    public ByteBuf setDouble(int p_setDouble_1_, double p_setDouble_2_) {
        return buf.setDouble(p_setDouble_1_, p_setDouble_2_);
    }

    @Override
    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_) {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    @Override
    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_) {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    @Override
    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_) {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }

    @Override
    public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_) {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    @Override
    public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_) {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }

    @Override
    public ByteBuf setBytes(int p_setBytes_1_, ByteBuffer p_setBytes_2_) {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    @Override
    public int setBytes(int p_setBytes_1_, InputStream p_setBytes_2_, int p_setBytes_3_) throws IOException {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    @Override
    public int setBytes(int p_setBytes_1_, ScatteringByteChannel p_setBytes_2_, int p_setBytes_3_) throws IOException {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    @Override
    public ByteBuf setZero(int p_setZero_1_, int p_setZero_2_) {
        return buf.setZero(p_setZero_1_, p_setZero_2_);
    }

    @Override
    public boolean readBoolean() {
        return buf.readBoolean();
    }

    @Override
    public byte readByte() {
        return buf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return buf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return buf.readShort();
    }

    @Override
    public int readUnsignedShort() {
        return buf.readUnsignedShort();
    }

    @Override
    public int readMedium() {
        return buf.readMedium();
    }

    @Override
    public int readUnsignedMedium() {
        return buf.readUnsignedMedium();
    }

    @Override
    public int readInt() {
        return buf.readInt();
    }

    @Override
    public long readUnsignedInt() {
        return buf.readUnsignedInt();
    }

    @Override
    public long readLong() {
        return buf.readLong();
    }

    @Override
    public char readChar() {
        return buf.readChar();
    }

    @Override
    public float readFloat() {
        return buf.readFloat();
    }

    @Override
    public double readDouble() {
        return buf.readDouble();
    }

    @Override
    public ByteBuf readBytes(int p_readBytes_1_) {
        return buf.readBytes(p_readBytes_1_);
    }

    @Override
    public ByteBuf readSlice(int p_readSlice_1_) {
        return buf.readSlice(p_readSlice_1_);
    }

    @Override
    public ByteBuf readBytes(ByteBuf p_readBytes_1_) {
        return buf.readBytes(p_readBytes_1_);
    }

    @Override
    public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_) {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    @Override
    public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_) {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }

    @Override
    public ByteBuf readBytes(byte[] p_readBytes_1_) {
        return buf.readBytes(p_readBytes_1_);
    }

    @Override
    public ByteBuf readBytes(byte[] p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_) {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer p_readBytes_1_) {
        return buf.readBytes(p_readBytes_1_);
    }

    @Override
    public ByteBuf readBytes(OutputStream p_readBytes_1_, int p_readBytes_2_) throws IOException {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    @Override
    public int readBytes(GatheringByteChannel p_readBytes_1_, int p_readBytes_2_) throws IOException {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    @Override
    public ByteBuf skipBytes(int p_skipBytes_1_) {
        return buf.skipBytes(p_skipBytes_1_);
    }

    @Override
    public ByteBuf writeBoolean(boolean p_writeBoolean_1_) {
        return buf.writeBoolean(p_writeBoolean_1_);
    }

    @Override
    public ByteBuf writeByte(int p_writeByte_1_) {
        return buf.writeByte(p_writeByte_1_);
    }

    @Override
    public ByteBuf writeShort(int p_writeShort_1_) {
        return buf.writeShort(p_writeShort_1_);
    }

    @Override
    public ByteBuf writeMedium(int p_writeMedium_1_) {
        return buf.writeMedium(p_writeMedium_1_);
    }

    @Override
    public ByteBuf writeInt(int p_writeInt_1_) {
        return buf.writeInt(p_writeInt_1_);
    }

    @Override
    public ByteBuf writeLong(long p_writeLong_1_) {
        return buf.writeLong(p_writeLong_1_);
    }

    @Override
    public ByteBuf writeChar(int p_writeChar_1_) {
        return buf.writeChar(p_writeChar_1_);
    }

    @Override
    public ByteBuf writeFloat(float p_writeFloat_1_) {
        return buf.writeFloat(p_writeFloat_1_);
    }

    @Override
    public ByteBuf writeDouble(double p_writeDouble_1_) {
        return buf.writeDouble(p_writeDouble_1_);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_) {
        return buf.writeBytes(p_writeBytes_1_);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_) {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_) {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }

    @Override
    public ByteBuf writeBytes(byte[] p_writeBytes_1_) {
        return buf.writeBytes(p_writeBytes_1_);
    }

    @Override
    public ByteBuf writeBytes(byte[] p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_) {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer p_writeBytes_1_) {
        return buf.writeBytes(p_writeBytes_1_);
    }

    @Override
    public int writeBytes(InputStream p_writeBytes_1_, int p_writeBytes_2_) throws IOException {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    @Override
    public int writeBytes(ScatteringByteChannel p_writeBytes_1_, int p_writeBytes_2_) throws IOException {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    @Override
    public ByteBuf writeZero(int p_writeZero_1_) {
        return buf.writeZero(p_writeZero_1_);
    }

    @Override
    public int indexOf(int p_indexOf_1_, int p_indexOf_2_, byte p_indexOf_3_) {
        return buf.indexOf(p_indexOf_1_, p_indexOf_2_, p_indexOf_3_);
    }

    @Override
    public int bytesBefore(byte p_bytesBefore_1_) {
        return buf.bytesBefore(p_bytesBefore_1_);
    }

    @Override
    public int bytesBefore(int p_bytesBefore_1_, byte p_bytesBefore_2_) {
        return buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_);
    }

    @Override
    public int bytesBefore(int p_bytesBefore_1_, int p_bytesBefore_2_, byte p_bytesBefore_3_) {
        return buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_, p_bytesBefore_3_);
    }

    @Override
    public int forEachByte(ByteBufProcessor p_forEachByte_1_) {
        return buf.forEachByte(p_forEachByte_1_);
    }

    @Override
    public int forEachByte(int p_forEachByte_1_, int p_forEachByte_2_, ByteBufProcessor p_forEachByte_3_) {
        return buf.forEachByte(p_forEachByte_1_, p_forEachByte_2_, p_forEachByte_3_);
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor p_forEachByteDesc_1_) {
        return buf.forEachByteDesc(p_forEachByteDesc_1_);
    }

    @Override
    public int forEachByteDesc(int p_forEachByteDesc_1_, int p_forEachByteDesc_2_, ByteBufProcessor p_forEachByteDesc_3_) {
        return buf.forEachByteDesc(p_forEachByteDesc_1_, p_forEachByteDesc_2_, p_forEachByteDesc_3_);
    }

    @Override
    public ByteBuf copy() {
        return buf.copy();
    }

    @Override
    public ByteBuf copy(int p_copy_1_, int p_copy_2_) {
        return buf.copy(p_copy_1_, p_copy_2_);
    }

    @Override
    public ByteBuf slice() {
        return buf.slice();
    }

    @Override
    public ByteBuf slice(int p_slice_1_, int p_slice_2_) {
        return buf.slice(p_slice_1_, p_slice_2_);
    }

    @Override
    public ByteBuf duplicate() {
        return buf.duplicate();
    }

    @Override
    public int nioBufferCount() {
        return buf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return buf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int p_nioBuffer_1_, int p_nioBuffer_2_) {
        return buf.nioBuffer(p_nioBuffer_1_, p_nioBuffer_2_);
    }

    @Override
    public ByteBuffer internalNioBuffer(int p_internalNioBuffer_1_, int p_internalNioBuffer_2_) {
        return buf.internalNioBuffer(p_internalNioBuffer_1_, p_internalNioBuffer_2_);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return buf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int p_nioBuffers_1_, int p_nioBuffers_2_) {
        return buf.nioBuffers(p_nioBuffers_1_, p_nioBuffers_2_);
    }

    @Override
    public boolean hasArray() {
        return buf.hasArray();
    }

    @Override
    public byte[] array() {
        return buf.array();
    }

    @Override
    public int arrayOffset() {
        return buf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return buf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return buf.memoryAddress();
    }

    @Override
    public String toString(Charset p_toString_1_) {
        return buf.toString(p_toString_1_);
    }

    @Override
    public String toString(int p_toString_1_, int p_toString_2_, Charset p_toString_3_) {
        return buf.toString(p_toString_1_, p_toString_2_, p_toString_3_);
    }

    @Override
    public int hashCode() {
        return buf.hashCode();
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        return buf.equals(p_equals_1_);
    }

    @Override
    public int compareTo(ByteBuf p_compareTo_1_) {
        return buf.compareTo(p_compareTo_1_);
    }

    @Override
    public String toString() {
        return buf.toString();
    }

    @Override
    public ByteBuf retain(int p_retain_1_) {
        return buf.retain(p_retain_1_);
    }

    @Override
    public ByteBuf retain() {
        return buf.retain();
    }

    @Override
    public int refCnt() {
        return buf.refCnt();
    }

    @Override
    public boolean release() {
        return buf.release();
    }

    @Override
    public boolean release(int p_release_1_) {
        return buf.release(p_release_1_);
    }
}
