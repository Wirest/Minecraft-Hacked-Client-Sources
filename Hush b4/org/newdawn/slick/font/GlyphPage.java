// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font;

import java.util.ListIterator;
import java.awt.image.WritableRaster;
import java.awt.Shape;
import org.newdawn.slick.font.effects.Effect;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.util.Iterator;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import java.util.ArrayList;
import java.awt.RenderingHints;
import java.nio.ByteOrder;
import org.newdawn.slick.opengl.renderer.Renderer;
import java.util.List;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import java.awt.font.FontRenderContext;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.newdawn.slick.opengl.renderer.SGL;

public class GlyphPage
{
    private static final SGL GL;
    public static final int MAX_GLYPH_SIZE = 256;
    private static ByteBuffer scratchByteBuffer;
    private static IntBuffer scratchIntBuffer;
    private static BufferedImage scratchImage;
    private static Graphics2D scratchGraphics;
    public static FontRenderContext renderContext;
    private final UnicodeFont unicodeFont;
    private final int pageWidth;
    private final int pageHeight;
    private final Image pageImage;
    private int pageX;
    private int pageY;
    private int rowHeight;
    private boolean orderAscending;
    private final List pageGlyphs;
    
    static {
        GL = Renderer.get();
        (GlyphPage.scratchByteBuffer = ByteBuffer.allocateDirect(262144)).order(ByteOrder.LITTLE_ENDIAN);
        GlyphPage.scratchIntBuffer = GlyphPage.scratchByteBuffer.asIntBuffer();
        GlyphPage.scratchImage = new BufferedImage(256, 256, 2);
        (GlyphPage.scratchGraphics = (Graphics2D)GlyphPage.scratchImage.getGraphics()).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GlyphPage.scratchGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        GlyphPage.scratchGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        GlyphPage.renderContext = GlyphPage.scratchGraphics.getFontRenderContext();
    }
    
    public static Graphics2D getScratchGraphics() {
        return GlyphPage.scratchGraphics;
    }
    
    public GlyphPage(final UnicodeFont unicodeFont, final int pageWidth, final int pageHeight) throws SlickException {
        this.pageGlyphs = new ArrayList(32);
        this.unicodeFont = unicodeFont;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.pageImage = new Image(pageWidth, pageHeight);
    }
    
    public int loadGlyphs(final List glyphs, final int maxGlyphsToLoad) throws SlickException {
        if (this.rowHeight != 0 && maxGlyphsToLoad == -1) {
            int testX = this.pageX;
            int testY = this.pageY;
            int testRowHeight = this.rowHeight;
            final Iterator iter = this.getIterator(glyphs);
            while (iter.hasNext()) {
                final Glyph glyph = iter.next();
                final int width = glyph.getWidth();
                final int height = glyph.getHeight();
                if (testX + width >= this.pageWidth) {
                    testX = 0;
                    testY += testRowHeight;
                    testRowHeight = height;
                }
                else if (height > testRowHeight) {
                    testRowHeight = height;
                }
                if (testY + testRowHeight >= this.pageWidth) {
                    return 0;
                }
                testX += width;
            }
        }
        Color.white.bind();
        this.pageImage.bind();
        int i = 0;
        final Iterator iter2 = this.getIterator(glyphs);
        while (iter2.hasNext()) {
            final Glyph glyph2 = iter2.next();
            final int width2 = Math.min(256, glyph2.getWidth());
            final int height2 = Math.min(256, glyph2.getHeight());
            if (this.rowHeight == 0) {
                this.rowHeight = height2;
            }
            else if (this.pageX + width2 >= this.pageWidth) {
                if (this.pageY + this.rowHeight + height2 >= this.pageHeight) {
                    break;
                }
                this.pageX = 0;
                this.pageY += this.rowHeight;
                this.rowHeight = height2;
            }
            else if (height2 > this.rowHeight) {
                if (this.pageY + height2 >= this.pageHeight) {
                    break;
                }
                this.rowHeight = height2;
            }
            this.renderGlyph(glyph2, width2, height2);
            this.pageGlyphs.add(glyph2);
            this.pageX += width2;
            iter2.remove();
            if (++i == maxGlyphsToLoad) {
                this.orderAscending = !this.orderAscending;
                break;
            }
        }
        TextureImpl.bindNone();
        this.orderAscending = !this.orderAscending;
        return i;
    }
    
    private void renderGlyph(final Glyph glyph, final int width, final int height) throws SlickException {
        GlyphPage.scratchGraphics.setComposite(AlphaComposite.Clear);
        GlyphPage.scratchGraphics.fillRect(0, 0, 256, 256);
        GlyphPage.scratchGraphics.setComposite(AlphaComposite.SrcOver);
        GlyphPage.scratchGraphics.setColor(java.awt.Color.white);
        final Iterator iter = this.unicodeFont.getEffects().iterator();
        while (iter.hasNext()) {
            iter.next().draw(GlyphPage.scratchImage, GlyphPage.scratchGraphics, this.unicodeFont, glyph);
        }
        glyph.setShape(null);
        final WritableRaster raster = GlyphPage.scratchImage.getRaster();
        final int[] row = new int[width];
        for (int y = 0; y < height; ++y) {
            raster.getDataElements(0, y, width, 1, row);
            GlyphPage.scratchIntBuffer.put(row);
        }
        GlyphPage.GL.glTexSubImage2D(3553, 0, this.pageX, this.pageY, width, height, 32993, 5121, GlyphPage.scratchByteBuffer);
        GlyphPage.scratchIntBuffer.clear();
        glyph.setImage(this.pageImage.getSubImage(this.pageX, this.pageY, width, height));
    }
    
    private Iterator getIterator(final List glyphs) {
        if (this.orderAscending) {
            return glyphs.iterator();
        }
        final ListIterator iter = glyphs.listIterator(glyphs.size());
        return new Iterator() {
            @Override
            public boolean hasNext() {
                return iter.hasPrevious();
            }
            
            @Override
            public Object next() {
                return iter.previous();
            }
            
            @Override
            public void remove() {
                iter.remove();
            }
        };
    }
    
    public List getGlyphs() {
        return this.pageGlyphs;
    }
    
    public Image getImage() {
        return this.pageImage;
    }
}
