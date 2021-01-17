// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FilterOutputStream;

abstract class StreamBridge extends FilterOutputStream
{
    private InputStream input;
    private final Object INPUT_LOCK;
    
    protected StreamBridge(final OutputStream out) {
        super(out);
        this.INPUT_LOCK = new Object();
    }
    
    protected StreamBridge() {
        this(null);
    }
    
    InputStream getInput() throws IOException {
        synchronized (this.INPUT_LOCK) {
            if (this.input == null) {
                this.input = this.getInputView();
            }
        }
        return this.input;
    }
    
    abstract InputStream getInputView() throws IOException;
    
    void stop() throws IOException {
        this.close();
        synchronized (this.INPUT_LOCK) {
            if (this.input != null) {
                this.input.close();
                this.input = null;
            }
        }
    }
}
