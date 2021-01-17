// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.DataOutput;

public interface ByteArrayDataOutput extends DataOutput
{
    void write(final int p0);
    
    void write(final byte[] p0);
    
    void write(final byte[] p0, final int p1, final int p2);
    
    void writeBoolean(final boolean p0);
    
    void writeByte(final int p0);
    
    void writeShort(final int p0);
    
    void writeChar(final int p0);
    
    void writeInt(final int p0);
    
    void writeLong(final long p0);
    
    void writeFloat(final float p0);
    
    void writeDouble(final double p0);
    
    void writeChars(final String p0);
    
    void writeUTF(final String p0);
    
    @Deprecated
    void writeBytes(final String p0);
    
    byte[] toByteArray();
}
