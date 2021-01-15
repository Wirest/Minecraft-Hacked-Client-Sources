// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.utils;

import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public final class RenderUtils
{
    public static void drawEsp(final EntityLivingBase ent, final float pTicks, final int hexColor, final int hexColorIn) {
        if (!ent.isEntityAlive()) {
            return;
        }
        final double x = getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
        final double y = getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
        final double z = getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
        boundingBox(ent, x, y, z, hexColor, hexColorIn);
    }
    
    public static void boundingBox(final Entity entity, final double x, final double y, final double z, final int color, final int colorIn) {
        GlStateManager.pushMatrix();
        GL11.glLineWidth(1.0f);
        final AxisAlignedBB var11 = entity.getEntityBoundingBox();
        final AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
        if (color != 0) {
            GlStateManager.disableDepth();
            filledBox(var12, colorIn, true);
            GlStateManager.disableLighting();
            RenderGlobal.drawOutlinedBoundingBox(var12, color);
        }
        GlStateManager.popMatrix();
    }
    
    public static void filledBox(final AxisAlignedBB boundingBox, final int color, final boolean shouldColor) {
        GlStateManager.pushMatrix();
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        if (shouldColor) {
            GlStateManager.color(var12, var13, var14, var11);
        }
        final byte draw = 7;
        worldRenderer.startDrawing(draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(draw);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }
    
    public static void rectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(left, bottom, 0.0);
        worldRenderer.addVertex(right, bottom, 0.0);
        worldRenderer.addVertex(right, top, 0.0);
        worldRenderer.addVertex(left, top, 0.0);
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor, final Side side) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        if (!side.equals(Side.Top)) {
            rectangle(x + width, y, x1 - width, y + width, borderColor);
        }
        if (!side.equals(Side.Left)) {
            rectangle(x, y, x + width, y1, borderColor);
        }
        if (!side.equals(Side.Right)) {
            rectangle(x1 - width, y, x1, y1, borderColor);
        }
        if (!side.equals(Side.Bottom)) {
            rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        }
    }
    
    public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        rectangle(x, y, x + width, y1, borderColor);
        rectangle(x1 - width, y, x1, y1, borderColor);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }
    
    public static void rectangleGradient(final double x1, final double y1, final double x2, final double y2, final int[] color) {
        final float[] r = new float[color.length];
        final float[] g = new float[color.length];
        final float[] b = new float[color.length];
        final float[] a = new float[color.length];
        for (int i = 0; i < color.length; ++i) {
            r[i] = (color[i] >> 16 & 0xFF) / 255.0f;
            g[i] = (color[i] >> 8 & 0xFF) / 255.0f;
            b[i] = (color[i] & 0xFF) / 255.0f;
            a[i] = (color[i] >> 24 & 0xFF) / 255.0f;
        }
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.shadeModel(7425);
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        worldRenderer.startDrawingQuads();
        worldRenderer.setColorRGBA_F(r[0], g[0], b[0], a[0]);
        worldRenderer.addVertex(x2, y1, 0.0);
        worldRenderer.setColorRGBA_F(r[1], g[1], b[1], a[1]);
        worldRenderer.addVertex(x1, y1, 0.0);
        worldRenderer.setColorRGBA_F(r[2], g[2], b[2], a[2]);
        worldRenderer.addVertex(x1, y2, 0.0);
        worldRenderer.setColorRGBA_F(r[3], g[3], b[3], a[3]);
        worldRenderer.addVertex(x2, y2, 0.0);
        Tessellator.getInstance().draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void rectangleOutlinedGradient(final double x1, final double y1, final double x2, final double y2, final int[] color, final double width) {
        rectangleGradient(x1, y1, x2, y1 + width, new int[] { color[0], color[1], color[0], color[1] });
        rectangleGradient(x1, y2 - width, x2, y2, new int[] { color[2], color[3], color[2], color[3] });
        rectangleGradient(x1, y1 + width, x1 + width, y2 - width, color);
        rectangleGradient(x2 - width, y1 + width, x2, y2 - width, color);
    }
    
    public static void rectangleBorderedGradient(final double x1, final double y1, final double x2, final double y2, final int[] fill, final int[] outline, final double width) {
        rectangleOutlinedGradient(x1, y1, x2, y2, outline, width);
        rectangleGradient(x1 + width, y1 + width, x2 - width, y2 - width, fill);
    }
    
    public static int blend(final int color1, final int color2, final float perc) {
        final Color x = new Color(color1);
        final Color y = new Color(color2);
        final float inverse_blending = 1.0f - perc;
        final float red = x.getRed() * perc + y.getRed() * inverse_blending;
        final float green = x.getGreen() * perc + y.getGreen() * inverse_blending;
        final float blue = x.getBlue() * perc + y.getBlue() * inverse_blending;
        Color blended;
        try {
            blended = new Color(red / 255.0f, green / 255.0f, blue / 255.0f);
        }
        catch (Exception e) {
            blended = new Color(-1);
        }
        return blended.getRGB();
    }
    
    public static void enableGL3D(final float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }
    
    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static double getDiff(final double lastI, final double i, final float ticks, final double ownI) {
        return lastI + (i - lastI) * ticks - ownI;
    }
    
    public enum Side
    {
        Top("Top", 0), 
        Right("Right", 1), 
        Bottom("Bottom", 2), 
        Left("Left", 3), 
        None("None", 4);
        
        private Side(final String s, final int n) {
        }
    }
}
