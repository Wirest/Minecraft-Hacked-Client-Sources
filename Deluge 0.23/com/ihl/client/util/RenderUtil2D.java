package com.ihl.client.util;

import com.ihl.client.font.MinecraftFontRenderer;
import com.ihl.client.util.part.ChatColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

import javax.vecmath.Point2d;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil2D extends RenderUtil {

    public static void rectBordered(double x1, double y1, double x2, double y2, int fill, int outline, float width) {
        rect(x1, y1, x2, y2, fill, 1, GL_QUADS);
        rect(x1, y1, x2, y2, outline, width, GL_LINE_LOOP);
    }

    public static void rect(double x1, double y1, double x2, double y2, int color, float width, int renderCode) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        glLineWidth((float) Math.max(0.1, width));

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        render.startDrawing(renderCode);
        render.addVertex(x1, y2, 0);
        render.addVertex(x2, y2, 0);
        render.addVertex(x2, y1, 0);
        render.addVertex(x1, y1, 0);
        tess.draw();

        postRender();
    }

    public static void texturedRect(double x1, double y1, double x2, double y2, int color) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        GlStateManager.enableTexture2D();

        render.startDrawingQuads();
        render.addVertexWithUV((int) x1, (int) y2, 0, 0, 1);
        render.addVertexWithUV((int) x2, (int) y2, 0, 1, 1);
        render.addVertexWithUV((int) x2, (int) y1, 0, 1, 0);
        render.addVertexWithUV((int) x1, (int) y1, 0, 0, 0);
        tess.draw();

        postRender();
    }

    public static void polygonBordered(List<Point2d> points, int fill, int outline, float width, boolean connected) {
        polygon(points, fill, 1, GL_TRIANGLE_FAN);
        polygon(points, outline, width, connected ? GL_LINE_LOOP : GL_LINE_STRIP);
    }

    public static void polygon(List<Point2d> points, int color, float width, int renderCode) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        glLineWidth((float)Math.max(0.1, width));

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        render.startDrawing(renderCode);
        for (int i = points.size() - 1; i >= 0; i--) {
            Point2d point = points.get(i);
            render.addVertex(point.x, point.y, 0.0D);
        }
        tess.draw();

        postRender();
    }

    public static void line(double x1, double y1, double x2, double y2, int color, float width) {
        RenderUtil3D.line(x1, y1, 0, x2, y2, 0, color, width);
    }

    public static void circle(double x, double y, double radius, int color) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        for(double i = 0; i < 360; i++) {
            double cs = i*Math.PI/180;
            double ps = (i-1)*Math.PI/180;
            double[] outer = new double[] {
                    Math.cos(cs) * radius, -Math.sin(cs) * radius,
                    Math.cos(ps) * radius, -Math.sin(ps) * radius
            };

            render.startDrawing(6);
            render.addVertex(x + outer[2], y + outer[3], 0);
            render.addVertex(x + outer[0], y + outer[1], 0);
            render.addVertex(x, y, 0);
            tess.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public static void circleOutline(double x, double y, double radius, int color, float width) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        glLineWidth((float) Math.max(0.1, width));

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        render.startDrawing(2);
        for(double i = 0; i < 360; i++) {
            double cs = i*Math.PI/180;
            double[] outer = new double[] {
                    Math.cos(cs) * radius, -Math.sin(cs) * radius
            };

            render.addVertex(x + outer[0], y + outer[1], 0);
        }
        tess.draw();

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public static void donut(double x, double y, double radius, double hole, int color) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        for(double i = 0; i < 360; i++) {
            double cs = i*Math.PI/180D;
            double ps = (i-1)*Math.PI/180D;
            double[] outer = new double[] { Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius, -Math.sin(ps) * radius };
            double[] inner = new double[] { Math.cos(cs) * hole, -Math.sin(cs) * hole, Math.cos(ps) * hole, -Math.sin(ps) * hole };

            render.startDrawing(7);
            render.addVertex(x + inner[0], y + inner[1], 0);
            render.addVertex(x + inner[2], y + inner[3], 0);
            render.addVertex(x + outer[2], y + outer[3], 0);
            render.addVertex(x + outer[0], y + outer[1], 0);
            tess.draw();
        }

        postRender();
    }

    public static void donutSeg(double x, double y, double radius, double hole, double part, int maxParts, double padding, int color) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        part = (part+maxParts)%maxParts;

        for(double i = (360D/maxParts)*(part+padding); i < ((360D/maxParts)*(part+1-padding))-0.5; i+= 1) {
            double cs = (-i)*Math.PI/180;
            double ps = (-i-1)*Math.PI/180;
            double[] outer = new double[] { Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius, -Math.sin(ps) * radius };
            double[] inner = new double[] { Math.cos(cs) * hole, -Math.sin(cs) * hole, Math.cos(ps) * hole, -Math.sin(ps) * hole };

            render.startDrawing(7);
            render.addVertex(x + inner[0], y + inner[1], 0);
            render.addVertex(x + inner[2], y + inner[3], 0);
            render.addVertex(x + outer[2], y + outer[3], 0);
            render.addVertex(x + outer[0], y + outer[1], 0);
            tess.draw();
        }

        postRender();
    }

    public static void donutSegFrac(double x, double y, double radius, double hole, double part, int maxParts, double frac, double padding, int color) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        part = (part+maxParts)%maxParts;

        for(double i = (360D/maxParts)*part; i < ((360D/maxParts)*(part+frac))-0.5; i++) {
            double cs = (-i)*Math.PI/180D;
            double ps = (-i-1)*Math.PI/180D;
            double[] outer = new double[] { Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius, -Math.sin(ps) * radius };
            double[] inner = new double[] { Math.cos(cs) * hole, -Math.sin(cs) * hole, Math.cos(ps) * hole, -Math.sin(ps) * hole };

            render.startDrawing(7);
            render.addVertex(x + inner[0], y + inner[1], 0);
            render.addVertex(x + inner[2], y + inner[3], 0);
            render.addVertex(x + outer[2], y + outer[3], 0);
            render.addVertex(x + outer[0], y + outer[1], 0);
            tess.draw();
        }

        postRender();
    }

    public static void string(MinecraftFontRenderer font, String text, double x, double y, int color, int xAlign, int yAlign, boolean shadow) {
        for(String key : ColorUtil.colors.keySet()) {
            ChatColor c = ColorUtil.colors.get(key);
            text = text.replace(c.regex, "&"+c.color);
        }

        text = ChatUtil.addFormat(text, "&");
        x -= font.getStringWidth(text) / 2;
        x += (font.getStringWidth(text) / 2) * (xAlign);

        y -= font.getHeight() / 2;
        y += (font.getHeight() / 2) * (yAlign);
        GlStateManager.alphaFunc(516, 0.001F);
        if ((color >> 24 & 255) / 255f > 0.008) {
            if (shadow) {
                font.drawStringWithShadow(text, (int) x, (int) y, color);
            } else {
                font.drawString(text, (int) x, (int) y, color);
            }
        }
        GlStateManager.alphaFunc(516, 0.1F);
    }
}
