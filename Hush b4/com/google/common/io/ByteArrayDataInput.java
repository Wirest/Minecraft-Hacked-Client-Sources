// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.DataInput;

public interface ByteArrayDataInput extends DataInput
{
    void readFully(final byte[] p0);
    
    void readFully(final byte[] p0, final int p1, final int p2);
    
    int skipBytes(final int p0);
    
    boolean readBoolean();
    
    byte readByte();
    
    int readUnsignedByte();
    
    short readShort();
    
    int readUnsignedShort();
    
    char readChar();
    
    int readInt();
    
    long readLong();
    
    float readFloat();
    
    double readDouble();
    
    String readLine();
    
    String readUTF();
}
