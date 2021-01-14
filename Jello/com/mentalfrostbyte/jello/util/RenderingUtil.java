package com.mentalfrostbyte.jello.util;

import net.minecraft.client.*;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;

public class RenderingUtil
{
    /*public static void drawOutlinedString(final String str, final float x, final float y, final int color) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.fontRendererObj.drawString(str, x - 0.3f, y, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x + 0.3f, y, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x, y + 0.3f, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x, y - 0.3f, Colors.getColor(0));
        mc.fontRendererObj.drawString(str, x, y, color);
    }*/
	
	public static void draw2RoundedRect(float x2, float y2, float x1, float y1, int borderC, int insideC) {
        RenderingUtil.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RenderingUtil.drawVLine(x2 *= 2.0f, (y2 *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        RenderingUtil.drawVLine((x1 *= 2.0f) - 1.0f, y2 + 1.0f, y1 - 2.0f, borderC);
        RenderingUtil.drawHLine(x2 + 2.0f, x1 - 3.0f, y2, borderC);
        RenderingUtil.drawHLine(x2 + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        RenderingUtil.drawHLine(x2 + 1.0f, x2 + 1.0f, y2 + 1.0f, borderC);
        RenderingUtil.drawHLine(x1 - 2.0f, x1 - 2.0f, y2 + 1.0f, borderC);
        RenderingUtil.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        RenderingUtil.drawHLine(x2 + 1.0f, x2 + 1.0f, y1 - 2.0f, borderC);
        RenderingUtil.drawRect(x2 + 1.0f, y2 + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        RenderingUtil.disableGL2D();
    }
    
    public static void drawFancy(final double d, final double e, final double f2, final double f3, final int paramColor) {
        final float alpha = (paramColor >> 24 & 0xFF) / 255.0f;
        final float red = (paramColor >> 16 & 0xFF) / 255.0f;
        final float green = (paramColor >> 8 & 0xFF) / 255.0f;
        final float blue = (paramColor & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        //GlStateManager.disableTextures();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2d(f2 + 1.300000011920929, e);
        GL11.glVertex2d(d + 1.0, e);
        GL11.glVertex2d(d - 1.300000011920929, f3);
        GL11.glVertex2d(f2 - 1.0, f3);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glDisable(2832);
        GL11.glDisable(3042);
       // GlStateManager.enableTextures();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
    
    public static void drawGradient(final double x, final double y, final double x2, final double y2, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawGradientSideways(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
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
        //GlStateManager.disableTextures();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(left, bottom, 0.0);
        worldRenderer.addVertex(right, bottom, 0.0);
        worldRenderer.addVertex(right, top, 0.0);
        worldRenderer.addVertex(left, top, 0.0);
        Tessellator.getInstance().draw();
       // GlStateManager.enableTextures();
        GlStateManager.disableBlend();
    }
    
    public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        rectangle(x, y, x + width, y1, borderColor);
        rectangle(x1 - width, y, x1, y1, borderColor);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
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
        worldRenderer.startDrawing((int)draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing((int)draw);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing((int)draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing((int)draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing((int)draw);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing((int)draw);
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
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB boundingBox) {
        final Tessellator var1 = Tessellator.getInstance();
        final WorldRenderer var2 = var1.getWorldRenderer();
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
    
    public static void drawLines(final AxisAlignedBB boundingBox) {
        GL11.glPushMatrix();
        GL11.glBegin(2);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public static void drawBoundingBox(final AxisAlignedBB axisalignedbb) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrender = Tessellator.getInstance().getWorldRenderer();
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
    
    public static void drawLine3D(final float x, final float y, final float z, final float x1, final float y1, final float z1, final int color) {
        pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(var12, var13, var14, var11);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(3);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glVertex3d((double)x1, (double)y1, (double)z1);
        GL11.glEnd();
        post3D();
    }
    
    public static void draw3DLine(final float x, final float y, final float z, final int color) {
        pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(var12, var13, var14, var11);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(3);
        GL11.glVertex3d(0.0, (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        post3D();
    }
    
    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }
    
    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
        final float red = 0.003921569f * redRGB;
        final float green = 0.003921569f * greenRGB;
        final float blue = 0.003921569f * blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color) {
        enableGL2D();
        glColor(color);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }
    public static void drawInternalRoundedRect3( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
      	 enableGL2D();
      	    x *= 2.0F;
      	    y *= 2.0F;
      	    x1 *= 2.0F;
      	    y1 *= 2.0F;
      	    GL11.glScalef(0.5F, 0.5F, 0.5F);
      	    drawVLine(x, y + 4.0F, y1 - 5.0F, borderC);
      	    drawVLine(x1 - 1.0F, y + 4.0F, y1 - 5.0F, borderC);
      	    drawHLine(x + 5.0F, x1 - 6.0F, y, borderC);
      	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
      	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
      	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
      	    //GlStateManager.enableAlpha();
      	    //TOP RIGHT
      	    drawHLine(x1 - 4, x1 - 4.0F, y, insideC);
      	    drawHLine(x1 - 1, x1 - 1.0F, y + 3, insideC);
      	    
      	    //TOP LEFT
      	    drawHLine(x + 3.0F, x + 3.0F, y, insideC);
      	    drawHLine(x + 0.0F, x + 0.0F, y + 3, insideC);
      	    
      	    //BOTTOM LEFT
      	    drawHLine(x + 0.0F, x + 0.0F, y1 - 4, insideC);
      	    drawHLine(x + 2.0F, x + 2.0F, y1 - 0, insideC);
      	    
      	  //BOTTOM RIGHT
      	   drawHLine(x1 - 1, x1 - 1.0F, y1 - 4, insideC);
      	    drawHLine(x1 - 4, x1 - 4.0F, y1 - 1, insideC);
      	    
      	    drawHLine(x + 5.0F, x1 - 6.0F, y1 - 1.0F, borderC);
      	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
      	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
      	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
      	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
      	    drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
      	    GL11.glScalef(2.0F, 2.0F, 2.0F);
      	    disableGL2D();
      }
    public static void drawInternalRoundedRect2( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
      	 enableGL2D();
      	    x *= 2.0F;
      	    y *= 2.0F;
      	    x1 *= 2.0F;
      	    y1 *= 2.0F;
      	    GL11.glScalef(0.5F, 0.5F, 0.5F);
     	   drawVLine(x, y + 5.5F, y1 - 6.5F, borderC);
      	    drawVLine(x + 2, y + 3F, y1 - 4F, borderC);
      	    
      	  //drawVLine(x1, y + 5.5F, y1 - 6.5F, borderC);
    	    drawVLine(x1 - 3, y + 3F, y1 - 4F, borderC);
      	    
      	    drawVLine(x1 - 1.0F, y + 5.5F, y1 - 6.5F, borderC);
      	    
      	    drawHLine(x + 6.5F, x1 - 7.5F, y, borderC);
      	    drawHLine(x + 4F, x1 - 4.5F, y + 2, borderC);
      	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
      	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
      	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
      	    //GlStateManager.enableAlpha();
      	    //TOP RIGHT
      	    drawHLine(x1 - 5.5f, x1 - 6.5F, y, insideC);
      	    drawHLine(x1 - 1, x1 - 1.0F, y + 5.5f, insideC);
      	    
      	    //TOP LEFT
      	    drawHLine(x + 4.5F, x + 5.5F, y, insideC);
      	    drawVLine(x + 0.0F, y + 3.5f, y + 6.5f, insideC);
      	    
      	    //BOTTOM LEFT
      	//    drawVLine(x + 1.5F, y1 - 4f, y1 - 7.5f, borderC);
      	 //   drawHLine(x + 4.5F, x + 5.5F, y1  - 2.5f, borderC);
      	    
      	  drawVLine(x + 0F, y1 - 7.5f, y1 - 5f, insideC);
   	    drawVLine(x + 5.5F, y1 - 2.5f, y1 - 0.0f, insideC);
      	    
      	  //BOTTOM RIGHT
      	   drawHLine(x1 - 1, x1 - 1.0F, y1 - 6.5f, insideC);
      	    drawHLine(x1 - 5.5f, x1 - 6.5F, y1 - 1, insideC);
      	    
      	  drawHLine(x + 4F, x1 - 4.5F, y1 - 2.5F, borderC);
      	    drawHLine(x + 6.5F, x1 - 8.0F, y1 - 1.0F, borderC);
      	    
      	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
      	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
      	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
      	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
      	    
      	    
      	    drawRect(x + 3F, y + 3F, x1 - 3F, y1 - 3F, borderC);
      	    GL11.glScalef(2.0F, 2.0F, 2.0F);
      	    disableGL2D();
      }
    public static void drawInternalRoundedRect1( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
   	 enableGL2D();
   	    x *= 2.0F;
   	    y *= 2.0F;
   	    x1 *= 2.0F;
   	    y1 *= 2.0F;
   	    GL11.glScalef(0.5F, 0.5F, 0.5F);
   	    drawVLine(x, y + 4.0F, y1 - 5.0F, borderC);
   	    drawVLine(x1 - 1.0F, y + 4.0F, y1 - 5.0F, borderC);
   	    drawHLine(x + 5.0F, x1 - 6.0F, y, borderC);
   	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
   	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
   	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
   	    //GlStateManager.enableAlpha();
   	    //TOP RIGHT
   	    drawHLine(x1 - 4, x1 - 4.0F, y, insideC);
   	    drawHLine(x1 - 1, x1 - 1.0F, y + 3, insideC);
   	    
   	    //TOP LEFT
   	    drawHLine(x + 3.0F, x + 3.0F, y, insideC);
   	    drawHLine(x + 0.0F, x + 0.0F, y + 3, insideC);
   	    
   	    //BOTTOM LEFT
   	    drawHLine(x + 0.0F, x + 0.0F, y1 - 4, insideC);
   	    drawHLine(x + 2.0F, x + 2.0F, y1 - 0, insideC);
   	    
   	  //BOTTOM RIGHT
   	   drawHLine(x1 - 1, x1 - 1.0F, y1 - 4, insideC);
   	    drawHLine(x1 - 4, x1 - 4.0F, y1 - 1, insideC);
   	    
   	    drawHLine(x + 5.0F, x1 - 6.0F, y1 - 1.0F, borderC);
   	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
   	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
   	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
   	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
   	    drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
   	    GL11.glScalef(2.0F, 2.0F, 2.0F);
   	    disableGL2D();
   }
    public static void drawRoundedShadow( float x,  float y, float x1,  float y1) {
      	 enableGL2D();
      	    x *= 2.0F;
      	    y *= 2.0F;
      	    x1 *= 2.0F;
      	    y1 *= 2.0F;
      	    GL11.glScalef(0.5F, 0.5F, 0.5F);
      	    //drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0xff606060, 0x90606060);
      	    drawInternalRoundedRect2(x - 02.5F, y-01.6F, x1+2.6F, y1+2.5F, 0x30505050, 0x10505050);
      	    drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0x50505050, 0x30606060);
      	    drawInternalRoundedRect2(x - 0.5F, y-0.6F, x1+0.6F, y1+0.5F, 0x60505050, 0x50505050);
      	   
      	    GL11.glScalef(2.0F, 2.0F, 2.0F);
      	    disableGL2D();
      }
    public static void drawRoundedRectWithShadow( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
   	 enableGL2D();
   	    x *= 2.0F;
   	    y *= 2.0F;
   	    x1 *= 2.0F;
   	    y1 *= 2.0F;
   	    GL11.glScalef(0.5F, 0.5F, 0.5F);
   	    //drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0xff606060, 0x90606060);
   	    drawInternalRoundedRect2(x - 02.5F, y-01.6F, x1+2.6F, y1+2.5F, 0x30505050, 0x10505050);
   	    drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0x50505050, 0x30606060);
   	    drawInternalRoundedRect2(x - 0.5F, y-0.6F, x1+0.6F, y1+0.5F, 0x60505050, 0x50505050);
   	    drawVLine(x, y + 2.0F, y1 - 3.0F, borderC);
   	    drawVLine(x1 - 1.0F, y + 2.0F, y1 - 3.0F, borderC);
   	    drawHLine(x + 3.0F, x1 - 4.0F, y, borderC);
   	   // GlStateManager.enableBlend();
   		//  GlStateManager.disableAlpha();
   	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
   	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
   	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
   		 
   	    //GlStateManager.enableAlpha();
   	    //TOP RIGHT
   	    drawHLine(x1 - 3, x1 - 3.0F, y, insideC);
   	    drawHLine(x1 - 1, x1 - 1.0F, y + 2, insideC);
   	    
   	    //TOP LEFT
   	    drawHLine(x + 2.0F, x + 2.0F, y, insideC);
   	    drawHLine(x + 0.0F, x + 0.0F, y + 2, insideC);
   	    
   	    //BOTTOM LEFT
   	    drawHLine(x + 0.0F, x + 0.0F, y1 - 3, insideC);
   	    drawHLine(x + 2.0F, x + 2.0F, y1 - 1, insideC);
   	    
   	  //BOTTOM RIGHT
   	    drawHLine(x1 - 1, x1 - 1.0F, y1 - 3, insideC);
   	    drawHLine(x1 - 3, x1 - 3.0F, y1 - 1, insideC);
   	    
   	    drawHLine(x + 3.0F, x1 - 4.0F, y1 - 1.0F, borderC);
   	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
   	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
   	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
   	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
   	    drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
   	    GL11.glScalef(2.0F, 2.0F, 2.0F);
   	    disableGL2D();
   }
    public static void drawRoundedRect( double d,  float y, double e,  float y1,  int borderC,  int insideC) {
    	 enableGL2D();
    	    d *= 2.0F;
    	    y *= 2.0F;
    	    e *= 2.0F;
    	    y1 *= 2.0F;
    	    GL11.glScalef(0.5F, 0.5F, 0.5F);
    	    //drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0xff606060, 0x90606060);
    	   drawVLine(d, y + 2.0F, y1 - 3.0F, borderC);
    	    drawVLine(e - 1.0F, y + 2.0F, y1 - 3.0F, borderC);
    	    drawHLine(d + 3.0F, e - 4.0F, y, borderC);
    	   // GlStateManager.enableBlend();
    		//  GlStateManager.disableAlpha();
    	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
    	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
    	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
    		 
    	    //GlStateManager.enableAlpha();
    	    //TOP RIGHT
    	    drawHLine(e - 3, e - 3.0F, y, insideC);
    	    drawHLine(e - 1, e - 1.0F, y + 2, insideC);
    	    
    	    //TOP LEFT
    	    drawHLine(d + 2.0F, d + 2.0F, y, insideC);
    	    drawHLine(d + 0.0F, d + 0.0F, y + 2, insideC);
    	    
    	    //BOTTOM LEFT
    	    drawHLine(d + 0.0F, d + 0.0F, y1 - 3, insideC);
    	    drawHLine(d + 2.0F, d + 2.0F, y1 - 1, insideC);
    	    
    	  //BOTTOM RIGHT
    	    drawHLine(e - 1, e - 1.0F, y1 - 3, insideC);
    	    drawHLine(e - 3, e - 3.0F, y1 - 1, insideC);
    	    
    	    drawHLine(d + 3.0F, e - 4.0F, y1 - 1.0F, borderC);
    	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
    	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
    	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
    	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
    	    Gui.drawFloatRect(d + 1.0F, y + 1.0F, e - 1.0F, y1 - 1.0F, borderC);
    	    GL11.glScalef(2.0F, 2.0F, 2.0F);
    	    disableGL2D();
    }
    public static void drawVLine(double d, float y, float x1, int y1)
    {
      if (x1 < y)
      {
        float var5 = y;
        y = x1;
        x1 = var5;
      }
      Gui.drawFloatRect(d, y + 1.0F, d + 1.0F, x1, y1);
    }
    public static void drawHLine(double d, double e, final float x1, final int y1) {
        if (e < d) {
            final float var5 = (float) d;
            d = e;
            e = var5;
        }
        Gui.drawFloatRect(d, x1, e + 1.0f, x1 + 1.0f, y1);
    }
    
    public static void drawVLine(final float x, final float y, final float x1, final float y1, final float width, final int color) {
        if (width <= 0.0f) {
            return;
        }
        GL11.glPushMatrix();
        pre3D();
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        final int shade = GL11.glGetInteger(2900);
        GlStateManager.shadeModel(7425);
        GL11.glColor4f(var12, var13, var14, var11);
        final float line = GL11.glGetFloat(2849);
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        GL11.glVertex3d((double)x, (double)y, 0.0);
        GL11.glVertex3d((double)x1, (double)y1, 0.0);
        GL11.glEnd();
        GlStateManager.shadeModel(shade);
        GL11.glLineWidth(line);
        post3D();
        GL11.glPopMatrix();
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        //GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }


    public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        final float theta = (float)(6.2831852 / num_segments);
        final float p = (float)Math.cos(theta);
        final float s = (float)Math.sin(theta);
        float x;
        r = (x = r * 2.0f);
        float y = 0.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
        //  GL11.glEnable((int)3042);
          GL11.glDisable((int)3553);
          GL11.glBlendFunc((int)770, (int)771);
          GL11.glEnable((int)2848);
          GL11.glPushMatrix();
          float scale = 0.1f;
          GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
          drawCircle(x *= 10, y *= 10, radius *= 10.0f, insideC);
         // drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
          GL11.glScalef((float)10.0f, (float)10.0f, (float)10.0f);
          GL11.glPopMatrix();
          GL11.glEnable((int)3553);
        //  GL11.glDisable((int)3042);
          GL11.glDisable((int)2848);
      }
    public static void drawUnfilledCircle(double x, double y, float radius, float lineWidth, int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        float red = (float)(color >> 16 & 255) / 255.0f;
        float green = (float)(color >> 8 & 255) / 255.0f;
        float blue = (float)(color & 255) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glLineWidth((float)lineWidth);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }
    public static void drawCircle(double x, double y, float radius, int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        float red = (float)(color >> 16 & 255) / 255.0f;
        float green = (float)(color >> 8 & 255) / 255.0f;
        float blue = (float)(color & 255) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
            ++i;
        }
        GL11.glEnd();
    }
    public static void drawBorderedCircle(final float circleX, final float circleY, final double radius, final double width, final int borderColor, final int innerColor) {
        enableGL2D();
        GlStateManager.enableBlend();
        GL11.glEnable(2881);
        drawCircle(circleX, circleY, (float)(radius - 0.5 + width), 72, borderColor);
        drawFullCircle(circleX, circleY, (float)radius, innerColor);
        GlStateManager.disableBlend();
        GL11.glDisable(2881);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        disableGL2D();
    }
    
    public static void drawCircleNew(final float x, final float y, final float radius, final int numberOfSides) {
        final float z = 0.0f;
        final int numberOfVertices = numberOfSides + 2;
        final float doublePi = 6.2831855f;
    }
    
    public static void drawFullCircle(float cx, float cy, float r, final int c) {
        r *= 2.0f;
        cx *= 2.0f;
        cy *= 2.0f;
        final float theta = 0.19634953f;
        final float p = (float)Math.cos(theta);
        final float s = (float)Math.sin(theta);
        float x = r;
        float y = 0.0f;
        enableGL2D();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(3024);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        glColor(c);
        GL11.glBegin(9);
        for (int ii = 0; ii < 32; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        disableGL2D();
    }

    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue,
			float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
