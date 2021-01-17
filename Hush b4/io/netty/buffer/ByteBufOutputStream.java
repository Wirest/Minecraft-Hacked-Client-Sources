// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataOutput;
import java.io.OutputStream;

public class ByteBufOutputStream extends OutputStream implements DataOutput
{
    private final ByteBuf buffer;
    private final int startIndex;
    private final DataOutputStream utf8out;
    
    public ByteBufOutputStream(final ByteBuf buffer) {
        this.utf8out = new DataOutputStream(this);
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        this.buffer = buffer;
        this.startIndex = buffer.writerIndex();
    }
    
    public int writtenBytes() {
        return this.buffer.writerIndex() - this.startIndex;
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (len == 0) {
            return;
        }
        this.buffer.writeBytes(b, off, len);
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.buffer.writeBytes(b);
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.buffer.writeByte((byte)b);
    }
    
    @Override
    public void writeBoolean(final boolean v) throws IOException {
        this.write(v ? 1 : 0);
    }
    
    @Override
    public void writeByte(final int v) throws IOException {
        this.write(v);
    }
    
    @Override
    public void writeBytes(final String s) throws IOException {
        for (int len = s.length(), i = 0; i < len; ++i) {
            this.write((byte)s.charAt(i));
        }
    }
    
    @Override
    public void writeChar(final int v) throws IOException {
        this.writeShort((short)v);
    }
    
    @Override
    public void writeChars(final String s) throws IOException {
        for (int len = s.length(), i = 0; i < len; ++i) {
            this.writeChar(s.charAt(i));
        }
    }
    
    @Override
    public void writeDouble(final double v) throws IOException {
        this.writeLong(Double.doubleToLongBits(v));
    }
    
    @Override
    public void writeFloat(final float v) throws IOException {
        this.writeInt(Float.floatToIntBits(v));
    }
    
    @Override
    public void writeInt(final int v) throws IOException {
        this.buffer.writeInt(v);
    }
    
    @Override
    public void writeLong(final long v) throws IOException {
        this.buffer.writeLong(v);
    }
    
    @Override
    public void writeShort(final int v) throws IOException {
        this.buffer.writeShort((short)v);
    }
    
    @Override
    public void writeUTF(final String s) throws IOException {
        this.utf8out.writeUTF(s);
    }
    
    public ByteBuf buffer() {
        return this.buffer;
    }
}
