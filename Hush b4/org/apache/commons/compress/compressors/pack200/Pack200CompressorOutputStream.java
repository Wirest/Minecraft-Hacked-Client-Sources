// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.pack200;

import java.io.Closeable;
import org.apache.commons.compress.utils.IOUtils;
import java.util.jar.JarInputStream;
import java.util.jar.Pack200;
import java.io.IOException;
import java.util.Map;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

public class Pack200CompressorOutputStream extends CompressorOutputStream
{
    private boolean finished;
    private final OutputStream originalOutput;
    private final StreamBridge streamBridge;
    private final Map<String, String> properties;
    
    public Pack200CompressorOutputStream(final OutputStream out) throws IOException {
        this(out, Pack200Strategy.IN_MEMORY);
    }
    
    public Pack200CompressorOutputStream(final OutputStream out, final Pack200Strategy mode) throws IOException {
        this(out, mode, null);
    }
    
    public Pack200CompressorOutputStream(final OutputStream out, final Map<String, String> props) throws IOException {
        this(out, Pack200Strategy.IN_MEMORY, props);
    }
    
    public Pack200CompressorOutputStream(final OutputStream out, final Pack200Strategy mode, final Map<String, String> props) throws IOException {
        this.finished = false;
        this.originalOutput = out;
        this.streamBridge = mode.newStreamBridge();
        this.properties = props;
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.streamBridge.write(b);
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.streamBridge.write(b);
    }
    
    @Override
    public void write(final byte[] b, final int from, final int length) throws IOException {
        this.streamBridge.write(b, from, length);
    }
    
    @Override
    public void close() throws IOException {
        this.finish();
        try {
            this.streamBridge.stop();
        }
        finally {
            this.originalOutput.close();
        }
    }
    
    public void finish() throws IOException {
        if (!this.finished) {
            this.finished = true;
            final Pack200.Packer p = Pack200.newPacker();
            if (this.properties != null) {
                p.properties().putAll((Map<?, ?>)this.properties);
            }
            JarInputStream ji = null;
            boolean success = false;
            try {
                p.pack(ji = new JarInputStream(this.streamBridge.getInput()), this.originalOutput);
                success = true;
            }
            finally {
                if (!success) {
                    IOUtils.closeQuietly(ji);
                }
            }
        }
    }
}
