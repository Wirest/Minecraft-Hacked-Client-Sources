// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import me.nico.hush.Client;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import optifine.CustomColors;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.ArabicShaping;
import org.lwjgl.opengl.GL11;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.util.Properties;
import java.awt.image.BufferedImage;
import optifine.Config;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import optifine.FontUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class FontRenderer implements IResourceManagerReloadListener
{
    private static final ResourceLocation[] unicodePageLocations;
    private float[] charWidth;
    public int FONT_HEIGHT;
    public Random fontRandom;
    private byte[] glyphWidth;
    private int[] colorCode;
    private ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    private float posX;
    private float posY;
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
    private static final String __OBFID = "CL_00000660";
    public GameSettings gameSettings;
    public ResourceLocation locationFontTextureBase;
    public boolean enabled;
    public float offsetBold;
    
    static {
        unicodePageLocations = new ResourceLocation[256];
    }
    
    public FontRenderer(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
        this.charWidth = new float[256];
        this.FONT_HEIGHT = 9;
        this.fontRandom = new Random();
        this.glyphWidth = new byte[65536];
        this.colorCode = new int[32];
        this.enabled = true;
        this.offsetBold = 1.0f;
        this.gameSettings = gameSettingsIn;
        this.locationFontTextureBase = location;
        this.locationFontTexture = location;
        this.renderEngine = textureManagerIn;
        this.unicodeFlag = unicode;
        this.bindTexture(this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase));
        for (int i = 0; i < 32; ++i) {
            final int j = (i >> 3 & 0x1) * 85;
            int k = (i >> 2 & 0x1) * 170 + j;
            int l = (i >> 1 & 0x1) * 170 + j;
            int i2 = (i >> 0 & 0x1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (gameSettingsIn.anaglyph) {
                final int j2 = (k * 30 + l * 59 + i2 * 11) / 100;
                final int k2 = (k * 30 + l * 70) / 100;
                final int l2 = (k * 30 + i2 * 70) / 100;
                k = j2;
                l = k2;
                i2 = l2;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i2 /= 4;
            }
            this.colorCode[i] = ((k & 0xFF) << 16 | (l & 0xFF) << 8 | (i2 & 0xFF));
        }
        this.readGlyphSizes();
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        for (int i = 0; i < FontRenderer.unicodePageLocations.length; ++i) {
            FontRenderer.unicodePageLocations[i] = null;
        }
        this.readFontTexture();
        this.readGlyphSizes();
    }
    
    private void readFontTexture() {
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.readBufferedImage(this.getResourceInputStream(this.locationFontTexture));
        }
        catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
        final Properties properties = FontUtils.readFontProperties(this.locationFontTexture);
        final int i = bufferedimage.getWidth();
        final int j = bufferedimage.getHeight();
        final int k = i / 16;
        final int l = j / 16;
        final float f = i / 128.0f;
        final float f2 = Config.limit(f, 1.0f, 2.0f);
        this.offsetBold = 1.0f / f2;
        final float f3 = FontUtils.readFloat(properties, "offsetBold", -1.0f);
        if (f3 >= 0.0f) {
            this.offsetBold = f3;
        }
        final int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        for (int i2 = 0; i2 < 256; ++i2) {
            final int j2 = i2 % 16;
            final int k2 = i2 / 16;
            int l2;
            int i3;
            boolean flag;
            int j3;
            int k3;
            int l3;
            int i4;
            for (l2 = 0, l2 = k - 1; l2 >= 0; --l2) {
                i3 = j2 * k + l2;
                for (flag = true, j3 = 0; j3 < l && flag; ++j3) {
                    k3 = (k2 * l + j3) * i;
                    l3 = aint[i3 + k3];
                    i4 = (l3 >> 24 & 0xFF);
                    if (i4 > 16) {
                        flag = false;
                    }
                }
                if (!flag) {
                    break;
                }
            }
            if (i2 == 65) {
                i2 = i2;
            }
            if (i2 == 32) {
                if (k <= 8) {
                    l2 = (int)(2.0f * f);
                }
                else {
                    l2 = (int)(1.5f * f);
                }
            }
            this.charWidth[i2] = (l2 + 1) / f + 1.0f;
        }
        FontUtils.readCustomCharWidths(properties, this.charWidth);
    }
    
    private void readGlyphSizes() {
        InputStream inputstream = null;
        try {
            inputstream = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
            inputstream.read(this.glyphWidth);
        }
        catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
        IOUtils.closeQuietly(inputstream);
    }
    
    private float func_181559_a(final char p_181559_1_, final boolean p_181559_2_) {
        if (p_181559_1_ == ' ') {
            return this.unicodeFlag ? 4.0f : this.charWidth[p_181559_1_];
        }
        final int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(p_181559_1_);
        return (i != -1 && !this.unicodeFlag) ? this.renderDefaultChar(i, p_181559_2_) : this.renderUnicodeChar(p_181559_1_, p_181559_2_);
    }
    
    private float renderDefaultChar(final int p_78266_1_, final boolean p_78266_2_) {
        final int i = p_78266_1_ % 16 * 8;
        final int j = p_78266_1_ / 16 * 8;
        final int k = p_78266_2_ ? 1 : 0;
        this.bindTexture(this.locationFontTexture);
        final float f = this.charWidth[p_78266_1_];
        final float f2 = 7.99f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(i / 128.0f, j / 128.0f);
        GL11.glVertex3f(this.posX + k, this.posY, 0.0f);
        GL11.glTexCoord2f(i / 128.0f, (j + 7.99f) / 128.0f);
        GL11.glVertex3f(this.posX - k, this.posY + 7.99f, 0.0f);
        GL11.glTexCoord2f((i + f2 - 1.0f) / 128.0f, j / 128.0f);
        GL11.glVertex3f(this.posX + f2 - 1.0f + k, this.posY, 0.0f);
        GL11.glTexCoord2f((i + f2 - 1.0f) / 128.0f, (j + 7.99f) / 128.0f);
        GL11.glVertex3f(this.posX + f2 - 1.0f - k, this.posY + 7.99f, 0.0f);
        GL11.glEnd();
        return f;
    }
    
    private ResourceLocation getUnicodePageLocation(final int p_111271_1_) {
        if (FontRenderer.unicodePageLocations[p_111271_1_] == null) {
            FontRenderer.unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", p_111271_1_));
            FontRenderer.unicodePageLocations[p_111271_1_] = FontUtils.getHdFontLocation(FontRenderer.unicodePageLocations[p_111271_1_]);
        }
        return FontRenderer.unicodePageLocations[p_111271_1_];
    }
    
    private void loadGlyphTexture(final int p_78257_1_) {
        this.bindTexture(this.getUnicodePageLocation(p_78257_1_));
    }
    
    private float renderUnicodeChar(final char p_78277_1_, final boolean p_78277_2_) {
        if (this.glyphWidth[p_78277_1_] == 0) {
            return 0.0f;
        }
        final int i = p_78277_1_ / '\u0100';
        this.loadGlyphTexture(i);
        int j = this.glyphWidth[p_78277_1_] >>> 4;
        final int k = this.glyphWidth[p_78277_1_] & 0xF;
        j &= 0xF;
        final float f = (float)j;
        final float f2 = (float)(k + 1);
        final float f3 = p_78277_1_ % '\u0010' * 16 + f;
        final float f4 = (float)((p_78277_1_ & '\u00ff') / 16 * 16);
        final float f5 = f2 - f - 0.02f;
        final float f6 = p_78277_2_ ? 1.0f : 0.0f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f3 / 256.0f, f4 / 256.0f);
        GL11.glVertex3f(this.posX + f6, this.posY, 0.0f);
        GL11.glTexCoord2f(f3 / 256.0f, (f4 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX - f6, this.posY + 7.99f, 0.0f);
        GL11.glTexCoord2f((f3 + f5) / 256.0f, f4 / 256.0f);
        GL11.glVertex3f(this.posX + f5 / 2.0f + f6, this.posY, 0.0f);
        GL11.glTexCoord2f((f3 + f5) / 256.0f, (f4 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX + f5 / 2.0f - f6, this.posY + 7.99f, 0.0f);
        GL11.glEnd();
        return (f2 - f) / 2.0f + 1.0f;
    }
    
    public int drawStringWithShadow(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x, y, color, true);
    }
    
    public int drawString(final String text, final double x, final double y, final int color) {
        return this.enabled ? this.drawString(text, (float)x, (float)y, color, false) : 0;
    }
    
    public int drawString(final String text, final float x, final float y, final int color, final boolean dropShadow) {
        this.enableAlpha();
        this.resetStyles();
        int i;
        if (dropShadow) {
            i = this.renderString(text, x + 1.0f, y + 1.0f, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        }
        else {
            i = this.renderString(text, x, y, color, false);
        }
        return i;
    }
    
    private String bidiReorder(final String p_147647_1_) {
        try {
            final Bidi bidi = new Bidi(new ArabicShaping(8).shape(p_147647_1_), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException var3) {
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
        for (int i = 0; i < p_78255_1_.length(); ++i) {
            char c0 = p_78255_1_.charAt(i);
            if (c0 == '§' && i + 1 < p_78255_1_.length()) {
                int i2 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i + 1));
                if (i2 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (i2 < 0 || i2 > 15) {
                        i2 = 15;
                    }
                    if (p_78255_2_) {
                        i2 += 16;
                    }
                    int j1 = this.colorCode[i2];
                    if (Config.isCustomColors()) {
                        j1 = CustomColors.getTextColor(i2, j1);
                    }
                    this.textColor = j1;
                    this.setColor((j1 >> 16) / 255.0f, (j1 >> 8 & 0xFF) / 255.0f, (j1 & 0xFF) / 255.0f, this.alpha);
                }
                else if (i2 == 16) {
                    this.randomStyle = true;
                }
                else if (i2 == 17) {
                    this.boldStyle = true;
                }
                else if (i2 == 18) {
                    this.strikethroughStyle = true;
                }
                else if (i2 == 19) {
                    this.underlineStyle = true;
                }
                else if (i2 == 20) {
                    this.italicStyle = true;
                }
                else if (i2 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
            }
            else {
                int k = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(c0);
                if (this.randomStyle && k != -1) {
                    final int l = this.getCharWidth(c0);
                    char c2;
                    do {
                        k = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".length());
                        c2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".charAt(k);
                    } while (l != this.getCharWidth(c2));
                    c0 = c2;
                }
                final float f1 = (k != -1 && !this.unicodeFlag) ? this.offsetBold : 0.5f;
                final boolean flag = (c0 == '\0' || k == -1 || this.unicodeFlag) && p_78255_2_;
                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }
                float f2 = this.func_181559_a(c0, this.italicStyle);
                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }
                if (this.boldStyle) {
                    this.posX += f1;
                    if (flag) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }
                    this.func_181559_a(c0, this.italicStyle);
                    this.posX -= f1;
                    if (flag) {
                        this.posX += f1;
                        this.posY += f1;
                    }
                    f2 += f1;
                }
                if (this.strikethroughStyle) {
                    final Tessellator tessellator = Tessellator.getInstance();
                    final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldrenderer.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
                    worldrenderer.pos(this.posX + f2, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
                    worldrenderer.pos(this.posX + f2, this.posY + this.FONT_HEIGHT / 2 - 1.0f, 0.0).endVertex();
                    worldrenderer.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0f, 0.0).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                if (this.underlineStyle) {
                    final Tessellator tessellator2 = Tessellator.getInstance();
                    final WorldRenderer worldrenderer2 = tessellator2.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldrenderer2.begin(7, DefaultVertexFormats.POSITION);
                    final int m = this.underlineStyle ? -1 : 0;
                    worldrenderer2.pos(this.posX + m, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
                    worldrenderer2.pos(this.posX + f2, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
                    worldrenderer2.pos(this.posX + f2, this.posY + this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                    worldrenderer2.pos(this.posX + m, this.posY + this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                    tessellator2.draw();
                    GlStateManager.enableTexture2D();
                }
                this.posX += f2;
            }
        }
    }
    
    private int renderStringAligned(final String text, int x, final int y, final int p_78274_4_, final int color, final boolean dropShadow) {
        if (this.bidiFlag) {
            final int i = this.getStringWidth(this.bidiReorder(text));
            x = x + p_78274_4_ - i;
        }
        return this.renderString(text, (float)x, (float)y, color, dropShadow);
    }
    
    private int renderString(String text, final float x, final float y, int color, final boolean dropShadow) {
        if (Client.instance.moduleManager.getModuleName("NameProtect").isEnabled()) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer != null && Minecraft.getMinecraft().theWorld != null && text.contains(Minecraft.getMinecraft().session.getUsername())) {
                text = text.replace(Minecraft.getMinecraft().session.getUsername(), "§7[§8Hush§fUser§7]");
            }
        }
        if (text == null) {
            return 0;
        }
        if (this.bidiFlag) {
            text = this.bidiReorder(text);
        }
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        this.red = (color >> 16 & 0xFF) / 255.0f;
        this.blue = (color >> 8 & 0xFF) / 255.0f;
        this.green = (color & 0xFF) / 255.0f;
        this.alpha = (color >> 24 & 0xFF) / 255.0f;
        this.setColor(this.red, this.blue, this.green, this.alpha);
        this.posX = x;
        this.posY = y;
        this.renderStringAtPos(text, dropShadow);
        return (int)this.posX;
    }
    
    public int getStringWidth(final String text) {
        if (text == null) {
            return 0;
        }
        float f = 0.0f;
        boolean flag = false;
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            float f2 = this.getCharWidthFloat(c0);
            if (f2 < 0.0f && i < text.length() - 1) {
                ++i;
                c0 = text.charAt(i);
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                    }
                }
                else {
                    flag = true;
                }
                f2 = 0.0f;
            }
            f += f2;
            if (flag && f2 > 0.0f) {
                f += (this.unicodeFlag ? 1.0f : this.offsetBold);
            }
        }
        return (int)f;
    }
    
    public int getCharWidth(final char character) {
        return Math.round(this.getCharWidthFloat(character));
    }
    
    private float getCharWidthFloat(final char p_getCharWidthFloat_1_) {
        if (p_getCharWidthFloat_1_ == '§') {
            return -1.0f;
        }
        if (p_getCharWidthFloat_1_ == ' ') {
            return this.charWidth[32];
        }
        final int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(p_getCharWidthFloat_1_);
        if (p_getCharWidthFloat_1_ > '\0' && i != -1 && !this.unicodeFlag) {
            return this.charWidth[i];
        }
        if (this.glyphWidth[p_getCharWidthFloat_1_] != 0) {
            int j = this.glyphWidth[p_getCharWidthFloat_1_] >>> 4;
            int k = this.glyphWidth[p_getCharWidthFloat_1_] & 0xF;
            j &= 0xF;
            return (float)((++k - j) / 2 + 1);
        }
        return 0.0f;
    }
    
    public String trimStringToWidth(final String text, final int width) {
        return this.trimStringToWidth(text, width, false);
    }
    
    public String trimStringToWidth(final String text, final int width, final boolean reverse) {
        final StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0f;
        final int i = reverse ? (text.length() - 1) : 0;
        final int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag2 = false;
        for (int k = i; k >= 0 && k < text.length() && f < width; k += j) {
            final char c0 = text.charAt(k);
            final float f2 = this.getCharWidthFloat(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag2 = false;
                    }
                }
                else {
                    flag2 = true;
                }
            }
            else if (f2 < 0.0f) {
                flag = true;
            }
            else {
                f += f2;
                if (flag2) {
                    ++f;
                }
            }
            if (f > width) {
                break;
            }
            if (reverse) {
                stringbuilder.insert(0, c0);
            }
            else {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }
    
    private String trimStringNewline(String text) {
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }
    
    public void drawSplitString(String str, final int x, final int y, final int wrapWidth, final int textColor) {
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, false);
    }
    
    private void renderSplitString(final String str, final int x, int y, final int wrapWidth, final boolean addShadow) {
        for (final Object s : this.listFormattedStringToWidth(str, wrapWidth)) {
            this.renderStringAligned((String)s, x, y, wrapWidth, this.textColor, addShadow);
            y += this.FONT_HEIGHT;
        }
    }
    
    public int splitStringWidth(final String p_78267_1_, final int p_78267_2_) {
        return this.FONT_HEIGHT * this.listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
    }
    
    public void setUnicodeFlag(final boolean unicodeFlagIn) {
        this.unicodeFlag = unicodeFlagIn;
    }
    
    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }
    
    public void setBidiFlag(final boolean bidiFlagIn) {
        this.bidiFlag = bidiFlagIn;
    }
    
    public List listFormattedStringToWidth(final String str, final int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }
    
    String wrapFormattedStringToWidth(final String str, final int wrapWidth) {
        final int i = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= i) {
            return str;
        }
        final String s = str.substring(0, i);
        final char c0 = str.charAt(i);
        final boolean flag = c0 == ' ' || c0 == '\n';
        final String s2 = String.valueOf(getFormatFromString(s)) + str.substring(i + (flag ? 1 : 0));
        return String.valueOf(s) + "\n" + this.wrapFormattedStringToWidth(s2, wrapWidth);
    }
    
    private int sizeStringToWidth(final String str, final int wrapWidth) {
        final int i = str.length();
        float f = 0.0f;
        int j = 0;
        int k = -1;
        boolean flag = false;
        while (j < i) {
            final char c0 = str.charAt(j);
            Label_0163: {
                switch (c0) {
                    case '\n': {
                        --j;
                        break Label_0163;
                    }
                    case ' ': {
                        k = j;
                        break;
                    }
                    case '§': {
                        if (j >= i - 1) {
                            break Label_0163;
                        }
                        ++j;
                        final char c2 = str.charAt(j);
                        if (c2 == 'l' || c2 == 'L') {
                            flag = true;
                            break Label_0163;
                        }
                        if (c2 == 'r' || c2 == 'R' || isFormatColor(c2)) {
                            flag = false;
                        }
                        break Label_0163;
                    }
                }
                f += this.getCharWidthFloat(c0);
                if (flag) {
                    ++f;
                }
            }
            if (c0 == '\n') {
                k = ++j;
                break;
            }
            if (f > wrapWidth) {
                break;
            }
            ++j;
        }
        return (j != i && k != -1 && k < j) ? k : j;
    }
    
    private static boolean isFormatColor(final char colorChar) {
        return (colorChar >= '0' && colorChar <= '9') || (colorChar >= 'a' && colorChar <= 'f') || (colorChar >= 'A' && colorChar <= 'F');
    }
    
    private static boolean isFormatSpecial(final char formatChar) {
        return (formatChar >= 'k' && formatChar <= 'o') || (formatChar >= 'K' && formatChar <= 'O') || formatChar == 'r' || formatChar == 'R';
    }
    
    public static String getFormatFromString(final String text) {
        String s = "";
        int i = -1;
        final int j = text.length();
        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                final char c0 = text.charAt(i + 1);
                if (isFormatColor(c0)) {
                    s = "§" + c0;
                }
                else {
                    if (!isFormatSpecial(c0)) {
                        continue;
                    }
                    s = String.valueOf(s) + "§" + c0;
                }
            }
        }
        return s;
    }
    
    public boolean getBidiFlag() {
        return this.bidiFlag;
    }
    
    public int getColorCode(final char character) {
        final int i = "0123456789abcdef".indexOf(character);
        if (i >= 0 && i < this.colorCode.length) {
            int j = this.colorCode[i];
            if (Config.isCustomColors()) {
                j = CustomColors.getTextColor(i, j);
            }
            return j;
        }
        return 16777215;
    }
    
    protected void setColor(final float p_setColor_1_, final float p_setColor_2_, final float p_setColor_3_, final float p_setColor_4_) {
        GlStateManager.color(p_setColor_1_, p_setColor_2_, p_setColor_3_, p_setColor_4_);
    }
    
    protected void enableAlpha() {
        GlStateManager.enableAlpha();
    }
    
    protected void bindTexture(final ResourceLocation p_bindTexture_1_) {
        this.renderEngine.bindTexture(p_bindTexture_1_);
    }
    
    protected InputStream getResourceInputStream(final ResourceLocation p_getResourceInputStream_1_) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(p_getResourceInputStream_1_).getInputStream();
    }
    
    public void func_175063_a(final String s, final int mwidth, final int mheight, final int i) {
    }
}
