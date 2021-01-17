// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.nio.ByteBuffer;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.util.zip.ZipException;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.Closeable;
import org.apache.commons.compress.utils.IOUtils;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.zip.Deflater;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.List;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ZipArchiveOutputStream extends ArchiveOutputStream
{
    static final int BUFFER_SIZE = 512;
    protected boolean finished;
    private static final int DEFLATER_BLOCK_SIZE = 8192;
    public static final int DEFLATED = 8;
    public static final int DEFAULT_COMPRESSION = -1;
    public static final int STORED = 0;
    static final String DEFAULT_ENCODING = "UTF8";
    @Deprecated
    public static final int EFS_FLAG = 2048;
    private static final byte[] EMPTY;
    private CurrentEntry entry;
    private String comment;
    private int level;
    private boolean hasCompressionLevelChanged;
    private int method;
    private final List<ZipArchiveEntry> entries;
    private final CRC32 crc;
    private long written;
    private long cdOffset;
    private long cdLength;
    private static final byte[] ZERO;
    private static final byte[] LZERO;
    private final Map<ZipArchiveEntry, Long> offsets;
    private String encoding;
    private ZipEncoding zipEncoding;
    protected final Deflater def;
    private final byte[] buf;
    private final RandomAccessFile raf;
    private final OutputStream out;
    private boolean useUTF8Flag;
    private boolean fallbackToUTF8;
    private UnicodeExtraFieldPolicy createUnicodeExtraFields;
    private boolean hasUsedZip64;
    private Zip64Mode zip64Mode;
    static final byte[] LFH_SIG;
    static final byte[] DD_SIG;
    static final byte[] CFH_SIG;
    static final byte[] EOCD_SIG;
    static final byte[] ZIP64_EOCD_SIG;
    static final byte[] ZIP64_EOCD_LOC_SIG;
    private static final byte[] ONE;
    
    public ZipArchiveOutputStream(final OutputStream out) {
        this.finished = false;
        this.comment = "";
        this.level = -1;
        this.hasCompressionLevelChanged = false;
        this.method = 8;
        this.entries = new LinkedList<ZipArchiveEntry>();
        this.crc = new CRC32();
        this.written = 0L;
        this.cdOffset = 0L;
        this.cdLength = 0L;
        this.offsets = new HashMap<ZipArchiveEntry, Long>();
        this.encoding = "UTF8";
        this.zipEncoding = ZipEncodingHelper.getZipEncoding("UTF8");
        this.def = new Deflater(this.level, true);
        this.buf = new byte[512];
        this.useUTF8Flag = true;
        this.fallbackToUTF8 = false;
        this.createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
        this.hasUsedZip64 = false;
        this.zip64Mode = Zip64Mode.AsNeeded;
        this.out = out;
        this.raf = null;
    }
    
    public ZipArchiveOutputStream(final File file) throws IOException {
        this.finished = false;
        this.comment = "";
        this.level = -1;
        this.hasCompressionLevelChanged = false;
        this.method = 8;
        this.entries = new LinkedList<ZipArchiveEntry>();
        this.crc = new CRC32();
        this.written = 0L;
        this.cdOffset = 0L;
        this.cdLength = 0L;
        this.offsets = new HashMap<ZipArchiveEntry, Long>();
        this.encoding = "UTF8";
        this.zipEncoding = ZipEncodingHelper.getZipEncoding("UTF8");
        this.def = new Deflater(this.level, true);
        this.buf = new byte[512];
        this.useUTF8Flag = true;
        this.fallbackToUTF8 = false;
        this.createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
        this.hasUsedZip64 = false;
        this.zip64Mode = Zip64Mode.AsNeeded;
        OutputStream o = null;
        RandomAccessFile _raf = null;
        try {
            _raf = new RandomAccessFile(file, "rw");
            _raf.setLength(0L);
        }
        catch (IOException e) {
            IOUtils.closeQuietly(_raf);
            _raf = null;
            o = new FileOutputStream(file);
        }
        this.out = o;
        this.raf = _raf;
    }
    
    public boolean isSeekable() {
        return this.raf != null;
    }
    
    public void setEncoding(final String encoding) {
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        if (this.useUTF8Flag && !ZipEncodingHelper.isUTF8(encoding)) {
            this.useUTF8Flag = false;
        }
    }
    
    public String getEncoding() {
        return this.encoding;
    }
    
    public void setUseLanguageEncodingFlag(final boolean b) {
        this.useUTF8Flag = (b && ZipEncodingHelper.isUTF8(this.encoding));
    }
    
    public void setCreateUnicodeExtraFields(final UnicodeExtraFieldPolicy b) {
        this.createUnicodeExtraFields = b;
    }
    
    public void setFallbackToUTF8(final boolean b) {
        this.fallbackToUTF8 = b;
    }
    
    public void setUseZip64(final Zip64Mode mode) {
        this.zip64Mode = mode;
    }
    
    @Override
    public void finish() throws IOException {
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        if (this.entry != null) {
            throw new IOException("This archive contains unclosed entries.");
        }
        this.cdOffset = this.written;
        for (final ZipArchiveEntry ze : this.entries) {
            this.writeCentralFileHeader(ze);
        }
        this.cdLength = this.written - this.cdOffset;
        this.writeZip64CentralDirectory();
        this.writeCentralDirectoryEnd();
        this.offsets.clear();
        this.entries.clear();
        this.def.end();
        this.finished = true;
    }
    
    @Override
    public void closeArchiveEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (this.entry == null) {
            throw new IOException("No current entry to close");
        }
        if (!this.entry.hasWritten) {
            this.write(ZipArchiveOutputStream.EMPTY, 0, 0);
        }
        this.flushDeflater();
        final Zip64Mode effectiveMode = this.getEffectiveZip64Mode(this.entry.entry);
        final long bytesWritten = this.written - this.entry.dataStart;
        final long realCrc = this.crc.getValue();
        this.crc.reset();
        final boolean actuallyNeedsZip64 = this.handleSizesAndCrc(bytesWritten, realCrc, effectiveMode);
        if (this.raf != null) {
            this.rewriteSizesAndCrc(actuallyNeedsZip64);
        }
        this.writeDataDescriptor(this.entry.entry);
        this.entry = null;
    }
    
    private void flushDeflater() throws IOException {
        if (this.entry.entry.getMethod() == 8) {
            this.def.finish();
            while (!this.def.finished()) {
                this.deflate();
            }
        }
    }
    
    private boolean handleSizesAndCrc(final long bytesWritten, final long crc, final Zip64Mode effectiveMode) throws ZipException {
        if (this.entry.entry.getMethod() == 8) {
            this.entry.entry.setSize(this.entry.bytesRead);
            this.entry.entry.setCompressedSize(bytesWritten);
            this.entry.entry.setCrc(crc);
            this.def.reset();
        }
        else if (this.raf == null) {
            if (this.entry.entry.getCrc() != crc) {
                throw new ZipException("bad CRC checksum for entry " + this.entry.entry.getName() + ": " + Long.toHexString(this.entry.entry.getCrc()) + " instead of " + Long.toHexString(crc));
            }
            if (this.entry.entry.getSize() != bytesWritten) {
                throw new ZipException("bad size for entry " + this.entry.entry.getName() + ": " + this.entry.entry.getSize() + " instead of " + bytesWritten);
            }
        }
        else {
            this.entry.entry.setSize(bytesWritten);
            this.entry.entry.setCompressedSize(bytesWritten);
            this.entry.entry.setCrc(crc);
        }
        final boolean actuallyNeedsZip64 = effectiveMode == Zip64Mode.Always || this.entry.entry.getSize() >= 4294967295L || this.entry.entry.getCompressedSize() >= 4294967295L;
        if (actuallyNeedsZip64 && effectiveMode == Zip64Mode.Never) {
            throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
        }
        return actuallyNeedsZip64;
    }
    
    private void rewriteSizesAndCrc(final boolean actuallyNeedsZip64) throws IOException {
        final long save = this.raf.getFilePointer();
        this.raf.seek(this.entry.localDataStart);
        this.writeOut(ZipLong.getBytes(this.entry.entry.getCrc()));
        if (!this.hasZip64Extra(this.entry.entry) || !actuallyNeedsZip64) {
            this.writeOut(ZipLong.getBytes(this.entry.entry.getCompressedSize()));
            this.writeOut(ZipLong.getBytes(this.entry.entry.getSize()));
        }
        else {
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
        }
        if (this.hasZip64Extra(this.entry.entry)) {
            this.raf.seek(this.entry.localDataStart + 12L + 4L + this.getName(this.entry.entry).limit() + 4L);
            this.writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getSize()));
            this.writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getCompressedSize()));
            if (!actuallyNeedsZip64) {
                this.raf.seek(this.entry.localDataStart - 10L);
                this.writeOut(ZipShort.getBytes(10));
                this.entry.entry.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
                this.entry.entry.setExtra();
                if (this.entry.causedUseOfZip64) {
                    this.hasUsedZip64 = false;
                }
            }
        }
        this.raf.seek(save);
    }
    
    @Override
    public void putArchiveEntry(final ArchiveEntry archiveEntry) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (this.entry != null) {
            this.closeArchiveEntry();
        }
        this.entry = new CurrentEntry((ZipArchiveEntry)archiveEntry);
        this.entries.add(this.entry.entry);
        this.setDefaults(this.entry.entry);
        final Zip64Mode effectiveMode = this.getEffectiveZip64Mode(this.entry.entry);
        this.validateSizeInformation(effectiveMode);
        if (this.shouldAddZip64Extra(this.entry.entry, effectiveMode)) {
            final Zip64ExtendedInformationExtraField z64 = this.getZip64Extra(this.entry.entry);
            ZipEightByteInteger size = ZipEightByteInteger.ZERO;
            if (this.entry.entry.getMethod() == 0 && this.entry.entry.getSize() != -1L) {
                size = new ZipEightByteInteger(this.entry.entry.getSize());
            }
            z64.setSize(size);
            z64.setCompressedSize(size);
            this.entry.entry.setExtra();
        }
        if (this.entry.entry.getMethod() == 8 && this.hasCompressionLevelChanged) {
            this.def.setLevel(this.level);
            this.hasCompressionLevelChanged = false;
        }
        this.writeLocalFileHeader(this.entry.entry);
    }
    
    private void setDefaults(final ZipArchiveEntry entry) {
        if (entry.getMethod() == -1) {
            entry.setMethod(this.method);
        }
        if (entry.getTime() == -1L) {
            entry.setTime(System.currentTimeMillis());
        }
    }
    
    private void validateSizeInformation(final Zip64Mode effectiveMode) throws ZipException {
        if (this.entry.entry.getMethod() == 0 && this.raf == null) {
            if (this.entry.entry.getSize() == -1L) {
                throw new ZipException("uncompressed size is required for STORED method when not writing to a file");
            }
            if (this.entry.entry.getCrc() == -1L) {
                throw new ZipException("crc checksum is required for STORED method when not writing to a file");
            }
            this.entry.entry.setCompressedSize(this.entry.entry.getSize());
        }
        if ((this.entry.entry.getSize() >= 4294967295L || this.entry.entry.getCompressedSize() >= 4294967295L) && effectiveMode == Zip64Mode.Never) {
            throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
        }
    }
    
    private boolean shouldAddZip64Extra(final ZipArchiveEntry entry, final Zip64Mode mode) {
        return mode == Zip64Mode.Always || entry.getSize() >= 4294967295L || entry.getCompressedSize() >= 4294967295L || (entry.getSize() == -1L && this.raf != null && mode != Zip64Mode.Never);
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    public void setLevel(final int level) {
        if (level < -1 || level > 9) {
            throw new IllegalArgumentException("Invalid compression level: " + level);
        }
        this.hasCompressionLevelChanged = (this.level != level);
        this.level = level;
    }
    
    public void setMethod(final int method) {
        this.method = method;
    }
    
    @Override
    public boolean canWriteEntryData(final ArchiveEntry ae) {
        if (ae instanceof ZipArchiveEntry) {
            final ZipArchiveEntry zae = (ZipArchiveEntry)ae;
            return zae.getMethod() != ZipMethod.IMPLODING.getCode() && zae.getMethod() != ZipMethod.UNSHRINKING.getCode() && ZipUtil.canHandleEntryData(zae);
        }
        return false;
    }
    
    @Override
    public void write(final byte[] b, final int offset, final int length) throws IOException {
        if (this.entry == null) {
            throw new IllegalStateException("No current entry");
        }
        ZipUtil.checkRequestedFeatures(this.entry.entry);
        this.entry.hasWritten = true;
        if (this.entry.entry.getMethod() == 8) {
            this.writeDeflated(b, offset, length);
        }
        else {
            this.writeOut(b, offset, length);
            this.written += length;
        }
        this.crc.update(b, offset, length);
        this.count(length);
    }
    
    private void writeDeflated(final byte[] b, final int offset, final int length) throws IOException {
        if (length > 0 && !this.def.finished()) {
            this.entry.bytesRead += length;
            if (length <= 8192) {
                this.def.setInput(b, offset, length);
                this.deflateUntilInputIsNeeded();
            }
            else {
                final int fullblocks = length / 8192;
                for (int i = 0; i < fullblocks; ++i) {
                    this.def.setInput(b, offset + i * 8192, 8192);
                    this.deflateUntilInputIsNeeded();
                }
                final int done = fullblocks * 8192;
                if (done < length) {
                    this.def.setInput(b, offset + done, length - done);
                    this.deflateUntilInputIsNeeded();
                }
            }
        }
    }
    
    @Override
    public void close() throws IOException {
        if (!this.finished) {
            this.finish();
        }
        this.destroy();
    }
    
    @Override
    public void flush() throws IOException {
        if (this.out != null) {
            this.out.flush();
        }
    }
    
    protected final void deflate() throws IOException {
        final int len = this.def.deflate(this.buf, 0, this.buf.length);
        if (len > 0) {
            this.writeOut(this.buf, 0, len);
            this.written += len;
        }
    }
    
    protected void writeLocalFileHeader(final ZipArchiveEntry ze) throws IOException {
        final boolean encodable = this.zipEncoding.canEncode(ze.getName());
        final ByteBuffer name = this.getName(ze);
        if (this.createUnicodeExtraFields != UnicodeExtraFieldPolicy.NEVER) {
            this.addUnicodeExtraFields(ze, encodable, name);
        }
        this.offsets.put(ze, this.written);
        this.writeOut(ZipArchiveOutputStream.LFH_SIG);
        this.written += 4L;
        final int zipMethod = ze.getMethod();
        this.writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, !encodable && this.fallbackToUTF8, this.hasZip64Extra(ze));
        this.written += 4L;
        this.writeOut(ZipShort.getBytes(zipMethod));
        this.written += 2L;
        this.writeOut(ZipUtil.toDosTime(ze.getTime()));
        this.written += 4L;
        this.entry.localDataStart = this.written;
        if (zipMethod == 8 || this.raf != null) {
            this.writeOut(ZipArchiveOutputStream.LZERO);
            if (this.hasZip64Extra(this.entry.entry)) {
                this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
                this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            }
            else {
                this.writeOut(ZipArchiveOutputStream.LZERO);
                this.writeOut(ZipArchiveOutputStream.LZERO);
            }
        }
        else {
            this.writeOut(ZipLong.getBytes(ze.getCrc()));
            byte[] size = ZipLong.ZIP64_MAGIC.getBytes();
            if (!this.hasZip64Extra(ze)) {
                size = ZipLong.getBytes(ze.getSize());
            }
            this.writeOut(size);
            this.writeOut(size);
        }
        this.written += 12L;
        this.writeOut(ZipShort.getBytes(name.limit()));
        this.written += 2L;
        final byte[] extra = ze.getLocalFileDataExtra();
        this.writeOut(ZipShort.getBytes(extra.length));
        this.written += 2L;
        this.writeOut(name.array(), name.arrayOffset(), name.limit() - name.position());
        this.written += name.limit();
        this.writeOut(extra);
        this.written += extra.length;
        this.entry.dataStart = this.written;
    }
    
    private void addUnicodeExtraFields(final ZipArchiveEntry ze, final boolean encodable, final ByteBuffer name) throws IOException {
        if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !encodable) {
            ze.addExtraField(new UnicodePathExtraField(ze.getName(), name.array(), name.arrayOffset(), name.limit() - name.position()));
        }
        final String comm = ze.getComment();
        if (comm != null && !"".equals(comm)) {
            final boolean commentEncodable = this.zipEncoding.canEncode(comm);
            if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !commentEncodable) {
                final ByteBuffer commentB = this.getEntryEncoding(ze).encode(comm);
                ze.addExtraField(new UnicodeCommentExtraField(comm, commentB.array(), commentB.arrayOffset(), commentB.limit() - commentB.position()));
            }
        }
    }
    
    protected void writeDataDescriptor(final ZipArchiveEntry ze) throws IOException {
        if (ze.getMethod() != 8 || this.raf != null) {
            return;
        }
        this.writeOut(ZipArchiveOutputStream.DD_SIG);
        this.writeOut(ZipLong.getBytes(ze.getCrc()));
        int sizeFieldSize = 4;
        if (!this.hasZip64Extra(ze)) {
            this.writeOut(ZipLong.getBytes(ze.getCompressedSize()));
            this.writeOut(ZipLong.getBytes(ze.getSize()));
        }
        else {
            sizeFieldSize = 8;
            this.writeOut(ZipEightByteInteger.getBytes(ze.getCompressedSize()));
            this.writeOut(ZipEightByteInteger.getBytes(ze.getSize()));
        }
        this.written += 8 + 2 * sizeFieldSize;
    }
    
    protected void writeCentralFileHeader(final ZipArchiveEntry ze) throws IOException {
        this.writeOut(ZipArchiveOutputStream.CFH_SIG);
        this.written += 4L;
        final long lfhOffset = this.offsets.get(ze);
        final boolean needsZip64Extra = this.hasZip64Extra(ze) || ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L || lfhOffset >= 4294967295L;
        if (needsZip64Extra && this.zip64Mode == Zip64Mode.Never) {
            throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
        }
        this.handleZip64Extra(ze, lfhOffset, needsZip64Extra);
        this.writeOut(ZipShort.getBytes(ze.getPlatform() << 8 | (this.hasUsedZip64 ? 45 : 20)));
        this.written += 2L;
        final int zipMethod = ze.getMethod();
        final boolean encodable = this.zipEncoding.canEncode(ze.getName());
        this.writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, !encodable && this.fallbackToUTF8, needsZip64Extra);
        this.written += 4L;
        this.writeOut(ZipShort.getBytes(zipMethod));
        this.written += 2L;
        this.writeOut(ZipUtil.toDosTime(ze.getTime()));
        this.written += 4L;
        this.writeOut(ZipLong.getBytes(ze.getCrc()));
        if (ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L) {
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
        }
        else {
            this.writeOut(ZipLong.getBytes(ze.getCompressedSize()));
            this.writeOut(ZipLong.getBytes(ze.getSize()));
        }
        this.written += 12L;
        final ByteBuffer name = this.getName(ze);
        this.writeOut(ZipShort.getBytes(name.limit()));
        this.written += 2L;
        final byte[] extra = ze.getCentralDirectoryExtra();
        this.writeOut(ZipShort.getBytes(extra.length));
        this.written += 2L;
        String comm = ze.getComment();
        if (comm == null) {
            comm = "";
        }
        final ByteBuffer commentB = this.getEntryEncoding(ze).encode(comm);
        this.writeOut(ZipShort.getBytes(commentB.limit()));
        this.written += 2L;
        this.writeOut(ZipArchiveOutputStream.ZERO);
        this.written += 2L;
        this.writeOut(ZipShort.getBytes(ze.getInternalAttributes()));
        this.written += 2L;
        this.writeOut(ZipLong.getBytes(ze.getExternalAttributes()));
        this.written += 4L;
        this.writeOut(ZipLong.getBytes(Math.min(lfhOffset, 4294967295L)));
        this.written += 4L;
        this.writeOut(name.array(), name.arrayOffset(), name.limit() - name.position());
        this.written += name.limit();
        this.writeOut(extra);
        this.written += extra.length;
        this.writeOut(commentB.array(), commentB.arrayOffset(), commentB.limit() - commentB.position());
        this.written += commentB.limit();
    }
    
    private void handleZip64Extra(final ZipArchiveEntry ze, final long lfhOffset, final boolean needsZip64Extra) {
        if (needsZip64Extra) {
            final Zip64ExtendedInformationExtraField z64 = this.getZip64Extra(ze);
            if (ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L) {
                z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
                z64.setSize(new ZipEightByteInteger(ze.getSize()));
            }
            else {
                z64.setCompressedSize(null);
                z64.setSize(null);
            }
            if (lfhOffset >= 4294967295L) {
                z64.setRelativeHeaderOffset(new ZipEightByteInteger(lfhOffset));
            }
            ze.setExtra();
        }
    }
    
    protected void writeCentralDirectoryEnd() throws IOException {
        this.writeOut(ZipArchiveOutputStream.EOCD_SIG);
        this.writeOut(ZipArchiveOutputStream.ZERO);
        this.writeOut(ZipArchiveOutputStream.ZERO);
        final int numberOfEntries = this.entries.size();
        if (numberOfEntries > 65535 && this.zip64Mode == Zip64Mode.Never) {
            throw new Zip64RequiredException("archive contains more than 65535 entries.");
        }
        if (this.cdOffset > 4294967295L && this.zip64Mode == Zip64Mode.Never) {
            throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
        }
        final byte[] num = ZipShort.getBytes(Math.min(numberOfEntries, 65535));
        this.writeOut(num);
        this.writeOut(num);
        this.writeOut(ZipLong.getBytes(Math.min(this.cdLength, 4294967295L)));
        this.writeOut(ZipLong.getBytes(Math.min(this.cdOffset, 4294967295L)));
        final ByteBuffer data = this.zipEncoding.encode(this.comment);
        this.writeOut(ZipShort.getBytes(data.limit()));
        this.writeOut(data.array(), data.arrayOffset(), data.limit() - data.position());
    }
    
    protected void writeZip64CentralDirectory() throws IOException {
        if (this.zip64Mode == Zip64Mode.Never) {
            return;
        }
        if (!this.hasUsedZip64 && (this.cdOffset >= 4294967295L || this.cdLength >= 4294967295L || this.entries.size() >= 65535)) {
            this.hasUsedZip64 = true;
        }
        if (!this.hasUsedZip64) {
            return;
        }
        final long offset = this.written;
        this.writeOut(ZipArchiveOutputStream.ZIP64_EOCD_SIG);
        this.writeOut(ZipEightByteInteger.getBytes(44L));
        this.writeOut(ZipShort.getBytes(45));
        this.writeOut(ZipShort.getBytes(45));
        this.writeOut(ZipArchiveOutputStream.LZERO);
        this.writeOut(ZipArchiveOutputStream.LZERO);
        final byte[] num = ZipEightByteInteger.getBytes(this.entries.size());
        this.writeOut(num);
        this.writeOut(num);
        this.writeOut(ZipEightByteInteger.getBytes(this.cdLength));
        this.writeOut(ZipEightByteInteger.getBytes(this.cdOffset));
        this.writeOut(ZipArchiveOutputStream.ZIP64_EOCD_LOC_SIG);
        this.writeOut(ZipArchiveOutputStream.LZERO);
        this.writeOut(ZipEightByteInteger.getBytes(offset));
        this.writeOut(ZipArchiveOutputStream.ONE);
    }
    
    protected final void writeOut(final byte[] data) throws IOException {
        this.writeOut(data, 0, data.length);
    }
    
    protected final void writeOut(final byte[] data, final int offset, final int length) throws IOException {
        if (this.raf != null) {
            this.raf.write(data, offset, length);
        }
        else {
            this.out.write(data, offset, length);
        }
    }
    
    private void deflateUntilInputIsNeeded() throws IOException {
        while (!this.def.needsInput()) {
            this.deflate();
        }
    }
    
    private void writeVersionNeededToExtractAndGeneralPurposeBits(final int zipMethod, final boolean utfFallback, final boolean zip64) throws IOException {
        int versionNeededToExtract = 10;
        final GeneralPurposeBit b = new GeneralPurposeBit();
        b.useUTF8ForNames(this.useUTF8Flag || utfFallback);
        if (zipMethod == 8 && this.raf == null) {
            versionNeededToExtract = 20;
            b.useDataDescriptor(true);
        }
        if (zip64) {
            versionNeededToExtract = 45;
        }
        this.writeOut(ZipShort.getBytes(versionNeededToExtract));
        this.writeOut(b.encode());
    }
    
    @Override
    public ArchiveEntry createArchiveEntry(final File inputFile, final String entryName) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new ZipArchiveEntry(inputFile, entryName);
    }
    
    private Zip64ExtendedInformationExtraField getZip64Extra(final ZipArchiveEntry ze) {
        if (this.entry != null) {
            this.entry.causedUseOfZip64 = !this.hasUsedZip64;
        }
        this.hasUsedZip64 = true;
        Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField)ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        if (z64 == null) {
            z64 = new Zip64ExtendedInformationExtraField();
        }
        ze.addAsFirstExtraField(z64);
        return z64;
    }
    
    private boolean hasZip64Extra(final ZipArchiveEntry ze) {
        return ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID) != null;
    }
    
    private Zip64Mode getEffectiveZip64Mode(final ZipArchiveEntry ze) {
        if (this.zip64Mode != Zip64Mode.AsNeeded || this.raf != null || ze.getMethod() != 8 || ze.getSize() != -1L) {
            return this.zip64Mode;
        }
        return Zip64Mode.Never;
    }
    
    private ZipEncoding getEntryEncoding(final ZipArchiveEntry ze) {
        final boolean encodable = this.zipEncoding.canEncode(ze.getName());
        return (!encodable && this.fallbackToUTF8) ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
    }
    
    private ByteBuffer getName(final ZipArchiveEntry ze) throws IOException {
        return this.getEntryEncoding(ze).encode(ze.getName());
    }
    
    void destroy() throws IOException {
        if (this.raf != null) {
            this.raf.close();
        }
        if (this.out != null) {
            this.out.close();
        }
    }
    
    static {
        EMPTY = new byte[0];
        ZERO = new byte[] { 0, 0 };
        LZERO = new byte[] { 0, 0, 0, 0 };
        LFH_SIG = ZipLong.LFH_SIG.getBytes();
        DD_SIG = ZipLong.DD_SIG.getBytes();
        CFH_SIG = ZipLong.CFH_SIG.getBytes();
        EOCD_SIG = ZipLong.getBytes(101010256L);
        ZIP64_EOCD_SIG = ZipLong.getBytes(101075792L);
        ZIP64_EOCD_LOC_SIG = ZipLong.getBytes(117853008L);
        ONE = ZipLong.getBytes(1L);
    }
    
    public static final class UnicodeExtraFieldPolicy
    {
        public static final UnicodeExtraFieldPolicy ALWAYS;
        public static final UnicodeExtraFieldPolicy NEVER;
        public static final UnicodeExtraFieldPolicy NOT_ENCODEABLE;
        private final String name;
        
        private UnicodeExtraFieldPolicy(final String n) {
            this.name = n;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        static {
            ALWAYS = new UnicodeExtraFieldPolicy("always");
            NEVER = new UnicodeExtraFieldPolicy("never");
            NOT_ENCODEABLE = new UnicodeExtraFieldPolicy("not encodeable");
        }
    }
    
    private static final class CurrentEntry
    {
        private final ZipArchiveEntry entry;
        private long localDataStart;
        private long dataStart;
        private long bytesRead;
        private boolean causedUseOfZip64;
        private boolean hasWritten;
        
        private CurrentEntry(final ZipArchiveEntry entry) {
            this.localDataStart = 0L;
            this.dataStart = 0L;
            this.bytesRead = 0L;
            this.causedUseOfZip64 = false;
            this.entry = entry;
        }
    }
}
