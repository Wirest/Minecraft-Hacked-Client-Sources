// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.gzip;

import java.util.zip.DataFormatException;
import java.io.EOFException;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class GzipCompressorInputStream extends CompressorInputStream
{
    private static final int FHCRC = 2;
    private static final int FEXTRA = 4;
    private static final int FNAME = 8;
    private static final int FCOMMENT = 16;
    private static final int FRESERVED = 224;
    private final InputStream in;
    private final boolean decompressConcatenated;
    private final byte[] buf;
    private int bufUsed;
    private Inflater inf;
    private final CRC32 crc;
    private int memberSize;
    private boolean endReached;
    private final byte[] oneByte;
    private final GzipParameters parameters;
    
    public GzipCompressorInputStream(final InputStream inputStream) throws IOException {
        this(inputStream, false);
    }
    
    public GzipCompressorInputStream(final InputStream inputStream, final boolean decompressConcatenated) throws IOException {
        this.buf = new byte[8192];
        this.bufUsed = 0;
        this.inf = new Inflater(true);
        this.crc = new CRC32();
        this.endReached = false;
        this.oneByte = new byte[1];
        this.parameters = new GzipParameters();
        if (inputStream.markSupported()) {
            this.in = inputStream;
        }
        else {
            this.in = new BufferedInputStream(inputStream);
        }
        this.decompressConcatenated = decompressConcatenated;
        this.init(true);
    }
    
    public GzipParameters getMetaData() {
        return this.parameters;
    }
    
    private boolean init(final boolean isFirstMember) throws IOException {
        assert isFirstMember || this.decompressConcatenated;
        final int magic0 = this.in.read();
        final int magic2 = this.in.read();
        if (magic0 == -1 && !isFirstMember) {
            return false;
        }
        if (magic0 != 31 || magic2 != 139) {
            throw new IOException(isFirstMember ? "Input is not in the .gz format" : "Garbage after a valid .gz stream");
        }
        final DataInputStream inData = new DataInputStream(this.in);
        final int method = inData.readUnsignedByte();
        if (method != 8) {
            throw new IOException("Unsupported compression method " + method + " in the .gz header");
        }
        final int flg = inData.readUnsignedByte();
        if ((flg & 0xE0) != 0x0) {
            throw new IOException("Reserved flags are set in the .gz header");
        }
        this.parameters.setModificationTime(this.readLittleEndianInt(inData) * 1000);
        switch (inData.readUnsignedByte()) {
            case 2: {
                this.parameters.setCompressionLevel(9);
                break;
            }
            case 4: {
                this.parameters.setCompressionLevel(1);
                break;
            }
        }
        this.parameters.setOperatingSystem(inData.readUnsignedByte());
        if ((flg & 0x4) != 0x0) {
            int xlen = inData.readUnsignedByte();
            xlen |= inData.readUnsignedByte() << 8;
            while (xlen-- > 0) {
                inData.readUnsignedByte();
            }
        }
        if ((flg & 0x8) != 0x0) {
            this.parameters.setFilename(new String(this.readToNull(inData), "ISO-8859-1"));
        }
        if ((flg & 0x10) != 0x0) {
            this.parameters.setComment(new String(this.readToNull(inData), "ISO-8859-1"));
        }
        if ((flg & 0x2) != 0x0) {
            inData.readShort();
        }
        this.inf.reset();
        this.crc.reset();
        this.memberSize = 0;
        return true;
    }
    
    private byte[] readToNull(final DataInputStream inData) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int b = 0;
        while ((b = inData.readUnsignedByte()) != 0) {
            bos.write(b);
        }
        return bos.toByteArray();
    }
    
    private int readLittleEndianInt(final DataInputStream inData) throws IOException {
        return inData.readUnsignedByte() | inData.readUnsignedByte() << 8 | inData.readUnsignedByte() << 16 | inData.readUnsignedByte() << 24;
    }
    
    @Override
    public int read() throws IOException {
        return (this.read(this.oneByte, 0, 1) == -1) ? -1 : (this.oneByte[0] & 0xFF);
    }
    
    @Override
    public int read(final byte[] b, int off, int len) throws IOException {
        if (this.endReached) {
            return -1;
        }
        int size = 0;
        while (len > 0) {
            if (this.inf.needsInput()) {
                this.in.mark(this.buf.length);
                this.bufUsed = this.in.read(this.buf);
                if (this.bufUsed == -1) {
                    throw new EOFException();
                }
                this.inf.setInput(this.buf, 0, this.bufUsed);
            }
            int ret;
            try {
                ret = this.inf.inflate(b, off, len);
            }
            catch (DataFormatException e) {
                throw new IOException("Gzip-compressed data is corrupt");
            }
            this.crc.update(b, off, ret);
            this.memberSize += ret;
            off += ret;
            len -= ret;
            size += ret;
            this.count(ret);
            if (this.inf.finished()) {
                this.in.reset();
                final int skipAmount = this.bufUsed - this.inf.getRemaining();
                if (this.in.skip(skipAmount) != skipAmount) {
                    throw new IOException();
                }
                this.bufUsed = 0;
                final DataInputStream inData = new DataInputStream(this.in);
                long crcStored = 0L;
                for (int i = 0; i < 4; ++i) {
                    crcStored |= (long)inData.readUnsignedByte() << i * 8;
                }
                if (crcStored != this.crc.getValue()) {
                    throw new IOException("Gzip-compressed data is corrupt (CRC32 error)");
                }
                int isize = 0;
                for (int j = 0; j < 4; ++j) {
                    isize |= inData.readUnsignedByte() << j * 8;
                }
                if (isize != this.memberSize) {
                    throw new IOException("Gzip-compressed data is corrupt(uncompressed size mismatch)");
                }
                if (!this.decompressConcatenated || !this.init(false)) {
                    this.inf.end();
                    this.inf = null;
                    this.endReached = true;
                    return (size == 0) ? -1 : size;
                }
                continue;
            }
        }
        return size;
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return length >= 2 && signature[0] == 31 && signature[1] == -117;
    }
    
    @Override
    public void close() throws IOException {
        if (this.inf != null) {
            this.inf.end();
            this.inf = null;
        }
        if (this.in != System.in) {
            this.in.close();
        }
    }
}
