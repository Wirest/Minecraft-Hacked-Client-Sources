/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.memewaredevs.altmanager;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
    public static void drawOutlinedString(String str, float x, float y2, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRendererObj.drawString(str, x - 0.3f, y2, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x + 0.3f, y2, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x, y2 + 0.3f, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x, y2 - 0.3f, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x, y2, color);
    }

    public static void drawFancy(double d, double e, double f2, double f3, int paramColor) {
        float alpha = (float) (paramColor >> 24 & 255) / 255.0f;
        float red = (float) (paramColor >> 16 & 255) / 255.0f;
        float green = (float) (paramColor >> 8 & 255) / 255.0f;
        float blue = (float) (paramColor & 255) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glEnable((int) 2848);
        GL11.glEnable((int) 2881);
        GL11.glEnable((int) 2832);
        GL11.glEnable((int) 3042);
        GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
        GL11.glBegin((int) 7);
        GL11.glVertex2d((double) (f2 + 1.300000011920929), (double) e);
        GL11.glVertex2d((double) (d + 1.0), (double) e);
        GL11.glVertex2d((double) (d - 1.300000011920929), (double) f3);
        GL11.glVertex2d((double) (f2 - 1.0), (double) f3);
        GL11.glEnd();
        GL11.glDisable((int) 2848);
        GL11.glDisable((int) 2881);
        GL11.glDisable((int) 2832);
        GL11.glDisable((int) 3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawGradient(double x, double y2, double x2, double y22, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0f;
        float f2 = (float) (col1 >> 16 & 255) / 255.0f;
        float f3 = (float) (col1 >> 8 & 255) / 255.0f;
        float f4 = (float) (col1 & 255) / 255.0f;
        float f5 = (float) (col2 >> 24 & 255) / 255.0f;
        float f6 = (float) (col2 >> 16 & 255) / 255.0f;
        float f7 = (float) (col2 >> 8 & 255) / 255.0f;
        float f8 = (float) (col2 & 255) / 255.0f;
        GL11.glEnable((int) 3042);
        GL11.glDisable((int) 3553);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glEnable((int) 2848);
        GL11.glShadeModel((int) 7425);
        GL11.glPushMatrix();
        GL11.glBegin((int) 7);
        GL11.glColor4f((float) f2, (float) f3, (float) f4, (float) f);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glColor4f((float) f6, (float) f7, (float) f8, (float) f5);
        GL11.glVertex2d((double) x, (double) y22);
        GL11.glVertex2d((double) x2, (double) y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        GL11.glDisable((int) 2848);
        GL11.glShadeModel((int) 7424);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0f;
        float f2 = (float) (col1 >> 16 & 255) / 255.0f;
        float f3 = (float) (col1 >> 8 & 255) / 255.0f;
        float f4 = (float) (col1 & 255) / 255.0f;
        float f5 = (float) (col2 >> 24 & 255) / 255.0f;
        float f6 = (float) (col2 >> 16 & 255) / 255.0f;
        float f7 = (float) (col2 >> 8 & 255) / 255.0f;
        float f8 = (float) (col2 & 255) / 255.0f;
        GL11.glEnable((int) 3042);
        GL11.glDisable((int) 3553);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glEnable((int) 2848);
        GL11.glShadeModel((int) 7425);
        GL11.glPushMatrix();
        GL11.glBegin((int) 7);
        GL11.glColor4f((float) f2, (float) f3, (float) f4, (float) f);
        GL11.glVertex2d((double) left, (double) top);
        GL11.glVertex2d((double) left, (double) bottom);
        GL11.glColor4f((float) f6, (float) f7, (float) f8, (float) f5);
        GL11.glVertex2d((double) right, (double) bottom);
        GL11.glVertex2d((double) right, (double) top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        GL11.glDisable((int) 2848);
        GL11.glShadeModel((int) 7424);
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var6 = (float) (color >> 24 & 255) / 255.0f;
        float var7 = (float) (color >> 16 & 255) / 255.0f;
        float var8 = (float) (color >> 8 & 255) / 255.0f;
        float var9 = (float) (color & 255) / 255.0f;
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
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

    public static void rectangleBordered(double x, double y2, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtil.rectangle(x + width, y2 + width, x1 - width, y1 - width, internalColor);
        RenderUtil.rectangle(x + width, y2, x1 - width, y2 + width, borderColor);
        RenderUtil.rectangle(x, y2, x + width, y1, borderColor);
        RenderUtil.rectangle(x1 - width, y2, x1, y1, borderColor);
        RenderUtil.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static void filledBox(AxisAlignedBB boundingBox, int color, boolean shouldColor) {
        GlStateManager.pushMatrix();
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var12 = (float) (color >> 16 & 255) / 255.0f;
        float var13 = (float) (color >> 8 & 255) / 255.0f;
        float var14 = (float) (color & 255) / 255.0f;
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        if (shouldColor) {
            GlStateManager.color(var12, var13, var14, var11);
        }
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
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

    public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator var1 = Tessellator.getInstance();
        WorldRenderer var2 = var1.getWorldRenderer();
        var2.startDrawing(3);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        var1.draw();
        var2.startDrawing(3);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        var1.draw();
        var2.startDrawing(1);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        var1.draw();
    }

    public static void drawLines(AxisAlignedBB boundingBox) {
        GL11.glPushMatrix();
        GL11.glBegin((int) 2);
        GL11.glVertex3d((double) boundingBox.minX, (double) boundingBox.minY, (double) boundingBox.minZ);
        GL11.glVertex3d((double) boundingBox.minX, (double) boundingBox.maxY, (double) boundingBox.maxZ);
        GL11.glVertex3d((double) boundingBox.maxX, (double) boundingBox.minY, (double) boundingBox.minZ);
        GL11.glVertex3d((double) boundingBox.maxX, (double) boundingBox.maxY, (double) boundingBox.maxZ);
        GL11.glVertex3d((double) boundingBox.maxX, (double) boundingBox.minY, (double) boundingBox.maxZ);
        GL11.glVertex3d((double) boundingBox.minX, (double) boundingBox.maxY, (double) boundingBox.maxZ);
        GL11.glVertex3d((double) boundingBox.maxX, (double) boundingBox.minY, (double) boundingBox.minZ);
        GL11.glVertex3d((double) boundingBox.minX, (double) boundingBox.maxY, (double) boundingBox.minZ);
        GL11.glVertex3d((double) boundingBox.maxX, (double) boundingBox.minY, (double) boundingBox.minZ);
        GL11.glVertex3d((double) boundingBox.minX, (double) boundingBox.minY, (double) boundingBox.maxZ);
        GL11.glVertex3d((double) boundingBox.maxX, (double) boundingBox.maxY, (double) boundingBox.minZ);
        GL11.glVertex3d((double) boundingBox.minX, (double) boundingBox.maxY, (double) boundingBox.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawBoundingBox(AxisAlignedBB axisalignedbb) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrender = Tessellator.getInstance().getWorldRenderer();
        worldrender.startDrawingQuads();
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.draw();
    }

    public static void drawLine3D(float x, float y2, float z, float x1, float y1, float z1, int color) {
        RenderUtil.pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var12 = (float) (color >> 16 & 255) / 255.0f;
        float var13 = (float) (color >> 8 & 255) / 255.0f;
        float var14 = (float) (color & 255) / 255.0f;
        GL11.glColor4f((float) var12, (float) var13, (float) var14, (float) var11);
        GL11.glLineWidth((float) 0.5f);
        GL11.glBegin((int) 3);
        GL11.glVertex3d((double) x, (double) y2, (double) z);
        GL11.glVertex3d((double) x1, (double) y1, (double) z1);
        GL11.glEnd();
        RenderUtil.post3D();
    }

    public static void draw3DLine(float x, float y2, float z, int color) {
        RenderUtil.pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var12 = (float) (color >> 16 & 255) / 255.0f;
        float var13 = (float) (color >> 8 & 255) / 255.0f;
        float var14 = (float) (color & 255) / 255.0f;
        GL11.glColor4f((float) var12, (float) var13, (float) var14, (float) var11);
        GL11.glLineWidth((float) 0.5f);
        GL11.glBegin((int) 3);
        GL11.glVertex3d((double) 0.0, (double) Minecraft.getMinecraft().thePlayer.getEyeHeight(), (double) 0.0);
        GL11.glVertex3d((double) x, (double) y2, (double) z);
        GL11.glEnd();
        RenderUtil.post3D();
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int) 3042);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glShadeModel((int) 7425);
        GL11.glDisable((int) 3553);
        GL11.glEnable((int) 2848);
        GL11.glDisable((int) 2929);
        GL11.glDisable((int) 2896);
        GL11.glDepthMask((boolean) false);
        GL11.glHint((int) 3154, (int) 4354);
    }

    public static void post3D() {
        GL11.glDepthMask((boolean) true);
        GL11.glEnable((int) 2929);
        GL11.glDisable((int) 2848);
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float) redRGB;
        float green = 0.003921569f * (float) greenRGB;
        float blue = 0.003921569f * (float) blueRGB;
        GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
    }

    public static void drawRect(float x, float y2, float x1, float y1) {
        GL11.glBegin((int) 7);
        GL11.glVertex2f((float) x, (float) y1);
        GL11.glVertex2f((float) x1, (float) y1);
        GL11.glVertex2f((float) x1, (float) y2);
        GL11.glVertex2f((float) x, (float) y2);
        GL11.glEnd();
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0f;
        float red = (float) (hex >> 16 & 255) / 255.0f;
        float green = (float) (hex >> 8 & 255) / 255.0f;
        float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
    }

    public static void drawRect(float x, float y2, float x1, float y1, int color) {
        RenderUtil.enableGL2D();
        RenderUtil.glColor(color);
        RenderUtil.drawRect(x, y2, x1, y1);
        RenderUtil.disableGL2D();
    }

    public static void drawRoundedRect(float x, float y2, float x1, float y1, int borderC, int insideC) {
        RenderUtil.drawRect(x + 0.5f, y2, x1 - 0.5f, y2 + 0.5f, insideC);
        RenderUtil.drawRect(x + 0.5f, y1 - 0.5f, x1 - 0.5f, y1, insideC);
        RenderUtil.drawRect(x, y2 + 0.5f, x1, y1 - 0.5f, insideC);
    }

    public static void drawHLine(float x, float y2, float x1, int y1) {
        if (y2 < x) {
            float var5 = x;
            x = y2;
            y2 = var5;
        }
        RenderUtil.drawRect(x, x1, y2 + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x, float y2, float x1, float y1, float width, int color) {
        if (width <= 0.0f) {
            return;
        }
        GL11.glPushMatrix();
        RenderUtil.pre3D();
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var12 = (float) (color >> 16 & 255) / 255.0f;
        float var13 = (float) (color >> 8 & 255) / 255.0f;
        float var14 = (float) (color & 255) / 255.0f;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        int shade = GL11.glGetInteger((int) 2900);
        GlStateManager.shadeModel(7425);
        GL11.glColor4f((float) var12, (float) var13, (float) var14, (float) var11);
        float line = GL11.glGetFloat((int) 2849);
        GL11.glLineWidth((float) width);
        GL11.glBegin((int) 3);
        GL11.glVertex3d((double) x, (double) y2, (double) 0.0);
        GL11.glVertex3d((double) x1, (double) y1, (double) 0.0);
        GL11.glEnd();
        GlStateManager.shadeModel(shade);
        GL11.glLineWidth((float) line);
        RenderUtil.post3D();
        GL11.glPopMatrix();
    }

    public static void enableGL2D() {
        GL11.glDisable((int) 2929);
        GL11.glEnable((int) 3042);
        GL11.glDisable((int) 3553);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glDepthMask((boolean) true);
        GL11.glEnable((int) 2848);
        GL11.glHint((int) 3154, (int) 4354);
        GL11.glHint((int) 3155, (int) 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        GL11.glEnable((int) 2929);
        GL11.glDisable((int) 2848);
        GL11.glHint((int) 3154, (int) 4352);
        GL11.glHint((int) 3155, (int) 4352);
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c2) {
        float f = (c2 >> 24 & 255) / 255.0F;
        float f1 = (c2 >> 16 & 255) / 255.0F;
        float f2 = (c2 >> 8 & 255) / 255.0F;
        float f3 = (c2 & 255) / 255.0F;
        // Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        // GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(cx, cy); // center of circle
        for (int i = num_segments; i >= 0; i--) {
            double theta = i * (Math.PI * 2) / num_segments;
            GL11.glVertex2d(cx + r * Math.cos(theta), cy + r * Math.sin(theta));
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawBorderedCircle(float circleX, float circleY, double radius, double width, int borderColor, int innerColor) {
        RenderUtil.enableGL2D();
        GlStateManager.enableBlend();
        GL11.glEnable((int) 2881);
        RenderUtil.drawCircle(circleX, circleY, (float) (radius - 0.5 + width), 72, borderColor);
        RenderUtil.drawFullCircle(circleX, circleY, (float) radius, innerColor);
        GlStateManager.disableBlend();
        GL11.glDisable((int) 2881);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.disableGL2D();
    }

    public static void drawCircleNew(float x, float y2, float radius, int numberOfSides) {
    }

    public static void drawFullCircle(float cx, float cy, float r, int c2) {
        float x;
        cx *= 2.0f;
        cy *= 2.0f;
        float p2 = (float) Math.cos(0.19634953141212463);
        float s = (float) Math.sin(0.19634953141212463);
        r = x = r * 2.0f;
        float y2 = 0.0f;
        RenderUtil.enableGL2D();
        GL11.glEnable((int) 2848);
        GL11.glHint((int) 3154, (int) 4354);
        GL11.glEnable((int) 3024);
        GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
        RenderUtil.glColor(c2);
        GL11.glBegin((int) 9);
        for (int ii = 0; ii < 32; ++ii) {
            GL11.glVertex2f((float) (x + cx), (float) (y2 + cy));
            float t = x;
            x = p2 * x - s * y2;
            y2 = s * t + p2 * y2;
        }
        GL11.glEnd();
        GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.disableGL2D();
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float) ((float) color.getRed() / 255.0f), (float) ((float) color.getGreen() / 255.0f), (float) ((float) color.getBlue() / 255.0f), (float) ((float) color.getAlpha() / 255.0f));
    }

    public static void drawCircle(int x, int y2, double r, int c2) {
        float f = (float) (c2 >> 24 & 255) / 255.0f;
        float f2 = (float) (c2 >> 16 & 255) / 255.0f;
        float f3 = (float) (c2 >> 8 & 255) / 255.0f;
        float f4 = (float) (c2 & 255) / 255.0f;
        GL11.glEnable((int) 3042);
        GL11.glDisable((int) 3553);
        GL11.glEnable((int) 2848);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glColor4f((float) f2, (float) f3, (float) f4, (float) f);
        GL11.glBegin((int) 2);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin((double) i * 3.141592653589793 / 180.0) * r;
            double y22 = Math.cos((double) i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d((double) ((double) x + x2), (double) ((double) y2 + y22));
        }
        GL11.glEnd();
        GL11.glDisable((int) 2848);
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
    }

    public static void drawTracerLine(double x, double y2, double z, Color color, float alpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int) 3042);
        GL11.glEnable((int) 2848);
        GL11.glDisable((int) 2929);
        GL11.glDisable((int) 3553);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glEnable((int) 3042);
        GL11.glLineWidth((float) lineWdith);
        RenderUtil.glColor(color);
        GL11.glBegin((int) 2);
        Minecraft.getMinecraft();
        GL11.glVertex3d((double) 0.0, (double) (0.0 + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()), (double) 0.0);
        GL11.glVertex3d((double) x, (double) y2, (double) z);
        GL11.glEnd();
        GL11.glDisable((int) 3042);
        GL11.glEnable((int) 3553);
        GL11.glEnable((int) 2929);
        GL11.glDisable((int) 2848);
        GL11.glDisable((int) 3042);
        GL11.glPopMatrix();
    }
}

