// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.awt.BasicStroke;
import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Stroke;
import java.awt.Color;

public class OutlineEffect implements ConfigurableEffect
{
    private float width;
    private Color color;
    private int join;
    private Stroke stroke;
    
    public OutlineEffect() {
        this.width = 2.0f;
        this.color = Color.black;
        this.join = 2;
    }
    
    public OutlineEffect(final int width, final Color color) {
        this.width = 2.0f;
        this.color = Color.black;
        this.join = 2;
        this.width = (float)width;
        this.color = color;
    }
    
    @Override
    public void draw(final BufferedImage image, Graphics2D g, final UnicodeFont unicodeFont, final Glyph glyph) {
        g = (Graphics2D)g.create();
        if (this.stroke != null) {
            g.setStroke(this.stroke);
        }
        else {
            g.setStroke(this.getStroke());
        }
        g.setColor(this.color);
        g.draw(glyph.getShape());
        g.dispose();
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = (float)width;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public int getJoin() {
        return this.join;
    }
    
    public Stroke getStroke() {
        if (this.stroke == null) {
            return new BasicStroke(this.width, 2, this.join);
        }
        return this.stroke;
    }
    
    public void setStroke(final Stroke stroke) {
        this.stroke = stroke;
    }
    
    public void setJoin(final int join) {
        this.join = join;
    }
    
    @Override
    public String toString() {
        return "Outline";
    }
    
    @Override
    public List getValues() {
        final List values = new ArrayList();
        values.add(EffectUtil.colorValue("Color", this.color));
        values.add(EffectUtil.floatValue("Width", this.width, 0.1f, 999.0f, "This setting controls the width of the outline. The glyphs will need padding so the outline doesn't get clipped."));
        values.add(EffectUtil.optionValue("Join", String.valueOf(this.join), new String[][] { { "Bevel", "2" }, { "Miter", "0" }, { "Round", "1" } }, "This setting defines how the corners of the outline are drawn. This is usually only noticeable at large outline widths."));
        return values;
    }
    
    @Override
    public void setValues(final List values) {
        for (final Value value : values) {
            if (value.getName().equals("Color")) {
                this.color = (Color)value.getObject();
            }
            else if (value.getName().equals("Width")) {
                this.width = (float)value.getObject();
            }
            else {
                if (!value.getName().equals("Join")) {
                    continue;
                }
                this.join = Integer.parseInt((String)value.getObject());
            }
        }
    }
}
