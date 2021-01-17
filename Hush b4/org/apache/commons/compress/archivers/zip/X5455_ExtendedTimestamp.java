// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.util.Date;
import java.util.zip.ZipException;
import java.io.Serializable;

public class X5455_ExtendedTimestamp implements ZipExtraField, Cloneable, Serializable
{
    private static final ZipShort HEADER_ID;
    private static final long serialVersionUID = 1L;
    public static final byte MODIFY_TIME_BIT = 1;
    public static final byte ACCESS_TIME_BIT = 2;
    public static final byte CREATE_TIME_BIT = 4;
    private byte flags;
    private boolean bit0_modifyTimePresent;
    private boolean bit1_accessTimePresent;
    private boolean bit2_createTimePresent;
    private ZipLong modifyTime;
    private ZipLong accessTime;
    private ZipLong createTime;
    
    public ZipShort getHeaderId() {
        return X5455_ExtendedTimestamp.HEADER_ID;
    }
    
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(1 + (this.bit0_modifyTimePresent ? 4 : 0) + ((this.bit1_accessTimePresent && this.accessTime != null) ? 4 : 0) + ((this.bit2_createTimePresent && this.createTime != null) ? 4 : 0));
    }
    
    public ZipShort getCentralDirectoryLength() {
        return new ZipShort(1 + (this.bit0_modifyTimePresent ? 4 : 0));
    }
    
    public byte[] getLocalFileDataData() {
        final byte[] data = new byte[this.getLocalFileDataLength().getValue()];
        int pos = 0;
        data[pos++] = 0;
        if (this.bit0_modifyTimePresent) {
            final byte[] array = data;
            final int n = 0;
            array[n] |= 0x1;
            System.arraycopy(this.modifyTime.getBytes(), 0, data, pos, 4);
            pos += 4;
        }
        if (this.bit1_accessTimePresent && this.accessTime != null) {
            final byte[] array2 = data;
            final int n2 = 0;
            array2[n2] |= 0x2;
            System.arraycopy(this.accessTime.getBytes(), 0, data, pos, 4);
            pos += 4;
        }
        if (this.bit2_createTimePresent && this.createTime != null) {
            final byte[] array3 = data;
            final int n3 = 0;
            array3[n3] |= 0x4;
            System.arraycopy(this.createTime.getBytes(), 0, data, pos, 4);
            pos += 4;
        }
        return data;
    }
    
    public byte[] getCentralDirectoryData() {
        final byte[] centralData = new byte[this.getCentralDirectoryLength().getValue()];
        final byte[] localData = this.getLocalFileDataData();
        System.arraycopy(localData, 0, centralData, 0, centralData.length);
        return centralData;
    }
    
    public void parseFromLocalFileData(final byte[] data, int offset, final int length) throws ZipException {
        this.reset();
        final int len = offset + length;
        this.setFlags(data[offset++]);
        if (this.bit0_modifyTimePresent) {
            this.modifyTime = new ZipLong(data, offset);
            offset += 4;
        }
        if (this.bit1_accessTimePresent && offset + 4 <= len) {
            this.accessTime = new ZipLong(data, offset);
            offset += 4;
        }
        if (this.bit2_createTimePresent && offset + 4 <= len) {
            this.createTime = new ZipLong(data, offset);
            offset += 4;
        }
    }
    
    public void parseFromCentralDirectoryData(final byte[] buffer, final int offset, final int length) throws ZipException {
        this.reset();
        this.parseFromLocalFileData(buffer, offset, length);
    }
    
    private void reset() {
        this.setFlags((byte)0);
        this.modifyTime = null;
        this.accessTime = null;
        this.createTime = null;
    }
    
    public void setFlags(final byte flags) {
        this.flags = flags;
        this.bit0_modifyTimePresent = ((flags & 0x1) == 0x1);
        this.bit1_accessTimePresent = ((flags & 0x2) == 0x2);
        this.bit2_createTimePresent = ((flags & 0x4) == 0x4);
    }
    
    public byte getFlags() {
        return this.flags;
    }
    
    public boolean isBit0_modifyTimePresent() {
        return this.bit0_modifyTimePresent;
    }
    
    public boolean isBit1_accessTimePresent() {
        return this.bit1_accessTimePresent;
    }
    
    public boolean isBit2_createTimePresent() {
        return this.bit2_createTimePresent;
    }
    
    public ZipLong getModifyTime() {
        return this.modifyTime;
    }
    
    public ZipLong getAccessTime() {
        return this.accessTime;
    }
    
    public ZipLong getCreateTime() {
        return this.createTime;
    }
    
    public Date getModifyJavaTime() {
        return (this.modifyTime != null) ? new Date(this.modifyTime.getValue() * 1000L) : null;
    }
    
    public Date getAccessJavaTime() {
        return (this.accessTime != null) ? new Date(this.accessTime.getValue() * 1000L) : null;
    }
    
    public Date getCreateJavaTime() {
        return (this.createTime != null) ? new Date(this.createTime.getValue() * 1000L) : null;
    }
    
    public void setModifyTime(final ZipLong l) {
        this.bit0_modifyTimePresent = (l != null);
        this.flags = (byte)((l != null) ? (this.flags | 0x1) : (this.flags & 0xFFFFFFFE));
        this.modifyTime = l;
    }
    
    public void setAccessTime(final ZipLong l) {
        this.bit1_accessTimePresent = (l != null);
        this.flags = (byte)((l != null) ? (this.flags | 0x2) : (this.flags & 0xFFFFFFFD));
        this.accessTime = l;
    }
    
    public void setCreateTime(final ZipLong l) {
        this.bit2_createTimePresent = (l != null);
        this.flags = (byte)((l != null) ? (this.flags | 0x4) : (this.flags & 0xFFFFFFFB));
        this.createTime = l;
    }
    
    public void setModifyJavaTime(final Date d) {
        this.setModifyTime(dateToZipLong(d));
    }
    
    public void setAccessJavaTime(final Date d) {
        this.setAccessTime(dateToZipLong(d));
    }
    
    public void setCreateJavaTime(final Date d) {
        this.setCreateTime(dateToZipLong(d));
    }
    
    private static ZipLong dateToZipLong(final Date d) {
        if (d == null) {
            return null;
        }
        final long TWO_TO_32 = 4294967296L;
        final long l = d.getTime() / 1000L;
        if (l >= 4294967296L) {
            throw new IllegalArgumentException("Cannot set an X5455 timestamp larger than 2^32: " + l);
        }
        return new ZipLong(l);
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("0x5455 Zip Extra Field: Flags=");
        buf.append(Integer.toBinaryString(ZipUtil.unsignedIntToSignedByte(this.flags))).append(" ");
        if (this.bit0_modifyTimePresent && this.modifyTime != null) {
            final Date m = this.getModifyJavaTime();
            buf.append(" Modify:[").append(m).append("] ");
        }
        if (this.bit1_accessTimePresent && this.accessTime != null) {
            final Date a = this.getAccessJavaTime();
            buf.append(" Access:[").append(a).append("] ");
        }
        if (this.bit2_createTimePresent && this.createTime != null) {
            final Date c = this.getCreateJavaTime();
            buf.append(" Create:[").append(c).append("] ");
        }
        return buf.toString();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof X5455_ExtendedTimestamp) {
            final X5455_ExtendedTimestamp xf = (X5455_ExtendedTimestamp)o;
            return (this.flags & 0x7) == (xf.flags & 0x7) && (this.modifyTime == xf.modifyTime || (this.modifyTime != null && this.modifyTime.equals(xf.modifyTime))) && (this.accessTime == xf.accessTime || (this.accessTime != null && this.accessTime.equals(xf.accessTime))) && (this.createTime == xf.createTime || (this.createTime != null && this.createTime.equals(xf.createTime)));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hc = -123 * (this.flags & 0x7);
        if (this.modifyTime != null) {
            hc ^= this.modifyTime.hashCode();
        }
        if (this.accessTime != null) {
            hc ^= Integer.rotateLeft(this.accessTime.hashCode(), 11);
        }
        if (this.createTime != null) {
            hc ^= Integer.rotateLeft(this.createTime.hashCode(), 22);
        }
        return hc;
    }
    
    static {
        HEADER_ID = new ZipShort(21589);
    }
}
