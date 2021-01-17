// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.Serializable;

public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable
{
    private static final long serialVersionUID = -547733176983104172L;
    private final byte[] magicNumbers;
    private final long byteOffset;
    
    public MagicNumberFileFilter(final byte[] magicNumber) {
        this(magicNumber, 0L);
    }
    
    public MagicNumberFileFilter(final String magicNumber) {
        this(magicNumber, 0L);
    }
    
    public MagicNumberFileFilter(final String magicNumber, final long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.length() == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0L) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
        this.magicNumbers = magicNumber.getBytes();
        this.byteOffset = offset;
    }
    
    public MagicNumberFileFilter(final byte[] magicNumber, final long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0L) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
        System.arraycopy(magicNumber, 0, this.magicNumbers = new byte[magicNumber.length], 0, magicNumber.length);
        this.byteOffset = offset;
    }
    
    @Override
    public boolean accept(final File file) {
        if (file != null && file.isFile() && file.canRead()) {
            RandomAccessFile randomAccessFile = null;
            try {
                final byte[] fileBytes = new byte[this.magicNumbers.length];
                randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(this.byteOffset);
                final int read = randomAccessFile.read(fileBytes);
                return read == this.magicNumbers.length && Arrays.equals(this.magicNumbers, fileBytes);
            }
            catch (IOException ioe) {}
            finally {
                IOUtils.closeQuietly(randomAccessFile);
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());
        builder.append("(");
        builder.append(new String(this.magicNumbers));
        builder.append(",");
        builder.append(this.byteOffset);
        builder.append(")");
        return builder.toString();
    }
}
