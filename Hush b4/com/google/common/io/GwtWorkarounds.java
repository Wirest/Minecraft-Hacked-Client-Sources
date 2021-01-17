// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.Writer;
import java.io.OutputStream;
import java.io.InputStream;
import com.google.common.annotations.GwtIncompatible;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.Reader;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class GwtWorkarounds
{
    private GwtWorkarounds() {
    }
    
    @GwtIncompatible("Reader")
    static CharInput asCharInput(final Reader reader) {
        Preconditions.checkNotNull(reader);
        return new CharInput() {
            @Override
            public int read() throws IOException {
                return reader.read();
            }
            
            @Override
            public void close() throws IOException {
                reader.close();
            }
        };
    }
    
    static CharInput asCharInput(final CharSequence chars) {
        Preconditions.checkNotNull(chars);
        return new CharInput() {
            int index = 0;
            
            @Override
            public int read() {
                if (this.index < chars.length()) {
                    return chars.charAt(this.index++);
                }
                return -1;
            }
            
            @Override
            public void close() {
                this.index = chars.length();
            }
        };
    }
    
    @GwtIncompatible("InputStream")
    static InputStream asInputStream(final ByteInput input) {
        Preconditions.checkNotNull(input);
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return input.read();
            }
            
            @Override
            public int read(final byte[] b, final int off, final int len) throws IOException {
                Preconditions.checkNotNull(b);
                Preconditions.checkPositionIndexes(off, off + len, b.length);
                if (len == 0) {
                    return 0;
                }
                final int firstByte = this.read();
                if (firstByte == -1) {
                    return -1;
                }
                b[off] = (byte)firstByte;
                for (int dst = 1; dst < len; ++dst) {
                    final int readByte = this.read();
                    if (readByte == -1) {
                        return dst;
                    }
                    b[off + dst] = (byte)readByte;
                }
                return len;
            }
            
            @Override
            public void close() throws IOException {
                input.close();
            }
        };
    }
    
    @GwtIncompatible("OutputStream")
    static OutputStream asOutputStream(final ByteOutput output) {
        Preconditions.checkNotNull(output);
        return new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                output.write((byte)b);
            }
            
            @Override
            public void flush() throws IOException {
                output.flush();
            }
            
            @Override
            public void close() throws IOException {
                output.close();
            }
        };
    }
    
    @GwtIncompatible("Writer")
    static CharOutput asCharOutput(final Writer writer) {
        Preconditions.checkNotNull(writer);
        return new CharOutput() {
            @Override
            public void write(final char c) throws IOException {
                writer.append(c);
            }
            
            @Override
            public void flush() throws IOException {
                writer.flush();
            }
            
            @Override
            public void close() throws IOException {
                writer.close();
            }
        };
    }
    
    static CharOutput stringBuilderOutput(final int initialSize) {
        final StringBuilder builder = new StringBuilder(initialSize);
        return new CharOutput() {
            @Override
            public void write(final char c) {
                builder.append(c);
            }
            
            @Override
            public void flush() {
            }
            
            @Override
            public void close() {
            }
            
            @Override
            public String toString() {
                return builder.toString();
            }
        };
    }
    
    interface CharOutput
    {
        void write(final char p0) throws IOException;
        
        void flush() throws IOException;
        
        void close() throws IOException;
    }
    
    interface ByteOutput
    {
        void write(final byte p0) throws IOException;
        
        void flush() throws IOException;
        
        void close() throws IOException;
    }
    
    interface ByteInput
    {
        int read() throws IOException;
        
        void close() throws IOException;
    }
    
    interface CharInput
    {
        int read() throws IOException;
        
        void close() throws IOException;
    }
}
