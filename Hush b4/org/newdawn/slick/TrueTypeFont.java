// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.io.IOException;
import org.newdawn.slick.util.BufferedImageUtil;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.newdawn.slick.opengl.GLUtils;
import java.util.HashMap;
import org.newdawn.slick.opengl.renderer.Renderer;
import java.awt.FontMetrics;
import org.newdawn.slick.opengl.Texture;
import java.util.Map;
import org.newdawn.slick.opengl.renderer.SGL;

public class TrueTypeFont implements Font
{
    private static final SGL GL;
    private IntObject[] charArray;
    private Map customChars;
    private boolean antiAlias;
    private int fontSize;
    private int fontHeight;
    private Texture fontTexture;
    private int textureWidth;
    private int textureHeight;
    private java.awt.Font font;
    private FontMetrics fontMetrics;
    
    static {
        GL = Renderer.get();
    }
    
    public TrueTypeFont(final java.awt.Font font, final boolean antiAlias, final char[] additionalChars) {
        this.charArray = new IntObject[256];
        this.customChars = new HashMap();
        this.fontSize = 0;
        this.fontHeight = 0;
        this.textureWidth = 512;
        this.textureHeight = 512;
        GLUtils.checkGLContext();
        this.font = font;
        this.fontSize = font.getSize();
        this.antiAlias = antiAlias;
        this.createSet(additionalChars);
    }
    
    public TrueTypeFont(final java.awt.Font font, final boolean antiAlias) {
        this(font, antiAlias, null);
    }
    
    private BufferedImage getFontImage(final char ch) {
        final BufferedImage tempfontImage = new BufferedImage(1, 1, 2);
        final Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
        if (this.antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(this.font);
        this.fontMetrics = g.getFontMetrics();
        int charwidth = this.fontMetrics.charWidth(ch);
        if (charwidth <= 0) {
            charwidth = 1;
        }
        int charheight = this.fontMetrics.getHeight();
        if (charheight <= 0) {
            charheight = this.fontSize;
        }
        final BufferedImage fontImage = new BufferedImage(charwidth, charheight, 2);
        final Graphics2D gt = (Graphics2D)fontImage.getGraphics();
        if (this.antiAlias) {
            gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        gt.setFont(this.font);
        gt.setColor(Color.WHITE);
        final int charx = 0;
        final int chary = 0;
        gt.drawString(String.valueOf(ch), charx, chary + this.fontMetrics.getAscent());
        return fontImage;
    }
    
    private void createSet(final char[] customCharsArray) {
        if (customCharsArray != null && customCharsArray.length > 0) {
            this.textureWidth *= 2;
        }
        try {
            final BufferedImage imgTemp = new BufferedImage(this.textureWidth, this.textureHeight, 2);
            final Graphics2D g = (Graphics2D)imgTemp.getGraphics();
            g.setColor(new Color(255, 255, 255, 1));
            g.fillRect(0, 0, this.textureWidth, this.textureHeight);
            int rowHeight = 0;
            int positionX = 0;
            int positionY = 0;
            for (int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0, i = 0; i < 256 + customCharsLength; ++i) {
                final char ch = (i < 256) ? ((char)i) : customCharsArray[i - 256];
                BufferedImage fontImage = this.getFontImage(ch);
                final IntObject newIntObject = new IntObject((IntObject)null);
                newIntObject.width = fontImage.getWidth();
                newIntObject.height = fontImage.getHeight();
                if (positionX + newIntObject.width >= this.textureWidth) {
                    positionX = 0;
                    positionY += rowHeight;
                    rowHeight = 0;
                }
                newIntObject.storedX = positionX;
                newIntObject.storedY = positionY;
                if (newIntObject.height > this.fontHeight) {
                    this.fontHeight = newIntObject.height;
                }
                if (newIntObject.height > rowHeight) {
                    rowHeight = newIntObject.height;
                }
                g.drawImage(fontImage, positionX, positionY, null);
                positionX += newIntObject.width;
                if (i < 256) {
                    this.charArray[i] = newIntObject;
                }
                else {
                    this.customChars.put(new Character(ch), newIntObject);
                }
                fontImage = null;
            }
            this.fontTexture = BufferedImageUtil.getTexture(this.font.toString(), imgTemp);
        }
        catch (IOException e) {
            System.err.println("Failed to create font.");
            e.printStackTrace();
        }
    }
    
    private void drawQuad(final float drawX, final float drawY, final float drawX2, final float drawY2, final float srcX, final float srcY, final float srcX2, final float srcY2) {
        final float DrawWidth = drawX2 - drawX;
        final float DrawHeight = drawY2 - drawY;
        final float TextureSrcX = srcX / this.textureWidth;
        final float TextureSrcY = srcY / this.textureHeight;
        final float SrcWidth = srcX2 - srcX;
        final float SrcHeight = srcY2 - srcY;
        final float RenderWidth = SrcWidth / this.textureWidth;
        final float RenderHeight = SrcHeight / this.textureHeight;
        TrueTypeFont.GL.glTexCoord2f(TextureSrcX, TextureSrcY);
        TrueTypeFont.GL.glVertex2f(drawX, drawY);
        TrueTypeFont.GL.glTexCoord2f(TextureSrcX, TextureSrcY + RenderHeight);
        TrueTypeFont.GL.glVertex2f(drawX, drawY + DrawHeight);
        TrueTypeFont.GL.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight);
        TrueTypeFont.GL.glVertex2f(drawX + DrawWidth, drawY + DrawHeight);
        TrueTypeFont.GL.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY);
        TrueTypeFont.GL.glVertex2f(drawX + DrawWidth, drawY);
    }
    
    @Override
    public int getWidth(final String whatchars) {
        int totalwidth = 0;
        IntObject intObject = null;
        int currentChar = 0;
        for (int i = 0; i < whatchars.length(); ++i) {
            currentChar = whatchars.charAt(i);
            if (currentChar < 256) {
                intObject = this.charArray[currentChar];
            }
            else {
                intObject = this.customChars.get(new Character((char)currentChar));
            }
            if (intObject != null) {
                totalwidth += intObject.width;
            }
        }
        return totalwidth;
    }
    
    public int getHeight() {
        return this.fontHeight;
    }
    
    @Override
    public int getHeight(final String HeightString) {
        return this.fontHeight;
    }
    
    @Override
    public int getLineHeight() {
        return this.fontHeight;
    }
    
    @Override
    public void drawString(final float x, final float y, final String whatchars, final org.newdawn.slick.Color color) {
        this.drawString(x, y, whatchars, color, 0, whatchars.length() - 1);
    }
    
    @Override
    public void drawString(final float x, final float y, final String whatchars, final org.newdawn.slick.Color color, final int startIndex, final int endIndex) {
        color.bind();
        this.fontTexture.bind();
        IntObject intObject = null;
        TrueTypeFont.GL.glBegin(7);
        int totalwidth = 0;
        for (int i = 0; i < whatchars.length(); ++i) {
            final int charCurrent = whatchars.charAt(i);
            if (charCurrent < 256) {
                intObject = this.charArray[charCurrent];
            }
            else {
                intObject = this.customChars.get(new Character((char)charCurrent));
            }
            if (intObject != null) {
                if (i >= startIndex || i <= endIndex) {
                    this.drawQuad(x + totalwidth, y, x + totalwidth + intObject.width, y + intObject.height, (float)intObject.storedX, (float)intObject.storedY, (float)(intObject.storedX + intObject.width), (float)(intObject.storedY + intObject.height));
                }
                totalwidth += intObject.width;
            }
        }
        TrueTypeFont.GL.glEnd();
    }
    
    @Override
    public void drawString(final float x, final float y, final String whatchars) {
        this.drawString(x, y, whatchars, org.newdawn.slick.Color.white);
    }
    
    private class IntObject
    {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }
}
