// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBuf;

class Snappy
{
    private static final int MAX_HT_SIZE = 16384;
    private static final int MIN_COMPRESSIBLE_BYTES = 15;
    private static final int PREAMBLE_NOT_FULL = -1;
    private static final int NOT_ENOUGH_INPUT = -1;
    private static final int LITERAL = 0;
    private static final int COPY_1_BYTE_OFFSET = 1;
    private static final int COPY_2_BYTE_OFFSET = 2;
    private static final int COPY_4_BYTE_OFFSET = 3;
    private State state;
    private byte tag;
    private int written;
    
    Snappy() {
        this.state = State.READY;
    }
    
    public void reset() {
        this.state = State.READY;
        this.tag = 0;
        this.written = 0;
    }
    
    public void encode(final ByteBuf in, final ByteBuf out, final int length) {
        int i = 0;
        int b;
        while (true) {
            b = length >>> i * 7;
            if ((b & 0xFFFFFF80) == 0x0) {
                break;
            }
            out.writeByte((b & 0x7F) | 0x80);
            ++i;
        }
        out.writeByte(b);
        final int baseIndex;
        int inIndex = baseIndex = in.readerIndex();
        final short[] table = getHashTable(length);
        final int shift = 32 - (int)Math.floor(Math.log(table.length) / Math.log(2.0));
        int nextEmit = inIndex;
        Label_0384: {
            if (length - inIndex >= 15) {
                int nextHash = hash(in, ++inIndex, shift);
                while (true) {
                    int skip = 32;
                    int nextIndex = inIndex;
                    int candidate;
                    do {
                        inIndex = nextIndex;
                        final int hash = nextHash;
                        final int bytesBetweenHashLookups = skip++ >> 5;
                        nextIndex = inIndex + bytesBetweenHashLookups;
                        if (nextIndex > length - 4) {
                            break Label_0384;
                        }
                        nextHash = hash(in, nextIndex, shift);
                        candidate = baseIndex + table[hash];
                        table[hash] = (short)(inIndex - baseIndex);
                    } while (in.getInt(inIndex) != in.getInt(candidate));
                    encodeLiteral(in, out, inIndex - nextEmit);
                    int insertTail;
                    do {
                        final int base = inIndex;
                        final int matched = 4 + findMatchingLength(in, candidate + 4, inIndex + 4, length);
                        inIndex += matched;
                        final int offset = base - candidate;
                        encodeCopy(out, offset, matched);
                        in.readerIndex(in.readerIndex() + matched);
                        insertTail = inIndex - 1;
                        if ((nextEmit = inIndex) >= length - 4) {
                            break Label_0384;
                        }
                        final int prevHash = hash(in, insertTail, shift);
                        table[prevHash] = (short)(inIndex - baseIndex - 1);
                        final int currentHash = hash(in, insertTail + 1, shift);
                        candidate = baseIndex + table[currentHash];
                        table[currentHash] = (short)(inIndex - baseIndex);
                    } while (in.getInt(insertTail + 1) == in.getInt(candidate));
                    nextHash = hash(in, insertTail + 2, shift);
                    ++inIndex;
                }
            }
        }
        if (nextEmit < length) {
            encodeLiteral(in, out, length - nextEmit);
        }
    }
    
    private static int hash(final ByteBuf in, final int index, final int shift) {
        return in.getInt(index) + 506832829 >>> shift;
    }
    
    private static short[] getHashTable(final int inputSize) {
        int htSize;
        for (htSize = 256; htSize < 16384 && htSize < inputSize; htSize <<= 1) {}
        short[] table;
        if (htSize <= 256) {
            table = new short[256];
        }
        else {
            table = new short[16384];
        }
        return table;
    }
    
    private static int findMatchingLength(final ByteBuf in, final int minIndex, int inIndex, final int maxIndex) {
        int matched;
        for (matched = 0; inIndex <= maxIndex - 4 && in.getInt(inIndex) == in.getInt(minIndex + matched); inIndex += 4, matched += 4) {}
        while (inIndex < maxIndex && in.getByte(minIndex + matched) == in.getByte(inIndex)) {
            ++inIndex;
            ++matched;
        }
        return matched;
    }
    
    private static int bitsToEncode(final int value) {
        int highestOneBit = Integer.highestOneBit(value);
        int bitLength = 0;
        while ((highestOneBit >>= 1) != 0) {
            ++bitLength;
        }
        return bitLength;
    }
    
    private static void encodeLiteral(final ByteBuf in, final ByteBuf out, final int length) {
        if (length < 61) {
            out.writeByte(length - 1 << 2);
        }
        else {
            final int bitLength = bitsToEncode(length - 1);
            final int bytesToEncode = 1 + bitLength / 8;
            out.writeByte(59 + bytesToEncode << 2);
            for (int i = 0; i < bytesToEncode; ++i) {
                out.writeByte(length - 1 >> i * 8 & 0xFF);
            }
        }
        out.writeBytes(in, length);
    }
    
    private static void encodeCopyWithOffset(final ByteBuf out, final int offset, final int length) {
        if (length < 12 && offset < 2048) {
            out.writeByte(0x1 | length - 4 << 2 | offset >> 8 << 5);
            out.writeByte(offset & 0xFF);
        }
        else {
            out.writeByte(0x2 | length - 1 << 2);
            out.writeByte(offset & 0xFF);
            out.writeByte(offset >> 8 & 0xFF);
        }
    }
    
    private static void encodeCopy(final ByteBuf out, final int offset, int length) {
        while (length >= 68) {
            encodeCopyWithOffset(out, offset, 64);
            length -= 64;
        }
        if (length > 64) {
            encodeCopyWithOffset(out, offset, 60);
            length -= 60;
        }
        encodeCopyWithOffset(out, offset, length);
    }
    
    public void decode(final ByteBuf in, final ByteBuf out) {
        while (in.isReadable()) {
            switch (this.state) {
                case READY: {
                    this.state = State.READING_PREAMBLE;
                }
                case READING_PREAMBLE: {
                    final int uncompressedLength = readPreamble(in);
                    if (uncompressedLength == -1) {
                        return;
                    }
                    if (uncompressedLength == 0) {
                        this.state = State.READY;
                        return;
                    }
                    out.ensureWritable(uncompressedLength);
                    this.state = State.READING_TAG;
                }
                case READING_TAG: {
                    if (!in.isReadable()) {
                        return;
                    }
                    this.tag = in.readByte();
                    switch (this.tag & 0x3) {
                        case 0: {
                            this.state = State.READING_LITERAL;
                            continue;
                        }
                        case 1:
                        case 2:
                        case 3: {
                            this.state = State.READING_COPY;
                            continue;
                        }
                    }
                    continue;
                }
                case READING_LITERAL: {
                    final int literalWritten = decodeLiteral(this.tag, in, out);
                    if (literalWritten != -1) {
                        this.state = State.READING_TAG;
                        this.written += literalWritten;
                        continue;
                    }
                }
                case READING_COPY: {
                    switch (this.tag & 0x3) {
                        case 1: {
                            final int decodeWritten = decodeCopyWith1ByteOffset(this.tag, in, out, this.written);
                            if (decodeWritten != -1) {
                                this.state = State.READING_TAG;
                                this.written += decodeWritten;
                                continue;
                            }
                            return;
                        }
                        case 2: {
                            final int decodeWritten = decodeCopyWith2ByteOffset(this.tag, in, out, this.written);
                            if (decodeWritten != -1) {
                                this.state = State.READING_TAG;
                                this.written += decodeWritten;
                                continue;
                            }
                            return;
                        }
                        case 3: {
                            final int decodeWritten = decodeCopyWith4ByteOffset(this.tag, in, out, this.written);
                            if (decodeWritten != -1) {
                                this.state = State.READING_TAG;
                                this.written += decodeWritten;
                                continue;
                            }
                            return;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    private static int readPreamble(final ByteBuf in) {
        int length = 0;
        int byteIndex = 0;
        while (in.isReadable()) {
            final int current = in.readUnsignedByte();
            length |= (current & 0x7F) << byteIndex++ * 7;
            if ((current & 0x80) == 0x0) {
                return length;
            }
            if (byteIndex >= 4) {
                throw new DecompressionException("Preamble is greater than 4 bytes");
            }
        }
        return 0;
    }
    
    private static int decodeLiteral(final byte tag, final ByteBuf in, final ByteBuf out) {
        in.markReaderIndex();
        int length = 0;
        switch (tag >> 2 & 0x3F) {
            case 60: {
                if (!in.isReadable()) {
                    return -1;
                }
                length = in.readUnsignedByte();
                break;
            }
            case 61: {
                if (in.readableBytes() < 2) {
                    return -1;
                }
                length = ByteBufUtil.swapShort(in.readShort());
                break;
            }
            case 62: {
                if (in.readableBytes() < 3) {
                    return -1;
                }
                length = ByteBufUtil.swapMedium(in.readUnsignedMedium());
                break;
            }
            case 64: {
                if (in.readableBytes() < 4) {
                    return -1;
                }
                length = ByteBufUtil.swapInt(in.readInt());
                break;
            }
            default: {
                length = (tag >> 2 & 0x3F);
                break;
            }
        }
        ++length;
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return -1;
        }
        out.writeBytes(in, length);
        return length;
    }
    
    private static int decodeCopyWith1ByteOffset(final byte tag, final ByteBuf in, final ByteBuf out, final int writtenSoFar) {
        if (!in.isReadable()) {
            return -1;
        }
        final int initialIndex = out.writerIndex();
        final int length = 4 + ((tag & 0x1C) >> 2);
        final int offset = (tag & 0xE0) << 8 >> 5 | in.readUnsignedByte();
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; --copies) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        }
        else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }
    
    private static int decodeCopyWith2ByteOffset(final byte tag, final ByteBuf in, final ByteBuf out, final int writtenSoFar) {
        if (in.readableBytes() < 2) {
            return -1;
        }
        final int initialIndex = out.writerIndex();
        final int length = 1 + (tag >> 2 & 0x3F);
        final int offset = ByteBufUtil.swapShort(in.readShort());
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; --copies) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        }
        else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }
    
    private static int decodeCopyWith4ByteOffset(final byte tag, final ByteBuf in, final ByteBuf out, final int writtenSoFar) {
        if (in.readableBytes() < 4) {
            return -1;
        }
        final int initialIndex = out.writerIndex();
        final int length = 1 + (tag >> 2 & 0x3F);
        final int offset = ByteBufUtil.swapInt(in.readInt());
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; --copies) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        }
        else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }
    
    private static void validateOffset(final int offset, final int chunkSizeSoFar) {
        if (offset > 32767) {
            throw new DecompressionException("Offset exceeds maximum permissible value");
        }
        if (offset <= 0) {
            throw new DecompressionException("Offset is less than minimum permissible value");
        }
        if (offset > chunkSizeSoFar) {
            throw new DecompressionException("Offset exceeds size of chunk");
        }
    }
    
    public static int calculateChecksum(final ByteBuf data) {
        return calculateChecksum(data, data.readerIndex(), data.readableBytes());
    }
    
    public static int calculateChecksum(final ByteBuf data, final int offset, final int length) {
        final Crc32c crc32 = new Crc32c();
        try {
            if (data.hasArray()) {
                crc32.update(data.array(), data.arrayOffset() + offset, length);
            }
            else {
                final byte[] array = new byte[length];
                data.getBytes(offset, array);
                crc32.update(array, 0, length);
            }
            return maskChecksum((int)crc32.getValue());
        }
        finally {
            crc32.reset();
        }
    }
    
    static void validateChecksum(final int expectedChecksum, final ByteBuf data) {
        validateChecksum(expectedChecksum, data, data.readerIndex(), data.readableBytes());
    }
    
    static void validateChecksum(final int expectedChecksum, final ByteBuf data, final int offset, final int length) {
        final int actualChecksum = calculateChecksum(data, offset, length);
        if (actualChecksum != expectedChecksum) {
            throw new DecompressionException("mismatching checksum: " + Integer.toHexString(actualChecksum) + " (expected: " + Integer.toHexString(expectedChecksum) + ')');
        }
    }
    
    static int maskChecksum(final int checksum) {
        return (checksum >> 15 | checksum << 17) - 1568478504;
    }
    
    private enum State
    {
        READY, 
        READING_PREAMBLE, 
        READING_TAG, 
        READING_LITERAL, 
        READING_COPY;
    }
}
