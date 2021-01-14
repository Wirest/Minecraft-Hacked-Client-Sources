package com.mentalfrostbyte.jello.ttf;

import net.minecraft.client.renderer.*;
import java.awt.*;

import com.mentalfrostbyte.jello.main.Jello;

public final class MinecraftFontRenderer extends BasicFontRenderer
{
    private final FontData boldFont;
    private final FontData italicFont;
    private final FontData boldItalicFont;
    private final int[] colorCode;
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    
    public MinecraftFontRenderer() {
        this.boldFont = new FontData();
        this.italicFont = new FontData();
        this.boldItalicFont = new FontData();
        this.colorCode = new int[32];
        for (int index = 0; index < 32; ++index) {
            final int noClue = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + noClue;
            int green = (index >> 1 & 0x1) * 170 + noClue;
            int blue = (index & 0x1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }
    
   /* @Deprecated
    public int drawString(final FontData fontData, final String text, final int x, final int y, final int color) {
        return 0;
    }*/
    
    public double drawString(final String text, final double d, final double f, final int color) {
        return this.drawString(text, d, f, color, false, 0.0f);
    }
    
    public double drawString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, (int)x, (int)y, color, false, 0.0f);
    }
    
    public double drawStringWithShadow(final String text, final int x, final int y, final int color) {
        return Math.max(this.drawString(text, x + 1, y + 1, color, true, 0.0f), this.drawString(text, x, y, color, false, 0.0f));
    }
    
    public void drawCenteredStringWithShadow(final String text, final int x, final int y, final int color) {
        this.drawStringWithShadow(text, x - this.getStringWidth(text) / 2, y - this.getStringHeight(text) / 2, color);
    }
    
    public void drawCenteredString(final String text, final int x, final int y, final int color) {
        this.drawString(text, x - this.getStringWidth(text) / 2, y - this.getStringHeight(text) / 2, color);
    }
    
    public void drawCenteredString(final String text, final float x, final float y, final int color) {
        this.drawString(text, (int)(x - this.getStringWidth(text) / 2), (int)(y - this.getStringHeight(text) / 2), color);
    }
    
    public double drawString(final String text, final int x, final int y, final int color, final float z) {
        return this.drawString(text, x, y, color, false, z);
    }
    
    public double drawStringWithShadow(final String text, final int x, final int y, final int color, final float z) {
        return Math.max(this.drawString(text, x + 1, y + 1, color, true, z), this.drawString(text, x, y, color, false, z));
    }
    
    public void drawCenteredStringWithShadow(final String text, final int x, final int y, final int color, final float z) {
        this.drawStringWithShadow(text, x - this.getStringWidth(text) / 2, y - this.getStringHeight(text) / 2, color, z);
    }
    
    public void drawCenteredString(final String text, final int x, final int y, final int color, final float z) {
        this.drawString(text, x - this.getStringWidth(text) / 2, y - this.getStringHeight(text) / 2, color, z);
    }
    
    public double drawString(final String text, double d, double f, int color, final boolean shadow, final float z) {
    	
        if (text == null) {
            return 0;
        }
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        FontData currentFont = this.fontData;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        d *= (int)2.0f;
        f *= (int)2.0f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
        final int size = text.length();
        currentFont.bind();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '§' && i < size && i + 1 < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                if (colorIndex < 16) {
                    bold = false;
                    randomCase = false;
                    currentFont = this.fontData;
                    currentFont.bind();
                    if (colorIndex < 0 || colorIndex > 15) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    final int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                }
                else if (colorIndex == 16) {
                    randomCase = true;
                }
                else if (colorIndex != 17 && colorIndex != 18 && colorIndex != 19) {
                    if (colorIndex == 20) {
                        if (bold) {
                            currentFont = this.boldItalicFont;
                            currentFont.bind();
                        }
                        else {
                            currentFont = this.italicFont;
                            currentFont.bind();
                        }
                    }
                    else if (colorIndex == 21) {
                        bold = false;
                        randomCase = false;
                        GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
                        currentFont = this.fontData;
                        currentFont.bind();
                    }
                }
                ++i;
            }
            else if (currentFont.hasBounds(character)) {
                if (randomCase) {
                    char newChar;
                    for (newChar = '\0'; currentFont.getCharacterBounds(newChar).width != currentFont.getCharacterBounds(character).width; newChar = (char)(Math.random() * 256.0)) {}
                    character = newChar;
                }
                final FontData.CharacterData area = currentFont.getCharacterBounds(character);
                FontUtils.drawTextureRect((float)d, (float)f, (float)area.width, (float)area.height, area.x / currentFont.getTextureWidth(), area.y / currentFont.getTextureHeight(), (area.x + area.width) / currentFont.getTextureWidth(), (area.y + area.height) / currentFont.getTextureHeight(), z);
                d += area.width + this.kerning;
                
            }
        }
        GlStateManager.popMatrix();
        return d;
    }
    
    public int getStringHeight(final String text) {
        if (text == null) {
            return 0;
        }
        int height = 0;
        FontData currentFont = this.fontData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text.length(), i = 0; i < size; ++i) {
            final char character = text.charAt(i);
            if (character == '§' && i < size) {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                }
                else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        currentFont = this.boldItalicFont;
                    }
                    else {
                        currentFont = this.boldFont;
                    }
                }
                else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        currentFont = this.boldItalicFont;
                    }
                    else {
                        currentFont = this.italicFont;
                    }
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentFont = this.fontData;
                }
                ++i;
            }
            else if (currentFont.hasBounds(character) && currentFont.getCharacterBounds(character).height > height) {
                height = currentFont.getCharacterBounds(character).height;
            }
        }
        return height / 2;
    }
    
    public int getStringWidth(final String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        FontData currentFont = this.fontData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text.length(), i = 0; i < size; ++i) {
            final char character = text.charAt(i);
            if (character == '§' && i < size) {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                }
                else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        currentFont = this.boldItalicFont;
                    }
                    else {
                        currentFont = this.boldFont;
                    }
                }
                else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        currentFont = this.boldItalicFont;
                    }
                    else {
                        currentFont = this.italicFont;
                    }
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentFont = this.fontData;
                }
                ++i;
            }
            else if (currentFont.hasBounds(character)) {
                width += currentFont.getCharacterBounds(character).width + this.kerning;
            }
        }
        return width / 2;
    }
    
    public void setFont(final Font font, final boolean antialias) {
        this.fontData.setFont(font, antialias);
        this.boldFont.setFont(font.deriveFont(1), antialias);
        this.italicFont.setFont(font.deriveFont(2), antialias);
        this.boldItalicFont.setFont(font.deriveFont(3), antialias);
    }
}