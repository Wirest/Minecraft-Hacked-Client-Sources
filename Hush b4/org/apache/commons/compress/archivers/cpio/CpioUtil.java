// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.cpio;

class CpioUtil
{
    static long fileType(final long mode) {
        return mode & 0xF000L;
    }
    
    static long byteArray2long(final byte[] number, final boolean swapHalfWord) {
        if (number.length % 2 != 0) {
            throw new UnsupportedOperationException();
        }
        long ret = 0L;
        int pos = 0;
        final byte[] tmp_number = new byte[number.length];
        System.arraycopy(number, 0, tmp_number, 0, number.length);
        if (!swapHalfWord) {
            byte tmp;
            for (tmp = 0, pos = 0; pos < tmp_number.length; tmp_number[pos++] = tmp_number[pos], tmp_number[pos] = tmp, ++pos) {
                tmp = tmp_number[pos];
            }
        }
        ret = (tmp_number[0] & 0xFF);
        for (pos = 1; pos < tmp_number.length; ++pos) {
            ret <<= 8;
            ret |= (tmp_number[pos] & 0xFF);
        }
        return ret;
    }
    
    static byte[] long2byteArray(final long number, final int length, final boolean swapHalfWord) {
        final byte[] ret = new byte[length];
        int pos = 0;
        long tmp_number = 0L;
        if (length % 2 != 0 || length < 2) {
            throw new UnsupportedOperationException();
        }
        tmp_number = number;
        for (pos = length - 1; pos >= 0; --pos) {
            ret[pos] = (byte)(tmp_number & 0xFFL);
            tmp_number >>= 8;
        }
        if (!swapHalfWord) {
            byte tmp;
            for (tmp = 0, pos = 0; pos < length; ret[pos++] = ret[pos], ret[pos] = tmp, ++pos) {
                tmp = ret[pos];
            }
        }
        return ret;
    }
}
