// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.awt.image.ImageObserver;
import java.awt.Image;
import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class FilterEffect implements Effect
{
    private BufferedImageOp filter;
    
    public FilterEffect() {
    }
    
    public FilterEffect(final BufferedImageOp filter) {
        this.filter = filter;
    }
    
    @Override
    public void draw(final BufferedImage image, final Graphics2D g, final UnicodeFont unicodeFont, final Glyph glyph) {
        final BufferedImage scratchImage = EffectUtil.getScratchImage();
        this.filter.filter(image, scratchImage);
        image.getGraphics().drawImage(scratchImage, 0, 0, null);
    }
    
    public BufferedImageOp getFilter() {
        return this.filter;
    }
    
    public void setFilter(final BufferedImageOp filter) {
        this.filter = filter;
    }
}
