// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;
import java.math.BigInteger;
import java.io.Serializable;

public class X7875_NewUnix implements ZipExtraField, Cloneable, Serializable
{
    private static final ZipShort HEADER_ID;
    private static final BigInteger ONE_THOUSAND;
    private static final long serialVersionUID = 1L;
    private int version;
    private BigInteger uid;
    private BigInteger gid;
    
    public X7875_NewUnix() {
        this.version = 1;
        this.reset();
    }
    
    public ZipShort getHeaderId() {
        return X7875_NewUnix.HEADER_ID;
    }
    
    public long getUID() {
        return ZipUtil.bigToLong(this.uid);
    }
    
    public long getGID() {
        return ZipUtil.bigToLong(this.gid);
    }
    
    public void setUID(final long l) {
        this.uid = ZipUtil.longToBig(l);
    }
    
    public void setGID(final long l) {
        this.gid = ZipUtil.longToBig(l);
    }
    
    public ZipShort getLocalFileDataLength() {
        final int uidSize = trimLeadingZeroesForceMinLength(this.uid.toByteArray()).length;
        final int gidSize = trimLeadingZeroesForceMinLength(this.gid.toByteArray()).length;
        return new ZipShort(3 + uidSize + gidSize);
    }
    
    public ZipShort getCentralDirectoryLength() {
        return this.getLocalFileDataLength();
    }
    
    public byte[] getLocalFileDataData() {
        byte[] uidBytes = this.uid.toByteArray();
        byte[] gidBytes = this.gid.toByteArray();
        uidBytes = trimLeadingZeroesForceMinLength(uidBytes);
        gidBytes = trimLeadingZeroesForceMinLength(gidBytes);
        final byte[] data = new byte[3 + uidBytes.length + gidBytes.length];
        ZipUtil.reverse(uidBytes);
        ZipUtil.reverse(gidBytes);
        int pos = 0;
        data[pos++] = ZipUtil.unsignedIntToSignedByte(this.version);
        data[pos++] = ZipUtil.unsignedIntToSignedByte(uidBytes.length);
        System.arraycopy(uidBytes, 0, data, pos, uidBytes.length);
        pos += uidBytes.length;
        data[pos++] = ZipUtil.unsignedIntToSignedByte(gidBytes.length);
        System.arraycopy(gidBytes, 0, data, pos, gidBytes.length);
        return data;
    }
    
    public byte[] getCentralDirectoryData() {
        return this.getLocalFileDataData();
    }
    
    public void parseFromLocalFileData(final byte[] data, int offset, final int length) throws ZipException {
        this.reset();
        this.version = ZipUtil.signedByteToUnsignedInt(data[offset++]);
        final int uidSize = ZipUtil.signedByteToUnsignedInt(data[offset++]);
        final byte[] uidBytes = new byte[uidSize];
        System.arraycopy(data, offset, uidBytes, 0, uidSize);
        offset += uidSize;
        this.uid = new BigInteger(1, ZipUtil.reverse(uidBytes));
        final int gidSize = ZipUtil.signedByteToUnsignedInt(data[offset++]);
        final byte[] gidBytes = new byte[gidSize];
        System.arraycopy(data, offset, gidBytes, 0, gidSize);
        this.gid = new BigInteger(1, ZipUtil.reverse(gidBytes));
    }
    
    public void parseFromCentralDirectoryData(final byte[] buffer, final int offset, final int length) throws ZipException {
        this.reset();
        this.parseFromLocalFileData(buffer, offset, length);
    }
    
    private void reset() {
        this.uid = X7875_NewUnix.ONE_THOUSAND;
        this.gid = X7875_NewUnix.ONE_THOUSAND;
    }
    
    @Override
    public String toString() {
        return "0x7875 Zip Extra Field: UID=" + this.uid + " GID=" + this.gid;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof X7875_NewUnix) {
            final X7875_NewUnix xf = (X7875_NewUnix)o;
            return this.version == xf.version && this.uid.equals(xf.uid) && this.gid.equals(xf.gid);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hc = -1234567 * this.version;
        hc ^= Integer.rotateLeft(this.uid.hashCode(), 16);
        hc ^= this.gid.hashCode();
        return hc;
    }
    
    static byte[] trimLeadingZeroesForceMinLength(final byte[] array) {
        if (array == null) {
            return array;
        }
        int pos = 0;
        for (final byte b : array) {
            if (b != 0) {
                break;
            }
            ++pos;
        }
        final int MIN_LENGTH = 1;
        final byte[] trimmedArray = new byte[Math.max(1, array.length - pos)];
        final int startPos = trimmedArray.length - (array.length - pos);
        System.arraycopy(array, pos, trimmedArray, startPos, trimmedArray.length - startPos);
        return trimmedArray;
    }
    
    static {
        HEADER_ID = new ZipShort(30837);
        ONE_THOUSAND = BigInteger.valueOf(1000L);
    }
}
