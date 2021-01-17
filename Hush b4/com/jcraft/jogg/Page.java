// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jogg;

public class Page
{
    private static int[] crc_lookup;
    public byte[] header_base;
    public int header;
    public int header_len;
    public byte[] body_base;
    public int body;
    public int body_len;
    
    private static int crc_entry(final int index) {
        int r = index << 24;
        for (int i = 0; i < 8; ++i) {
            if ((r & Integer.MIN_VALUE) != 0x0) {
                r = (r << 1 ^ 0x4C11DB7);
            }
            else {
                r <<= 1;
            }
        }
        return r & -1;
    }
    
    int version() {
        return this.header_base[this.header + 4] & 0xFF;
    }
    
    int continued() {
        return this.header_base[this.header + 5] & 0x1;
    }
    
    public int bos() {
        return this.header_base[this.header + 5] & 0x2;
    }
    
    public int eos() {
        return this.header_base[this.header + 5] & 0x4;
    }
    
    public long granulepos() {
        long foo = this.header_base[this.header + 13] & 0xFF;
        foo = (foo << 8 | (long)(this.header_base[this.header + 12] & 0xFF));
        foo = (foo << 8 | (long)(this.header_base[this.header + 11] & 0xFF));
        foo = (foo << 8 | (long)(this.header_base[this.header + 10] & 0xFF));
        foo = (foo << 8 | (long)(this.header_base[this.header + 9] & 0xFF));
        foo = (foo << 8 | (long)(this.header_base[this.header + 8] & 0xFF));
        foo = (foo << 8 | (long)(this.header_base[this.header + 7] & 0xFF));
        foo = (foo << 8 | (long)(this.header_base[this.header + 6] & 0xFF));
        return foo;
    }
    
    public int serialno() {
        return (this.header_base[this.header + 14] & 0xFF) | (this.header_base[this.header + 15] & 0xFF) << 8 | (this.header_base[this.header + 16] & 0xFF) << 16 | (this.header_base[this.header + 17] & 0xFF) << 24;
    }
    
    int pageno() {
        return (this.header_base[this.header + 18] & 0xFF) | (this.header_base[this.header + 19] & 0xFF) << 8 | (this.header_base[this.header + 20] & 0xFF) << 16 | (this.header_base[this.header + 21] & 0xFF) << 24;
    }
    
    void checksum() {
        int crc_reg = 0;
        for (int i = 0; i < this.header_len; ++i) {
            crc_reg = (crc_reg << 8 ^ Page.crc_lookup[(crc_reg >>> 24 & 0xFF) ^ (this.header_base[this.header + i] & 0xFF)]);
        }
        for (int i = 0; i < this.body_len; ++i) {
            crc_reg = (crc_reg << 8 ^ Page.crc_lookup[(crc_reg >>> 24 & 0xFF) ^ (this.body_base[this.body + i] & 0xFF)]);
        }
        this.header_base[this.header + 22] = (byte)crc_reg;
        this.header_base[this.header + 23] = (byte)(crc_reg >>> 8);
        this.header_base[this.header + 24] = (byte)(crc_reg >>> 16);
        this.header_base[this.header + 25] = (byte)(crc_reg >>> 24);
    }
    
    public Page copy() {
        return this.copy(new Page());
    }
    
    public Page copy(final Page p) {
        byte[] tmp = new byte[this.header_len];
        System.arraycopy(this.header_base, this.header, tmp, 0, this.header_len);
        p.header_len = this.header_len;
        p.header_base = tmp;
        p.header = 0;
        tmp = new byte[this.body_len];
        System.arraycopy(this.body_base, this.body, tmp, 0, this.body_len);
        p.body_len = this.body_len;
        p.body_base = tmp;
        p.body = 0;
        return p;
    }
    
    static {
        Page.crc_lookup = new int[256];
        for (int i = 0; i < Page.crc_lookup.length; ++i) {
            Page.crc_lookup[i] = crc_entry(i);
        }
    }
}
