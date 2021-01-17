// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyPixbuf
{
    private TinyPoint a;
    public int width;
    public int height;
    public int[] pixels32;
    public int pixelSize;
    public int pixelOffset;
    
    public TinyPixbuf(final int width, final int height) {
        this.a = new TinyPoint();
        this.width = width;
        this.height = height;
        this.pixelSize = width * height;
        this.pixels32 = new int[this.pixelSize];
    }
    
    void a(final int n, final TinyRect tinyRect) {
        final int n2 = tinyRect.xmax - tinyRect.xmin;
        int n3 = tinyRect.ymax - tinyRect.ymin;
        int n4 = tinyRect.ymin * this.width;
        final int[] pixels32 = this.pixels32;
        while (n3-- > 0) {
            int n5 = tinyRect.xmin + n4;
            int n6 = n2;
            while (n6-- > 0) {
                pixels32[n5++] = n;
            }
            n4 += this.width;
        }
    }
    
    void a(final TinyColor tinyColor, int n, final int n2, final int n3, final int n4) {
        int i = n3 - n2;
        int n5 = n2 + this.pixelOffset;
        switch (tinyColor.fillType) {
            case 0: {
                final int value = tinyColor.value;
                final int n6 = value >>> 24 & 0xFF;
                if (n6 != 255) {
                    n = n * n6 >> 8;
                }
                if (n == 255) {
                    while (i-- > 0) {
                        this.pixels32[n5++] = value;
                    }
                }
                else if (n != 0) {
                    while (i > 0) {
                        final int n7 = this.pixels32[n5];
                        if (n7 == 0) {
                            this.pixels32[n5] = (n << 24) + (value & 0xFFFFFF);
                        }
                        else {
                            final int n8 = n7 >> 16 & 0xFF;
                            final int n9 = n7 >> 8 & 0xFF;
                            final int n10 = n7 & 0xFF;
                            this.pixels32[n5] = -16777216 + (n8 + (((value >> 16 & 0xFF) - n8) * n + 128 >> 8) << 16) + (n9 + (((value >> 8 & 0xFF) - n9) * n + 128 >> 8) << 8) + (n10 + (((value & 0xFF) - n10) * n + 128 >> 8));
                        }
                        ++n5;
                        --i;
                    }
                }
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                final int n11 = n;
                int n12 = 0;
                final TinyPoint a = this.a;
                final TinyPoint a2 = this.a;
                final int n13 = 0;
                a2.y = n13;
                a.x = n13;
                final TinyMatrix try1 = tinyColor.try;
                while (i > 0) {
                    this.a.x = n2 + n12 << 8;
                    this.a.y = n4 << 8;
                    try1.transform(this.a);
                    final int a3 = tinyColor.a(this.a.x, this.a.y);
                    final int n14 = a3 >>> 24 & 0xFF;
                    n = n11;
                    if (n14 != 255) {
                        n = n * n14 >> 8;
                    }
                    if (n == 255) {
                        this.pixels32[n5] = a3;
                    }
                    else if (n != 0) {
                        final int n15 = this.pixels32[n5];
                        if (n15 == 0) {
                            this.pixels32[n5] = (n << 24) + (a3 & 0xFFFFFF);
                        }
                        else {
                            final int n16 = n15 >> 16 & 0xFF;
                            final int n17 = n15 >> 8 & 0xFF;
                            final int n18 = n15 & 0xFF;
                            this.pixels32[n5] = -16777216 + (n16 + (((a3 >> 16 & 0xFF) - n16) * n + 128 >> 8) << 16) + (n17 + (((a3 >> 8 & 0xFF) - n17) * n + 128 >> 8) << 8) + (n18 + (((a3 & 0xFF) - n18) * n + 128 >> 8));
                        }
                    }
                    ++n12;
                    ++n5;
                    --i;
                }
                break;
            }
        }
    }
}
