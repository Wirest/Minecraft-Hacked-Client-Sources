// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

public class Zip64ExtendedInformationExtraField implements ZipExtraField
{
    static final ZipShort HEADER_ID;
    private static final String LFH_MUST_HAVE_BOTH_SIZES_MSG = "Zip64 extended information must contain both size values in the local file header.";
    private static final byte[] EMPTY;
    private ZipEightByteInteger size;
    private ZipEightByteInteger compressedSize;
    private ZipEightByteInteger relativeHeaderOffset;
    private ZipLong diskStart;
    private byte[] rawCentralDirectoryData;
    
    public Zip64ExtendedInformationExtraField() {
    }
    
    public Zip64ExtendedInformationExtraField(final ZipEightByteInteger size, final ZipEightByteInteger compressedSize) {
        this(size, compressedSize, null, null);
    }
    
    public Zip64ExtendedInformationExtraField(final ZipEightByteInteger size, final ZipEightByteInteger compressedSize, final ZipEightByteInteger relativeHeaderOffset, final ZipLong diskStart) {
        this.size = size;
        this.compressedSize = compressedSize;
        this.relativeHeaderOffset = relativeHeaderOffset;
        this.diskStart = diskStart;
    }
    
    public ZipShort getHeaderId() {
        return Zip64ExtendedInformationExtraField.HEADER_ID;
    }
    
    public ZipShort getLocalFileDataLength() {
        return new ZipShort((this.size != null) ? 16 : 0);
    }
    
    public ZipShort getCentralDirectoryLength() {
        return new ZipShort(((this.size != null) ? 8 : 0) + ((this.compressedSize != null) ? 8 : 0) + ((this.relativeHeaderOffset != null) ? 8 : 0) + ((this.diskStart != null) ? 4 : 0));
    }
    
    public byte[] getLocalFileDataData() {
        if (this.size == null && this.compressedSize == null) {
            return Zip64ExtendedInformationExtraField.EMPTY;
        }
        if (this.size == null || this.compressedSize == null) {
            throw new IllegalArgumentException("Zip64 extended information must contain both size values in the local file header.");
        }
        final byte[] data = new byte[16];
        this.addSizes(data);
        return data;
    }
    
    public byte[] getCentralDirectoryData() {
        final byte[] data = new byte[this.getCentralDirectoryLength().getValue()];
        int off = this.addSizes(data);
        if (this.relativeHeaderOffset != null) {
            System.arraycopy(this.relativeHeaderOffset.getBytes(), 0, data, off, 8);
            off += 8;
        }
        if (this.diskStart != null) {
            System.arraycopy(this.diskStart.getBytes(), 0, data, off, 4);
            off += 4;
        }
        return data;
    }
    
    public void parseFromLocalFileData(final byte[] buffer, int offset, final int length) throws ZipException {
        if (length == 0) {
            return;
        }
        if (length < 16) {
            throw new ZipException("Zip64 extended information must contain both size values in the local file header.");
        }
        this.size = new ZipEightByteInteger(buffer, offset);
        offset += 8;
        this.compressedSize = new ZipEightByteInteger(buffer, offset);
        offset += 8;
        int remaining = length - 16;
        if (remaining >= 8) {
            this.relativeHeaderOffset = new ZipEightByteInteger(buffer, offset);
            offset += 8;
            remaining -= 8;
        }
        if (remaining >= 4) {
            this.diskStart = new ZipLong(buffer, offset);
            offset += 4;
            remaining -= 4;
        }
    }
    
    public void parseFromCentralDirectoryData(final byte[] buffer, int offset, final int length) throws ZipException {
        System.arraycopy(buffer, offset, this.rawCentralDirectoryData = new byte[length], 0, length);
        if (length >= 28) {
            this.parseFromLocalFileData(buffer, offset, length);
        }
        else if (length == 24) {
            this.size = new ZipEightByteInteger(buffer, offset);
            offset += 8;
            this.compressedSize = new ZipEightByteInteger(buffer, offset);
            offset += 8;
            this.relativeHeaderOffset = new ZipEightByteInteger(buffer, offset);
        }
        else if (length % 8 == 4) {
            this.diskStart = new ZipLong(buffer, offset + length - 4);
        }
    }
    
    public void reparseCentralDirectoryData(final boolean hasUncompressedSize, final boolean hasCompressedSize, final boolean hasRelativeHeaderOffset, final boolean hasDiskStart) throws ZipException {
        if (this.rawCentralDirectoryData != null) {
            final int expectedLength = (hasUncompressedSize ? 8 : 0) + (hasCompressedSize ? 8 : 0) + (hasRelativeHeaderOffset ? 8 : 0) + (hasDiskStart ? 4 : 0);
            if (this.rawCentralDirectoryData.length < expectedLength) {
                throw new ZipException("central directory zip64 extended information extra field's length doesn't match central directory data.  Expected length " + expectedLength + " but is " + this.rawCentralDirectoryData.length);
            }
            int offset = 0;
            if (hasUncompressedSize) {
                this.size = new ZipEightByteInteger(this.rawCentralDirectoryData, offset);
                offset += 8;
            }
            if (hasCompressedSize) {
                this.compressedSize = new ZipEightByteInteger(this.rawCentralDirectoryData, offset);
                offset += 8;
            }
            if (hasRelativeHeaderOffset) {
                this.relativeHeaderOffset = new ZipEightByteInteger(this.rawCentralDirectoryData, offset);
                offset += 8;
            }
            if (hasDiskStart) {
                this.diskStart = new ZipLong(this.rawCentralDirectoryData, offset);
                offset += 4;
            }
        }
    }
    
    public ZipEightByteInteger getSize() {
        return this.size;
    }
    
    public void setSize(final ZipEightByteInteger size) {
        this.size = size;
    }
    
    public ZipEightByteInteger getCompressedSize() {
        return this.compressedSize;
    }
    
    public void setCompressedSize(final ZipEightByteInteger compressedSize) {
        this.compressedSize = compressedSize;
    }
    
    public ZipEightByteInteger getRelativeHeaderOffset() {
        return this.relativeHeaderOffset;
    }
    
    public void setRelativeHeaderOffset(final ZipEightByteInteger rho) {
        this.relativeHeaderOffset = rho;
    }
    
    public ZipLong getDiskStartNumber() {
        return this.diskStart;
    }
    
    public void setDiskStartNumber(final ZipLong ds) {
        this.diskStart = ds;
    }
    
    private int addSizes(final byte[] data) {
        int off = 0;
        if (this.size != null) {
            System.arraycopy(this.size.getBytes(), 0, data, 0, 8);
            off += 8;
        }
        if (this.compressedSize != null) {
            System.arraycopy(this.compressedSize.getBytes(), 0, data, off, 8);
            off += 8;
        }
        return off;
    }
    
    static {
        HEADER_ID = new ZipShort(1);
        EMPTY = new byte[0];
    }
}
