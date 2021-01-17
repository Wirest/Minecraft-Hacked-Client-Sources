// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.primitives.Longs;
import java.io.IOException;
import java.io.DataOutputStream;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import com.google.common.annotations.Beta;
import java.io.DataOutput;
import java.io.FilterOutputStream;

@Beta
public class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput
{
    public LittleEndianDataOutputStream(final OutputStream out) {
        super(new DataOutputStream(Preconditions.checkNotNull(out)));
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
    }
    
    @Override
    public void writeBoolean(final boolean v) throws IOException {
        ((DataOutputStream)this.out).writeBoolean(v);
    }
    
    @Override
    public void writeByte(final int v) throws IOException {
        ((DataOutputStream)this.out).writeByte(v);
    }
    
    @Deprecated
    @Override
    public void writeBytes(final String s) throws IOException {
        ((DataOutputStream)this.out).writeBytes(s);
    }
    
    @Override
    public void writeChar(final int v) throws IOException {
        this.writeShort(v);
    }
    
    @Override
    public void writeChars(final String s) throws IOException {
        for (int i = 0; i < s.length(); ++i) {
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
        this.out.write(0xFF & v);
        this.out.write(0xFF & v >> 8);
        this.out.write(0xFF & v >> 16);
        this.out.write(0xFF & v >> 24);
    }
    
    @Override
    public void writeLong(final long v) throws IOException {
        final byte[] bytes = Longs.toByteArray(Long.reverseBytes(v));
        this.write(bytes, 0, bytes.length);
    }
    
    @Override
    public void writeShort(final int v) throws IOException {
        this.out.write(0xFF & v);
        this.out.write(0xFF & v >> 8);
    }
    
    @Override
    public void writeUTF(final String str) throws IOException {
        ((DataOutputStream)this.out).writeUTF(str);
    }
    
    @Override
    public void close() throws IOException {
        this.out.close();
    }
}
