// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream extends ProxyInputStream
{
    private final OutputStream branch;
    private final boolean closeBranch;
    
    public TeeInputStream(final InputStream input, final OutputStream branch) {
        this(input, branch, false);
    }
    
    public TeeInputStream(final InputStream input, final OutputStream branch, final boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = closeBranch;
    }
    
    @Override
    public void close() throws IOException {
        try {
            super.close();
        }
        finally {
            if (this.closeBranch) {
                this.branch.close();
            }
        }
    }
    
    @Override
    public int read() throws IOException {
        final int ch = super.read();
        if (ch != -1) {
            this.branch.write(ch);
        }
        return ch;
    }
    
    @Override
    public int read(final byte[] bts, final int st, final int end) throws IOException {
        final int n = super.read(bts, st, end);
        if (n != -1) {
            this.branch.write(bts, st, n);
        }
        return n;
    }
    
    @Override
    public int read(final byte[] bts) throws IOException {
        final int n = super.read(bts);
        if (n != -1) {
            this.branch.write(bts, 0, n);
        }
        return n;
    }
}
