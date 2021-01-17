// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public final class UnicodeCompressor implements SCSU
{
    private static boolean[] sSingleTagTable;
    private static boolean[] sUnicodeTagTable;
    private int fCurrentWindow;
    private int[] fOffsets;
    private int fMode;
    private int[] fIndexCount;
    private int[] fTimeStamps;
    private int fTimeStamp;
    
    public UnicodeCompressor() {
        this.fCurrentWindow = 0;
        this.fOffsets = new int[8];
        this.fMode = 0;
        this.fIndexCount = new int[256];
        this.fTimeStamps = new int[8];
        this.fTimeStamp = 0;
        this.reset();
    }
    
    public static byte[] compress(final String buffer) {
        return compress(buffer.toCharArray(), 0, buffer.length());
    }
    
    public static byte[] compress(final char[] buffer, final int start, final int limit) {
        final UnicodeCompressor comp = new UnicodeCompressor();
        final int len = Math.max(4, 3 * (limit - start) + 1);
        final byte[] temp = new byte[len];
        final int byteCount = comp.compress(buffer, start, limit, null, temp, 0, len);
        final byte[] result = new byte[byteCount];
        System.arraycopy(temp, 0, result, 0, byteCount);
        return result;
    }
    
    public int compress(final char[] charBuffer, final int charBufferStart, final int charBufferLimit, final int[] charsRead, final byte[] byteBuffer, final int byteBufferStart, final int byteBufferLimit) {
        int bytePos = byteBufferStart;
        int ucPos = charBufferStart;
        int curUC = -1;
        int curIndex = -1;
        int nextUC = -1;
        int forwardUC = -1;
        int whichWindow = 0;
        int hiByte = 0;
        int loByte = 0;
        if (byteBuffer.length < 4 || byteBufferLimit - byteBufferStart < 4) {
            throw new IllegalArgumentException("byteBuffer.length < 4");
        }
    Label_1668:
        while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
            switch (this.fMode) {
                case 0: {
                    while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
                        curUC = charBuffer[ucPos++];
                        if (ucPos < charBufferLimit) {
                            nextUC = charBuffer[ucPos];
                        }
                        else {
                            nextUC = -1;
                        }
                        if (curUC < 128) {
                            loByte = (curUC & 0xFF);
                            if (UnicodeCompressor.sSingleTagTable[loByte]) {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = 1;
                            }
                            byteBuffer[bytePos++] = (byte)loByte;
                        }
                        else if (this.inDynamicWindow(curUC, this.fCurrentWindow)) {
                            byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[this.fCurrentWindow] + 128);
                        }
                        else if (!isCompressible(curUC)) {
                            if (nextUC != -1 && isCompressible(nextUC)) {
                                if (bytePos + 2 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = 14;
                                byteBuffer[bytePos++] = (byte)(curUC >>> 8);
                                byteBuffer[bytePos++] = (byte)(curUC & 0xFF);
                            }
                            else {
                                if (bytePos + 3 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = 15;
                                hiByte = curUC >>> 8;
                                loByte = (curUC & 0xFF);
                                if (UnicodeCompressor.sUnicodeTagTable[hiByte]) {
                                    byteBuffer[bytePos++] = -16;
                                }
                                byteBuffer[bytePos++] = (byte)hiByte;
                                byteBuffer[bytePos++] = (byte)loByte;
                                this.fMode = 1;
                                break;
                            }
                        }
                        else if ((whichWindow = this.findDynamicWindow(curUC)) != -1) {
                            if (ucPos + 1 < charBufferLimit) {
                                forwardUC = charBuffer[ucPos + 1];
                            }
                            else {
                                forwardUC = -1;
                            }
                            if (this.inDynamicWindow(nextUC, whichWindow) && this.inDynamicWindow(forwardUC, whichWindow)) {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = (byte)(16 + whichWindow);
                                byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                                this.fCurrentWindow = whichWindow;
                            }
                            else {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = (byte)(1 + whichWindow);
                                byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                            }
                        }
                        else if ((whichWindow = findStaticWindow(curUC)) != -1 && !inStaticWindow(nextUC, whichWindow)) {
                            if (bytePos + 1 >= byteBufferLimit) {
                                --ucPos;
                                break Label_1668;
                            }
                            byteBuffer[bytePos++] = (byte)(1 + whichWindow);
                            byteBuffer[bytePos++] = (byte)(curUC - UnicodeCompressor.sOffsets[whichWindow]);
                        }
                        else {
                            curIndex = makeIndex(curUC);
                            final int[] fIndexCount = this.fIndexCount;
                            final int n = curIndex;
                            ++fIndexCount[n];
                            if (ucPos + 1 < charBufferLimit) {
                                forwardUC = charBuffer[ucPos + 1];
                            }
                            else {
                                forwardUC = -1;
                            }
                            if (this.fIndexCount[curIndex] > 1 || (curIndex == makeIndex(nextUC) && curIndex == makeIndex(forwardUC))) {
                                if (bytePos + 2 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                whichWindow = this.getLRDefinedWindow();
                                byteBuffer[bytePos++] = (byte)(24 + whichWindow);
                                byteBuffer[bytePos++] = (byte)curIndex;
                                byteBuffer[bytePos++] = (byte)(curUC - UnicodeCompressor.sOffsetTable[curIndex] + 128);
                                this.fOffsets[whichWindow] = UnicodeCompressor.sOffsetTable[curIndex];
                                this.fCurrentWindow = whichWindow;
                                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                            }
                            else {
                                if (bytePos + 3 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = 15;
                                hiByte = curUC >>> 8;
                                loByte = (curUC & 0xFF);
                                if (UnicodeCompressor.sUnicodeTagTable[hiByte]) {
                                    byteBuffer[bytePos++] = -16;
                                }
                                byteBuffer[bytePos++] = (byte)hiByte;
                                byteBuffer[bytePos++] = (byte)loByte;
                                this.fMode = 1;
                                break;
                            }
                        }
                    }
                    continue;
                }
                case 1: {
                    while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
                        curUC = charBuffer[ucPos++];
                        if (ucPos < charBufferLimit) {
                            nextUC = charBuffer[ucPos];
                        }
                        else {
                            nextUC = -1;
                        }
                        if (!isCompressible(curUC) || (nextUC != -1 && !isCompressible(nextUC))) {
                            if (bytePos + 2 >= byteBufferLimit) {
                                --ucPos;
                                break Label_1668;
                            }
                            hiByte = curUC >>> 8;
                            loByte = (curUC & 0xFF);
                            if (UnicodeCompressor.sUnicodeTagTable[hiByte]) {
                                byteBuffer[bytePos++] = -16;
                            }
                            byteBuffer[bytePos++] = (byte)hiByte;
                            byteBuffer[bytePos++] = (byte)loByte;
                        }
                        else if (curUC < 128) {
                            loByte = (curUC & 0xFF);
                            if (nextUC != -1 && nextUC < 128 && !UnicodeCompressor.sSingleTagTable[loByte]) {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                whichWindow = this.fCurrentWindow;
                                byteBuffer[bytePos++] = (byte)(224 + whichWindow);
                                byteBuffer[bytePos++] = (byte)loByte;
                                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                                this.fMode = 0;
                                break;
                            }
                            else {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = 0;
                                byteBuffer[bytePos++] = (byte)loByte;
                            }
                        }
                        else if ((whichWindow = this.findDynamicWindow(curUC)) != -1) {
                            if (this.inDynamicWindow(nextUC, whichWindow)) {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                byteBuffer[bytePos++] = (byte)(224 + whichWindow);
                                byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                                this.fCurrentWindow = whichWindow;
                                this.fMode = 0;
                                break;
                            }
                            else {
                                if (bytePos + 2 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                hiByte = curUC >>> 8;
                                loByte = (curUC & 0xFF);
                                if (UnicodeCompressor.sUnicodeTagTable[hiByte]) {
                                    byteBuffer[bytePos++] = -16;
                                }
                                byteBuffer[bytePos++] = (byte)hiByte;
                                byteBuffer[bytePos++] = (byte)loByte;
                            }
                        }
                        else {
                            curIndex = makeIndex(curUC);
                            final int[] fIndexCount2 = this.fIndexCount;
                            final int n2 = curIndex;
                            ++fIndexCount2[n2];
                            if (ucPos + 1 < charBufferLimit) {
                                forwardUC = charBuffer[ucPos + 1];
                            }
                            else {
                                forwardUC = -1;
                            }
                            if (this.fIndexCount[curIndex] > 1 || (curIndex == makeIndex(nextUC) && curIndex == makeIndex(forwardUC))) {
                                if (bytePos + 2 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                whichWindow = this.getLRDefinedWindow();
                                byteBuffer[bytePos++] = (byte)(232 + whichWindow);
                                byteBuffer[bytePos++] = (byte)curIndex;
                                byteBuffer[bytePos++] = (byte)(curUC - UnicodeCompressor.sOffsetTable[curIndex] + 128);
                                this.fOffsets[whichWindow] = UnicodeCompressor.sOffsetTable[curIndex];
                                this.fCurrentWindow = whichWindow;
                                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                                this.fMode = 0;
                                break;
                            }
                            else {
                                if (bytePos + 2 >= byteBufferLimit) {
                                    --ucPos;
                                    break Label_1668;
                                }
                                hiByte = curUC >>> 8;
                                loByte = (curUC & 0xFF);
                                if (UnicodeCompressor.sUnicodeTagTable[hiByte]) {
                                    byteBuffer[bytePos++] = -16;
                                }
                                byteBuffer[bytePos++] = (byte)hiByte;
                                byteBuffer[bytePos++] = (byte)loByte;
                            }
                        }
                    }
                    continue;
                }
            }
        }
        if (charsRead != null) {
            charsRead[0] = ucPos - charBufferStart;
        }
        return bytePos - byteBufferStart;
    }
    
    public void reset() {
        this.fOffsets[0] = 128;
        this.fOffsets[1] = 192;
        this.fOffsets[2] = 1024;
        this.fOffsets[3] = 1536;
        this.fOffsets[4] = 2304;
        this.fOffsets[5] = 12352;
        this.fOffsets[6] = 12448;
        this.fOffsets[7] = 65280;
        for (int i = 0; i < 8; ++i) {
            this.fTimeStamps[i] = 0;
        }
        for (int i = 0; i <= 255; ++i) {
            this.fIndexCount[i] = 0;
        }
        this.fTimeStamp = 0;
        this.fCurrentWindow = 0;
        this.fMode = 0;
    }
    
    private static int makeIndex(final int c) {
        if (c >= 192 && c < 320) {
            return 249;
        }
        if (c >= 592 && c < 720) {
            return 250;
        }
        if (c >= 880 && c < 1008) {
            return 251;
        }
        if (c >= 1328 && c < 1424) {
            return 252;
        }
        if (c >= 12352 && c < 12448) {
            return 253;
        }
        if (c >= 12448 && c < 12576) {
            return 254;
        }
        if (c >= 65376 && c < 65439) {
            return 255;
        }
        if (c >= 128 && c < 13312) {
            return c / 128 & 0xFF;
        }
        if (c >= 57344 && c <= 65535) {
            return (c - 44032) / 128 & 0xFF;
        }
        return 0;
    }
    
    private boolean inDynamicWindow(final int c, final int whichWindow) {
        return c >= this.fOffsets[whichWindow] && c < this.fOffsets[whichWindow] + 128;
    }
    
    private static boolean inStaticWindow(final int c, final int whichWindow) {
        return c >= UnicodeCompressor.sOffsets[whichWindow] && c < UnicodeCompressor.sOffsets[whichWindow] + 128;
    }
    
    private static boolean isCompressible(final int c) {
        return c < 13312 || c >= 57344;
    }
    
    private int findDynamicWindow(final int c) {
        for (int i = 7; i >= 0; --i) {
            if (this.inDynamicWindow(c, i)) {
                final int[] fTimeStamps = this.fTimeStamps;
                final int n = i;
                ++fTimeStamps[n];
                return i;
            }
        }
        return -1;
    }
    
    private static int findStaticWindow(final int c) {
        for (int i = 7; i >= 0; --i) {
            if (inStaticWindow(c, i)) {
                return i;
            }
        }
        return -1;
    }
    
    private int getLRDefinedWindow() {
        int leastRU = Integer.MAX_VALUE;
        int whichWindow = -1;
        for (int i = 7; i >= 0; --i) {
            if (this.fTimeStamps[i] < leastRU) {
                leastRU = this.fTimeStamps[i];
                whichWindow = i;
            }
        }
        return whichWindow;
    }
    
    static {
        UnicodeCompressor.sSingleTagTable = new boolean[] { false, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
        UnicodeCompressor.sUnicodeTagTable = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false };
    }
}
