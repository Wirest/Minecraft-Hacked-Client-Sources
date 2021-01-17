// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class AutoCloseInputStream extends ProxyInputStream
{
    public AutoCloseInputStream(final InputStream in) {
        super(in);
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
        this.in = new ClosedInputStream();
    }
    
    @Override
    protected void afterRead(final int n) throws IOException {
        if (n == -1) {
            this.close();
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
