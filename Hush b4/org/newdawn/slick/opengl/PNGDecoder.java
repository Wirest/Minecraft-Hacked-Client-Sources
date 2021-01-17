// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import java.util.zip.DataFormatException;
import java.io.EOFException;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.zip.CRC32;
import java.io.InputStream;

public class PNGDecoder
{
    public static Format ALPHA;
    public static Format LUMINANCE;
    public static Format LUMINANCE_ALPHA;
    public static Format RGB;
    public static Format RGBA;
    public static Format BGRA;
    public static Format ABGR;
    private static final byte[] SIGNATURE;
    private static final int IHDR = 1229472850;
    private static final int PLTE = 1347179589;
    private static final int tRNS = 1951551059;
    private static final int IDAT = 1229209940;
    private static final int IEND = 1229278788;
    private static final byte COLOR_GREYSCALE = 0;
    private static final byte COLOR_TRUECOLOR = 2;
    private static final byte COLOR_INDEXED = 3;
    private static final byte COLOR_GREYALPHA = 4;
    private static final byte COLOR_TRUEALPHA = 6;
    private final InputStream input;
    private final CRC32 crc;
    private final byte[] buffer;
    private int chunkLength;
    private int chunkType;
    private int chunkRemaining;
    private int width;
    private int height;
    private int bitdepth;
    private int colorType;
    private int bytesPerPixel;
    private byte[] palette;
    private byte[] paletteA;
    private byte[] transPixel;
    
    static {
        PNGDecoder.ALPHA = new Format(1, true, null);
        PNGDecoder.LUMINANCE = new Format(1, false, null);
        PNGDecoder.LUMINANCE_ALPHA = new Format(2, true, null);
        PNGDecoder.RGB = new Format(3, false, null);
        PNGDecoder.RGBA = new Format(4, true, null);
        PNGDecoder.BGRA = new Format(4, true, null);
        PNGDecoder.ABGR = new Format(4, true, null);
        SIGNATURE = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
    }
    
    public PNGDecoder(final InputStream input) throws IOException {
        this.input = input;
        this.crc = new CRC32();
        this.readFully(this.buffer = new byte[4096], 0, PNGDecoder.SIGNATURE.length);
        if (!checkSignature(this.buffer)) {
            throw new IOException("Not a valid PNG file");
        }
        this.openChunk(1229472850);
        this.readIHDR();
        this.closeChunk();
    Label_0120:
        while (true) {
            this.openChunk();
            switch (this.chunkType) {
                case 1229209940: {
                    break Label_0120;
                }
                case 1347179589: {
                    this.readPLTE();
                    break;
                }
                case 1951551059: {
                    this.readtRNS();
                    break;
                }
            }
            this.closeChunk();
        }
        if (this.colorType == 3 && this.palette == null) {
            throw new IOException("Missing PLTE chunk");
        }
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean hasAlpha() {
        return this.colorType == 6 || this.paletteA != null || this.transPixel != null;
    }
    
    public boolean isRGB() {
        return this.colorType == 6 || this.colorType == 2 || this.colorType == 3;
    }
    
    public Format decideTextureFormat(final Format fmt) {
        switch (this.colorType) {
            case 2: {
                if (fmt == PNGDecoder.ABGR || fmt == PNGDecoder.RGBA || fmt == PNGDecoder.BGRA || fmt == PNGDecoder.RGB) {
                    return fmt;
                }
                return PNGDecoder.RGB;
            }
            case 6: {
                if (fmt == PNGDecoder.ABGR || fmt == PNGDecoder.RGBA || fmt == PNGDecoder.BGRA || fmt == PNGDecoder.RGB) {
                    return fmt;
                }
                return PNGDecoder.RGBA;
            }
            case 0: {
                if (fmt == PNGDecoder.LUMINANCE || fmt == PNGDecoder.ALPHA) {
                    return fmt;
                }
                return PNGDecoder.LUMINANCE;
            }
            case 4: {
                return PNGDecoder.LUMINANCE_ALPHA;
            }
            case 3: {
                if (fmt == PNGDecoder.ABGR || fmt == PNGDecoder.RGBA || fmt == PNGDecoder.BGRA) {
                    return fmt;
                }
                return PNGDecoder.RGBA;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
    }
    
    public void decode(final ByteBuffer buffer, final int stride, final Format fmt) throws IOException {
        final int offset = buffer.position();
        final int lineSize = (this.width * this.bitdepth + 7) / 8 * this.bytesPerPixel;
        byte[] curLine = new byte[lineSize + 1];
        byte[] prevLine = new byte[lineSize + 1];
        byte[] palLine = (byte[])((this.bitdepth < 8) ? new byte[this.width + 1] : null);
        final Inflater inflater = new Inflater();
        try {
            for (int y = 0; y < this.height; ++y) {
                this.readChunkUnzip(inflater, curLine, 0, curLine.length);
                this.unfilter(curLine, prevLine);
                buffer.position(offset + y * stride);
                switch (this.colorType) {
                    case 2: {
                        if (fmt == PNGDecoder.ABGR) {
                            this.copyRGBtoABGR(buffer, curLine);
                            break;
                        }
                        if (fmt == PNGDecoder.RGBA) {
                            this.copyRGBtoRGBA(buffer, curLine);
                            break;
                        }
                        if (fmt == PNGDecoder.BGRA) {
                            this.copyRGBtoBGRA(buffer, curLine);
                            break;
                        }
                        if (fmt == PNGDecoder.RGB) {
                            this.copy(buffer, curLine);
                            break;
                        }
                        throw new UnsupportedOperationException("Unsupported format for this image");
                    }
                    case 6: {
                        if (fmt == PNGDecoder.ABGR) {
                            this.copyRGBAtoABGR(buffer, curLine);
                            break;
                        }
                        if (fmt == PNGDecoder.RGBA) {
                            this.copy(buffer, curLine);
                            break;
                        }
                        if (fmt == PNGDecoder.BGRA) {
                            this.copyRGBAtoBGRA(buffer, curLine);
                            break;
                        }
                        if (fmt == PNGDecoder.RGB) {
                            this.copyRGBAtoRGB(buffer, curLine);
                            break;
                        }
                        throw new UnsupportedOperationException("Unsupported format for this image");
                    }
                    case 0: {
                        if (fmt == PNGDecoder.LUMINANCE || fmt == PNGDecoder.ALPHA) {
                            this.copy(buffer, curLine);
                            break;
                        }
                        throw new UnsupportedOperationException("Unsupported format for this image");
                    }
                    case 4: {
                        if (fmt == PNGDecoder.LUMINANCE_ALPHA) {
                            this.copy(buffer, curLine);
                            break;
                        }
                        throw new UnsupportedOperationException("Unsupported format for this image");
                    }
                    case 3: {
                        switch (this.bitdepth) {
                            case 8: {
                                palLine = curLine;
                                break;
                            }
                            case 4: {
                                this.expand4(curLine, palLine);
                                break;
                            }
                            case 2: {
                                this.expand2(curLine, palLine);
                                break;
                            }
                            case 1: {
                                this.expand1(curLine, palLine);
                                break;
                            }
                            default: {
                                throw new UnsupportedOperationException("Unsupported bitdepth for this image");
                            }
                        }
                        if (fmt == PNGDecoder.ABGR) {
                            this.copyPALtoABGR(buffer, palLine);
                            break;
                        }
                        if (fmt == PNGDecoder.RGBA) {
                            this.copyPALtoRGBA(buffer, palLine);
                            break;
                        }
                        if (fmt == PNGDecoder.BGRA) {
                            this.copyPALtoBGRA(buffer, palLine);
                            break;
                        }
                        throw new UnsupportedOperationException("Unsupported format for this image");
                    }
                    default: {
                        throw new UnsupportedOperationException("Not yet implemented");
                    }
                }
                final byte[] tmp = curLine;
                curLine = prevLine;
                prevLine = tmp;
            }
        }
        finally {
            inflater.end();
        }
        inflater.end();
    }
    
    private void copy(final ByteBuffer buffer, final byte[] curLine) {
        buffer.put(curLine, 1, curLine.length - 1);
    }
    
    private void copyRGBtoABGR(final ByteBuffer buffer, final byte[] curLine) {
        if (this.transPixel != null) {
            final byte tr = this.transPixel[1];
            final byte tg = this.transPixel[3];
            final byte tb = this.transPixel[5];
            for (int i = 1, n = curLine.length; i < n; i += 3) {
                final byte r = curLine[i];
                final byte g = curLine[i + 1];
                final byte b = curLine[i + 2];
                byte a = -1;
                if (r == tr && g == tg && b == tb) {
                    a = 0;
                }
                buffer.put(a).put(b).put(g).put(r);
            }
        }
        else {
            for (int j = 1, n2 = curLine.length; j < n2; j += 3) {
                buffer.put((byte)(-1)).put(curLine[j + 2]).put(curLine[j + 1]).put(curLine[j]);
            }
        }
    }
    
    private void copyRGBtoRGBA(final ByteBuffer buffer, final byte[] curLine) {
        if (this.transPixel != null) {
            final byte tr = this.transPixel[1];
            final byte tg = this.transPixel[3];
            final byte tb = this.transPixel[5];
            for (int i = 1, n = curLine.length; i < n; i += 3) {
                final byte r = curLine[i];
                final byte g = curLine[i + 1];
                final byte b = curLine[i + 2];
                byte a = -1;
                if (r == tr && g == tg && b == tb) {
                    a = 0;
                }
                buffer.put(r).put(g).put(b).put(a);
            }
        }
        else {
            for (int j = 1, n2 = curLine.length; j < n2; j += 3) {
                buffer.put(curLine[j]).put(curLine[j + 1]).put(curLine[j + 2]).put((byte)(-1));
            }
        }
    }
    
    private void copyRGBtoBGRA(final ByteBuffer buffer, final byte[] curLine) {
        if (this.transPixel != null) {
            final byte tr = this.transPixel[1];
            final byte tg = this.transPixel[3];
            final byte tb = this.transPixel[5];
            for (int i = 1, n = curLine.length; i < n; i += 3) {
                final byte r = curLine[i];
                final byte g = curLine[i + 1];
                final byte b = curLine[i + 2];
                byte a = -1;
                if (r == tr && g == tg && b == tb) {
                    a = 0;
                }
                buffer.put(b).put(g).put(r).put(a);
            }
        }
        else {
            for (int j = 1, n2 = curLine.length; j < n2; j += 3) {
                buffer.put(curLine[j + 2]).put(curLine[j + 1]).put(curLine[j]).put((byte)(-1));
            }
        }
    }
    
    private void copyRGBAtoABGR(final ByteBuffer buffer, final byte[] curLine) {
        for (int i = 1, n = curLine.length; i < n; i += 4) {
            buffer.put(curLine[i + 3]).put(curLine[i + 2]).put(curLine[i + 1]).put(curLine[i]);
        }
    }
    
    private void copyRGBAtoBGRA(final ByteBuffer buffer, final byte[] curLine) {
        for (int i = 1, n = curLine.length; i < n; i += 4) {
            buffer.put(curLine[i + 2]).put(curLine[i + 1]).put(curLine[i + 0]).put(curLine[i + 3]);
        }
    }
    
    private void copyRGBAtoRGB(final ByteBuffer buffer, final byte[] curLine) {
        for (int i = 1, n = curLine.length; i < n; i += 4) {
            buffer.put(curLine[i]).put(curLine[i + 1]).put(curLine[i + 2]);
        }
    }
    
    private void copyPALtoABGR(final ByteBuffer buffer, final byte[] curLine) {
        if (this.paletteA != null) {
            for (int i = 1, n = curLine.length; i < n; ++i) {
                final int idx = curLine[i] & 0xFF;
                final byte r = this.palette[idx * 3 + 0];
                final byte g = this.palette[idx * 3 + 1];
                final byte b = this.palette[idx * 3 + 2];
                final byte a = this.paletteA[idx];
                buffer.put(a).put(b).put(g).put(r);
            }
        }
        else {
            for (int i = 1, n = curLine.length; i < n; ++i) {
                final int idx = curLine[i] & 0xFF;
                final byte r = this.palette[idx * 3 + 0];
                final byte g = this.palette[idx * 3 + 1];
                final byte b = this.palette[idx * 3 + 2];
                final byte a = -1;
                buffer.put(a).put(b).put(g).put(r);
            }
        }
    }
    
    private void copyPALtoRGBA(final ByteBuffer buffer, final byte[] curLine) {
        if (this.paletteA != null) {
            for (int i = 1, n = curLine.length; i < n; ++i) {
                final int idx = curLine[i] & 0xFF;
                final byte r = this.palette[idx * 3 + 0];
                final byte g = this.palette[idx * 3 + 1];
                final byte b = this.palette[idx * 3 + 2];
                final byte a = this.paletteA[idx];
                buffer.put(r).put(g).put(b).put(a);
            }
        }
        else {
            for (int i = 1, n = curLine.length; i < n; ++i) {
                final int idx = curLine[i] & 0xFF;
                final byte r = this.palette[idx * 3 + 0];
                final byte g = this.palette[idx * 3 + 1];
                final byte b = this.palette[idx * 3 + 2];
                final byte a = -1;
                buffer.put(r).put(g).put(b).put(a);
            }
        }
    }
    
    private void copyPALtoBGRA(final ByteBuffer buffer, final byte[] curLine) {
        if (this.paletteA != null) {
            for (int i = 1, n = curLine.length; i < n; ++i) {
                final int idx = curLine[i] & 0xFF;
                final byte r = this.palette[idx * 3 + 0];
                final byte g = this.palette[idx * 3 + 1];
                final byte b = this.palette[idx * 3 + 2];
                final byte a = this.paletteA[idx];
                buffer.put(b).put(g).put(r).put(a);
            }
        }
        else {
            for (int i = 1, n = curLine.length; i < n; ++i) {
                final int idx = curLine[i] & 0xFF;
                final byte r = this.palette[idx * 3 + 0];
                final byte g = this.palette[idx * 3 + 1];
                final byte b = this.palette[idx * 3 + 2];
                final byte a = -1;
                buffer.put(b).put(g).put(r).put(a);
            }
        }
    }
    
    private void expand4(final byte[] src, final byte[] dst) {
        int i = 1;
        final int n = dst.length;
        while (i < n) {
            final int val = src[1 + (i >> 1)] & 0xFF;
            switch (n - i) {
                default: {
                    dst[i + 1] = (byte)(val & 0xF);
                }
                case 1: {
                    dst[i] = (byte)(val >> 4);
                    i += 2;
                    continue;
                }
            }
        }
    }
    
    private void expand2(final byte[] src, final byte[] dst) {
        int i = 1;
        final int n = dst.length;
        while (i < n) {
            final int val = src[1 + (i >> 2)] & 0xFF;
            switch (n - i) {
                default: {
                    dst[i + 3] = (byte)(val & 0x3);
                }
                case 3: {
                    dst[i + 2] = (byte)(val >> 2 & 0x3);
                }
                case 2: {
                    dst[i + 1] = (byte)(val >> 4 & 0x3);
                }
                case 1: {
                    dst[i] = (byte)(val >> 6);
                    i += 4;
                    continue;
                }
            }
        }
    }
    
    private void expand1(final byte[] src, final byte[] dst) {
        int i = 1;
        final int n = dst.length;
        while (i < n) {
            final int val = src[1 + (i >> 3)] & 0xFF;
            switch (n - i) {
                default: {
                    dst[i + 7] = (byte)(val & 0x1);
                }
                case 7: {
                    dst[i + 6] = (byte)(val >> 1 & 0x1);
                }
                case 6: {
                    dst[i + 5] = (byte)(val >> 2 & 0x1);
                }
                case 5: {
                    dst[i + 4] = (byte)(val >> 3 & 0x1);
                }
                case 4: {
                    dst[i + 3] = (byte)(val >> 4 & 0x1);
                }
                case 3: {
                    dst[i + 2] = (byte)(val >> 5 & 0x1);
                }
                case 2: {
                    dst[i + 1] = (byte)(val >> 6 & 0x1);
                }
                case 1: {
                    dst[i] = (byte)(val >> 7);
                    i += 8;
                    continue;
                }
            }
        }
    }
    
    private void unfilter(final byte[] curLine, final byte[] prevLine) throws IOException {
        switch (curLine[0]) {
            case 0: {
                break;
            }
            case 1: {
                this.unfilterSub(curLine);
                break;
            }
            case 2: {
                this.unfilterUp(curLine, prevLine);
                break;
            }
            case 3: {
                this.unfilterAverage(curLine, prevLine);
                break;
            }
            case 4: {
                this.unfilterPaeth(curLine, prevLine);
                break;
            }
            default: {
                throw new IOException("invalide filter type in scanline: " + curLine[0]);
            }
        }
    }
    
    private void unfilterSub(final byte[] curLine) {
        final int bpp = this.bytesPerPixel;
        for (int i = bpp + 1, n = curLine.length; i < n; ++i) {
            final int n2 = i;
            curLine[n2] += curLine[i - bpp];
        }
    }
    
    private void unfilterUp(final byte[] curLine, final byte[] prevLine) {
        final int bpp = this.bytesPerPixel;
        for (int i = 1, n = curLine.length; i < n; ++i) {
            final int n2 = i;
            curLine[n2] += prevLine[i];
        }
    }
    
    private void unfilterAverage(final byte[] curLine, final byte[] prevLine) {
        int bpp;
        int i;
        for (bpp = this.bytesPerPixel, i = 1; i <= bpp; ++i) {
            final int n2 = i;
            curLine[n2] += (byte)((prevLine[i] & 0xFF) >>> 1);
        }
        for (int n = curLine.length; i < n; ++i) {
            final int n3 = i;
            curLine[n3] += (byte)((prevLine[i] & 0xFF) + (curLine[i - bpp] & 0xFF) >>> 1);
        }
    }
    
    private void unfilterPaeth(final byte[] curLine, final byte[] prevLine) {
        int bpp;
        int i;
        for (bpp = this.bytesPerPixel, i = 1; i <= bpp; ++i) {
            final int n2 = i;
            curLine[n2] += prevLine[i];
        }
        for (int n = curLine.length; i < n; ++i) {
            final int a = curLine[i - bpp] & 0xFF;
            final int b = prevLine[i] & 0xFF;
            int c = prevLine[i - bpp] & 0xFF;
            final int p = a + b - c;
            int pa = p - a;
            if (pa < 0) {
                pa = -pa;
            }
            int pb = p - b;
            if (pb < 0) {
                pb = -pb;
            }
            int pc = p - c;
            if (pc < 0) {
                pc = -pc;
            }
            if (pa <= pb && pa <= pc) {
                c = a;
            }
            else if (pb <= pc) {
                c = b;
            }
            final int n3 = i;
            curLine[n3] += (byte)c;
        }
    }
    
    private void readIHDR() throws IOException {
        this.checkChunkLength(13);
        this.readChunk(this.buffer, 0, 13);
        this.width = this.readInt(this.buffer, 0);
        this.height = this.readInt(this.buffer, 4);
        this.bitdepth = (this.buffer[8] & 0xFF);
        Label_0416: {
            switch (this.colorType = (this.buffer[9] & 0xFF)) {
                case 0: {
                    if (this.bitdepth != 8) {
                        throw new IOException("Unsupported bit depth: " + this.bitdepth);
                    }
                    this.bytesPerPixel = 1;
                    break;
                }
                case 4: {
                    if (this.bitdepth != 8) {
                        throw new IOException("Unsupported bit depth: " + this.bitdepth);
                    }
                    this.bytesPerPixel = 2;
                    break;
                }
                case 2: {
                    if (this.bitdepth != 8) {
                        throw new IOException("Unsupported bit depth: " + this.bitdepth);
                    }
                    this.bytesPerPixel = 3;
                    break;
                }
                case 6: {
                    if (this.bitdepth != 8) {
                        throw new IOException("Unsupported bit depth: " + this.bitdepth);
                    }
                    this.bytesPerPixel = 4;
                    break;
                }
                case 3: {
                    switch (this.bitdepth) {
                        case 1:
                        case 2:
                        case 4:
                        case 8: {
                            this.bytesPerPixel = 1;
                            break Label_0416;
                        }
                        default: {
                            throw new IOException("Unsupported bit depth: " + this.bitdepth);
                        }
                    }
                    break;
                }
                default: {
                    throw new IOException("unsupported color format: " + this.colorType);
                }
            }
        }
        if (this.buffer[10] != 0) {
            throw new IOException("unsupported compression method");
        }
        if (this.buffer[11] != 0) {
            throw new IOException("unsupported filtering method");
        }
        if (this.buffer[12] != 0) {
            throw new IOException("unsupported interlace method");
        }
    }
    
    private void readPLTE() throws IOException {
        final int paletteEntries = this.chunkLength / 3;
        if (paletteEntries < 1 || paletteEntries > 256 || this.chunkLength % 3 != 0) {
            throw new IOException("PLTE chunk has wrong length");
        }
        this.readChunk(this.palette = new byte[paletteEntries * 3], 0, this.palette.length);
    }
    
    private void readtRNS() throws IOException {
        switch (this.colorType) {
            case 0: {
                this.checkChunkLength(2);
                this.readChunk(this.transPixel = new byte[2], 0, 2);
                break;
            }
            case 2: {
                this.checkChunkLength(6);
                this.readChunk(this.transPixel = new byte[6], 0, 6);
                break;
            }
            case 3: {
                if (this.palette == null) {
                    throw new IOException("tRNS chunk without PLTE chunk");
                }
                Arrays.fill(this.paletteA = new byte[this.palette.length / 3], (byte)(-1));
                this.readChunk(this.paletteA, 0, this.paletteA.length);
                break;
            }
        }
    }
    
    private void closeChunk() throws IOException {
        if (this.chunkRemaining > 0) {
            this.skip(this.chunkRemaining + 4);
        }
        else {
            this.readFully(this.buffer, 0, 4);
            final int expectedCrc = this.readInt(this.buffer, 0);
            final int computedCrc = (int)this.crc.getValue();
            if (computedCrc != expectedCrc) {
                throw new IOException("Invalid CRC");
            }
        }
        this.chunkRemaining = 0;
        this.chunkLength = 0;
        this.chunkType = 0;
    }
    
    private void openChunk() throws IOException {
        this.readFully(this.buffer, 0, 8);
        this.chunkLength = this.readInt(this.buffer, 0);
        this.chunkType = this.readInt(this.buffer, 4);
        this.chunkRemaining = this.chunkLength;
        this.crc.reset();
        this.crc.update(this.buffer, 4, 4);
    }
    
    private void openChunk(final int expected) throws IOException {
        this.openChunk();
        if (this.chunkType != expected) {
            throw new IOException("Expected chunk: " + Integer.toHexString(expected));
        }
    }
    
    private void checkChunkLength(final int expected) throws IOException {
        if (this.chunkLength != expected) {
            throw new IOException("Chunk has wrong size");
        }
    }
    
    private int readChunk(final byte[] buffer, final int offset, int length) throws IOException {
        if (length > this.chunkRemaining) {
            length = this.chunkRemaining;
        }
        this.readFully(buffer, offset, length);
        this.crc.update(buffer, offset, length);
        this.chunkRemaining -= length;
        return length;
    }
    
    private void refillInflater(final Inflater inflater) throws IOException {
        while (this.chunkRemaining == 0) {
            this.closeChunk();
            this.openChunk(1229209940);
        }
        final int read = this.readChunk(this.buffer, 0, this.buffer.length);
        inflater.setInput(this.buffer, 0, read);
    }
    
    private void readChunkUnzip(final Inflater inflater, final byte[] buffer, int offset, int length) throws IOException {
        try {
            do {
                final int read = inflater.inflate(buffer, offset, length);
                if (read <= 0) {
                    if (inflater.finished()) {
                        throw new EOFException();
                    }
                    if (!inflater.needsInput()) {
                        throw new IOException("Can't inflate " + length + " bytes");
                    }
                    this.refillInflater(inflater);
                }
                else {
                    offset += read;
                    length -= read;
                }
            } while (length > 0);
        }
        catch (DataFormatException ex) {
            throw (IOException)new IOException("inflate error").initCause(ex);
        }
    }
    
    private void readFully(final byte[] buffer, int offset, int length) throws IOException {
        do {
            final int read = this.input.read(buffer, offset, length);
            if (read < 0) {
                throw new EOFException();
            }
            offset += read;
            length -= read;
        } while (length > 0);
    }
    
    private int readInt(final byte[] buffer, final int offset) {
        return buffer[offset] << 24 | (buffer[offset + 1] & 0xFF) << 16 | (buffer[offset + 2] & 0xFF) << 8 | (buffer[offset + 3] & 0xFF);
    }
    
    private void skip(long amount) throws IOException {
        while (amount > 0L) {
            final long skipped = this.input.skip(amount);
            if (skipped < 0L) {
                throw new EOFException();
            }
            amount -= skipped;
        }
    }
    
    private static boolean checkSignature(final byte[] buffer) {
        for (int i = 0; i < PNGDecoder.SIGNATURE.length; ++i) {
            if (buffer[i] != PNGDecoder.SIGNATURE[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static class Format
    {
        final int numComponents;
        final boolean hasAlpha;
        
        private Format(final int numComponents, final boolean hasAlpha) {
            this.numComponents = numComponents;
            this.hasAlpha = hasAlpha;
        }
        
        public int getNumComponents() {
            return this.numComponents;
        }
        
        public boolean isHasAlpha() {
            return this.hasAlpha;
        }
    }
}
