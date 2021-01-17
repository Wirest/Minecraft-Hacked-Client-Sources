// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.DataInput;
import java.io.InputStream;

public class ByteBufInputStream extends InputStream implements DataInput
{
    private final ByteBuf buffer;
    private final int startIndex;
    private final int endIndex;
    private final StringBuilder lineBuf;
    
    public ByteBufInputStream(final ByteBuf buffer) {
        this(buffer, buffer.readableBytes());
    }
    
    public ByteBufInputStream(final ByteBuf buffer, final int length) {
        this.lineBuf = new StringBuilder();
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length: " + length);
        }
        if (length > buffer.readableBytes()) {
            throw new IndexOutOfBoundsException("Too many bytes to be read - Needs " + length + ", maximum is " + buffer.readableBytes());
        }
        this.buffer = buffer;
        this.startIndex = buffer.readerIndex();
        this.endIndex = this.startIndex + length;
        buffer.markReaderIndex();
    }
    
    public int readBytes() {
        return this.buffer.readerIndex() - this.startIndex;
    }
    
    @Override
    public int available() throws IOException {
        return this.endIndex - this.buffer.readerIndex();
    }
    
    @Override
    public void mark(final int readlimit) {
        this.buffer.markReaderIndex();
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public int read() throws IOException {
        if (!this.buffer.isReadable()) {
            return -1;
        }
        return this.buffer.readByte() & 0xFF;
    }
    
    @Override
    public int read(final byte[] b, final int off, int len) throws IOException {
        final int available = this.available();
        if (available == 0) {
            return -1;
        }
        len = Math.min(available, len);
        this.buffer.readBytes(b, off, len);
        return len;
    }
    
    @Override
    public void reset() throws IOException {
        this.buffer.resetReaderIndex();
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n > 2147483647L) {
            return this.skipBytes(Integer.MAX_VALUE);
        }
        return this.skipBytes((int)n);
    }
    
    @Override
    public boolean readBoolean() throws IOException {
        this.checkAvailable(1);
        return this.read() != 0;
    }
    
    @Override
    public byte readByte() throws IOException {
        if (!this.buffer.isReadable()) {
            throw new EOFException();
        }
        return this.buffer.readByte();
    }
    
    @Override
    public char readChar() throws IOException {
        return (char)this.readShort();
    }
    
    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }
    
    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }
    
    @Override
    public void readFully(final byte[] b) throws IOException {
        this.readFully(b, 0, b.length);
    }
    
    @Override
    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        this.checkAvailable(len);
        this.buffer.readBytes(b, off, len);
    }
    
    @Override
    public int readInt() throws IOException {
        this.checkAvailable(4);
        return this.buffer.readInt();
    }
    
    @Override
    public String readLine() throws IOException {
        this.lineBuf.setLength(0);
        while (this.buffer.isReadable()) {
            final int c = this.buffer.readUnsignedByte();
            switch (c) {
                case 10: {
                    break;
                }
                case 13: {
                    if (this.buffer.isReadable() && (char)this.buffer.getUnsignedByte(this.buffer.readerIndex()) == '\n') {
                        this.buffer.skipBytes(1);
                        break;
                    }
                    break;
                }
                default: {
                    this.lineBuf.append((char)c);
                    continue;
                }
            }
            return this.lineBuf.toString();
        }
        return (this.lineBuf.length() > 0) ? this.lineBuf.toString() : null;
    }
    
    @Override
    public long readLong() throws IOException {
        this.checkAvailable(8);
        return this.buffer.readLong();
    }
    
    @Override
    public short readShort() throws IOException {
        this.checkAvailable(2);
        return this.buffer.readShort();
    }
    
    @Override
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
    
    @Override
    public int readUnsignedByte() throws IOException {
        return this.readByte() & 0xFF;
    }
    
    @Override
    public int readUnsignedShort() throws IOException {
        return this.readShort() & 0xFFFF;
    }
    
    @Override
    public int skipBytes(final int n) throws IOException {
        final int nBytes = Math.min(this.available(), n);
        this.buffer.skipBytes(nBytes);
        return nBytes;
    }
    
    private void checkAvailable(final int fieldSize) throws IOException {
        if (fieldSize < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        }
        if (fieldSize > this.available()) {
            throw new EOFException("fieldSize is too long! Length is " + fieldSize + ", but maximum is " + this.available());
        }
    }
}
