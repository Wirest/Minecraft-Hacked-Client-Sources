package me.rigamortis.faurax.gui.font;

import java.awt.image.*;
import net.minecraft.util.*;
import java.util.regex.*;
import java.io.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import org.lwjgl.opengl.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import net.minecraft.client.renderer.*;

public class CustomFont
{
    private Font theFont;
    private Graphics2D theGraphics;
    private FontMetrics theMetrics;
    private float fontSize;
    private int startChar;
    private int endChar;
    private float[] xPos;
    private float[] yPos;
    private BufferedImage bufferedImage;
    private float extraSpacing;
    private DynamicTexture dynamicTexture;
    private ResourceLocation resourceLocation;
    private final Pattern patternControlCode;
    private final Pattern patternUnsupported;
    
    public CustomFont(final Object font, final float size) {
        this(font, size, 0.0f);
    }
    
    public CustomFont(final Object font) {
        this(font, 18.0f, 0.0f);
    }
    
    public CustomFont(final Object font, final float size, final float spacing) {
        this.extraSpacing = 0.0f;
        this.patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]");
        this.patternUnsupported = Pattern.compile("(?i)\\u00A7[K-O]");
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.extraSpacing = spacing;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        this.setupGraphics2D();
        this.createFont(font, size);
    }
    
    private final void setupGraphics2D() {
        this.bufferedImage = new BufferedImage(256, 256, 2);
        (this.theGraphics = (Graphics2D)this.bufferedImage.getGraphics()).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    
    private final void createFont(final Object font, final float size) {
        try {
            if (font instanceof Font) {
                this.theFont = (Font)font;
            }
            else if (font instanceof File) {
                this.theFont = Font.createFont(0, (File)font).deriveFont(size);
            }
            else if (font instanceof InputStream) {
                this.theFont = Font.createFont(0, (InputStream)font).deriveFont(size);
            }
            else if (font instanceof String) {
                this.theFont = new Font((String)font, 0, Math.round(size));
            }
            else {
                this.theFont = new Font("Verdana", 0, Math.round(size));
            }
            this.theGraphics.setFont(this.theFont);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.theFont = new Font("Verdana", 0, Math.round(size));
            this.theGraphics.setFont(this.theFont);
        }
        this.theGraphics.setColor(new Color(255, 255, 255, 0));
        this.theGraphics.fillRect(0, 0, 256, 256);
        this.theGraphics.setColor(Color.white);
        this.theMetrics = this.theGraphics.getFontMetrics();
        float x = 5.0f;
        float y = 5.0f;
        for (int i = this.startChar; i < this.endChar; ++i) {
            this.theGraphics.drawString(Character.toString((char)i), x, y + this.theMetrics.getAscent());
            this.xPos[i - this.startChar] = x;
            this.yPos[i - this.startChar] = y - this.theMetrics.getMaxDescent();
            x += this.theMetrics.stringWidth(Character.toString((char)i)) + 2.0f;
            if (x >= 250 - this.theMetrics.getMaxAdvance()) {
                x = 5.0f;
                y += this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent() + this.fontSize / 2.0f;
            }
        }
        final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        final String string = "font" + font.toString() + size;
        final DynamicTexture dynamicTexture = new DynamicTexture(this.bufferedImage);
        this.dynamicTexture = dynamicTexture;
        this.resourceLocation = textureManager.getDynamicTextureLocation(string, dynamicTexture);
    }
    
    public final void drawString(String text, final float x, final float y, final FontType fontType, final int color, final int color2) {
        GL11.glPushMatrix();
        text = this.stripUnsupported(text);
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        final String text2 = this.stripControlCodes(text);
        switch (fontType.ordinal()) {
            case 1: {
                this.drawer(text2, x + 0.5f, y, color2);
                this.drawer(text2, x - 0.5f, y, color2);
                this.drawer(text2, x, y + 0.5f, color2);
                this.drawer(text2, x, y - 0.5f, color2);
                break;
            }
            case 2: {
                this.drawer(text2, x + 0.5f, y + 0.5f, color2);
                break;
            }
            case 3: {
                this.drawer(text2, x + 0.5f, y + 1.0f, color2);
                break;
            }
            case 4: {
                this.drawer(text2, x, y + 0.5f, color2);
                break;
            }
            case 5: {
                this.drawer(text2, x, y - 0.5f, color2);
                break;
            }
        }
        this.drawer(text, x, y, color);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public void drawCenteredString(final String text, final float x, final float y, final int color) {
        this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, FontType.SHADOW_THIN, color);
    }
    
    public final void drawString(final String text, final float x, final float y, final FontType fontType, final int color) {
        this.drawString(text, x, y, fontType, color, -1157627904);
    }
    
    private final void drawer(final String text, float x, float y, final int color) {
        x *= 2.0f;
        y *= 2.0f;
        GL11.glEnable(3553);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        final float startX = x;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == '\ufffd' && i + 1 < text.length()) {
                final char oneMore = Character.toLowerCase(text.charAt(i + 1));
                if (oneMore == 'n') {
                    y += this.theMetrics.getAscent() + 2;
                    x = startX;
                }
                final int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                if (colorCode < 16) {
                    try {
                        final int newColor = Minecraft.getMinecraft().fontRendererObj.colorCode[colorCode];
                        GL11.glColor4f((newColor >> 16) / 255.0f, (newColor >> 8 & 0xFF) / 255.0f, (newColor & 0xFF) / 255.0f, alpha);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                else if (oneMore == 'f') {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
                }
                else if (oneMore == 'r') {
                    GL11.glColor4f(red, green, blue, alpha);
                }
                else if (oneMore == 'g') {
                    GL11.glColor4f(0.47f, 0.67f, 0.27f, alpha);
                }
                ++i;
            }
            else {
                try {
                    final char c = text.charAt(i);
                    this.drawChar(c, x, y);
                    x += this.getStringWidth(Character.toString(c)) * 2.0f;
                }
                catch (ArrayIndexOutOfBoundsException indexException) {
                    final char c2 = text.charAt(i);
                    System.out.println("Can't draw character: " + c2 + " (" + Character.getNumericValue(c2) + ")");
                }
            }
        }
    }
    
    public final float getStringWidth(final String text) {
        return (float)(this.getBounds(text).getWidth() + this.extraSpacing) / 2.0f;
    }
    
    public final float getStringHeight(final String text) {
        return (float)this.getBounds(text).getHeight() / 2.0f;
    }
    
    private final Rectangle2D getBounds(final String text) {
        return this.theMetrics.getStringBounds(text, this.theGraphics);
    }
    
    private final void drawChar(final char character, final float x, final float y) throws ArrayIndexOutOfBoundsException {
        final Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
        this.drawTexturedModalRect(x, y, this.xPos[character - this.startChar], this.yPos[character - this.startChar], (float)bounds.getWidth(), (float)bounds.getHeight() + this.theMetrics.getMaxDescent() + 1.0f);
    }
    
    private final List listFormattedStringToWidth(final String s, final int width) {
        return Arrays.asList(this.wrapFormattedStringToWidth(s, width).split("\n"));
    }
    
    private final String wrapFormattedStringToWidth(final String s, final float width) {
        final int wrapWidth = this.sizeStringToWidth(s, width);
        if (s.length() <= wrapWidth) {
            return s;
        }
        final String split = s.substring(0, wrapWidth);
        final String split2 = String.valueOf(this.getFormatFromString(split)) + s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ' || s.charAt(wrapWidth) == '\n') ? 1 : 0));
        try {
            return String.valueOf(split) + "\n" + this.wrapFormattedStringToWidth(split2, width);
        }
        catch (Exception e) {
            System.out.println("Cannot wrap string to width.");
            return "";
        }
    }
    
    private final int sizeStringToWidth(final String par1Str, final float par2) {
        int var3 = par1Str.length();
        float var2 = 0.0f;
        var3 = 0;
        int var4 = -1;
        boolean var5 = false;
        while (var3 < var3) {
            final char var6 = par1Str.charAt(var3);
            Label_0207: {
                switch (var6) {
                    case '\n': {
                        --var3;
                        break Label_0207;
                    }
                    case '\ufffd': {
                        if (var3 >= var3 - 1) {
                            break Label_0207;
                        }
                        ++var3;
                        final char var7 = par1Str.charAt(var3);
                        if (var7 == 'l' || var7 == 'L') {
                            var5 = true;
                            break Label_0207;
                        }
                        if (var7 == 'r' || var7 == 'R' || this.isFormatColor(var7)) {
                            var5 = false;
                        }
                        break Label_0207;
                    }
                    case ' ': {
                        var4 = var3;
                    }
                    case '-': {
                        var4 = var3;
                    }
                    case '_': {
                        var4 = var3;
                    }
                    case ':': {
                        var4 = var3;
                        break;
                    }
                }
                final String text = String.valueOf(var6);
                var2 += this.getStringWidth(text);
                if (var5) {
                    ++var2;
                }
            }
            if (var6 == '\n') {
                var4 = ++var3;
            }
            else if (var2 > par2) {
                break;
            }
            ++var3;
        }
        return (var3 != var3 && var4 != -1 && var4 < var3) ? var4 : var3;
    }
    
    private final String getFormatFromString(final String par0Str) {
        String var1 = "";
        int var2 = -1;
        final int var3 = par0Str.length();
        while ((var2 = par0Str.indexOf(65533, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                final char var4 = par0Str.charAt(var2 + 1);
                if (this.isFormatColor(var4)) {
                    var1 = "\ufffd" + var4;
                }
                else {
                    if (!this.isFormatSpecial(var4)) {
                        continue;
                    }
                    var1 = String.valueOf(var1) + "\ufffd" + var4;
                }
            }
        }
        return var1;
    }
    
    private final boolean isFormatColor(final char par0) {
        return (par0 >= '0' && par0 <= '9') || (par0 >= 'a' && par0 <= 'f') || (par0 >= 'A' && par0 <= 'F');
    }
    
    private final boolean isFormatSpecial(final char par0) {
        return (par0 >= 'k' && par0 <= 'o') || (par0 >= 'K' && par0 <= 'O') || par0 == 'r' || par0 == 'R';
    }
    
    private final void drawTexturedModalRect(final float x, final float y, final float u, final float v, final float width, final float height) {
        final float scale = 0.0039063f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.startDrawingQuads();
        renderer.addVertexWithUV(x + 0.0f, y + height, 0.0, (u + 0.0f) * scale, (v + height) * scale);
        renderer.addVertexWithUV(x + width, y + height, 0.0, (u + width) * scale, (v + height) * scale);
        renderer.addVertexWithUV(x + width, y + 0.0f, 0.0, (u + width) * scale, (v + 0.0f) * scale);
        renderer.addVertexWithUV(x + 0.0f, y + 0.0f, 0.0, (u + 0.0f) * scale, (v + 0.0f) * scale);
        tessellator.draw();
    }
    
    public final String stripControlCodes(final String s) {
        return this.patternControlCode.matcher(s).replaceAll("");
    }
    
    public final String stripUnsupported(final String s) {
        return this.patternUnsupported.matcher(s).replaceAll("");
    }
    
    public final Graphics2D getGraphics() {
        return this.theGraphics;
    }
    
    public final Font getFont() {
        return this.theFont;
    }
    
    public enum FontType
    {
        NORMAL, 
        SHADOW_THICK, 
        SHADOW_THIN, 
        OUTLINE_THIN, 
        EMBOSS_TOP, 
        EMBOSS_BOTTOM;
    }
}
