// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends ProxyInputStream
{
    private long count;
    
    public CountingInputStream(final InputStream in) {
        super(in);
    }
    
    @Override
    public synchronized long skip(final long length) throws IOException {
        final long skip = super.skip(length);
        this.count += skip;
        return skip;
    }
    
    @Override
    protected synchronized void afterRead(final int n) {
        if (n != -1) {
            this.count += n;
        }
    }
    
    public int getCount() {
        final long result = this.getByteCount();
        if (result > 2147483647L) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int)result;
    }
    
    public int resetCount() {
        final long result = this.resetByteCount();
        if (result > 2147483647L) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int)result;
    }
    
    public synchronized long getByteCount() {
        return this.count;
    }
    
    public synchronized long resetByteCount() {
        final long tmp = this.count;
        this.count = 0L;
        return tmp;
    }
}
