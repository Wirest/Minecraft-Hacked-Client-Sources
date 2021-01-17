// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.OutputStream;

public class CountingOutputStream extends ProxyOutputStream
{
    private long count;
    
    public CountingOutputStream(final OutputStream out) {
        super(out);
        this.count = 0L;
    }
    
    @Override
    protected synchronized void beforeWrite(final int n) {
        this.count += n;
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
