package cedo.ui.elements;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class drawTriangle {
    public static void drawTri(double x1, double y1, double x2, double y2,
                               double x3, double y3, double width, Color c) {
        GlStateManager.pushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4d(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
        GL11.glLineWidth((float) width);
        GL11.glBegin(3);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x3, y3);
        GL11.glEnd();
        GlStateManager.popMatrix();
    }
}
