// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyBitmap
{
    public int width;
    public int height;
    public int[] pixels32;
    public boolean loaded;
    
    public TinyBitmap() {
    }
    
    public TinyBitmap(final TinyBitmap tinyBitmap) {
        if (tinyBitmap != null) {
            this.width = tinyBitmap.width;
            this.height = tinyBitmap.height;
            this.loaded = tinyBitmap.loaded;
            if (tinyBitmap.pixels32 != null) {
                final int n = this.width * this.height;
                this.pixels32 = new int[n];
                System.arraycopy(tinyBitmap.pixels32, 0, this.pixels32, 0, n);
            }
        }
    }
    
    int a(int n, int n2, final boolean b) {
        if (n < 0) {
            n = 0;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 >= this.height) {
            if (b) {
                n2 = this.height - 1;
            }
            else {
                n2 %= this.height;
            }
        }
        if (n >= this.width) {
            if (b) {
                n = this.width - 1;
            }
            else {
                n %= this.width;
            }
        }
        return this.pixels32[n2 * this.width + n];
    }
}
