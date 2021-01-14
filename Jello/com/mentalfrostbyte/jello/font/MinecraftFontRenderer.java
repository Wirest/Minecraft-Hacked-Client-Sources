package com.mentalfrostbyte.jello.font;


import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import com.mentalfrostbyte.jello.main.Jello;

public class MinecraftFontRenderer
extends CFont {
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public MinecraftFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    @Override
    public int drawStringWithShadow(String text, double x2, float y2, int color) {
        float shadowWidth = this.drawString(text, (double)x2 + 1.0, (double)y2 + 3.5, color, true, 8.3f);
        return (int)Math.max(shadowWidth, this.drawString(text, x2, y2 + 2.5, color, false, 8.3f));
    }

    @Override
    public int drawString(String text, double x2, float y2, int color) {
        return (int)this.drawString(text, x2, y2, color, false, 8.3f);
    }
    
    @Override
    public int drawPassword(String text, double x2, float y2, int color) {
        return (int)this.drawString(text.replaceAll(".", "."), x2, y2, color, false, 8f);
    }

    @Override
    public int drawNoBSString(String text, double x2, float y2, int color) {
        return (int)this.drawNoBSString(text, x2, y2, color, false);
    }
    
    @Override
    public int drawSmoothString(String text, double x2, float y2, int color) {
        return (int)this.drawSmoothString(text, x2, y2, color, false);
    }
    
    @Override
    public double getPasswordWidth(String text) {
    	return this.getStringWidth(text.replaceAll(".", "."), 8);
    }
    
    public float drawCenteredString(String text, float x2, float y2, int color) {
        return this.drawString(text, x2 - (float)(this.getStringWidth(text) / 2), y2, color);
    }
    
    public float drawNoBSCenteredString(String text, float x2, float y2, int color) {
        return this.drawNoBSString(text, x2 - (float)(this.getStringWidth(text) / 2), y2, color);
    }
    
    public float drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
        return this.drawStringWithShadow(text, x2 - (float)(this.getStringWidth(text) / 2), y2, color);
    }
    public float drawString(String text, double x2, double y2, int color, boolean shadow, float kerning) {
    	
        x2 -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & -67108864) == 0) {
            color |= -16777216;
        }
        if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x2 *= 2.0 * Jello.fontScaleOffset;
        y2 = (y2 - 3.0) * 2.0 * Jello.fontScaleOffset;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5 / Jello.fontScaleOffset, 0.5 / Jello.fontScaleOffset, 0.5 / Jello.fontScaleOffset);
        //GlStateManager.scale(0.5, 0.5, 0.5);
        //GlStateManager.scale(1,1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.func_179144_i(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
       // GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        int i2 = 0;
        while (i2 < size) {
            char character = text.charAt(i2);
            if (character == '\u00a7' && i2 < size) {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i2 + 1));
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179144_i(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0 || colorIndex > 15) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((float)(colorcode >> 16 & 255) / 255.0f, (float)(colorcode >> 8 & 255) / 255.0f, (float)(colorcode & 255) / 255.0f, alpha);
                } else if (colorIndex == 16) {
                    randomCase = true;
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i(this.texBold.getGlTextureId());
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i(this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
                    GlStateManager.func_179144_i(this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                ++i2;
            } else if (character < currentData.length && character >= '\u0000') {
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x2, (float)y2);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x2, y2 + (double)(currentData[character].height / 2), x2 + (double)currentData[character].width - 8.0, y2 + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x2, y2 + (double)currentData[character].height - 2.0, x2 + (double)currentData[character].width - 8.0, y2 + (double)currentData[character].height - 2.0, 1.0f);
                }
                x2 += (double)(currentData[character].width - kerning + this.charOffset);
            }
            ++i2;
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        RenderSystem.setColor(Color.WHITE);
        return (float)x2 / 2.0f;
    }
    
    public float drawSmoothString(String text, double x2, double y2, int color, boolean shadow) {
        x2 -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x2 *= 2.0 * Jello.fontScaleOffset;
        y2 = (y2 - 3.0) * 2.0 * Jello.fontScaleOffset;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5 / Jello.fontScaleOffset, 0.5 / Jello.fontScaleOffset, 0.5 / Jello.fontScaleOffset);
        //GlStateManager.scale(0.5, 0.5, 0.5);
        //GlStateManager.scale(1,1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.func_179144_i(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        int i2 = 0;
        while (i2 < size) {
            char character = text.charAt(i2);
            if (character == '\u00a7' && i2 < size) {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i2 + 1));
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179144_i(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0 || colorIndex > 15) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((float)(colorcode >> 16 & 255) / 255.0f, (float)(colorcode >> 8 & 255) / 255.0f, (float)(colorcode & 255) / 255.0f, alpha);
                } else if (colorIndex == 16) {
                    randomCase = true;
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i(this.texBold.getGlTextureId());
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i(this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
                    GlStateManager.func_179144_i(this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                ++i2;
            } else if (character < currentData.length && character >= '\u0000') {
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x2, (float)y2);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x2, y2 + (double)(currentData[character].height / 2), x2 + (double)currentData[character].width - 8.0, y2 + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x2, y2 + (double)currentData[character].height - 2.0, x2 + (double)currentData[character].width - 8.0, y2 + (double)currentData[character].height - 2.0, 1.0f);
                }
                x2 += (double)(currentData[character].width - 8.3f + this.charOffset);
            }
            ++i2;
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        RenderSystem.setColor(Color.WHITE);
        return (float)x2 / 2.0f;
    }
    
    public float drawNoBSString(String text, double x2, double y2, int color, boolean shadow) {
        x2 -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x2 *= 2.0 * Jello.fontScaleOffset;
        y2 = (y2 - 3.0) * 2.0 * Jello.fontScaleOffset;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5 / Jello.fontScaleOffset, 0.5 / Jello.fontScaleOffset, 0.5 / Jello.fontScaleOffset);
        //GlStateManager.scale(0.5, 0.5, 0.5);
        //GlStateManager.scale(1,1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.func_179144_i(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        int i2 = 0;
        while (i2 < size) {
            char character = text.charAt(i2);
            if (character == '\u00a7' && i2 < size) {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i2 + 1));
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179144_i(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0 || colorIndex > 15) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((float)(colorcode >> 16 & 255) / 255.0f, (float)(colorcode >> 8 & 255) / 255.0f, (float)(colorcode & 255) / 255.0f, alpha);
                } else if (colorIndex == 16) {
                    randomCase = true;
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i(this.texBold.getGlTextureId());
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.func_179144_i(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i(this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
                    GlStateManager.func_179144_i(this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                ++i2;
            } else if (character < currentData.length && character >= '\u0000') {
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x2, (float)y2);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x2, y2 + (double)(currentData[character].height / 2), x2 + (double)currentData[character].width - 8.0, y2 + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x2, y2 + (double)currentData[character].height - 2.0, x2 + (double)currentData[character].width - 8.0, y2 + (double)currentData[character].height - 2.0, 1.0f);
                }
                x2 += (double)(currentData[character].width - 8.3f + this.charOffset);
            }
            ++i2;
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        RenderSystem.setColor(Color.WHITE);
        return (float)x2 / 2.0f;
    }

    @Override
    public double getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        float width = 0;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        int i2 = 0;
        while (i2 < size) {
            char character = text.charAt(i2);
            if (character == '\u00a7' && i2 < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i2;
            } else if (character < currentData.length && character >= '\u0000') {
                width += currentData[character].width - 8.3f + this.charOffset;
            }
            ++i2;
        }
        return width / (2 * Jello.fontScaleOffset);
    }

    public double getStringWidth(String text, float kerning) {
        if (text == null) {
            return 0;
        }
        float width = 0;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        int i2 = 0;
        while (i2 < size) {
            char character = text.charAt(i2);
            if (character == '\u00a7' && i2 < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i2;
            } else if (character < currentData.length && character >= '\u0000') {
                width += currentData[character].width - kerning + this.charOffset;
            }
            ++i2;
        }
        return width / (2 * Jello.fontScaleOffset);
    }

    @Override
    public int getHeight() {
        return (this.fontHeight - (int)(8* Jello.fontScaleOffset)) / (int)(2 * Jello.fontScaleOffset);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double x2, double y2, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double)this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            String[] array = words;
            int length = array.length;
            int j2 = 0;
            while (j2 < length) {
                String word = array[j2];
                int i2 = 0;
                while (i2 < word.toCharArray().length) {
                    char c2 = word.toCharArray()[i2];
                    if (c2 == '\u00a7' && i2 < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[i2 + 1];
                    }
                    ++i2;
                }
                if ((double)this.getStringWidth(String.valueOf(String.valueOf(String.valueOf(currentWord))) + word + " ") < width) {
                    currentWord = String.valueOf(String.valueOf(String.valueOf(currentWord))) + word + " ";
                } else {
                    finalWords.add(currentWord);
                    currentWord = "\u00a7" + lastColorCode + word + " ";
                }
                ++j2;
            }
            if (currentWord.length() > 0) {
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add("\u00a7" + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        char[] chars = string.toCharArray();
        int i2 = 0;
        while (i2 < chars.length) {
            char c2 = chars[i2];
            if (c2 == '\u00a7' && i2 < chars.length - 1) {
                lastColorCode = chars[i2 + 1];
            }
            if ((double)this.getStringWidth(String.valueOf(String.valueOf(String.valueOf(currentWord))) + c2) < width) {
                currentWord = String.valueOf(String.valueOf(String.valueOf(currentWord))) + c2;
            } else {
                finalWords.add(currentWord);
                currentWord = "\u00a7" + lastColorCode + String.valueOf(c2);
            }
            ++i2;
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        int index = 0;
        while (index < 32) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index >> 0 & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
            ++index;
        }
    }
    
    public String trimStringToWidth(String p_78269_1_, int p_78269_2_)
    {
        return this.trimStringToWidth(p_78269_1_, p_78269_2_, false);
    }
    public String trimStringToWidthPassword(String p_78269_1_, int p_78269_2_, boolean wha)
    {
    	p_78269_1_ = p_78269_1_.replaceAll(".", ".");
        return this.trimStringToWidth(p_78269_1_, p_78269_2_, wha);
    }
    private float getCharWidthFloat(char p_78263_1_)
    {
        if (p_78263_1_ == 167)
        {
            return -1.0F;
        }
        else if (p_78263_1_ == 32)
        {
            return 2;
        }
        else
        {
            int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_78263_1_);

            if (p_78263_1_ > 0 && var2 != -1)
            {
                return ((charData[var2].width/2.f)-4.f);//this.charData[var2].width;
            }
            else if (((charData[p_78263_1_].width/2.f)-4.f)/*this.charData[p_78263_1_].width*/ != 0)
            {
                int var3 = ((int)((charData[p_78263_1_].width/2.f)-4.f))/*this.charData[p_78263_1_].width*/ >>> 4;
                int var4 = ((int)((charData[p_78263_1_].width/2.f)-4.f))/*this.charData[p_78263_1_].width*/ & 15;
                var3 &= 15;
                ++var4;
                return (float)((var4 - var3) / 2 + 1);
            }
            else
            {
                return 0.0F;
            }
        }
    }
    public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_)
    {
        StringBuilder var4 = new StringBuilder();
        float var5 = 0.0F;
        int var6 = p_78262_3_ ? p_78262_1_.length() - 1 : 0;
        int var7 = p_78262_3_ ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;

        for (int var10 = var6; var10 >= 0 && var10 < p_78262_1_.length() && var5 < (float)p_78262_2_; var10 += var7)
        {
            char var11 = p_78262_1_.charAt(var10);
            float var12 = this.getCharWidthFloat(var11);

            if (var8)
            {
                var8 = false;

                if (var11 != 108 && var11 != 76)
                {
                    if (var11 == 114 || var11 == 82)
                    {
                        var9 = false;
                    }
                }
                else
                {
                    var9 = true;
                }
            }
            else if (var12 < 0.0F)
            {
                var8 = true;
            }
            else
            {
                var5 += var12;

                if (var9)
                {
                    ++var5;
                }
            }

            if (var5 > (float)p_78262_2_)
            {
                break;
            }

            if (p_78262_3_)
            {
                var4.insert(0, var11);
            }
            else
            {
                var4.append(var11);
            }
        }

        return var4.toString();
    }
}

