// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.nio.FloatBuffer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import java.io.Serializable;

public class Color implements Serializable
{
    private static final long serialVersionUID = 1393939L;
    protected transient SGL GL;
    public static final Color transparent;
    public static final Color white;
    public static final Color yellow;
    public static final Color red;
    public static final Color blue;
    public static final Color green;
    public static final Color black;
    public static final Color gray;
    public static final Color cyan;
    public static final Color darkGray;
    public static final Color lightGray;
    public static final Color pink;
    public static final Color orange;
    public static final Color magenta;
    public float r;
    public float g;
    public float b;
    public float a;
    
    static {
        transparent = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        white = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        yellow = new Color(1.0f, 1.0f, 0.0f, 1.0f);
        red = new Color(1.0f, 0.0f, 0.0f, 1.0f);
        blue = new Color(0.0f, 0.0f, 1.0f, 1.0f);
        green = new Color(0.0f, 1.0f, 0.0f, 1.0f);
        black = new Color(0.0f, 0.0f, 0.0f, 1.0f);
        gray = new Color(0.5f, 0.5f, 0.5f, 1.0f);
        cyan = new Color(0.0f, 1.0f, 1.0f, 1.0f);
        darkGray = new Color(0.3f, 0.3f, 0.3f, 1.0f);
        lightGray = new Color(0.7f, 0.7f, 0.7f, 1.0f);
        pink = new Color(255, 175, 175, 255);
        orange = new Color(255, 200, 0, 255);
        magenta = new Color(255, 0, 255, 255);
    }
    
    public Color(final Color color) {
        this.GL = Renderer.get();
        this.a = 1.0f;
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }
    
    public Color(final FloatBuffer buffer) {
        this.GL = Renderer.get();
        this.a = 1.0f;
        this.r = buffer.get();
        this.g = buffer.get();
        this.b = buffer.get();
        this.a = buffer.get();
    }
    
    public Color(final float r, final float g, final float b) {
        this.GL = Renderer.get();
        this.a = 1.0f;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0f;
    }
    
    public Color(final float r, final float g, final float b, final float a) {
        this.GL = Renderer.get();
        this.a = 1.0f;
        this.r = Math.min(r, 1.0f);
        this.g = Math.min(g, 1.0f);
        this.b = Math.min(b, 1.0f);
        this.a = Math.min(a, 1.0f);
    }
    
    public Color(final int r, final int g, final int b) {
        this.GL = Renderer.get();
        this.a = 1.0f;
        this.r = r / 255.0f;
        this.g = g / 255.0f;
        this.b = b / 255.0f;
        this.a = 1.0f;
    }
    
    public Color(final int r, final int g, final int b, final int a) {
        this.GL = Renderer.get();
        this.a = 1.0f;
        this.r = r / 255.0f;
        this.g = g / 255.0f;
        this.b = b / 255.0f;
        this.a = a / 255.0f;
    }
    
    public Color(final int value) {
        this.GL = Renderer.get();
        this.a = 1.0f;
        final int r = (value & 0xFF0000) >> 16;
        final int g = (value & 0xFF00) >> 8;
        final int b = value & 0xFF;
        int a = (value & 0xFF000000) >> 24;
        if (a < 0) {
            a += 256;
        }
        if (a == 0) {
            a = 255;
        }
        this.r = r / 255.0f;
        this.g = g / 255.0f;
        this.b = b / 255.0f;
        this.a = a / 255.0f;
    }
    
    public static Color decode(final String nm) {
        return new Color(Integer.decode(nm));
    }
    
    public void bind() {
        this.GL.glColor4f(this.r, this.g, this.b, this.a);
    }
    
    @Override
    public int hashCode() {
        return (int)(this.r + this.g + this.b + this.a) * 255;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Color) {
            final Color o = (Color)other;
            return o.r == this.r && o.g == this.g && o.b == this.b && o.a == this.a;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Color (" + this.r + "," + this.g + "," + this.b + "," + this.a + ")";
    }
    
    public Color darker() {
        return this.darker(0.5f);
    }
    
    public Color darker(float scale) {
        scale = 1.0f - scale;
        final Color temp = new Color(this.r * scale, this.g * scale, this.b * scale, this.a);
        return temp;
    }
    
    public Color brighter() {
        return this.brighter(0.2f);
    }
    
    public int getRed() {
        return (int)(this.r * 255.0f);
    }
    
    public int getGreen() {
        return (int)(this.g * 255.0f);
    }
    
    public int getBlue() {
        return (int)(this.b * 255.0f);
    }
    
    public int getAlpha() {
        return (int)(this.a * 255.0f);
    }
    
    public int getRedByte() {
        return (int)(this.r * 255.0f);
    }
    
    public int getGreenByte() {
        return (int)(this.g * 255.0f);
    }
    
    public int getBlueByte() {
        return (int)(this.b * 255.0f);
    }
    
    public int getAlphaByte() {
        return (int)(this.a * 255.0f);
    }
    
    public Color brighter(float scale) {
        ++scale;
        final Color temp = new Color(this.r * scale, this.g * scale, this.b * scale, this.a);
        return temp;
    }
    
    public Color multiply(final Color c) {
        return new Color(this.r * c.r, this.g * c.g, this.b * c.b, this.a * c.a);
    }
    
    public void add(final Color c) {
        this.r += c.r;
        this.g += c.g;
        this.b += c.b;
        this.a += c.a;
    }
    
    public void scale(final float value) {
        this.r *= value;
        this.g *= value;
        this.b *= value;
        this.a *= value;
    }
    
    public Color addToCopy(final Color c) {
        final Color color;
        final Color copy = color = new Color(this.r, this.g, this.b, this.a);
        color.r += c.r;
        final Color color2 = copy;
        color2.g += c.g;
        final Color color3 = copy;
        color3.b += c.b;
        final Color color4 = copy;
        color4.a += c.a;
        return copy;
    }
    
    public Color scaleCopy(final float value) {
        final Color color;
        final Color copy = color = new Color(this.r, this.g, this.b, this.a);
        color.r *= value;
        final Color color2 = copy;
        color2.g *= value;
        final Color color3 = copy;
        color3.b *= value;
        final Color color4 = copy;
        color4.a *= value;
        return copy;
    }
}
