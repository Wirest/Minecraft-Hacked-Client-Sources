// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.InputStream;

public class CloseShieldInputStream extends ProxyInputStream
{
    public CloseShieldInputStream(final InputStream in) {
        super(in);
    }
    
    @Override
    public void close() {
        this.in = new ClosedInputStream();
    }
}
