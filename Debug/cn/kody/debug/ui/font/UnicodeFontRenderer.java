package cn.kody.debug.ui.font;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import cn.kody.debug.ui.Notification;
import cn.kody.debug.utils.color.Colors;

public class UnicodeFontRenderer
extends FontRenderer {
    private final UnicodeFont font;
    public HashMap<String, Integer> widthMap;
    public HashMap<String, Integer> heightMap;
    
    
    public UnicodeFontRenderer(final Font awtFont, final float n, final int p_addGlyphs_1_, final int p_addGlyphs_2_, final boolean b) {
	super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.widthMap = new HashMap<String, Integer>();
        this.heightMap = new HashMap<String, Integer>();
        (this.font = new UnicodeFont(awtFont)).addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        if (p_addGlyphs_2_ > -1 && p_addGlyphs_1_ > -1) {
            this.font.addGlyphs(p_addGlyphs_1_, p_addGlyphs_2_);
        }
        if (b) {
            this.font.addGlyphs(0, 65535);
        }
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException class1245) {
            throw new RuntimeException(class1245);
        }
        this.FONT_HEIGHT = this.font.getHeight("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789") / 2;
    }
    public int drawStringWithColor(String text, float x, float y, int color, int alpha) {
        String[] array;
        text = "\u00a7r" + text;
        float len = -1.0f;
        for (String str : array = text.split("\u00a7")) {
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            this.drawString(str, x + len + 0.5f, y + 0.5f, Colors.BLACK.c);
            this.drawString(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), alpha));
            len += (float)(this.getStringWidth(str) + 1);
        }
        return (int)len;
    }
    public int getColor(int red, int green, int blue, int alpha) {
        byte color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        color1 |= blue;
        return color1;
     }
    
    public int drawString(String string, float x, float y, int color) {
        if (string == null) {
            return 0;
        }
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean lighting = GL11.glIsEnabled((int)2896);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (lighting) {
            GL11.glDisable((int)2896);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        this.font.drawString(x *= 2.0f, y *= 2.0f, string, new org.newdawn.slick.Color(color));
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (lighting) {
            GL11.glEnable((int)2896);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glPopMatrix();
        GlStateManager.bindCurrentTexture();
        return (int)x;
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x + 1.0f, y + 1.0f, -16777216);
        return this.drawString(text, x, y, color);
    }

    @Override
    public int getCharWidth(char c) {
        return this.getStringWidth(Character.toString(c));
    }

    @Override
    public int getStringWidth(String string) {
        return this.font.getWidth(string) / 2;
    }

    public int getStringHeight(String string) {
        return this.font.getHeight(string) / 2;
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }
    public int drawStringWithShadowWithAlpha(final String s, final float n, final float n2, final int n3, final float n4) {
        this.drawString(s, n + 0.5f, n2 + 0.5f, Notification.reAlpha(Colors.BLACK.c, n4));
        return this.drawString(s, n, n2, Notification.reAlpha(n3, n4));
    }
    
}

