package com.ihl.client.util;

import com.ihl.client.font.MinecraftFontRenderer;
import com.ihl.client.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.Display;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

    public static MinecraftFontRenderer[] fontTiny = new MinecraftFontRenderer[]{
            new MinecraftFontRenderer(new Font("Calibri", Font.PLAIN, 10), true, true),
            new MinecraftFontRenderer(new Font("Calibri", Font.BOLD, 10), true, true)
    };

    public static MinecraftFontRenderer[] fontSmall = new MinecraftFontRenderer[]{
            new MinecraftFontRenderer(new Font("Verdana", Font.PLAIN, 12), true, true),
            new MinecraftFontRenderer(new Font("Verdana", Font.BOLD, 12), true, true)
    };

    public static MinecraftFontRenderer[] fontMedium = new MinecraftFontRenderer[]{
            new MinecraftFontRenderer(new Font("Verdana", Font.PLAIN, 18), true, true),
            new MinecraftFontRenderer(new Font("Verdana", Font.BOLD, 18), true, true)
    };

    public static MinecraftFontRenderer[] fontLarge = new MinecraftFontRenderer[]{
            new MinecraftFontRenderer(new Font("Verdana", Font.PLAIN, 24), true, true),
            new MinecraftFontRenderer(new Font("Verdana", Font.BOLD, 24), true, true)
    };

    public static void startClip(double x1, double y1, double x2, double y2) {
        double temp;
        if (y1 > y2) {
            temp = y2;
            y2 = y1;
            y1 = temp;
        }

        glEnable(GL_SCISSOR_TEST);
        glScissor((int) x1, (int) (Display.getHeight() - y2), (int) (x2 - x1), (int) (y2 - y1));
    }

    public static void endClip() {
        glDisable(GL_SCISSOR_TEST);
    }

    public static void startStencil() {
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);

        glClear(GL_STENCIL_BUFFER_BIT);
    }

    public static void stencilClear() {
        glStencilFunc(GL_NEVER, 1, 0xFF);
        glStencilMask(0xFF);

        glColorMask(false, false, false, false);
        glDepthMask(false);
    }

    public static void stencilWrite() {
        glColorMask(true, true, true, true);
        glDepthMask(true);

        glStencilFunc(GL_NOTEQUAL, 1, 0xFF);
        glStencilMask(0x00);
    }

    public static void endStencil() {
        glStencilFunc(GL_ALWAYS, 1, 0xFF);
        glStencilMask(0xFF);

        glEnable(GL_DEPTH_TEST);
    }

    public static void preRender(float[] rgba) {
        GlStateManager.alphaFunc(516, 0.001F);
        GlStateManager.color(rgba[0], rgba[1], rgba[2], rgba[3]);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public static void postRender() {
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        glLineWidth(1f);
    }

    public static boolean renderEntities() {
        return Module.get("esp").active || Module.get("freecam").active;
    }

}
