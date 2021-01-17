// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.nio.ByteBuffer;
import java.io.Serializable;

public final class Color implements ReadableColor, Serializable, WritableColor
{
    static final long serialVersionUID = 1L;
    private byte red;
    private byte green;
    private byte blue;
    private byte alpha;
    
    public Color() {
        this(0, 0, 0, 255);
    }
    
    public Color(final int r, final int g, final int b) {
        this(r, g, b, 255);
    }
    
    public Color(final byte r, final byte g, final byte b) {
        this(r, g, b, (byte)(-1));
    }
    
    public Color(final int r, final int g, final int b, final int a) {
        this.set(r, g, b, a);
    }
    
    public Color(final byte r, final byte g, final byte b, final byte a) {
        this.set(r, g, b, a);
    }
    
    public Color(final ReadableColor c) {
        this.setColor(c);
    }
    
    public void set(final int r, final int g, final int b, final int a) {
        this.red = (byte)r;
        this.green = (byte)g;
        this.blue = (byte)b;
        this.alpha = (byte)a;
    }
    
    public void set(final byte r, final byte g, final byte b, final byte a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }
    
    public void set(final int r, final int g, final int b) {
        this.set(r, g, b, 255);
    }
    
    public void set(final byte r, final byte g, final byte b) {
        this.set(r, g, b, (byte)(-1));
    }
    
    public int getRed() {
        return this.red & 0xFF;
    }
    
    public int getGreen() {
        return this.green & 0xFF;
    }
    
    public int getBlue() {
        return this.blue & 0xFF;
    }
    
    public int getAlpha() {
        return this.alpha & 0xFF;
    }
    
    public void setRed(final int red) {
        this.red = (byte)red;
    }
    
    public void setGreen(final int green) {
        this.green = (byte)green;
    }
    
    public void setBlue(final int blue) {
        this.blue = (byte)blue;
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = (byte)alpha;
    }
    
    public void setRed(final byte red) {
        this.red = red;
    }
    
    public void setGreen(final byte green) {
        this.green = green;
    }
    
    public void setBlue(final byte blue) {
        this.blue = blue;
    }
    
    public void setAlpha(final byte alpha) {
        this.alpha = alpha;
    }
    
    @Override
    public String toString() {
        return "Color [" + this.getRed() + ", " + this.getGreen() + ", " + this.getBlue() + ", " + this.getAlpha() + "]";
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof ReadableColor && ((ReadableColor)o).getRed() == this.getRed() && ((ReadableColor)o).getGreen() == this.getGreen() && ((ReadableColor)o).getBlue() == this.getBlue() && ((ReadableColor)o).getAlpha() == this.getAlpha();
    }
    
    @Override
    public int hashCode() {
        return this.red << 24 | this.green << 16 | this.blue << 8 | this.alpha;
    }
    
    public byte getAlphaByte() {
        return this.alpha;
    }
    
    public byte getBlueByte() {
        return this.blue;
    }
    
    public byte getGreenByte() {
        return this.green;
    }
    
    public byte getRedByte() {
        return this.red;
    }
    
    public void writeRGBA(final ByteBuffer dest) {
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
        dest.put(this.alpha);
    }
    
    public void writeRGB(final ByteBuffer dest) {
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
    }
    
    public void writeABGR(final ByteBuffer dest) {
        dest.put(this.alpha);
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
    }
    
    public void writeARGB(final ByteBuffer dest) {
        dest.put(this.alpha);
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
    }
    
    public void writeBGR(final ByteBuffer dest) {
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
    }
    
    public void writeBGRA(final ByteBuffer dest) {
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
        dest.put(this.alpha);
    }
    
    public void readRGBA(final ByteBuffer src) {
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
        this.alpha = src.get();
    }
    
    public void readRGB(final ByteBuffer src) {
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
    }
    
    public void readARGB(final ByteBuffer src) {
        this.alpha = src.get();
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
    }
    
    public void readBGRA(final ByteBuffer src) {
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
        this.alpha = src.get();
    }
    
    public void readBGR(final ByteBuffer src) {
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
    }
    
    public void readABGR(final ByteBuffer src) {
        this.alpha = src.get();
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
    }
    
    public void setColor(final ReadableColor src) {
        this.red = src.getRedByte();
        this.green = src.getGreenByte();
        this.blue = src.getBlueByte();
        this.alpha = src.getAlphaByte();
    }
    
    public void fromHSB(final float hue, final float saturation, final float brightness) {
        if (saturation == 0.0f) {
            final byte red = (byte)(brightness * 255.0f + 0.5f);
            this.blue = red;
            this.green = red;
            this.red = red;
        }
        else {
            final float f3 = (hue - (float)Math.floor(hue)) * 6.0f;
            final float f4 = f3 - (float)Math.floor(f3);
            final float f5 = brightness * (1.0f - saturation);
            final float f6 = brightness * (1.0f - saturation * f4);
            final float f7 = brightness * (1.0f - saturation * (1.0f - f4));
            switch ((int)f3) {
                case 0: {
                    this.red = (byte)(brightness * 255.0f + 0.5f);
                    this.green = (byte)(f7 * 255.0f + 0.5f);
                    this.blue = (byte)(f5 * 255.0f + 0.5f);
                    break;
                }
                case 1: {
                    this.red = (byte)(f6 * 255.0f + 0.5f);
                    this.green = (byte)(brightness * 255.0f + 0.5f);
                    this.blue = (byte)(f5 * 255.0f + 0.5f);
                    break;
                }
                case 2: {
                    this.red = (byte)(f5 * 255.0f + 0.5f);
                    this.green = (byte)(brightness * 255.0f + 0.5f);
                    this.blue = (byte)(f7 * 255.0f + 0.5f);
                    break;
                }
                case 3: {
                    this.red = (byte)(f5 * 255.0f + 0.5f);
                    this.green = (byte)(f6 * 255.0f + 0.5f);
                    this.blue = (byte)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 4: {
                    this.red = (byte)(f7 * 255.0f + 0.5f);
                    this.green = (byte)(f5 * 255.0f + 0.5f);
                    this.blue = (byte)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 5: {
                    this.red = (byte)(brightness * 255.0f + 0.5f);
                    this.green = (byte)(f5 * 255.0f + 0.5f);
                    this.blue = (byte)(f6 * 255.0f + 0.5f);
                    break;
                }
            }
        }
    }
    
    public float[] toHSB(float[] dest) {
        final int r = this.getRed();
        final int g = this.getGreen();
        final int b = this.getBlue();
        if (dest == null) {
            dest = new float[3];
        }
        int l = (r <= g) ? g : r;
        if (b > l) {
            l = b;
        }
        int i1 = (r >= g) ? g : r;
        if (b < i1) {
            i1 = b;
        }
        final float brightness = l / 255.0f;
        float saturation;
        if (l != 0) {
            saturation = (l - i1) / (float)l;
        }
        else {
            saturation = 0.0f;
        }
        float hue;
        if (saturation == 0.0f) {
            hue = 0.0f;
        }
        else {
            final float f3 = (l - r) / (float)(l - i1);
            final float f4 = (l - g) / (float)(l - i1);
            final float f5 = (l - b) / (float)(l - i1);
            if (r == l) {
                hue = f5 - f4;
            }
            else if (g == l) {
                hue = 2.0f + f3 - f5;
            }
            else {
                hue = 4.0f + f4 - f3;
            }
            hue /= 6.0f;
            if (hue < 0.0f) {
                ++hue;
            }
        }
        dest[0] = hue;
        dest[1] = saturation;
        dest[2] = brightness;
        return dest;
    }
}
