package com.mentalfrostbyte.jello.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.mentalfrostbyte.jello.main.Jello;

public class GuiUtils
extends Gui {
	public void drawOutlineRect(float drawX, float drawY, float drawWidth, float drawHeight, int color) {
        GuiUtils.drawRect(drawX-0.4, drawY - 0.4f, drawWidth -7.8, drawY + 0.5f, color);
        GuiUtils.drawRect(drawX-0.4, drawY + 0.5f, drawX + 0.5f, drawHeight + drawY/2 - 7.4, color);
        GuiUtils.drawRect(drawWidth - 0.5f, drawY-drawY/1.5 + 0.5f + 3.43, drawWidth + 0.4f, drawHeight - 0.5f, color);
        GuiUtils.drawRect(drawX + 0.3f + 7.5, drawHeight - 0.5f, drawWidth + 0.4f, drawHeight+0.4f, color);
    }

    public void drawOutlinedRect(float drawX, float drawY, float drawWidth, float drawHeight, float alpha, float red, float green, float blue) {
        GuiUtils.drawRectColor(drawX, drawY, drawWidth -8, drawY + 0.5f, alpha, red, green, blue);
        GuiUtils.drawRectColor(drawX, drawY + 0.5f, drawX + 0.5f, drawHeight + drawY/2 - 7.5, alpha, red, green, blue);
        GuiUtils.drawRectColor(drawWidth - 0.5f, drawY-drawY/1.5 + 0.5f + 3.5, drawWidth, drawHeight - 0.5f, alpha, red, green, blue);
        GuiUtils.drawRectColor(drawX + 0.5f + 7.5, drawHeight - 0.5f, drawWidth, drawHeight, alpha, red, green, blue);
    }

    public static void drawCheck(int x2, int y2, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawString("\u2713", x2, y2, color);
        Minecraft.getMinecraft().fontRendererObj.drawString("\u2713", x2, (int)((double)y2 + 0.5), color);
    }

    public static void drawRoundedRect(float x2, float y2, float maxX, float maxY, int color) {
        GuiUtils.drawRect(x2 + 0.5f, y2, maxX - 0.5f, y2 + 0.5f, color);
        GuiUtils.drawRect(x2 + 0.5f, maxY - 0.5f, maxX - 0.5f, maxY, color);
        GuiUtils.drawRect(x2, y2 + 0.5f, maxX, maxY - 0.5f, color);
    }

    public void drawTexturedRectangle(ResourceLocation texture, float x2, float y2, float width, float height, float u2, float v2, float uWidth, float vHeight) {
        GL11.glEnable(3042);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float scale = 0.00390625f;
        Tessellator tessellator = Tessellator.instance;
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV(x2, y2 + height, 0.0, u2 * 0.00390625f, (v2 + vHeight) * 0.00390625f);
        worldRenderer.addVertexWithUV(x2 + width, y2 + height, 0.0, (u2 + uWidth) * 0.00390625f, (v2 + vHeight) * 0.00390625f);
        worldRenderer.addVertexWithUV(x2 + width, y2, 0.0, (u2 + uWidth) * 0.00390625f, v2 * 0.00390625f);
        worldRenderer.addVertexWithUV(x2, y2, 0.0, u2 * 0.00390625f, v2 * 0.00390625f);
        worldRenderer.draw();
        GL11.glDisable(3042);
    }

    public static void scissorBox(float x1, float y1, float x2, float y2) {
        /*int width = xend - x2;
        int height = yend - y2;
        ScaledResolution sr2 = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int factor = sr2.getScaleFactor();
        int bottomY = GuiScreen.height - yend;
        GL11.glScissor(x2 * factor, bottomY * factor, width * factor, height * factor);*/
    	float temp;
		if (y1 > y2) {
			temp = y2;
			y2 = y1;
			y1 = temp;
		}
		GL11.glScissor((int)x1, (int)(Display.getHeight()-y2), (int)(x2-x1), (int)(y2-y1));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public int getFadeHex(int hex1, int hex2, double ratio) {
        int r2 = hex1 >> 16;
        int g2 = hex1 >> 8 & 255;
        int b2 = hex1 & 255;
        return r2 << 16 | g2 << 8 | b2;
    }

    public static void start3DGLConstants() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
    }

    public static void finish3DGLConstants() {
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void drawTriangle(double cx, double cy2, float g2, float theta, float h2, int c2) {
        GL11.glTranslated(cx, cy2, 0.0);
        GL11.glRotatef(180.0f + theta, 0.0f, 0.0f, 1.0f);
        float f2 = (float)(c2 >> 24 & 255) / 255.0f;
        float f22 = (float)(c2 >> 16 & 255) / 255.0f;
        float f3 = (float)(c2 >> 8 & 255) / 255.0f;
        float f4 = (float)(c2 & 255) / 255.0f;
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(h2);
        GL11.glBegin(6);
        GL11.glVertex2d(0.0, 1.0f * g2);
        GL11.glVertex2d(1.0f * g2, - 1.0f * g2);
        GL11.glVertex2d(- 1.0f * g2, - 1.0f * g2);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glRotatef(-180.0f - theta, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(- cx, - cy2, 0.0);
    }

    public static void drawTextureID(float g2, float y2, float width, float height, int id2) {
        GL11.glPushMatrix();
        GL11.glBindTexture(3553, id2);
        GL11.glTranslatef(g2, y2, 0.0f);
        Tessellator tessellator = Tessellator.instance;
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV(0.0, height, 0.0, 0.0, 1.0);
        worldRenderer.addVertexWithUV(width, height, 0.0, 1.0, 1.0);
        worldRenderer.addVertexWithUV(width, 0.0, 0.0, 1.0, 0.0);
        worldRenderer.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        worldRenderer.draw();
        GL11.glPopMatrix();
    }

    public static void drawRect(double d2, double e2, double f2, double f3, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2d(f2, e2);
        GL11.glVertex2d(d2, e2);
        GL11.glVertex2d(d2, f3);
        GL11.glVertex2d(f2, f3);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawRect(double d2, double e2, double f2, double f3, int paramColor) {
        float alpha = (float)(paramColor >> 24 & 255) / 255.0f;
        float red = (float)(paramColor >> 16 & 255) / 255.0f;
        float green = (float)(paramColor >> 8 & 255) / 255.0f;
        float blue = (float)(paramColor & 255) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2d(f2, e2);
        GL11.glVertex2d(d2, e2);
        GL11.glVertex2d(d2, f3);
        GL11.glVertex2d(f2, f3);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawRectColor(double d2, double e2, double f2, double f3, float alpha, float red, float green, float blue) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glColor4f(alpha, red, green, blue);
        GL11.glBegin(7);
        GL11.glVertex2d(f2, e2);
        GL11.glVertex2d(d2, e2);
        GL11.glVertex2d(d2, f3);
        GL11.glVertex2d(f2, f3);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public int fadeColor(int color1, int color2) {
        float alpha = (float)(color1 >> 24 & 255) / 255.0f;
        float red = (float)(color1 >> 16 & 255) / 255.0f;
        float green = (float)(color1 >> 8 & 255) / 255.0f;
        float blue = (float)(color1 & 255) / 255.0f;
        float alpha2 = (float)(color2 >> 24 & 255) / 255.0f;
        float red2 = (float)(color2 >> 16 & 255) / 255.0f;
        float green2 = (float)(color2 >> 8 & 255) / 255.0f;
        float blue2 = (float)(color2 & 255) / 255.0f;
        if ((alpha += 0.001f) >= 1.0f) {
            alpha = 1.0f;
        }
        return 0;
    }

    public static void drawSideGradientRect(float i2, float j2, float k2, float l2, int i1, int j1) {
        float f2 = (float)(i1 >> 24 & 255) / 255.0f;
        float f22 = (float)(i1 >> 16 & 255) / 255.0f;
        float f3 = (float)(i1 >> 8 & 255) / 255.0f;
        float f4 = (float)(i1 & 255) / 255.0f;
        float f5 = (float)(j1 >> 24 & 255) / 255.0f;
        float f6 = (float)(j1 >> 16 & 255) / 255.0f;
        float f7 = (float)(j1 >> 8 & 255) / 255.0f;
        float f8 = (float)(j1 & 255) / 255.0f;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator tessellator = Tessellator.instance;
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawingQuads();
        GL11.glColor4f(f22, f3, f4, f2);
        worldRenderer.addVertex(k2, j2, zLevel);
        GL11.glColor4f(f6, f7, f8, f5);
        worldRenderer.addVertex(i2, j2, zLevel);
        worldRenderer.addVertex(i2, l2, zLevel);
        GL11.glColor4f(f22, f3, f4, f2);
        worldRenderer.addVertex(k2, l2, zLevel);
        worldRenderer.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public static void drawGradientRect(double x2, double y2, double x22, double y22, int col1, int col2) {
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f22 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        float f5 = (float)(col2 >> 24 & 255) / 255.0f;
        float f6 = (float)(col2 >> 16 & 255) / 255.0f;
        float f7 = (float)(col2 >> 8 & 255) / 255.0f;
        float f8 = (float)(col2 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawGradient(double x2, double y2, double x22, double y22, int col1, int col2) {
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f22 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        float f5 = (float)(col2 >> 24 & 255) / 255.0f;
        float f6 = (float)(col2 >> 16 & 255) / 255.0f;
        float f7 = (float)(col2 >> 8 & 255) / 255.0f;
        float f8 = (float)(col2 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public void drawFilledBBESP(AxisAlignedBB axisalignedbb, int color) {
        GL11.glPushMatrix();
        float red = (float)(color >> 24 & 255) / 255.0f;
        float green = (float)(color >> 16 & 255) / 255.0f;
        float blue = (float)(color >> 8 & 255) / 255.0f;
        float alpha = (float)(color & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        GuiUtils.drawFilledBox(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public void drawFilledBoxForEntity(Entity entity, AxisAlignedBB axisalignedbb, int color) {
        GL11.glPushMatrix();
        float red = (float)(color >> 24 & 255) / 255.0f;
        float green = (float)(color >> 16 & 255) / 255.0f;
        float blue = (float)(color >> 8 & 255) / 255.0f;
        float alpha = (float)(color & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        GuiUtils.drawFilledBox(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
    }

    public static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glDisable(2832);
        GL11.glDisable(3042);
    }

    public static void drawSCBox(double x2, double y2, double z2, double x22, double y22, double z22) {
        GL11.glBegin(7);
        GL11.glVertex3d(x2 -= RenderManager.renderPosX, y2 -= RenderManager.renderPosY, z2 -= RenderManager.renderPosZ);
        GL11.glVertex3d(x2, y22 -= RenderManager.renderPosY, z2);
        GL11.glVertex3d(x22 -= RenderManager.renderPosX, y2, z2);
        GL11.glVertex3d(x22, y22, z2);
        GL11.glVertex3d(x22, y2, z22 -= RenderManager.renderPosZ);
        GL11.glVertex3d(x22, y22, z22);
        GL11.glVertex3d(x2, y2, z22);
        GL11.glVertex3d(x2, y22, z22);
        GL11.glVertex3d(x22, y22, z2);
        GL11.glVertex3d(x22, y2, z2);
        GL11.glVertex3d(x2, y22, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x2, y22, z22);
        GL11.glVertex3d(x2, y2, z22);
        GL11.glVertex3d(x22, y22, z22);
        GL11.glVertex3d(x22, y2, z22);
        GL11.glVertex3d(x2, y22, z2);
        GL11.glVertex3d(x22, y22, z2);
        GL11.glVertex3d(x22, y22, z22);
        GL11.glVertex3d(x2, y22, z22);
        GL11.glVertex3d(x2, y22, z2);
        GL11.glVertex3d(x2, y22, z22);
        GL11.glVertex3d(x22, y22, z22);
        GL11.glVertex3d(x22, y22, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x22, y2, z2);
        GL11.glVertex3d(x22, y2, z22);
        GL11.glVertex3d(x2, y2, z22);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x2, y2, z22);
        GL11.glVertex3d(x22, y2, z22);
        GL11.glVertex3d(x22, y2, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x2, y22, z2);
        GL11.glVertex3d(x2, y2, z22);
        GL11.glVertex3d(x2, y22, z22);
        GL11.glVertex3d(x22, y2, z22);
        GL11.glVertex3d(x22, y22, z22);
        GL11.glVertex3d(x22, y2, z2);
        GL11.glVertex3d(x22, y22, z2);
        GL11.glVertex3d(x2, y22, z22);
        GL11.glVertex3d(x2, y2, z22);
        GL11.glVertex3d(x2, y22, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x22, y22, z2);
        GL11.glVertex3d(x22, y2, z2);
        GL11.glVertex3d(x22, y22, z22);
        GL11.glVertex3d(x22, y2, z22);
        GL11.glEnd();
    }

    public void drawBoundingBoxESP(AxisAlignedBB axisalignedbb, float width, int color) {
        GL11.glPushMatrix();
        float red = (float)(color >> 24 & 255) / 255.0f;
        float green = (float)(color >> 16 & 255) / 255.0f;
        float blue = (float)(color >> 8 & 255) / 255.0f;
        float alpha = (float)(color & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glColor4f(red, green, blue, alpha);
        GuiUtils.drawOutlinedBox(axisalignedbb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public void draw2DPlayerESP(EntityPlayer ep2, double d2, double d1, double d22) {
        Minecraft.getMinecraft();
        float distance = Jello.core.player().getDistanceToEntity(ep2);
        Minecraft.getMinecraft();
        float scale = (float)(0.09 + Jello.core.player().getDistance(ep2.posX, ep2.posY, ep2.posZ) / 10000.0);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d2, (float)d1, (float)d22);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(- RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glScalef(- scale, - scale, scale);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glScaled(0.5, 0.5, 0.5);
        this.drawOutlineRect(-13.0f, -45.0f, 13.0f, 5.0f, -65536);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public void drawOutlineForEntity(Entity e2, AxisAlignedBB axisalignedbb, float width, float red, float green, float blue, float alpha) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glColor4f(red, green, blue, alpha);
        GuiUtils.drawOutlinedBox(axisalignedbb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void drawLines(AxisAlignedBB bb2, float width, float red, float green, float blue, float alpha) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawing(2);
        worldRenderer.addVertex(bb2.minX, bb2.minY, bb2.minZ);
        worldRenderer.addVertex(bb2.minX, bb2.maxY, bb2.maxZ);
        tessellator.draw();
        worldRenderer.startDrawing(2);
        worldRenderer.addVertex(bb2.maxX, bb2.minY, bb2.minZ);
        worldRenderer.addVertex(bb2.minX, bb2.maxY, bb2.maxZ);
        tessellator.draw();
        worldRenderer.startDrawing(2);
        worldRenderer.addVertex(bb2.maxX, bb2.minY, bb2.maxZ);
        worldRenderer.addVertex(bb2.minX, bb2.maxY, bb2.maxZ);
        tessellator.draw();
        worldRenderer.startDrawing(2);
        worldRenderer.addVertex(bb2.maxX, bb2.minY, bb2.minZ);
        worldRenderer.addVertex(bb2.minX, bb2.maxY, bb2.minZ);
        tessellator.draw();
        worldRenderer.startDrawing(2);
        worldRenderer.addVertex(bb2.maxX, bb2.minY, bb2.minZ);
        worldRenderer.addVertex(bb2.minX, bb2.minY, bb2.maxZ);
        tessellator.draw();
        worldRenderer.startDrawing(2);
        worldRenderer.addVertex(bb2.maxX, bb2.maxY, bb2.minZ);
        worldRenderer.addVertex(bb2.minX, bb2.maxY, bb2.maxZ);
        tessellator.draw();
        GL11.glLineWidth(1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public void drawOutlineBox(AxisAlignedBB axisalignedbb, float width, int color) {
        GL11.glPushMatrix();
        float red = (float)(color >> 24 & 255) / 255.0f;
        float green = (float)(color >> 16 & 255) / 255.0f;
        float blue = (float)(color >> 8 & 255) / 255.0f;
        float alpha = (float)(color & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glColor4f(red, green, blue, alpha);
        GuiUtils.drawOutlinedBox(axisalignedbb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawFilledBox(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
    }

    public static void drawOutlinedBox(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
    }

    public void drawBoundingBox(AxisAlignedBB axisalignedbb) {
        Tessellator tessellator = Tessellator.instance;
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldRenderer.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldRenderer.draw();
    }

    public static void drawRect(int left, int top, int right, int bottom, Color color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
}

