/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.memewaredevs.client.util.render;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtils {

    public static void box(double x, double y2, double z, double x2, double y22, double z2, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        final double n = x;

        final double x3 = x = n - RenderManager.renderPosX;
        final double n2 = y2;

        final double y3 = y2 = n2 - RenderManager.renderPosY;
        final double n3 = z;

        final double z3 = z = n3 - RenderManager.renderPosZ;
        final double n4 = x2;

        final double x4 = x2 = n4 - RenderManager.renderPosX;
        final double n5 = y22;

        final double y4 = y22 = n5 - RenderManager.renderPosY;
        final double n6 = z2;

        GL11.glColor4d(0.0, 0.0, 0.0, 0.2);
        z2 = n6 - RenderManager.renderPosZ;
        RenderUtils.drawColorBox(new AxisAlignedBB(x3, y3, z3, x4, y4, z2));
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y2, z, x2, y22, z2), color.getRGB());
        GL11.glColor4d(0.0, 0.0, 0.0, 0.2);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void drawColorBox(final AxisAlignedBB axisalignedbb) {
        final Tessellator ts = Tessellator.getInstance();
        final WorldRenderer wr = ts.getWorldRenderer();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        ts.draw();
        wr.startDrawingQuads();
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        ts.draw();
    }

    public static void drawGradientRect(final float x, final float y2, final float x1, final float y1,
                                        final int topColor, final int bottomColor) {
        RenderUtils.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        ColorUtils.glColor(topColor);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        ColorUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x, y2);
        GL11.glEnd();
        GL11.glShadeModel(7425);
        RenderUtils.disableGL2D();
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
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawEsp(final Entity o, final int c2) {
        RenderUtils.box(o.posX - 0.5, o.posY, o.posZ - 0.5, o.posX + 0.5, o.posY + 2.0, o.posZ + 0.5, new Color(c2));
    }
    public static void drawEsp(final TileEntity o, final int c2) {
        RenderUtils.box(o.getPos().getX() + 1, o.getPos().getY(),
                o.getPos().getZ() + 1, o.getPos().getX(),
                o.getPos().getY() + 1.0, o.getPos().getZ(), new Color(c2));
    }
}
