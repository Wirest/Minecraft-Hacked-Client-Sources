package com.mentalfrostbyte.jello.ttf;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class FontUtils
{
    private static final Tessellator tessellator = Tessellator.getInstance();
    
    
    public static void drawTextureRect(final float x, final float y, final float width, final float height, final float u, final float v, final float t, final float s) {
        final WorldRenderer renderer = FontUtils.tessellator.getWorldRenderer();
        renderer.startDrawing(4);
        renderer.addVertexWithUV((double)(x + width), (double)y, 0.0, (double)t, (double)v);
        renderer.addVertexWithUV((double)x, (double)y, 0.0, (double)u, (double)v);
        renderer.addVertexWithUV((double)x, (double)(y + height), 0.0, (double)u, (double)s);
        renderer.addVertexWithUV((double)x, (double)(y + height), 0.0, (double)u, (double)s);
        renderer.addVertexWithUV((double)(x + width), (double)(y + height), 0.0, (double)t, (double)s);
        renderer.addVertexWithUV((double)(x + width), (double)y, 0.0, (double)t, (double)v);
        FontUtils.tessellator.draw();
    }
    
    public static void drawTextureRect(final float x, final float y, final float width, final float height, final float u, final float v, final float t, final float s, final float z) {
        final WorldRenderer renderer = FontUtils.tessellator.getWorldRenderer();
        renderer.startDrawing(4);
        renderer.addVertexWithUV((double)(x + width), (double)y, (double)z, (double)t, (double)v);
        renderer.addVertexWithUV((double)x, (double)y, (double)z, (double)u, (double)v);
        renderer.addVertexWithUV((double)x, (double)(y + height), (double)z, (double)u, (double)s);
        renderer.addVertexWithUV((double)x, (double)(y + height), (double)z, (double)u, (double)s);
        renderer.addVertexWithUV((double)(x + width), (double)(y + height), (double)z, (double)t, (double)s);
        renderer.addVertexWithUV((double)(x + width), (double)y, (double)z, (double)t, (double)v);
        FontUtils.tessellator.draw();
    }
}
