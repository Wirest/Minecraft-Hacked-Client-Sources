// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.util.zip.CRC32;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.IOException;
import java.io.EOFException;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.zip.Inflater;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;

public class ZipArchiveInputStream extends ArchiveInputStream
{
    private final ZipEncoding zipEncoding;
    private final boolean useUnicodeExtraFields;
    private final InputStream in;
    private final Inflater inf;
    private final ByteBuffer buf;
    private CurrentEntry current;
    private boolean closed;
    private boolean hitCentralDirectory;
    private ByteArrayInputStream lastStoredEntry;
    private boolean allowStoredEntriesWithDataDescriptor;
    private static final int LFH_LEN = 30;
    private static final int CFH_LEN = 46;
    private static final long TWO_EXP_32 = 4294967296L;
    private final byte[] LFH_BUF;
    private final byte[] SKIP_BUF;
    private final byte[] SHORT_BUF;
    private final byte[] WORD_BUF;
    private final byte[] TWO_DWORD_BUF;
    private int entriesRead;
    private static final byte[] LFH;
    private static final byte[] CFH;
    private static final byte[] DD;
    
    public ZipArchiveInputStream(final InputStream inputStream) {
        this(inputStream, "UTF8");
    }
    
    public ZipArchiveInputStream(final InputStream inputStream, final String encoding) {
        this(inputStream, encoding, true);
    }
    
    public ZipArchiveInputStream(final InputStream inputStream, final String encoding, final boolean useUnicodeExtraFields) {
        this(inputStream, encoding, useUnicodeExtraFields, false);
    }
    
    public ZipArchiveInputStream(final InputStream inputStream, final String encoding, final boolean useUnicodeExtraFields, final boolean allowStoredEntriesWithDataDescriptor) {
        this.inf = new Inflater(true);
        this.buf = ByteBuffer.allocate(512);
        this.current = null;
        this.closed = false;
        this.hitCentralDirectory = false;
        this.lastStoredEntry = null;
        this.allowStoredEntriesWithDataDescriptor = false;
        this.LFH_BUF = new byte[30];
        this.SKIP_BUF = new byte[1024];
        this.SHORT_BUF = new byte[2];
        this.WORD_BUF = new byte[4];
        this.TWO_DWORD_BUF = new byte[16];
        this.entriesRead = 0;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.useUnicodeExtraFields = useUnicodeExtraFields;
        this.in = new PushbackInputStream(inputStream, this.buf.capacity());
        this.allowStoredEntriesWithDataDescriptor = allowStoredEntriesWithDataDescriptor;
        this.buf.limit(0);
    }
    
    public ZipArchiveEntry getNextZipEntry() throws IOException {
        boolean firstEntry = true;
        if (this.closed || this.hitCentralDirectory) {
            return null;
        }
        if (this.current != null) {
            this.closeEntry();
            firstEntry = false;
        }
        try {
            if (firstEntry) {
                this.readFirstLocalFileHeader(this.LFH_BUF);
            }
            else {
                this.readFully(this.LFH_BUF);
            }
        }
        catch (EOFException e) {
            return null;
        }
        final ZipLong sig = new ZipLong(this.LFH_BUF);
        if (sig.equals(ZipLong.CFH_SIG) || sig.equals(ZipLong.AED_SIG)) {
            this.hitCentralDirectory = true;
            this.skipRemainderOfArchive();
        }
        if (!sig.equals(ZipLong.LFH_SIG)) {
            return null;
        }
        int off = 4;
        this.current = new CurrentEntry();
        final int versionMadeBy = ZipShort.getValue(this.LFH_BUF, off);
        off += 2;
        this.current.entry.setPlatform(versionMadeBy >> 8 & 0xF);
        final GeneralPurposeBit gpFlag = GeneralPurposeBit.parse(this.LFH_BUF, off);
        final boolean hasUTF8Flag = gpFlag.usesUTF8ForNames();
        final ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
        this.current.hasDataDescriptor = gpFlag.usesDataDescriptor();
        this.current.entry.setGeneralPurposeBit(gpFlag);
        off += 2;
        this.current.entry.setMethod(ZipShort.getValue(this.LFH_BUF, off));
        off += 2;
        final long time = ZipUtil.dosToJavaTime(ZipLong.getValue(this.LFH_BUF, off));
        this.current.entry.setTime(time);
        off += 4;
        ZipLong size = null;
        ZipLong cSize = null;
        if (!this.current.hasDataDescriptor) {
            this.current.entry.setCrc(ZipLong.getValue(this.LFH_BUF, off));
            off += 4;
            cSize = new ZipLong(this.LFH_BUF, off);
            off += 4;
            size = new ZipLong(this.LFH_BUF, off);
            off += 4;
        }
        else {
            off += 12;
        }
        final int fileNameLen = ZipShort.getValue(this.LFH_BUF, off);
        off += 2;
        final int extraLen = ZipShort.getValue(this.LFH_BUF, off);
        off += 2;
        final byte[] fileName = new byte[fileNameLen];
        this.readFully(fileName);
        this.current.entry.setName(entryEncoding.decode(fileName), fileName);
        final byte[] extraData = new byte[extraLen];
        this.readFully(extraData);
        this.current.entry.setExtra(extraData);
        if (!hasUTF8Flag && this.useUnicodeExtraFields) {
            ZipUtil.setNameAndCommentFromExtraFields(this.current.entry, fileName, null);
        }
        this.processZip64Extra(size, cSize);
        if (this.current.entry.getCompressedSize() != -1L) {
            if (this.current.entry.getMethod() == ZipMethod.UNSHRINKING.getCode()) {
                this.current.in = new UnshrinkingInputStream(new BoundedInputStream(this.in, this.current.entry.getCompressedSize()));
            }
            else if (this.current.entry.getMethod() == ZipMethod.IMPLODING.getCode()) {
                this.current.in = new ExplodingInputStream(this.current.entry.getGeneralPurposeBit().getSlidingDictionarySize(), this.current.entry.getGeneralPurposeBit().getNumberOfShannonFanoTrees(), new BoundedInputStream(this.in, this.current.entry.getCompressedSize()));
            }
        }
        ++this.entriesRead;
        return this.current.entry;
    }
    
    private void readFirstLocalFileHeader(final byte[] lfh) throws IOException {
        this.readFully(lfh);
        final ZipLong sig = new ZipLong(lfh);
        if (sig.equals(ZipLong.DD_SIG)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.SPLITTING);
        }
        if (sig.equals(ZipLong.SINGLE_SEGMENT_SPLIT_MARKER)) {
            final byte[] missedLfhBytes = new byte[4];
            this.readFully(missedLfhBytes);
            System.arraycopy(lfh, 4, lfh, 0, 26);
            System.arraycopy(missedLfhBytes, 0, lfh, 26, 4);
        }
    }
    
    private void processZip64Extra(final ZipLong size, final ZipLong cSize) {
        final Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField)this.current.entry.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        this.current.usesZip64 = (z64 != null);
        if (!this.current.hasDataDescriptor) {
            if (z64 != null && (cSize.equals(ZipLong.ZIP64_MAGIC) || size.equals(ZipLong.ZIP64_MAGIC))) {
                this.current.entry.setCompressedSize(z64.getCompressedSize().getLongValue());
                this.current.entry.setSize(z64.getSize().getLongValue());
            }
            else {
                this.current.entry.setCompressedSize(cSize.getValue());
                this.current.entry.setSize(size.getValue());
            }
        }
    }
    
    @Override
    public ArchiveEntry getNextEntry() throws IOException {
        return this.getNextZipEntry();
    }
    
    @Override
    public boolean canReadEntryData(final ArchiveEntry ae) {
        if (ae instanceof ZipArchiveEntry) {
            final ZipArchiveEntry ze = (ZipArchiveEntry)ae;
            return ZipUtil.canHandleEntryData(ze) && this.supportsDataDescriptorFor(ze);
        }
        return false;
    }
    
    @Override
    public int read(final byte[] buffer, final int offset, final int length) throws IOException {
        if (this.closed) {
            throw new IOException("The stream is closed");
        }
        if (this.current == null) {
            return -1;
        }
        if (offset > buffer.length || length < 0 || offset < 0 || buffer.length - offset < length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        ZipUtil.checkRequestedFeatures(this.current.entry);
        if (!this.supportsDataDescriptorFor(this.current.entry)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.DATA_DESCRIPTOR, this.current.entry);
        }
        int read;
        if (this.current.entry.getMethod() == 0) {
            read = this.readStored(buffer, offset, length);
        }
        else if (this.current.entry.getMethod() == 8) {
            read = this.readDeflated(buffer, offset, length);
        }
        else {
            if (this.current.entry.getMethod() != ZipMethod.UNSHRINKING.getCode() && this.current.entry.getMethod() != ZipMethod.IMPLODING.getCode()) {
                throw new UnsupportedZipFeatureException(ZipMethod.getMethodByCode(this.current.entry.getMethod()), this.current.entry);
            }
            read = this.current.in.read(buffer, offset, length);
        }
        if (read >= 0) {
            this.current.crc.update(buffer, offset, read);
        }
        return read;
    }
    
    private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {
        if (this.current.hasDataDescriptor) {
            if (this.lastStoredEntry == null) {
                this.readStoredEntry();
            }
            return this.lastStoredEntry.read(buffer, offset, length);
        }
        final long csize = this.current.entry.getSize();
        if (this.current.bytesRead >= csize) {
            return -1;
        }
        if (this.buf.position() >= this.buf.limit()) {
            this.buf.position(0);
            final int l = this.in.read(this.buf.array());
            if (l == -1) {
                return -1;
            }
            this.buf.limit(l);
            this.count(l);
            this.current.bytesReadFromStream += l;
        }
        int toRead = Math.min(this.buf.remaining(), length);
        if (csize - this.current.bytesRead < toRead) {
            toRead = (int)(csize - this.current.bytesRead);
        }
        this.buf.get(buffer, offset, toRead);
        this.current.bytesRead += toRead;
        return toRead;
    }
    
    private int readDeflated(final byte[] buffer, final int offset, final int length) throws IOException {
        final int read = this.readFromInflater(buffer, offset, length);
        if (read <= 0) {
            if (this.inf.finished()) {
                return -1;
            }
            if (this.inf.needsDictionary()) {
                throw new ZipException("This archive needs a preset dictionary which is not supported by Commons Compress.");
            }
            if (read == -1) {
                throw new IOException("Truncated ZIP file");
            }
        }
        return read;
    }
    
    private int readFromInflater(final byte[] buffer, final int offset, final int length) throws IOException {
        int read = 0;
        do {
            if (this.inf.needsInput()) {
                final int l = this.fill();
                if (l > 0) {
                    this.current.bytesReadFromStream += this.buf.limit();
                }
                else {
                    if (l == -1) {
                        return -1;
                    }
                    break;
                }
            }
            try {
                read = this.inf.inflate(buffer, offset, length);
            }
            catch (DataFormatException e) {
                throw (IOException)new ZipException(e.getMessage()).initCause(e);
            }
        } while (read == 0 && this.inf.needsInput());
        return read;
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.in.close();
            this.inf.end();
        }
    }
    
    @Override
    public long skip(final long value) throws IOException {
        if (value >= 0L) {
            long skipped;
            int x;
            for (skipped = 0L; skipped < value; skipped += x) {
                final long rem = value - skipped;
                x = this.read(this.SKIP_BUF, 0, (int)((this.SKIP_BUF.length > rem) ? rem : this.SKIP_BUF.length));
                if (x == -1) {
                    return skipped;
                }
            }
            return skipped;
        }
        throw new IllegalArgumentException();
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return length >= ZipArchiveOutputStream.LFH_SIG.length && (checksig(signature, ZipArchiveOutputStream.LFH_SIG) || checksig(signature, ZipArchiveOutputStream.EOCD_SIG) || checksig(signature, ZipArchiveOutputStream.DD_SIG) || checksig(signature, ZipLong.SINGLE_SEGMENT_SPLIT_MARKER.getBytes()));
    }
    
    private static boolean checksig(final byte[] signature, final byte[] expected) {
        for (int i = 0; i < expected.length; ++i) {
            if (signature[i] != expected[i]) {
                return false;
            }
        }
        return true;
    }
    
    private void closeEntry() throws IOException {
        if (this.closed) {
            throw new IOException("The stream is closed");
        }
        if (this.current == null) {
            return;
        }
        if (this.current.bytesReadFromStream <= this.current.entry.getCompressedSize() && !this.current.hasDataDescriptor) {
            this.drainCurrentEntryData();
        }
        else {
            this.skip(Long.MAX_VALUE);
            final long inB = (this.current.entry.getMethod() == 8) ? this.getBytesInflated() : this.current.bytesRead;
            final int diff = (int)(this.current.bytesReadFromStream - inB);
            if (diff > 0) {
                this.pushback(this.buf.array(), this.buf.limit() - diff, diff);
            }
        }
        if (this.lastStoredEntry == null && this.current.hasDataDescriptor) {
            this.readDataDescriptor();
        }
        this.inf.reset();
        this.buf.clear().flip();
        this.current = null;
        this.lastStoredEntry = null;
    }
    
    private void drainCurrentEntryData() throws IOException {
        long n;
        for (long remaining = this.current.entry.getCompressedSize() - this.current.bytesReadFromStream; remaining > 0L; remaining -= n) {
            n = this.in.read(this.buf.array(), 0, (int)Math.min(this.buf.capacity(), remaining));
            if (n < 0L) {
                throw new EOFException("Truncated ZIP entry: " + this.current.entry.getName());
            }
            this.count(n);
        }
    }
    
    private long getBytesInflated() {
        long inB = this.inf.getBytesRead();
        if (this.current.bytesReadFromStream >= 4294967296L) {
            while (inB + 4294967296L <= this.current.bytesReadFromStream) {
                inB += 4294967296L;
            }
        }
        return inB;
    }
    
    private int fill() throws IOException {
        if (this.closed) {
            throw new IOException("The stream is closed");
        }
        final int length = this.in.read(this.buf.array());
        if (length > 0) {
            this.buf.limit(length);
            this.count(this.buf.limit());
            this.inf.setInput(this.buf.array(), 0, this.buf.limit());
        }
        return length;
    }
    
    private void readFully(final byte[] b) throws IOException {
        final int count = IOUtils.readFully(this.in, b);
        this.count(count);
        if (count < b.length) {
            throw new EOFException();
        }
    }
    
    private void readDataDescriptor() throws IOException {
        this.readFully(this.WORD_BUF);
        ZipLong val = new ZipLong(this.WORD_BUF);
        if (ZipLong.DD_SIG.equals(val)) {
            this.readFully(this.WORD_BUF);
            val = new ZipLong(this.WORD_BUF);
        }
        this.current.entry.setCrc(val.getValue());
        this.readFully(this.TWO_DWORD_BUF);
        final ZipLong potentialSig = new ZipLong(this.TWO_DWORD_BUF, 8);
        if (potentialSig.equals(ZipLong.CFH_SIG) || potentialSig.equals(ZipLong.LFH_SIG)) {
            this.pushback(this.TWO_DWORD_BUF, 8, 8);
            this.current.entry.setCompressedSize(ZipLong.getValue(this.TWO_DWORD_BUF));
            this.current.entry.setSize(ZipLong.getValue(this.TWO_DWORD_BUF, 4));
        }
        else {
            this.current.entry.setCompressedSize(ZipEightByteInteger.getLongValue(this.TWO_DWORD_BUF));
            this.current.entry.setSize(ZipEightByteInteger.getLongValue(this.TWO_DWORD_BUF, 8));
        }
    }
    
    private boolean supportsDataDescriptorFor(final ZipArchiveEntry entry) {
        return !entry.getGeneralPurposeBit().usesDataDescriptor() || (this.allowStoredEntriesWithDataDescriptor && entry.getMethod() == 0) || entry.getMethod() == 8;
    }
    
    private void readStoredEntry() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int off = 0;
        boolean done = false;
        final int ddLen = this.current.usesZip64 ? 20 : 12;
        while (!done) {
            final int r = this.in.read(this.buf.array(), off, 512 - off);
            if (r <= 0) {
                throw new IOException("Truncated ZIP file");
            }
            if (r + off < 4) {
                off += r;
            }
            else {
                done = this.bufferContainsSignature(bos, off, r, ddLen);
                if (done) {
                    continue;
                }
                off = this.cacheBytesRead(bos, off, r, ddLen);
            }
        }
        final byte[] b = bos.toByteArray();
        this.lastStoredEntry = new ByteArrayInputStream(b);
    }
    
    private boolean bufferContainsSignature(final ByteArrayOutputStream bos, final int offset, final int lastRead, final int expectedDDLen) throws IOException {
        boolean done = false;
        int readTooMuch = 0;
        for (int i = 0; !done && i < lastRead - 4; ++i) {
            if (this.buf.array()[i] == ZipArchiveInputStream.LFH[0] && this.buf.array()[i + 1] == ZipArchiveInputStream.LFH[1]) {
                if ((this.buf.array()[i + 2] == ZipArchiveInputStream.LFH[2] && this.buf.array()[i + 3] == ZipArchiveInputStream.LFH[3]) || (this.buf.array()[i] == ZipArchiveInputStream.CFH[2] && this.buf.array()[i + 3] == ZipArchiveInputStream.CFH[3])) {
                    readTooMuch = offset + lastRead - i - expectedDDLen;
                    done = true;
                }
                else if (this.buf.array()[i + 2] == ZipArchiveInputStream.DD[2] && this.buf.array()[i + 3] == ZipArchiveInputStream.DD[3]) {
                    readTooMuch = offset + lastRead - i;
                    done = true;
                }
                if (done) {
                    this.pushback(this.buf.array(), offset + lastRead - readTooMuch, readTooMuch);
                    bos.write(this.buf.array(), 0, i);
                    this.readDataDescriptor();
                }
            }
        }
        return done;
    }
    
    private int cacheBytesRead(final ByteArrayOutputStream bos, int offset, final int lastRead, final int expecteDDLen) {
        final int cacheable = offset + lastRead - expecteDDLen - 3;
        if (cacheable > 0) {
            bos.write(this.buf.array(), 0, cacheable);
            System.arraycopy(this.buf.array(), cacheable, this.buf.array(), 0, expecteDDLen + 3);
            offset = expecteDDLen + 3;
        }
        else {
            offset += lastRead;
        }
        return offset;
    }
    
    private void pushback(final byte[] buf, final int offset, final int length) throws IOException {
        ((PushbackInputStream)this.in).unread(buf, offset, length);
        this.pushedBackBytes(length);
    }
    
    private void skipRemainderOfArchive() throws IOException {
        this.realSkip(this.entriesRead * 46 - 30);
        this.findEocdRecord();
        this.realSkip(16L);
        this.readFully(this.SHORT_BUF);
        this.realSkip(ZipShort.getValue(this.SHORT_BUF));
    }
    
    private void findEocdRecord() throws IOException {
        int currentByte = -1;
        boolean skipReadCall = false;
        while (skipReadCall || (currentByte = this.readOneByte()) > -1) {
            skipReadCall = false;
            if (!this.isFirstByteOfEocdSig(currentByte)) {
                continue;
            }
            currentByte = this.readOneByte();
            if (currentByte != ZipArchiveOutputStream.EOCD_SIG[1]) {
                if (currentByte == -1) {
                    break;
                }
                skipReadCall = this.isFirstByteOfEocdSig(currentByte);
            }
            else {
                currentByte = this.readOneByte();
                if (currentByte != ZipArchiveOutputStream.EOCD_SIG[2]) {
                    if (currentByte == -1) {
                        break;
                    }
                    skipReadCall = this.isFirstByteOfEocdSig(currentByte);
                }
                else {
                    currentByte = this.readOneByte();
                    if (currentByte == -1) {
                        break;
                    }
                    if (currentByte == ZipArchiveOutputStream.EOCD_SIG[3]) {
                        break;
                    }
                    skipReadCall = this.isFirstByteOfEocdSig(currentByte);
                }
            }
        }
    }
    
    private void realSkip(final long value) throws IOException {
        if (value >= 0L) {
            int x;
            for (long skipped = 0L; skipped < value; skipped += x) {
                final long rem = value - skipped;
                x = this.in.read(this.SKIP_BUF, 0, (int)((this.SKIP_BUF.length > rem) ? rem : this.SKIP_BUF.length));
                if (x == -1) {
                    return;
                }
                this.count(x);
            }
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private int readOneByte() throws IOException {
        final int b = this.in.read();
        if (b != -1) {
            this.count(1);
        }
        return b;
    }
    
    private boolean isFirstByteOfEocdSig(final int b) {
        return b == ZipArchiveOutputStream.EOCD_SIG[0];
    }
    
    static {
        LFH = ZipLong.LFH_SIG.getBytes();
        CFH = ZipLong.CFH_SIG.getBytes();
        DD = ZipLong.DD_SIG.getBytes();
    }
    
    private static final class CurrentEntry
    {
        private final ZipArchiveEntry entry;
        private boolean hasDataDescriptor;
        private boolean usesZip64;
        private long bytesRead;
        private long bytesReadFromStream;
        private final CRC32 crc;
        private InputStream in;
        
        private CurrentEntry() {
            this.entry = new ZipArchiveEntry();
            this.crc = new CRC32();
        }
    }
    
    private class BoundedInputStream extends InputStream
    {
        private final InputStream in;
        private final long max;
        private long pos;
        
        public BoundedInputStream(final InputStream in, final long size) {
            this.pos = 0L;
            this.max = size;
            this.in = in;
        }
        
        @Override
        public int read() throws IOException {
            if (this.max >= 0L && this.pos >= this.max) {
                return -1;
            }
            final int result = this.in.read();
            ++this.pos;
            ArchiveInputStream.this.count(1);
            ZipArchiveInputStream.this.current.bytesReadFromStream++;
            return result;
        }
        
        @Override
        public int read(final byte[] b) throws IOException {
            return this.read(b, 0, b.length);
        }
        
        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            if (this.max >= 0L && this.pos >= this.max) {
                return -1;
            }
            final long maxRead = (this.max >= 0L) ? Math.min(len, this.max - this.pos) : len;
            final int bytesRead = this.in.read(b, off, (int)maxRead);
            if (bytesRead == -1) {
                return -1;
            }
            this.pos += bytesRead;
            ArchiveInputStream.this.count(bytesRead);
            ZipArchiveInputStream.this.current.bytesReadFromStream += bytesRead;
            return bytesRead;
        }
        
        @Override
        public long skip(final long n) throws IOException {
            final long toSkip = (this.max >= 0L) ? Math.min(n, this.max - this.pos) : n;
            final long skippedBytes = this.in.skip(toSkip);
            this.pos += skippedBytes;
            return skippedBytes;
        }
        
        @Override
        public int available() throws IOException {
            if (this.max >= 0L && this.pos >= this.max) {
                return 0;
            }
            return this.in.available();
        }
    }
}
