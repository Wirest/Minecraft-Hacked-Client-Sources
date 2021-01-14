package cedo.util.render;

import cedo.ui.elements.Draw;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

    private final static Frustum frustrum = new Frustum();
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void enable(int glTarget) {
        glEnable(glTarget);
    }

    public static void disable(int glTarget) {
        glDisable(glTarget);
    }

    public static void vertex(double x, double y) {
        glVertex2d(x, y);
    }

    public static void start() {
        enable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        disable(GL_TEXTURE_2D);
        disable(GL_CULL_FACE);
        GlStateManager.disableAlpha();
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        enable(GL_CULL_FACE);
        enable(GL_TEXTURE_2D);
        disable(GL_BLEND);
        color(Color.white);
    }

    public static void color(Color color) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public static void color(double red, double green, double blue, double alpha) {
        glColor4d(red, green, blue, alpha);
    }

    private static void gradientSideways(double x, double y, double width, double height, boolean filled, Color color1, Color color2) {
        start();
        glShadeModel(GL_SMOOTH);
        GlStateManager.disableAlpha();
        if (color1 != null)
            color(color1);
        glBegin(filled ? GL_TRIANGLE_FAN : GL_LINES);
        {
            vertex(x, y);
            vertex(x, y + height);
            if (color2 != null)
                color(color2);
            vertex(x + width, y + height);
            vertex(x + width, y);
        }
        glEnd();
        GlStateManager.enableAlpha();
        glShadeModel(GL_FLAT);
        stop();
    }

    public static void gradientSideways(double x, double y, double width, double height, Color color1, Color color2) {
        gradientSideways(x, y, width, height, true, color1, color2);
    }

    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        glColor4f(red, green, blue, alpha);
    }

    public static void drawFullCircle(float cx, float cy, float r, final int c) {
        r *= 2.0f;
        cx *= 2.0f;
        cy *= 2.0f;
        final float theta = 0.19634953f;
        final float p = (float) Math.cos(theta);
        final float s = (float) Math.sin(theta);
        float x = r;
        float y = 0.0f;
        enableGL2D();
        glEnable(2848);
        glHint(3154, 4354);
        glEnable(3024);
        glScalef(0.5f, 0.5f, 0.5f);
        glColor(c);
        glBegin(9);
        for (int ii = 0; ii < 32; ++ii) {
            glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        glEnd();
        glScalef(2.0f, 2.0f, 2.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        disableGL2D();
    }

    static void enableGL2D() {
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glDepthMask(true);
        glEnable(2848);
        glHint(3154, 4354);
        glHint(3155, 4354);
    }

    public static void disableGL2D() {
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glHint(3154, 4352);
        glHint(3155, 4352);
    }


    /*public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        glPushMatrix();
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
        glScalef(0.5f, 0.5f, 0.5f);
        glColor4f(f2, f3, f4, f);
        glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        glEnd();
        glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        glPopMatrix();
    }

    public static void drawBorderedCircle(final float circleX, final float circleY, final double radius, final double width, final int borderColor, final int innerColor) {
        enableGL2D();
        GlStateManager.enableBlend();
        glEnable(2881);
        drawCircle(circleX, circleY, (float)(radius - 0.5 + width), 72, borderColor);
        drawFullCircle(circleX, circleY, (float)radius, innerColor);
        GlStateManager.disableBlend();
        glDisable(2881);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        disableGL2D();
    }*/
    public static void drawCircle(double x, double y, float radius, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0f;
        float red = (float) (color >> 16 & 255) / 255.0f;
        float green = (float) (color >> 8 & 255) / 255.0f;
        float blue = (float) (color & 255) / 255.0f;
        glColor4f(red, green, blue, alpha);
        glBegin(9);
        int i = 0;
        while (i <= 360) {
            glVertex2d(x + Math.sin((double) i * 3.141526 / 180.0) * (double) radius, y + Math.cos((double) i * 3.141526 / 180.0) * (double) radius);
            ++i;
        }
        glEnd();
    }

    public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
        //  glEnable((int)3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();
        float scale = 0.1f;
        glScalef(0.1f, 0.1f, 0.1f);
        drawCircle(x * 10, y * 10, radius * 10.0f, insideC);
        // drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
        glScalef(10.0f, 10.0f, 10.0f);
        glPopMatrix();
        glEnable(3553);
        //  glDisable((int)3042);
        glDisable(2848);
    }

    public static void drawAxisAlignedBBFilled(AxisAlignedBB axisAlignedBB, int color, boolean depth) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        if (depth) glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        Draw.color(color);
        drawBoxFilled(axisAlignedBB);
        GlStateManager.resetColor();
        glEnable(GL_TEXTURE_2D);
        if (depth) glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void drawBox(BlockPos pos, int color, boolean depth) {
        final RenderManager renderManager = mc.getRenderManager();
        final Timer timer = mc.timer;

        final double x = pos.getX() - renderManager.renderPosX;
        final double y = pos.getY() - renderManager.renderPosY;
        final double z = pos.getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = mc.theWorld.getBlockState(pos).getBlock();

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, pos).expand(.002, .002, .002).offset(-posX, -posY, -posZ);

            drawAxisAlignedBBFilled(axisAlignedBB, color, depth);
        }
    }

    public static void drawBoxFilled(AxisAlignedBB axisAlignedBB) {
        glBegin(GL_QUADS);
        {
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        }
        glEnd();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
        vertexbuffer.begin(3, DefaultVertexFormats.field_181705_e);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.field_181705_e);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.field_181705_e);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static double[] interpolate(Entity entity) {
        double partialTicks = mc.timer.renderPartialTicks;
        return new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks};
    }

    public static void blockEsp(BlockPos blockPos, double red, double green, double blue, float thickness) {
        double x = (double) blockPos.getX() - mc.getRenderManager().renderPosX;
        double y = (double) blockPos.getY() - mc.getRenderManager().renderPosY;
        double z = (double) blockPos.getZ() - mc.getRenderManager().renderPosZ;
        glBlendFunc(770, 771);
        glEnable(3042);
        glLineWidth(thickness);
        glEnable(GL_LINE_SMOOTH);
        glDisable(3553);
        glDisable(2929);
        glDepthMask(false);
        glColor4d(red, green, blue, 13);
        glColor4d(red, green, blue, 1);
        drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void drawImg(ResourceLocation loc, double posX, double posY, double width, double height) {
        mc.getTextureManager().bindTexture(loc);
        float f = 1.0F / (float) width;
        float f1 = 1.0F / (float) height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.pos(posX, (posY + height), 0.0D).tex(0 * f, ((0 + (float) height) * f1)).endVertex();
        worldrenderer.pos((posX + width), (posY + height), 0.0D).tex((0 + (float) width) * f, (0 + (float) height) * f1).endVertex();
        worldrenderer.pos((posX + width), posY, 0.0D).tex((0 + (float) width) * f, 0 * f1).endVertex();
        worldrenderer.pos(posX, posY, 0.0D).tex(0 * f, 0 * f1).endVertex();
        tessellator.draw();
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181704_d);
        worldrenderer.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawHsvScale(double left, double top, double right, double bottom) {
        float width = (float) (right - left);
        for (float i = 0; i < width; i++) {
            double posX = left + i;
            int color = Color.getHSBColor(i / width, 1, 1).getRGB();
            Gui.drawRect(posX, top, posX + 1, bottom, color);
        }
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        glScissor((int) (x * (float) factor), (int) (((float) scale.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor) {
        Gui.drawRect(left - borderWidth, top - borderWidth, right + borderWidth, bottom + borderWidth, borderColor);
        Gui.drawRect(left, top, right, bottom, insideColor);
    }

    public static void drawBorder(double left, double top, double width, double height, double lineWidth, int color) {
        Gui.drawRect(left, top, left + width, top + lineWidth, color);
        Gui.drawRect(left, top, left + lineWidth, top + height, color);
        Gui.drawRect(left, top + height - lineWidth, left + width, top + height, color);
        Gui.drawRect(left + width - lineWidth, top, left + width, top + height, color);
    }

    public static void startSmooth() {
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
    }

    public static void endSmooth() {
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
    }

    public static Color fade(final Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        float brightness = Math.abs((((System.currentTimeMillis() % 2000) / 1000F + (index / (float) count) * 2F) % 2F) - 1);
        brightness = 0.5f + (0.5f * brightness);

        hsb[2] = brightness % 2F;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    public static int getOppositeColor(int color) {
        int R = color & 255;
        int G = (color >> 8) & 255;
        int B = (color >> 16) & 255;
        int A = (color >> 24) & 255;
        R = 255 - R;
        G = 255 - G;
        B = 255 - B;
        return R + (G << 8) + (B << 16) + (A << 24);
    }

}