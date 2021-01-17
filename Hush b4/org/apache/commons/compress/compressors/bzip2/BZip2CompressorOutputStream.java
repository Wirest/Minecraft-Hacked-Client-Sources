// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

public class BZip2CompressorOutputStream extends CompressorOutputStream implements BZip2Constants
{
    public static final int MIN_BLOCKSIZE = 1;
    public static final int MAX_BLOCKSIZE = 9;
    private static final int GREATER_ICOST = 15;
    private static final int LESSER_ICOST = 0;
    private int last;
    private final int blockSize100k;
    private int bsBuff;
    private int bsLive;
    private final CRC crc;
    private int nInUse;
    private int nMTF;
    private int currentChar;
    private int runLength;
    private int blockCRC;
    private int combinedCRC;
    private final int allowableBlockSize;
    private Data data;
    private BlockSort blockSorter;
    private OutputStream out;
    
    private static void hbMakeCodeLengths(final byte[] len, final int[] freq, final Data dat, final int alphaSize, final int maxLen) {
        final int[] heap = dat.heap;
        final int[] weight = dat.weight;
        final int[] parent = dat.parent;
        int i = alphaSize;
        while (--i >= 0) {
            weight[i + 1] = ((freq[i] == 0) ? 1 : freq[i]) << 8;
        }
        boolean tooLong = true;
        while (tooLong) {
            tooLong = false;
            int nNodes = alphaSize;
            int nHeap = 0;
            heap[0] = 0;
            parent[weight[0] = 0] = -2;
            for (int j = 1; j <= alphaSize; ++j) {
                parent[j] = -1;
                ++nHeap;
                heap[nHeap] = j;
                int zz;
                int tmp;
                for (zz = nHeap, tmp = heap[zz]; weight[tmp] < weight[heap[zz >> 1]]; zz >>= 1) {
                    heap[zz] = heap[zz >> 1];
                }
                heap[zz] = tmp;
            }
            while (nHeap > 1) {
                final int n1 = heap[1];
                heap[1] = heap[nHeap];
                --nHeap;
                int yy = 0;
                int zz2 = 1;
                int tmp2 = heap[1];
                while (true) {
                    yy = zz2 << 1;
                    if (yy > nHeap) {
                        break;
                    }
                    if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]]) {
                        ++yy;
                    }
                    if (weight[tmp2] < weight[heap[yy]]) {
                        break;
                    }
                    heap[zz2] = heap[yy];
                    zz2 = yy;
                }
                heap[zz2] = tmp2;
                final int n2 = heap[1];
                heap[1] = heap[nHeap];
                --nHeap;
                yy = 0;
                zz2 = 1;
                tmp2 = heap[1];
                while (true) {
                    yy = zz2 << 1;
                    if (yy > nHeap) {
                        break;
                    }
                    if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]]) {
                        ++yy;
                    }
                    if (weight[tmp2] < weight[heap[yy]]) {
                        break;
                    }
                    heap[zz2] = heap[yy];
                    zz2 = yy;
                }
                heap[zz2] = tmp2;
                ++nNodes;
                parent[n1] = (parent[n2] = nNodes);
                final int weight_n1 = weight[n1];
                final int weight_n2 = weight[n2];
                weight[nNodes] = ((weight_n1 & 0xFFFFFF00) + (weight_n2 & 0xFFFFFF00) | 1 + (((weight_n1 & 0xFF) > (weight_n2 & 0xFF)) ? (weight_n1 & 0xFF) : (weight_n2 & 0xFF)));
                parent[nNodes] = -1;
                ++nHeap;
                heap[nHeap] = nNodes;
                tmp2 = 0;
                zz2 = nHeap;
                tmp2 = heap[zz2];
                for (int weight_tmp = weight[tmp2]; weight_tmp < weight[heap[zz2 >> 1]]; zz2 >>= 1) {
                    heap[zz2] = heap[zz2 >> 1];
                }
                heap[zz2] = tmp2;
            }
            for (int j = 1; j <= alphaSize; ++j) {
                int k = 0;
                int parent_k;
                for (int l = j; (parent_k = parent[l]) >= 0; l = parent_k, ++k) {}
                len[j - 1] = (byte)k;
                if (k > maxLen) {
                    tooLong = true;
                }
            }
            if (tooLong) {
                for (int j = 1; j < alphaSize; ++j) {
                    int k = weight[j] >> 8;
                    k = 1 + (k >> 1);
                    weight[j] = k << 8;
                }
            }
        }
    }
    
    public static int chooseBlockSize(final long inputLength) {
        return (inputLength > 0L) ? ((int)Math.min(inputLength / 132000L + 1L, 9L)) : 9;
    }
    
    public BZip2CompressorOutputStream(final OutputStream out) throws IOException {
        this(out, 9);
    }
    
    public BZip2CompressorOutputStream(final OutputStream out, final int blockSize) throws IOException {
        this.crc = new CRC();
        this.currentChar = -1;
        this.runLength = 0;
        if (blockSize < 1) {
            throw new IllegalArgumentException("blockSize(" + blockSize + ") < 1");
        }
        if (blockSize > 9) {
            throw new IllegalArgumentException("blockSize(" + blockSize + ") > 9");
        }
        this.blockSize100k = blockSize;
        this.out = out;
        this.allowableBlockSize = this.blockSize100k * 100000 - 20;
        this.init();
    }
    
    @Override
    public void write(final int b) throws IOException {
        if (this.out != null) {
            this.write0(b);
            return;
        }
        throw new IOException("closed");
    }
    
    private void writeRun() throws IOException {
        final int lastShadow = this.last;
        if (lastShadow < this.allowableBlockSize) {
            final int currentCharShadow = this.currentChar;
            final Data dataShadow = this.data;
            dataShadow.inUse[currentCharShadow] = true;
            final byte ch = (byte)currentCharShadow;
            int runLengthShadow = this.runLength;
            this.crc.updateCRC(currentCharShadow, runLengthShadow);
            switch (runLengthShadow) {
                case 1: {
                    dataShadow.block[lastShadow + 2] = ch;
                    this.last = lastShadow + 1;
                    break;
                }
                case 2: {
                    dataShadow.block[lastShadow + 2] = ch;
                    dataShadow.block[lastShadow + 3] = ch;
                    this.last = lastShadow + 2;
                    break;
                }
                case 3: {
                    final byte[] block = dataShadow.block;
                    block[lastShadow + 2] = ch;
                    block[lastShadow + 4] = (block[lastShadow + 3] = ch);
                    this.last = lastShadow + 3;
                    break;
                }
                default: {
                    runLengthShadow -= 4;
                    dataShadow.inUse[runLengthShadow] = true;
                    final byte[] block = dataShadow.block;
                    block[lastShadow + 3] = (block[lastShadow + 2] = ch);
                    block[lastShadow + 5] = (block[lastShadow + 4] = ch);
                    block[lastShadow + 6] = (byte)runLengthShadow;
                    this.last = lastShadow + 5;
                    break;
                }
            }
        }
        else {
            this.endBlock();
            this.initBlock();
            this.writeRun();
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.finish();
        super.finalize();
    }
    
    public void finish() throws IOException {
        if (this.out != null) {
            try {
                if (this.runLength > 0) {
                    this.writeRun();
                }
                this.currentChar = -1;
                this.endBlock();
                this.endCompression();
            }
            finally {
                this.out = null;
                this.data = null;
                this.blockSorter = null;
            }
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this.out != null) {
            final OutputStream outShadow = this.out;
            this.finish();
            outShadow.close();
        }
    }
    
    @Override
    public void flush() throws IOException {
        final OutputStream outShadow = this.out;
        if (outShadow != null) {
            outShadow.flush();
        }
    }
    
    private void init() throws IOException {
        this.bsPutUByte(66);
        this.bsPutUByte(90);
        this.data = new Data(this.blockSize100k);
        this.blockSorter = new BlockSort(this.data);
        this.bsPutUByte(104);
        this.bsPutUByte(48 + this.blockSize100k);
        this.combinedCRC = 0;
        this.initBlock();
    }
    
    private void initBlock() {
        this.crc.initialiseCRC();
        this.last = -1;
        final boolean[] inUse = this.data.inUse;
        int i = 256;
        while (--i >= 0) {
            inUse[i] = false;
        }
    }
    
    private void endBlock() throws IOException {
        this.blockCRC = this.crc.getFinalCRC();
        this.combinedCRC = (this.combinedCRC << 1 | this.combinedCRC >>> 31);
        this.combinedCRC ^= this.blockCRC;
        if (this.last == -1) {
            return;
        }
        this.blockSort();
        this.bsPutUByte(49);
        this.bsPutUByte(65);
        this.bsPutUByte(89);
        this.bsPutUByte(38);
        this.bsPutUByte(83);
        this.bsPutUByte(89);
        this.bsPutInt(this.blockCRC);
        this.bsW(1, 0);
        this.moveToFrontCodeAndSend();
    }
    
    private void endCompression() throws IOException {
        this.bsPutUByte(23);
        this.bsPutUByte(114);
        this.bsPutUByte(69);
        this.bsPutUByte(56);
        this.bsPutUByte(80);
        this.bsPutUByte(144);
        this.bsPutInt(this.combinedCRC);
        this.bsFinishedWithStream();
    }
    
    public final int getBlockSize() {
        return this.blockSize100k;
    }
    
    @Override
    public void write(final byte[] buf, int offs, final int len) throws IOException {
        if (offs < 0) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
        }
        if (offs + len > buf.length) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > buf.length(" + buf.length + ").");
        }
        if (this.out == null) {
            throw new IOException("stream closed");
        }
        final int hi = offs + len;
        while (offs < hi) {
            this.write0(buf[offs++]);
        }
    }
    
    private void write0(int b) throws IOException {
        if (this.currentChar != -1) {
            b &= 0xFF;
            if (this.currentChar == b) {
                if (++this.runLength > 254) {
                    this.writeRun();
                    this.currentChar = -1;
                    this.runLength = 0;
                }
            }
            else {
                this.writeRun();
                this.runLength = 1;
                this.currentChar = b;
            }
        }
        else {
            this.currentChar = (b & 0xFF);
            ++this.runLength;
        }
    }
    
    private static void hbAssignCodes(final int[] code, final byte[] length, final int minLen, final int maxLen, final int alphaSize) {
        int vec = 0;
        for (int n = minLen; n <= maxLen; ++n) {
            for (int i = 0; i < alphaSize; ++i) {
                if ((length[i] & 0xFF) == n) {
                    code[i] = vec;
                    ++vec;
                }
            }
            vec <<= 1;
        }
    }
    
    private void bsFinishedWithStream() throws IOException {
        while (this.bsLive > 0) {
            final int ch = this.bsBuff >> 24;
            this.out.write(ch);
            this.bsBuff <<= 8;
            this.bsLive -= 8;
        }
    }
    
    private void bsW(final int n, final int v) throws IOException {
        final OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        while (bsLiveShadow >= 8) {
            outShadow.write(bsBuffShadow >> 24);
            bsBuffShadow <<= 8;
            bsLiveShadow -= 8;
        }
        this.bsBuff = (bsBuffShadow | v << 32 - bsLiveShadow - n);
        this.bsLive = bsLiveShadow + n;
    }
    
    private void bsPutUByte(final int c) throws IOException {
        this.bsW(8, c);
    }
    
    private void bsPutInt(final int u) throws IOException {
        this.bsW(8, u >> 24 & 0xFF);
        this.bsW(8, u >> 16 & 0xFF);
        this.bsW(8, u >> 8 & 0xFF);
        this.bsW(8, u & 0xFF);
    }
    
    private void sendMTFValues() throws IOException {
        final byte[][] len = this.data.sendMTFValues_len;
        final int alphaSize = this.nInUse + 2;
        int t = 6;
        while (--t >= 0) {
            final byte[] len_t = len[t];
            int v = alphaSize;
            while (--v >= 0) {
                len_t[v] = 15;
            }
        }
        final int nGroups = (this.nMTF < 200) ? 2 : ((this.nMTF < 600) ? 3 : ((this.nMTF < 1200) ? 4 : ((this.nMTF < 2400) ? 5 : 6)));
        this.sendMTFValues0(nGroups, alphaSize);
        final int nSelectors = this.sendMTFValues1(nGroups, alphaSize);
        this.sendMTFValues2(nGroups, nSelectors);
        this.sendMTFValues3(nGroups, alphaSize);
        this.sendMTFValues4();
        this.sendMTFValues5(nGroups, nSelectors);
        this.sendMTFValues6(nGroups, alphaSize);
        this.sendMTFValues7();
    }
    
    private void sendMTFValues0(final int nGroups, final int alphaSize) {
        final byte[][] len = this.data.sendMTFValues_len;
        final int[] mtfFreq = this.data.mtfFreq;
        int remF = this.nMTF;
        int gs = 0;
        for (int nPart = nGroups; nPart > 0; --nPart) {
            final int tFreq = remF / nPart;
            int ge = gs - 1;
            int aFreq = 0;
            for (int a = alphaSize - 1; aFreq < tFreq && ge < a; aFreq += mtfFreq[++ge]) {}
            if (ge > gs && nPart != nGroups && nPart != 1 && (nGroups - nPart & 0x1) != 0x0) {
                aFreq -= mtfFreq[ge--];
            }
            final byte[] len_np = len[nPart - 1];
            int v = alphaSize;
            while (--v >= 0) {
                if (v >= gs && v <= ge) {
                    len_np[v] = 0;
                }
                else {
                    len_np[v] = 15;
                }
            }
            gs = ge + 1;
            remF -= aFreq;
        }
    }
    
    private int sendMTFValues1(final int nGroups, final int alphaSize) {
        final Data dataShadow = this.data;
        final int[][] rfreq = dataShadow.sendMTFValues_rfreq;
        final int[] fave = dataShadow.sendMTFValues_fave;
        final short[] cost = dataShadow.sendMTFValues_cost;
        final char[] sfmap = dataShadow.sfmap;
        final byte[] selector = dataShadow.selector;
        final byte[][] len = dataShadow.sendMTFValues_len;
        final byte[] len_0 = len[0];
        final byte[] len_2 = len[1];
        final byte[] len_3 = len[2];
        final byte[] len_4 = len[3];
        final byte[] len_5 = len[4];
        final byte[] len_6 = len[5];
        final int nMTFShadow = this.nMTF;
        int nSelectors = 0;
        for (int iter = 0; iter < 4; ++iter) {
            int t = nGroups;
            while (--t >= 0) {
                fave[t] = 0;
                final int[] rfreqt = rfreq[t];
                int i = alphaSize;
                while (--i >= 0) {
                    rfreqt[i] = 0;
                }
            }
            nSelectors = 0;
            int ge;
            for (int gs = 0; gs < this.nMTF; gs = ge + 1) {
                ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
                if (nGroups == 6) {
                    short cost2 = 0;
                    short cost3 = 0;
                    short cost4 = 0;
                    short cost5 = 0;
                    short cost6 = 0;
                    short cost7 = 0;
                    for (int j = gs; j <= ge; ++j) {
                        final int icv = sfmap[j];
                        cost2 += (short)(len_0[icv] & 0xFF);
                        cost3 += (short)(len_2[icv] & 0xFF);
                        cost4 += (short)(len_3[icv] & 0xFF);
                        cost5 += (short)(len_4[icv] & 0xFF);
                        cost6 += (short)(len_5[icv] & 0xFF);
                        cost7 += (short)(len_6[icv] & 0xFF);
                    }
                    cost[0] = cost2;
                    cost[1] = cost3;
                    cost[2] = cost4;
                    cost[3] = cost5;
                    cost[4] = cost6;
                    cost[5] = cost7;
                }
                else {
                    int t2 = nGroups;
                    while (--t2 >= 0) {
                        cost[t2] = 0;
                    }
                    for (int i = gs; i <= ge; ++i) {
                        final int icv2 = sfmap[i];
                        int t3 = nGroups;
                        while (--t3 >= 0) {
                            final short[] array = cost;
                            final int n = t3;
                            array[n] += (short)(len[t3][icv2] & 0xFF);
                        }
                    }
                }
                int bt = -1;
                int t4 = nGroups;
                int bc = 999999999;
                while (--t4 >= 0) {
                    final int cost_t = cost[t4];
                    if (cost_t < bc) {
                        bc = cost_t;
                        bt = t4;
                    }
                }
                final int[] array2 = fave;
                final int n2 = bt;
                ++array2[n2];
                selector[nSelectors] = (byte)bt;
                ++nSelectors;
                final int[] rfreq_bt = rfreq[bt];
                for (int k = gs; k <= ge; ++k) {
                    final int[] array3 = rfreq_bt;
                    final char c = sfmap[k];
                    ++array3[c];
                }
            }
            for (t = 0; t < nGroups; ++t) {
                hbMakeCodeLengths(len[t], rfreq[t], this.data, alphaSize, 20);
            }
        }
        return nSelectors;
    }
    
    private void sendMTFValues2(final int nGroups, final int nSelectors) {
        final Data dataShadow = this.data;
        final byte[] pos = dataShadow.sendMTFValues2_pos;
        int i = nGroups;
        while (--i >= 0) {
            pos[i] = (byte)i;
        }
        for (i = 0; i < nSelectors; ++i) {
            byte ll_i;
            byte tmp;
            int j;
            byte tmp2;
            for (ll_i = dataShadow.selector[i], tmp = pos[0], j = 0; ll_i != tmp; tmp = pos[j], pos[j] = tmp2) {
                ++j;
                tmp2 = tmp;
            }
            pos[0] = tmp;
            dataShadow.selectorMtf[i] = (byte)j;
        }
    }
    
    private void sendMTFValues3(final int nGroups, final int alphaSize) {
        final int[][] code = this.data.sendMTFValues_code;
        final byte[][] len = this.data.sendMTFValues_len;
        for (int t = 0; t < nGroups; ++t) {
            int minLen = 32;
            int maxLen = 0;
            final byte[] len_t = len[t];
            int i = alphaSize;
            while (--i >= 0) {
                final int l = len_t[i] & 0xFF;
                if (l > maxLen) {
                    maxLen = l;
                }
                if (l < minLen) {
                    minLen = l;
                }
            }
            hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
        }
    }
    
    private void sendMTFValues4() throws IOException {
        final boolean[] inUse = this.data.inUse;
        final boolean[] inUse2 = this.data.sentMTFValues4_inUse16;
        int i = 16;
        while (--i >= 0) {
            inUse2[i] = false;
            final int i2 = i * 16;
            int j = 16;
            while (--j >= 0) {
                if (inUse[i2 + j]) {
                    inUse2[i] = true;
                }
            }
        }
        for (i = 0; i < 16; ++i) {
            this.bsW(1, inUse2[i] ? 1 : 0);
        }
        final OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (int k = 0; k < 16; ++k) {
            if (inUse2[k]) {
                final int i3 = k * 16;
                for (int l = 0; l < 16; ++l) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    if (inUse[i3 + l]) {
                        bsBuffShadow |= 1 << 32 - bsLiveShadow - 1;
                    }
                    ++bsLiveShadow;
                }
            }
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }
    
    private void sendMTFValues5(final int nGroups, final int nSelectors) throws IOException {
        this.bsW(3, nGroups);
        this.bsW(15, nSelectors);
        final OutputStream outShadow = this.out;
        final byte[] selectorMtf = this.data.selectorMtf;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (int i = 0; i < nSelectors; ++i) {
            for (int j = 0, hj = selectorMtf[i] & 0xFF; j < hj; ++j) {
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                bsBuffShadow |= 1 << 32 - bsLiveShadow - 1;
                ++bsLiveShadow;
            }
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24);
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            ++bsLiveShadow;
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }
    
    private void sendMTFValues6(final int nGroups, final int alphaSize) throws IOException {
        final byte[][] len = this.data.sendMTFValues_len;
        final OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (final byte[] len_t : len) {
            int curr = len_t[0] & 0xFF;
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24);
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            bsBuffShadow |= curr << 32 - bsLiveShadow - 5;
            bsLiveShadow += 5;
            for (int i = 0; i < alphaSize; ++i) {
                int lti;
                for (lti = (len_t[i] & 0xFF); curr < lti; ++curr) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 2 << 32 - bsLiveShadow - 2;
                    bsLiveShadow += 2;
                }
                while (curr > lti) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 3 << 32 - bsLiveShadow - 2;
                    bsLiveShadow += 2;
                    --curr;
                }
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                ++bsLiveShadow;
            }
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }
    
    private void sendMTFValues7() throws IOException {
        final Data dataShadow = this.data;
        final byte[][] len = dataShadow.sendMTFValues_len;
        final int[][] code = dataShadow.sendMTFValues_code;
        final OutputStream outShadow = this.out;
        final byte[] selector = dataShadow.selector;
        final char[] sfmap = dataShadow.sfmap;
        final int nMTFShadow = this.nMTF;
        int selCtr = 0;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        int ge;
        for (int gs = 0; gs < nMTFShadow; gs = ge + 1, ++selCtr) {
            ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
            final int selector_selCtr = selector[selCtr] & 0xFF;
            final int[] code_selCtr = code[selector_selCtr];
            final byte[] len_selCtr = len[selector_selCtr];
            while (gs <= ge) {
                final int sfmap_i = sfmap[gs];
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                final int n = len_selCtr[sfmap_i] & 0xFF;
                bsBuffShadow |= code_selCtr[sfmap_i] << 32 - bsLiveShadow - n;
                bsLiveShadow += n;
                ++gs;
            }
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }
    
    private void moveToFrontCodeAndSend() throws IOException {
        this.bsW(24, this.data.origPtr);
        this.generateMTFValues();
        this.sendMTFValues();
    }
    
    private void blockSort() {
        this.blockSorter.blockSort(this.data, this.last);
    }
    
    private void generateMTFValues() {
        final int lastShadow = this.last;
        final Data dataShadow = this.data;
        final boolean[] inUse = dataShadow.inUse;
        final byte[] block = dataShadow.block;
        final int[] fmap = dataShadow.fmap;
        final char[] sfmap = dataShadow.sfmap;
        final int[] mtfFreq = dataShadow.mtfFreq;
        final byte[] unseqToSeq = dataShadow.unseqToSeq;
        final byte[] yy = dataShadow.generateMTFValues_yy;
        int nInUseShadow = 0;
        for (int i = 0; i < 256; ++i) {
            if (inUse[i]) {
                unseqToSeq[i] = (byte)nInUseShadow;
                ++nInUseShadow;
            }
        }
        this.nInUse = nInUseShadow;
        int j;
        int eob;
        for (eob = (j = nInUseShadow + 1); j >= 0; --j) {
            mtfFreq[j] = 0;
        }
        j = nInUseShadow;
        while (--j >= 0) {
            yy[j] = (byte)j;
        }
        int wr = 0;
        int zPend = 0;
        for (int k = 0; k <= lastShadow; ++k) {
            byte ll_i;
            byte tmp;
            int l;
            byte tmp2;
            for (ll_i = unseqToSeq[block[fmap[k]] & 0xFF], tmp = yy[0], l = 0; ll_i != tmp; tmp = yy[l], yy[l] = tmp2) {
                ++l;
                tmp2 = tmp;
            }
            yy[0] = tmp;
            if (l == 0) {
                ++zPend;
            }
            else {
                if (zPend > 0) {
                    --zPend;
                    while (true) {
                        if ((zPend & 0x1) == 0x0) {
                            sfmap[wr] = '\0';
                            ++wr;
                            final int[] array = mtfFreq;
                            final int n = 0;
                            ++array[n];
                        }
                        else {
                            sfmap[wr] = '\u0001';
                            ++wr;
                            final int[] array2 = mtfFreq;
                            final int n2 = 1;
                            ++array2[n2];
                        }
                        if (zPend < 2) {
                            break;
                        }
                        zPend = zPend - 2 >> 1;
                    }
                    zPend = 0;
                }
                sfmap[wr] = (char)(l + 1);
                ++wr;
                final int[] array3 = mtfFreq;
                final int n3 = l + 1;
                ++array3[n3];
            }
        }
        if (zPend > 0) {
            --zPend;
            while (true) {
                if ((zPend & 0x1) == 0x0) {
                    sfmap[wr] = '\0';
                    ++wr;
                    final int[] array4 = mtfFreq;
                    final int n4 = 0;
                    ++array4[n4];
                }
                else {
                    sfmap[wr] = '\u0001';
                    ++wr;
                    final int[] array5 = mtfFreq;
                    final int n5 = 1;
                    ++array5[n5];
                }
                if (zPend < 2) {
                    break;
                }
                zPend = zPend - 2 >> 1;
            }
        }
        sfmap[wr] = (char)eob;
        final int[] array6 = mtfFreq;
        final int n6 = eob;
        ++array6[n6];
        this.nMTF = wr + 1;
    }
    
    static final class Data
    {
        final boolean[] inUse;
        final byte[] unseqToSeq;
        final int[] mtfFreq;
        final byte[] selector;
        final byte[] selectorMtf;
        final byte[] generateMTFValues_yy;
        final byte[][] sendMTFValues_len;
        final int[][] sendMTFValues_rfreq;
        final int[] sendMTFValues_fave;
        final short[] sendMTFValues_cost;
        final int[][] sendMTFValues_code;
        final byte[] sendMTFValues2_pos;
        final boolean[] sentMTFValues4_inUse16;
        final int[] heap;
        final int[] weight;
        final int[] parent;
        final byte[] block;
        final int[] fmap;
        final char[] sfmap;
        int origPtr;
        
        Data(final int blockSize100k) {
            this.inUse = new boolean[256];
            this.unseqToSeq = new byte[256];
            this.mtfFreq = new int[258];
            this.selector = new byte[18002];
            this.selectorMtf = new byte[18002];
            this.generateMTFValues_yy = new byte[256];
            this.sendMTFValues_len = new byte[6][258];
            this.sendMTFValues_rfreq = new int[6][258];
            this.sendMTFValues_fave = new int[6];
            this.sendMTFValues_cost = new short[6];
            this.sendMTFValues_code = new int[6][258];
            this.sendMTFValues2_pos = new byte[6];
            this.sentMTFValues4_inUse16 = new boolean[16];
            this.heap = new int[260];
            this.weight = new int[516];
            this.parent = new int[516];
            final int n = blockSize100k * 100000;
            this.block = new byte[n + 1 + 20];
            this.fmap = new int[n];
            this.sfmap = new char[2 * n];
        }
    }
}
