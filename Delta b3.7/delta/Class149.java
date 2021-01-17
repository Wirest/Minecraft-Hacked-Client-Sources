/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class145;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class Class149 {
    private int theology$;
    private HashMap<Character, Class145> posts$;
    private boolean fioricet$;
    private int nasty$ = 180 - 327 + 293 - 253 + 106;
    private DynamicTexture charter$;
    private BufferedImage indexed$;
    private boolean europe$;
    private boolean cartoons$;
    private Font attitude$;

    public boolean _turkey() {
        return this.fioricet$;
    }

    public void _united() {
        GL11.glBindTexture((int)(44 - 77 + 57 + 3529), (int)(237 - 287 + 176 + -126));
    }

    public boolean _spirits() {
        return this.europe$;
    }

    public void _diving() {
        GL11.glBindTexture((int)(173 - 185 + 148 + 3417), (int)this.charter$.func_110552_b());
    }

    public float _bulgaria(char c) {
        return Class145._morgan(this.posts$.get(Character.valueOf(c)));
    }

    public int _logic() {
        return this.nasty$;
    }

    public Class149(Font font, boolean bl, boolean bl2, boolean bl3) {
        this.posts$ = new HashMap();
        this.attitude$ = font;
        this.fioricet$ = bl;
        this.europe$ = bl2;
        this.cartoons$ = bl3;
    }

    public void _cliff() {
        this.charter$ = new DynamicTexture(this.indexed$);
    }

    public float _islamic(char c, double d, double d2) {
        Class145 class145 = this.posts$.get(Character.valueOf(c));
        if (class145 == null) {
            throw new IllegalArgumentException("'" + c + "' wasn't found");
        }
        float f = (float)Class145._pulse(class145) / (float)this.theology$;
        float f2 = (float)Class145._amended(class145) / (float)this.theology$;
        float f3 = (float)Class145._morgan(class145) / (float)this.theology$;
        float f4 = (float)Class145._poverty(class145) / (float)this.theology$;
        float f5 = Class145._morgan(class145);
        float f6 = Class145._poverty(class145);
        GL11.glBegin((int)(252 - 269 + 252 - 242 + 11));
        GL11.glTexCoord2f((float)(f + f3), (float)f2);
        GL11.glVertex2d((double)(d + (double)f5), (double)d2);
        GL11.glTexCoord2f((float)f, (float)f2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glTexCoord2f((float)f, (float)(f2 + f4));
        GL11.glVertex2d((double)d, (double)(d2 + (double)f6));
        GL11.glTexCoord2f((float)f, (float)(f2 + f4));
        GL11.glVertex2d((double)d, (double)(d2 + (double)f6));
        GL11.glTexCoord2f((float)(f + f3), (float)(f2 + f4));
        GL11.glVertex2d((double)(d + (double)f5), (double)(d2 + (double)f6));
        GL11.glTexCoord2f((float)(f + f3), (float)f2);
        GL11.glVertex2d((double)(d + (double)f5), (double)d2);
        GL11.glEnd();
        return f5 - 8.0f;
    }

    public void _codes(char[] arrc) {
        int n;
        int n2;
        double d = -1.0;
        double d2 = -1.0;
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, this.fioricet$, this.europe$);
        Object object = arrc;
        int n3 = ((char[])object).length;
        for (n2 = 31 - 54 + 45 - 20 + -2; n2 < n3; ++n2) {
            n = object[n2];
            Rectangle2D rectangle2D = this.attitude$.getStringBounds(Character.toString((char)n), fontRenderContext);
            if (d < rectangle2D.getWidth()) {
                d = rectangle2D.getWidth();
            }
            if (!(d2 < rectangle2D.getHeight())) continue;
            d2 = rectangle2D.getHeight();
        }
        this.theology$ = (int)Math.ceil(Math.max(Math.ceil(Math.sqrt((d += 2.0) * d * (double)arrc.length) / d), Math.ceil(Math.sqrt((d2 += 2.0) * d2 * (double)arrc.length) / d2)) * Math.max(d, d2)) + (166 - 277 + 138 + -26);
        this.indexed$ = new BufferedImage(this.theology$, this.theology$, 140 - 259 + 60 + 61);
        object = (Graphics2D)this.indexed$.getGraphics();
        ((Graphics)object).setFont(this.attitude$);
        ((Graphics)object).setColor(new Color(34 - 66 + 9 - 4 + 282, 63 - 74 + 46 + 220, 175 - 197 + 7 + 270, 50 - 89 + 15 + 24));
        ((Graphics)object).fillRect(239 - 395 + 27 - 24 + 153, 173 - 209 + 110 - 104 + 30, this.theology$, this.theology$);
        ((Graphics)object).setColor(Color.white);
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.europe$ ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.fioricet$ ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.fioricet$ ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        FontMetrics fontMetrics = ((Graphics)object).getFontMetrics();
        n2 = 269 - 274 + 206 - 88 + -113;
        n = 124 - 198 + 63 + 11;
        int n4 = 100 - 147 + 124 - 7 + -69;
        char[] arrc2 = arrc;
        int n5 = arrc2.length;
        for (int i = 59 - 74 + 48 + -33; i < n5; ++i) {
            char c = arrc2[i];
            Class145 class145 = new Class145();
            Rectangle2D rectangle2D = fontMetrics.getStringBounds(Character.toString(c), (Graphics)object);
            Class145._indiana(class145, rectangle2D.getBounds().width + (242 - 438 + 319 - 63 + -52));
            Class145._content(class145, rectangle2D.getBounds().height);
            if (n + Class145._morgan(class145) >= this.theology$) {
                n = 27 - 38 + 10 + 1;
                n4 += n2;
                n2 = 57 - 95 + 18 - 18 + 38;
            }
            Class145._wishes(class145, n);
            Class145._vocals(class145, n4);
            if (Class145._poverty(class145) > this.nasty$) {
                this.nasty$ = Class145._poverty(class145);
            }
            if (Class145._poverty(class145) > n2) {
                n2 = Class145._poverty(class145);
            }
            ((Graphics2D)object).drawString(Character.toString(c), n + (226 - 314 + 288 + -198), n4 + fontMetrics.getAscent());
            n += Class145._morgan(class145);
            if (this.cartoons$ && c == 178 - 191 + 116 + 2) {
                Class145 class1452 = class145;
                Class145._content(class1452, Class145._poverty(class1452) - (237 - 267 + 218 - 131 + -50));
            }
            this.posts$.put(Character.valueOf(c), class145);
        }
    }

    public Class149(Font font, boolean bl, boolean bl2) {
        this(font, bl, bl2, 147 - 234 + 215 - 48 + -80);
    }
}

