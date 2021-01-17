// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.awt.Paint;
import java.awt.GradientPaint;
import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GradientEffect implements ConfigurableEffect
{
    private Color topColor;
    private Color bottomColor;
    private int offset;
    private float scale;
    private boolean cyclic;
    
    public GradientEffect() {
        this.topColor = Color.cyan;
        this.bottomColor = Color.blue;
        this.offset = 0;
        this.scale = 1.0f;
    }
    
    public GradientEffect(final Color topColor, final Color bottomColor, final float scale) {
        this.topColor = Color.cyan;
        this.bottomColor = Color.blue;
        this.offset = 0;
        this.scale = 1.0f;
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        this.scale = scale;
    }
    
    @Override
    public void draw(final BufferedImage image, final Graphics2D g, final UnicodeFont unicodeFont, final Glyph glyph) {
        final int ascent = unicodeFont.getAscent();
        final float height = ascent * this.scale;
        final float top = -glyph.getYOffset() + unicodeFont.getDescent() + this.offset + ascent / 2 - height / 2.0f;
        g.setPaint(new GradientPaint(0.0f, top, this.topColor, 0.0f, top + height, this.bottomColor, this.cyclic));
        g.fill(glyph.getShape());
    }
    
    public Color getTopColor() {
        return this.topColor;
    }
    
    public void setTopColor(final Color topColor) {
        this.topColor = topColor;
    }
    
    public Color getBottomColor() {
        return this.bottomColor;
    }
    
    public void setBottomColor(final Color bottomColor) {
        this.bottomColor = bottomColor;
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public void setOffset(final int offset) {
        this.offset = offset;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public void setScale(final float scale) {
        this.scale = scale;
    }
    
    public boolean isCyclic() {
        return this.cyclic;
    }
    
    public void setCyclic(final boolean cyclic) {
        this.cyclic = cyclic;
    }
    
    @Override
    public String toString() {
        return "Gradient";
    }
    
    @Override
    public List getValues() {
        final List values = new ArrayList();
        values.add(EffectUtil.colorValue("Top color", this.topColor));
        values.add(EffectUtil.colorValue("Bottom color", this.bottomColor));
        values.add(EffectUtil.intValue("Offset", this.offset, "This setting allows you to move the gradient up or down. The gradient is normally centered on the glyph."));
        values.add(EffectUtil.floatValue("Scale", this.scale, 0.0f, 1.0f, "This setting allows you to change the height of the gradient by apercentage. The gradient is normally the height of most glyphs in the font."));
        values.add(EffectUtil.booleanValue("Cyclic", this.cyclic, "If this setting is checked, the gradient will repeat."));
        return values;
    }
    
    @Override
    public void setValues(final List values) {
        for (final Value value : values) {
            if (value.getName().equals("Top color")) {
                this.topColor = (Color)value.getObject();
            }
            else if (value.getName().equals("Bottom color")) {
                this.bottomColor = (Color)value.getObject();
            }
            else if (value.getName().equals("Offset")) {
                this.offset = (int)value.getObject();
            }
            else if (value.getName().equals("Scale")) {
                this.scale = (float)value.getObject();
            }
            else {
                if (!value.getName().equals("Cyclic")) {
                    continue;
                }
                this.cyclic = (boolean)value.getObject();
            }
        }
    }
}
