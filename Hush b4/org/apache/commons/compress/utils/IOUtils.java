// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.utils;

import java.io.Closeable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public final class IOUtils
{
    private static final int COPY_BUF_SIZE = 8024;
    private static final int SKIP_BUF_SIZE = 4096;
    private static final byte[] SKIP_BUF;
    
    private IOUtils() {
    }
    
    public static long copy(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, 8024);
    }
    
    public static long copy(final InputStream input, final OutputStream output, final int buffersize) throws IOException {
        final byte[] buffer = new byte[buffersize];
        int n = 0;
        long count = 0L;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    public static long skip(final InputStream input, long numToSkip) throws IOException {
        final long available = numToSkip;
        while (numToSkip > 0L) {
            final long skipped = input.skip(numToSkip);
            if (skipped == 0L) {
                break;
            }
            numToSkip -= skipped;
        }
        while (numToSkip > 0L) {
            final int read = readFully(input, IOUtils.SKIP_BUF, 0, (int)Math.min(numToSkip, 4096L));
            if (read < 1) {
                break;
            }
            numToSkip -= read;
        }
        return available - numToSkip;
    }
    
    public static int readFully(final InputStream input, final byte[] b) throws IOException {
        return readFully(input, b, 0, b.length);
    }
    
    public static int readFully(final InputStream input, final byte[] b, final int offset, final int len) throws IOException {
        if (len < 0 || offset < 0 || len + offset > b.length) {
            throw new IndexOutOfBoundsException();
        }
        int count = 0;
        for (int x = 0; count != len; count += x) {
            x = input.read(b, offset + count, len - count);
            if (x == -1) {
                break;
            }
        }
        return count;
    }
    
    public static byte[] toByteArray(final InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
    
    public static void closeQuietly(final Closeable c) {
        if (c != null) {
            try {
                c.close();
            }
            catch (IOException ex) {}
        }
    }
    
    static {
        SKIP_BUF = new byte[4096];
    }
}
