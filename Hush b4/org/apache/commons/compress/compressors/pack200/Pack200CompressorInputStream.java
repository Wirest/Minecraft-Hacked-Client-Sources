// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.pack200;

import java.io.FilterInputStream;
import java.util.jar.Pack200;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class Pack200CompressorInputStream extends CompressorInputStream
{
    private final InputStream originalInput;
    private final StreamBridge streamBridge;
    private static final byte[] CAFE_DOOD;
    private static final int SIG_LENGTH;
    
    public Pack200CompressorInputStream(final InputStream in) throws IOException {
        this(in, Pack200Strategy.IN_MEMORY);
    }
    
    public Pack200CompressorInputStream(final InputStream in, final Pack200Strategy mode) throws IOException {
        this(in, null, mode, null);
    }
    
    public Pack200CompressorInputStream(final InputStream in, final Map<String, String> props) throws IOException {
        this(in, Pack200Strategy.IN_MEMORY, props);
    }
    
    public Pack200CompressorInputStream(final InputStream in, final Pack200Strategy mode, final Map<String, String> props) throws IOException {
        this(in, null, mode, props);
    }
    
    public Pack200CompressorInputStream(final File f) throws IOException {
        this(f, Pack200Strategy.IN_MEMORY);
    }
    
    public Pack200CompressorInputStream(final File f, final Pack200Strategy mode) throws IOException {
        this(null, f, mode, null);
    }
    
    public Pack200CompressorInputStream(final File f, final Map<String, String> props) throws IOException {
        this(f, Pack200Strategy.IN_MEMORY, props);
    }
    
    public Pack200CompressorInputStream(final File f, final Pack200Strategy mode, final Map<String, String> props) throws IOException {
        this(null, f, mode, props);
    }
    
    private Pack200CompressorInputStream(final InputStream in, final File f, final Pack200Strategy mode, final Map<String, String> props) throws IOException {
        this.originalInput = in;
        this.streamBridge = mode.newStreamBridge();
        final JarOutputStream jarOut = new JarOutputStream(this.streamBridge);
        final Pack200.Unpacker u = Pack200.newUnpacker();
        if (props != null) {
            u.properties().putAll((Map<?, ?>)props);
        }
        if (f == null) {
            u.unpack(new FilterInputStream(in) {
                @Override
                public void close() {
                }
            }, jarOut);
        }
        else {
            u.unpack(f, jarOut);
        }
        jarOut.close();
    }
    
    @Override
    public int read() throws IOException {
        return this.streamBridge.getInput().read();
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.streamBridge.getInput().read(b);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int count) throws IOException {
        return this.streamBridge.getInput().read(b, off, count);
    }
    
    @Override
    public int available() throws IOException {
        return this.streamBridge.getInput().available();
    }
    
    @Override
    public boolean markSupported() {
        try {
            return this.streamBridge.getInput().markSupported();
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    @Override
    public void mark(final int limit) {
        try {
            this.streamBridge.getInput().mark(limit);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void reset() throws IOException {
        this.streamBridge.getInput().reset();
    }
    
    @Override
    public long skip(final long count) throws IOException {
        return this.streamBridge.getInput().skip(count);
    }
    
    @Override
    public void close() throws IOException {
        try {
            this.streamBridge.stop();
        }
        finally {
            if (this.originalInput != null) {
                this.originalInput.close();
            }
        }
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        if (length < Pack200CompressorInputStream.SIG_LENGTH) {
            return false;
        }
        for (int i = 0; i < Pack200CompressorInputStream.SIG_LENGTH; ++i) {
            if (signature[i] != Pack200CompressorInputStream.CAFE_DOOD[i]) {
                return false;
            }
        }
        return true;
    }
    
    static {
        CAFE_DOOD = new byte[] { -54, -2, -48, 13 };
        SIG_LENGTH = Pack200CompressorInputStream.CAFE_DOOD.length;
    }
}
