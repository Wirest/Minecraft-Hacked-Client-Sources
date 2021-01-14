package store.shadowclient.client.utils.render;

import java.awt.Color;
import java.awt.Rectangle;

import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RectangleUtils {
    public static void drawBorderedRect(float x2, float y2, float x1, float y1, float width, int internalColor, int borderColor) {
        enableGL2D();
        glColor(internalColor);
        drawRect(x2 + width, y2 + width, x1 - width, y1 - width);
        glColor(borderColor);
        drawRect(x2 + width, y2, x1 - width, y2 + width);
        drawRect(x2, y2, x2 + width, y1);
        drawRect(x1 - width, y2, x1, y1);
        drawRect(x2 + width, y1 - width, x1 - width, y1);
        disableGL2D();
    }

    public static void drawRect(double left, double top, double right, double bottom, Color color)
    {
        double var5;

        if (left < right)
        {
            var5 = left;
            left = right;
            right = var5;
        }

        if (top < bottom)
        {
            var5 = top;
            top = bottom;
            bottom = var5;
        }

        float f3 = (float)(color.getRGB() >> 24 & 255) / 255.0F;
        float f = (float)(color.getRGB() >> 16 & 255) / 255.0F;
        float f1 = (float)(color.getRGB() >> 8 & 255) / 255.0F;
        float f2 = (float)(color.getRGB() & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
    private static ScaledResolution scaledResolution;
    private static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    private static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    private static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawRect(float x2, float y2, float x1, float y1, float r2, float g2, float b2, float a2) {
        enableGL2D();
        GL11.glColor4f(r2, g2, b2, a2);
        drawRect(x2, y2, x1, y1);
        disableGL2D();
    }

    public static void drawRect(float x2, float y2, float x1, float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }
}
