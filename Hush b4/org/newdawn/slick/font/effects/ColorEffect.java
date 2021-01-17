// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class ColorEffect implements ConfigurableEffect
{
    private Color color;
    
    public ColorEffect() {
        this.color = Color.white;
    }
    
    public ColorEffect(final Color color) {
        this.color = Color.white;
        this.color = color;
    }
    
    @Override
    public void draw(final BufferedImage image, final Graphics2D g, final UnicodeFont unicodeFont, final Glyph glyph) {
        g.setColor(this.color);
        g.fill(glyph.getShape());
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null.");
        }
        this.color = color;
    }
    
    @Override
    public String toString() {
        return "Color";
    }
    
    @Override
    public List getValues() {
        final List values = new ArrayList();
        values.add(EffectUtil.colorValue("Color", this.color));
        return values;
    }
    
    @Override
    public void setValues(final List values) {
        for (final Value value : values) {
            if (value.getName().equals("Color")) {
                this.setColor((Color)value.getObject());
            }
        }
    }
}
