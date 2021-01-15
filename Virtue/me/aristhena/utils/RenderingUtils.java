package me.aristhena.utils;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.aristhena.event.events.Render3DEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public final class RenderingUtils {
    public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
    private static ScaledResolution scaledResolution;

    public static void drawSearchBlock(Block block, BlockPos blockPos, Render3DEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        GlStateManager.pushMatrix();
        GL11.glLineWidth((float)1.0f);
        GlStateManager.disableDepth();
        RenderingUtils.disableLighting();
        double var8 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.getPartialTicks();
        double var10 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.getPartialTicks();
        double var12 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.getPartialTicks();
        RenderGlobal.drawOutlinedBoundingBox((AxisAlignedBB)block.getSelectedBoundingBox((World)Minecraft.getMinecraft().theWorld, blockPos).expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(- var8, - var10, - var12), (int)-1);
        GlStateManager.popMatrix();
    }

    public static void drawEsp(EntityLivingBase ent, float pTicks, int hexColor, int hexColorIn) {
        if (!ent.isEntityAlive()) {
            return;
        }
        double x = RenderingUtils.getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
        double y = RenderingUtils.getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
        double z = RenderingUtils.getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
        RenderingUtils.boundingBox((Entity)ent, x, y, z, hexColor, hexColorIn);
    }

    public static void boundingBox(Entity entity, double x, double y, double z, int color, int colorIn) {
        GlStateManager.pushMatrix();
        GL11.glLineWidth((float)1.0f);
        AxisAlignedBB var11 = entity.getEntityBoundingBox();
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
        if (color != 0) {
            GlStateManager.disableDepth();
            RenderingUtils.filledBox(var12, colorIn, true);
            RenderingUtils.disableLighting();
            RenderGlobal.drawOutlinedBoundingBox((AxisAlignedBB)var12, (int)color);
        }
        GlStateManager.popMatrix();
    }

    public static void enableLighting() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        OpenGlHelper.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GL11.glMatrixMode((int)5890);
        GL11.glLoadIdentity();
        float var3 = 0.0039063f;
        GL11.glScalef((float)var3, (float)var3, (float)var3);
        GL11.glTranslatef((float)8.0f, (float)8.0f, (float)8.0f);
        GL11.glMatrixMode((int)5888);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        OpenGlHelper.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
    }

    public static void disableLighting() {
        OpenGlHelper.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GL11.glDisable((int)3553);
        OpenGlHelper.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glLineWidth((float)lineWidth);
    }

    public static void disableGL3D() {
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawRect(float x, float y, float x1, float y1, int color) {
        RenderingUtils.enableGL2D();
        RenderingUtils.glColor(color);
        RenderingUtils.drawRect(x, y, x1, y1);
        RenderingUtils.disableGL2D();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
        RenderingUtils.enableGL2D();
        RenderingUtils.glColor(internalColor);
        RenderingUtils.drawRect(x + width, y + width, x1 - width, y1 - width);
        RenderingUtils.glColor(borderColor);
        RenderingUtils.drawRect(x + width, y, x1 - width, y + width);
        RenderingUtils.drawRect(x, y, x + width, y1);
        RenderingUtils.drawRect(x1 - width, y, x1, y1);
        RenderingUtils.drawRect(x + width, y1 - width, x1 - width, y1);
        RenderingUtils.disableGL2D();
    }

    public static void drawBorderedRect(int x, int y, int x1, int y1, int insideC, int borderC) {
        RenderingUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderingUtils.drawVLine(x *= 2, y *= 2, (y1 *= 2) - 1, borderC);
        RenderingUtils.drawVLine((x1 *= 2) - 1, y, y1, borderC);
        RenderingUtils.drawHLine(x, x1 - 1, y, borderC);
        RenderingUtils.drawHLine(x, x1 - 2, y1 - 1, borderC);
        RenderingUtils.drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderingUtils.disableGL2D();
    }

    public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
        RenderingUtils.enableGL2D();
        RenderingUtils.drawRect(x, y, x1, y1, inside);
        RenderingUtils.glColor(border);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderingUtils.disableGL2D();
    }

    public static void drawGradientBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        RenderingUtils.enableGL2D();
        RenderingUtils.drawGradientRect(x, y, x1, y1, top, bottom);
        RenderingUtils.glColor(border);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderingUtils.disableGL2D();
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        RenderingUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderingUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        RenderingUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        RenderingUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        RenderingUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        RenderingUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        RenderingUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        RenderingUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        RenderingUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        RenderingUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderingUtils.disableGL2D();
    }

    public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
        float x = rectangle.x;
        float y = rectangle.y;
        float x1 = rectangle.x + rectangle.width;
        float y1 = rectangle.y + rectangle.height;
        RenderingUtils.enableGL2D();
        RenderingUtils.glColor(internalColor);
        RenderingUtils.drawRect(x + width, y + width, x1 - width, y1 - width);
        RenderingUtils.glColor(borderColor);
        RenderingUtils.drawRect(x + 1.0f, y, x1 - 1.0f, y + width);
        RenderingUtils.drawRect(x, y, x + width, y1);
        RenderingUtils.drawRect(x1 - width, y, x1, y1);
        RenderingUtils.drawRect(x + 1.0f, y1 - width, x1 - 1.0f, y1);
        RenderingUtils.disableGL2D();
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        RenderingUtils.enableGL2D();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderingUtils.glColor(topColor);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        RenderingUtils.glColor(bottomColor);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        RenderingUtils.disableGL2D();
    }

    public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        RenderingUtils.enableGL2D();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderingUtils.glColor(topColor);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x, (float)y1);
        RenderingUtils.glColor(bottomColor);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        RenderingUtils.disableGL2D();
    }

    public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        RenderingUtils.glColor(col1);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        RenderingUtils.glColor(col2);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3) {
        RenderingUtils.enableGL2D();
        GL11.glPushMatrix();
        RenderingUtils.glColor(col1);
        GL11.glLineWidth((float)1.0f);
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
        RenderingUtils.drawGradientRect(x, y, x2, y2, col2, col3);
        RenderingUtils.disableGL2D();
    }

    public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
        float yc;
        float xc;
        float a;
        int i;
        float f1 = (float)(color >> 24 & 255) / 255.0f;
        float f2 = (float)(color >> 16 & 255) / 255.0f;
        float f3 = (float)(color >> 8 & 255) / 255.0f;
        float f4 = (float)(color & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glTranslated((double)x, (double)y, (double)0.0);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f1);
        GL11.glLineWidth((float)width);
        if (angle > 0.0) {
            GL11.glBegin((int)3);
            i = 0;
            while ((double)i < angle) {
                a = (float)((double)i * (angle * 3.141592653589793 / (double)points));
                xc = (float)(Math.cos(a) * (double)radius);
                yc = (float)(Math.sin(a) * (double)radius);
                GL11.glVertex2f((float)xc, (float)yc);
                ++i;
            }
            GL11.glEnd();
        }
        if (angle < 0.0) {
            GL11.glBegin((int)3);
            i = 0;
            while ((double)i > angle) {
                a = (float)((double)i * (angle * 3.141592653589793 / (double)points));
                xc = (float)(Math.cos(a) * (double)(- radius));
                yc = (float)(Math.sin(a) * (double)(- radius));
                GL11.glVertex2f((float)xc, (float)yc);
                --i;
            }
            GL11.glEnd();
        }
        RenderingUtils.disableGL2D();
        GL11.glDisable((int)3479);
        GL11.glPopMatrix();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderingUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        RenderingUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void drawHLine(float x, float y, float x1, int y1, int y2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderingUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
    }

    public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
        RenderingUtils.enableGL2D();
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
        RenderingUtils.drawRect(x, y, x1, y1);
        RenderingUtils.disableGL2D();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float f = (float)(c >> 24 & 255) / 255.0f;
        float f1 = (float)(c >> 16 & 255) / 255.0f;
        float f2 = (float)(c >> 8 & 255) / 255.0f;
        float f3 = (float)(c & 255) / 255.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x = r *= 2.0f;
        float y = 0.0f;
        RenderingUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)2);
        int ii = 0;
        while (ii < num_segments) {
            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ++ii;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderingUtils.disableGL2D();
        GL11.glPopMatrix();
    }

    public static void drawFullCircle(int cx, int cy, double r, int c) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        float f = (float)(c >> 24 & 255) / 255.0f;
        float f1 = (float)(c >> 16 & 255) / 255.0f;
        float f2 = (float)(c >> 8 & 255) / 255.0f;
        float f3 = (float)(c & 255) / 255.0f;
        RenderingUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)6);
        int i = 0;
        while (i <= 360) {
            double x = Math.sin((double)i * 3.141592653589793 / 180.0) * r;
            double y = Math.cos((double)i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderingUtils.disableGL2D();
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

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float)redRGB;
        float green = 0.003921569f * (float)greenRGB;
        float blue = 0.003921569f * (float)blueRGB;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void updateScaledResolution() {
        scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }

    public static ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        RenderingUtils.updateScaledResolution();
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scaledResolution.getScaledHeight() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void drawOutlinedBox(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.maxZ, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glVertex3d((double)boundingBox.minX, (double)boundingBox.maxZ, (double)boundingBox.maxZ);
        GL11.glEnd();
    }

    public static void drawBox(RenderBox box) {
        GL11.glEnable((int)1537);
        if (box == null) {
            return;
        }
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxZ, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxZ, (double)box.maxZ, (double)box.maxZ);
        GL11.glEnd();
    }

    public static void filledBox(AxisAlignedBB aa, int color, boolean shouldColor) {
        GlStateManager.pushMatrix();
        float var11 = (float)(color >> 24 & 255) / 255.0f;
        float var6 = (float)(color >> 16 & 255) / 255.0f;
        float var7 = (float)(color >> 8 & 255) / 255.0f;
        float var8 = (float)(color & 255) / 255.0f;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer t = var9.getWorldRenderer();
        if (shouldColor) {
            GlStateManager.color((float)var6, (float)var7, (float)var8, (float)var11);
        }
        int draw = 7;
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        var9.draw();
        t.startDrawing(draw);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        var9.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        var9.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        var9.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        var9.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        var9.draw();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.popMatrix();
    }

    private static double getDiff(double lastI, double i, float ticks, double ownI) {
        return lastI + (i - lastI) * (double)ticks - ownI;
    }

    public static void drawBeacon(BlockPos pos, int color, int colorIn, float partialTicks) {
        GlStateManager.pushMatrix();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
        GL11.glLineWidth((float)1.0f);
        AxisAlignedBB var11 = new AxisAlignedBB((double)(pos.getX() + 1), (double)pos.getY(), (double)(pos.getZ() + 1), (double)pos.getX(), (double)(pos.getY() + 200), (double)pos.getZ());
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - x, var11.minY - y, var11.minZ - z, var11.maxX - x, var11.maxY - y, var11.maxZ - z);
        if (color != 0) {
            GlStateManager.disableDepth();
            RenderingUtils.filledBox(var12, colorIn, true);
            RenderingUtils.disableLighting();
            RenderGlobal.drawOutlinedBoundingBox((AxisAlignedBB)var12, (int)color);
        }
        GlStateManager.popMatrix();
    }
    
    public static Color getRainbow(final long offset, final float fade) {
        final float hue = (System.nanoTime() + offset) / 5.0E9f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
	private static int Y = 22;

	private static int pY = 0;
	private static float pY2 = 0;

	private static int rectY = 32;
    
	public static void drawPotionStatus(ScaledResolution sr) {
		pY = 0;
		for (final PotionEffect effect : (Collection<PotionEffect>) ClientUtils.mc().thePlayer
				.getActivePotionEffects()) {
			final Potion potion = Potion.potionTypes[effect.getPotionID()];
			String PType = I18n.format(potion.getName());

			if (effect.getAmplifier() == 1) {
				PType = PType + " II";
			} else if (effect.getAmplifier() == 2) {
				PType = PType + " III";
			} else if (effect.getAmplifier() == 3) {
				PType = PType + " IV";
			}
			if (effect.getDuration() < 600 && effect.getDuration() > 300) {
				PType = PType + "\2477:\2476 " + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				PType = PType + "\2477:\247c " + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				PType = PType + "\2477:\2477 " + Potion.getDurationString(effect);
			}
			ClientUtils.mc().fontRendererObj.drawString(PType,
					sr.getScaledWidth() - (ClientUtils.mc().fontRendererObj.getStringWidth(PType)),
					sr.getScaledHeight() - ClientUtils.mc().fontRendererObj.FONT_HEIGHT + pY,
					potion.getLiquidColor());
			pY -= 9;
		}
	}
	
	public static void drawStuffStatus(ScaledResolution scaledRes) {
		boolean currentItem = true;

		GL11.glPushMatrix();
		RenderItem ir = new RenderItem(ClientUtils.mc().getTextureManager(), ClientUtils.mc().modelManager);
		List<ItemStack> stuff = new ArrayList();
        boolean onwater =  ClientUtils.mc().thePlayer.isEntityAlive() && ClientUtils.mc().thePlayer.isInsideOfMaterial(Material.water);
		int split = 15;

		for (int index = 3; index >= 0; index--) {
			final ItemStack armer = ClientUtils.mc().thePlayer.inventory.armorInventory[index];
			if (armer != null) {
				stuff.add(armer);
			}
		}
		if (ClientUtils.mc().thePlayer.getCurrentEquippedItem() != null && currentItem) {
			stuff.add(ClientUtils.mc().thePlayer.getCurrentEquippedItem());
		}
		for (ItemStack errything : stuff) {
			if (ClientUtils.mc().theWorld != null) {
				RenderHelper.enableGUIStandardItemLighting();
				ir.renderItemIntoGUI(errything, split + scaledRes.getScaledWidth() / 2 - 4, scaledRes.getScaledHeight() - 
						(onwater ? 66 : 56));
				ir.renderItemIntoGUI
						(errything,
								split + scaledRes.getScaledWidth() / 2 - 4,
								scaledRes.getScaledHeight() - (onwater ? 65 : 55));
				RenderHelper.enableGUIStandardItemLighting();
				split += 16;
			}
			GlStateManager.enableAlpha();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.disableLighting();
			GlStateManager.clear(256);
			
			NBTTagList enchants = errything.getEnchantmentTagList();

			if (enchants != null) {
				int ency = 0;
				for(int index = 0; index < enchants.tagCount(); ++index) {
					short id = enchants.getCompoundTagAt(index).getShort("id");
					short level = enchants.getCompoundTagAt(index).getShort("lvl");
					if(Enchantment.enchantmentsList[id] != null) {
						Enchantment enc = Enchantment.enchantmentsList[id];
						String encName = enc.getTranslatedName(level).substring(0, 2).toLowerCase();
					

						GuiUtils.drawSmallString(
								encName
										+ "\247f"
										+ level,
								split - 30 + scaledRes.getScaledWidth() / 2 + 12,
								((scaledRes.getScaledHeight() - (onwater ? 65 : 55) + ency) ),
								0xFFFFFFFF);
						ency += ClientUtils.mc().fontRendererObj.FONT_HEIGHT / 2 ;

					}

				}
			}
		}


		GL11.glPopMatrix();


	}
}

