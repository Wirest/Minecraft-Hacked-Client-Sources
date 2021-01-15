// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.utils.minecraft;

import java.util.Set;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.src.Config;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.ArabicShaping;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class FontRenderer implements IResourceManagerReloadListener
{
    private static final ResourceLocation[] unicodePageLocations;
    private static float[] charWidth;
    public int FONT_HEIGHT;
    public Random fontRandom;
    private byte[] glyphWidth;
    public int[] colorCode;
    private static ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    private double posX;
    private double posY;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    public GameSettings gameSettings;
    public ResourceLocation locationFontTextureBase;
    public boolean enabled;
    public float scaleFactor;
    
    static {
        unicodePageLocations = new ResourceLocation[256];
    }
    
    public FontRenderer(final GameSettings p_i1035_1_, final ResourceLocation p_i1035_2_, final TextureManager p_i1035_3_, final boolean p_i1035_4_) {
        this.charWidth = new float[256];
        this.FONT_HEIGHT = 9;
        this.fontRandom = new Random();
        this.glyphWidth = new byte[65536];
        this.colorCode = new int[32];
        this.enabled = true;
        this.scaleFactor = 1.0f;
        this.gameSettings = p_i1035_1_;
        this.locationFontTextureBase = p_i1035_2_;
        this.locationFontTexture = p_i1035_2_;
        this.renderEngine = p_i1035_3_;
        this.unicodeFlag = p_i1035_4_;
        p_i1035_3_.bindTexture(this.locationFontTexture);
        for (int var5 = 0; var5 < 32; ++var5) {
            final int var6 = (var5 >> 3 & 0x1) * 85;
            int var7 = (var5 >> 2 & 0x1) * 170 + var6;
            int var8 = (var5 >> 1 & 0x1) * 170 + var6;
            int var9 = (var5 >> 0 & 0x1) * 170 + var6;
            if (var5 == 6) {
                var7 += 85;
            }
            if (p_i1035_1_.anaglyph) {
                final int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
                final int var11 = (var7 * 30 + var8 * 70) / 100;
                final int var12 = (var7 * 30 + var9 * 70) / 100;
                var7 = var10;
                var8 = var11;
                var9 = var12;
            }
            if (var5 >= 16) {
                var7 /= 4;
                var8 /= 4;
                var9 /= 4;
            }
            this.colorCode[var5] = ((var7 & 0xFF) << 16 | (var8 & 0xFF) << 8 | (var9 & 0xFF));
        }
        this.readGlyphSizes();
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.locationFontTexture = getHdFontLocation(this.locationFontTextureBase);
        for (int i = 0; i < FontRenderer.unicodePageLocations.length; ++i) {
            FontRenderer.unicodePageLocations[i] = null;
        }
        this.readFontTexture();
    }
    
    private void readFontTexture() {
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.func_177053_a(ClientUtils.mc().getResourceManager().getResource(this.locationFontTexture).getInputStream());
        }
        catch (IOException var18) {
            throw new RuntimeException(var18);
        }
        final int imgWidth = bufferedimage.getWidth();
        final int imgHeight = bufferedimage.getHeight();
        final int charW = imgWidth / 16;
        final int charH = imgHeight / 16;
        final float kx = imgWidth / 128.0f;
        this.scaleFactor = kx;
        final int[] ai = new int[imgWidth * imgHeight];
        bufferedimage.getRGB(0, 0, imgWidth, imgHeight, ai, 0, imgWidth);
        for (int k = 0; k < 256; ++k) {
            final int cx = k % 16;
            final int cy = k / 16;
            final boolean px = false;
            int var19;
            for (var19 = charW - 1; var19 >= 0; --var19) {
                final int x = cx * charW + var19;
                boolean flag = true;
                for (int py = 0; py < charH && flag; ++py) {
                    final int ypos = (cy * charH + py) * imgWidth;
                    final int col = ai[x + ypos];
                    final int al = col >> 24 & 0xFF;
                    if (al > 16) {
                        flag = false;
                    }
                }
                if (!flag) {
                    break;
                }
            }
            if (k == 32) {
                if (charW <= 8) {
                    var19 = (int)(2.0f * kx);
                }
                else {
                    var19 = (int)(1.5f * kx);
                }
            }
            this.charWidth[k] = (var19 + 1) / kx + 1.0f;
        }
        this.readCustomCharWidths();
    }
    
    private void readGlyphSizes() {
        InputStream var1 = null;
        try {
            var1 = ClientUtils.mc().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
            var1.read(this.glyphWidth);
        }
        catch (IOException var2) {
            throw new RuntimeException(var2);
        }
        finally {
            IOUtils.closeQuietly(var1);
        }
        IOUtils.closeQuietly(var1);
    }
    
    private float renderCharAtPos(final int p_78278_1_, final char p_78278_2_, final boolean p_78278_3_) {
        return (p_78278_2_ == ' ') ? this.charWidth[p_78278_2_] : ((p_78278_2_ == ' ') ? 4.0f : (("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(p_78278_2_) != -1 && !this.unicodeFlag) ? this.renderDefaultChar(p_78278_1_, p_78278_3_) : this.renderUnicodeChar(p_78278_2_, p_78278_3_)));
    }
    
    private float renderDefaultChar(final int p_78266_1_, final boolean p_78266_2_) {
        final float var3 = p_78266_1_ % 16 * 8;
        final float var4 = p_78266_1_ / 16 * 8;
        final float var5 = p_78266_2_ ? 1.0f : 0.0f;
        this.renderEngine.bindTexture(this.locationFontTexture);
        final float var6 = 7.99f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(var3 / 128.0f, var4 / 128.0f);
        GL11.glVertex3d(this.posX + var5, this.posY, 0.0);
        GL11.glTexCoord2f(var3 / 128.0f, (var4 + 7.99f) / 128.0f);
        GL11.glVertex3d(this.posX - var5, this.posY + 7.989999771118164, 0.0);
        GL11.glTexCoord2f((var3 + var6 - 1.0f) / 128.0f, var4 / 128.0f);
        GL11.glVertex3d(this.posX + var6 - 1.0 + var5, this.posY, 0.0);
        GL11.glTexCoord2f((var3 + var6 - 1.0f) / 128.0f, (var4 + 7.99f) / 128.0f);
        GL11.glVertex3d(this.posX + var6 - 1.0 - var5, this.posY + 7.989999771118164, 0.0);
        GL11.glEnd();
        return this.charWidth[p_78266_1_];
    }
    
    private ResourceLocation getUnicodePageLocation(final int p_111271_1_) {
        if (FontRenderer.unicodePageLocations[p_111271_1_] == null) {
            FontRenderer.unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", p_111271_1_));
            FontRenderer.unicodePageLocations[p_111271_1_] = getHdFontLocation(FontRenderer.unicodePageLocations[p_111271_1_]);
        }
        return FontRenderer.unicodePageLocations[p_111271_1_];
    }
    
    private void loadGlyphTexture(final int p_78257_1_) {
        this.renderEngine.bindTexture(this.getUnicodePageLocation(p_78257_1_));
    }
    
    private float renderUnicodeChar(final char p_78277_1_, final boolean p_78277_2_) {
        if (this.glyphWidth[p_78277_1_] == 0) {
            return 0.0f;
        }
        final int var3 = p_78277_1_ / '\u0100';
        this.loadGlyphTexture(var3);
        final int var4 = this.glyphWidth[p_78277_1_] >>> 4;
        final int var5 = this.glyphWidth[p_78277_1_] & 0xF;
        final float var6 = var4;
        final float var7 = var5 + 1;
        final float var8 = p_78277_1_ % '\u0010' * '\u0010' + var6;
        final float var9 = (p_78277_1_ & '\u00ff') / '\u0010' * '\u0010';
        final float var10 = var7 - var6 - 0.02f;
        final float var11 = p_78277_2_ ? 1.0f : 0.0f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(var8 / 256.0f, var9 / 256.0f);
        GL11.glVertex3d(this.posX + var11, this.posY, 0.0);
        GL11.glTexCoord2f(var8 / 256.0f, (var9 + 15.98f) / 256.0f);
        GL11.glVertex3d(this.posX - var11, this.posY + 7.989999771118164, 0.0);
        GL11.glTexCoord2f((var8 + var10) / 256.0f, var9 / 256.0f);
        GL11.glVertex3d(this.posX + var10 / 2.0f + var11, this.posY, 0.0);
        GL11.glTexCoord2f((var8 + var10) / 256.0f, (var9 + 15.98f) / 256.0f);
        GL11.glVertex3d(this.posX + var10 / 2.0f - var11, this.posY + 7.989999771118164, 0.0);
        GL11.glEnd();
        return (var7 - var6) / 2.0f + 1.0f;
    }
    
    public void drawScaledString(final String string, final double x, final double y, final int color, final double scale) {
        int strWidth = this.getStringWidth(string);
        strWidth *= (int)scale;
        double totalX = x - strWidth / 2;
        double totalY = y - this.FONT_HEIGHT * scale / 2.0;
        totalX /= scale;
        totalY /= scale;
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, 1.0);
        this.drawString(string, totalX, totalY, color);
        GL11.glPopMatrix();
    }
    
    public void drawCenteredString(final String text, final double x, final double y, final int color) {
        this.drawStringWithShadow(text, (float)(x - this.getStringWidth(text) / 2), (float)y, color);
    }
    
    public int drawStringWithShadow(final String p_175063_1_, final double p_175063_2_, final double p_175063_3_, final int p_175063_4_) {
        return this.func_175065_a(p_175063_1_, p_175063_2_, p_175063_3_, p_175063_4_, true);
    }
    
    public int drawString(final String text, final double x, final double y, final int color) {
        return this.enabled ? this.func_175065_a(text, x, y, color, false) : 0;
    }
    
    public int func_175065_a(final String p_175065_1_, final double p_175065_2_, final double p_175065_3_, final int p_175065_4_, final boolean p_175065_5_) {
        GlStateManager.enableAlpha();
        this.resetStyles();
        int var6;
        if (p_175065_5_) {
            var6 = this.func_180455_b(p_175065_1_, p_175065_2_ + 0.800000011920929, p_175065_3_ + 0.800000011920929, p_175065_4_, true);
            var6 = Math.max(var6, this.func_180455_b(p_175065_1_, p_175065_2_, p_175065_3_, p_175065_4_, false));
        }
        else {
            var6 = this.func_180455_b(p_175065_1_, p_175065_2_, p_175065_3_, p_175065_4_, false);
        }
        return var6;
    }
    
    private String bidiReorder(final String p_147647_1_) {
        try {
            final Bidi var3 = new Bidi(new ArabicShaping(8).shape(p_147647_1_), 127);
            var3.setReorderingMode(0);
            return var3.writeReordered(2);
        }
        catch (ArabicShapingException var4) {
            return p_147647_1_;
        }
    }
    
    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }
    
    private void renderStringAtPos(final String p_78255_1_, final boolean p_78255_2_) {
        for (int var3 = 0; var3 < p_78255_1_.length(); ++var3) {
            final char var4 = p_78255_1_.charAt(var3);
            if (var4 == '�' && var3 + 1 < p_78255_1_.length()) {
                int var5 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(var3 + 1));
                if (var5 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (var5 < 0 || var5 > 15) {
                        var5 = 15;
                    }
                    if (p_78255_2_) {
                        var5 += 16;
                    }
                    final int var6 = this.colorCode[var5];
                    this.textColor = var6;
                    GlStateManager.color((var6 >> 16) / 255.0f, (var6 >> 8 & 0xFF) / 255.0f, (var6 & 0xFF) / 255.0f, this.alpha);
                }
                else if (var5 == 16) {
                    this.randomStyle = true;
                }
                else if (var5 == 17) {
                    this.boldStyle = true;
                }
                else if (var5 == 18) {
                    this.strikethroughStyle = true;
                }
                else if (var5 == 19) {
                    this.underlineStyle = true;
                }
                else if (var5 == 20) {
                    this.italicStyle = true;
                }
                else if (var5 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                }
                ++var3;
            }
            else {
                int var5 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(var4);
                if (this.randomStyle && var5 != -1) {
                    int var6;
                    do {
                        var6 = this.fontRandom.nextInt(this.charWidth.length);
                    } while ((int)this.charWidth[var5] != (int)this.charWidth[var6]);
                    var5 = var6;
                }
                final float var7 = this.unicodeFlag ? 0.5f : (1.0f / this.scaleFactor);
                final boolean var8 = (var4 == '\0' || var5 == -1 || this.unicodeFlag) && p_78255_2_;
                if (var8) {
                    this.posX -= var7;
                    this.posY -= var7;
                }
                float var9 = this.renderCharAtPos(var5, var4, this.italicStyle);
                if (var8) {
                    this.posX += var7;
                    this.posY += var7;
                }
                if (this.boldStyle) {
                    this.posX += var7;
                    if (var8) {
                        this.posX -= var7;
                        this.posY -= var7;
                    }
                    this.renderCharAtPos(var5, var4, this.italicStyle);
                    this.posX -= var7;
                    if (var8) {
                        this.posX += var7;
                        this.posY += var7;
                    }
                    var9 += var7;
                }
                if (this.strikethroughStyle) {
                    final Tessellator var10 = Tessellator.getInstance();
                    final WorldRenderer var11 = var10.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    var11.startDrawingQuads();
                    var11.addVertex(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0);
                    var11.addVertex(this.posX + var9, this.posY + this.FONT_HEIGHT / 2, 0.0);
                    var11.addVertex(this.posX + var9, this.posY + this.FONT_HEIGHT / 2 - 1.0, 0.0);
                    var11.addVertex(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0, 0.0);
                    var10.draw();
                    GlStateManager.enableTexture2D();
                }
                if (this.underlineStyle) {
                    final Tessellator var10 = Tessellator.getInstance();
                    final WorldRenderer var11 = var10.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    var11.startDrawingQuads();
                    final int var12 = this.underlineStyle ? -1 : 0;
                    var11.addVertex(this.posX + var12, this.posY + this.FONT_HEIGHT, 0.0);
                    var11.addVertex(this.posX + var9, this.posY + this.FONT_HEIGHT, 0.0);
                    var11.addVertex(this.posX + var9, this.posY + this.FONT_HEIGHT - 1.0, 0.0);
                    var11.addVertex(this.posX + var12, this.posY + this.FONT_HEIGHT - 1.0, 0.0);
                    var10.draw();
                    GlStateManager.enableTexture2D();
                }
                this.posX += var9;
            }
        }
    }
    
    private int renderStringAligned(final String p_78274_1_, int p_78274_2_, final int p_78274_3_, final int p_78274_4_, final int p_78274_5_, final boolean p_78274_6_) {
        if (this.bidiFlag) {
            final int var7 = this.getStringWidth(this.bidiReorder(p_78274_1_));
            p_78274_2_ = p_78274_2_ + p_78274_4_ - var7;
        }
        return this.func_180455_b(p_78274_1_, p_78274_2_, p_78274_3_, p_78274_5_, p_78274_6_);
    }
    
    private int func_180455_b(String p_180455_1_, final double p_180455_2_, final double p_180455_3_, int p_180455_4_, final boolean p_180455_5_) {
        if (p_180455_1_ == null) {
            return 0;
        }
        if (this.bidiFlag) {
            p_180455_1_ = this.bidiReorder(p_180455_1_);
        }
        if ((p_180455_4_ & 0xFC000000) == 0x0) {
            p_180455_4_ |= 0xFF000000;
        }
        if (p_180455_5_) {
            p_180455_4_ = ((p_180455_4_ & 0xFCFCFC) >> 2 | (p_180455_4_ & 0xFF000000));
        }
        this.red = (p_180455_4_ >> 16 & 0xFF) / 255.0f;
        this.blue = (p_180455_4_ >> 8 & 0xFF) / 255.0f;
        this.green = (p_180455_4_ & 0xFF) / 255.0f;
        this.alpha = (p_180455_4_ >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(this.red, this.blue, this.green, this.alpha);
        this.posX = p_180455_2_;
        this.posY = p_180455_3_;
        this.renderStringAtPos(p_180455_1_, p_180455_5_);
        return (int)this.posX;
    }
    
    public int getStringWidth(final String p_78256_1_) {
        if (p_78256_1_ == null) {
            return 0;
        }
        float var2 = 0.0f;
        boolean var3 = false;
        for (int var4 = 0; var4 < p_78256_1_.length(); ++var4) {
            char var5 = p_78256_1_.charAt(var4);
            float var6 = this.getCharWidthFloat(var5);
            if (var6 < 0.0f && var4 < p_78256_1_.length() - 1) {
                ++var4;
                var5 = p_78256_1_.charAt(var4);
                if (var5 != 'l' && var5 != 'L') {
                    if (var5 == 'r' || var5 == 'R') {
                        var3 = false;
                    }
                }
                else {
                    var3 = true;
                }
                var6 = 0.0f;
            }
            var2 += var6;
            if (var3 && var6 > 0.0f) {
                ++var2;
            }
        }
        return (int)var2;
    }
    
    public int getCharWidth(final char par1) {
        return Math.round(this.getCharWidthFloat(par1));
    }
    
    private float getCharWidthFloat(final char p_78263_1_) {
        if (p_78263_1_ == '�') {
            return -1.0f;
        }
        if (p_78263_1_ == ' ') {
            return this.charWidth[32];
        }
        final int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(p_78263_1_);
        if (p_78263_1_ > '\0' && var2 != -1 && !this.unicodeFlag) {
            return this.charWidth[var2];
        }
        if (this.glyphWidth[p_78263_1_] != 0) {
            int var3 = this.glyphWidth[p_78263_1_] >>> 4;
            int var4 = this.glyphWidth[p_78263_1_] & 0xF;
            if (var4 > 7) {
                var4 = 15;
                var3 = 0;
            }
            return (++var4 - var3) / 2 + 1;
        }
        return 0.0f;
    }
    
    public String trimStringToWidth(final String p_78269_1_, final int p_78269_2_) {
        return this.trimStringToWidth(p_78269_1_, p_78269_2_, false);
    }
    
    public String trimStringToWidth(final String p_78262_1_, final int p_78262_2_, final boolean p_78262_3_) {
        final StringBuilder var4 = new StringBuilder();
        float var5 = 0.0f;
        final int var6 = p_78262_3_ ? (p_78262_1_.length() - 1) : 0;
        final int var7 = p_78262_3_ ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;
        for (int var10 = var6; var10 >= 0 && var10 < p_78262_1_.length() && var5 < p_78262_2_; var10 += var7) {
            final char var11 = p_78262_1_.charAt(var10);
            final float var12 = this.getCharWidthFloat(var11);
            if (var8) {
                var8 = false;
                if (var11 != 'l' && var11 != 'L') {
                    if (var11 == 'r' || var11 == 'R') {
                        var9 = false;
                    }
                }
                else {
                    var9 = true;
                }
            }
            else if (var12 < 0.0f) {
                var8 = true;
            }
            else {
                var5 += var12;
                if (var9) {
                    ++var5;
                }
            }
            if (var5 > p_78262_2_) {
                break;
            }
            if (p_78262_3_) {
                var4.insert(0, var11);
            }
            else {
                var4.append(var11);
            }
        }
        return var4.toString();
    }
    
    private String trimStringNewline(String p_78273_1_) {
        while (p_78273_1_ != null && p_78273_1_.endsWith("\n")) {
            p_78273_1_ = p_78273_1_.substring(0, p_78273_1_.length() - 1);
        }
        return p_78273_1_;
    }
    
    public void drawSplitString(String str, final int x, final int y, final int wrapWidth, final int textColor) {
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, false);
    }
    
    private void renderSplitString(final String str, final int x, int y, final int wrapWidth, final boolean addShadow) {
        final List var6 = this.listFormattedStringToWidth(str, wrapWidth);
        for (Iterator var7 = var6.iterator(); var7.hasNext(); y += FONT_HEIGHT)
        {
        	String var8 = (String)var7.next();
            this.renderStringAligned(var8, x, y, wrapWidth, this.textColor, addShadow);
            y += this.FONT_HEIGHT;
        }
    }
    
    public int splitStringWidth(final String p_78267_1_, final int p_78267_2_) {
        return this.FONT_HEIGHT * this.listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
    }
    
    public void setUnicodeFlag(final boolean p_78264_1_) {
        this.unicodeFlag = p_78264_1_;
    }
    
    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }
    
    public void setBidiFlag(final boolean p_78275_1_) {
        this.bidiFlag = p_78275_1_;
    }
    
    public List listFormattedStringToWidth(final String str, final int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }
    
    protected String wrapFormattedStringToWidth(final String str, final int wrapWidth) {
        final int var3 = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= var3) {
            return str;
        }
        final String var4 = str.substring(0, var3);
        final char var5 = str.charAt(var3);
        final boolean var6 = var5 == ' ' || var5 == '\n';
        final String var7 = String.valueOf(getFormatFromString(var4)) + str.substring(var3 + (var6 ? 1 : 0));
        return String.valueOf(var4) + "\n" + this.wrapFormattedStringToWidth(var7, wrapWidth);
    }
    
    private int sizeStringToWidth(final String str, final int wrapWidth) {
        final int var3 = str.length();
        float var4 = 0.0f;
        int var5 = 0;
        int var6 = -1;
        boolean var7 = false;
        while (var5 < var3) {
            final char var8 = str.charAt(var5);
            Label_0163: {
                switch (var8) {
                    case '\n': {
                        --var5;
                        break Label_0163;
                    }
                    case '�': {
                        if (var5 >= var3 - 1) {
                            break Label_0163;
                        }
                        ++var5;
                        final char var9 = str.charAt(var5);
                        if (var9 == 'l' || var9 == 'L') {
                            var7 = true;
                            break Label_0163;
                        }
                        if (var9 == 'r' || var9 == 'R' || isFormatColor(var9)) {
                            var7 = false;
                        }
                        break Label_0163;
                    }
                    case ' ': {
                        var6 = var5;
                        break;
                    }
                }
                var4 += this.getCharWidthFloat(var8);
                if (var7) {
                    ++var4;
                }
            }
            if (var8 == '\n') {
                var6 = ++var5;
                break;
            }
            if (var4 > wrapWidth) {
                break;
            }
            ++var5;
        }
        return (var5 != var3 && var6 != -1 && var6 < var5) ? var6 : var5;
    }
    
    private static boolean isFormatColor(final char colorChar) {
        return (colorChar >= '0' && colorChar <= '9') || (colorChar >= 'a' && colorChar <= 'f') || (colorChar >= 'A' && colorChar <= 'F');
    }
    
    private static boolean isFormatSpecial(final char formatChar) {
        return (formatChar >= 'k' && formatChar <= 'o') || (formatChar >= 'K' && formatChar <= 'O') || formatChar == 'r' || formatChar == 'R';
    }
    
    public static String getFormatFromString(final String p_78282_0_) {
        String var1 = "";
        int var2 = -1;
        final int var3 = p_78282_0_.length();
        while ((var2 = p_78282_0_.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                final char var4 = p_78282_0_.charAt(var2 + 1);
                if (isFormatColor(var4)) {
                    var1 = "§" + var4;
                }
                else {
                    if (!isFormatSpecial(var4)) {
                        continue;
                    }
                    var1 = String.valueOf(var1) + "§" + var4;
                }
            }
        }
        return var1;
    }
    
    public boolean getBidiFlag() {
        return this.bidiFlag;
    }
    
    public int func_175064_b(final char p_175064_1_) {
        final int index = "0123456789abcdef".indexOf(p_175064_1_);
        return (index >= 0 && index < this.colorCode.length) ? this.colorCode[index] : 16777215;
    }
    
    private static void readCustomCharWidths()
    {
      String fontFileName = locationFontTexture.getResourcePath();
      String suffix = ".png";
      if (fontFileName.endsWith(suffix))
      {
        String fileName = fontFileName.substring(0, fontFileName.length() - suffix.length()) + ".properties";
        try
        {
          ResourceLocation e = new ResourceLocation(locationFontTexture.getResourceDomain(), fileName);
          InputStream in = Config.getResourceStream(Config.getResourceManager(), e);
          if (in == null) {
            return;
          }
          Config.log("Loading " + fileName);
          Properties props = new Properties();
          props.load(in);
          Set keySet = props.keySet();
          Iterator iter = keySet.iterator();
          while (iter.hasNext())
          {
            String key = (String)iter.next();
            String prefix = "width.";
            if (key.startsWith(prefix))
            {
              String numStr = key.substring(prefix.length());
              int num = Config.parseInt(numStr, -1);
              if ((num >= 0) && (num < charWidth.length))
              {
                String value = props.getProperty(key);
                float width = Config.parseFloat(value, -1.0F);
                if (width >= 0.0F) {
                  charWidth[num] = width;
                }
              }
            }
          }
        }
        catch (FileNotFoundException localFileNotFoundException) {}catch (IOException var16)
        {
          var16.printStackTrace();
        }
      }
    }
    
    
    private static ResourceLocation getHdFontLocation(final ResourceLocation fontLoc) {
        String fontName = fontLoc.getResourcePath();
        final String texturesStr = "textures/";
        final String mcpatcherStr = "client/";
        if (!fontName.startsWith(texturesStr)) {
            return fontLoc;
        }
        fontName = fontName.substring(texturesStr.length());
        fontName = String.valueOf(mcpatcherStr) + fontName;
        final ResourceLocation fontLocHD = new ResourceLocation(fontLoc.getResourceDomain(), fontName);
        return Config.hasResource(Config.getResourceManager(), fontLocHD) ? fontLocHD : fontLoc;
    }
}
