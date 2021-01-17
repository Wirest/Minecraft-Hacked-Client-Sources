// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyFont
{
    static TinyHash a;
    public int horizAdvX;
    public TinyString fontFamily;
    public int unitsPerEm;
    public int ascent;
    public int descent;
    public int baseline;
    public TinyHash glyphs;
    public TinyGlyph missing_glyph;
    
    public TinyFont() {
        this.fontFamily = new TinyString(new char[] { 'H', 'e', 'l', 'v', 'e', 't', 'i', 'c', 'a' });
        this.unitsPerEm = 2048;
        this.ascent = 1024;
        this.descent = 0;
        this.baseline = 0;
        this.horizAdvX = 1024;
        this.glyphs = new TinyHash(0, 256);
    }
    
    public TinyMatrix charToUserTransform(final TinyPath tinyPath, final int n, final int n2, final int n3, final int n4) {
        if (tinyPath == null) {
            return null;
        }
        final TinyMatrix tinyMatrix = new TinyMatrix();
        tinyMatrix.translate(n2, n3);
        final TinyMatrix tinyMatrix2 = new TinyMatrix();
        tinyMatrix2.a = (n << 8) / this.unitsPerEm;
        tinyMatrix2.d = -tinyMatrix2.a;
        tinyMatrix2.concatenate(tinyMatrix);
        final TinyRect bBox = tinyPath.getBBox();
        if (n4 != 1) {
            int n5 = bBox.xmax - bBox.xmin;
            if (n4 == 2) {
                n5 /= 2;
            }
            tinyMatrix.translate(-n5, 0);
            tinyMatrix2.preConcatenate(tinyMatrix);
        }
        return tinyMatrix2;
    }
    
    public static void addFont(final TinyFont tinyFont) {
        TinyFont.a.put(tinyFont.fontFamily, tinyFont);
    }
    
    public static TinyFont getFont(final TinyString tinyString) {
        return (TinyFont)TinyFont.a.get(tinyString);
    }
    
    static {
        TinyFont.a = new TinyHash(1, 11);
    }
}
