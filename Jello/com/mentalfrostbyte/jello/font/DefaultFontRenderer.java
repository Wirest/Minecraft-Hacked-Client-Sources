package com.mentalfrostbyte.jello.font;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class DefaultFontRenderer
extends JelloFontRenderer {
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    @Override
    public int drawString(String text, double x2, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return this.fontRenderer.drawString(text, (int)x2, (int)y2, color);
    }

    @Override
    public int drawStringWithShadow(String text, double x2, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return this.fontRenderer.drawStringWithShadow(text, (float) x2, y2, color);
    }

    @Override
    public double getStringWidth(String text) {
        text = text.replaceAll("\u00c3\u201a", "");
        return this.fontRenderer.getStringWidth(text);
    }

    @Override
    public int getHeight() {
        return this.fontRenderer.FONT_HEIGHT;
    }
}

