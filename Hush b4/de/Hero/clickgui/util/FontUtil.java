// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.clickgui.util;

import net.minecraft.util.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class FontUtil
{
    private static FontRenderer fontRenderer;
    
    public static void setupFontUtils() {
        FontUtil.fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    }
    
    public static int getStringWidth(final String text) {
        return FontUtil.fontRenderer.getStringWidth(StringUtils.stripControlCodes(text));
    }
    
    public static int getFontHeight() {
        return FontUtil.fontRenderer.FONT_HEIGHT;
    }
    
    public static void drawString(final String text, final double x, final double y, final int color) {
        FontUtil.fontRenderer.drawString(text, x, y, color);
    }
    
    public static void drawStringWithShadow(final String text, final double x, final double y, final int color) {
        FontUtil.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public static void drawCenteredString(final String text, final double x, final double y, final int color) {
        drawString(text, x - FontUtil.fontRenderer.getStringWidth(text) / 2, y, color);
    }
    
    public static void drawCenteredStringWithShadow(final String text, final double x, final double y, final int color) {
        drawStringWithShadow(text, x - FontUtil.fontRenderer.getStringWidth(text) / 2, y, color);
    }
    
    public static void drawTotalCenteredString(final String text, final double x, final double y, final int color) {
        drawString(text, x - FontUtil.fontRenderer.getStringWidth(text) / 2, y - FontUtil.fontRenderer.FONT_HEIGHT / 2, color);
    }
    
    public static void drawTotalCenteredStringWithShadow(final String text, final double x, final double y, final int color) {
        drawStringWithShadow(text, x - FontUtil.fontRenderer.getStringWidth(text) / 2, y - FontUtil.fontRenderer.FONT_HEIGHT / 2.0f, color);
    }
}
