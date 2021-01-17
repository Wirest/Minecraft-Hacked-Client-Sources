// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.arj;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.util.zip.CRC32;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveException;
import java.io.InputStream;
import java.io.DataInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;

public class ArjArchiveInputStream extends ArchiveInputStream
{
    private static final int ARJ_MAGIC_1 = 96;
    private static final int ARJ_MAGIC_2 = 234;
    private final DataInputStream in;
    private final String charsetName;
    private final MainHeader mainHeader;
    private LocalFileHeader currentLocalFileHeader;
    private InputStream currentInputStream;
    
    public ArjArchiveInputStream(final InputStream inputStream, final String charsetName) throws ArchiveException {
        this.currentLocalFileHeader = null;
        this.currentInputStream = null;
        this.in = new DataInputStream(inputStream);
        this.charsetName = charsetName;
        try {
            this.mainHeader = this.readMainHeader();
            if ((this.mainHeader.arjFlags & 0x1) != 0x0) {
                throw new ArchiveException("Encrypted ARJ files are unsupported");
            }
            if ((this.mainHeader.arjFlags & 0x4) != 0x0) {
                throw new ArchiveException("Multi-volume ARJ files are unsupported");
            }
        }
        catch (IOException ioException) {
            throw new ArchiveException(ioException.getMessage(), ioException);
        }
    }
    
    public ArjArchiveInputStream(final InputStream inputStream) throws ArchiveException {
        this(inputStream, "CP437");
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
    
    private int read8(final DataInputStream dataIn) throws IOException {
        final int value = dataIn.readUnsignedByte();
        this.count(1);
        return value;
    }
    
    private int read16(final DataInputStream dataIn) throws IOException {
        final int value = dataIn.readUnsignedShort();
        this.count(2);
        return Integer.reverseBytes(value) >>> 16;
    }
    
    private int read32(final DataInputStream dataIn) throws IOException {
        final int value = dataIn.readInt();
        this.count(4);
        return Integer.reverseBytes(value);
    }
    
    private String readString(final DataInputStream dataIn) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = dataIn.readUnsignedByte()) != 0) {
            buffer.write(nextByte);
        }
        if (this.charsetName != null) {
            return new String(buffer.toByteArray(), this.charsetName);
        }
        return new String(buffer.toByteArray());
    }
    
    private void readFully(final DataInputStream dataIn, final byte[] b) throws IOException {
        dataIn.readFully(b);
        this.count(b.length);
    }
    
    private byte[] readHeader() throws IOException {
        boolean found = false;
        byte[] basicHeaderBytes = null;
        do {
            int first = 0;
            int second = this.read8(this.in);
            do {
                first = second;
                second = this.read8(this.in);
            } while (first != 96 && second != 234);
            final int basicHeaderSize = this.read16(this.in);
            if (basicHeaderSize == 0) {
                return null;
            }
            if (basicHeaderSize > 2600) {
                continue;
            }
            basicHeaderBytes = new byte[basicHeaderSize];
            this.readFully(this.in, basicHeaderBytes);
            final long basicHeaderCrc32 = (long)this.read32(this.in) & 0xFFFFFFFFL;
            final CRC32 crc32 = new CRC32();
            crc32.update(basicHeaderBytes);
            if (basicHeaderCrc32 != crc32.getValue()) {
                continue;
            }
            found = true;
        } while (!found);
        return basicHeaderBytes;
    }
    
    private MainHeader readMainHeader() throws IOException {
        final byte[] basicHeaderBytes = this.readHeader();
        if (basicHeaderBytes == null) {
            throw new IOException("Archive ends without any headers");
        }
        final DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes));
        final int firstHeaderSize = basicHeader.readUnsignedByte();
        final byte[] firstHeaderBytes = new byte[firstHeaderSize - 1];
        basicHeader.readFully(firstHeaderBytes);
        final DataInputStream firstHeader = new DataInputStream(new ByteArrayInputStream(firstHeaderBytes));
        final MainHeader hdr = new MainHeader();
        hdr.archiverVersionNumber = firstHeader.readUnsignedByte();
        hdr.minVersionToExtract = firstHeader.readUnsignedByte();
        hdr.hostOS = firstHeader.readUnsignedByte();
        hdr.arjFlags = firstHeader.readUnsignedByte();
        hdr.securityVersion = firstHeader.readUnsignedByte();
        hdr.fileType = firstHeader.readUnsignedByte();
        hdr.reserved = firstHeader.readUnsignedByte();
        hdr.dateTimeCreated = this.read32(firstHeader);
        hdr.dateTimeModified = this.read32(firstHeader);
        hdr.archiveSize = (0xFFFFFFFFL & (long)this.read32(firstHeader));
        hdr.securityEnvelopeFilePosition = this.read32(firstHeader);
        hdr.fileSpecPosition = this.read16(firstHeader);
        hdr.securityEnvelopeLength = this.read16(firstHeader);
        this.pushedBackBytes(20L);
        hdr.encryptionVersion = firstHeader.readUnsignedByte();
        hdr.lastChapter = firstHeader.readUnsignedByte();
        if (firstHeaderSize >= 33) {
            hdr.arjProtectionFactor = firstHeader.readUnsignedByte();
            hdr.arjFlags2 = firstHeader.readUnsignedByte();
            firstHeader.readUnsignedByte();
            firstHeader.readUnsignedByte();
        }
        hdr.name = this.readString(basicHeader);
        hdr.comment = this.readString(basicHeader);
        final int extendedHeaderSize = this.read16(this.in);
        if (extendedHeaderSize > 0) {
            hdr.extendedHeaderBytes = new byte[extendedHeaderSize];
            this.readFully(this.in, hdr.extendedHeaderBytes);
            final long extendedHeaderCrc32 = 0xFFFFFFFFL & (long)this.read32(this.in);
            final CRC32 crc32 = new CRC32();
            crc32.update(hdr.extendedHeaderBytes);
            if (extendedHeaderCrc32 != crc32.getValue()) {
                throw new IOException("Extended header CRC32 verification failure");
            }
        }
        return hdr;
    }
    
    private LocalFileHeader readLocalFileHeader() throws IOException {
        final byte[] basicHeaderBytes = this.readHeader();
        if (basicHeaderBytes == null) {
            return null;
        }
        final DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes));
        final int firstHeaderSize = basicHeader.readUnsignedByte();
        final byte[] firstHeaderBytes = new byte[firstHeaderSize - 1];
        basicHeader.readFully(firstHeaderBytes);
        final DataInputStream firstHeader = new DataInputStream(new ByteArrayInputStream(firstHeaderBytes));
        final LocalFileHeader localFileHeader = new LocalFileHeader();
        localFileHeader.archiverVersionNumber = firstHeader.readUnsignedByte();
        localFileHeader.minVersionToExtract = firstHeader.readUnsignedByte();
        localFileHeader.hostOS = firstHeader.readUnsignedByte();
        localFileHeader.arjFlags = firstHeader.readUnsignedByte();
        localFileHeader.method = firstHeader.readUnsignedByte();
        localFileHeader.fileType = firstHeader.readUnsignedByte();
        localFileHeader.reserved = firstHeader.readUnsignedByte();
        localFileHeader.dateTimeModified = this.read32(firstHeader);
        localFileHeader.compressedSize = (0xFFFFFFFFL & (long)this.read32(firstHeader));
        localFileHeader.originalSize = (0xFFFFFFFFL & (long)this.read32(firstHeader));
        localFileHeader.originalCrc32 = (0xFFFFFFFFL & (long)this.read32(firstHeader));
        localFileHeader.fileSpecPosition = this.read16(firstHeader);
        localFileHeader.fileAccessMode = this.read16(firstHeader);
        this.pushedBackBytes(20L);
        localFileHeader.firstChapter = firstHeader.readUnsignedByte();
        localFileHeader.lastChapter = firstHeader.readUnsignedByte();
        this.readExtraData(firstHeaderSize, firstHeader, localFileHeader);
        localFileHeader.name = this.readString(basicHeader);
        localFileHeader.comment = this.readString(basicHeader);
        final ArrayList<byte[]> extendedHeaders = new ArrayList<byte[]>();
        int extendedHeaderSize;
        while ((extendedHeaderSize = this.read16(this.in)) > 0) {
            final byte[] extendedHeaderBytes = new byte[extendedHeaderSize];
            this.readFully(this.in, extendedHeaderBytes);
            final long extendedHeaderCrc32 = 0xFFFFFFFFL & (long)this.read32(this.in);
            final CRC32 crc32 = new CRC32();
            crc32.update(extendedHeaderBytes);
            if (extendedHeaderCrc32 != crc32.getValue()) {
                throw new IOException("Extended header CRC32 verification failure");
            }
            extendedHeaders.add(extendedHeaderBytes);
        }
        localFileHeader.extendedHeaders = extendedHeaders.toArray(new byte[extendedHeaders.size()][]);
        return localFileHeader;
    }
    
    private void readExtraData(final int firstHeaderSize, final DataInputStream firstHeader, final LocalFileHeader localFileHeader) throws IOException {
        if (firstHeaderSize >= 33) {
            localFileHeader.extendedFilePosition = this.read32(firstHeader);
            if (firstHeaderSize >= 45) {
                localFileHeader.dateTimeAccessed = this.read32(firstHeader);
                localFileHeader.dateTimeCreated = this.read32(firstHeader);
                localFileHeader.originalSizeEvenForVolumes = this.read32(firstHeader);
                this.pushedBackBytes(12L);
            }
            this.pushedBackBytes(4L);
        }
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return length >= 2 && (0xFF & signature[0]) == 0x60 && (0xFF & signature[1]) == 0xEA;
    }
    
    public String getArchiveName() {
        return this.mainHeader.name;
    }
    
    public String getArchiveComment() {
        return this.mainHeader.comment;
    }
    
    @Override
    public ArjArchiveEntry getNextEntry() throws IOException {
        if (this.currentInputStream != null) {
            IOUtils.skip(this.currentInputStream, Long.MAX_VALUE);
            this.currentInputStream.close();
            this.currentLocalFileHeader = null;
            this.currentInputStream = null;
        }
        this.currentLocalFileHeader = this.readLocalFileHeader();
        if (this.currentLocalFileHeader != null) {
            this.currentInputStream = new BoundedInputStream(this.in, this.currentLocalFileHeader.compressedSize);
            if (this.currentLocalFileHeader.method == 0) {
                this.currentInputStream = new CRC32VerifyingInputStream(this.currentInputStream, this.currentLocalFileHeader.originalSize, this.currentLocalFileHeader.originalCrc32);
            }
            return new ArjArchiveEntry(this.currentLocalFileHeader);
        }
        this.currentInputStream = null;
        return null;
    }
    
    @Override
    public boolean canReadEntryData(final ArchiveEntry ae) {
        return ae instanceof ArjArchiveEntry && ((ArjArchiveEntry)ae).getMethod() == 0;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.currentLocalFileHeader == null) {
            throw new IllegalStateException("No current arj entry");
        }
        if (this.currentLocalFileHeader.method != 0) {
            throw new IOException("Unsupported compression method " + this.currentLocalFileHeader.method);
        }
        return this.currentInputStream.read(b, off, len);
    }
}
