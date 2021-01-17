// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jogg;

public class Buffer
{
    private static final int BUFFER_INCREMENT = 256;
    private static final int[] mask;
    int ptr;
    byte[] buffer;
    int endbit;
    int endbyte;
    int storage;
    
    public Buffer() {
        this.ptr = 0;
        this.buffer = null;
        this.endbit = 0;
        this.endbyte = 0;
        this.storage = 0;
    }
    
    public void writeinit() {
        this.buffer = new byte[256];
        this.ptr = 0;
        this.buffer[0] = 0;
        this.storage = 256;
    }
    
    public void write(final byte[] s) {
        for (int i = 0; i < s.length && s[i] != 0; ++i) {
            this.write(s[i], 8);
        }
    }
    
    public void read(final byte[] s, int bytes) {
        int i = 0;
        while (bytes-- != 0) {
            s[i++] = (byte)this.read(8);
        }
    }
    
    void reset() {
        this.ptr = 0;
        this.buffer[0] = 0;
        final int n = 0;
        this.endbyte = n;
        this.endbit = n;
    }
    
    public void writeclear() {
        this.buffer = null;
    }
    
    public void readinit(final byte[] buf, final int bytes) {
        this.readinit(buf, 0, bytes);
    }
    
    public void readinit(final byte[] buf, final int start, final int bytes) {
        this.ptr = start;
        this.buffer = buf;
        final int n = 0;
        this.endbyte = n;
        this.endbit = n;
        this.storage = bytes;
    }
    
    public void write(int value, int bits) {
        if (this.endbyte + 4 >= this.storage) {
            final byte[] foo = new byte[this.storage + 256];
            System.arraycopy(this.buffer, 0, foo, 0, this.storage);
            this.buffer = foo;
            this.storage += 256;
        }
        value &= Buffer.mask[bits];
        bits += this.endbit;
        final byte[] buffer = this.buffer;
        final int ptr = this.ptr;
        buffer[ptr] |= (byte)(value << this.endbit);
        if (bits >= 8) {
            this.buffer[this.ptr + 1] = (byte)(value >>> 8 - this.endbit);
            if (bits >= 16) {
                this.buffer[this.ptr + 2] = (byte)(value >>> 16 - this.endbit);
                if (bits >= 24) {
                    this.buffer[this.ptr + 3] = (byte)(value >>> 24 - this.endbit);
                    if (bits >= 32) {
                        if (this.endbit > 0) {
                            this.buffer[this.ptr + 4] = (byte)(value >>> 32 - this.endbit);
                        }
                        else {
                            this.buffer[this.ptr + 4] = 0;
                        }
                    }
                }
            }
        }
        this.endbyte += bits / 8;
        this.ptr += bits / 8;
        this.endbit = (bits & 0x7);
    }
    
    public int look(int bits) {
        final int m = Buffer.mask[bits];
        bits += this.endbit;
        if (this.endbyte + 4 >= this.storage && this.endbyte + (bits - 1) / 8 >= this.storage) {
            return -1;
        }
        int ret = (this.buffer[this.ptr] & 0xFF) >>> this.endbit;
        if (bits > 8) {
            ret |= (this.buffer[this.ptr + 1] & 0xFF) << 8 - this.endbit;
            if (bits > 16) {
                ret |= (this.buffer[this.ptr + 2] & 0xFF) << 16 - this.endbit;
                if (bits > 24) {
                    ret |= (this.buffer[this.ptr + 3] & 0xFF) << 24 - this.endbit;
                    if (bits > 32 && this.endbit != 0) {
                        ret |= (this.buffer[this.ptr + 4] & 0xFF) << 32 - this.endbit;
                    }
                }
            }
        }
        return m & ret;
    }
    
    public int look1() {
        if (this.endbyte >= this.storage) {
            return -1;
        }
        return this.buffer[this.ptr] >> this.endbit & 0x1;
    }
    
    public void adv(int bits) {
        bits += this.endbit;
        this.ptr += bits / 8;
        this.endbyte += bits / 8;
        this.endbit = (bits & 0x7);
    }
    
    public void adv1() {
        ++this.endbit;
        if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
        }
    }
    
    public int read(int bits) {
        final int m = Buffer.mask[bits];
        bits += this.endbit;
        if (this.endbyte + 4 >= this.storage) {
            final int ret = -1;
            if (this.endbyte + (bits - 1) / 8 >= this.storage) {
                this.ptr += bits / 8;
                this.endbyte += bits / 8;
                this.endbit = (bits & 0x7);
                return ret;
            }
        }
        int ret = (this.buffer[this.ptr] & 0xFF) >>> this.endbit;
        if (bits > 8) {
            ret |= (this.buffer[this.ptr + 1] & 0xFF) << 8 - this.endbit;
            if (bits > 16) {
                ret |= (this.buffer[this.ptr + 2] & 0xFF) << 16 - this.endbit;
                if (bits > 24) {
                    ret |= (this.buffer[this.ptr + 3] & 0xFF) << 24 - this.endbit;
                    if (bits > 32 && this.endbit != 0) {
                        ret |= (this.buffer[this.ptr + 4] & 0xFF) << 32 - this.endbit;
                    }
                }
            }
        }
        ret &= m;
        this.ptr += bits / 8;
        this.endbyte += bits / 8;
        this.endbit = (bits & 0x7);
        return ret;
    }
    
    public int readB(int bits) {
        final int m = 32 - bits;
        bits += this.endbit;
        if (this.endbyte + 4 >= this.storage) {
            final int ret = -1;
            if (this.endbyte * 8 + bits > this.storage * 8) {
                this.ptr += bits / 8;
                this.endbyte += bits / 8;
                this.endbit = (bits & 0x7);
                return ret;
            }
        }
        int ret = (this.buffer[this.ptr] & 0xFF) << 24 + this.endbit;
        if (bits > 8) {
            ret |= (this.buffer[this.ptr + 1] & 0xFF) << 16 + this.endbit;
            if (bits > 16) {
                ret |= (this.buffer[this.ptr + 2] & 0xFF) << 8 + this.endbit;
                if (bits > 24) {
                    ret |= (this.buffer[this.ptr + 3] & 0xFF) << this.endbit;
                    if (bits > 32 && this.endbit != 0) {
                        ret |= (this.buffer[this.ptr + 4] & 0xFF) >> 8 - this.endbit;
                    }
                }
            }
        }
        ret = ret >>> (m >> 1) >>> (m + 1 >> 1);
        this.ptr += bits / 8;
        this.endbyte += bits / 8;
        this.endbit = (bits & 0x7);
        return ret;
    }
    
    public int read1() {
        if (this.endbyte >= this.storage) {
            final int ret = -1;
            ++this.endbit;
            if (this.endbit > 7) {
                this.endbit = 0;
                ++this.ptr;
                ++this.endbyte;
            }
            return ret;
        }
        final int ret = this.buffer[this.ptr] >> this.endbit & 0x1;
        ++this.endbit;
        if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
        }
        return ret;
    }
    
    public int bytes() {
        return this.endbyte + (this.endbit + 7) / 8;
    }
    
    public int bits() {
        return this.endbyte * 8 + this.endbit;
    }
    
    public byte[] buffer() {
        return this.buffer;
    }
    
    public static int ilog(int v) {
        int ret = 0;
        while (v > 0) {
            ++ret;
            v >>>= 1;
        }
        return ret;
    }
    
    public static void report(final String in) {
        System.err.println(in);
        System.exit(1);
    }
    
    static {
        mask = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1 };
    }
}
