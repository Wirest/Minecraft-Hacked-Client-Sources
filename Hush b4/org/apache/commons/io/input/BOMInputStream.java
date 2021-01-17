// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.util.Iterator;
import java.io.IOException;
import java.util.Arrays;
import java.io.InputStream;
import java.util.Comparator;
import org.apache.commons.io.ByteOrderMark;
import java.util.List;

public class BOMInputStream extends ProxyInputStream
{
    private final boolean include;
    private final List<ByteOrderMark> boms;
    private ByteOrderMark byteOrderMark;
    private int[] firstBytes;
    private int fbLength;
    private int fbIndex;
    private int markFbIndex;
    private boolean markedAtStart;
    private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator;
    
    public BOMInputStream(final InputStream delegate) {
        this(delegate, false, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
    }
    
    public BOMInputStream(final InputStream delegate, final boolean include) {
        this(delegate, include, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
    }
    
    public BOMInputStream(final InputStream delegate, final ByteOrderMark... boms) {
        this(delegate, false, boms);
    }
    
    public BOMInputStream(final InputStream delegate, final boolean include, final ByteOrderMark... boms) {
        super(delegate);
        if (boms == null || boms.length == 0) {
            throw new IllegalArgumentException("No BOMs specified");
        }
        this.include = include;
        Arrays.sort(boms, BOMInputStream.ByteOrderMarkLengthComparator);
        this.boms = Arrays.asList(boms);
    }
    
    public boolean hasBOM() throws IOException {
        return this.getBOM() != null;
    }
    
    public boolean hasBOM(final ByteOrderMark bom) throws IOException {
        if (!this.boms.contains(bom)) {
            throw new IllegalArgumentException("Stream not configure to detect " + bom);
        }
        return this.byteOrderMark != null && this.getBOM().equals(bom);
    }
    
    public ByteOrderMark getBOM() throws IOException {
        if (this.firstBytes == null) {
            this.fbLength = 0;
            final int maxBomSize = this.boms.get(0).length();
            this.firstBytes = new int[maxBomSize];
            for (int i = 0; i < this.firstBytes.length; ++i) {
                this.firstBytes[i] = this.in.read();
                ++this.fbLength;
                if (this.firstBytes[i] < 0) {
                    break;
                }
            }
            this.byteOrderMark = this.find();
            if (this.byteOrderMark != null && !this.include) {
                if (this.byteOrderMark.length() < this.firstBytes.length) {
                    this.fbIndex = this.byteOrderMark.length();
                }
                else {
                    this.fbLength = 0;
                }
            }
        }
        return this.byteOrderMark;
    }
    
    public String getBOMCharsetName() throws IOException {
        this.getBOM();
        return (this.byteOrderMark == null) ? null : this.byteOrderMark.getCharsetName();
    }
    
    private int readFirstBytes() throws IOException {
        this.getBOM();
        return (this.fbIndex < this.fbLength) ? this.firstBytes[this.fbIndex++] : -1;
    }
    
    private ByteOrderMark find() {
        for (final ByteOrderMark bom : this.boms) {
            if (this.matches(bom)) {
                return bom;
            }
        }
        return null;
    }
    
    private boolean matches(final ByteOrderMark bom) {
        for (int i = 0; i < bom.length(); ++i) {
            if (bom.get(i) != this.firstBytes[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int read() throws IOException {
        final int b = this.readFirstBytes();
        return (b >= 0) ? b : this.in.read();
    }
    
    @Override
    public int read(final byte[] buf, int off, int len) throws IOException {
        int firstCount = 0;
        for (int b = 0; len > 0 && b >= 0; --len, ++firstCount) {
            b = this.readFirstBytes();
            if (b >= 0) {
                buf[off++] = (byte)(b & 0xFF);
            }
        }
        final int secondCount = this.in.read(buf, off, len);
        return (secondCount < 0) ? ((firstCount > 0) ? firstCount : -1) : (firstCount + secondCount);
    }
    
    @Override
    public int read(final byte[] buf) throws IOException {
        return this.read(buf, 0, buf.length);
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        this.markFbIndex = this.fbIndex;
        this.markedAtStart = (this.firstBytes == null);
        this.in.mark(readlimit);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        this.fbIndex = this.markFbIndex;
        if (this.markedAtStart) {
            this.firstBytes = null;
        }
        this.in.reset();
    }
    
    @Override
    public long skip(long n) throws IOException {
        while (n > 0L && this.readFirstBytes() >= 0) {
            --n;
        }
        return this.in.skip(n);
    }
    
    static {
        ByteOrderMarkLengthComparator = new Comparator<ByteOrderMark>() {
            @Override
            public int compare(final ByteOrderMark bom1, final ByteOrderMark bom2) {
                final int len1 = bom1.length();
                final int len2 = bom2.length();
                if (len1 > len2) {
                    return -1;
                }
                if (len2 > len1) {
                    return 1;
                }
                return 0;
            }
        };
    }
}
