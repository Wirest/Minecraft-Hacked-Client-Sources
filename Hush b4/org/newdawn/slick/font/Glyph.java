// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font;

import java.awt.font.GlyphMetrics;
import org.newdawn.slick.UnicodeFont;
import java.awt.font.GlyphVector;
import java.awt.Rectangle;
import org.newdawn.slick.Image;
import java.awt.Shape;

public class Glyph
{
    private int codePoint;
    private short width;
    private short height;
    private short yOffset;
    private boolean isMissing;
    private Shape shape;
    private Image image;
    
    public Glyph(final int codePoint, final Rectangle bounds, final GlyphVector vector, final int index, final UnicodeFont unicodeFont) {
        this.codePoint = codePoint;
        final GlyphMetrics metrics = vector.getGlyphMetrics(index);
        int lsb = (int)metrics.getLSB();
        if (lsb > 0) {
            lsb = 0;
        }
        int rsb = (int)metrics.getRSB();
        if (rsb > 0) {
            rsb = 0;
        }
        final int glyphWidth = bounds.width - lsb - rsb;
        final int glyphHeight = bounds.height;
        if (glyphWidth > 0 && glyphHeight > 0) {
            final int padTop = unicodeFont.getPaddingTop();
            final int padRight = unicodeFont.getPaddingRight();
            final int padBottom = unicodeFont.getPaddingBottom();
            final int padLeft = unicodeFont.getPaddingLeft();
            final int glyphSpacing = 1;
            this.width = (short)(glyphWidth + padLeft + padRight + glyphSpacing);
            this.height = (short)(glyphHeight + padTop + padBottom + glyphSpacing);
            this.yOffset = (short)(unicodeFont.getAscent() + bounds.y - padTop);
        }
        this.shape = vector.getGlyphOutline(index, (float)(-bounds.x + unicodeFont.getPaddingLeft()), (float)(-bounds.y + unicodeFont.getPaddingTop()));
        this.isMissing = !unicodeFont.getFont().canDisplay((char)codePoint);
    }
    
    public int getCodePoint() {
        return this.codePoint;
    }
    
    public boolean isMissing() {
        return this.isMissing;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Shape getShape() {
        return this.shape;
    }
    
    public void setShape(final Shape shape) {
        this.shape = shape;
    }
    
    public Image getImage() {
        return this.image;
    }
    
    public void setImage(final Image image) {
        this.image = image;
    }
    
    public int getYOffset() {
        return this.yOffset;
    }
}
