// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.utils;

import java.util.zip.Checksum;
import java.util.zip.CRC32;
import java.io.InputStream;

public class CRC32VerifyingInputStream extends ChecksumVerifyingInputStream
{
    public CRC32VerifyingInputStream(final InputStream in, final long size, final int expectedCrc32) {
        this(in, size, (long)expectedCrc32 & 0xFFFFFFFFL);
    }
    
    public CRC32VerifyingInputStream(final InputStream in, final long size, final long expectedCrc32) {
        super(new CRC32(), in, size, expectedCrc32);
    }
}
