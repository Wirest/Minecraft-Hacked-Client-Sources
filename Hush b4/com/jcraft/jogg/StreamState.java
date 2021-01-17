// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jogg;

public class StreamState
{
    byte[] body_data;
    int body_storage;
    int body_fill;
    private int body_returned;
    int[] lacing_vals;
    long[] granule_vals;
    int lacing_storage;
    int lacing_fill;
    int lacing_packet;
    int lacing_returned;
    byte[] header;
    int header_fill;
    public int e_o_s;
    int b_o_s;
    int serialno;
    int pageno;
    long packetno;
    long granulepos;
    
    public StreamState() {
        this.header = new byte[282];
        this.init();
    }
    
    StreamState(final int serialno) {
        this();
        this.init(serialno);
    }
    
    void init() {
        this.body_storage = 16384;
        this.body_data = new byte[this.body_storage];
        this.lacing_storage = 1024;
        this.lacing_vals = new int[this.lacing_storage];
        this.granule_vals = new long[this.lacing_storage];
    }
    
    public void init(final int serialno) {
        if (this.body_data == null) {
            this.init();
        }
        else {
            for (int i = 0; i < this.body_data.length; ++i) {
                this.body_data[i] = 0;
            }
            for (int i = 0; i < this.lacing_vals.length; ++i) {
                this.lacing_vals[i] = 0;
            }
            for (int i = 0; i < this.granule_vals.length; ++i) {
                this.granule_vals[i] = 0L;
            }
        }
        this.serialno = serialno;
    }
    
    public void clear() {
        this.body_data = null;
        this.lacing_vals = null;
        this.granule_vals = null;
    }
    
    void destroy() {
        this.clear();
    }
    
    void body_expand(final int needed) {
        if (this.body_storage <= this.body_fill + needed) {
            this.body_storage += needed + 1024;
            final byte[] foo = new byte[this.body_storage];
            System.arraycopy(this.body_data, 0, foo, 0, this.body_data.length);
            this.body_data = foo;
        }
    }
    
    void lacing_expand(final int needed) {
        if (this.lacing_storage <= this.lacing_fill + needed) {
            this.lacing_storage += needed + 32;
            final int[] foo = new int[this.lacing_storage];
            System.arraycopy(this.lacing_vals, 0, foo, 0, this.lacing_vals.length);
            this.lacing_vals = foo;
            final long[] bar = new long[this.lacing_storage];
            System.arraycopy(this.granule_vals, 0, bar, 0, this.granule_vals.length);
            this.granule_vals = bar;
        }
    }
    
    public int packetin(final Packet op) {
        final int lacing_val = op.bytes / 255 + 1;
        if (this.body_returned != 0) {
            this.body_fill -= this.body_returned;
            if (this.body_fill != 0) {
                System.arraycopy(this.body_data, this.body_returned, this.body_data, 0, this.body_fill);
            }
            this.body_returned = 0;
        }
        this.body_expand(op.bytes);
        this.lacing_expand(lacing_val);
        System.arraycopy(op.packet_base, op.packet, this.body_data, this.body_fill, op.bytes);
        this.body_fill += op.bytes;
        int j;
        for (j = 0; j < lacing_val - 1; ++j) {
            this.lacing_vals[this.lacing_fill + j] = 255;
            this.granule_vals[this.lacing_fill + j] = this.granulepos;
        }
        this.lacing_vals[this.lacing_fill + j] = op.bytes % 255;
        final long[] granule_vals = this.granule_vals;
        final int n = this.lacing_fill + j;
        final long granulepos = op.granulepos;
        granule_vals[n] = granulepos;
        this.granulepos = granulepos;
        final int[] lacing_vals = this.lacing_vals;
        final int lacing_fill = this.lacing_fill;
        lacing_vals[lacing_fill] |= 0x100;
        this.lacing_fill += lacing_val;
        ++this.packetno;
        if (op.e_o_s != 0) {
            this.e_o_s = 1;
        }
        return 0;
    }
    
    public int packetout(final Packet op) {
        int ptr = this.lacing_returned;
        if (this.lacing_packet <= ptr) {
            return 0;
        }
        if ((this.lacing_vals[ptr] & 0x400) != 0x0) {
            ++this.lacing_returned;
            ++this.packetno;
            return -1;
        }
        int size = this.lacing_vals[ptr] & 0xFF;
        int bytes = 0;
        op.packet_base = this.body_data;
        op.packet = this.body_returned;
        op.e_o_s = (this.lacing_vals[ptr] & 0x200);
        op.b_o_s = (this.lacing_vals[ptr] & 0x100);
        bytes += size;
        while (size == 255) {
            final int val = this.lacing_vals[++ptr];
            size = (val & 0xFF);
            if ((val & 0x200) != 0x0) {
                op.e_o_s = 512;
            }
            bytes += size;
        }
        op.packetno = this.packetno;
        op.granulepos = this.granule_vals[ptr];
        op.bytes = bytes;
        this.body_returned += bytes;
        this.lacing_returned = ptr + 1;
        ++this.packetno;
        return 1;
    }
    
    public int pagein(final Page og) {
        final byte[] header_base = og.header_base;
        final int header = og.header;
        final byte[] body_base = og.body_base;
        int body = og.body;
        int bodysize = og.body_len;
        int segptr = 0;
        final int version = og.version();
        final int continued = og.continued();
        int bos = og.bos();
        final int eos = og.eos();
        final long granulepos = og.granulepos();
        final int _serialno = og.serialno();
        final int _pageno = og.pageno();
        final int segments = header_base[header + 26] & 0xFF;
        final int lr = this.lacing_returned;
        final int br = this.body_returned;
        if (br != 0) {
            this.body_fill -= br;
            if (this.body_fill != 0) {
                System.arraycopy(this.body_data, br, this.body_data, 0, this.body_fill);
            }
            this.body_returned = 0;
        }
        if (lr != 0) {
            if (this.lacing_fill - lr != 0) {
                System.arraycopy(this.lacing_vals, lr, this.lacing_vals, 0, this.lacing_fill - lr);
                System.arraycopy(this.granule_vals, lr, this.granule_vals, 0, this.lacing_fill - lr);
            }
            this.lacing_fill -= lr;
            this.lacing_packet -= lr;
            this.lacing_returned = 0;
        }
        if (_serialno != this.serialno) {
            return -1;
        }
        if (version > 0) {
            return -1;
        }
        this.lacing_expand(segments + 1);
        if (_pageno != this.pageno) {
            for (int i = this.lacing_packet; i < this.lacing_fill; ++i) {
                this.body_fill -= (this.lacing_vals[i] & 0xFF);
            }
            this.lacing_fill = this.lacing_packet;
            if (this.pageno != -1) {
                this.lacing_vals[this.lacing_fill++] = 1024;
                ++this.lacing_packet;
            }
            if (continued != 0) {
                bos = 0;
                while (segptr < segments) {
                    final int val = header_base[header + 27 + segptr] & 0xFF;
                    body += val;
                    bodysize -= val;
                    if (val < 255) {
                        ++segptr;
                        break;
                    }
                    ++segptr;
                }
            }
        }
        if (bodysize != 0) {
            this.body_expand(bodysize);
            System.arraycopy(body_base, body, this.body_data, this.body_fill, bodysize);
            this.body_fill += bodysize;
        }
        int saved = -1;
        while (segptr < segments) {
            final int val = header_base[header + 27 + segptr] & 0xFF;
            this.lacing_vals[this.lacing_fill] = val;
            this.granule_vals[this.lacing_fill] = -1L;
            if (bos != 0) {
                final int[] lacing_vals = this.lacing_vals;
                final int lacing_fill = this.lacing_fill;
                lacing_vals[lacing_fill] |= 0x100;
                bos = 0;
            }
            if (val < 255) {
                saved = this.lacing_fill;
            }
            ++this.lacing_fill;
            ++segptr;
            if (val < 255) {
                this.lacing_packet = this.lacing_fill;
            }
        }
        if (saved != -1) {
            this.granule_vals[saved] = granulepos;
        }
        if (eos != 0) {
            this.e_o_s = 1;
            if (this.lacing_fill > 0) {
                final int[] lacing_vals2 = this.lacing_vals;
                final int n = this.lacing_fill - 1;
                lacing_vals2[n] |= 0x200;
            }
        }
        this.pageno = _pageno + 1;
        return 0;
    }
    
    public int flush(final Page og) {
        int vals = 0;
        final int maxvals = (this.lacing_fill > 255) ? 255 : this.lacing_fill;
        int bytes = 0;
        int acc = 0;
        long granule_pos = this.granule_vals[0];
        if (maxvals == 0) {
            return 0;
        }
        if (this.b_o_s == 0) {
            granule_pos = 0L;
            for (vals = 0; vals < maxvals; ++vals) {
                if ((this.lacing_vals[vals] & 0xFF) < 255) {
                    ++vals;
                    break;
                }
            }
        }
        else {
            for (vals = 0; vals < maxvals; ++vals) {
                if (acc > 4096) {
                    break;
                }
                acc += (this.lacing_vals[vals] & 0xFF);
                granule_pos = this.granule_vals[vals];
            }
        }
        System.arraycopy("OggS".getBytes(), 0, this.header, 0, 4);
        this.header[4] = 0;
        this.header[5] = 0;
        if ((this.lacing_vals[0] & 0x100) == 0x0) {
            final byte[] header = this.header;
            final int n = 5;
            header[n] |= 0x1;
        }
        if (this.b_o_s == 0) {
            final byte[] header2 = this.header;
            final int n2 = 5;
            header2[n2] |= 0x2;
        }
        if (this.e_o_s != 0 && this.lacing_fill == vals) {
            final byte[] header3 = this.header;
            final int n3 = 5;
            header3[n3] |= 0x4;
        }
        this.b_o_s = 1;
        for (int i = 6; i < 14; ++i) {
            this.header[i] = (byte)granule_pos;
            granule_pos >>>= 8;
        }
        int _serialno = this.serialno;
        for (int i = 14; i < 18; ++i) {
            this.header[i] = (byte)_serialno;
            _serialno >>>= 8;
        }
        if (this.pageno == -1) {
            this.pageno = 0;
        }
        int _pageno = this.pageno++;
        for (int i = 18; i < 22; ++i) {
            this.header[i] = (byte)_pageno;
            _pageno >>>= 8;
        }
        this.header[22] = 0;
        this.header[23] = 0;
        this.header[24] = 0;
        this.header[25] = 0;
        this.header[26] = (byte)vals;
        for (int i = 0; i < vals; ++i) {
            this.header[i + 27] = (byte)this.lacing_vals[i];
            bytes += (this.header[i + 27] & 0xFF);
        }
        og.header_base = this.header;
        og.header = 0;
        final int n4 = vals + 27;
        this.header_fill = n4;
        og.header_len = n4;
        og.body_base = this.body_data;
        og.body = this.body_returned;
        og.body_len = bytes;
        this.lacing_fill -= vals;
        System.arraycopy(this.lacing_vals, vals, this.lacing_vals, 0, this.lacing_fill * 4);
        System.arraycopy(this.granule_vals, vals, this.granule_vals, 0, this.lacing_fill * 8);
        this.body_returned += bytes;
        og.checksum();
        return 1;
    }
    
    public int pageout(final Page og) {
        if ((this.e_o_s != 0 && this.lacing_fill != 0) || this.body_fill - this.body_returned > 4096 || this.lacing_fill >= 255 || (this.lacing_fill != 0 && this.b_o_s == 0)) {
            return this.flush(og);
        }
        return 0;
    }
    
    public int eof() {
        return this.e_o_s;
    }
    
    public int reset() {
        this.body_fill = 0;
        this.body_returned = 0;
        this.lacing_fill = 0;
        this.lacing_packet = 0;
        this.lacing_returned = 0;
        this.header_fill = 0;
        this.e_o_s = 0;
        this.b_o_s = 0;
        this.pageno = -1;
        this.packetno = 0L;
        this.granulepos = 0L;
        return 0;
    }
}
