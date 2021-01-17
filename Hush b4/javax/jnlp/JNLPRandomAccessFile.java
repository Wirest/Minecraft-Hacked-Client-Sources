// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.io.IOException;
import java.io.DataOutput;
import java.io.DataInput;

public interface JNLPRandomAccessFile extends DataInput, DataOutput
{
    void close() throws IOException;
    
    long getFilePointer() throws IOException;
    
    long length() throws IOException;
    
    int read() throws IOException;
    
    int read(final byte[] p0) throws IOException;
    
    int read(final byte[] p0, final int p1, final int p2) throws IOException;
    
    boolean readBoolean() throws IOException;
    
    byte readByte() throws IOException;
    
    char readChar() throws IOException;
    
    double readDouble() throws IOException;
    
    float readFloat() throws IOException;
    
    void readFully(final byte[] p0) throws IOException;
    
    void readFully(final byte[] p0, final int p1, final int p2) throws IOException;
    
    int readInt() throws IOException;
    
    String readLine() throws IOException;
    
    long readLong() throws IOException;
    
    short readShort() throws IOException;
    
    String readUTF() throws IOException;
    
    int readUnsignedByte() throws IOException;
    
    int readUnsignedShort() throws IOException;
    
    void seek(final long p0) throws IOException;
    
    void setLength(final long p0) throws IOException;
    
    int skipBytes(final int p0) throws IOException;
    
    void write(final int p0) throws IOException;
    
    void write(final byte[] p0) throws IOException;
    
    void write(final byte[] p0, final int p1, final int p2) throws IOException;
    
    void writeBoolean(final boolean p0) throws IOException;
    
    void writeByte(final int p0) throws IOException;
    
    void writeBytes(final String p0) throws IOException;
    
    void writeChar(final int p0) throws IOException;
    
    void writeChars(final String p0) throws IOException;
    
    void writeDouble(final double p0) throws IOException;
    
    void writeFloat(final float p0) throws IOException;
    
    void writeInt(final int p0) throws IOException;
    
    void writeLong(final long p0) throws IOException;
    
    void writeShort(final int p0) throws IOException;
    
    void writeUTF(final String p0) throws IOException;
}
