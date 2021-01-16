/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.razerboy420.weepcraft.util.esp;

import java.awt.Color;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ESP {
    public static int player = 0;
    public static int item = 6;

    public static void drawBlockBox(BlockPos blockPos, Color color) {
        Minecraft.getMinecraft().getRenderManager();
        double x = (double)blockPos.getX() - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double y = (double)blockPos.getY() - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double z = (double)blockPos.getZ() - RenderManager.renderPosZ;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.15000000596046448);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)color.getRed(), (double)color.getGreen(), (double)color.getBlue(), (double)0.25);
        ESP.drawFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawEntityBox(Entity entity, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        ESP.glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), color == Color.BLACK ? 60 : 45));
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        ESP.drawFilledBox(new AxisAlignedBB(entity.boundingBox.minX - 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.minY - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.minZ - 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ), entity.boundingBox.maxX + 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.maxY + 0.1 - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.maxZ + 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ)));
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawEntityBox(Entity entity, int mode) {
        Color col = null;
        if (mode == player) {
            col = Color.ORANGE;
        }
        ESP.drawEntityBox(entity, col);
    }

    public static void tracerLine(Entity entity, Color color) {
        Minecraft.getMinecraft().getRenderManager();
        double x = entity.posX - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double y = entity.posY + (double)(entity.height / 2.0f) - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double z = entity.posZ - RenderManager.renderPosZ;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        ESP.glColor(color);
        Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(- (float)Math.toRadians(Minecraft.getMinecraft().player.rotationPitch)).rotateYaw(- (float)Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)eyes.xCoord, (double)((double)Minecraft.getMinecraft().player.getEyeHeight() + eyes.yCoord), (double)eyes.zCoord);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawPlatform(double x, double y, double z, Color color, double size) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        ESP.glColor(color);
        Minecraft.getMinecraft().getRenderManager();
        double renderX = x - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double renderY = y - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double renderZ = z - RenderManager.renderPosZ;
        AxisAlignedBB bb = new AxisAlignedBB(renderX + size, renderY + 0.02, renderZ + size, renderX - size, renderY, renderZ - size);
        ESP.drawFilledBox(bb);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawPlatformOverEntity(Entity entity, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        ESP.glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), color == Color.BLACK ? 60 : 45));
        Minecraft.getMinecraft().getRenderManager();
        double renderY = entity.boundingBox.maxY - RenderManager.renderPosY + 0.2;
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        AxisAlignedBB bb = new AxisAlignedBB(entity.boundingBox.minX - 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), renderY, entity.boundingBox.minZ - 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ), entity.boundingBox.maxX + 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), renderY, entity.boundingBox.maxZ + 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ));
        ESP.drawFilledBox(bb);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawHollowRect(float posX, float posY, float posX2, float posY2, float width, int color, boolean center) {
        float corners = width / 2.0f;
        float side = width / 2.0f;
        if (center) {
            ESP.drawRect(posX - side, posY - corners, posX + side, posY2 + corners, color);
            ESP.drawRect(posX2 - side, posY - corners, posX2 + side, posY2 + corners, color);
            ESP.drawRect(posX - corners, posY - side, posX2 + corners, posY + side, color);
            ESP.drawRect(posX - corners, posY2 - side, posX2 + corners, posY2 + side, color);
        } else {
            ESP.drawRect(posX - width, posY - corners, posX, posY2 + corners, color);
            ESP.drawRect(posX2, posY - corners, posX2 + width, posY2 + corners, color);
            ESP.drawRect(posX - corners, posY - width, posX2 + corners, posY, color);
            ESP.drawRect(posX - corners, posY2, posX2 + corners, posY2 + width, color);
        }
    }

    public static void drawGradientBorderedRect(float posX, float posY, float posX2, float posY2, float width, int color, int startColor, int endColor, boolean center) {
        ESP.drawGradientRect(posX, posY, posX2, posY2, startColor, endColor);
        ESP.drawHollowRect(posX, posY, posX2, posY2, width, color, center);
    }

    public static void drawBorderedCorneredRect(float x, float y, float x2, float y2, float lineWidth, int lineColor, int bgColor) {
        ESP.drawRect(x, y, x2, y2, bgColor);
        float f = (float)(lineColor >> 24 & 255) / 255.0f;
        float f2 = (float)(lineColor >> 16 & 255) / 255.0f;
        float f3 = (float)(lineColor >> 8 & 255) / 255.0f;
        float f4 = (float)(lineColor & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3553);
        ESP.drawRect(x - 1.0f, y, x2 + 1.0f, y - lineWidth, lineColor);
        ESP.drawRect(x, y, x - lineWidth, y2, lineColor);
        ESP.drawRect(x - 1.0f, y2, x2 + 1.0f, y2 + lineWidth, lineColor);
        ESP.drawRect(x2, y, x2 + lineWidth, y2, lineColor);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void drawFilledBox(AxisAlignedBB bb) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder vb = ts.getBuffer();
        vb.begin(7, DefaultVertexFormats.POSITION);
        vb.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION);
        vb.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION);
        vb.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION);
        vb.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION);
        vb.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION);
        vb.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        vb.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        vb.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        vb.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        ts.draw();
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        GL11.glEnable((int)1536);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        ESP.glColor(topColor);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        ESP.glColor(bottomColor);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        GL11.glDisable((int)1536);
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        float f = (float)(col1 >> 24 & 255) / 255.0f;
        float f2 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)i, (double)h);
        GL11.glVertex2d((double)g, (double)h);
        GL11.glVertex2d((double)g, (double)j);
        GL11.glVertex2d((double)i, (double)j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawRect(float g, float h, float i, float j, Color c) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        ESP.glColor(c);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)i, (double)h);
        GL11.glVertex2d((double)g, (double)h);
        GL11.glVertex2d((double)g, (double)j);
        GL11.glVertex2d((double)i, (double)j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        ESP.drawRect(x, y, x2, y2, col2);
        float f = (float)(col1 >> 24 & 255) / 255.0f;
        float f2 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, Color c, Color c2) {
        ESP.drawRect(x, y, x2, y2, c2);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        ESP.glColor(c);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawClickTPESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
        Wrapper.mc().entityRenderer.setupCameraTransform(Wrapper.mc().timer.field_194147_b, 0);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)1.0f);
        float sinus = 1.0f - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 10000) / 10000.0f * 3.1415927f * 4.0f) * 1.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)(1.0f - sinus), (float)sinus, (float)0.0f, (float)0.15f);
        ESP.drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.0f - sinus, sinus, 0.0f, 0.15f);
        GL11.glColor4d((double)0.0, (double)0.0, (double)0.0, (double)0.5);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder vb = ts.getBuffer();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }

    public static void drawFace(String name, int x, int y, int w, int h, boolean selected) {
        try {
            AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            float fw = 192.0f;
            float fh = 192.0f;
            float u = 24.0f;
            float v = 24.0f;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);
            fw = 192.0f;
            fh = 192.0f;
            u = 120.0f;
            v = 24.0f;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);
            GL11.glDisable((int)3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void entityESPBox(Entity entity, int mode) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        if (mode == 0) {
            GL11.glColor4d((double)(1.0f - Minecraft.getMinecraft().player.getDistanceToEntity(entity) / 40.0f), (double)(Minecraft.getMinecraft().player.getDistanceToEntity(entity) / 40.0f), (double)0.0, (double)0.5);
        } else if (mode == 1) {
            GL11.glColor4d((double)5.0, (double)2.0, (double)4.0, (double)2.5);
        } else if (mode == 2) {
            GL11.glColor4d((double)1.0, (double)1.0, (double)0.0, (double)0.5);
        } else if (mode == 3) {
            GL11.glColor4d((double)1.0, (double)0.0, (double)0.0, (double)0.5);
        } else if (mode == 4) {
            GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.5);
        }
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(entity.boundingBox.minX - 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.minY - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.minZ - 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ), entity.boundingBox.maxX + 0.05 - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.maxY + 0.1 - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.maxZ + 0.05 - entity.posZ + (entity.posZ - RenderManager.renderPosZ)), 1.0f, 1.0f, 1.0f, 2.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }
}

