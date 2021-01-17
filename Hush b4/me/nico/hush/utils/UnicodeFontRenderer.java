// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.awt.Font;
import org.newdawn.slick.UnicodeFont;
import net.minecraft.client.gui.FontRenderer;

public class UnicodeFontRenderer extends FontRenderer
{
    private final UnicodeFont font;
    
    public UnicodeFontRenderer(final Font awtFont) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        (this.font = new UnicodeFont(awtFont)).addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException exception) {
            throw new RuntimeException(exception);
        }
        final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }
    
    public int drawString(final String string, int x, int y, final int color) {
        if (string == null) {
            return 0;
        }
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        final boolean blend = GL11.glIsEnabled(3042);
        final boolean lighting = GL11.glIsEnabled(2896);
        final boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (lighting) {
            GL11.glDisable(2896);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        x *= 2;
        y *= 2;
        this.font.drawString((float)x, (float)y, string);
        if (texture) {
            GL11.glEnable(3553);
        }
        if (lighting) {
            GL11.glEnable(2896);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
        GL11.glPopMatrix();
        return x;
    }
    
    @Override
    public int getCharWidth(final char c) {
        return this.getStringWidth(Character.toString(c));
    }
    
    @Override
    public int getStringWidth(final String string) {
        return this.font.getWidth(string) / 2;
    }
    
    public int getStringHeight(final String string) {
        return this.font.getHeight(string) / 2;
    }
}
