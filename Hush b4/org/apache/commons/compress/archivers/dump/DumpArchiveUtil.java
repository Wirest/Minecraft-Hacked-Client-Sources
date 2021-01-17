// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncoding;

class DumpArchiveUtil
{
    private DumpArchiveUtil() {
    }
    
    public static int calculateChecksum(final byte[] buffer) {
        int calc = 0;
        for (int i = 0; i < 256; ++i) {
            calc += convert32(buffer, 4 * i);
        }
        return 84446 - (calc - convert32(buffer, 28));
    }
    
    public static final boolean verify(final byte[] buffer) {
        final int magic = convert32(buffer, 24);
        if (magic != 60012) {
            return false;
        }
        final int checksum = convert32(buffer, 28);
        return checksum == calculateChecksum(buffer);
    }
    
    public static final int getIno(final byte[] buffer) {
        return convert32(buffer, 20);
    }
    
    public static final long convert64(final byte[] buffer, final int offset) {
        long i = 0L;
        i += (long)buffer[offset + 7] << 56;
        i += ((long)buffer[offset + 6] << 48 & 0xFF000000000000L);
        i += ((long)buffer[offset + 5] << 40 & 0xFF0000000000L);
        i += ((long)buffer[offset + 4] << 32 & 0xFF00000000L);
        i += ((long)buffer[offset + 3] << 24 & 0xFF000000L);
        i += ((long)buffer[offset + 2] << 16 & 0xFF0000L);
        i += ((long)buffer[offset + 1] << 8 & 0xFF00L);
        i += ((long)buffer[offset] & 0xFFL);
        return i;
    }
    
    public static final int convert32(final byte[] buffer, final int offset) {
        int i = 0;
        i = buffer[offset + 3] << 24;
        i += (buffer[offset + 2] << 16 & 0xFF0000);
        i += (buffer[offset + 1] << 8 & 0xFF00);
        i += (buffer[offset] & 0xFF);
        return i;
    }
    
    public static final int convert16(final byte[] buffer, final int offset) {
        int i = 0;
        i += (buffer[offset + 1] << 8 & 0xFF00);
        i += (buffer[offset] & 0xFF);
        return i;
    }
    
    static String decode(final ZipEncoding encoding, final byte[] b, final int offset, final int len) throws IOException {
        final byte[] copy = new byte[len];
        System.arraycopy(b, offset, copy, 0, len);
        return encoding.decode(copy);
    }
}
