// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public interface Effect
{
    void draw(final BufferedImage p0, final Graphics2D p1, final UnicodeFont p2, final Glyph p3);
}
