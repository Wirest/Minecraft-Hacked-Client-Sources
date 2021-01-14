package com.mentalfrostbyte.jello.ttf;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.mentalfrostbyte.jello.main.Jello;

import net.minecraft.client.renderer.GlStateManager;

public final class FontData
{
    private final FontData.CharacterData[] characterBounds;
    private int texId;
    private int fontHeight;
    private int textureWidth;
    private int textureHeight;
    
    public FontData() {
        this.characterBounds = new FontData.CharacterData[256];
        this.texId = -1;
        this.fontHeight = 0;
    }
    
    public FontData setFont(final Font font, final boolean antialias) {
        return this.setFont(font, antialias, antialias, 16, 2);
    }
    
    private FontData setFont(final Font font, final boolean antialias, final boolean fractionalmetrics, final int characterCount, final int padding) {
        if (this.texId == -1) {
            this.texId = GLUtils.getTexture();
        }
        FontMetrics fontMetrics = new Canvas().getFontMetrics(font);
        int charHeight = 0;
        int positionX = 0;
        int positionY = 0;
        for (int i = 0; i < this.characterBounds.length; ++i) {
            final char character = (char)i;
            final int height = fontMetrics.getHeight();
            final int width = fontMetrics.charWidth(character);
            if (i != 0 && i % characterCount == 0) {
                positionX = padding;
                positionY += charHeight + padding;
                charHeight = 0;
            }
            if (height > charHeight) {
                charHeight = height;
                if (charHeight > this.fontHeight) {
                    this.fontHeight = charHeight;
                }
            }
            this.characterBounds[i] = new FontData.CharacterData(this, positionX, positionY, width, height);
            positionX += width + padding;
            if (positionX + width + padding > this.textureWidth) {
                this.textureWidth = positionX + width + padding;
            }
            if (positionY + height + padding > this.textureHeight) {
                this.textureHeight = positionY + height + padding;
            }
        }
        final BufferedImage bufferedImage = new BufferedImage(this.textureWidth, this.textureHeight, 2);
        final Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setFont(font);
        fontMetrics = graphics2D.getFontMetrics(font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, this.textureWidth, this.textureHeight);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalmetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antialias ? RenderingHints.VALUE_TEXT_ANTIALIAS_GASP : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, antialias ? RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY : RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        for (int j = 0; j < this.characterBounds.length; ++j) {
            graphics2D.drawString(String.valueOf((char)j), this.characterBounds[j].x, this.characterBounds[j].y + fontMetrics.getAscent());
        }
        GLUtils.applyTexture(this.texId, bufferedImage, antialias ? 9729 : 9728, 10497);
        return this;
    }
    
    public void bind() {
        GlStateManager.func_179144_i(this.texId);
    }
    
    public FontData.CharacterData getCharacterBounds(final char character) {
        return this.characterBounds[character];
    }
    
    public int getStringWidth(final String text) {
        int width = 0;
        char[] charArray;
        for (int length = (charArray = text.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            width += this.characterBounds[c].width;
        }
        return width;
    }
    
    public boolean hasBounds(final char character) {
        return character >= '\0' && character < '\u0100';
    }
    
    public boolean hasFont() {
        return this.texId != -1;
    }
    
    public int getFontHeight() {
        return this.fontHeight;
    }
    
    public int getTextureWidth() {
        return this.textureWidth;
    }
    
    public int getTextureHeight() {
        return this.textureHeight;
    }
    
    public class CharacterData
    {
        public final int x;
        public final int y;
        public final int width;
        public final int height;
        
        public CharacterData(final FontData this$0, final int x, final int y, final int width, final int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    
    
}
