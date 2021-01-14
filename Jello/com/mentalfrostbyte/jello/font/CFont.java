package com.mentalfrostbyte.jello.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import com.mentalfrostbyte.jello.main.Jello;

public class CFont
extends JelloFontRenderer {
    private float imgSize = 512f;
    protected CharData[] charData = new CharData[256];
    protected Font font;
    protected boolean antiAlias;
    protected boolean fractionalMetrics;
    protected int fontHeight = -1;
    protected int charOffset = 0;
    protected DynamicTexture tex;

    public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.tex = this.setupTexture(font, antiAlias, fractionalMetrics, this.charData);
    }

    protected DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
        BufferedImage img = this.generateFontImage(font, antiAlias, fractionalMetrics, chars);
        try {
            return new DynamicTexture(img);
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
        int imgSize = (int)this.imgSize;
        BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
        Graphics2D g2 = (Graphics2D)bufferedImage.getGraphics();
        g2.setFont(font);
        g2.setColor(new Color(255, 255, 255, 0));
        g2.fillRect(0, 0, imgSize, imgSize);
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        int i2 = 0;
        while (i2 < chars.length) {
            char ch2 = (char)i2;
            CharData charData = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch2), g2);
            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;
            if (positionX + charData.width >= imgSize) {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }
            if (charData.height > charHeight) {
                charHeight = charData.height;
            }
            charData.storedX = positionX;
            charData.storedY = positionY;
            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }
            chars[i2] = charData;
            g2.drawString(String.valueOf(ch2), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
            ++i2;
        }
        return bufferedImage;
    }

    public void drawChar(CharData[] chars, char c2, float x2, float y2) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(x2, y2, chars[c2].width, chars[c2].height, chars[c2].storedX, chars[c2].storedY, chars[c2].width, chars[c2].height);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    protected void drawQuad(float x2, float y2, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
        float renderSRCX = srcX / this.imgSize;
        float renderSRCY = srcY / this.imgSize;
        float renderSRCWidth = srcWidth / this.imgSize;
        float renderSRCHeight = srcHeight / this.imgSize;
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x2 + width, y2);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d(x2, y2);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x2, y2 + height);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x2, y2 + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x2 + width, y2 + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x2 + width, y2);
    }

    public int getStringHeight(String text) {
        return this.getHeight();
    }

    @Override
    public double getStringWidth(String text) {
        int width = 0;
        char[] charArray = text.toCharArray();
        int length = charArray.length;
        int i2 = 0;
        while (i2 < length) {
            char c2 = charArray[i2];
            if (c2 < this.charData.length && c2 >= '\u0000') {
                width += this.charData[c2].width - 8 + this.charOffset;
            }
            ++i2;
        }
        return width / 2;
    }

    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    public void setAntiAlias(boolean antiAlias) {
        if (this.antiAlias != antiAlias) {
            this.antiAlias = antiAlias;
            this.tex = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
        }
    }

    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }

    public void setFractionalMetrics(boolean fractionalMetrics) {
        if (this.fractionalMetrics != fractionalMetrics) {
            this.fractionalMetrics = fractionalMetrics;
            this.tex = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
        }
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }

    protected class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;

        protected CharData() {
        }
    }

}

