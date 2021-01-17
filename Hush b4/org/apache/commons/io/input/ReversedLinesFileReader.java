// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.nio.charset.CharsetEncoder;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.Charsets;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.io.Closeable;

public class ReversedLinesFileReader implements Closeable
{
    private final int blockSize;
    private final Charset encoding;
    private final RandomAccessFile randomAccessFile;
    private final long totalByteLength;
    private final long totalBlockCount;
    private final byte[][] newLineSequences;
    private final int avoidNewlineSplitBufferSize;
    private final int byteDecrement;
    private FilePart currentFilePart;
    private boolean trailingNewlineOfFileSkipped;
    
    public ReversedLinesFileReader(final File file) throws IOException {
        this(file, 4096, Charset.defaultCharset().toString());
    }
    
    public ReversedLinesFileReader(final File file, final int blockSize, final Charset encoding) throws IOException {
        this.trailingNewlineOfFileSkipped = false;
        this.blockSize = blockSize;
        this.encoding = encoding;
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.totalByteLength = this.randomAccessFile.length();
        int lastBlockLength = (int)(this.totalByteLength % blockSize);
        if (lastBlockLength > 0) {
            this.totalBlockCount = this.totalByteLength / blockSize + 1L;
        }
        else {
            this.totalBlockCount = this.totalByteLength / blockSize;
            if (this.totalByteLength > 0L) {
                lastBlockLength = blockSize;
            }
        }
        this.currentFilePart = new FilePart(this.totalBlockCount, lastBlockLength, (byte[])null);
        final Charset charset = Charsets.toCharset(encoding);
        final CharsetEncoder charsetEncoder = charset.newEncoder();
        final float maxBytesPerChar = charsetEncoder.maxBytesPerChar();
        if (maxBytesPerChar == 1.0f) {
            this.byteDecrement = 1;
        }
        else if (charset == Charset.forName("UTF-8")) {
            this.byteDecrement = 1;
        }
        else if (charset == Charset.forName("Shift_JIS")) {
            this.byteDecrement = 1;
        }
        else if (charset == Charset.forName("UTF-16BE") || charset == Charset.forName("UTF-16LE")) {
            this.byteDecrement = 2;
        }
        else {
            if (charset == Charset.forName("UTF-16")) {
                throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
            }
            throw new UnsupportedEncodingException("Encoding " + encoding + " is not supported yet (feel free to submit a patch)");
        }
        this.newLineSequences = new byte[][] { "\r\n".getBytes(encoding), "\n".getBytes(encoding), "\r".getBytes(encoding) };
        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
    }
    
    public ReversedLinesFileReader(final File file, final int blockSize, final String encoding) throws IOException {
        this(file, blockSize, Charsets.toCharset(encoding));
    }
    
    public String readLine() throws IOException {
        String line;
        for (line = this.currentFilePart.readLine(); line == null; line = this.currentFilePart.readLine()) {
            this.currentFilePart = this.currentFilePart.rollOver();
            if (this.currentFilePart == null) {
                break;
            }
        }
        if ("".equals(line) && !this.trailingNewlineOfFileSkipped) {
            this.trailingNewlineOfFileSkipped = true;
            line = this.readLine();
        }
        return line;
    }
    
    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
    }
    
    private class FilePart
    {
        private final long no;
        private final byte[] data;
        private byte[] leftOver;
        private int currentLastBytePos;
        
        private FilePart(final long no, final int length, final byte[] leftOverOfLastFilePart) throws IOException {
            this.no = no;
            final int dataLength = length + ((leftOverOfLastFilePart != null) ? leftOverOfLastFilePart.length : 0);
            this.data = new byte[dataLength];
            final long off = (no - 1L) * ReversedLinesFileReader.this.blockSize;
            if (no > 0L) {
                ReversedLinesFileReader.this.randomAccessFile.seek(off);
                final int countRead = ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, length);
                if (countRead != length) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            if (leftOverOfLastFilePart != null) {
                System.arraycopy(leftOverOfLastFilePart, 0, this.data, length, leftOverOfLastFilePart.length);
            }
            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }
        
        private FilePart rollOver() throws IOException {
            if (this.currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
            }
            if (this.no > 1L) {
                return new FilePart(this.no - 1L, ReversedLinesFileReader.this.blockSize, this.leftOver);
            }
            if (this.leftOver != null) {
                throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
            }
            return null;
        }
        
        private String readLine() throws IOException {
            String line = null;
            final boolean isLastFilePart = this.no == 1L;
            int i = this.currentLastBytePos;
            while (i > -1) {
                if (!isLastFilePart && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
                    this.createLeftOver();
                    break;
                }
                final int newLineMatchByteCount;
                if ((newLineMatchByteCount = this.getNewLineMatchByteCount(this.data, i)) > 0) {
                    final int lineStart = i + 1;
                    final int lineLengthBytes = this.currentLastBytePos - lineStart + 1;
                    if (lineLengthBytes < 0) {
                        throw new IllegalStateException("Unexpected negative line length=" + lineLengthBytes);
                    }
                    final byte[] lineData = new byte[lineLengthBytes];
                    System.arraycopy(this.data, lineStart, lineData, 0, lineLengthBytes);
                    line = new String(lineData, ReversedLinesFileReader.this.encoding);
                    this.currentLastBytePos = i - newLineMatchByteCount;
                    break;
                }
                else {
                    i -= ReversedLinesFileReader.this.byteDecrement;
                    if (i < 0) {
                        this.createLeftOver();
                        break;
                    }
                    continue;
                }
            }
            if (isLastFilePart && this.leftOver != null) {
                line = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
                this.leftOver = null;
            }
            return line;
        }
        
        private void createLeftOver() {
            final int lineLengthBytes = this.currentLastBytePos + 1;
            if (lineLengthBytes > 0) {
                this.leftOver = new byte[lineLengthBytes];
                System.arraycopy(this.data, 0, this.leftOver, 0, lineLengthBytes);
            }
            else {
                this.leftOver = null;
            }
            this.currentLastBytePos = -1;
        }
        
        private int getNewLineMatchByteCount(final byte[] data, final int i) {
            for (final byte[] newLineSequence : ReversedLinesFileReader.this.newLineSequences) {
                boolean match = true;
                for (int j = newLineSequence.length - 1; j >= 0; --j) {
                    final int k = i + j - (newLineSequence.length - 1);
                    match &= (k >= 0 && data[k] == newLineSequence[j]);
                }
                if (match) {
                    return newLineSequence.length;
                }
            }
            return 0;
        }
    }
}
